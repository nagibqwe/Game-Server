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
-- База данных: `tzj_stat_log`
--

-- --------------------------------------------------------

--
-- Структура таблицы `stat_last_insert`
--

CREATE TABLE `stat_last_insert` (
  `id` int(20) NOT NULL,
  `sid` int(11) NOT NULL DEFAULT 0,
  `src_table` varchar(64) NOT NULL DEFAULT '' COMMENT '表名',
  `src_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '最后一次插入的时间',
  `src_id` int(11) UNSIGNED ZEROFILL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='记录抓数据最后一次时间' ROW_FORMAT=COMPACT;

-- --------------------------------------------------------

--
-- Структура таблицы `stat_login`
--

CREATE TABLE `stat_login` (
  `srcId` int(11) NOT NULL DEFAULT 0 COMMENT '平台ID',
  `platId` varchar(256) NOT NULL COMMENT '渠道ID',
  `serverId` int(11) UNSIGNED DEFAULT 0 COMMENT '区服ID',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `machineCode` varchar(70) DEFAULT NULL,
  `roleId` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色ID',
  `timeLogin` datetime NOT NULL COMMENT '时间-登录',
  `time` bigint(20) NOT NULL DEFAULT 0,
  `sid` int(11) NOT NULL DEFAULT 0 COMMENT '//原区服ID',
  `level` int(4) DEFAULT NULL,
  `platformName` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='统计-登录表';

-- --------------------------------------------------------

--
-- Структура таблицы `stat_recharge`
--

CREATE TABLE `stat_recharge` (
  `srcId` int(11) NOT NULL DEFAULT 0 COMMENT '平台ID',
  `roleId` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  `sid` int(11) NOT NULL DEFAULT 0,
  `orderNo` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `gameMoney` int(11) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `statusReason` int(11) DEFAULT NULL,
  `addTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `time` datetime DEFAULT NULL,
  `totalFee` int(11) DEFAULT NULL,
  `itemId` int(11) DEFAULT NULL,
  `platformName` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `timesec` bigint(20) DEFAULT NULL,
  `src` tinyint(4) DEFAULT NULL,
  `goodsId` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `stat_role`
--

CREATE TABLE `stat_role` (
  `userId` bigint(64) NOT NULL COMMENT '用户ID',
  `roleId` bigint(64) NOT NULL COMMENT '角色ID',
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
  `createTime` varchar(50) DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='统计-角色表';

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `stat_last_insert`
--
ALTER TABLE `stat_last_insert`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD KEY `sid` (`sid`) USING BTREE;

--
-- Индексы таблицы `stat_login`
--
ALTER TABLE `stat_login`
  ADD PRIMARY KEY (`srcId`,`time`,`sid`),
  ADD KEY `test` (`roleId`,`timeLogin`),
  ADD KEY `timeLogin` (`timeLogin`) USING BTREE,
  ADD KEY `roleId` (`roleId`) USING BTREE;

--
-- Индексы таблицы `stat_recharge`
--
ALTER TABLE `stat_recharge`
  ADD PRIMARY KEY (`srcId`,`sid`,`addTime`),
  ADD KEY `index_1` (`timesec`) USING BTREE,
  ADD KEY `index_2` (`sid`) USING BTREE,
  ADD KEY `index_3` (`roleId`) USING BTREE;

--
-- Индексы таблицы `stat_role`
--
ALTER TABLE `stat_role`
  ADD PRIMARY KEY (`roleId`,`createsid`),
  ADD UNIQUE KEY `key` (`roleId`) USING BTREE,
  ADD KEY `userId_sid` (`userId`,`createsid`) USING BTREE,
  ADD KEY `index_2` (`userId`) USING BTREE,
  ADD KEY `role_sid` (`roleId`,`createsid`) USING BTREE,
  ADD KEY `index_3` (`roleId`) USING BTREE;

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `stat_last_insert`
--
ALTER TABLE `stat_last_insert`
  MODIFY `id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5593;

--
-- AUTO_INCREMENT для таблицы `stat_role`
--
ALTER TABLE `stat_role`
  MODIFY `roleId` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '角色ID', AUTO_INCREMENT=5655221081082453064;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
