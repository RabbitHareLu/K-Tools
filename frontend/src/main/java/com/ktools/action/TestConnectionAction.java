package com.ktools.action;

import com.ktools.KToolsContext;
import com.ktools.api.DataSourceApi;
import com.ktools.common.utils.DialogUtil;
import com.ktools.exception.KToolException;
import com.ktools.panel.RegularPanel;
import lombok.Data;

import java.awt.*;
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

    private RegularPanel regularPanel;
    private String type;

    public TestConnectionAction() {
    }

    public TestConnectionAction(RegularPanel regularPanel, String type) {
        this.regularPanel = regularPanel;
        this.type = type;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Map<String, String> propertiesMap = buildMap();
        try {
            KToolsContext.getInstance().getApi(DataSourceApi.class).testDataSource(type, propertiesMap);
        } catch (KToolException ex) {
            DialogUtil.showErrorDialog((Component) e.getSource(), ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private Map<String, String> buildMap() {
        Map<String, String> map = new HashMap<>();
        map.put("username", regularPanel.getUsernameField().getText());
        map.put("jdbcUrl", regularPanel.getUrlField().getText());
        map.put("password", String.valueOf(regularPanel.getPasswordField().getPassword()));
        return map;
    }
}
