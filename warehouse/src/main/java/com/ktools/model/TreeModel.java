package com.ktools.model;

import com.ktools.common.db.TableField;
import com.ktools.common.db.TableName;
import lombok.Data;

import java.util.List;

/**
 * @author WCG
 */
@Data
@TableName("tree")
public class TreeModel {

    @TableField("id")
    private String id;

    @TableField("parent_node_id")
    private String parentNodeId;

    @TableField("node_name")
    private String nodeName;

    @TableField("node_type")
    private String nodeType;

    @TableField("node_comment")
    private String nodeComment;

    private List<TreeModel> child;

}
