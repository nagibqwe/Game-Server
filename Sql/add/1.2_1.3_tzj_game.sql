
/*be changed list*/
/* 
//1.2.0_2020.11.26_0.20.1030_main:
DROP TABLE IF EXISTS `peakpk`;
CREATE TABLE peakpk
(
    roleId bigint(20) NOT NULL COMMENT '玩家ID',
    rankId int(10) default 0 COMMENT '段位ID',
    score  int(10) default 0 COMMENT '积分',
    time   bigint(20) NOT NULL COMMENT '更新时间',
    times  int(10) default 0 COMMENT '本赛季参赛场次',
    PRIMARY KEY (roleId),
    KEY `score` (`score`) USING BTREE
)
    ENGINE = INNODB,
    CHARACTER SET utf8,
    COLLATE utf8_general_ci,
    COMMENT = '巅峰竞技';

//1.3.0_2020.12.28_0.21.1130_main:
DROP TABLE IF EXISTS `peakpk`;
CREATE TABLE peakpk
(
    roleId bigint(20) NOT NULL COMMENT '玩家ID',
    rankId int(10) default 0 COMMENT '段位ID',
    score  int(10) default 0 COMMENT '积分',
    time   bigint(20) NOT NULL COMMENT '更新时间',
    times  int(10) default 0 COMMENT '本赛季参赛场次',
    dayTimes int(10) default 0 COMMENT '当天参赛场次',
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

/*1.  `peakpk`; 巅峰竞技 */
ALTER TABLE peakpk ADD COLUMN dayTimes int(10) default 0 COMMENT '当天参赛场次';
ALTER TABLE peakpk ADD COLUMN timesReward bigint(20) default 0 COMMENT '场次奖励领取状态';
ALTER TABLE peakpk ADD COLUMN stageReward bigint(20) default 0 COMMENT '段位奖励领取状态';
/*2.  "rankplayer" "排行榜" */
ALTER TABLE rankplayer ADD COLUMN consumeGold int(11) DEFAULT '0' COMMENT '消费排行';

/*3.  "TableName3" "COMMENT3" */
