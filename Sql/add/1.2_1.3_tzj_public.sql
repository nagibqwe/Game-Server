
/*be changed list*/
/* 
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

//1.3.0_2020.12.28_0.21.1130_main:
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


DROP TABLE IF EXISTS `eightintegralrank`;
CREATE TABLE eightintegralrank (
  roleID bigint(50) NOT NULL COMMENT '角色唯一ID',
  name varchar(20) binary CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色名',
  hurt bigint(50) DEFAULT 0 COMMENT '总伤害',
  integral int(11) DEFAULT 0 COMMENT '击杀积分',
  platSid varchar(20) DEFAULT NULL COMMENT '区服信息',
  colorCamp int(11) DEFAULT 0 COMMENT '阵营',
  serverid int(11) DEFAULT 0 COMMENT '服务器ID',
  groupId int(11) DEFAULT 0 COMMENT '组ID',
  PRIMARY KEY (roleID)
)
ENGINE = INNODB,
CHARACTER SET utf8,
COLLATE utf8_general_ci;			
COMMENT = '八级阵图周期积分排行';

    
*/

/*The following statements need to be executed*/
/*1.  `peakpk`; 巅峰竞技 */
ALTER TABLE peakpk ADD COLUMN dayTimes int(10) default 0 COMMENT '当天参赛场次';
ALTER TABLE peakpk ADD COLUMN timesReward bigint(20) default 0 COMMENT '场次奖励领取状态';
ALTER TABLE peakpk ADD COLUMN stageReward bigint(20) default 0 COMMENT '段位奖励领取状态';
ALTER TABLE peakpk MODIFY COLUMN POWER BIGINT(20) NOT NULL COMMENT '战力';

/*2.  "eightintegralrank" "八级阵图周期积分排行" */
DROP TABLE IF EXISTS `eightintegralrank`;
CREATE TABLE eightintegralrank (
  roleID bigint(50) NOT NULL COMMENT '角色唯一ID',
  name varchar(64) DEFAULT NULL COMMENT '角色名',
  hurt bigint(50) DEFAULT 0 COMMENT '总伤害',
  integral int(11) DEFAULT 0 COMMENT '击杀积分',
  platSid varchar(20) DEFAULT NULL COMMENT '区服信息',
  colorCamp int(11) DEFAULT 0 COMMENT '阵营',
  serverid int(11) DEFAULT 0 COMMENT '服务器ID',
  groupId int(11) DEFAULT 0 COMMENT '组ID',
  PRIMARY KEY (roleID)
)
ENGINE = INNODB,
CHARACTER SET utf8,
COLLATE utf8_general_ci,
COMMENT = '八级阵图周期积分排行';



