package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.bean.User;
import com.backend.filter.MenuFilter;
import com.backend.manager.BlackListManager;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.ExportExcelUtil;
import com.backend.utils.ListToStringUtil;
import com.backend.utils.QueryUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@IocBean
@Ok("json")
@At("/roleCollection")
@Fail("http:500")
/**
 * 玩家收集统计
 */
public class RoleCollectionStatisticModule {
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static String ROLESTATE = "rolestate";
    private Map<User, List<Map<String, Object>>> dataMap = new HashMap<>();
    private String exName = "";

    @Inject
    protected Dao dao;

    @At
    @Ok("jsp:jsp.statistic.roleCollection")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index(HttpServletRequest request) {
        BackendLogUtil.getInstance().log(request, "进入玩家收集统计页面");
        request.setAttribute("nowDate", sdf.format(new Date()));
    }

    @At
    /*
     * 玩家收集统计
     */
    public Object getRoleCollectionData(String platfName, String serverId, String channelNames, String date, int pageIndex, int pageSize, boolean isBlack) {
        String judge = "getRoleCollectionData";
        List<Map<String, Object>> roleCollectionMap = getData(judge, platfName, serverId, channelNames, date, pageIndex, pageSize, isBlack);
        exName = "RoleCollectionCount";
        dataMap.put((User) Mvcs.getHttpSession().getAttribute("USER"), roleCollectionMap);
        return roleCollectionMap;
    }

    @At
    /*
     * 玩家收集统计的条数
     */
    public Object getDataCounts(String platfName, String serverId, String channelNames, String date, int pageIndex, int pageSize, boolean isBlack) {
        String judge = "getDataCounts";
        return getData(judge, platfName, serverId, channelNames, date, pageIndex, pageSize, isBlack);
    }

    private List<Map<String, Object>> getData(String judge, String platfName, String serverId, String channelNames, String date, int pageIndex, int pageSize, boolean isBlack) {
        date = date + " 23:59:59";
        Dblog dblog;
        List<Map<String, Object>> roleCollectionMap = new ArrayList<>();
        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        //黑名单
        String blackUsers = "";
        if (isBlack) {
            List<Object> blackList = BlackListManager.getInstance().getBlackListUsers(platfName.trim());
            if (!blackList.isEmpty()) {
                blackUsers = ListToStringUtil.listToString(blackList);
            }
        }
        if (!Strings.isBlank(platfName)) {
            int sid = QueryUtil.getInstance().getHeFuId(Integer.parseInt(serverId));
            dblog = dao.fetch(Dblog.class, Cnd.where("serverId", "=", sid).and("isDeleted", "=", 0));
            if (dblog != null) {
                if (judge.equals("getDataCounts")) {
                    String sqlCountStr = getRoleCollectionCountsSql(ROLESTATE, channelNames, date, blackUsers);
                    Map<String, Object> countMap = new TreeMap<>();
                    int counts = QueryUtil.getInstance().queryCount(dblog, sqlCountStr);
                    countMap.put("counts", counts);
                    roleCollectionMap.add(countMap);
                } else if (judge.equals("getRoleCollectionData")) {
                    //获取sql
                    String sqlStr = getRoleCollectionDataSql(ROLESTATE, channelNames, date, pageIndex, pageSize, blackUsers);
                    List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, sqlStr);

                    for (Map<String, Object> map : dataMap) {
                        //columnName：列名
                        for (String columnName : map.keySet()) {
                            if (columnName.equals("roleId") || columnName.equals("userId")) {
                                String idStr = map.get(columnName).toString();
                                map.put(columnName, idStr);
                            }
                            if (columnName.equals("horseIds") || columnName.equals("wingIds") || columnName.equals("petIds")) {
                                if (!map.containsValue(null)) {
                                    String countStr = map.get(columnName).toString();
                                    if (!Strings.isBlank(countStr)) {
                                        countStr = countStr.substring(1, (countStr.length() - 1));
                                        int count = 0;
                                        if (!countStr.equals("")) {
                                            String[] counts = countStr.split(",");
                                            count = counts.length;
                                        }
                                        map.put(columnName, count);
                                    }
                                }
                            }
                        }
                    }
                    roleCollectionMap.addAll(dataMap);
                }
            }
        }

        return roleCollectionMap;
    }

    private String getRoleCollectionDataSql(String table, String channelNames, String date, int pageIndex, int pageSize, String blackUsers) {
        String strSql = "select platformName,userId,roleId,roleName,horseIds,wingIds,petIds,date_FORMAT(FROM_UNIXTIME(ts), '%Y-%m-%d') date";
        strSql += " FROM " + table;
        strSql += " WHERE ts <= UNIX_TIMESTAMP('" + date + "') ";
        if (!Strings.isBlank(blackUsers)) {
            strSql += " AND userId NOT IN (";
            strSql += blackUsers + ")";
        }
        if (!Strings.isBlank(channelNames)) {
            strSql += " and platformName in (" + channelNames + ")";
        }
        strSql += " group by roleId limit " + pageIndex + "," + pageSize;
        return strSql;
    }

    private String getRoleCollectionCountsSql(String table, String channelNames, String date, String blackUsers) {
        String strSql = "select count(*) count from (select platformName,userId,roleId,roleName,horseIds,wingIds,petIds,date_FORMAT(FROM_UNIXTIME(ts), '%Y-%m-%d') date";
        strSql += " FROM " + table;
        strSql += " WHERE ts <= UNIX_TIMESTAMP('" + date + "') ";
        if (!Strings.isBlank(blackUsers)) {
            strSql += " AND userId NOT IN (";
            strSql += blackUsers + ")";
        }
        if (!Strings.isBlank(channelNames)) {
            strSql += " and platformName in (" + channelNames + ")";
        }
        strSql += " group by roleId) a";
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
