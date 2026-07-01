
/*be changed list*/
/* 
//1.7.0_2021.07.22_0.25.0730_main:


//1.8.0_2021.11.09_0.26.1110_main:


*/

/*The following statements need to be executed*/
/*1.   "TableName1" "COMMENT1" */


/*2.  "TableName2" "COMMENT2" */


/*3.  "TableName3" "COMMENT3" */


-- ----------------------------
-- 3.Table structure for `serverparam`
-- ----------------------------
CREATE TABLE `serverparam` (
  `paramkey` varchar(64) NOT NULL,
  `serverid` int(11) DEFAULT NULL,
  `paramvalue` longtext NOT NULL,
  PRIMARY KEY (`paramkey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '服务器参数表';