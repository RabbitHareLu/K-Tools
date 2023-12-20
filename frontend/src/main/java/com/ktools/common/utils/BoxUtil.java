package com.ktools.common.utils;

import javax.swing.*;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月19日 14:52
 */
public class BoxUtil {

    /**
     * 填充固定宽度的空白区域
     *
     * @param box
     * @param value
     * @return
     * @author lsl
     * @date 2023/12/19 14:50
     */
    public static void addHorizontalStrut(Box box, int value) {
        box.add(Box.createHorizontalStrut(value));
    }

    /**
     * 填充固定高度的空白区域
     *
     * @param box
     * @param value
     * @return
     * @author lsl
     * @date 2023/12/19 14:51
     */
    public static void addVerticalStrut(Box box, int value) {
        box.add(Box.createVerticalStrut(value));
    }

}
