package com.ktools.datasource.impala.handler;

import com.ktools.datasource.impala.config.ImpalaConfig;
import com.ktools.datasource.impala.type.ImpalaType;
import com.ktools.warehouse.common.utils.ConfigParamUtil;
import com.ktools.warehouse.common.utils.StringUtil;
import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.manager.datasource.jdbc.AbstractJdbcHandler;
import com.ktools.warehouse.task.element.BaseColumn;
import com.ktools.warehouse.task.element.BaseRow;
import com.ktools.warehouse.task.element.DataType;
import com.ktools.warehouse.task.model.JobSinkConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.client.*;

import java.sql.SQLType;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * @author WCG
 */
@Slf4j
public class ImpalaHandler extends AbstractJdbcHandler {

    private final ImpalaConfig impalaConfig;

    public ImpalaHandler(Properties properties) throws KToolException {
        super(properties);
        this.impalaConfig = ConfigParamUtil.buildConfig(properties, ImpalaConfig.class);
    }

    @Override
    protected String getDriverClass() {
        return "com.cloudera.impala.jdbc41.Driver";
    }

    @Override
    protected SQLType getSqlTypeByJdbcType(String typeName) {
        return ImpalaType.valueOf(typeName);
    }

    @Override
    protected DataType typeConversion(SQLType sqlType) {
        if (sqlType instanceof ImpalaType impalaType) {
            return switch (impalaType) {
                case INT -> DataType.INT;
                case CHAR, STRING, VARCHAR -> DataType.STRING;
                case REAL -> DataType.UNKNOWN_DATA;
                case FLOAT -> DataType.FLOAT;
                case BIGINT -> DataType.LONG;
                case DOUBLE -> DataType.DOUBLE;
                case BOOLEAN -> DataType.BOOLEAN;
                case DECIMAL -> DataType.DECIMAL;
                case TINYINT, SMALLINT -> DataType.SHORT;
                case TIMESTAMP -> DataType.TIMESTAMP;
            };
        }
        return DataType.UNKNOWN_DATA;
    }

    @Override
    public void sink(JobSinkConfig sinkConfig, Stream<BaseRow> stream) throws KToolException {
        if (StringUtil.isBlank(impalaConfig.getKuduMaster())) {
            // 没有配置kudu地址，走jdbc
            super.sink(sinkConfig, stream);
        } else {
            final int batchSize = 10000;
            // 配置了kudu地址，走kudu client
            try (KuduClient kuduClient = new KuduClient.KuduClientBuilder(impalaConfig.getKuduMaster()).build()) {
                KuduSession kuduSession = kuduClient.newSession();
                kuduSession.setFlushMode(SessionConfiguration.FlushMode.MANUAL_FLUSH);
                kuduSession.setMutationBufferSpace(batchSize);
                // 获取kudu表名
                String configTableName = sinkConfig.getSinkSchema() + "." + sinkConfig.getSinkTableName();
                String kuduTableName = kuduClient.getTablesList().getTablesList().stream()
                        .filter(name -> name.equalsIgnoreCase(configTableName))
                        .findFirst()
                        .orElseThrow(() -> new KToolException("未匹配到目标表名！"));

                // 查询kudu表列信息
                Map<String, DataType> fieldMap = getKuduMetadata(kuduClient, kuduTableName);

                KuduTable table = kuduClient.openTable(kuduTableName);
                AtomicInteger size = new AtomicInteger();
                stream.forEach(baseRow -> {
                    Upsert upsert = table.newUpsert();
                    PartialRow row = upsert.getRow();
                    fieldMap.forEach((k, type) -> {
                        BaseColumn field = baseRow.getField(k);
                        Optional.ofNullable(field).ifPresent(baseColumn -> row.addObject(k, type.convertData(baseColumn.getData())));
                    });
                    try {
                        kuduSession.apply(upsert);
                        if (size.incrementAndGet() >= batchSize) {
                            kuduSession.flush();
                            size.set(0);
                        }
                    } catch (KuduException e) {
                        throw new RuntimeException(e);
                    }
                });
                kuduSession.flush();
            } catch (KuduException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 查询kudu列类型信息
     */
    private static Map<String, DataType> getKuduMetadata(KuduClient kuduClient, String tableName) throws KToolException {
        try {
            KuduTable table = kuduClient.openTable(tableName);
            LinkedHashMap<String, DataType> fieldMap = new LinkedHashMap<>();
            for (ColumnSchema column : table.getSchema().getColumns()) {
                DataType dataType = switch (column.getType()) {
                    case INT8 -> DataType.BYTE;
                    case INT16 -> DataType.SHORT;
                    case INT32 -> DataType.INT;
                    case INT64 -> DataType.LONG;
                    case BINARY -> DataType.BINARY;
                    case STRING, VARCHAR -> DataType.STRING;
                    case BOOL -> DataType.BOOLEAN;
                    case FLOAT -> DataType.FLOAT;
                    case DOUBLE -> DataType.DOUBLE;
                    case DECIMAL -> DataType.DECIMAL;
                    case DATE -> DataType.DATE;
                    case UNIXTIME_MICROS -> DataType.TIMESTAMP;
                };
                fieldMap.put(column.getName(), dataType);
            }
            return fieldMap;
        } catch (KuduException e) {
            String msg = String.format("获取表(%s)元数据异常", tableName);
            log.error(msg, e);
            throw new KToolException(msg);
        }
    }

}
