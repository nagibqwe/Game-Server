-- phpMyAdmin SQL Dump
-- version 5.2.1deb3
-- https://www.phpmyadmin.net/
--
-- Хост: localhost:3306
-- Время создания: Июл 02 2026 г., 22:56
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
-- База данных: `tzj_login`
--

-- --------------------------------------------------------

--
-- Структура таблицы `activecode`
--

CREATE TABLE `activecode` (
  `id` int(10) UNSIGNED NOT NULL COMMENT '激活码ID',
  `code` char(32) NOT NULL COMMENT '激活码',
  `activeName` varchar(128) DEFAULT NULL COMMENT '激活码类型名',
  `batch` char(32) DEFAULT NULL COMMENT '批次号',
  `itemList` text DEFAULT NULL COMMENT '物品id',
  `param` int(4) DEFAULT 0 COMMENT '参数（默认=0）,1表示万能码',
  `valide_time_begin` datetime DEFAULT NULL COMMENT '有效开始时间（NULL表示无）',
  `valide_time_end` datetime DEFAULT NULL COMMENT '有效结束时间（NULL表示无）',
  `plateform_name_big` varchar(128) DEFAULT NULL COMMENT '平台名（大）',
  `plateform_name_small` varchar(128) DEFAULT NULL COMMENT '平台名（小）',
  `valide_server_id_list` varchar(512) DEFAULT NULL COMMENT '仅某些服有效（[]表示通用），例如：2、3、4服=[2,3,4]',
  `create_time` int(4) DEFAULT 0 COMMENT '创建时间',
  `get_time` int(4) DEFAULT 0 COMMENT '激活码使用时间',
  `get_player_id` bigint(8) DEFAULT 0 COMMENT '激活码使用角色id',
  `get_server_id` bigint(8) DEFAULT 0 COMMENT '服务器编号',
  `get_account_id` bigint(8) DEFAULT 0 COMMENT '激活码使用者帐号id',
  `get_plateform_aid` varchar(255) DEFAULT NULL COMMENT '激活码使用者平台帐号id',
  `get_plateform_name` varchar(64) DEFAULT NULL COMMENT '激活码使用者平台名',
  `deleteTime` bigint(64) DEFAULT 0 COMMENT '激活码使用者平台名'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='激活码表';

-- --------------------------------------------------------

--
-- Структура таблицы `chatblacklist`
--

CREATE TABLE `chatblacklist` (
  `userId` bigint(20) NOT NULL DEFAULT 0 COMMENT '账号ID',
  `serverId` int(11) NOT NULL DEFAULT 0 COMMENT '服务器ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天黑名单';

-- --------------------------------------------------------

--
-- Структура таблицы `chatword`
--

CREATE TABLE `chatword` (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `serverId` int(11) NOT NULL COMMENT '服务器ID',
  `word` varchar(190) NOT NULL COMMENT '内容或关键字',
  `replace` varchar(190) DEFAULT NULL COMMENT '替换内容或关键字',
  `type` int(11) DEFAULT NULL COMMENT '0 关键字 1 内容'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天屏蔽内容替换表';

-- --------------------------------------------------------

--
-- Структура таблицы `forbid`
--

CREATE TABLE `forbid` (
  `str` varchar(64) NOT NULL DEFAULT '' COMMENT '屏蔽字符串',
  `time` int(4) NOT NULL COMMENT '屏蔽时间，-1是永久屏蔽'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动态屏蔽字表';

-- --------------------------------------------------------

--
-- Структура таблицы `forbidspeeking`
--

CREATE TABLE `forbidspeeking` (
  `userId` bigint(20) NOT NULL DEFAULT 0 COMMENT '账号ID',
  `forbidType` int(11) DEFAULT 0 COMMENT '禁言类型1:工作室禁言2:全文替换禁言3:关键字替换禁言4:常规禁言5:隐形禁言6:隔离禁言',
  `createTime` varchar(50) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `endTime` bigint(20) NOT NULL DEFAULT 0 COMMENT '结束时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='禁言类型表';

-- --------------------------------------------------------

--
-- Структура таблицы `platforms`
--

CREATE TABLE `platforms` (
  `str` varchar(64) NOT NULL DEFAULT '' COMMENT '渠道名'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='渠道表';

--
-- Дамп данных таблицы `platforms`
--

INSERT INTO `platforms` (`str`) VALUES
('PC');

-- --------------------------------------------------------

--
-- Структура таблицы `rechargereturn`
--

CREATE TABLE `rechargereturn` (
  `userId` bigint(20) NOT NULL COMMENT '游戏生成的账号Id',
  `roleId` bigint(20) DEFAULT NULL COMMENT '领取返还的角色Id',
  `createSid` int(11) DEFAULT NULL COMMENT '领取返还的角色创建服',
  `rechargeTotalMoney` int(11) DEFAULT 0 COMMENT '账号删档测试期间充值总数',
  `returnGold` int(11) DEFAULT 0 COMMENT '返还等量的元宝数',
  `returnTime` bigint(20) DEFAULT NULL COMMENT '领取返还的时间',
  `userName` varchar(255) DEFAULT NULL COMMENT '553平台生成的账号名字',
  `platformAccount` varchar(255) DEFAULT NULL COMMENT '平台生成的账号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试删档返利表';

-- --------------------------------------------------------

--
-- Структура таблицы `rechargetotalmoney`
--

CREATE TABLE `rechargetotalmoney` (
  `userId` bigint(20) NOT NULL,
  `rechargeTotalMoney` int(11) DEFAULT 0 COMMENT '账号删档测试期间总充值',
  `userName` varchar(255) DEFAULT NULL COMMENT '553平台生成的账号名字',
  `platformAccount` varchar(255) DEFAULT NULL COMMENT '平台生成的账号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试删档充值表';

-- --------------------------------------------------------

--
-- Структура таблицы `rolelogin`
--

CREATE TABLE `rolelogin` (
  `roleId` bigint(20) NOT NULL DEFAULT 0 COMMENT '角色id',
  `userId` bigint(20) NOT NULL COMMENT '账号id',
  `serverId` int(11) NOT NULL COMMENT '服务器id',
  `roleName` varchar(64) NOT NULL COMMENT '角色名',
  `lv` int(11) NOT NULL COMMENT '等级',
  `career` tinyint(4) NOT NULL COMMENT '职业',
  `deleteTime` int(4) NOT NULL COMMENT '角色删除时间，0表示未删除',
  `fight` bigint(255) NOT NULL COMMENT '战力'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色登录表';

--
-- Дамп данных таблицы `rolelogin`
--

INSERT INTO `rolelogin` (`roleId`, `userId`, `serverId`, `roleName`, `lv`, `career`, `deleteTime`, `fight`) VALUES
(281873304391993039, 844541782760554497, 1001, '解半云', 5, 0, 0, 7998);

-- --------------------------------------------------------

--
-- Структура таблицы `servername`
--

CREATE TABLE `servername` (
  `serverId` int(11) NOT NULL COMMENT '服务器编号',
  `changeName` varchar(64) DEFAULT NULL COMMENT '更改后的服务器名字',
  `changeTime` int(11) DEFAULT NULL COMMENT '更改时间',
  `roleId` bigint(20) DEFAULT NULL COMMENT '更改的玩家id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务器改名表';

-- --------------------------------------------------------

--
-- Структура таблицы `tag`
--

CREATE TABLE `tag` (
  `id` int(11) NOT NULL COMMENT '标签ID',
  `name` varchar(255) DEFAULT NULL COMMENT '标签名',
  `icon` varchar(500) DEFAULT NULL COMMENT '标签icon',
  `style` int(11) DEFAULT NULL COMMENT 'UI风格'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签库';

-- --------------------------------------------------------

--
-- Структура таблицы `userlogin`
--

CREATE TABLE `userlogin` (
  `userId` bigint(8) NOT NULL COMMENT '游戏生成的账号id',
  `userName` varchar(190) NOT NULL COMMENT '553平台生成的账号名字',
  `platformAccount` varchar(190) NOT NULL COMMENT '平台生成的账号',
  `platformName` varchar(50) NOT NULL COMMENT '平台名',
  `lastEnterServerId` int(4) DEFAULT 0 COMMENT '上次进入的区服id',
  `data` text NOT NULL COMMENT '区服创建角色信息',
  `createTime` int(4) NOT NULL COMMENT '创建时间',
  `isDelete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1删除',
  `forbidenTime` int(4) NOT NULL DEFAULT 0 COMMENT '禁止登录结束时间,0正常登录，-1永久禁止',
  `lastLoginIp` varchar(100) DEFAULT NULL COMMENT '最后一次登录的IP',
  `lastEnterRoleId` bigint(20) DEFAULT NULL COMMENT '上次登录的角色id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户登录表';

--
-- Дамп данных таблицы `userlogin`
--

INSERT INTO `userlogin` (`userId`, `userName`, `platformAccount`, `platformName`, `lastEnterServerId`, `data`, `createTime`, `isDelete`, `forbidenTime`, `lastLoginIp`, `lastEnterRoleId`) VALUES
(844541782760554497, 'nagibqwe', '', 'PC', 1001, '[1001]', 1783029611, 0, 0, '95.26.138.241', 281873304391993039);

-- --------------------------------------------------------

--
-- Структура таблицы `white`
--

CREATE TABLE `white` (
  `str` varchar(64) NOT NULL DEFAULT '' COMMENT '白名单账号，可以是mac、imei、ip、funcellUUid、machineCode'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='白名单表';

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `activecode`
--
ALTER TABLE `activecode`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `NewIndex1` (`code`) USING BTREE,
  ADD KEY `NewIndex2` (`create_time`) USING BTREE,
  ADD KEY `NewIndex3` (`batch`) USING BTREE,
  ADD KEY `NewIndex4` (`get_player_id`) USING BTREE,
  ADD KEY `NewIndex5` (`get_time`) USING BTREE;

--
-- Индексы таблицы `chatblacklist`
--
ALTER TABLE `chatblacklist`
  ADD PRIMARY KEY (`userId`,`serverId`);

--
-- Индексы таблицы `chatword`
--
ALTER TABLE `chatword`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `serverId_word` (`serverId`,`word`);

--
-- Индексы таблицы `forbid`
--
ALTER TABLE `forbid`
  ADD PRIMARY KEY (`str`);

--
-- Индексы таблицы `forbidspeeking`
--
ALTER TABLE `forbidspeeking`
  ADD PRIMARY KEY (`userId`);

--
-- Индексы таблицы `platforms`
--
ALTER TABLE `platforms`
  ADD PRIMARY KEY (`str`);

--
-- Индексы таблицы `rechargereturn`
--
ALTER TABLE `rechargereturn`
  ADD PRIMARY KEY (`userId`);

--
-- Индексы таблицы `rechargetotalmoney`
--
ALTER TABLE `rechargetotalmoney`
  ADD PRIMARY KEY (`userId`);

--
-- Индексы таблицы `rolelogin`
--
ALTER TABLE `rolelogin`
  ADD PRIMARY KEY (`roleId`) USING BTREE;

--
-- Индексы таблицы `servername`
--
ALTER TABLE `servername`
  ADD PRIMARY KEY (`serverId`) USING BTREE;

--
-- Индексы таблицы `tag`
--
ALTER TABLE `tag`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `userlogin`
--
ALTER TABLE `userlogin`
  ADD UNIQUE KEY `userId` (`userId`) USING BTREE,
  ADD UNIQUE KEY `index1` (`userName`) USING BTREE,
  ADD KEY `index2` (`platformAccount`) USING BTREE,
  ADD KEY `index3` (`platformName`) USING BTREE,
  ADD KEY `index4` (`createTime`) USING BTREE;

--
-- Индексы таблицы `white`
--
ALTER TABLE `white`
  ADD PRIMARY KEY (`str`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `activecode`
--
ALTER TABLE `activecode`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '激活码ID';

--
-- AUTO_INCREMENT для таблицы `chatword`
--
ALTER TABLE `chatword`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID', AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT для таблицы `rechargereturn`
--
ALTER TABLE `rechargereturn`
  MODIFY `userId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '游戏生成的账号Id';

--
-- AUTO_INCREMENT для таблицы `rechargetotalmoney`
--
ALTER TABLE `rechargetotalmoney`
  MODIFY `userId` bigint(20) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
