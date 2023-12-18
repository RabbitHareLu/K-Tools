import com.ktools.KToolsContext;
import com.ktools.api.SystemApi;
import com.ktools.common.model.TreeNodeType;
import com.ktools.component.Tree;
import com.ktools.component.TreeNode;
import com.ktools.manager.uid.UidKey;
import com.ktools.mybatis.entity.TreeEntity;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年11月30日 12:49
 */
public class TreeTest {

    public static void main(String[] args) {
        JTree jTree = Tree.getInstance().getJTree();
        TreePath selectionPath = new TreePath(jTree.getModel().getRoot());
        testTreeAddNode(selectionPath);
//        testTreeAddMoreNode(selectionPath);
    }

    public static void testTreeAddMoreNode(TreePath selectionPath) {
        TreeNode currentTreeNode = (TreeNode) selectionPath.getLastPathComponent();

        Iterator<javax.swing.tree.TreeNode> iterator = currentTreeNode.children().asIterator();
        while (iterator.hasNext()) {
            TreeNode next = (TreeNode) iterator.next();
            TreePath treePath = new TreePath(next.getPath());
            testTreeAddNode(treePath);
        }

    }

    public static void testTreeAddNode(TreePath selectionPath) {

        TreeNode currentTreeNode = (TreeNode) selectionPath.getLastPathComponent();

        for (int i = 0; i < 10000; i++) {
            TreeEntity treeEntity = new TreeEntity();
            treeEntity.setId(KToolsContext.getInstance().getIdGenerator().getId(UidKey.TREE));
            treeEntity.setParentNodeId(currentTreeNode.getTreeEntity().getId());
            treeEntity.setNodeName("目录" + (i + 1));
            treeEntity.setNodeType(TreeNodeType.FOLDER);
            treeEntity.setNodeComment(null);
            treeEntity.setChild(null);

            List<String> nodePathList = new ArrayList<>();
            nodePathList.add(String.valueOf(treeEntity.getId()));
            buildTreeNodePath(nodePathList, selectionPath);
            treeEntity.setNodePath(getNodePathString(nodePathList));

            KToolsContext.getInstance().getApi(SystemApi.class).addNode(treeEntity);
        }

    }

    public static String getNodePathString(List<String> nodePathList) {
        StringBuilder nodePathString = new StringBuilder();
        for (int i = nodePathList.size() - 1; i >= 0; i--) {
            nodePathString.append(nodePathList.get(i)).append("/");
        }
        return nodePathString.delete(nodePathString.length() - 1, nodePathString.length()).toString();
    }

    public static void buildTreeNodePath(List<String> list, TreePath selectionPath) {
        TreeNode currentTreeNode = (TreeNode) selectionPath.getLastPathComponent();
        Integer id = currentTreeNode.getTreeEntity().getId();
        list.add(String.valueOf(id));

        TreePath parentPath = selectionPath.getParentPath();
        if (Objects.nonNull(parentPath)) {
            buildTreeNodePath(list, parentPath);
        }
    }
}
