package com.ktools.frontend.action;

import com.ktools.frontend.frame.JDBCConnectionFrame;
import com.ktools.warehouse.manager.datasource.model.KDataSourceMetadata;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月19日 10:59
 */
@Slf4j
@Data
public class NewJDBCConnectionAction implements ActionListener {

    JDBCConnectionFrame jdbcConnectionFrame = null;

    private KDataSourceMetadata kDataSourceMetadata;

    public NewJDBCConnectionAction(KDataSourceMetadata kDataSourceMetadata) {
        this.kDataSourceMetadata = kDataSourceMetadata;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (JDBCConnectionFrame.openFlag) {
            jdbcConnectionFrame = new JDBCConnectionFrame(kDataSourceMetadata);
        } else {
            log.info("已有新建Impala连接窗口!");
            jdbcConnectionFrame.toFront();
            jdbcConnectionFrame.repaint();
        }
    }
}
