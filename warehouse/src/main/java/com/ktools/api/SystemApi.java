package com.ktools.api;

import com.ktools.model.TreeModel;

import java.util.List;

/**
 * @author WCG
 */
public interface SystemApi {

    List<TreeModel> getTree(int nodeId);

}