package com.ktools.datasource.impala.handler;

import com.ktools.common.utils.ConfigParamUtil;
import com.ktools.datasource.impala.config.ImpalaConfig;
import com.ktools.datasource.impala.type.ImpalaType;
import com.ktools.exception.KToolException;
import com.ktools.manager.datasource.jdbc.AbstractJdbcHandler;
import com.ktools.manager.datasource.jdbc.OperationType;
import com.ktools.manager.datasource.jdbc.query.PageQuery;

import java.sql.SQLType;
import java.util.Properties;

/**
 *
 *
 * @author WCG
 */
public class ImpalaHandler extends AbstractJdbcHandler {

    private final ImpalaConfig impalaConfig;

    public ImpalaHandler(Properties properties) throws KToolException {
        super(properties);
        this.impalaConfig = ConfigParamUtil.buildConfig(properties, ImpalaConfig.class);
    }

    @Override
    protected String getDriverClass() {
        return "com.cloudera.impala.jdbc41.Driver";
    }

    @Override
    protected String processKeywords(String keyword) {
        return "`" + keyword + "`";
    }

    @Override
    protected SQLType getSqlTypeByJdbcType(String typeName) {
        return ImpalaType.valueOf(typeName);
    }

    @Override
    protected OperationType parseSql(String sql) {
        String upperSql = sql.trim().toUpperCase();
        if (upperSql.startsWith("SELECT")) {
            return OperationType.QUERY;
        }
        if (upperSql.startsWith("UPDATE")) {
            return OperationType.UPDATE;
        }
        if (upperSql.startsWith("DELETE")) {
            return OperationType.DELETE;
        }
        return OperationType.UNKNOWN;
    }

    @Override
    protected String buildPageSql(String querySql, PageQuery pageQuery) {
        StringBuilder sqlBuilder = new StringBuilder(querySql.length() + 14);
        sqlBuilder.append(querySql);
        long offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        if (offset == 0L) {
            sqlBuilder.append("\n LIMIT ").append(pageQuery.getPageSize());
        } else {
            sqlBuilder.append("\n LIMIT ").append(offset).append(",").append(pageQuery.getPageSize());
        }
        return sqlBuilder.toString();
    }

}
