-- ------------------------------------------
-- Database: tzj_gm
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

-- ----------------------------
-- 29.Table structure for table `sys_notice`
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `notice_id` int(4) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` varchar(2000) DEFAULT NULL COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:通知公告表';


-- ----------------------------
-- 30.Table structure for table `gen_table`
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table` (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作 sub主子表操作）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:代码生成业务表';

-- ----------------------------
-- 31.Table structure for table `gen_table_column`
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;

CREATE TABLE `gen_table_column` (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` varchar(64) DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='框架基础系统:代码生成业务表字段';


-- ====================================================================

-- ================具体业务逻辑相关的表============================

-- ----------------------------
-- 32.Table structure for `t_server`
-- ----------------------------
DROP TABLE IF EXISTS `t_server`;
CREATE TABLE `t_server` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `serverName` varchar(128) NOT NULL DEFAULT '' COMMENT '服务器名称',
  `serverId` int(32) NOT NULL DEFAULT '0' COMMENT '服务器ID',
  `groupName` varchar(128) NOT NULL DEFAULT '' COMMENT '平台名',
  `serverIP` varchar(50) NOT NULL DEFAULT '' COMMENT '服务器IP',
  `serverPort` int(32) NOT NULL DEFAULT '0' COMMENT '服务器端口',
  `dblogIp` varchar(200) NOT NULL DEFAULT '' COMMENT '数据库IP',
  `dblogPort` int(11) NOT NULL DEFAULT '0' COMMENT '数据库Port',
  `dblogName` varchar(128) NOT NULL DEFAULT '' COMMENT '数据库名称',
  `dblogUser` varchar(128) NOT NULL DEFAULT '' COMMENT '数据库用户名',
  `dblogPwd` varchar(100) NOT NULL DEFAULT '' COMMENT '数据库密码',
  `serverIdList` varchar(200) NOT NULL DEFAULT '' COMMENT '合服列表',
  `isHeFu` tinyint(4) NOT NULL DEFAULT '0' COMMENT '合服标识',
  `hefuServerID` int(32) NOT NULL DEFAULT '0' COMMENT '合服目标服ID',
  `hefuTime` datetime DEFAULT NULL COMMENT '合服时间',
  `serverType` tinyint(4) NOT NULL DEFAULT '0' COMMENT '服务器类型 0:测试服 1:正式服 2:登录服 3:公共服 4:跨服',
  `isDeleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `isShow` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0为展示，1为不展示',
  `serverOpenTime` varchar(128) NOT NULL DEFAULT '' COMMENT '开服时间',
  `openState` int(32) NOT NULL DEFAULT '0' COMMENT '服务器状态 0:备服状态 1:开服状态',
  `heartTime` varchar(128) NOT NULL DEFAULT '' COMMENT '服务器最新心跳时间',
  `registerNum` int(11) NOT NULL DEFAULT '0' COMMENT '服务器注册人数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `serverId` (`serverId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='服务器列表';


-- ----------------------------
-- 33.Table structure for `t_cmd_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_cmd_log`;
CREATE TABLE `t_cmd_log` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `action` varchar(100) DEFAULT NULL COMMENT '操作命令',
  `params` varchar(500) DEFAULT NULL COMMENT '参数',
  `serverName` varchar(100) DEFAULT NULL COMMENT '服务器名',
  `serverId` int(32) DEFAULT NULL COMMENT '服务器ID',
  `isOk` tinyint(1) DEFAULT NULL COMMENT '操作结果',
  `result` text COMMENT '处理结果',
  `operDate` datetime DEFAULT NULL COMMENT '操作时间',
  `user` varchar(50) DEFAULT NULL COMMENT '用户',
  `ip` varchar(50) DEFAULT NULL COMMENT '操作者IP',
  `gmType` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'GM命令类型 0:游戏服GM(socket) 1:公共服或登录服GM(http)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='热更服务器操作日志';


-- ----------------------------
-- 34.Table structure for `t_recharge_item`
-- ----------------------------
DROP TABLE IF EXISTS `t_recharge_item`;
CREATE TABLE `t_recharge_item` (
  `goods_id` int(32) NOT NULL DEFAULT '0' COMMENT '充值ID',
  `goods_system_cfg_id` int(32) DEFAULT NULL COMMENT '游戏内部配置ID',
  `goods_name` varchar(128) DEFAULT NULL COMMENT '商品名字描述（主要用于BI后台数据）',
  `goods_pay_channel` varchar(128) DEFAULT NULL COMMENT '渠道名称',
  `goods_pay_type` int(11) DEFAULT '0' COMMENT '支付渠道（第三方支付）',
  `goods_type` int(32) DEFAULT NULL COMMENT '充值类型',
  `goods_subtype` int(32) DEFAULT NULL COMMENT '充值子类型',
  `goods_limit` int(32) DEFAULT NULL COMMENT '充值次数（当前轮每个挡位对应充值的次数)',
  `goods_icon` int(32) DEFAULT NULL COMMENT '显示的图标的ID',
  `goods_url` varchar(1000) DEFAULT '' COMMENT '商品图片地址',
  `goods_price` varchar(1000) DEFAULT '' COMMENT '充值档位对应消耗的真实货币',
  `goods_price_point` varchar(500) DEFAULT '' COMMENT '充值计费点',
  `goods_show_price` varchar(128) DEFAULT '' COMMENT '界面默认显示的货币 例如:THB',
  `goods_reward` varchar(500) DEFAULT '' COMMENT '充值奖励',
  `goods_multiple` varchar(128) DEFAULT NULL COMMENT '充值奖励倍数',
  `goods_extra_reward` varchar(500) DEFAULT '' COMMENT '额外奖励',
  `goods_extra_reward_limit` int(32) DEFAULT NULL COMMENT '额外奖励次数',
  `isTotalRecharge` int(11) DEFAULT '0' COMMENT '是否计入到游戏累充活动',
  `totalVipPower` int(11) DEFAULT '0' COMMENT '是否增加VIP经验',
  PRIMARY KEY (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值表数据';

-- ====================================================================

-- ----------------------------
-- 35.Table structure for `t_recharge_item_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_recharge_item_log`;
CREATE TABLE `t_recharge_item_log` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `userId` int(32) DEFAULT NULL COMMENT '修改人id',
  `ip` varchar(128) DEFAULT NULL COMMENT '修改人IP',
  `userName` varchar(128) DEFAULT NULL COMMENT '修改人名',
  `time` bigint(64) DEFAULT NULL COMMENT '修改时间 ',
  `tableName` varchar(128) DEFAULT NULL COMMENT '操作表名',
  `content` longtext COMMENT '操作内容(详情)',
  PRIMARY KEY (`id`),
  KEY `userId_index` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值表更改记录';

-- ----------------------------
-- 36.Table structure for `t_mail`
-- ----------------------------
DROP TABLE IF EXISTS `t_mail`;
CREATE TABLE `t_mail` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `groupName` varchar(128) DEFAULT NULL COMMENT '平台名字',
  `serverId` int(32) DEFAULT NULL COMMENT '服务器编号',
  `roleIds` text NOT NULL COMMENT '角色ID列表',
  `title` varchar(120) NOT NULL COMMENT '邮件标题',
  `content` text NOT NULL COMMENT '邮件内容',
  `items` varchar(500) DEFAULT NULL COMMENT '邮件附件物品列表',
  `reason` varchar(300) NOT NULL COMMENT '邮件发送理由',
  `createDate` varchar(128) DEFAULT NULL COMMENT '邮件创建时间',
  `createUser` varchar(128) DEFAULT NULL COMMENT '邮件创建的后台账号名',
  `adminUser` varchar(128) DEFAULT NULL COMMENT '邮件审核的后台账号名',
  `adminDate` varchar(128) DEFAULT NULL COMMENT '邮件审核的日期',
  `adminState` int(32) DEFAULT '0' COMMENT '审核是否通过',
  `sendState` int(32) DEFAULT '0' COMMENT '发送到游戏服的状态值',
  `sendErrorMess` varchar(300) DEFAULT NULL COMMENT '发送到服务返回的结果信息',
  `isDelete` int(32) DEFAULT '0' COMMENT '邮件的删除标志',
  `sended` int(32) DEFAULT '0' COMMENT '是否已经发送过',
  PRIMARY KEY (`id`),
  KEY `createDate_isdel` (`isDelete`,`createDate`),
  KEY `createUser` (`createUser`,`isDelete`,`createDate`),
  KEY `sended` (`sended`,`isDelete`),
  KEY `isDelete` (`isDelete`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮件';

-- ----------------------------
-- 37.Table structure for `t_mail_all`
-- ----------------------------
DROP TABLE IF EXISTS `t_mail_all`;
CREATE TABLE `t_mail_all` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `groupName` varchar(128) DEFAULT NULL COMMENT '平台名字',
  `minLv` int(32) DEFAULT NULL COMMENT '最小等级',
  `maxLv` int(32) DEFAULT NULL COMMENT '最大等级',
  `career` int(32) DEFAULT NULL COMMENT '职业',
  `serverIdList` varchar(128) DEFAULT NULL COMMENT '服务器列表',
  `title` varchar(120) NOT NULL COMMENT '邮件标题',
  `content` text NOT NULL COMMENT '邮件内容',
  `items` varchar(500) DEFAULT NULL COMMENT '邮件附件物品列表',
  `reason` varchar(300) NOT NULL COMMENT '邮件发送理由',
  `createDate` varchar(128) DEFAULT NULL COMMENT '邮件创建时间',
  `createUser` varchar(128) DEFAULT NULL COMMENT '邮件创建的后台账号名',
  `adminUser` varchar(128) DEFAULT NULL COMMENT '邮件审核的后台账号名',
  `adminDate` varchar(128) DEFAULT NULL COMMENT '邮件审核的日期',
  `adminState` int(32) DEFAULT '0' COMMENT '审核是否通过',
  `sendState` int(32) DEFAULT '0' COMMENT '发送到游戏服的状态值',
  `sendErrorMess` varchar(300) DEFAULT NULL COMMENT '发送到服务返回的结果信息',
  `isDelete` int(32) DEFAULT '0' COMMENT '邮件的删除标志',
  `sended` int(32) DEFAULT '0' COMMENT '是否已经发送过',
  PRIMARY KEY (`id`),
  KEY `createDate_isdel` (`isDelete`,`createDate`),
  KEY `createUser` (`createUser`,`isDelete`,`createDate`),
  KEY `sended` (`sended`,`isDelete`),
  KEY `isDelete` (`isDelete`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='全服邮件';

-- ----------------------------
-- 38.Table structure for `t_item`
-- ----------------------------
DROP TABLE IF EXISTS `t_item`;
CREATE TABLE `t_item` (
  `itemId` int(32) NOT NULL AUTO_INCREMENT COMMENT '物品Id',
  `itemName` varchar(128) DEFAULT NULL COMMENT '物品名',
  `itemType` int(32) DEFAULT NULL COMMENT '物品类型',
  `color` int(32) DEFAULT NULL COMMENT '物品颜色',
  PRIMARY KEY (`itemId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='道具表数据';

-- ----------------------------
-- 39.Table structure for `t_hefu`
-- ----------------------------
DROP TABLE IF EXISTS `t_hefu`;
CREATE TABLE `t_hefu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `fromServer` varchar(100) NOT NULL COMMENT '源服务器',
  `toServer` int(11) NOT NULL COMMENT '目标服务器',
  `status` int(255) NOT NULL COMMENT '合服状态0新建1合服中2成功3失败4取消',
  `language` varchar(20) NOT NULL COMMENT 'cn:国内，tw:台湾，kor:韩国，yn:越南，thai:泰国，ros:新马,en:英语',
  `step` int(4) NOT NULL COMMENT '当前进度',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `record` longtext COMMENT '数据量记录用于合服检查',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合服记录';

-- ----------------------------
-- 40.Table structure for `t_gm_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_gm_log`;
CREATE TABLE `t_gm_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `content` varchar(2000) DEFAULT '' COMMENT '日志内容',
  `time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='GM后台日志记录';

-- ----------------------------
-- 41.Table structure for `t_db`
-- ----------------------------
DROP TABLE IF EXISTS `t_db`;
CREATE TABLE `t_db` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `serverId` int(11) NOT NULL DEFAULT '0' COMMENT '服务器ID',
  `dbIp` varchar(200) NOT NULL DEFAULT '' COMMENT '数据库IP',
  `dbPort` int(11) NOT NULL DEFAULT '0' COMMENT '数据库Port',
  `dbname` varchar(128) NOT NULL DEFAULT '' COMMENT '数据库名称',
  `dbuser` varchar(128) NOT NULL DEFAULT '' COMMENT '数据库用户名',
  `dbpassword` varchar(100) NOT NULL DEFAULT '' COMMENT '数据库密码',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `serverId` (`serverId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏库列表';

-- ----------------------------
-- 42.Table structure for `t_dbbak`
-- ----------------------------
DROP TABLE IF EXISTS `t_dbbak`;
CREATE TABLE `t_dbbak` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `serverId` int(11) NOT NULL COMMENT '服务器id',
  `type` tinyint(4) NOT NULL COMMENT '类型1游戏库 2日志库',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `url` varchar(255) NOT NULL COMMENT '备份文件地址',
  `size` bigint(20) NOT NULL COMMENT '文件大小',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据库备份';

-- ----------------------------
-- 43.Table structure for `t_code_batch`
-- ----------------------------
DROP TABLE IF EXISTS `t_code_batch`;
CREATE TABLE `t_code_batch` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `batchId` int(32) DEFAULT NULL COMMENT '批次号',
  `userId` int(32) DEFAULT NULL COMMENT '账号ID',
  `time` bigint(64) DEFAULT NULL COMMENT '时间',
  `platform` varchar(50) DEFAULT NULL COMMENT '平台名',
  `isUniversal` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否为万能码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='激活码批次号';

-- ----------------------------
-- 44.Table structure for `t_activity`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity`;
CREATE TABLE `t_activity` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `type` int(32) DEFAULT '0' COMMENT '活动类型',
  `subType` int(32) DEFAULT '0' COMMENT '节日类型',
  `minLv` int(32) DEFAULT '0' COMMENT '最小开放等级',
  `maxLv` int(32) DEFAULT '0' COMMENT '最大开放等级',
  `tag` int(32) DEFAULT '0' COMMENT '标签(用于区分展示在哪个活动标签下)',
  `sort` int(32) DEFAULT '0' COMMENT '活动排序',
  `name` varchar(255) DEFAULT NULL COMMENT '活动名称',
  `description` varchar(200) DEFAULT NULL COMMENT '活动说明',
  `timeType` int(32) DEFAULT '0' COMMENT '时间类型 0固定时间（配置时间）1开服时间变量（根据开服时间+时间变量计算）',
  `openServerOffsetBegin` int(32) DEFAULT '0' COMMENT '距离开服多少天',
  `openServerOffset` int(32) DEFAULT '0' COMMENT '活动天数',
  `beginTime` varchar(128) DEFAULT NULL COMMENT '活动开始时间',
  `endTime` varchar(128) DEFAULT NULL COMMENT '活动结束时间',
  `openServerRecordOffsetBegin` int(32) DEFAULT '0' COMMENT '记录距离开服多少天',
  `openServerRecordOffset` int(32) DEFAULT '0' COMMENT '活动记录持续天数',
  `startRecordTime` varchar(128) DEFAULT NULL COMMENT '开始记录时间',
  `endRecordTime` varchar(128) DEFAULT NULL COMMENT '结束记录时间',
  `state` int(32) DEFAULT '0' COMMENT '活动状态，0：未验证(测试、删除)，1：已验证(发布、删除)，2：已发布(删除)，     //已过期(删除)这个通过活动结束时间去判断',
  `toSidList` varchar(500) DEFAULT '' COMMENT '活动要发布到的区服列表(List JSON化后的字串[sid1,sid2,..])',
  `okSidList` varchar(500) DEFAULT '' COMMENT '活动发布成功的区服列表(List JSON化后的字串[sid1,sid2,..])',
  `isDeleted` tinyint(8) DEFAULT '0' COMMENT '活动是否被删除，0：否，1：是',
  `autoSend` int(32) DEFAULT '0' COMMENT '开服自动发布活动标识，0：否，1：是',
  `isOpenServer` int(32) DEFAULT '0' COMMENT '是否是新服活动',
  `custom` text COMMENT '自定义参数',
  `cover` int(32) DEFAULT '0' COMMENT '活动是否被覆盖正在进行的活动，0：否，1：是',
  `configData` text COMMENT '配置参数，用于GM后台反解析',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `state_is_end` (`state`,`isDeleted`,`endTime`) USING BTREE,
  KEY `type_is_end` (`type`,`isDeleted`,`endTime`) USING BTREE,
  KEY `type_is` (`type`,`isDeleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='运营活动';

-- ----------------------------
-- 45.Table structure for `t_activity_boss_type`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_boss_type`;
CREATE TABLE `t_activity_boss_type` (
  `id` int(32) DEFAULT NULL COMMENT '活动BOSS分类配置ID',
  `name` varchar(128) DEFAULT NULL COMMENT '后台显示的BOSS类型',
  `boss_id` text COMMENT '对应的BOSSID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='运营活动boss类型';

-- ----------------------------
-- 46.Table structure for `t_activity_festival_type`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_festival_type`;
CREATE TABLE `t_activity_festival_type` (
  `id` int(32) DEFAULT NULL COMMENT '节日ID',
  `name` varchar(128) DEFAULT NULL COMMENT '节日名'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='运营活动节日类型';

-- ----------------------------
-- 47.Table structure for `t_activity_lucky_value`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_lucky_value`;
CREATE TABLE `t_activity_lucky_value` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `totalLuckyValue` int(32) DEFAULT NULL COMMENT '总幸运值',
  `tips` varchar(128) DEFAULT NULL COMMENT '备注说明',
  `state` int(32) DEFAULT NULL COMMENT '活动状态，0：未验证(测试、删除)，1：已验证(发布、删除)，2：已发布(删除)，     //已过期(删除)这个通过活动结束时间去判断',
  `platform` varchar(300) DEFAULT NULL COMMENT '活动发布平台(groupName)(List JSON化后的字串[plat1,plat2,..])',
  `toSidList` varchar(500) DEFAULT NULL COMMENT '活动要发布到的区服列表(List JSON化后的字串[sid1,sid2,..])',
  `okSidList` varchar(500) DEFAULT NULL COMMENT '活动发布成功的区服列表(List JSON化后的字串[sid1,sid2,..])',
  `isDeleted` tinyint(8) DEFAULT '0' COMMENT '活动是否被删除，0：否，1：是',
  `cover` int(32) DEFAULT NULL COMMENT '活动是否被覆盖正在进行的活动，0：否，1：是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='运营活动幸运值';

-- ----------------------------
-- 48.Table structure for `t_activity_model`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_model`;
CREATE TABLE `t_activity_model` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `career` varchar(500) DEFAULT NULL COMMENT '职业',
  `modelId` varchar(500) DEFAULT NULL COMMENT '模型ID',
  `scale` varchar(500) DEFAULT NULL COMMENT '模型大小倍数',
  `rotX` varchar(500) DEFAULT NULL COMMENT '对应的旋转参数x',
  `rotY` varchar(500) DEFAULT NULL COMMENT '对应的旋转参数y',
  `rotZ` varchar(500) DEFAULT NULL COMMENT '对应的旋转参数z',
  `posX` varchar(500) DEFAULT NULL COMMENT '对应的位置参数x',
  `posY` varchar(500) DEFAULT NULL COMMENT '对应的位置参数y',
  `tips` varchar(128) DEFAULT NULL COMMENT '模型库的备注说明',
  `modelData` varchar(1024) DEFAULT NULL COMMENT '发送给服务器的模型库数据',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运营活动模型库';

-- ----------------------------
-- 49.Table structure for `t_activity_template`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_template`;
CREATE TABLE `t_activity_template` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '活动模板ID',
  `templateName` varchar(50) DEFAULT NULL COMMENT '模版名称',
  `createTime` varchar(128) DEFAULT NULL COMMENT '活动模板创建时间',
  `type` int(32) DEFAULT '0' COMMENT '活动类型',
  `subType` int(32) DEFAULT '0' COMMENT '活动子类型',
  `minLv` int(32) DEFAULT '0' COMMENT '最小开放等级',
  `maxLv` int(32) DEFAULT '0' COMMENT '最大开放等级',
  `tag` int(32) DEFAULT '0' COMMENT '标签(用于区分展示在哪个活动标签下)',
  `sort` int(32) DEFAULT '0' COMMENT '活动排序',
  `name` varchar(128) DEFAULT NULL COMMENT '活动名称',
  `timeType` int(32) DEFAULT '0' COMMENT '时间类型 0固定时间（配置时间） 1开服时间变量（根据开服时间+时间变量计算）',
  `openServerOffsetBegin` int(32) DEFAULT '0' COMMENT '距离开服多少天',
  `openServerOffset` int(32) DEFAULT '0' COMMENT '活动天数',
  `beginTime` varchar(128) DEFAULT NULL COMMENT '活动开始时间',
  `endTime` varchar(128) DEFAULT NULL COMMENT '活动结束时间',
  `openServerRecordOffsetBegin` int(32) DEFAULT '0' COMMENT '记录距离开服多少天',
  `openServerRecordOffset` int(32) DEFAULT '0' COMMENT '活动记录持续天数',
  `startRecordTime` varchar(128) DEFAULT NULL COMMENT '开始记录时间',
  `endRecordTime` varchar(128) DEFAULT NULL COMMENT '结束记录时间',
  `autoSend` int(32) DEFAULT '0' COMMENT '开服自动发布活动标识，0：否，1：是',
  `isOpenServer` int(32) DEFAULT '0' COMMENT '是否是新服活动',
  `custom` text COMMENT '自定义参数',
  `templateDec` text COMMENT '模板描述',
  `description` varchar(200) DEFAULT NULL COMMENT '活动说明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='运营活动模板';

-- ----------------------------
-- 50.Table structure for `t_announce`
-- ----------------------------
DROP TABLE IF EXISTS `t_announce`;
CREATE TABLE `t_announce` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `createTime` bigint(64) DEFAULT NULL COMMENT '公告创建时间',
  `createDate` varchar(128) DEFAULT NULL COMMENT '创建时间的字符串式',
  `userId` int(32) DEFAULT NULL COMMENT '创建者账号ID',
  `userName` char(100) DEFAULT NULL COMMENT '创建者名字',
  `groupName` varchar(128) DEFAULT NULL COMMENT '服务器组',
  `serverIds` varchar(128) DEFAULT NULL COMMENT '服务器id',
  `type` int(32) DEFAULT NULL COMMENT '类型',
  `content` longtext COMMENT '公告的内容',
  `reason` varchar(128) DEFAULT NULL COMMENT '原因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='即时公告';

-- ----------------------------
-- 51.Table structure for `t_cyannounce`
-- ----------------------------
DROP TABLE IF EXISTS `t_cyannounce`;
CREATE TABLE `t_cyannounce` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '公告的编号',
  `groupName` varchar(128) DEFAULT NULL COMMENT '公告的平台分组',
  `serverIds` varchar(100) DEFAULT NULL COMMENT '公告的发送的服务器列表',
  `batchTag` varchar(128) DEFAULT NULL COMMENT '公告的标识',
  `content` text COMMENT '公告的内容',
  `createTime` bigint(64) DEFAULT NULL COMMENT '公告的创建时间',
  `createDate` varchar(128) DEFAULT NULL COMMENT '公告的创建时间字符格式化',
  `createUserId` int(32) DEFAULT NULL COMMENT '公告的添加者ID',
  `createUserName` varchar(128) DEFAULT NULL COMMENT '公告的添加者名字',
  `fromTime` bigint(64) DEFAULT NULL COMMENT '公告的开始时间',
  `fromDate` varchar(128) DEFAULT NULL COMMENT '公告的开始字符格式化',
  `toTime` bigint(64) DEFAULT NULL COMMENT '公告的结束时间',
  `toDate` varchar(128) DEFAULT NULL COMMENT '公告的结束时间字符格式化',
  `totalTimes` int(32) DEFAULT NULL COMMENT '公告发送的总次数',
  `nowTimes` bigint(64) DEFAULT NULL COMMENT '公告的当前已经发送的次数',
  `nextTimes` bigint(64) DEFAULT NULL COMMENT '公告的下一次发送的时间',
  `nextDate` varchar(128) DEFAULT NULL COMMENT '公告的下一次发送时间字符格式化',
  `state` int(32) DEFAULT '0' COMMENT '公告的当前状态，启用还是禁用',
  `cycleInterval` int(32) DEFAULT NULL COMMENT '公告发送的频率',
  `type` int(32) DEFAULT NULL COMMENT '公告发送的位置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- 52.Table structure for `t_update_notice`
-- ----------------------------
DROP TABLE IF EXISTS `t_update_notice`;
CREATE TABLE `t_update_notice` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `serverIds` varchar(128) DEFAULT NULL COMMENT '服务器ID',
  `content` text COMMENT '公告内容',
  `reward` varchar(128) DEFAULT NULL COMMENT '公告奖励',
  `type` int(32) DEFAULT NULL COMMENT '操作类型，0 ：只更新公告， 1： 重置奖励',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='更新公告';

-- ----------------------------
-- 53.Table structure for `t_deduct_item`
-- ----------------------------
DROP TABLE IF EXISTS `t_deduct_item`;
CREATE TABLE `t_deduct_item` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '道具扣除ID',
  `serverId` int(32) DEFAULT NULL COMMENT '服务ID',
  `itemId` int(32) DEFAULT NULL COMMENT '物品ID',
  `roleId` varchar(128) DEFAULT NULL COMMENT '角色ID',
  `dedCount` int(32) DEFAULT NULL COMMENT '欲扣除的数量',
  `realCount` int(32) DEFAULT NULL COMMENT '真实扣除的数量',
  `isMail` int(32) DEFAULT NULL COMMENT '是否发送邮件，0 不发送 1 发送',
  `isBind` tinyint(1) DEFAULT NULL COMMENT '是否绑定 true 绑定，false 不绑定',
  `dedTime` datetime DEFAULT NULL COMMENT '扣除时间',
  `reason` varchar(128) DEFAULT NULL COMMENT '扣除原因',
  `mailTitle` varchar(128) DEFAULT NULL COMMENT '邮件标题',
  `mailContent` varchar(128) DEFAULT NULL COMMENT '邮件标题',
  `isDelete` int(32) DEFAULT NULL COMMENT '是否删除，0 ：不删除， 1： 删除',
  `sendUser` varchar(128) DEFAULT NULL COMMENT '发起者名字',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='道具扣除';

-- ----------------------------
-- 54.Table structure for `t_role_attr`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_attr`;
CREATE TABLE `t_role_attr` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '属性设置ID',
  `serverId` int(32) DEFAULT NULL COMMENT '服务器ID',
  `roleId` varchar(128) DEFAULT NULL COMMENT '角色ID',
  `attrType` int(32) DEFAULT NULL COMMENT '属性类型',
  `attrValue` int(32) DEFAULT NULL COMMENT '设置的属性值',
  `realValue` int(32) DEFAULT NULL COMMENT '真实的属性值',
  `actionTime` datetime DEFAULT NULL COMMENT '执行时间',
  `reason` varchar(128) DEFAULT NULL COMMENT '设置原因',
  `isDelete` int(32) DEFAULT NULL COMMENT '是否删除，0 ：不删除， 1： 删除',
  `actionUser` varchar(128) DEFAULT NULL COMMENT '操作者名字',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='角色属性';

-- ----------------------------
-- 55.Table structure for `t_role_transfer`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_transfer`;
CREATE TABLE `t_role_transfer` (
  `roleId` varchar(50) NOT NULL COMMENT '角色ID',
  `srcUserId` varchar(50) DEFAULT NULL COMMENT '原账号ID',
  `targetUserId` varchar(50) DEFAULT NULL COMMENT '目标账号ID',
  `serverId` int(32) DEFAULT NULL COMMENT '服务器ID',
  `reason` varchar(300) DEFAULT NULL COMMENT '操作原因',
  `isDeleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除，0 ：不删除， 1： 删除',
  `time` datetime DEFAULT NULL COMMENT '执行时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色转移';

-- ----------------------------
-- 56.Table structure for `t_blackuser`
-- ----------------------------
DROP TABLE IF EXISTS `t_blackuser`;
CREATE TABLE `t_blackuser` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `userId` bigint(64) DEFAULT NULL COMMENT '黑名单用户Id',
  `platform` varchar(50) DEFAULT NULL COMMENT '平台名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='黑名单';

-- ----------------------------
-- 57.Table structure for `t_evaluate`
-- ----------------------------
DROP TABLE IF EXISTS `t_evaluate`;
CREATE TABLE `t_evaluate` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `serverId` int(32) DEFAULT NULL COMMENT '服务器ID',
  `eType` int(32) DEFAULT NULL COMMENT '评价类型',
  `state` tinyint(1) DEFAULT NULL COMMENT '开关状态',
  `actionTime` datetime DEFAULT NULL COMMENT '执行时间',
  `reason` varchar(128) DEFAULT NULL COMMENT '设置原因',
  `isDelete` int(32) DEFAULT NULL COMMENT '是否删除，0 ：不删除， 1： 删除',
  `actionUser` varchar(128) DEFAULT NULL COMMENT '操作者名字',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='评价开关';

-- ----------------------------
-- 58.Table structure for `t_function`
-- ----------------------------
DROP TABLE IF EXISTS `t_function`;
CREATE TABLE `t_function` (
  `funcId` int(32) DEFAULT NULL COMMENT '功能Id',
  `funcName` varchar(128) DEFAULT NULL COMMENT '功能名',
  `parentId` int(32) DEFAULT NULL COMMENT '父Id',
  `openState` int(32) DEFAULT NULL COMMENT '开启状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='系统开关';

-- ----------------------------
-- 59.Table structure for `t_activity_festival_relation`
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_festival_relation`;
CREATE TABLE `t_activity_festival_relation` (
  `logic_id` int(32) DEFAULT NULL COMMENT '运营活动type的ID',
  `festival_id` int(32) DEFAULT NULL COMMENT '节日ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运营活动节日关系表';

-- ----------------------------
-- 60.Table structure for `game_info`
-- ----------------------------
DROP TABLE IF EXISTS `game_info`;
CREATE TABLE `game_info` (
  `gameId` int(11) NOT NULL COMMENT '游戏ID',
  `rechargeSecretkey` varchar(50) DEFAULT NULL COMMENT '第三方充值密钥',
  `autoFirstServerId` int(11) NOT NULL DEFAULT '0' COMMENT '自动开服起始服务器ID',
  `autoUserCount` int(11) NOT NULL DEFAULT '0' COMMENT '自动开服注册人数条件',
  `autoServerId` int(11) DEFAULT '0' COMMENT '自动开服ID',
  `time` bigint(64) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`gameId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏参数表';

-- ----------------------------
-- 61.Table structure for `t_ban_account`
-- ----------------------------
DROP TABLE IF EXISTS `t_ban_account`;
CREATE TABLE `t_ban_account` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `con` varchar(190) DEFAULT NULL COMMENT '封号的条件',
  `endTime` varchar(50) DEFAULT NULL COMMENT '封禁结束时间',
  `reason` varchar(600) DEFAULT NULL COMMENT '操作理由',
  `state` int(11) DEFAULT NULL COMMENT '状态0:封禁1:解封',
  PRIMARY KEY (`id`),
  UNIQUE KEY `CON` (`con`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账号封禁表';

-- ----------------------------
-- 62.Table structure for `t_ban_chat`
-- ----------------------------
DROP TABLE IF EXISTS `t_ban_chat`;
CREATE TABLE `t_ban_chat` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `userId` varchar(190) DEFAULT NULL COMMENT '聊天禁言的用户ID',
  `crimeType` int(32) DEFAULT NULL COMMENT '违规类型 1黑色产业 2不良信息',
  `banType` int(32) DEFAULT NULL COMMENT '禁言类型 1:工作室禁言2:全文替换禁言3:关键字替换禁言4:常规禁言5:隐形禁言6:隔离禁言',
  `endTime` varchar(128) DEFAULT NULL COMMENT '封禁结束时间',
  `reason` varchar(600) DEFAULT NULL COMMENT '操作理由',
  `serverIds` varchar(100) DEFAULT NULL COMMENT '发送到游戏服列表',
  `state` int(32) DEFAULT NULL COMMENT '状态0:封禁1:解封',
  PRIMARY KEY (`id`),
  UNIQUE KEY `USERID` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='聊天封禁表';

-- ----------------------------
-- 63.Table structure for `t_white`
-- ----------------------------
DROP TABLE IF EXISTS `t_white`;
CREATE TABLE `t_white` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `con` varchar(190) DEFAULT NULL COMMENT '白名单条件',
  `state` int(32) DEFAULT NULL COMMENT '状态:0=生效1=失效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `CON` (`con`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='白名单表';

-- ----------------------------
-- 64.Table structure for `t_recharge`
-- ----------------------------
DROP TABLE IF EXISTS `t_recharge`;
CREATE TABLE `t_recharge` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `roleId` varchar(50) DEFAULT NULL COMMENT '角色ID',
  `rechargeNumber` int(32) DEFAULT NULL COMMENT '充值订单金额(单位分)',
  `rechargeTotalGold` int(32) DEFAULT NULL COMMENT '充值累积数量',
  `rechargeVipExp` int(32) DEFAULT NULL COMMENT '充值VIP经验',
  `rechargeState` int(11) DEFAULT NULL COMMENT '充值状态,0为待审核,1为通过,2为失败',
  `reason` varchar(200) DEFAULT NULL COMMENT '操作原因',
  `toServerId` int(11) DEFAULT NULL COMMENT '操作服务器ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台模拟充值表';

-- =====================================================--------------

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
(8,'主框架页-菜单导航显示风格','sys.index.menuStyle','default','Y','admin','2021-04-22 19:32:07','',NULL,'菜单导航显示风格（default为左侧导航菜单，topnav为顶部导航菜单）'),
(9,'主框架页-是否开启页脚','sys.index.ignoreFooter','true','Y','admin','2021-04-22 19:32:07','',NULL,'是否开启底部页脚显示（true显示，false隐藏）');


-- ----------------------------
-- 2.Data for the table `sys_dict_data`
-- ----------------------------
insert into sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) values 
('1', '1', '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2021-04-22 19:32:02', '', null, '性别男'),
('2', '2', '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2021-04-22 19:32:02', '', null, '性别女'),
('3', '3', '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2021-04-22 19:32:02', '', null, '性别未知'),
('4', '1', '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2021-04-22 19:32:02', '', null, '显示菜单'),
('5', '2', '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:02', '', null, '隐藏菜单'),
('6', '1', '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2021-04-22 19:32:02', '', null, '正常状态'),
('7', '2', '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:03', '', null, '停用状态'),
('8', '1', '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2021-04-22 19:32:03', '', null, '正常状态'),
('9', '2', '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:03', '', null, '停用状态'),
('10', '1', '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2021-04-22 19:32:03', '', null, '默认分组'),
('11', '2', '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2021-04-22 19:32:03', '', null, '系统分组'),
('12', '1', '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2021-04-22 19:32:03', '', null, '系统默认是'),
('13', '2', '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:03', '', null, '系统默认否'),
('14', '1', '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2021-04-22 19:32:03', '', null, '通知'),
('15', '2', '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2021-04-22 19:32:03', '', null, '公告'),
('16', '1', '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2021-04-22 19:32:03', '', null, '正常状态'),
('17', '2', '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:03', '', null, '关闭状态'),
('18', '99', '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2021-04-22 19:32:03', '', null, '其他操作'),
('19', '1', '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2021-04-22 19:32:03', '', null, '新增操作'),
('20', '2', '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2021-04-22 19:32:03', '', null, '修改操作'),
('21', '3', '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:03', '', null, '删除操作'),
('22', '4', '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2021-04-22 19:32:03', '', null, '授权操作'),
('23', '5', '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2021-04-22 19:32:04', '', null, '导出操作'),
('24', '6', '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2021-04-22 19:32:04', '', null, '导入操作'),
('25', '7', '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:04', '', null, '强退操作'),
('26', '8', '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2021-04-22 19:32:04', '', null, '生成操作'),
('27', '9', '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:04', '', null, '清空操作'),
('28', '1', '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2021-04-22 19:32:04', '', null, '正常状态'),
('29', '2', '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:04', '', null, '停用状态'),
('100', '1', '测试服', '0', 'server_type', null, 'default', 'Y', '0', 'admin', '2021-04-23 13:23:02', '', null, null),
('101', '2', '正式服', '1', 'server_type', null, 'default', 'Y', '0', 'admin', '2021-04-23 13:23:21', '', null, null),
('102', '3', '登录服', '2', 'server_type', null, 'default', 'Y', '0', 'admin', '2021-04-23 13:23:38', '', null, null),
('103', '3', '跨服', '3', 'server_type', null, 'default', 'Y', '0', 'admin', '2021-04-23 13:23:55', '', null, null),
('104', '1', '游戏库', '0', 'db_log_type', null, 'default', 'Y', '0', 'admin', '2021-04-23 13:25:33', '', null, null),
('105', '2', '日志库', '1', 'db_log_type', null, 'default', 'Y', '0', 'admin', '2021-04-23 13:25:50', '', null, null),
('106', '1', '未合服', '0', 'he_fu_type', null, 'default', 'Y', '0', 'admin', '2021-04-23 13:28:41', '', null, null),
('107', '2', '已合服', '1', 'he_fu_type', '', 'default', 'Y', '0', 'admin', '2021-04-23 13:28:53', 'admin', '2021-04-23 13:29:03', ''),
('108', '1', '启用', '0', 'deleted_type', null, 'default', 'Y', '0', 'admin', '2021-04-23 13:29:53', '', null, null),
('109', '2', '删除', '1', 'deleted_type', null, 'default', 'Y', '0', 'admin', '2021-04-23 13:30:06', '', null, null),
('110', '1', '开启', '0', 'status_type', '', 'primary', 'Y', '0', 'admin', '2021-04-26 10:51:47', 'admin', '2021-04-27 10:31:12', ''),
('111', '2', '关闭', '1', 'status_type', '', 'danger', 'Y', '0', 'admin', '2021-04-26 10:52:19', 'admin', '2021-04-27 10:31:06', '');


-- ----------------------------
-- 3.Data for the table `sys_dict_type`
-- ----------------------------
insert into sys_dict_type(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) values 
('1', '用户性别', 'sys_user_sex', '0', 'admin', '2021-04-22 19:31:59', '', null, '用户性别列表'),
('2', '菜单状态', 'sys_show_hide', '0', 'admin', '2021-04-22 19:31:59', '', null, '菜单状态列表'),
('3', '系统开关', 'sys_normal_disable', '0', 'admin', '2021-04-22 19:31:59', '', null, '系统开关列表'),
('4', '任务状态', 'sys_job_status', '0', 'admin', '2021-04-22 19:31:59', '', null, '任务状态列表'),
('5', '任务分组', 'sys_job_group', '0', 'admin', '2021-04-22 19:31:59', '', null, '任务分组列表'),
('6', '系统是否', 'sys_yes_no', '0', 'admin', '2021-04-22 19:32:00', '', null, '系统是否列表'),
('7', '通知类型', 'sys_notice_type', '0', 'admin', '2021-04-22 19:32:00', '', null, '通知类型列表'),
('8', '通知状态', 'sys_notice_status', '0', 'admin', '2021-04-22 19:32:00', '', null, '通知状态列表'),
('9', '操作类型', 'sys_oper_type', '0', 'admin', '2021-04-22 19:32:00', '', null, '操作类型列表'),
('10', '系统状态', 'sys_common_status', '0', 'admin', '2021-04-22 19:32:00', '', null, '登录状态列表'),
('100', '服务类型', 'server_type', '0', 'admin', '2021-04-23 13:20:09', '', null, '服务类型'),
('101', '数据库类型', 'db_log_type', '0', 'admin', '2021-04-23 13:25:15', '', null, '数据库类型'),
('102', '合服标识', 'he_fu_type', '0', 'admin', '2021-04-23 13:28:18', '', null, '合服标识'),
('103', '是否删除', 'deleted_type', '0', 'admin', '2021-04-23 13:29:36', '', null, null),
('104', '是否开启', 'status_type', '0', 'admin', '2021-04-26 10:50:44', '', null, '开启或关闭状态');

-- ----------------------------
-- 4.Data for the table `sys_menu`
-- ----------------------------
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1, '系统管理', 0, 1, '#', '', 'M', '0', '1', '', 'fa fa-gear', 'admin', '2021-04-22 19:31:36', '', NULL, '系统管理目录'),
(2, '系统监控', 0, 2, '#', '', 'M', '0', '1', '', 'fa fa-video-camera', 'admin', '2021-04-22 19:31:36', '', NULL, '系统监控目录'),
(100, '用户管理', 1, 1, '/system/user', '', 'C', '0', '1', 'system:user:view', 'fa fa-user-o', 'admin', '2021-04-22 19:31:37', '', NULL, '用户管理菜单'),
(101, '角色管理', 1, 2, '/system/role', '', 'C', '0', '1', 'system:role:view', 'fa fa-user-secret', 'admin', '2021-04-22 19:31:37', '', NULL, '角色管理菜单'),
(102, '菜单管理', 1, 3, '/system/menu', '', 'C', '0', '1', 'system:menu:view', 'fa fa-th-list', 'admin', '2021-04-22 19:31:37', '', NULL, '菜单管理菜单'),
(103, '部门管理', 1, 4, '/system/dept', '', 'C', '0', '1', 'system:dept:view', 'fa fa-outdent', 'admin', '2021-04-22 19:31:37', '', NULL, '部门管理菜单'),
(104, '岗位管理', 1, 5, '/system/post', '', 'C', '0', '1', 'system:post:view', 'fa fa-address-card-o', 'admin', '2021-04-22 19:31:37', '', NULL, '岗位管理菜单'),
(105, '字典管理', 1, 6, '/system/dict', '', 'C', '0', '1', 'system:dict:view', 'fa fa-bookmark-o', 'admin', '2021-04-22 19:31:37', '', NULL, '字典管理菜单'),
(106, '参数设置', 1, 7, '/system/config', '', 'C', '0', '1', 'system:config:view', 'fa fa-sun-o', 'admin', '2021-04-22 19:31:37', '', NULL, '参数设置菜单'),
(107, '通知公告', 1, 8, '/system/notice', '', 'C', '0', '1', 'system:notice:view', 'fa fa-bullhorn', 'admin', '2021-04-22 19:31:37', '', NULL, '通知公告菜单'),
(108, '日志管理', 1, 9, '#', '', 'M', '0', '1', '', 'fa fa-pencil-square-o', 'admin', '2021-04-22 19:31:37', '', NULL, '日志管理菜单'),
(109, '在线用户', 2, 1, '/monitor/online', '', 'C', '0', '1', 'monitor:online:view', 'fa fa-user-circle', 'admin', '2021-04-22 19:31:37', '', NULL, '在线用户菜单'),
(110, '定时任务', 2, 2, '/monitor/job', '', 'C', '0', '1', 'monitor:job:view', 'fa fa-tasks', 'admin', '2021-04-22 19:31:37', '', NULL, '定时任务菜单'),
(111, '数据监控', 2, 3, '/monitor/data', '', 'C', '0', '1', 'monitor:data:view', 'fa fa-bug', 'admin', '2021-04-22 19:31:37', '', NULL, '数据监控菜单'),
(112, '服务监控', 2, 4, '/monitor/server', '', 'C', '0', '1', 'monitor:server:view', 'fa fa-server', 'admin', '2021-04-22 19:31:37', '', NULL, '服务监控菜单'),
(113, '缓存监控', 2, 5, '/monitor/cache', '', 'C', '0', '1', 'monitor:cache:view', 'fa fa-cube', 'admin', '2021-04-22 19:31:37', '', NULL, '缓存监控菜单'),
(500, '操作日志', 108, 1, '/monitor/operlog', '', 'C', '0', '1', 'monitor:operlog:view', 'fa fa-address-book', 'admin', '2021-04-22 19:31:37', '', NULL, '操作日志菜单'),
(501, '登录日志', 108, 2, '/monitor/logininfor', '', 'C', '0', '1', 'monitor:logininfor:view', 'fa fa-file-image-o', 'admin', '2021-04-22 19:31:38', '', NULL, '登录日志菜单'),
(1000, '用户查询', 100, 1, '#', '', 'F', '0', '1', 'system:user:list', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1001, '用户新增', 100, 2, '#', '', 'F', '0', '1', 'system:user:add', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1002, '用户修改', 100, 3, '#', '', 'F', '0', '1', 'system:user:edit', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1003, '用户删除', 100, 4, '#', '', 'F', '0', '1', 'system:user:remove', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1004, '用户导出', 100, 5, '#', '', 'F', '0', '1', 'system:user:export', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1005, '用户导入', 100, 6, '#', '', 'F', '0', '1', 'system:user:import', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1006, '重置密码', 100, 7, '#', '', 'F', '0', '1', 'system:user:resetPwd', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1007, '角色查询', 101, 1, '#', '', 'F', '0', '1', 'system:role:list', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1008, '角色新增', 101, 2, '#', '', 'F', '0', '1', 'system:role:add', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1009, '角色修改', 101, 3, '#', '', 'F', '0', '1', 'system:role:edit', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1010, '角色删除', 101, 4, '#', '', 'F', '0', '1', 'system:role:remove', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1011, '角色导出', 101, 5, '#', '', 'F', '0', '1', 'system:role:export', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1012, '菜单查询', 102, 1, '#', '', 'F', '0', '1', 'system:menu:list', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1013, '菜单新增', 102, 2, '#', '', 'F', '0', '1', 'system:menu:add', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1014, '菜单修改', 102, 3, '#', '', 'F', '0', '1', 'system:menu:edit', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1015, '菜单删除', 102, 4, '#', '', 'F', '0', '1', 'system:menu:remove', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1016, '部门查询', 103, 1, '#', '', 'F', '0', '1', 'system:dept:list', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1017, '部门新增', 103, 2, '#', '', 'F', '0', '1', 'system:dept:add', '#', 'admin', '2021-04-22 19:31:38', '', NULL, ''),
(1018, '部门修改', 103, 3, '#', '', 'F', '0', '1', 'system:dept:edit', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1019, '部门删除', 103, 4, '#', '', 'F', '0', '1', 'system:dept:remove', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1020, '岗位查询', 104, 1, '#', '', 'F', '0', '1', 'system:post:list', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1021, '岗位新增', 104, 2, '#', '', 'F', '0', '1', 'system:post:add', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1022, '岗位修改', 104, 3, '#', '', 'F', '0', '1', 'system:post:edit', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1023, '岗位删除', 104, 4, '#', '', 'F', '0', '1', 'system:post:remove', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1024, '岗位导出', 104, 5, '#', '', 'F', '0', '1', 'system:post:export', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1025, '字典查询', 105, 1, '#', '', 'F', '0', '1', 'system:dict:list', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1026, '字典新增', 105, 2, '#', '', 'F', '0', '1', 'system:dict:add', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1027, '字典修改', 105, 3, '#', '', 'F', '0', '1', 'system:dict:edit', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1028, '字典删除', 105, 4, '#', '', 'F', '0', '1', 'system:dict:remove', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1029, '字典导出', 105, 5, '#', '', 'F', '0', '1', 'system:dict:export', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1030, '参数查询', 106, 1, '#', '', 'F', '0', '1', 'system:config:list', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1031, '参数新增', 106, 2, '#', '', 'F', '0', '1', 'system:config:add', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1032, '参数修改', 106, 3, '#', '', 'F', '0', '1', 'system:config:edit', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1033, '参数删除', 106, 4, '#', '', 'F', '0', '1', 'system:config:remove', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1034, '参数导出', 106, 5, '#', '', 'F', '0', '1', 'system:config:export', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1035, '公告查询', 107, 1, '#', '', 'F', '0', '1', 'system:notice:list', '#', 'admin', '2021-04-22 19:31:39', '', NULL, ''),
(1036, '公告新增', 107, 2, '#', '', 'F', '0', '1', 'system:notice:add', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1037, '公告修改', 107, 3, '#', '', 'F', '0', '1', 'system:notice:edit', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1038, '公告删除', 107, 4, '#', '', 'F', '0', '1', 'system:notice:remove', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1039, '操作查询', 500, 1, '#', '', 'F', '0', '1', 'monitor:operlog:list', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1040, '操作删除', 500, 2, '#', '', 'F', '0', '1', 'monitor:operlog:remove', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1041, '详细信息', 500, 3, '#', '', 'F', '0', '1', 'monitor:operlog:detail', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1042, '日志导出', 500, 4, '#', '', 'F', '0', '1', 'monitor:operlog:export', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1043, '登录查询', 501, 1, '#', '', 'F', '0', '1', 'monitor:logininfor:list', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1044, '登录删除', 501, 2, '#', '', 'F', '0', '1', 'monitor:logininfor:remove', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1045, '日志导出', 501, 3, '#', '', 'F', '0', '1', 'monitor:logininfor:export', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1046, '账户解锁', 501, 4, '#', '', 'F', '0', '1', 'monitor:logininfor:unlock', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1047, '在线查询', 109, 1, '#', '', 'F', '0', '1', 'monitor:online:list', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1048, '批量强退', 109, 2, '#', '', 'F', '0', '1', 'monitor:online:batchForceLogout', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1049, '单条强退', 109, 3, '#', '', 'F', '0', '1', 'monitor:online:forceLogout', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1050, '任务查询', 110, 1, '#', '', 'F', '0', '1', 'monitor:job:list', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1051, '任务新增', 110, 2, '#', '', 'F', '0', '1', 'monitor:job:add', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1052, '任务修改', 110, 3, '#', '', 'F', '0', '1', 'monitor:job:edit', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1053, '任务删除', 110, 4, '#', '', 'F', '0', '1', 'monitor:job:remove', '#', 'admin', '2021-04-22 19:31:40', '', NULL, ''),
(1054, '状态修改', 110, 5, '#', '', 'F', '0', '1', 'monitor:job:changeStatus', '#', 'admin', '2021-04-22 19:31:41', '', NULL, ''),
(1055, '任务详细', 110, 6, '#', '', 'F', '0', '1', 'monitor:job:detail', '#', 'admin', '2021-04-22 19:31:41', '', NULL, ''),
(1056, '任务导出', 110, 7, '#', '', 'F', '0', '1', 'monitor:job:export', '#', 'admin', '2021-04-22 19:31:41', '', NULL, ''),
(1057, '生成查询', 115, 1, '#', '', 'F', '0', '1', 'tool:gen:list', '#', 'admin', '2021-04-22 19:31:41', '', NULL, ''),
(1058, '生成修改', 115, 2, '#', '', 'F', '0', '1', 'tool:gen:edit', '#', 'admin', '2021-04-22 19:31:41', '', NULL, ''),
(1059, '生成删除', 115, 3, '#', '', 'F', '0', '1', 'tool:gen:remove', '#', 'admin', '2021-04-22 19:31:41', '', NULL, ''),
(1060, '预览代码', 115, 4, '#', '', 'F', '0', '1', 'tool:gen:preview', '#', 'admin', '2021-04-22 19:31:41', '', NULL, ''),
(1061, '生成代码', 115, 5, '#', '', 'F', '0', '1', 'tool:gen:code', '#', 'admin', '2021-04-22 19:31:41', '', NULL, ''),
(2000, '运维工具', 0, 4, '#', '', 'M', '0', '1', '', 'fa fa-wifi', 'admin', '2021-04-23 13:46:10', '', NULL, '运维工具目录'),
(2046, '数据统计', 0, 8, '#', 'menuItem', 'M', '0', '1', '', 'fa fa-tv', 'admin', '2021-05-19 15:04:47', 'admin', '2021-08-14 14:04:14', ''),
(2054, '留存统计', 2046, 1, '/stat/stat_remain', '', 'C', '0', '1', 'stat:stat_remain:view', '#', 'admin', '2021-05-25 16:44:39', '', NULL, '留存统计菜单'),
(2055, '留存统计查询', 2054, 1, '#', '', 'F', '0', '1', 'stat:stat_remain:list', '#', 'admin', '2021-05-25 16:44:39', '', NULL, ''),
(2059, '留存统计导出', 2054, 5, '#', '', 'F', '0', '1', 'stat:stat_remain:export', '#', 'admin', '2021-05-25 16:44:39', '', NULL, ''),
(2060, 'ltv统计', 2046, 2, '/stat/stat_ltv', 'menuItem', 'C', '0', '1', '', '#', 'admin', '2021-05-28 16:57:49', 'admin', '2021-06-03 17:00:56', '留存统计菜单'),
(2061, '游戏日志', 0, 7, '#', 'menuItem', 'M', '0', '1', NULL, 'fa fa-certificate', 'admin', '2021-06-08 14:57:36', '', NULL, ''),
(2068, '聊天日志', 2061, 1, '/gamelog/chatlog', '', 'C', '0', '1', 'gamelog:chatlog:view', '#', 'admin', '2021-06-08 17:14:48', '', NULL, '聊天日志菜单'),
(2069, '聊天日志查询', 2068, 1, '#', '', 'F', '0', '1', 'gamelog:chatlog:list', '#', 'admin', '2021-06-08 17:14:48', '', NULL, ''),
(2076, '服务器列表', 2000, 1, '/gmtool/server', 'menuItem', 'C', '0', '1', 'gmtool:server:view', '#', 'admin', '2021-07-16 09:52:27', 'admin', '2021-07-30 14:51:04', '服务器列表菜单'),
(2077, '服务器列表查询', 2076, 1, '#', '', 'F', '0', '1', 'gmtool:server:list', '#', 'admin', '2021-07-16 09:52:27', '', NULL, ''),
(2078, '服务器列表新增', 2076, 2, '#', '', 'F', '0', '1', 'gmtool:server:add', '#', 'admin', '2021-07-16 09:52:27', '', NULL, ''),
(2079, '服务器列表修改', 2076, 3, '#', '', 'F', '0', '1', 'gmtool:server:edit', '#', 'admin', '2021-07-16 09:52:27', '', NULL, ''),
(2080, '服务器列表删除', 2076, 4, '#', '', 'F', '0', '1', 'gmtool:server:remove', '#', 'admin', '2021-07-16 09:52:27', '', NULL, ''),
(2081, '服务器列表导出', 2076, 5, '#', '', 'F', '0', '1', 'gmtool:server:export', '#', 'admin', '2021-07-16 09:52:27', '', NULL, ''),
(2082, '服务器指令', 2000, 4, '/gmtool/cmd', 'menuItem', 'C', '0', '1', 'gmtool:cmd:view', '#', 'admin', '2021-07-30 16:20:43', 'admin', '2021-08-31 11:56:46', '热更服务器操作日志菜单'),
(2088, '公共服指令', 2000, 5, '/gmtool/cmd/psCmd', 'menuItem', 'C', '0', '1', 'gmtool:cmd:psCmd', '#', 'admin', '2021-08-03 14:29:25', 'admin', '2021-08-31 11:57:02', ''),
(2089, '等级分布', 2046, 1, '/stat/stat_level_distribute', 'menuItem', 'C', '0', '1', 'stat:stat_level_distribute:view', '#', 'admin', '2021-08-06 18:02:33', 'admin', '2021-08-11 16:32:40', '角色等级分布菜单'),
(2090, '角色等级分布查询', 2089, 1, '#', '', 'F', '0', '1', 'stat:stat_level_distribute:list', '#', 'admin', '2021-08-06 18:02:33', '', NULL, ''),
(2091, '设置开服时间', 2000, 6, '/gmtool/cmd/opstime', 'menuItem', 'C', '0', '1', 'gmtool:cmd:opstime', '#', 'admin', '2021-08-10 20:33:35', 'admin', '2021-08-31 11:57:16', ''),
(2092, '数据查询', 0, 5, '#', 'menuItem', 'M', '0', '1', '', 'fa fa-desktop', 'admin', '2021-08-14 14:03:16', 'admin', '2021-11-10 11:15:48', ''),
(2093, '运营管理', 0, 6, '#', 'menuItem', 'M', '0', '1', NULL, 'fa fa-edit', 'admin', '2021-08-14 14:04:59', '', NULL, ''),
(2094, '运营工具', 2093, 1, '#', 'menuItem', 'M', '0', '1', NULL, '#', 'admin', '2021-08-14 14:16:26', '', NULL, ''),
(2095, '跨服配置', 2094, 5, '/gmtool/serverGroup', 'menuItem', 'C', '0', '1', 'gmtool:serverGroup:view', '#', 'admin', '2021-08-14 14:32:17', 'admin', '2021-09-23 17:38:55', ''),
(2096, '充值配置', 2094, 4, '/gmtool/rechargeItem', 'menuItem', 'C', '0', '1', 'gmtool:rechargeItem:view', '#', 'admin', '2021-08-25 14:00:47', 'admin', '2021-09-23 17:38:40', ''),
(2097, '邮件管理', 2093, 2, '#', 'menuItem', 'M', '0', '1', NULL, '#', 'admin', '2021-08-30 17:26:16', '', NULL, ''),
(2098, '邮件列表', 2097, 1, '/gmtool/mail', 'menuItem', 'C', '0', '1', 'gmtool:mail:view', '#', 'admin', '2021-08-30 18:21:24', '', NULL, ''),
(2099, '全服邮件列表', 2097, 2, '/gmtool/mail/allMail', 'menuItem', 'C', '0', '1', 'gmtool:mail:allMail', '#', 'admin', '2021-08-30 20:27:10', '', NULL, ''),
(2100, '后台数据加载', 2000, 3, '/gmtool/backenddataload', 'menuItem', 'C', '0', '1', 'gmtool:backenddataload:view', '#', 'admin', '2021-08-31 11:55:20', 'admin', '2021-09-29 16:55:25', ''),
(2101, '邮件发送', 2097, 3, '/gmtool/mail/sendmail', 'menuItem', 'C', '0', '1', 'gmtool:mail:sendmail', '#', 'admin', '2021-09-01 17:17:40', 'admin', '2021-09-01 17:28:38', ''),
(2114, 'GM后台日志记录', 108, 1, '/gmtool/gmlog', '', 'C', '0', '1', 'gmtool:gmlog:view', '#', 'admin', '2021-09-01 21:15:04', '', NULL, 'GM后台日志记录菜单'),
(2115, 'GM后台日志记录查询', 2114, 1, '#', '', 'F', '0', '1', 'gmtool:gmlog:list', '#', 'admin', '2021-09-01 21:15:04', '', NULL, ''),
(2119, 'GM后台日志记录导出', 2114, 2, '#', '', 'F', '0', '1', 'gmtool:gmlog:export', '#', 'admin', '2021-09-01 21:15:04', '', NULL, ''),
(2121, '超级邮件发送', 2097, 4, '/gmtool/mail/sendsupermail', 'menuItem', 'C', '0', '1', 'gmtool:mail:sendsupermail', '#', 'admin', '2021-09-06 15:55:07', '', NULL, ''),
(2122, '全服邮件发送', 2097, 5, '/gmtool/mail/sendAllMail', 'menuItem', 'C', '0', '1', 'gmtool:mail:sendAllMail', '#', 'admin', '2021-09-06 16:55:53', '', NULL, ''),
(2123, '职业分布', 2046, 1, '/stat/stat_career_distribute', '', 'C', '0', '1', 'stat:stat_career_distribute:view', '#', 'admin', '2021-09-07 10:56:32', '', NULL, '职业分布菜单'),
(2124, '职业分布查询', 2123, 1, '#', '', 'F', '0', '1', 'stat:stat_career_distribute:list', '#', 'admin', '2021-09-07 11:04:12', '', NULL, ''),
(2126, '活动制作', 2093, 3, '#', 'menuItem', 'M', '0', '1', '', '#', 'admin', '2021-09-07 17:05:57', 'admin', '2021-11-30 15:33:13', ''),
(2127, '活动总览', 2400, 1, '/gmtool/activity/getPage?type=0', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-07 17:54:39', 'admin', '2021-11-30 15:36:28', ''),
(2128, '活跃活动', 2126, 2, '/gmtool/activity/getPage?type=1', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-07 18:29:07', '', NULL, ''),
(2129, '角色快照日志', 2061, 2, '/gamelog/rolestate', '', 'C', '0', '1', 'gamelog:rolestate:view', '#', 'admin', '2021-09-07 19:50:58', '', NULL, '角色快照日志菜单'),
(2130, '角色快照日志查询', 2129, 1, '#', '', 'F', '0', '1', 'gamelog:rolestate:list', '#', 'admin', '2021-09-07 19:50:58', '', NULL, ''),
(2131, '角色快照日志导出', 2129, 2, '#', '', 'F', '0', '1', 'gamelog:rolestate:export', '#', 'admin', '2021-09-07 19:50:58', '', NULL, ''),
(2132, '邮件日志', 2061, 3, '/gamelog/maillog', '', 'C', '0', '1', 'gamelog:maillog:view', '#', 'admin', '2021-09-08 14:40:40', '', NULL, '邮件日志菜单'),
(2133, '邮件日志查询', 2132, 1, '#', '', 'F', '0', '1', 'gamelog:maillog:list', '#', 'admin', '2021-09-08 14:40:40', '', NULL, ''),
(2134, '排行榜日志', 2061, 4, '/gamelog/ranklistlog', '', 'C', '0', '1', 'gamelog:ranklistlog:view', '#', 'admin', '2021-09-08 17:18:25', '', NULL, '排行榜日志菜单'),
(2135, '排行榜日志查询', 2134, 1, '#', '', 'F', '0', '1', 'gamelog:ranklistlog:list', '#', 'admin', '2021-09-08 17:18:25', '', NULL, ''),
(2136, 'gm命令日志', 2061, 5, '/gamelog/gmcommandlog', '', 'C', '0', '1', 'gamelog:gmcommandlog:view', '#', 'admin', '2021-09-08 18:22:45', '', NULL, 'gm命令日志菜单'),
(2137, 'gm命令日志查询', 2136, 1, '#', '', 'F', '0', '1', 'gamelog:gmcommandlog:list', '#', 'admin', '2021-09-08 18:22:45', '', NULL, ''),
(2138, 'gm命令日志导出', 2136, 5, '#', '', 'F', '0', '1', 'gamelog:gmcommandlog:export', '#', 'admin', '2021-09-08 18:22:45', '', NULL, ''),
(2139, '排行榜日志导出', 2134, 5, '#', '', 'F', '0', '1', 'gamelog:ranklistlog:export', '#', 'admin', '2021-09-08 19:59:04', '', NULL, ''),
(2140, '邮件日志导出', 2132, 5, '#', '', 'F', '0', '1', 'gamelog:maillog:export', '#', 'admin', '2021-09-08 20:06:32', '', NULL, ''),
(2141, '聊天日志导出', 2068, 5, '#', '', 'F', '0', '1', 'gamelog:chatlog:export', '#', 'admin', '2021-09-08 20:22:08', '', NULL, ''),
(2147, '充值日志', 2061, 6, '/gamelog/rechargelog', '', 'C', '0', '1', 'gamelog:rechargelog:view', '#', 'admin', '2021-09-09 10:16:56', '', NULL, '充值日志菜单'),
(2148, '充值日志查询', 2147, 1, '#', '', 'F', '0', '1', 'gamelog:rechargelog:list', '#', 'admin', '2021-09-09 10:16:56', '', NULL, ''),
(2149, '充值日志导出', 2147, 5, '#', '', 'F', '0', '1', 'gamelog:rechargelog:export', '#', 'admin', '2021-09-09 10:16:56', '', NULL, ''),
(2150, '改名日志', 2061, 10, '/gamelog/changerolenamelog', '', 'C', '0', '1', 'gamelog:changerolenamelog:view', '#', 'admin', '2021-09-09 11:15:07', '', NULL, '改名日志菜单'),
(2151, '改名日志查询', 2150, 1, '#', '', 'F', '0', '1', 'gamelog:changerolenamelog:list', '#', 'admin', '2021-09-09 11:15:07', '', NULL, ''),
(2152, '改名日志导出', 2150, 5, '#', '', 'F', '0', '1', 'gamelog:changerolenamelog:export', '#', 'admin', '2021-09-09 11:15:07', '', NULL, ''),
(2156, '物品变化日志', 2061, 11, '/gamelog/itemchangelog', '', 'C', '0', '1', 'gamelog:itemchangelog:view', '#', 'admin', '2021-09-09 11:59:08', '', NULL, '物品变化日志菜单'),
(2157, '物品变化日志查询', 2156, 1, '#', '', 'F', '0', '1', 'gamelog:itemchangelog:list', '#', 'admin', '2021-09-09 11:59:08', '', NULL, ''),
(2158, '物品变化日志导出', 2156, 5, '#', '', 'F', '0', '1', 'gamelog:itemchangelog:export', '#', 'admin', '2021-09-09 11:59:08', '', NULL, ''),
(2159, '货币变化日志', 2061, 12, '/gamelog/moneychangelog', 'menuItem', 'C', '1', '1', 'gamelog:moneychangelog:view', '#', 'admin', '2021-09-09 14:17:16', 'admin', '2021-11-08 15:58:22', '货币变化日志菜单'),
(2160, '货币变化日志查询', 2159, 1, '#', '', 'F', '0', '1', 'gamelog:moneychangelog:list', '#', 'admin', '2021-09-09 14:17:16', '', NULL, ''),
(2161, '货币变化日志导出', 2159, 5, '#', '', 'F', '0', '1', 'gamelog:moneychangelog:export', '#', 'admin', '2021-09-09 14:17:16', '', NULL, ''),
(2166, '首领死亡复活日志', 2061, 13, '/gamelog/bossdierelivelog', '', 'C', '0', '1', 'gamelog:bossdierelivelog:view', '#', 'admin', '2021-09-10 09:55:51', '', NULL, '首领死亡复活日志菜单'),
(2167, '首领死亡复活日志查询', 2166, 1, '#', '', 'F', '0', '1', 'gamelog:bossdierelivelog:list', '#', 'admin', '2021-09-10 09:55:51', '', NULL, ''),
(2168, '首领死亡复活日志导出', 2166, 5, '#', '', 'F', '0', '1', 'gamelog:bossdierelivelog:export', '#', 'admin', '2021-09-10 09:55:51', '', NULL, ''),
(2169, '后台指令日志', 2061, 14, '/gamelog/backgmcmdlog', '', 'C', '0', '1', 'gamelog:backgmcmdlog:view', '#', 'admin', '2021-09-10 10:15:57', '', NULL, '后台指令日志菜单'),
(2170, '后台指令日志查询', 2169, 1, '#', '', 'F', '0', '1', 'gamelog:backgmcmdlog:list', '#', 'admin', '2021-09-10 10:15:57', '', NULL, ''),
(2171, '后台指令日志导出', 2169, 5, '#', '', 'F', '0', '1', 'gamelog:backgmcmdlog:export', '#', 'admin', '2021-09-10 10:15:57', '', NULL, ''),
(2172, '反馈日志', 2061, 15, '/gamelog/feedbacklog', '', 'C', '0', '1', 'gamelog:feedbacklog:view', '#', 'admin', '2021-09-10 11:35:51', '', NULL, '反馈日志菜单'),
(2173, '反馈日志查询', 2172, 1, '#', '', 'F', '0', '1', 'gamelog:feedbacklog:list', '#', 'admin', '2021-09-10 11:35:51', '', NULL, ''),
(2174, '反馈日志导出', 2172, 5, '#', '', 'F', '0', '1', 'gamelog:feedbacklog:export', '#', 'admin', '2021-09-10 11:35:51', '', NULL, ''),
(2175, '元宝变化日志', 2061, 16, '/gamelog/goldchangelog', '', 'C', '0', '1', 'gamelog:goldchangelog:view', '#', 'admin', '2021-09-11 15:25:08', '', NULL, '元宝变化日志菜单'),
(2176, '元宝变化日志查询', 2175, 1, '#', '', 'F', '0', '1', 'gamelog:goldchangelog:list', '#', 'admin', '2021-09-11 15:25:08', '', NULL, ''),
(2177, '元宝变化日志导出', 2175, 5, '#', '', 'F', '0', '1', 'gamelog:goldchangelog:export', '#', 'admin', '2021-09-11 15:25:08', '', NULL, ''),
(2178, '充值金额分布统计', 2046, 1, '/stat/stat_recharge_distribute', 'menuItem', 'C', '0', '1', 'stat:stat_pay_distribute:view', '#', 'admin', '2021-09-13 14:26:54', 'admin', '2021-12-22 11:13:11', '充值金额分布统计菜单'),
(2179, '充值金额分布统计查询', 2178, 1, '#', '', 'F', '0', '1', 'stat:stat_pay_distribute:list', '#', 'admin', '2021-09-13 14:26:54', '', NULL, ''),
(2180, '每日充值', 2126, 3, '/gmtool/activity/getPage?type=2', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-14 13:57:18', '', NULL, ''),
(2181, '每日登陆', 2126, 4, '/gmtool/activity/getPage?type=3', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-14 16:21:19', 'admin', '2021-09-14 16:28:43', ''),
(2182, '限购礼包', 2126, 5, '/gmtool/activity/getPage?type=4', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-14 16:32:03', '', NULL, ''),
(2183, '天帝宝库', 2126, 6, '/gmtool/activity/getPage?type=5', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-14 16:40:19', '', NULL, ''),
(2184, '累计充值', 2126, 7, '/gmtool/activity/getPage?type=6', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-14 17:10:59', '', NULL, ''),
(2185, '累计消耗', 2126, 8, '/gmtool/activity/getPage?type=7', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-14 20:58:41', '', NULL, ''),
(2186, '集物兑换', 2126, 9, '/gmtool/activity/getPage?type=8', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 10:01:12', '', NULL, ''),
(2187, '团购活动', 2126, 10, '/gmtool/activity/getPage?type=9', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 11:24:25', '', NULL, ''),
(2188, '招财猫', 2126, 11, '/gmtool/activity/getPage?type=10', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 11:31:47', '', NULL, ''),
(2189, '首领狂欢', 2126, 12, '/gmtool/activity/getPage?type=11', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 13:43:17', '', NULL, ''),
(2190, '庆典任务', 2126, 13, '/gmtool/activity/getPage?type=12', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 14:22:14', '', NULL, ''),
(2191, '节日集字', 2126, 14, '/gmtool/activity/getPage?type=13', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 14:30:28', '', NULL, ''),
(2192, '节日特惠', 2126, 15, '/gmtool/activity/getPage?type=14', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 15:20:41', '', NULL, ''),
(2193, '连续累充', 2126, 16, '/gmtool/activity/getPage?type=15', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 16:06:15', '', NULL, ''),
(2194, '限时商城', 2126, 17, '/gmtool/activity/getPage?type=16', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 16:22:19', '', NULL, ''),
(2195, '节日礼包', 2126, 18, '/gmtool/activity/getPage?type=17', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 16:24:35', '', NULL, ''),
(2196, '积分排名', 2126, 19, '/gmtool/activity/getPage?type=18', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 16:49:33', '', NULL, ''),
(2197, '节日许愿', 2126, 20, '/gmtool/activity/getPage?type=19', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 16:57:26', '', NULL, ''),
(2198, 'FB分享', 2126, 21, '/gmtool/activity/getPage?type=20', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 17:07:09', '', NULL, ''),
(2199, '连续累充2(购买礼包)', 2126, 22, '/gmtool/activity/getPage?type=21', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 17:18:26', '', NULL, ''),
(2201, '节日祝福', 2126, 23, '/gmtool/activity/getPage?type=22', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 17:32:20', '', NULL, ''),
(2202, '掷骰子', 2126, 24, '/gmtool/activity/getPage?type=23', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-15 17:50:55', '', NULL, ''),
(2203, '外观展示', 2126, 25, '/gmtool/activity/getPage?type=24', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-16 10:09:50', '', NULL, ''),
(2204, '登陆展示', 2126, 26, '/gmtool/activity/getPage?type=25', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-16 10:21:38', '', NULL, ''),
(2205, '聚宝盆', 2126, 27, '/gmtool/activity/getPage?type=26', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-16 10:30:58', '', NULL, ''),
(2206, '幸运砸蛋', 2126, 28, '/gmtool/activity/getPage?type=27', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-09-16 11:19:45', '', NULL, ''),
(2207, '游戏库列表', 2000, 1, '/gmtool/db', '', 'C', '0', '1', 'gmtool:db:view', '#', 'admin', '2021-09-08 15:26:41', '', NULL, '游戏库列表菜单'),
(2208, '游戏库列表查询', 2207, 1, '#', '', 'F', '0', '1', 'gmtool:db:list', '#', 'admin', '2021-09-08 15:26:41', '', NULL, ''),
(2209, '游戏库列表新增', 2207, 2, '#', '', 'F', '0', '1', 'gmtool:db:add', '#', 'admin', '2021-09-08 15:26:41', '', NULL, ''),
(2210, '游戏库列表修改', 2207, 3, '#', '', 'F', '0', '1', 'gmtool:db:edit', '#', 'admin', '2021-09-08 15:26:41', '', NULL, ''),
(2211, '游戏库列表删除', 2207, 4, '#', '', 'F', '0', '1', 'gmtool:db:remove', '#', 'admin', '2021-09-08 15:26:41', '', NULL, ''),
(2212, '游戏库列表导出', 2207, 5, '#', '', 'F', '0', '1', 'gmtool:db:export', '#', 'admin', '2021-09-08 15:26:41', '', NULL, ''),
(2213, '合服', 2000, 1, '/gmtool/hefu', '', 'C', '0', '1', 'gmtool:hefu:view', '#', 'admin', '2021-09-08 15:30:57', '', NULL, '合服菜单'),
(2215, '合服新增', 2213, 2, '#', '', 'F', '0', '1', 'gmtool:hefu:add', '#', 'admin', '2021-09-08 15:30:57', '', NULL, ''),
(2216, '合服修改', 2213, 3, '#', '', 'F', '0', '1', 'gmtool:hefu:edit', '#', 'admin', '2021-09-08 15:30:57', '', NULL, ''),
(2217, '合服删除', 2213, 4, '#', '', 'F', '0', '1', 'gmtool:hefu:remove', '#', 'admin', '2021-09-08 15:30:57', '', NULL, ''),
(2218, '合服导出', 2213, 5, '#', '', 'F', '0', '1', 'gmtool:hefu:export', '#', 'admin', '2021-09-08 15:30:57', '', NULL, ''),
(2219, '合服开始', 2213, 6, '#', 'menuItem', 'F', '0', '1', 'gmtool:hefu:start', '#', 'admin', '2021-09-09 11:02:38', '', NULL, ''),
(2220, '数据库备份', 2000, 1, '/gmtool/dbbak', '', 'C', '0', '1', 'gmtool:dbbak:view', '#', 'admin', '2021-09-13 15:48:29', '', NULL, '数据库备份菜单'),
(2221, '数据库备份查询', 2220, 1, '#', '', 'F', '0', '1', 'gmtool:dbbak:list', '#', 'admin', '2021-09-13 15:48:29', '', NULL, ''),
(2222, '数据库备份新增', 2220, 2, '#', '', 'F', '0', '1', 'gmtool:dbbak:add', '#', 'admin', '2021-09-13 15:48:29', '', NULL, ''),
(2223, '数据库备份修改', 2220, 3, '#', '', 'F', '0', '1', 'gmtool:dbbak:edit', '#', 'admin', '2021-09-13 15:48:29', '', NULL, ''),
(2224, '数据库备份删除', 2220, 4, '#', '', 'F', '0', '1', 'gmtool:dbbak:remove', '#', 'admin', '2021-09-13 15:48:29', '', NULL, ''),
(2225, '数据库备份导出', 2220, 5, '#', '', 'F', '0', '1', 'gmtool:dbbak:export', '#', 'admin', '2021-09-13 15:48:29', '', NULL, ''),
(2226, '抽奖幸运值', 2400, 4, '/gmtool/activityConfig/luckyValue', 'menuItem', 'C', '0', '1', 'gmtool:activityConfig:luckyValue', '#', 'admin', '2021-09-16 12:03:15', 'admin', '2021-11-30 16:04:58', ''),
(2227, '模型库', 2400, 2, '/gmtool/activityConfig/model', 'menuItem', 'C', '0', '1', 'gmtool:activityConfig:model', '#', 'admin', '2021-09-16 17:28:55', 'admin', '2021-11-30 15:37:33', ''),
(2228, '标签库', 2400, 3, '/gmtool/activityConfig/tag', 'menuItem', 'C', '0', '1', 'gmtool:activityConfig:tag', '#', 'admin', '2021-09-17 10:54:06', 'admin', '2021-11-30 15:37:53', ''),
(2229, '激活码生成', 2094, 1, '/gmtool/operation/getPage?type=1', 'menuItem', 'C', '0', '1', 'gmtool:operation:view', '#', 'admin', '2021-09-18 11:26:13', 'admin', '2021-09-18 11:34:43', ''),
(2230, '激活码查询', 2094, 2, '/gmtool/operation/getPage?type=2', 'menuItem', 'C', '0', '1', 'gmtool:operation:view', '#', 'admin', '2021-09-18 11:28:39', 'admin', '2021-09-18 14:15:03', ''),
(2231, '累计充值统计', 2046, 4, '/stat/stat_recharge_accumulate/to_stat_recharge_accumulate', 'menuItem', 'C', '0', '1', '', '#', 'admin', '2021-09-22 17:41:13', 'admin', '2021-12-10 16:51:17', ''),
(2237, '商城配置', 2094, 3, '/gmtool/shop', 'menuItem', 'C', '1', '1', 'gmtool:shop:view', '#', 'admin', '2021-09-23 17:38:21', 'admin', '2021-11-06 14:20:30', ''),
(2238, 'DAU统计', 2046, 5, '/stat/stat_dau/to_stat_dau', 'menuItem', 'C', '0', '1', NULL, '#', 'admin', '2021-09-27 15:07:07', '', NULL, ''),
(2239, '付费次数统计', 2046, 5, '/stat/stat_recharge_counts/to_stat_recharge_counts', 'menuItem', 'C', '0', '1', '', '#', 'admin', '2021-09-27 17:13:28', 'admin', '2021-09-27 17:47:05', ''),
(2240, '商城购买统计', 2046, 6, '/stat/stat_shop_item/to_stat_shop_item', 'menuItem', 'C', '0', '1', NULL, '#', 'admin', '2021-09-29 15:06:02', '', NULL, ''),
(2242, '二次付费统计', 2046, 7, '/stat/stat_recharge_second/to_stat_recharge_second', 'menuItem', 'C', '0', '1', NULL, '#', 'admin', '2021-09-30 16:44:27', '', NULL, ''),
(2243, '游戏角色信息', 2094, 6, '/gmtool/gamerole', 'menuItem', 'C', '0', '1', 'gmtool:gamerole:view', '#', 'admin', '2021-09-07 19:50:58', 'admin', '2021-10-30 15:13:46', '游戏角色查询菜单'),
(2244, '游戏角色信息查询', 2243, 1, '#', 'menuItem', 'F', '0', '1', 'gmtool:gamerole:list', '#', 'admin', '2021-09-07 19:50:58', 'admin', '2021-10-30 15:12:55', ''),
(2245, '灵玉用途统计', 2046, 8, '/stat/stat_gold_purpose/to_stat_gold_purpose', 'menuItem', 'C', '0', '1', '', '#', 'admin', '2021-10-12 19:44:21', 'admin', '2021-12-10 16:52:46', ''),
(2246, '系统开关', 2000, 8, '/gmtool/function', 'menuItem', 'C', '0', '1', 'gmtool:function:view', '#', 'admin', '2021-10-12 19:44:21', 'admin', '2021-12-01 14:04:46', '系统开关'),
(2247, '全服角色信息统计', 2046, 8, '/stat/stat_role/to_stat_role', 'menuItem', 'C', '0', '1', NULL, '#', 'admin', '2021-10-15 15:54:32', '', NULL, ''),
(2248, '在线信息(全服)', 2046, 9, '/stat/stat_online/to_stat_all_online_count_list', 'menuItem', 'C', '0', '1', NULL, '#', 'admin', '2021-10-16 18:16:43', '', NULL, ''),
(2249, '玩家流失统计', 2046, 9, '/stat/stat_churn_rate/to_stat_churn_rate', 'menuItem', 'C', '0', '1', NULL, '#', 'admin', '2021-10-20 16:03:03', '', NULL, ''),
(2250, '公告管理', 2093, 4, '#', 'menuItem', 'M', '0', '1', NULL, '#', 'admin', '2021-10-21 15:22:46', '', NULL, ''),
(2251, '即时公告', 2250, 1, '/gmtool/announce', 'menuItem', 'C', '0', '1', 'gmtool:announce:view', '#', 'admin', '2021-10-21 15:49:51', '', NULL, ''),
(2252, '幸运宝玉', 2126, 29, '/gmtool/activity/getPage?type=28', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-10-27 14:24:34', 'admin', '2021-10-27 14:25:59', ''),
(2253, '循环公告', 2250, 2, '/gmtool/cyAnnounce', 'menuItem', 'C', '0', '1', 'gmtool:cyAnnounce:view', '#', 'admin', '2021-10-27 17:47:02', 'admin', '2021-10-28 09:25:57', ''),
(2254, '更新公告', 2250, 3, '/gmtool/updateNotice', 'menuItem', 'C', '0', '1', 'gmtool:updateNotice:view', '#', 'admin', '2021-10-30 10:53:37', '', NULL, ''),
(2255, '付费总览', 2046, 10, '/stat/stat_recharge_overview/to_stat_recharge_overview', 'menuItem', 'C', '1', '1', '', '#', 'admin', '2021-10-30 16:12:35', 'admin', '2021-12-10 16:48:29', ''),
(2256, '道具扣除', 2094, 6, '/gmtool/deductItem', 'menuItem', 'C', '0', '1', 'gmtool:deductItem:view', '#', 'admin', '2021-11-01 11:35:53', '', NULL, ''),
(2257, '修改属性', 2094, 7, '/gmtool/roleAttr', 'menuItem', 'C', '0', '1', 'gmtool:roleAttr:view', '#', 'admin', '2021-11-02 11:29:27', '', NULL, ''),
(2258, '日常数据', 2046, 11, '/stat/stat_daily_data/to_stat_daily_data', 'menuItem', 'C', '0', '1', NULL, '#', 'admin', '2021-11-02 19:47:42', '', NULL, ''),
(2259, '角色转移', 2094, 8, '/gmtool/roleTransfer', 'menuItem', 'C', '0', '1', 'gmtool:roleTransfer:view', '#', 'admin', '2021-11-03 16:17:50', '', NULL, ''),
(2260, '统计黑名单', 2046, 14, '/gmtool/blackuser', 'menuItem', 'C', '0', '1', 'gmtool:blackuser:view', '#', 'admin', '2021-11-04 11:26:43', 'admin', '2021-12-29 16:36:01', ''),
(2261, '评价开关', 2094, 10, '/gmtool/evaluate', 'menuItem', 'C', '0', '1', 'gmtool:evaluate:view', '#', 'admin', '2021-11-04 17:42:39', '', NULL, ''),
(2262, '单个连接的数据库查询', 2000, 9, '/gmtool/server/customSqlPage', 'menuItem', 'C', '0', '1', 'gmtool:server:customSqlPage', '#', 'admin', '2021-11-05 15:16:20', 'admin', '2021-11-05 15:17:23', ''),
(2263, '货币变化日志', 2061, 17, '/gamelog/coinchangelog', '', 'C', '0', '1', 'gamelog:coinchangelog:view', '#', 'admin', '2021-11-08 15:16:58', '', NULL, '货币变化日志菜单'),
(2264, '货币变化日志查询', 2263, 1, '#', '', 'F', '0', '1', 'gamelog:coinchangelog:list', '#', 'admin', '2021-11-08 15:16:58', '', NULL, ''),
(2265, '货币变化日志导出', 2263, 5, '#', '', 'F', '0', '1', 'gamelog:coinchangelog:export', '#', 'admin', '2021-11-08 15:16:58', '', NULL, ''),
(2266, '排行榜', 2092, 1, '#', 'menuItem', 'M', '0', '1', NULL, '#', 'admin', '2021-11-10 11:16:28', '', NULL, ''),
(2267, '实时排行榜查询', 2266, 1, '/gmquerydata/rankinglist', 'menuItem', 'C', '0', '1', 'gmquerydata:rankinglist:view', '#', 'admin', '2021-11-10 11:18:07', '', NULL, ''),
(2268, '实时排行榜查询', 2267, 1, '#', 'menuItem', 'F', '0', '1', 'gmquerydata:rankinglist:list', '#', 'admin', '2021-11-10 11:19:27', '', NULL, ''),
(2269, '充值排行榜', 2266, 2, '/gmquerydata/rechargerank/to_rechargerank', 'menuItem', 'C', '0', '1', '', '#', 'admin', '2021-11-12 16:35:58', 'admin', '2021-11-12 17:43:17', ''),
(2270, '游戏参数信息', 2000, 1, '/gmtool/gameInfo', '', 'C', '0', '1', 'gmtool:gameInfo:view', '#', 'admin', '2021-11-15 16:54:43', '', NULL, '游戏参数信息菜单'),
(2271, '游戏参数信息查询', 2270, 1, '#', '', 'F', '0', '1', 'gmtool:gameInfo:list', '#', 'admin', '2021-11-15 16:54:43', '', NULL, ''),
(2272, '游戏参数信息新增', 2270, 2, '#', '', 'F', '0', '1', 'gmtool:gameInfo:add', '#', 'admin', '2021-11-15 16:54:43', '', NULL, ''),
(2273, '游戏参数信息修改', 2270, 3, '#', '', 'F', '0', '1', 'gmtool:gameInfo:edit', '#', 'admin', '2021-11-15 16:54:43', '', NULL, ''),
(2274, '游戏参数信息删除', 2270, 4, '#', '', 'F', '0', '1', 'gmtool:gameInfo:remove', '#', 'admin', '2021-11-15 16:54:43', '', NULL, ''),
(2275, '游戏参数信息导出', 2270, 5, '#', '', 'F', '0', '1', 'gmtool:gameInfo:export', '#', 'admin', '2021-11-15 16:54:43', '', NULL, ''),
(2276, '踢人下线', 2094, 11, '/gmtool/kick', '', 'C', '0', '1', 'gmtool:kick:view', '#', 'admin', '2021-11-20 19:15:09', '', NULL, '踢人下线菜单'),
(2280, '封号禁言', 2093, 5, '#', 'menuItem', 'M', '0', '1', '', '#', 'admin', '2021-11-18 20:47:33', 'admin', '2021-11-18 20:48:04', ''),
(2281, '账号解封', 2280, 2, '/gmtool/banAccount/unBanAccount', '', 'C', '0', '1', 'gmtool:unBanAccount:view', '#', 'admin', '2021-11-18 20:47:33', '', NULL, '账号解封菜单'),
(2282, '账号封禁', 2280, 1, '/gmtool/banAccount', '', 'C', '0', '1', 'gmtool:banAccount:view', '#', 'admin', '2021-11-18 20:57:44', '', NULL, '账号封禁菜单'),
(2283, '账号封禁查询', 2282, 1, '#', '', 'F', '0', '1', 'gmtool:banAccount:list', '#', 'admin', '2021-11-18 20:57:44', '', NULL, ''),
(2284, '账号封禁新增', 2282, 2, '#', '', 'F', '0', '1', 'gmtool:banAccount:add', '#', 'admin', '2021-11-18 20:57:44', '', NULL, ''),
(2285, '账号封禁修改', 2282, 3, '#', '', 'F', '0', '1', 'gmtool:banAccount:edit', '#', 'admin', '2021-11-18 20:57:44', '', NULL, ''),
(2286, '账号封禁删除', 2282, 4, '#', '', 'F', '0', '1', 'gmtool:banAccount:remove', '#', 'admin', '2021-11-18 20:57:44', '', NULL, ''),
(2287, '账号封禁导出', 2282, 5, '#', '', 'F', '0', '1', 'gmtool:banAccount:export', '#', 'admin', '2021-11-18 20:57:44', '', NULL, ''),
(2288, '聊天封禁', 2280, 3, '/gmtool/banChat', '', 'C', '0', '1', 'gmtool:banChat:view', '#', 'admin', '2021-11-20 19:15:09', '', NULL, '聊天封禁菜单'),
(2289, '聊天封禁查询', 2288, 1, '#', '', 'F', '0', '1', 'gmtool:banChat:list', '#', 'admin', '2021-11-20 19:15:09', '', NULL, ''),
(2290, '聊天封禁新增', 2288, 2, '#', '', 'F', '0', '1', 'gmtool:banChat:add', '#', 'admin', '2021-11-20 19:15:09', '', NULL, ''),
(2291, '聊天封禁修改', 2288, 3, '#', '', 'F', '0', '1', 'gmtool:banChat:edit', '#', 'admin', '2021-11-20 19:15:09', '', NULL, ''),
(2292, '聊天封禁删除', 2288, 4, '#', '', 'F', '0', '1', 'gmtool:banChat:remove', '#', 'admin', '2021-11-20 19:15:09', '', NULL, ''),
(2293, '聊天封禁导出', 2288, 5, '#', '', 'F', '0', '1', 'gmtool:banChat:export', '#', 'admin', '2021-11-20 19:15:09', '', NULL, ''),
(2294, '聊天解封', 2280, 4, '/gmtool/banChat/unBanChat', '', 'C', '0', '1', 'gmtool:unBanChat:view', '#', 'admin', '2021-11-20 19:15:09', '', NULL, '聊天解封菜单'),
(2295, '聊天屏蔽字', 2280, 5, '/gmtool/banChat/shieldWord', '', 'C', '0', '1', 'gmtool:shieldWord:view', '#', 'admin', '2021-11-20 19:15:09', '', NULL, '聊天屏蔽字菜单'),
(2296, '聊天替换字', 2280, 6, '/gmtool/banChat/replaceWord', '', 'C', '0', '1', 'gmtool:replaceWord:view', '#', 'admin', '2021-11-20 19:15:09', '', NULL, '聊天替换菜单'),
(2297, '聊天黑名单', 2280, 7, '/gmtool/banChat/blackList', '', 'C', '0', '1', 'gmtool:blackList:view', '#', 'admin', '2021-11-20 19:15:09', '', NULL, '聊天黑名单菜单'),
(2302, '方泽探宝', 2126, 33, '/gmtool/activity/getPage?type=29', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-11-23 12:00:16', '', NULL, ''),
(2303, '后台模拟充值', 2093, 1, '#', 'menuItem', 'M', '0', '1', NULL, '#', 'admin', '2021-11-23 12:00:16', '', NULL, ''),
(2304, '后台模拟充值', 2303, 1, '/gmtool/recharge', '', 'C', '0', '1', 'gmtool:recharge:view', '#', 'admin', '2021-11-23 12:00:16', '', NULL, '后台模拟充值菜单'),
(2305, '后台模拟充值查询', 2304, 1, '#', '', 'F', '0', '1', 'gmtool:recharge:list', '#', 'admin', '2021-11-23 12:00:16', '', NULL, ''),
(2306, '后台模拟充值新增', 2304, 2, '#', '', 'F', '0', '1', 'gmtool:recharge:add', '#', 'admin', '2021-11-23 12:00:16', '', NULL, ''),
(2307, '后台模拟充值审核', 2304, 3, '#', '', 'F', '0', '1', 'gmtool:recharge:verify', '#', 'admin', '2021-11-23 12:00:16', '', NULL, ''),
(2308, '后台模拟充值删除', 2304, 4, '#', '', 'F', '0', '1', 'gmtool:recharge:remove', '#', 'admin', '2021-11-23 12:00:16', '', NULL, ''),
(2309, '后台模拟充值导出', 2304, 5, '#', '', 'F', '0', '1', 'gmtool:recharge:export', '#', 'admin', '2021-11-23 12:00:16', '', NULL, ''),
(2394, '白名单管理', 2094, 12, '/gmtool/white', '', 'C', '0', '1', 'gmtool:white:view', '#', 'admin', '2021-11-20 19:15:09', '', NULL, '白名单管理菜单'),
(2395, '白名单管理查询', 2394, 1, '#', '', 'F', '0', '1', 'gmtool:white:list', '#', 'admin', '2021-11-20 19:15:09', '', NULL, ''),
(2396, '白名单管理新增', 2394, 2, '#', '', 'F', '0', '1', 'gmtool:white:add', '#', 'admin', '2021-11-20 19:15:09', '', NULL, ''),
(2397, '白名单管理修改', 2394, 3, '#', '', 'F', '0', '1', 'gmtool:white:edit', '#', 'admin', '2021-11-20 19:15:09', '', NULL, ''),
(2398, '白名单管理删除', 2394, 4, '#', '', 'F', '0', '1', 'gmtool:white:remove', '#', 'admin', '2021-11-20 19:15:09', '', NULL, ''),
(2399, '白名单管理导出', 2394, 5, '#', '', 'F', '0', '1', 'gmtool:white:export', '#', 'admin', '2021-11-20 19:15:09', '', NULL, ''),
(2400, '活动管理', 2093, 6, '#', 'menuItem', 'M', '0', '1', NULL, '#', 'admin', '2021-11-30 15:34:43', '', NULL, ''),
(2401, '活动模板库', 2400, 5, '/gmtool/activityTemplate', 'menuItem', 'C', '0', '1', 'gmtool:activityTemplate:view', '#', 'admin', '2021-12-01 10:26:57', '', NULL, ''),
(2402, '留存LTV多维度分析', 2046, 12, '/stat/stat_remain_ltv', 'menuItem', 'C', '0', '1', 'stat:stat_remain_ltv:view', '#', 'admin', '2021-12-02 20:54:30', '', NULL, ''),
(2403, '公会基础信息', 2061, 7, '/gamelog/guildbaselog/guildbaselog', '', 'C', '0', '1', 'gamelog:guildbaselog:view', '#', 'admin', '2021-12-06 23:17:58', '', NULL, '公会基础信息菜单'),
(2404, '公会基础信息查询', 2403, 1, '#', '', 'F', '0', '1', 'gamelog:guildbaselog:list', '#', 'admin', '2021-12-06 23:17:58', '', NULL, ''),
(2405, '公会基础信息导出', 2403, 5, '#', '', 'F', '0', '1', 'gamelog:guildbaselog:export', '#', 'admin', '2021-12-06 23:17:58', '', NULL, ''),
(2406, '公会成员信息', 2061, 8, '/gamelog/guildbaselog/guildmemberlog', '', 'C', '0', '1', 'gamelog:guildmemberlog:view', '#', 'admin', '2021-12-06 23:17:58', '', NULL, '公会基础信息菜单'),
(2407, '公会成员信息查询', 2403, 1, '#', '', 'F', '0', '1', 'gamelog:guildbaselog:list', '#', 'admin', '2021-12-06 23:17:58', '', NULL, ''),
(2408, '公会成员信息导出', 2403, 5, '#', '', 'F', '0', '1', 'gamelog:guildbaselog:export', '#', 'admin', '2021-12-06 23:17:58', '', NULL, ''),
(2409, '公会动态日志', 2061, 9, '/gamelog/guildbaselog/guildchangelog', '', 'C', '0', '1', 'gamelog:guildchangelog:view', '#', 'admin', '2021-12-06 23:17:58', '', NULL, '公会基础信息菜单'),
(2410, '公会动态信息查询', 2403, 1, '#', '', 'F', '0', '1', 'gamelog:guildbaselog:list', '#', 'admin', '2021-12-06 23:17:58', '', NULL, ''),
(2411, '公会动态信息导出', 2403, 5, '#', '', 'F', '0', '1', 'gamelog:guildbaselog:export', '#', 'admin', '2021-12-06 23:17:58', '', NULL, ''),
(2412, '仙境探宝', 2126, 34, '/gmtool/activity/getPage?type=30', 'menuItem', 'C', '0', '1', 'gmtool:activity:view', '#', 'admin', '2021-12-07 20:27:27', '', NULL, ''),
(2413, '激活码使用查询', 2094, 3, '/gmtool/operation/getPage?type=3', 'menuItem', 'C', '0', '1', 'gmtool:operation:view', '#', 'admin', '2021-12-20 10:30:30', '', NULL, ''),
(2414, '聊天监控', 2061, 0, '/gamelog/chatlog/to_chatlog_monitor', 'menuItem', 'C', '0', '1', NULL, '#', 'admin', '2021-12-23 10:25:31', '', NULL, ''),
(2415, '服务器运营活动操作', 2400, 7, '/gmtool/activityDeal', 'menuItem', 'C', '0', '1', 'gmtool:activityDeal:view', '#', 'admin', '2021-09-07 17:54:39', 'admin', '2021-11-30 15:36:28', ''),
(2416, '日志收集记录', 2046, 13, '/stat/stat_last_insert/to_stat_last_insert', 'menuItem', 'C', '0', '1', NULL, '#', 'admin', '2021-12-27 14:51:03', '', NULL, '');
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
