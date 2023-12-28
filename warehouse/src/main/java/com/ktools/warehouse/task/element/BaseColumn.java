package com.ktools.warehouse.task.element;

import com.ktools.warehouse.common.utils.StringUtil;
import lombok.Getter;

import java.io.Serializable;


/**
 * 列数据
 *
 * @author WCG
 */
@Getter
public class BaseColumn implements Serializable {

    private final String columnName;

    private final Object data;

    private final DataType dataType;

    private BaseColumn(final String columnName, final Object data, final DataType dataType) {
        this.columnName = columnName;
        this.data = data;
        this.dataType = dataType;
    }

    public static BaseColumn create(final String columnName, final Object data, final DataType dataType) {
        if (StringUtil.isBlank(columnName)) {
            throw new RuntimeException("列名为空！");
        }
        return new BaseColumn(columnName, data, dataType);
    }

}