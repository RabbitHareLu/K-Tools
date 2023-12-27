package com.ktools.warehouse.api;

import com.ktools.warehouse.exception.KToolException;
import com.ktools.warehouse.mybatis.entity.TreeEntity;

import java.util.List;

/**
 * @author WCG
 */
public interface SystemApi {

    List<TreeEntity> getTree(int nodeId);

    void addNode(TreeEntity treeEntity) throws KToolException;

    void updateNode(TreeEntity treeEntity) throws KToolException;

    void deleteNode(TreeEntity treeEntity);

    void deleteChildNode(TreeEntity treeEntity);

    void saveOrUpdateProp(String key, String value);

}