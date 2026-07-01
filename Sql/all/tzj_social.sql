-- ------------------------------------------
-- Database: tzj_social
-- Usage: Create all tables for the database
-- ------------------------------------------
-- Modification records:
-- ==============2021/08/18 13:55==============
-- Desc: Change default charset from utf8 to utf8mb4 , collate use utf8mb4_unicode_ci;
-- ============================================



-- ----------------------------
-- 1.Table structure for `friend`
-- ----------------------------
DROP TABLE IF EXISTS `friend`;
CREATE TABLE `friend` (
  `roleId` bigint(20) NOT NULL COMMENT '角色ID',
  `latelyPlayers` longtext COMMENT '最近聊天列表',
  `friends` longtext COMMENT '好友列表',
  `enemies` longtext COMMENT '仇人列表',
  `shields` longtext COMMENT '屏蔽列表',
  `sendLogs` longtext COMMENT '送出礼物的日志',
  `receiveLogs` longtext COMMENT '接收礼物的日志',
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '好友表';


-- ----------------------------
-- 2.Table structure for `player`
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player` (
  `id` bigint(20) NOT NULL COMMENT '角色ID',
  `plat` varchar(64) COMMENT '平台',
  `serverId` int(11) COMMENT '角色当前服务器',
  `createServerId` int(11) COMMENT '角色创建服务器',
  `createTime` bigint(20) COMMENT '角色创建时间',
  `userId` bigint(20) COMMENT '用户ID',
  `roleName` varchar(64) COMMENT '角色名',
  `fightPower` bigint(20) COMMENT '战力',
  `level` int(11) COMMENT '等级',
  `career` int(11) COMMENT '职业',
  `playerVip` int(11) COMMENT 'vip等级',
  `data` longtext COMMENT '玩家其他数据',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '玩家信息表';


-- ----------------------------
-- 3.Table structure for `serverparam`
-- ----------------------------
DROP TABLE IF EXISTS `serverparam`;
CREATE TABLE `serverparam` (
  `paramkey` varchar(64) NOT NULL,
  `serverid` int(11) DEFAULT NULL,
  `paramvalue` longtext NOT NULL,
  PRIMARY KEY (`paramkey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '服务器参数表';
