package com.ktools.action;

import com.ktools.KToolsContext;
import com.ktools.api.DataSourceApi;
import com.ktools.common.utils.DialogUtil;
import com.ktools.exception.KToolException;
import lombok.Data;

import javax.swing.*;
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

    private Map<String, Object> componentMap;
    private String type;

    public TestConnectionAction() {
    }

    public TestConnectionAction(Map<String, Object> map, String type) {
        this.componentMap = map;
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
        for (Map.Entry<String, Object> stringObjectEntry : componentMap.entrySet()) {
            switch (stringObjectEntry.getValue()) {
                case JPasswordField value -> map.put(stringObjectEntry.getKey(), String.valueOf(value.getPassword()));
                case JTextField value -> map.put(stringObjectEntry.getKey(), value.getText());
                default -> throw new IllegalStateException("Unexpected value: " + stringObjectEntry.getValue());
            }

        }
        return map;
    }
}
