package com.ktools.warehouse.mybatis.entity;

import com.ktools.warehouse.manager.datasource.SysDataSource;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * @author WCG
 */
@Data
@Table(value = "PROP", dataSource = SysDataSource.DATASOURCE_NAME)
public class PropEntity {

    @Id("ID")
    private Integer id;

    @Column("KEY")
    private String key;

    @Column("VALUE")
    private String value;

}
