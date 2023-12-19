package com.ktools.mybatis;

import com.ktools.manager.datasource.SysDataSource;
import com.ktools.mybatis.entity.PropEntity;
import com.ktools.mybatis.mapper.PropMapper;
import com.ktools.mybatis.mapper.TreeMapper;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.MybatisFlexBootstrap;
import com.mybatisflex.core.datasource.FlexDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 *
 *
 * @author WCG
 */
@Slf4j
public class MybatisContext {

    private final MybatisFlexBootstrap bootstrap;

    public MybatisContext(DataSource dataSource) {
        FlexGlobalConfig.getDefaultConfig().setPrintBanner(false);
        this.bootstrap = MybatisFlexBootstrap.getInstance()
                .addDataSource(SysDataSource.DATASOURCE_NAME, dataSource)
                .addMapper(PropMapper.class)
                .addMapper(TreeMapper.class)
                .start();
    }

    public <T> T getMapper(Class<T> tClass) {
        return bootstrap.getMapper(tClass);
    }

    public Properties loadAllProperties() {
        List<PropEntity> propEntities = this.getMapper(PropMapper.class).selectAll();
        Properties properties = new Properties();
        propEntities.forEach(prop -> properties.put(prop.getKey(), prop.getValue()));
        return properties;
    }

    public void addDataSource(String key, DataSource dataSource) {
        FlexDataSource flexDataSource = FlexGlobalConfig.getDefaultConfig().getDataSource();
        flexDataSource.addDataSource(key, dataSource);
    }

    public void removeDataSource(String key) {
        FlexDataSource flexDataSource = FlexGlobalConfig.getDefaultConfig().getDataSource();
        flexDataSource.removeDatasource(key);
    }

    public boolean existDataSource(String key) {
        FlexDataSource flexDataSource = FlexGlobalConfig.getDefaultConfig().getDataSource();
        return flexDataSource.getDataSourceMap().containsKey(key);
    }

    public void showdown() {
        FlexDataSource flexDataSource = FlexGlobalConfig.getDefaultConfig().getDataSource();
        Set<String> keySet = flexDataSource.getDataSourceMap().keySet();
        keySet.forEach(flexDataSource::removeDatasource);
    }
}
