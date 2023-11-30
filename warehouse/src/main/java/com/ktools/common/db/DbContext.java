package com.ktools.common.db;

import com.ktools.common.stream.StreamFunction;
import com.ktools.common.utils.StreamUtil;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * @author WCG
 */
public class DbContext {

    private static final String PROPERTIES_TABLE = "prop";
    private static final String PROPERTIES_KEY_COLUMN = "key";
    private static final String PROPERTIES_VALUE_COLUMN = "value";

    public void select(DataSource dataSource, String tableName, StreamFunction<Stream<Map<String, Object>>> streamFunction) {
        String sql = String.format("select * from %s", tableName);
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)
        ) {
            Stream<Map<String, Object>> mapStream = StreamUtil.buildStream(resultSet);
            streamFunction.invoke(mapStream);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Properties loadAllProperties(DataSource sysDataSource) {
        Properties properties = new Properties();
        select(sysDataSource, PROPERTIES_TABLE, mapStream -> {
            mapStream.forEach(map -> properties.put(map.get(PROPERTIES_KEY_COLUMN), map.get(PROPERTIES_VALUE_COLUMN)));
        });
        return properties;
    }

}
