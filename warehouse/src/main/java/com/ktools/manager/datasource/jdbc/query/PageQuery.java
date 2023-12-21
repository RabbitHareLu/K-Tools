package com.ktools.manager.datasource.jdbc.query;

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
    private Integer pageNum;

    /**
     * 页大小
     */
    private Integer pageSize;

}
