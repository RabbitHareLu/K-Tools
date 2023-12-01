package com.ktools.service;

import com.ktools.KToolsContext;
import com.ktools.mybatis.MybatisContext;

/**
 * @author WCG
 */
public abstract class BaseService {

    protected final KToolsContext kToolsContext;

    protected final MybatisContext mybatisContext;

    public BaseService() {
        this.kToolsContext = KToolsContext.getInstance();
        this.mybatisContext = this.kToolsContext.getMybatisContext();
    }

    protected <T> T getMapper(Class<T> tClass) {
        return this.mybatisContext.getMapper(tClass);
    }

}
