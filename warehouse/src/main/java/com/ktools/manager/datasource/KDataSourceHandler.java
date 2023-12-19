package com.ktools.manager.datasource;

import com.ktools.exception.KToolException;

/**
 * 数据源接口
 *
 * @author WCG
 */
public interface KDataSourceHandler {

    /**
     * 测试连接
     */
    void testConn() throws KToolException;

}
