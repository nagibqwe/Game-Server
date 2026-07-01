/*==================be changed list====================*/
/* 
//1.0.0_2020.09.18_0.19.0930_main:
DROP TABLE IF EXISTS `peakpk`;
CREATE TABLE peakpk
(
    roleId     bigint(20) NOT NULL COMMENT '玩家ID',
    name       varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '玩家名字',
    serverId   int(10) NOT NULL COMMENT '服务器ID',
    rankId     int(10) NOT NULL COMMENT '段位ID',
    score      int(10) NOT NULL COMMENT '积分',
    power      int(10) NOT NULL COMMENT '战力',
    time       bigint(20) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (roleId),
    KEY `score` (`score`) USING BTREE
)
    ENGINE = INNODB,
    CHARACTER SET utf8,
    COLLATE utf8_general_ci,
    COMMENT = '巅峰竞技';


//1.2.0_2020.11.26_0.20.1030_main:
DROP TABLE IF EXISTS `peakpk`;
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
    PRIMARY KEY (roleId),
    KEY `score` (`score`) USING BTREE
)
    ENGINE = INNODB,
    CHARACTER SET utf8,
    COLLATE utf8_general_ci,
    COMMENT = '巅峰竞技';
 */

/*==========The following statements need to be executed===========*/
/*1.  `peakpk`; 巅峰竞技 */
ALTER TABLE peakpk ADD COLUMN  platform   varchar(20) NOT NULL COMMENT '平台' AFTER name;
ALTER TABLE peakpk ADD COLUMN  times      int(10) NOT NULL COMMENT '本赛季场次'  AFTER time;

