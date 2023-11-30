package com.ktools.component;

import lombok.Data;

import javax.swing.*;
import java.util.Objects;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年11月30日 19:44
 */
@Data
public class ImageLoad {

    private static final ImageLoad INSTANCE = new ImageLoad();

    private ImageIcon rootIcon = null;
    private ImageIcon tableIcon = null;
    private ImageIcon folderIcon = null;
    private ImageIcon databaseIcon = null;
    private ImageIcon colIcon = null;
    private ImageIcon connectionIcon = null;
    private ImageIcon disConnectionIcon = null;
    private ImageIcon refreshIcon = null;
    private ImageIcon deleteIcon = null;
    private ImageIcon editIcon = null;
    private ImageIcon sqlConsoleIcon = null;
    private ImageIcon newFolderIcon = null;
    private ImageIcon impalaConnectionIcon = null;
    private ImageIcon renameIcon = null;
    private ImageIcon aboutIcon = null;
    private ImageIcon fontIcon = null;

    private ImageLoad() {
        rootIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/root.png")));
        tableIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/table.png")));
        folderIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/folder.png")));
        databaseIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/database.png")));
        colIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/col.png")));
        connectionIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/connection.png")));
        disConnectionIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/disConnection.png")));
        refreshIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/refresh.png")));
        deleteIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/delete.png")));
        editIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/edit.png")));
        sqlConsoleIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/sqlConsole.png")));
        newFolderIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/newFolder.png")));
        impalaConnectionIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/impalaConnection.png")));
        renameIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/rename.png")));
        aboutIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/about.png")));
        fontIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/tree/font.png")));
    }

    public static ImageLoad getInstance() {
        return INSTANCE;
    }
}
