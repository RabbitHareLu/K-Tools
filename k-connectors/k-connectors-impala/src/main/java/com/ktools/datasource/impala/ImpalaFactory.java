package com.ktools.datasource.impala;

import com.ktools.common.utils.ConfigParamUtil;
import com.ktools.datasource.impala.config.ImpalaConfig;
import com.ktools.manager.datasource.KDataSourceFactory;
import com.ktools.manager.datasource.KDataSourceHandler;
import com.ktools.manager.datasource.model.KDataSourceMetadata;

import java.util.Properties;

/**
 * @author WCG
 */
public class ImpalaFactory implements KDataSourceFactory {

    private static final String DATA_SOURCE_TYPE = "IMPALA";

    private static final KDataSourceMetadata METADATA = createMetadata();

    @Override
    public KDataSourceMetadata getMetadata() {
        return METADATA;
    }

    @Override
    public KDataSourceHandler createDataSourceHandler(Properties properties) {

        return null;
    }

    private static KDataSourceMetadata createMetadata() {
        // 构建元数据
        var kDataSourceMetadata = new KDataSourceMetadata();
        kDataSourceMetadata.setName(DATA_SOURCE_TYPE);
        kDataSourceMetadata.setLogo("/images/impala.svg");
        kDataSourceMetadata.setConfig(ConfigParamUtil.parseConfigClass(ImpalaConfig.class));
        return kDataSourceMetadata;
    }

}
