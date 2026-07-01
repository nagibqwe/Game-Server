-- ------------------------------------------
-- Database: tzj_game
-- Usage: Create all tables for the database
-- ------------------------------------------
-- Modification records:
-- ==============2021/08/18 13:55==============
-- Desc: Change default charset from utf8 to utf8mb4 , collate use utf8mb4_unicode_ci;
-- ============================================


-- ----------------------------
-- 1.Table structure for table `activityconfig`
-- ----------------------------
DROP TABLE IF EXISTS `activityconfig`;
CREATE TABLE `activityconfig` (
  `id` int(11) NOT NULL COMMENT '活动ID',
  `type` int(11) NOT NULL COMMENT '活动类型',
  `minLv` int(11) NOT NULL DEFAULT '1' COMMENT '最小开放等级',
  `maxLv` int(11) NOT NULL DEFAULT '800' COMMENT '最大开放等级',
  `tag` tinyint(4) NOT NULL COMMENT '标签(用于区分展示在哪个活动标签下)',
  `sort` tinyint(4) NOT NULL DEFAULT '1' COMMENT '活动排序',
  `name` varchar(200) NOT NULL COMMENT '活动名称',
  `beginTime` bigint(20) NOT NULL COMMENT '活动开始时间',
  `endTime` bigint(20) NOT NULL COMMENT '活动结束时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除(1：是，0：否)',
  `custom` longtext NOT NULL COMMENT '自定义配置活动数据',
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '活动状态：0预发布，1进行中',
  `startRecordTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '活动开始记录时间',
  `endRecordTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '活动结束记录时间',
  `isOpenServer` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否为开服活动',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '运营活动的配置数据表';


-- ----------------------------
-- 2.Table structure for table `activitydata`
-- ----------------------------
DROP TABLE IF EXISTS `activitydata`;
CREATE TABLE `activitydata`
(
    `type`    int(11)  NOT NULL COMMENT '活动类型',
    `actData` longtext NOT NULL COMMENT '活动相关数据',
    PRIMARY KEY (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '运营活动的运行数据表';


-- ----------------------------
-- 3.Table structure for table `activityrank`
-- ----------------------------
DROP TABLE IF EXISTS `activityrank`;
CREATE TABLE `activityrank`
(
    `id`       bigint(50) NOT NULL,
    `roleId`   bigint(50) NOT NULL,
    `type`     bigint(50)  DEFAULT NULL,
    `funtionV` int(5)      DEFAULT NULL,
    `rankDate` int(20)     DEFAULT NULL,
    `name`     varchar(20) DEFAULT NULL,
    `serverId` int(5)      DEFAULT NULL,
    `plat`     varchar(10) DEFAULT NULL,
    `serial`   int(10)    NOT NULL,
    `send`     bit(1)     NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '运营活动的排行数据表';

-- ----------------------------
-- 4.Table structure for table `bossdierecord`
-- ----------------------------
DROP TABLE IF EXISTS `bossdierecord`;
CREATE TABLE `bossdierecord`
(
    `id`         bigint(20) NOT NULL AUTO_INCREMENT,
    `playerId`   bigint(20)  DEFAULT NULL,
    `mapName`    varchar(50) DEFAULT NULL,
    `xPos`       int(11)     DEFAULT NULL,
    `yPos`       int(11)     DEFAULT NULL,
    `killedTime` bigint(20)  DEFAULT NULL,
    `bossName`   varchar(50) DEFAULT NULL,
    `reward`     text,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = 'boss死亡记录表';


-- ----------------------------
-- 5.Table structure for table `dailyaccrecharge`
-- ----------------------------
DROP TABLE IF EXISTS `dailyaccrecharge`;
CREATE TABLE `dailyaccrecharge`
(
    `roleId`               bigint(20) NOT NULL COMMENT '角色ID',
    `DailyAccRechargeData` text COMMENT '每日累充数据',
    PRIMARY KEY (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '日常累充表';

-- ----------------------------
-- 6.Table structure for table `forbidword`
-- ----------------------------
DROP TABLE IF EXISTS `forbidword`;
CREATE TABLE `forbidword`
(
    `id`   int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `word` varchar(500) DEFAULT NULL COMMENT '禁言内容或关键字',
    `type` int(11)      DEFAULT NULL COMMENT '0 关键字 1 内容',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '禁言数据表';

-- ----------------------------
-- 7.Table structure for table `friend`
-- ----------------------------
DROP TABLE IF EXISTS `friend`;
CREATE TABLE `friend`
(
    `roleId`        bigint(20) NOT NULL COMMENT '角色ID',
    `latelyPlayers` longtext COMMENT '最近聊天列表',
    `friends`       longtext COMMENT '好友列表',
    `enemies`       longtext COMMENT '仇人列表',
    `shields`       longtext COMMENT '屏蔽列表',
    `sendLogs`      longtext COMMENT '送出礼物的日志',
    `receiveLogs`   longtext COMMENT '接收礼物的日志',
	`approvalList`   longtext COMMENT '审批列表',
	`shieldAddFriend`   longtext COMMENT '屏蔽好友申请列表',
    PRIMARY KEY (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '好友表';

-- ----------------------------
-- 8.Table structure for table `gold`
-- ----------------------------
DROP TABLE IF EXISTS `gold`;
CREATE TABLE `gold`
(
    `userId`        bigint(20)                   DEFAULT NULL COMMENT '账号ID',
    `serverId`      int(11)                      DEFAULT NULL COMMENT '服务器id',
    `platformName`  varchar(64)                  DEFAULT NULL COMMENT '平台名',
    `rechargeGold`  int(11)                      DEFAULT NULL COMMENT '充值获得元宝数',
    `reaminGold`    int(11)                      DEFAULT NULL COMMENT '剩余元宝数',
    `costGold`      int(11)                      DEFAULT NULL COMMENT '非交易消耗元宝数',
    `tradeAddGold`  int(11)                      DEFAULT NULL COMMENT '交易获得元宝数',
    `tradeCostGold` int(11)                      DEFAULT NULL COMMENT '交易消耗元宝数',
    UNIQUE KEY `userAndsId` (`userId`, `serverId`) USING BTREE,
    KEY `index_1` (`userId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '元宝快照表';

-- ----------------------------
-- 9.Table structure for table `goldchange`
-- ----------------------------
DROP TABLE IF EXISTS `goldchange`;
CREATE TABLE `goldchange`
(
    `userid`       bigint(20)  DEFAULT NULL COMMENT '账号id',
    `roleId`       bigint(20)  DEFAULT NULL COMMENT '角色id',
    `serverId`     int(4)      DEFAULT NULL COMMENT '服务器id',
    `platformName` varchar(32) DEFAULT NULL COMMENT '渠道号',
    `beforeNum`    int(4)      DEFAULT NULL COMMENT '改变前数量',
    `changeNum`    int(4)      DEFAULT NULL COMMENT '改变数量，小于0表示减少',
    `afterNum`     int(4)      DEFAULT NULL COMMENT '改变后数量',
    `reason`       int(4)      DEFAULT NULL COMMENT '改变原因',
    `time`         int(4)      DEFAULT NULL COMMENT '改变时间',
    KEY `userid_serverId_time` (`userid`, `serverId`, `time`, `reason`),
    KEY `roleId_reason_time` (`roleId`, `reason`, `time`),
    KEY `userid_serverId_time_reason_changeNum` (`userid`, `serverId`, `changeNum`, `reason`, `time`),
    KEY `roleId_reason_time_changeNum` (`roleId`, `changeNum`, `time`, `reason`),
    KEY `time_changeNum_reason` (`changeNum`, `reason`, `time`),
    KEY `time_reason` (`reason`, `time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '元宝改变表';

-- ----------------------------
-- 10.Table structure for table `guild`
-- ----------------------------
DROP TABLE IF EXISTS `guild`;
CREATE TABLE `guild`
(
    `guildId`        bigint(20)                                      NOT NULL DEFAULT '0' COMMENT '公会id',
    `guildName`      varchar(64)                                     NOT NULL COMMENT '帮会名',
    `chairmanId`     bigint(20)                                      NOT NULL DEFAULT '0' COMMENT '会长id',
    `createTime`     int(4)                                          NOT NULL DEFAULT '0' COMMENT '创建时间',
    `level`          int(4)                                          NOT NULL DEFAULT '0' COMMENT '当前基地等级',
    `buildValue`     bigint(20)                                      NOT NULL DEFAULT '0' COMMENT '当前建设度',
    `datas`          longtext                                        COMMENT '其他帮会数据',
    `guildredpacket` text 										     COMMENT '红包的数据日志',
    UNIQUE KEY `id` (`guildId`) USING BTREE,
    UNIQUE KEY `name` (`guildName`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '帮会表';

-- ----------------------------
-- 11.Table structure for table `jjc`
-- ----------------------------
DROP TABLE IF EXISTS `jjc`;
CREATE TABLE `jjc`
(
    `roleId`  bigint(20)                     NOT NULL COMMENT '玩家ID',
    `career`  int(11)                        NOT NULL COMMENT '职业',
    `camp`    int(11)                        NOT NULL COMMENT '出生阵营',
    `score`   int(11)                        NOT NULL COMMENT '积分',
    `time`    int(11)                        NOT NULL COMMENT '积分修改时间',
    `records` varchar(3000)                  NOT NULL COMMENT '战绩',
    PRIMARY KEY (`roleId`),
    KEY `index1` (`career`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '竞技场表';

-- ----------------------------
-- 12.Table structure for table `mail`
-- ----------------------------
DROP TABLE IF EXISTS `mail`;
CREATE TABLE `mail`
(
    `readTable`        tinyint(4)          DEFAULT NULL COMMENT '用来标识是否是多语言处理包',
    `type`             int(11)             NOT NULL COMMENT '邮件类型，1：系统，2：后台',
    `mailId`           bigint(20)          NOT NULL DEFAULT '0' COMMENT '邮件唯一Id',
    `receiveTime`      bigint(20)          DEFAULT NULL COMMENT '邮件收到时间，单位ms',
    `sender`           varchar(64)         DEFAULT NULL COMMENT '邮件发件人，1：系统(集市、某副本、商城等)，2：后台(后台发件人)',
    `receiverId`       bigint(20)          DEFAULT NULL COMMENT '邮件收件人角色Id',
    `isRead`           tinyint(4)          DEFAULT NULL COMMENT '是否已读，0：未读，1：已读',
    `hasAttachment`    tinyint(4)          DEFAULT NULL COMMENT '是否有附件，0：无，1：有',
    `isAttachReceived` tinyint(4)          DEFAULT NULL COMMENT '附件是否已领取，0：未领取，1：已领取',
    `mailData`         longtext            COMMENT '整个邮件数据(JSON化存储，包含邮件全部信息[邮件标题、内容、附件等会在内])',
	`source`           int(11)             NOT NULL DEFAULT '0' COMMENT '附件来源',
    PRIMARY KEY (`mailId`),
    KEY `mailId` (`mailId`) USING BTREE,
    KEY `receiverId` (`receiverId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '邮件表';

-- ----------------------------
-- 13.Table structure for table `marriage`
-- ----------------------------
DROP TABLE IF EXISTS `marriage`;
CREATE TABLE `marriage`
(
    `marriageId`   bigint(20) NOT NULL COMMENT '婚姻唯一id',
    `husbandId`    bigint(20) NOT NULL COMMENT '丈夫id',
    `wifeId`       bigint(20) NOT NULL COMMENT '妻子id',
    `time`         int(11)    NOT NULL COMMENT '结婚时间',
    `top`          int(11)    NOT NULL COMMENT '第几对夫妇',
    `coupleInfo`   text       NOT NULL COMMENT '夫妻双方详细信息',
    `hasMarryType` text       NOT NULL COMMENT '求婚已经选择过的婚礼类型',
    `weddingType`  text       NOT NULL COMMENT '待办的婚宴',
    PRIMARY KEY (`marriageId`),
    KEY `husbandId` (`husbandId`) USING BTREE,
    KEY `wifeId` (`wifeId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '婚姻表';


-- ----------------------------
-- 14.Table structure for table `newserveractivity`
-- ----------------------------
DROP TABLE IF EXISTS `newserveractivity`;
CREATE TABLE `newserveractivity`
(
    `roleId`       bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
    `activityData` longtext COMMENT '角色开服7天活动信息',
    PRIMARY KEY (`roleId`),
    UNIQUE KEY `roleId` (`roleId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '开服7天活动信息';

-- ----------------------------
-- 15.Table structure for table `playerworldinfo`
-- ----------------------------
DROP TABLE IF EXISTS `playerworldinfo`;
CREATE TABLE `playerworldinfo`
(
    `roleid`          		bigint(20) NOT NULL              DEFAULT '0' COMMENT '角色ID',
    `userId`          		bigint(20) NOT NULL              DEFAULT '0' COMMENT '账号ID',
    `rolename`        		varchar(64)                     DEFAULT NULL COMMENT '角色名',
    `career`          		tinyint(4)                       DEFAULT NULL COMMENT '职业',
    `level`           		int(11)                          DEFAULT NULL COMMENT '等级',
    `csid`            		int(11)    NOT NULL              DEFAULT '0' COMMENT '角色创建服id',
    `lastOffTime`     		int(11)    NOT NULL              DEFAULT '0' COMMENT '角色上次离线时间',
    `horseId`         		int(11)                          DEFAULT NULL COMMENT '玩家当前坐骑',
    `wingId`          		int(11)                          DEFAULT NULL,
    `fightPower`      		bigint(20)                       DEFAULT '0' COMMENT '战斗力',
    `guildId`         		bigint(20)                       DEFAULT '0' COMMENT '仙盟id',
    `fashionBodyId`   		int(11)                          DEFAULT NULL COMMENT '时装身体ID',
    `fashionWeaponId` 		int(11)                          DEFAULT NULL COMMENT '时装武器ID',
    `createTime`      		int(11)                          DEFAULT NULL COMMENT '角色的创建时间',
    `plat`            		varchar(50)                      DEFAULT NULL COMMENT '平台的名字',
    `stateVip`        		int(10)                          DEFAULT '0' COMMENT '境界等级',
    `shiHaiLevel`     		int(10)                          DEFAULT '0' COMMENT '识海等级',
    `sex`             		tinyint(4)                       DEFAULT NULL COMMENT '性别',
    `fashionHalo`     		int(10)                          DEFAULT '0' COMMENT '',
    `fashionMatrix`   		int(10)                          DEFAULT '0' COMMENT '',
    `playerVip`       		int(10)                          DEFAULT '0' COMMENT '玩家VIP',
    `spiritId`        		int(10)                          DEFAULT '0' COMMENT '灵体外观',
	`soulArmorId`     		int(10)                          DEFAULT '0' COMMENT '魂甲品质',
	`fashionHeadId`   		int(10)                          DEFAULT '0' COMMENT 'fashionBodyId',
	`fashionHeadFrameId`    int(10)                          DEFAULT '0' COMMENT '所穿时装武器Id',
	`customHeadPath`   		varchar(255)                     DEFAULT NULL COMMENT '自定义头像路径',
	`useCustomHead`    tinyint(4)                            DEFAULT '0' COMMENT '是否使用自定义头像',
    PRIMARY KEY (`roleid`),
    KEY `userId` (`userId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '角色简要信息表';

-- ----------------------------
-- 16.Table structure for table `rankplayer`
-- ----------------------------
DROP TABLE IF EXISTS `rankplayer`;
CREATE TABLE `rankplayer`
(
    `roleId`               bigint(20) NOT NULL              DEFAULT '0' COMMENT '角色Id',
    `career`               tinyint(4)                       DEFAULT NULL COMMENT '角色职业',
    `guildFlag`            tinyint(1)                       DEFAULT NULL COMMENT '是否为公会',
    `name`                 varchar(64)                      DEFAULT NULL COMMENT '角色名字',
    `createTime`           bigint(20)                       DEFAULT NULL COMMENT '角色创建时间',
    `createSid`            int(11)                          DEFAULT NULL COMMENT '角色创建区服',
    `level`                int(11)                          DEFAULT '0' COMMENT '角色等级',
    `levelUpTime`          int(11)                          DEFAULT '0' COMMENT '上次升级时间',
    `fightPower`           bigint(20)                       DEFAULT '0' COMMENT '战斗力',
    `horseId`              int(11)                          DEFAULT '0' COMMENT '坐骑最高阶',
    `horseFightPoint`      int(11)                          DEFAULT '0' COMMENT '坐骑系统战斗力',
    `wingId`               int(11)                          DEFAULT '0' COMMENT '翅膀最高阶',
    `wingFightPoint`       int(11)                          DEFAULT NULL COMMENT '翅膀战斗力',
    `clothesEquipId`       int(11)                          DEFAULT '0' COMMENT '衣服装备Id',
    `weaponsEquipId`       int(11)                          DEFAULT '0' COMMENT '武器装备Id',
    `clothesStar`          int(11)                          DEFAULT '0' COMMENT '衣服部位的星级',
    `weaponStar`           int(11)                          DEFAULT '0' COMMENT '武器部位的星级',
    `fashionBodyId`        int(11)                          DEFAULT '0' COMMENT '时装身体Id',
    `fashionWeaponId`      int(11)                          DEFAULT '0' COMMENT '时装武器Id',
    `beWorshipedNum`       int(11)                          DEFAULT '0' COMMENT '被崇拜次数',
    `exp`                  bigint(20)                       DEFAULT '0' COMMENT '当前角色的经验值',
    `fashionLayer`         int(11)                          DEFAULT '0' COMMENT '时装升阶等级',
    `fashionStar`          int(11)                          DEFAULT '0' COMMENT '时装升星',
    `lastUpdateTime`       varchar(500)                     DEFAULT NULL COMMENT '一些数据的最后更新时间',
    `equipWashPer`         int(11)                          DEFAULT '0' COMMENT '装备洗练评分',
    `equipStrengthenLv`    int(11)                          DEFAULT '0' COMMENT '装备强化',
    `equipFightPower`      int(11)                          DEFAULT '0' COMMENT '装备战力',
    `gemLv`                int(11)                          DEFAULT '0' COMMENT '宝石总等级',
    `gemFightPower`        int(11)                          DEFAULT '0' COMMENT '宝石战力',
    `magicWeaponDamage`    int(11)                          DEFAULT '0' COMMENT '法宝等级',
    `talismanFightPower`   int(11)                          DEFAULT '0' COMMENT '法器战力',
    `magicFightPower`      int(11)                          DEFAULT '0' COMMENT '阵法战力',
    `weaponFightPower`     int(11)                          DEFAULT '0' COMMENT '神器战力',
    `strengthenFightPower` int(11)                          DEFAULT '0' COMMENT '强化战力',
    `charm`                int(11)                          DEFAULT '0' COMMENT '魅力值',
    `offlineEfficiency`    bigint(20)                       DEFAULT '0' COMMENT '离线效率',
    `sendFlower`           int(11)                          DEFAULT '0' COMMENT '送花值',
    `shihai`               int(11)                          DEFAULT '0' COMMENT '石海层数',
    `arenaRank`            int(11)                          DEFAULT '0' COMMENT '竞技场排名',
    `topHallFightPower`    bigint(20)                       DEFAULT '0' COMMENT '名人堂排名战力',
    `universeFightPower`   bigint(20)                       DEFAULT '0' COMMENT '天墟战场名人堂战力',
    `equipStar`            int(11)                          DEFAULT '0' COMMENT '穿戴装备大于6阶总星数',
    `equipStarGradeNum`    int(11)                          DEFAULT '0' COMMENT '穿戴装备大于6阶总阶数',
    `equipAllStar`         int(11)                          DEFAULT '0' COMMENT '装备灵体总星级',
    `petFightPower`        int(11)                          DEFAULT '0' COMMENT '宠物战力',
    `spiritFightPower`     int(11)                          DEFAULT '0' COMMENT '灵体战力',
    `immEquipFightPower`   int(11)                          DEFAULT '0' COMMENT '仙甲战力',
	`holyEquipFightPower`  int(11)							DEFAULT '0' COMMENT '圣装战力',
    `monsterFightPower`    int(11)							DEFAULT '0' COMMENT '神兽战力',
	`petSoulLv`			   int(11)							DEFAULT '0' COMMENT '宠物御魂等级',
	`petLv`				   int(11)							DEFAULT '0' COMMENT '宠物等级',
	`horseSoulLv`		   int(11)							DEFAULT '0' COMMENT '坐骑御魂等级',
	`horseLv`			   int(11)							DEFAULT '0' COMMENT '坐骑等级',
	`consumeGold`          int(11)                          DEFAULT '0' COMMENT '消费排行',
	`baguaPower`           int(11)                          DEFAULT '0' COMMENT '八卦排行',
	`immortalsoulPower`    int(11)                          DEFAULT '0' COMMENT '灵魂排行',
	`devilSoulPower`       int(11)                          DEFAULT '0' COMMENT '魔魂排行',
	`horseEquipPower`      int(11)                          DEFAULT '0' COMMENT '坐骑装备排行',
	`intimacy`             int(11)                          DEFAULT '0' COMMENT '亲密度',

    PRIMARY KEY (`roleId`),
    KEY `f_top` (`roleId`, `createTime`, `level`, `fightPower`),
    KEY `f_level` (`roleId`, `createTime`, `level`, `fightPower`, `exp`),
    KEY `f_horse` (`roleId`, `createTime`, `horseFightPoint`, `level`, `fightPower`, `horseId`),
    KEY `f_wing` (`roleId`, `createTime`, `wingFightPoint`, `level`, `fightPower`, `wingId`),
    KEY `f_roleId` (`roleId`),
    KEY `f_fashionlayer` (`roleId`, `createTime`, `level`, `fightPower`, `fashionLayer`, `fashionStar`),
    KEY `f_equip` (`equipFightPower`, `roleId`, `createTime`, `level`, `fightPower`),
    KEY `f_equipwash` (`equipWashPer`, `roleId`, `createTime`, `level`, `fightPower`),
    KEY `f_equipstrengthenlv` (`equipStrengthenLv`, `roleId`, `createTime`, `level`, `fightPower`),
    KEY `f_fightPower` (`roleId`, `createTime`, `fightPower`),
    KEY `f_talisman` (`roleId`, `talismanFightPower`, `level`, `fightPower`, `createTime`),
    KEY `f_magic` (`roleId`, `magicFightPower`, `level`, `fightPower`, `createTime`),
    KEY `f_weapon` (`roleId`, `weaponFightPower`, `level`, `fightPower`, `createTime`),
    KEY `f_gem` (`roleId`, `gemFightPower`, `level`, `fightPower`, `createTime`),
    KEY `f_magicweapon` (`roleId`, `magicWeaponDamage`, `level`, `fightPower`, `createTime`),
    KEY `f_charm` (`roleId`, `charm`, `level`, `fightPower`, `createTime`),
    KEY `f_offlineEfficiency` (`roleId`, `offlineEfficiency`, `level`, `fightPower`, `createTime`),
    KEY `f_sendFlower` (`roleId`, `sendFlower`, `level`, `fightPower`, `createTime`),
    KEY `f_shihai` (`roleId`, `shihai`, `level`, `fightPower`, `createTime`),
    KEY `f_arena` (`roleId`, `arenaRank`, `level`, `fightPower`, `createTime`),
    KEY `f_intimacy` (`roleId`, `intimacy`, `level`, `fightPower`, `createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '玩家排行榜表';

-- ----------------------------
-- 17.Table structure for table `recharge`
-- ----------------------------
DROP TABLE IF EXISTS `recharge`;
CREATE TABLE recharge
(
    order_no   varchar(64) NOT NULL DEFAULT '' COMMENT '订单号',
    user_id    bigint(20)           DEFAULT 0 COMMENT '账号Id',
    role_id    bigint(20)           DEFAULT 0 COMMENT '角色ID',
    srv_id     int(11)              DEFAULT 0 COMMENT '服务器ID',
    goods_id   int(11)              DEFAULT 0 COMMENT '商品ID',
    goods_type varchar(32)         DEFAULT '' COMMENT '商品类型',
    goods_ext  varchar(255)         DEFAULT '' COMMENT '商品扩展数据',
    goods_name varchar(64)         DEFAULT '' COMMENT '商品名称',
    goods_cfg  varchar(32)         DEFAULT '' COMMENT '商品映射',
    total_fee  int(11)              DEFAULT 0 COMMENT '订单金额：单位分',
    item_id    int(11)              DEFAULT 0 COMMENT '待发放道具ID',
    game_money int(11)              DEFAULT 0 COMMENT '待发放游戏货币',
    ext_param  varchar(255)         DEFAULT '' COMMENT '透传参数',
    sign_type  varchar(16)         DEFAULT '' COMMENT '签名算法',
    sign       varchar(128)         DEFAULT '' COMMENT '签名',
    add_time   bigint(20)           DEFAULT 0 COMMENT '添加时间',
    status     tinyint(4)           DEFAULT 0 COMMENT '订单状态：0未发货，1已发货，2异常',
    src        tinyint(4)           DEFAULT 0 COMMENT '订单来源',
    data       text                 DEFAULT NULL COMMENT '原始数据',
	money_type varchar(16)         DEFAULT 'CNY' COMMENT '货币类型',
    notify_time varchar(32)        DEFAULT '' COMMENT '异步通知发货时间',
    notify_id   varchar(255)         DEFAULT ''  COMMENT '异步通知ID',
    trade_no varchar(255)           DEFAULT '' COMMENT '第三方支付订单',
    trade_status int(11)            DEFAULT 0 COMMENT '支付成功,目前就只有此类型',
	totalRecharge int(11)           DEFAULT 0 COMMENT '计算到游戏累充值',
    totalVipPower int(11)           DEFAULT 0 COMMENT 'vip经验加成',
    PRIMARY KEY (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '玩家充值订单表';

-- ----------------------------
-- 18.Table structure for table `redpacket`
-- ----------------------------
DROP TABLE IF EXISTS `redpacket`;
CREATE TABLE `redpacket`
(
    `rpId`         bigint(20) NOT NULL COMMENT '红包实例ID',
    `redpacket`    text       COMMENT '红包内容',
    `rpCreateTime` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `rpType`       int(11)    DEFAULT '1' COMMENT '红包类型',
    PRIMARY KEY (`rpId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '红包表';

-- ----------------------------
-- 19.Table structure for table `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `roleid`          bigint(20) NOT NULL                              DEFAULT '0' COMMENT '角色ID',
    `rolename`        varchar(64)                                      DEFAULT NULL COMMENT '角色名',
    `userId`          bigint(20)                                       DEFAULT NULL COMMENT '账号ID',
    `platformName`    varchar(100)                                     DEFAULT NULL COMMENT '平台名',
    `career`          tinyint(4)                                       DEFAULT NULL COMMENT '职业',
    `degree`          tinyint(4)                                       DEFAULT NULL COMMENT '转职阶位',
    `lv`              int(11)                                          DEFAULT NULL COMMENT '转职等级',
    `weapon`          int(9)                                           DEFAULT NULL COMMENT '武器',
    `wingId`          int(4)                                           DEFAULT NULL COMMENT '翅膀ID',
    `roledata`        longtext                                         COMMENT '角色数据',
    `lastLoginTime`   bigint(20)                                       DEFAULT NULL COMMENT '上一次登录时间',
    `createTime`      int(4)                                           DEFAULT NULL COMMENT '创建时间',
    `deleteTime`      int(4)                                           DEFAULT NULL COMMENT '角色删除时间，0表示未删除',
    `serverId`        int(11)                                          DEFAULT NULL COMMENT '服务器id',
    `equipMinStar`    int(11)                                          DEFAULT NULL COMMENT '最低星级',
    `languageType`    int(11)                                          DEFAULT '0' COMMENT '语言类型，0：中文简体，1：中文繁体，2：泰文，3：越南文，4：韩文 等等',
    `fashionBodyId`   int(11)                                          DEFAULT NULL COMMENT '时装身体ID',
    `fashionWeaponId` int(11)                                          DEFAULT NULL COMMENT '时装武器ID',
    `useIconState`    int(11)                                          DEFAULT '0' COMMENT '头像的状态值',
    PRIMARY KEY (`roleid`),
    KEY `index_2` (`userId`) USING BTREE,
    KEY `index_3` (`platformName`) USING BTREE,
    KEY `index_1` (`rolename`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '角色表';


-- ----------------------------
-- 20.Table structure for table `roleactivitydata`
-- ----------------------------
DROP TABLE IF EXISTS `roleactivitydata`;
CREATE TABLE `roleactivitydata`
(
    `roleId`  bigint(20) NOT NULL COMMENT '角色ID',
    `actData` longtext   NOT NULL COMMENT '角色活动相关数据',
    PRIMARY KEY (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '运营活动角色数据表';

-- ----------------------------
-- 21.Table structure for table `marry_declaration` 
-- ----------------------------
DROP TABLE IF EXISTS `marry_declaration`;
CREATE TABLE `marry_declaration`
(
    `roleId`        bigint(20) NOT NULL COMMENT '角色ID',
    `declarationId` int(10)    NOT NULL COMMENT '宣言ID',
    `timeout`       bigint(20) NOT NULL COMMENT '过期时间',
    PRIMARY KEY (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '婚姻宣言表';

-- ----------------------------
-- 22.Table structure for table `roleloginfo`
-- ----------------------------
DROP TABLE IF EXISTS `roleloginfo`;
CREATE TABLE `roleloginfo`
(
    `userId`        bigint(20) NOT NULL COMMENT '账号ID',
    `platUserId`    varchar(50) DEFAULT NULL COMMENT '平台的账号名',
    `platformName`  varchar(50) DEFAULT NULL COMMENT '平台名称',
    `os`            varchar(15) DEFAULT NULL COMMENT '系统',
    `maCode`        varchar(50) DEFAULT NULL COMMENT '设备码',
    `uuid`          varchar(50) DEFAULT NULL COMMENT 'funcell生成的uuid',
    `createTime`    bigint(20)  DEFAULT NULL COMMENT '创建时间',
    `lastLoginTime` bigint(20)  DEFAULT NULL,
    PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '角色账号登录信息表';

-- ----------------------------
-- 23.Table structure for table `serverparam`
-- ----------------------------
DROP TABLE IF EXISTS `serverparam`;
CREATE TABLE `serverparam`
(
    `paramkey`   varchar(64) NOT NULL,
    `serverid`   int(11) DEFAULT NULL,
    `paramvalue` longtext    NOT NULL,
    PRIMARY KEY (`paramkey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '服务器信息表';

-- ----------------------------
-- 24.Table structure for table `useractivity`
-- ----------------------------
DROP TABLE IF EXISTS `useractivity`;
CREATE TABLE `useractivity`
(
    `userId`             bigint(20)   NOT NULL COMMENT '玩家userId值',
    `serverId`           int(4)      DEFAULT NULL COMMENT '服务器id',
    `vipRewardGet`       varchar(200) NOT NULL COMMENT 'vip等级奖励领取结果',
    `rewardContext`      longtext     NOT NULL COMMENT '活动奖励记录值',
    `rewardVesionIdList` varchar(300) NOT NULL COMMENT '已领取的资源更新奖励对应资源版本号列表',
    `isEvaluated`        int(11)     DEFAULT '0' COMMENT '是否已经对软件进行评价，0 还未评价，1 已评价',
    `isPop`              varchar(50) DEFAULT NULL COMMENT '限时直购今日是否弹窗标识',
    UNIQUE KEY `userIdAndServerId` (`userId`, `serverId`) USING BTREE,
    KEY `userId` (`userId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '账号活动数据表';

-- ----------------------------
-- 25.Table structure for table `wedding`
-- ----------------------------
DROP TABLE IF EXISTS `wedding`;
CREATE TABLE `wedding`
(
    `id`          bigint(20)                                          NOT NULL COMMENT '主键',
    `marriageId`  bigint(20)                                          NOT NULL COMMENT '结婚证唯一id',
    `level`       int(11)                                             NOT NULL COMMENT '婚宴等级',
    `holdTime`    int(11)                                             NOT NULL COMMENT '预约时间（秒）',
    `prayTime`    int(11)                                             NOT NULL COMMENT '预约的时间段的开启时间',
    `holderId`    bigint(20)                                          NOT NULL COMMENT '预约人id',
    `husbandId`   bigint(20)                                          NOT NULL COMMENT '丈夫id',
    `wifeId`      bigint(20)                                          NOT NULL COMMENT '妻子id',
    `joinPlayers` longtext                                            NOT NULL COMMENT '参加婚宴的人员',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '预约婚宴表';

-- ----------------------------
-- 26.Table structure for table `shop`
-- ----------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop`
(
    ID                int(11) UNSIGNED NOT NULL COMMENT '商品唯一ID',
    itemID            int(11) UNSIGNED DEFAULT 0 COMMENT '道具ID',
    shopID            int(11) UNSIGNED DEFAULT 0 COMMENT '所属商城ID,1.元宝商城,2.兑换商城',
    labelID           int(11) UNSIGNED DEFAULT 0 COMMENT '商品标签页ID',
    level             int(11) UNSIGNED DEFAULT 0 COMMENT '购买需求等级',
    militaryLevel     int(11) UNSIGNED DEFAULT 0 COMMENT '购买需求军衔',
    guildLevel        int(11) UNSIGNED DEFAULT 0 COMMENT '购买需求帮会等级',
    guildShopLvlStart int(11) UNSIGNED DEFAULT 0 COMMENT '购买需要的仙盟商店的最低等级',
    guildShopLvlEnd   int(11) UNSIGNED DEFAULT 0 COMMENT '购买需要的仙盟商店的最高等级',
    worldLvlStart     int(11) UNSIGNED DEFAULT 0 COMMENT '购买需求最低世界等级',
    worldLvlEnd       int(11) UNSIGNED DEFAULT 0 COMMENT '购买需求结束世界等级',
    isDiscount        int(11) UNSIGNED DEFAULT 0 COMMENT '开通修神锻体后是否打折，具体折扣读取修神锻体特权表,0代表不打折/1代表打折',
    vipLevel          int(11) UNSIGNED DEFAULT 0 COMMENT '购买需求境界等级',
    occupation        int(11)          DEFAULT -1 COMMENT '角色职业限制,-1.通用无限制',
    limitType         int(11) UNSIGNED DEFAULT 0 COMMENT '限购类型,0.不限购；1.日限够；2.周限购；3.月限购；4.年限购；5.终身限购',
    buyNum            int(11)          DEFAULT -1 COMMENT '可购买次数,-1为无限',
    currencyID        int(11) UNSIGNED DEFAULT 0 COMMENT '购买货币ID',
    price             int(11) UNSIGNED DEFAULT 0 COMMENT '打折前价格',
    discountPrice     int(11) UNSIGNED DEFAULT 0 COMMENT '打折后价格',
    discount          int(11) UNSIGNED DEFAULT 0 COMMENT '打折数,0为不打折,>0为具体打折数',
    promotion         int(11) UNSIGNED DEFAULT 0 COMMENT '促销标签',
    sort              int(11) UNSIGNED DEFAULT 0 COMMENT '排列优先级',
    upTime            varchar(50)      DEFAULT '1970-1-1 00:00:00' COMMENT '上架时间,年_月_日_时_分_秒，0则为即刻上架',
    downTime          varchar(50)      DEFAULT '1970-1-1 00:00:00' COMMENT '下架时间,年_月_日_时_分_秒，0为永不下架',
    overdue           varchar(50)      DEFAULT '1970-1-1 00:00:00' COMMENT '商品的道具过期时间,年_月_日_时_分_秒,0则取duration',
    duration          int(11) UNSIGNED DEFAULT 0 COMMENT '持续时间',
    bind              int(11) UNSIGNED DEFAULT 1 COMMENT '是否绑定,0非绑定，1绑定',
    refreshCurrency   int(11)          DEFAULT -1 COMMENT '刷新使用货币类型，-1为不能刷新',
    refreshNum        int(11) UNSIGNED DEFAULT 0 COMMENT '刷新货币消耗数量',
    shopType          varchar(255)     DEFAULT '' COMMENT '商城标签',
    countdiscount     varchar(255)     DEFAULT '' COMMENT '根据购买次数打折',
    openday           int(11) UNSIGNED DEFAULT 0 COMMENT '上架时间',
    closeday          int(11) UNSIGNED DEFAULT 0 COMMENT '下架时间',
    PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '商城表';

-- ----------------------------
-- 27.Table structure for table `chum`
-- ----------------------------
DROP TABLE IF EXISTS `chum`;
CREATE TABLE chum
(
    id            bigint(20) UNSIGNED NOT NULL COMMENT '挚友ID',
    rID1          bigint(20) UNSIGNED                             DEFAULT 0 COMMENT '创建者ID1',
    rID2          bigint(20) UNSIGNED                             DEFAULT 0 COMMENT '创建者ID2',
    name          varchar(128)                                    DEFAULT '' COMMENT '挚友组名',
    anno          varchar(255)                                    DEFAULT '' COMMENT 'announcement',
    lvl           int(11) UNSIGNED                                DEFAULT 0 COMMENT '等级',
    exp           int(11) UNSIGNED                                DEFAULT 0 COMMENT '经验',
    freeT         smallint(6) UNSIGNED                            DEFAULT 0 COMMENT '免费改名次数',
    datas         text                                            DEFAULT NULL COMMENT '数据',
    lastFreshTime bigint(20) UNSIGNED                             DEFAULT 0 COMMENT '最后刷新时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '挚友';


-- ----------------------------
-- 28.Table structure for table `auction`
-- ----------------------------
DROP TABLE IF EXISTS `auction`;
CREATE TABLE auction
(
    auctionId     bigint(20)    NOT NULL COMMENT '竞拍ID',
    auctionItem   varchar(2048) NOT NULL COMMENT '竞拍物品',
    auctionTime   bigint(20)    NOT NULL COMMENT '竞拍上架时间',
    auctionPrice  int(11)       NOT NULL COMMENT '竞拍价格',
    auctionOwnId  bigint(20)    NOT NULL COMMENT '竞拍上架着',
    auctionRoleId bigint(20)    NOT NULL COMMENT '竞拍者',
    auctionGuild  bigint(20)    NOT NULL COMMENT '竞拍类型',
	  `password` varchar(45) DEFAULT NULL COMMENT '竞拍密码',
    PRIMARY KEY (auctionId),
    UNIQUE INDEX UK_auction_auction (auctionId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '竞拍';


-- ----------------------------
-- 29.Table structure for table `guildMember`
-- ----------------------------
DROP TABLE IF EXISTS `guildMember`;
CREATE TABLE guildMember
(
    id         bigint(20) NOT NULL COMMENT '玩家ID',
    guildId    bigint(20) NOT NULL COMMENT '公会ID',
    contribute bigint(20) NOT NULL COMMENT '公会贡献',
    position   int(11)    NOT NULL COMMENT '公会职位',
    joinTime   bigint(20) NOT NULL COMMENT '时间',
    datas      longtext   COMMENT '其他数据',
    PRIMARY KEY (id),
    UNIQUE INDEX UK_auction_auction (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '公会成员';


-- ----------------------------
-- 30.Table structure for table `marray`
-- ----------------------------
DROP TABLE IF EXISTS `marray`;
CREATE TABLE marray
(
    marriageId bigint(20) NOT NULL COMMENT '婚姻唯一id',
    aId        bigint(20) NOT NULL COMMENT '丈夫id',
    bId        bigint(20) NOT NULL COMMENT '妻子id',
    time       bigint(20) NOT NULL COMMENT '结婚时间',
    data       text       NOT NULL COMMENT '其他信息',
    PRIMARY KEY (marriageId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '婚姻表';


-- ----------------------------
-- 31.Table structure for table `peakpk`
-- ----------------------------
DROP TABLE IF EXISTS `peakpk`;
CREATE TABLE peakpk
(
    roleId bigint(20) NOT NULL COMMENT '玩家ID',
    rankId int(10) default 0 COMMENT '段位ID',
    score  int(10) default 0 COMMENT '积分',
    time   bigint(20) NOT NULL COMMENT '更新时间',
    times  int(10) default 0 COMMENT '本赛季参赛场次',
    dayTimes int(10) default 0 COMMENT '当天参赛场次',
    timesReward bigint(20) default 0 COMMENT '场次奖励领取状态',
    stageReward bigint(20) default 0 COMMENT '段位奖励领取状态',
    PRIMARY KEY (roleId),
    KEY `score` (`score`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '巅峰竞技';
	
