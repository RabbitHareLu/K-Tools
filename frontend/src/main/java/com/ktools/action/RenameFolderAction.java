package com.ktools.action;

import com.ktools.KToolsContext;
import com.ktools.Main;
import com.ktools.api.SystemApi;
import com.ktools.common.utils.DialogUtil;
import com.ktools.common.utils.StringUtil;
import com.ktools.component.Tree;
import com.ktools.component.TreeNode;
import com.ktools.exception.KToolException;
import com.ktools.mybatis.entity.TreeEntity;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月22日 12:46
 */
@Slf4j
public class RenameFolderAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Tree instance = Tree.getInstance();
        TreePath selectionPath = instance.getCurrentTreePath();
        TreeNode currentTreeNode = instance.getCurrentTreeNode(selectionPath);

        String result = String.valueOf(JOptionPane.showInputDialog(
                Main.kToolsRootJFrame,
                "目录名称",
                "重命名文件夹",
                JOptionPane.INFORMATION_MESSAGE,
                null, null, currentTreeNode.getTreeEntity().getNodeName()
        ));

        if (StringUtil.isNotBlank(result)) {
            log.info("重命名文件夹: {} -> {}", currentTreeNode.getTreeEntity().getNodeName(), result);

            TreeEntity treeEntity = currentTreeNode.getTreeEntity();
            treeEntity.setNodeName(result);

            JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor((JMenuItem) e.getSource());
            try {
                KToolsContext.getInstance().getApi(SystemApi.class).updateNode(treeEntity);
            } catch (KToolException ex) {
                DialogUtil.showErrorDialog(jFrame, ex.getMessage());
                throw new RuntimeException(ex);
            }

            currentTreeNode.setTreeEntity(treeEntity);
            Tree.getInstance().getDefaultTreeModel().nodeChanged(currentTreeNode);
        }
    }
}
