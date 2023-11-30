package com.ktools;

import com.ktools.api.SystemApi;
import com.ktools.common.db.DbContext;
import com.ktools.manager.datasource.DataSourceManager;
import com.ktools.manager.datasource.SysDataSource;
import com.ktools.manager.task.TaskManager;
import com.ktools.service.SystemService;
import lombok.Getter;

import java.util.Properties;

/**
 * K-Tools 上下文
 *
 * @author WCG
 */
@Getter
public class KToolsContext {

    private static volatile KToolsContext INSTANCE;

    private final DataSourceManager dataSourceManager;

    private final DbContext dbContext;

    private final Properties properties;

    private final TaskManager taskManager;

    private KToolsContext() {
        // 初始化数据源管理器
        this.dataSourceManager = DataSourceManager.getInstance();
        // 初始化系统数据源
        SysDataSource.init();
        // 初始化DbContext
        this.dbContext = new DbContext();
        // 初始化配置信息
        this.properties = this.dbContext.loadAllProperties(this.dataSourceManager.getSystemDataSource());
        // 初始化任务管理器
        this.taskManager = new TaskManager();
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
        this.dataSourceManager.close();
        this.taskManager.shutdown();
    }

    public <T> T getApi(Class<T> tClass) {
        if (tClass == SystemApi.class) {
            return tClass.cast(new SystemService());
        }
        return null;
    }
}
