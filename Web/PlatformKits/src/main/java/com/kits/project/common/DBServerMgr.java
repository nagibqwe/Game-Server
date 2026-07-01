package com.kits.project.common;

import java.util.ArrayList;
import java.util.List;

import com.kits.common.utils.spring.SpringUtils;
import com.kits.project.gmtool.dblog.domain.TDblog;
import com.kits.project.gmtool.dblog.service.ITDblogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class DBServerMgr {
	private static Logger logger = LoggerFactory.getLogger(DBServerMgr.class);
	private static DBServerMgr instance = new DBServerMgr();
	private DBServerMgr() {
	}
	public static DBServerMgr getInstance() {
		return instance;
	}
	/**
	 * 获取快速数据库连接，一般用于查游戏日志库短连接操作
	 */
	public JdbcTemplate getJdbcTemplateLog(long serverId) {
		ITDblogService itDblogService = SpringUtils.getBean(ITDblogService.class);
		TDblog tDblog = itDblogService.selectTDblogById(serverId);
		return getJdbcTemplateLog(tDblog);
	}
	/**
	 * 获取快速数据库连接，一般用于查游戏日志库短连接操作
	 */
	public JdbcTemplate getJdbcTemplateLog(TDblog tDblog) {
		String jdbcUrl = "jdbc:mysql://" + tDblog.getServerIpPort() + "/" + tDblog.getDbname() + "?useUnicode=true&characterEncoding=UTF-8";
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
		dataSource.setUsername(tDblog.getDbuser());
		dataSource.setUrl(jdbcUrl);
		dataSource.setPassword(tDblog.getDbpassword());
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate;
	}
	/**
	 * 得到数据列表
	 * @param jdbcTemplate
	 * @param tableName
	 * @return
	 */
	public List<String> getTableList(JdbcTemplate jdbcTemplate,String dbName,String tableName){
		//查询所有表名
		String allTablesSql =  "select table_name from information_schema.tables where table_schema = ?";
		List<String> tableList = null;
		try{
			tableList =	jdbcTemplate.queryForList(allTablesSql,new Object[]{dbName},String.class);
		}catch (Exception e){
			e.getStackTrace();
		}
		List<String> newTableList = null;
		if(tableList!=null && tableList.size()>0){
			newTableList = new ArrayList<>();
			for(int i = 0;i<tableList.size();i++){
				if(tableList.get(i).startsWith(tableName)){
					newTableList.add(tableList.get(i));
				}
			}
		}
		return newTableList;
	}
}
