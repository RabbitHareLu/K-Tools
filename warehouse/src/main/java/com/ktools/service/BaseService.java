package com.ktools.service;

import com.ktools.KToolsContext;
import com.ktools.mybatis.MybatisContext;

import javax.sql.DataSource;

/**
 * @author WCG
 */
public abstract class BaseService {

    private final KToolsContext kToolsContext;

    private final MybatisContext mybatisContext;

    public BaseService() {
        this.kToolsContext = KToolsContext.getInstance();
        this.mybatisContext = getkToolsContext().getMybatisContext();
    }

    protected KToolsContext getkToolsContext() {
        return this.kToolsContext;
    }

    protected MybatisContext getMybatisContext() {
        return this.mybatisContext;
    }

}
