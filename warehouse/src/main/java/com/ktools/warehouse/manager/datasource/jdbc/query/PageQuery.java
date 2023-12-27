package com.ktools.warehouse.manager.datasource.jdbc.query;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页条件
 *
 * @author WCG
 */
@Data
public class PageQuery implements Serializable {

    /**
     * 页数
     */
    private Long pageNum;

    /**
     * 页大小
     */
    private Long pageSize;

    /**
     * 总页数
     */
    private Long totalPage;

    /**
     * 数据总数
     */
    private Long total;

}
