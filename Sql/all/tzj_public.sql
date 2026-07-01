-- ------------------------------------------
-- Database: tzj_public
-- Usage: Create all tables for the database
-- ------------------------------------------
-- Modification records:
-- ==============2021/08/18 13:55==============
-- Desc: Change default charset from utf8 to utf8mb4 , collate use utf8mb4_unicode_ci;
-- ============================================

-- ----------------------------
-- 1.Table structure for table `activityrank`
-- ----------------------------
DROP TABLE IF EXISTS `activityrank`;
CREATE TABLE `activityrank` (
  `id` bigint(50) NOT NULL,
  `roleId` bigint(50) NOT NULL,
  `type` bigint(50) DEFAULT NULL,
  `funtionV` int(5) DEFAULT NULL,
  `rankDate` int(20) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `serverId` int(5) DEFAULT NULL,
  `plat` varchar(10) DEFAULT NULL,
  `serial` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '活动排行';


-- ----------------------------
-- 2.Table structure for table `serverparam`
-- ----------------------------
DROP TABLE IF EXISTS `serverparam`;
CREATE TABLE `serverparam` (
  `paramkey` varchar(64) NOT NULL,
  `serverid` int(11) DEFAULT NULL,
  `paramvalue` longtext NOT NULL,
  PRIMARY KEY (`paramkey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '服务器参数';


-- ----------------------------
-- 3.Table structure for table `crossrank`
-- ----------------------------
DROP TABLE IF EXISTS `crossrank`;
CREATE TABLE `crossrank` (
  `roleId` bigint(50) NOT NULL COMMENT '角色唯一ID',
  `roleName` varchar(64) DEFAULT NULL COMMENT '角色名',
  `serverId` int(5) DEFAULT NULL COMMENT '服务器ID',
  `career` tinyint(4) DEFAULT NULL COMMENT '职业',
  `stateVip` tinyint(4) DEFAULT '0' COMMENT '境界等级',
  `level` int(11) DEFAULT '0' COMMENT '角色等级',
  `fightPower` bigint(20) DEFAULT '0' COMMENT '战斗力',
  `fashionBodyId` int(11) DEFAULT '0' COMMENT '时装身体',
  `fashionWeaponId` int(11) DEFAULT '0' COMMENT '时装武器',
  `wingModel` int(11) DEFAULT '0',
  `fashionHalo` int(11) DEFAULT '0',
  `fashionMatrix` int(11) DEFAULT '0',
  `spiritId` int(11) DEFAULT '0',
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '跨服排行';

-- ----------------------------
-- 4.Table structure for table `peakpk`
-- ----------------------------
DROP TABLE IF EXISTS `peakpk`;
CREATE TABLE peakpk
(
    roleId     bigint(20) NOT NULL COMMENT '玩家ID',
    name       varchar(64) NOT NULL COMMENT '玩家名字',
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
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '巅峰竞技';

-- ----------------------------
-- 5.Table structure for table `fudrole`
-- ----------------------------
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
    data       longtext NOT NULL COMMENT '数据',
    PRIMARY KEY (roleId),
    KEY `score` (`score`) USING BTREE,
    KEY `kill` (`kill`) USING BTREE
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '跨服福地积分排名';


-- ----------------------------
-- 6.Table structure for table `fud`
-- ----------------------------
DROP TABLE IF EXISTS `fud`;
CREATE TABLE fud
(
    groupId    int(10) NOT NULL COMMENT '福地分组ID',
    data       longtext NOT NULL COMMENT '福地数据',
    PRIMARY KEY (groupId)
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '跨服福地';


-- ----------------------------
-- 7.Table structure for table `eightintegralrank`
-- ----------------------------
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
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '八极阵图';
	

DROP TABLE IF EXISTS `couplefight`;
CREATE TABLE `couplefight` (
  `activityId` int(11) NOT NULL COMMENT '活动id',
  `group` int(11) NOT NULL COMMENT '服务器组id',
  `data` longtext COMMENT '数据',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `status` int(11) DEFAULT NULL COMMENT '活动状态',
  PRIMARY KEY (`activityId`,`group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT = '仙侣对决';

DROP TABLE IF EXISTS `couplefightguess`;
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

DROP TABLE IF EXISTS `couplefightteam`;
CREATE TABLE `couplefightteam` (
  `id` bigint(20) NOT NULL COMMENT '队伍id',
  `activityId` int(11) DEFAULT NULL COMMENT '活动id',
  `group` int(11) DEFAULT NULL COMMENT '组id',
  `data` longtext COMMENT '数据',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT = '仙侣对决队伍';
