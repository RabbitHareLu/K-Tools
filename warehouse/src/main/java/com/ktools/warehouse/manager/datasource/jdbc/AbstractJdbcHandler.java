package com.ktools.warehouse.manager.datasource.jdbc;


import com.ktools.warehouse.KToolsContext;
import com.ktools.warehouse.common.utils.ConfigParamUtil;
import com.ktools.warehouse.common.utils.DataSourceUtil;
import com.ktools.warehouse.common.utils.StreamUtil;
import com.ktools.warehouse.common.utils.StringUtil;
import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.manager.datasource.KDataSourceHandler;
import com.ktools.warehouse.manager.datasource.jdbc.model.TableColumn;
import com.ktools.warehouse.manager.datasource.jdbc.model.TableMetadata;
import com.ktools.warehouse.manager.datasource.jdbc.query.QueryCondition;
import com.ktools.warehouse.mybatis.MybatisContext;
import com.ktools.warehouse.task.element.BaseColumn;
import com.ktools.warehouse.task.element.BaseRow;
import com.ktools.warehouse.task.element.DataType;
import com.ktools.warehouse.task.model.JobSinkConfig;
import com.ktools.warehouse.task.model.JobSourceConfig;
import com.mybatisflex.core.datasource.DataSourceKey;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.DbChain;
import com.mybatisflex.core.row.Row;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * JDBC 处理器
 *
 * @author WCG
 */
@Slf4j
public abstract class AbstractJdbcHandler implements KDataSourceHandler {

    protected final Properties properties;

    protected final JdbcConfig jdbcConfig;

    protected AbstractJdbcHandler(Properties properties) throws KToolException {
        this.properties = properties;
        this.jdbcConfig = ConfigParamUtil.buildConfig(properties, JdbcConfig.class);
        this.jdbcConfig.setKey(UUID.randomUUID().toString());
        this.jdbcConfig.setDriver(getDriverClass());
    }

    @Override
    public void testConn() throws KToolException {
        try (Connection ignored = DriverManager.getConnection(jdbcConfig.getJdbcUrl(), jdbcConfig.getUsername(), jdbcConfig.getPassword())) {
            log.info("数据源连接测试成功！");
        } catch (SQLException e) {
            throw new KToolException("测试连接失败", e);
        }
    }

    @Override
    public void conn() {
        MybatisContext mybatisContext = KToolsContext.getInstance().getMybatisContext();
        if (!mybatisContext.existDataSource(jdbcConfig.getKey())) {
            synchronized (this) {
                if (!mybatisContext.existDataSource(jdbcConfig.getKey())) {
                    DataSource dataSource = DataSourceUtil.createDataSource(jdbcConfig);
                    mybatisContext.addDataSource(jdbcConfig.getKey(), dataSource);
                }
            }
        }
    }

    @Override
    public void disConn() {
        MybatisContext mybatisContext = KToolsContext.getInstance().getMybatisContext();
        if (mybatisContext.existDataSource(jdbcConfig.getKey())) {
            synchronized (this) {
                if (mybatisContext.existDataSource(jdbcConfig.getKey())) {
                    mybatisContext.removeDataSource(jdbcConfig.getKey());
                }
            }
        }
    }

    @Override
    public List<String> selectAllSchema() throws KToolException {
        try (Connection connection = getDataSource().getConnection();
             ResultSet schemas = connection.getMetaData().getSchemas()
        ) {
            return StreamUtil.buildStream(schemas)
                    .map(map -> String.valueOf(map.get("TABLE_SCHEM")))
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> selectAllTable(String schema) throws KToolException {
        try (Connection connection = getDataSource().getConnection();
             ResultSet tables = connection.getMetaData().getTables(null, schema, "%", new String[]{"TABLE"})
        ) {
            return StreamUtil.buildStream(tables)
                    .map(map -> String.valueOf(map.get("TABLE_NAME")))
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TableMetadata selectTableMetadata(String schema, String tableName) throws KToolException {
        TableMetadata metadata = new TableMetadata();
        metadata.setSchema(schema);
        metadata.setTableName(tableName);

        try (Connection connection = getDataSource().getConnection();
             ResultSet primaryKeys = connection.getMetaData().getPrimaryKeys(null, schema, tableName);
             ResultSet columns = connection.getMetaData().getColumns(null, schema, tableName, "%")
        ) {
            // 获取主键信息的结果集
            List<String> primaryKeyList = StreamUtil.buildStream(primaryKeys)
                    .map(map -> String.valueOf(map.get("COLUMN_NAME")))
                    .toList();
            // 处理字段
            Map<String, TableColumn> columnMap = StreamUtil.buildStream(columns).map(column -> {
                TableColumn columnTemp = new TableColumn();
                columnTemp.setName(String.valueOf(column.get("COLUMN_NAME")));
                columnTemp.setDataType(getSqlTypeByJdbcType(String.valueOf(column.get("TYPE_NAME"))));
                columnTemp.setPrimaryKey(primaryKeyList.contains(columnTemp.getName()));
                columnTemp.setNullable(ResultSetMetaData.columnNullable == Integer.parseInt(String.valueOf(column.get("NULLABLE"))));
                columnTemp.setLength(Integer.parseInt(String.valueOf(column.get("COLUMN_SIZE"))));
                columnTemp.setPrecision(Integer.parseInt(String.valueOf(column.get("DECIMAL_DIGITS"))));
                return columnTemp;
            }).collect(Collectors.toMap(TableColumn::getName, Function.identity(), (tableColumn, tableColumn2) -> {
                throw new RuntimeException("列名冲突！");
            }, LinkedHashMap::new));

            // 赋值给元数据
            metadata.setColumns(columnMap);
            // 返回结果
            return metadata;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Row> selectData(String schema, String tableName, QueryCondition queryCondition) throws KToolException {
        // 查询表元数据
        TableMetadata tableMetadata = selectTableMetadata(schema, tableName);
        List<QueryColumn> fields = tableMetadata.getColumns().keySet().stream().map(QueryColumn::new).toList();
        return DataSourceKey.use(jdbcConfig.getKey(), () -> {
            DbChain dbChain = DbChain.table(tableMetadata.getSchema(), tableMetadata.getTableName()).select(fields);
            if (StringUtil.isNotBlank(queryCondition.getWhereCondition())) {
                dbChain = dbChain.where(queryCondition.getWhereCondition());
            }
            dbChain = dbChain.orderBy(fields.getFirst(), true);

            return dbChain.page(queryCondition.getPage(Row.class));
        });
    }

    @Override
    public void source(JobSourceConfig sourceConfig, Consumer<Stream<BaseRow>> consumer) throws KToolException {
        String finalSql;
        if (StringUtil.isBlank(sourceConfig.getViewSql())) {
            // 视图sql为空，查询表数据
            finalSql = String.format("select * from %s.%s", sourceConfig.getSourceSchema(), sourceConfig.getSourceTableName());
        } else {
            // 视图sql不为空，根据sql查询数据
            finalSql = sourceConfig.getViewSql();
        }

        try (Connection connection = getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(finalSql)
        ) {
            // 构建流
            Stream<Map<String, Object>> mapStream = StreamUtil.buildStream(resultSet);
            // 转换数据
            Stream<BaseRow> rowStream = mapStream.map(map -> {
                BaseRow baseRow = new BaseRow(map.size());
                map.forEach((k, v) -> {
                    BaseColumn baseColumn = BaseColumn.create(k, String.valueOf(v), DataType.STRING);
                    baseRow.addField(baseColumn);
                });
                return baseRow;
            });
            // 回调
            consumer.accept(rowStream);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sink(JobSinkConfig sinkConfig, Stream<BaseRow> stream) throws KToolException {
        TableMetadata tableMetadata = selectTableMetadata(sinkConfig.getSinkSchema(), sinkConfig.getSinkTableName());
        List<String> primaryKey = tableMetadata.getPrimaryKey();

        List<BaseRow> baseRows = new ArrayList<>();
        stream.filter(baseRow -> {
            // 检查主键数据完整性
            boolean whole = true;
            for (String key : primaryKey) {
                BaseColumn field = baseRow.getField(key);
                if (field == null || field.getData() == null) {
                    whole = false;
                }
            }
            return whole;
        }).forEach(row -> {
            // 开始落库
            baseRows.add(row);
            // 判断缓存大小
            if (baseRows.size() >= 1000) {
                saveData(tableMetadata, new ArrayList<>(baseRows));
                baseRows.clear();
            }
        });
        saveData(tableMetadata, new ArrayList<>(baseRows));
        baseRows.clear();
    }

    private void saveData(TableMetadata tableMetadata, List<BaseRow> baseRows) {
        List<Row> rowList = baseRows.stream().map(baseRow -> {
            // 转换为mybatis认识的row
            Row row = new Row();
            tableMetadata.getColumns().values().forEach(tableColumn -> {
                BaseColumn field = baseRow.getField(tableColumn.getName());
                Object value = typeConversion(tableColumn.getDataType()).convertData(field.getData());
                row.set(tableColumn.getName(), value);
            });
            return row;
        }).toList();
        DataSourceKey.use(jdbcConfig.getKey(), () -> {
            Db.insertBatchWithFirstRowColumns(tableMetadata.getSchema(), tableMetadata.getTableName(), rowList);
        });
    }

    protected DataSource getDataSource() throws KToolException {
        MybatisContext mybatisContext = KToolsContext.getInstance().getMybatisContext();
        return mybatisContext.getDataSource(jdbcConfig.getKey());
    }

    protected abstract String getDriverClass();

    protected abstract SQLType getSqlTypeByJdbcType(String typeName);

    protected abstract DataType typeConversion(SQLType sqlType);

}
