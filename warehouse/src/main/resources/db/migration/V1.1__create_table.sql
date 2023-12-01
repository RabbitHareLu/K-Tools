create table `tree`
(
    `id`             int not null primary key auto_increment,
    `node_name`      varchar(100),
    `node_type`      varchar(20),
    `node_comment`   varchar(255),
    `parent_node_id` int
);

create table `prop`
(
    `id`    int not null primary key auto_increment,
    `key`   varchar(255),
    `value` varchar(255)
);
