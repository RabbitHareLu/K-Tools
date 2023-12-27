package com.ktools.frontend.style;

import com.ktools.warehouse.KToolsContext;
import com.ktools.warehouse.api.DataSourceApi;
import com.ktools.frontend.common.model.TreeNodeType;
import com.ktools.frontend.component.ImageLoad;
import com.ktools.frontend.component.TreeNode;
import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.manager.datasource.model.KDataSourceMetadata;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.Objects;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月01日 11:08
 */
@Slf4j
public class TreeNodeRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        TreeNode treeNode = (TreeNode) value;

        this.setText(String.valueOf(treeNode.getUserObject()));

        // 处理根节点的展开和叶子节点的情况
        if (Objects.isNull(treeNode.getParent())) {
            if (tree.isRootVisible()) {
                // 如果根节点可见，则处理展开和叶子节点的情况
                if (expanded) {
                    setIcon(UIManager.getIcon("Tree.openIcon"));
                } else {
                    setIcon(UIManager.getIcon("Tree.closedIcon"));
                }
            }
        } else {
            // 非根节点
            switch (treeNode.getTreeEntity().getNodeType()) {
                case TreeNodeType.FOLDER -> {
                    if (expanded) {
                        this.setIcon(UIManager.getIcon("Tree.openIcon"));
                    } else {
                        this.setIcon(UIManager.getIcon("Tree.closedIcon"));
                    }
                }
                case "IMPALA" -> {
                    KDataSourceMetadata metadata;
                    try {
                        metadata = KToolsContext.getInstance().getApi(DataSourceApi.class).getMetadata("IMPALA");
                    } catch (KToolException e) {
                        throw new RuntimeException(e);
                    }
                    this.setIcon(ImageLoad.getInstance().buildIcon(metadata.getLogo()));
                }
                case TreeNodeType.SCHEMA -> this.setIcon(ImageLoad.getInstance().getSchemaIcon());
                case TreeNodeType.TABLE -> this.setIcon(ImageLoad.getInstance().getTableIcon());
                case TreeNodeType.COLUMN -> this.setIcon(ImageLoad.getInstance().getColumnIcon());
                default -> log.info("default");
            }
        }

        return this;
    }
}
