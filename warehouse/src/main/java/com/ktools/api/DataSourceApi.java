package com.ktools.api;

import com.ktools.exception.KToolException;
import com.ktools.manager.datasource.jdbc.model.TableMetadata;
import com.ktools.manager.datasource.jdbc.query.CommonPage;
import com.ktools.manager.datasource.jdbc.query.QueryCondition;
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

    /**
     * 查询所有schema
     */
    List<String> selectAllSchema(String id) throws KToolException;

    /**
     * 查询所有表名
     */
    List<String> selectAllTable(String id, String schema) throws KToolException;

    /**
     * 查询表元数据
     */
    TableMetadata selectTableMetadata(String id, String schema, String tableName) throws KToolException;

    /**
     * 查询数据
     */
    CommonPage<Map<String, Object>> selectData(String id, String schema, String tableName, QueryCondition queryCondition) throws KToolException;

}
