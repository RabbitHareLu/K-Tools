package com.ktools.action;

import com.ktools.KToolsContext;
import com.ktools.Main;
import com.ktools.api.SystemApi;
import com.ktools.common.model.TreeNodeType;
import com.ktools.common.utils.DialogUtil;
import com.ktools.common.utils.StringUtil;
import com.ktools.component.Tree;
import com.ktools.component.TreeNode;
import com.ktools.exception.KToolException;
import com.ktools.manager.uid.UidKey;
import com.ktools.mybatis.entity.TreeEntity;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月01日 10:50
 */
@Slf4j
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
                JOptionPane.INFORMATION_MESSAGE
        );

        if (StringUtil.isNotBlank(result)) {
            log.info("新建文件夹: {}", result);

            TreeEntity treeEntity = new TreeEntity();
            treeEntity.setId(KToolsContext.getInstance().getIdGenerator().getId(UidKey.TREE));
            treeEntity.setParentNodeId(currentTreeNode.getTreeEntity().getId());
            treeEntity.setNodeName(result);
            treeEntity.setNodeType(TreeNodeType.FOLDER);
            treeEntity.setNodeComment(null);
            treeEntity.setChild(null);

            List<String> nodePathList = new ArrayList<>();
            buildTreeNodePath(nodePathList, selectionPath);
            treeEntity.setNodePath(getNodePathString(nodePathList));

            try {
                KToolsContext.getInstance().getApi(SystemApi.class).addNode(treeEntity);
            } catch (KToolException ex) {
                DialogUtil.showErrorDialog(Main.kToolsRootJFrame, ex.getMessage());
                throw new RuntimeException(ex);
            }

            TreeNode treeNode = new TreeNode(treeEntity);
            DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
            model.insertNodeInto(treeNode, currentTreeNode, currentTreeNode.getChildCount());
            expandTreeNode(selectionPath);
        }
    }

    private void expandTreeNode(TreePath selectionPath) {
        if (Objects.nonNull(selectionPath)) {
            if (!jTree.isExpanded(selectionPath)) {
                jTree.expandPath(selectionPath);
            }
        }
    }

    private String getNodePathString(List<String> nodePathList) {
        StringBuilder nodePathString = new StringBuilder();
        for (int i = nodePathList.size() - 1; i >= 0; i--) {
            nodePathString.append(nodePathList.get(i)).append("/");
        }
        return nodePathString.delete(nodePathString.length() - 1, nodePathString.length()).toString();
    }

    private void buildTreeNodePath(List<String> list, TreePath selectionPath) {
        TreeNode currentTreeNode = (TreeNode) selectionPath.getLastPathComponent();
        Integer id = currentTreeNode.getTreeEntity().getId();
        list.add(String.valueOf(id));

        TreePath parentPath = selectionPath.getParentPath();
        if (Objects.nonNull(parentPath)) {
            buildTreeNodePath(list, parentPath);
        }
    }
}


