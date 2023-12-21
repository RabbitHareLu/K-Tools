package com.ktools.manager.datasource.jdbc.query;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据封装类
 *
 * @author WCG
 */
@Data
public class CommonPage<T> implements Serializable {

    private Long pageNum;

    private Long pageSize;

    private Long totalPage;

    private Long total;

    private List<T> records;

}
