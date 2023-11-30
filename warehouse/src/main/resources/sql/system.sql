drop table if exists tree;

create table tree
(
    id             varchar(36) primary key,
    node_name      varchar(100),
    node_type      varchar(20),
    node_comment   varchar(255),
    parent_node_id varchar(36)
);

insert into tree (id, node_name, node_type, node_comment, parent_node_id) values ('ROOT', 'ROOT', 'ROOT', null, null);