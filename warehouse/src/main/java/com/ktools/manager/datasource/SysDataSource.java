package com.ktools.manager.datasource;

import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

/**
 * @author WCG
 */
public class SysDataSource {

    public static final String DATASOURCE_NAME = "system";
    private static final String H2_DRIVER = "org.h2.Driver";
    private static final String H2_URL = "jdbc:h2:file:./db/KT";
    private static final String H2_USER_NAME = "system";
    private static final String H2_PASSWORD = "123456";


    public static void init() {
        DataSourceManager dataSourceManager = DataSourceManager.getInstance();
        DataSource dataSource = dataSourceManager.initAndGetDataSource(getDataSourceProperties());

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .table("flyway_schema_history")
                .outOfOrder(false)
                .validateOnMigrate(true)
                .load();

        flyway.migrate();
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
