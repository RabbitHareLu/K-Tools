package com.ktools.service;

import com.ktools.api.SystemApi;
import com.ktools.common.db.DbContext;
import com.ktools.model.TreeModel;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 *
 * @author WCG
 */
public class SystemService extends BaseService implements SystemApi {

    @Override
    public List<TreeModel> getTree(String nodeId) {
        DbContext dbContext = getkToolsContext().getDbContext();
        List<TreeModel> treeModelList = dbContext.selectAll(getSysDataSource(), TreeModel.class);

        Map<String, List<TreeModel>> map = treeModelList
                .stream()
                .filter(node -> node.getParentNodeId() != null)
                .collect(Collectors.groupingBy(TreeModel::getParentNodeId));

        treeModelList.forEach(node -> node.setChild(map.get(node.getId())));

        return treeModelList.stream().filter(node -> Objects.equals(node.getParentNodeId(), nodeId)).toList();
    }

}
