package com.backend.utils;

import com.backend.bean.Server;
import com.backend.log.struct.TableType;
import com.backend.manager.CrossManager;
import com.backend.manager.ServerListManager;
import org.apache.log4j.Logger;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.sql.Sql;

import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class QueryUtil {

    private static final Logger log = Logger.getLogger(QueryUtil.class);

    private DateFormat dtdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat ddf = new SimpleDateFormat("yyyyMMdd");
    private DateFormat mdf = new SimpleDateFormat("yyyyMM");
    private DateFormat ydf = new SimpleDateFormat("yyyy");

    private Dao dao;

    private enum Singleton {

        INSTANCE;
        QueryUtil manager;

        Singleton() {
            this.manager = new QueryUtil();
        }

        QueryUtil getProcessor() {
            return manager;
        }
    }

    public static QueryUtil getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public void init(Dao dao) {
        this.dao = dao;
    }

    /**
     * 得到最终合服后的serverId
     */
    public int getHeFuId(int serverId) {
        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            log.error("t_server中未找到serverId = " + serverId);
            return serverId;
        }
        if (server.getIsHeFu() == 1) {
            return getHeFuId(server.getHefuServerID());
        }
        return server.getServerId();
    }

    /**
     * 得到最终合服的dblog
     */
    public Server getFinalHeFuDB(int serverId) {

        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            log.error("在t_dblog表中未找到serverId=" + serverId);
            return null;
        }

        if (server.getIsHeFu() == 0) {
            return server;
        }
        return getFinalHeFuDB(server.getHefuServerID());
    }


    /**
     * 得到所有合服的日志库
     *
     * @param serverId 源服id
     */
    public List<Server> checkHeFu(int serverId) {
        List<Server> dbList = new ArrayList<>();
        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            log.error("未找到serverId=" + serverId + "的server");
            return dbList;
        }

        Set<Integer> idSet = new HashSet<>();
        checkHeFuServer(server, idSet);

        String ids = idSet.toString().replaceAll("[\\[\\] ]", "");
        List<Server> list = getHeFuDBLogList(ids);
        if (list.isEmpty()) {
            log.error("未找到ids=" + ids + "的dblog");
            return dbList;
        }
        dbList.addAll(list);
        return dbList;
    }

    private List<Server> getHeFuDBLogList(String idStr) {
        String sqlStr = "select * from $table where serverId in($ids) and isDeleted=0";
        Sql sql = Sqls.create(sqlStr);
        sql.vars().set("table", "t_server");
        sql.vars().set("ids", idStr);
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(dao.getEntity(Server.class));
        dao.execute(sql);
        return sql.getList(Server.class);
    }

    private void checkHeFuServer(Server server, Set<Integer> idSet) {
        if (server != null) {
            idSet.add(server.getServerId());
            Server dest = ServerListManager.getInstance().getServer(server.getHefuServerID());
            checkHeFuServer(dest, idSet);
        }
    }

    /**
     * 得到存在查询时间段内的日志库（包括合服的）
     *
     * @param serverId 源服id
     * @param sTime    开始时间
     * @param eTime    结束时间
     * @return 返回需要查询的服
     */
    public List<Server> checkHeFu(int serverId, Date sTime, Date eTime) {
        List<Server> dbList = new ArrayList<>();
        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            server = CrossManager.getInstance().getDB(serverId);
        }
        if (server == null) {
            log.error("未找到serverId=" + serverId + "的server");
            return dbList;
        }

        checkHeFuServer(server, dbList, sTime, eTime);

        if (dbList.isEmpty()) {
            log.error("未找到合服的dblog");
            return dbList;
        }
        return dbList;
    }

    private void checkHeFuServer(Server server, List<Server> list, Date sTime, Date eTime) {
        if (eTime.compareTo(new Date()) > 0) {
            eTime = new Date();
        }
        if (server.getIsHeFu() == 1) {
            List<Server> serverList = getHeFuServerList(server.getServerId());
            /*
              遍历所有此服和过的服务器日志库
              得到合服时间在查询时间段内的服务器日志库
              并加上合服时间在查询结束时间之后的第一个日志库（如果有的话）
             */
            Map<Integer, Server> serverKeyMap = new HashMap<>();
            for (Server serverDb : serverList) {
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
            list.add(server);
        }
    }

    private List<Server> getHeFuServerList(int serverId) {
        String sqlStr = "select * from $table where serverIdList like $serverId";
        Sql sql = Sqls.create(sqlStr);
        sql.vars().set("table", "t_server");
        sql.vars().set("serverId", "'%" + serverId + "%'");
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(dao.getEntity(Server.class));
        dao.execute(sql);
        return sql.getList(Server.class);
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
    public Map<String, List<String>> getHefuTable(String serverId, String table, Date sTime, Date eTime) {
        Map<String, List<String>> mapList = new HashMap<>();
        SimpleDataSource dsLog;
        List<Server> dblogList = checkHeFu(Integer.parseInt(serverId), sTime, eTime);
        Dao daoLog;
        for (Server dblog : dblogList) {
            dsLog = DbConfigUtil.getInstance().getSDS(dblog);
            daoLog = new NutDao(dsLog);
            String sqlStr = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='$dbname' and table_name like '$table%' order by table_name desc";
            Sql sql = Sqls.create(sqlStr);
            sql.vars().set("dbname", dblog.getDblogName());
            sql.vars().set("table", table);
            sql.setCallback((Connection conn, ResultSet rs, Sql sql1) -> {
                List<String> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(rs.getString("TABLE_NAME"));
                }
                return list;
            });
            daoLog.execute(sql);
            dsLog.close();
            mapList.put(dblog.getServerId() + "", sql.getList(String.class));
        }
        return mapList;
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
    public List<String> getQueryTables(String table, int tableType, String startTime, String endTime) throws ParseException {
        Date starDate = dtdf.parse(startTime);
        Date endDate = dtdf.parse(endTime);
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(starDate.getTime());
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(endDate.getTime());
        return getQueryTables(table, tableType, start, end);
    }

    /**
     * 取得时间段内的表名列表
     *
     * @param table     表名
     * @param tableType 表类型
     * @param start     开始时间
     * @param end       结束时间
     * @return 表名列表
     */
    public List<String> getQueryTables(String table, int tableType, Calendar start, Calendar end) {
        List<String> tableList = new ArrayList<>();
        boolean haveTime = true;
        String newTable;
        DateFormat fmt = null;
        switch (tableType) {
            case TableType.Day:
                fmt = ddf;
                DateUtil.getLastOfDay(end);
                break;
            case TableType.Month:
                fmt = mdf;
                DateUtil.getLastOfMonth(end);
                break;
            case TableType.Year:
                fmt = ydf;
                DateUtil.getLastOfYear(end);
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
     * 取得日志库中实际存在的表名
     *
     * @param dblog 日志库信息
     * @param table 表名
     * @return 实际存在的表名
     */
    public List<String> queryTables(Server dblog, String table) {
        SimpleDataSource dsLog = DbConfigUtil.getInstance().getSDS(dblog);
        Dao daoLog = new NutDao(dsLog);
        return queryTables(daoLog, dblog.getDblogName(), table);
    }

    @SuppressWarnings("unchecked")
    public List<String> queryTables(Dao daoLSLog, String dbName, String table) {
        String sqlStr = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='$dbName'" +
                " and table_name like '$table%' order by table_name desc";
        Sql sql = Sqls.create(sqlStr);
        sql.vars().set("dbName", dbName);
        sql.vars().set("table", table);
        sql.setCallback((Connection conn, ResultSet rs, Sql sql1) -> {
            List list = new ArrayList();
            while (rs.next()) {
                list.add(rs.getString(1));
            }
            return list;
        });
        daoLSLog.execute(sql);
        List<String> tables = sql.getList(String.class);
        tables.remove("loguserdobean");
        return tables;
    }

    /**
     * 获取条数
     *
     * @param dblog  日志库
     * @param sqlStr sql
     * @return 条数
     */
    public int queryCount(Server dblog, String sqlStr) {
        SimpleDataSource dsLog = DbConfigUtil.getInstance().getSDS(dblog);
        Dao daoLog = new NutDao(dsLog);
        Sql sql = Sqls.create(sqlStr);
        sql.setCallback((Connection conn, ResultSet rs, Sql sql1) -> {
            int counts = 0;
            while (rs.next()) {
                counts = rs.getInt(1);
            }
            return counts;
        });
        daoLog.execute(sql);
        dsLog.close();
        return (int) sql.getResult();
    }

    public int queryCount(Server dblog, String sqlStr, String table) {
        SimpleDataSource dsLog = DbConfigUtil.getInstance().getSDS(dblog);
        Dao daoLog = new NutDao(dsLog);
        Sql sql = Sqls.create(sqlStr);
        sql.vars().set("table", table);
        sql.setCallback((Connection conn, ResultSet rs, Sql sql1) -> {
            int counts = 0;
            while (rs.next()) {
                counts = rs.getInt(1);
            }
            return counts;
        });
        daoLog.execute(sql);
        dsLog.close();
        return (int) sql.getResult();
    }

    /**
     * 查询表字段
     *
     * @param dblog  日志库
     * @param sqlStr sql
     * @return list<map < string, obj>>
     */
    public List<Map<String, Object>> query(Server dblog, String sqlStr) {
        SimpleDataSource dsLog = DbConfigUtil.getInstance().getSDS(dblog);
        Dao daoLog = new NutDao(dsLog);
        return query(daoLog, sqlStr);
    }

    /**
     * 查询表字段
     *
     * @param dao    dao
     * @param sqlStr sql
     * @return list<map < string, obj>>
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> query(Dao dao, String sqlStr) {
        Sql sql = Sqls.create(sqlStr);
        sql.setCallback((Connection conn, ResultSet rs, Sql sql1) -> {
            List list = new ArrayList();
            Map<String, Object> map;
            while (rs.next()) {
                map = new HashMap<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    map.put(rs.getMetaData().getColumnName(i), rs.getObject(i) == null ? "" : rs.getObject(i).toString());
                }
                list.add(map);
            }
            return list;
        });
        dao.execute(sql);
        return (List<Map<String, Object>>) sql.getResult();
    }

    /**
     * 查询表字段
     *
     * @param dblog  日志库
     * @param sqlStr sql
     * @param table  表名
     * @return list<map < string, obj>>
     */
    public List<Map<String, String>> query(Server dblog, String sqlStr, String table) {
        return query(dblog, sqlStr, table, new HashMap<>());
    }

    /**
     * 查询表字段
     *
     * @param dblog   日志库
     * @param sqlStr  sql
     * @param table   表名
     * @param paraMap 参数
     * @return list<map < string, obj>>
     */
    public List<Map<String, String>> query(Server dblog, String sqlStr, String table, Map<String, Object> paraMap) {
        SimpleDataSource dsLog = DbConfigUtil.getInstance().getSDS(dblog);
        Dao daoLog = new NutDao(dsLog);
        return query(daoLog, sqlStr, table, paraMap);
    }

    /**
     * 查询表字段
     *
     * @param dao     dao
     * @param sqlStr  sql
     * @param table   表名
     * @param paraMap 参数
     * @return map<string, object>
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, String>> query(Dao dao, String sqlStr, String table, Map<String, Object> paraMap) {
        Sql sql = Sqls.create(sqlStr);
        sql.vars().set("table", table);
        for (Entry<String, Object> entry : paraMap.entrySet()) {
            sql.params().set(entry.getKey(), entry.getValue());
        }
        sql.setCallback((Connection conn, ResultSet rs, Sql sql1) -> {
            List<Map<String, Object>> list = new ArrayList();
            Map<String, Object> map;
            Blob blob;
            while (rs.next()) {
                map = new HashMap<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if (rs.getMetaData().getColumnTypeName(i).equals("BLOB")) {
                        blob = rs.getBlob(i);
                        map.put(rs.getMetaData().getColumnName(i), new String(blob.getBytes(1, (int) blob.length()), StandardCharsets.UTF_8));
                    } else {
                        if (rs.getObject(i) == null) {
                            map.put(rs.getMetaData().getColumnName(i), "null");
                            continue;
                        }
                        map.put(rs.getMetaData().getColumnName(i), rs.getObject(i).toString());
                    }
                }
                list.add(map);
            }
            return list;
        });
        dao.execute(sql);
        return (List<Map<String, String>>) sql.getResult();
    }

    /**
     * 查询实体类列表
     *
     * @param dblog       日志库
     * @param sqlStr      sql
     * @param table       表名
     * @param paraMap     参数
     * @param resultClass 实体class
     * @return List<?>
     */
    public List<?> queryWithEntity(Server dblog, String sqlStr, String table, Map<String, Object> paraMap, Class<?> resultClass) {
        SimpleDataSource dsLog = DbConfigUtil.getInstance().getSDS(dblog);
        Dao daoLog = new NutDao(dsLog);

        Sql sql = Sqls.create(sqlStr);
        sql.vars().set("table", table);
        for (Entry<String, Object> entry : paraMap.entrySet()) {
            sql.params().set(entry.getKey(), entry.getValue());
        }
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(daoLog.getEntity(resultClass));
        daoLog.execute(sql);
        dsLog.close();
        return sql.getList(resultClass);
    }


    /**
     * 统计查询game日志库
     *
     * @param dblog   日志库
     * @param sqlStr  sql
     * @param key     groupBy字段
     * @param paraMap 参数
     * @return map<string, map < string>>
     */
    @SuppressWarnings("unchecked")
    public Map<String, Map<String, String>> queryStat(Server dblog, String sqlStr, String key, Map<String, Object> paraMap) {
        SimpleDataSource dsLog = DbConfigUtil.getInstance().getSDS(dblog);
        Dao daoLog = new NutDao(dsLog);

        Sql sql = Sqls.create(sqlStr);
        for (Entry<String, Object> entry : paraMap.entrySet()) {
            sql.params().set(entry.getKey(), entry.getValue());
        }
        sql.setCallback((Connection conn, ResultSet rs, Sql sql1) -> {
            Map<String, Map<String, String>> result = new TreeMap<>();
            int count = rs.getMetaData().getColumnCount();
            Map<String, String> map;
            while (rs.next()) {
                map = new HashMap<>();
                for (int i = 1; i <= count; i++) {
                    map.put(rs.getMetaData().getColumnName(i), rs.getObject(i) + "");
                }
                // 移除group by的字段
                map.remove(rs.getString(key));
                result.put(rs.getString(key), map);
            }
            return result;
        });
        daoLog.execute(sql);
        dsLog.close();
        return (Map<String, Map<String, String>>) sql.getResult();
    }

}
