package com.ktools.warehouse.task.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据源配置
 *
 * @author WCG
 */
@Data
public class JobSourceConfig implements Serializable {

    /**
     * 数据源类型
     */
    private String sourceType;

    // JDBC配置
    /**
     * 数据源id
     */
    private String sourceId;

    /**
     * 数据源schema
     */
    private String sourceSchema;

    /**
     * 数据源表名
     */
    private String sourceTableName;

    /**
     * 视图sql
     */
    private String viewSql;

    // 文件配置
    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 分割符
     */
    private char separator;

}
