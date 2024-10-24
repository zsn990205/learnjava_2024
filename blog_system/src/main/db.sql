create database if not exists java_2024_blog_system;

use java_2024_blog_system;

drop table if exists blog;
drop table if exists user;

create table blog (
    blogId int primary key auto_increment,
    title varchar(1024),
    content varchar(2048),
    postTime datetime,
    userId int
);

create table user(
    username varchar(50) unique,
    userId int primary key auto_increment,
    password varchar(50)
);

insert into blog values(1,'这是第一篇博客','# 从今天开始我要认真写代码',now(),1);
insert into blog values(2,'这是第二篇博客','# 从昨天开始我要认真写代码',now(),1);
insert into blog values(3,'这是第三篇博客','# 从前天开始我要认真写代码',now(),1);

insert into user values('张三',1,'123');
insert into user values('李四',2,'123');

insert into user values('zhangsan',1,'123');
insert into user values('lisi',2,'123');