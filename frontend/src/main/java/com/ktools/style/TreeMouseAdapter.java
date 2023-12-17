package com.ktools.style;

import com.ktools.common.model.TreeNodeType;
import com.ktools.component.AllJPopupMenu;
import com.ktools.component.TreeNode;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月17日 13:49
 */
public class TreeMouseAdapter extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {
        JTree jTree = (JTree) e.getSource();

        // 判断是否为鼠标左键点击
        if (SwingUtilities.isLeftMouseButton(e)) {
            int x = e.getX();
            int y = e.getY();

            TreePath path = jTree.getClosestPathForLocation(x, y);
            Rectangle pathBounds = jTree.getPathBounds(path);
            if (Objects.nonNull(pathBounds)) {
                if (y >= pathBounds.getY() && y <= (pathBounds.getY() + pathBounds.getHeight())) {

                } else {
                    jTree.setSelectionPath(new TreePath(jTree.getModel().getRoot()));
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JTree jTree = (JTree) e.getSource();

        // 判断是否为鼠标右键点击
        if (SwingUtilities.isRightMouseButton(e)) {
            int x = e.getX();
            int y = e.getY();

            TreePath path = jTree.getClosestPathForLocation(x, y);
            Rectangle pathBounds = jTree.getPathBounds(path);

            if (Objects.nonNull(pathBounds)) {
                if (y >= pathBounds.getY() && y <= (pathBounds.getY() + pathBounds.getHeight())) {
                    // 如果在树的节点上点击
                    jTree.setSelectionPath(path);
                    TreeNode currentTreeNode = (TreeNode) path.getLastPathComponent();

                    if (Objects.equals(currentTreeNode.getTreeEntity().getNodeType(), TreeNodeType.FOLDER)) {
                        AllJPopupMenu.getInstance().getFolderPopupMenu().show(jTree, x, y);
                    }
                } else {
                    // 如果在树的空白处点击
                    jTree.setSelectionPath(new TreePath(jTree.getModel().getRoot()));
                    AllJPopupMenu.getInstance().getRootPopupMenu().show(jTree, x, y);
                }
            } else {
                // 当前菜单没有任何节点
                jTree.setSelectionPath(new TreePath(jTree.getModel().getRoot()));
                AllJPopupMenu.getInstance().getRootPopupMenu().show(jTree, x, y);
            }
        }
    }
}
