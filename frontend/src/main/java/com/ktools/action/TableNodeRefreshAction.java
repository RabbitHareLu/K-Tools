package com.ktools.action;

import com.ktools.KToolsContext;
import com.ktools.Main;
import com.ktools.api.DataSourceApi;
import com.ktools.api.SystemApi;
import com.ktools.common.model.TreeNodeType;
import com.ktools.common.utils.CollectionUtil;
import com.ktools.common.utils.DialogUtil;
import com.ktools.component.Tree;
import com.ktools.component.TreeNode;
import com.ktools.exception.KToolException;
import com.ktools.manager.datasource.jdbc.model.TableColumn;
import com.ktools.manager.datasource.jdbc.model.TableMetadata;
import com.ktools.manager.uid.UidKey;
import com.ktools.mybatis.entity.TreeEntity;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月22日 16:14
 */
@Slf4j
public class TableNodeRefreshAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        sync();
    }

    private void sync() {
        Tree instance = Tree.getInstance();
        TreePath selectionPath = instance.getCurrentTreePath();
        TreeNode currentTreeNode = instance.getCurrentTreeNode(selectionPath);

        TreeNode schemaNode = instance.getCurrentTreeNode(new TreePath(currentTreeNode.getParent()));
        TreeEntity schemaTreeEntity = schemaNode.getTreeEntity();

        TreeNode jdbcNode = instance.getCurrentTreeNode(new TreePath(currentTreeNode.getParent().getParent()));
        TreeEntity jdbcTreeEntity = jdbcNode.getTreeEntity();
        TreeEntity treeEntity = currentTreeNode.getTreeEntity();


        try {
            KToolsContext.getInstance().getApi(DataSourceApi.class).conn(String.valueOf(jdbcTreeEntity.getId()), jdbcTreeEntity.getNodeType(), jdbcTreeEntity.getNodeInfo());

            new SwingWorker<Void, Integer>() {

                @Override
                protected Void doInBackground() {
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
                        TableMetadata tableMetadata = KToolsContext.getInstance().getApi(DataSourceApi.class).selectTableMetadata(String.valueOf(jdbcNode.getTreeEntity().getId()), schemaTreeEntity.getNodeName(), treeEntity.getNodeName());
                        Map<String, TableColumn> columnsMap = tableMetadata.getColumns();
                        if (CollectionUtil.isNotEmpty(columnsMap)) {
                            for (Map.Entry<String, TableColumn> stringTableColumnEntry : columnsMap.entrySet()) {
                                TreeEntity newTreeEntity = new TreeEntity();
                                newTreeEntity.setId(KToolsContext.getInstance().getIdGenerator().getId(UidKey.TREE));
                                newTreeEntity.setParentNodeId(treeEntity.getId());

                                StringBuilder name = new StringBuilder(stringTableColumnEntry.getKey());
                                name.append("   ");
                                TableColumn tableColumn = stringTableColumnEntry.getValue();
                                String sqlType = tableColumn.getDataType().getName();
                                name.append(sqlType).append("(");
                                if (Objects.equals(sqlType, "DECIMAL") || Objects.equals(sqlType, "TIMESTAMP")) {
                                    name.append(tableColumn.getLength()).append(",").append(tableColumn.getPrecision());
                                } else {
                                    name.append(tableColumn.getLength());
                                }
                                name.append(")");

                                newTreeEntity.setNodeName(name.toString());
                                newTreeEntity.setNodeType(TreeNodeType.COLUMN);
                                List<String> nodePathList = new ArrayList<>();
                                instance.buildTreeNodePath(nodePathList, selectionPath);
                                newTreeEntity.setNodePath(instance.getNodePathString(nodePathList));
                                newTreeEntity.setNodeInfo(null);
                                newTreeEntity.setChild(null);

                                KToolsContext.getInstance().getApi(SystemApi.class).addNode(newTreeEntity);

                                TreeNode treeNode = new TreeNode(newTreeEntity);
                                currentTreeNode.add(treeNode);
                            }
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

        TreeNode schemaNode = instance.getCurrentTreeNode(new TreePath(currentTreeNode.getParent()));
        TreeEntity schemaTreeEntity = schemaNode.getTreeEntity();

        TreeNode jdbcNode = instance.getCurrentTreeNode(new TreePath(currentTreeNode.getParent().getParent()));
        TreeEntity jdbcTreeEntity = jdbcNode.getTreeEntity();
        TreeEntity treeEntity = currentTreeNode.getTreeEntity();

        try {
            KToolsContext.getInstance().getApi(DataSourceApi.class).conn(String.valueOf(jdbcTreeEntity.getId()), jdbcTreeEntity.getNodeType(), jdbcTreeEntity.getNodeInfo());

            if (currentTreeNode.getChildCount() > 0) {
                KToolsContext.getInstance().getApi(SystemApi.class).deleteChildNode(treeEntity);
                instance.deleteTreeChildNode(currentTreeNode);
            }
            TableMetadata tableMetadata = KToolsContext.getInstance().getApi(DataSourceApi.class).selectTableMetadata(String.valueOf(jdbcNode.getTreeEntity().getId()), schemaTreeEntity.getNodeName(), treeEntity.getNodeName());
            Map<String, TableColumn> columnsMap = tableMetadata.getColumns();
            if (CollectionUtil.isNotEmpty(columnsMap)) {

                for (Map.Entry<String, TableColumn> stringTableColumnEntry : columnsMap.entrySet()) {
                    TreeEntity newTreeEntity = new TreeEntity();
                    newTreeEntity.setId(KToolsContext.getInstance().getIdGenerator().getId(UidKey.TREE));
                    newTreeEntity.setParentNodeId(treeEntity.getId());

                    StringBuilder name = new StringBuilder(stringTableColumnEntry.getKey());
                    name.append("   ");
                    TableColumn tableColumn = stringTableColumnEntry.getValue();
                    String sqlType = tableColumn.getDataType().getName();
                    name.append(sqlType).append("(");
                    if (Objects.equals(sqlType, "DECIMAL") || Objects.equals(sqlType, "TIMESTAMP")) {
                        name.append(tableColumn.getLength()).append(",").append(tableColumn.getPrecision());
                    } else {
                        name.append(tableColumn.getLength());
                    }
                    name.append(")");

                    newTreeEntity.setNodeName(name.toString());
                    newTreeEntity.setNodeType(TreeNodeType.COLUMN);
                    List<String> nodePathList = new ArrayList<>();
                    instance.buildTreeNodePath(nodePathList, selectionPath);
                    newTreeEntity.setNodePath(instance.getNodePathString(nodePathList));
                    newTreeEntity.setNodeInfo(null);
                    newTreeEntity.setChild(null);

                    KToolsContext.getInstance().getApi(SystemApi.class).addNode(newTreeEntity);

                    TreeNode treeNode = new TreeNode(newTreeEntity);
                    currentTreeNode.add(treeNode);
                }
            }


        } catch (KToolException e) {
            DialogUtil.showErrorDialog(Main.kToolsRootJFrame, e.getMessage());
            throw new RuntimeException(e);
        }
        instance.getDefaultTreeModel().nodeStructureChanged(currentTreeNode);
        instance.expandTreeNode(selectionPath);
    }
}
