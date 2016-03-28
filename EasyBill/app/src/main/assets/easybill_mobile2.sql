DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` INTEGER PRIMARY KEY,
  `user_id` int(11) NOT NULL,
  `cate` TEXT CHECK( cate IN ('支付宝','建行卡','工行卡','VISA','其他') ) NOT NULL,
  `num` varchar(255) NOT NULL,
  `money` double NOT NULL
);
DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill` (
  `id` INTEGER PRIMARY KEY,
  `user_id` int(11) NOT NULL,
  `father_id` TEXT CHECK( father_id IN ('一般','项目','预算','梦想','债务') ) NOT NULL,
  `child_id` int(10) NOT NULL,
  `io` int(1) NOT NULL,
  `number` double NOT NULL,
  `date` datetime NOT NULL,
  `detail` varchar(255) NOT NULL
);
DROP TABLE IF EXISTS `bugget`;
CREATE TABLE `bugget` (
  `id` INTEGER PRIMARY KEY,
  `user_id` int(11) NOT NULL,
  `begin` date NOT NULL,
  `end` date NOT NULL,
  `number` double NOT NULL,
  `used` double NOT NULL,
  `over` int(1) NOT NULL
);
DROP TABLE IF EXISTS `debt`;
CREATE TABLE `debt` (
  `id` INTEGER PRIMARY KEY,
  `user_id` int(10) NOT NULL,
  `io` int(1) NOT NULL,
  `num` double NOT NULL,
  `who` varchar(255) NOT NULL,
  `begin` time NOT NULL,
  `end` time NOT NULL,
  `actual_end` time NOT NULL,
  `over` int(1) NOT NULL,
  `remark` varchar(255) default NULL
);
DROP TABLE IF EXISTS `dream`;
CREATE TABLE `dream` (
  `id` INTEGER PRIMARY KEY,
  `user_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `content` varchar(255) NOT NULL,
  `number` double NOT NULL,
  `have_money` double NOT NULL,
  `begin` time NOT NULL,
  `end` time NOT NULL,
  `done` int(1) NOT NULL
);
DROP TABLE IF EXISTS `label`;
CREATE TABLE `label` (
  `id` INTEGER PRIMARY KEY,
  `user_id` int(11) NOT NULL,
  `io` int(1) NOT NULL,
  `where_in_main` varchar(255) NOT NULL,
  `ico` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL
);
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` INTEGER PRIMARY KEY,
  `user_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `number` double NOT NULL,
  `leaving` double NOT NULL,
  `begin` date NOT NULL,
  `end` date NOT NULL,
  `done` int(1) NOT NULL,
  `remark` varchar(255) default NULL
);
DROP TABLE IF EXISTS `setting`;
CREATE TABLE `setting` (
  `user_id` int(11) NOT NULL,
  `last_sync_date` datetime default NULL,
  `auto_sync` int(1) NOT NULL,
  `update` int(1) NOT NULL,
  `auto_update` int(1) NOT NULL
);
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(6) NOT NULL,
  `name` varchar(255) NOT NULL,
  `portrait` varchar(255) default NULL,
  `sex` TEXT CHECK( sex IN ('男','女') ) NOT NULL,
  `birth` date default NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) default NULL,
  `email` varchar(255) default NULL,
  `money` double default NULL
);
