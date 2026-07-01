package com.kits.common.dbclient;

import com.kits.framework.aspectj.lang.enums.DataSourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class DBServerMgr {
	public enum DBServer {
		MANGER, ACCOUNT, GM, GAME, LOG
	}

	private static Logger logger = LoggerFactory.getLogger(DBServerMgr.class);
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
		String allTablesSql =  "select table_name from information_schema.tables where table_schema = "+dbName;
		List<String> tableList = null;
		try{
			tableList = dbClient.selectList(allTablesSql,String.class);

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

	public DBClient getDBClient(DBServer dbType) {
		DBClient dbClient = dbClientMap.get(dbType.name());
		DBServerConfig dbServerConfig = new DBServerConfig();
		if (dbClient != null)
			return dbClient;
		switch (dbType) {
		case LOG:
			dbServerConfig.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/ryxfgamelog_2?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8");
			dbServerConfig.setUserName("root");
			dbServerConfig.setPassword("123456");
			dbClient = setDbClient(dbServerConfig);
			break;
    	case GM:
			dbServerConfig.setDataSourceName(DataSourceType.MASTER.name());
			dbServerConfig.setUseConnectionPool(true);
			dbClient = setDbClient(dbServerConfig);
    		break;
		default:
			break;
		}
		dbClientMap.put(dbType.name(), dbClient);
		return dbClient;
	}

	private DBClient setDbClient(DBServerConfig dbServerConfig)
	{
		DBClient dbClient = null;
		try {
			dbClient = new DBClient(dbServerConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbClient;
	}

	public DBClient getDbClient(){
		InputStream inStream = DBServerMgr.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties prop = new Properties();
		try {
			prop.load(inStream);
			String jdbcUrl = prop.getProperty("jdbc.url");
			String userName = prop.getProperty("jdbc.username");
			String password = prop.getProperty("jdbc.password");
			DBServerConfig dbServerConfig = new DBServerConfig();
			dbServerConfig.setJdbcUrl(jdbcUrl);
			dbServerConfig.setUserName(userName);
			dbServerConfig.setPassword(password);
			return setDbClient(dbServerConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
