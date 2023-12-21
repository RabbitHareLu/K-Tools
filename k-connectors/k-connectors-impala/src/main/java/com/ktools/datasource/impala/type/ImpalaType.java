package com.ktools.datasource.impala.type;

import java.sql.SQLType;

/**
 * impala字段类型
 *
 * @author WCG
 */
public enum ImpalaType implements SQLType {

    BIGINT("BIGINT", -5),
    TINYINT("TINYINT", -6),
    BOOLEAN("BOOLEAN", 16),
    CHAR("CHAR", 1),
    DECIMAL("DECIMAL", 3),
    DOUBLE("DOUBLE", 8),
    FLOAT("FLOAT", 6),
    INT("INT", 4),
    REAL("REAL", 7),
    SMALLINT("SMALLINT", 5),
    STRING("STRING", 12),
    TIMESTAMP("TIMESTAMP", 93),
    VARCHAR("VARCHAR", 12),
    ;

    private final String name;

    private final int jdbcType;

    ImpalaType(String name, int jdbcType) {
        this.name = name;
        this.jdbcType = jdbcType;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getVendor() {
        return "kdp.impala";
    }

    @Override
    public Integer getVendorTypeNumber() {
        return jdbcType;
    }

}
