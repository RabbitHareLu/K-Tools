package com.ktools.service;

import com.ktools.api.SystemApi;
import com.ktools.mybatis.entity.TreeEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 *
 *
 * @author WCG
 */
public class SystemService extends BaseService implements SystemApi {

    @Override
    public List<TreeEntity> getTree(int nodeId) {
        List<TreeEntity> treeModelList = getMybatisContext().getTreeMapper().selectAll();

        Map<Integer, List<TreeEntity>> map = treeModelList
                .stream()
                .filter(node -> node.getParentNodeId() != null)
                .collect(Collectors.groupingBy(TreeEntity::getParentNodeId));

        treeModelList.forEach(node -> node.setChild(map.get(node.getId())));

        return treeModelList.stream().filter(node -> Objects.equals(node.getId(), nodeId)).toList();
    }

    @Override
    public void addNode(TreeEntity treeModel) {

    }

    @Override
    public void saveOrUpdateProp(String key, String value) {
        Properties properties = getkToolsContext().getProperties();
        if (properties.contains(key)) {
            // 更新属性
            properties.replace(key, value);
            // 更新数据库
        } else {
            // 新增属性
            properties.put(key, value);
            // 更新数据库
        }
    }

}
