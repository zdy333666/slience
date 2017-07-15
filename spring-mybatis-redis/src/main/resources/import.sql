drop table if exists group_category;

create table group_category (id int primary key auto_increment, name varchar);

insert into group_category (name) values ('family');
insert into group_category (name) values ('school');
insert into group_category (name) values ('company');
