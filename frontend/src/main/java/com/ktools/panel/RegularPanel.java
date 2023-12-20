package com.ktools.panel;

import com.ktools.common.utils.BoxUtil;
import com.ktools.manager.datasource.model.KDataSourceMetadata;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月19日 17:00
 */
@Data
public class RegularPanel extends JPanel {

    private Map<String, Object> componentMap = new HashMap<>();

    public RegularPanel() {
        setLayout(new BorderLayout());
        Box box = Box.createVerticalBox();
        BoxUtil.addVerticalStrut(box, 30);

        initSubBox(box, "用户名: ");
        BoxUtil.addVerticalStrut(box, 30);
        initPasswordBox(box, "密码: ");
        BoxUtil.addVerticalStrut(box, 30);
        initSubBox(box, "URL: ");

        add(box, BorderLayout.NORTH);
    }

//    private RegularPanel() {
//
//    }


    private void initSubBox(Box rootBox, String key) {
        Box keyBox = Box.createHorizontalBox();

        JLabel keyLabel = new JLabel(key);
        Dimension dimension = keyLabel.getPreferredSize();
        double fixedWidth = 100;
        double height = dimension.getHeight();
        dimension.setSize(fixedWidth, height);
        keyLabel.setPreferredSize(dimension);
        keyLabel.setToolTipText(key);
        keyBox.add(keyLabel);

        BoxUtil.addVerticalStrut(keyBox, 30);

        JTextField valueInputField = new JTextField();
        keyBox.add(valueInputField);
        BoxUtil.addHorizontalStrut(keyBox, 70);

        rootBox.add(keyBox);

        switch (key) {
            case "用户名: " -> componentMap.put("username", valueInputField);
            case "URL: " -> componentMap.put("jdbcUrl", valueInputField);
        }

    }

    private void initPasswordBox(Box rootBox, String key) {
        Box keyBox = Box.createHorizontalBox();

        JLabel keyLabel = new JLabel(key);
        Dimension dimension = keyLabel.getPreferredSize();
        double fixedWidth = 100;
        double height = dimension.getHeight();
        dimension.setSize(fixedWidth, height);
        keyLabel.setPreferredSize(dimension);
        keyLabel.setToolTipText(key);
        keyBox.add(keyLabel);

        BoxUtil.addVerticalStrut(keyBox, 30);

        JPasswordField valueInputField = new JPasswordField();
        keyBox.add(valueInputField);
        BoxUtil.addHorizontalStrut(keyBox, 70);

        rootBox.add(keyBox);
        componentMap.put("password", valueInputField);
    }

}
