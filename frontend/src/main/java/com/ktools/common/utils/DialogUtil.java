package com.ktools.common.utils;

import com.ktools.Main;

import javax.swing.*;
import java.awt.*;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月01日 11:32
 */
public class DialogUtil {

    public static void showErrorDialog(Component parentComponent, Object message) {
        JOptionPane.showMessageDialog(
                parentComponent,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

}
