package com.ktools.mybatis.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.util.List;

/**
 * @author WCG
 */
@Data
@Table(value = "tree")
public class TreeEntity {

    @Id("id")
    private Integer id;

    @Column("parent_node_id")
    private Integer parentNodeId;

    @Column("node_name")
    private String nodeName;

    @Column("node_type")
    private String nodeType;

    @Column("node_comment")
    private String nodeComment;

    @Column("node_path")
    private String nodePath;

    @Column(ignore = true)
    private List<TreeEntity> child;

}
