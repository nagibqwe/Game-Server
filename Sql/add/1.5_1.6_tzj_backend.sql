/*be changed list*/
/* 
//1.6.0_2021.05.27_0.24.0530_main:


//trunk:

*/

/*The following statements need to be executed*/
/*1.  "TableName1" "COMMENT1" */

/*2.  "TableName2" "COMMENT2" */

/*3.  "TableName3" "COMMENT3" */

/*Data for the table `t_recharge_item` */
ALTER TABLE `t_recharge_item` CHANGE `id` `goods_id` INT(32) DEFAULT 0 NOT NULL COMMENT '充值ID';
ALTER TABLE `t_recharge_item` CHANGE `systemCfgId` `goods_system_cfg_id` int(32) DEFAULT NULL COMMENT '游戏内部配置ID';
ALTER TABLE `t_recharge_item` CHANGE `goodsDesc` `goods_name` varchar(128) DEFAULT NULL COMMENT '商品名字描述（主要用于BI后台数据）';
ALTER TABLE `t_recharge_item` CHANGE `goods_pay_channel` `goods_pay_channel` varchar(128) DEFAULT NULL COMMENT '渠道名称';
ALTER TABLE `t_recharge_item` CHANGE `type` `goods_type` INT(32) DEFAULT NULL COMMENT '充值类型';
ALTER TABLE `t_recharge_item` CHANGE `subType` `goods_subtype` INT(32) DEFAULT NULL COMMENT '充值子类型';
ALTER TABLE `t_recharge_item` CHANGE `goodsLimit` `goods_limit` INT(32) DEFAULT NULL COMMENT '充值次数（当前轮每个挡位对应充值的次数)';
ALTER TABLE `t_recharge_item` CHANGE `icon` `goods_icon` INT(32) DEFAULT NULL COMMENT '显示的图标的ID';
ALTER TABLE `t_recharge_item` CHANGE `money` `goods_price` varchar(1000) DEFAULT '' COMMENT '充值档位对应消耗的真实货币';
ALTER TABLE `t_recharge_item` CHANGE `moneyPoint` `goods_price_point` varchar(500) DEFAULT '' COMMENT '充值计费点';
ALTER TABLE `t_recharge_item` CHANGE `showMoneyIcon` `goods_show_price` varchar(128) DEFAULT '' COMMENT '界面默认显示的货币 例如:THB';
ALTER TABLE `t_recharge_item` CHANGE `reward` `goods_reward` varchar(500) DEFAULT '' COMMENT '充值奖励';
ALTER TABLE `t_recharge_item` CHANGE `multiple` `goods_multiple` varchar(128) DEFAULT NULL COMMENT '充值奖励倍数';
ALTER TABLE `t_recharge_item` CHANGE `extraReward` `goods_extra_reward` varchar(500) DEFAULT '' COMMENT '额外奖励';
ALTER TABLE `t_recharge_item` CHANGE `extraRewardCount` `goods_extra_reward_limit` INT(32) DEFAULT NULL COMMENT '额外奖励次数';


ALTER TABLE `game_info` ADD COLUMN `autoFirstServerId` INT DEFAULT 0 NOT NULL COMMENT '自动开服起始服务器ID' AFTER `rechargeSecretkey`, ADD COLUMN `autoUserCount` INT DEFAULT 0 NOT NULL COMMENT '自动开服注册人数条件' AFTER `autoFirstServerId`; 


/*Data for the table `t_menu` */
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
) ENGINE=INNODB AUTO_INCREMENT=210 DEFAULT CHARSET=utf8;

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
(255,'跨服配置',3,12,NULL,'serverGroup/serverGroup','',0);