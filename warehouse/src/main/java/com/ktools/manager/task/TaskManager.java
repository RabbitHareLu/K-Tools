package com.ktools.manager.task;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 任务执行器
 *
 * @author WCG
 */
@Slf4j
public class TaskManager {

    private final ExecutorService executorService;

    public TaskManager() {
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
    }

    public <V> Future<V> submitTask(Callable<V> callable) {
        return executorService.submit(callable);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
