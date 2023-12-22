package com.ktools.common.utils;

import com.formdev.flatlaf.FlatLaf;
import com.ktools.KToolsContext;
import com.ktools.action.UpdateFontNameAction;
import com.ktools.action.UpdateFontSizeAction;
import com.ktools.action.UpdateFontStyleAction;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Properties;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月01日 10:22
 */
public class FontUtil {

    public static String[] getAllFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return ge.getAvailableFontFamilyNames();
    }

    public static void updateUIFont(Font newFont) {
        UIManager.put("defaultFont", newFont);
        FlatLaf.updateUI();
    }

    public static void putUIFont() {
        Properties properties = KToolsContext.getInstance().getProperties();
        String fontName = String.valueOf(properties.get("font.name"));
        int fontSize = Integer.parseInt(String.valueOf(properties.get("font.size")));
        String fontStyle = String.valueOf(properties.get("font.style"));
        UIManager.put("defaultFont", new Font(fontName, FontUtil.getFontStyle(fontStyle), fontSize));
    }

    public static int getFontStyle(String fontStyle) {
        if (Objects.equals(fontStyle, "粗体")) {
            return Font.BOLD;
        } else if (Objects.equals(fontStyle, "斜体")) {
            return Font.ITALIC;
        } else {
            return Font.PLAIN;
        }
    }

    public static void updateFont() {
        Properties properties = KToolsContext.getInstance().getProperties();
        String fontName = String.valueOf(properties.get("font.name"));
        int fontSize = Integer.parseInt(String.valueOf(properties.get("font.size")));
        String fontStyle = String.valueOf(properties.get("font.style"));

        FontUtil.updateUIFont(new Font(fontName, FontUtil.getFontStyle(fontStyle), fontSize));
    }
}
