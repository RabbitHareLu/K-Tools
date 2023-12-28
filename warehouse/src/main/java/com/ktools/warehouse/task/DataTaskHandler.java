package com.ktools.warehouse.task;

import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.task.element.BaseRow;
import com.ktools.warehouse.task.model.JobSinkConfig;
import com.ktools.warehouse.task.model.JobSourceConfig;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 数据任务处理器
 *
 * @author WCG
 */
public interface DataTaskHandler {

    /**
     * 构建源节点流
     */
    void source(JobSourceConfig sourceConfig, Consumer<Stream<BaseRow>> consumer) throws KToolException;

    /**
     * 落库
     */
    void sink(JobSinkConfig sinkConfig, Stream<BaseRow> stream) throws KToolException;

}
