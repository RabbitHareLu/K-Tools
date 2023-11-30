package com.ktools.service;

import com.ktools.KToolsContext;

import javax.sql.DataSource;

/**
 * @author WCG
 */
public abstract class BaseService {

    private final KToolsContext kToolsContext;

    private final DataSource sysDataSource;

    public BaseService() {
        this.kToolsContext = KToolsContext.getInstance();
        this.sysDataSource = this.kToolsContext.getDataSourceManager().getSystemDataSource();
    }

    protected KToolsContext getkToolsContext() {
        return this.kToolsContext;
    }

    protected DataSource getSysDataSource() {
        return this.sysDataSource;
    }

}
