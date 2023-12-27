package com.ktools.warehouse;

import com.ktools.warehouse.api.DataSourceApi;
import com.ktools.warehouse.api.SystemApi;
import com.ktools.warehouse.manager.datasource.KDataSourceManager;
import com.ktools.warehouse.manager.datasource.SysDataSource;
import com.ktools.warehouse.manager.task.TaskManager;
import com.ktools.warehouse.manager.uid.IdGenerator;
import com.ktools.warehouse.mybatis.MybatisContext;
import com.ktools.warehouse.service.DataSourceService;
import com.ktools.warehouse.service.SystemService;
import lombok.Getter;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * K-Tools 上下文
 *
 * @author WCG
 */
@Getter
public class KToolsContext {

    private static volatile KToolsContext INSTANCE;

    private final MybatisContext mybatisContext;

    private final Properties properties;

    private final TaskManager taskManager;

    private final IdGenerator idGenerator;

    private final KDataSourceManager dataSourceManager;

    private KToolsContext() {
        // 初始化系统数据源
        DataSource dataSource = SysDataSource.init();
        // 初始化mybatis
        this.mybatisContext = new MybatisContext(dataSource);
        // 像mybatis注册系统数据源
        mybatisContext.addDataSource(SysDataSource.DATASOURCE_NAME, dataSource);
        // 初始化配置信息
        this.properties = this.mybatisContext.loadAllProperties();
        // 初始化任务管理器
        this.taskManager = new TaskManager();
        // 初始化id生成器
        this.idGenerator = new IdGenerator(mybatisContext);
        // 初始化数据源管理器
        this.dataSourceManager = new KDataSourceManager();
    }

    public static KToolsContext getInstance() {
        if (INSTANCE == null) {
            synchronized (KToolsContext.class) {
                if (INSTANCE == null) {
                    INSTANCE = new KToolsContext();
                }
            }
        }
        return INSTANCE;
    }

    public void showdown() {
        this.mybatisContext.showdown();
        this.taskManager.shutdown();
    }

    public <T> T getApi(Class<T> tClass) {
        if (tClass == SystemApi.class) {
            return tClass.cast(new SystemService());
        } else if (tClass == DataSourceApi.class){
            return tClass.cast(new DataSourceService());
        }
        return null;
    }
}
