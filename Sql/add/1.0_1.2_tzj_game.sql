/*==================be changed list====================*/
/* 
//1.0.0_2020.09.18_0.19.0930_main:
DROP TABLE IF EXISTS `peakpk`;
CREATE TABLE peakpk
(
    roleId     bigint(20) NOT NULL COMMENT '玩家ID',
    rankId     int(10) NOT NULL COMMENT '段位ID',
    score      int(10) NOT NULL COMMENT '积分',
    time       bigint(20) NOT NULL COMMENT '更新时间',
	times  int(10) default 0 COMMENT '本赛季参赛场次',
    PRIMARY KEY (roleId),
    KEY `score` (`score`) USING BTREE
)
    ENGINE = INNODB,
    CHARACTER SET utf8,
    COLLATE utf8_general_ci,
    COMMENT = '巅峰竞技';


DROP TABLE IF EXISTS `activityconfig`;
CREATE TABLE `activityconfig`
(
    `id`        int(11)      NOT NULL COMMENT '活动ID',
    `type`      int(11)      NOT NULL COMMENT '活动类型',
    `minLv`     int(11)               DEFAULT '1' COMMENT '最小开放等级',
    `maxLv`     int(11)               DEFAULT '800' COMMENT '最大开放等级',
    `tag`       tinyint(4)   NOT NULL COMMENT '标签(用于区分展示在哪个活动标签下)',
    `sort`      tinyint(4)            DEFAULT NULL COMMENT '活动排序',
    `name`      varchar(200) NOT NULL COMMENT '活动名称',
    `beginTime` bigint(20)   NOT NULL COMMENT '活动开始时间',
    `endTime`   bigint(20)   NOT NULL COMMENT '活动结束时间',
    `isDelete`  tinyint(4)   NOT NULL DEFAULT '0' COMMENT '是否删除(1：是，0：否)',
    `custom`    longtext     NOT NULL COMMENT '自定义配置活动数据',
    PRIMARY KEY (`type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPACT;

DROP TABLE IF EXISTS `rankplayer`;
CREATE TABLE `rankplayer`
(
    `roleId`               bigint(20) NOT NULL              DEFAULT '0' COMMENT '角色Id',
    `career`               tinyint(4)                       DEFAULT NULL COMMENT '角色职业',
    `guildFlag`            tinyint(1)                       DEFAULT NULL COMMENT '是否为公会',
    `name`                 varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色名字',
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
	`petFightPower`        int(11)                          DEFAULT '0'  COMMENT '宠物战力',
	`spiritFightPower`     int(11)                          DEFAULT '0'  COMMENT '灵体战力',
    `immEquipFightPower`   int(11)                          DEFAULT '0'  COMMENT '仙甲战力',
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
    KEY `f_arena` (`roleId`, `arenaRank`, `level`, `fightPower`, `createTime`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPACT;


//1.2.0_2020.11.26_0.20.1030_main:
DROP TABLE IF EXISTS `peakpk`;
CREATE TABLE peakpk
(
    roleId bigint(20) NOT NULL COMMENT '玩家ID',
    rankId int(10) default 0 COMMENT '段位ID',
    score  int(10) default 0 COMMENT '积分',
    time   bigint(20) NOT NULL COMMENT '更新时间',
    times  int(10) default 0 COMMENT '本赛季参赛场次',
    PRIMARY KEY (roleId),
    KEY `score` (`score`) USING BTREE
)
    ENGINE = INNODB,
    CHARACTER SET utf8,
    COLLATE utf8_general_ci,
    COMMENT = '巅峰竞技';

DROP TABLE IF EXISTS `activityconfig';
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT


DROP TABLE IF EXISTS `rankplayer`;
CREATE TABLE `rankplayer`
(
    `roleId`               bigint(20) NOT NULL              DEFAULT '0' COMMENT '角色Id',
    `career`               tinyint(4)                       DEFAULT NULL COMMENT '角色职业',
    `guildFlag`            tinyint(1)                       DEFAULT NULL COMMENT '是否为公会',
    `name`                 varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色名字',
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
    KEY `f_arena` (`roleId`, `arenaRank`, `level`, `fightPower`, `createTime`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPACT;
 */



/*==========The following statements need to be executed===========*/

/*1.  `peakpk`; 巅峰竞技 */
ALTER TABLE peakpk MODIFY rankId int(10) default 0 COMMENT '段位ID';
ALTER TABLE peakpk MODIFY score  int(10) default 0 COMMENT '积分';


/*2.  `activityconfig`; 运营活动配置 */
ALTER TABLE activityconfig MODIFY sort tinyint(4) NOT NULL DEFAULT '1' COMMENT '活动排序';
ALTER TABLE activityconfig ADD COLUMN state tinyint(4) NOT NULL DEFAULT '0' COMMENT '活动状态：0预发布，1进行中';
ALTER TABLE activityconfig DROP PRIMARY KEY, ADD PRIMARY KEY (id);

/*3.  `rankplayer`; 玩家排行 */
ALTER TABLE rankplayer ADD COLUMN holyEquipFightPower  int(11)							DEFAULT '0' COMMENT '圣装战力';
ALTER TABLE rankplayer ADD COLUMN monsterFightPower    int(11)							DEFAULT '0' COMMENT '神兽战力';
ALTER TABLE rankplayer ADD COLUMN petSoulLv			   int(11)							DEFAULT '0' COMMENT '宠物御魂等级';
ALTER TABLE rankplayer ADD COLUMN petLv				   int(11)							DEFAULT '0' COMMENT '宠物等级';
ALTER TABLE rankplayer ADD COLUMN horseSoulLv		   int(11)							DEFAULT '0' COMMENT '坐骑御魂等级';
ALTER TABLE rankplayer ADD COLUMN horseLv			   int(11)							DEFAULT '0' COMMENT '坐骑等级';





