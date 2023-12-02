package com.ktools.component;

import com.ktools.KToolsContext;
import com.ktools.api.SystemApi;
import com.ktools.common.model.TreeNodeType;
import com.ktools.common.utils.CollectionUtil;
import com.ktools.mybatis.entity.TreeEntity;
import com.ktools.style.TreeNodeRenderer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        jTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 判断是否为鼠标右键点击
                if (SwingUtilities.isRightMouseButton(e)) {
                    int x = e.getX();
                    int y = e.getY();

                    TreePath path = jTree.getClosestPathForLocation(x, y);
                    Rectangle pathBounds = jTree.getPathBounds(path);

                    if (Objects.nonNull(pathBounds)) {
                        if (y >= pathBounds.getY() && y <= (pathBounds.getY() + pathBounds.getHeight())) {
                            jTree.setSelectionPath(path);

                        }
                    } else {
                        // 当前菜单没有任何节点


                    }
                }
            }
        });
    }

    public static Tree getInstance() {
        return INSTANCE;
    }

    public TreeNode initTree() {
        SystemApi api = KToolsContext.getInstance().getApi(SystemApi.class);
        List<TreeEntity> tree = api.getTree(0);

        TreeNode rootNode = new TreeNode(0, null, "ROOT", TreeNodeType.ROOT, "ROOT");
        buildTree(rootNode, tree.get(0).getChild());
        return rootNode;
    }

    public void buildTree(TreeNode parentNode, List<TreeEntity> children) {
        if (CollectionUtil.isNotEmpty(children)) {
            for (TreeEntity child : children) {
                TreeNode treeNode = new TreeNode(child);
                parentNode.add(treeNode);
                buildTree(treeNode, child.getChild());
            }
        }
    }
}
