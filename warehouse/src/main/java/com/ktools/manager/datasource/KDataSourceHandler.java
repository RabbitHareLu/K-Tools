package com.ktools.manager.datasource;

import com.ktools.exception.KToolException;
import com.ktools.manager.datasource.jdbc.query.QueryCondition;
import com.ktools.manager.datasource.jdbc.model.TableMetadata;

import java.util.List;
import java.util.Map;

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

    /**
     * 连接数据源
     */
    void conn();

    /**
     * 断开连接数据源
     */
    void disConn();

    /**
     * 查询所有schema
     */
    List<String> selectAllSchema() throws KToolException;

    /**
     * 查询所有表名
     *
     * @param schema schema
     */
    List<String> selectAllTable(String schema) throws KToolException;

    /**
     * 查询表元数据
     */
    TableMetadata selectTableMetadata(String schema, String tableName) throws KToolException;

    /**
     * 查询数据
     */
    List<Map<String, Object>> selectData(String schema, String tableName, QueryCondition queryCondition) throws KToolException;

}
