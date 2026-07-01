-- ------------------------------------------
-- Database: tzj_login
-- Usage: Create all tables for the database
-- ------------------------------------------
-- Modification records:
-- ==============2021/08/18 13:55==============
-- Desc: Change default charset from utf8 to utf8mb4 , collate use utf8mb4_unicode_ci;
-- ============================================


-- ----------------------------
-- 1. Table structure for table `activecode`
-- ----------------------------
use tzj_login;
DROP TABLE IF EXISTS `activecode`;
CREATE TABLE `activecode` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '激活码ID',
  `code` char(32) NOT NULL COMMENT '激活码',
  `activeName` varchar(128) DEFAULT NULL COMMENT '激活码类型名',
  `batch` char(32) DEFAULT NULL COMMENT '批次号',
  `itemList` text COMMENT '物品id',
  `param` int(4) DEFAULT '0' COMMENT '参数（默认=0）,1表示万能码',
  `valide_time_begin` datetime DEFAULT NULL COMMENT '有效开始时间（NULL表示无）',
  `valide_time_end` datetime DEFAULT NULL COMMENT '有效结束时间（NULL表示无）',
  `plateform_name_big` varchar(128) DEFAULT NULL COMMENT '平台名（大）',
  `plateform_name_small` varchar(128) DEFAULT NULL COMMENT '平台名（小）',
  `valide_server_id_list` varchar(512) DEFAULT NULL COMMENT '仅某些服有效（[]表示通用），例如：2、3、4服=[2,3,4]',
  `create_time` int(4) DEFAULT '0' COMMENT '创建时间',
  `get_time` int(4) DEFAULT '0' COMMENT '激活码使用时间',
  `get_player_id` bigint(8) DEFAULT '0' COMMENT '激活码使用角色id',
  `get_server_id` bigint(8) DEFAULT '0' COMMENT '服务器编号',
  `get_account_id` bigint(8) DEFAULT '0' COMMENT '激活码使用者帐号id',
  `get_plateform_aid` varchar(255) DEFAULT NULL COMMENT '激活码使用者平台帐号id',
  `get_plateform_name` varchar(64) DEFAULT NULL COMMENT '激活码使用者平台名',
  `deleteTime` bigint(64) DEFAULT '0' COMMENT '激活码使用者平台名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `NewIndex1` (`code`) USING BTREE,
  KEY `NewIndex2` (`create_time`) USING BTREE,
  KEY `NewIndex3` (`batch`) USING BTREE,
  KEY `NewIndex4` (`get_player_id`) USING BTREE,
  KEY `NewIndex5` (`get_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '激活码表';

-- ----------------------------
-- 2.Table structure for table `platforms`
-- ----------------------------
DROP TABLE IF EXISTS `platforms`;
CREATE TABLE `platforms` (
  `str` varchar(64) NOT NULL DEFAULT '' COMMENT '渠道名',
  PRIMARY KEY (`str`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '渠道表';

-- ----------------------------
-- 3.Table structure for table `forbid`
-- ----------------------------
DROP TABLE IF EXISTS `forbid`;
CREATE TABLE `forbid` (
  `str` varchar(64) NOT NULL DEFAULT '' COMMENT '屏蔽字符串',
  `time` int(4) NOT NULL COMMENT '屏蔽时间，-1是永久屏蔽',
  PRIMARY KEY (`str`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '动态屏蔽字表';

-- ----------------------------
-- 4.Table structure for table `forbidspeeking`
-- ----------------------------
DROP TABLE IF EXISTS `forbidspeeking`;
CREATE TABLE `forbidspeeking` (
  `userId` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '账号ID',
  `forbidType` INT(11) DEFAULT '0' COMMENT '禁言类型1:工作室禁言2:全文替换禁言3:关键字替换禁言4:常规禁言5:隐形禁言6:隔离禁言',
  `createTime` VARCHAR(50) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `endTime` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '结束时间',
  PRIMARY KEY (`userId`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '禁言类型表';

-- ----------------------------
-- 5.Table structure for table `userlogin`
-- ----------------------------
DROP TABLE IF EXISTS `userlogin`;
CREATE TABLE `userlogin` (
  `userId` bigint(8) NOT NULL COMMENT '游戏生成的账号id',
  `userName` varchar(190) NOT NULL COMMENT '553平台生成的账号名字',
  `platformAccount` varchar(190) NOT NULL COMMENT '平台生成的账号',
  `platformName` varchar(50) NOT NULL COMMENT '平台名',
  `lastEnterServerId` int(4) DEFAULT '0' COMMENT '上次进入的区服id',
  `data` text NOT NULL COMMENT '区服创建角色信息',
  `createTime` int(4) NOT NULL COMMENT '创建时间',
  `isDelete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0未删除，1删除',
  `forbidenTime` int(4) NOT NULL DEFAULT '0' COMMENT '禁止登录结束时间,0正常登录，-1永久禁止',
  `lastLoginIp` varchar(100) DEFAULT NULL COMMENT '最后一次登录的IP',
  `lastEnterRoleId` bigint(20) NULL DEFAULT NULL COMMENT '上次登录的角色id',
  UNIQUE KEY `userId` (`userId`) USING BTREE,
  UNIQUE KEY `index1` (`userName`) USING BTREE,
  KEY `index2` (`platformAccount`) USING BTREE,
  KEY `index3` (`platformName`) USING BTREE,
  KEY `index4` (`createTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '用户登录表';

-- ----------------------------
-- 6.Table structure for table `white`
-- ----------------------------
DROP TABLE IF EXISTS `white`;
CREATE TABLE `white` (
  `str` varchar(64) NOT NULL DEFAULT '' COMMENT '白名单账号，可以是mac、imei、ip、funcellUUid、machineCode',
  PRIMARY KEY (`str`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '白名单表';

-- ----------------------------
-- 7.Table structure for rolelogin
-- ----------------------------
DROP TABLE IF EXISTS `rolelogin`;
CREATE TABLE `rolelogin`  (
  `roleId` bigint(20) NOT NULL DEFAULT 0 COMMENT '角色id',
  `userId` bigint(20) NOT NULL COMMENT '账号id',
  `serverId` int(11) NOT NULL COMMENT '服务器id',
  `roleName` varchar(64) NOT NULL COMMENT '角色名',
  `lv` int(11) NOT NULL COMMENT '等级',
  `career` tinyint(4) NOT NULL COMMENT '职业',
  `deleteTime` int(4) NOT NULL COMMENT '角色删除时间，0表示未删除',
  `fight` bigint(255) NOT NULL COMMENT '战力',
  PRIMARY KEY (`roleId`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '角色登录表';

-- ----------------------------
-- 8.Table structure for servername
-- ----------------------------
DROP TABLE IF EXISTS `servername`;
CREATE TABLE `servername`  (
  `serverId` int(11) NOT NULL COMMENT '服务器编号',
  `changeName` varchar(64) NULL DEFAULT NULL COMMENT '更改后的服务器名字',
  `changeTime` int(11) NULL DEFAULT NULL COMMENT '更改时间',
  `roleId` bigint(20) NULL DEFAULT NULL COMMENT '更改的玩家id',
  PRIMARY KEY (`serverId`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '服务器改名表';

-- ----------------------------
-- 9.Table structure for table `chatblacklist` 
-- ----------------------------
DROP TABLE IF EXISTS `chatblacklist`;
CREATE TABLE `chatblacklist` (
  `userId` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '账号ID',
  `serverId` INT(11) NOT NULL DEFAULT '0' COMMENT '服务器ID',
  PRIMARY KEY (`userId`,`serverId`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '聊天黑名单';

-- ----------------------------
-- 10.Table structure for table `chatword`
-- ----------------------------
DROP TABLE IF EXISTS `chatword`;
CREATE TABLE `chatword` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `serverId` INT(11) NOT NULL COMMENT '服务器ID',
  `word` VARCHAR(190) NOT NULL COMMENT '内容或关键字',
  `replace` VARCHAR(190) DEFAULT NULL COMMENT '替换内容或关键字',
  `type` INT(11) DEFAULT NULL COMMENT '0 关键字 1 内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `serverId_word` (`serverId`,`word`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci AUTO_INCREMENT=3 COMMENT = '聊天屏蔽内容替换表';

-- ----------------------------
-- 11.Table structure for table `tag`
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` INT(11) NOT NULL COMMENT '标签ID',
  `name` VARCHAR(255) DEFAULT NULL COMMENT '标签名',
  `icon` VARCHAR(500) DEFAULT NULL COMMENT '标签icon',
  `style` int(11) DEFAULT NULL COMMENT 'UI风格',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签库';



DROP TABLE IF EXISTS `rechargetotalmoney`;
CREATE TABLE `rechargetotalmoney` (
  userId bigint(20) NOT NULL AUTO_INCREMENT,
  rechargeTotalMoney int(11) DEFAULT 0 COMMENT '账号删档测试期间总充值',
  userName varchar(255) DEFAULT NULL COMMENT '553平台生成的账号名字',
  platformAccount varchar(255) DEFAULT NULL COMMENT '平台生成的账号',
  PRIMARY KEY (userId)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试删档充值表';


DROP TABLE IF EXISTS `rechargereturn`;
CREATE TABLE `rechargereturn` (
  userId bigint(20) NOT NULL AUTO_INCREMENT COMMENT '游戏生成的账号Id',
  roleId bigint(20) DEFAULT NULL COMMENT '领取返还的角色Id',
  createSid int(11) DEFAULT NULL COMMENT '领取返还的角色创建服',
  rechargeTotalMoney int(11) DEFAULT 0 COMMENT '账号删档测试期间充值总数',
  returnGold int(11) DEFAULT 0 COMMENT '返还等量的元宝数',
  returnTime bigint(20) DEFAULT NULL COMMENT '领取返还的时间',
  userName varchar(255) DEFAULT NULL COMMENT '553平台生成的账号名字',
  platformAccount varchar(255) DEFAULT NULL COMMENT '平台生成的账号',
  PRIMARY KEY (userId)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试删档返利表';

