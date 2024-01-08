package com.ktools.warehouse.task.job;

import com.ktools.warehouse.KToolsContext;
import com.ktools.warehouse.common.utils.StringUtil;
import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.task.DataTaskHandler;
import com.ktools.warehouse.task.element.BaseRow;
import com.ktools.warehouse.task.file.FileTaskHandlerManager;
import com.ktools.warehouse.task.model.Job;
import com.ktools.warehouse.task.model.JobSinkConfig;
import com.ktools.warehouse.task.model.JobSourceConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * 数据任务
 *
 * @author WCG
 */
@Slf4j
@AllArgsConstructor
public class DataTask implements Callable<TaskResult> {

    private final Job job;

    @Override
    public TaskResult call() {
        long startTime = System.currentTimeMillis();

        // 任务执行结果
        TaskResult taskResult = new TaskResult();

        try {
            // 执行任务
            Long count = doTask();
            // 封装执行结果
            taskResult.setMessage("数据接入任务执行成功！");
            taskResult.setState(TaskResultState.SUCCESS);
            taskResult.setCount(count);
        } catch (Exception e) {
            // 处理异常
            String errorMsg = handleException(e);
            taskResult.setState(TaskResultState.FAIL);
            taskResult.setMessage(errorMsg);
        }

        long endTime = System.currentTimeMillis();
        long taskExecTime = endTime - startTime;
        taskResult.setTaskExecTime(taskExecTime);
        return taskResult;
    }

    private Long doTask() throws KToolException {
        // 数据来源处理器
        JobSourceConfig sourceConfig = job.getSourceConfig();
        String fileType = StringUtil.isNotBlank(sourceConfig.getFilePath())
                ? FileTaskHandlerManager.parseFileType(sourceConfig.getFilePath())
                : null;
        DataTaskHandler sourceHandler = getDataTaskHandler(sourceConfig.getSourceType(), sourceConfig.getSourceId(), fileType);
        // 目标节点处理器
        JobSinkConfig sinkConfig = job.getSinkConfig();
        DataTaskHandler sinkHandler = getDataTaskHandler(sinkConfig.getSinkType(), sinkConfig.getSinkId(), sinkConfig.getSinkFileType());

        AtomicLong count = new AtomicLong(0L);
        // 开始处理数据
        sourceHandler.source(job.getSourceConfig(), stream -> {
            try {
                Stream<BaseRow> peek = stream.peek(baseRow -> log.info("数据量：" + count.incrementAndGet()));
                sinkHandler.sink(job.getSinkConfig(), peek);
            } catch (KToolException e) {
                throw new RuntimeException(e);
            }
        });
        return count.get();
    }

    private static DataTaskHandler getDataTaskHandler(String type, String dataSourceId, String fileType) throws KToolException {
        if ("file".equalsIgnoreCase(type)) {
            return FileTaskHandlerManager.getFileTaskHandler(fileType);
        } else {
            return KToolsContext.getInstance().getDataSourceManager().getHandler(dataSourceId);
        }
    }

    private String handleException(Exception e) {
        // 构建错误信息
        StringBuilder logContent = new StringBuilder("数据任务执行失败！任务名:" + job.getJobName() + ", 异常信息:" + e.getMessage());
        Throwable cause = e.getCause();
        while (cause != null) {
            logContent.append(" cause: ").append(cause.getMessage());
            Throwable nextCause = cause.getCause();
            // 解决异常循环问题
            if (nextCause == cause) {
                break;
            }
            cause = nextCause;
        }

        // 返回错误信息
        return logContent.toString();
    }

}
