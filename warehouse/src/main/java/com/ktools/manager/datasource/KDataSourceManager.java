package com.ktools.manager.datasource;

import com.ktools.exception.KToolException;
import com.ktools.manager.datasource.model.KDataSourceMetadata;

import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * 数据源管理器
 *
 * @author WCG
 */
public class KDataSourceManager {

    /**
     * 数据源工厂
     */
    private final ServiceLoader<KDataSourceFactory> factories;

    public KDataSourceManager() {
        this.factories = ServiceLoader.load(KDataSourceFactory.class);
    }

    /**
     * 查询加载
     */
    public void reload() {
        this.factories.reload();
    }

    /**
     * 查询支持的数据源类型
     */
    public List<KDataSourceMetadata> supportDataSource() {
        this.reload();
        return this.factories.stream().map(provider -> provider.get().getMetadata()).collect(Collectors.toList());
    }

    /**
     * 获取数据源工厂
     *
     * @param name 数据源名称
     */
    public KDataSourceFactory getFactory(String name) throws KToolException {
        for (KDataSourceFactory factory : this.factories) {
            if (Objects.equals(factory.getMetadata().getName(), name)) {
                return factory;
            }
        }
        throw new KToolException("不支持的元数据类型");
    }

}
