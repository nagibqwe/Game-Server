
/*be changed list*/
/* 
//1.6.0_2021.05.27_0.24.0530_main:


//1.7.0_2021.07.22_0.25.0730_main:

*/

/*The following statements need to be executed*/
/*1.  "TableName1" "COMMENT1" */

/*2.  "TableName2" "COMMENT2" */

/*3.  "TableName3" "COMMENT3" */


ALTER TABLE `rankplayer` 
ADD COLUMN `baguaPower` INT(11) NULL DEFAULT 0 COMMENT '八卦战斗力' AFTER `consumeGold`,
ADD COLUMN `immortalsoulPower` INT(11) NULL DEFAULT 0 COMMENT '灵魂战力' AFTER `baguaPower`,
ADD COLUMN `devilSoulPower` INT(11) NULL DEFAULT 0 COMMENT '魔魂战力' AFTER `immortalsoulPower`,
ADD COLUMN `horseEquipPower` INT(11) NULL DEFAULT 0 COMMENT '坐骑脉轮（装备）战力' AFTER `devilSoulPower`,
ADD COLUMN `intimacy` int(11) NULL DEFAULT 0 COMMENT '亲密度' AFTER `horseEquipPower`;

ALTER TABLE `friend` 
ADD COLUMN `approvalList` longtext COMMENT '审批列表' AFTER `receiveLogs`,
ADD COLUMN `shieldAddFriend` longtext COMMENT '屏蔽好友申请列表' AFTER `approvalList`;

ALTER TABLE `activityconfig` 
ADD COLUMN `isOpenServer` TINYINT(4) DEFAULT 0 NOT NULL COMMENT '是否为开服活动' AFTER `endRecordTime`; 
 
