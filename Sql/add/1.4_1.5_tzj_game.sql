
/*be changed list*/
/* 
//1.4.0_2021.03.29_0.22.0331_main:


//1.5.0_2021.04.28_0.23.0510_main:

*/

/*The following statements need to be executed*/
/*1.  "TableName1" "COMMENT1" */

/*2.  "TableName2" "COMMENT2" */

/*3.  "TableName3" "COMMENT3" */

ALTER TABLE playerworldinfo ADD COLUMN soulArmorId	int(10)		DEFAULT '0' COMMENT '魂甲品质';

ALTER TABLE playerworldinfo ADD COLUMN fashionHeadId	int(10)		DEFAULT '0' COMMENT '时装头像';

ALTER TABLE playerworldinfo ADD COLUMN fashionHeadFrameId	int(10)		DEFAULT '0' COMMENT '时装头像框';

ALTER TABLE activityconfig ADD COLUMN startRecordTime	bigint(20)		DEFAULT '0' COMMENT '活动开始记录时间';

ALTER TABLE activityconfig ADD COLUMN endRecordTime	bigint(20)		DEFAULT '0' COMMENT '活动结束记录时间';

ALTER TABLE recharge ADD COLUMN  notify_time varchar(255)        DEFAULT '' COMMENT '异步通知发货时间';

ALTER TABLE recharge ADD COLUMN  notify_id   varchar(255)            DEFAULT ''  COMMENT '异步通知ID';

ALTER TABLE recharge ADD COLUMN  trade_no varchar(255)           DEFAULT '' COMMENT '第三方支付订单';

ALTER TABLE recharge ADD COLUMN  trade_status int(11)            DEFAULT 0 COMMENT '支付成功,目前就只有此类型';



