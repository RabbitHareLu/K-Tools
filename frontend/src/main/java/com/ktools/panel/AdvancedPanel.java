package com.ktools.panel;

import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月19日 17:01
 */
@Data
public class AdvancedPanel extends JPanel {

    private Map<String, String> advanceValueMap = new LinkedHashMap<>();

    public AdvancedPanel() {
        setLayout(new BorderLayout());

    }

}
