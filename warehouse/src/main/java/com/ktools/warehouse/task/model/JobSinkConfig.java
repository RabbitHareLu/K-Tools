package com.ktools.warehouse.task.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 目标数据源配置
 *
 * @author WCG
 */
@Data
public class JobSinkConfig implements Serializable {

    /**
     * 目标类型
     */
    private String sinkType;

    /**
     * 目标节点id
     */
    private String sinkId;

    /**
     * 目标schema
     */
    private String sinkSchema;

    /**
     * 目标表
     */
    private String sinkTableName;

    /**
     * 文件类型
     */
    private String sinkFileType;

    /**
     * 文件目标路径
     */
    private String fileSinkPath;

    /**
     * 分割符
     */
    private char separator;
}
