-- phpMyAdmin SQL Dump
-- version 5.2.1deb3
-- https://www.phpmyadmin.net/
--
-- Хост: localhost:3306
-- Время создания: Июл 02 2026 г., 22:57
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
-- База данных: `tzj_public`
--

-- --------------------------------------------------------

--
-- Структура таблицы `activityrank`
--

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动排行';

-- --------------------------------------------------------

--
-- Структура таблицы `couplefight`
--

CREATE TABLE `couplefight` (
  `activityId` int(11) NOT NULL COMMENT '活动id',
  `group` int(11) NOT NULL COMMENT '服务器组id',
  `data` longtext DEFAULT NULL COMMENT '数据',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `status` int(11) DEFAULT NULL COMMENT '活动状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='仙侣对决';

-- --------------------------------------------------------

--
-- Структура таблицы `couplefightguess`
--

CREATE TABLE `couplefightguess` (
  `id` int(11) NOT NULL COMMENT 'ID',
  `activityId` int(11) DEFAULT NULL COMMENT '活动id',
  `group` int(11) DEFAULT NULL COMMENT '服务器组',
  `type` int(11) DEFAULT NULL COMMENT '类型 1天 2地',
  `round` int(11) DEFAULT NULL COMMENT '所在轮次',
  `fightId` int(11) DEFAULT NULL COMMENT '战斗id',
  `teamId` bigint(20) DEFAULT NULL COMMENT '队伍id',
  `rid` bigint(20) DEFAULT NULL COMMENT '竞猜玩家id',
  `name` varchar(45) DEFAULT NULL COMMENT '竞猜玩家名称',
  `level` int(11) DEFAULT NULL COMMENT '竞猜玩家等级',
  `power` bigint(20) DEFAULT NULL COMMENT '竞猜玩家战力'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='仙侣对决竞猜';

-- --------------------------------------------------------

--
-- Структура таблицы `couplefightteam`
--

CREATE TABLE `couplefightteam` (
  `id` bigint(20) NOT NULL COMMENT '队伍id',
  `activityId` int(11) DEFAULT NULL COMMENT '活动id',
  `group` int(11) DEFAULT NULL COMMENT '组id',
  `data` longtext DEFAULT NULL COMMENT '数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='仙侣对决队伍';

-- --------------------------------------------------------

--
-- Структура таблицы `crossrank`
--

CREATE TABLE `crossrank` (
  `roleId` bigint(50) NOT NULL COMMENT '角色唯一ID',
  `roleName` varchar(64) DEFAULT NULL COMMENT '角色名',
  `serverId` int(5) DEFAULT NULL COMMENT '服务器ID',
  `career` tinyint(4) DEFAULT NULL COMMENT '职业',
  `stateVip` tinyint(4) DEFAULT 0 COMMENT '境界等级',
  `level` int(11) DEFAULT 0 COMMENT '角色等级',
  `fightPower` bigint(20) DEFAULT 0 COMMENT '战斗力',
  `fashionBodyId` int(11) DEFAULT 0 COMMENT '时装身体',
  `fashionWeaponId` int(11) DEFAULT 0 COMMENT '时装武器',
  `wingModel` int(11) DEFAULT 0,
  `fashionHalo` int(11) DEFAULT 0,
  `fashionMatrix` int(11) DEFAULT 0,
  `spiritId` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='跨服排行';

-- --------------------------------------------------------

--
-- Структура таблицы `eightintegralrank`
--

CREATE TABLE `eightintegralrank` (
  `roleID` bigint(50) NOT NULL COMMENT '角色唯一ID',
  `name` varchar(64) DEFAULT NULL COMMENT '角色名',
  `hurt` bigint(50) DEFAULT 0 COMMENT '总伤害',
  `integral` int(11) DEFAULT 0 COMMENT '击杀积分',
  `platSid` varchar(20) DEFAULT NULL COMMENT '区服信息',
  `colorCamp` int(11) DEFAULT 0 COMMENT '阵营',
  `serverid` int(11) DEFAULT 0 COMMENT '服务器ID',
  `groupId` int(11) DEFAULT 0 COMMENT '组ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='八极阵图';

-- --------------------------------------------------------

--
-- Структура таблицы `fud`
--

CREATE TABLE `fud` (
  `groupId` int(10) NOT NULL COMMENT '福地分组ID',
  `data` longtext NOT NULL COMMENT '福地数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='跨服福地';

-- --------------------------------------------------------

--
-- Структура таблицы `fudrole`
--

CREATE TABLE `fudrole` (
  `roleId` bigint(20) NOT NULL COMMENT '玩家ID',
  `name` varchar(64) NOT NULL COMMENT '玩家名字',
  `platform` varchar(20) NOT NULL COMMENT '平台',
  `serverId` int(10) NOT NULL COMMENT '服务器ID',
  `tValue` int(10) NOT NULL COMMENT '天禁值',
  `score` int(10) NOT NULL COMMENT '个人积分',
  `kill` int(10) NOT NULL COMMENT '击杀',
  `lock` int(10) NOT NULL COMMENT '是否解锁奖励',
  `scoreReward` bigint(20) DEFAULT 0 COMMENT '奖励领取状态',
  `time` bigint(20) NOT NULL COMMENT '更新时间',
  `data` longtext NOT NULL COMMENT '数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='跨服福地积分排名';

-- --------------------------------------------------------

--
-- Структура таблицы `peakpk`
--

CREATE TABLE `peakpk` (
  `roleId` bigint(20) NOT NULL COMMENT '玩家ID',
  `name` varchar(64) NOT NULL COMMENT '玩家名字',
  `platform` varchar(20) NOT NULL COMMENT '平台',
  `serverId` int(10) NOT NULL COMMENT '服务器ID',
  `rankId` int(10) NOT NULL COMMENT '段位ID',
  `score` int(10) NOT NULL COMMENT '积分',
  `power` bigint(20) NOT NULL COMMENT '战力',
  `time` bigint(20) NOT NULL COMMENT '更新时间',
  `times` int(10) NOT NULL COMMENT '本赛季场次',
  `dayTimes` int(10) DEFAULT 0 COMMENT '当天参赛场次',
  `timesReward` bigint(20) DEFAULT 0 COMMENT '场次奖励领取状态',
  `stageReward` bigint(20) DEFAULT 0 COMMENT '段位奖励领取状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='巅峰竞技';

-- --------------------------------------------------------

--
-- Структура таблицы `serverparam`
--

CREATE TABLE `serverparam` (
  `paramkey` varchar(64) NOT NULL,
  `serverid` int(11) DEFAULT NULL,
  `paramvalue` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务器参数';

--
-- Дамп данных таблицы `serverparam`
--

INSERT INTO `serverparam` (`paramkey`, `serverid`, `paramvalue`) VALUES
('BRAVE_PEAK_DATA', 2000, '{\"maxFloor\":{},\"lastModifyTime\":1783026000175}'),
('CountKey', 2000, '{\"0_1\":{\"type\":\"Week\",\"key\":\"0_1\",\"count\":1,\"lastTime\":1782934431998,\"hour\":24,\"minute\":0,\"second\":0},\"0_2\":{\"type\":\"Day\",\"key\":\"0_2\",\"count\":1,\"lastTime\":1783026001179,\"hour\":0,\"minute\":0,\"second\":0},\"0_3\":{\"type\":\"Day\",\"key\":\"0_3\",\"count\":1,\"lastTime\":1782957601318,\"hour\":5,\"minute\":0,\"second\":0},\"0_4\":{\"type\":\"Day\",\"key\":\"0_4\",\"count\":1,\"lastTime\":1783026001183,\"hour\":0,\"minute\":0,\"second\":0},\"1_25\":{\"type\":\"Forever\",\"key\":\"1_25\",\"count\":0,\"lastTime\":1782934432003,\"hour\":0,\"minute\":0,\"second\":0},\"1_24\":{\"type\":\"Forever\",\"key\":\"1_24\",\"count\":0,\"lastTime\":1782934432003,\"hour\":0,\"minute\":0,\"second\":0},\"0_6\":{\"type\":\"Week\",\"key\":\"0_6\",\"count\":1,\"lastTime\":1782934440183,\"hour\":24,\"minute\":0,\"second\":0},\"1_23\":{\"type\":\"Forever\",\"key\":\"1_23\",\"count\":0,\"lastTime\":1782934432003,\"hour\":0,\"minute\":0,\"second\":0},\"1_22\":{\"type\":\"Forever\",\"key\":\"1_22\",\"count\":0,\"lastTime\":1782934432003,\"hour\":0,\"minute\":0,\"second\":0},\"1_21\":{\"type\":\"Forever\",\"key\":\"1_21\",\"count\":0,\"lastTime\":1782934432003,\"hour\":0,\"minute\":0,\"second\":0}}'),
('GameServerInfo', 2000, '{\"cn_1001\":{\"serverId\":1001,\"serverType\":1,\"serverIp\":\"155.212.166.16\",\"platName\":\"cn\",\"port\":9101,\"sids\":[1001],\"openTime\":\"2023-05-12 00:00:00\",\"serverWorldLv\":79,\"isMerge\":false,\"bigGroupID\":1,\"stageWithGroupIndex\":{\"1\":2,\"2\":10001,\"4\":20001,\"8\":30001},\"firstConnectTime\":1782934435779,\"isGMbackgroundSet\":false,\"gmbackgroundSet\":false}}'),
('peakSeasonKey', 2000, '1');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `couplefight`
--
ALTER TABLE `couplefight`
  ADD PRIMARY KEY (`activityId`,`group`);

--
-- Индексы таблицы `couplefightguess`
--
ALTER TABLE `couplefightguess`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `couplefightteam`
--
ALTER TABLE `couplefightteam`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `crossrank`
--
ALTER TABLE `crossrank`
  ADD PRIMARY KEY (`roleId`);

--
-- Индексы таблицы `eightintegralrank`
--
ALTER TABLE `eightintegralrank`
  ADD PRIMARY KEY (`roleID`);

--
-- Индексы таблицы `fud`
--
ALTER TABLE `fud`
  ADD PRIMARY KEY (`groupId`);

--
-- Индексы таблицы `fudrole`
--
ALTER TABLE `fudrole`
  ADD PRIMARY KEY (`roleId`),
  ADD KEY `score` (`score`) USING BTREE,
  ADD KEY `kill` (`kill`) USING BTREE;

--
-- Индексы таблицы `peakpk`
--
ALTER TABLE `peakpk`
  ADD PRIMARY KEY (`roleId`),
  ADD KEY `score` (`score`) USING BTREE;

--
-- Индексы таблицы `serverparam`
--
ALTER TABLE `serverparam`
  ADD PRIMARY KEY (`paramkey`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `couplefightguess`
--
ALTER TABLE `couplefightguess`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID';
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
