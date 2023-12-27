package com.ktools.frontend.frame;

import com.ktools.warehouse.KToolsContext;
import com.ktools.frontend.action.CancelDisposeFrameAction;
import com.ktools.frontend.action.TestConnectionAction;
import com.ktools.frontend.action.TreeJDBCNodeAction;
import com.ktools.warehouse.api.DataSourceApi;
import com.ktools.frontend.common.utils.BoxUtil;
import com.ktools.frontend.component.ImageLoad;
import com.ktools.frontend.component.Tree;
import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.manager.datasource.model.KDataSourceMetadata;
import com.ktools.warehouse.mybatis.entity.TreeEntity;
import com.ktools.frontend.panel.AdvancedPanel;
import com.ktools.frontend.panel.RegularPanel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Objects;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月18日 17:34
 */
@Slf4j
@Data
public class JDBCConnectionFrame extends JFrame {

    public static boolean openFlag = true;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 700;

    private KDataSourceMetadata kDataSourceMetadata;
    private TreeEntity treeEntity;
    private TreePath treePath;

    private RegularPanel regularPanel;
    private AdvancedPanel advancedPanel;

    private JTextField nameField;
    private JTextField commentField;

    public JDBCConnectionFrame() {
        Tree instance = Tree.getInstance();
        this.treePath = instance.getCurrentTreePath();
        this.treeEntity = instance.getCurrentTreeEntity(treePath);
        try {
            this.kDataSourceMetadata = KToolsContext.getInstance().getApi(DataSourceApi.class).getMetadata(treeEntity.getNodeType());
        } catch (KToolException e) {
            throw new RuntimeException(e);
        }
        setTitle("编辑" + kDataSourceMetadata.getName() + "连接");
        frameStartInit();
        initEdit();
        frameEndInit();
    }

    public JDBCConnectionFrame(KDataSourceMetadata kDataSourceMetadata) {
        Tree instance = Tree.getInstance();
        this.kDataSourceMetadata = kDataSourceMetadata;
        this.treePath = instance.getCurrentTreePath();
        setTitle("新建" + kDataSourceMetadata.getName() + "连接");
        frameStartInit();
        initNew();
        frameEndInit();
    }

    private void frameStartInit() {
        openFlag = false;
        setIconImage(ImageLoad.getInstance().buildIcon(kDataSourceMetadata.getLogo()).getImage());
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        closeAction();
    }

    private void frameEndInit() {
        setVisible(true);
    }

    private void initEdit() {
        if (Objects.nonNull(treeEntity)) {
            Box publicBox = Box.createVerticalBox();
            BoxUtil.addVerticalStrut(publicBox, 30);
            nameField = initSubBox(publicBox, "名称: ");
            nameField.setText(treeEntity.getNodeName());
            BoxUtil.addVerticalStrut(publicBox, 30);
            commentField = initSubBox(publicBox, "注释: ");
            commentField.setText(treeEntity.getNodeComment());
            BoxUtil.addVerticalStrut(publicBox, 30);
            add(publicBox, BorderLayout.NORTH);

            Box tabBox = Box.createHorizontalBox();
            BoxUtil.addHorizontalStrut(tabBox, 30);
            JTabbedPane tabbedPane = new JTabbedPane();
            regularPanel = new RegularPanel(treeEntity);
            advancedPanel = new AdvancedPanel(treeEntity, kDataSourceMetadata);
            tabbedPane.addTab("常规", null, regularPanel, "常规");
            tabbedPane.addTab("高级", null, advancedPanel, "高级");
            tabBox.add(tabbedPane);
            BoxUtil.addHorizontalStrut(tabBox, 30);
            add(tabBox, BorderLayout.CENTER);

            Box southBox = Box.createVerticalBox();

            Box buttonBox = Box.createHorizontalBox();
            BoxUtil.addHorizontalStrut(buttonBox, 30);
            JButton testButton = new JButton("测试连接");
            testButton.addActionListener(new TestConnectionAction(this));
            buttonBox.add(testButton);
            buttonBox.add(Box.createHorizontalGlue());
            JButton okButton = new JButton("确认");
            okButton.setBackground(new Color(53, 116, 240));
            okButton.addActionListener(new TreeJDBCNodeAction(this));

            buttonBox.add(okButton);
            BoxUtil.addHorizontalStrut(buttonBox, 20);
            JButton cancelButton = new JButton("取消");
            cancelButton.addActionListener(new CancelDisposeFrameAction());
            buttonBox.add(cancelButton);
            BoxUtil.addHorizontalStrut(buttonBox, 30);

            southBox.add(buttonBox);
            BoxUtil.addVerticalStrut(southBox, 30);

            add(southBox, BorderLayout.SOUTH);
        }
    }

    private void initNew() {
        Box publicBox = Box.createVerticalBox();
        BoxUtil.addVerticalStrut(publicBox, 30);
        nameField = initSubBox(publicBox, "名称: ");
        BoxUtil.addVerticalStrut(publicBox, 30);
        commentField = initSubBox(publicBox, "注释: ");
        BoxUtil.addVerticalStrut(publicBox, 30);
        add(publicBox, BorderLayout.NORTH);

        Box tabBox = Box.createHorizontalBox();
        BoxUtil.addHorizontalStrut(tabBox, 30);
        JTabbedPane tabbedPane = new JTabbedPane();
        regularPanel = new RegularPanel();
        advancedPanel = new AdvancedPanel(kDataSourceMetadata);
        tabbedPane.addTab("常规", null, regularPanel, "常规");
        tabbedPane.addTab("高级", null, advancedPanel, "高级");
        tabBox.add(tabbedPane);
        BoxUtil.addHorizontalStrut(tabBox, 30);
        add(tabBox, BorderLayout.CENTER);

        Box southBox = Box.createVerticalBox();

        Box buttonBox = Box.createHorizontalBox();
        BoxUtil.addHorizontalStrut(buttonBox, 30);
        JButton testButton = new JButton("测试连接");
        testButton.addActionListener(new TestConnectionAction(this));
        buttonBox.add(testButton);
        buttonBox.add(Box.createHorizontalGlue());
        JButton okButton = new JButton("确认");
        okButton.setBackground(new Color(53, 116, 240));
        okButton.addActionListener(new TreeJDBCNodeAction(this));

        buttonBox.add(okButton);
        BoxUtil.addHorizontalStrut(buttonBox, 20);
        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(new CancelDisposeFrameAction());
        buttonBox.add(cancelButton);
        BoxUtil.addHorizontalStrut(buttonBox, 30);

        southBox.add(buttonBox);
        BoxUtil.addVerticalStrut(southBox, 30);

        add(southBox, BorderLayout.SOUTH);
    }

    private JTextField initSubBox(Box rootBox, String key) {
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
        return valueInputField;
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
