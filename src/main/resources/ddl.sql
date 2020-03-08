CREATE TABLE `dianping`.`Untitled`  (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  `telphone` varchar(40) NOT NULL DEFAULT '',
  `password` varchar(400) NOT NULL DEFAULT '',
  `nick_name` varchar(40) NOT NULL DEFAULT '',
  `gender` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `telphone_unique_index`(`telphone`) USING BTREE
);