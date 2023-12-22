package com.ktools.component;

import com.kitfox.svg.app.beans.SVGIcon;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;

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

    private ImageLoad() {
        logoIcon = buildIcon(this.getClass().getResource("/images/logo/kt.svg"));
        aboutIcon = buildIcon(this.getClass().getResource("/images/tree/about.svg"));
        deleteIcon = buildIcon(this.getClass().getResource("/images/tree/delete.svg"));
        fontIcon = buildIcon(this.getClass().getResource("/images/tree/font.svg"));
        newIcon = buildIcon(this.getClass().getResource("/images/tree/new.svg"));
        newFolderIcon = buildIcon(this.getClass().getResource("/images/tree/newFolder.svg"));
        databaseIcon = buildIcon(this.getClass().getResource("/images/tree/database.svg"));
        sqlConsoleIcon = buildIcon(this.getClass().getResource("/images/tree/sqlConsole.svg"));
        exitIcon = buildIcon(this.getClass().getResource("/images/tree/exit.svg"));
        fontNameIcon = buildIcon(this.getClass().getResource("/images/tree/font-name.svg"));
        fontSizeIcon = buildIcon(this.getClass().getResource("/images/tree/font-size.svg"));
        fontStyleIcon = buildIcon(this.getClass().getResource("/images/tree/font-style.svg"));
        requiredIcon = buildIcon(this.getClass().getResource("/images/tree/required.svg"));
        editIcon = buildIcon(this.getClass().getResource("/images/tree/edit.svg"));
        connectIcon = buildIcon(this.getClass().getResource("/images/tree/connect.svg"));
        disConnectIcon = buildIcon(this.getClass().getResource("/images/tree/disconnect.svg"));
        renameIcon = buildIcon(this.getClass().getResource("/images/tree/rename.svg"));
        refreshIcon = buildIcon(this.getClass().getResource("/images/tree/refresh.svg"));
        schemaIcon = buildIcon(this.getClass().getResource("/images/tree/schema.svg"));
        tableIcon = buildIcon(this.getClass().getResource("/images/tree/table.svg"));
    }

    public static ImageLoad getInstance() {
        return INSTANCE;
    }

    public SVGIcon buildIcon(URL url) {
        URI svgUri = URI.create(String.valueOf(url));
        SVGIcon svgIcon = new SVGIcon();
        svgIcon.setAntiAlias(true);
        svgIcon.setSvgURI(svgUri);
        return svgIcon;
    }

    public SVGIcon buildIcon(URL url, int width, int height) {
        URI svgUri = URI.create(String.valueOf(url));
        SVGIcon svgIcon = new SVGIcon();
        svgIcon.setPreferredSize(new Dimension(width, height));
        svgIcon.setAntiAlias(true);
        svgIcon.setSvgURI(svgUri);
        return svgIcon;
    }
}
