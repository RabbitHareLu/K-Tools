package com.ktools.api;

import com.ktools.mybatis.entity.TreeEntity;

import java.util.List;

/**
 * @author WCG
 */
public interface SystemApi {

    List<TreeEntity> getTree(int nodeId);

    void addNode(TreeEntity treeEntity);

    void updateNode(TreeEntity treeEntity);

    void delete(String nodeId);

    void saveOrUpdateProp(String key, String value);

}