create table `tree`
(
    `id`             varchar(36) primary key,
    `node_name`      varchar(100),
    `node_type`      varchar(20),
    `node_comment`   varchar(255),
    `parent_node_id` varchar(36)
);

create table `prop`
(
    `id`    varchar(36) primary key,
    `key`   varchar(255),
    `value` varchar(255)
);
