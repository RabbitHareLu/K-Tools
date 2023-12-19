package com.ktools.service;

import com.ktools.api.DataSourceApi;
import com.ktools.manager.datasource.model.KDataSourceMetadata;

import java.util.List;

/**
 * @author WCG
 */
public class DataSourceService extends BaseService implements DataSourceApi {

    @Override
    public List<KDataSourceMetadata> getAllMetadata() {
        return this.kToolsContext.getDataSourceManager().supportDataSource();
    }

}
