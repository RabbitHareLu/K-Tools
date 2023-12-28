package com.ktools.frontend.panel;

import com.formdev.flatlaf.FlatClientProperties;
import com.ktools.frontend.common.utils.BoxUtil;
import com.ktools.warehouse.mybatis.entity.TreeEntity;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月19日 17:00
 */
@Data
public class RegularPanel extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField urlField;

    public RegularPanel() {
        setLayout(new BorderLayout());
        Box box = Box.createVerticalBox();
        BoxUtil.addVerticalStrut(box, 30);

        usernameField = initSubBox(box, "用户名: ");
        BoxUtil.addVerticalStrut(box, 30);
        passwordField = initPasswordBox(box, "密码: ");
        BoxUtil.addVerticalStrut(box, 30);
        urlField = initSubBox(box, "URL: ");

        add(box, BorderLayout.NORTH);
    }

    public RegularPanel(TreeEntity treeEntity) {
        Map<String, String> nodeInfo = treeEntity.getNodeInfo();
        setLayout(new BorderLayout());
        Box box = Box.createVerticalBox();
        BoxUtil.addVerticalStrut(box, 30);

        usernameField = initSubBox(box, "用户名: ");
        usernameField.setText(nodeInfo.get("username"));
        BoxUtil.addVerticalStrut(box, 30);
        passwordField = initPasswordBox(box, "密码: ");
        passwordField.setText(nodeInfo.get("password"));
        passwordField.putClientProperty( FlatClientProperties.STYLE, "showRevealButton: true" );
        BoxUtil.addVerticalStrut(box, 30);
        urlField = initSubBox(box, "URL: ");
        urlField.setText(nodeInfo.get("jdbcUrl"));

        add(box, BorderLayout.NORTH);
    }

    private JTextField initSubBox(Box rootBox, String key) {
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
        return valueInputField;
    }

    private JPasswordField initPasswordBox(Box rootBox, String key) {
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
        return valueInputField;
    }

}
