package com.ktools.datasource.impala.handler;

import com.ktools.common.utils.ConfigParamUtil;
import com.ktools.datasource.impala.config.ImpalaConfig;
import com.ktools.exception.KToolException;
import com.ktools.manager.datasource.jdbc.AbstractJdbcHandler;

import java.util.Properties;

/**
 *
 *
 * @author WCG
 */
public class ImpalaHandler extends AbstractJdbcHandler {

    private final ImpalaConfig impalaConfig;

    public ImpalaHandler(Properties properties) throws KToolException {
        super(properties);
        this.impalaConfig = ConfigParamUtil.buildConfig(properties, ImpalaConfig.class);
    }

}
