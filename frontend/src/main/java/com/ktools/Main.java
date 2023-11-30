package com.ktools;

import com.formdev.flatlaf.FlatDarkLaf;
import lombok.Data;

import javax.swing.*;

@Data
public class Main {

    public static KToolsRootJFrame kToolsRootJFrame = null;
    public static void main(String[] args) {
        KToolsContext kToolsContext = KToolsContext.getInstance();

        FlatDarkLaf.setup();
        // 使用 FlatLaf 提供的组件样式
        UIManager.put("Button.arc", 50);  // 设置按钮的弧度
        UIManager.put("Component.focusWidth", 0);  // 设置组件的焦点边框宽度
        UIManager.put("TextComponent.arc", 10);
//        UIManager.put("AreaComponent.arc", 10);

        kToolsRootJFrame = new KToolsRootJFrame();
    }
}