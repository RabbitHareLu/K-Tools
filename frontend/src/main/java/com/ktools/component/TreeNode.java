package com.ktools.component;

import com.ktools.mybatis.entity.TreeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年11月30日 19:39
 */
@Getter
@Setter
@NoArgsConstructor
public class TreeNode extends DefaultMutableTreeNode {

    private ImageIcon icon;
    private TreeEntity treeEntity;

    public TreeNode(ImageIcon icon, TreeEntity treeEntity) {
        super(treeEntity.getNodeName());
        this.icon = icon;
        this.treeEntity = treeEntity;
    }

    public TreeNode(ImageIcon icon, Integer id, Integer parentNodeId, String nodeName, String nodeType, String nodeComment) {
        super(nodeName);
        this.icon = icon;
        this.treeEntity = new TreeEntity(id, parentNodeId, nodeName, nodeType, nodeComment, "0", null);
    }

}
