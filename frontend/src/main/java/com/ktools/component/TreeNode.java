package com.ktools.component;

import com.ktools.model.TreeModel;
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
    private TreeModel treeModel;

    public TreeNode(ImageIcon icon, TreeModel treeModel) {
        super(treeModel.getNodeName());
        this.icon = icon;
        this.treeModel = treeModel;
    }

}
