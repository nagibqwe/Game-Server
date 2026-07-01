
/*be changed list*/
/* 
//1.7.0_2021.07.22_0.25.0730_main:


//1.8.0_2021.11.09_0.26.1110_main:


*/

/*The following statements need to be executed*/
/*1.   "TableName1" "COMMENT1" */


/*2.  "TableName2" "COMMENT2" */


/*3.  "TableName3" "COMMENT3" */


CREATE TABLE `tag` (
  `id` INT(11) NOT NULL COMMENT '标签ID',
  `name` VARCHAR(255) DEFAULT NULL COMMENT '标签名',
  `icon` VARCHAR(500) DEFAULT NULL COMMENT '标签icon',
  `style` int(11) DEFAULT NULL COMMENT 'UI风格',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签库';

ALTER TABLE `rechargetotalmoney`
ADD COLUMN `userName` varchar(255) DEFAULT NULL COMMENT '553平台生成的账号名字' AFTER `rechargeTotalMoney`,
ADD COLUMN `platformAccount` varchar(255) DEFAULT NULL COMMENT '平台生成的账号' AFTER `userName`;

ALTER TABLE `rechargereturn`
ADD COLUMN `userName` varchar(255) DEFAULT NULL COMMENT '553平台生成的账号名字' AFTER `returnTime`,
ADD COLUMN `platformAccount` varchar(255) DEFAULT NULL COMMENT '平台生成的账号' AFTER `userName`;



