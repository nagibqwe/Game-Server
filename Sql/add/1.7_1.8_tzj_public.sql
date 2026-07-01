
/*be changed list*/
/* 
//1.7.0_2021.07.22_0.25.0730_main:


//1.8.0_2021.11.09_0.26.1110_main:


*/

/*The following statements need to be executed*/
/*1.   "TableName1" "COMMENT1" */


/*2.  "TableName2" "COMMENT2" */


/*3.  "TableName3" "COMMENT3" */

CREATE TABLE `couplefight` (
  `activityId` int(11) NOT NULL COMMENT '活动id',
  `group` int(11) NOT NULL COMMENT '服务器组id',
  `data` longtext COMMENT '数据',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `status` int(11) DEFAULT NULL COMMENT '活动状态',
  PRIMARY KEY (`activityId`,`group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT = '仙侣对决';

CREATE TABLE `couplefightguess` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `activityId` int(11) DEFAULT NULL COMMENT '活动id',
  `group` int(11) DEFAULT NULL COMMENT '服务器组',
  `type` int(11) DEFAULT NULL COMMENT '类型 1天 2地',
  `round` int(11) DEFAULT NULL COMMENT '所在轮次',
  `fightId` int(11) DEFAULT NULL COMMENT '战斗id',
  `teamId` bigint(20) DEFAULT NULL COMMENT '队伍id',
  `rid` bigint(20) DEFAULT NULL COMMENT '竞猜玩家id',
  `name` varchar(45) DEFAULT NULL COMMENT '竞猜玩家名称',
  `level` int(11) DEFAULT NULL COMMENT '竞猜玩家等级',
  `power` bigint(20) DEFAULT NULL COMMENT '竞猜玩家战力',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT = '仙侣对决竞猜';

CREATE TABLE `couplefightteam` (
  `id` bigint(20) NOT NULL COMMENT '队伍id',
  `activityId` int(11) DEFAULT NULL COMMENT '活动id',
  `group` int(11) DEFAULT NULL COMMENT '组id',
  `data` longtext COMMENT '数据',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT = '仙侣对决队伍';
