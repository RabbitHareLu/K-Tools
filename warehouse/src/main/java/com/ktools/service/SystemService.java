package com.ktools.service;

import com.ktools.api.SystemApi;
import com.ktools.mybatis.entity.PropEntity;
import com.ktools.mybatis.entity.TreeEntity;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.core.update.UpdateWrapper;

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
        List<TreeEntity> treeModelList = this.treeMapper.selectAll();

        Map<Integer, List<TreeEntity>> map = treeModelList
                .stream()
                .filter(node -> node.getParentNodeId() != null)
                .collect(Collectors.groupingBy(TreeEntity::getParentNodeId));

        treeModelList.forEach(node -> node.setChild(map.get(node.getId())));

        return treeModelList.stream().filter(node -> Objects.equals(node.getId(), nodeId)).toList();
    }

    @Override
    public void addNode(TreeEntity treeEntity) {
        // 更新数据库
        this.treeMapper.insert(treeEntity);
    }

    @Override
    public void updateNode(TreeEntity treeEntity) {
        // 更新数据库
        this.treeMapper.update(treeEntity);
    }

    @Override
    public void saveOrUpdateProp(String key, String value) {
        Properties properties = this.kToolsContext.getProperties();
        if (properties.contains(key)) {
            // 更新属性
            properties.replace(key, value);
            // 更新数据库
            UpdateChain.of(PropEntity.class)
                    .set(PropEntity::getValue, value)
                    .where(PropEntity::getKey).eq(key)
                    .update();
        } else {
            // 新增属性
            properties.put(key, value);
            // 更新数据库
            PropEntity entity = UpdateWrapper.of(PropEntity.class)
                    .set(PropEntity::getKey, key)
                    .set(PropEntity::getValue, value)
                    .toEntity();
            this.propMapper.insert(entity);
        }
    }

}
