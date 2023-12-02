package com.ktools;

import com.ktools.api.SystemApi;
import com.ktools.manager.datasource.SysDataSource;
import com.ktools.manager.task.TaskManager;
import com.ktools.manager.uid.IdGenerator;
import com.ktools.mybatis.MybatisContext;
import com.ktools.service.SystemService;
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
        }
        return null;
    }
}
