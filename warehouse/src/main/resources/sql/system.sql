drop table if exists tree;

create table tree
(
    id             varchar(36) primary key,
    node_name      varchar(100),
    node_type      varchar(20),
    comment        varchar(255),
    url            varchar(100),
    kudu_master    varchar(255),
    node_level     int,
    parent_node_id varchar(36),
    create_time    timestamp
);

insert into tree (id, node_name, node_type, comment, url, kudu_master, node_level, parent_node_id, create_time)
values ('ROOT', 'ROOT', 'ROOT', null, null, null, 0, null, now());