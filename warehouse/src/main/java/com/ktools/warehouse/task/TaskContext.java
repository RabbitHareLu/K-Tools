package com.ktools.warehouse.task;

import com.ktools.warehouse.KToolsContext;
import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.manager.task.TaskManager;
import com.ktools.warehouse.task.job.DataTask;
import com.ktools.warehouse.task.job.TaskResult;
import com.ktools.warehouse.task.model.Job;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 *
 * @author WCG
 */
@Slf4j
public class TaskContext {

    public static void submit(Job job) throws KToolException {
        TaskManager taskManager = KToolsContext.getInstance().getTaskManager();
        Future<TaskResult> future = taskManager.submitTask(new DataTask(job));

        try {
            TaskResult taskResult = future.get();
            log.info("数据量：" + taskResult.getCount());
            log.info("执行时间：" + taskResult.getTaskExecTime() + "ms");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

}
