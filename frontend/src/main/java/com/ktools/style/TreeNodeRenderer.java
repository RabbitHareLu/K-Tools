package com.ktools.style;

import com.ktools.component.TreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月01日 11:08
 */
public class TreeNodeRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        TreeNode treeNode = (TreeNode) value;

        this.setIcon(treeNode.getIcon());
        this.setText(String.valueOf(treeNode.getUserObject()));

        return this;
    }
}
