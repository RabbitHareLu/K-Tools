package com.ktools.component;

import com.ktools.KToolsContext;
import com.ktools.Main;
import com.ktools.api.SystemApi;
import com.ktools.common.model.TreeNodeType;
import com.ktools.common.utils.CollectionUtil;
import com.ktools.common.utils.DialogUtil;
import com.ktools.model.TreeModel;
import com.ktools.style.TreeNodeRenderer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.util.List;

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
        jTree.setCellRenderer(new TreeNodeRenderer());
    }

    public static Tree getInstance() {
        return INSTANCE;
    }

    public TreeNode initTree() {
        SystemApi api = KToolsContext.getInstance().getApi(SystemApi.class);
        List<TreeModel> tree = api.getTree(0);

        TreeNode rootNode = new TreeNode(ImageLoad.getInstance().getRootIcon(), 0, null, "ROOT", TreeNodeType.ROOT, "ROOT");
        buildTree(rootNode, tree.get(0).getChild());
        return rootNode;
    }

    public void buildTree(TreeNode parentNode, List<TreeModel> children) {
        if (CollectionUtil.isNotEmpty(children)) {
            for (TreeModel child : children) {
                TreeNode treeNode = switch (child.getNodeType()) {
                    case TreeNodeType.FOLDER -> new TreeNode(ImageLoad.getInstance().getFolderIcon(), child);
                    case TreeNodeType.CONNECTION -> new TreeNode(ImageLoad.getInstance().getConnectionIcon(), child);
                    case TreeNodeType.TABLE -> new TreeNode(ImageLoad.getInstance().getTableIcon(), child);
                    case TreeNodeType.COLUMN -> new TreeNode(ImageLoad.getInstance().getColIcon(), child);
                    default -> {
                        DialogUtil.showErrorDialog(Main.kToolsRootJFrame, new Object[]{"Build Left Tree Error!"});
                        throw new RuntimeException("Build Left Tree Error!");
                    }
                };
                parentNode.add(treeNode);
                buildTree(treeNode, child.getChild());
            }
        }
    }
}
