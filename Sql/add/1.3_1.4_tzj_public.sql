
/*be changed list*/
/* 
//1.3.0_2020.12.28_0.21.1130_main:

CREATE TABLE peakpk
(
    roleId     bigint(20) NOT NULL COMMENT '玩家ID',
    name       varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '玩家名字',
    platform   varchar(20) NOT NULL COMMENT '平台',
    serverId   int(10) NOT NULL COMMENT '服务器ID',
    rankId     int(10) NOT NULL COMMENT '段位ID',
    score      int(10) NOT NULL COMMENT '积分',
    power      int(10) NOT NULL COMMENT '战力',
    time       bigint(20) NOT NULL COMMENT '更新时间',
    times      int(10) NOT NULL COMMENT '本赛季场次',
    dayTimes   int(10) default 0 COMMENT '当天参赛场次',
    timesReward bigint(20) default 0 COMMENT '场次奖励领取状态',
    stageReward bigint(20) default 0 COMMENT '段位奖励领取状态',
    PRIMARY KEY (roleId),
    KEY `score` (`score`) USING BTREE
)
    ENGINE = INNODB,
    CHARACTER SET utf8,
    COLLATE utf8_general_ci,
    COMMENT = '巅峰竞技';

//1.4.0_2021.03.29_0.22.0331_main

CREATE TABLE peakpk
(
    roleId     bigint(20) NOT NULL COMMENT '玩家ID',
    name       varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '玩家名字',
    platform   varchar(20) NOT NULL COMMENT '平台',
    serverId   int(10) NOT NULL COMMENT '服务器ID',
    rankId     int(10) NOT NULL COMMENT '段位ID',
    score      int(10) NOT NULL COMMENT '积分',
    power      bigint(20) NOT NULL COMMENT '战力',
    time       bigint(20) NOT NULL COMMENT '更新时间',
    times      int(10) NOT NULL COMMENT '本赛季场次',
    dayTimes   int(10) default 0 COMMENT '当天参赛场次',
    timesReward bigint(20) default 0 COMMENT '场次奖励领取状态',
    stageReward bigint(20) default 0 COMMENT '段位奖励领取状态',
    PRIMARY KEY (roleId),
    KEY `score` (`score`) USING BTREE
)
    ENGINE = INNODB,
    CHARACTER SET utf8,
    COLLATE utf8_general_ci,
    COMMENT = '巅峰竞技';

*/

/*The following statements need to be executed*/
/*1.  "peakpk" "巅峰竞技" */
ALTER TABLE `peakpk` MODIFY COLUMN `power` bigint(20) NOT NULL COMMENT '战力' AFTER `score`;

/*2.  "TableName2" "COMMENT2" */

DROP TABLE IF EXISTS `fudrole`;
CREATE TABLE fudrole
(
    roleId     bigint(20) NOT NULL COMMENT '玩家ID',
    name       varchar(64) NOT NULL COMMENT '玩家名字',
    platform   varchar(20) NOT NULL COMMENT '平台',
    serverId   int(10) NOT NULL COMMENT '服务器ID',
    tValue     int(10) NOT NULL COMMENT '天禁值',
    score      int(10) NOT NULL COMMENT '个人积分',
    `kill`       int(10) NOT NULL COMMENT '击杀',
    `lock`       int(10) NOT NULL COMMENT '是否解锁奖励',
    scoreReward bigint(20) default 0 COMMENT '奖励领取状态',
    time       bigint(20) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (roleId),
    KEY `score` (`score`) USING BTREE,
    KEY `kill` (`kill`) USING BTREE
)
    ENGINE = INNODB,
    CHARACTER SET utf8,
    COLLATE utf8_general_ci,
    COMMENT = '跨服福地积分排名';

DROP TABLE IF EXISTS `fud`;
CREATE TABLE fud
(
    groupId    int(10) NOT NULL COMMENT '福地分组ID',
    data       longtext NOT NULL COMMENT '福地数据',
    PRIMARY KEY (groupId)
)
    ENGINE = INNODB,
    CHARACTER SET utf8,
    COLLATE utf8_general_ci,
    COMMENT = '跨服福地';

/*3.  "TableName3" "COMMENT3" */
