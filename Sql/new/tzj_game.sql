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
-- База данных: `tzj_game`
--

-- --------------------------------------------------------

--
-- Структура таблицы `activityconfig`
--

CREATE TABLE `activityconfig` (
  `id` int(11) NOT NULL COMMENT '活动ID',
  `type` int(11) NOT NULL COMMENT '活动类型',
  `minLv` int(11) NOT NULL DEFAULT 1 COMMENT '最小开放等级',
  `maxLv` int(11) NOT NULL DEFAULT 800 COMMENT '最大开放等级',
  `tag` tinyint(4) NOT NULL COMMENT '标签(用于区分展示在哪个活动标签下)',
  `sort` tinyint(4) NOT NULL DEFAULT 1 COMMENT '活动排序',
  `name` varchar(200) NOT NULL COMMENT '活动名称',
  `beginTime` bigint(20) NOT NULL COMMENT '活动开始时间',
  `endTime` bigint(20) NOT NULL COMMENT '活动结束时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除(1：是，0：否)',
  `custom` longtext NOT NULL COMMENT '自定义配置活动数据',
  `state` tinyint(4) NOT NULL DEFAULT 0 COMMENT '活动状态：0预发布，1进行中',
  `startRecordTime` bigint(20) NOT NULL DEFAULT 0 COMMENT '活动开始记录时间',
  `endRecordTime` bigint(20) NOT NULL DEFAULT 0 COMMENT '活动结束记录时间',
  `isOpenServer` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否为开服活动'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运营活动的配置数据表';

-- --------------------------------------------------------

--
-- Структура таблицы `activitydata`
--

CREATE TABLE `activitydata` (
  `type` int(11) NOT NULL COMMENT '活动类型',
  `actData` longtext NOT NULL COMMENT '活动相关数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运营活动的运行数据表';

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
  `name` varchar(20) DEFAULT NULL,
  `serverId` int(5) DEFAULT NULL,
  `plat` varchar(10) DEFAULT NULL,
  `serial` int(10) NOT NULL,
  `send` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运营活动的排行数据表';

-- --------------------------------------------------------

--
-- Структура таблицы `auction`
--

CREATE TABLE `auction` (
  `auctionId` bigint(20) NOT NULL COMMENT '竞拍ID',
  `auctionItem` varchar(2048) NOT NULL COMMENT '竞拍物品',
  `auctionTime` bigint(20) NOT NULL COMMENT '竞拍上架时间',
  `auctionPrice` int(11) NOT NULL COMMENT '竞拍价格',
  `auctionOwnId` bigint(20) NOT NULL COMMENT '竞拍上架着',
  `auctionRoleId` bigint(20) NOT NULL COMMENT '竞拍者',
  `auctionGuild` bigint(20) NOT NULL COMMENT '竞拍类型',
  `password` varchar(45) DEFAULT NULL COMMENT '竞拍密码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='竞拍';

-- --------------------------------------------------------

--
-- Структура таблицы `bossdierecord`
--

CREATE TABLE `bossdierecord` (
  `id` bigint(20) NOT NULL,
  `playerId` bigint(20) DEFAULT NULL,
  `mapName` varchar(50) DEFAULT NULL,
  `xPos` int(11) DEFAULT NULL,
  `yPos` int(11) DEFAULT NULL,
  `killedTime` bigint(20) DEFAULT NULL,
  `bossName` varchar(50) DEFAULT NULL,
  `reward` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='boss死亡记录表';

-- --------------------------------------------------------

--
-- Структура таблицы `chum`
--

CREATE TABLE `chum` (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '挚友ID',
  `rID1` bigint(20) UNSIGNED DEFAULT 0 COMMENT '创建者ID1',
  `rID2` bigint(20) UNSIGNED DEFAULT 0 COMMENT '创建者ID2',
  `name` varchar(128) DEFAULT '' COMMENT '挚友组名',
  `anno` varchar(255) DEFAULT '' COMMENT 'announcement',
  `lvl` int(11) UNSIGNED DEFAULT 0 COMMENT '等级',
  `exp` int(11) UNSIGNED DEFAULT 0 COMMENT '经验',
  `freeT` smallint(6) UNSIGNED DEFAULT 0 COMMENT '免费改名次数',
  `datas` text DEFAULT NULL COMMENT '数据',
  `lastFreshTime` bigint(20) UNSIGNED DEFAULT 0 COMMENT '最后刷新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='挚友';

-- --------------------------------------------------------

--
-- Структура таблицы `dailyaccrecharge`
--

CREATE TABLE `dailyaccrecharge` (
  `roleId` bigint(20) NOT NULL COMMENT '角色ID',
  `DailyAccRechargeData` text DEFAULT NULL COMMENT '每日累充数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='日常累充表';

-- --------------------------------------------------------

--
-- Структура таблицы `forbidword`
--

CREATE TABLE `forbidword` (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `word` varchar(500) DEFAULT NULL COMMENT '禁言内容或关键字',
  `type` int(11) DEFAULT NULL COMMENT '0 关键字 1 内容'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='禁言数据表';

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
  `receiveLogs` longtext DEFAULT NULL COMMENT '接收礼物的日志',
  `approvalList` longtext DEFAULT NULL COMMENT '审批列表',
  `shieldAddFriend` longtext DEFAULT NULL COMMENT '屏蔽好友申请列表'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='好友表';

-- --------------------------------------------------------

--
-- Структура таблицы `gold`
--

CREATE TABLE `gold` (
  `userId` bigint(20) DEFAULT NULL COMMENT '账号ID',
  `serverId` int(11) DEFAULT NULL COMMENT '服务器id',
  `platformName` varchar(64) DEFAULT NULL COMMENT '平台名',
  `rechargeGold` int(11) DEFAULT NULL COMMENT '充值获得元宝数',
  `reaminGold` int(11) DEFAULT NULL COMMENT '剩余元宝数',
  `costGold` int(11) DEFAULT NULL COMMENT '非交易消耗元宝数',
  `tradeAddGold` int(11) DEFAULT NULL COMMENT '交易获得元宝数',
  `tradeCostGold` int(11) DEFAULT NULL COMMENT '交易消耗元宝数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='元宝快照表';

-- --------------------------------------------------------

--
-- Структура таблицы `goldchange`
--

CREATE TABLE `goldchange` (
  `userid` bigint(20) DEFAULT NULL COMMENT '账号id',
  `roleId` bigint(20) DEFAULT NULL COMMENT '角色id',
  `serverId` int(4) DEFAULT NULL COMMENT '服务器id',
  `platformName` varchar(32) DEFAULT NULL COMMENT '渠道号',
  `beforeNum` int(4) DEFAULT NULL COMMENT '改变前数量',
  `changeNum` int(4) DEFAULT NULL COMMENT '改变数量，小于0表示减少',
  `afterNum` int(4) DEFAULT NULL COMMENT '改变后数量',
  `reason` int(4) DEFAULT NULL COMMENT '改变原因',
  `time` int(4) DEFAULT NULL COMMENT '改变时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='元宝改变表';

-- --------------------------------------------------------

--
-- Структура таблицы `guild`
--

CREATE TABLE `guild` (
  `guildId` bigint(20) NOT NULL DEFAULT 0 COMMENT '公会id',
  `guildName` varchar(64) NOT NULL COMMENT '帮会名',
  `chairmanId` bigint(20) NOT NULL DEFAULT 0 COMMENT '会长id',
  `createTime` int(4) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `level` int(4) NOT NULL DEFAULT 0 COMMENT '当前基地等级',
  `buildValue` bigint(20) NOT NULL DEFAULT 0 COMMENT '当前建设度',
  `datas` longtext DEFAULT NULL COMMENT '其他帮会数据',
  `guildredpacket` text DEFAULT NULL COMMENT '红包的数据日志'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帮会表';

-- --------------------------------------------------------

--
-- Структура таблицы `guildmember`
--

CREATE TABLE `guildmember` (
  `id` bigint(20) NOT NULL COMMENT '玩家ID',
  `guildId` bigint(20) NOT NULL COMMENT '公会ID',
  `contribute` bigint(20) NOT NULL COMMENT '公会贡献',
  `position` int(11) NOT NULL COMMENT '公会职位',
  `joinTime` bigint(20) NOT NULL COMMENT '时间',
  `datas` longtext DEFAULT NULL COMMENT '其他数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公会成员';

-- --------------------------------------------------------

--
-- Структура таблицы `jjc`
--

CREATE TABLE `jjc` (
  `roleId` bigint(20) NOT NULL COMMENT '玩家ID',
  `career` int(11) NOT NULL COMMENT '职业',
  `camp` int(11) NOT NULL COMMENT '出生阵营',
  `score` int(11) NOT NULL COMMENT '积分',
  `time` int(11) NOT NULL COMMENT '积分修改时间',
  `records` varchar(3000) NOT NULL COMMENT '战绩'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='竞技场表';

-- --------------------------------------------------------

--
-- Структура таблицы `mail`
--

CREATE TABLE `mail` (
  `readTable` tinyint(4) DEFAULT NULL COMMENT '用来标识是否是多语言处理包',
  `type` int(11) NOT NULL COMMENT '邮件类型，1：系统，2：后台',
  `mailId` bigint(20) NOT NULL DEFAULT 0 COMMENT '邮件唯一Id',
  `receiveTime` bigint(20) DEFAULT NULL COMMENT '邮件收到时间，单位ms',
  `sender` varchar(64) DEFAULT NULL COMMENT '邮件发件人，1：系统(集市、某副本、商城等)，2：后台(后台发件人)',
  `receiverId` bigint(20) DEFAULT NULL COMMENT '邮件收件人角色Id',
  `isRead` tinyint(4) DEFAULT NULL COMMENT '是否已读，0：未读，1：已读',
  `hasAttachment` tinyint(4) DEFAULT NULL COMMENT '是否有附件，0：无，1：有',
  `isAttachReceived` tinyint(4) DEFAULT NULL COMMENT '附件是否已领取，0：未领取，1：已领取',
  `mailData` longtext DEFAULT NULL COMMENT '整个邮件数据(JSON化存储，包含邮件全部信息[邮件标题、内容、附件等会在内])',
  `source` int(11) NOT NULL DEFAULT 0 COMMENT '附件来源'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮件表';

-- --------------------------------------------------------

--
-- Структура таблицы `marray`
--

CREATE TABLE `marray` (
  `marriageId` bigint(20) NOT NULL COMMENT '婚姻唯一id',
  `aId` bigint(20) NOT NULL COMMENT '丈夫id',
  `bId` bigint(20) NOT NULL COMMENT '妻子id',
  `time` bigint(20) NOT NULL COMMENT '结婚时间',
  `data` text NOT NULL COMMENT '其他信息'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='婚姻表';

-- --------------------------------------------------------

--
-- Структура таблицы `marriage`
--

CREATE TABLE `marriage` (
  `marriageId` bigint(20) NOT NULL COMMENT '婚姻唯一id',
  `husbandId` bigint(20) NOT NULL COMMENT '丈夫id',
  `wifeId` bigint(20) NOT NULL COMMENT '妻子id',
  `time` int(11) NOT NULL COMMENT '结婚时间',
  `top` int(11) NOT NULL COMMENT '第几对夫妇',
  `coupleInfo` text NOT NULL COMMENT '夫妻双方详细信息',
  `hasMarryType` text NOT NULL COMMENT '求婚已经选择过的婚礼类型',
  `weddingType` text NOT NULL COMMENT '待办的婚宴'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='婚姻表';

-- --------------------------------------------------------

--
-- Структура таблицы `marry_declaration`
--

CREATE TABLE `marry_declaration` (
  `roleId` bigint(20) NOT NULL COMMENT '角色ID',
  `declarationId` int(10) NOT NULL COMMENT '宣言ID',
  `timeout` bigint(20) NOT NULL COMMENT '过期时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='婚姻宣言表';

-- --------------------------------------------------------

--
-- Структура таблицы `newserveractivity`
--

CREATE TABLE `newserveractivity` (
  `roleId` bigint(20) NOT NULL DEFAULT 0 COMMENT '角色ID',
  `activityData` longtext DEFAULT NULL COMMENT '角色开服7天活动信息'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='开服7天活动信息';

-- --------------------------------------------------------

--
-- Структура таблицы `peakpk`
--

CREATE TABLE `peakpk` (
  `roleId` bigint(20) NOT NULL COMMENT '玩家ID',
  `rankId` int(10) DEFAULT 0 COMMENT '段位ID',
  `score` int(10) DEFAULT 0 COMMENT '积分',
  `time` bigint(20) NOT NULL COMMENT '更新时间',
  `times` int(10) DEFAULT 0 COMMENT '本赛季参赛场次',
  `dayTimes` int(10) DEFAULT 0 COMMENT '当天参赛场次',
  `timesReward` bigint(20) DEFAULT 0 COMMENT '场次奖励领取状态',
  `stageReward` bigint(20) DEFAULT 0 COMMENT '段位奖励领取状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='巅峰竞技';

-- --------------------------------------------------------

--
-- Структура таблицы `playerworldinfo`
--

CREATE TABLE `playerworldinfo` (
  `roleid` bigint(20) NOT NULL DEFAULT 0 COMMENT '角色ID',
  `userId` bigint(20) NOT NULL DEFAULT 0 COMMENT '账号ID',
  `rolename` varchar(64) DEFAULT NULL COMMENT '角色名',
  `career` tinyint(4) DEFAULT NULL COMMENT '职业',
  `level` int(11) DEFAULT NULL COMMENT '等级',
  `csid` int(11) NOT NULL DEFAULT 0 COMMENT '角色创建服id',
  `lastOffTime` int(11) NOT NULL DEFAULT 0 COMMENT '角色上次离线时间',
  `horseId` int(11) DEFAULT NULL COMMENT '玩家当前坐骑',
  `wingId` int(11) DEFAULT NULL,
  `fightPower` bigint(20) DEFAULT 0 COMMENT '战斗力',
  `guildId` bigint(20) DEFAULT 0 COMMENT '仙盟id',
  `fashionBodyId` int(11) DEFAULT NULL COMMENT '时装身体ID',
  `fashionWeaponId` int(11) DEFAULT NULL COMMENT '时装武器ID',
  `createTime` int(11) DEFAULT NULL COMMENT '角色的创建时间',
  `plat` varchar(50) DEFAULT NULL COMMENT '平台的名字',
  `stateVip` int(10) DEFAULT 0 COMMENT '境界等级',
  `shiHaiLevel` int(10) DEFAULT 0 COMMENT '识海等级',
  `sex` tinyint(4) DEFAULT NULL COMMENT '性别',
  `fashionHalo` int(10) DEFAULT 0,
  `fashionMatrix` int(10) DEFAULT 0,
  `playerVip` int(10) DEFAULT 0 COMMENT '玩家VIP',
  `spiritId` int(10) DEFAULT 0 COMMENT '灵体外观',
  `soulArmorId` int(10) DEFAULT 0 COMMENT '魂甲品质',
  `fashionHeadId` int(10) DEFAULT 0 COMMENT 'fashionBodyId',
  `fashionHeadFrameId` int(10) DEFAULT 0 COMMENT '所穿时装武器Id',
  `customHeadPath` varchar(255) DEFAULT NULL COMMENT '自定义头像路径',
  `useCustomHead` tinyint(4) DEFAULT 0 COMMENT '是否使用自定义头像'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色简要信息表';

--
-- Дамп данных таблицы `playerworldinfo`
--

INSERT INTO `playerworldinfo` (`roleid`, `userId`, `rolename`, `career`, `level`, `csid`, `lastOffTime`, `horseId`, `wingId`, `fightPower`, `guildId`, `fashionBodyId`, `fashionWeaponId`, `createTime`, `plat`, `stateVip`, `shiHaiLevel`, `sex`, `fashionHalo`, `fashionMatrix`, `playerVip`, `spiritId`, `soulArmorId`, `fashionHeadId`, `fashionHeadFrameId`, `customHeadPath`, `useCustomHead`) VALUES
(281873304391993039, 844541782760554497, '解半云', 0, 5, 1001, 1783030871, 0, 0, 7998, 0, 110000108, 210000100, 1783030771, 'PC', 0, 0, 1, 0, 0, 0, 0, 0, 1100000001, 1200000001, NULL, 1);

-- --------------------------------------------------------

--
-- Структура таблицы `rankplayer`
--

CREATE TABLE `rankplayer` (
  `roleId` bigint(20) NOT NULL DEFAULT 0 COMMENT '角色Id',
  `career` tinyint(4) DEFAULT NULL COMMENT '角色职业',
  `guildFlag` tinyint(1) DEFAULT NULL COMMENT '是否为公会',
  `name` varchar(64) DEFAULT NULL COMMENT '角色名字',
  `createTime` bigint(20) DEFAULT NULL COMMENT '角色创建时间',
  `createSid` int(11) DEFAULT NULL COMMENT '角色创建区服',
  `level` int(11) DEFAULT 0 COMMENT '角色等级',
  `levelUpTime` int(11) DEFAULT 0 COMMENT '上次升级时间',
  `fightPower` bigint(20) DEFAULT 0 COMMENT '战斗力',
  `horseId` int(11) DEFAULT 0 COMMENT '坐骑最高阶',
  `horseFightPoint` int(11) DEFAULT 0 COMMENT '坐骑系统战斗力',
  `wingId` int(11) DEFAULT 0 COMMENT '翅膀最高阶',
  `wingFightPoint` int(11) DEFAULT NULL COMMENT '翅膀战斗力',
  `clothesEquipId` int(11) DEFAULT 0 COMMENT '衣服装备Id',
  `weaponsEquipId` int(11) DEFAULT 0 COMMENT '武器装备Id',
  `clothesStar` int(11) DEFAULT 0 COMMENT '衣服部位的星级',
  `weaponStar` int(11) DEFAULT 0 COMMENT '武器部位的星级',
  `fashionBodyId` int(11) DEFAULT 0 COMMENT '时装身体Id',
  `fashionWeaponId` int(11) DEFAULT 0 COMMENT '时装武器Id',
  `beWorshipedNum` int(11) DEFAULT 0 COMMENT '被崇拜次数',
  `exp` bigint(20) DEFAULT 0 COMMENT '当前角色的经验值',
  `fashionLayer` int(11) DEFAULT 0 COMMENT '时装升阶等级',
  `fashionStar` int(11) DEFAULT 0 COMMENT '时装升星',
  `lastUpdateTime` varchar(500) DEFAULT NULL COMMENT '一些数据的最后更新时间',
  `equipWashPer` int(11) DEFAULT 0 COMMENT '装备洗练评分',
  `equipStrengthenLv` int(11) DEFAULT 0 COMMENT '装备强化',
  `equipFightPower` int(11) DEFAULT 0 COMMENT '装备战力',
  `gemLv` int(11) DEFAULT 0 COMMENT '宝石总等级',
  `gemFightPower` int(11) DEFAULT 0 COMMENT '宝石战力',
  `magicWeaponDamage` int(11) DEFAULT 0 COMMENT '法宝等级',
  `talismanFightPower` int(11) DEFAULT 0 COMMENT '法器战力',
  `magicFightPower` int(11) DEFAULT 0 COMMENT '阵法战力',
  `weaponFightPower` int(11) DEFAULT 0 COMMENT '神器战力',
  `strengthenFightPower` int(11) DEFAULT 0 COMMENT '强化战力',
  `charm` int(11) DEFAULT 0 COMMENT '魅力值',
  `offlineEfficiency` bigint(20) DEFAULT 0 COMMENT '离线效率',
  `sendFlower` int(11) DEFAULT 0 COMMENT '送花值',
  `shihai` int(11) DEFAULT 0 COMMENT '石海层数',
  `arenaRank` int(11) DEFAULT 0 COMMENT '竞技场排名',
  `topHallFightPower` bigint(20) DEFAULT 0 COMMENT '名人堂排名战力',
  `universeFightPower` bigint(20) DEFAULT 0 COMMENT '天墟战场名人堂战力',
  `equipStar` int(11) DEFAULT 0 COMMENT '穿戴装备大于6阶总星数',
  `equipStarGradeNum` int(11) DEFAULT 0 COMMENT '穿戴装备大于6阶总阶数',
  `equipAllStar` int(11) DEFAULT 0 COMMENT '装备灵体总星级',
  `petFightPower` int(11) DEFAULT 0 COMMENT '宠物战力',
  `spiritFightPower` int(11) DEFAULT 0 COMMENT '灵体战力',
  `immEquipFightPower` int(11) DEFAULT 0 COMMENT '仙甲战力',
  `holyEquipFightPower` int(11) DEFAULT 0 COMMENT '圣装战力',
  `monsterFightPower` int(11) DEFAULT 0 COMMENT '神兽战力',
  `petSoulLv` int(11) DEFAULT 0 COMMENT '宠物御魂等级',
  `petLv` int(11) DEFAULT 0 COMMENT '宠物等级',
  `horseSoulLv` int(11) DEFAULT 0 COMMENT '坐骑御魂等级',
  `horseLv` int(11) DEFAULT 0 COMMENT '坐骑等级',
  `consumeGold` int(11) DEFAULT 0 COMMENT '消费排行',
  `baguaPower` int(11) DEFAULT 0 COMMENT '八卦排行',
  `immortalsoulPower` int(11) DEFAULT 0 COMMENT '灵魂排行',
  `devilSoulPower` int(11) DEFAULT 0 COMMENT '魔魂排行',
  `horseEquipPower` int(11) DEFAULT 0 COMMENT '坐骑装备排行',
  `intimacy` int(11) DEFAULT 0 COMMENT '亲密度'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家排行榜表';

-- --------------------------------------------------------

--
-- Структура таблицы `recharge`
--

CREATE TABLE `recharge` (
  `order_no` varchar(64) NOT NULL DEFAULT '' COMMENT '订单号',
  `user_id` bigint(20) DEFAULT 0 COMMENT '账号Id',
  `role_id` bigint(20) DEFAULT 0 COMMENT '角色ID',
  `srv_id` int(11) DEFAULT 0 COMMENT '服务器ID',
  `goods_id` int(11) DEFAULT 0 COMMENT '商品ID',
  `goods_type` varchar(32) DEFAULT '' COMMENT '商品类型',
  `goods_ext` varchar(255) DEFAULT '' COMMENT '商品扩展数据',
  `goods_name` varchar(64) DEFAULT '' COMMENT '商品名称',
  `goods_cfg` varchar(32) DEFAULT '' COMMENT '商品映射',
  `total_fee` int(11) DEFAULT 0 COMMENT '订单金额：单位分',
  `item_id` int(11) DEFAULT 0 COMMENT '待发放道具ID',
  `game_money` int(11) DEFAULT 0 COMMENT '待发放游戏货币',
  `ext_param` varchar(255) DEFAULT '' COMMENT '透传参数',
  `sign_type` varchar(16) DEFAULT '' COMMENT '签名算法',
  `sign` varchar(128) DEFAULT '' COMMENT '签名',
  `add_time` bigint(20) DEFAULT 0 COMMENT '添加时间',
  `status` tinyint(4) DEFAULT 0 COMMENT '订单状态：0未发货，1已发货，2异常',
  `src` tinyint(4) DEFAULT 0 COMMENT '订单来源',
  `data` text DEFAULT NULL COMMENT '原始数据',
  `money_type` varchar(16) DEFAULT 'CNY' COMMENT '货币类型',
  `notify_time` varchar(32) DEFAULT '' COMMENT '异步通知发货时间',
  `notify_id` varchar(255) DEFAULT '' COMMENT '异步通知ID',
  `trade_no` varchar(255) DEFAULT '' COMMENT '第三方支付订单',
  `trade_status` int(11) DEFAULT 0 COMMENT '支付成功,目前就只有此类型',
  `totalRecharge` int(11) DEFAULT 0 COMMENT '计算到游戏累充值',
  `totalVipPower` int(11) DEFAULT 0 COMMENT 'vip经验加成'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家充值订单表';

-- --------------------------------------------------------

--
-- Структура таблицы `redpacket`
--

CREATE TABLE `redpacket` (
  `rpId` bigint(20) NOT NULL COMMENT '红包实例ID',
  `redpacket` text DEFAULT NULL COMMENT '红包内容',
  `rpCreateTime` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `rpType` int(11) DEFAULT 1 COMMENT '红包类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='红包表';

-- --------------------------------------------------------

--
-- Структура таблицы `role`
--

CREATE TABLE `role` (
  `roleid` bigint(20) NOT NULL DEFAULT 0 COMMENT '角色ID',
  `rolename` varchar(64) DEFAULT NULL COMMENT '角色名',
  `userId` bigint(20) DEFAULT NULL COMMENT '账号ID',
  `platformName` varchar(100) DEFAULT NULL COMMENT '平台名',
  `career` tinyint(4) DEFAULT NULL COMMENT '职业',
  `degree` tinyint(4) DEFAULT NULL COMMENT '转职阶位',
  `lv` int(11) DEFAULT NULL COMMENT '转职等级',
  `weapon` int(9) DEFAULT NULL COMMENT '武器',
  `wingId` int(4) DEFAULT NULL COMMENT '翅膀ID',
  `roledata` longtext DEFAULT NULL COMMENT '角色数据',
  `lastLoginTime` bigint(20) DEFAULT NULL COMMENT '上一次登录时间',
  `createTime` int(4) DEFAULT NULL COMMENT '创建时间',
  `deleteTime` int(4) DEFAULT NULL COMMENT '角色删除时间，0表示未删除',
  `serverId` int(11) DEFAULT NULL COMMENT '服务器id',
  `equipMinStar` int(11) DEFAULT NULL COMMENT '最低星级',
  `languageType` int(11) DEFAULT 0 COMMENT '语言类型，0：中文简体，1：中文繁体，2：泰文，3：越南文，4：韩文 等等',
  `fashionBodyId` int(11) DEFAULT NULL COMMENT '时装身体ID',
  `fashionWeaponId` int(11) DEFAULT NULL COMMENT '时装武器ID',
  `useIconState` int(11) DEFAULT 0 COMMENT '头像的状态值'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

--
-- Дамп данных таблицы `role`
--

INSERT INTO `role` (`roleid`, `rolename`, `userId`, `platformName`, `career`, `degree`, `lv`, `weapon`, `wingId`, `roledata`, `lastLoginTime`, `createTime`, `deleteTime`, `serverId`, `equipMinStar`, `languageType`, `fashionBodyId`, `fashionWeaponId`, `useIconState`) VALUES
(281873304391993039, '解半云', 844541782760554497, 'PC', 0, 0, 5, 0, 0, '#20150414#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', 1783030772162, 1783030771, 0, 1001, 0, 0, 110000108, 110000108, 0);

-- --------------------------------------------------------

--
-- Структура таблицы `roleactivitydata`
--

CREATE TABLE `roleactivitydata` (
  `roleId` bigint(20) NOT NULL COMMENT '角色ID',
  `actData` longtext NOT NULL COMMENT '角色活动相关数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运营活动角色数据表';

-- --------------------------------------------------------

--
-- Структура таблицы `roleloginfo`
--

CREATE TABLE `roleloginfo` (
  `userId` bigint(20) NOT NULL COMMENT '账号ID',
  `platUserId` varchar(50) DEFAULT NULL COMMENT '平台的账号名',
  `platformName` varchar(50) DEFAULT NULL COMMENT '平台名称',
  `os` varchar(15) DEFAULT NULL COMMENT '系统',
  `maCode` varchar(50) DEFAULT NULL COMMENT '设备码',
  `uuid` varchar(50) DEFAULT NULL COMMENT 'funcell生成的uuid',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `lastLoginTime` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色账号登录信息表';

--
-- Дамп данных таблицы `roleloginfo`
--

INSERT INTO `roleloginfo` (`userId`, `platUserId`, `platformName`, `os`, `maCode`, `uuid`, `createTime`, `lastLoginTime`) VALUES
(844541782760554497, '', 'PC', 'android', '63c0f016bbf4405e', 'test_nil', 1783030772179, 1783030772162);

-- --------------------------------------------------------

--
-- Структура таблицы `serverparam`
--

CREATE TABLE `serverparam` (
  `paramkey` varchar(64) NOT NULL,
  `serverid` int(11) DEFAULT NULL,
  `paramvalue` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务器信息表';

--
-- Дамп данных таблицы `serverparam`
--

INSERT INTO `serverparam` (`paramkey`, `serverid`, `paramvalue`) VALUES
('CangbaogeRoundData', 1001, '{\"1\":1784926800000}'),
('common', 1001, '{\"fu_di_forever_title\":\"1\"}'),
('FallingSky', 1001, '{\"1\":1787407367296}'),
('LoginCheck', 1001, '1'),
('RchargeCheck ', 1001, '1'),
('ServerControl', 1001, '#20150414#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'),
('ServerCount', 1001, '#20150414#H8KLCAAAAAAAAADCncOTw41Kw4NAEAfDsHfCmcOzHsOmYz9zVXzCgsKCw4fCpcKUBcKlaiTCpkIofcO3blMMw6kSw43DmltIw77DucONMMOMHAEjeWjCjsOQD8KfCRp4aA8fw70mP8OHw4fDrQAKw7Zpw4hvw4fCkMKCw53DpSM0wqTDoG3Du8OVb17Dn8OzH8OkPAfDoyzCkgRWw7DDkh46aMOMSQHDmcKIwovDsFPDm8Klw6/DlE3DuBjCnHBtOHjDrcKFwosiwoLCgsOOwqMOw67CpwjDpiLCuTFbw5PCvcO9wr17QcK2eMOpPhQwUVXDu8OXw6Qfw4MRNMKBwpllw45Lw6Q6fQxOOC7Do8OkXcKBcy3DjnfDoFLCi8OLHcK4wq7DhcO1Oh7CsMOATS1uw77Ci2PClMOlwrE8wqfCtMKfw63CisOIw6rCqsKQw7XCt3LCqMOYw7DCsMK6w6DCmm8awrYSw6s2cAxOwrjCiMKWw6XDizTDhMOFAWHDncK4wq/DicOVA8Kiw5lUTmd+wqAbLMK4BAAA'),
('ServerPeakSeason', 1001, '1'),
('ServerWorldBoss', 1001, '{\"11301\":{\"bossId\":11301,\"bornTime\":1782947169917,\"dieNum\":0,\"reBornBaseTime\":1500,\"rebornTime\":0,\"mapUid\":0},\"11302\":{\"bossId\":11302,\"bornTime\":1782947169950,\"dieNum\":0,\"reBornBaseTime\":900,\"rebornTime\":0,\"mapUid\":0},\"11303\":{\"bossId\":11303,\"bornTime\":1782947169950,\"dieNum\":0,\"reBornBaseTime\":960,\"rebornTime\":0,\"mapUid\":0},\"11304\":{\"bossId\":11304,\"bornTime\":1782947169951,\"dieNum\":0,\"reBornBaseTime\":1020,\"rebornTime\":0,\"mapUid\":0},\"11305\":{\"bossId\":11305,\"bornTime\":1782947169951,\"dieNum\":0,\"reBornBaseTime\":1080,\"rebornTime\":0,\"mapUid\":0},\"11306\":{\"bossId\":11306,\"bornTime\":1782947169951,\"dieNum\":0,\"reBornBaseTime\":1140,\"rebornTime\":0,\"mapUid\":0},\"11307\":{\"bossId\":11307,\"bornTime\":1782947169952,\"dieNum\":0,\"reBornBaseTime\":1200,\"rebornTime\":0,\"mapUid\":0},\"11308\":{\"bossId\":11308,\"bornTime\":1782947169952,\"dieNum\":0,\"reBornBaseTime\":1320,\"rebornTime\":0,\"mapUid\":0},\"13401\":{\"bossId\":13401,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":720,\"rebornTime\":0,\"mapUid\":0},\"13402\":{\"bossId\":13402,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":720,\"rebornTime\":0,\"mapUid\":0},\"13403\":{\"bossId\":13403,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":720,\"rebornTime\":0,\"mapUid\":0},\"13404\":{\"bossId\":13404,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":720,\"rebornTime\":0,\"mapUid\":0},\"13405\":{\"bossId\":13405,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":810,\"rebornTime\":0,\"mapUid\":0},\"13406\":{\"bossId\":13406,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":810,\"rebornTime\":0,\"mapUid\":0},\"13407\":{\"bossId\":13407,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":810,\"rebornTime\":0,\"mapUid\":0},\"13408\":{\"bossId\":13408,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":810,\"rebornTime\":0,\"mapUid\":0},\"11401\":{\"bossId\":11401,\"bornTime\":1782947169949,\"dieNum\":0,\"reBornBaseTime\":1800,\"rebornTime\":0,\"mapUid\":0},\"11402\":{\"bossId\":11402,\"bornTime\":1782947169967,\"dieNum\":0,\"reBornBaseTime\":1200,\"rebornTime\":0,\"mapUid\":0},\"11403\":{\"bossId\":11403,\"bornTime\":1782947169968,\"dieNum\":0,\"reBornBaseTime\":1260,\"rebornTime\":0,\"mapUid\":0},\"11404\":{\"bossId\":11404,\"bornTime\":1782947169972,\"dieNum\":0,\"reBornBaseTime\":1320,\"rebornTime\":0,\"mapUid\":0},\"11405\":{\"bossId\":11405,\"bornTime\":1782947169977,\"dieNum\":0,\"reBornBaseTime\":1380,\"rebornTime\":0,\"mapUid\":0},\"11406\":{\"bossId\":11406,\"bornTime\":1782947169978,\"dieNum\":0,\"reBornBaseTime\":1440,\"rebornTime\":0,\"mapUid\":0},\"11407\":{\"bossId\":11407,\"bornTime\":1782947169983,\"dieNum\":0,\"reBornBaseTime\":1500,\"rebornTime\":0,\"mapUid\":0},\"11408\":{\"bossId\":11408,\"bornTime\":1782947169986,\"dieNum\":0,\"reBornBaseTime\":1620,\"rebornTime\":0,\"mapUid\":0},\"13501\":{\"bossId\":13501,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":900,\"rebornTime\":0,\"mapUid\":0},\"13502\":{\"bossId\":13502,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":900,\"rebornTime\":0,\"mapUid\":0},\"13503\":{\"bossId\":13503,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":900,\"rebornTime\":0,\"mapUid\":0},\"13504\":{\"bossId\":13504,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":900,\"rebornTime\":0,\"mapUid\":0},\"13505\":{\"bossId\":13505,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":990,\"rebornTime\":0,\"mapUid\":0},\"13506\":{\"bossId\":13506,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":990,\"rebornTime\":0,\"mapUid\":0},\"13507\":{\"bossId\":13507,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":990,\"rebornTime\":0,\"mapUid\":0},\"13508\":{\"bossId\":13508,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":990,\"rebornTime\":0,\"mapUid\":0},\"11501\":{\"bossId\":11501,\"bornTime\":1782947169987,\"dieNum\":0,\"reBornBaseTime\":2100,\"rebornTime\":0,\"mapUid\":0},\"11502\":{\"bossId\":11502,\"bornTime\":1782947169996,\"dieNum\":0,\"reBornBaseTime\":1500,\"rebornTime\":0,\"mapUid\":0},\"11503\":{\"bossId\":11503,\"bornTime\":1782947169998,\"dieNum\":0,\"reBornBaseTime\":1560,\"rebornTime\":0,\"mapUid\":0},\"11504\":{\"bossId\":11504,\"bornTime\":1782947169998,\"dieNum\":0,\"reBornBaseTime\":1620,\"rebornTime\":0,\"mapUid\":0},\"11505\":{\"bossId\":11505,\"bornTime\":1782947169999,\"dieNum\":0,\"reBornBaseTime\":1680,\"rebornTime\":0,\"mapUid\":0},\"11506\":{\"bossId\":11506,\"bornTime\":1782947169999,\"dieNum\":0,\"reBornBaseTime\":1740,\"rebornTime\":0,\"mapUid\":0},\"11507\":{\"bossId\":11507,\"bornTime\":1782947169999,\"dieNum\":0,\"reBornBaseTime\":1800,\"rebornTime\":0,\"mapUid\":0},\"11508\":{\"bossId\":11508,\"bornTime\":1782947170000,\"dieNum\":0,\"reBornBaseTime\":1920,\"rebornTime\":0,\"mapUid\":0},\"11008\":{\"bossId\":11008,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":150,\"rebornTime\":0,\"mapUid\":0},\"11009\":{\"bossId\":11009,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":180,\"rebornTime\":0,\"mapUid\":0},\"11010\":{\"bossId\":11010,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":210,\"rebornTime\":0,\"mapUid\":0},\"11011\":{\"bossId\":11011,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":240,\"rebornTime\":0,\"mapUid\":0},\"11016\":{\"bossId\":11016,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":270,\"rebornTime\":0,\"mapUid\":0},\"11017\":{\"bossId\":11017,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":300,\"rebornTime\":0,\"mapUid\":0},\"13601\":{\"bossId\":13601,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1080,\"rebornTime\":0,\"mapUid\":0},\"13602\":{\"bossId\":13602,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1080,\"rebornTime\":0,\"mapUid\":0},\"13603\":{\"bossId\":13603,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1080,\"rebornTime\":0,\"mapUid\":0},\"13604\":{\"bossId\":13604,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1080,\"rebornTime\":0,\"mapUid\":0},\"13605\":{\"bossId\":13605,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1170,\"rebornTime\":0,\"mapUid\":0},\"13606\":{\"bossId\":13606,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1170,\"rebornTime\":0,\"mapUid\":0},\"13607\":{\"bossId\":13607,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1170,\"rebornTime\":0,\"mapUid\":0},\"13608\":{\"bossId\":13608,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1170,\"rebornTime\":0,\"mapUid\":0},\"13609\":{\"bossId\":13609,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1170,\"rebornTime\":0,\"mapUid\":0},\"11601\":{\"bossId\":11601,\"bornTime\":1782947169992,\"dieNum\":0,\"reBornBaseTime\":2400,\"rebornTime\":0,\"mapUid\":0},\"11602\":{\"bossId\":11602,\"bornTime\":1782947170012,\"dieNum\":0,\"reBornBaseTime\":1500,\"rebornTime\":0,\"mapUid\":0},\"11603\":{\"bossId\":11603,\"bornTime\":1782947170012,\"dieNum\":0,\"reBornBaseTime\":1560,\"rebornTime\":0,\"mapUid\":0},\"11604\":{\"bossId\":11604,\"bornTime\":1782947170013,\"dieNum\":0,\"reBornBaseTime\":1620,\"rebornTime\":0,\"mapUid\":0},\"11605\":{\"bossId\":11605,\"bornTime\":1782947170013,\"dieNum\":0,\"reBornBaseTime\":1680,\"rebornTime\":0,\"mapUid\":0},\"11606\":{\"bossId\":11606,\"bornTime\":1782947170014,\"dieNum\":0,\"reBornBaseTime\":1740,\"rebornTime\":0,\"mapUid\":0},\"11607\":{\"bossId\":11607,\"bornTime\":1782947170015,\"dieNum\":0,\"reBornBaseTime\":1800,\"rebornTime\":0,\"mapUid\":0},\"11608\":{\"bossId\":11608,\"bornTime\":1782947170015,\"dieNum\":0,\"reBornBaseTime\":1920,\"rebornTime\":0,\"mapUid\":0},\"11609\":{\"bossId\":11609,\"bornTime\":1782947170016,\"dieNum\":0,\"reBornBaseTime\":2400,\"rebornTime\":0,\"mapUid\":0},\"11102\":{\"bossId\":11102,\"bornTime\":1782947169857,\"dieNum\":0,\"reBornBaseTime\":150,\"rebornTime\":0,\"mapUid\":0},\"11103\":{\"bossId\":11103,\"bornTime\":1782947169862,\"dieNum\":0,\"reBornBaseTime\":180,\"rebornTime\":0,\"mapUid\":0},\"11104\":{\"bossId\":11104,\"bornTime\":1782947169864,\"dieNum\":0,\"reBornBaseTime\":210,\"rebornTime\":0,\"mapUid\":0},\"11105\":{\"bossId\":11105,\"bornTime\":1782947169865,\"dieNum\":0,\"reBornBaseTime\":240,\"rebornTime\":0,\"mapUid\":0},\"11106\":{\"bossId\":11106,\"bornTime\":1782947169866,\"dieNum\":0,\"reBornBaseTime\":270,\"rebornTime\":0,\"mapUid\":0},\"11107\":{\"bossId\":11107,\"bornTime\":1782947169867,\"dieNum\":0,\"reBornBaseTime\":300,\"rebornTime\":0,\"mapUid\":0},\"11108\":{\"bossId\":11108,\"bornTime\":1782947169868,\"dieNum\":0,\"reBornBaseTime\":360,\"rebornTime\":0,\"mapUid\":0},\"13202\":{\"bossId\":13202,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":360,\"rebornTime\":0,\"mapUid\":0},\"13203\":{\"bossId\":13203,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":360,\"rebornTime\":0,\"mapUid\":0},\"13204\":{\"bossId\":13204,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":360,\"rebornTime\":0,\"mapUid\":0},\"13205\":{\"bossId\":13205,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":450,\"rebornTime\":0,\"mapUid\":0},\"13206\":{\"bossId\":13206,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":450,\"rebornTime\":0,\"mapUid\":0},\"13207\":{\"bossId\":13207,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":450,\"rebornTime\":0,\"mapUid\":0},\"13208\":{\"bossId\":13208,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":450,\"rebornTime\":0,\"mapUid\":0},\"11709\":{\"bossId\":11709,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":360,\"rebornTime\":0,\"mapUid\":0},\"11710\":{\"bossId\":11710,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":480,\"rebornTime\":0,\"mapUid\":0},\"11711\":{\"bossId\":11711,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":600,\"rebornTime\":0,\"mapUid\":0},\"11712\":{\"bossId\":11712,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":660,\"rebornTime\":0,\"mapUid\":0},\"11201\":{\"bossId\":11201,\"bornTime\":1782947169854,\"dieNum\":0,\"reBornBaseTime\":600,\"rebornTime\":0,\"mapUid\":0},\"11713\":{\"bossId\":11713,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":900,\"rebornTime\":0,\"mapUid\":0},\"11202\":{\"bossId\":11202,\"bornTime\":1782947169919,\"dieNum\":0,\"reBornBaseTime\":300,\"rebornTime\":0,\"mapUid\":0},\"11714\":{\"bossId\":11714,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1200,\"rebornTime\":0,\"mapUid\":0},\"11203\":{\"bossId\":11203,\"bornTime\":1782947169923,\"dieNum\":0,\"reBornBaseTime\":330,\"rebornTime\":0,\"mapUid\":0},\"11715\":{\"bossId\":11715,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1320,\"rebornTime\":0,\"mapUid\":0},\"11204\":{\"bossId\":11204,\"bornTime\":1782947169923,\"dieNum\":0,\"reBornBaseTime\":360,\"rebornTime\":0,\"mapUid\":0},\"11716\":{\"bossId\":11716,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1500,\"rebornTime\":0,\"mapUid\":0},\"11205\":{\"bossId\":11205,\"bornTime\":1782947169924,\"dieNum\":0,\"reBornBaseTime\":600,\"rebornTime\":0,\"mapUid\":0},\"11717\":{\"bossId\":11717,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":600,\"rebornTime\":0,\"mapUid\":0},\"11206\":{\"bossId\":11206,\"bornTime\":1782947169924,\"dieNum\":0,\"reBornBaseTime\":780,\"rebornTime\":0,\"mapUid\":0},\"11718\":{\"bossId\":11718,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":660,\"rebornTime\":0,\"mapUid\":0},\"11207\":{\"bossId\":11207,\"bornTime\":1782947169924,\"dieNum\":0,\"reBornBaseTime\":900,\"rebornTime\":0,\"mapUid\":0},\"11719\":{\"bossId\":11719,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":900,\"rebornTime\":0,\"mapUid\":0},\"11208\":{\"bossId\":11208,\"bornTime\":1782947169925,\"dieNum\":0,\"reBornBaseTime\":1020,\"rebornTime\":0,\"mapUid\":0},\"11720\":{\"bossId\":11720,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1200,\"rebornTime\":0,\"mapUid\":0},\"11721\":{\"bossId\":11721,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1320,\"rebornTime\":0,\"mapUid\":0},\"11722\":{\"bossId\":11722,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1500,\"rebornTime\":0,\"mapUid\":0},\"11723\":{\"bossId\":11723,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1620,\"rebornTime\":0,\"mapUid\":0},\"11724\":{\"bossId\":11724,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1800,\"rebornTime\":0,\"mapUid\":0},\"11725\":{\"bossId\":11725,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":660,\"rebornTime\":0,\"mapUid\":0},\"11726\":{\"bossId\":11726,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":900,\"rebornTime\":0,\"mapUid\":0},\"11727\":{\"bossId\":11727,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1200,\"rebornTime\":0,\"mapUid\":0},\"11728\":{\"bossId\":11728,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1320,\"rebornTime\":0,\"mapUid\":0},\"11729\":{\"bossId\":11729,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1500,\"rebornTime\":0,\"mapUid\":0},\"11730\":{\"bossId\":11730,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1620,\"rebornTime\":0,\"mapUid\":0},\"11731\":{\"bossId\":11731,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1800,\"rebornTime\":0,\"mapUid\":0},\"11732\":{\"bossId\":11732,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1920,\"rebornTime\":0,\"mapUid\":0},\"11733\":{\"bossId\":11733,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1500,\"rebornTime\":0,\"mapUid\":0},\"11734\":{\"bossId\":11734,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1620,\"rebornTime\":0,\"mapUid\":0},\"11735\":{\"bossId\":11735,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1800,\"rebornTime\":0,\"mapUid\":0},\"11736\":{\"bossId\":11736,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1920,\"rebornTime\":0,\"mapUid\":0},\"11737\":{\"bossId\":11737,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2040,\"rebornTime\":0,\"mapUid\":0},\"11738\":{\"bossId\":11738,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2100,\"rebornTime\":0,\"mapUid\":0},\"11739\":{\"bossId\":11739,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2160,\"rebornTime\":0,\"mapUid\":0},\"11740\":{\"bossId\":11740,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2220,\"rebornTime\":0,\"mapUid\":0},\"11741\":{\"bossId\":11741,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1620,\"rebornTime\":0,\"mapUid\":0},\"11742\":{\"bossId\":11742,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1800,\"rebornTime\":0,\"mapUid\":0},\"11743\":{\"bossId\":11743,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1920,\"rebornTime\":0,\"mapUid\":0},\"11744\":{\"bossId\":11744,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2040,\"rebornTime\":0,\"mapUid\":0},\"11745\":{\"bossId\":11745,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2160,\"rebornTime\":0,\"mapUid\":0},\"11746\":{\"bossId\":11746,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2220,\"rebornTime\":0,\"mapUid\":0},\"11747\":{\"bossId\":11747,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2280,\"rebornTime\":0,\"mapUid\":0},\"11748\":{\"bossId\":11748,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2340,\"rebornTime\":0,\"mapUid\":0},\"11749\":{\"bossId\":11749,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":1920,\"rebornTime\":0,\"mapUid\":0},\"11750\":{\"bossId\":11750,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2040,\"rebornTime\":0,\"mapUid\":0},\"11751\":{\"bossId\":11751,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2160,\"rebornTime\":0,\"mapUid\":0},\"11752\":{\"bossId\":11752,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2220,\"rebornTime\":0,\"mapUid\":0},\"11753\":{\"bossId\":11753,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2280,\"rebornTime\":0,\"mapUid\":0},\"11754\":{\"bossId\":11754,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2340,\"rebornTime\":0,\"mapUid\":0},\"11755\":{\"bossId\":11755,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2400,\"rebornTime\":0,\"mapUid\":0},\"11756\":{\"bossId\":11756,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2460,\"rebornTime\":0,\"mapUid\":0},\"11757\":{\"bossId\":11757,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2160,\"rebornTime\":0,\"mapUid\":0},\"11758\":{\"bossId\":11758,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2220,\"rebornTime\":0,\"mapUid\":0},\"11759\":{\"bossId\":11759,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2280,\"rebornTime\":0,\"mapUid\":0},\"11760\":{\"bossId\":11760,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2340,\"rebornTime\":0,\"mapUid\":0},\"11761\":{\"bossId\":11761,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2400,\"rebornTime\":0,\"mapUid\":0},\"11762\":{\"bossId\":11762,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2460,\"rebornTime\":0,\"mapUid\":0},\"11763\":{\"bossId\":11763,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2520,\"rebornTime\":0,\"mapUid\":0},\"11764\":{\"bossId\":11764,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2580,\"rebornTime\":0,\"mapUid\":0},\"11765\":{\"bossId\":11765,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2400,\"rebornTime\":0,\"mapUid\":0},\"13301\":{\"bossId\":13301,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":540,\"rebornTime\":0,\"mapUid\":0},\"11766\":{\"bossId\":11766,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2460,\"rebornTime\":0,\"mapUid\":0},\"13302\":{\"bossId\":13302,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":540,\"rebornTime\":0,\"mapUid\":0},\"11767\":{\"bossId\":11767,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2520,\"rebornTime\":0,\"mapUid\":0},\"13303\":{\"bossId\":13303,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":540,\"rebornTime\":0,\"mapUid\":0},\"11768\":{\"bossId\":11768,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2580,\"rebornTime\":0,\"mapUid\":0},\"13304\":{\"bossId\":13304,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":540,\"rebornTime\":0,\"mapUid\":0},\"11769\":{\"bossId\":11769,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2640,\"rebornTime\":0,\"mapUid\":0},\"13305\":{\"bossId\":13305,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":630,\"rebornTime\":0,\"mapUid\":0},\"11770\":{\"bossId\":11770,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2700,\"rebornTime\":0,\"mapUid\":0},\"13306\":{\"bossId\":13306,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":630,\"rebornTime\":0,\"mapUid\":0},\"11771\":{\"bossId\":11771,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2760,\"rebornTime\":0,\"mapUid\":0},\"13307\":{\"bossId\":13307,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":630,\"rebornTime\":0,\"mapUid\":0},\"11772\":{\"bossId\":11772,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":2820,\"rebornTime\":0,\"mapUid\":0},\"13308\":{\"bossId\":13308,\"bornTime\":0,\"dieNum\":0,\"reBornBaseTime\":630,\"rebornTime\":0,\"mapUid\":0}}'),
('WeddingData', 1001, '{}'),
('xianjiaHuntData', 1001, '{\"2\":1783630800000}');

-- --------------------------------------------------------

--
-- Структура таблицы `shop`
--

CREATE TABLE `shop` (
  `ID` int(11) UNSIGNED NOT NULL COMMENT '商品唯一ID',
  `itemID` int(11) UNSIGNED DEFAULT 0 COMMENT '道具ID',
  `shopID` int(11) UNSIGNED DEFAULT 0 COMMENT '所属商城ID,1.元宝商城,2.兑换商城',
  `labelID` int(11) UNSIGNED DEFAULT 0 COMMENT '商品标签页ID',
  `level` int(11) UNSIGNED DEFAULT 0 COMMENT '购买需求等级',
  `militaryLevel` int(11) UNSIGNED DEFAULT 0 COMMENT '购买需求军衔',
  `guildLevel` int(11) UNSIGNED DEFAULT 0 COMMENT '购买需求帮会等级',
  `guildShopLvlStart` int(11) UNSIGNED DEFAULT 0 COMMENT '购买需要的仙盟商店的最低等级',
  `guildShopLvlEnd` int(11) UNSIGNED DEFAULT 0 COMMENT '购买需要的仙盟商店的最高等级',
  `worldLvlStart` int(11) UNSIGNED DEFAULT 0 COMMENT '购买需求最低世界等级',
  `worldLvlEnd` int(11) UNSIGNED DEFAULT 0 COMMENT '购买需求结束世界等级',
  `isDiscount` int(11) UNSIGNED DEFAULT 0 COMMENT '开通修神锻体后是否打折，具体折扣读取修神锻体特权表,0代表不打折/1代表打折',
  `vipLevel` int(11) UNSIGNED DEFAULT 0 COMMENT '购买需求境界等级',
  `occupation` int(11) DEFAULT -1 COMMENT '角色职业限制,-1.通用无限制',
  `limitType` int(11) UNSIGNED DEFAULT 0 COMMENT '限购类型,0.不限购；1.日限够；2.周限购；3.月限购；4.年限购；5.终身限购',
  `buyNum` int(11) DEFAULT -1 COMMENT '可购买次数,-1为无限',
  `currencyID` int(11) UNSIGNED DEFAULT 0 COMMENT '购买货币ID',
  `price` int(11) UNSIGNED DEFAULT 0 COMMENT '打折前价格',
  `discountPrice` int(11) UNSIGNED DEFAULT 0 COMMENT '打折后价格',
  `discount` int(11) UNSIGNED DEFAULT 0 COMMENT '打折数,0为不打折,>0为具体打折数',
  `promotion` int(11) UNSIGNED DEFAULT 0 COMMENT '促销标签',
  `sort` int(11) UNSIGNED DEFAULT 0 COMMENT '排列优先级',
  `upTime` varchar(50) DEFAULT '1970-1-1 00:00:00' COMMENT '上架时间,年_月_日_时_分_秒，0则为即刻上架',
  `downTime` varchar(50) DEFAULT '1970-1-1 00:00:00' COMMENT '下架时间,年_月_日_时_分_秒，0为永不下架',
  `overdue` varchar(50) DEFAULT '1970-1-1 00:00:00' COMMENT '商品的道具过期时间,年_月_日_时_分_秒,0则取duration',
  `duration` int(11) UNSIGNED DEFAULT 0 COMMENT '持续时间',
  `bind` int(11) UNSIGNED DEFAULT 1 COMMENT '是否绑定,0非绑定，1绑定',
  `refreshCurrency` int(11) DEFAULT -1 COMMENT '刷新使用货币类型，-1为不能刷新',
  `refreshNum` int(11) UNSIGNED DEFAULT 0 COMMENT '刷新货币消耗数量',
  `shopType` varchar(255) DEFAULT '' COMMENT '商城标签',
  `countdiscount` varchar(255) DEFAULT '' COMMENT '根据购买次数打折',
  `openday` int(11) UNSIGNED DEFAULT 0 COMMENT '上架时间',
  `closeday` int(11) UNSIGNED DEFAULT 0 COMMENT '下架时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商城表';

--
-- Дамп данных таблицы `shop`
--

INSERT INTO `shop` (`ID`, `itemID`, `shopID`, `labelID`, `level`, `militaryLevel`, `guildLevel`, `guildShopLvlStart`, `guildShopLvlEnd`, `worldLvlStart`, `worldLvlEnd`, `isDiscount`, `vipLevel`, `occupation`, `limitType`, `buyNum`, `currencyID`, `price`, `discountPrice`, `discount`, `promotion`, `sort`, `upTime`, `downTime`, `overdue`, `duration`, `bind`, `refreshCurrency`, `refreshNum`, `shopType`, `countdiscount`, `openday`, `closeday`) VALUES
(101001, 1004, 1, 1241200, 60, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 0, -1, 1, 25, 25, 0, 0, 5, '', '', '', 0, 1, 0, 0, '2_5', '', 0, 0),
(101002, 60008, 1, 1241200, 0, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 0, -1, 1, 10, 10, 0, 0, 6, '', '', '', 0, 1, 0, 0, '5', '', 0, 0),
(101003, 16002, 1, 1241200, 0, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 1, 30, 1, 40, 20, 5, 1, 7, '', '', '', 0, 1, 0, 0, '5', '', 0, 0),
(101004, 1018, 1, 1241200, 250, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 0, -1, 1, 80, 80, 0, 0, 8, '', '', '', 0, 1, 0, 0, '5', '', 4, 0),
(101005, 15103, 1, 1241200, 0, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 998, 998, 0, 0, 9, '', '', '', 0, 1, 0, 0, '2_4', '', 0, 0),
(101006, 15104, 1, 1241200, 0, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 2998, 2998, 0, 0, 10, '', '', '', 0, 1, 0, 0, '2_4', '', 0, 0),
(101007, 15105, 1, 1241200, 0, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 9998, 9998, 0, 0, 11, '', '', '', 0, 1, 0, 0, '2_4', '', 0, 0),
(101008, 15106, 1, 1241200, 0, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 998, 998, 0, 0, 12, '', '', '', 0, 1, 0, 0, '4', '', 0, 0),
(101009, 15107, 1, 1241200, 0, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 2998, 2998, 0, 0, 13, '', '', '', 0, 1, 0, 0, '4', '', 0, 0),
(101010, 15108, 1, 1241200, 0, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 9998, 9998, 0, 0, 14, '', '', '', 0, 1, 0, 0, '4', '', 0, 0),
(101011, 15109, 1, 1241200, 0, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 998, 998, 0, 0, 15, '', '', '', 0, 1, 0, 0, '4', '', 0, 0),
(101012, 15110, 1, 1241200, 0, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 2998, 2998, 0, 0, 16, '', '', '', 0, 1, 0, 0, '4', '', 0, 0),
(101013, 15111, 1, 1241200, 0, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 9998, 9998, 0, 0, 17, '', '', '', 0, 1, 0, 0, '4', '', 0, 0),
(101014, 2003470, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 100, 100, 0, 0, 500, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101015, 2003496, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 1800, 980, 6, 1, 502, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101016, 2003522, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 300, 300, 0, 0, 504, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101017, 2003548, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 500, 500, 0, 0, 506, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101018, 2003574, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 700, 700, 0, 0, 508, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101019, 2003600, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 900, 900, 0, 0, 510, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101020, 2003626, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 1500, 1500, 0, 0, 512, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101021, 2003652, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 2200, 2200, 0, 0, 514, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101022, 2003678, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 3600, 3600, 0, 0, 516, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101023, 2003704, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 6000, 6000, 0, 0, 518, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101024, 2003730, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 9000, 9000, 0, 0, 520, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101025, 2003469, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 100, 100, 0, 0, 501, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101026, 2003495, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 1800, 980, 6, 1, 503, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101027, 2003521, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 300, 300, 0, 0, 505, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101028, 2003547, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 500, 500, 0, 0, 507, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101029, 2003573, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 700, 700, 0, 0, 509, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101030, 2003599, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 900, 900, 0, 0, 511, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101031, 2003625, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 1500, 1500, 0, 0, 513, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101032, 2003651, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 2200, 2200, 0, 0, 515, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101033, 2003677, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 3600, 3600, 0, 0, 517, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101034, 2003703, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 6000, 6000, 0, 0, 519, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101035, 2003729, 1, 1241200, 99999, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 1, 1, 9000, 9000, 0, 0, 521, '', '', '', 0, 1, 0, 0, '2_8', '', 0, 0),
(101036, 60054, 1, 1241200, 340, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 30, 1, 15, 15, 0, 0, 64, '', '', '', 0, 1, 0, 0, '2_4', '', 0, 0),
(101037, 60056, 1, 1241200, 450, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 60, 1, 25, 25, 0, 0, 65, '', '', '', 0, 1, 0, 0, '2_4', '', 0, 0),
(101038, 60057, 1, 1241200, 610, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 80, 1, 50, 50, 0, 0, 66, '', '', '', 0, 1, 0, 0, '2_4', '', 0, 0),
(101039, 60058, 1, 1241200, 730, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 5, 100, 1, 60, 60, 0, 0, 67, '', '', '', 0, 1, 0, 0, '2_4', '', 0, 0),
(101040, 50002, 1, 1241200, 178, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 0, -1, 1, 99, 99, 0, 0, 68, '', '', '', 0, 1, 0, 0, '5', '', 0, 0),
(101041, 50003, 1, 1241200, 178, 0, 0, 0, 9999, 0, 9999, 0, 0, -1, 0, -1, 1, 999, 999, 0, 0, 69, '', '', '', 0, 1, 0, 0, '5', '', 0, 0);

-- --------------------------------------------------------

--
-- Структура таблицы `useractivity`
--

CREATE TABLE `useractivity` (
  `userId` bigint(20) NOT NULL COMMENT '玩家userId值',
  `serverId` int(4) DEFAULT NULL COMMENT '服务器id',
  `vipRewardGet` varchar(200) NOT NULL COMMENT 'vip等级奖励领取结果',
  `rewardContext` longtext NOT NULL COMMENT '活动奖励记录值',
  `rewardVesionIdList` varchar(300) NOT NULL COMMENT '已领取的资源更新奖励对应资源版本号列表',
  `isEvaluated` int(11) DEFAULT 0 COMMENT '是否已经对软件进行评价，0 还未评价，1 已评价',
  `isPop` varchar(50) DEFAULT NULL COMMENT '限时直购今日是否弹窗标识'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账号活动数据表';

-- --------------------------------------------------------

--
-- Структура таблицы `wedding`
--

CREATE TABLE `wedding` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `marriageId` bigint(20) NOT NULL COMMENT '结婚证唯一id',
  `level` int(11) NOT NULL COMMENT '婚宴等级',
  `holdTime` int(11) NOT NULL COMMENT '预约时间（秒）',
  `prayTime` int(11) NOT NULL COMMENT '预约的时间段的开启时间',
  `holderId` bigint(20) NOT NULL COMMENT '预约人id',
  `husbandId` bigint(20) NOT NULL COMMENT '丈夫id',
  `wifeId` bigint(20) NOT NULL COMMENT '妻子id',
  `joinPlayers` longtext NOT NULL COMMENT '参加婚宴的人员'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预约婚宴表';

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `activityconfig`
--
ALTER TABLE `activityconfig`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `activitydata`
--
ALTER TABLE `activitydata`
  ADD PRIMARY KEY (`type`);

--
-- Индексы таблицы `auction`
--
ALTER TABLE `auction`
  ADD PRIMARY KEY (`auctionId`),
  ADD UNIQUE KEY `UK_auction_auction` (`auctionId`);

--
-- Индексы таблицы `bossdierecord`
--
ALTER TABLE `bossdierecord`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `chum`
--
ALTER TABLE `chum`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `dailyaccrecharge`
--
ALTER TABLE `dailyaccrecharge`
  ADD PRIMARY KEY (`roleId`);

--
-- Индексы таблицы `forbidword`
--
ALTER TABLE `forbidword`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `friend`
--
ALTER TABLE `friend`
  ADD PRIMARY KEY (`roleId`);

--
-- Индексы таблицы `gold`
--
ALTER TABLE `gold`
  ADD UNIQUE KEY `userAndsId` (`userId`,`serverId`) USING BTREE,
  ADD KEY `index_1` (`userId`) USING BTREE;

--
-- Индексы таблицы `goldchange`
--
ALTER TABLE `goldchange`
  ADD KEY `userid_serverId_time` (`userid`,`serverId`,`time`,`reason`),
  ADD KEY `roleId_reason_time` (`roleId`,`reason`,`time`),
  ADD KEY `userid_serverId_time_reason_changeNum` (`userid`,`serverId`,`changeNum`,`reason`,`time`),
  ADD KEY `roleId_reason_time_changeNum` (`roleId`,`changeNum`,`time`,`reason`),
  ADD KEY `time_changeNum_reason` (`changeNum`,`reason`,`time`),
  ADD KEY `time_reason` (`reason`,`time`);

--
-- Индексы таблицы `guild`
--
ALTER TABLE `guild`
  ADD UNIQUE KEY `id` (`guildId`) USING BTREE,
  ADD UNIQUE KEY `name` (`guildName`) USING BTREE;

--
-- Индексы таблицы `guildmember`
--
ALTER TABLE `guildmember`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_auction_auction` (`id`);

--
-- Индексы таблицы `jjc`
--
ALTER TABLE `jjc`
  ADD PRIMARY KEY (`roleId`),
  ADD KEY `index1` (`career`);

--
-- Индексы таблицы `mail`
--
ALTER TABLE `mail`
  ADD PRIMARY KEY (`mailId`),
  ADD KEY `mailId` (`mailId`) USING BTREE,
  ADD KEY `receiverId` (`receiverId`) USING BTREE;

--
-- Индексы таблицы `marray`
--
ALTER TABLE `marray`
  ADD PRIMARY KEY (`marriageId`);

--
-- Индексы таблицы `marriage`
--
ALTER TABLE `marriage`
  ADD PRIMARY KEY (`marriageId`),
  ADD KEY `husbandId` (`husbandId`) USING BTREE,
  ADD KEY `wifeId` (`wifeId`) USING BTREE;

--
-- Индексы таблицы `marry_declaration`
--
ALTER TABLE `marry_declaration`
  ADD PRIMARY KEY (`roleId`);

--
-- Индексы таблицы `newserveractivity`
--
ALTER TABLE `newserveractivity`
  ADD PRIMARY KEY (`roleId`),
  ADD UNIQUE KEY `roleId` (`roleId`) USING BTREE;

--
-- Индексы таблицы `peakpk`
--
ALTER TABLE `peakpk`
  ADD PRIMARY KEY (`roleId`),
  ADD KEY `score` (`score`) USING BTREE;

--
-- Индексы таблицы `playerworldinfo`
--
ALTER TABLE `playerworldinfo`
  ADD PRIMARY KEY (`roleid`),
  ADD KEY `userId` (`userId`) USING BTREE;

--
-- Индексы таблицы `rankplayer`
--
ALTER TABLE `rankplayer`
  ADD PRIMARY KEY (`roleId`),
  ADD KEY `f_top` (`roleId`,`createTime`,`level`,`fightPower`),
  ADD KEY `f_level` (`roleId`,`createTime`,`level`,`fightPower`,`exp`),
  ADD KEY `f_horse` (`roleId`,`createTime`,`horseFightPoint`,`level`,`fightPower`,`horseId`),
  ADD KEY `f_wing` (`roleId`,`createTime`,`wingFightPoint`,`level`,`fightPower`,`wingId`),
  ADD KEY `f_roleId` (`roleId`),
  ADD KEY `f_fashionlayer` (`roleId`,`createTime`,`level`,`fightPower`,`fashionLayer`,`fashionStar`),
  ADD KEY `f_equip` (`equipFightPower`,`roleId`,`createTime`,`level`,`fightPower`),
  ADD KEY `f_equipwash` (`equipWashPer`,`roleId`,`createTime`,`level`,`fightPower`),
  ADD KEY `f_equipstrengthenlv` (`equipStrengthenLv`,`roleId`,`createTime`,`level`,`fightPower`),
  ADD KEY `f_fightPower` (`roleId`,`createTime`,`fightPower`),
  ADD KEY `f_talisman` (`roleId`,`talismanFightPower`,`level`,`fightPower`,`createTime`),
  ADD KEY `f_magic` (`roleId`,`magicFightPower`,`level`,`fightPower`,`createTime`),
  ADD KEY `f_weapon` (`roleId`,`weaponFightPower`,`level`,`fightPower`,`createTime`),
  ADD KEY `f_gem` (`roleId`,`gemFightPower`,`level`,`fightPower`,`createTime`),
  ADD KEY `f_magicweapon` (`roleId`,`magicWeaponDamage`,`level`,`fightPower`,`createTime`),
  ADD KEY `f_charm` (`roleId`,`charm`,`level`,`fightPower`,`createTime`),
  ADD KEY `f_offlineEfficiency` (`roleId`,`offlineEfficiency`,`level`,`fightPower`,`createTime`),
  ADD KEY `f_sendFlower` (`roleId`,`sendFlower`,`level`,`fightPower`,`createTime`),
  ADD KEY `f_shihai` (`roleId`,`shihai`,`level`,`fightPower`,`createTime`),
  ADD KEY `f_arena` (`roleId`,`arenaRank`,`level`,`fightPower`,`createTime`),
  ADD KEY `f_intimacy` (`roleId`,`intimacy`,`level`,`fightPower`,`createTime`);

--
-- Индексы таблицы `recharge`
--
ALTER TABLE `recharge`
  ADD PRIMARY KEY (`order_no`);

--
-- Индексы таблицы `redpacket`
--
ALTER TABLE `redpacket`
  ADD PRIMARY KEY (`rpId`);

--
-- Индексы таблицы `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`roleid`),
  ADD KEY `index_2` (`userId`) USING BTREE,
  ADD KEY `index_3` (`platformName`) USING BTREE,
  ADD KEY `index_1` (`rolename`) USING BTREE;

--
-- Индексы таблицы `roleactivitydata`
--
ALTER TABLE `roleactivitydata`
  ADD PRIMARY KEY (`roleId`);

--
-- Индексы таблицы `roleloginfo`
--
ALTER TABLE `roleloginfo`
  ADD PRIMARY KEY (`userId`);

--
-- Индексы таблицы `serverparam`
--
ALTER TABLE `serverparam`
  ADD PRIMARY KEY (`paramkey`);

--
-- Индексы таблицы `shop`
--
ALTER TABLE `shop`
  ADD PRIMARY KEY (`ID`);

--
-- Индексы таблицы `useractivity`
--
ALTER TABLE `useractivity`
  ADD UNIQUE KEY `userIdAndServerId` (`userId`,`serverId`) USING BTREE,
  ADD KEY `userId` (`userId`) USING BTREE;

--
-- Индексы таблицы `wedding`
--
ALTER TABLE `wedding`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `bossdierecord`
--
ALTER TABLE `bossdierecord`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `forbidword`
--
ALTER TABLE `forbidword`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID';
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
