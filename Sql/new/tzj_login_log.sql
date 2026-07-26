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
-- База данных: `tzj_login_log`
--

-- --------------------------------------------------------

--
-- Структура таблицы `loguser202607`
--

CREATE TABLE `loguser202607` (
  `id` int(11) NOT NULL,
  `lastLoginIp` varchar(32) DEFAULT NULL,
  `userName` varchar(64) DEFAULT NULL,
  `imei` varchar(64) DEFAULT NULL,
  `machineCode` varchar(64) DEFAULT NULL,
  `platformName` varchar(16) DEFAULT NULL,
  `createTime` int(11) DEFAULT NULL,
  `platformAccount` varchar(64) DEFAULT NULL,
  `mac` varchar(64) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  `userid` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

--
-- Дамп данных таблицы `loguser202607`
--

INSERT INTO `loguser202607` (`id`, `lastLoginIp`, `userName`, `imei`, `machineCode`, `platformName`, `createTime`, `platformAccount`, `mac`, `time`, `userid`) VALUES
(1, '95.26.138.241', 'nagibqwe', '63c0f016bbf4405e', '63c0f016bbf4405e', 'PC', 1783029611, '', '63c0f016bbf4405e', 1783029611, 844541782760554497),
(2, '95.26.138.241', 'nagibqwe', '63c0f016bbf4405e', '63c0f016bbf4405e', 'PC', 1783029611, '', '63c0f016bbf4405e', 1783030009, 844541782760554497),
(3, '95.26.138.241', 'nagibqwe', '63c0f016bbf4405e', '63c0f016bbf4405e', 'PC', 1783029611, '', '63c0f016bbf4405e', 1783030704, 844541782760554497);

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `loguser202607`
--
ALTER TABLE `loguser202607`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_1` (`time`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `loguser202607`
--
ALTER TABLE `loguser202607`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
