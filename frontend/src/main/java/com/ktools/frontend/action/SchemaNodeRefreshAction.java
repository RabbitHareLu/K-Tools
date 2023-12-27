package com.ktools.frontend.action;

import com.ktools.warehouse.KToolsContext;
import com.ktools.frontend.Main;
import com.ktools.warehouse.api.DataSourceApi;
import com.ktools.warehouse.api.SystemApi;
import com.ktools.frontend.common.model.TreeNodeType;
import com.ktools.frontend.common.utils.DialogUtil;
import com.ktools.frontend.component.Tree;
import com.ktools.frontend.component.TreeNode;
import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.manager.uid.UidKey;
import com.ktools.warehouse.mybatis.entity.TreeEntity;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月22日 15:19
 */
public class SchemaNodeRefreshAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        sync();
    }

    private void sync() {
        Tree instance = Tree.getInstance();
        TreePath selectionPath = instance.getCurrentTreePath();
        TreeNode currentTreeNode = instance.getCurrentTreeNode(selectionPath);
        TreeNode jdbcNode = instance.getCurrentTreeNode(new TreePath(currentTreeNode.getParent()));
        TreeEntity jdbcTreeEntity = jdbcNode.getTreeEntity();
        TreeEntity treeEntity = currentTreeNode.getTreeEntity();

        try {
            KToolsContext.getInstance().getApi(DataSourceApi.class).conn(String.valueOf(jdbcTreeEntity.getId()), jdbcTreeEntity.getNodeType(), jdbcTreeEntity.getNodeInfo());

            new SwingWorker<Void, Integer>(){

                @Override
                protected Void doInBackground() throws Exception {
                    publish(currentTreeNode.getChildCount());
                    return null;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    if (!chunks.isEmpty()) {
                        KToolsContext.getInstance().getApi(SystemApi.class).deleteChildNode(treeEntity);
                        instance.deleteTreeChildNode(currentTreeNode);
                    }
                }

                @Override
                protected void done() {
                    try {
                        List<String> tableNameList = KToolsContext.getInstance().getApi(DataSourceApi.class).selectAllTable(String.valueOf(jdbcNode.getTreeEntity().getId()), treeEntity.getNodeName());
                        for (String tableName : tableNameList) {
                            TreeEntity newTreeEntity = new TreeEntity();
                            newTreeEntity.setId(KToolsContext.getInstance().getIdGenerator().getId(UidKey.TREE));
                            newTreeEntity.setParentNodeId(treeEntity.getId());
                            newTreeEntity.setNodeName(tableName);
                            newTreeEntity.setNodeType(TreeNodeType.TABLE);
                            List<String> nodePathList = new ArrayList<>();
                            instance.buildTreeNodePath(nodePathList, selectionPath);
                            newTreeEntity.setNodePath(instance.getNodePathString(nodePathList));
                            newTreeEntity.setNodeInfo(null);
                            newTreeEntity.setChild(null);

                            KToolsContext.getInstance().getApi(SystemApi.class).addNode(newTreeEntity);

                            TreeNode treeNode = new TreeNode(newTreeEntity);
                            currentTreeNode.add(treeNode);
                        }
                    } catch (KToolException e) {
                        throw new RuntimeException(e);
                    }
                    instance.getDefaultTreeModel().nodeStructureChanged(currentTreeNode);
                    instance.expandTreeNode(selectionPath);
                }
            }.execute();

        } catch (KToolException e) {
            DialogUtil.showErrorDialog(Main.kToolsRootJFrame, e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private void async() {
        Tree instance = Tree.getInstance();
        TreePath selectionPath = instance.getCurrentTreePath();
        TreeNode currentTreeNode = instance.getCurrentTreeNode(selectionPath);
        TreeNode jdbcNode = instance.getCurrentTreeNode(new TreePath(currentTreeNode.getParent()));
        TreeEntity jdbcTreeEntity = jdbcNode.getTreeEntity();
        TreeEntity treeEntity = currentTreeNode.getTreeEntity();

        try {
            KToolsContext.getInstance().getApi(DataSourceApi.class).conn(String.valueOf(jdbcTreeEntity.getId()), jdbcTreeEntity.getNodeType(), jdbcTreeEntity.getNodeInfo());

            if (currentTreeNode.getChildCount() > 0) {
                KToolsContext.getInstance().getApi(SystemApi.class).deleteChildNode(treeEntity);
                instance.deleteTreeChildNode(currentTreeNode);
            }
            List<String> tableNameList = KToolsContext.getInstance().getApi(DataSourceApi.class).selectAllTable(String.valueOf(jdbcNode.getTreeEntity().getId()), treeEntity.getNodeName());
            for (String tableName : tableNameList) {
                TreeEntity newTreeEntity = new TreeEntity();
                newTreeEntity.setId(KToolsContext.getInstance().getIdGenerator().getId(UidKey.TREE));
                newTreeEntity.setParentNodeId(treeEntity.getId());
                newTreeEntity.setNodeName(tableName);
                newTreeEntity.setNodeType(TreeNodeType.TABLE);
                List<String> nodePathList = new ArrayList<>();
                instance.buildTreeNodePath(nodePathList, selectionPath);
                newTreeEntity.setNodePath(instance.getNodePathString(nodePathList));
                newTreeEntity.setNodeInfo(null);
                newTreeEntity.setChild(null);

                KToolsContext.getInstance().getApi(SystemApi.class).addNode(newTreeEntity);

                TreeNode treeNode = new TreeNode(newTreeEntity);
                currentTreeNode.add(treeNode);
            }

        } catch (KToolException e) {
            DialogUtil.showErrorDialog(Main.kToolsRootJFrame, e.getMessage());
            throw new RuntimeException(e);
        }
        instance.getDefaultTreeModel().nodeStructureChanged(currentTreeNode);
        instance.expandTreeNode(selectionPath);
    }
}
