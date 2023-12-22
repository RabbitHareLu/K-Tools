package com.ktools.action;

import com.ktools.frame.JDBCConnectionFrame;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月20日 21:08
 */
@Slf4j
@Data
public class EditJDBCConnectionAction implements ActionListener {

    JDBCConnectionFrame jdbcConnectionFrame = null;

    public EditJDBCConnectionAction() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (JDBCConnectionFrame.openFlag) {
            jdbcConnectionFrame = new JDBCConnectionFrame();
        } else {
            log.info("已有新建Impala连接窗口!");
            jdbcConnectionFrame.toFront();
            jdbcConnectionFrame.repaint();
        }
    }
}
