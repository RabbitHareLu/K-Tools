package com.ktools.frontend.action;

import com.ktools.warehouse.KToolsContext;
import com.ktools.warehouse.api.DataSourceApi;
import com.ktools.frontend.common.utils.DialogUtil;
import com.ktools.warehouse.exception.KToolException;
import com.ktools.frontend.frame.JDBCConnectionFrame;
import com.ktools.frontend.panel.RegularPanel;
import lombok.Data;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月19日 17:36
 */
@Data
public class TestConnectionAction implements ActionListener {


    private JDBCConnectionFrame jdbcConnectionFrame;

    public TestConnectionAction() {
    }

    public TestConnectionAction(JDBCConnectionFrame jdbcConnectionFrame) {
        this.jdbcConnectionFrame = jdbcConnectionFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Map<String, String> propertiesMap = buildMap();
        JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor((JButton) e.getSource());
        try {
            KToolsContext.getInstance().getApi(DataSourceApi.class).testDataSource(jdbcConnectionFrame.getKDataSourceMetadata().getName(), propertiesMap);
            JOptionPane.showMessageDialog(jFrame,
                    new Object[]{"数据源连接测试成功！"},
                    "测试连接", JOptionPane.PLAIN_MESSAGE);
        } catch (KToolException ex) {
            DialogUtil.showErrorDialog(jFrame, ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private Map<String, String> buildMap() {
        RegularPanel regularPanel = jdbcConnectionFrame.getRegularPanel();
        Map<String, String> map = new HashMap<>();
        map.put("username", regularPanel.getUsernameField().getText());
        map.put("jdbcUrl", regularPanel.getUrlField().getText());
        map.put("password", String.valueOf(regularPanel.getPasswordField().getPassword()));
        return map;
    }
}
