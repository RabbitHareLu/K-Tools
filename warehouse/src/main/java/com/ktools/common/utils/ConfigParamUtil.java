package com.ktools.common.utils;

import com.ktools.manager.datasource.jdbc.ConfigParam;
import com.ktools.manager.datasource.model.KDataSourceConfig;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置项解析工具类
 *
 * @author WCG
 */
public class ConfigParamUtil {

    /**
     * 解析配置类
     *
     * @param configClass 配置类
     * @return 配置信息
     */
    public static List<KDataSourceConfig> parseConfigClass(Class<?> configClass) {
        List<KDataSourceConfig> configs = new ArrayList<>();
        for (Field field : ReflectUtil.getAllFields(configClass)) {
            if (field.isAnnotationPresent(ConfigParam.class)) {
                ConfigParam configParam = field.getAnnotation(ConfigParam.class);
                configs.add(new KDataSourceConfig(configParam.name(), configParam.key(), configParam.must(), configParam.defaultValue()));
            }
        }
        return configs;
    }

}
