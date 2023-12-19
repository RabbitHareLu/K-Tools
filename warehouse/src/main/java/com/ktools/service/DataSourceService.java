package com.ktools.service;

import com.ktools.api.DataSourceApi;
import com.ktools.exception.KToolException;
import com.ktools.manager.datasource.KDataSourceFactory;
import com.ktools.manager.datasource.KDataSourceHandler;
import com.ktools.manager.datasource.model.KDataSourceMetadata;

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

}
