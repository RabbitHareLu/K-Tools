package com.ktools.action;

import com.ktools.KToolsContext;
import com.ktools.api.SystemApi;
import com.ktools.common.utils.CollectionUtil;
import com.ktools.common.utils.DialogUtil;
import com.ktools.common.utils.StringUtil;
import com.ktools.component.Tree;
import com.ktools.component.TreeNode;
import com.ktools.exception.KToolException;
import com.ktools.frame.JDBCConnectionFrame;
import com.ktools.manager.datasource.model.KDataSourceConfig;
import com.ktools.manager.datasource.model.KDataSourceMetadata;
import com.ktools.manager.uid.UidKey;
import com.ktools.mybatis.entity.TreeEntity;
import lombok.Data;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月20日 21:28
 */
@Data
public class TreeJDBCNodeAction implements ActionListener {

    JTree jTree = Tree.getInstance().getJTree();

    private JDBCConnectionFrame jdbcConnectionFrame;
    private String name;
    private String comment;
    private String username;
    private String url;
    private String password;
    private Map<String, String> advanceValueMap;
    private KDataSourceMetadata kDataSourceMetadata;

    public TreeJDBCNodeAction() {
    }

    public TreeJDBCNodeAction(JDBCConnectionFrame jdbcConnectionFrame) {
        this.jdbcConnectionFrame = jdbcConnectionFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        initVariable();
        checkNameNotNull();
        checkVariableValid();

        TreePath selectionPath = jTree.getSelectionPath();

        // 如果selectionPath为null, 说明未选择任何节点, 因此直接默认在根节点的目录下创建
        if (Objects.isNull(selectionPath)) {
            selectionPath = new TreePath(jTree.getModel().getRoot());
        }

        TreeNode currentTreeNode = (TreeNode) selectionPath.getLastPathComponent();

        TreeEntity treeEntity = new TreeEntity();
        treeEntity.setId(KToolsContext.getInstance().getIdGenerator().getId(UidKey.TREE));
        treeEntity.setParentNodeId(currentTreeNode.getTreeEntity().getId());
        treeEntity.setNodeName(name);
        treeEntity.setNodeType(kDataSourceMetadata.getName());
        treeEntity.setNodeComment(comment);

        List<String> nodePathList = new ArrayList<>();
        buildTreeNodePath(nodePathList, selectionPath);
        treeEntity.setNodePath(getNodePathString(nodePathList));
        treeEntity.setNodeInfo(advanceValueMap);

        JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor((JButton) e.getSource());
        try {
            KToolsContext.getInstance().getApi(SystemApi.class).addNode(treeEntity);
        } catch (KToolException ex) {
            DialogUtil.showErrorDialog(jFrame, ex.getMessage());
            throw new RuntimeException(ex);
        }

        TreeNode treeNode = new TreeNode(treeEntity);
        DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
        model.insertNodeInto(treeNode, currentTreeNode, currentTreeNode.getChildCount());
        expandTreeNode(selectionPath);
        jdbcConnectionFrame.dispose();
    }

    private void initVariable() {
        kDataSourceMetadata = jdbcConnectionFrame.getKDataSourceMetadata();
        name = jdbcConnectionFrame.getNameField().getText();
        comment = jdbcConnectionFrame.getCommentField().getText();

        jdbcConnectionFrame.getAdvancedPanel().collectValue();
        advanceValueMap = jdbcConnectionFrame.getAdvancedPanel().getAdvanceValueMap();

        username = jdbcConnectionFrame.getRegularPanel().getUsernameField().getText();
        url = jdbcConnectionFrame.getRegularPanel().getUrlField().getText();
        password = String.valueOf(jdbcConnectionFrame.getRegularPanel().getPasswordField().getPassword());

        advanceValueMap.put("username", username);
        advanceValueMap.put("jdbcUrl", url);
        advanceValueMap.put("password", password);
    }

    private void checkNameNotNull() {
        if (StringUtil.isBlank(name)) {
            DialogUtil.showErrorDialog(jdbcConnectionFrame, "名称不能为空");
            throw new RuntimeException("名称为必填项, 不能为空");
        }
    }

    private void checkVariableValid() {
        List<KDataSourceConfig> config = kDataSourceMetadata.getConfig();
        if (CollectionUtil.isNotEmpty(config) && CollectionUtil.isNotEmpty(advanceValueMap)) {
            for (KDataSourceConfig kDataSourceConfig : config) {
                if (kDataSourceConfig.isMust()) {
                    advanceValueMap.computeIfPresent(kDataSourceConfig.getKey(), (k, v) -> {
                        if (StringUtil.isBlank(v)) {
                            DialogUtil.showErrorDialog(jdbcConnectionFrame, kDataSourceConfig.getName() + "不能为空");
                            throw new RuntimeException(kDataSourceConfig.getName() + "为必填项, 不能为空");
                        }
                        return v;
                    });
                }

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

    private void expandTreeNode(TreePath selectionPath) {
        if (Objects.nonNull(selectionPath)) {
            if (!jTree.isExpanded(selectionPath)) {
                jTree.expandPath(selectionPath);
            }
        }
    }
}
