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
-- База данных: `tzj_social`
--

-- --------------------------------------------------------

--
-- Структура таблицы `friend`
--

CREATE TABLE `friend` (
  `roleId` bigint(20) NOT NULL COMMENT '角色ID',
  `latelyPlayers` longtext DEFAULT NULL COMMENT '最近聊天列表',
  `friends` longtext DEFAULT NULL COMMENT '好友列表',
  `enemies` longtext DEFAULT NULL COMMENT '仇人列表',
  `shields` longtext DEFAULT NULL COMMENT '屏蔽列表',
  `sendLogs` longtext DEFAULT NULL COMMENT '送出礼物的日志',
  `receiveLogs` longtext DEFAULT NULL COMMENT '接收礼物的日志'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='好友表';

-- --------------------------------------------------------

--
-- Структура таблицы `player`
--

CREATE TABLE `player` (
  `id` bigint(20) NOT NULL COMMENT '角色ID',
  `plat` varchar(64) DEFAULT NULL COMMENT '平台',
  `serverId` int(11) DEFAULT NULL COMMENT '角色当前服务器',
  `createServerId` int(11) DEFAULT NULL COMMENT '角色创建服务器',
  `createTime` bigint(20) DEFAULT NULL COMMENT '角色创建时间',
  `userId` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `roleName` varchar(64) DEFAULT NULL COMMENT '角色名',
  `fightPower` bigint(20) DEFAULT NULL COMMENT '战力',
  `level` int(11) DEFAULT NULL COMMENT '等级',
  `career` int(11) DEFAULT NULL COMMENT '职业',
  `playerVip` int(11) DEFAULT NULL COMMENT 'vip等级',
  `data` longtext DEFAULT NULL COMMENT '玩家其他数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家信息表';

--
-- Дамп данных таблицы `player`
--

INSERT INTO `player` (`id`, `plat`, `serverId`, `createServerId`, `createTime`, `userId`, `roleName`, `fightPower`, `level`, `career`, `playerVip`, `data`) VALUES
(281873304391993039, 'cn', 1001, 1001, 1783030771, 844541782760554497, '解半云', 7998, 5, 0, 0, '{\"dealTime\":0,\"expRate\":1.0,\"popularity\":0,\"lastOffTime\":1783030871,\"horseId\":0,\"wingId\":0,\"guildId\":0,\"fashionHeadId\":1100000001,\"fashionHeadFrameId\":1200000001,\"fashionBodyId\":110000108,\"fashionWeaponId\":210000100,\"fashionHalo\":0,\"fashionMatrix\":0,\"sex\":1,\"stateVip\":0,\"shiHaiLevel\":0,\"spiritId\":0,\"soulArmorId\":0,\"counts\":{},\"house\":{\"level\":1,\"tupLevel\":1,\"tupExp\":100,\"vote\":0,\"decorate\":100,\"authUnFriendEnter\":false,\"authUnFriendGift\":true,\"helper\":0,\"store\":{\"20001\":{\"modelId\":20001,\"count\":0}},\"style\":{\"1407491736257888257\":{\"id\":1407491736257888257,\"modelId\":20001,\"pos\":{\"x\":0.0,\"y\":0.0,\"z\":0.0},\"dir\":0}},\"ve\":[]},\"playerCommunityInfoSettingInfo\":{\"decorate\":0,\"pendan\":0,\"notFriendLeaveMsg\":false},\"commcunityLeaveMessageInfoList\":[],\"customHeadPath\":\"\",\"useCustomHead\":true,\"friendCircleInfoList\":[],\"guildName\":\"\"}');

-- --------------------------------------------------------

--
-- Структура таблицы `serverparam`
--

CREATE TABLE `serverparam` (
  `paramkey` varchar(64) NOT NULL,
  `serverid` int(11) DEFAULT NULL,
  `paramvalue` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务器参数表';

--
-- Дамп данных таблицы `serverparam`
--

INSERT INTO `serverparam` (`paramkey`, `serverid`, `paramvalue`) VALUES
('HomeRankTurn', 5000, '1');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `friend`
--
ALTER TABLE `friend`
  ADD PRIMARY KEY (`roleId`);

--
-- Индексы таблицы `player`
--
ALTER TABLE `player`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `serverparam`
--
ALTER TABLE `serverparam`
  ADD PRIMARY KEY (`paramkey`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
