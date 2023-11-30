package com.ktools.common.stream;

/**
 * 流式处理接口
 *
 * @author WCG
 */
@FunctionalInterface
public interface StreamFunction<T> {

    void invoke(T t);

}