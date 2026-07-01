package com.gm.common.dbclient;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.gm.common.utils.DateUtils;
import com.gm.common.utils.spring.SpringUtils;
import com.gm.framework.aspectj.lang.enums.DataSourceType;
import com.gm.project.gmtool.db.domain.TDb;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.impl.TServerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBServerMgr {
    public enum DBServer {
        MANGER, STAT_LOG, GM, GAME, LOG, LOGIN
    }

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
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
     *
     * @param dbClient
     * @param tableName
     * @return
     */
    public List<String> getTableList(DBClient dbClient, String dbName, String tableName) {
        //查询所有表名
        String allTablesSql = "select table_name from information_schema.tables where table_schema = ?";
        List<Map<String, String>> tableList = null;
        try {
            tableList = dbClient.selectList(allTablesSql, dbName);
        } catch (Exception e) {
            e.getStackTrace();
        }
        List<String> newTableList = null;
        if (tableList != null && tableList.size() > 0) {
            newTableList = new ArrayList<>();
            for (int i = 0; i < tableList.size(); i++) {
                String table_name = tableList.get(i).get("table_name");
                if (table_name.startsWith(tableName)) {
                    newTableList.add(table_name);
                }
            }
        }
        return newTableList;
    }

//	public DBClient getLogDBClient(int serverId) {
//		TDblog tDblog = this.getTDblog(serverId);
//		if (tDblog == null) {
//			return null;
//		}
//		DBClient logDBClient = this.getLogDBClient(tDblog);
//		if (logDBClient == null) {
//			return  null;
//		}
//		return logDBClient;
//	}

//	public DBClient getLogDBClient(TDblog tDblog) {
//		String key = DBServer.LOG.name() + "@" + tDblog.getServerId();
//		DBClient dbClient = dbClientMap.get(key);
//		if (dbClient != null)
//			return dbClient;
//		DBServerConfig dbServerConfig = new DBServerConfig();
//		String jdbcUrl = "jdbc:mysql://" + tDblog.getDbIp()+":"+tDblog.getDbPort() + "/" + tDblog.getDbname() + "?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=UTC";
//		dbServerConfig.setJdbcUrl(jdbcUrl);
//		dbServerConfig.setUserName(tDblog.getDbuser());
//		dbServerConfig.setPassword(tDblog.getDbpassword());
//		dbClient = setDbClient(dbServerConfig);
//		dbClientMap.put(key, dbClient);
//		return dbClient;
//
//	}

    public DBClient getLogDBClient(int serverId) {
        TServer tServer = this.getTServer(serverId);
        if (tServer == null) {
            return null;
        }
        DBClient logDBClient = this.getLogDBClient(tServer);
        if (logDBClient == null) {
            return null;
        }
        return logDBClient;
    }

    //获取日志库数据库连接
    public DBClient getLogDBClient(TServer tServer) {
        String key = DBServer.LOG.name() + "@" + tServer.getServerId();
        DBClient dbClient = dbClientMap.get(key);
        if (dbClient != null)
            return dbClient;
        DBServerConfig dbServerConfig = new DBServerConfig();
        String jdbcUrl = "jdbc:mysql://" + tServer.getDblogIp() + ":" + tServer.getDblogPort() + "/" + tServer.getDblogName() + "?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=UTC";
        dbServerConfig.setJdbcUrl(jdbcUrl);
        dbServerConfig.setUserName(tServer.getDblogUser());
        dbServerConfig.setPassword(tServer.getDblogPwd());
        dbClient = setDbClient(dbServerConfig);
        dbClientMap.put(key, dbClient);
        return dbClient;

    }

    //获取逻辑服数据库连接
    public DBClient getLogicDBClient(TDb tDb) {
        String key = DBServer.GAME.name() + "@" + tDb.getServerId();
        DBClient dbClient = dbClientMap.get(key);
        if (dbClient != null)
            return dbClient;
        DBServerConfig dbServerConfig = new DBServerConfig();
        String jdbcUrl = "jdbc:mysql://" + tDb.getDbIp() + ":" + tDb.getDbPort() + "/" + tDb.getDbname() + "?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=UTC";
        dbServerConfig.setJdbcUrl(jdbcUrl);
        dbServerConfig.setUserName(tDb.getDbuser());
        dbServerConfig.setPassword(tDb.getDbpassword());
        dbClient = setDbClient(dbServerConfig);
        dbClientMap.put(key, dbClient);
        return dbClient;

    }

    //清除连接池中缓存的对象
    public void clearDBClient(TServer tServer,TDb tDb) {
        if (null == tServer && tDb != null){
            removeDbClientMap(tDb);
        }else if (tServer != null && tDb != null){
            removeDbClientMap(tServer);
            removeDbClientMap(tDb);
        }else {
            removeDbClientMap(tServer);
        }
    }

    //移除日志库数据库连接
    public void removeDbClientMap(TServer tServer){
        String key = DBServer.LOG.name() + "@" + tServer.getServerId();
        if(dbClientMap.containsKey(key)){
            dbClientMap.remove(key);
        }
    }

    //移除逻辑服数据库连接
    public void removeDbClientMap(TDb tDb){
        String key = DBServer.GAME.name() + "@" + tDb.getServerId();
        if(dbClientMap.containsKey(key)){
            dbClientMap.remove(key);
        }
    }

    public DBClient getLogDBClient(Map<String, Object> tDblogBeanMap) {
        String key = DBServer.LOG.name() + "@" + tDblogBeanMap.get("serverId");
        DBClient dbClient = dbClientMap.get(key);
        if (dbClient != null)
            return dbClient;
        DBServerConfig dbServerConfig = new DBServerConfig();
        String jdbcUrl = "jdbc:mysql://" + tDblogBeanMap.get("serverIpPort") + "/" + tDblogBeanMap.get("dbname") + "?useUnicode=true&characterEncoding=UTF-8&useSSL=true";
        dbServerConfig.setJdbcUrl(jdbcUrl);
        dbServerConfig.setUserName(tDblogBeanMap.get("dbuser").toString());
        dbServerConfig.setPassword(tDblogBeanMap.get("dbpassword").toString());
        dbClient = setDbClient(dbServerConfig);
        dbClientMap.put(key, dbClient);
        return dbClient;

    }

    public DBClient getDBClient(DBServer dbType) {
        DBClient dbClient = dbClientMap.get(dbType.name());
        DBServerConfig dbServerConfig = new DBServerConfig();
        if (dbClient != null)
            return dbClient;
        switch (dbType) {
            case STAT_LOG: //日志统计库 所在地址
                dbServerConfig.setDataSourceName(DataSourceType.STAT.name());
                dbServerConfig.setUseConnectionPool(true);
                dbClient = setDbClient(dbServerConfig);
                break;
            case GM:
                dbServerConfig.setDataSourceName(DataSourceType.MASTER.name());
                dbServerConfig.setUseConnectionPool(true);
                dbClient = setDbClient(dbServerConfig);
                break;
            case LOGIN://登陆服逻辑服
                dbServerConfig.setDataSourceName(DataSourceType.LOGIN.name());
                dbServerConfig.setUseConnectionPool(true);
                dbClient = setDbClient(dbServerConfig);
                break;
            default:
                break;
        }
        dbClientMap.put(dbType.name(), dbClient);
        return dbClient;
    }

    private DBClient setDbClient(DBServerConfig dbServerConfig) {
        DBClient dbClient = null;
        try {
            dbClient = new DBClient(dbServerConfig);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
        return dbClient;
    }


    /**
     * 合服相关
     */
    /**
     * 得到所有合服的日志库
     *
     * @param serverId 源服id
     */
    public TServer getTServer(int serverId) {
        TServerServiceImpl tServerServiceImpl = (TServerServiceImpl) SpringUtils.getBean("TServerServiceImpl");
        TServer tserver = new TServer();
        tserver.setServerId(serverId);
        List<TServer> serverList = tServerServiceImpl.selectTServerList(tserver);
        if (serverList != null && serverList.size() > 0) {
            return serverList.get(0);
        }
        return null;
    }

//    public TDblog getTDblog(int serverId) {
//        TDblogServiceImpl tDblogServiceImpl = (TDblogServiceImpl) SpringUtils.getBean("TDblogServiceImpl");
//        TDblog tDblog = new TDblog();
//        tDblog.setServerId(serverId);
//        List<TDblog> serverLogList = tDblogServiceImpl.selectTDblogList(tDblog);
//        if (serverLogList != null && serverLogList.size() > 0) {
//            return serverLogList.get(0);
//        }
//        return null;
//    }

    /**
     * 递归得到最终合服后的serverId
     */
    public int getFinalHeFuServerId(int serverId) {
        TServer server = this.getTServer(serverId);
        if (server == null) {
            logger.error("t_server中未找到serverId = " + serverId);
            return serverId;
        }
        if (server.getIsHeFu() == 1) {
            return getFinalHeFuServerId(server.getHefuServerID());
        }
        return server.getServerId();
    }

    /**
     * 递归得到最终合服的server
     */
    public TServer getFinalHeFuServer(int serverId) {
        TServer tServer = this.getTServer(serverId);
        if (tServer == null) {
            logger.error("在t_server表中未找到serverId=" + serverId);
            return null;
        }

        if (tServer.getIsHeFu() == 0) {
            return tServer;
        }
        return getFinalHeFuServer(tServer.getHefuServerID());
    }

    /**
     * 递归得到最终合服的dblog
     */
    public TServer getFinalHeFuDBlog(int serverId) {
        TServer tServer = this.getTServer(serverId);
        if (tServer == null) {
            logger.error("在t_dblog表中未找到serverId=" + serverId);
            return null;
        }

        if (tServer.getIsHeFu() == 0) {
            return tServer;
        }
        return getFinalHeFuDBlog(tServer.getHefuServerID());
    }

    /**
     * 检查合服
     *
     * @param serverId
     * @return
     */
    public List<TServer> checkHeFu(int serverId) {
        List<TServer> dbList = new ArrayList<>();
        TServer tServer = this.getTServer(serverId);
        if (tServer == null) {
            logger.error("未找到serverId=" + serverId + "的server");
            return dbList;
        }
        Set<Integer> idSet = new HashSet<>();
        checkHeFuDBlog(tServer, idSet);
        //String ids = idSet.toString().replaceAll("[\\[\\] ]", "");
        List<TServer> list = getHeFuDBLogList(idSet);
        if (list.isEmpty()) {
            logger.error("未找到ids=" + idSet.toString() + "的dblog");
            return dbList;
        }
        dbList.addAll(list);
        return dbList;
    }

    private List<TServer> getHeFuDBLogList(Set<Integer> serverIdList) {
        List<TServer> tAllTServerList = new ArrayList<>();
       // TDblogServiceImpl tDblogServiceImpl = (TDblogServiceImpl) SpringUtils.getBean("TDblogServiceImpl");

        TServerServiceImpl tServerServiceImpl = (TServerServiceImpl) SpringUtils.getBean("TServerServiceImpl");

        TServer tServer = null;
        for (int serverId : serverIdList) {
            tServer = new TServer();
            tServer.setServerId(serverId);
            List<TServer> tServerList =  tServerServiceImpl.selectTServerList(tServer); // tDblogServiceImpl.selectTDblogList(tDblog);
            if (tServerList != null && tServerList.size() > 0) {
                tAllTServerList.addAll(tServerList);
            }
        }
        return tAllTServerList;
    }

    /**
     * 递归查找合服
     *
     * @param tServer
     * @param idSet
     */
    private void checkHeFuDBlog(TServer tServer, Set<Integer> idSet) {
        if (tServer != null) {
            idSet.add(tServer.getServerId());
            TServer dest =  this.getTServer(tServer.getHefuServerID());
            checkHeFuDBlog(dest, idSet);
        }
    }

    /**
     * 递归查找合服
     *
     * @param server
     * @param idSet
     */
    private void checkHeFuServer(TServer server, Set<Integer> idSet) {
        if (server != null) {
            idSet.add(server.getServerId());
            TServer dest = this.getTServer(server.getHefuServerID());
            checkHeFuServer(dest, idSet);
        }
    }

    private List<TServer> getHeFuServerList(int serverId) {
        String sqlStr = "select * from t_server where serverIdList like ?";
        DBClient DBClientGm = this.getDBClient(DBServer.GM);

        List<TServer> tDblogList = DBClientGm.selectList(sqlStr, TServer.class, serverId);
        return tDblogList;
    }

    /**
     * 得到最终合服后的serverId
     */
    public int getHeFuId(int serverId) {
        TServer server = getTServer(serverId);
        if (server == null) {
            logger.error("t_server中未找到serverId = " + serverId);
            return serverId;
        }
        if (server.getIsHeFu() == 1) {
            return getHeFuId(server.getHefuServerID());
        }
        return server.getServerId();
    }

    /**
     * 得到存在查询时间段内的日志库（包括合服的）
     *
     * @param serverId 源服id
     * @param sTime    开始时间
     * @param eTime    结束时间
     * @return 返回需要查询的服
     */
    public List<TServer> checkHeFu(int serverId, Date sTime, Date eTime) {
        List<TServer> dbList = new ArrayList<>();
        TServer tServer = this.getTServer(serverId);
        if (tServer == null) {
            logger.error("未找到serverId=" + serverId + "的server");
            return dbList;
        }
        checkHeFuDBlog(tServer, dbList, sTime, eTime);
        if (dbList.isEmpty()) {
            logger.error("未找到合服的dblog");
            return dbList;
        }
        return dbList;
    }

    /**
     * 检查合服
     *
     * @param tServer
     * @param list
     * @param sTime
     * @param eTime
     */
    private void checkHeFuDBlog(TServer tServer, List<TServer> list, Date sTime, Date eTime) {
        if (eTime.compareTo(new Date()) > 0) {
            eTime = new Date();
        }
        if (tServer.getIsHeFu() == 1) {
            List<TServer> serverList = getHeFuServerList(tServer.getServerId());
            /*
              遍历所有此服和过的服务器日志库
              得到合服时间在查询时间段内的服务器日志库
              并加上合服时间在查询结束时间之后的第一个日志库（如果有的话）
             */
            Map<Integer, TServer> serverKeyMap = new HashMap<>();
            for (TServer serverDb : serverList) {
                if (serverDb.getHefuTime() == null) {
                    serverDb.setHefuTime(new Date());
                }
                //如果合服时间大于等于查询的开始时间
                if (serverDb.getHefuTime().compareTo(sTime) >= 0 && serverDb.getHefuTime().compareTo(eTime) < 0) {
                    list.add(serverDb);//保存到list
                }
                if (serverDb.getHefuTime().compareTo(eTime) > 0) {
                    //算出合服时间到查询结束时间的间隔天数
                    int key = (int) ((serverDb.getHefuTime().getTime() - eTime.getTime()) / (1000 * 60));
                    serverKeyMap.put(key, serverDb);
                }
            }
            //获取合服时间与结算时间间隔最短的计入
            Integer minKey = -1;
            for (Integer key : serverKeyMap.keySet()) {
                if (minKey == -1 || minKey > key) {
                    minKey = key;
                }
            }
            if (minKey != -1) {
                list.add(serverKeyMap.get(minKey));
            }
        } else {
            list.add(tServer);
        }
    }


//    public List<String> queryTables(TDblog dblog, String table) {
//        String sqlStr = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='" + dblog.getDbname() + "' and table_name like '" + table + "%' order by table_name desc";
//        DBClient logDBClient = this.getLogDBClient(dblog);
//        List<String> tables = logDBClient.selectList(sqlStr, (Class<String>) null);
//        if (tables != null) {
//            tables.remove("loguserdobean");
//        }
//        return tables;
//    }

	public List<String> queryTables(TServer tServer, String table) {
		String sqlStr = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='" + tServer.getDblogName() + "' and table_name like '" + table + "%' order by table_name desc";
		DBClient logDBClient = this.getLogDBClient(tServer);
		List<String> tables = logDBClient.selectList(sqlStr, (Class<String>) null);
		if (tables != null) {
			tables.remove("loguserdobean");
		}
		return tables;
	}

    public List<String> getQueryTables(String table, int tableType, Calendar start, Calendar end) {
        List<String> tableList = new ArrayList<>();
        boolean haveTime = true;
        String newTable;
        DateFormat fmt = null;
        switch (tableType) {
            case TableType.Day:
                fmt = new SimpleDateFormat(DateUtils.yyyyMMdd);
                end.setTime(end.getTime());
                DateUtils.getLastOfDay(end);
                break;
            case TableType.Month:
                fmt = new SimpleDateFormat(DateUtils.yyyyMM);
                DateUtils.getLastOfMonth(end);
                break;
            case TableType.Year:
                fmt = new SimpleDateFormat(DateUtils.YYYY);
                DateUtils.getLastOfYear(end);
                break;
            default:
                haveTime = false;
                break;
        }

        if (haveTime) {
            while ((start.getTimeInMillis() <= end.getTimeInMillis())) {
                newTable = table + fmt.format(start.getTime());
                tableList.add(newTable);
                switch (tableType) {
                    case 1:
                        start.add(Calendar.DAY_OF_MONTH, 1);
                        break;
                    case 2:
                        start.add(Calendar.MONTH, 1);
                        break;
                    case 3:
                        start.add(Calendar.YEAR, 1);
                        break;
                }
            }
        } else {
            tableList.add(table);
        }
        return tableList;
    }
    /**
     * 取得时间段内的表名列表
     *
     * @param table     表名
     * @param tableType 表类型
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 表名列表
     */
    public List<String> getQueryTables(String table, int tableType, String startTime, String endTime) {
        List<String> tableList = new ArrayList<>();
        if(startTime == null || endTime == null){
            tableList.add(table);
            return tableList;
        }

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(DateUtils.parseDate(startTime));
        end.setTime(DateUtils.parseDate(endTime));

        boolean haveTime = true;
        String newTable;
        DateFormat fmt = null;
        switch (tableType) {
            case TableType.Day:
                fmt = new SimpleDateFormat(DateUtils.yyyyMMdd);
                end.setTime(DateUtils.parseDate(endTime));
                DateUtils.getLastOfDay(end);
                break;
            case TableType.Month:
                fmt = new SimpleDateFormat(DateUtils.yyyyMM);
                DateUtils.getLastOfMonth(end);
                break;
            case TableType.Year:
                fmt = new SimpleDateFormat(DateUtils.YYYY);
                DateUtils.getLastOfYear(end);
                break;
            default:
                haveTime = false;
                break;
        }

        if (haveTime) {
            while ((start.getTimeInMillis() <= end.getTimeInMillis())) {
                newTable = table + fmt.format(start.getTime());
                tableList.add(newTable);
                switch (tableType) {
                    case 1:
                        start.add(Calendar.DAY_OF_MONTH, 1);
                        break;
                    case 2:
                        start.add(Calendar.MONTH, 1);
                        break;
                    case 3:
                        start.add(Calendar.YEAR, 1);
                        break;
                }
            }
        } else {
            tableList.add(table);
        }
        return tableList;
    }

    /**
     * 得到存在查询时间段内的日志库表名
     *
     * @param serverId 源服id
     * @param table    表名
     * @param sTime    开始时间
     * @param eTime    结束时间
     * @return map<serverId, 表名列表>
     */
    public Map<String, List<String>> getHefuTable(int serverId, String table, Date sTime, Date eTime) {
        Map<String, List<String>> mapList = new HashMap<>();
        List<TServer> dblogList = checkHeFu(serverId, sTime, eTime);
        for (TServer dblog : dblogList) {
            String sqlStr = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='" + dblog.getDblogName() + "' and table_name like '" + table + "%' order by table_name desc";
            DBClient logDBClient = this.getLogDBClient(dblog);
            List<String> list = logDBClient.selectList(sqlStr, (Class<String>) null);
            mapList.put(dblog.getServerId() + "", list);
        }
        return mapList;
    }

}
