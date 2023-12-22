package com.ktools.panel;

import com.ktools.component.ImageLoad;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月22日 17:11
 */
@Slf4j
@Data
public class TableDataPanel extends JPanel {

    private TreePath selectionPath;

    public TableDataPanel() {
    }

    public TableDataPanel(TreePath selectionPath) {
        this.selectionPath = selectionPath;
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        Box rootBox = Box.createVerticalBox();
        add(rootBox);
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable( false );
        jToolBar.setBorder( null );
        JButton refreshButton = new JButton(ImageLoad.getInstance().getTableRefreshIcon());
        jToolBar.add(refreshButton);
        rootBox.add(jToolBar);

    }


}
