package com.ktools;

import com.ktools.component.Menu;
import com.ktools.component.Tree;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

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
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/logo/kts.png")));
        setIconImage(imageIcon.getImage());

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
                rootJSplitPane.setLeftComponent(new JScrollPane(jTree));
                rootJSplitPane.setRightComponent(new JScrollPane(new JTextArea()));
                rootJSplitPane.setDividerLocation(0.2);
            }
        }.execute();
    }

}
