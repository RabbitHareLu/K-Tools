package com.ktools.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.ktools.KToolsContext;
import com.ktools.KToolsRootJFrame;
import com.ktools.Main;
import com.ktools.action.NewFolderAction;
import com.ktools.action.UpdateFontNameAction;
import com.ktools.action.UpdateFontSizeAction;
import com.ktools.action.UpdateFontStyleAction;
import com.ktools.common.utils.FontUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.time.Year;
import java.util.Objects;
import java.util.Properties;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年11月30日 19:53
 */
@Data
@Slf4j
public class Menu {

    private static Menu INSTANCE = new Menu();

    private String[] fontStyleArr = new String[]{"普通", "斜体", "粗体"};

    private JMenuBar jMenuBar;

    private JMenu newMenu;
    private JMenu editMenu;
    private JMenu settingsMenu;
    private JMenu helpMenu;
    private JMenu fontMenu;
    private JMenu fontNameMenu;
    private JMenu fontSizeMenu;
    private JMenu fontStyleMenu;

    private JMenuItem newFolder;
    private JMenuItem newImpalaConnection;
    private JMenuItem newSqlConsole;
    private JMenuItem about;

    private Menu() {
        jMenuBar = new JMenuBar();

        newMenu = new JMenu("新建");
        editMenu = new JMenu("编辑");
        settingsMenu = new JMenu("设置");
        helpMenu = new JMenu("帮助");

        fontMenu = new JMenu("字体");
        fontMenu.setIcon(ImageLoad.getInstance().getFontIcon());
        fontNameMenu = new JMenu("字体名称");
        fontSizeMenu = new JMenu("字体大小");
        fontStyleMenu = new JMenu("字体样式");

        newFolder = new JMenuItem("新建文件夹");
        newFolder.setIcon(ImageLoad.getInstance().getNewFolderIcon());
        newImpalaConnection = new JMenuItem("新建Impala连接");
        newImpalaConnection.setIcon(ImageLoad.getInstance().getImpalaConnectionIcon());
        newSqlConsole = new JMenuItem("新建SQL查询控制台");
        newSqlConsole.setIcon(ImageLoad.getInstance().getSqlConsoleIcon());
        about = new JMenuItem("关于");
        about.setIcon(ImageLoad.getInstance().getAboutIcon());


        jMenuBar.add(newMenu);
        jMenuBar.add(editMenu);
        jMenuBar.add(settingsMenu);
        jMenuBar.add(helpMenu);

        newMenu.add(newFolder);
        newMenu.add(newImpalaConnection);
        newMenu.add(newSqlConsole);

        settingsMenu.add(fontMenu);
        fontMenu.add(fontNameMenu);
        fontMenu.add(fontSizeMenu);
        fontMenu.add(fontStyleMenu);
        initFontMenu();

        helpMenu.add(about);

        setNewFolderAction();
        setNewImpalaConnectionAction();
        setAboutAction();
    }

    private void initFontMenu() {
        Properties properties = KToolsContext.getInstance().getProperties();
        String fontName = String.valueOf(properties.get("font.name"));
        Integer fontSize = Integer.parseInt(String.valueOf(properties.get("font.size")));
        String fontStyle = String.valueOf(properties.get("font.style"));

        String[] allFonts = FontUtil.getAllFonts();

        ButtonGroup fontNameGroup = new ButtonGroup();
        for (String fontItem : allFonts) {
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(fontItem);
            if (Objects.equals(fontName, fontItem)) {
                jCheckBoxMenuItem.setSelected(true);
            }

            jCheckBoxMenuItem.addActionListener(new UpdateFontNameAction());

            fontNameGroup.add(jCheckBoxMenuItem);
            fontNameMenu.add(jCheckBoxMenuItem);
        }

        ButtonGroup fontSizeGroup = new ButtonGroup();
        for (int i = 10; i <= 30; i++) {
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(i + "");
            if (Objects.equals(fontSize, i)) {
                jCheckBoxMenuItem.setSelected(true);
            }

            jCheckBoxMenuItem.addActionListener(new UpdateFontSizeAction());
            fontSizeGroup.add(jCheckBoxMenuItem);
            fontSizeMenu.add(jCheckBoxMenuItem);
        }

        ButtonGroup fontStyleGroup = new ButtonGroup();
        for (String style : fontStyleArr) {
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(style);
            if (Objects.equals(style, fontStyle)) {
                jCheckBoxMenuItem.setSelected(true);
            }

            jCheckBoxMenuItem.addActionListener(new UpdateFontStyleAction());
            fontStyleGroup.add(jCheckBoxMenuItem);
            fontStyleMenu.add(jCheckBoxMenuItem);
        }

        FontUtil.updateUIFont(new Font(fontName, FontUtil.getFontStyle(fontStyle), fontSize));
    }

    public static Menu getInstance() {
        return INSTANCE;
    }

    private void setNewFolderAction() {
        newFolder.addActionListener(new NewFolderAction());
    }

    private void setNewImpalaConnectionAction() {
//        newImpalaConnection.addActionListener(new NewImpalaConnectionAction());
    }

    private void setAboutAction() {
        about.addActionListener(e -> {
            JLabel titleLabel = new JLabel(KToolsRootJFrame.TITLE);
            titleLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h1");
            JOptionPane.showMessageDialog(Main.kToolsRootJFrame,
                    new Object[]{
                            titleLabel,
                            "致力于实现ALL数据库的全方位管理",
                            " ",
                            "Copyright 2021-" + Year.now() + " Liu sl, Wang cg",
                    },
                    "About", JOptionPane.PLAIN_MESSAGE);

        });
    }
}
