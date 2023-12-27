import com.ktools.warehouse.manager.datasource.KDataSourceFactory;

module com.ktools.warehouse {

    requires java.base;
    requires transitive java.logging;
    requires transitive java.sql;
    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.databind;
    requires mybatis.flex.core;
    requires mybatis.flex.annotation;
    requires mybatis.flex.processor;
    requires org.mybatis;
    requires com.zaxxer.hikari;
    requires transitive lombok;
    requires transitive org.slf4j;
    requires flyway.core;

    exports com.ktools.warehouse;
    exports com.ktools.warehouse.api;
    exports com.ktools.warehouse.exception;
    exports com.ktools.warehouse.manager.uid;
    exports com.ktools.warehouse.common.utils;
    exports com.ktools.warehouse.mybatis.entity;
    exports com.ktools.warehouse.manager.datasource;
    exports com.ktools.warehouse.manager.datasource.jdbc;
    exports com.ktools.warehouse.manager.datasource.model;
    exports com.ktools.warehouse.manager.datasource.jdbc.model;
    exports com.ktools.warehouse.manager.datasource.jdbc.query;

    opens db.migration;
    opens com.ktools.warehouse.mybatis.handler;

    uses KDataSourceFactory;

}