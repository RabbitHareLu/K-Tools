package com.ktools.manager.datasource;

import lombok.Data;

/**
 * 数据源配置类
 *
 * @author WCG
 */
@Data
public class DataSourceProperties {

    private String name;

    private String jdbcDriver;

    private String jdbcUrl;

    private String jdbcUserName;

    private String jdbcPassword;

}
