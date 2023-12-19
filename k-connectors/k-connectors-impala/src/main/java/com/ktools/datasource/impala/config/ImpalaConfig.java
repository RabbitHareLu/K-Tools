package com.ktools.datasource.impala.config;

import com.ktools.manager.datasource.jdbc.ConfigParam;
import com.ktools.manager.datasource.jdbc.JdbcConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * impala数据源配置文件
 *
 * @author WCG
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ImpalaConfig extends JdbcConfig {

    /**
     * 存储类型
     */
    @ConfigParam(name = "存储类型", key = "impala.storageType", defaultValue = "kudu")
    private String storageType;

    /**
     * kudu 地址
     */
    @ConfigParam(name = "kudu 地址", key = "impala.kuduMaster")
    private String kuduMaster;

}
