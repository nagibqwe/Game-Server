package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.filter.MenuFilter;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.QueryUtil;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@IocBean
@Ok("json")
@At("/businessContent")
@Fail("http:500")
public class BusinessContentStatisticModule {

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static String RECHARGESUCCESSLOG = "rechargesuccesslog";
    private static String DAILYRECHARGEAWARDLOG = "dailyrechargeawardlog";

    @Inject
    protected Dao dao;

    @At
    @Ok("jsp:jsp.statistic.businessContent")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index(HttpServletRequest request) {
        BackendLogUtil.getInstance().log(request, "进入首充页面");
        request.setAttribute("nowDate", sdf.format(new Date()));
    }

    /**
     * 获取续冲人数
     */
    @At
    public Object businessContentInfo(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isBlack) throws ParseException {
        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        //筛选roleId
        String blackUserStr = "";//黑名单账号
        if (isBlack) {
            List<Map<String, Object>> blackList = BlackListManager.getInstance().getBlackList(groupName);
            Set<String> blackSet = new HashSet<>();
            for (Map<String, Object> map : blackList) {
                blackSet.add(map.get("userId").toString());
            }
            blackUserStr = blackSet.toString();
            blackUserStr = blackUserStr.substring(1, blackUserStr.length() - 1);
        }
        LinkedHashMap<String, LinkedHashMap<String, Integer>> dataMap = new LinkedHashMap<>();
        for (String s : serverId) {
            List<Dblog> dblogs = QueryUtil.getInstance().checkHeFu(Integer.parseInt(s));
            for (Dblog dblog : dblogs) {
                String sqlStr1 = getRoleIdSql(channelNames, startDate, endDate, blackUserStr);
                List<Map<String, Object>> roleIdMap = QueryUtil.getInstance().query(dblog, sqlStr1);
                StringBuilder roleIds = new StringBuilder();
                for (Map<String, Object> map : roleIdMap) {
                    roleIds.append(map.get("roleId").toString()).append(",");
                }
                if (!"".equals(roleIds.toString())) {
                    roleIds = new StringBuilder(roleIds.substring(0, roleIds.length() - 1));
                    List<String> goalTables = QueryUtil.getInstance().getQueryTables(RECHARGESUCCESSLOG, TableType.Month, startDate, endDate);
                    for (String goalTable : goalTables) {
                        String sqlStr2 = getRoleCountSql(roleIds.toString(), channelNames, goalTable);
                        List<Map<String, Object>> roleCountMap = QueryUtil.getInstance().query(dblog, sqlStr2);

                        for (Map<String, Object> Map2 : roleCountMap) {
                            String counts = Map2.get("counts").toString();
                            String date = Map2.get("time").toString();
                            if (dataMap.containsKey(date)) {
                                LinkedHashMap<String, Integer> countsMap = dataMap.get(date);
                                putCounts(Integer.parseInt(counts), countsMap);
                                dataMap.put(date, countsMap);
                            } else {
                                //初始化
                                LinkedHashMap<String, Integer> countsMap = new LinkedHashMap<>();
                                countsMap.put("one", 0);
                                countsMap.put("two", 0);
                                countsMap.put("three", 0);
                                countsMap.put("four", 0);
                                countsMap.put("five", 0);
                                countsMap.put("six", 0);
                                countsMap.put("seven", 0);
                                countsMap.put("eight", 0);
                                countsMap.put("nine", 0);
                                putCounts(Integer.parseInt(counts), countsMap);
                                dataMap.put(date, countsMap);
                            }
                        }
                    }
                } else {
                    return null;
                }
            }
        }
        return dataMap;

    }

    /**
     * 获取购买项人数
     */
    @At
    public Object businessContentStatis(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isBlack) throws ParseException {
        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        //筛选roleId
        String blackUserStr = "";//黑名单账号
        if (isBlack) {
            List<Map<String, Object>> blackList = BlackListManager.getInstance().getBlackList(groupName);
            Set<String> blackSet = new HashSet<>();
            for (Map<String, Object> map : blackList) {
                blackSet.add(map.get("userId").toString());
            }
            blackUserStr = blackSet.toString();
            blackUserStr = blackUserStr.substring(1, blackUserStr.length() - 1);
        }
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Integer>>> dataMap = new LinkedHashMap<>();
        LinkedHashMap<String, LinkedHashMap<String, Integer>> dataMap1 = new LinkedHashMap<>();//存每日礼包人数
        LinkedHashMap<String, LinkedHashMap<String, Integer>> dataMap2 = new LinkedHashMap<>();//存每日累充人数
        for (String s : serverId) {
            List<Dblog> dblogs = QueryUtil.getInstance().checkHeFu(Integer.parseInt(s));
            for (Dblog dblog : dblogs) {
                String sqlStr1 = getRoleIdSql(channelNames, startDate, endDate, blackUserStr);
                List<Map<String, Object>> roleIdMap = QueryUtil.getInstance().query(dblog, sqlStr1);
                StringBuilder roleIds = new StringBuilder();
                for (Map<String, Object> map : roleIdMap) {
                    roleIds.append(map.get("roleId").toString()).append(",");
                }
                if (!"".equals(roleIds.toString())) {
                    roleIds = new StringBuilder(roleIds.substring(0, roleIds.length() - 1));
                    List<String> goalTables1 = QueryUtil.getInstance().getQueryTables(RECHARGESUCCESSLOG, TableType.Month, startDate, endDate);
                    for (String value : goalTables1) {
                        String sqlStr2 = getRoleCountSql2(roleIds.toString(), value);
                        List<Map<String, Object>> roleCountMap = QueryUtil.getInstance().query(dblog, sqlStr2);
                        for (Map<String, Object> map : roleCountMap) {
                            String counts = map.get("counts").toString();
                            String itemId = map.get("itemId").toString();
                            if (dataMap1.containsKey(itemId)) {
                                LinkedHashMap<String, Integer> countsMap = dataMap1.get(itemId);
                                putCounts2(Integer.parseInt(counts), countsMap);
                                dataMap1.put(itemId, countsMap);
                            } else {
                                //初始化
                                LinkedHashMap<String, Integer> countsMap = new LinkedHashMap<>();
                                countsMap.put("one", 0);
                                countsMap.put("two", 0);
                                countsMap.put("three", 0);
                                countsMap.put("seven", 0);
                                countsMap.put("fourteen", 0);
                                putCounts2(Integer.parseInt(counts), countsMap);
                                dataMap1.put(itemId, countsMap);
                            }
                        }
                    }
                    List<String> goalTables2 = QueryUtil.getInstance().getQueryTables(DAILYRECHARGEAWARDLOG, TableType.Month, startDate, endDate);
                    for (String value : goalTables2) {
                        String sqlStr3 = getdailyCountSql(roleIds.toString(), value);
                        List<Map<String, Object>> dailyCountMap = QueryUtil.getInstance().query(dblog, sqlStr3);
                        for (Map<String, Object> map : dailyCountMap) {
                            String counts = map.get("counts").toString();
                            String awardId = map.get("awardId").toString();
                            if (dataMap2.containsKey(awardId)) {
                                LinkedHashMap<String, Integer> countsMap = dataMap2.get(awardId);
                                putCounts3(Integer.parseInt(counts), countsMap);
                                dataMap2.put(awardId, countsMap);
                            } else {
                                //初始化
                                LinkedHashMap<String, Integer> countsMap = new LinkedHashMap<>();
                                countsMap.put("one", 0);
                                countsMap.put("two", 0);
                                countsMap.put("three", 0);
                                countsMap.put("four", 0);
                                putCounts3(Integer.parseInt(counts), countsMap);
                                dataMap2.put(awardId, countsMap);
                            }
                        }
                    }
                } else {
                    return null;
                }
            }
        }
        dataMap.put("daygift", dataMap1);
        dataMap.put("dailyrecharge", dataMap2);
        return dataMap;

    }

    /**
     * 存放数据(每日累充)
     */
    private void putCounts3(int count, LinkedHashMap<String, Integer> countsMap) {
        if (count == 1) {
            countsMap.put("one", countsMap.get("one") + 1);
        }
        if (count == 2) {
            countsMap.put("one", countsMap.get("one") + 1);
            countsMap.put("two", countsMap.get("two") + 1);
        }
        if (count == 3) {
            countsMap.put("one", countsMap.get("one") + 1);
            countsMap.put("two", countsMap.get("two") + 1);
            countsMap.put("three", countsMap.get("three") + 1);
        }
        if (count >= 4) {
            countsMap.put("one", countsMap.get("one") + 1);
            countsMap.put("two", countsMap.get("two") + 1);
            countsMap.put("three", countsMap.get("three") + 1);
            countsMap.put("four", countsMap.get("four") + 1);
        }

    }

    /**
     * 存放数据(连续购买项)
     */
    private void putCounts2(int count, LinkedHashMap<String, Integer> countsMap) {
        if (count == 1) {
            countsMap.put("one", countsMap.get("one") + 1);
        }
        if (count == 2) {
            countsMap.put("one", countsMap.get("one") + 1);
            countsMap.put("two", countsMap.get("two") + 1);
        }
        if (count == 3) {
            countsMap.put("one", countsMap.get("one") + 1);
            countsMap.put("two", countsMap.get("two") + 1);
            countsMap.put("three", countsMap.get("three") + 1);
        }
        if (count == 7) {
            countsMap.put("one", countsMap.get("one") + 1);
            countsMap.put("two", countsMap.get("two") + 1);
            countsMap.put("three", countsMap.get("three") + 1);
            countsMap.put("seven", countsMap.get("seven") + 1);
        }
        if (count >= 14) {
            countsMap.put("one", countsMap.get("one") + 1);
            countsMap.put("two", countsMap.get("two") + 1);
            countsMap.put("three", countsMap.get("three") + 1);
            countsMap.put("seven", countsMap.get("seven") + 1);
            countsMap.put("fourteen", countsMap.get("fourteen") + 1);
        }
    }

    /**
     * 存放数据(续费)
     */
    private void putCounts(Integer count, LinkedHashMap<String, Integer> countsMap) {
        if (count == 1) {
            countsMap.put("one", countsMap.get("one") + 1);
        }
        if (count == 2) {
            countsMap.put("one", countsMap.get("one") + 1);
            countsMap.put("two", countsMap.get("two") + 1);
        }
        if (count == 3) {
            countsMap.put("one", countsMap.get("one") + 1);
            countsMap.put("two", countsMap.get("two") + 1);
            countsMap.put("three", countsMap.get("three") + 1);
        }
        if (count == 4) {
            countsMap.put("one", countsMap.get("one") + 1);
            countsMap.put("two", countsMap.get("two") + 1);
            countsMap.put("three", countsMap.get("three") + 1);
            countsMap.put("four", countsMap.get("four") + 1);
        }
        if (count == 5) {
            countsMap.put("one", countsMap.get("one") + 1);
            countsMap.put("two", countsMap.get("two") + 1);
            countsMap.put("three", countsMap.get("three") + 1);
            countsMap.put("four", countsMap.get("four") + 1);
            countsMap.put("five", countsMap.get("five") + 1);
        }
        if (count == 6) {
            countsMap.put("one", countsMap.get("one") + 1);
            countsMap.put("two", countsMap.get("two") + 1);
            countsMap.put("three", countsMap.get("three") + 1);
            countsMap.put("four", countsMap.get("four") + 1);
            countsMap.put("five", countsMap.get("five") + 1);
            countsMap.put("six", countsMap.get("six") + 1);
        }
        if (count == 7) {
            countsMap.put("one", countsMap.get("one") + 1);
            countsMap.put("two", countsMap.get("two") + 1);
            countsMap.put("three", countsMap.get("three") + 1);
            countsMap.put("four", countsMap.get("four") + 1);
            countsMap.put("five", countsMap.get("five") + 1);
            countsMap.put("six", countsMap.get("six") + 1);
            countsMap.put("seven", countsMap.get("seven") + 1);
        }
        if (count == 8) {
            countsMap.put("one", countsMap.get("one") + 1);
            countsMap.put("two", countsMap.get("two") + 1);
            countsMap.put("three", countsMap.get("three") + 1);
            countsMap.put("four", countsMap.get("four") + 1);
            countsMap.put("five", countsMap.get("five") + 1);
            countsMap.put("six", countsMap.get("six") + 1);
            countsMap.put("seven", countsMap.get("seven") + 1);
            countsMap.put("eight", countsMap.get("eight") + 1);
        }
        if (count >= 9) {
            countsMap.put("one", countsMap.get("one") + 1);
            countsMap.put("two", countsMap.get("two") + 1);
            countsMap.put("three", countsMap.get("three") + 1);
            countsMap.put("four", countsMap.get("four") + 1);
            countsMap.put("five", countsMap.get("five") + 1);
            countsMap.put("six", countsMap.get("six") + 1);
            countsMap.put("seven", countsMap.get("seven") + 1);
            countsMap.put("eight", countsMap.get("eight") + 1);
            countsMap.put("nine", countsMap.get("nine") + 1);
        }
    }

    //获取续费统计人数
    private String getRoleCountSql(String roleIds, String channelNames, String table) {
        String sqlStr = "select count(roleId) counts,DATE_FORMAT(FROM_UNIXTIME(time), '%Y-%m-%d') time";
        sqlStr += " from " + table;
        sqlStr += " where type=5 and reason=1";
        sqlStr += " and roleId in (" + roleIds + ")";
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND platformName IN (" + channelNames + ")";
        }
        sqlStr += " group by roleId,DATE_FORMAT(FROM_UNIXTIME(time), '%Y-%m-%d') ORDER BY time";
        return sqlStr;
    }

    //获取连续购买项人数
    private String getRoleCountSql2(String roleIds, String table) {
        String sqlStr = "select COUNT(roleId) counts,itemId,roleId from " + table;
        sqlStr += " where itemId in ('com.yzry.1.day','com.yzry.3.day','com.yzry.8.day')";
        sqlStr += "  and roleId in (" + roleIds + ") GROUP BY roleId,itemId";
        /*System.out.println(sqlStr);*/
        return sqlStr;
    }

    //获取每日累充人数
    private String getdailyCountSql(String roleIds, String table) {
        String sqlStr = "select COUNT(roleId) counts,awardId,roleId from " + table;
        sqlStr += " where roleId in (" + roleIds + ") and awardId in (1,2,3) GROUP BY roleId,awardId";
        return sqlStr;
    }

    //获取符合的roleIds
    private String getRoleIdSql(String channelNames, String startDate, String endDate, String blackUserStr) {
        String sqlStr = "select roleId from rolestate";
        sqlStr += " where createtime  BETWEEN '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59'";
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND platformName IN (" + channelNames + ")";
        }
        if (!Strings.isBlank(blackUserStr)) {
            sqlStr += " AND userId NOT IN (" + blackUserStr + ")";
        }
        return sqlStr;
    }
}
