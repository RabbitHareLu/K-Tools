package com.ktools.frame;

import com.ktools.action.TestConnectionAction;
import com.ktools.common.utils.BoxUtil;
import com.ktools.component.ImageLoad;
import com.ktools.manager.datasource.model.KDataSourceMetadata;
import com.ktools.panel.AdvancedPanel;
import com.ktools.panel.RegularPanel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月18日 17:34
 */
@Slf4j
@Data
public class NewJDBCConnectionFrame extends JFrame {

    public static boolean openFlag = true;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 700;
    private Map<String, Object> componentMap = new HashMap<>();
    private KDataSourceMetadata kDataSourceMetadata;

    private RegularPanel regularPanel;
    private AdvancedPanel advancedPanel;

    public NewJDBCConnectionFrame() {
    }

    public NewJDBCConnectionFrame(KDataSourceMetadata kDataSourceMetadata) {
        openFlag = false;
        this.kDataSourceMetadata = kDataSourceMetadata;

        setIconImage(ImageLoad.getInstance().buildIcon(this.getClass().getResource(kDataSourceMetadata.getLogo())).getImage());

        setTitle("新建" + kDataSourceMetadata.getName() + "连接");
        setSize(WIDTH, HEIGHT);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        init();
        setResizable(false);
        setVisible(true);

        closeAction();
    }

    private void init() {
        Box publicBox = Box.createVerticalBox();
        BoxUtil.addVerticalStrut(publicBox, 10);
        initSubBox(publicBox, "名称: ");
        BoxUtil.addVerticalStrut(publicBox, 30);
        initSubBox(publicBox, "注释: ");
        BoxUtil.addVerticalStrut(publicBox, 30);
        add(publicBox, BorderLayout.NORTH);

        Box tabBox = Box.createHorizontalBox();
        BoxUtil.addHorizontalStrut(tabBox, 30);
        JTabbedPane tabbedPane = new JTabbedPane();
        RegularPanel regularPanel = new RegularPanel();
        AdvancedPanel advancedPanel = new AdvancedPanel();
        tabbedPane.addTab("常规", null, regularPanel, "常规");
        tabbedPane.addTab("高级", null, advancedPanel, "高级");
        tabBox.add(tabbedPane);
        BoxUtil.addHorizontalStrut(tabBox, 30);
        add(tabBox, BorderLayout.CENTER);


        Box southBox = Box.createVerticalBox();

        Box buttonBox = Box.createHorizontalBox();
        BoxUtil.addHorizontalStrut(buttonBox, 30);
        JButton testButton = new JButton("测试连接");
        testButton.addActionListener(new TestConnectionAction(regularPanel.getComponentMap(), kDataSourceMetadata.getName()));
        buttonBox.add(testButton);
        buttonBox.add(Box.createHorizontalGlue());
        JButton okButton = new JButton("确认");
        okButton.setBackground(new Color(53, 116, 240));
        buttonBox.add(okButton);
        BoxUtil.addHorizontalStrut(buttonBox, 20);
        JButton cancelButton = new JButton("取消");
        buttonBox.add(cancelButton);
        BoxUtil.addHorizontalStrut(buttonBox, 30);

        southBox.add(buttonBox);
        BoxUtil.addVerticalStrut(southBox, 30);

        add(southBox, BorderLayout.SOUTH);
    }

    private void initSubBox(Box rootBox, String key) {
        Box keyBox = Box.createHorizontalBox();
        BoxUtil.addHorizontalStrut(keyBox, 30);

        JLabel keyLabel = new JLabel(key);
        Dimension dimension = keyLabel.getPreferredSize();
        double fixedWidth = 60;
        double height = dimension.getHeight();
        dimension.setSize(fixedWidth, height);
        keyLabel.setPreferredSize(dimension);
        keyLabel.setToolTipText(key);
        keyBox.add(keyLabel);

        BoxUtil.addHorizontalStrut(keyBox, 20);

        JTextField valueInputField = new JTextField();
        keyBox.add(valueInputField);

        BoxUtil.addHorizontalStrut(keyBox, 100);
        rootBox.add(keyBox);
        componentMap.put(key, valueInputField);
    }

    private void closeAction() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                openFlag = true;
            }
        });
    }

}
