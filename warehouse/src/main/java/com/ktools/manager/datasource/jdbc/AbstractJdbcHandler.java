package com.ktools.manager.datasource.jdbc;


import com.ktools.KToolsContext;
import com.ktools.common.utils.ConfigParamUtil;
import com.ktools.common.utils.DataSourceUtil;
import com.ktools.exception.KToolException;
import com.ktools.manager.datasource.KDataSourceHandler;
import com.ktools.mybatis.MybatisContext;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

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

    protected abstract String getDriverClass();

}
