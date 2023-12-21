package com.ktools.manager.datasource.jdbc;


import com.ktools.KToolsContext;
import com.ktools.common.utils.ConfigParamUtil;
import com.ktools.common.utils.DataSourceUtil;
import com.ktools.common.utils.StreamUtil;
import com.ktools.common.utils.StringUtil;
import com.ktools.exception.KToolException;
import com.ktools.manager.datasource.KDataSourceHandler;
import com.ktools.manager.datasource.jdbc.model.TableColumn;
import com.ktools.manager.datasource.jdbc.model.TableMetadata;
import com.ktools.manager.datasource.jdbc.query.PageQuery;
import com.ktools.manager.datasource.jdbc.query.QueryCondition;
import com.ktools.mybatis.MybatisContext;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        try (Connection connection = getDataSource().getConnection()) {
            ResultSet schemas = connection.getMetaData().getSchemas();
            return StreamUtil.buildStream(schemas)
                    .map(map -> String.valueOf(map.get("TABLE_SCHEM")))
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> selectAllTable(String schema) throws KToolException {
        try (Connection connection = getDataSource().getConnection()) {
            ResultSet tables = connection.getMetaData().getTables(null, schema, "%", new String[]{"TABLE"});
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

        try (Connection connection = getDataSource().getConnection()) {
            // 获取主键信息的结果集
            ResultSet primaryKeys = connection.getMetaData().getPrimaryKeys(null, schema, tableName);
            List<String> primaryKeyList = StreamUtil.buildStream(primaryKeys)
                    .map(map -> String.valueOf(map.get("COLUMN_NAME")))
                    .toList();
            // 查询
            ResultSet columns = connection.getMetaData().getColumns(null, schema, tableName, "%");
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
    public List<Map<String, Object>> selectData(String schema, String tableName, QueryCondition queryCondition) throws KToolException {
        // 查询表元数据
        TableMetadata tableMetadata = selectTableMetadata(schema, tableName);
        // 构建查询sql
        String querySql = buildQuerySql(tableMetadata, queryCondition.getWhereCondition());
        // 构建分页查询sql
        String pageSql = buildPageSql(querySql, queryCondition);
        // 执行sql
        return executeSql(pageSql);
    }

    protected String buildQuerySql(TableMetadata tableMetadata, String whereCondition) {
        StringBuilder sql = new StringBuilder("select ");
        StringJoiner selectField = new StringJoiner(",");
        tableMetadata.getColumns().keySet().forEach(key -> selectField.add(processKeywords(key)));
        sql.append(" ").append(selectField).append(" ");
        sql.append(" from ")
                .append(processKeywords(tableMetadata.getSchema()))
                .append(".")
                .append(processKeywords(tableMetadata.getTableName()));
        sql.append(" ");
        if (StringUtil.isNotBlank(whereCondition)) {
            sql.append(" where ").append(whereCondition);
        }
        return sql.toString();
    }

    protected DataSource getDataSource() throws KToolException {
        MybatisContext mybatisContext = KToolsContext.getInstance().getMybatisContext();
        return mybatisContext.getDataSource(jdbcConfig.getKey());
    }

    protected List<Map<String, Object>> executeSql(String sql, Object... params) throws KToolException {
        // 解析sql操作类型
        OperationType operationType = parseSql(sql);

        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            // 填充参数
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            // 根据不同的操作类型执行sql，并封装执行结果
            switch (operationType) {
                case QUERY -> {
                    // 执行sql
                    ResultSet query = preparedStatement.executeQuery();
                    // 获取返回结果
                    return StreamUtil.buildStream(query).toList();
                }
                case UPDATE,DELETE -> {
                    int result = preparedStatement.executeUpdate();
                    return Collections.singletonList(Map.of(JdbcConstant.SQL_EXEC_RESULT, result));
                }
                default -> {
                    boolean result = preparedStatement.execute();
                    return Collections.singletonList(Map.of(JdbcConstant.SQL_EXEC_RESULT, result));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract String getDriverClass();

    protected abstract String processKeywords(String keyword);

    protected abstract SQLType getSqlTypeByJdbcType(String typeName);

    protected abstract OperationType parseSql(String sql);

    protected abstract String buildPageSql(String querySql, PageQuery pageQuery);

}
