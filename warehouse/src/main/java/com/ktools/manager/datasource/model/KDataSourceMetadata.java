package com.ktools.manager.datasource.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 数据源元数据
 *
 * @author WCG
 */
@Data
public class KDataSourceMetadata implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * logo
     */
    private String logo;

    /**
     * 可配置项
     */
    private List<KDataSourceConfig> config;

}
