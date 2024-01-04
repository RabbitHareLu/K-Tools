package com.ktools.warehouse.task.job;

import lombok.Data;

/**
 * 任务执行结果
 *
 * @author WCG
 */
@Data
public class TaskResult {

    /**
     * 任务状态
     */
    private TaskResultState state;

    /**
     * 任务消息
     */
    private String message;

    /**
     * 任务执行时间
     */
    private Long taskExecTime;

    /**
     * 任务数据量
     */
    private Long count;

}
