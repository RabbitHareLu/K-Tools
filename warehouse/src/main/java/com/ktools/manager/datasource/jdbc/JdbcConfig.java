package com.ktools.manager.datasource.jdbc;

import lombok.Data;

/**
 * @author WCG
 */
@Data
public class JdbcConfig {

    /**
     * key
     */
    private String key;

    /**
     * driver
     */
    private String driver;

    /**
     * url
     */
    @ConfigParam(name = "url", key = "jdbcUrl", must = true)
    private String jdbcUrl;

    /**
     * 用户名
     */
    @ConfigParam(name = "用户名", key = "username")
    private String username;

    /**
     * 密码
     */
    @ConfigParam(name = "密码", key = "password")
    private String password;

}
