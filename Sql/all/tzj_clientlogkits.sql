-- ------------------------------------------
-- Database: tzj_clientlogkits
-- Usage: Create all tables for the database
-- ------------------------------------------
-- Modification records:
-- ==============2021/08/20 13:55==============
-- Desc: Change default charset from utf8 to utf8mb4 , collate use utf8mb4_unicode_ci;
-- ============================================


-- ==============Script Switch Setting=========================
-- Disable foreign keys
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
-- Set SQL mode
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
-- ============================================



-- ================框架Quartz任务调度相关的表============================

-- ----------------------------
-- Quartz::Drop tables
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
DROP TABLE IF EXISTS `qrtz_triggers`;
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
DROP TABLE IF EXISTS `qrtz_calendars`;
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
DROP TABLE IF EXISTS `qrtz_locks`;
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;

-- ----------------------------
-- 1.Table structure for table `qrtz_job_details`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
  `sched_name` varchar(120) NOT NULL,
  `job_name` varchar(190) NOT NULL,
  `job_group` varchar(190) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `job_class_name` varchar(250) NOT NULL,
  `is_durable` varchar(1) NOT NULL,
  `is_nonconcurrent` varchar(1) NOT NULL,
  `is_update_data` varchar(1) NOT NULL,
  `requests_recovery` varchar(1) NOT NULL,
  `job_data` blob,
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '框架Quartz定时任务:存放一个jobDetail信息';


-- ----------------------------
-- 2. Table structure for table `qrtz_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(190) NOT NULL,
  `trigger_group` varchar(190) NOT NULL,
  `job_name` varchar(190) NOT NULL,
  `job_group` varchar(190) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `next_fire_time` bigint(13) DEFAULT NULL,
  `prev_fire_time` bigint(13) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `trigger_state` varchar(16) NOT NULL,
  `trigger_type` varchar(8) NOT NULL,
  `start_time` bigint(13) NOT NULL,
  `end_time` bigint(13) DEFAULT NULL,
  `calendar_name` varchar(200) DEFAULT NULL,
  `misfire_instr` smallint(2) DEFAULT NULL,
  `job_data` blob,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`),
  CONSTRAINT qrtz_triggers_ibfk_1 FOREIGN KEY (sched_name, job_name, job_group) REFERENCES qrtz_job_details (sched_name, job_name, job_group)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '框架Quartz定时任务:触发器的基本信息';


-- ----------------------------
-- 3.Table structure for table `qrtz_blob_triggers` 
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(190) NOT NULL,
  `trigger_group` varchar(190) NOT NULL,
  `blob_data` blob,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '框架Quartz定时任务:以Blob类型存储的触发器';


-- ----------------------------
-- 4.Table structure for table `qrtz_calendars` */
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
  `sched_name` varchar(120) NOT NULL,
  `calendar_name` varchar(190) NOT NULL,
  `calendar` blob NOT NULL,
  PRIMARY KEY (`sched_name`,`calendar_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '框架Quartz定时任务:存放日历信息,可配置一个日历来指定一个时间范围';


-- ----------------------------
-- 5.Table structure for table `qrtz_cron_triggers` */
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(190) NOT NULL,
  `trigger_group` varchar(190) NOT NULL,
  `cron_expression` varchar(190) NOT NULL,
  `time_zone_id` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '框架Quartz定时任务:存放cron类型的触发器';

-- ----------------------------
-- 6.Table structure for table `qrtz_fired_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `entry_id` varchar(95) NOT NULL,
  `trigger_name` varchar(190) NOT NULL,
  `trigger_group` varchar(190) NOT NULL,
  `instance_name` varchar(190) NOT NULL,
  `fired_time` bigint(13) NOT NULL,
  `sched_time` bigint(13) NOT NULL,
  `priority` int(11) NOT NULL,
  `state` varchar(16) NOT NULL,
  `job_name` varchar(200) DEFAULT NULL,
  `job_group` varchar(200) DEFAULT NULL,
  `is_nonconcurrent` varchar(1) DEFAULT NULL,
  `requests_recovery` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '框架Quartz定时任务:存放已触发的触发器';


-- ----------------------------
-- 7.Table structure for table `qrtz_locks` 
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
  `sched_name` varchar(120) NOT NULL,
  `lock_name` varchar(40) NOT NULL,
  PRIMARY KEY (`sched_name`,`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '框架Quartz定时任务:存储程序的悲观锁的信息(假如使用了悲观锁)';

-- ----------------------------
-- 8.Table structure for table `qrtz_paused_trigger_grps` 
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_group` varchar(190) NOT NULL,
  PRIMARY KEY (`sched_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '框架Quartz定时任务:存放暂停掉的触发器';


-- ----------------------------
-- 9.Table structure for table `qrtz_scheduler_state`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
  `sched_name` varchar(120) NOT NULL,
  `instance_name` varchar(190) NOT NULL,
  `last_checkin_time` bigint(13) NOT NULL,
  `checkin_interval` bigint(13) NOT NULL,
  PRIMARY KEY (`sched_name`,`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '框架Quartz定时任务:调度器状态';


-- ----------------------------
-- 10.Table structure for table `qrtz_simple_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(190) NOT NULL,
  `trigger_group` varchar(190) NOT NULL,
  `repeat_count` bigint(7) NOT NULL,
  `repeat_interval` bigint(12) NOT NULL,
  `times_triggered` bigint(10) NOT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '框架Quartz定时任务:简单触发器的信息';

-- ----------------------------
-- 11.Table structure for table `qrtz_simprop_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(190) NOT NULL,
  `trigger_group` varchar(190) NOT NULL,
  `str_prop_1` varchar(512) DEFAULT NULL,
  `str_prop_2` varchar(512) DEFAULT NULL,
  `str_prop_3` varchar(512) DEFAULT NULL,
  `int_prop_1` int(11) DEFAULT NULL,
  `int_prop_2` int(11) DEFAULT NULL,
  `long_prop_1` bigint(20) DEFAULT NULL,
  `long_prop_2` bigint(20) DEFAULT NULL,
  `dec_prop_1` decimal(13,4) DEFAULT NULL,
  `dec_prop_2` decimal(13,4) DEFAULT NULL,
  `bool_prop_1` varchar(1) DEFAULT NULL,
  `bool_prop_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '框架Quartz定时任务:存储CalendarIntervalTrigger和DailyTimeIntervalTrigger两种类型的触发器';


-- ====================================================================


-- ================框架基础系统相关的表============================
-- ----------------------------
-- 12.Table structure for table `sys_config` 
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `config_id` int(5) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:参数配置表';


-- ----------------------------
-- 13.Table structure for table `sys_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:部门表';


-- ----------------------------
-- 14.Table structure for table `sys_dict_data`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(4) DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:字典数据表';


-- ----------------------------
-- 15.Table structure for table `sys_dict_type`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE KEY `dict_type` (`dict_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:字典类型表';


-- ----------------------------
-- 16.Table structure for table `sys_job` 
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:定时任务调度表';

-- ----------------------------
-- 17.Table structure for table `sys_job_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log` (
  `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) DEFAULT NULL COMMENT '日志信息',
  `status` char(1) DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:定时任务调度日志表';


-- ----------------------------
-- 18.Table structure for table `sys_logininfor`
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor` (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `login_name` varchar(50) DEFAULT '' COMMENT '登录账号',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:系统访问记录';

-- ----------------------------
-- 19.Table structure for table `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `url` varchar(200) DEFAULT '#' COMMENT '请求地址',
  `target` varchar(20) DEFAULT '' COMMENT '打开方式（menuItem页签 menuBlank新窗口）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `is_refresh` char(1) DEFAULT '1' COMMENT '是否刷新（0刷新 1不刷新）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:菜单权限表';


-- ----------------------------
-- 20.Table structure for table `sys_oper_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:操作日志记录';

-- ----------------------------
-- 21.Table structure for table `sys_post`
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:岗位信息表';

-- ----------------------------
-- 22.Table structure for table `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:角色信息表';


-- ----------------------------
-- 23.Table structure for table `sys_role_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:角色和部门关联表';


-- ----------------------------
-- 24.Table structure for table `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:角色和菜单关联表';

-- ----------------------------
-- 25.Table structure for table `sys_user` 
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `login_name` varchar(30) NOT NULL COMMENT '登录账号',
  `user_name` varchar(30) DEFAULT '' COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户 01注册用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像路径',
  `password` varchar(50) DEFAULT '' COMMENT '密码',
  `salt` varchar(20) DEFAULT '' COMMENT '盐加密',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `pwd_update_date` datetime DEFAULT NULL COMMENT '密码最后更新时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:用户信息表';

-- ----------------------------
-- 26.Table structure for table `sys_user_online`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_online`;
CREATE TABLE `sys_user_online` (
  `sessionId` varchar(50) NOT NULL DEFAULT '' COMMENT '用户会话id',
  `login_name` varchar(50) DEFAULT '' COMMENT '登录账号',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` varchar(10) DEFAULT '' COMMENT '在线状态on_line在线off_line离线',
  `start_timestamp` datetime DEFAULT NULL COMMENT 'session创建时间',
  `last_access_time` datetime DEFAULT NULL COMMENT 'session最后访问时间',
  `expire_time` int(5) DEFAULT '0' COMMENT '超时时间，单位为分钟',
  PRIMARY KEY (`sessionId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:在线用户记录';

-- ----------------------------
-- 27.Table structure for table `sys_user_post`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:用户与岗位关联表';

-- ----------------------------
-- 28.Table structure for table `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:用户和角色关联表';

-- ====================================================================


-- ================具体业务逻辑相关的表============================
-- ----------------------------
-- 29.Table structure for table `t_clientlog`
-- ----------------------------
DROP TABLE IF EXISTS `t_clientlog`;
CREATE TABLE `t_clientlog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(50) NOT NULL COMMENT '设备ID',
  `miei` char(50) DEFAULT NULL COMMENT '设备MIEI',
  `idfa` char(50) DEFAULT NULL COMMENT '设备IDFA',
  `game` char(100) NOT NULL COMMENT '游戏包',
  `version` char(40) DEFAULT NULL COMMENT '版本',
  `playerinfo` longtext COMMENT '玩家信息',
  `mem_used` char(40) DEFAULT NULL COMMENT '已使用内存',
  `mem_free` char(40) DEFAULT NULL COMMENT '未使用内存',
  `time` char(40) DEFAULT NULL COMMENT '最后一次上传时间',
  `filemd5` char(50) DEFAULT NULL COMMENT '文件md5',
  PRIMARY KEY (`uuid`,`game`),
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台逻辑:客户端日志表';

-- ====================================================================



-- =====================================================
-- Initialize database data
-- =====================================================


-- ----------------------------
-- 1.Data for the table `sys_config`
-- ----------------------------
insert  into `sys_config`(`config_id`,`config_name`,`config_key`,`config_value`,`config_type`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2021-04-22 19:32:06','',NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),
(2,'用户管理-账号初始密码','sys.user.initPassword','123456','Y','admin','2021-04-22 19:32:06','',NULL,'初始化密码 123456'),
(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y','admin','2021-04-22 19:32:06','',NULL,'深黑主题theme-dark，浅色主题theme-light，深蓝主题theme-blue'),
(4,'账号自助-是否开启用户注册功能','sys.account.registerUser','false','Y','admin','2021-04-22 19:32:06','',NULL,'是否开启注册用户功能（true开启，false关闭）'),
(5,'用户管理-密码字符范围','sys.account.chrtype','0','Y','admin','2021-04-22 19:32:07','',NULL,'默认任意字符范围，0任意（密码可以输入任意字符），1数字（密码只能为0-9数字），2英文字母（密码只能为a-z和A-Z字母），3字母和数字（密码必须包含字母，数字）,4字母数字和特殊字符（目前支持的特殊字符包括：~!@#$%^&*()-=_+）'),
(6,'用户管理-初始密码修改策略','sys.account.initPasswordModify','0','Y','admin','2021-04-22 19:32:07','',NULL,'0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框'),
(7,'用户管理-账号密码更新周期','sys.account.passwordValidateDays','0','Y','admin','2021-04-22 19:32:07','',NULL,'密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框'),
(8,'主框架页-菜单导航显示风格','sys.index.menuStyle','topnav','Y','admin','2021-04-22 19:32:07','',NULL,'菜单导航显示风格（default为左侧导航菜单，topnav为顶部导航菜单）'),
(9,'主框架页-是否开启页脚','sys.index.ignoreFooter','true','Y','admin','2021-04-22 19:32:07','',NULL,'是否开启底部页脚显示（true显示，false隐藏）');


-- ----------------------------
-- 2.Data for the table `sys_dict_data`
-- ----------------------------
insert  into `sys_dict_data`(`dict_code`,`dict_sort`,`dict_label`,`dict_value`,`dict_type`,`css_class`,`list_class`,`is_default`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,1,'男','0','sys_user_sex','','','Y','0','admin','2021-04-22 19:32:02','',NULL,'性别男'),
(2,2,'女','1','sys_user_sex','','','N','0','admin','2021-04-22 19:32:02','',NULL,'性别女'),
(3,3,'未知','2','sys_user_sex','','','N','0','admin','2021-04-22 19:32:02','',NULL,'性别未知'),
(4,1,'显示','0','sys_show_hide','','primary','Y','0','admin','2021-04-22 19:32:02','',NULL,'显示菜单'),
(5,2,'隐藏','1','sys_show_hide','','danger','N','0','admin','2021-04-22 19:32:02','',NULL,'隐藏菜单'),
(6,1,'正常','0','sys_normal_disable','','primary','Y','0','admin','2021-04-22 19:32:02','',NULL,'正常状态'),
(7,2,'停用','1','sys_normal_disable','','danger','N','0','admin','2021-04-22 19:32:03','',NULL,'停用状态'),
(8,1,'正常','0','sys_job_status','','primary','Y','0','admin','2021-04-22 19:32:03','',NULL,'正常状态'),
(9,2,'暂停','1','sys_job_status','','danger','N','0','admin','2021-04-22 19:32:03','',NULL,'停用状态'),
(10,1,'默认','DEFAULT','sys_job_group','','','Y','0','admin','2021-04-22 19:32:03','',NULL,'默认分组'),
(11,2,'系统','SYSTEM','sys_job_group','','','N','0','admin','2021-04-22 19:32:03','',NULL,'系统分组'),
(12,1,'是','Y','sys_yes_no','','primary','Y','0','admin','2021-04-22 19:32:03','',NULL,'系统默认是'),
(13,2,'否','N','sys_yes_no','','danger','N','0','admin','2021-04-22 19:32:03','',NULL,'系统默认否'),
(14,1,'通知','1','sys_notice_type','','warning','Y','0','admin','2021-04-22 19:32:03','',NULL,'通知'),
(15,2,'公告','2','sys_notice_type','','success','N','0','admin','2021-04-22 19:32:03','',NULL,'公告'),
(16,1,'正常','0','sys_notice_status','','primary','Y','0','admin','2021-04-22 19:32:03','',NULL,'正常状态'),
(17,2,'关闭','1','sys_notice_status','','danger','N','0','admin','2021-04-22 19:32:03','',NULL,'关闭状态'),
(18,99,'其他','0','sys_oper_type','','info','N','0','admin','2021-04-22 19:32:03','',NULL,'其他操作'),
(19,1,'新增','1','sys_oper_type','','info','N','0','admin','2021-04-22 19:32:03','',NULL,'新增操作'),
(20,2,'修改','2','sys_oper_type','','info','N','0','admin','2021-04-22 19:32:03','',NULL,'修改操作'),
(21,3,'删除','3','sys_oper_type','','danger','N','0','admin','2021-04-22 19:32:03','',NULL,'删除操作'),
(22,4,'授权','4','sys_oper_type','','primary','N','0','admin','2021-04-22 19:32:03','',NULL,'授权操作'),
(23,5,'导出','5','sys_oper_type','','warning','N','0','admin','2021-04-22 19:32:04','',NULL,'导出操作'),
(24,6,'导入','6','sys_oper_type','','warning','N','0','admin','2021-04-22 19:32:04','',NULL,'导入操作'),
(25,7,'强退','7','sys_oper_type','','danger','N','0','admin','2021-04-22 19:32:04','',NULL,'强退操作'),
(26,8,'生成代码','8','sys_oper_type','','warning','N','0','admin','2021-04-22 19:32:04','',NULL,'生成操作'),
(27,9,'清空数据','9','sys_oper_type','','danger','N','0','admin','2021-04-22 19:32:04','',NULL,'清空操作'),
(28,1,'成功','0','sys_common_status','','primary','N','0','admin','2021-04-22 19:32:04','',NULL,'正常状态'),
(29,2,'失败','1','sys_common_status','','danger','N','0','admin','2021-04-22 19:32:04','',NULL,'停用状态');

-- ----------------------------
-- 3.Data for the table `sys_dict_type`
-- ----------------------------
insert  into `sys_dict_type`(`dict_id`,`dict_name`,`dict_type`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'用户性别','sys_user_sex','0','admin','2021-04-22 19:31:59','',NULL,'用户性别列表'),
(2,'菜单状态','sys_show_hide','0','admin','2021-04-22 19:31:59','',NULL,'菜单状态列表'),
(3,'系统开关','sys_normal_disable','0','admin','2021-04-22 19:31:59','',NULL,'系统开关列表'),
(4,'任务状态','sys_job_status','0','admin','2021-04-22 19:31:59','',NULL,'任务状态列表'),
(5,'任务分组','sys_job_group','0','admin','2021-04-22 19:31:59','',NULL,'任务分组列表'),
(6,'系统是否','sys_yes_no','0','admin','2021-04-22 19:32:00','',NULL,'系统是否列表'),
(7,'通知类型','sys_notice_type','0','admin','2021-04-22 19:32:00','',NULL,'通知类型列表'),
(8,'通知状态','sys_notice_status','0','admin','2021-04-22 19:32:00','',NULL,'通知状态列表'),
(9,'操作类型','sys_oper_type','0','admin','2021-04-22 19:32:00','',NULL,'操作类型列表'),
(10,'系统状态','sys_common_status','0','admin','2021-04-22 19:32:00','',NULL,'登录状态列表');

-- ----------------------------
-- 4.Data for the table `sys_menu`
-- ----------------------------
insert  into `sys_menu`(`menu_id`,`menu_name`,`parent_id`,`order_num`,`url`,`target`,`menu_type`,`visible`,`is_refresh`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'系统管理',0,2,'#','','M','0','1','','fa fa-gear','admin','2021-04-22 19:31:36','',NULL,'系统管理目录'),
(2,'系统监控',0,3,'#','','M','0','1','','fa fa-video-camera','admin','2021-04-22 19:31:36','',NULL,'系统监控目录'),
(100,'用户管理',1,1,'/system/user','','C','0','1','system:user:view','fa fa-user-o','admin','2021-04-22 19:31:37','',NULL,'用户管理菜单'),
(101,'角色管理',1,2,'/system/role','','C','0','1','system:role:view','fa fa-user-secret','admin','2021-04-22 19:31:37','',NULL,'角色管理菜单'),
(102,'菜单管理',1,3,'/system/menu','','C','0','1','system:menu:view','fa fa-th-list','admin','2021-04-22 19:31:37','',NULL,'菜单管理菜单'),
(103,'部门管理',1,4,'/system/dept','','C','0','1','system:dept:view','fa fa-outdent','admin','2021-04-22 19:31:37','',NULL,'部门管理菜单'),
(104,'岗位管理',1,5,'/system/post','','C','0','1','system:post:view','fa fa-address-card-o','admin','2021-04-22 19:31:37','',NULL,'岗位管理菜单'),
(105,'字典管理',1,6,'/system/dict','','C','0','1','system:dict:view','fa fa-bookmark-o','admin','2021-04-22 19:31:37','',NULL,'字典管理菜单'),
(106,'参数设置',1,7,'/system/config','','C','0','1','system:config:view','fa fa-sun-o','admin','2021-04-22 19:31:37','',NULL,'参数设置菜单'),
(108,'日志管理',1,9,'#','','M','0','1','','fa fa-pencil-square-o','admin','2021-04-22 19:31:37','',NULL,'日志管理菜单'),
(109,'在线用户',2,1,'/monitor/online','','C','0','1','monitor:online:view','fa fa-user-circle','admin','2021-04-22 19:31:37','',NULL,'在线用户菜单'),
(110,'定时任务',2,2,'/monitor/job','','C','0','1','monitor:job:view','fa fa-tasks','admin','2021-04-22 19:31:37','',NULL,'定时任务菜单'),
(111,'数据监控',2,3,'/monitor/data','','C','0','1','monitor:data:view','fa fa-bug','admin','2021-04-22 19:31:37','',NULL,'数据监控菜单'),
(112,'服务监控',2,4,'/monitor/server','','C','0','1','monitor:server:view','fa fa-server','admin','2021-04-22 19:31:37','',NULL,'服务监控菜单'),
(113,'缓存监控',2,5,'/monitor/cache','','C','0','1','monitor:cache:view','fa fa-cube','admin','2021-04-22 19:31:37','',NULL,'缓存监控菜单'),
(500,'操作日志',108,1,'/monitor/operlog','','C','0','1','monitor:operlog:view','fa fa-address-book','admin','2021-04-22 19:31:37','',NULL,'操作日志菜单'),
(501,'登录日志',108,2,'/monitor/logininfor','','C','0','1','monitor:logininfor:view','fa fa-file-image-o','admin','2021-04-22 19:31:38','',NULL,'登录日志菜单'),
(1000,'用户查询',100,1,'#','','F','0','1','system:user:list','#','admin','2021-04-22 19:31:38','',NULL,''),
(1001,'用户新增',100,2,'#','','F','0','1','system:user:add','#','admin','2021-04-22 19:31:38','',NULL,''),
(1002,'用户修改',100,3,'#','','F','0','1','system:user:edit','#','admin','2021-04-22 19:31:38','',NULL,''),
(1003,'用户删除',100,4,'#','','F','0','1','system:user:remove','#','admin','2021-04-22 19:31:38','',NULL,''),
(1004,'用户导出',100,5,'#','','F','0','1','system:user:export','#','admin','2021-04-22 19:31:38','',NULL,''),
(1005,'用户导入',100,6,'#','','F','0','1','system:user:import','#','admin','2021-04-22 19:31:38','',NULL,''),
(1006,'重置密码',100,7,'#','','F','0','1','system:user:resetPwd','#','admin','2021-04-22 19:31:38','',NULL,''),
(1007,'角色查询',101,1,'#','','F','0','1','system:role:list','#','admin','2021-04-22 19:31:38','',NULL,''),
(1008,'角色新增',101,2,'#','','F','0','1','system:role:add','#','admin','2021-04-22 19:31:38','',NULL,''),
(1009,'角色修改',101,3,'#','','F','0','1','system:role:edit','#','admin','2021-04-22 19:31:38','',NULL,''),
(1010,'角色删除',101,4,'#','','F','0','1','system:role:remove','#','admin','2021-04-22 19:31:38','',NULL,''),
(1011,'角色导出',101,5,'#','','F','0','1','system:role:export','#','admin','2021-04-22 19:31:38','',NULL,''),
(1012,'菜单查询',102,1,'#','','F','0','1','system:menu:list','#','admin','2021-04-22 19:31:38','',NULL,''),
(1013,'菜单新增',102,2,'#','','F','0','1','system:menu:add','#','admin','2021-04-22 19:31:38','',NULL,''),
(1014,'菜单修改',102,3,'#','','F','0','1','system:menu:edit','#','admin','2021-04-22 19:31:38','',NULL,''),
(1015,'菜单删除',102,4,'#','','F','0','1','system:menu:remove','#','admin','2021-04-22 19:31:38','',NULL,''),
(1016,'部门查询',103,1,'#','','F','0','1','system:dept:list','#','admin','2021-04-22 19:31:38','',NULL,''),
(1017,'部门新增',103,2,'#','','F','0','1','system:dept:add','#','admin','2021-04-22 19:31:38','',NULL,''),
(1018,'部门修改',103,3,'#','','F','0','1','system:dept:edit','#','admin','2021-04-22 19:31:39','',NULL,''),
(1019,'部门删除',103,4,'#','','F','0','1','system:dept:remove','#','admin','2021-04-22 19:31:39','',NULL,''),
(1020,'岗位查询',104,1,'#','','F','0','1','system:post:list','#','admin','2021-04-22 19:31:39','',NULL,''),
(1021,'岗位新增',104,2,'#','','F','0','1','system:post:add','#','admin','2021-04-22 19:31:39','',NULL,''),
(1022,'岗位修改',104,3,'#','','F','0','1','system:post:edit','#','admin','2021-04-22 19:31:39','',NULL,''),
(1023,'岗位删除',104,4,'#','','F','0','1','system:post:remove','#','admin','2021-04-22 19:31:39','',NULL,''),
(1024,'岗位导出',104,5,'#','','F','0','1','system:post:export','#','admin','2021-04-22 19:31:39','',NULL,''),
(1025,'字典查询',105,1,'#','','F','0','1','system:dict:list','#','admin','2021-04-22 19:31:39','',NULL,''),
(1026,'字典新增',105,2,'#','','F','0','1','system:dict:add','#','admin','2021-04-22 19:31:39','',NULL,''),
(1027,'字典修改',105,3,'#','','F','0','1','system:dict:edit','#','admin','2021-04-22 19:31:39','',NULL,''),
(1028,'字典删除',105,4,'#','','F','0','1','system:dict:remove','#','admin','2021-04-22 19:31:39','',NULL,''),
(1029,'字典导出',105,5,'#','','F','0','1','system:dict:export','#','admin','2021-04-22 19:31:39','',NULL,''),
(1030,'参数查询',106,1,'#','','F','0','1','system:config:list','#','admin','2021-04-22 19:31:39','',NULL,''),
(1031,'参数新增',106,2,'#','','F','0','1','system:config:add','#','admin','2021-04-22 19:31:39','',NULL,''),
(1032,'参数修改',106,3,'#','','F','0','1','system:config:edit','#','admin','2021-04-22 19:31:39','',NULL,''),
(1033,'参数删除',106,4,'#','','F','0','1','system:config:remove','#','admin','2021-04-22 19:31:39','',NULL,''),
(1034,'参数导出',106,5,'#','','F','0','1','system:config:export','#','admin','2021-04-22 19:31:39','',NULL,''),
(1039,'操作查询',500,1,'#','','F','0','1','monitor:operlog:list','#','admin','2021-04-22 19:31:40','',NULL,''),
(1040,'操作删除',500,2,'#','','F','0','1','monitor:operlog:remove','#','admin','2021-04-22 19:31:40','',NULL,''),
(1041,'详细信息',500,3,'#','','F','0','1','monitor:operlog:detail','#','admin','2021-04-22 19:31:40','',NULL,''),
(1042,'日志导出',500,4,'#','','F','0','1','monitor:operlog:export','#','admin','2021-04-22 19:31:40','',NULL,''),
(1043,'登录查询',501,1,'#','','F','0','1','monitor:logininfor:list','#','admin','2021-04-22 19:31:40','',NULL,''),
(1044,'登录删除',501,2,'#','','F','0','1','monitor:logininfor:remove','#','admin','2021-04-22 19:31:40','',NULL,''),
(1045,'日志导出',501,3,'#','','F','0','1','monitor:logininfor:export','#','admin','2021-04-22 19:31:40','',NULL,''),
(1046,'账户解锁',501,4,'#','','F','0','1','monitor:logininfor:unlock','#','admin','2021-04-22 19:31:40','',NULL,''),
(1047,'在线查询',109,1,'#','','F','0','1','monitor:online:list','#','admin','2021-04-22 19:31:40','',NULL,''),
(1048,'批量强退',109,2,'#','','F','0','1','monitor:online:batchForceLogout','#','admin','2021-04-22 19:31:40','',NULL,''),
(1049,'单条强退',109,3,'#','','F','0','1','monitor:online:forceLogout','#','admin','2021-04-22 19:31:40','',NULL,''),
(1050,'任务查询',110,1,'#','','F','0','1','monitor:job:list','#','admin','2021-04-22 19:31:40','',NULL,''),
(1051,'任务新增',110,2,'#','','F','0','1','monitor:job:add','#','admin','2021-04-22 19:31:40','',NULL,''),
(1052,'任务修改',110,3,'#','','F','0','1','monitor:job:edit','#','admin','2021-04-22 19:31:40','',NULL,''),
(1053,'任务删除',110,4,'#','','F','0','1','monitor:job:remove','#','admin','2021-04-22 19:31:40','',NULL,''),
(1054,'状态修改',110,5,'#','','F','0','1','monitor:job:changeStatus','#','admin','2021-04-22 19:31:41','',NULL,''),
(1055,'任务详细',110,6,'#','','F','0','1','monitor:job:detail','#','admin','2021-04-22 19:31:41','',NULL,''),
(1056,'任务导出',110,7,'#','','F','0','1','monitor:job:export','#','admin','2021-04-22 19:31:41','',NULL,''),
(2000,'客户端日志工具',0,1,'#','menuItem','M','0','1',NULL,'fa fa-wifi','admin','2021-04-23 13:46:10','',NULL,''),
(2046,'客户端日志',2000,1,'/clientlog/clientlogdata','','C','0','1','clientlog:clientlogdata:view','#','admin','2021-06-18 14:18:03','',NULL,'客户端日志菜单'),
(2047,'客户端日志查询',2046,1,'#','','F','0','1','clientlog:clientlogdata:list','#','admin','2021-06-18 14:18:03','',NULL,''),
(2050,'客户端日志删除',2046,4,'#','','F','0','1','clientlog:clientlogdata:remove','#','admin','2021-06-18 14:18:03','',NULL,''),
(2051,'客户端日志导出',2046,5,'#','','F','0','1','clientlog:clientlogdata:export','#','admin','2021-06-18 14:18:04','',NULL,''),
(2052,'客户端日志下载',2046,6,'#','','F','0','1','clientlog:clientlogdata:download','#','admin','2021-06-18 14:18:04','',NULL,'');




-- ----------------------------
-- 5.Data for the table `sys_dept`
-- ----------------------------
insert  into `sys_dept`(`dept_id`,`parent_id`,`ancestors`,`dept_name`,`order_num`,`leader`,`phone`,`email`,`status`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`) values 
(100,0,'0','后台应用',0,'千向','15888888888','zk@qq.com','0','0','admin','2021-04-22 19:31:26','',NULL),
(101,100,'0,100','行政部门',1,'千向','15888888888','xz@qq.com','0','0','admin','2021-04-22 19:31:26','',NULL),
(102,100,'0,100','运维部门',2,'千向','15888888888','yw@qq.com','0','0','admin','2021-04-22 19:31:26','',NULL),
(103,100,'0,100','运营部门',3,'千向','15888888888','yy@qq.com','0','0','admin','2021-04-22 19:31:26','',NULL),
(104,100,'0,100','设计部门',4,'千向','15888888888','sj@qq.com','0','0','admin','2021-04-22 19:31:26','',NULL),
(105,100,'0,100','研发部门',5,'千向','15888888888','yf@qq.com','0','0','admin','2021-04-22 19:31:26','',NULL),
(106,100,'0,100','质检部门',6,'千向','15888888888','zj@qq.com','0','0','admin','2021-04-22 19:31:26','',NULL);


-- ----------------------------
-- 6.Data for the table `sys_post`
-- ----------------------------
insert into `sys_post`(`post_id`,`post_code`,`post_name`,`post_sort`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'root','权限管理者',1,'0','admin','2021-04-22 19:31:31','',NULL,'');

-- ----------------------------
-- 7.Data for the table `sys_role`
-- ----------------------------
insert  into `sys_role`(`role_id`,`role_name`,`role_key`,`role_sort`,`data_scope`,`status`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'超级管理员','admin',1,'1','0','0','admin','2021-04-22 19:31:34','',NULL,'超级管理员');

-- ----------------------------
-- 8.Data for the table `sys_user`
-- ----------------------------
insert  into `sys_user`(`user_id`,`dept_id`,`login_name`,`user_name`,`user_type`,`email`,`phonenumber`,`sex`,`avatar`,`password`,`salt`,`status`,`del_flag`,`login_ip`,`login_date`,`pwd_update_date`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,100,'admin','千向','00','ry@163.com','15888888888','1','','29c67a30398638269fe600f73a054934','111111','0','0','182.150.165.20','2021-06-09 15:08:53','2021-04-22 19:31:29','admin','2021-04-22 19:31:29','','2021-06-09 15:08:52','管理员');


-- ----------------------------
-- 9.Data for the table `sys_user_post`
-- ----------------------------
insert  into `sys_user_post`(`user_id`,`post_id`) values 
(1,1);

-- ----------------------------
-- 10.Data for the table `sys_user_role`
-- ----------------------------
insert  into `sys_user_role`(`user_id`,`role_id`) values 
(1,1);



-- ==============Script Switch Reset=========================
-- Enable foreign keys
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
-- Restore previous SQL mode
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
-- ============================================