package com.ktools.manager.datasource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author WCG
 */
public class SysDataSource {

    public static final String DATASOURCE_NAME = "system";
    private static final String H2_DRIVER = "org.h2.Driver";
    private static final String H2_URL = "jdbc:h2:file:./db/KT";
    private static final String H2_USER_NAME = "system";
    private static final String H2_PASSWORD = "123456";

    private static final String DROP_TREE_TABLE_SQL = "drop table if exists tree";

    private static final String CREATE_TREE_TABLE_SQL = """
        create table tree (
            id             varchar(36) primary key,
            node_name      varchar(100),
            node_type      varchar(20),
            node_comment   varchar(255),
            parent_node_id varchar(36)
        )
        """;
    private static final String INSERT_TREE_DATA_SQL = """
        insert into tree (id, node_name, node_type, node_comment, parent_node_id) values ('ROOT', 'ROOT', 'ROOT', null, null)
        """;

    public static void init() {
        DataSourceManager dataSourceManager = DataSourceManager.getInstance();
        DataSource dataSource = dataSourceManager.initAndGetDataSource(getDataSourceProperties());
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()
        ) {
            statement.execute(DROP_TREE_TABLE_SQL);
            statement.execute(CREATE_TREE_TABLE_SQL);
            statement.execute(INSERT_TREE_DATA_SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static DataSourceProperties getDataSourceProperties() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setName(DATASOURCE_NAME);
        dataSourceProperties.setJdbcDriver(H2_DRIVER);
        dataSourceProperties.setJdbcUrl(H2_URL);
        dataSourceProperties.setJdbcUserName(H2_USER_NAME);
        dataSourceProperties.setJdbcPassword(H2_PASSWORD);
        return dataSourceProperties;
    }

}
