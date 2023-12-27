package com.ktools.frontend.component;

import com.ktools.frontend.action.*;
import lombok.Data;

import javax.swing.*;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月17日 13:56
 */
@Data
public class AllJPopupMenu {
    private static final AllJPopupMenu INSTANCE = new AllJPopupMenu();
    private JPopupMenu rootPopupMenu;
    private JPopupMenu folderPopupMenu;
    private JPopupMenu jdbcPopupMenu;
    private JPopupMenu schemaPopupMenu;
    private JPopupMenu tablePopupMenu;

    private AllJPopupMenu() {
        initRootPopupMenu();
        initFolderPopupMenu();
        initJDBCPopupMenu();
        initSchemaPopupMenu();
        initTablePopupMenu();
    }

    public static AllJPopupMenu getInstance() {
        return INSTANCE;
    }

    private void initRootPopupMenu() {
        rootPopupMenu = new JPopupMenu();
        JMenuItem rootNewFolderItem = new JMenuItem("新建目录");
        rootNewFolderItem.setIcon(UIManager.getIcon("FileChooser.newFolderIcon"));
        rootNewFolderItem.addActionListener(new NewFolderAction());
        rootPopupMenu.add(rootNewFolderItem);
    }

    private void initFolderPopupMenu() {
        folderPopupMenu = new JPopupMenu();
        JMenuItem folderNewFolderItem = new JMenuItem("新建目录");
        folderNewFolderItem.setIcon(UIManager.getIcon("FileChooser.newFolderIcon"));
        folderNewFolderItem.addActionListener(new NewFolderAction());
        folderPopupMenu.add(folderNewFolderItem);

        JMenuItem folderRenameFolderItem = new JMenuItem("重命名");
        folderRenameFolderItem.setIcon(ImageLoad.getInstance().getRenameIcon());
        folderRenameFolderItem.addActionListener(new RenameFolderAction());
        folderPopupMenu.add(folderRenameFolderItem);

        JMenuItem folderDeleteItem = new JMenuItem("删除");
        folderDeleteItem.setIcon(ImageLoad.getInstance().getDeleteIcon());
        folderDeleteItem.addActionListener(new DeleteTreeNodeAction());
        folderPopupMenu.add(folderDeleteItem);
    }

    private void initJDBCPopupMenu() {
        jdbcPopupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("编辑");
        editItem.setIcon(ImageLoad.getInstance().getEditIcon());
        editItem.addActionListener(new EditJDBCConnectionAction());
        jdbcPopupMenu.add(editItem);
        JMenuItem refreshItem = new JMenuItem("刷新");
        refreshItem.setIcon(ImageLoad.getInstance().getRefreshIcon());
        refreshItem.addActionListener(new JDBCNodeRefreshAction());
        jdbcPopupMenu.add(refreshItem);
        JMenuItem connectItem = new JMenuItem("连接");
        connectItem.setIcon(ImageLoad.getInstance().getConnectIcon());
        jdbcPopupMenu.add(connectItem);
        JMenuItem disconnectItem = new JMenuItem("断开");
        disconnectItem.setIcon(ImageLoad.getInstance().getDisConnectIcon());
        jdbcPopupMenu.add(disconnectItem);
    }

    private void initSchemaPopupMenu() {
        schemaPopupMenu = new JPopupMenu();
        JMenuItem refreshItem = new JMenuItem("刷新");
        refreshItem.setIcon(ImageLoad.getInstance().getRefreshIcon());
        refreshItem.addActionListener(new SchemaNodeRefreshAction());
        schemaPopupMenu.add(refreshItem);
    }

    private void initTablePopupMenu() {
        tablePopupMenu = new JPopupMenu();
        JMenuItem lookItem = new JMenuItem("查看数据");
        lookItem.setIcon(ImageLoad.getInstance().getLookIcon());
        tablePopupMenu.add(lookItem);

        JMenuItem refreshItem = new JMenuItem("刷新");
        refreshItem.setIcon(ImageLoad.getInstance().getRefreshIcon());
        refreshItem.addActionListener(new TableNodeRefreshAction());
        tablePopupMenu.add(refreshItem);
    }

}
