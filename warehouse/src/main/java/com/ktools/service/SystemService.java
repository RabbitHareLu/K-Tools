package com.ktools.service;

import com.ktools.api.SystemApi;
import com.ktools.mybatis.entity.PropEntity;
import com.ktools.mybatis.entity.TreeEntity;
import com.ktools.mybatis.mapper.PropMapper;
import com.ktools.mybatis.mapper.TreeMapper;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
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
        List<TreeEntity> treeModelList = QueryChain.of(TreeEntity.class).list();

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
        this.getMapper(TreeMapper.class).insert(treeEntity);
    }

    @Override
    public void updateNode(TreeEntity treeEntity) {
        // 更新数据库
        this.getMapper(TreeMapper.class).update(treeEntity);
    }

    @Override
    public void deleteNode(String nodeId) {
        TreeEntity treeEntity = this.getMapper(TreeMapper.class).selectOneById(nodeId);
        String path = treeEntity.getNodePath() + "/" + treeEntity.getId();
        // 删除当前节点
        this.getMapper(TreeMapper.class).delete(treeEntity);
        // 删除全部子节点
        QueryWrapper queryWrapper = QueryWrapper.create().where(TreeEntity::getNodePath).eq(path);
        this.getMapper(TreeMapper.class).deleteByQuery(queryWrapper);
    }

    @Override
    public void saveOrUpdateProp(String key, String value) {
        Properties properties = this.kToolsContext.getProperties();
        if (properties.containsKey(key)) {
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
            this.getMapper(PropMapper.class).insert(entity);
        }


    }

}
