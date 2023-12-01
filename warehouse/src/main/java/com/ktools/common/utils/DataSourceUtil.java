package com.ktools.common.utils;

import com.ktools.manager.datasource.DataSourceProperties;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * @author WCG
 */
public class DataSourceUtil {

    public static DataSource createDataSource(DataSourceProperties dataSourceProperties) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(dataSourceProperties.getJdbcDriver());
        hikariDataSource.setJdbcUrl(dataSourceProperties.getJdbcUrl());
        hikariDataSource.setUsername(dataSourceProperties.getJdbcUserName());
        hikariDataSource.setPassword(dataSourceProperties.getJdbcPassword());
        hikariDataSource.setMinimumIdle(1);
        hikariDataSource.setMaximumPoolSize(10);
        hikariDataSource.setMaxLifetime(1800000);
        hikariDataSource.setKeepaliveTime(30000);
        hikariDataSource.setConnectionTestQuery("SELECT 1");
        hikariDataSource.setIdleTimeout(600000);
        return hikariDataSource;
    }

}
