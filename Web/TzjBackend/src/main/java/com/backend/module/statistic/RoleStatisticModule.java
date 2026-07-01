package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.bean.Server;
import com.backend.bean.User;
import com.backend.filter.MenuFilter;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.ExportExcelUtil;
import com.backend.utils.QueryUtil;
import com.backend.utils.Toolkit;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@IocBean
@Ok("json")
@At("/roleStatistic")
@Fail("http:500")
public class RoleStatisticModule {

    private Map<User, List<Map<String, Object>>> dataMap = new HashMap<>();
    private String exName = "";

    @Inject
    protected Dao dao;

    @At("/")
    @Ok("jsp:jsp.statistic.roleStatistic")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index(HttpServletRequest request) {
        BackendLogUtil.getInstance().log(request, "进入角色信息统计页面");
    }

    @At
    /*
     * 全服角色信息统计
     */
    public Object roleState(String groupName, String serverId, String channelNames, String sortType, int pageIndex, int pageSize) {
        String judge = "roleState";
        List<Map<String, Object>> resultList = getData(judge, groupName, serverId, channelNames, sortType, pageIndex, pageSize);
        Map<String, String> langMap = Mvcs.getMessages(Mvcs.getReq());
        if (resultList.isEmpty()) {
            return Toolkit.outResult(false, langMap.get("jsp.rolelog.nodata"));
        }
        exName = "RoleState";
        dataMap.put((User) Mvcs.getHttpSession().getAttribute("USER"), resultList);
        return resultList;
    }

    @At
    /*
     * 全服角色信息的总条数
     */
    public Object roleStateCount(String groupName, String serverId, String channelNames, String sortType, int pageIndex, int pageSize) {
        String judge = "roleStateCount";
        List<Map<String, Object>> resultList = getData(judge, groupName, serverId, channelNames, sortType, pageIndex, pageSize);
        Map<String, String> langMap = Mvcs.getMessages(Mvcs.getReq());
        if (resultList.isEmpty()) {
            return new NutMap().setv("ok", false).setv("msg", langMap.get("jsp.rolelog.nodata"));
        }
        return resultList;
    }

    //获取数据
    private List<Map<String, Object>> getData(String judge, String groupName, String serverId, String channelNames, String sortType, int pageIndex, int pageSize) {

        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }

        List<Map<String, Object>> resultList = new ArrayList<>();
        if (Strings.isBlank(sortType)) sortType = "rechargeGold";//设置默认

        String table = "rolestate";
        Dblog dblog;
        List<Map<String, Object>> tempList;
        //指定单服查询
        if (!Strings.isBlank(groupName)) {
            int sid = QueryUtil.getInstance().getHeFuId(Integer.parseInt(serverId));
            dblog = dao.fetch(Dblog.class, Cnd.where("serverId", "=", sid).and("isDeleted", "=", 0));
            if (dblog != null) {
                if (judge.equals("roleStateCount")) {
                    String sqlCountStr = getRoleStateCountSql(table, channelNames);
                    Map<String, Object> countMap = new TreeMap<>();
                    int counts = QueryUtil.getInstance().queryCount(dblog, sqlCountStr);
                    countMap.put("counts", counts);
                    resultList.add(countMap);
                } else if (judge.equals("roleState")) {
                    String sqlStr = getRoleStateSql(table, channelNames, sortType, pageIndex, pageSize);
                    tempList = QueryUtil.getInstance().query(dblog, sqlStr);
                    if (!tempList.isEmpty()) {
                        for (Map<String, Object> map : tempList) {
                            //columnName：列名
                            for (String columnName : map.keySet()) {
                                if (columnName.equals("roleId")) {
                                    String roleIdStr = map.get(columnName).toString();
                                    map.put(columnName, roleIdStr);
                                }
                                if (columnName.equals("userId")) {
                                    String userIdStr = map.get(columnName).toString();
                                    map.put(columnName, userIdStr);
                                }
                            }
                        }
                    }
                    resultList.addAll(tempList);
                }
            }
            return resultList;
        }

        //全区服查询
        List<Server> serverList = dao.query(Server.class, Cnd.where("serverType", "=", 1).and("isHeFu", "=", 0).and("isDeleted", "=", 0));
        List<String> realTables;
        for (Server server : serverList) {
            dblog = dao.fetch(Dblog.class, Cnd.where("serverId", "=", server.getServerId()));
            if (dblog == null) {
                continue;
            }
            realTables = QueryUtil.getInstance().queryTables(dblog, table);
            if (!realTables.isEmpty()) {
                if (judge.equals("roleStateCount")) {
                    String sqlCountStr = getRoleStateCountSql(table, channelNames);
                    Map<String, Object> countMap = new TreeMap<>();
                    int counts = QueryUtil.getInstance().queryCount(dblog, sqlCountStr);
                    countMap.put("counts", counts);
                    resultList.add(countMap);
                } else if (judge.equals("roleState")) {
                    String sqlStr = getRoleStateSql(table, channelNames, sortType, pageIndex, pageSize);
                    tempList = QueryUtil.getInstance().query(dblog, sqlStr);
                    if (!tempList.isEmpty()) {
                        for (Map<String, Object> map : tempList) {
                            //columnName：列名
                            for (String columnName : map.keySet()) {
                                if (columnName.equals("roleId") || columnName.equals("userId")) {
                                    String idStr = map.get(columnName).toString();
                                    map.put(columnName, idStr);
                                }
                            }
                        }
                    }
                    resultList.addAll(tempList);
                }
            }
        }
        return resultList;
    }

    //获取数据的SQL
    private String getRoleStateSql(String table, String channelNames, String sortType, int pageIndex, int pageSize) {
        String strSql = "select t2.platformName,t2.createsid,t2.funcellUUid,t2.userId,t2.roleId,t2.roleName,t2.`level`,t2.onlineTime,t2.rechargeGold,t2.gold,t2.createTime,t2.lastLoginTime,t2.isDelete,t2.ts from " + table + " t2,(SELECT t0.userId,max(t0.ts) as maxtime FROM " + table + " t0 GROUP BY t0.userId) t1 where t2.userId=t1.userId and t2.ts=t1.maxtime ";
        if (!Strings.isBlank(channelNames)) {
            strSql += " and platformName in (" + channelNames + ")";
        }
        strSql += " order by t2." + sortType + " desc limit " + pageIndex + "," + pageSize;
        return strSql;
    }

    //获取数据条数的SQL
    private String getRoleStateCountSql(String table, String channelNames) {
        String strSql = "select count(*) as counts from (select t2.platformName,t2.createsid,t2.funcellUUid,t2.userId,t2.roleId,t2.roleName,t2.`level`,t2.onlineTime,t2.rechargeGold,t2.gold,t2.createTime,t2.lastLoginTime,t2.isDelete,t2.ts from " + table + " t2,(SELECT t0.userId,max(t0.ts) as maxtime FROM " + table + " t0 GROUP BY t0.userId) t1 where t2.userId=t1.userId and t2.ts=t1.maxtime ";
        if (!Strings.isBlank(channelNames)) {
            strSql += " and platformName in (" + channelNames + ")";
        }
        strSql += ") t3";
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
