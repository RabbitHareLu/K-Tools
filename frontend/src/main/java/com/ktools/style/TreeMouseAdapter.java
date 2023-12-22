package com.ktools.style;

import com.ktools.Main;
import com.ktools.common.model.TreeNodeType;
import com.ktools.component.AllJPopupMenu;
import com.ktools.component.Tree;
import com.ktools.component.TreeNode;
import com.ktools.mybatis.entity.TreeEntity;
import com.ktools.panel.TableDataPanel;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import static com.formdev.flatlaf.FlatClientProperties.TABBED_PANE_TAB_CLOSABLE;
import static com.formdev.flatlaf.FlatClientProperties.TABBED_PANE_TAB_CLOSE_TOOLTIPTEXT;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月17日 13:49
 */
@Slf4j
public class TreeMouseAdapter extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {
        JTree jTree = (JTree) e.getSource();

        // 判断鼠标左键双击
        if (e.getClickCount() == 2) {
            log.info("鼠标左键双击");
            Tree instance = Tree.getInstance();
            TreePath selectionPath = instance.getCurrentTreePath();
            TreeNode currentTreeNode = instance.getCurrentTreeNode(selectionPath);
            TreeEntity treeEntity = currentTreeNode.getTreeEntity();
            if (Objects.equals(treeEntity.getNodeType(), TreeNodeType.TABLE)) {
                JTabbedPane closableTabsTabbedPane = Main.kToolsRootJFrame.getClosableTabsTabbedPane();
                if (Objects.isNull(closableTabsTabbedPane)) {
                    JTabbedPane tabbedPane = new JTabbedPane();
                    tabbedPane.putClientProperty(TABBED_PANE_TAB_CLOSABLE, true);
                    tabbedPane.putClientProperty(TABBED_PANE_TAB_CLOSE_TOOLTIPTEXT, "Close");
                    tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                    Main.kToolsRootJFrame.setClosableTabsTabbedPane(tabbedPane);
                    Main.kToolsRootJFrame.getRootJSplitPane().setRightComponent(Main.kToolsRootJFrame.getClosableTabsTabbedPane());
                    closableTabsTabbedPane = Main.kToolsRootJFrame.getClosableTabsTabbedPane();
                }
                closableTabsTabbedPane.add(treeEntity.getNodeName(), new TableDataPanel(selectionPath));
            } else {
                // 单独处理鼠标双击事件，如果节点是table则双击事件默认为打开一个tab页，其他节点默认展开
                if (jTree.isExpanded(selectionPath)) {
                    jTree.collapsePath(selectionPath);
                } else {
                    jTree.expandPath(selectionPath);
                }
            }
        }

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
                    } else if (Objects.equals(currentTreeNode.getTreeEntity().getNodeType(), TreeNodeType.SCHEMA)) {
                        AllJPopupMenu.getInstance().getSchemaPopupMenu().show(jTree, x, y);
                    } else if (Objects.equals(currentTreeNode.getTreeEntity().getNodeType(), TreeNodeType.TABLE)) {
                        AllJPopupMenu.getInstance().getTablePopupMenu().show(jTree, x, y);
                    } else {
                        AllJPopupMenu.getInstance().getJdbcPopupMenu().show(jTree, x, y);
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
