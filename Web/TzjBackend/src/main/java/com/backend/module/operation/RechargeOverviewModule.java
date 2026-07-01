package com.backend.module.operation;

import com.backend.bean.Dblog;
import com.backend.filter.MenuFilter;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.utils.QueryUtil;
import com.backend.utils.DateUtil;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@IocBean
@Ok("json")
@At("/rechargeOverview")
@Fail("http:500")
public class RechargeOverviewModule {

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private DecimalFormat df = new DecimalFormat("0.00");

    @Inject
    protected Dao dao;

    @At
    @Ok("jsp:jsp.operation.rechargeOverview")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index(HttpServletRequest request) {
        request.setAttribute("newDate", sdf.format(new Date()));
    }

    @At
    public Object getAllRechargeOverview(String groupName, String channelNames, String[] serverId, String startDate, String endDate, boolean isblack) throws Exception {
        List<Map<String, Object>> rechargeOverviewList = new ArrayList<>();
        channelNames = "'" + channelNames + "'";
        channelNames = channelNames.replace(",", "','");
        String blackUserStr = "";//黑名单账号
        if (isblack) {
            List<Map<String, Object>> blackList = BlackListManager.getInstance().getBlackList(groupName);
            Set<String> blackSet = new HashSet<>();
            for (Map<String, Object> map : blackList) {
                blackSet.add(map.get("userId").toString());
            }
            blackUserStr = blackSet.toString();
            blackUserStr = blackUserStr.substring(1, blackUserStr.length() - 1);
        }
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        List<Date> dateList = new ArrayList<Date>();
        int dvalue = DateUtil.dValue(start, end);
        long oneday = 1000 * 60 * 60 * 24;
        long dateTime = start.getTime();
        Date date;
        for (int i = 0; i < dvalue + 1; i++) {
            date = new Date(dateTime);
            dateList.add(date);
            dateTime += oneday;
        }

        List<String> rechargeGoalTables;
        Map<String, List<String>> hefuTableMap;
        List<String> loginGoalTbables;
        Map<String, List<String>> loginHefuTableMap;
        List<Map<String, Object>> loginUsersMap;//当天登录账号
        List<Map<String, Object>> newUsersMap;//当天新增账号
        List<Map<String, Object>> rechargeUsersMap;//当天付费账号以及付费总额
        List<Map<String, Object>> newRechargeUsersMap;//当天新增付费账号
        List<Map<String, Object>> oldRechargeUsersMap;//老玩家付费账号

        for (Date d : dateList) {// 将数据按天查询
            loginUsersMap = new ArrayList<>();
            newUsersMap = new ArrayList<>();
            rechargeUsersMap = new ArrayList<>();
            newRechargeUsersMap = new ArrayList<>();
            oldRechargeUsersMap = new ArrayList<>();
            String s = sdf.format(d);
            for (String item : serverId) {
                String ROLELOGINLOG = "roleloginlog";
                loginGoalTbables = QueryUtil.getInstance().getQueryTables(ROLELOGINLOG, TableType.Year, s + " 00:00:00", s + " 23:59:59");
                loginHefuTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(item, ROLELOGINLOG, sdf.parse(s), sdf.parse(s + " 23:59:59")));
                for (String key : loginHefuTableMap.keySet()) {
                    List<String> tableList = loginHefuTableMap.get(key);
                    tableList.retainAll(loginGoalTbables);//过滤重复数据表
                    for (String value : tableList) {
                        Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                        if (dblog == null) {
                            continue;
                        }
                        //获取当天登录的账号
                        String loginUserSqlStr = getLoginUsersSql(value, item, channelNames, s, s, blackUserStr);
                        List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, loginUserSqlStr);
                        loginUsersMap.addAll(dataMap);
                    }
                }

                //获取当日新增账号
                int finalServerId = QueryUtil.getInstance().getHeFuId(Integer.parseInt(item));
                Dblog rolestatedblog = DbLogListManager.getInstance().getDBServer(groupName, finalServerId);
                String ROLESTATE = "rolestate";
                String newUserSqlStr = getNewUsersSql(ROLESTATE, item, channelNames, s, s, blackUserStr);
                List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(rolestatedblog, newUserSqlStr);
                newUsersMap.addAll(dataMap);

                String RECHARGESUCCESSLOG = "rechargesuccesslog";
                rechargeGoalTables = QueryUtil.getInstance().getQueryTables(RECHARGESUCCESSLOG, TableType.Year, s + " 00:00:00", s + " 23:59:59");
                hefuTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(item, RECHARGESUCCESSLOG, sdf.parse(s), sdf.parse(s + " 23:59:59")));
                for (String key : hefuTableMap.keySet()) {
                    List<String> tableList = hefuTableMap.get(key);
                    tableList.retainAll(rechargeGoalTables);//过滤重复数据表
                    for (String value : tableList) {
                        Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                        if (dblog == null) {
                            continue;
                        }

                        //获取当天付费的账号以及付费总额
                        String rechargeUserSqlStr = getRechargeUsersSql(value, item, channelNames, s, s, blackUserStr);
                        //System.out.println("sql===="+rechargeUserSqlStr);
                        List<Map<String, Object>> rechargeUsersDataMap = QueryUtil.getInstance().query(dblog, rechargeUserSqlStr);
                        rechargeUsersMap.addAll(rechargeUsersDataMap);

                        //获取当天新增的付费账号
                        String newRechargeUserSqlStr = getNewRechargeUsersSql(value, item, channelNames, s, s, blackUserStr);
                        List<Map<String, Object>> newRechargeUsersDataMap = QueryUtil.getInstance().query(dblog, newRechargeUserSqlStr);
                        newRechargeUsersMap.addAll(newRechargeUsersDataMap);

                        //获取老玩家的付费账号
                        String oldRechargeUserSqlStr = getOldRechargeUsersSql(value, item, channelNames, s, s, blackUserStr);
                        List<Map<String, Object>> oldRechargeUsersDataMap = QueryUtil.getInstance().query(dblog, oldRechargeUserSqlStr);
                        oldRechargeUsersMap.addAll(oldRechargeUsersDataMap);
                    }
                }

            }
            Set<String> loginUsersSet = new HashSet<>();
            Set<String> newUsersSet = new HashSet<>();
            Set<String> rechargeUsersSet = new HashSet<>();
            Set<String> newRechargeUsersSet = new HashSet<>();
            Set<String> oldRechargeUsersSet = new HashSet<>();
            for (Map<String, Object> loginUser : loginUsersMap) {
                loginUsersSet.add(loginUser.get("userId").toString());
            }
            for (Map<String, Object> newUser : newUsersMap) {
                newUsersSet.add(newUser.get("userId").toString());
            }
            double rechargePayment = 0.0;
            for (Map<String, Object> rechargeUser : rechargeUsersMap) {
                rechargeUsersSet.add(rechargeUser.get("userId").toString());
                rechargePayment += Double.valueOf(rechargeUser.get("amount").toString());
            }
            for (Map<String, Object> newRechargeUser : newRechargeUsersMap) {
                newRechargeUsersSet.add(newRechargeUser.get("userId").toString());
            }
            for (Map<String, Object> oldRechargeUser : oldRechargeUsersMap) {
                oldRechargeUsersSet.add(oldRechargeUser.get("userId").toString());
            }
            long loginUsersCount = loginUsersSet.size();
            long newUsersCount = newUsersSet.size();
            long rechargeUsersCount = rechargeUsersSet.size();

            long newRechargeUsersCount = newRechargeUsersSet.size();
            long oldRechargeUsersCount = oldRechargeUsersSet.size();
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("date", s);
            dataMap.put("totalPayment", rechargePayment);
            dataMap.put("DAU", loginUsersCount);
            dataMap.put("newUsers", newUsersCount);
            dataMap.put("rechargeUsers", rechargeUsersCount);
            dataMap.put("newRechargeUsers", newRechargeUsersCount);
            dataMap.put("oldRechargeUsers", oldRechargeUsersCount);
            dataMap.put("newrechargeRate", df.format((double) newRechargeUsersCount / (double) newUsersCount));
            dataMap.put("oldrechargeRate", df.format((double) oldRechargeUsersCount / (double) (loginUsersCount - newUsersCount)));
            dataMap.put("rechargeRate", df.format((double) rechargeUsersCount / (double) loginUsersCount));
            dataMap.put("ARPU", df.format((double) rechargePayment / (double) loginUsersCount));
            dataMap.put("ARPPU", df.format((double) rechargePayment / (double) rechargeUsersCount));

            rechargeOverviewList.add(dataMap);
        }

        return rechargeOverviewList;
    }

    /*
     * 获取当天登录过的账号(DAU)
     */
    private String getLoginUsersSql(String table, String serverId, String channelNames, String start, String end, String blackUserStr) {
        String sqlStr = " SELECT userId";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE TIME BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59') AND sid in (" + serverId + ")";
        if (channelNames != null)
            if (!Strings.isBlank(channelNames)) {
                sqlStr += " AND platformName in (" + channelNames + ")";
            }
        if (!blackUserStr.equals("")) {
            sqlStr += " AND userId NOT IN (" + blackUserStr + ")";
        }
        sqlStr += " GROUP BY userId";
        return sqlStr;
    }

    /*
     * 获取当天付费人数、付费总额
     */
    private String getRechargeUsersSql(String table, String serverId, String channelNames, String start, String end, String blackUserStr) {
        String sqlStr = " SELECT userId,sum(amount) as amount";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE TIME BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59')";
        sqlStr += " AND type=5 AND reason=1 AND sid in ('" + serverId + "')";
        if (channelNames != null)
            if (!Strings.isBlank(channelNames)) {
                sqlStr += " AND platformName in (" + channelNames + ")";
            }
        if (!blackUserStr.equals("")) {
            sqlStr += " AND userId NOT IN (" + blackUserStr + ")";
        }
        sqlStr += " GROUP BY userId";
        return sqlStr;
    }

    /*
     * 获取当天新增账号
     */
    private String getNewUsersSql(String table, String serverId, String channelNames, String start, String end, String blackUserStr) {
        String sqlStr = " SELECT r.userId";
        sqlStr += " FROM (SELECT userId, DATE_FORMAT(MIN(createTime), '%Y-%m-%d') createTime,platformName,createsid FROM " + table + " GROUP BY userId) r";
        sqlStr += " WHERE UNIX_TIMESTAMP(r.createTime) BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59') AND r.createsid in ('" + serverId + "')";
        if (channelNames != null)
            if (!Strings.isBlank(channelNames)) {
                sqlStr += " AND r.platformName in (" + channelNames + ")";
            }
        if (!blackUserStr.equals("")) {
            sqlStr += " AND r.userId NOT IN (" + blackUserStr + ")";
        }
        sqlStr += " GROUP BY r.userId";
        return sqlStr;
    }

    /*
     * 获取当天新增付费账号
     */
    private String getNewRechargeUsersSql(String table, String serverId, String channelNames, String start, String end, String blackUserStr) {
        String sqlStr = " SELECT t.userId FROM (SELECT userId, DATE_FORMAT(FROM_UNIXTIME(time), '%Y-%m-%d') dateTime";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE time BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59')";
        sqlStr += " AND type=5 AND reason=1 AND sid in ('" + serverId + "')";
        sqlStr += " GROUP BY userId ) t";
        sqlStr += " LEFT JOIN (SELECT userId, DATE_FORMAT(MIN(createTime), '%Y-%m-%d') createTime,platformName";
        sqlStr += " FROM rolestate";
        sqlStr += " GROUP BY userId) r";
        sqlStr += " ON t.userId=r.userId";
        sqlStr += " WHERE UNIX_TIMESTAMP(createTime) BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59')";
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND r.platformName in (" + channelNames + ")";
        }
        if (!blackUserStr.equals("")) {
            sqlStr += " AND t.userId NOT IN (" + blackUserStr + ")";
        }
        return sqlStr;
    }

    /*
     * 老玩家付费账号
     */
    private String getOldRechargeUsersSql(String table, String serverId, String channelNames, String start, String end, String blackUserStr) {
        String sqlStr = " SELECT t.userId FROM (SELECT userId, DATE_FORMAT(FROM_UNIXTIME(time), '%Y-%m-%d') dateTime";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE time BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59')";
        sqlStr += " AND type=5 AND reason=1 AND sid in ('" + serverId + "')";
        sqlStr += " GROUP BY userId ) t";
        sqlStr += " LEFT JOIN (SELECT userId, DATE_FORMAT(MIN(createTime), '%Y-%m-%d') createTime,platformName";
        sqlStr += " FROM rolestate";
        sqlStr += " GROUP BY userId) r";
        sqlStr += " ON t.userId=r.userId";
        sqlStr += " WHERE UNIX_TIMESTAMP(createTime) < UNIX_TIMESTAMP('" + start + " 00:00:00') ";
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND r.platformName in (" + channelNames + ")";
        }
        if (!blackUserStr.equals("")) {
            sqlStr += " AND t.userId NOT IN (" + blackUserStr + ")";
        }
        return sqlStr;
    }
}
