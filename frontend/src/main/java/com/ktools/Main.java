package com.ktools;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

@Data
public class Main {

    public static KToolsRootJFrame kToolsRootJFrame = null;
    public static void main(String[] args) {
        FlatMacLightLaf.setup();
        // 使用 FlatLaf 提供的组件样式
        UIManager.put("Button.arc", 50);  // 设置按钮的弧度
        UIManager.put("Component.focusWidth", 0);  // 设置组件的焦点边框宽度
        UIManager.put("TextComponent.arc", 10);
//        UIManager.put("AreaComponent.arc", 10);

        kToolsRootJFrame = new KToolsRootJFrame();
//        showIcon();
    }

    public static void showIcon() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("UIManager Icons Display");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

            Enumeration<Object> keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                if (key instanceof String && ((String) key).endsWith("Icon")) {
                    Icon icon = UIManager.getIcon(key);
                    if (icon != null) {
                        JLabel iconLabel = new JLabel(icon);
                        iconLabel.setToolTipText((String) key);
                        panel.add(iconLabel);

                        JTextField keyField = new JTextField((String) key);
                        keyField.setEditable(false);
                        keyField.addMouseListener(new CopyMouseListener());
                        panel.add(keyField);
                    }
                }
            }

            JScrollPane scrollPane = new JScrollPane(panel);
            frame.add(scrollPane);

            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static class CopyMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                JTextField textField = (JTextField) e.getSource();
                String text = textField.getText();
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(text), null);
            }
        }
    }
}