create database if not exists message_wall;

use message_wall;

drop table if not exists message;

create table message(`from` varchar(1024),`to` varchar(1024),message varchar(1024));
