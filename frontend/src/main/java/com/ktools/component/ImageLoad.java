package com.ktools.component;

import com.kitfox.svg.app.beans.SVGIcon;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年11月30日 19:44
 */
@Data
public class ImageLoad {

    private static final ImageLoad INSTANCE = new ImageLoad();

    private ImageIcon logoIcon = null;
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
        logoIcon = buildIcon(this.getClass().getResource("/images/logo/kt.svg"));
        aboutIcon = buildIcon(this.getClass().getResource("/images/tree/about.svg"));
        deleteIcon = buildIcon(this.getClass().getResource("/images/tree/delete.svg"));
        fontIcon = buildIcon(this.getClass().getResource("/images/tree/font.svg"));
    }

    public static ImageLoad getInstance() {
        return INSTANCE;
    }

    private SVGIcon buildIcon(URL url) {
        URI svgUri = URI.create(String.valueOf(url));
        SVGIcon svgIcon = new SVGIcon();
        svgIcon.setAntiAlias(true);
        svgIcon.setSvgURI(svgUri);
        return svgIcon;
    }

    private SVGIcon buildIcon(URL url, int width, int height) {
        URI svgUri = URI.create(String.valueOf(url));
        SVGIcon svgIcon = new SVGIcon();
        svgIcon.setPreferredSize(new Dimension(width, height));
        svgIcon.setAntiAlias(true);
        svgIcon.setSvgURI(svgUri);
        return svgIcon;
    }
}
