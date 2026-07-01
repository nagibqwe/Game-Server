package com.gm.common.dbclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.gm.project.gmtool.config.StatConfigService;
import com.gm.project.gmtool.job.BaseTask;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

public class DBServerMgr {
	public enum DBServer {
		MANGER, STAT_LOG, GM, GAME, LOG
	}

	private static org.apache.log4j.Logger logger = Logger.getLogger(DBServerMgr.class);
	// serverId -- DBCluster
	private Map<Integer, DBCluster> dbServerConfigMap = new ConcurrentHashMap<>();

	//
	private Map<String, DBClient> dbClientMap = new ConcurrentHashMap<>();

	private static DBServerMgr instance = new DBServerMgr();

	private DBServerMgr() {
	}

	public static DBServerMgr getInstance() {
		return instance;
	}
	/**
	 * 得到数据列表
	 * @param dbClient
	 * @param tableName
	 * @return
	 */
	public List<String> getTableList(DBClient dbClient, String dbName, String tableName){
		//查询所有表名
		String allTablesSql =  "select table_name from information_schema.tables where table_schema = ?";
		List<Map<String,String>> tableList = null;
		try{
			tableList = dbClient.selectList(allTablesSql,dbName);
		}catch (Exception e){
			e.getStackTrace();
		}
		List<String> newTableList = null;
		if(tableList!=null && tableList.size()>0){
			newTableList = new ArrayList<>();
			for(int i = 0;i<tableList.size();i++){
				String table_name = tableList.get(i).get("table_name");
				if(table_name.startsWith(tableName)){
					newTableList.add(table_name);
				}
			}
		}
		return newTableList;
	}
	public DBClient getLogDBClient(Map<String, Object> tDblogBeanMap) {
		String key = DBServer.LOG.name() + "@" + tDblogBeanMap.get("serverId");
		DBClient dbClient = dbClientMap.get(key);
		if (dbClient != null)
			return dbClient;
		DBServerConfig dbServerConfig = new DBServerConfig();
		String jdbcUrl = "jdbc:mysql://" +  tDblogBeanMap.get("dblogIp")+":"+ tDblogBeanMap.get("dblogPort")+ "/" + tDblogBeanMap.get("dblogName") + "?useUnicode=true&characterEncoding=UTF-8&useSSL=true";
		dbServerConfig.setJdbcUrl(jdbcUrl);
		dbServerConfig.setUserName(tDblogBeanMap.get("dblogUser").toString());
		dbServerConfig.setPassword(tDblogBeanMap.get("dblogPwd").toString());
		dbServerConfig.setUseConnectionPool(true);//使用连接池
		dbClient = setDbClient(dbServerConfig);
		dbClientMap.put(key, dbClient);
		return dbClient;

	}
	public DBClient getDBClient(DBServer dbType) {
		DBClient dbClient = dbClientMap.get(dbType.name());
		DBServerConfig dbServerConfig = new DBServerConfig();
		if (dbClient != null){
			return dbClient;
		}
		switch (dbType) {
		case STAT_LOG:
			String statjdbcUrl = StatConfigService.getInstance().getValue("stat.jdbc.url");
			String statusername = StatConfigService.getInstance().getValue("stat.jdbc.username");
			String statpassword = StatConfigService.getInstance().getValue("stat.jdbc.password");
			dbServerConfig.setJdbcUrl(statjdbcUrl);
			dbServerConfig.setUserName(statusername);
			dbServerConfig.setPassword(statpassword);
			dbServerConfig.setUseConnectionPool(true);//使用连接池
			dbClient = setDbClient(dbServerConfig);
			break;
    	case GM:
			String jdbcUrl = StatConfigService.getInstance().getValue("gm.jdbc.url");
			String username = StatConfigService.getInstance().getValue("gm.jdbc.username");
			String password = StatConfigService.getInstance().getValue("gm.jdbc.password");
			dbServerConfig.setJdbcUrl(jdbcUrl);
			dbServerConfig.setUserName(username);
			dbServerConfig.setPassword(password);
			dbServerConfig.setUseConnectionPool(true);//使用连接池
			dbClient = setDbClient(dbServerConfig);
    		break;
		default:
			break;
		}
		dbClientMap.put( dbType.name(), dbClient);
		return dbClient;
	}

	private DBClient setDbClient(DBServerConfig dbServerConfig)
	{
		DBClient dbClient = null;
		try {
			dbClient = new DBClient(dbServerConfig);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return dbClient;
	}

	 
}
