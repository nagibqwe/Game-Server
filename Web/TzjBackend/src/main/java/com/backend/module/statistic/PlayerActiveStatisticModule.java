package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.bean.User;
import com.backend.filter.MenuFilter;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.manager.ItemManager;
import com.backend.utils.*;
import net.sf.json.JSONArray;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@IocBean
@Ok("json")
@At("/playeractive")
@Fail("http:500")
//玩家活跃度统计
public class PlayerActiveStatisticModule {
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Map<User, List<Map<String, Object>>> dataMap = new HashMap<>();
    private String exName = "";

    @At
    @Ok("jsp:jsp.statistic.playerActive")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    //初始化界面
    public void index(HttpServletRequest request) {
        request.setAttribute("nowDate", sdf.format(new Date()));
        BackendLogUtil.getInstance().log(request, "进入玩家活跃度统计页面");
        Set<String> groupNames = DbLogListManager.getInstance().getPlatformDBNames();
        //获取平台列表
        request.setAttribute("groupNameList", groupNames);
        //获取平台列表(直接使用接口的)
        //request.setAttribute("groupcheckbox",GlobalModule.GetGroupSelect());
        //获取平台对应游戏数据库列表
        //request.setAttribute("servercheckbox", GlobalModule.renderCheckBox(ServerListManager.getInstance().getNoHeFuServerListByGroup(g)));
        List<Map<String, Object>> dbServerList = new ArrayList<>();
        //获取平台对应游戏数据库列表
        for (String groupName : groupNames) {
            List<Dblog> dbServers = DbLogListManager.getInstance().getServerDBs(groupName);
            for (Dblog dbServer : dbServers) {
                Map<String, Object> dbServerMap = new HashMap<>();
                dbServerMap.put("groupName", dbServer.getGroupName());
                dbServerMap.put("serverType", dbServer.getServerType());
                dbServerMap.put("serverId", dbServer.getServerId());
                dbServerMap.put("serverName", dbServer.getServerName());
                dbServerList.add(dbServerMap);
            }
        }
        JSONArray ja = new JSONArray();
        ja.addAll(dbServerList);
        request.setAttribute("dbServerList", ja);
        //获取副本列表
        //TODO 删除读gamedata库
//		Map<String, String> cloneMaps = CopyMapManager.getInstance().getCopyMapNameMap();
//		Map<Integer,String> cloneMapList = new TreeMap<Integer,String>();
//		for(String key : cloneMaps.keySet()){
        //TODO 修改item报错
//			String cloneMapName = ItemManager.getInstance().getGameData(key, cloneMaps.get(key));
//			Integer keyInt = Integer.parseInt(key);
//			cloneMapList.put(keyInt,cloneMapName);
//		}
//		request.setAttribute("cloneMapList",cloneMapList);
    }


    //统计玩家角色进入副本数
    @At
    public Object userRoleCount(String groupName, String channelNames, String startDate, String endDate, String[] serverId, String cloneIds, boolean isBlack) throws Exception {

        String judge = "userRoleCount";
        List<Map<String, Object>> copyMapEnterLogMap = getDataList(groupName, channelNames, startDate, endDate, serverId, cloneIds, judge, isBlack);
        exName = "EnterCopyMapRoles";
        dataMap.put((User) Mvcs.getHttpSession().getAttribute("USER"), copyMapEnterLogMap);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(copyMapEnterLogMap);
        return jsonArray;
    }

    //统计玩家进入副本数
    @At
    public Object userCount(String groupName, String channelNames, String startDate, String endDate, String[] serverId, String cloneIds, boolean isBlack) throws Exception {
        String judge = "userCount";
        List<Map<String, Object>> copyMapEnterLogMap = getDataList(groupName, channelNames, startDate, endDate, serverId, cloneIds, judge, isBlack);
        exName = "EnterCopyMapUsers";
        dataMap.put((User) Mvcs.getHttpSession().getAttribute("USER"), copyMapEnterLogMap);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(copyMapEnterLogMap);
        return jsonArray;
    }

    private List<Map<String, Object>> getDataList(String groupName, String channelNames, String startDate, String endDate, String[] serverId, String cloneIds, String judge, boolean isBlack) throws Exception {
        startDate = startDate + " 00:00:00";
        endDate = endDate + " 23:59:59";
        //获取从页面传过来的副本id列表
        String[] cloneIdStrs = cloneIds.split(",");
        Map<String, String> cloneMap = new TreeMap<>();
        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        //黑名单
        String blackUsers = "";
        if (isBlack) {
            List<Object> blackList = BlackListManager.getInstance().getBlackListUsers(groupName.trim());
            if (!blackList.isEmpty()) {
                blackUsers = ListToStringUtil.listToString(blackList);
            }
        }
        //将副本列表按照副本名称，副本id组成map，并合并重复key的副本map
        for (String str : cloneIdStrs) {
            String[] cloneIdName = str.split("-");
            if (!cloneMap.containsKey(cloneIdName[1])) {
                cloneMap.put(cloneIdName[1], cloneIdName[0]);
            } else {
                cloneMap.put(cloneIdName[1], cloneMap.get(cloneIdName[1]) + "," + cloneIdName[0]);
            }
        }
        List<Map<String, Object>> copyMapEnterLogMap = new ArrayList<>();
        for (String s : serverId) {
            String[] tables = new String[]{"copymapenterlog", "crosscloneenterlog"};
            for (String table : tables) {
                List<String> goalTables = QueryUtil.getInstance().getQueryTables(table, TableType.Month, startDate, endDate);
                Map<String, List<String>> hefuTableMap = new TreeMap<>(QueryUtil.getInstance().getHefuTable(s, table, sdf.parse(startDate), sdf.parse(endDate)));
                for (String key : hefuTableMap.keySet()) {
                    List<String> tableList = hefuTableMap.get(key);
                    tableList.retainAll(goalTables);//过滤重复数据表
                    for (String value : tableList) {
                        for (Entry<String, String> entry : cloneMap.entrySet()) {
                            String cloneId = entry.getValue();
                            //获取玩家总数的sql
                            String sqlStr = "";
                            if (judge.equals("userRoleCount")) {
                                sqlStr = userRoleCountSql(value, channelNames, startDate, endDate, cloneId, blackUsers);
                            } else if (judge.equals("userCount")) {
                                sqlStr = userCountSql(value, channelNames, startDate, endDate, cloneId, blackUsers);
                            }

                            Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                            if (dblog == null) {
                                continue;
                            }
                            List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                            for (Map<String, Object> map : dataMap) {
                                //columnName：列名
                                for (String columnName : map.keySet()) {
                                    if (columnName.equals("cloneId")) {
                                        String cloneIdStr = map.get(columnName).toString();
                                        map.put(columnName, ItemManager.getInstance().getStr(cloneIdStr, "clone_map"));
                                    }
                                    if (columnName.equals("roleId") || columnName.equals("userId")) {
                                        String idStr = map.get(columnName).toString();
                                        map.put(columnName, idStr);
                                    }
                                }
                            }
                            copyMapEnterLogMap.addAll(dataMap);
                        }
                    }
                }
            }
        }

        return copyMapEnterLogMap;
    }

    //每个玩家下每个角色每天进入此副本的次数统计sql(非战斗服)
    private String userRoleCountSql(String table, String channelNames, String startDate, String endDate, String cloneId, String blackUsers) {
        String strSql = "";
        strSql += " SELECT userId,roleId,roleName,platformName,cloneId,clonename,sid,max(level) as level,DATE_FORMAT(FROM_UNIXTIME(TIME), '%Y-%m-%d') AS joinTime,COUNT(*) AS counts";
        strSql += " FROM " + table;
        strSql += " WHERE cloneId IN (" + cloneId + ") AND TIME BETWEEN UNIX_TIMESTAMP('" + startDate + "') AND UNIX_TIMESTAMP('" + endDate + "')";
        if (!Strings.isBlank(blackUsers)) {
            strSql += " AND userId NOT IN (" + blackUsers + ")";
        }
        if (!Strings.isBlank(channelNames)) {
            strSql += " and platformName in (" + channelNames + ")";
        }
        strSql += " GROUP BY roleId,DATE_FORMAT(FROM_UNIXTIME(TIME), '%Y-%m-%d')";
        strSql += " ORDER BY joinTime ASC";
        return strSql;
    }

    //每个玩家下每天进入此副本的次数统计sql
    private String userCountSql(String table, String channelNames, String startDate, String endDate, String cloneId, String blackUsers) {
        String strSql = "";
        strSql += "SELECT c.userId,c.sid,c.platformName,c.cloneId,SUM(c.joinCount) AS counts,c.joinDate AS DATE";
        strSql += " FROM (SELECT userId,platformName,cloneId,sid,DATE_FORMAT(FROM_UNIXTIME(TIME), '%Y-%m-%d') AS joinDate,COUNT(*) AS joinCount";
        strSql += " FROM " + table;
        strSql += " WHERE cloneId IN (" + cloneId + ") AND TIME BETWEEN UNIX_TIMESTAMP('" + startDate + "') AND UNIX_TIMESTAMP('" + endDate + "')";
        if (!Strings.isBlank(blackUsers)) {
            strSql += " AND userId NOT IN (" + blackUsers + ")";
        }
        if (!Strings.isBlank(channelNames)) {
            strSql += " and platformName in (" + channelNames + ")";
        }
        strSql += " GROUP BY userId, DATE_FORMAT(FROM_UNIXTIME(TIME), '%Y-%m-%d')) c GROUP BY c.userId,DATE ORDER BY DATE ASC";
        return strSql;
    }

    @At
    @Ok("")
    public void exportExcel() {
        // 取得文件名
        String fileName = exName;
        // 取得表名
        String tableName = exName;
        ExportExcelUtil ex = new ExportExcelUtil();
        User user = (User) Mvcs.getHttpSession().getAttribute("USER");
        List<Map<String, Object>> dataMapList = new ArrayList<>();
        for (User key : dataMap.keySet()) {
            if (key.getId() == user.getId()) {
                dataMapList = dataMap.get(key);
                break;
            }
        }
        HttpServletResponse response = Mvcs.getResp();
        ex.exportExcel(tableName, dataMapList, response, fileName);
    }

}
