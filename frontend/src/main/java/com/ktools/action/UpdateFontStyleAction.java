package com.ktools.action;

import com.ktools.KToolsContext;
import com.ktools.api.SystemApi;
import com.ktools.common.utils.FontUtil;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月01日 10:33
 */
@Slf4j
public class UpdateFontStyleAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Properties properties = KToolsContext.getInstance().getProperties();
        String fontName = String.valueOf(properties.get("font.name"));
        Integer fontSize = Integer.parseInt(String.valueOf(properties.get("font.size")));

        JCheckBoxMenuItem source = (JCheckBoxMenuItem) e.getSource();
        String newFontStyle = source.getText();

        KToolsContext.getInstance().getApi(SystemApi.class).saveOrUpdateProp("font.style", newFontStyle);

        log.info("修改字体样式为: {}", newFontStyle);
        FontUtil.updateUIFont(new Font(fontName, FontUtil.getFontStyle(newFontStyle), fontSize));

    }
}
