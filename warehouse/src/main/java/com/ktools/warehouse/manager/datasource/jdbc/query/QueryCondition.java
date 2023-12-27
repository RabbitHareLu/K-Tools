package com.ktools.warehouse.manager.datasource.jdbc.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询条件
 *
 * @author WCG
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryCondition extends PageQuery implements Serializable {

    /**
     * where条件
     */
    private String whereCondition;

}
