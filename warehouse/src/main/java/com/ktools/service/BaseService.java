package com.ktools.service;

import com.ktools.KToolsContext;
import com.ktools.mybatis.MybatisContext;
import com.ktools.mybatis.mapper.PropMapper;
import com.ktools.mybatis.mapper.TreeMapper;

/**
 * @author WCG
 */
public abstract class BaseService {

    protected final KToolsContext kToolsContext;

    protected final MybatisContext mybatisContext;

    protected final TreeMapper treeMapper;

    protected final PropMapper propMapper;

    public BaseService() {
        this.kToolsContext = KToolsContext.getInstance();
        this.mybatisContext = this.kToolsContext.getMybatisContext();
        this.treeMapper = this.mybatisContext.getTreeMapper();
        this.propMapper = this.mybatisContext.getPropMapper();
    }

}
