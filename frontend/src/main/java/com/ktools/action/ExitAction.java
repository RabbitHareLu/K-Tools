package com.ktools.action;

import lombok.extern.slf4j.Slf4j;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年12月18日 17:26
 */
@Slf4j
public class ExitAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        log.info("K-Tools退出");
        System.exit(0);
    }
}
