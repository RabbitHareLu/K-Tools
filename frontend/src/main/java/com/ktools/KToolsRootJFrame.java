package com.ktools;

import com.ktools.common.utils.FontUtil;
import com.ktools.component.ImageLoad;
import com.ktools.component.Menu;
import com.ktools.component.Tree;
import lombok.Data;

import javax.swing.*;
import java.awt.*;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年11月30日 12:44
 */
@Data
public class KToolsRootJFrame extends JFrame {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    public static final String TITLE = "K-Tools-V1.0.0";

    JMenuBar jMenuBar;
    JSplitPane rootJSplitPane;
    JTree jTree;

    public KToolsRootJFrame() {
        setIconImage(ImageLoad.getInstance().getLogoIcon().getImage());
        setSize(WIDTH, HEIGHT);
        setTitle(TITLE);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        init();

        setVisible(true);
    }

    private void init() {
        jMenuBar = Menu.getInstance().getJMenuBar();
        add(jMenuBar, BorderLayout.NORTH);

        rootJSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        rootJSplitPane.setDividerSize(3);

        add(rootJSplitPane, BorderLayout.CENTER);

        new SwingWorker<>() {
            @Override
            protected Object doInBackground() throws Exception {
                jTree = Tree.getInstance().getJTree();
                return null;
            }

            @Override
            protected void done() {
                JScrollPane jTreeScrollPane = new JScrollPane(jTree);
                jTreeScrollPane.setMinimumSize(new Dimension(200, 0));
                rootJSplitPane.setLeftComponent(jTreeScrollPane);

                JScrollPane jTextAreaScrollPane = new JScrollPane(new JTextArea());
                jTextAreaScrollPane.setMinimumSize(new Dimension(200, 0));

                rootJSplitPane.setRightComponent(jTextAreaScrollPane);
                rootJSplitPane.setDividerLocation(0.2);

                FontUtil.updateFont();
            }
        }.execute();
    }

}
