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
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
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
 * @date 2023年12月20日 21:28
 */
@Data
@Slf4j
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

        TreePath selectionPath = jdbcConnectionFrame.getTreePath();
        TreeNode currentTreeNode = (TreeNode) selectionPath.getLastPathComponent();

        Tree instance = Tree.getInstance();

        if (Objects.isNull(jdbcConnectionFrame.getTreeEntity())) {
            // 如果TreeEntity为空表示为新增，否则为修改
            TreeEntity treeEntity = new TreeEntity();
            treeEntity.setId(KToolsContext.getInstance().getIdGenerator().getId(UidKey.TREE));
            treeEntity.setParentNodeId(currentTreeNode.getTreeEntity().getId());
            treeEntity.setNodeName(name);
            treeEntity.setNodeType(kDataSourceMetadata.getName());
            treeEntity.setNodeComment(comment);

            List<String> nodePathList = new ArrayList<>();
            instance.buildTreeNodePath(nodePathList, selectionPath);
            treeEntity.setNodePath(instance.getNodePathString(nodePathList));
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
            jdbcConnectionFrame.dispose();
            log.info("新增{}节点: {}", jdbcConnectionFrame.getKDataSourceMetadata().getName(), treeEntity.getNodeName());
        } else {
            TreeEntity treeEntity = jdbcConnectionFrame.getTreeEntity();
            treeEntity.setNodeName(name);
            treeEntity.setNodeType(kDataSourceMetadata.getName());
            treeEntity.setNodeComment(comment);
            treeEntity.setNodeInfo(advanceValueMap);

            JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor((JButton) e.getSource());
            try {
                KToolsContext.getInstance().getApi(SystemApi.class).updateNode(treeEntity);
            } catch (KToolException ex) {
                DialogUtil.showErrorDialog(jFrame, ex.getMessage());
                throw new RuntimeException(ex);
            }

            currentTreeNode.setTreeEntity(treeEntity);
            Tree.getInstance().getDefaultTreeModel().nodeChanged(currentTreeNode);

            jdbcConnectionFrame.dispose();
            log.info("修改{}节点: {}", jdbcConnectionFrame.getKDataSourceMetadata().getName(), treeEntity.getNodeName());
        }


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

}
