package com.ktools.component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年11月30日 19:38
 */
@Data
@Slf4j
public class Tree {

    private static final Tree INSTANCE = new Tree();

    private JTree jTree;

    private DefaultTreeModel defaultTreeModel;

    private Tree() {

    }

    public static Tree getInstance() {
        return INSTANCE;
    }
}
