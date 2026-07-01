
/*be changed list*/
/* 
//1.7.0_2021.07.22_0.25.0730_main:


//1.8.0_2021.11.09_0.26.1110_main:


*/

/*The following statements need to be executed*/
/*1.   "TableName1" "COMMENT1" */


/*2.  "TableName2" "COMMENT2" */


/*3.  "TableName3" "COMMENT3" */

ALTER TABLE `sdk_server_list`
DROP COLUMN `login_server_name`, 
DROP COLUMN `login_server_port`, 
CHANGE `login_server_ip` `login_server_group` TEXT  NULL COMMENT '登录服务器组信息'; 

UPDATE `sys_job` SET `status` = '0' WHERE `job_id` = '4' AND `job_name` = '向APIServre请求服务器状态信息' AND `job_group` = 'DEFAULT'; 

UPDATE `sys_job` SET `cron_expression` = '10' WHERE `job_id` = '1' AND `job_name` = '系统默认（无参）' AND `job_group` = 'DEFAULT'; 
UPDATE `sys_job` SET `cron_expression` = '15' WHERE `job_id` = '2' AND `job_name` = '系统默认（有参）' AND `job_group` = 'DEFAULT'; 
UPDATE `sys_job` SET `cron_expression` = '20' WHERE `job_id` = '3' AND `job_name` = '系统默认（多参）' AND `job_group` = 'DEFAULT'; 
UPDATE `sys_job` SET `cron_expression` = '15' WHERE `job_id` = '4' AND `job_name` = '向APIServre请求服务器状态信息' AND `job_group` = 'DEFAULT'; 

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='菜单权限表';

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


CREATE TABLE `sdk_login_server` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `login_server_name` varchar(50) DEFAULT NULL COMMENT '登录服务器名称',
  `login_server_ip` varchar(128) DEFAULT NULL COMMENT '登录服务器IP',
  `login_server_port` int(11) DEFAULT NULL COMMENT '登录服端口',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录服信息';

CREATE TABLE `sdk_server_update` (
  `updateTime` bigint(20) DEFAULT NULL COMMENT '服务器信息修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务器信息修改时间记录';

INSERT INTO qrtz_job_details(sched_name, job_name, job_group, description, job_class_name, is_durable, is_nonconcurrent, is_update_data, requests_recovery, job_data) VALUES('RuoyiScheduler', 'TASK_CLASS_NAME5', 'DEFAULT', NULL, 'com.kits.project.monitor.job.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', x'ACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000F5441534B5F50524F5045525449455373720027636F6D2E6B6974732E70726F6A6563742E6D6F6E69746F722E6A6F622E646F6D61696E2E4A6F6200000000000000010200084C000A636F6E63757272656E747400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C000C696E766F6B6554617267657471007E00094C00086A6F6247726F757071007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C00076A6F624E616D6571007E00094C000D6D697366697265506F6C69637971007E00094C000673746174757371007E000978720028636F6D2E6B6974732E6672616D65776F726B2E7765622E646F6D61696E2E42617365456E7469747900000000000000010200074C0008637265617465427971007E00094C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C0006706172616D7371007E00034C000672656D61726B71007E00094C000B73656172636856616C756571007E00094C0008757064617465427971007E00094C000A75706461746554696D6571007E000C787074000561646D696E7372000E6A6176612E7574696C2E44617465686A81014B597419030000787077080000017D98D56E407870740000707070740001317400013574001972795461736B2E72656672657368536572766572436163686574000744454641554C547372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000000005740027E5AE9AE697B6E588B7E696B0E69C8DE58AA1E599A8E9858DE7BDAEE4BFA1E681AFE7BC93E5AD9874000131740001307800');

INSERT INTO sys_job(job_id, job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time, update_by, update_time, remark) VALUES(5, '定时刷新服务器配置信息缓存', 'DEFAULT', 'ryTask.refreshServerCache', '5', '3', '1', '0', 'admin', '2021-12-08 14:57:44', 'admin', '2021-12-08 16:01:26', '');


