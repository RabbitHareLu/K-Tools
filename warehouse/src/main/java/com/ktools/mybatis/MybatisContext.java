package com.ktools.mybatis;

import com.ktools.mybatis.entity.PropEntity;
import com.ktools.mybatis.mapper.PropMapper;
import com.ktools.mybatis.mapper.TreeMapper;
import com.mybatisflex.core.MybatisFlexBootstrap;
import lombok.Getter;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

/**
 *
 *
 * @author WCG
 */
@Getter
public class MybatisContext {

    private final MybatisFlexBootstrap bootstrap;

    private final PropMapper propMapper;

    private final TreeMapper treeMapper;

    public MybatisContext(DataSource dataSource) {
        this.bootstrap = MybatisFlexBootstrap.getInstance()
                .setDataSource(dataSource)
                .addMapper(PropMapper.class)
                .addMapper(TreeMapper.class)
                .start();

        this.propMapper = bootstrap.getMapper(PropMapper.class);
        this.treeMapper = bootstrap.getMapper(TreeMapper.class);
    }

    public Properties loadAllProperties() {
        List<PropEntity> propEntities = this.propMapper.selectAll();
        Properties properties = new Properties();
        propEntities.forEach(prop -> properties.put(prop.getKey(), prop.getValue()));
        return properties;
    }
}
