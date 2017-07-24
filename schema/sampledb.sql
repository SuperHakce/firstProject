DROP DATABASE IF EXISTS sampledb;
CREATE DATABASE sampledb DEFAULT CHARACTER SET utf8;
USE sampledb;

##创建用户表
CREATE TABLE t_user (
   user_id   INT AUTO_INCREMENT PRIMARY KEY,
   user_name VARCHAR(30),
   password  VARCHAR(32),
   credits INT,
   last_visit datetime,
   last_ip  VARCHAR(23)
)ENGINE=InnoDB;

##创建用户登录日志表
CREATE TABLE t_login_log (
   login_log_id  INT AUTO_INCREMENT PRIMARY KEY,
   user_id   INT,
   ip  VARCHAR(23),
   login_datetime datetime
)ENGINE=InnoDB;

create table t_history
(
   history_id             int(11) not null auto_increment,
   user_id             int(11) not null default 0,
   user_name          varchar(100) not null default '',
   school_name           varchar(100) not null default '',
   start_date          date NOT NULL,
   end_date        date NOT NULL,
   primary key (history_id)
);

create table t_event
(
   event_id             varchar(100) not null primary key,
   school_code             int(11) not null default 0,
   school_name           varchar(100) not null default '',
   event_text           varchar(100) not null default '',
   start_date          date,
   end_date        date,
   is_check        int(11) NOT NULL DEFAULT 0,
   INDEX (school_name)
);

create table t_ticket
(
   tickit_id varchar(100) not null primary key,
   is_success boolean not NULL,
   INDEX (tickit_id)
);

##插入初始化数据
INSERT INTO t_user (user_name,password) 
             VALUES('admin','123456');
COMMIT;