package com.ktools.action;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月20日 21:22
 */
public class CancelDisposeFrameAction implements ActionListener {
    private JFrame jFrame;

    public CancelDisposeFrameAction() {
    }

    public CancelDisposeFrameAction(JFrame jFrame) {
        this.jFrame = jFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        jFrame.dispose();
    }
}
