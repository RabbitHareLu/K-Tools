package com.ktools.frontend.action;

import com.ktools.warehouse.KToolsContext;
import com.ktools.warehouse.api.SystemApi;
import com.ktools.frontend.common.utils.FontUtil;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月01日 10:29
 */
@Slf4j
public class UpdateFontSizeAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Properties properties = KToolsContext.getInstance().getProperties();
        String fontName = String.valueOf(properties.get("font.name"));
        String fontStyle = String.valueOf(properties.get("font.style"));

        JCheckBoxMenuItem source = (JCheckBoxMenuItem) e.getSource();
        int newFontSize = Integer.parseInt(source.getText());

        KToolsContext.getInstance().getApi(SystemApi.class).saveOrUpdateProp("font.size", String.valueOf(newFontSize));

        log.info("修改字体大小为: {}", newFontSize);
        FontUtil.updateUIFont(new Font(fontName, FontUtil.getFontStyle(fontStyle), newFontSize));
    }
}
