package com.ktools.component;

import com.ktools.mybatis.entity.TreeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private TreeEntity treeEntity;

    public TreeNode(TreeEntity treeEntity) {
        super(treeEntity.getNodeName());
        this.treeEntity = treeEntity;
    }

    public TreeNode(Integer id, Integer parentNodeId, String nodeName, String nodeType, String nodeComment) {
        super(nodeName);
        this.treeEntity = new TreeEntity(id, parentNodeId, nodeName, nodeType, nodeComment, "0", null, null);
    }

    public TreeEntity getTreeEntity() {
        return treeEntity;
    }

    public void setTreeEntity(TreeEntity treeEntity) {
        setUserObject(treeEntity.getNodeName());
        this.treeEntity = treeEntity;
    }
}
