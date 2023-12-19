package com.ktools.api;

import com.ktools.exception.KToolException;
import com.ktools.manager.datasource.model.KDataSourceMetadata;

import java.util.List;
import java.util.Map;

/**
 * @author WCG
 */
public interface DataSourceApi {

    /**
     * 查询所有支持的数据源
     */
    List<KDataSourceMetadata> getAllMetadata();

    /**
     * 查询指定类型的数据源元数据
     */
    KDataSourceMetadata getMetadata(String name) throws KToolException;

    /**
     * 测试数据源
     */
    void testDataSource(String type, Map<String, String> properties) throws KToolException;

    /**
     * 连接数据源
     *
     * @param id         当前节点id
     * @param type       数据源类型
     * @param properties 属性
     */
    void conn(String id, String type, Map<String, String> properties) throws KToolException;

    /**
     * 数据源连接断开
     *
     * @param id 当前节点id
     */
    void disConn(String id);

}
