package com.ktools.common.db;

import com.ktools.common.stream.StreamFunction;
import com.ktools.common.utils.ReflectUtil;
import com.ktools.common.utils.StreamUtil;
import com.ktools.common.utils.StringUtil;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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

    public <E> List<E> selectAll(DataSource dataSource, Class<E> eClass) {
        TableName tableName = eClass.getAnnotation(TableName.class);
        if (tableName == null || StringUtil.isBlank(tableName.value())) {
            throw new RuntimeException("未配置表名");
        }

        List<E> result = new ArrayList<>();
        select(dataSource, tableName.value(), mapStream -> {
            Field[] fields = ReflectUtil.getAllFields(eClass);
            List<E> list = mapStream.map(map -> {
                E e;
                try {
                    e = eClass.getConstructor().newInstance();
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(TableField.class)) {
                            TableField tableField = field.getAnnotation(TableField.class);
                            field.setAccessible(true);
                            field.set(e, tableField.type().convertData(map.getOrDefault(tableField.value(), null)));
                            field.setAccessible(false);
                        }
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                }
                return e;
            }).toList();
            result.addAll(list);
        });
        return result;
    }

}
