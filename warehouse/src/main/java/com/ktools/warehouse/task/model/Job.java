package com.ktools.warehouse.task.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 任务
 *
 * @author WCG
 */
@Data
public class Job implements Serializable {

    /**
     * 任务id
     */
    private String jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 数据源配置
     */
    private JobSourceConfig sourceConfig;

    /**
     * 目标数据源配置
     */
    private JobSinkConfig sinkConfig;

}
