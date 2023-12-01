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
 * @date 2023年12月01日 10:26
 */
@Slf4j
public class UpdateFontNameAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Properties properties = KToolsContext.getInstance().getProperties();
        Integer fontSize = Integer.parseInt(String.valueOf(properties.get("font.size")));
        String fontStyle = String.valueOf(properties.get("font.style"));

        JCheckBoxMenuItem source = (JCheckBoxMenuItem) e.getSource();
        String newFontName = source.getText();

        KToolsContext.getInstance().getApi(SystemApi.class).saveOrUpdateProp("font.name", newFontName);

        log.info("修改字体名称为: {}", newFontName);
        FontUtil.updateUIFont(new Font(newFontName, FontUtil.getFontStyle(fontStyle), fontSize));
    }
}
