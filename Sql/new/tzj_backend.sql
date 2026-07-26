-- phpMyAdmin SQL Dump
-- version 5.2.1deb3
-- https://www.phpmyadmin.net/
--
-- Хост: localhost:3306
-- Время создания: Июл 02 2026 г., 22:55
-- Версия сервера: 10.11.14-MariaDB-0ubuntu0.24.04.1
-- Версия PHP: 8.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `tzj_backend`
--

-- --------------------------------------------------------

--
-- Структура таблицы `channel`
--

CREATE TABLE `channel` (
  `id` int(32) NOT NULL COMMENT '渠道id',
  `name` varchar(128) DEFAULT NULL COMMENT '渠道名'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='渠道表';

-- --------------------------------------------------------

--
-- Структура таблицы `db_game`
--

CREATE TABLE `db_game` (
  `id` int(32) NOT NULL,
  `serverName` varchar(128) NOT NULL COMMENT '服务器名称',
  `serverId` int(32) DEFAULT NULL COMMENT '服务器ID',
  `groupName` varchar(128) DEFAULT NULL COMMENT '平台名',
  `serverIpPort` varchar(200) DEFAULT NULL COMMENT '服务器IP及端口',
  `dbname` varchar(128) DEFAULT NULL COMMENT '数据库名称',
  `dbuser` varchar(128) DEFAULT NULL COMMENT '数据库用户名',
  `dbpassword` varchar(100) DEFAULT NULL COMMENT '数据库密码',
  `owerlist` varchar(200) DEFAULT NULL COMMENT '合服列表',
  `isHeFu` tinyint(4) NOT NULL DEFAULT 0 COMMENT '合服标识 0:未合服 1:已合服',
  `hefuServerID` int(32) DEFAULT 0 COMMENT '合服目标服ID',
  `hefuTime` datetime DEFAULT NULL COMMENT '合服时间',
  `serverType` tinyint(4) NOT NULL DEFAULT 0 COMMENT '服务器类型 0:测试服 1:正式服 2:登录服 3:跨服',
  `isDeleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除 0:启用 1:删除 '
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据库和服务器表(UNUSE)';

-- --------------------------------------------------------

--
-- Структура таблицы `game_info`
--

CREATE TABLE `game_info` (
  `gameId` int(11) NOT NULL COMMENT '游戏ID',
  `rechargeVersion` varchar(50) DEFAULT NULL COMMENT '充值配置版本号',
  `rechargeCurrency` varchar(50) DEFAULT NULL COMMENT '充值货币币种',
  `rechargeSecretkey` varchar(50) DEFAULT NULL COMMENT '第三方充值密钥',
  `autoFirstServerId` int(11) NOT NULL DEFAULT 0 COMMENT '自动开服起始服务器ID',
  `autoUserCount` int(11) NOT NULL DEFAULT 0 COMMENT '自动开服注册人数条件',
  `autoServerId` int(11) DEFAULT 0 COMMENT '自动开服ID',
  `time` bigint(64) DEFAULT NULL COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏自动开服信息表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_activity`
--

CREATE TABLE `t_activity` (
  `id` int(32) NOT NULL COMMENT '活动ID',
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
  `autoSend` int(32) DEFAULT 0 COMMENT '开服自动发布活动标识，0：否，1：是',
  `isOpenServer` int(32) DEFAULT 0 COMMENT '是否是新服活动',
  `submitBeginTime` varchar(128) DEFAULT NULL COMMENT '提交开始时间',
  `submitEndTime` varchar(128) DEFAULT NULL COMMENT '提交结束时间',
  `custom` text DEFAULT NULL COMMENT '自定义参数',
  `cover` int(32) DEFAULT NULL COMMENT '活动是否被覆盖正在进行的活动，0：否，1：是',
  `configData` text DEFAULT NULL COMMENT '配置参数，用于GM后台反解析'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运营活动表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_activity_boss_type`
--

CREATE TABLE `t_activity_boss_type` (
  `id` int(32) NOT NULL COMMENT '活动BOSS分类配置ID',
  `name` varchar(128) DEFAULT NULL COMMENT '后台显示的BOSS类型',
  `boss_id` text DEFAULT NULL COMMENT '对应的BOSSID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `t_activity_festival_type`
--

CREATE TABLE `t_activity_festival_type` (
  `id` int(32) DEFAULT NULL COMMENT '活动类型配置ID',
  `name` varchar(128) DEFAULT NULL COMMENT '活动类型名字'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动类型表';

--
-- Дамп данных таблицы `t_activity_festival_type`
--

INSERT INTO `t_activity_festival_type` (`id`, `name`) VALUES
(0, '普通活动'),
(1, '元旦'),
(2, '情人节'),
(3, '妇女节'),
(4, '愚人节'),
(5, '劳动节'),
(6, '儿童节'),
(7, '教师节'),
(8, '圣诞节'),
(9, '新年'),
(10, '元宵节'),
(11, '清明节'),
(12, '端午节'),
(13, '七夕'),
(14, '中秋节'),
(15, '重阳节'),
(16, '腊八节'),
(17, '除夕');

-- --------------------------------------------------------

--
-- Структура таблицы `t_activity_lucky_value`
--

CREATE TABLE `t_activity_lucky_value` (
  `id` int(32) NOT NULL COMMENT '活动ID',
  `totalLuckyValue` int(32) DEFAULT NULL COMMENT '总幸运值',
  `tips` varchar(128) DEFAULT NULL COMMENT '备注说明',
  `state` int(32) DEFAULT NULL COMMENT '活动状态，0：未验证(测试、删除)，1：已验证(发布、删除)，2：已发布(删除)，     //已过期(删除)这个通过活动结束时间去判断',
  `platform` varchar(300) DEFAULT NULL COMMENT '活动发布平台(groupName)(List JSON化后的字串[plat1,plat2,..])',
  `toSidList` varchar(500) DEFAULT NULL COMMENT '活动要发布到的区服列表(List JSON化后的字串[sid1,sid2,..])',
  `okSidList` varchar(500) DEFAULT NULL COMMENT '活动发布成功的区服列表(List JSON化后的字串[sid1,sid2,..])',
  `isDeleted` tinyint(8) DEFAULT NULL COMMENT '活动是否被删除，0：否，1：是',
  `cover` int(32) DEFAULT NULL COMMENT '活动是否被覆盖正在进行的活动，0：否，1：是'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `t_activity_template`
--

CREATE TABLE `t_activity_template` (
  `id` int(32) NOT NULL COMMENT '活动模板ID',
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
  `autoSend` int(32) DEFAULT 0 COMMENT '开服自动发布活动标识，0：否，1：是',
  `isOpenServer` int(32) DEFAULT 0 COMMENT '是否是新服活动',
  `custom` text DEFAULT NULL COMMENT '自定义参数',
  `description` text DEFAULT NULL COMMENT '模板描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运营活动模板表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_announce`
--

CREATE TABLE `t_announce` (
  `id` int(32) NOT NULL,
  `createTime` bigint(64) DEFAULT NULL COMMENT '公告创建时间',
  `createDate` varchar(128) DEFAULT NULL COMMENT '创建时间的字符串式',
  `userId` int(32) DEFAULT NULL COMMENT '创建者后台账号ID',
  `userName` char(100) DEFAULT NULL COMMENT '创建者后台名字',
  `groupName` varchar(128) DEFAULT NULL COMMENT '服务器组',
  `serverIds` text DEFAULT NULL COMMENT '服务器id',
  `type` int(32) DEFAULT NULL COMMENT '类型',
  `content` longtext DEFAULT NULL COMMENT '公告的内容',
  `reason` varchar(128) DEFAULT NULL COMMENT '原因'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_api_log`
--

CREATE TABLE `t_api_log` (
  `id` int(32) NOT NULL,
  `url` varchar(255) DEFAULT NULL COMMENT '请求URL',
  `params` text DEFAULT NULL COMMENT '参数',
  `result` text DEFAULT NULL COMMENT '结果',
  `type` int(32) DEFAULT NULL COMMENT 'API类型',
  `time` bigint(64) DEFAULT NULL COMMENT '操作时间 ',
  `ip` varchar(128) DEFAULT NULL COMMENT '请求IP '
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='api被调用的日志表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_backend_log`
--

CREATE TABLE `t_backend_log` (
  `id` int(32) NOT NULL,
  `userId` int(32) DEFAULT NULL COMMENT '后台用户ID',
  `userName` varchar(50) DEFAULT NULL COMMENT '后台用户名',
  `content` longtext DEFAULT NULL COMMENT '内容',
  `time` bigint(64) DEFAULT NULL COMMENT '操作时间',
  `ip` varchar(50) DEFAULT NULL COMMENT '操作IP'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台操作日志表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_blackip`
--

CREATE TABLE `t_blackip` (
  `id` int(32) NOT NULL COMMENT 'ID',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP类型的黑名单'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ip黑名单表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_blackuser`
--

CREATE TABLE `t_blackuser` (
  `id` int(32) NOT NULL COMMENT 'ID',
  `userNumber` bigint(64) DEFAULT NULL COMMENT '用户ID',
  `platform` varchar(50) DEFAULT NULL COMMENT '平台'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账号黑名单表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_changereason`
--

CREATE TABLE `t_changereason` (
  `id` int(32) DEFAULT NULL COMMENT '原因码id',
  `name` varchar(128) DEFAULT NULL COMMENT '原因码名字'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='改变原因码';

-- --------------------------------------------------------

--
-- Структура таблицы `t_code_batch`
--

CREATE TABLE `t_code_batch` (
  `id` int(32) NOT NULL,
  `batchId` int(32) DEFAULT NULL COMMENT '批号',
  `userId` int(32) DEFAULT NULL COMMENT '后台用户ID',
  `time` bigint(64) DEFAULT NULL COMMENT '添加时间',
  `platform` varchar(50) DEFAULT NULL COMMENT '平台',
  `isUniversal` tinyint(4) NOT NULL DEFAULT 0 COMMENT '万能码标识0:普通激活码1:万能码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='激活码添加日志表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_cyannounce`
--

CREATE TABLE `t_cyannounce` (
  `id` int(32) NOT NULL COMMENT '公告的编号',
  `groupName` varchar(128) DEFAULT NULL COMMENT '公告的平台分组',
  `serverIds` varchar(100) DEFAULT NULL COMMENT '公告的发送的服务器列表',
  `batchTag` varchar(128) DEFAULT NULL COMMENT '公告的标识',
  `content` text DEFAULT NULL COMMENT '公告的内容',
  `createTime` bigint(64) DEFAULT NULL COMMENT '公告的创建时间',
  `createDate` varchar(128) DEFAULT NULL COMMENT '公告的创建时间字符格式化',
  `createUserId` int(32) DEFAULT NULL COMMENT '公告的添加者ID',
  `createUserName` varchar(128) DEFAULT NULL COMMENT '公告的添加者名字',
  `fromTime` bigint(64) DEFAULT NULL COMMENT '公告的开始时间',
  `fromDate` varchar(128) DEFAULT NULL COMMENT '公告的开始字符格式化',
  `toTime` bigint(64) DEFAULT NULL COMMENT '公告的结束时间',
  `toDate` varchar(128) DEFAULT NULL COMMENT '公告的结束时间字符格式化',
  `totalTimes` int(32) DEFAULT NULL COMMENT '公告发送的总次数',
  `nowTimes` bigint(64) DEFAULT NULL COMMENT '公告的当前已经发送的次数',
  `nextTimes` bigint(64) DEFAULT NULL COMMENT '公告的下一次发送的时间',
  `nextDate` varchar(128) DEFAULT NULL COMMENT '公告的下一次发送时间字符格式化',
  `state` int(32) DEFAULT NULL COMMENT '公告的当前状态，启用还是禁用',
  `cycleInterval` int(32) DEFAULT NULL COMMENT '公告发送的频率',
  `type` int(32) DEFAULT NULL COMMENT '公告发送的位置'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='计时频率公告的数据结构表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_dblog`
--

CREATE TABLE `t_dblog` (
  `id` int(32) NOT NULL,
  `serverId` int(32) DEFAULT NULL COMMENT '服务器ID',
  `serverName` varchar(128) DEFAULT NULL COMMENT '服务器名称',
  `groupName` varchar(128) DEFAULT NULL COMMENT '平台名',
  `type` int(32) DEFAULT 0 COMMENT '类型：0游戏库，1日志库',
  `serverIpPort` varchar(200) DEFAULT NULL COMMENT '服务器IP及端口',
  `dbname` varchar(128) DEFAULT NULL COMMENT '数据库名称',
  `dbuser` varchar(128) DEFAULT NULL COMMENT '数据库用户名',
  `dbpassword` varchar(100) DEFAULT NULL COMMENT '数据库密码',
  `owerlist` varchar(200) DEFAULT NULL COMMENT '合服列表',
  `isHeFu` tinyint(4) NOT NULL DEFAULT 0 COMMENT '合服标识 0:未合服 1:已合服',
  `hefuServerID` int(32) DEFAULT 0 COMMENT '合服目标服ID',
  `hefuTime` datetime DEFAULT NULL COMMENT '合服时间',
  `serverType` tinyint(4) NOT NULL DEFAULT 0 COMMENT '服务器类型 0:测试服 1:正式服 2:登录服 3:跨服',
  `isDeleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除 0:启用 1:删除 ',
  `serverOpenTime` varchar(128) DEFAULT NULL COMMENT '开服时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务器数据库表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_deduct_item`
--

CREATE TABLE `t_deduct_item` (
  `id` int(32) NOT NULL COMMENT '道具扣除ID',
  `serverId` int(32) DEFAULT NULL COMMENT '服务ID',
  `itemId` int(32) DEFAULT NULL COMMENT '物品ID',
  `roleId` varchar(128) DEFAULT NULL COMMENT '角色ID',
  `dedCount` int(32) DEFAULT NULL COMMENT '欲扣除的数量',
  `realCount` int(32) DEFAULT NULL COMMENT '真实扣除的数量',
  `isMail` int(32) DEFAULT NULL COMMENT '是否发送邮件，0 不发送 1 发送',
  `isBind` tinyint(1) DEFAULT NULL COMMENT '是否绑定 true 绑定，false 不绑定',
  `dedTime` datetime DEFAULT NULL COMMENT '扣除时间',
  `reason` varchar(128) DEFAULT NULL COMMENT '扣除原因',
  `mailTitle` varchar(128) DEFAULT NULL COMMENT '邮件标题',
  `mailContent` varchar(128) DEFAULT NULL COMMENT '邮件标题',
  `isDelete` int(32) DEFAULT NULL COMMENT '是否删除，0 ：不删除， 1： 删除',
  `sendUser` varchar(128) DEFAULT NULL COMMENT '发起者名字'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='道具扣除表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_errorlog`
--

CREATE TABLE `t_errorlog` (
  `id` int(32) NOT NULL,
  `receTime` varchar(50) DEFAULT NULL COMMENT '接收错误日志的时间',
  `serverId` int(32) DEFAULT NULL COMMENT '接收的服务器ID',
  `platform` varchar(50) DEFAULT NULL COMMENT '接收的平台编号',
  `type` int(32) DEFAULT NULL COMMENT '错误类型数字编号',
  `mKey` varchar(200) DEFAULT NULL COMMENT '错误关键字',
  `content` text DEFAULT NULL COMMENT '错误的具体 信息',
  `lastValue` bigint(64) DEFAULT NULL COMMENT '错误产生的数量',
  `demo` text DEFAULT NULL COMMENT '备注，用于记录此条有没有做过其它操作',
  `state` int(32) DEFAULT NULL COMMENT '当前标志位'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务器发来的错误日志表' ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- Структура таблицы `t_evaluate`
--

CREATE TABLE `t_evaluate` (
  `id` int(32) NOT NULL COMMENT '评价ID',
  `serverId` int(32) DEFAULT NULL COMMENT '服务器ID',
  `eType` int(32) DEFAULT NULL COMMENT '评价类型',
  `state` tinyint(1) DEFAULT NULL COMMENT '开关状态',
  `actionTime` datetime DEFAULT NULL COMMENT '执行时间',
  `reason` varchar(128) DEFAULT NULL COMMENT '设置原因',
  `isDelete` int(32) DEFAULT NULL COMMENT '是否删除，0 ：不删除， 1： 删除',
  `actionUser` varchar(128) DEFAULT NULL COMMENT '操作者名字'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `t_forbidchat`
--

CREATE TABLE `t_forbidchat` (
  `id` bigint(64) NOT NULL,
  `userId` varchar(200) DEFAULT NULL COMMENT '聊天禁言的账号',
  `crimeType` int(32) DEFAULT NULL COMMENT '违规类型 1黑色产业 2不良信息',
  `forbidType` int(32) DEFAULT NULL COMMENT '禁言类型 1:工作室禁言2:全文替换禁言3:关键字替换禁言4:常规禁言5:隐形禁言6:隔离禁言',
  `createTime` varchar(128) DEFAULT NULL COMMENT '创建的时间',
  `endTime` varchar(128) DEFAULT NULL COMMENT '封号的结束时间',
  `backUserName` varchar(120) DEFAULT NULL COMMENT '后台那个管理人员添加的',
  `backMUserName` varchar(120) DEFAULT NULL COMMENT '后台那个管理人员修改',
  `reason` varchar(600) DEFAULT NULL COMMENT '操作的理由',
  `serverIds` varchar(100) DEFAULT NULL COMMENT '发送到游戏服列表',
  `state` int(32) DEFAULT NULL COMMENT '删除状态值'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天禁言表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_forbiduser`
--

CREATE TABLE `t_forbiduser` (
  `id` bigint(64) NOT NULL,
  `userId` varchar(200) DEFAULT NULL COMMENT '封号的条件',
  `createTime` varchar(50) DEFAULT NULL COMMENT '创建的时间',
  `endTime` varchar(50) DEFAULT NULL COMMENT '封号的结束时间',
  `backUserName` varchar(120) DEFAULT NULL COMMENT '后台那个管理人员添加的',
  `backMUserName` varchar(120) DEFAULT NULL COMMENT '后台那个管理人员修改',
  `reason` varchar(600) DEFAULT NULL COMMENT '操作的理由',
  `lsIds` varchar(100) DEFAULT NULL COMMENT '登录服ID',
  `state` int(32) DEFAULT NULL COMMENT '删除状态值',
  `backStr` varchar(600) DEFAULT NULL COMMENT '登录返回的结果'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='封号表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_function`
--

CREATE TABLE `t_function` (
  `funcId` int(32) DEFAULT NULL COMMENT '功能Id',
  `funcName` varchar(128) DEFAULT NULL COMMENT '功能名',
  `parentId` int(32) DEFAULT NULL COMMENT '父Id',
  `openState` int(32) DEFAULT NULL COMMENT '开启状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏功能表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_gm_log`
--

CREATE TABLE `t_gm_log` (
  `id` int(32) NOT NULL,
  `action` varchar(100) DEFAULT NULL COMMENT '操作命令',
  `params` varchar(500) DEFAULT NULL COMMENT '参数',
  `serverName` varchar(100) DEFAULT NULL COMMENT '服务器名',
  `serverId` int(32) DEFAULT NULL COMMENT '服务器ID',
  `isOk` tinyint(1) DEFAULT NULL COMMENT '成功失败',
  `result` text DEFAULT NULL COMMENT '处理结果',
  `operDate` datetime DEFAULT NULL COMMENT '操作时间',
  `user` varchar(50) DEFAULT NULL COMMENT '后台用户',
  `ip` varchar(50) DEFAULT NULL COMMENT 'ip',
  `gmType` tinyint(4) NOT NULL DEFAULT 0 COMMENT 'GM命令类型 0:游戏服GM(socket) 1:跨服或登录服GM(http)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='GM命令操作表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_item`
--

CREATE TABLE `t_item` (
  `itemId` int(32) NOT NULL COMMENT '物品Id',
  `itemName` varchar(128) DEFAULT NULL COMMENT '物品名',
  `itemType` int(32) DEFAULT NULL COMMENT '物品类型',
  `color` int(32) DEFAULT NULL COMMENT '物品颜色'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='物品装备表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_mail`
--

CREATE TABLE `t_mail` (
  `id` bigint(64) NOT NULL,
  `groupName` varchar(128) DEFAULT NULL COMMENT '平台名字',
  `serverId` int(32) DEFAULT NULL COMMENT '服务器编号',
  `roleIds` text NOT NULL COMMENT '角色ID列表',
  `title` varchar(120) NOT NULL COMMENT '邮件标题',
  `content` text NOT NULL COMMENT '邮件内容',
  `items` varchar(500) DEFAULT NULL COMMENT '邮件附件物品列表',
  `reason` varchar(300) NOT NULL COMMENT '邮件发送理由',
  `createDate` varchar(128) DEFAULT NULL COMMENT '邮件创建时间',
  `createUser` varchar(128) DEFAULT NULL COMMENT '邮件创建的后台账号名',
  `adminUser` varchar(128) DEFAULT NULL COMMENT '邮件审核的后台账号名',
  `adminDate` varchar(128) DEFAULT NULL COMMENT '邮件审核的日期',
  `adminState` int(32) DEFAULT NULL COMMENT '审核是否通过',
  `sendState` int(32) DEFAULT NULL COMMENT '发送到游戏服的状态值',
  `sendErrorMess` varchar(300) DEFAULT NULL COMMENT '发送到服务返回的结果信息',
  `isDelete` int(32) DEFAULT NULL COMMENT '邮件的删除标志',
  `sended` int(32) DEFAULT NULL COMMENT '是否已经发送过'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮件发送记录表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_mail_all`
--

CREATE TABLE `t_mail_all` (
  `id` bigint(64) NOT NULL,
  `groupName` varchar(128) DEFAULT NULL COMMENT '平台名字',
  `minLv` int(32) DEFAULT NULL COMMENT '最小等级',
  `maxLv` int(32) DEFAULT NULL COMMENT '最大等级',
  `career` int(32) DEFAULT NULL COMMENT '职业',
  `serverIdList` varchar(128) DEFAULT NULL COMMENT '服务器列表',
  `title` varchar(120) NOT NULL COMMENT '邮件标题',
  `content` text NOT NULL COMMENT '邮件内容',
  `items` varchar(500) DEFAULT NULL COMMENT '邮件附件物品列表',
  `reason` varchar(300) NOT NULL COMMENT '邮件发送理由',
  `createDate` varchar(128) DEFAULT NULL COMMENT '邮件创建时间',
  `createUser` varchar(128) DEFAULT NULL COMMENT '邮件创建的后台账号名',
  `adminUser` varchar(128) DEFAULT NULL COMMENT '邮件审核的后台账号名',
  `adminDate` varchar(128) DEFAULT NULL COMMENT '邮件审核的日期',
  `adminState` int(32) DEFAULT NULL COMMENT '审核是否通过',
  `sendState` int(32) DEFAULT NULL COMMENT '发送到游戏服的状态值',
  `sendErrorMess` varchar(300) DEFAULT NULL COMMENT '发送到服务返回的结果信息',
  `isDelete` int(32) DEFAULT NULL COMMENT '邮件的删除标志',
  `sended` int(32) DEFAULT NULL COMMENT '是否已经发送过'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `t_menu`
--

CREATE TABLE `t_menu` (
  `menuId` int(32) NOT NULL COMMENT '菜单ID',
  `menuName` varchar(50) DEFAULT NULL COMMENT '菜单名',
  `level` tinyint(4) NOT NULL DEFAULT 3 COMMENT '菜单级别',
  `parentId` int(32) DEFAULT NULL COMMENT '菜单父ID',
  `alias` varchar(50) DEFAULT NULL COMMENT '菜单别名',
  `urlPath` varchar(50) DEFAULT NULL COMMENT '菜单路径',
  `description` text DEFAULT NULL COMMENT '描述',
  `isDeleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='功能菜单表';

--
-- Дамп данных таблицы `t_menu`
--

INSERT INTO `t_menu` (`menuId`, `menuName`, `level`, `parentId`, `alias`, `urlPath`, `description`, `isDeleted`) VALUES
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
(253, '外观展示', 3, 43, NULL, 'activity/getPage?type=24', '', 0),
(254, '登陆展示', 3, 43, NULL, 'activity/getPage?type=25', '', 0),
(255, '跨服配置', 3, 12, NULL, 'serverGroup/serverGroup', '', 0),
(256, '聚宝盆', 3, 43, NULL, 'activity/getPage?type=26', '', 0),
(257, '幸运砸蛋', 3, 43, NULL, 'activity/getPage?type=27', '', 0),
(258, '标签库', 3, 43, NULL, 'activityConfig/tag', '', 0);

-- --------------------------------------------------------

--
-- Структура таблицы `t_model`
--

CREATE TABLE `t_model` (
  `id` int(32) NOT NULL COMMENT 'ID',
  `career` varchar(500) DEFAULT NULL COMMENT '职业',
  `modelId` varchar(500) DEFAULT NULL COMMENT '模型ID',
  `scale` varchar(500) DEFAULT NULL COMMENT '模型大小倍数',
  `rotX` varchar(500) DEFAULT NULL COMMENT '对应的旋转参数x',
  `rotY` varchar(500) DEFAULT NULL COMMENT '对应的旋转参数y',
  `rotZ` varchar(500) DEFAULT NULL COMMENT '对应的旋转参数z',
  `posX` varchar(500) DEFAULT NULL COMMENT '对应的位置参数x',
  `posY` varchar(500) DEFAULT NULL COMMENT '对应的位置参数y',
  `tips` varchar(128) DEFAULT NULL COMMENT '模型库的备注说明',
  `modelData` varchar(1024) DEFAULT NULL COMMENT '发送给服务器的模型库数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模型库';

-- --------------------------------------------------------

--
-- Структура таблицы `t_recharge`
--

CREATE TABLE `t_recharge` (
  `id` int(32) NOT NULL COMMENT '充值ID',
  `serverId` int(32) DEFAULT NULL COMMENT '充值服务器id',
  `roleId` varchar(50) DEFAULT NULL COMMENT '游戏角色ID',
  `rechargeNumber` int(32) DEFAULT NULL COMMENT '充值数量',
  `rechargeTotalGold` int(11) DEFAULT NULL COMMENT '充值累积数量',
  `rechargeVipExp` int(11) DEFAULT NULL COMMENT '充值VIP经验',
  `platformName` varchar(50) DEFAULT NULL COMMENT '平台名字',
  `createUser` varchar(50) DEFAULT NULL COMMENT '创建者',
  `createTime` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `rechargeState` int(32) DEFAULT NULL COMMENT '充值状态,0为待审核，1为通过，2为失败',
  `approvalUser` varchar(50) DEFAULT NULL COMMENT '审核者',
  `approvalTime` varchar(50) DEFAULT NULL COMMENT '审核时间',
  `reason` varchar(50) DEFAULT NULL COMMENT '充值理由'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值审核表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_recharge_item`
--

CREATE TABLE `t_recharge_item` (
  `goods_id` int(32) NOT NULL DEFAULT 0 COMMENT '充值ID',
  `goods_system_cfg_id` int(32) DEFAULT NULL COMMENT '游戏内部配置ID',
  `goods_name` varchar(128) DEFAULT NULL COMMENT '商品名字描述（主要用于BI后台数据）',
  `goods_pay_channel` varchar(128) DEFAULT NULL COMMENT '渠道名称',
  `goods_pay_type` int(32) DEFAULT 0 COMMENT '支付类型(第三方支付)',
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
  `isTotalRecharge` int(11) DEFAULT 0 COMMENT '是否计入到游戏累充活动',
  `totalVipPower` int(11) DEFAULT 0 COMMENT '是否增加VIP经验'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值配置数据表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_recharge_item_log`
--

CREATE TABLE `t_recharge_item_log` (
  `id` int(32) NOT NULL,
  `userId` int(32) DEFAULT NULL COMMENT '修改人id',
  `ip` varchar(128) DEFAULT NULL COMMENT '修改人IP',
  `userName` varchar(128) DEFAULT NULL COMMENT '修改人名',
  `time` bigint(64) DEFAULT NULL COMMENT '修改时间 ',
  `tableName` varchar(128) DEFAULT NULL COMMENT '操作表名',
  `content` longtext DEFAULT NULL COMMENT '操作内容(详情)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `t_role`
--

CREATE TABLE `t_role` (
  `roleId` int(32) NOT NULL COMMENT '角色id',
  `roleName` varchar(50) NOT NULL COMMENT '角色名称',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `description` varchar(50) DEFAULT NULL COMMENT '描述',
  `isDeleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '生效标记 0:生效1:无效'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台用户角色表';

--
-- Дамп данных таблицы `t_role`
--

INSERT INTO `t_role` (`roleId`, `roleName`, `createTime`, `description`, `isDeleted`) VALUES
(1, 'admin', '2019-03-29 09:57:42', '', 0),
(2, 'test', '2021-03-12 13:56:41', '测试', 0);

-- --------------------------------------------------------

--
-- Структура таблицы `t_role_attr`
--

CREATE TABLE `t_role_attr` (
  `id` int(32) NOT NULL COMMENT '属性设置ID',
  `serverId` int(32) DEFAULT NULL COMMENT '服务器ID',
  `roleId` varchar(128) DEFAULT NULL COMMENT '角色ID',
  `attrType` int(32) DEFAULT NULL COMMENT '属性类型',
  `attrValue` int(32) DEFAULT NULL COMMENT '设置的属性值',
  `realValue` int(32) DEFAULT NULL COMMENT '真实的属性值',
  `actionTime` datetime DEFAULT NULL COMMENT '执行时间',
  `reason` varchar(128) DEFAULT NULL COMMENT '设置原因',
  `isDelete` int(32) DEFAULT NULL COMMENT '是否删除，0 ：不删除， 1： 删除',
  `actionUser` varchar(128) DEFAULT NULL COMMENT '操作者名字'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台用户角色属性表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_role_menu`
--

CREATE TABLE `t_role_menu` (
  `roleId` int(32) DEFAULT NULL COMMENT '后台用户角色id',
  `menuId` int(32) DEFAULT NULL COMMENT '菜单id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色和菜单关联表';

--
-- Дамп данных таблицы `t_role_menu`
--

INSERT INTO `t_role_menu` (`roleId`, `menuId`) VALUES
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

-- --------------------------------------------------------

--
-- Структура таблицы `t_role_transfer`
--

CREATE TABLE `t_role_transfer` (
  `roleId` varchar(50) NOT NULL COMMENT '被转移的角色ID',
  `srcUserId` varchar(50) DEFAULT NULL COMMENT '被转移角色的原始帐号ID',
  `targetUserId` varchar(50) DEFAULT NULL COMMENT '转移目标帐号ID',
  `serverId` int(32) DEFAULT NULL COMMENT '区服',
  `reason` varchar(300) NOT NULL COMMENT '转移原因',
  `isDeleted` tinyint(4) NOT NULL COMMENT '是否生效,0为生效 1为无效',
  `time` int(11) DEFAULT NULL COMMENT '操作时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏角色转移表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_server`
--

CREATE TABLE `t_server` (
  `id` int(32) NOT NULL,
  `serverId` int(32) DEFAULT NULL COMMENT '服务器ID',
  `serverName` varchar(128) DEFAULT NULL COMMENT '服务器名称',
  `serverIP` varchar(64) DEFAULT '',
  `serverPort` int(11) DEFAULT 0,
  `groupName` varchar(128) DEFAULT NULL COMMENT '平台名',
  `WorldIP` varchar(128) DEFAULT NULL COMMENT '游戏服IP',
  `worldPort` int(32) DEFAULT NULL COMMENT '游戏服监听GM后台消息端口',
  `isHeFu` tinyint(4) NOT NULL DEFAULT 0 COMMENT '合服标识0:未合服1:合服',
  `hefuTime` datetime DEFAULT NULL COMMENT '合服时间',
  `hefuServerID` int(32) DEFAULT 0 COMMENT '合服目标服ID',
  `serverType` tinyint(4) NOT NULL DEFAULT 0 COMMENT '服务器标识 0:测试服1:正式服2:登录服3:世界服4:跨服',
  `isDeleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除 0:启用 1:删除 ',
  `isShow` tinyint(4) NOT NULL DEFAULT 0 COMMENT '0为展示，1为不展示 ',
  `serverOpenTime` varchar(128) DEFAULT NULL COMMENT '开服时间',
  `openState` int(32) NOT NULL DEFAULT 0 COMMENT '服务器状态 0:备服状态 1:开服状态',
  `heartTime` varchar(128) DEFAULT NULL COMMENT '服务器最新心跳时间',
  `registerNum` int(11) NOT NULL DEFAULT 0 COMMENT '服务器注册人数',
  `dblogIp` varchar(64) DEFAULT '',
  `dblogPort` int(11) DEFAULT 0,
  `dblogName` varchar(64) DEFAULT NULL,
  `dblogUser` varchar(64) DEFAULT NULL,
  `dblogPwd` varchar(128) DEFAULT NULL,
  `serverIdList` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务器的地址表';

--
-- Дамп данных таблицы `t_server`
--

INSERT INTO `t_server` (`id`, `serverId`, `serverName`, `serverIP`, `serverPort`, `groupName`, `WorldIP`, `worldPort`, `isHeFu`, `hefuTime`, `hefuServerID`, `serverType`, `isDeleted`, `isShow`, `serverOpenTime`, `openState`, `heartTime`, `registerNum`, `dblogIp`, `dblogPort`, `dblogName`, `dblogUser`, `dblogPwd`, `serverIdList`) VALUES
(1, 1001, 'gameserver', '155.212.166.16', 8191, 'cn', '155.212.166.16', 8191, 0, NULL, 0, 1, 0, 1, '2023-05-12 00:00:00', 1, '2026-07-03 01:55:35', 1, '', 0, '', '', '', ''),
(2, 3000, 'Local AgentServer', '155.212.166.16', 3002, 'cn', '155.212.166.16', 3002, 0, NULL, 0, 2, 0, 1, '2026-07-03 01:05:34', 1, '2026-07-03 01:05:34', 0, '', 0, '', '', '', '');

-- --------------------------------------------------------

--
-- Структура таблицы `t_servernum`
--

CREATE TABLE `t_servernum` (
  `id` int(32) NOT NULL,
  `serverId` int(32) DEFAULT NULL COMMENT '服务器区号',
  `day` varchar(50) DEFAULT NULL COMMENT '标记的日期',
  `hour` int(32) DEFAULT NULL COMMENT '标记的小时',
  `min` int(32) DEFAULT NULL COMMENT '标记的分钟',
  `num` int(32) DEFAULT NULL COMMENT '服务器的人数',
  `time` int(32) DEFAULT NULL COMMENT '时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务器在线人数表';

--
-- Дамп данных таблицы `t_servernum`
--

INSERT INTO `t_servernum` (`id`, `serverId`, `day`, `hour`, `min`, `num`, `time`) VALUES
(1, 1001, '2026-07-02', 1, 3, 0, 1782943415),
(2, 1001, '2026-07-02', 1, 4, 0, 1782943475),
(3, 1001, '2026-07-02', 1, 5, 0, 1782943535),
(4, 1001, '2026-07-02', 1, 6, 0, 1782943595),
(5, 1001, '2026-07-02', 1, 7, 0, 1782943655),
(6, 1001, '2026-07-02', 1, 8, 0, 1782943715),
(7, 1001, '2026-07-02', 1, 9, 0, 1782943775),
(8, 1001, '2026-07-02', 1, 10, 0, 1782943835),
(9, 1001, '2026-07-02', 1, 11, 0, 1782943895),
(10, 1001, '2026-07-02', 1, 12, 0, 1782943955),
(11, 1001, '2026-07-02', 1, 13, 0, 1782944015),
(12, 1001, '2026-07-02', 1, 14, 0, 1782944075),
(13, 1001, '2026-07-02', 1, 15, 0, 1782944135),
(14, 1001, '2026-07-02', 1, 16, 0, 1782944195),
(15, 1001, '2026-07-02', 1, 17, 0, 1782944255),
(16, 1001, '2026-07-02', 1, 18, 0, 1782944315),
(17, 1001, '2026-07-02', 1, 19, 0, 1782944375),
(18, 1001, '2026-07-02', 1, 20, 0, 1782944435),
(19, 1001, '2026-07-02', 1, 21, 0, 1782944495),
(20, 1001, '2026-07-02', 1, 22, 0, 1782944555),
(21, 1001, '2026-07-02', 1, 23, 0, 1782944615),
(22, 1001, '2026-07-02', 1, 24, 0, 1782944675),
(23, 1001, '2026-07-02', 1, 25, 0, 1782944735),
(24, 1001, '2026-07-02', 1, 26, 0, 1782944795),
(25, 1001, '2026-07-02', 1, 27, 0, 1782944855),
(26, 1001, '2026-07-02', 1, 28, 0, 1782944915),
(27, 1001, '2026-07-02', 1, 29, 0, 1782944975),
(28, 1001, '2026-07-02', 1, 30, 0, 1782945035),
(29, 1001, '2026-07-02', 1, 31, 0, 1782945095),
(30, 1001, '2026-07-02', 1, 32, 0, 1782945155),
(31, 1001, '2026-07-02', 1, 33, 0, 1782945215),
(32, 1001, '2026-07-02', 1, 34, 0, 1782945275),
(33, 1001, '2026-07-02', 1, 35, 0, 1782945335),
(34, 1001, '2026-07-02', 1, 36, 0, 1782945395),
(35, 1001, '2026-07-02', 1, 37, 0, 1782945455),
(36, 1001, '2026-07-02', 1, 38, 0, 1782945515),
(37, 1001, '2026-07-02', 1, 39, 0, 1782945575),
(38, 1001, '2026-07-02', 1, 40, 0, 1782945635),
(39, 1001, '2026-07-02', 1, 41, 0, 1782945695),
(40, 1001, '2026-07-02', 1, 42, 0, 1782945755),
(41, 1001, '2026-07-02', 1, 43, 0, 1782945815),
(42, 1001, '2026-07-02', 1, 44, 0, 1782945875),
(43, 1001, '2026-07-02', 1, 45, 0, 1782945935),
(44, 1001, '2026-07-02', 1, 46, 0, 1782945995),
(45, 1001, '2026-07-02', 1, 47, 0, 1782946055),
(46, 1001, '2026-07-02', 1, 48, 0, 1782946115),
(47, 1001, '2026-07-02', 1, 49, 0, 1782946175),
(48, 1001, '2026-07-02', 1, 50, 0, 1782946235),
(49, 1001, '2026-07-02', 1, 51, 0, 1782946295),
(50, 1001, '2026-07-02', 1, 52, 0, 1782946355),
(51, 1001, '2026-07-02', 1, 53, 0, 1782946415),
(52, 1001, '2026-07-02', 1, 54, 0, 1782946475),
(53, 1001, '2026-07-02', 1, 55, 0, 1782946535),
(54, 1001, '2026-07-02', 1, 56, 0, 1782946595),
(55, 1001, '2026-07-02', 1, 57, 0, 1782946655),
(56, 1001, '2026-07-02', 1, 58, 0, 1782946715),
(57, 1001, '2026-07-02', 1, 59, 0, 1782946775),
(58, 1001, '2026-07-02', 2, 0, 0, 1782946835),
(59, 1001, '2026-07-02', 2, 1, 0, 1782946895),
(60, 1001, '2026-07-02', 2, 2, 0, 1782946955),
(61, 1001, '2026-07-02', 2, 3, 0, 1782947015),
(62, 1001, '2026-07-02', 2, 4, 0, 1782947075),
(63, 1001, '2026-07-02', 2, 5, 0, 1782947135),
(64, 1001, '2026-07-02', 2, 6, 0, 1782947171),
(65, 1001, '2026-07-02', 2, 6, 0, 1782947195),
(66, 1001, '2026-07-02', 2, 7, 0, 1782947255),
(67, 1001, '2026-07-02', 2, 8, 0, 1782947315),
(68, 1001, '2026-07-02', 2, 9, 0, 1782947375),
(69, 1001, '2026-07-02', 2, 10, 0, 1782947435),
(70, 1001, '2026-07-02', 2, 11, 0, 1782947495),
(71, 1001, '2026-07-02', 2, 12, 0, 1782947555),
(72, 1001, '2026-07-02', 2, 13, 0, 1782947615),
(73, 1001, '2026-07-02', 2, 14, 0, 1782947675),
(74, 1001, '2026-07-02', 2, 15, 0, 1782947735),
(75, 1001, '2026-07-02', 2, 16, 0, 1782947795),
(76, 1001, '2026-07-02', 2, 17, 0, 1782947855),
(77, 1001, '2026-07-02', 2, 18, 0, 1782947915),
(78, 1001, '2026-07-02', 2, 19, 0, 1782947975),
(79, 1001, '2026-07-02', 2, 20, 0, 1782948035),
(80, 1001, '2026-07-02', 2, 21, 0, 1782948095),
(81, 1001, '2026-07-02', 2, 22, 0, 1782948155),
(82, 1001, '2026-07-02', 2, 23, 0, 1782948215),
(83, 1001, '2026-07-02', 2, 24, 0, 1782948275),
(84, 1001, '2026-07-02', 2, 25, 0, 1782948335),
(85, 1001, '2026-07-02', 2, 26, 0, 1782948395),
(86, 1001, '2026-07-02', 2, 27, 0, 1782948455),
(87, 1001, '2026-07-02', 2, 28, 0, 1782948515),
(88, 1001, '2026-07-02', 2, 29, 0, 1782948575),
(89, 1001, '2026-07-02', 2, 30, 0, 1782948635),
(90, 1001, '2026-07-02', 2, 31, 0, 1782948695),
(91, 1001, '2026-07-02', 2, 32, 0, 1782948755),
(92, 1001, '2026-07-02', 2, 33, 0, 1782948815),
(93, 1001, '2026-07-02', 2, 34, 0, 1782948875),
(94, 1001, '2026-07-02', 2, 35, 0, 1782948935),
(95, 1001, '2026-07-02', 2, 36, 0, 1782948995),
(96, 1001, '2026-07-02', 2, 37, 0, 1782949055),
(97, 1001, '2026-07-02', 2, 38, 0, 1782949115),
(98, 1001, '2026-07-02', 2, 39, 0, 1782949175),
(99, 1001, '2026-07-02', 2, 40, 0, 1782949235),
(100, 1001, '2026-07-02', 2, 41, 0, 1782949295),
(101, 1001, '2026-07-02', 2, 42, 0, 1782949355),
(102, 1001, '2026-07-02', 2, 43, 0, 1782949415),
(103, 1001, '2026-07-02', 2, 44, 0, 1782949475),
(104, 1001, '2026-07-02', 2, 45, 0, 1782949535),
(105, 1001, '2026-07-02', 2, 46, 0, 1782949595),
(106, 1001, '2026-07-02', 2, 47, 0, 1782949655),
(107, 1001, '2026-07-02', 2, 48, 0, 1782949715),
(108, 1001, '2026-07-02', 2, 49, 0, 1782949775),
(109, 1001, '2026-07-02', 2, 50, 0, 1782949835),
(110, 1001, '2026-07-02', 2, 51, 0, 1782949895),
(111, 1001, '2026-07-02', 2, 52, 0, 1782949955),
(112, 1001, '2026-07-02', 2, 53, 0, 1782950015),
(113, 1001, '2026-07-02', 2, 54, 0, 1782950075),
(114, 1001, '2026-07-02', 2, 55, 0, 1782950135),
(115, 1001, '2026-07-02', 2, 56, 0, 1782950195),
(116, 1001, '2026-07-02', 2, 57, 0, 1782950255),
(117, 1001, '2026-07-02', 2, 58, 0, 1782950315),
(118, 1001, '2026-07-02', 2, 59, 0, 1782950375),
(119, 1001, '2026-07-02', 3, 0, 0, 1782950435),
(120, 1001, '2026-07-02', 3, 1, 0, 1782950495),
(121, 1001, '2026-07-02', 3, 2, 0, 1782950555),
(122, 1001, '2026-07-02', 3, 3, 0, 1782950615),
(123, 1001, '2026-07-02', 3, 4, 0, 1782950675),
(124, 1001, '2026-07-02', 3, 5, 0, 1782950735),
(125, 1001, '2026-07-02', 3, 6, 0, 1782950795),
(126, 1001, '2026-07-02', 3, 7, 0, 1782950855),
(127, 1001, '2026-07-02', 3, 8, 0, 1782950915),
(128, 1001, '2026-07-02', 3, 9, 0, 1782950975),
(129, 1001, '2026-07-02', 3, 10, 0, 1782951035),
(130, 1001, '2026-07-02', 3, 11, 0, 1782951095),
(131, 1001, '2026-07-02', 3, 12, 0, 1782951155),
(132, 1001, '2026-07-02', 3, 13, 0, 1782951215),
(133, 1001, '2026-07-02', 3, 14, 0, 1782951275),
(134, 1001, '2026-07-02', 3, 15, 0, 1782951335),
(135, 1001, '2026-07-02', 3, 16, 0, 1782951395),
(136, 1001, '2026-07-02', 3, 17, 0, 1782951455),
(137, 1001, '2026-07-02', 3, 18, 0, 1782951515),
(138, 1001, '2026-07-02', 3, 19, 0, 1782951575),
(139, 1001, '2026-07-02', 3, 20, 0, 1782951635),
(140, 1001, '2026-07-02', 3, 21, 0, 1782951695),
(141, 1001, '2026-07-02', 3, 22, 0, 1782951755),
(142, 1001, '2026-07-02', 3, 23, 0, 1782951815),
(143, 1001, '2026-07-02', 3, 24, 0, 1782951875),
(144, 1001, '2026-07-02', 3, 25, 0, 1782951935),
(145, 1001, '2026-07-02', 3, 26, 0, 1782951995),
(146, 1001, '2026-07-02', 3, 27, 0, 1782952055),
(147, 1001, '2026-07-02', 3, 28, 0, 1782952115),
(148, 1001, '2026-07-02', 3, 29, 0, 1782952175),
(149, 1001, '2026-07-02', 3, 30, 0, 1782952235),
(150, 1001, '2026-07-02', 3, 31, 0, 1782952295),
(151, 1001, '2026-07-02', 3, 32, 0, 1782952355),
(152, 1001, '2026-07-02', 3, 33, 0, 1782952415),
(153, 1001, '2026-07-02', 3, 34, 0, 1782952475),
(154, 1001, '2026-07-02', 3, 35, 0, 1782952535),
(155, 1001, '2026-07-02', 3, 36, 0, 1782952595),
(156, 1001, '2026-07-02', 3, 37, 0, 1782952655),
(157, 1001, '2026-07-02', 3, 38, 0, 1782952715),
(158, 1001, '2026-07-02', 3, 39, 0, 1782952775),
(159, 1001, '2026-07-02', 3, 40, 0, 1782952835),
(160, 1001, '2026-07-02', 3, 41, 0, 1782952895),
(161, 1001, '2026-07-02', 3, 42, 0, 1782952955),
(162, 1001, '2026-07-02', 3, 43, 0, 1782953015),
(163, 1001, '2026-07-02', 3, 44, 0, 1782953075),
(164, 1001, '2026-07-02', 3, 45, 0, 1782953135),
(165, 1001, '2026-07-02', 3, 46, 0, 1782953195),
(166, 1001, '2026-07-02', 3, 47, 0, 1782953255),
(167, 1001, '2026-07-02', 3, 48, 0, 1782953315),
(168, 1001, '2026-07-02', 3, 49, 0, 1782953375),
(169, 1001, '2026-07-02', 3, 50, 0, 1782953435),
(170, 1001, '2026-07-02', 3, 51, 0, 1782953495),
(171, 1001, '2026-07-02', 3, 52, 0, 1782953555),
(172, 1001, '2026-07-02', 3, 53, 0, 1782953615),
(173, 1001, '2026-07-02', 3, 54, 0, 1782953675),
(174, 1001, '2026-07-02', 3, 55, 0, 1782953735),
(175, 1001, '2026-07-02', 3, 56, 0, 1782953795),
(176, 1001, '2026-07-02', 3, 57, 0, 1782953855),
(177, 1001, '2026-07-02', 3, 58, 0, 1782953915),
(178, 1001, '2026-07-02', 3, 59, 0, 1782953975),
(179, 1001, '2026-07-02', 4, 0, 0, 1782954035),
(180, 1001, '2026-07-02', 4, 1, 0, 1782954095),
(181, 1001, '2026-07-02', 4, 2, 0, 1782954155),
(182, 1001, '2026-07-02', 4, 3, 0, 1782954215),
(183, 1001, '2026-07-02', 4, 4, 0, 1782954275),
(184, 1001, '2026-07-02', 4, 5, 0, 1782954335),
(185, 1001, '2026-07-02', 4, 6, 0, 1782954395),
(186, 1001, '2026-07-02', 4, 7, 0, 1782954455),
(187, 1001, '2026-07-02', 4, 8, 0, 1782954515),
(188, 1001, '2026-07-02', 4, 9, 0, 1782954575),
(189, 1001, '2026-07-02', 4, 10, 0, 1782954635),
(190, 1001, '2026-07-02', 4, 11, 0, 1782954695),
(191, 1001, '2026-07-02', 4, 12, 0, 1782954755),
(192, 1001, '2026-07-02', 4, 13, 0, 1782954815),
(193, 1001, '2026-07-02', 4, 14, 0, 1782954875),
(194, 1001, '2026-07-02', 4, 15, 0, 1782954935),
(195, 1001, '2026-07-02', 4, 16, 0, 1782954995),
(196, 1001, '2026-07-02', 4, 17, 0, 1782955055),
(197, 1001, '2026-07-02', 4, 18, 0, 1782955115),
(198, 1001, '2026-07-02', 4, 19, 0, 1782955175),
(199, 1001, '2026-07-02', 4, 20, 0, 1782955235),
(200, 1001, '2026-07-02', 4, 21, 0, 1782955295),
(201, 1001, '2026-07-02', 4, 22, 0, 1782955355),
(202, 1001, '2026-07-02', 4, 23, 0, 1782955415),
(203, 1001, '2026-07-02', 4, 24, 0, 1782955475),
(204, 1001, '2026-07-02', 4, 25, 0, 1782955535),
(205, 1001, '2026-07-02', 4, 26, 0, 1782955595),
(206, 1001, '2026-07-02', 4, 27, 0, 1782955655),
(207, 1001, '2026-07-02', 4, 28, 0, 1782955715),
(208, 1001, '2026-07-02', 4, 29, 0, 1782955775),
(209, 1001, '2026-07-02', 4, 30, 0, 1782955835),
(210, 1001, '2026-07-02', 4, 31, 0, 1782955895),
(211, 1001, '2026-07-02', 4, 32, 0, 1782955955),
(212, 1001, '2026-07-02', 4, 33, 0, 1782956015),
(213, 1001, '2026-07-02', 4, 34, 0, 1782956075),
(214, 1001, '2026-07-02', 4, 35, 0, 1782956135),
(215, 1001, '2026-07-02', 4, 36, 0, 1782956195),
(216, 1001, '2026-07-02', 4, 37, 0, 1782956255),
(217, 1001, '2026-07-02', 4, 38, 0, 1782956315),
(218, 1001, '2026-07-02', 4, 39, 0, 1782956375),
(219, 1001, '2026-07-02', 4, 40, 0, 1782956435),
(220, 1001, '2026-07-02', 4, 41, 0, 1782956495),
(221, 1001, '2026-07-02', 4, 42, 0, 1782956555),
(222, 1001, '2026-07-02', 4, 43, 0, 1782956615),
(223, 1001, '2026-07-02', 4, 44, 0, 1782956675),
(224, 1001, '2026-07-02', 4, 45, 0, 1782956735),
(225, 1001, '2026-07-02', 4, 46, 0, 1782956795),
(226, 1001, '2026-07-02', 4, 47, 0, 1782956855),
(227, 1001, '2026-07-02', 4, 48, 0, 1782956915),
(228, 1001, '2026-07-02', 4, 49, 0, 1782956975),
(229, 1001, '2026-07-02', 4, 50, 0, 1782957035),
(230, 1001, '2026-07-02', 4, 51, 0, 1782957095),
(231, 1001, '2026-07-02', 4, 52, 0, 1782957155),
(232, 1001, '2026-07-02', 4, 53, 0, 1782957215),
(233, 1001, '2026-07-02', 4, 54, 0, 1782957275),
(234, 1001, '2026-07-02', 4, 55, 0, 1782957335),
(235, 1001, '2026-07-02', 4, 56, 0, 1782957395),
(236, 1001, '2026-07-02', 4, 57, 0, 1782957455),
(237, 1001, '2026-07-02', 4, 58, 0, 1782957515),
(238, 1001, '2026-07-02', 4, 59, 0, 1782957575),
(239, 1001, '2026-07-02', 5, 0, 0, 1782957635),
(240, 1001, '2026-07-02', 5, 1, 0, 1782957695),
(241, 1001, '2026-07-02', 5, 2, 0, 1782957755),
(242, 1001, '2026-07-02', 5, 3, 0, 1782957815),
(243, 1001, '2026-07-02', 5, 4, 0, 1782957875),
(244, 1001, '2026-07-02', 5, 5, 0, 1782957935),
(245, 1001, '2026-07-02', 5, 6, 0, 1782957995),
(246, 1001, '2026-07-02', 5, 7, 0, 1782958055),
(247, 1001, '2026-07-02', 5, 8, 0, 1782958115),
(248, 1001, '2026-07-02', 5, 9, 0, 1782958175),
(249, 1001, '2026-07-02', 5, 10, 0, 1782958235),
(250, 1001, '2026-07-02', 5, 11, 0, 1782958295),
(251, 1001, '2026-07-02', 5, 12, 0, 1782958355),
(252, 1001, '2026-07-02', 5, 13, 0, 1782958415),
(253, 1001, '2026-07-02', 5, 14, 0, 1782958475),
(254, 1001, '2026-07-02', 5, 15, 0, 1782958535),
(255, 1001, '2026-07-02', 5, 16, 0, 1782958595),
(256, 1001, '2026-07-02', 5, 17, 0, 1782958655),
(257, 1001, '2026-07-02', 5, 18, 0, 1782958715),
(258, 1001, '2026-07-02', 5, 19, 0, 1782958775),
(259, 1001, '2026-07-02', 5, 20, 0, 1782958835),
(260, 1001, '2026-07-02', 5, 21, 0, 1782958895),
(261, 1001, '2026-07-02', 5, 22, 0, 1782958955),
(262, 1001, '2026-07-02', 5, 23, 0, 1782959015),
(263, 1001, '2026-07-02', 5, 24, 0, 1782959075),
(264, 1001, '2026-07-02', 5, 25, 0, 1782959135),
(265, 1001, '2026-07-02', 5, 26, 0, 1782959195),
(266, 1001, '2026-07-02', 5, 27, 0, 1782959255),
(267, 1001, '2026-07-02', 5, 28, 0, 1782959315),
(268, 1001, '2026-07-02', 5, 29, 0, 1782959375),
(269, 1001, '2026-07-02', 5, 30, 0, 1782959435),
(270, 1001, '2026-07-02', 5, 31, 0, 1782959495),
(271, 1001, '2026-07-02', 5, 32, 0, 1782959555),
(272, 1001, '2026-07-02', 5, 33, 0, 1782959615),
(273, 1001, '2026-07-02', 5, 34, 0, 1782959675),
(274, 1001, '2026-07-02', 5, 35, 0, 1782959735),
(275, 1001, '2026-07-02', 5, 36, 0, 1782959795),
(276, 1001, '2026-07-02', 5, 37, 0, 1782959855),
(277, 1001, '2026-07-02', 5, 38, 0, 1782959915),
(278, 1001, '2026-07-02', 5, 39, 0, 1782959975),
(279, 1001, '2026-07-02', 5, 40, 0, 1782960035),
(280, 1001, '2026-07-02', 5, 41, 0, 1782960095),
(281, 1001, '2026-07-02', 5, 42, 0, 1782960155),
(282, 1001, '2026-07-02', 5, 43, 0, 1782960215),
(283, 1001, '2026-07-02', 5, 44, 0, 1782960275),
(284, 1001, '2026-07-02', 5, 45, 0, 1782960335),
(285, 1001, '2026-07-02', 5, 46, 0, 1782960395),
(286, 1001, '2026-07-02', 5, 47, 0, 1782960455),
(287, 1001, '2026-07-02', 5, 48, 0, 1782960515),
(288, 1001, '2026-07-02', 5, 49, 0, 1782960575),
(289, 1001, '2026-07-02', 5, 50, 0, 1782960635),
(290, 1001, '2026-07-02', 5, 51, 0, 1782960695),
(291, 1001, '2026-07-02', 5, 52, 0, 1782960755),
(292, 1001, '2026-07-02', 5, 53, 0, 1782960815),
(293, 1001, '2026-07-02', 5, 54, 0, 1782960875),
(294, 1001, '2026-07-02', 5, 55, 0, 1782960935),
(295, 1001, '2026-07-02', 5, 56, 0, 1782960995),
(296, 1001, '2026-07-02', 5, 57, 0, 1782961055),
(297, 1001, '2026-07-02', 5, 58, 0, 1782961115),
(298, 1001, '2026-07-02', 5, 59, 0, 1782961175),
(299, 1001, '2026-07-02', 6, 0, 0, 1782961235),
(300, 1001, '2026-07-02', 6, 1, 0, 1782961295),
(301, 1001, '2026-07-02', 6, 2, 0, 1782961355),
(302, 1001, '2026-07-02', 6, 3, 0, 1782961415),
(303, 1001, '2026-07-02', 6, 4, 0, 1782961475),
(304, 1001, '2026-07-02', 6, 5, 0, 1782961535),
(305, 1001, '2026-07-02', 6, 6, 0, 1782961595),
(306, 1001, '2026-07-02', 6, 7, 0, 1782961655),
(307, 1001, '2026-07-02', 6, 8, 0, 1782961715),
(308, 1001, '2026-07-02', 6, 9, 0, 1782961775),
(309, 1001, '2026-07-02', 6, 10, 0, 1782961835),
(310, 1001, '2026-07-02', 6, 11, 0, 1782961895),
(311, 1001, '2026-07-02', 6, 12, 0, 1782961955),
(312, 1001, '2026-07-02', 6, 13, 0, 1782962015),
(313, 1001, '2026-07-02', 6, 14, 0, 1782962075),
(314, 1001, '2026-07-02', 6, 15, 0, 1782962135),
(315, 1001, '2026-07-02', 6, 16, 0, 1782962195),
(316, 1001, '2026-07-02', 6, 17, 0, 1782962255),
(317, 1001, '2026-07-02', 6, 18, 0, 1782962315),
(318, 1001, '2026-07-02', 6, 19, 0, 1782962375),
(319, 1001, '2026-07-02', 6, 20, 0, 1782962435),
(320, 1001, '2026-07-02', 6, 21, 0, 1782962495),
(321, 1001, '2026-07-02', 6, 22, 0, 1782962555),
(322, 1001, '2026-07-02', 6, 23, 0, 1782962615),
(323, 1001, '2026-07-02', 6, 24, 0, 1782962675),
(324, 1001, '2026-07-02', 6, 25, 0, 1782962735),
(325, 1001, '2026-07-02', 6, 26, 0, 1782962795),
(326, 1001, '2026-07-02', 6, 27, 0, 1782962855),
(327, 1001, '2026-07-02', 6, 28, 0, 1782962915),
(328, 1001, '2026-07-02', 6, 29, 0, 1782962975),
(329, 1001, '2026-07-02', 6, 30, 0, 1782963035),
(330, 1001, '2026-07-02', 6, 31, 0, 1782963095),
(331, 1001, '2026-07-02', 6, 32, 0, 1782963155),
(332, 1001, '2026-07-02', 6, 33, 0, 1782963215),
(333, 1001, '2026-07-02', 6, 34, 0, 1782963275),
(334, 1001, '2026-07-02', 6, 35, 0, 1782963335),
(335, 1001, '2026-07-02', 6, 36, 0, 1782963395),
(336, 1001, '2026-07-02', 6, 37, 0, 1782963455),
(337, 1001, '2026-07-02', 6, 38, 0, 1782963515),
(338, 1001, '2026-07-02', 6, 39, 0, 1782963575),
(339, 1001, '2026-07-02', 6, 40, 0, 1782963635),
(340, 1001, '2026-07-02', 6, 41, 0, 1782963695),
(341, 1001, '2026-07-02', 6, 42, 0, 1782963755),
(342, 1001, '2026-07-02', 6, 43, 0, 1782963815),
(343, 1001, '2026-07-02', 6, 44, 0, 1782963875),
(344, 1001, '2026-07-02', 6, 45, 0, 1782963935),
(345, 1001, '2026-07-02', 6, 46, 0, 1782963995),
(346, 1001, '2026-07-02', 6, 47, 0, 1782964055),
(347, 1001, '2026-07-02', 6, 48, 0, 1782964115),
(348, 1001, '2026-07-02', 6, 49, 0, 1782964175),
(349, 1001, '2026-07-02', 6, 50, 0, 1782964235),
(350, 1001, '2026-07-02', 6, 51, 0, 1782964295),
(351, 1001, '2026-07-02', 6, 52, 0, 1782964355),
(352, 1001, '2026-07-02', 6, 53, 0, 1782964415),
(353, 1001, '2026-07-02', 6, 54, 0, 1782964475),
(354, 1001, '2026-07-02', 6, 55, 0, 1782964535),
(355, 1001, '2026-07-02', 6, 56, 0, 1782964595),
(356, 1001, '2026-07-02', 6, 57, 0, 1782964655),
(357, 1001, '2026-07-02', 6, 58, 0, 1782964715),
(358, 1001, '2026-07-02', 6, 59, 0, 1782964775),
(359, 1001, '2026-07-02', 7, 0, 0, 1782964835),
(360, 1001, '2026-07-02', 7, 1, 0, 1782964895),
(361, 1001, '2026-07-02', 7, 2, 0, 1782964955),
(362, 1001, '2026-07-02', 7, 3, 0, 1782965015),
(363, 1001, '2026-07-02', 7, 4, 0, 1782965075),
(364, 1001, '2026-07-02', 7, 5, 0, 1782965135),
(365, 1001, '2026-07-02', 7, 6, 0, 1782965195),
(366, 1001, '2026-07-02', 7, 7, 0, 1782965255),
(367, 1001, '2026-07-02', 7, 8, 0, 1782965315),
(368, 1001, '2026-07-02', 7, 9, 0, 1782965375),
(369, 1001, '2026-07-02', 7, 10, 0, 1782965435),
(370, 1001, '2026-07-02', 7, 11, 0, 1782965495),
(371, 1001, '2026-07-02', 7, 12, 0, 1782965555),
(372, 1001, '2026-07-02', 7, 13, 0, 1782965615),
(373, 1001, '2026-07-02', 7, 14, 0, 1782965675),
(374, 1001, '2026-07-02', 7, 15, 0, 1782965735),
(375, 1001, '2026-07-02', 7, 16, 0, 1782965795),
(376, 1001, '2026-07-02', 7, 17, 0, 1782965855),
(377, 1001, '2026-07-02', 7, 18, 0, 1782965915),
(378, 1001, '2026-07-02', 7, 19, 0, 1782965975),
(379, 1001, '2026-07-02', 7, 20, 0, 1782966035),
(380, 1001, '2026-07-02', 7, 21, 0, 1782966095),
(381, 1001, '2026-07-02', 7, 22, 0, 1782966155),
(382, 1001, '2026-07-02', 7, 23, 0, 1782966215),
(383, 1001, '2026-07-02', 7, 24, 0, 1782966275),
(384, 1001, '2026-07-02', 7, 25, 0, 1782966335),
(385, 1001, '2026-07-02', 7, 26, 0, 1782966395),
(386, 1001, '2026-07-02', 7, 27, 0, 1782966455),
(387, 1001, '2026-07-02', 7, 28, 0, 1782966515),
(388, 1001, '2026-07-02', 7, 29, 0, 1782966575),
(389, 1001, '2026-07-02', 7, 30, 0, 1782966635),
(390, 1001, '2026-07-02', 7, 31, 0, 1782966695),
(391, 1001, '2026-07-02', 7, 32, 0, 1782966755),
(392, 1001, '2026-07-02', 7, 33, 0, 1782966815),
(393, 1001, '2026-07-02', 7, 34, 0, 1782966875),
(394, 1001, '2026-07-02', 7, 35, 0, 1782966935),
(395, 1001, '2026-07-02', 7, 36, 0, 1782966995),
(396, 1001, '2026-07-02', 7, 37, 0, 1782967055),
(397, 1001, '2026-07-02', 7, 38, 0, 1782967115),
(398, 1001, '2026-07-02', 7, 39, 0, 1782967175),
(399, 1001, '2026-07-02', 7, 40, 0, 1782967235),
(400, 1001, '2026-07-02', 7, 41, 0, 1782967295),
(401, 1001, '2026-07-02', 7, 42, 0, 1782967355),
(402, 1001, '2026-07-02', 7, 43, 0, 1782967415),
(403, 1001, '2026-07-02', 7, 44, 0, 1782967475),
(404, 1001, '2026-07-02', 7, 45, 0, 1782967535),
(405, 1001, '2026-07-02', 7, 46, 0, 1782967595),
(406, 1001, '2026-07-02', 7, 47, 0, 1782967655),
(407, 1001, '2026-07-02', 7, 48, 0, 1782967715),
(408, 1001, '2026-07-02', 7, 49, 0, 1782967775),
(409, 1001, '2026-07-02', 7, 50, 0, 1782967835),
(410, 1001, '2026-07-02', 7, 51, 0, 1782967895),
(411, 1001, '2026-07-02', 7, 52, 0, 1782967955),
(412, 1001, '2026-07-02', 7, 53, 0, 1782968015),
(413, 1001, '2026-07-02', 7, 54, 0, 1782968075),
(414, 1001, '2026-07-02', 7, 55, 0, 1782968135),
(415, 1001, '2026-07-02', 7, 56, 0, 1782968195),
(416, 1001, '2026-07-02', 7, 57, 0, 1782968255),
(417, 1001, '2026-07-02', 7, 58, 0, 1782968315),
(418, 1001, '2026-07-02', 7, 59, 0, 1782968375),
(419, 1001, '2026-07-02', 8, 0, 0, 1782968435),
(420, 1001, '2026-07-02', 8, 1, 0, 1782968495),
(421, 1001, '2026-07-02', 8, 2, 0, 1782968555),
(422, 1001, '2026-07-02', 8, 3, 0, 1782968615),
(423, 1001, '2026-07-02', 8, 4, 0, 1782968675),
(424, 1001, '2026-07-02', 8, 5, 0, 1782968735),
(425, 1001, '2026-07-02', 8, 6, 0, 1782968795),
(426, 1001, '2026-07-02', 8, 7, 0, 1782968855),
(427, 1001, '2026-07-02', 8, 8, 0, 1782968915),
(428, 1001, '2026-07-02', 8, 9, 0, 1782968975),
(429, 1001, '2026-07-02', 8, 10, 0, 1782969035),
(430, 1001, '2026-07-02', 8, 11, 0, 1782969095),
(431, 1001, '2026-07-02', 8, 12, 0, 1782969155),
(432, 1001, '2026-07-02', 8, 13, 0, 1782969215),
(433, 1001, '2026-07-02', 8, 14, 0, 1782969275),
(434, 1001, '2026-07-02', 8, 15, 0, 1782969335),
(435, 1001, '2026-07-02', 8, 16, 0, 1782969395),
(436, 1001, '2026-07-02', 8, 17, 0, 1782969455),
(437, 1001, '2026-07-02', 8, 18, 0, 1782969515),
(438, 1001, '2026-07-02', 8, 19, 0, 1782969575),
(439, 1001, '2026-07-02', 8, 20, 0, 1782969635),
(440, 1001, '2026-07-02', 8, 21, 0, 1782969695),
(441, 1001, '2026-07-02', 8, 22, 0, 1782969755),
(442, 1001, '2026-07-02', 8, 23, 0, 1782969815),
(443, 1001, '2026-07-02', 8, 24, 0, 1782969875),
(444, 1001, '2026-07-02', 8, 25, 0, 1782969935),
(445, 1001, '2026-07-02', 8, 26, 0, 1782969995),
(446, 1001, '2026-07-02', 8, 27, 0, 1782970055),
(447, 1001, '2026-07-02', 8, 28, 0, 1782970115),
(448, 1001, '2026-07-02', 8, 29, 0, 1782970175),
(449, 1001, '2026-07-02', 8, 30, 0, 1782970235),
(450, 1001, '2026-07-02', 8, 31, 0, 1782970295),
(451, 1001, '2026-07-02', 8, 32, 0, 1782970355),
(452, 1001, '2026-07-02', 8, 33, 0, 1782970415),
(453, 1001, '2026-07-02', 8, 34, 0, 1782970475),
(454, 1001, '2026-07-02', 8, 35, 0, 1782970535),
(455, 1001, '2026-07-02', 8, 36, 0, 1782970595),
(456, 1001, '2026-07-02', 8, 37, 0, 1782970655),
(457, 1001, '2026-07-02', 8, 38, 0, 1782970715),
(458, 1001, '2026-07-02', 8, 39, 0, 1782970775),
(459, 1001, '2026-07-02', 8, 40, 0, 1782970835),
(460, 1001, '2026-07-02', 8, 41, 0, 1782970895),
(461, 1001, '2026-07-02', 8, 42, 0, 1782970955),
(462, 1001, '2026-07-02', 8, 43, 0, 1782971015),
(463, 1001, '2026-07-02', 8, 44, 0, 1782971075),
(464, 1001, '2026-07-02', 8, 45, 0, 1782971135),
(465, 1001, '2026-07-02', 8, 46, 0, 1782971195),
(466, 1001, '2026-07-02', 8, 47, 0, 1782971255),
(467, 1001, '2026-07-02', 8, 48, 0, 1782971315),
(468, 1001, '2026-07-02', 8, 49, 0, 1782971375),
(469, 1001, '2026-07-02', 8, 50, 0, 1782971435),
(470, 1001, '2026-07-02', 8, 51, 0, 1782971495),
(471, 1001, '2026-07-02', 8, 52, 0, 1782971555),
(472, 1001, '2026-07-02', 8, 53, 0, 1782971615),
(473, 1001, '2026-07-02', 8, 54, 0, 1782971675),
(474, 1001, '2026-07-02', 8, 55, 0, 1782971735),
(475, 1001, '2026-07-02', 8, 56, 0, 1782971795),
(476, 1001, '2026-07-02', 8, 57, 0, 1782971855),
(477, 1001, '2026-07-02', 8, 58, 0, 1782971915),
(478, 1001, '2026-07-02', 8, 59, 0, 1782971975),
(479, 1001, '2026-07-02', 9, 0, 0, 1782972035),
(480, 1001, '2026-07-02', 9, 1, 0, 1782972095),
(481, 1001, '2026-07-02', 9, 2, 0, 1782972155),
(482, 1001, '2026-07-02', 9, 3, 0, 1782972215),
(483, 1001, '2026-07-02', 9, 4, 0, 1782972275),
(484, 1001, '2026-07-02', 9, 5, 0, 1782972335),
(485, 1001, '2026-07-02', 9, 6, 0, 1782972395),
(486, 1001, '2026-07-02', 9, 7, 0, 1782972455),
(487, 1001, '2026-07-02', 9, 8, 0, 1782972515),
(488, 1001, '2026-07-02', 9, 9, 0, 1782972575),
(489, 1001, '2026-07-02', 9, 10, 0, 1782972635),
(490, 1001, '2026-07-02', 9, 11, 0, 1782972695),
(491, 1001, '2026-07-02', 9, 12, 0, 1782972755),
(492, 1001, '2026-07-02', 9, 13, 0, 1782972815),
(493, 1001, '2026-07-02', 9, 14, 0, 1782972875),
(494, 1001, '2026-07-02', 9, 15, 0, 1782972935),
(495, 1001, '2026-07-02', 9, 16, 0, 1782972995),
(496, 1001, '2026-07-02', 9, 17, 0, 1782973055),
(497, 1001, '2026-07-02', 9, 18, 0, 1782973115),
(498, 1001, '2026-07-02', 9, 19, 0, 1782973175),
(499, 1001, '2026-07-02', 9, 20, 0, 1782973235),
(500, 1001, '2026-07-02', 9, 21, 0, 1782973295),
(501, 1001, '2026-07-02', 9, 22, 0, 1782973355),
(502, 1001, '2026-07-02', 9, 23, 0, 1782973415),
(503, 1001, '2026-07-02', 9, 24, 0, 1782973475),
(504, 1001, '2026-07-02', 9, 25, 0, 1782973535),
(505, 1001, '2026-07-02', 9, 26, 0, 1782973595),
(506, 1001, '2026-07-02', 9, 27, 0, 1782973655),
(507, 1001, '2026-07-02', 9, 28, 0, 1782973715),
(508, 1001, '2026-07-02', 9, 29, 0, 1782973775),
(509, 1001, '2026-07-02', 9, 30, 0, 1782973835),
(510, 1001, '2026-07-02', 9, 31, 0, 1782973895),
(511, 1001, '2026-07-02', 9, 32, 0, 1782973955),
(512, 1001, '2026-07-02', 9, 33, 0, 1782974015),
(513, 1001, '2026-07-02', 9, 34, 0, 1782974075),
(514, 1001, '2026-07-02', 9, 35, 0, 1782974135),
(515, 1001, '2026-07-02', 9, 36, 0, 1782974195),
(516, 1001, '2026-07-02', 9, 37, 0, 1782974255),
(517, 1001, '2026-07-02', 9, 38, 0, 1782974315),
(518, 1001, '2026-07-02', 9, 39, 0, 1782974375),
(519, 1001, '2026-07-02', 9, 40, 0, 1782974435),
(520, 1001, '2026-07-02', 9, 41, 0, 1782974495),
(521, 1001, '2026-07-02', 9, 42, 0, 1782974555),
(522, 1001, '2026-07-02', 9, 43, 0, 1782974615),
(523, 1001, '2026-07-02', 9, 44, 0, 1782974675),
(524, 1001, '2026-07-02', 9, 45, 0, 1782974735),
(525, 1001, '2026-07-02', 9, 46, 0, 1782974795),
(526, 1001, '2026-07-02', 9, 47, 0, 1782974855),
(527, 1001, '2026-07-02', 9, 48, 0, 1782974915),
(528, 1001, '2026-07-02', 9, 49, 0, 1782974975),
(529, 1001, '2026-07-02', 9, 50, 0, 1782975035),
(530, 1001, '2026-07-02', 9, 51, 0, 1782975095),
(531, 1001, '2026-07-02', 9, 52, 0, 1782975155),
(532, 1001, '2026-07-02', 9, 53, 0, 1782975215),
(533, 1001, '2026-07-02', 9, 54, 0, 1782975275),
(534, 1001, '2026-07-02', 9, 55, 0, 1782975335),
(535, 1001, '2026-07-02', 9, 56, 0, 1782975395),
(536, 1001, '2026-07-02', 9, 57, 0, 1782975455),
(537, 1001, '2026-07-02', 9, 58, 0, 1782975515),
(538, 1001, '2026-07-02', 9, 59, 0, 1782975575),
(539, 1001, '2026-07-02', 10, 0, 0, 1782975635),
(540, 1001, '2026-07-02', 10, 1, 0, 1782975695),
(541, 1001, '2026-07-02', 10, 2, 0, 1782975755),
(542, 1001, '2026-07-02', 10, 3, 0, 1782975815),
(543, 1001, '2026-07-02', 10, 4, 0, 1782975875),
(544, 1001, '2026-07-02', 10, 5, 0, 1782975935),
(545, 1001, '2026-07-02', 10, 6, 0, 1782975995),
(546, 1001, '2026-07-02', 10, 7, 0, 1782976055),
(547, 1001, '2026-07-02', 10, 8, 0, 1782976115),
(548, 1001, '2026-07-02', 10, 9, 0, 1782976175),
(549, 1001, '2026-07-02', 10, 10, 0, 1782976235),
(550, 1001, '2026-07-02', 10, 11, 0, 1782976295),
(551, 1001, '2026-07-02', 10, 12, 0, 1782976355),
(552, 1001, '2026-07-02', 10, 13, 0, 1782976415),
(553, 1001, '2026-07-02', 10, 14, 0, 1782976475),
(554, 1001, '2026-07-02', 10, 15, 0, 1782976535),
(555, 1001, '2026-07-02', 10, 16, 0, 1782976595),
(556, 1001, '2026-07-02', 10, 17, 0, 1782976655),
(557, 1001, '2026-07-02', 10, 18, 0, 1782976715),
(558, 1001, '2026-07-02', 10, 19, 0, 1782976775),
(559, 1001, '2026-07-02', 10, 20, 0, 1782976835),
(560, 1001, '2026-07-02', 10, 21, 0, 1782976895),
(561, 1001, '2026-07-02', 10, 22, 0, 1782976955),
(562, 1001, '2026-07-02', 10, 23, 0, 1782977015),
(563, 1001, '2026-07-02', 10, 24, 0, 1782977075),
(564, 1001, '2026-07-02', 10, 25, 0, 1782977135),
(565, 1001, '2026-07-02', 10, 26, 0, 1782977195),
(566, 1001, '2026-07-02', 10, 27, 0, 1782977255),
(567, 1001, '2026-07-02', 10, 28, 0, 1782977315),
(568, 1001, '2026-07-02', 10, 29, 0, 1782977375),
(569, 1001, '2026-07-02', 10, 30, 0, 1782977435),
(570, 1001, '2026-07-02', 10, 31, 0, 1782977495),
(571, 1001, '2026-07-02', 10, 32, 0, 1782977555),
(572, 1001, '2026-07-02', 10, 33, 0, 1782977615),
(573, 1001, '2026-07-02', 10, 34, 0, 1782977675),
(574, 1001, '2026-07-02', 10, 35, 0, 1782977735),
(575, 1001, '2026-07-02', 10, 36, 0, 1782977795),
(576, 1001, '2026-07-02', 10, 37, 0, 1782977855),
(577, 1001, '2026-07-02', 10, 38, 0, 1782977915),
(578, 1001, '2026-07-02', 10, 39, 0, 1782977975),
(579, 1001, '2026-07-02', 10, 40, 0, 1782978035),
(580, 1001, '2026-07-02', 10, 41, 0, 1782978095),
(581, 1001, '2026-07-02', 10, 42, 0, 1782978155),
(582, 1001, '2026-07-02', 10, 43, 0, 1782978215),
(583, 1001, '2026-07-02', 10, 44, 0, 1782978275),
(584, 1001, '2026-07-02', 10, 45, 0, 1782978335),
(585, 1001, '2026-07-02', 10, 46, 0, 1782978395),
(586, 1001, '2026-07-02', 10, 47, 0, 1782978455),
(587, 1001, '2026-07-02', 10, 48, 0, 1782978515),
(588, 1001, '2026-07-02', 10, 49, 0, 1782978575),
(589, 1001, '2026-07-02', 10, 50, 0, 1782978635),
(590, 1001, '2026-07-02', 10, 51, 0, 1782978695),
(591, 1001, '2026-07-02', 10, 52, 0, 1782978755),
(592, 1001, '2026-07-02', 10, 53, 0, 1782978815),
(593, 1001, '2026-07-02', 10, 54, 0, 1782978875),
(594, 1001, '2026-07-02', 10, 55, 0, 1782978935),
(595, 1001, '2026-07-02', 10, 56, 0, 1782978995),
(596, 1001, '2026-07-02', 10, 57, 0, 1782979055),
(597, 1001, '2026-07-02', 10, 58, 0, 1782979115),
(598, 1001, '2026-07-02', 10, 59, 0, 1782979175),
(599, 1001, '2026-07-02', 11, 0, 0, 1782979235),
(600, 1001, '2026-07-02', 11, 1, 0, 1782979295),
(601, 1001, '2026-07-02', 11, 2, 0, 1782979355),
(602, 1001, '2026-07-02', 11, 3, 0, 1782979415),
(603, 1001, '2026-07-02', 11, 4, 0, 1782979475),
(604, 1001, '2026-07-02', 11, 5, 0, 1782979535),
(605, 1001, '2026-07-02', 11, 6, 0, 1782979595),
(606, 1001, '2026-07-02', 11, 7, 0, 1782979655),
(607, 1001, '2026-07-02', 11, 8, 0, 1782979715),
(608, 1001, '2026-07-02', 11, 9, 0, 1782979775),
(609, 1001, '2026-07-02', 11, 10, 0, 1782979835),
(610, 1001, '2026-07-02', 11, 11, 0, 1782979895),
(611, 1001, '2026-07-02', 11, 12, 0, 1782979955),
(612, 1001, '2026-07-02', 11, 13, 0, 1782980015),
(613, 1001, '2026-07-02', 11, 14, 0, 1782980075),
(614, 1001, '2026-07-02', 11, 15, 0, 1782980135),
(615, 1001, '2026-07-02', 11, 16, 0, 1782980195),
(616, 1001, '2026-07-02', 11, 17, 0, 1782980255),
(617, 1001, '2026-07-02', 11, 18, 0, 1782980315),
(618, 1001, '2026-07-02', 11, 19, 0, 1782980375),
(619, 1001, '2026-07-02', 11, 20, 0, 1782980435),
(620, 1001, '2026-07-02', 11, 21, 0, 1782980495),
(621, 1001, '2026-07-02', 11, 22, 0, 1782980555),
(622, 1001, '2026-07-02', 11, 23, 0, 1782980615),
(623, 1001, '2026-07-02', 11, 24, 0, 1782980675),
(624, 1001, '2026-07-02', 11, 25, 0, 1782980735),
(625, 1001, '2026-07-02', 11, 26, 0, 1782980795),
(626, 1001, '2026-07-02', 11, 27, 0, 1782980855),
(627, 1001, '2026-07-02', 11, 28, 0, 1782980915),
(628, 1001, '2026-07-02', 11, 29, 0, 1782980975),
(629, 1001, '2026-07-02', 11, 30, 0, 1782981035),
(630, 1001, '2026-07-02', 11, 31, 0, 1782981095),
(631, 1001, '2026-07-02', 11, 32, 0, 1782981155),
(632, 1001, '2026-07-02', 11, 33, 0, 1782981215),
(633, 1001, '2026-07-02', 11, 34, 0, 1782981275),
(634, 1001, '2026-07-02', 11, 35, 0, 1782981335),
(635, 1001, '2026-07-02', 11, 36, 0, 1782981395),
(636, 1001, '2026-07-02', 11, 37, 0, 1782981455),
(637, 1001, '2026-07-02', 11, 38, 0, 1782981515),
(638, 1001, '2026-07-02', 11, 39, 0, 1782981575),
(639, 1001, '2026-07-02', 11, 40, 0, 1782981635),
(640, 1001, '2026-07-02', 11, 41, 0, 1782981695),
(641, 1001, '2026-07-02', 11, 42, 0, 1782981755),
(642, 1001, '2026-07-02', 11, 43, 0, 1782981815),
(643, 1001, '2026-07-02', 11, 44, 0, 1782981875),
(644, 1001, '2026-07-02', 11, 45, 0, 1782981935),
(645, 1001, '2026-07-02', 11, 46, 0, 1782981995),
(646, 1001, '2026-07-02', 11, 47, 0, 1782982055),
(647, 1001, '2026-07-02', 11, 48, 0, 1782982115),
(648, 1001, '2026-07-02', 11, 49, 0, 1782982175),
(649, 1001, '2026-07-02', 11, 50, 0, 1782982235),
(650, 1001, '2026-07-02', 11, 51, 0, 1782982295),
(651, 1001, '2026-07-02', 11, 52, 0, 1782982355),
(652, 1001, '2026-07-02', 11, 53, 0, 1782982415),
(653, 1001, '2026-07-02', 11, 54, 0, 1782982475),
(654, 1001, '2026-07-02', 11, 55, 0, 1782982535),
(655, 1001, '2026-07-02', 11, 56, 0, 1782982595),
(656, 1001, '2026-07-02', 11, 57, 0, 1782982655),
(657, 1001, '2026-07-02', 11, 58, 0, 1782982715),
(658, 1001, '2026-07-02', 11, 59, 0, 1782982775),
(659, 1001, '2026-07-02', 12, 0, 0, 1782982835),
(660, 1001, '2026-07-02', 12, 1, 0, 1782982895),
(661, 1001, '2026-07-02', 12, 2, 0, 1782982955),
(662, 1001, '2026-07-02', 12, 3, 0, 1782983015),
(663, 1001, '2026-07-02', 12, 4, 0, 1782983075),
(664, 1001, '2026-07-02', 12, 5, 0, 1782983135),
(665, 1001, '2026-07-02', 12, 6, 0, 1782983195),
(666, 1001, '2026-07-02', 12, 7, 0, 1782983255),
(667, 1001, '2026-07-02', 12, 8, 0, 1782983315),
(668, 1001, '2026-07-02', 12, 9, 0, 1782983375),
(669, 1001, '2026-07-02', 12, 10, 0, 1782983435),
(670, 1001, '2026-07-02', 12, 11, 0, 1782983495),
(671, 1001, '2026-07-02', 12, 12, 0, 1782983555),
(672, 1001, '2026-07-02', 12, 13, 0, 1782983615),
(673, 1001, '2026-07-02', 12, 14, 0, 1782983675),
(674, 1001, '2026-07-02', 12, 15, 0, 1782983735),
(675, 1001, '2026-07-02', 12, 16, 0, 1782983795),
(676, 1001, '2026-07-02', 12, 17, 0, 1782983855),
(677, 1001, '2026-07-02', 12, 18, 0, 1782983915),
(678, 1001, '2026-07-02', 12, 19, 0, 1782983975),
(679, 1001, '2026-07-02', 12, 20, 0, 1782984035),
(680, 1001, '2026-07-02', 12, 21, 0, 1782984095),
(681, 1001, '2026-07-02', 12, 22, 0, 1782984155),
(682, 1001, '2026-07-02', 12, 23, 0, 1782984215),
(683, 1001, '2026-07-02', 12, 24, 0, 1782984275),
(684, 1001, '2026-07-02', 12, 25, 0, 1782984335),
(685, 1001, '2026-07-02', 12, 26, 0, 1782984395),
(686, 1001, '2026-07-02', 12, 27, 0, 1782984455),
(687, 1001, '2026-07-02', 12, 28, 0, 1782984515),
(688, 1001, '2026-07-02', 12, 29, 0, 1782984575),
(689, 1001, '2026-07-02', 12, 30, 0, 1782984635),
(690, 1001, '2026-07-02', 12, 31, 0, 1782984695),
(691, 1001, '2026-07-02', 12, 32, 0, 1782984755),
(692, 1001, '2026-07-02', 12, 33, 0, 1782984815),
(693, 1001, '2026-07-02', 12, 34, 0, 1782984875),
(694, 1001, '2026-07-02', 12, 35, 0, 1782984935),
(695, 1001, '2026-07-02', 12, 36, 0, 1782984995),
(696, 1001, '2026-07-02', 12, 37, 0, 1782985055),
(697, 1001, '2026-07-02', 12, 38, 0, 1782985115),
(698, 1001, '2026-07-02', 12, 39, 0, 1782985175),
(699, 1001, '2026-07-02', 12, 40, 0, 1782985235),
(700, 1001, '2026-07-02', 12, 41, 0, 1782985295),
(701, 1001, '2026-07-02', 12, 42, 0, 1782985355),
(702, 1001, '2026-07-02', 12, 43, 0, 1782985415),
(703, 1001, '2026-07-02', 12, 44, 0, 1782985475),
(704, 1001, '2026-07-02', 12, 45, 0, 1782985535),
(705, 1001, '2026-07-02', 12, 46, 0, 1782985595),
(706, 1001, '2026-07-02', 12, 47, 0, 1782985655),
(707, 1001, '2026-07-02', 12, 48, 0, 1782985715),
(708, 1001, '2026-07-02', 12, 49, 0, 1782985775),
(709, 1001, '2026-07-02', 12, 50, 0, 1782985835),
(710, 1001, '2026-07-02', 12, 51, 0, 1782985895),
(711, 1001, '2026-07-02', 12, 52, 0, 1782985955),
(712, 1001, '2026-07-02', 12, 53, 0, 1782986015),
(713, 1001, '2026-07-02', 12, 54, 0, 1782986075),
(714, 1001, '2026-07-02', 12, 55, 0, 1782986135),
(715, 1001, '2026-07-02', 12, 56, 0, 1782986195),
(716, 1001, '2026-07-02', 12, 57, 0, 1782986255),
(717, 1001, '2026-07-02', 12, 58, 0, 1782986315),
(718, 1001, '2026-07-02', 12, 59, 0, 1782986375),
(719, 1001, '2026-07-02', 13, 0, 0, 1782986435),
(720, 1001, '2026-07-02', 13, 1, 0, 1782986495),
(721, 1001, '2026-07-02', 13, 2, 0, 1782986555),
(722, 1001, '2026-07-02', 13, 3, 0, 1782986615),
(723, 1001, '2026-07-02', 13, 4, 0, 1782986675),
(724, 1001, '2026-07-02', 13, 5, 0, 1782986735),
(725, 1001, '2026-07-02', 13, 6, 0, 1782986795),
(726, 1001, '2026-07-02', 13, 7, 0, 1782986855),
(727, 1001, '2026-07-02', 13, 8, 0, 1782986915),
(728, 1001, '2026-07-02', 13, 9, 0, 1782986975),
(729, 1001, '2026-07-02', 13, 10, 0, 1782987035),
(730, 1001, '2026-07-02', 13, 11, 0, 1782987095),
(731, 1001, '2026-07-02', 13, 12, 0, 1782987155),
(732, 1001, '2026-07-02', 13, 13, 0, 1782987215),
(733, 1001, '2026-07-02', 13, 14, 0, 1782987275),
(734, 1001, '2026-07-02', 13, 15, 0, 1782987335),
(735, 1001, '2026-07-02', 13, 16, 0, 1782987395),
(736, 1001, '2026-07-02', 13, 17, 0, 1782987455),
(737, 1001, '2026-07-02', 13, 18, 0, 1782987515),
(738, 1001, '2026-07-02', 13, 19, 0, 1782987575),
(739, 1001, '2026-07-02', 13, 20, 0, 1782987635),
(740, 1001, '2026-07-02', 13, 21, 0, 1782987695),
(741, 1001, '2026-07-02', 13, 22, 0, 1782987755),
(742, 1001, '2026-07-02', 13, 23, 0, 1782987815),
(743, 1001, '2026-07-02', 13, 24, 0, 1782987875),
(744, 1001, '2026-07-02', 13, 25, 0, 1782987935),
(745, 1001, '2026-07-02', 13, 26, 0, 1782987995),
(746, 1001, '2026-07-02', 13, 27, 0, 1782988055),
(747, 1001, '2026-07-02', 13, 28, 0, 1782988115),
(748, 1001, '2026-07-02', 13, 29, 0, 1782988175),
(749, 1001, '2026-07-02', 13, 30, 0, 1782988235),
(750, 1001, '2026-07-02', 13, 31, 0, 1782988295),
(751, 1001, '2026-07-02', 13, 32, 0, 1782988355),
(752, 1001, '2026-07-02', 13, 33, 0, 1782988415),
(753, 1001, '2026-07-02', 13, 34, 0, 1782988475),
(754, 1001, '2026-07-02', 13, 35, 0, 1782988535),
(755, 1001, '2026-07-02', 13, 36, 0, 1782988595),
(756, 1001, '2026-07-02', 13, 37, 0, 1782988655),
(757, 1001, '2026-07-02', 13, 38, 0, 1782988715),
(758, 1001, '2026-07-02', 13, 39, 0, 1782988775),
(759, 1001, '2026-07-02', 13, 40, 0, 1782988835),
(760, 1001, '2026-07-02', 13, 41, 0, 1782988895),
(761, 1001, '2026-07-02', 13, 42, 0, 1782988955),
(762, 1001, '2026-07-02', 13, 43, 0, 1782989015),
(763, 1001, '2026-07-02', 13, 44, 0, 1782989075),
(764, 1001, '2026-07-02', 13, 45, 0, 1782989135),
(765, 1001, '2026-07-02', 13, 46, 0, 1782989195),
(766, 1001, '2026-07-02', 13, 47, 0, 1782989255),
(767, 1001, '2026-07-02', 13, 48, 0, 1782989315),
(768, 1001, '2026-07-02', 13, 49, 0, 1782989375),
(769, 1001, '2026-07-02', 13, 50, 0, 1782989435),
(770, 1001, '2026-07-02', 13, 51, 0, 1782989495),
(771, 1001, '2026-07-02', 13, 52, 0, 1782989555),
(772, 1001, '2026-07-02', 13, 53, 0, 1782989615),
(773, 1001, '2026-07-02', 13, 54, 0, 1782989675),
(774, 1001, '2026-07-02', 13, 55, 0, 1782989735),
(775, 1001, '2026-07-02', 13, 56, 0, 1782989795),
(776, 1001, '2026-07-02', 13, 57, 0, 1782989855),
(777, 1001, '2026-07-02', 13, 58, 0, 1782989915),
(778, 1001, '2026-07-02', 13, 59, 0, 1782989975),
(779, 1001, '2026-07-02', 14, 0, 0, 1782990035),
(780, 1001, '2026-07-02', 14, 1, 0, 1782990095),
(781, 1001, '2026-07-02', 14, 2, 0, 1782990155),
(782, 1001, '2026-07-02', 14, 3, 0, 1782990215),
(783, 1001, '2026-07-02', 14, 4, 0, 1782990275),
(784, 1001, '2026-07-02', 14, 5, 0, 1782990335),
(785, 1001, '2026-07-02', 14, 6, 0, 1782990395),
(786, 1001, '2026-07-02', 14, 7, 0, 1782990455),
(787, 1001, '2026-07-02', 14, 8, 0, 1782990515),
(788, 1001, '2026-07-02', 14, 9, 0, 1782990575),
(789, 1001, '2026-07-02', 14, 10, 0, 1782990635),
(790, 1001, '2026-07-02', 14, 11, 0, 1782990695),
(791, 1001, '2026-07-02', 14, 12, 0, 1782990755),
(792, 1001, '2026-07-02', 14, 13, 0, 1782990815),
(793, 1001, '2026-07-02', 14, 14, 0, 1782990875),
(794, 1001, '2026-07-02', 14, 15, 0, 1782990935),
(795, 1001, '2026-07-02', 14, 16, 0, 1782990995),
(796, 1001, '2026-07-02', 14, 17, 0, 1782991055),
(797, 1001, '2026-07-02', 14, 18, 0, 1782991115),
(798, 1001, '2026-07-02', 14, 19, 0, 1782991175),
(799, 1001, '2026-07-02', 14, 20, 0, 1782991235),
(800, 1001, '2026-07-02', 14, 21, 0, 1782991295),
(801, 1001, '2026-07-02', 14, 22, 0, 1782991355),
(802, 1001, '2026-07-02', 14, 23, 0, 1782991415),
(803, 1001, '2026-07-02', 14, 24, 0, 1782991475),
(804, 1001, '2026-07-02', 14, 25, 0, 1782991535),
(805, 1001, '2026-07-02', 14, 26, 0, 1782991595),
(806, 1001, '2026-07-02', 14, 27, 0, 1782991655),
(807, 1001, '2026-07-02', 14, 28, 0, 1782991715),
(808, 1001, '2026-07-02', 14, 29, 0, 1782991775),
(809, 1001, '2026-07-02', 14, 30, 0, 1782991835),
(810, 1001, '2026-07-02', 14, 31, 0, 1782991895),
(811, 1001, '2026-07-02', 14, 32, 0, 1782991955),
(812, 1001, '2026-07-02', 14, 33, 0, 1782992015),
(813, 1001, '2026-07-02', 14, 34, 0, 1782992075),
(814, 1001, '2026-07-02', 14, 35, 0, 1782992135),
(815, 1001, '2026-07-02', 14, 36, 0, 1782992195),
(816, 1001, '2026-07-02', 14, 37, 0, 1782992255),
(817, 1001, '2026-07-02', 14, 38, 0, 1782992315),
(818, 1001, '2026-07-02', 14, 39, 0, 1782992375),
(819, 1001, '2026-07-02', 14, 40, 0, 1782992435),
(820, 1001, '2026-07-02', 14, 41, 0, 1782992495),
(821, 1001, '2026-07-02', 14, 42, 0, 1782992555),
(822, 1001, '2026-07-02', 14, 43, 0, 1782992615),
(823, 1001, '2026-07-02', 14, 44, 0, 1782992675),
(824, 1001, '2026-07-02', 14, 45, 0, 1782992735),
(825, 1001, '2026-07-02', 14, 46, 0, 1782992795),
(826, 1001, '2026-07-02', 14, 47, 0, 1782992855),
(827, 1001, '2026-07-02', 14, 48, 0, 1782992915),
(828, 1001, '2026-07-02', 14, 49, 0, 1782992975),
(829, 1001, '2026-07-02', 14, 50, 0, 1782993035),
(830, 1001, '2026-07-02', 14, 51, 0, 1782993095),
(831, 1001, '2026-07-02', 14, 52, 0, 1782993155),
(832, 1001, '2026-07-02', 14, 53, 0, 1782993215),
(833, 1001, '2026-07-02', 14, 54, 0, 1782993275),
(834, 1001, '2026-07-02', 14, 55, 0, 1782993335),
(835, 1001, '2026-07-02', 14, 56, 0, 1782993395),
(836, 1001, '2026-07-02', 14, 57, 0, 1782993455),
(837, 1001, '2026-07-02', 14, 58, 0, 1782993515),
(838, 1001, '2026-07-02', 14, 59, 0, 1782993575),
(839, 1001, '2026-07-02', 15, 0, 0, 1782993635),
(840, 1001, '2026-07-02', 15, 1, 0, 1782993695),
(841, 1001, '2026-07-02', 15, 2, 0, 1782993755),
(842, 1001, '2026-07-02', 15, 3, 0, 1782993815),
(843, 1001, '2026-07-02', 15, 4, 0, 1782993875),
(844, 1001, '2026-07-02', 15, 5, 0, 1782993935),
(845, 1001, '2026-07-02', 15, 6, 0, 1782993995),
(846, 1001, '2026-07-02', 15, 7, 0, 1782994055),
(847, 1001, '2026-07-02', 15, 8, 0, 1782994115),
(848, 1001, '2026-07-02', 15, 9, 0, 1782994175),
(849, 1001, '2026-07-02', 15, 10, 0, 1782994235),
(850, 1001, '2026-07-02', 15, 11, 0, 1782994295),
(851, 1001, '2026-07-02', 15, 12, 0, 1782994355),
(852, 1001, '2026-07-02', 15, 13, 0, 1782994415),
(853, 1001, '2026-07-02', 15, 14, 0, 1782994475),
(854, 1001, '2026-07-02', 15, 15, 0, 1782994535),
(855, 1001, '2026-07-02', 15, 16, 0, 1782994595),
(856, 1001, '2026-07-02', 15, 17, 0, 1782994655),
(857, 1001, '2026-07-02', 15, 18, 0, 1782994715),
(858, 1001, '2026-07-02', 15, 19, 0, 1782994775),
(859, 1001, '2026-07-02', 15, 20, 0, 1782994835),
(860, 1001, '2026-07-02', 15, 21, 0, 1782994895),
(861, 1001, '2026-07-02', 15, 22, 0, 1782994955),
(862, 1001, '2026-07-02', 15, 23, 0, 1782995015),
(863, 1001, '2026-07-02', 15, 24, 0, 1782995075),
(864, 1001, '2026-07-02', 15, 25, 0, 1782995135),
(865, 1001, '2026-07-02', 15, 26, 0, 1782995195),
(866, 1001, '2026-07-02', 15, 27, 0, 1782995255),
(867, 1001, '2026-07-02', 15, 28, 0, 1782995315),
(868, 1001, '2026-07-02', 15, 29, 0, 1782995375),
(869, 1001, '2026-07-02', 15, 30, 0, 1782995435),
(870, 1001, '2026-07-02', 15, 31, 0, 1782995495),
(871, 1001, '2026-07-02', 15, 32, 0, 1782995555),
(872, 1001, '2026-07-02', 15, 33, 0, 1782995615),
(873, 1001, '2026-07-02', 15, 34, 0, 1782995675),
(874, 1001, '2026-07-02', 15, 35, 0, 1782995735),
(875, 1001, '2026-07-02', 15, 36, 0, 1782995795),
(876, 1001, '2026-07-02', 15, 37, 0, 1782995855),
(877, 1001, '2026-07-02', 15, 38, 0, 1782995915),
(878, 1001, '2026-07-02', 15, 39, 0, 1782995975),
(879, 1001, '2026-07-02', 15, 40, 0, 1782996035),
(880, 1001, '2026-07-02', 15, 41, 0, 1782996095),
(881, 1001, '2026-07-02', 15, 42, 0, 1782996155),
(882, 1001, '2026-07-02', 15, 43, 0, 1782996215),
(883, 1001, '2026-07-02', 15, 44, 0, 1782996275),
(884, 1001, '2026-07-02', 15, 45, 0, 1782996335),
(885, 1001, '2026-07-02', 15, 46, 0, 1782996395),
(886, 1001, '2026-07-02', 15, 47, 0, 1782996455),
(887, 1001, '2026-07-02', 15, 48, 0, 1782996515),
(888, 1001, '2026-07-02', 15, 49, 0, 1782996575),
(889, 1001, '2026-07-02', 15, 50, 0, 1782996635),
(890, 1001, '2026-07-02', 15, 51, 0, 1782996695),
(891, 1001, '2026-07-02', 15, 52, 0, 1782996755),
(892, 1001, '2026-07-02', 15, 53, 0, 1782996815),
(893, 1001, '2026-07-02', 15, 54, 0, 1782996875),
(894, 1001, '2026-07-02', 15, 55, 0, 1782996935),
(895, 1001, '2026-07-02', 15, 56, 0, 1782996995),
(896, 1001, '2026-07-02', 15, 57, 0, 1782997055),
(897, 1001, '2026-07-02', 15, 58, 0, 1782997115),
(898, 1001, '2026-07-02', 15, 59, 0, 1782997175),
(899, 1001, '2026-07-02', 16, 0, 0, 1782997235),
(900, 1001, '2026-07-02', 16, 1, 0, 1782997295),
(901, 1001, '2026-07-02', 16, 2, 0, 1782997355),
(902, 1001, '2026-07-02', 16, 3, 0, 1782997415),
(903, 1001, '2026-07-02', 16, 4, 0, 1782997475),
(904, 1001, '2026-07-02', 16, 5, 0, 1782997535),
(905, 1001, '2026-07-02', 16, 6, 0, 1782997595),
(906, 1001, '2026-07-02', 16, 7, 0, 1782997655),
(907, 1001, '2026-07-02', 16, 8, 0, 1782997715),
(908, 1001, '2026-07-02', 16, 9, 0, 1782997775),
(909, 1001, '2026-07-02', 16, 10, 0, 1782997835),
(910, 1001, '2026-07-02', 16, 11, 0, 1782997895),
(911, 1001, '2026-07-02', 16, 12, 0, 1782997955),
(912, 1001, '2026-07-02', 16, 13, 0, 1782998015),
(913, 1001, '2026-07-02', 16, 14, 0, 1782998075),
(914, 1001, '2026-07-02', 16, 15, 0, 1782998135),
(915, 1001, '2026-07-02', 16, 16, 0, 1782998195),
(916, 1001, '2026-07-02', 16, 17, 0, 1782998255),
(917, 1001, '2026-07-02', 16, 18, 0, 1782998315),
(918, 1001, '2026-07-02', 16, 19, 0, 1782998375),
(919, 1001, '2026-07-02', 16, 20, 0, 1782998435),
(920, 1001, '2026-07-02', 16, 21, 0, 1782998495),
(921, 1001, '2026-07-02', 16, 22, 0, 1782998555),
(922, 1001, '2026-07-02', 16, 23, 0, 1782998615),
(923, 1001, '2026-07-02', 16, 24, 0, 1782998675),
(924, 1001, '2026-07-02', 16, 25, 0, 1782998735),
(925, 1001, '2026-07-02', 16, 26, 0, 1782998795),
(926, 1001, '2026-07-02', 16, 27, 0, 1782998855),
(927, 1001, '2026-07-02', 16, 28, 0, 1782998915),
(928, 1001, '2026-07-02', 16, 29, 0, 1782998975),
(929, 1001, '2026-07-02', 16, 30, 0, 1782999035),
(930, 1001, '2026-07-02', 16, 31, 0, 1782999095),
(931, 1001, '2026-07-02', 16, 32, 0, 1782999155),
(932, 1001, '2026-07-02', 16, 33, 0, 1782999215),
(933, 1001, '2026-07-02', 16, 34, 0, 1782999275),
(934, 1001, '2026-07-02', 16, 35, 0, 1782999335),
(935, 1001, '2026-07-02', 16, 36, 0, 1782999395),
(936, 1001, '2026-07-02', 16, 37, 0, 1782999455),
(937, 1001, '2026-07-02', 16, 38, 0, 1782999515),
(938, 1001, '2026-07-02', 16, 39, 0, 1782999575),
(939, 1001, '2026-07-02', 16, 40, 0, 1782999635),
(940, 1001, '2026-07-02', 16, 41, 0, 1782999695),
(941, 1001, '2026-07-02', 16, 42, 0, 1782999755),
(942, 1001, '2026-07-02', 16, 43, 0, 1782999815),
(943, 1001, '2026-07-02', 16, 44, 0, 1782999875),
(944, 1001, '2026-07-02', 16, 45, 0, 1782999935),
(945, 1001, '2026-07-02', 16, 46, 0, 1782999995),
(946, 1001, '2026-07-02', 16, 47, 0, 1783000055),
(947, 1001, '2026-07-02', 16, 48, 0, 1783000115),
(948, 1001, '2026-07-02', 16, 49, 0, 1783000175),
(949, 1001, '2026-07-02', 16, 50, 0, 1783000235),
(950, 1001, '2026-07-02', 16, 51, 0, 1783000295),
(951, 1001, '2026-07-02', 16, 52, 0, 1783000355),
(952, 1001, '2026-07-02', 16, 53, 0, 1783000415),
(953, 1001, '2026-07-02', 16, 54, 0, 1783000475),
(954, 1001, '2026-07-02', 16, 55, 0, 1783000535),
(955, 1001, '2026-07-02', 16, 56, 0, 1783000595),
(956, 1001, '2026-07-02', 16, 57, 0, 1783000655),
(957, 1001, '2026-07-02', 16, 58, 0, 1783000715),
(958, 1001, '2026-07-02', 16, 59, 0, 1783000775),
(959, 1001, '2026-07-02', 17, 0, 0, 1783000835),
(960, 1001, '2026-07-02', 17, 1, 0, 1783000895),
(961, 1001, '2026-07-02', 17, 2, 0, 1783000955),
(962, 1001, '2026-07-02', 17, 3, 0, 1783001015),
(963, 1001, '2026-07-02', 17, 4, 0, 1783001075),
(964, 1001, '2026-07-02', 17, 5, 0, 1783001135),
(965, 1001, '2026-07-02', 17, 6, 0, 1783001195),
(966, 1001, '2026-07-02', 17, 7, 0, 1783001255),
(967, 1001, '2026-07-02', 17, 8, 0, 1783001315),
(968, 1001, '2026-07-02', 17, 9, 0, 1783001375),
(969, 1001, '2026-07-02', 17, 10, 0, 1783001435),
(970, 1001, '2026-07-02', 17, 11, 0, 1783001495),
(971, 1001, '2026-07-02', 17, 12, 0, 1783001555),
(972, 1001, '2026-07-02', 17, 13, 0, 1783001615),
(973, 1001, '2026-07-02', 17, 14, 0, 1783001675),
(974, 1001, '2026-07-02', 17, 15, 0, 1783001735),
(975, 1001, '2026-07-02', 17, 16, 0, 1783001795),
(976, 1001, '2026-07-02', 17, 17, 0, 1783001855),
(977, 1001, '2026-07-02', 17, 18, 0, 1783001915),
(978, 1001, '2026-07-02', 17, 19, 0, 1783001975),
(979, 1001, '2026-07-02', 17, 20, 0, 1783002035),
(980, 1001, '2026-07-02', 17, 21, 0, 1783002095),
(981, 1001, '2026-07-02', 17, 22, 0, 1783002155),
(982, 1001, '2026-07-02', 17, 23, 0, 1783002215),
(983, 1001, '2026-07-02', 17, 24, 0, 1783002275),
(984, 1001, '2026-07-02', 17, 25, 0, 1783002335),
(985, 1001, '2026-07-02', 17, 26, 0, 1783002395),
(986, 1001, '2026-07-02', 17, 27, 0, 1783002455),
(987, 1001, '2026-07-02', 17, 28, 0, 1783002515),
(988, 1001, '2026-07-02', 17, 29, 0, 1783002575),
(989, 1001, '2026-07-02', 17, 30, 0, 1783002635),
(990, 1001, '2026-07-02', 17, 31, 0, 1783002695),
(991, 1001, '2026-07-02', 17, 32, 0, 1783002755),
(992, 1001, '2026-07-02', 17, 33, 0, 1783002815),
(993, 1001, '2026-07-02', 17, 34, 0, 1783002875),
(994, 1001, '2026-07-02', 17, 35, 0, 1783002935),
(995, 1001, '2026-07-02', 17, 36, 0, 1783002995),
(996, 1001, '2026-07-02', 17, 37, 0, 1783003055),
(997, 1001, '2026-07-02', 17, 38, 0, 1783003115),
(998, 1001, '2026-07-02', 17, 39, 0, 1783003175),
(999, 1001, '2026-07-02', 17, 40, 0, 1783003235),
(1000, 1001, '2026-07-02', 17, 41, 0, 1783003295),
(1001, 1001, '2026-07-02', 17, 42, 0, 1783003355),
(1002, 1001, '2026-07-02', 17, 43, 0, 1783003415),
(1003, 1001, '2026-07-02', 17, 44, 0, 1783003475),
(1004, 1001, '2026-07-02', 17, 45, 0, 1783003535),
(1005, 1001, '2026-07-02', 17, 46, 0, 1783003595),
(1006, 1001, '2026-07-02', 17, 47, 0, 1783003655),
(1007, 1001, '2026-07-02', 17, 48, 0, 1783003715),
(1008, 1001, '2026-07-02', 17, 49, 0, 1783003775),
(1009, 1001, '2026-07-02', 17, 50, 0, 1783003835),
(1010, 1001, '2026-07-02', 17, 51, 0, 1783003895),
(1011, 1001, '2026-07-02', 17, 52, 0, 1783003955),
(1012, 1001, '2026-07-02', 17, 53, 0, 1783004015),
(1013, 1001, '2026-07-02', 17, 54, 0, 1783004075),
(1014, 1001, '2026-07-02', 17, 55, 0, 1783004135),
(1015, 1001, '2026-07-02', 17, 56, 0, 1783004195),
(1016, 1001, '2026-07-02', 17, 57, 0, 1783004255),
(1017, 1001, '2026-07-02', 17, 58, 0, 1783004315),
(1018, 1001, '2026-07-02', 17, 59, 0, 1783004375),
(1019, 1001, '2026-07-02', 18, 0, 0, 1783004435),
(1020, 1001, '2026-07-02', 18, 1, 0, 1783004495),
(1021, 1001, '2026-07-02', 18, 2, 0, 1783004555),
(1022, 1001, '2026-07-02', 18, 3, 0, 1783004615),
(1023, 1001, '2026-07-02', 18, 4, 0, 1783004675),
(1024, 1001, '2026-07-02', 18, 5, 0, 1783004735),
(1025, 1001, '2026-07-02', 18, 6, 0, 1783004795),
(1026, 1001, '2026-07-02', 18, 7, 0, 1783004855),
(1027, 1001, '2026-07-02', 18, 8, 0, 1783004915),
(1028, 1001, '2026-07-02', 18, 9, 0, 1783004975),
(1029, 1001, '2026-07-02', 18, 10, 0, 1783005035),
(1030, 1001, '2026-07-02', 18, 11, 0, 1783005095),
(1031, 1001, '2026-07-02', 18, 12, 0, 1783005155),
(1032, 1001, '2026-07-02', 18, 13, 0, 1783005215),
(1033, 1001, '2026-07-02', 18, 14, 0, 1783005275),
(1034, 1001, '2026-07-02', 18, 15, 0, 1783005335),
(1035, 1001, '2026-07-02', 18, 16, 0, 1783005395),
(1036, 1001, '2026-07-02', 18, 17, 0, 1783005455),
(1037, 1001, '2026-07-02', 18, 18, 0, 1783005515),
(1038, 1001, '2026-07-02', 18, 19, 0, 1783005575),
(1039, 1001, '2026-07-02', 18, 20, 0, 1783005635),
(1040, 1001, '2026-07-02', 18, 21, 0, 1783005695),
(1041, 1001, '2026-07-02', 18, 22, 0, 1783005755),
(1042, 1001, '2026-07-02', 18, 23, 0, 1783005815),
(1043, 1001, '2026-07-02', 18, 24, 0, 1783005875),
(1044, 1001, '2026-07-02', 18, 25, 0, 1783005935),
(1045, 1001, '2026-07-02', 18, 26, 0, 1783005995),
(1046, 1001, '2026-07-02', 18, 27, 0, 1783006055),
(1047, 1001, '2026-07-02', 18, 28, 0, 1783006115),
(1048, 1001, '2026-07-02', 18, 29, 0, 1783006175),
(1049, 1001, '2026-07-02', 18, 30, 0, 1783006235),
(1050, 1001, '2026-07-02', 18, 31, 0, 1783006295),
(1051, 1001, '2026-07-02', 18, 32, 0, 1783006355),
(1052, 1001, '2026-07-02', 18, 33, 0, 1783006415),
(1053, 1001, '2026-07-02', 18, 34, 0, 1783006475),
(1054, 1001, '2026-07-02', 18, 35, 0, 1783006535),
(1055, 1001, '2026-07-02', 18, 36, 0, 1783006595);
INSERT INTO `t_servernum` (`id`, `serverId`, `day`, `hour`, `min`, `num`, `time`) VALUES
(1056, 1001, '2026-07-02', 18, 37, 0, 1783006655),
(1057, 1001, '2026-07-02', 18, 38, 0, 1783006715),
(1058, 1001, '2026-07-02', 18, 39, 0, 1783006775),
(1059, 1001, '2026-07-02', 18, 40, 0, 1783006835),
(1060, 1001, '2026-07-02', 18, 41, 0, 1783006895),
(1061, 1001, '2026-07-02', 18, 42, 0, 1783006955),
(1062, 1001, '2026-07-02', 18, 43, 0, 1783007015),
(1063, 1001, '2026-07-02', 18, 44, 0, 1783007075),
(1064, 1001, '2026-07-02', 18, 45, 0, 1783007135),
(1065, 1001, '2026-07-02', 18, 46, 0, 1783007195),
(1066, 1001, '2026-07-02', 18, 47, 0, 1783007255),
(1067, 1001, '2026-07-02', 18, 48, 0, 1783007315),
(1068, 1001, '2026-07-02', 18, 49, 0, 1783007375),
(1069, 1001, '2026-07-02', 18, 50, 0, 1783007435),
(1070, 1001, '2026-07-02', 18, 51, 0, 1783007495),
(1071, 1001, '2026-07-02', 18, 52, 0, 1783007555),
(1072, 1001, '2026-07-02', 18, 53, 0, 1783007615),
(1073, 1001, '2026-07-02', 18, 54, 0, 1783007675),
(1074, 1001, '2026-07-02', 18, 55, 0, 1783007735),
(1075, 1001, '2026-07-02', 18, 56, 0, 1783007795),
(1076, 1001, '2026-07-02', 18, 57, 0, 1783007855),
(1077, 1001, '2026-07-02', 18, 58, 0, 1783007915),
(1078, 1001, '2026-07-02', 18, 59, 0, 1783007975),
(1079, 1001, '2026-07-02', 19, 0, 0, 1783008035),
(1080, 1001, '2026-07-02', 19, 1, 0, 1783008095),
(1081, 1001, '2026-07-02', 19, 2, 0, 1783008155),
(1082, 1001, '2026-07-02', 19, 3, 0, 1783008215),
(1083, 1001, '2026-07-02', 19, 4, 0, 1783008275),
(1084, 1001, '2026-07-02', 19, 5, 0, 1783008335),
(1085, 1001, '2026-07-02', 19, 6, 0, 1783008395),
(1086, 1001, '2026-07-02', 19, 7, 0, 1783008455),
(1087, 1001, '2026-07-02', 19, 8, 0, 1783008515),
(1088, 1001, '2026-07-02', 19, 9, 0, 1783008575),
(1089, 1001, '2026-07-02', 19, 10, 0, 1783008635),
(1090, 1001, '2026-07-02', 19, 11, 0, 1783008695),
(1091, 1001, '2026-07-02', 19, 12, 0, 1783008755),
(1092, 1001, '2026-07-02', 19, 13, 0, 1783008815),
(1093, 1001, '2026-07-02', 19, 14, 0, 1783008875),
(1094, 1001, '2026-07-02', 19, 15, 0, 1783008935),
(1095, 1001, '2026-07-02', 19, 16, 0, 1783008995),
(1096, 1001, '2026-07-02', 19, 17, 0, 1783009055),
(1097, 1001, '2026-07-02', 19, 18, 0, 1783009115),
(1098, 1001, '2026-07-02', 19, 19, 0, 1783009175),
(1099, 1001, '2026-07-02', 19, 20, 0, 1783009235),
(1100, 1001, '2026-07-02', 19, 21, 0, 1783009295),
(1101, 1001, '2026-07-02', 19, 22, 0, 1783009355),
(1102, 1001, '2026-07-02', 19, 23, 0, 1783009415),
(1103, 1001, '2026-07-02', 19, 24, 0, 1783009475),
(1104, 1001, '2026-07-02', 19, 25, 0, 1783009535),
(1105, 1001, '2026-07-02', 19, 26, 0, 1783009595),
(1106, 1001, '2026-07-02', 19, 27, 0, 1783009655),
(1107, 1001, '2026-07-02', 19, 28, 0, 1783009715),
(1108, 1001, '2026-07-02', 19, 29, 0, 1783009775),
(1109, 1001, '2026-07-02', 19, 30, 0, 1783009835),
(1110, 1001, '2026-07-02', 19, 31, 0, 1783009895),
(1111, 1001, '2026-07-02', 19, 32, 0, 1783009955),
(1112, 1001, '2026-07-02', 19, 33, 0, 1783010015),
(1113, 1001, '2026-07-02', 19, 34, 0, 1783010075),
(1114, 1001, '2026-07-02', 19, 35, 0, 1783010135),
(1115, 1001, '2026-07-02', 19, 36, 0, 1783010195),
(1116, 1001, '2026-07-02', 19, 37, 0, 1783010255),
(1117, 1001, '2026-07-02', 19, 38, 0, 1783010315),
(1118, 1001, '2026-07-02', 19, 39, 0, 1783010375),
(1119, 1001, '2026-07-02', 19, 40, 0, 1783010435),
(1120, 1001, '2026-07-02', 19, 41, 0, 1783010495),
(1121, 1001, '2026-07-02', 19, 42, 0, 1783010555),
(1122, 1001, '2026-07-02', 19, 43, 0, 1783010615),
(1123, 1001, '2026-07-02', 19, 44, 0, 1783010675),
(1124, 1001, '2026-07-02', 19, 45, 0, 1783010735),
(1125, 1001, '2026-07-02', 19, 46, 0, 1783010795),
(1126, 1001, '2026-07-02', 19, 47, 0, 1783010855),
(1127, 1001, '2026-07-02', 19, 48, 0, 1783010915),
(1128, 1001, '2026-07-02', 19, 49, 0, 1783010975),
(1129, 1001, '2026-07-02', 19, 50, 0, 1783011035),
(1130, 1001, '2026-07-02', 19, 51, 0, 1783011095),
(1131, 1001, '2026-07-02', 19, 52, 0, 1783011155),
(1132, 1001, '2026-07-02', 19, 53, 0, 1783011215),
(1133, 1001, '2026-07-02', 19, 54, 0, 1783011275),
(1134, 1001, '2026-07-02', 19, 55, 0, 1783011335),
(1135, 1001, '2026-07-02', 19, 56, 0, 1783011395),
(1136, 1001, '2026-07-02', 19, 57, 0, 1783011455),
(1137, 1001, '2026-07-02', 19, 58, 0, 1783011515),
(1138, 1001, '2026-07-02', 19, 59, 0, 1783011575),
(1139, 1001, '2026-07-02', 20, 0, 0, 1783011635),
(1140, 1001, '2026-07-02', 20, 1, 0, 1783011695),
(1141, 1001, '2026-07-02', 20, 2, 0, 1783011755),
(1142, 1001, '2026-07-02', 20, 3, 0, 1783011815),
(1143, 1001, '2026-07-02', 20, 4, 0, 1783011875),
(1144, 1001, '2026-07-02', 20, 5, 0, 1783011935),
(1145, 1001, '2026-07-02', 20, 6, 0, 1783011995),
(1146, 1001, '2026-07-02', 20, 7, 0, 1783012055),
(1147, 1001, '2026-07-02', 20, 8, 0, 1783012115),
(1148, 1001, '2026-07-02', 20, 9, 0, 1783012175),
(1149, 1001, '2026-07-02', 20, 10, 0, 1783012235),
(1150, 1001, '2026-07-02', 20, 11, 0, 1783012295),
(1151, 1001, '2026-07-02', 20, 12, 0, 1783012355),
(1152, 1001, '2026-07-02', 20, 13, 0, 1783012415),
(1153, 1001, '2026-07-02', 20, 14, 0, 1783012475),
(1154, 1001, '2026-07-02', 20, 15, 0, 1783012535),
(1155, 1001, '2026-07-02', 20, 16, 0, 1783012595),
(1156, 1001, '2026-07-02', 20, 17, 0, 1783012655),
(1157, 1001, '2026-07-02', 20, 18, 0, 1783012715),
(1158, 1001, '2026-07-02', 20, 19, 0, 1783012775),
(1159, 1001, '2026-07-02', 20, 20, 0, 1783012835),
(1160, 1001, '2026-07-02', 20, 21, 0, 1783012895),
(1161, 1001, '2026-07-02', 20, 22, 0, 1783012955),
(1162, 1001, '2026-07-02', 20, 23, 0, 1783013015),
(1163, 1001, '2026-07-02', 20, 24, 0, 1783013075),
(1164, 1001, '2026-07-02', 20, 25, 0, 1783013135),
(1165, 1001, '2026-07-02', 20, 26, 0, 1783013195),
(1166, 1001, '2026-07-02', 20, 27, 0, 1783013255),
(1167, 1001, '2026-07-02', 20, 28, 0, 1783013315),
(1168, 1001, '2026-07-02', 20, 29, 0, 1783013375),
(1169, 1001, '2026-07-02', 20, 30, 0, 1783013435),
(1170, 1001, '2026-07-02', 20, 31, 0, 1783013495),
(1171, 1001, '2026-07-02', 20, 32, 0, 1783013555),
(1172, 1001, '2026-07-02', 20, 33, 0, 1783013615),
(1173, 1001, '2026-07-02', 20, 34, 0, 1783013675),
(1174, 1001, '2026-07-02', 20, 35, 0, 1783013735),
(1175, 1001, '2026-07-02', 20, 36, 0, 1783013795),
(1176, 1001, '2026-07-02', 20, 37, 0, 1783013855),
(1177, 1001, '2026-07-02', 20, 38, 0, 1783013915),
(1178, 1001, '2026-07-02', 20, 39, 0, 1783013975),
(1179, 1001, '2026-07-02', 20, 40, 0, 1783014035),
(1180, 1001, '2026-07-02', 20, 41, 0, 1783014095),
(1181, 1001, '2026-07-02', 20, 42, 0, 1783014155),
(1182, 1001, '2026-07-02', 20, 43, 0, 1783014215),
(1183, 1001, '2026-07-02', 20, 44, 0, 1783014275),
(1184, 1001, '2026-07-02', 20, 45, 0, 1783014335),
(1185, 1001, '2026-07-02', 20, 46, 0, 1783014395),
(1186, 1001, '2026-07-02', 20, 47, 0, 1783014455),
(1187, 1001, '2026-07-02', 20, 48, 0, 1783014515),
(1188, 1001, '2026-07-02', 20, 49, 0, 1783014575),
(1189, 1001, '2026-07-02', 20, 50, 0, 1783014635),
(1190, 1001, '2026-07-02', 20, 51, 0, 1783014695),
(1191, 1001, '2026-07-02', 20, 52, 0, 1783014755),
(1192, 1001, '2026-07-02', 20, 53, 0, 1783014815),
(1193, 1001, '2026-07-02', 20, 54, 0, 1783014875),
(1194, 1001, '2026-07-02', 20, 55, 0, 1783014935),
(1195, 1001, '2026-07-02', 20, 56, 0, 1783014995),
(1196, 1001, '2026-07-02', 20, 57, 0, 1783015055),
(1197, 1001, '2026-07-02', 20, 58, 0, 1783015115),
(1198, 1001, '2026-07-02', 20, 59, 0, 1783015175),
(1199, 1001, '2026-07-02', 21, 0, 0, 1783015235),
(1200, 1001, '2026-07-02', 21, 1, 0, 1783015295),
(1201, 1001, '2026-07-02', 21, 2, 0, 1783015355),
(1202, 1001, '2026-07-02', 21, 3, 0, 1783015415),
(1203, 1001, '2026-07-02', 21, 4, 0, 1783015475),
(1204, 1001, '2026-07-02', 21, 5, 0, 1783015535),
(1205, 1001, '2026-07-02', 21, 6, 0, 1783015595),
(1206, 1001, '2026-07-02', 21, 7, 0, 1783015655),
(1207, 1001, '2026-07-02', 21, 8, 0, 1783015715),
(1208, 1001, '2026-07-02', 21, 9, 0, 1783015775),
(1209, 1001, '2026-07-02', 21, 10, 0, 1783015835),
(1210, 1001, '2026-07-02', 21, 11, 0, 1783015895),
(1211, 1001, '2026-07-02', 21, 12, 0, 1783015955),
(1212, 1001, '2026-07-02', 21, 13, 0, 1783016015),
(1213, 1001, '2026-07-02', 21, 14, 0, 1783016075),
(1214, 1001, '2026-07-02', 21, 15, 0, 1783016135),
(1215, 1001, '2026-07-02', 21, 16, 0, 1783016195),
(1216, 1001, '2026-07-02', 21, 17, 0, 1783016255),
(1217, 1001, '2026-07-02', 21, 18, 0, 1783016315),
(1218, 1001, '2026-07-02', 21, 19, 0, 1783016375),
(1219, 1001, '2026-07-02', 21, 20, 0, 1783016435),
(1220, 1001, '2026-07-02', 21, 21, 0, 1783016495),
(1221, 1001, '2026-07-02', 21, 22, 0, 1783016555),
(1222, 1001, '2026-07-02', 21, 23, 0, 1783016615),
(1223, 1001, '2026-07-02', 21, 24, 0, 1783016675),
(1224, 1001, '2026-07-02', 21, 25, 0, 1783016735),
(1225, 1001, '2026-07-02', 21, 26, 0, 1783016795),
(1226, 1001, '2026-07-02', 21, 27, 0, 1783016855),
(1227, 1001, '2026-07-02', 21, 28, 0, 1783016915),
(1228, 1001, '2026-07-02', 21, 29, 0, 1783016975),
(1229, 1001, '2026-07-02', 21, 30, 0, 1783017035),
(1230, 1001, '2026-07-02', 21, 31, 0, 1783017095),
(1231, 1001, '2026-07-02', 21, 32, 0, 1783017155),
(1232, 1001, '2026-07-02', 21, 33, 0, 1783017215),
(1233, 1001, '2026-07-02', 21, 34, 0, 1783017275),
(1234, 1001, '2026-07-02', 21, 35, 0, 1783017335),
(1235, 1001, '2026-07-02', 21, 36, 0, 1783017395),
(1236, 1001, '2026-07-02', 21, 37, 0, 1783017455),
(1237, 1001, '2026-07-02', 21, 38, 0, 1783017515),
(1238, 1001, '2026-07-02', 21, 39, 0, 1783017575),
(1239, 1001, '2026-07-02', 21, 40, 0, 1783017635),
(1240, 1001, '2026-07-02', 21, 41, 0, 1783017695),
(1241, 1001, '2026-07-02', 21, 42, 0, 1783017755),
(1242, 1001, '2026-07-02', 21, 43, 0, 1783017815),
(1243, 1001, '2026-07-02', 21, 44, 0, 1783017875),
(1244, 1001, '2026-07-02', 21, 45, 0, 1783017935),
(1245, 1001, '2026-07-02', 21, 46, 0, 1783017995),
(1246, 1001, '2026-07-02', 21, 47, 0, 1783018055),
(1247, 1001, '2026-07-02', 21, 48, 0, 1783018115),
(1248, 1001, '2026-07-02', 21, 49, 0, 1783018175),
(1249, 1001, '2026-07-02', 21, 50, 0, 1783018235),
(1250, 1001, '2026-07-02', 21, 51, 0, 1783018295),
(1251, 1001, '2026-07-02', 21, 52, 0, 1783018355),
(1252, 1001, '2026-07-02', 21, 53, 0, 1783018415),
(1253, 1001, '2026-07-02', 21, 54, 0, 1783018475),
(1254, 1001, '2026-07-02', 21, 55, 0, 1783018535),
(1255, 1001, '2026-07-02', 21, 56, 0, 1783018595),
(1256, 1001, '2026-07-02', 21, 57, 0, 1783018655),
(1257, 1001, '2026-07-02', 21, 58, 0, 1783018715),
(1258, 1001, '2026-07-02', 21, 59, 0, 1783018775),
(1259, 1001, '2026-07-02', 22, 0, 0, 1783018835),
(1260, 1001, '2026-07-02', 22, 1, 0, 1783018895),
(1261, 1001, '2026-07-02', 22, 2, 0, 1783018955),
(1262, 1001, '2026-07-02', 22, 3, 0, 1783019015),
(1263, 1001, '2026-07-02', 22, 4, 0, 1783019075),
(1264, 1001, '2026-07-02', 22, 5, 0, 1783019135),
(1265, 1001, '2026-07-02', 22, 6, 0, 1783019195),
(1266, 1001, '2026-07-02', 22, 7, 0, 1783019255),
(1267, 1001, '2026-07-02', 22, 8, 0, 1783019315),
(1268, 1001, '2026-07-02', 22, 9, 0, 1783019375),
(1269, 1001, '2026-07-02', 22, 10, 0, 1783019435),
(1270, 1001, '2026-07-02', 22, 11, 0, 1783019495),
(1271, 1001, '2026-07-02', 22, 12, 0, 1783019555),
(1272, 1001, '2026-07-02', 22, 13, 0, 1783019615),
(1273, 1001, '2026-07-02', 22, 14, 0, 1783019675),
(1274, 1001, '2026-07-02', 22, 15, 0, 1783019735),
(1275, 1001, '2026-07-02', 22, 16, 0, 1783019795),
(1276, 1001, '2026-07-02', 22, 17, 0, 1783019855),
(1277, 1001, '2026-07-02', 22, 18, 0, 1783019915),
(1278, 1001, '2026-07-02', 22, 19, 0, 1783019975),
(1279, 1001, '2026-07-02', 22, 20, 0, 1783020035),
(1280, 1001, '2026-07-02', 22, 21, 0, 1783020095),
(1281, 1001, '2026-07-02', 22, 22, 0, 1783020155),
(1282, 1001, '2026-07-02', 22, 23, 0, 1783020215),
(1283, 1001, '2026-07-02', 22, 24, 0, 1783020275),
(1284, 1001, '2026-07-02', 22, 25, 0, 1783020335),
(1285, 1001, '2026-07-02', 22, 26, 0, 1783020395),
(1286, 1001, '2026-07-02', 22, 27, 0, 1783020455),
(1287, 1001, '2026-07-02', 22, 28, 0, 1783020515),
(1288, 1001, '2026-07-02', 22, 29, 0, 1783020575),
(1289, 1001, '2026-07-02', 22, 30, 0, 1783020635),
(1290, 1001, '2026-07-02', 22, 31, 0, 1783020695),
(1291, 1001, '2026-07-02', 22, 32, 0, 1783020755),
(1292, 1001, '2026-07-02', 22, 33, 0, 1783020815),
(1293, 1001, '2026-07-02', 22, 34, 0, 1783020875),
(1294, 1001, '2026-07-02', 22, 35, 0, 1783020935),
(1295, 1001, '2026-07-02', 22, 36, 0, 1783020995),
(1296, 1001, '2026-07-02', 22, 37, 0, 1783021055),
(1297, 1001, '2026-07-02', 22, 38, 0, 1783021115),
(1298, 1001, '2026-07-02', 22, 39, 0, 1783021175),
(1299, 1001, '2026-07-02', 22, 40, 0, 1783021235),
(1300, 1001, '2026-07-02', 22, 41, 0, 1783021295),
(1301, 1001, '2026-07-02', 22, 42, 0, 1783021355),
(1302, 1001, '2026-07-02', 22, 43, 0, 1783021415),
(1303, 1001, '2026-07-02', 22, 44, 0, 1783021475),
(1304, 1001, '2026-07-02', 22, 45, 0, 1783021535),
(1305, 1001, '2026-07-02', 22, 46, 0, 1783021595),
(1306, 1001, '2026-07-02', 22, 47, 0, 1783021655),
(1307, 1001, '2026-07-02', 22, 48, 0, 1783021715),
(1308, 1001, '2026-07-02', 22, 49, 0, 1783021775),
(1309, 1001, '2026-07-02', 22, 50, 0, 1783021835),
(1310, 1001, '2026-07-02', 22, 51, 0, 1783021895),
(1311, 1001, '2026-07-02', 22, 52, 0, 1783021955),
(1312, 1001, '2026-07-02', 22, 53, 0, 1783022015),
(1313, 1001, '2026-07-02', 22, 54, 0, 1783022075),
(1314, 1001, '2026-07-02', 22, 55, 0, 1783022135),
(1315, 1001, '2026-07-02', 22, 56, 0, 1783022195),
(1316, 1001, '2026-07-02', 22, 57, 0, 1783022255),
(1317, 1001, '2026-07-02', 22, 58, 0, 1783022315),
(1318, 1001, '2026-07-02', 22, 59, 0, 1783022375),
(1319, 1001, '2026-07-02', 23, 0, 0, 1783022435),
(1320, 1001, '2026-07-02', 23, 1, 0, 1783022495),
(1321, 1001, '2026-07-02', 23, 2, 0, 1783022555),
(1322, 1001, '2026-07-02', 23, 3, 0, 1783022615),
(1323, 1001, '2026-07-02', 23, 4, 0, 1783022675),
(1324, 1001, '2026-07-02', 23, 5, 0, 1783022735),
(1325, 1001, '2026-07-02', 23, 6, 0, 1783022795),
(1326, 1001, '2026-07-02', 23, 7, 0, 1783022855),
(1327, 1001, '2026-07-02', 23, 8, 0, 1783022915),
(1328, 1001, '2026-07-02', 23, 9, 0, 1783022975),
(1329, 1001, '2026-07-02', 23, 10, 0, 1783023035),
(1330, 1001, '2026-07-02', 23, 11, 0, 1783023095),
(1331, 1001, '2026-07-02', 23, 12, 0, 1783023155),
(1332, 1001, '2026-07-02', 23, 13, 0, 1783023215),
(1333, 1001, '2026-07-02', 23, 14, 0, 1783023275),
(1334, 1001, '2026-07-02', 23, 15, 0, 1783023335),
(1335, 1001, '2026-07-02', 23, 16, 0, 1783023395),
(1336, 1001, '2026-07-02', 23, 17, 0, 1783023455),
(1337, 1001, '2026-07-02', 23, 18, 0, 1783023515),
(1338, 1001, '2026-07-02', 23, 19, 0, 1783023575),
(1339, 1001, '2026-07-02', 23, 20, 0, 1783023635),
(1340, 1001, '2026-07-02', 23, 21, 0, 1783023695),
(1341, 1001, '2026-07-02', 23, 22, 0, 1783023755),
(1342, 1001, '2026-07-02', 23, 23, 0, 1783023815),
(1343, 1001, '2026-07-02', 23, 24, 0, 1783023875),
(1344, 1001, '2026-07-02', 23, 25, 0, 1783023935),
(1345, 1001, '2026-07-02', 23, 26, 0, 1783023995),
(1346, 1001, '2026-07-02', 23, 27, 0, 1783024055),
(1347, 1001, '2026-07-02', 23, 28, 0, 1783024115),
(1348, 1001, '2026-07-02', 23, 29, 0, 1783024175),
(1349, 1001, '2026-07-02', 23, 30, 0, 1783024235),
(1350, 1001, '2026-07-02', 23, 31, 0, 1783024295),
(1351, 1001, '2026-07-02', 23, 32, 0, 1783024355),
(1352, 1001, '2026-07-02', 23, 33, 0, 1783024415),
(1353, 1001, '2026-07-02', 23, 34, 0, 1783024475),
(1354, 1001, '2026-07-02', 23, 35, 0, 1783024535),
(1355, 1001, '2026-07-02', 23, 36, 0, 1783024595),
(1356, 1001, '2026-07-02', 23, 37, 0, 1783024655),
(1357, 1001, '2026-07-02', 23, 38, 0, 1783024715),
(1358, 1001, '2026-07-02', 23, 39, 0, 1783024775),
(1359, 1001, '2026-07-02', 23, 40, 0, 1783024835),
(1360, 1001, '2026-07-02', 23, 41, 0, 1783024895),
(1361, 1001, '2026-07-02', 23, 42, 0, 1783024955),
(1362, 1001, '2026-07-02', 23, 43, 0, 1783025015),
(1363, 1001, '2026-07-02', 23, 44, 0, 1783025075),
(1364, 1001, '2026-07-02', 23, 45, 0, 1783025135),
(1365, 1001, '2026-07-02', 23, 46, 0, 1783025195),
(1366, 1001, '2026-07-02', 23, 47, 0, 1783025255),
(1367, 1001, '2026-07-02', 23, 48, 0, 1783025315),
(1368, 1001, '2026-07-02', 23, 49, 0, 1783025375),
(1369, 1001, '2026-07-02', 23, 50, 0, 1783025435),
(1370, 1001, '2026-07-02', 23, 51, 0, 1783025495),
(1371, 1001, '2026-07-02', 23, 52, 0, 1783025555),
(1372, 1001, '2026-07-02', 23, 53, 0, 1783025615),
(1373, 1001, '2026-07-02', 23, 54, 0, 1783025675),
(1374, 1001, '2026-07-02', 23, 55, 0, 1783025735),
(1375, 1001, '2026-07-02', 23, 56, 0, 1783025795),
(1376, 1001, '2026-07-02', 23, 57, 0, 1783025855),
(1377, 1001, '2026-07-02', 23, 58, 0, 1783025915),
(1378, 1001, '2026-07-02', 23, 59, 0, 1783025975),
(1379, 1001, '2026-07-03', 0, 0, 0, 1783026035),
(1380, 1001, '2026-07-03', 0, 1, 0, 1783026095),
(1381, 1001, '2026-07-03', 0, 2, 0, 1783026155),
(1382, 1001, '2026-07-03', 0, 3, 0, 1783026221),
(1383, 1001, '2026-07-03', 0, 4, 0, 1783026275),
(1384, 1001, '2026-07-03', 0, 5, 0, 1783026335),
(1385, 1001, '2026-07-03', 0, 6, 0, 1783026395),
(1386, 1001, '2026-07-03', 0, 7, 0, 1783026455),
(1387, 1001, '2026-07-03', 0, 8, 0, 1783026530),
(1388, 1001, '2026-07-03', 0, 9, 0, 1783026575),
(1389, 1001, '2026-07-03', 0, 10, 0, 1783026635),
(1390, 1001, '2026-07-03', 0, 11, 0, 1783026695),
(1391, 1001, '2026-07-03', 0, 12, 0, 1783026755),
(1392, 1001, '2026-07-03', 0, 13, 0, 1783026815),
(1393, 1001, '2026-07-03', 0, 14, 0, 1783026875),
(1394, 1001, '2026-07-03', 0, 15, 0, 1783026935),
(1395, 1001, '2026-07-03', 0, 16, 0, 1783026995),
(1396, 1001, '2026-07-03', 0, 17, 0, 1783027055),
(1397, 1001, '2026-07-03', 0, 18, 0, 1783027115),
(1398, 1001, '2026-07-03', 0, 19, 0, 1783027175),
(1399, 1001, '2026-07-03', 0, 20, 0, 1783027235),
(1400, 1001, '2026-07-03', 0, 21, 0, 1783027295),
(1401, 1001, '2026-07-03', 0, 22, 0, 1783027355),
(1402, 1001, '2026-07-03', 0, 23, 0, 1783027415),
(1403, 1001, '2026-07-03', 0, 24, 0, 1783027475),
(1404, 1001, '2026-07-03', 0, 25, 0, 1783027535),
(1405, 1001, '2026-07-03', 0, 26, 0, 1783027595),
(1406, 1001, '2026-07-03', 0, 27, 0, 1783027655),
(1407, 1001, '2026-07-03', 0, 28, 0, 1783027715),
(1408, 1001, '2026-07-03', 0, 29, 0, 1783027775),
(1409, 1001, '2026-07-03', 0, 30, 0, 1783027835),
(1410, 1001, '2026-07-03', 0, 31, 0, 1783027895),
(1411, 1001, '2026-07-03', 0, 32, 0, 1783027955),
(1412, 1001, '2026-07-03', 0, 33, 0, 1783028015),
(1413, 1001, '2026-07-03', 0, 34, 0, 1783028075),
(1414, 1001, '2026-07-03', 0, 35, 0, 1783028135),
(1415, 1001, '2026-07-03', 0, 36, 0, 1783028195),
(1416, 1001, '2026-07-03', 0, 37, 0, 1783028255),
(1417, 1001, '2026-07-03', 0, 38, 0, 1783028315),
(1418, 1001, '2026-07-03', 0, 39, 0, 1783028375),
(1419, 1001, '2026-07-03', 0, 40, 0, 1783028435),
(1420, 1001, '2026-07-03', 0, 41, 0, 1783028495),
(1421, 1001, '2026-07-03', 0, 42, 0, 1783028555),
(1422, 1001, '2026-07-03', 0, 43, 0, 1783028615),
(1423, 1001, '2026-07-03', 0, 44, 0, 1783028675),
(1424, 1001, '2026-07-03', 0, 45, 0, 1783028735),
(1425, 1001, '2026-07-03', 0, 46, 0, 1783028795),
(1426, 1001, '2026-07-03', 0, 47, 0, 1783028855),
(1427, 1001, '2026-07-03', 0, 48, 0, 1783028915),
(1428, 1001, '2026-07-03', 0, 49, 0, 1783028975),
(1429, 1001, '2026-07-03', 0, 50, 0, 1783029035),
(1430, 1001, '2026-07-03', 0, 51, 0, 1783029095),
(1431, 1001, '2026-07-03', 0, 52, 0, 1783029155),
(1432, 1001, '2026-07-03', 0, 53, 0, 1783029215),
(1433, 1001, '2026-07-03', 0, 54, 0, 1783029275),
(1434, 1001, '2026-07-03', 0, 55, 0, 1783029335),
(1435, 1001, '2026-07-03', 0, 56, 0, 1783029395),
(1436, 1001, '2026-07-03', 0, 57, 0, 1783029455),
(1437, 1001, '2026-07-03', 0, 58, 0, 1783029515),
(1438, 1001, '2026-07-03', 0, 59, 0, 1783029575),
(1439, 1001, '2026-07-03', 1, 0, 0, 1783029635),
(1440, 1001, '2026-07-03', 1, 1, 0, 1783029695),
(1441, 1001, '2026-07-03', 1, 2, 0, 1783029755),
(1442, 1001, '2026-07-03', 1, 3, 0, 1783029815),
(1443, 1001, '2026-07-03', 1, 4, 0, 1783029875),
(1444, 1001, '2026-07-03', 1, 5, 0, 1783029935),
(1445, 1001, '2026-07-03', 1, 6, 0, 1783029995),
(1446, 1001, '2026-07-03', 1, 7, 0, 1783030055),
(1447, 1001, '2026-07-03', 1, 8, 0, 1783030115),
(1448, 1001, '2026-07-03', 1, 9, 0, 1783030175),
(1449, 1001, '2026-07-03', 1, 10, 0, 1783030235),
(1450, 1001, '2026-07-03', 1, 11, 0, 1783030295),
(1451, 1001, '2026-07-03', 1, 12, 0, 1783030355),
(1452, 1001, '2026-07-03', 1, 13, 0, 1783030415),
(1453, 1001, '2026-07-03', 1, 14, 0, 1783030475),
(1454, 1001, '2026-07-03', 1, 15, 0, 1783030535),
(1455, 1001, '2026-07-03', 1, 16, 0, 1783030595),
(1456, 1001, '2026-07-03', 1, 17, 0, 1783030655),
(1457, 1001, '2026-07-03', 1, 18, 0, 1783030715),
(1458, 1001, '2026-07-03', 1, 19, 1, 1783030775),
(1459, 1001, '2026-07-03', 1, 20, 1, 1783030835),
(1460, 1001, '2026-07-03', 1, 21, 0, 1783030895),
(1461, 1001, '2026-07-03', 1, 22, 0, 1783030955),
(1462, 1001, '2026-07-03', 1, 23, 0, 1783031015),
(1463, 1001, '2026-07-03', 1, 24, 0, 1783031075),
(1464, 1001, '2026-07-03', 1, 25, 0, 1783031135),
(1465, 1001, '2026-07-03', 1, 26, 0, 1783031195),
(1466, 1001, '2026-07-03', 1, 27, 0, 1783031255),
(1467, 1001, '2026-07-03', 1, 28, 0, 1783031315),
(1468, 1001, '2026-07-03', 1, 29, 0, 1783031375),
(1469, 1001, '2026-07-03', 1, 30, 0, 1783031435),
(1470, 1001, '2026-07-03', 1, 31, 0, 1783031495),
(1471, 1001, '2026-07-03', 1, 32, 0, 1783031555),
(1472, 1001, '2026-07-03', 1, 33, 0, 1783031615),
(1473, 1001, '2026-07-03', 1, 34, 0, 1783031675),
(1474, 1001, '2026-07-03', 1, 35, 0, 1783031735),
(1475, 1001, '2026-07-03', 1, 36, 0, 1783031795),
(1476, 1001, '2026-07-03', 1, 37, 0, 1783031855),
(1477, 1001, '2026-07-03', 1, 38, 0, 1783031915),
(1478, 1001, '2026-07-03', 1, 39, 0, 1783031975),
(1479, 1001, '2026-07-03', 1, 40, 0, 1783032035),
(1480, 1001, '2026-07-03', 1, 41, 0, 1783032095),
(1481, 1001, '2026-07-03', 1, 42, 0, 1783032155),
(1482, 1001, '2026-07-03', 1, 43, 0, 1783032215),
(1483, 1001, '2026-07-03', 1, 44, 0, 1783032275),
(1484, 1001, '2026-07-03', 1, 45, 0, 1783032335),
(1485, 1001, '2026-07-03', 1, 46, 0, 1783032395),
(1486, 1001, '2026-07-03', 1, 47, 0, 1783032455),
(1487, 1001, '2026-07-03', 1, 48, 0, 1783032515),
(1488, 1001, '2026-07-03', 1, 49, 0, 1783032575),
(1489, 1001, '2026-07-03', 1, 50, 0, 1783032635),
(1490, 1001, '2026-07-03', 1, 51, 0, 1783032695),
(1491, 1001, '2026-07-03', 1, 52, 0, 1783032755),
(1492, 1001, '2026-07-03', 1, 53, 0, 1783032815),
(1493, 1001, '2026-07-03', 1, 54, 0, 1783032875),
(1494, 1001, '2026-07-03', 1, 55, 0, 1783032935);

-- --------------------------------------------------------

--
-- Структура таблицы `t_serverstate`
--

CREATE TABLE `t_serverstate` (
  `id` int(32) NOT NULL,
  `serverId` int(32) DEFAULT NULL COMMENT '服务器区号',
  `state` int(32) DEFAULT NULL COMMENT '服务器的状态',
  `ip` varchar(50) DEFAULT NULL COMMENT '服务器IP',
  `isConnectWord` int(32) DEFAULT NULL COMMENT '是否连接好world',
  `currentNum` int(32) DEFAULT NULL COMMENT '服务器的注册人数',
  `updateTime` varchar(50) DEFAULT NULL COMMENT '最后一次更新的时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务器状态表';

--
-- Дамп данных таблицы `t_serverstate`
--

INSERT INTO `t_serverstate` (`id`, `serverId`, `state`, `ip`, `isConnectWord`, `currentNum`, `updateTime`) VALUES
(1, 1001, 0, '', 1, 0, '2026-07-03 01:55:35');

-- --------------------------------------------------------

--
-- Структура таблицы `t_switch`
--

CREATE TABLE `t_switch` (
  `id` int(32) NOT NULL COMMENT 'ID',
  `state` int(32) DEFAULT 0 COMMENT '0???1??'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='开关表';

-- --------------------------------------------------------

--
-- Структура таблицы `t_update_notice`
--

CREATE TABLE `t_update_notice` (
  `id` int(32) NOT NULL COMMENT 'ID',
  `serverIds` varchar(128) DEFAULT NULL COMMENT '服务器ID',
  `content` text DEFAULT NULL COMMENT '公告内容',
  `reward` varchar(128) DEFAULT NULL COMMENT '公告奖励',
  `type` int(32) DEFAULT NULL COMMENT '操作类型，0 ：只更新公告， 1： 重置奖励'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `t_user`
--

CREATE TABLE `t_user` (
  `id` int(32) NOT NULL COMMENT '后台ID',
  `name` varchar(50) NOT NULL COMMENT '账号名字',
  `passwd` varchar(50) DEFAULT NULL COMMENT '密码',
  `salt` varchar(50) DEFAULT NULL COMMENT '加盐算法',
  `ct` datetime DEFAULT NULL COMMENT '创建时间',
  `ut` datetime DEFAULT NULL COMMENT '最后修改时间',
  `ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `language` varchar(20) DEFAULT NULL COMMENT '语言',
  `isDeleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '生效标记 0:生效1:无效'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台账户表';

--
-- Дамп данных таблицы `t_user`
--

INSERT INTO `t_user` (`id`, `name`, `passwd`, `salt`, `ct`, `ut`, `ip`, `language`, `isDeleted`) VALUES
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

-- --------------------------------------------------------

--
-- Структура таблицы `t_user_role`
--

CREATE TABLE `t_user_role` (
  `userId` int(32) DEFAULT NULL COMMENT '后台用户ID',
  `roleId` int(32) DEFAULT NULL COMMENT '后台角色id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台用户角色关联表';

--
-- Дамп данных таблицы `t_user_role`
--

INSERT INTO `t_user_role` (`userId`, `roleId`) VALUES
(1, 1);

-- --------------------------------------------------------

--
-- Структура таблицы `t_whitelist`
--

CREATE TABLE `t_whitelist` (
  `id` int(32) NOT NULL,
  `lsId` int(32) DEFAULT NULL COMMENT '登录服ID',
  `whiteCon` varchar(200) DEFAULT NULL COMMENT '白名单条件',
  `ctype` int(32) DEFAULT NULL COMMENT '是添加还是删除',
  `createtime` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `userName` varchar(50) DEFAULT NULL COMMENT '创建人',
  `userIP` varchar(50) DEFAULT NULL COMMENT '创建人的IP',
  `backStr` varchar(500) DEFAULT NULL COMMENT '登陆服返回的结果'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='白名单表';

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `channel`
--
ALTER TABLE `channel`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `db_game`
--
ALTER TABLE `db_game`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `serverName` (`serverName`);

--
-- Индексы таблицы `game_info`
--
ALTER TABLE `game_info`
  ADD PRIMARY KEY (`gameId`);

--
-- Индексы таблицы `t_activity`
--
ALTER TABLE `t_activity`
  ADD PRIMARY KEY (`id`),
  ADD KEY `state_is_end` (`state`,`isDeleted`,`endTime`),
  ADD KEY `type_is_end` (`type`,`isDeleted`,`endTime`),
  ADD KEY `type_is` (`type`,`isDeleted`);

--
-- Индексы таблицы `t_activity_boss_type`
--
ALTER TABLE `t_activity_boss_type`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_activity_lucky_value`
--
ALTER TABLE `t_activity_lucky_value`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_activity_template`
--
ALTER TABLE `t_activity_template`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UX_t_activity_template_type_createTime` (`type`,`createTime`);

--
-- Индексы таблицы `t_announce`
--
ALTER TABLE `t_announce`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_api_log`
--
ALTER TABLE `t_api_log`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_backend_log`
--
ALTER TABLE `t_backend_log`
  ADD PRIMARY KEY (`id`),
  ADD KEY `userId_index` (`userId`);

--
-- Индексы таблицы `t_blackip`
--
ALTER TABLE `t_blackip`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_blackuser`
--
ALTER TABLE `t_blackuser`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_code_batch`
--
ALTER TABLE `t_code_batch`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_cyannounce`
--
ALTER TABLE `t_cyannounce`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_dblog`
--
ALTER TABLE `t_dblog`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_deduct_item`
--
ALTER TABLE `t_deduct_item`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_errorlog`
--
ALTER TABLE `t_errorlog`
  ADD PRIMARY KEY (`id`),
  ADD KEY `s_re` (`serverId`,`receTime`),
  ADD KEY `type_re` (`type`,`receTime`),
  ADD KEY `s_t_k_re` (`serverId`,`type`,`mKey`,`receTime`),
  ADD KEY `t_k_re` (`type`,`mKey`,`receTime`),
  ADD KEY `receTime` (`receTime`);

--
-- Индексы таблицы `t_evaluate`
--
ALTER TABLE `t_evaluate`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_forbidchat`
--
ALTER TABLE `t_forbidchat`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_forbiduser`
--
ALTER TABLE `t_forbiduser`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_gm_log`
--
ALTER TABLE `t_gm_log`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_item`
--
ALTER TABLE `t_item`
  ADD PRIMARY KEY (`itemId`);

--
-- Индексы таблицы `t_mail`
--
ALTER TABLE `t_mail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `createDate_isdel` (`isDelete`,`createDate`),
  ADD KEY `createUser` (`createUser`,`isDelete`,`createDate`),
  ADD KEY `sended` (`sended`,`isDelete`),
  ADD KEY `isDelete` (`isDelete`);

--
-- Индексы таблицы `t_mail_all`
--
ALTER TABLE `t_mail_all`
  ADD PRIMARY KEY (`id`),
  ADD KEY `createDate_isdel` (`isDelete`,`createDate`),
  ADD KEY `createUser` (`createUser`,`isDelete`,`createDate`),
  ADD KEY `sended` (`sended`,`isDelete`),
  ADD KEY `isDelete` (`isDelete`);

--
-- Индексы таблицы `t_menu`
--
ALTER TABLE `t_menu`
  ADD PRIMARY KEY (`menuId`);

--
-- Индексы таблицы `t_model`
--
ALTER TABLE `t_model`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_recharge`
--
ALTER TABLE `t_recharge`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_recharge_item`
--
ALTER TABLE `t_recharge_item`
  ADD PRIMARY KEY (`goods_id`);

--
-- Индексы таблицы `t_recharge_item_log`
--
ALTER TABLE `t_recharge_item_log`
  ADD PRIMARY KEY (`id`),
  ADD KEY `userId_index` (`userId`);

--
-- Индексы таблицы `t_role`
--
ALTER TABLE `t_role`
  ADD PRIMARY KEY (`roleId`),
  ADD UNIQUE KEY `name` (`roleName`);

--
-- Индексы таблицы `t_role_attr`
--
ALTER TABLE `t_role_attr`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_server`
--
ALTER TABLE `t_server`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_servernum`
--
ALTER TABLE `t_servernum`
  ADD PRIMARY KEY (`id`),
  ADD KEY `serverId_index` (`serverId`),
  ADD KEY `time_index` (`time`);

--
-- Индексы таблицы `t_serverstate`
--
ALTER TABLE `t_serverstate`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `serverId_index` (`serverId`);

--
-- Индексы таблицы `t_switch`
--
ALTER TABLE `t_switch`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_update_notice`
--
ALTER TABLE `t_update_notice`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `t_user`
--
ALTER TABLE `t_user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Индексы таблицы `t_whitelist`
--
ALTER TABLE `t_whitelist`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `channel`
--
ALTER TABLE `channel`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '渠道id';

--
-- AUTO_INCREMENT для таблицы `db_game`
--
ALTER TABLE `db_game`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `t_activity`
--
ALTER TABLE `t_activity`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '活动ID';

--
-- AUTO_INCREMENT для таблицы `t_activity_boss_type`
--
ALTER TABLE `t_activity_boss_type`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '活动BOSS分类配置ID';

--
-- AUTO_INCREMENT для таблицы `t_activity_lucky_value`
--
ALTER TABLE `t_activity_lucky_value`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '活动ID';

--
-- AUTO_INCREMENT для таблицы `t_activity_template`
--
ALTER TABLE `t_activity_template`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '活动模板ID';

--
-- AUTO_INCREMENT для таблицы `t_announce`
--
ALTER TABLE `t_announce`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `t_api_log`
--
ALTER TABLE `t_api_log`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `t_backend_log`
--
ALTER TABLE `t_backend_log`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `t_blackip`
--
ALTER TABLE `t_blackip`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT для таблицы `t_blackuser`
--
ALTER TABLE `t_blackuser`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT для таблицы `t_code_batch`
--
ALTER TABLE `t_code_batch`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `t_cyannounce`
--
ALTER TABLE `t_cyannounce`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '公告的编号';

--
-- AUTO_INCREMENT для таблицы `t_dblog`
--
ALTER TABLE `t_dblog`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `t_deduct_item`
--
ALTER TABLE `t_deduct_item`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '道具扣除ID';

--
-- AUTO_INCREMENT для таблицы `t_errorlog`
--
ALTER TABLE `t_errorlog`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `t_evaluate`
--
ALTER TABLE `t_evaluate`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '评价ID';

--
-- AUTO_INCREMENT для таблицы `t_forbidchat`
--
ALTER TABLE `t_forbidchat`
  MODIFY `id` bigint(64) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `t_forbiduser`
--
ALTER TABLE `t_forbiduser`
  MODIFY `id` bigint(64) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `t_gm_log`
--
ALTER TABLE `t_gm_log`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `t_item`
--
ALTER TABLE `t_item`
  MODIFY `itemId` int(32) NOT NULL AUTO_INCREMENT COMMENT '物品Id';

--
-- AUTO_INCREMENT для таблицы `t_mail`
--
ALTER TABLE `t_mail`
  MODIFY `id` bigint(64) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `t_mail_all`
--
ALTER TABLE `t_mail_all`
  MODIFY `id` bigint(64) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `t_menu`
--
ALTER TABLE `t_menu`
  MODIFY `menuId` int(32) NOT NULL AUTO_INCREMENT COMMENT '菜单ID', AUTO_INCREMENT=259;

--
-- AUTO_INCREMENT для таблицы `t_model`
--
ALTER TABLE `t_model`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT для таблицы `t_recharge`
--
ALTER TABLE `t_recharge`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '充值ID';

--
-- AUTO_INCREMENT для таблицы `t_recharge_item_log`
--
ALTER TABLE `t_recharge_item_log`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `t_role`
--
ALTER TABLE `t_role`
  MODIFY `roleId` int(32) NOT NULL AUTO_INCREMENT COMMENT '角色id', AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT для таблицы `t_role_attr`
--
ALTER TABLE `t_role_attr`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '属性设置ID';

--
-- AUTO_INCREMENT для таблицы `t_server`
--
ALTER TABLE `t_server`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT для таблицы `t_servernum`
--
ALTER TABLE `t_servernum`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1495;

--
-- AUTO_INCREMENT для таблицы `t_serverstate`
--
ALTER TABLE `t_serverstate`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT для таблицы `t_switch`
--
ALTER TABLE `t_switch`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT для таблицы `t_update_notice`
--
ALTER TABLE `t_update_notice`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'ID';

--
-- AUTO_INCREMENT для таблицы `t_user`
--
ALTER TABLE `t_user`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '后台ID', AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT для таблицы `t_whitelist`
--
ALTER TABLE `t_whitelist`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
