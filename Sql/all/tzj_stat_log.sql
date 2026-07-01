/*
Navicat MySQL Data Transfer

Source Server         : 10.0.1.182
Source Server Version : 50631
Source Host           : 10.0.1.182:3306
Source Database       : tzj_stat_log

Target Server Type    : MYSQL
Target Server Version : 50631
File Encoding         : 65001

Date: 2021-12-18 14:47:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `stat_last_insert`
-- ----------------------------
DROP TABLE IF EXISTS `stat_last_insert`;
CREATE TABLE `stat_last_insert` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `sid` int(11) NOT NULL DEFAULT '0',
  `src_table` varchar(64) NOT NULL DEFAULT '' COMMENT '表名',
  `src_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '最后一次插入的时间',
  `src_id` int(11) unsigned zerofill DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sid` (`sid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5593 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='记录抓数据最后一次时间';

-- ----------------------------
-- Records of stat_last_insert
-- ----------------------------

-- ----------------------------
-- Table structure for `stat_login`
-- ----------------------------
DROP TABLE IF EXISTS `stat_login`;
CREATE TABLE `stat_login` (
  `srcId` int(11) NOT NULL DEFAULT '0' COMMENT '平台ID',
  `platId` varchar(256) NOT NULL COMMENT '渠道ID',
  `serverId` int(11) unsigned DEFAULT '0' COMMENT '区服ID',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `machineCode` varchar(70) DEFAULT NULL,
  `roleId` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '角色ID',
  `timeLogin` datetime NOT NULL COMMENT '时间-登录',
  `time` bigint(20) NOT NULL DEFAULT '0',
  `sid` int(11) NOT NULL DEFAULT '0' COMMENT '//原区服ID',
  `level` int(4) DEFAULT NULL,
  `platformName` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`srcId`,`time`,`sid`),
  KEY `test` (`roleId`,`timeLogin`),
  KEY `timeLogin` (`timeLogin`) USING BTREE,
  KEY `roleId` (`roleId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='统计-登录表';

-- ----------------------------
-- Records of stat_login
-- ----------------------------

-- ----------------------------
-- Table structure for `stat_recharge`
-- ----------------------------
DROP TABLE IF EXISTS `stat_recharge`;
CREATE TABLE `stat_recharge` (
  `srcId` int(11) NOT NULL DEFAULT '0' COMMENT '平台ID',
  `roleId` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  `sid` int(11) NOT NULL DEFAULT '0',
  `orderNo` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `gameMoney` int(11) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `statusReason` int(11) DEFAULT NULL,
  `addTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `time` datetime DEFAULT NULL,
  `totalFee` int(11) DEFAULT NULL,
  `itemId` int(11) DEFAULT NULL,
  `platformName` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `timesec` bigint(20) DEFAULT NULL,
  `src` tinyint(4) DEFAULT NULL,
  `goodsId` int(11) DEFAULT NULL,
  PRIMARY KEY (`srcId`,`sid`,`addTime`),
  KEY `index_1` (`timesec`) USING BTREE,
  KEY `index_2` (`sid`) USING BTREE,
  KEY `index_3` (`roleId`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of stat_recharge
-- ----------------------------

-- ----------------------------
-- Table structure for `stat_role`
-- ----------------------------
DROP TABLE IF EXISTS `stat_role`;
CREATE TABLE `stat_role` (
  `userId` bigint(64) NOT NULL COMMENT '用户ID',
  `roleId` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `roleName` varchar(64) NOT NULL DEFAULT '' COMMENT '角色名',
  `createsid` int(11) NOT NULL COMMENT '创建服ID',
  `machineCode` varchar(70) DEFAULT NULL COMMENT '机器码',
  `level` int(11) DEFAULT NULL COMMENT '等级',
  `sex` int(4) DEFAULT NULL COMMENT '性别',
  `career` int(4) DEFAULT NULL COMMENT '角色职业',
  `rechargeGold` bigint(20) DEFAULT NULL COMMENT '总充值获得元宝数',
  `gold` bigint(20) DEFAULT NULL COMMENT '元宝',
  `onlineTime` int(11) DEFAULT NULL COMMENT '在线时长',
  `lastLoginTime` int(11) DEFAULT NULL COMMENT '最后一次登录时间',
  `createTime` varchar(50) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`roleId`,`createsid`),
  UNIQUE KEY `key` (`roleId`) USING BTREE,
  KEY `userId_sid` (`userId`,`createsid`) USING BTREE,
  KEY `index_2` (`userId`) USING BTREE,
  KEY `role_sid` (`roleId`,`createsid`) USING BTREE,
  KEY `index_3` (`roleId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5655221081082453064 DEFAULT CHARSET=utf8 COMMENT='统计-角色表';

-- ----------------------------
-- Records of stat_role
-- ----------------------------
