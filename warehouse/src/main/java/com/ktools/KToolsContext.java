package com.ktools;

import com.ktools.manager.datasource.DataSourceManager;
import com.ktools.manager.datasource.SysDataSource;
import lombok.Getter;

/**
 * K-Tools 上下文
 *
 * @author WCG
 */
@Getter
public class KToolsContext {

    private static volatile KToolsContext INSTANCE;

    private final DataSourceManager dataSourceManager;

    private KToolsContext() {
        // 初始化数据源管理器
        this.dataSourceManager = DataSourceManager.getInstance();
        // 初始化系统数据源
        SysDataSource.init();
    }

    public static KToolsContext getInstance() {
        if (INSTANCE != null) {
            synchronized (KToolsContext.class) {
                if (INSTANCE != null) {
                    INSTANCE = new KToolsContext();
                }
            }
        }
        return INSTANCE;
    }

    public void showdown() {
        this.dataSourceManager.close();
    }

}
