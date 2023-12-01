package com.ktools.common.utils;

import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

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

    public static int getFontStyle(String fontStyle) {
        if (Objects.equals(fontStyle, "粗体")) {
            return Font.BOLD;
        } else if (Objects.equals(fontStyle, "斜体")) {
            return Font.ITALIC;
        } else {
            return Font.PLAIN;
        }
    }
}
