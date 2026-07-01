
/*be changed list*/
/* 
//1.2.0_2020.11.26_0.20.1030_main:


//1.3.0_2020.12.28_0.21.1130_main:
    
*/

/*The following statements need to be executed*/

/*1.  `t_menu`; 菜单表 */
/*Table structure for table `t_menu` */
DROP TABLE IF EXISTS `t_menu`;

CREATE TABLE `t_menu` (
  `menuId` int(32) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menuName` varchar(50) DEFAULT NULL COMMENT '菜单名',
  `level` tinyint(4) NOT NULL DEFAULT '3' COMMENT '菜单级别',
  `parentId` int(32) DEFAULT NULL COMMENT '菜单父ID',
  `alias` varchar(50) DEFAULT NULL COMMENT '菜单别名',
  `urlPath` varchar(50) DEFAULT NULL COMMENT '菜单路径',
  `description` text COMMENT '描述',
  `isDeleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`menuId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=243 DEFAULT CHARSET=utf8;

INSERT  INTO `t_menu`(`menuId`,`menuName`,`level`,`parentId`,`alias`,`urlPath`,`description`,`isDeleted`) VALUES (1,'后台管理',1,0,'','','',0),(2,'用户管理',2,1,'','','',0),(3,'用户列表',3,2,'','user','',0),(4,'权限管理',2,1,'','','',0),(6,'菜单管理',2,1,'','','',0),(7,'菜单列表',3,6,'','menu','',0),(8,'客服管理',1,0,'','','',0),(9,'玩家数据',2,8,'','','',0),(10,'账号角色互查',3,9,'','role','',0),(11,'运营管理',1,0,'','','',0),(12,'运营工具',2,11,'','','',0),(14,'激活码生成',3,12,'','operation/getPage?type=1','',0),(16,'用户操作日志',3,2,'','admin/backendlog','',0),(17,'日志管理',1,0,'','','',0),(18,'日志查询',2,17,'','','',0),(20,'元宝变化日志',3,18,'','log/getPage?logType=2','',0),(23,'留存统计',3,99,'','operation/getPage?type=7','',0),(24,'公告管理',2,8,'','','',0),(25,'封禁管理',2,8,'','','',0),(26,'踢人下线',3,25,'','forbidden/kickPlayerPage','',0),(27,'玩家禁言',3,25,'','forbidden/silence','',0),(28,'玩家封号',3,25,'','forbidden/forbidAccount','',0),(29,'禁言解除',3,25,'','forbidden/releaseSilence','',0),(30,'帐号解封',3,25,'','forbidden/releaseForbidden','',0),(31,'即时公告',3,24,'','announce/immediate','',0),(32,'邮件管理',2,8,'','','',0),(34,'邮件列表',3,32,'','mail/mailList','',0),(35,'邮件发送',3,32,'','mail/sendMail','',0),(36,'循环公告',3,24,'','announce/cycleAnnounce','',0),(37,'公会信息',1,9,'','log/guildPage?type=0','',0),(38,'日常数据',3,99,'','statistic/getPage?statType=1','',0),(40,'在线信息(单服)',3,99,'','operation/getPage?type=2','',0),(41,'激活码查询',3,12,'','operation/getPage?type=3','',0),(43,'运营活动',2,11,'','activity','',0),(64,'服务器列表',3,108,'','server','',0),(65,'日志库列表',3,108,'','dblog/log','',0),(66,'白名单管理',3,25,'','forbidden/whiteList','',0),(67,'公会成员信息',3,9,'','log/guildPage?type=1','',0),(68,'公会动态信息',3,9,'','log/guildPage?type=2','',0),(70,'商城购买统计',3,99,'','statistic/getPage?statType=2','',0),(71,'等级分布',3,99,'','operation/getPage?type=6','',0),(72,'职业分布',3,99,'','operation/getPage?type=5','',0),(73,'排行榜',2,17,'','','',0),(75,'实时排行榜查询',3,73,'','rank/getPage?type=2','',0),(76,'在线信息(全服)',3,99,'','operation/getPage?type=4','',0),(77,'后台数据',2,1,'','','',0),(78,'后台数据加载',3,77,'','admin/data','',0),(79,'DAU统计',3,99,'','dauStatistic/index','',0),(80,'角色详情(角色ID)',3,9,'','rolelog','',0),(86,'二次付费统计',3,99,'','secondRecharge/index','',0),(87,'付费次数统计',3,99,'','rechargeCounts/index','',0),(88,'累充统计',3,99,'','accumulateRecharge/index','',0),(91,'后台充值',2,11,'','','',0),(92,'角色列表',3,4,'','backrole','',0),(93,'系统开关',3,12,'','systemSwitch','',0),(99,'数据统计',2,11,'','','',0),(107,'道具扣除',3,12,'','deductgolditem/item','',0),(108,'运维工具',2,1,'','','',0),(109,'玩家流失统计',3,99,'','statistic/getPage?statType=8','',0),(110,'玩家充值统计',3,99,'','paystatistic','',0),(111,'充值排行榜',3,73,'','rank/getPage?type=8','',0),(112,'绑定元宝统计',3,99,'','bindgold','',0),(114,'黑名单',3,12,'','operation/getPage?type=8','',0),(116,'LTV统计',3,99,'','ltvstatistic','',0),(117,'充值金额分布统计',3,99,'','paydiststatistic','',0),(120,'在线时长统计',3,99,'','onlinestatistic','',0),(121,'元宝用途统计',3,99,'','goldpurstatistic','',0),(123,'屏蔽字管理',3,25,'','forbidden/shieldKeyword','',0),(132,'全服角色信息统计',3,99,'','roleStatistic','',0),(135,'跨服副本进入日志',3,137,'','log/getPage?logType=6','',0),(137,'跨服日志查询',2,17,'','','',0),(141,'公共服房间创建日志',3,137,'','log/getPage?logType=10','',0),(148,'提供对单个连接的数据库查询',3,108,'','server/forwardToCustomSqlPage','',0),(149,'服务器指令',3,108,'','gm/gsCommand','',0),(164,'付费总览',3,99,'','rechargeOverview/index','',0),(165,'首充统计',3,99,'','firstRecharge/index','',0),(166,'物品、货币流向统计',3,99,'','itemChange/index','',0),(179,'商业化内容统计',3,99,'','businessContent/index','',0),(182,'后台充值',3,91,'','recharge/getPage?type=1','',0),(183,'后台充值审核',3,91,'','recharge/getPage?type=3','',0),(184,'后台充值列表',3,91,'','recharge/getPage?type=4','',0),(189,'成就奖励领取日志',3,18,'','log/getPage?logType=13','',0),(191,'聊天日志',3,18,'','log/getPage?logType=21','',0),(192,'后台指令日志',3,18,'','log/getPage?logType=22','',0),(193,'gm命令日志',3,18,'','log/getPage?logType=23','',0),(194,'邮件日志',3,18,'','log/getPage?logType=24','',0),(195,'排行榜日志',3,18,'','log/getPage?logType=25','',0),(197,'反馈日志',3,18,'','log/getPage?logType=27','',0),(198,'公共服指令',3,108,NULL,'gm/psCommand','',0),(199,'设置开服时间',3,108,NULL,'gm/opstime','',0),(200,'有奖问答统计',3,99,NULL,'questionnaire/index','',0),(201,'充值日志',3,18,NULL,'log/getPage?logType=28','',0),(202,'改名日志',3,18,NULL,'log/getPage?logType=29','',0),(203,'首领死亡复活日志',3,18,NULL,'log/getPage?logType=30','',0),(204,'物品变化日志',3,18,NULL,'log/getPage?logType=3','',0),(205,'超级邮件发送',3,32,NULL,'mail/sendSuperMail','',0),(206,'修改属性',3,12,NULL,'roleattr/setAttr','',0),(207,'角色转移',3,12,NULL,'transfer','',0),(208,'商城配置',3,12,NULL,'shop','',0),(209,'游戏库列表',3,108,NULL,'dblog/game','',0),(210,'货币变化日志',3,18,NULL,'log/getPage?logType=31','',0),(211,'禁言替换字',3,25,NULL,'forbidden/chatreplaceword','',0),(212,'聊天黑名单',3,25,NULL,'forbidden/chatblacklist','',0),(213,'更新公告',3,24,'','announce/updateNotice','',0),(214,'活跃活动',3,43,'','activity/getPage?type=1','',0),(215,'每日充值',3,43,'','activity/getPage?type=2','',0),(216,'每日登陆',3,43,'','activity/getPage?type=3','',0),(217,'限购礼包',3,43,'','activity/getPage?type=4','',0),(218,'天帝宝库',3,43,'','activity/getPage?type=5','',0),(219,'累计充值',3,43,'','activity/getPage?type=6','',0),(220,'累计消耗',3,43,'','activity/getPage?type=7','',0),(221,'集物兑换',3,43,'','activity/getPage?type=8','',0),(222,'团购活动',3,43,'','activity/getPage?type=9','',0),(223,'招 财 猫',3,43,'','activity/getPage?type=10','',0),(224,'评价开关',3,12,'','evaluate/setEvaluate','',0),(227,'首领狂欢',3,43,NULL,'activity/getPage?type=11',NULL,0),(229,'庆典任务',3,43,NULL,'activity/getPage?type=12',NULL,0),(231,'节日集字',3,43,NULL,'activity/getPage?type=13',NULL,0),(232,'节日特惠',3,43,NULL,'activity/getPage?type=14',NULL,0),(233,'连续累充',3,43,NULL,'activity/getPage?type=15',NULL,0),(236,'限时商城',3,43,NULL,'activity/getPage?type=16',NULL,0),(237,'节日礼包',3,43,NULL,'activity/getPage?type=17',NULL,0),(238,'积分排名',3,43,NULL,'activity/getPage?type=18',NULL,0),(239,'节日许愿',3,43,NULL,'activity/getPage?type=19',NULL,0),(240,'FB分享',3,43,NULL,'activity/getPage?type=20',NULL,0),(242,'连续累充2(购买礼包)',3,43,NULL,'activity/getPage?type=21',NULL,0),(243,'节日祝福',3,43,NULL,'activity/getPage?type=22',NULL,0),(244,'掷骰子',3,43,NULL,'activity/getPage?type=23',NULL,0);


/*2.  `t_activity_festival_type`; 活动类型表 */
/*Table structure for table `t_activity_festival_type` */
DROP TABLE IF EXISTS `t_activity_festival_type`;

CREATE TABLE `t_activity_festival_type` (
  `id` int(32) DEFAULT NULL COMMENT '活动类型配置ID',
  `name` varchar(128) DEFAULT NULL COMMENT '活动类型名字'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_activity_festival_type` */
INSERT  INTO `t_activity_festival_type`(`id`,`name`) VALUES (0,'普通活动'),(1,'元旦'),(2,'情人节'),(3,'妇女节'),(4,'愚人节'),(5,'劳动节'),(6,'儿童节'),(7,'教师节'),(8,'圣诞节'),(9,'新年'),(10,'元宵节'),(11,'清明节'),(12,'端午节'),(13,'七夕'),(14,'中秋节'),(15,'重阳节'),(16,'腊八节'),(17,'除夕');

