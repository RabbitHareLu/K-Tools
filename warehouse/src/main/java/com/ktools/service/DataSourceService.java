package com.ktools.service;

import com.ktools.api.DataSourceApi;
import com.ktools.exception.KToolException;
import com.ktools.manager.datasource.KDataSourceFactory;
import com.ktools.manager.datasource.KDataSourceHandler;
import com.ktools.manager.datasource.KDataSourceManager;
import com.ktools.manager.datasource.jdbc.model.TableMetadata;
import com.ktools.manager.datasource.jdbc.query.CommonPage;
import com.ktools.manager.datasource.jdbc.query.QueryCondition;
import com.ktools.manager.datasource.model.KDataSourceMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author WCG
 */
public class DataSourceService extends BaseService implements DataSourceApi {

    @Override
    public List<KDataSourceMetadata> getAllMetadata() {
        return this.kToolsContext.getDataSourceManager().supportDataSource();
    }

    @Override
    public KDataSourceMetadata getMetadata(String name) throws KToolException {
        return this.kToolsContext.getDataSourceManager().getFactory(name).getMetadata();
    }

    @Override
    public void testDataSource(String type, Map<String, String> properties) throws KToolException {
        // 构建Properties
        Properties datasourceProperties = new Properties();
        datasourceProperties.putAll(properties);
        // 获取数据源处理器
        KDataSourceFactory sourceFactory = this.kToolsContext.getDataSourceManager().getFactory(type);
        KDataSourceHandler dataSourceHandler = sourceFactory.createDataSourceHandler(datasourceProperties);
        // 测试数据源连接
        dataSourceHandler.testConn();
    }

    @Override
    public void conn(String id, String type, Map<String, String> properties) throws KToolException {
        // 构建Properties
        Properties datasourceProperties = new Properties();
        datasourceProperties.putAll(properties);
        // 获取数据源处理器
        KDataSourceManager dataSourceManager = this.kToolsContext.getDataSourceManager();
        KDataSourceHandler dataSourceHandler;
        if (dataSourceManager.existHandler(id)) {
            dataSourceHandler = dataSourceManager.getHandler(id);
        } else {
            KDataSourceFactory sourceFactory = this.kToolsContext.getDataSourceManager().getFactory(type);
            dataSourceHandler = sourceFactory.createDataSourceHandler(datasourceProperties);
            // 加入管理器
            this.kToolsContext.getDataSourceManager().addHandler(id, dataSourceHandler);
        }
        // 连接数据源
        dataSourceHandler.conn();
    }

    @Override
    public void disConn(String id) {
        // 获取数据源处理器
        KDataSourceManager dataSourceManager = this.kToolsContext.getDataSourceManager();
        if (dataSourceManager.existHandler(id)) {
            KDataSourceHandler dataSourceHandler = dataSourceManager.getHandler(id);
            dataSourceHandler.disConn();
            // 从管理器中移除
            this.kToolsContext.getDataSourceManager().removeHandler(id);
        }
    }

    @Override
    public List<String> selectAllSchema(String id) throws KToolException {
        // 获取数据源处理器
        KDataSourceManager dataSourceManager = this.kToolsContext.getDataSourceManager();
        if (dataSourceManager.existHandler(id)) {
            KDataSourceHandler dataSourceHandler = dataSourceManager.getHandler(id);
            return dataSourceHandler.selectAllSchema();
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> selectAllTable(String id, String schema) throws KToolException {
        // 获取数据源处理器
        KDataSourceManager dataSourceManager = this.kToolsContext.getDataSourceManager();
        if (dataSourceManager.existHandler(id)) {
            KDataSourceHandler dataSourceHandler = dataSourceManager.getHandler(id);
            return dataSourceHandler.selectAllTable(schema);
        }
        return new ArrayList<>();
    }

    @Override
    public TableMetadata selectTableMetadata(String id, String schema, String tableName) throws KToolException {
        // 获取数据源处理器
        KDataSourceManager dataSourceManager = this.kToolsContext.getDataSourceManager();
        if (dataSourceManager.existHandler(id)) {
            KDataSourceHandler dataSourceHandler = dataSourceManager.getHandler(id);
            return dataSourceHandler.selectTableMetadata(schema, tableName);
        }
        return null;
    }

    @Override
    public CommonPage<Map<String, Object>> selectData(String id, String schema, String tableName, QueryCondition queryCondition) throws KToolException {
        // 获取数据源处理器
        KDataSourceManager dataSourceManager = this.kToolsContext.getDataSourceManager();
        if (dataSourceManager.existHandler(id)) {
            KDataSourceHandler dataSourceHandler = dataSourceManager.getHandler(id);
            return dataSourceHandler.selectData(schema, tableName, queryCondition);
        }
        return new CommonPage<>();
    }

}
