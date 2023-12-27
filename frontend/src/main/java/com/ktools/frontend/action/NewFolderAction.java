package com.ktools.frontend.action;

import com.ktools.warehouse.KToolsContext;
import com.ktools.frontend.Main;
import com.ktools.warehouse.api.SystemApi;
import com.ktools.frontend.common.model.TreeNodeType;
import com.ktools.frontend.common.utils.DialogUtil;
import com.ktools.warehouse.common.utils.StringUtil;
import com.ktools.frontend.component.Tree;
import com.ktools.frontend.component.TreeNode;
import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.manager.uid.UidKey;
import com.ktools.warehouse.mybatis.entity.TreeEntity;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月01日 10:50
 */
@Slf4j
public class NewFolderAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Tree instance = Tree.getInstance();
        TreePath selectionPath = instance.getCurrentTreePath();
        TreeNode currentTreeNode = instance.getCurrentTreeNode(selectionPath);

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
            instance.buildTreeNodePath(nodePathList, selectionPath);
            treeEntity.setNodePath(instance.getNodePathString(nodePathList));

            try {
                KToolsContext.getInstance().getApi(SystemApi.class).addNode(treeEntity);
            } catch (KToolException ex) {
                DialogUtil.showErrorDialog(Main.kToolsRootJFrame, ex.getMessage());
                throw new RuntimeException(ex);
            }

            TreeNode treeNode = new TreeNode(treeEntity);
            DefaultTreeModel model = (DefaultTreeModel) instance.getTreeModel();
            model.insertNodeInto(treeNode, currentTreeNode, currentTreeNode.getChildCount());
            instance.expandTreeNode(selectionPath);
        }
    }
}


