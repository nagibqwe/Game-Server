-- ------------------------------------------
-- Database: tzj_backend
-- Usage: Create all tables for the database
-- ------------------------------------------
-- Modification records:
-- ==============2021/08/20 13:55==============
-- Desc: Change default charset from utf8 to utf8mb4 , collate use utf8mb4_unicode_ci;
-- ============================================

-- ----------------------------
-- 1.Table structure for table `channel` 
-- ----------------------------
DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel` (
  `id` INT(32) NOT NULL AUTO_INCREMENT COMMENT '渠道id',
  `name` VARCHAR(128) DEFAULT NULL COMMENT '渠道名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '渠道表';

-- ----------------------------
-- 2.Table structure for table `db_game` 
-- ----------------------------
DROP TABLE IF EXISTS `db_game`;
CREATE TABLE `db_game` (
  `id` INT(32) NOT NULL AUTO_INCREMENT,
  `serverName` VARCHAR(128) NOT NULL COMMENT '服务器名称',
  `serverId` INT(32) DEFAULT NULL COMMENT '服务器ID',
  `groupName` VARCHAR(128) DEFAULT NULL COMMENT '平台名',
  `serverIpPort` VARCHAR(200) DEFAULT NULL COMMENT '服务器IP及端口',
  `dbname` VARCHAR(128) DEFAULT NULL COMMENT '数据库名称',
  `dbuser` VARCHAR(128) DEFAULT NULL COMMENT '数据库用户名',
  `dbpassword` VARCHAR(100) DEFAULT NULL COMMENT '数据库密码',
  `owerlist` VARCHAR(200) DEFAULT NULL COMMENT '合服列表',
  `isHeFu` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '合服标识 0:未合服 1:已合服',
  `hefuServerID` INT(32) DEFAULT '0' COMMENT '合服目标服ID',
  `hefuTime` DATETIME DEFAULT NULL COMMENT '合服时间',
  `serverType` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '服务器类型 0:测试服 1:正式服 2:登录服 3:跨服',
  `isDeleted` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0:启用 1:删除 ',
  PRIMARY KEY (`id`),
  UNIQUE KEY `serverName` (`serverName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '数据库和服务器表(UNUSE)';

-- ----------------------------
-- 3.Table structure for table `game_info`
-- ----------------------------
DROP TABLE IF EXISTS `game_info`;
CREATE TABLE `game_info` (
  `gameId` int(11) NOT NULL COMMENT '游戏ID',
  `rechargeVersion` varchar(50) DEFAULT NULL COMMENT '充值配置版本号',
  `rechargeCurrency` varchar(50) DEFAULT NULL COMMENT '充值货币币种',
  `rechargeSecretkey` varchar(50) DEFAULT NULL COMMENT '第三方充值密钥',
  `autoFirstServerId` int(11) NOT NULL DEFAULT '0' COMMENT '自动开服起始服务器ID',
  `autoUserCount` int(11) NOT NULL DEFAULT '0' COMMENT '自动开服注册人数条件',
  `autoServerId` int(11) DEFAULT '0' COMMENT '自动开服ID',
  `time` bigint(64) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`gameId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '游戏自动开服信息表';

-- ----------------------------
-- 4.Table structure for table `t_activity`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity`;
CREATE TABLE `t_activity` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `type` int(32) DEFAULT NULL COMMENT '活动类型',
  `subType` int(32) DEFAULT NULL COMMENT '节日类型',
  `minLv` int(32) DEFAULT NULL COMMENT '最小开放等级',
  `maxLv` int(32) DEFAULT NULL COMMENT '最大开放等级',
  `tag` int(32) DEFAULT NULL COMMENT '标签(用于区分展示在哪个活动标签下)',
  `sort` int(32) DEFAULT NULL COMMENT '活动排序',
  `name` varchar(255) DEFAULT NULL COMMENT '活动名称',
  `description` varchar(200) DEFAULT NULL COMMENT '活动说明',
  `timeType` int(32) DEFAULT NULL COMMENT '时间类型 0固定时间（配置时间）1开服时间变量（根据开服时间+时间变量计算）',
  `openServerOffsetBegin` int(32) DEFAULT NULL COMMENT '距离开服多少天',
  `openServerOffset` int(32) DEFAULT NULL COMMENT '活动天数',
  `beginTime` varchar(128) DEFAULT NULL COMMENT '活动开始时间',
  `endTime` varchar(128) DEFAULT NULL COMMENT '活动结束时间',
  `openServerRecordOffsetBegin` int(32) DEFAULT NULL COMMENT '记录距离开服多少天',
  `openServerRecordOffset` int(32) DEFAULT NULL COMMENT '活动记录持续天数',
  `startRecordTime` varchar(128) DEFAULT NULL COMMENT '开始记录时间',
  `endRecordTime` varchar(128) DEFAULT NULL COMMENT '结束记录时间',
  `state` int(32) DEFAULT NULL COMMENT '活动状态，0：未验证(测试、删除)，1：已验证(发布、删除)，2：已发布(删除)，     //已过期(删除)这个通过活动结束时间去判断',
  `platform` varchar(300) DEFAULT NULL COMMENT '活动发布平台(groupName)(List JSON化后的字串[plat1,plat2,..])',
  `toSidList` varchar(500) DEFAULT NULL COMMENT '活动要发布到的区服列表(List JSON化后的字串[sid1,sid2,..])',
  `okSidList` varchar(500) DEFAULT NULL COMMENT '活动发布成功的区服列表(List JSON化后的字串[sid1,sid2,..])',
  `isDeleted` tinyint(8) DEFAULT NULL COMMENT '活动是否被删除，0：否，1：是',
  `autoSend` int(32) DEFAULT '0' COMMENT '开服自动发布活动标识，0：否，1：是',
  `isOpenServer` int(32) DEFAULT '0' COMMENT '是否是新服活动',
  `submitBeginTime` varchar(128) DEFAULT NULL COMMENT '提交开始时间',
  `submitEndTime` varchar(128) DEFAULT NULL COMMENT '提交结束时间',
  `custom` text COMMENT '自定义参数',
  `cover` int(32) DEFAULT NULL COMMENT '活动是否被覆盖正在进行的活动，0：否，1：是',
  `configData` text COMMENT '配置参数，用于GM后台反解析',
  PRIMARY KEY (`id`),
  KEY `state_is_end` (`state`,`isDeleted`,`endTime`),
  KEY `type_is_end` (`type`,`isDeleted`,`endTime`),
  KEY `type_is` (`type`,`isDeleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '运营活动表';


-- ----------------------------
-- 5.Table structure for table `t_activity_template`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_template`;
CREATE TABLE `t_activity_template` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '活动模板ID',
  `templateName` varchar(50) DEFAULT NULL COMMENT '模版名称',
  `createTime` varchar(128) DEFAULT NULL COMMENT '活动模板创建时间',
  `type` int(32) DEFAULT NULL COMMENT '活动类型',
  `subType` int(32) DEFAULT NULL COMMENT '活动子类型',
  `minLv` int(32) DEFAULT NULL COMMENT '最小开放等级',
  `maxLv` int(32) DEFAULT NULL COMMENT '最大开放等级',
  `tag` int(32) DEFAULT NULL COMMENT '标签(用于区分展示在哪个活动标签下)',
  `sort` int(32) DEFAULT NULL COMMENT '活动排序',
  `name` varchar(128) DEFAULT NULL COMMENT '活动名称',
  `timeType` int(32) DEFAULT NULL COMMENT '时间类型 0固定时间（配置时间） 1开服时间变量（根据开服时间+时间变量计算）',
  `openServerOffsetBegin` int(32) DEFAULT NULL COMMENT '距离开服多少天',
  `openServerOffset` int(32) DEFAULT NULL COMMENT '活动天数',
  `beginTime` varchar(128) DEFAULT NULL COMMENT '活动开始时间',
  `endTime` varchar(128) DEFAULT NULL COMMENT '活动结束时间',
  `openServerRecordOffsetBegin` int(32) DEFAULT NULL COMMENT '记录距离开服多少天',
  `openServerRecordOffset` int(32) DEFAULT NULL COMMENT '活动记录持续天数',
  `startRecordTime` varchar(128) DEFAULT NULL COMMENT '开始记录时间',
  `endRecordTime` varchar(128) DEFAULT NULL COMMENT '结束记录时间',
  `autoSend` int(32) DEFAULT '0' COMMENT '开服自动发布活动标识，0：否，1：是',
  `isOpenServer` int(32) DEFAULT '0' COMMENT '是否是新服活动',
  `custom` text COMMENT '自定义参数',
  `description` text COMMENT '模板描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UX_t_activity_template_type_createTime` (`type`,`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '运营活动模板表';



-- ----------------------------
-- 6.Table structure for table `t_announce` 
-- ----------------------------
DROP TABLE IF EXISTS `t_announce`;
CREATE TABLE `t_announce` (
  `id` INT(32) NOT NULL AUTO_INCREMENT,
  `createTime` BIGINT(64) DEFAULT NULL COMMENT '公告创建时间',
  `createDate` VARCHAR(128) DEFAULT NULL COMMENT '创建时间的字符串式',
  `userId` INT(32) DEFAULT NULL COMMENT '创建者后台账号ID',
  `userName` CHAR(100) DEFAULT NULL COMMENT '创建者后台名字',
  `groupName` VARCHAR(128) DEFAULT NULL COMMENT '服务器组',
  `serverIds` TEXT DEFAULT NULL COMMENT '服务器id',
  `type` INT(32) DEFAULT NULL COMMENT '类型',
  `content` LONGTEXT COMMENT '公告的内容',
  `reason` VARCHAR(128) DEFAULT NULL COMMENT '原因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '公告表';

-- ----------------------------
-- 7.Table structure for table `t_api_log` 
-- ----------------------------
DROP TABLE IF EXISTS `t_api_log`;
CREATE TABLE `t_api_log` (
  `id` INT(32) NOT NULL AUTO_INCREMENT,
  `url` VARCHAR(255) DEFAULT NULL COMMENT '请求URL',
  `params` TEXT COMMENT '参数',
  `result` TEXT COMMENT '结果',
  `type` INT(32) DEFAULT NULL COMMENT 'API类型',
  `time` BIGINT(64) DEFAULT NULL COMMENT '操作时间 ',
  `ip` VARCHAR(128) DEFAULT NULL COMMENT '请求IP ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = 'api被调用的日志表';

-- ----------------------------
-- 8.Table structure for table `t_backend_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_backend_log`;
CREATE TABLE `t_backend_log` (
  `id` INT(32) NOT NULL AUTO_INCREMENT,
  `userId` INT(32) DEFAULT NULL COMMENT '后台用户ID',
  `userName` VARCHAR(50) DEFAULT NULL COMMENT '后台用户名',
  `content` LONGTEXT COMMENT '内容',
  `time` BIGINT(64) DEFAULT NULL COMMENT '操作时间',
  `ip` VARCHAR(50) DEFAULT NULL COMMENT '操作IP',
  PRIMARY KEY (`id`),
  KEY `userId_index` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '后台操作日志表';

-- ----------------------------
-- 9.Table structure for table `t_blackip`
-- ----------------------------
DROP TABLE IF EXISTS `t_blackip`;
CREATE TABLE `t_blackip` (
  `id` INT(32) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ip` VARCHAR(50) DEFAULT NULL COMMENT 'IP类型的黑名单',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = 'ip黑名单表';

-- ----------------------------
-- 10.Table structure for table `t_blackuser`
-- ----------------------------
DROP TABLE IF EXISTS `t_blackuser`;
CREATE TABLE `t_blackuser` (
  `id` INT(32) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `userNumber` BIGINT(64) DEFAULT NULL COMMENT '用户ID',
  `platform` VARCHAR(50) DEFAULT NULL COMMENT '平台',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '账号黑名单表';

-- ----------------------------
-- 11.Table structure for table `t_changereason`
-- ----------------------------
DROP TABLE IF EXISTS `t_changereason`;
CREATE TABLE `t_changereason` (
  `id` INT(32) DEFAULT NULL COMMENT '原因码id',
  `name` VARCHAR(128) DEFAULT NULL COMMENT '原因码名字'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '改变原因码';

-- ----------------------------
-- 12.Table structure for table `t_code_batch`
-- ----------------------------
DROP TABLE IF EXISTS `t_code_batch`;
CREATE TABLE `t_code_batch` (
  `id` INT(32) NOT NULL AUTO_INCREMENT,
  `batchId` INT(32) DEFAULT NULL COMMENT '批号',
  `userId` INT(32) DEFAULT NULL COMMENT '后台用户ID',
  `time` BIGINT(64) DEFAULT NULL COMMENT '添加时间',
  `platform` VARCHAR(50) DEFAULT NULL COMMENT '平台',
  `isUniversal` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '万能码标识0:普通激活码1:万能码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '激活码添加日志表';

-- ----------------------------
-- 13.Table structure for table `t_cyannounce` 
-- ----------------------------
DROP TABLE IF EXISTS `t_cyannounce`;
CREATE TABLE `t_cyannounce` (
  `id` INT(32) NOT NULL AUTO_INCREMENT COMMENT '公告的编号',
  `groupName` VARCHAR(128) DEFAULT NULL COMMENT '公告的平台分组',
  `serverIds` VARCHAR(100) DEFAULT NULL COMMENT '公告的发送的服务器列表',
  `batchTag` VARCHAR(128) DEFAULT NULL COMMENT '公告的标识',
  `content` TEXT COMMENT '公告的内容',
  `createTime` BIGINT(64) DEFAULT NULL COMMENT '公告的创建时间',
  `createDate` VARCHAR(128) DEFAULT NULL COMMENT '公告的创建时间字符格式化',
  `createUserId` INT(32) DEFAULT NULL COMMENT '公告的添加者ID',
  `createUserName` VARCHAR(128) DEFAULT NULL COMMENT '公告的添加者名字',
  `fromTime` BIGINT(64) DEFAULT NULL COMMENT '公告的开始时间',
  `fromDate` VARCHAR(128) DEFAULT NULL COMMENT '公告的开始字符格式化',
  `toTime` BIGINT(64) DEFAULT NULL COMMENT '公告的结束时间',
  `toDate` VARCHAR(128) DEFAULT NULL COMMENT '公告的结束时间字符格式化',
  `totalTimes` INT(32) DEFAULT NULL COMMENT '公告发送的总次数',
  `nowTimes` BIGINT(64) DEFAULT NULL COMMENT '公告的当前已经发送的次数',
  `nextTimes` BIGINT(64) DEFAULT NULL COMMENT '公告的下一次发送的时间',
  `nextDate` VARCHAR(128) DEFAULT NULL COMMENT '公告的下一次发送时间字符格式化',
  `state` INT(32) DEFAULT NULL COMMENT '公告的当前状态，启用还是禁用',
  `cycleInterval` INT(32) DEFAULT NULL COMMENT '公告发送的频率',
  `type` INT(32) DEFAULT NULL COMMENT '公告发送的位置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '计时频率公告的数据结构表';

-- ----------------------------
-- 14.Table structure for table `t_dblog`
-- ----------------------------
DROP TABLE IF EXISTS `t_dblog`;
CREATE TABLE `t_dblog` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `serverId` int(32) DEFAULT NULL COMMENT '服务器ID',
  `serverName` varchar(128) DEFAULT NULL COMMENT '服务器名称',
  `groupName` varchar(128) DEFAULT NULL COMMENT '平台名',
  `type` int(32) DEFAULT '0' COMMENT '类型：0游戏库，1日志库',
  `serverIpPort` varchar(200) DEFAULT NULL COMMENT '服务器IP及端口',
  `dbname` varchar(128) DEFAULT NULL COMMENT '数据库名称',
  `dbuser` varchar(128) DEFAULT NULL COMMENT '数据库用户名',
  `dbpassword` varchar(100) DEFAULT NULL COMMENT '数据库密码',
  `owerlist` varchar(200) DEFAULT NULL COMMENT '合服列表',
  `isHeFu` tinyint(4) NOT NULL DEFAULT '0' COMMENT '合服标识 0:未合服 1:已合服',
  `hefuServerID` int(32) DEFAULT '0' COMMENT '合服目标服ID',
  `hefuTime` datetime DEFAULT NULL COMMENT '合服时间',
  `serverType` tinyint(4) NOT NULL DEFAULT '0' COMMENT '服务器类型 0:测试服 1:正式服 2:登录服 3:跨服',
  `isDeleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0:启用 1:删除 ',
  `serverOpenTime` varchar(128) DEFAULT NULL COMMENT '开服时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '服务器数据库表';

-- ----------------------------
-- 15.Table structure for table `t_deduct_item`
-- ----------------------------
DROP TABLE IF EXISTS `t_deduct_item`;
CREATE TABLE `t_deduct_item` (
  `id` INT(32) NOT NULL AUTO_INCREMENT COMMENT '道具扣除ID',
  `serverId` INT(32) DEFAULT NULL COMMENT '服务ID',
  `itemId` INT(32) DEFAULT NULL COMMENT '物品ID',
  `roleId` VARCHAR(128) DEFAULT NULL COMMENT '角色ID',
  `dedCount` INT(32) DEFAULT NULL COMMENT '欲扣除的数量',
  `realCount` INT(32) DEFAULT NULL COMMENT '真实扣除的数量',
  `isMail` INT(32) DEFAULT NULL COMMENT '是否发送邮件，0 不发送 1 发送',
  `isBind` TINYINT(1) DEFAULT NULL COMMENT '是否绑定 true 绑定，false 不绑定',
  `dedTime` DATETIME DEFAULT NULL COMMENT '扣除时间',
  `reason` VARCHAR(128) DEFAULT NULL COMMENT '扣除原因',
  `mailTitle` VARCHAR(128) DEFAULT NULL COMMENT '邮件标题',
  `mailContent` VARCHAR(128) DEFAULT NULL COMMENT '邮件标题',
  `isDelete` INT(32) DEFAULT NULL COMMENT '是否删除，0 ：不删除， 1： 删除',
  `sendUser` VARCHAR(128) DEFAULT NULL COMMENT '发起者名字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '道具扣除表';

-- ----------------------------
-- 16.Table structure for table `t_errorlog` 
-- ----------------------------
DROP TABLE IF EXISTS `t_errorlog`;
CREATE TABLE `t_errorlog` (
  `id` INT(32) NOT NULL AUTO_INCREMENT,
  `receTime` VARCHAR(50) DEFAULT NULL COMMENT '接收错误日志的时间',
  `serverId` INT(32) DEFAULT NULL COMMENT '接收的服务器ID',
  `platform` VARCHAR(50) DEFAULT NULL COMMENT '接收的平台编号',
  `type` INT(32) DEFAULT NULL COMMENT '错误类型数字编号',
  `mKey` VARCHAR(200) DEFAULT NULL COMMENT '错误关键字',
  `content` TEXT COMMENT '错误的具体 信息',
  `lastValue` BIGINT(64) DEFAULT NULL COMMENT '错误产生的数量',
  `demo` TEXT COMMENT '备注，用于记录此条有没有做过其它操作',
  `state` INT(32) DEFAULT NULL COMMENT '当前标志位',
  PRIMARY KEY (`id`),
  KEY `s_re` (`serverId`,`receTime`),
  KEY `type_re` (`type`,`receTime`),
  KEY `s_t_k_re` (`serverId`,`type`,`mKey`,`receTime`),
  KEY `t_k_re` (`type`,`mKey`,`receTime`),
  KEY `receTime` (`receTime`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '服务器发来的错误日志表';

-- ----------------------------
-- 17.Table structure for table `t_forbidchat` */
-- ----------------------------
DROP TABLE IF EXISTS `t_forbidchat`;
CREATE TABLE `t_forbidchat` (
  `id` BIGINT(64) NOT NULL AUTO_INCREMENT,
  `userId` VARCHAR(200) DEFAULT NULL COMMENT '聊天禁言的账号',
  `crimeType` INT(32) DEFAULT NULL COMMENT '违规类型 1黑色产业 2不良信息',
  `forbidType` INT(32) DEFAULT NULL COMMENT '禁言类型 1:工作室禁言2:全文替换禁言3:关键字替换禁言4:常规禁言5:隐形禁言6:隔离禁言',
  `createTime` VARCHAR(128) DEFAULT NULL COMMENT '创建的时间',
  `endTime` VARCHAR(128) DEFAULT NULL COMMENT '封号的结束时间',
  `backUserName` VARCHAR(120) DEFAULT NULL COMMENT '后台那个管理人员添加的',
  `backMUserName` VARCHAR(120) DEFAULT NULL COMMENT '后台那个管理人员修改',
  `reason` VARCHAR(600) DEFAULT NULL COMMENT '操作的理由',
  `serverIds` VARCHAR(100) DEFAULT NULL COMMENT '发送到游戏服列表',
  `state` INT(32) DEFAULT NULL COMMENT '删除状态值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '聊天禁言表';

-- ----------------------------
-- 18.Table structure for table `t_forbiduser`
-- ----------------------------
DROP TABLE IF EXISTS `t_forbiduser`;
CREATE TABLE `t_forbiduser` (
  `id` BIGINT(64) NOT NULL AUTO_INCREMENT,
  `userId` VARCHAR(200) DEFAULT NULL COMMENT '封号的条件',
  `createTime` VARCHAR(50) DEFAULT NULL COMMENT '创建的时间',
  `endTime` VARCHAR(50) DEFAULT NULL COMMENT '封号的结束时间',
  `backUserName` VARCHAR(120) DEFAULT NULL COMMENT '后台那个管理人员添加的',
  `backMUserName` VARCHAR(120) DEFAULT NULL COMMENT '后台那个管理人员修改',
  `reason` VARCHAR(600) DEFAULT NULL COMMENT '操作的理由',
  `lsIds` VARCHAR(100) DEFAULT NULL COMMENT '登录服ID',
  `state` INT(32) DEFAULT NULL COMMENT '删除状态值',
  `backStr` VARCHAR(600) DEFAULT NULL COMMENT '登录返回的结果',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '封号表';

-- ----------------------------
-- 19.Table structure for table `t_function` */
-- ----------------------------
DROP TABLE IF EXISTS `t_function`;
CREATE TABLE `t_function` (
  `funcId` INT(32) DEFAULT NULL COMMENT '功能Id',
  `funcName` VARCHAR(128) DEFAULT NULL COMMENT '功能名',
  `parentId` INT(32) DEFAULT NULL COMMENT '父Id',
  `openState` INT(32) DEFAULT NULL COMMENT '开启状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '游戏功能表';

-- ----------------------------
-- 20.Table structure for table `t_gm_log` 
-- ----------------------------
DROP TABLE IF EXISTS `t_gm_log`;
CREATE TABLE `t_gm_log` (
  `id` INT(32) NOT NULL AUTO_INCREMENT,
  `action` VARCHAR(100) DEFAULT NULL COMMENT '操作命令',
  `params` VARCHAR(500) DEFAULT NULL COMMENT '参数',
  `serverName` VARCHAR(100) DEFAULT NULL COMMENT '服务器名',
  `serverId` INT(32) DEFAULT NULL COMMENT '服务器ID',
  `isOk` TINYINT(1) DEFAULT NULL COMMENT '成功失败',
  `result` TEXT COMMENT '处理结果',
  `operDate` DATETIME DEFAULT NULL COMMENT '操作时间',
  `user` VARCHAR(50) DEFAULT NULL COMMENT '后台用户',
  `ip` VARCHAR(50) DEFAULT NULL COMMENT 'ip',
  `gmType` TINYINT(4) NOT NULL DEFAULT '0' COMMENT 'GM命令类型 0:游戏服GM(socket) 1:跨服或登录服GM(http)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = 'GM命令操作表';

-- ----------------------------
-- 21.Table structure for table `t_item` 
-- ----------------------------
DROP TABLE IF EXISTS `t_item`;
CREATE TABLE `t_item` (
  `itemId` INT(32) NOT NULL AUTO_INCREMENT COMMENT '物品Id',
  `itemName` VARCHAR(128) DEFAULT NULL COMMENT '物品名',
  `itemType` INT(32) DEFAULT NULL COMMENT '物品类型',
  `color` INT(32) DEFAULT NULL COMMENT '物品颜色',
  PRIMARY KEY (`itemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '物品装备表';

-- ----------------------------
-- 22.Table structure for table `t_mail` 
-- ----------------------------
DROP TABLE IF EXISTS `t_mail`;
CREATE TABLE `t_mail` (
  `id` BIGINT(64) NOT NULL AUTO_INCREMENT,
  `groupName` VARCHAR(128) DEFAULT NULL COMMENT '平台名字',
  `serverId` INT(32) DEFAULT NULL COMMENT '服务器编号',
  `roleIds` TEXT NOT NULL COMMENT '角色ID列表',
  `title` VARCHAR(120) NOT NULL COMMENT '邮件标题',
  `content` TEXT NOT NULL COMMENT '邮件内容',
  `items` VARCHAR(500) DEFAULT NULL COMMENT '邮件附件物品列表',
  `reason` VARCHAR(300) NOT NULL COMMENT '邮件发送理由',
  `createDate` VARCHAR(128) DEFAULT NULL COMMENT '邮件创建时间',
  `createUser` VARCHAR(128) DEFAULT NULL COMMENT '邮件创建的后台账号名',
  `adminUser` VARCHAR(128) DEFAULT NULL COMMENT '邮件审核的后台账号名',
  `adminDate` VARCHAR(128) DEFAULT NULL COMMENT '邮件审核的日期',
  `adminState` INT(32) DEFAULT NULL COMMENT '审核是否通过',
  `sendState` INT(32) DEFAULT NULL COMMENT '发送到游戏服的状态值',
  `sendErrorMess` VARCHAR(300) DEFAULT NULL COMMENT '发送到服务返回的结果信息',
  `isDelete` INT(32) DEFAULT NULL COMMENT '邮件的删除标志',
  `sended` INT(32) DEFAULT NULL COMMENT '是否已经发送过',
  PRIMARY KEY (`id`),
  KEY `createDate_isdel` (`isDelete`,`createDate`),
  KEY `createUser` (`createUser`,`isDelete`,`createDate`),
  KEY `sended` (`sended`,`isDelete`),
  KEY `isDelete` (`isDelete`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '邮件发送记录表';

-- ----------------------------
-- 23.Table structure for table `t_menu` 
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `menuId` INT(32) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menuName` VARCHAR(50) DEFAULT NULL COMMENT '菜单名',
  `level` TINYINT(4) NOT NULL DEFAULT '3' COMMENT '菜单级别',
  `parentId` INT(32) DEFAULT NULL COMMENT '菜单父ID',
  `alias` VARCHAR(50) DEFAULT NULL COMMENT '菜单别名',
  `urlPath` VARCHAR(50) DEFAULT NULL COMMENT '菜单路径',
  `description` TEXT COMMENT '描述',
  `isDeleted` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`menuId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '功能菜单表';

-- ----------------------------
-- 24.Table structure for table `t_recharge` 
-- ----------------------------
DROP TABLE IF EXISTS `t_recharge`;
CREATE TABLE `t_recharge` (
  `id` INT(32) NOT NULL AUTO_INCREMENT COMMENT '充值ID',
  `serverId` INT(32) DEFAULT NULL COMMENT '充值服务器id',
  `roleId` VARCHAR(50) DEFAULT NULL COMMENT '游戏角色ID',
  `rechargeNumber` INT(32) DEFAULT NULL COMMENT '充值数量',
  `rechargeTotalGold` int(11) DEFAULT NULL COMMENT '充值累积数量',
  `rechargeVipExp` int(11) DEFAULT NULL COMMENT '充值VIP经验',
  `platformName` VARCHAR(50) DEFAULT NULL COMMENT '平台名字',
  `createUser` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
  `createTime` VARCHAR(50) DEFAULT NULL COMMENT '创建时间',
  `rechargeState` INT(32) DEFAULT NULL COMMENT '充值状态,0为待审核，1为通过，2为失败',
  `approvalUser` VARCHAR(50) DEFAULT NULL COMMENT '审核者',
  `approvalTime` VARCHAR(50) DEFAULT NULL COMMENT '审核时间',
  `reason` VARCHAR(50) DEFAULT NULL COMMENT '充值理由',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '充值审核表';

-- ----------------------------
-- 25.Table structure for table `t_role` */
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `roleId` INT(32) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `roleName` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `createTime` DATETIME DEFAULT NULL COMMENT '创建时间',
  `description` VARCHAR(50) DEFAULT NULL COMMENT '描述',
  `isDeleted` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '生效标记 0:生效1:无效',
  PRIMARY KEY (`roleId`),
  UNIQUE KEY `name` (`roleName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '后台用户角色表';

-- ----------------------------
-- 26.Table structure for table `t_role_attr` 
-- ----------------------------
DROP TABLE IF EXISTS `t_role_attr`;
CREATE TABLE `t_role_attr` (
  `id` INT(32) NOT NULL AUTO_INCREMENT COMMENT '属性设置ID',
  `serverId` INT(32) DEFAULT NULL COMMENT '服务器ID',
  `roleId` VARCHAR(128) DEFAULT NULL COMMENT '角色ID',
  `attrType` INT(32) DEFAULT NULL COMMENT '属性类型',
  `attrValue` INT(32) DEFAULT NULL COMMENT '设置的属性值',
  `realValue` INT(32) DEFAULT NULL COMMENT '真实的属性值',
  `actionTime` DATETIME DEFAULT NULL COMMENT '执行时间',
  `reason` VARCHAR(128) DEFAULT NULL COMMENT '设置原因',
  `isDelete` INT(32) DEFAULT NULL COMMENT '是否删除，0 ：不删除， 1： 删除',
  `actionUser` VARCHAR(128) DEFAULT NULL COMMENT '操作者名字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '后台用户角色属性表';

-- ----------------------------
-- 27.Table structure for table `t_role_menu` 
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu` (
  `roleId` INT(32) DEFAULT NULL COMMENT '后台用户角色id',
  `menuId` INT(32) DEFAULT NULL COMMENT '菜单id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '角色和菜单关联表';

-- ----------------------------
-- 28.Table structure for table `t_role_transfer` 
-- ----------------------------
DROP TABLE IF EXISTS `t_role_transfer`;
CREATE TABLE `t_role_transfer` (
  `roleId` VARCHAR(50) NOT NULL COMMENT '被转移的角色ID',
  `srcUserId` VARCHAR(50) DEFAULT NULL COMMENT '被转移角色的原始帐号ID',
  `targetUserId` VARCHAR(50) DEFAULT NULL COMMENT '转移目标帐号ID',
  `serverId` INT(32) DEFAULT NULL COMMENT '区服',
  `reason` VARCHAR(300) NOT NULL COMMENT '转移原因',
  `isDeleted` TINYINT(4) NOT NULL COMMENT '是否生效,0为生效 1为无效',
  `time` INT(11) DEFAULT NULL COMMENT '操作时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '游戏角色转移表';

-- ----------------------------
-- 29.Table structure for table `t_server`
-- ----------------------------
DROP TABLE IF EXISTS `t_server`;
CREATE TABLE `t_server` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `serverId` int(32) DEFAULT NULL COMMENT '服务器ID',
  `serverName` varchar(128) DEFAULT NULL COMMENT '服务器名称',
  `groupName` varchar(128) DEFAULT NULL COMMENT '平台名',
  `WorldIP` varchar(128) DEFAULT NULL COMMENT '游戏服IP',
  `worldPort` int(32) DEFAULT NULL COMMENT '游戏服监听GM后台消息端口',
  `isHeFu` tinyint(4) NOT NULL DEFAULT '0' COMMENT '合服标识0:未合服1:合服',
  `hefuTime` datetime DEFAULT NULL COMMENT '合服时间',
  `hefuServerID` int(32) DEFAULT '0' COMMENT '合服目标服ID',
  `serverType` tinyint(4) NOT NULL DEFAULT '0' COMMENT '服务器标识 0:测试服1:正式服2:登录服3:世界服4:跨服',
  `isDeleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0:启用 1:删除 ',
  `isShow` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0为展示，1为不展示 ',
  `serverOpenTime` varchar(128) DEFAULT NULL COMMENT '开服时间',
  `openState` int(32) NOT NULL DEFAULT '0' COMMENT '服务器状态 0:备服状态 1:开服状态',
  `heartTime` varchar(128) DEFAULT NULL COMMENT '服务器最新心跳时间',
  `registerNum` int(11) NOT NULL DEFAULT '0' COMMENT '服务器注册人数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '服务器的地址表';

-- ----------------------------
-- 30.Table structure for table `t_servernum`
-- ----------------------------
DROP TABLE IF EXISTS `t_servernum`;
CREATE TABLE `t_servernum` (
  `id` INT(32) NOT NULL AUTO_INCREMENT,
  `serverId` INT(32) DEFAULT NULL COMMENT '服务器区号',
  `day` VARCHAR(50) DEFAULT NULL COMMENT '标记的日期',
  `hour` INT(32) DEFAULT NULL COMMENT '标记的小时',
  `min` INT(32) DEFAULT NULL COMMENT '标记的分钟',
  `num` INT(32) DEFAULT NULL COMMENT '服务器的人数',
  `time` INT(32) DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`),
  KEY `serverId_index` (`serverId`),
  KEY `time_index` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '服务器在线人数表';

-- ----------------------------
-- 31.Table structure for table `t_serverstate`
-- ----------------------------
DROP TABLE IF EXISTS `t_serverstate`;
CREATE TABLE `t_serverstate` (
  `id` INT(32) NOT NULL AUTO_INCREMENT,
  `serverId` INT(32) DEFAULT NULL COMMENT '服务器区号',
  `state` INT(32) DEFAULT NULL COMMENT '服务器的状态',
  `ip` VARCHAR(50) DEFAULT NULL COMMENT '服务器IP',
  `isConnectWord` INT(32) DEFAULT NULL COMMENT '是否连接好world',
  `currentNum` INT(32) DEFAULT NULL COMMENT '服务器的注册人数',
  `updateTime` VARCHAR(50) DEFAULT NULL COMMENT '最后一次更新的时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `serverId_index` (`serverId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '服务器状态表';

-- ----------------------------
-- 32.Table structure for table `t_switch`
-- ----------------------------
DROP TABLE IF EXISTS `t_switch`;
CREATE TABLE `t_switch` (
  `id` INT(32) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `state` INT(32) DEFAULT '0' COMMENT '0???1??',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '开关表';

-- ----------------------------
-- 33.Table structure for table `t_user` 
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` INT(32) NOT NULL AUTO_INCREMENT COMMENT '后台ID',
  `name` VARCHAR(50) NOT NULL COMMENT '账号名字',
  `passwd` VARCHAR(50) DEFAULT NULL COMMENT '密码',
  `salt` VARCHAR(50) DEFAULT NULL COMMENT '加盐算法',
  `ct` DATETIME DEFAULT NULL COMMENT '创建时间',
  `ut` DATETIME DEFAULT NULL COMMENT '最后修改时间',
  `ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
  `language` VARCHAR(20) DEFAULT NULL COMMENT '语言',
  `isDeleted` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '生效标记 0:生效1:无效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '后台账户表';

-- ----------------------------
-- 34.Table structure for table `t_user_role` 
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `userId` INT(32) DEFAULT NULL COMMENT '后台用户ID',
  `roleId` INT(32) DEFAULT NULL COMMENT '后台角色id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '后台用户角色关联表';

-- ----------------------------
-- 35.Table structure for table `t_whitelist`
-- ----------------------------
DROP TABLE IF EXISTS `t_whitelist`;
CREATE TABLE `t_whitelist` (
  `id` INT(32) NOT NULL AUTO_INCREMENT,
  `lsId` INT(32) DEFAULT NULL COMMENT '登录服ID',
  `whiteCon` VARCHAR(200) DEFAULT NULL COMMENT '白名单条件',
  `ctype` INT(32) DEFAULT NULL COMMENT '是添加还是删除',
  `createtime` VARCHAR(50) DEFAULT NULL COMMENT '创建时间',
  `userName` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `userIP` VARCHAR(50) DEFAULT NULL COMMENT '创建人的IP',
  `backStr` VARCHAR(500) DEFAULT NULL COMMENT '登陆服返回的结果',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '白名单表';

-- ----------------------------
-- 36.Table structure for table `t_activity_festival_type`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_festival_type`;
CREATE TABLE `t_activity_festival_type` (
  `id` int(32) DEFAULT NULL COMMENT '活动类型配置ID',
  `name` varchar(128) DEFAULT NULL COMMENT '活动类型名字'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '活动类型表';

-- ----------------------------
-- 37.Table structure for table `t_recharge_item`
-- ----------------------------
DROP TABLE IF EXISTS `t_recharge_item`;
CREATE TABLE `t_recharge_item` (
  `goods_id` int(32) NOT NULL DEFAULT '0' COMMENT '充值ID',
  `goods_system_cfg_id` int(32) DEFAULT NULL COMMENT '游戏内部配置ID',
  `goods_name` varchar(128) DEFAULT NULL COMMENT '商品名字描述（主要用于BI后台数据）',
  `goods_pay_channel` varchar(128) DEFAULT NULL COMMENT '渠道名称',
  `goods_pay_type` int(32) DEFAULT 0 NULL COMMENT '支付类型(第三方支付)',
  `goods_type` int(32) DEFAULT NULL COMMENT '充值类型',
  `goods_subtype` int(32) DEFAULT NULL COMMENT '充值子类型',
  `goods_limit` int(32) DEFAULT NULL COMMENT '充值次数（当前轮每个挡位对应充值的次数)',
  `goods_icon` int(32) DEFAULT NULL COMMENT '显示的图标的ID',
  `goods_price` varchar(1000) DEFAULT '' COMMENT '充值档位对应消耗的真实货币',
  `goods_price_point` varchar(500) DEFAULT '' COMMENT '充值计费点',
  `goods_show_price` varchar(128) DEFAULT '' COMMENT '界面默认显示的货币 例如:THB',
  `goods_reward` varchar(500) DEFAULT '' COMMENT '充值奖励',
  `goods_multiple` varchar(128) DEFAULT NULL COMMENT '充值奖励倍数',
  `goods_extra_reward` varchar(500) DEFAULT '' COMMENT '额外奖励',
  `goods_extra_reward_limit` int(32) DEFAULT NULL COMMENT '额外奖励次数',
  `isTotalRecharge` int(11) DEFAULT '0' COMMENT '是否计入到游戏累充活动',
  `totalVipPower` int(11) DEFAULT '0' COMMENT '是否增加VIP经验',
  PRIMARY KEY (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '充值配置数据表';


-- ----------------------------
-- 38.Table structure for table `t_model`
-- ----------------------------
DROP TABLE IF EXISTS `t_model`;
CREATE TABLE `t_model` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `career` varchar(500) DEFAULT NULL COMMENT '职业',
  `modelId` varchar(500) DEFAULT NULL COMMENT '模型ID',
  `scale` varchar(500) DEFAULT NULL COMMENT '模型大小倍数',
  `rotX` varchar(500) DEFAULT NULL COMMENT '对应的旋转参数x',
  `rotY` varchar(500) DEFAULT NULL COMMENT '对应的旋转参数y',
  `rotZ` varchar(500) DEFAULT NULL COMMENT '对应的旋转参数z',
  `posX` varchar(500) DEFAULT NULL COMMENT '对应的位置参数x',
  `posY` varchar(500) DEFAULT NULL COMMENT '对应的位置参数y',
  `tips` varchar(128) DEFAULT NULL COMMENT '模型库的备注说明',
  `modelData` varchar(1024) DEFAULT NULL COMMENT '发送给服务器的模型库数据',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '模型库';




-- =====================================================
-- Initialize database data
-- =====================================================

-- ----------------------------
-- 1.Data for the table `t_menu` 
-- ----------------------------
INSERT INTO t_menu(menuId, menuName, level, parentId, alias, urlPath, description, isDeleted) VALUES
(1, '后台管理', 1, 0, '', '', '', 0),
(2, '用户管理', 2, 1, '', '', '', 0),
(3, '用户列表', 3, 2, '', 'user', '', 0),
(7, '菜单列表', 3, 2, '', 'menu', '', 0),
(8, '数据查询', 1, 0, NULL, NULL, NULL, 0),
(9, '玩家数据', 2, 8, '', '', '', 0),
(10, '账号角色互查', 3, 9, '', 'role', '', 0),
(11, '运营管理', 1, 0, '', '', '', 0),
(12, '运营工具', 2, 11, '', '', '', 0),
(14, '激活码生成', 3, 12, '', 'operation/getPage?type=1', '', 0),
(16, '用户操作日志', 3, 2, '', 'admin/backendlog', '', 0),
(17, '日志管理', 1, 0, '', '', '', 0),
(20, '元宝变化日志', 2, 17, '', 'log/getPage?logType=2', '', 0),
(23, '留存统计', 3, 247, '', 'operation/getPage?type=7', '', 0),
(24, '公告管理', 2, 11, '', '', '', 0),
(25, '封禁管理', 2, 11, '', '', '', 0),
(26, '踢人下线', 3, 25, '', 'forbidden/kickPlayerPage', '', 0),
(27, '玩家禁言', 3, 25, '', 'forbidden/silence', '', 0),
(28, '玩家封号', 3, 25, '', 'forbidden/forbidAccount', '', 0),
(29, '禁言解除', 3, 25, '', 'forbidden/releaseSilence', '', 0),
(30, '帐号解封', 3, 25, '', 'forbidden/releaseForbidden', '', 0),
(31, '即时公告', 3, 24, '', 'announce/immediate', '', 0),
(32, '邮件管理', 2, 11, '', '', '', 0),
(34, '邮件列表', 3, 32, '', 'mail/mailList', '', 0),
(35, '邮件发送', 3, 32, '', 'mail/sendMail', '', 0),
(36, '循环公告', 3, 24, '', 'announce/cycleAnnounce', '', 0),
(37, '公会信息', 3, 9, '', 'log/guildPage?type=0', '', 0),
(38, '日常数据', 3, 247, '', 'statistic/getPage?statType=1', '', 0),
(40, '在线信息(单服)', 3, 247, '', 'operation/getPage?type=2', '', 0),
(41, '激活码查询', 3, 12, '', 'operation/getPage?type=3', '', 0),
(43, '运营活动', 2, 11, '', 'activity', '', 0),
(64, '服务器列表', 3, 108, '', 'server', '', 0),
(65, '日志库列表', 3, 108, '', 'dblog/log', '', 0),
(66, '白名单管理', 3, 25, '', 'forbidden/whiteList', '', 0),
(67, '公会成员信息', 3, 9, '', 'log/guildPage?type=1', '', 0),
(68, '公会动态信息', 3, 9, '', 'log/guildPage?type=2', '', 0),
(70, '商城购买统计', 3, 247, '', 'statistic/getPage?statType=2', '', 0),
(71, '等级分布', 3, 247, '', 'operation/getPage?type=6', '', 0),
(72, '职业分布', 3, 247, '', 'operation/getPage?type=5', '', 0),
(73, '排行榜', 2, 8, '', '', '', 0),
(75, '实时排行榜查询', 3, 73, '', 'rank/getPage?type=2', '', 0),
(76, '在线信息(全服)', 3, 247, '', 'operation/getPage?type=4', '', 0),
(78, '后台数据加载', 3, 108, '', 'admin/data', '', 0),
(79, 'DAU统计', 3, 247, '', 'dauStatistic/index', '', 0),
(80, '角色详情(角色ID)', 3, 9, '', 'rolelog', '', 0),
(86, '二次付费统计', 3, 247, '', 'secondRecharge/index', '', 0),
(87, '付费次数统计', 3, 247, '', 'rechargeCounts/index', '', 0),
(88, '累充统计', 3, 247, '', 'accumulateRecharge/index', '', 0),
(91, '后台充值', 2, 11, '', '', '', 0),
(92, '角色列表', 3, 2, '', 'backrole', '', 0),
(93, '系统开关', 3, 12, '', 'systemSwitch', '', 0),
(99, '数据统计', 1, 0, '', '', '', 0),
(107, '道具扣除', 3, 12, '', 'deductgolditem/item', '', 0),
(108, '运维工具', 2, 1, '', '', '', 0),
(109, '玩家流失统计', 3, 247, '', 'statistic/getPage?statType=8', '', 0),
(110, '玩家充值统计', 3, 247, '', 'paystatistic', '', 0),
(111, '充值排行榜', 3, 248, '', 'rank/getPage?type=8', '', 0),
(112, '绑定元宝统计', 3, 247, '', 'bindgold', '', 0),
(114, '黑名单', 3, 12, '', 'operation/getPage?type=8', '', 0),
(116, 'LTV统计', 3, 247, '', 'ltvstatistic', '', 0),
(117, '充值金额分布统计', 3, 247, '', 'paydiststatistic', '', 0),
(120, '在线时长统计', 3, 247, '', 'onlinestatistic', '', 0),
(121, '元宝用途统计', 3, 247, '', 'goldpurstatistic', '', 0),
(123, '屏蔽字管理', 3, 25, '', 'forbidden/shieldKeyword', '', 0),
(132, '全服角色信息统计', 3, 247, '', 'roleStatistic', '', 0),
(135, '跨服副本进入日志', 2, 17, '', 'log/getPage?logType=6', '', 0),
(141, '公共服房间创建日志', 2, 17, '', 'log/getPage?logType=10', '', 0),
(148, '提供对单个连接的数据库查询', 3, 108, '', 'server/forwardToCustomSqlPage', '', 0),
(149, '服务器指令', 3, 108, '', 'gm/gsCommand', '', 0),
(164, '付费总览', 3, 247, '', 'rechargeOverview/index', '', 0),
(165, '首充统计', 3, 247, '', 'firstRecharge/index', '', 0),
(166, '物品、货币流向统计', 3, 247, '', 'itemChange/index', '', 0),
(179, '商业化内容统计', 3, 247, '', 'businessContent/index', '', 0),
(182, '后台充值', 3, 91, '', 'recharge/getPage?type=1', '', 0),
(183, '后台充值审核', 3, 91, '', 'recharge/getPage?type=3', '', 0),
(184, '后台充值列表', 3, 91, '', 'recharge/getPage?type=4', '', 0),
(189, '成就奖励领取日志', 2, 17, '', 'log/getPage?logType=13', '', 0),
(190, '活动总览', 3, 43, NULL, 'activity/getPage?type=0', '', 0),
(191, '聊天日志', 2, 17, '', 'log/getPage?logType=21', '', 0),
(192, '后台指令日志', 2, 17, '', 'log/getPage?logType=22', '', 0),
(193, 'gm命令日志', 2, 17, '', 'log/getPage?logType=23', '', 0),
(194, '邮件日志', 2, 17, '', 'log/getPage?logType=24', '', 0),
(195, '排行榜日志', 2, 17, '', 'log/getPage?logType=25', '', 0),
(197, '反馈日志', 2, 17, '', 'log/getPage?logType=27', '', 0),
(198, '公共服指令', 3, 108, NULL, 'gm/psCommand', '', 0),
(199, '设置开服时间', 3, 108, NULL, 'gm/opstime', '', 0),
(200, '有奖问答统计', 3, 247, NULL, 'questionnaire/index', '', 0),
(201, '充值日志', 2, 17, NULL, 'log/getPage?logType=28', '', 0),
(202, '改名日志', 2, 17, NULL, 'log/getPage?logType=29', '', 0),
(203, '首领死亡复活日志', 2, 17, NULL, 'log/getPage?logType=30', '', 0),
(204, '物品变化日志', 2, 17, NULL, 'log/getPage?logType=3', '', 0),
(205, '超级邮件发送', 3, 32, NULL, 'mail/sendSuperMail', '', 0),
(206, '修改属性', 3, 12, NULL, 'roleattr/setAttr', '', 0),
(207, '角色转移', 3, 12, NULL, 'transfer', '', 0),
(208, '商城配置', 3, 12, NULL, 'shop', '', 0),
(209, '游戏库列表', 3, 108, NULL, 'dblog/game', '', 0),
(210, '货币变化日志', 2, 17, NULL, 'log/getPage?logType=31', '', 0),
(211, '禁言替换字', 3, 25, NULL, 'forbidden/chatreplaceword', '', 0),
(212, '聊天黑名单', 3, 25, NULL, 'forbidden/chatblacklist', '', 0),
(213, '更新公告', 3, 24, '', 'announce/updateNotice', '', 0),
(214, '活跃活动', 3, 43, '', 'activity/getPage?type=1', '', 0),
(215, '每日充值', 3, 43, '', 'activity/getPage?type=2', '', 0),
(216, '每日登陆', 3, 43, '', 'activity/getPage?type=3', '', 0),
(217, '限购礼包', 3, 43, '', 'activity/getPage?type=4', '', 0),
(218, '天帝宝库', 3, 43, '', 'activity/getPage?type=5', '', 0),
(219, '累计充值', 3, 43, '', 'activity/getPage?type=6', '', 0),
(220, '累计消耗', 3, 43, '', 'activity/getPage?type=7', '', 0),
(221, '集物兑换', 3, 43, '', 'activity/getPage?type=8', '', 0),
(222, '团购活动', 3, 43, '', 'activity/getPage?type=9', '', 0),
(223, '招 财 猫', 3, 43, '', 'activity/getPage?type=10', '', 0),
(224, '评价开关', 3, 12, '', 'evaluate/setEvaluate', '', 0),
(227, '首领狂欢', 3, 43, NULL, 'activity/getPage?type=11', NULL, 0),
(229, '庆典任务', 3, 43, NULL, 'activity/getPage?type=12', NULL, 0),
(231, '节日集字', 3, 43, NULL, 'activity/getPage?type=13', NULL, 0),
(232, '节日特惠', 3, 43, NULL, 'activity/getPage?type=14', NULL, 0),
(233, '连续累充', 3, 43, NULL, 'activity/getPage?type=15', NULL, 0),
(236, '限时商城', 3, 43, NULL, 'activity/getPage?type=16', NULL, 0),
(237, '节日礼包', 3, 43, NULL, 'activity/getPage?type=17', NULL, 0),
(238, '积分排名', 3, 43, NULL, 'activity/getPage?type=18', NULL, 0),
(239, '节日许愿', 3, 43, NULL, 'activity/getPage?type=19', NULL, 0),
(240, 'FB分享', 3, 43, NULL, 'activity/getPage?type=20', NULL, 0),
(242, '连续累充2(购买礼包)', 3, 43, NULL, 'activity/getPage?type=21', NULL, 0),
(243, '节日祝福', 3, 43, NULL, 'activity/getPage?type=22', NULL, 0),
(244, '掷骰子', 3, 43, NULL, 'activity/getPage?type=23', NULL, 0),
(245, '全服邮件发送', 3, 32, NULL, 'mail/sendAllMail', '', 0),
(246, '全服邮件列表', 3, 32, NULL, 'mail/allMailList', '', 0),
(247, '数据统计', 2, 99, NULL, '', '', 0),
(248, '排行榜', 2, 99, NULL, '', '', 0),
(249, '抽奖幸运值', 3, 43, NULL, 'activityConfig/luckyValue', '', 0),
(250, '模型库', 3, 43, NULL, 'activityConfig/model', '', 0),
(251, '充值配置', 3, 12, NULL, 'rechargeItem/rechargeItem', '', 0),
(252, '游戏信息配置', 3, 108, NULL, 'gameInfo', '', 0),
(253,'外观展示',3,43,NULL,'activity/getPage?type=24','',0),
(254,'登陆展示',3,43,NULL,'activity/getPage?type=25','',0),
(255,'跨服配置',3,12,NULL,'serverGroup/serverGroup','',0),
(256,'聚宝盆',3,43,NULL,'activity/getPage?type=26','',0),
(257,'幸运砸蛋',3,43,NULL,'activity/getPage?type=27','',0),
(258,'标签库',3,43,NULL,'activityConfig/tag','',0);

-- ----------------------------
-- 2.Data for the table `t_role`
-- ----------------------------
INSERT INTO t_role(roleId, roleName, createTime, description, isDeleted) VALUES
(1, 'admin', '2019-03-29 09:57:42', '', 0),
(2, 'test', '2021-03-12 13:56:41', '测试', 0);

-- ----------------------------
-- 3.Data for the table `t_role_menu`
-- ----------------------------
INSERT INTO t_role_menu(roleId, menuId) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 7),
(1, 16),
(1, 92),
(1, 108),
(1, 64),
(1, 65),
(1, 78),
(1, 148),
(1, 149),
(1, 198),
(1, 199),
(1, 209),
(1, 8),
(1, 9),
(1, 10),
(1, 37),
(1, 67),
(1, 68),
(1, 80),
(1, 73),
(1, 75),
(1, 11),
(1, 12),
(1, 14),
(1, 41),
(1, 93),
(1, 107),
(1, 114),
(1, 206),
(1, 207),
(1, 208),
(1, 224),
(1, 251),
(1, 24),
(1, 31),
(1, 36),
(1, 213),
(1, 25),
(1, 26),
(1, 27),
(1, 28),
(1, 29),
(1, 30),
(1, 66),
(1, 123),
(1, 211),
(1, 212),
(1, 32),
(1, 34),
(1, 35),
(1, 205),
(1, 245),
(1, 246),
(1, 43),
(1, 214),
(1, 215),
(1, 216),
(1, 217),
(1, 218),
(1, 219),
(1, 220),
(1, 221),
(1, 222),
(1, 223),
(1, 227),
(1, 229),
(1, 231),
(1, 232),
(1, 233),
(1, 236),
(1, 237),
(1, 238),
(1, 239),
(1, 240),
(1, 242),
(1, 243),
(1, 244),
(1, 249),
(1, 250),
(1, 91),
(1, 182),
(1, 183),
(1, 184),
(1, 17),
(1, 20),
(1, 135),
(1, 141),
(1, 189),
(1, 191),
(1, 192),
(1, 193),
(1, 194),
(1, 195),
(1, 197),
(1, 201),
(1, 202),
(1, 203),
(1, 204),
(1, 210),
(1, 99),
(1, 247),
(1, 23),
(1, 38),
(1, 40),
(1, 70),
(1, 71),
(1, 72),
(1, 76),
(1, 79),
(1, 86),
(1, 87),
(1, 88),
(1, 109),
(1, 110),
(1, 112),
(1, 116),
(1, 117),
(1, 120),
(1, 121),
(1, 132),
(1, 164),
(1, 165),
(1, 166),
(1, 179),
(1, 200),
(1, 248),
(1, 111);

-- ----------------------------
-- 4. Data for the table `t_user`
-- ----------------------------
INSERT INTO t_user(id, name, passwd, salt, ct, ut, ip, language, isDeleted) VALUES
(1, 'admin', 'd07cdad6541113846231843042a18eeb', 'y5dLD', '2019-03-29 09:46:12', '2021-04-09 14:48:09', '10.0.1.182', 'zh_CN', 0),
(2, 'test', '60384a5f57e4f7f56106bd006ecd47d6', 'hjsHY', '2020-01-14 20:37:31', '2021-03-02 11:06:41', '10.0.1.182', 'zh_CN', 0),
(3, 'developer', '45f43b28b1a6128dbcfd8e65bd61e84c', '5xeFm', '2020-11-12 10:14:48', '2020-11-12 14:47:43', '10.0.1.98', 'zh_CN', 0),
(4, 'om', 'd5705b9c5663e3eb9ea2cc1d26b88f47', 'yCEgy', '2020-11-12 10:18:25', '2020-11-12 10:18:25', NULL, 'zh_CN', 0),
(5, 'ceshi1', '26ea06b1dd04994523666e77abb69cb9', 'YaGXB', '2021-03-09 20:27:25', '2021-03-09 20:27:25', NULL, 'zh_CN', 0),
(6, 'ceshi2', 'e6c8697c55d02f2d0de88328184342ab', 'BPBX4', '2021-03-09 20:27:37', '2021-03-09 20:27:37', NULL, 'zh_CN', 0),
(7, 'ceshi3', '13af000cf2920ba45d78a8104bc4e99b', 'RAblK', '2021-03-09 20:29:04', '2021-03-09 20:29:04', NULL, 'zh_CN', 0),
(8, 'ceshi4', '7f11b4c89f6aa33641c08b3ba49543e1', 'nGKR9', '2021-03-09 20:29:11', '2021-03-09 20:29:11', NULL, 'zh_CN', 0),
(9, 'ceshi5', '1855736b65387457754d3fc8b3b80d2e', 'Wp2Pu', '2021-03-09 20:29:19', '2021-03-09 20:29:19', NULL, 'zh_CN', 0),
(10, 'ceshi6', '95b1d6796fcd447044cb400fad2249f7', 'MyrA8', '2021-03-09 20:29:26', '2021-03-09 20:29:26', NULL, 'zh_CN', 0),
(11, 'ceshi7', 'c8fe89a78e998f7435f081de22e971bf', 'EFxg6', '2021-03-09 20:29:48', '2021-03-09 20:29:48', NULL, 'zh_CN', 0);

-- ----------------------------
-- 5.Data for the table `t_user_role` 
-- ----------------------------
INSERT INTO t_user_role(userId, roleId) VALUES
(1, 1);

-- ----------------------------
-- 6. Data for the table `t_activity_festival_type`
-- ----------------------------
INSERT  INTO `t_activity_festival_type`(`id`,`name`) VALUES 
(0,'普通活动'),
(1,'元旦'),
(2,'情人节'),
(3,'妇女节'),
(4,'愚人节'),
(5,'劳动节'),
(6,'儿童节'),
(7,'教师节'),
(8,'圣诞节'),
(9,'新年'),
(10,'元宵节'),
(11,'清明节'),
(12,'端午节'),
(13,'七夕'),
(14,'中秋节'),
(15,'重阳节'),
(16,'腊八节'),
(17,'除夕');
