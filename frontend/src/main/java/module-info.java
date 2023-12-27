module com.ktools.frontend {
    requires com.ktools.warehouse;
    requires java.desktop;
    requires com.formdev.flatlaf;
    requires com.formdev.flatlaf.extras;
    requires com.formdev.flatlaf.intellijthemes;
    requires mybatis.flex.core;
    requires com.github.weisj.jsvg;

    opens images.tree;
    opens images.logo;
}