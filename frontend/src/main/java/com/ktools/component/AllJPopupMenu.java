package com.ktools.component;

import com.ktools.action.DeleteTreeNodeAction;
import com.ktools.action.EditJDBCConnectionAction;
import com.ktools.action.NewFolderAction;
import com.ktools.action.RenameFolderAction;
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

    private AllJPopupMenu() {
        initRootPopupMenu();
        initFolderPopupMenu();
        initJDBCPopupMenu();
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
        JMenuItem connectItem = new JMenuItem("连接");
        connectItem.setIcon(ImageLoad.getInstance().getConnectIcon());
        jdbcPopupMenu.add(connectItem);
        JMenuItem disconnectItem = new JMenuItem("断开");
        disconnectItem.setIcon(ImageLoad.getInstance().getDisConnectIcon());
        jdbcPopupMenu.add(disconnectItem);
    }

}
