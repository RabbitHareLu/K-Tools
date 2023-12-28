package com.ktools.warehouse.manager.datasource.jdbc.query;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.row.Row;
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

    public <T> Page<T> getPage(Class<T> tClass) {
        Page<T> page = new Page<>();
        if (pageNum != null) {
            page.setPageNumber(pageNum);
        }
        if (pageSize != null) {
            page.setPageSize(pageSize);
        }
        if (total != null) {
            page.setTotalRow(total);
        }
        return page;
    }

}
