package com.ktools;

import java.util.Properties;

/**
 * @author WCG
 */
public class Main {

    public static void main(String[] args) {
        KToolsContext kToolsContext = KToolsContext.getInstance();
        Properties properties = kToolsContext.getProperties();
        System.out.println(properties.toString());
    }

}