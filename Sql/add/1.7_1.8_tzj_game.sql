
/*be changed list*/
/* 
//1.7.0_2021.07.22_0.25.0730_main:


//1.8.0_2021.11.09_0.26.1110_main:

*/

/*The following statements need to be executed*/
/*1.  "TableName1" "COMMENT1" */

/*2.  "TableName2" "COMMENT2" */

/*3.  "TableName3" "COMMENT3" */
 
ALTER TABLE `playerworldinfo` 
ADD COLUMN `customHeadPath` varchar(255) COMMENT '自定义头像路径' AFTER `fashionHeadFrameId`,
ADD COLUMN `useCustomHead` tinyint(4)  DEFAULT '0' COMMENT '是否使用自定义头像' AFTER `customHeadPath`;

ALTER TABLE `activityconfig` 
ADD COLUMN `isOpenServer` TINYINT(4) DEFAULT 0 NOT NULL COMMENT '是否为开服活动' AFTER `endRecordTime`; 


ALTER TABLE `recharge`
ADD COLUMN `totalRecharge` int(11) DEFAULT 0  COMMENT '计算到游戏累充值' AFTER `trade_status`,
ADD COLUMN `totalVipPower` int(11) DEFAULT 0  COMMENT 'vip经验加成' AFTER `totalRecharge`;

 