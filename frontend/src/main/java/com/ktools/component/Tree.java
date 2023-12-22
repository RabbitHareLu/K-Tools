package com.ktools.component;

import com.ktools.KToolsContext;
import com.ktools.api.SystemApi;
import com.ktools.common.model.TreeNodeType;
import com.ktools.common.utils.CollectionUtil;
import com.ktools.mybatis.entity.TreeEntity;
import com.ktools.style.TreeMouseAdapter;
import com.ktools.style.TreeNodeRenderer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.List;
import java.util.Objects;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年11月30日 19:38
 */
@Data
@Slf4j
public class Tree {
    private static final Tree INSTANCE = new Tree();

    private JTree jTree;

    private DefaultTreeModel defaultTreeModel;

    private Tree() {
        TreeNode root = initTree();

        defaultTreeModel = new DefaultTreeModel(root);
        jTree = new JTree(defaultTreeModel);
        jTree.setShowsRootHandles(true);
        jTree.setCellRenderer(new TreeNodeRenderer());
        jTree.setRootVisible(false);
        jTree.addMouseListener(new TreeMouseAdapter());
    }

    public static Tree getInstance() {
        return INSTANCE;
    }

    private TreeNode initTree() {
        SystemApi api = KToolsContext.getInstance().getApi(SystemApi.class);
        List<TreeEntity> tree = api.getTree(0);

        TreeNode rootNode = new TreeNode(0, null, "ROOT", TreeNodeType.ROOT, "ROOT");
        buildTree(rootNode, tree.getFirst().getChild());
        return rootNode;
    }

    private void buildTree(TreeNode parentNode, List<TreeEntity> children) {
        if (CollectionUtil.isNotEmpty(children)) {
            for (TreeEntity child : children) {
                TreeNode treeNode = new TreeNode(child);
                parentNode.add(treeNode);
                buildTree(treeNode, child.getChild());
            }
        }
    }


    public void expandTreeNode(TreePath selectionPath) {
        if (Objects.nonNull(selectionPath)) {
            if (!jTree.isExpanded(selectionPath)) {
                jTree.expandPath(selectionPath);
            }
        }
    }

    public TreeModel getTreeModel() {
        return jTree.getModel();
    }

    public DefaultTreeModel getDefaultTreeModel() {
        return (DefaultTreeModel) jTree.getModel();
    }

    public TreePath getCurrentTreePath() {
        TreePath selectionPath = jTree.getSelectionPath();

        // 如果selectionPath为null, 说明未选择任何节点, 因此直接默认在根节点的目录下创建
        if (Objects.isNull(selectionPath)) {
            selectionPath = new TreePath(jTree.getModel().getRoot());
        }
        return selectionPath;
    }

    public TreeNode getCurrentTreeNode() {
        JTree jTree = Tree.getInstance().getJTree();
        TreePath selectionPath = jTree.getSelectionPath();

        // 如果selectionPath为null, 说明未选择任何节点, 因此直接默认在根节点的目录下创建
        if (Objects.isNull(selectionPath)) {
            selectionPath = new TreePath(jTree.getModel().getRoot());
        }
        return (TreeNode) selectionPath.getLastPathComponent();
    }

    public TreeNode getCurrentTreeNode(TreePath treePath) {
        return (TreeNode) treePath.getLastPathComponent();
    }


    public TreeEntity getCurrentTreeEntity() {
        JTree jTree = Tree.getInstance().getJTree();
        TreePath selectionPath = jTree.getSelectionPath();

        // 如果selectionPath为null, 说明未选择任何节点, 因此直接默认在根节点的目录下创建
        if (Objects.isNull(selectionPath)) {
            selectionPath = new TreePath(jTree.getModel().getRoot());
        }
        return ((TreeNode) selectionPath.getLastPathComponent()).getTreeEntity();
    }

    public TreeEntity getCurrentTreeEntity(TreePath treePath) {
        return ((TreeNode) treePath.getLastPathComponent()).getTreeEntity();
    }

    public TreeEntity getCurrentTreeEntity(TreeNode treeNode) {
        return treeNode.getTreeEntity();
    }

    public String getNodePathString(List<String> nodePathList) {
        StringBuilder nodePathString = new StringBuilder();
        for (int i = nodePathList.size() - 1; i >= 0; i--) {
            nodePathString.append(nodePathList.get(i)).append("/");
        }
        return nodePathString.delete(nodePathString.length() - 1, nodePathString.length()).toString();
    }

    public void buildTreeNodePath(List<String> list, TreePath selectionPath) {
        TreeNode currentTreeNode = (TreeNode) selectionPath.getLastPathComponent();
        Integer id = currentTreeNode.getTreeEntity().getId();
        list.add(String.valueOf(id));

        TreePath parentPath = selectionPath.getParentPath();
        if (Objects.nonNull(parentPath)) {
            buildTreeNodePath(list, parentPath);
        }
    }

    public void deleteTreeChildNode(TreeNode currentTreeNode) {
        currentTreeNode.removeAllChildren();
        getDefaultTreeModel().nodeStructureChanged(currentTreeNode);
    }
}
