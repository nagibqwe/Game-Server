-- ------------------------------------------
-- Database: tzj_platformkits
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



-- ================九零一起玩 www.90175.com框架Quartz任务调度相关的表============================

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
-- 32.Table structure for table `sdk_apiserver`
-- ----------------------------
DROP TABLE IF EXISTS `sdk_apiserver`;
CREATE TABLE `sdk_apiserver` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `api_url` varchar(500) DEFAULT NULL COMMENT 'APIServer地址',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台逻辑:APIServer地址';


-- ----------------------------
-- 33.Table structure for table `sdk_channel`
-- ----------------------------
DROP TABLE IF EXISTS `sdk_channel`;
CREATE TABLE `sdk_channel` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `channel_id` bigint(20) DEFAULT NULL COMMENT '渠道ID',
  `channel_name` varchar(128) DEFAULT NULL COMMENT '渠道名',
  `is_use` int(11) DEFAULT '0' COMMENT '是否被占用 0-未占用  1-已占用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `channel_id` (`channel_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台逻辑:渠道信息';

-- ----------------------------
-- 34.Table structure for table `sdk_channelid_channelids`
-- ----------------------------
DROP TABLE IF EXISTS `sdk_channelid_channelids`;

CREATE TABLE `sdk_channelid_channelids` (
  `channel_id` bigint(20) NOT NULL COMMENT '渠道ID',
  `channel_ids` varchar(500) DEFAULT NULL COMMENT '渠道ID列表',
  PRIMARY KEY (`channel_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台逻辑:服务器列表中的渠道ID和渠道ID列表关系对应';

-- ----------------------------
-- 35.Table structure for table `sdk_notice`
-- ----------------------------
DROP TABLE IF EXISTS `sdk_notice`;
CREATE TABLE `sdk_notice` (
  `notice_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_name` varchar(128) DEFAULT NULL COMMENT '公告名',
  `notice_type` int(11) DEFAULT NULL COMMENT '公告类型(0:更新公告 1:登陆公告 2:活动公告)',
  `notice_content` text COMMENT '公告内容',
  `channel` text COMMENT '渠道',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `auto` int(11) DEFAULT NULL COMMENT '是否自动弹出 0:不自动弹出 1:自动弹出',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` int(11) DEFAULT NULL COMMENT '0:停用 1:启用',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(128) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` varchar(128) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台逻辑:公告管理';

-- ----------------------------
-- 36.Table structure for table `sdk_server`
-- ----------------------------
DROP TABLE IF EXISTS `sdk_server`;
CREATE TABLE `sdk_server` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `server_id` bigint(20) NOT NULL COMMENT '服务器ID',
  `server_name` varchar(50) DEFAULT NULL COMMENT '服务器名称',
  `server_ip` varchar(50) DEFAULT NULL COMMENT '服务器IP',
  `server_port` int(11) DEFAULT NULL COMMENT '端口',
  `open_state` int(11) DEFAULT NULL COMMENT '服务器状态 0:备服状态 1:开服状态',
  `is_backup` int(11) DEFAULT '0' COMMENT '是否为备服状态',
  `server_extconfig` text COMMENT '扩展数据',
  `update_time` varchar(128) DEFAULT NULL COMMENT '更新时间',
  `create_time` varchar(128) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `server_id` (`server_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台逻辑:服务器配置信息';

-- ----------------------------
-- 37.Table structure for table `sdk_server_extra`
-- ----------------------------
DROP TABLE IF EXISTS `sdk_server_extra`;
CREATE TABLE `sdk_server_extra` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `server_list_id` bigint(20) NOT NULL COMMENT '服务器列表ID',
  `server_id` bigint(20) NOT NULL COMMENT '服务器ID',
  `sort_id` int(11) DEFAULT '0' COMMENT '排序值',
  `server_status` int(11) DEFAULT NULL COMMENT '状态 0-未开放 1-开放中 2-维护中',
  `group_type` int(11) DEFAULT NULL COMMENT '游戏服类型 0-测试 1-先锋 2-正式',
  `server_label` varchar(255) DEFAULT NULL COMMENT '热度标签 0-正常  1-爆满  2-推荐  3-新服',
  `sce_id` varchar(255) DEFAULT NULL COMMENT '场景ID',
  `appVersion` varchar(255) DEFAULT NULL COMMENT '客户端版本ID',
  `update_time` varchar(128) DEFAULT NULL COMMENT '更新时间',
  `create_time` varchar(128) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`server_list_id`,`server_id`) USING BTREE,
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台逻辑:服务器额外信息';

-- ----------------------------
-- 38.Table structure for table `sdk_server_list`
-- ----------------------------
DROP TABLE IF EXISTS `sdk_server_list`;
CREATE TABLE `sdk_server_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(128) DEFAULT NULL COMMENT '服务器列表名称',
  `login_server_group` text COMMENT '登录服务器组信息',
  `channel_ids` varchar(500) DEFAULT NULL COMMENT '渠道ID列表',
  `server_ids` varchar(500) DEFAULT NULL COMMENT '服务器ID列表',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `update_time` varchar(128) DEFAULT NULL COMMENT '更新时间',
  `create_time` varchar(128) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台逻辑:服务器列表';


-- ----------------------------
-- 39.Table structure for table `sdk_white`
-- ----------------------------
DROP TABLE IF EXISTS `sdk_white`;
CREATE TABLE `sdk_white` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `white_name` varchar(128) DEFAULT '' COMMENT '白名单账号',
  `tips` text COMMENT '备注',
  `update_time` varchar(128) DEFAULT NULL COMMENT '更新时间',
  `create_time` varchar(128) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `white_name` (`white_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台逻辑:白名单';


-- ----------------------------
-- 40.Table structure for table `sdk_login_server`
-- ----------------------------
DROP TABLE IF EXISTS `sdk_login_server`;
CREATE TABLE `sdk_login_server` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `login_server_name` varchar(50) DEFAULT NULL COMMENT '登录服务器名称',
  `login_server_ip` varchar(128) DEFAULT NULL COMMENT '登录服务器IP',
  `login_server_port` int(11) DEFAULT NULL COMMENT '登录服端口',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录服信息';

-- ----------------------------
-- 41.Table structure for table `sdk_server_update`
-- ----------------------------
DROP TABLE IF EXISTS `sdk_server_update`;
CREATE TABLE `sdk_server_update` (
  `updateTime` bigint(20) DEFAULT NULL COMMENT '服务器信息修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务器信息修改时间记录';


-- =====================================================--------------

-- =====================================================
-- Initialize database data
-- =====================================================


-- ----------------------------
-- 1.Data for the table `qrtz_cron_triggers`
-- ----------------------------
INSERT INTO qrtz_cron_triggers(sched_name, trigger_name, trigger_group, cron_expression, time_zone_id) VALUES
('RuoyiScheduler', 'TASK_CLASS_NAME1', 'DEFAULT', '0/10 * * * * ?', 'Asia/Shanghai'),
('RuoyiScheduler', 'TASK_CLASS_NAME2', 'DEFAULT', '0/15 * * * * ?', 'Asia/Shanghai'),
('RuoyiScheduler', 'TASK_CLASS_NAME3', 'DEFAULT', '0/20 * * * * ?', 'Asia/Shanghai'),
('RuoyiScheduler', 'TASK_CLASS_NAME4', 'DEFAULT', '0/15 * * * * ?', 'Asia/Shanghai'),
('RuoyiScheduler', 'TASK_CLASS_NAME5', 'DEFAULT', '0/5 * * * * ?', 'Asia/Shanghai');

-- ----------------------------
-- 2.Data for the table `qrtz_job_details`
-- ----------------------------
INSERT INTO qrtz_job_details(sched_name, job_name, job_group, description, job_class_name, is_durable, is_nonconcurrent, is_update_data, requests_recovery, job_data) VALUES
('RuoyiScheduler', 'TASK_CLASS_NAME1', 'DEFAULT', NULL, 'com.kits.project.monitor.job.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', x'ACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000F5441534B5F50524F5045525449455373720027636F6D2E6B6974732E70726F6A6563742E6D6F6E69746F722E6A6F622E646F6D61696E2E4A6F6200000000000000010200084C000A636F6E63757272656E747400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C000C696E766F6B6554617267657471007E00094C00086A6F6247726F757071007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C00076A6F624E616D6571007E00094C000D6D697366697265506F6C69637971007E00094C000673746174757371007E000978720028636F6D2E6B6974732E6672616D65776F726B2E7765622E646F6D61696E2E42617365456E7469747900000000000000010200074C0008637265617465427971007E00094C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C0006706172616D7371007E00034C000672656D61726B71007E00094C000B73656172636856616C756571007E00094C0008757064617465427971007E00094C000A75706461746554696D6571007E000C787074000561646D696E7372000E6A6176612E7574696C2E44617465686A81014B5974190300007870770800000178F95A0E60787074000070707074000131740002313074001172795461736B2E72794E6F506172616D7374000744454641554C547372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000000001740018E7B3BBE7BB9FE9BB98E8AEA4EFBC88E697A0E58F82EFBC8974000133740001317800'),
('RuoyiScheduler', 'TASK_CLASS_NAME2', 'DEFAULT', NULL, 'com.kits.project.monitor.job.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', x'ACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000F5441534B5F50524F5045525449455373720027636F6D2E6B6974732E70726F6A6563742E6D6F6E69746F722E6A6F622E646F6D61696E2E4A6F6200000000000000010200084C000A636F6E63757272656E747400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C000C696E766F6B6554617267657471007E00094C00086A6F6247726F757071007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C00076A6F624E616D6571007E00094C000D6D697366697265506F6C69637971007E00094C000673746174757371007E000978720028636F6D2E6B6974732E6672616D65776F726B2E7765622E646F6D61696E2E42617365456E7469747900000000000000010200074C0008637265617465427971007E00094C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C0006706172616D7371007E00034C000672656D61726B71007E00094C000B73656172636856616C756571007E00094C0008757064617465427971007E00094C000A75706461746554696D6571007E000C787074000561646D696E7372000E6A6176612E7574696C2E44617465686A81014B5974190300007870770800000178F95A0E60787074000070707074000131740002313574001572795461736B2E7279506172616D7328277279272974000744454641554C547372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000000002740018E7B3BBE7BB9FE9BB98E8AEA4EFBC88E69C89E58F82EFBC8974000133740001317800'),
('RuoyiScheduler', 'TASK_CLASS_NAME3', 'DEFAULT', NULL, 'com.kits.project.monitor.job.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', x'ACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000F5441534B5F50524F5045525449455373720027636F6D2E6B6974732E70726F6A6563742E6D6F6E69746F722E6A6F622E646F6D61696E2E4A6F6200000000000000010200084C000A636F6E63757272656E747400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C000C696E766F6B6554617267657471007E00094C00086A6F6247726F757071007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C00076A6F624E616D6571007E00094C000D6D697366697265506F6C69637971007E00094C000673746174757371007E000978720028636F6D2E6B6974732E6672616D65776F726B2E7765622E646F6D61696E2E42617365456E7469747900000000000000010200074C0008637265617465427971007E00094C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C0006706172616D7371007E00034C000672656D61726B71007E00094C000B73656172636856616C756571007E00094C0008757064617465427971007E00094C000A75706461746554696D6571007E000C787074000561646D696E7372000E6A6176612E7574696C2E44617465686A81014B5974190300007870770800000178F95A0E60787074000070707074000131740002323074003872795461736B2E72794D756C7469706C65506172616D7328277279272C20747275652C20323030304C2C203331362E3530442C203130302974000744454641554C547372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000000003740018E7B3BBE7BB9FE9BB98E8AEA4EFBC88E5A49AE58F82EFBC8974000133740001317800'),
('RuoyiScheduler', 'TASK_CLASS_NAME4', 'DEFAULT', NULL, 'com.kits.project.monitor.job.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', x'ACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000F5441534B5F50524F5045525449455373720027636F6D2E6B6974732E70726F6A6563742E6D6F6E69746F722E6A6F622E646F6D61696E2E4A6F6200000000000000010200084C000A636F6E63757272656E747400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C000C696E766F6B6554617267657471007E00094C00086A6F6247726F757071007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C00076A6F624E616D6571007E00094C000D6D697366697265506F6C69637971007E00094C000673746174757371007E000978720028636F6D2E6B6974732E6672616D65776F726B2E7765622E646F6D61696E2E42617365456E7469747900000000000000010200074C0008637265617465427971007E00094C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C0006706172616D7371007E00034C000672656D61726B71007E00094C000B73656172636856616C756571007E00094C0008757064617465427971007E00094C000A75706461746554696D6571007E000C787074000561646D696E7372000E6A6176612E7574696C2E44617465686A81014B59741903000078707708000001794B620E60787074000070707074000131740002313574001572795461736B2E676574536572766572537461746574000744454641554C547372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000000004740027E59091415049536572767265E8AFB7E6B182E69C8DE58AA1E599A8E78AB6E68081E4BFA1E681AF74000133740001307800'),
('RuoyiScheduler', 'TASK_CLASS_NAME5', 'DEFAULT', NULL, 'com.kits.project.monitor.job.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', x'ACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000F5441534B5F50524F5045525449455373720027636F6D2E6B6974732E70726F6A6563742E6D6F6E69746F722E6A6F622E646F6D61696E2E4A6F6200000000000000010200084C000A636F6E63757272656E747400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C000C696E766F6B6554617267657471007E00094C00086A6F6247726F757071007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C00076A6F624E616D6571007E00094C000D6D697366697265506F6C69637971007E00094C000673746174757371007E000978720028636F6D2E6B6974732E6672616D65776F726B2E7765622E646F6D61696E2E42617365456E7469747900000000000000010200074C0008637265617465427971007E00094C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C0006706172616D7371007E00034C000672656D61726B71007E00094C000B73656172636856616C756571007E00094C0008757064617465427971007E00094C000A75706461746554696D6571007E000C787074000561646D696E7372000E6A6176612E7574696C2E44617465686A81014B597419030000787077080000017D98D56E407870740000707070740001317400013574001972795461736B2E72656672657368536572766572436163686574000744454641554C547372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000000005740027E5AE9AE697B6E588B7E696B0E69C8DE58AA1E599A8E9858DE7BDAEE4BFA1E681AFE7BC93E5AD9874000131740001307800');


-- ----------------------------
-- 3.Data for the table `qrtz_locks`
-- ----------------------------
insert  into `qrtz_locks`(`sched_name`,`lock_name`) values 
('RuoyiScheduler','STATE_ACCESS'),('RuoyiScheduler','TRIGGER_ACCESS');


-- ----------------------------
-- 4.Data for the table `qrtz_scheduler_state`
-- ----------------------------
INSERT INTO qrtz_scheduler_state(sched_name, instance_name, last_checkin_time, checkin_interval) VALUES
('RuoyiScheduler', 'QX-20210226VRFN1638952483993', 1638955771958, 15000);


-- ----------------------------
-- 5.Data for the table `sys_config`
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
-- 6.Data for the table `sys_dict_data`
-- ----------------------------
insert into sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) values
(1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2021-04-22 19:32:02', '', NULL, '性别男'),
(2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2021-04-22 19:32:02', '', NULL, '性别女'),
(3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2021-04-22 19:32:02', '', NULL, '性别未知'),
(4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2021-04-22 19:32:02', '', NULL, '显示菜单'),
(5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:02', '', NULL, '隐藏菜单'),
(6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2021-04-22 19:32:02', '', NULL, '正常状态'),
(7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '停用状态'),
(8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '正常状态'),
(9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '停用状态'),
(10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '默认分组'),
(11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '系统分组'),
(12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '系统默认是'),
(13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '系统默认否'),
(14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '通知'),
(15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '公告'),
(16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '正常状态'),
(17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '关闭状态'),
(18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '其他操作'),
(19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '新增操作'),
(20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '修改操作'),
(21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '删除操作'),
(22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2021-04-22 19:32:03', '', NULL, '授权操作'),
(23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2021-04-22 19:32:04', '', NULL, '导出操作'),
(24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2021-04-22 19:32:04', '', NULL, '导入操作'),
(25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:04', '', NULL, '强退操作'),
(26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2021-04-22 19:32:04', '', NULL, '生成操作'),
(27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:04', '', NULL, '清空操作'),
(28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2021-04-22 19:32:04', '', NULL, '正常状态'),
(29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2021-04-22 19:32:04', '', NULL, '停用状态'),
(100, 1, '测试服', '0', 'server_type', NULL, 'default', 'Y', '0', 'admin', '2021-04-23 13:23:02', '', NULL, NULL),
(101, 2, '正式服', '1', 'server_type', NULL, 'default', 'Y', '0', 'admin', '2021-04-23 13:23:21', '', NULL, NULL),
(102, 3, '登录服', '2', 'server_type', NULL, 'default', 'Y', '0', 'admin', '2021-04-23 13:23:38', '', NULL, NULL),
(103, 3, '跨服', '3', 'server_type', NULL, 'default', 'Y', '0', 'admin', '2021-04-23 13:23:55', '', NULL, NULL),
(104, 1, '游戏库', '0', 'db_log_type', NULL, 'default', 'Y', '0', 'admin', '2021-04-23 13:25:33', '', NULL, NULL),
(105, 2, '日志库', '1', 'db_log_type', NULL, 'default', 'Y', '0', 'admin', '2021-04-23 13:25:50', '', NULL, NULL),
(106, 1, '未合服', '0', 'he_fu_type', NULL, 'default', 'Y', '0', 'admin', '2021-04-23 13:28:41', '', NULL, NULL),
(107, 2, '已合服', '1', 'he_fu_type', '', 'default', 'Y', '0', 'admin', '2021-04-23 13:28:53', 'admin', '2021-04-23 13:29:03', ''),
(108, 1, '启用', '0', 'deleted_type', NULL, 'default', 'Y', '0', 'admin', '2021-04-23 13:29:53', '', NULL, NULL),
(109, 2, '删除', '1', 'deleted_type', NULL, 'default', 'Y', '0', 'admin', '2021-04-23 13:30:06', '', NULL, NULL),
(110, 1, '关闭', '0', 'status_type', '', 'danger', 'Y', '0', 'admin', '2021-04-26 10:51:47', 'admin', '2021-07-08 11:23:42', ''),
(111, 2, '开启', '1', 'status_type', '', 'primary', 'Y', '0', 'admin', '2021-04-26 10:52:19', 'admin', '2021-07-08 11:23:49', ''),
(112, 1, '未开放', '0', 'server_status', NULL, 'danger', 'Y', '0', 'admin', '2021-04-27 11:02:01', '', NULL, '服务器状态-未开放'),
(113, 2, '开放中', '1', 'server_status', '', 'primary', 'Y', '0', 'admin', '2021-04-27 11:02:31', 'admin', '2021-04-27 11:03:34', '服务器状态-开放中'),
(114, 3, '维护中', '2', 'server_status', NULL, 'warning', 'Y', '0', 'admin', '2021-04-27 11:03:24', '', NULL, '服务器状态-维护中'),
(115, 1, '测试', '0', 'server_group_type', '', 'default', 'Y', '0', 'admin', '2021-04-27 11:09:10', 'admin', '2021-06-22 14:55:08', '游戏服类型-测试'),
(116, 2, '先锋', '1', 'server_group_type', '', 'default', 'Y', '0', 'admin', '2021-04-27 11:09:42', 'admin', '2021-06-22 14:55:21', '游戏服类型-先锋'),
(117, 3, '正式', '2', 'server_group_type', '', 'default', 'Y', '0', 'admin', '2021-04-27 11:10:14', 'admin', '2021-06-22 14:55:32', '游戏服类型-正式'),
(119, 1, '正常', '0', 'server_label', NULL, 'primary', 'Y', '0', 'admin', '2021-04-27 11:12:37', '', NULL, '热度标签-正常'),
(120, 2, '爆满', '1', 'server_label', NULL, 'danger', 'Y', '0', 'admin', '2021-04-27 11:13:04', '', NULL, '热度标签-爆满'),
(121, 3, '推荐', '2', 'server_label', NULL, 'info', 'Y', '0', 'admin', '2021-04-27 11:14:01', '', NULL, '热度标签-推荐'),
(122, 4, '新服', '3', 'server_label', NULL, 'success', 'Y', '0', 'admin', '2021-04-27 11:14:47', '', NULL, '热度标签-新服'),
(123, 1, '备服状态', '0', 'server_open_state', NULL, 'danger', 'Y', '0', 'admin', '2021-05-08 16:01:33', '', NULL, '服务器状态 0:备服状态'),
(124, 2, '开放状态', '1', 'server_open_state', NULL, 'primary', 'Y', '0', 'admin', '2021-05-08 16:02:05', '', NULL, '服务器状态  1:开服状态'),
(125, 1, '否', '0', 'is_backup', NULL, 'warning', 'Y', '0', 'admin', '2021-05-25 14:35:42', '', NULL, '是否为备服 0:否'),
(126, 2, '是', '1', 'is_backup', NULL, 'primary', 'Y', '0', 'admin', '2021-05-25 14:36:14', '', NULL, '是否为备服 1：是'),
(127, 1, '停用', '0', 'notice_status', '', 'danger', 'Y', '0', 'admin', '2021-06-22 17:42:45', 'admin', '2021-06-22 17:43:47', '停用'),
(128, 2, '启用', '1', 'notice_status', '', 'primary', 'Y', '0', 'admin', '2021-06-22 17:43:14', 'admin', '2021-06-22 17:43:53', '启用'),
(129, 1, '更新公告', '0', 'notice_type', NULL, 'primary', 'Y', '0', 'admin', '2021-06-22 17:45:08', '', NULL, '更新公告'),
(130, 2, '登陆公告', '1', 'notice_type', NULL, 'success', 'Y', '0', 'admin', '2021-06-22 17:46:19', '', NULL, '登陆公告'),
(131, 3, '活动公告', '2', 'notice_type', NULL, 'info', 'Y', '0', 'admin', '2021-06-22 17:46:39', '', NULL, '活动公告'),
(132, 1, '否', '0', 'notice_auto', NULL, 'danger', 'Y', '0', 'admin', '2021-06-22 17:49:34', '', NULL, '不自动弹出'),
(133, 2, '是', '1', 'notice_auto', NULL, 'primary', 'Y', '0', 'admin', '2021-06-22 17:50:06', '', NULL, '自动弹出');



-- ----------------------------
-- 7.Data for the table `sys_dict_type`
-- ----------------------------
insert into sys_dict_type(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) values
(1, '用户性别', 'sys_user_sex', '0', 'admin', '2021-04-22 19:31:59', '', NULL, '用户性别列表'),
(2, '菜单状态', 'sys_show_hide', '0', 'admin', '2021-04-22 19:31:59', '', NULL, '菜单状态列表'),
(3, '系统开关', 'sys_normal_disable', '0', 'admin', '2021-04-22 19:31:59', '', NULL, '系统开关列表'),
(4, '任务状态', 'sys_job_status', '0', 'admin', '2021-04-22 19:31:59', '', NULL, '任务状态列表'),
(5, '任务分组', 'sys_job_group', '0', 'admin', '2021-04-22 19:31:59', '', NULL, '任务分组列表'),
(6, '系统是否', 'sys_yes_no', '0', 'admin', '2021-04-22 19:32:00', '', NULL, '系统是否列表'),
(7, '通知类型', 'sys_notice_type', '0', 'admin', '2021-04-22 19:32:00', '', NULL, '通知类型列表'),
(8, '通知状态', 'sys_notice_status', '0', 'admin', '2021-04-22 19:32:00', '', NULL, '通知状态列表'),
(9, '操作类型', 'sys_oper_type', '0', 'admin', '2021-04-22 19:32:00', '', NULL, '操作类型列表'),
(10, '系统状态', 'sys_common_status', '0', 'admin', '2021-04-22 19:32:00', '', NULL, '登录状态列表'),
(100, '服务类型', 'server_type', '0', 'admin', '2021-04-23 13:20:09', '', NULL, '服务类型'),
(101, '数据库类型', 'db_log_type', '0', 'admin', '2021-04-23 13:25:15', '', NULL, '数据库类型'),
(102, '合服标识', 'he_fu_type', '0', 'admin', '2021-04-23 13:28:18', '', NULL, '合服标识'),
(103, '是否删除', 'deleted_type', '0', 'admin', '2021-04-23 13:29:36', '', NULL, NULL),
(104, '是否开启', 'status_type', '0', 'admin', '2021-04-26 10:50:44', '', NULL, '开启或关闭状态'),
(105, '服务器状态', 'server_status', '0', 'admin', '2021-04-27 09:57:59', '', NULL, '0-未开放 1-开放中 2-维护中'),
(106, '游戏服类型', 'server_group_type', '0', 'admin', '2021-04-27 11:05:03', 'admin', '2021-06-22 14:54:30', '0-测试 1-先锋 2-正式'),
(107, '热度标签', 'server_label', '0', 'admin', '2021-04-27 11:11:23', '', NULL, '服务器热度标签 0-正常  1-爆满  2-推荐  3-新服'),
(108, '服务器开服状态', 'server_open_state', '0', 'admin', '2021-05-08 15:59:24', '', NULL, '服务器状态 0:备服状态 1:开服状态'),
(109, '公告状态', 'notice_status', '0', 'admin', '2021-06-22 17:41:53', '', NULL, '公告状态 0:停用  1:启用'),
(110, '公告类型', 'notice_type', '0', 'admin', '2021-06-22 17:44:42', '', NULL, '公告类型(0:更新公告 1:登陆公告 2:活动公告)'),
(111, '是否自动', 'notice_auto', '0', 'admin', '2021-06-22 17:49:04', '', NULL, '是否自动弹出 0:不自动弹出 1:自动弹出');


-- ----------------------------
-- 8.Data for the table `sys_job`
-- ----------------------------
INSERT INTO sys_job(job_id, job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time, update_by, update_time, remark) VALUES
(1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '10', '3', '1', '1', 'admin', '2021-04-22 19:32:12', 'admin', '2021-08-06 14:57:54', ''),
(2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(''ry'')', '15', '3', '1', '1', 'admin', '2021-04-22 19:32:12', 'admin', '2021-08-06 14:57:59', ''),
(3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(''ry'', true, 2000L, 316.50D, 100)', '20', '3', '1', '1', 'admin', '2021-04-22 19:32:12', 'admin', '2021-08-06 14:58:03', ''),
(4, '向APIServre请求服务器状态信息', 'DEFAULT', 'ryTask.getServerState', '15', '3', '1', '0', 'admin', '2021-05-08 17:49:48', 'admin', '2021-12-08 16:33:32', ''),
(5, '定时刷新服务器配置信息缓存', 'DEFAULT', 'ryTask.refreshServerCache', '5', '3', '1', '0', 'admin', '2021-12-08 14:57:44', 'admin', '2021-12-08 16:01:26', '');



-- ----------------------------
-- 9.Data for the table `sys_menu`
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
(110, '定时任务', 2, 2, '/monitor/job', 'menuItem', 'C', '0', '1', 'monitor:job:view', 'fa fa-tasks', 'admin', '2021-04-22 19:31:37', 'admin', '2021-08-05 18:22:27', '定时任务菜单'),
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
(2000, '运维工具', 0, 5, '#', 'menuItem', 'M', '0', '1', NULL, 'fa fa-wifi', 'admin', '2021-04-23 13:46:10', '', NULL, ''),
(2005, '渠道信息', 2000, 3, '/serverListConfig/channel', 'menuItem', 'C', '0', '1', 'serverListConfig:channel:view', '#', 'admin', '2021-04-25 16:55:25', 'admin', '2021-12-13 11:28:47', '渠道信息菜单'),
(2006, '渠道信息查询', 2005, 1, '#', '', 'F', '0', '1', 'serverListConfig:channel:list', '#', 'admin', '2021-04-25 16:55:25', '', NULL, ''),
(2007, '渠道信息新增', 2005, 2, '#', '', 'F', '0', '1', 'serverListConfig:channel:add', '#', 'admin', '2021-04-25 16:55:25', '', NULL, ''),
(2008, '渠道信息修改', 2005, 3, '#', '', 'F', '0', '1', 'serverListConfig:channel:edit', '#', 'admin', '2021-04-25 16:55:25', '', NULL, ''),
(2009, '渠道信息删除', 2005, 4, '#', '', 'F', '0', '1', 'serverListConfig:channel:remove', '#', 'admin', '2021-04-25 16:55:25', '', NULL, ''),
(2010, '渠道信息导出', 2005, 5, '#', '', 'F', '0', '1', 'serverListConfig:channel:export', '#', 'admin', '2021-04-25 16:55:25', '', NULL, ''),
(2017, '服务器配置信息', 2000, 4, '/serverListConfig/server', 'menuItem', 'C', '0', '1', 'serverListConfig:server:view', '#', 'admin', '2021-04-25 20:20:02', 'admin', '2021-12-13 11:28:58', '服务器配置信息菜单'),
(2018, '服务器配置信息查询', 2017, 1, '#', '', 'F', '0', '1', 'serverListConfig:server:list', '#', 'admin', '2021-04-25 20:20:02', '', NULL, ''),
(2019, '服务器配置信息新增', 2017, 2, '#', '', 'F', '0', '1', 'serverListConfig:server:add', '#', 'admin', '2021-04-25 20:20:02', '', NULL, ''),
(2020, '服务器配置信息修改', 2017, 3, '#', '', 'F', '0', '1', 'serverListConfig:server:edit', '#', 'admin', '2021-04-25 20:20:02', '', NULL, ''),
(2021, '服务器配置信息删除', 2017, 4, '#', '', 'F', '0', '1', 'serverListConfig:server:remove', '#', 'admin', '2021-04-25 20:20:02', '', NULL, ''),
(2022, '服务器配置信息导出', 2017, 5, '#', '', 'F', '0', '1', 'serverListConfig:server:export', '#', 'admin', '2021-04-25 20:20:02', '', NULL, ''),
(2028, '服务器列表', 2000, 5, '/serverListConfig/serverList', 'menuItem', 'C', '0', '1', 'serverListConfig:serverList:view', '#', 'admin', '2021-04-26 13:54:50', 'admin', '2021-12-13 11:29:10', '服务器列表菜单'),
(2029, '服务器列表查询', 2028, 1, '#', '', 'F', '0', '1', 'serverListConfig:serverList:list', '#', 'admin', '2021-04-26 13:54:50', '', NULL, ''),
(2030, '服务器列表新增', 2028, 2, '#', '', 'F', '0', '1', 'serverListConfig:serverList:add', '#', 'admin', '2021-04-26 13:54:50', '', NULL, ''),
(2031, '服务器列表修改', 2028, 3, '#', '', 'F', '0', '1', 'serverListConfig:serverList:edit', '#', 'admin', '2021-04-26 13:54:50', '', NULL, ''),
(2032, '服务器列表删除', 2028, 4, '#', '', 'F', '0', '1', 'serverListConfig:serverList:remove', '#', 'admin', '2021-04-26 13:54:50', '', NULL, ''),
(2033, '服务器列表导出', 2028, 5, '#', '', 'F', '0', '1', 'serverListConfig:serverList:export', '#', 'admin', '2021-04-26 13:54:50', '', NULL, ''),
(2040, '白名单', 2000, 6, '/serverListConfig/whiteList', 'menuItem', 'C', '0', '1', 'serverListConfig:whiteList:view', '#', 'admin', '2021-04-26 15:59:57', 'admin', '2021-12-13 11:29:19', '白名单菜单'),
(2041, '白名单查询', 2040, 1, '#', '', 'F', '0', '1', 'serverListConfig:whiteList:list', '#', 'admin', '2021-04-26 15:59:57', '', NULL, ''),
(2042, '白名单新增', 2040, 2, '#', '', 'F', '0', '1', 'serverListConfig:whiteList:add', '#', 'admin', '2021-04-26 15:59:57', '', NULL, ''),
(2043, '白名单修改', 2040, 3, '#', '', 'F', '0', '1', 'serverListConfig:whiteList:edit', '#', 'admin', '2021-04-26 15:59:57', '', NULL, ''),
(2044, '白名单删除', 2040, 4, '#', '', 'F', '0', '1', 'serverListConfig:whiteList:remove', '#', 'admin', '2021-04-26 15:59:57', '', NULL, ''),
(2045, '白名单导出', 2040, 5, '#', '', 'F', '0', '1', 'serverListConfig:whiteList:export', '#', 'admin', '2021-04-26 15:59:57', '', NULL, ''),
(2046, '公告管理', 2000, 7, '/serverListConfig/notice', 'menuItem', 'C', '0', '1', 'serverListConfig:notice:view', '#', 'admin', '2021-06-22 16:31:51', 'admin', '2021-12-13 11:29:27', '公告管理菜单'),
(2047, '公告管理查询', 2046, 1, '#', '', 'F', '0', '1', 'serverListConfig:notice:list', '#', 'admin', '2021-06-22 16:31:51', '', NULL, ''),
(2048, '公告管理新增', 2046, 2, '#', '', 'F', '0', '1', 'serverListConfig:notice:add', '#', 'admin', '2021-06-22 16:31:51', '', NULL, ''),
(2049, '公告管理修改', 2046, 3, '#', '', 'F', '0', '1', 'serverListConfig:notice:edit', '#', 'admin', '2021-06-22 16:31:51', '', NULL, ''),
(2050, '公告管理删除', 2046, 4, '#', '', 'F', '0', '1', 'serverListConfig:notice:remove', '#', 'admin', '2021-06-22 16:31:51', '', NULL, ''),
(2051, '公告管理导出', 2046, 5, '#', '', 'F', '0', '1', 'serverListConfig:notice:export', '#', 'admin', '2021-06-22 16:31:51', '', NULL, ''),
(2052, 'APIServer地址管理', 2000, 1, '/serverListConfig/apiserver', '', 'C', '0', '1', 'serverListConfig:apiserver:view', '#', 'admin', '2021-06-24 14:35:27', '', NULL, 'APIServer地址管理菜单'),
(2053, 'APIServer地址管理查询', 2052, 1, '#', '', 'F', '0', '1', 'serverListConfig:apiserver:list', '#', 'admin', '2021-06-24 14:35:27', '', NULL, ''),
(2054, 'APIServer地址管理新增', 2052, 2, '#', '', 'F', '0', '1', 'serverListConfig:apiserver:add', '#', 'admin', '2021-06-24 14:35:27', '', NULL, ''),
(2055, 'APIServer地址管理修改', 2052, 3, '#', '', 'F', '0', '1', 'serverListConfig:apiserver:edit', '#', 'admin', '2021-06-24 14:35:27', '', NULL, ''),
(2056, 'APIServer地址管理删除', 2052, 4, '#', '', 'F', '0', '1', 'serverListConfig:apiserver:remove', '#', 'admin', '2021-06-24 14:35:27', '', NULL, ''),
(2057, 'APIServer地址管理导出', 2052, 5, '#', '', 'F', '0', '1', 'serverListConfig:apiserver:export', '#', 'admin', '2021-06-24 14:35:27', '', NULL, ''),
(2058, '登录服信息', 2000, 2, '/serverListConfig/loginServer', 'menuItem', 'C', '0', '1', 'serverListConfig:loginServer:view', '#', 'admin', '2021-10-20 15:26:06', 'admin', '2021-12-13 11:28:14', '登录服信息菜单'),
(2059, '登录服信息查询', 2058, 1, '#', '', 'F', '0', '1', 'serverListConfig:loginServer:list', '#', 'admin', '2021-10-20 15:26:06', '', NULL, ''),
(2060, '登录服信息新增', 2058, 2, '#', '', 'F', '0', '1', 'serverListConfig:loginServer:add', '#', 'admin', '2021-10-20 15:26:06', '', NULL, ''),
(2061, '登录服信息修改', 2058, 3, '#', '', 'F', '0', '1', 'serverListConfig:loginServer:edit', '#', 'admin', '2021-10-20 15:26:06', '', NULL, ''),
(2062, '登录服信息删除', 2058, 4, '#', '', 'F', '0', '1', 'serverListConfig:loginServer:remove', '#', 'admin', '2021-10-20 15:26:06', '', NULL, ''),
(2063, '登录服信息导出', 2058, 5, '#', '', 'F', '0', '1', 'serverListConfig:loginServer:export', '#', 'admin', '2021-10-20 15:26:06', '', NULL, '');


-- ----------------------------
-- 10.Data for the table `sys_dept`
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
-- 11.Data for the table `sys_post`
-- ----------------------------
insert into `sys_post`(`post_id`,`post_code`,`post_name`,`post_sort`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'root','权限管理者',1,'0','admin','2021-04-22 19:31:31','',NULL,'');

-- ----------------------------
-- 12.Data for the table `sys_role`
-- ----------------------------
insert  into `sys_role`(`role_id`,`role_name`,`role_key`,`role_sort`,`data_scope`,`status`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'超级管理员','admin',1,'1','0','0','admin','2021-04-22 19:31:34','',NULL,'超级管理员');

-- ----------------------------
-- 13.Data for the table `sys_user`
-- ----------------------------
insert  into `sys_user`(`user_id`,`dept_id`,`login_name`,`user_name`,`user_type`,`email`,`phonenumber`,`sex`,`avatar`,`password`,`salt`,`status`,`del_flag`,`login_ip`,`login_date`,`pwd_update_date`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,100,'admin','千向','00','ry@163.com','15888888888','1','','29c67a30398638269fe600f73a054934','111111','0','0','182.150.165.20','2021-06-09 15:08:53','2021-04-22 19:31:29','admin','2021-04-22 19:31:29','','2021-06-09 15:08:52','管理员');


-- ----------------------------
-- 14.Data for the table `sys_user_post`
-- ----------------------------
insert  into `sys_user_post`(`user_id`,`post_id`) values 
(1,1);

-- ----------------------------
-- 15.Data for the table `sys_user_role`
-- ----------------------------
insert  into `sys_user_role`(`user_id`,`role_id`) values 
(1,1);

-- ==============Script Switch Reset=========================
-- Enable foreign keys
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
-- Restore previous SQL mode
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
