package com.ktools.manager.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年11月29日 10:33
 */
@Slf4j
public class DataSourceManager implements AutoCloseable {

    private static final DataSourceManager INSTANCE = new DataSourceManager();

    public static DataSourceManager getInstance() {
        return INSTANCE;
    }

    private final Map<String, HikariDataSource> dataSourceMap = new ConcurrentHashMap<>();

    private DataSourceManager() {
    }

    @Override
    public void close() {
        dataSourceMap.forEach((s, hikariDataSource) -> hikariDataSource.close());
    }

    public DataSource getSystemDataSource() {
        return dataSourceMap.get(SysDataSource.DATASOURCE_NAME);
    }

    private void initDataSource(DataSourceProperties dataSourceProperties) {
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

        dataSourceMap.put(dataSourceProperties.getName(), hikariDataSource);
    }

    public DataSource initAndGetDataSource(DataSourceProperties dataSourceProperties) {
        if (!dataSourceMap.containsKey(dataSourceProperties.getName())) {
            synchronized (DataSourceManager.class) {
                if (!dataSourceMap.containsKey(dataSourceProperties.getName())) {
                    initDataSource(dataSourceProperties);
                }
            }
        }
        return dataSourceMap.get(dataSourceProperties.getName());
    }
}
