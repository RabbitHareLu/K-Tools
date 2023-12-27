package com.ktools.datasource.impala.handler;

import com.ktools.warehouse.common.utils.ConfigParamUtil;
import com.ktools.datasource.impala.config.ImpalaConfig;
import com.ktools.datasource.impala.type.ImpalaType;
import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.manager.datasource.jdbc.AbstractJdbcHandler;

import java.sql.SQLType;
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

    @Override
    protected String getDriverClass() {
        return "com.cloudera.impala.jdbc41.Driver";
    }

    @Override
    protected SQLType getSqlTypeByJdbcType(String typeName) {
        return ImpalaType.valueOf(typeName);
    }

}
