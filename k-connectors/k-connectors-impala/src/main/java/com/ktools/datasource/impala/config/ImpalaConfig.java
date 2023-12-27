package com.ktools.datasource.impala.config;

import com.ktools.warehouse.manager.datasource.ConfigParam;
import com.ktools.warehouse.manager.datasource.jdbc.JdbcConfig;
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
     * kudu 地址
     */
    @ConfigParam(name = "kudu 地址", key = "impala.kuduMaster")
    private String kuduMaster;

}
