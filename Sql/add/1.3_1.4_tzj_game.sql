
/*be changed list*/
/* 
//1.3.0_2020.12.28_0.21.1130_main:


//1.4.0_2021.03.29_0.22.0331_main

*/

/*The following statements need to be executed*/
/*1.  "TableName1" "COMMENT1" */

/*2.  "TableName2" "COMMENT2" */

/*3.  "TableName3" "COMMENT3" */

ALTER TABLE rankplayer ADD COLUMN consumeGold int(11)	DEFAULT '0' COMMENT '消费排行';

ALTER TABLE auction ADD COLUMN `password` varchar(45) DEFAULT null COMMENT '竞拍密码' AFTER auctionGuild;

ALTER TABLE `mail` ADD COLUMN `source` INT NOT NULL DEFAULT 0 COMMENT '来源' AFTER `mailData`;