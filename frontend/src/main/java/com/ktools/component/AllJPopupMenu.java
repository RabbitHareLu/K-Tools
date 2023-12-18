package com.ktools.component;

import com.ktools.action.DeleteTreeNodeAction;
import com.ktools.action.NewFolderAction;
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

    private AllJPopupMenu() {
        initRootPopupMenu();
        initFolderPopupMenu();
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

        JMenuItem folderDeleteItem = new JMenuItem("删除");
        folderDeleteItem.setIcon(ImageLoad.getInstance().getDeleteIcon());
        folderDeleteItem.addActionListener(new DeleteTreeNodeAction());
        folderPopupMenu.add(folderDeleteItem);
    }

}
