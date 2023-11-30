package com.ktools;

import lombok.Data;

import javax.swing.*;
import java.util.Objects;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年11月30日 12:44
 */
@Data
public class KToolsRootJFrame extends JFrame {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    public KToolsRootJFrame() {
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/logo/kts.png")));
        setIconImage(imageIcon.getImage());

        setSize(WIDTH, HEIGHT);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }

}
