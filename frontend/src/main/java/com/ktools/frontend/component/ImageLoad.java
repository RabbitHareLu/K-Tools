package com.ktools.frontend.component;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import lombok.Data;

import javax.swing.*;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年11月30日 19:44
 */
@Data
public class ImageLoad {

    private static final ImageLoad INSTANCE = new ImageLoad();

    private ImageIcon logoIcon = null;
    private ImageIcon deleteIcon = null;
    private ImageIcon aboutIcon = null;
    private ImageIcon fontIcon = null;
    private ImageIcon newIcon = null;
    private ImageIcon newFolderIcon = null;
    private ImageIcon databaseIcon = null;
    private ImageIcon sqlConsoleIcon = null;
    private ImageIcon exitIcon = null;
    private ImageIcon fontNameIcon = null;
    private ImageIcon fontSizeIcon = null;
    private ImageIcon fontStyleIcon = null;
    private ImageIcon requiredIcon = null;
    private ImageIcon editIcon = null;
    private ImageIcon connectIcon = null;
    private ImageIcon disConnectIcon = null;
    private ImageIcon renameIcon = null;
    private ImageIcon refreshIcon = null;
    private ImageIcon schemaIcon = null;
    private ImageIcon tableIcon = null;
    private ImageIcon lookIcon = null;
    private ImageIcon columnIcon = null;
    private ImageIcon tableRefreshIcon = null;

    private ImageLoad() {
        logoIcon = buildIcon("images/logo/kt.svg");
        aboutIcon = buildIcon("images/tree/about.svg");
        deleteIcon = buildIcon("images/tree/delete.svg");
        fontIcon = buildIcon("images/tree/font.svg");
        newIcon = buildIcon("images/tree/new.svg");
        newFolderIcon = buildIcon("images/tree/newFolder.svg");
        databaseIcon = buildIcon("images/tree/database.svg");
        sqlConsoleIcon = buildIcon("images/tree/sqlConsole.svg");
        exitIcon = buildIcon("images/tree/exit.svg");
        fontNameIcon = buildIcon("images/tree/font-name.svg");
        fontSizeIcon = buildIcon("images/tree/font-size.svg");
        fontStyleIcon = buildIcon("images/tree/font-style.svg");
        requiredIcon = buildIcon("images/tree/required.svg");
        editIcon = buildIcon("images/tree/edit.svg");
        connectIcon = buildIcon("images/tree/connect.svg");
        disConnectIcon = buildIcon("images/tree/disconnect.svg");
        renameIcon = buildIcon("images/tree/rename.svg");
        refreshIcon = buildIcon("images/tree/refresh.svg");
        schemaIcon = buildIcon("images/tree/schema.svg");
        tableIcon = buildIcon("images/tree/table.svg");
        lookIcon = buildIcon("images/tree/look.svg");
        columnIcon = buildIcon("images/tree/column.svg");
        tableRefreshIcon = buildIcon("images/tree/tableRefresh.svg");
    }

    public static ImageLoad getInstance() {
        return INSTANCE;
    }
    
    public FlatSVGIcon buildIcon(String name) {
        return new FlatSVGIcon(name);
    }


}
