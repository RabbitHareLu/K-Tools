package com.ktools.frontend.action;

import com.ktools.warehouse.KToolsContext;
import com.ktools.frontend.Main;
import com.ktools.warehouse.api.SystemApi;
import com.ktools.frontend.component.Tree;
import com.ktools.frontend.component.TreeNode;
import com.ktools.warehouse.mybatis.entity.TreeEntity;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月17日 14:19
 */
@Slf4j
public class DeleteTreeNodeAction implements ActionListener {

    JTree jTree = Tree.getInstance().getJTree();

    @Override
    public void actionPerformed(ActionEvent e) {
        TreePath selectionPath = jTree.getSelectionPath();
        if (Objects.nonNull(selectionPath)) {
            int result = JOptionPane.showConfirmDialog(Main.kToolsRootJFrame, new Object[]{"是否删除当前节点"}, "删除", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                TreeNode currentTreeNode = (TreeNode) selectionPath.getLastPathComponent();
                log.info("删除节点: {}", currentTreeNode.getUserObject());

                TreeEntity treeEntity = currentTreeNode.getTreeEntity();
                KToolsContext.getInstance().getApi(SystemApi.class).deleteNode(treeEntity);

                DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
                model.removeNodeFromParent(currentTreeNode);
            }
        }

    }
}
