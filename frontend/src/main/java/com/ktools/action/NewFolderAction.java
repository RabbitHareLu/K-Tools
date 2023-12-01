package com.ktools.action;

import com.ktools.KToolsContext;
import com.ktools.Main;
import com.ktools.api.SystemApi;
import com.ktools.common.model.TreeNodeType;
import com.ktools.common.utils.StringUtil;
import com.ktools.component.Tree;
import com.ktools.component.TreeNode;
import com.ktools.mybatis.entity.TreeEntity;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月01日 10:50
 */
public class NewFolderAction implements ActionListener {

    JTree jTree = Tree.getInstance().getJTree();

    @Override
    public void actionPerformed(ActionEvent e) {
        TreePath selectionPath = jTree.getSelectionPath();

        // 如果selectionPath为null, 说明未选择任何节点, 因此直接默认在根节点的目录下创建
        if (Objects.isNull(selectionPath)) {
            selectionPath = new TreePath(jTree.getModel().getRoot());
        }

        TreeNode currentTreeNode = (TreeNode) selectionPath.getLastPathComponent();

        String result = JOptionPane.showInputDialog(
                Main.kToolsRootJFrame,
                "目录名称",
                "新建文件夹",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        if (StringUtil.isNotBlank(result)) {
            TreeEntity treeEntity = new TreeEntity();
            treeEntity.setId(10);
            treeEntity.setParentNodeId(currentTreeNode.getTreeEntity().getId());
            treeEntity.setNodeName(result);
            treeEntity.setNodeType(TreeNodeType.FOLDER);
            treeEntity.setNodeComment(null);
            treeEntity.setNodePath("0");
            treeEntity.setChild(null);

            KToolsContext.getInstance().getApi(SystemApi.class).addNode(treeEntity);

            TreeNode treeNode = new TreeNode(treeEntity);
            DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
            model.insertNodeInto(treeNode, currentTreeNode, currentTreeNode.getChildCount());
            expandTreeNode(selectionPath);
        }


    }

    public void expandTreeNode(TreePath selectionPath) {
        if (Objects.nonNull(selectionPath)) {
            if (!jTree.isExpanded(selectionPath)) {
                jTree.expandPath(selectionPath);
            }
        }
    }
}


