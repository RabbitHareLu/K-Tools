package com.ktools.api;

import com.ktools.manager.datasource.model.KDataSourceMetadata;

import java.util.List;

/**
 * @author WCG
 */
public interface DataSourceApi {

    List<KDataSourceMetadata> getAllMetadata();

}
