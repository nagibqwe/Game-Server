package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.filter.MenuFilter;
import com.backend.struct.log.TableName;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.QueryUtil;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@IocBean
@Ok("json")
@At("/secondRecharge")
@Fail("http:500")
public class SecondRechargeStatisticModule {
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Inject
    protected Dao dao;

    @At
    @Ok("jsp:jsp.statistic.secondRecharge")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index(HttpServletRequest request) {
        BackendLogUtil.getInstance().log(request, "进入二次付费页面");
        request.setAttribute("newDate", sdf.format(new Date()));
    }

    @At
    public Object getSecondRecharge(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isBlack) throws Exception {
        Date start = format.parse(startDate + " 00:00:00");
        Date end = format.parse(endDate + " 23:59:59");
        long startTime = start.getTime() / 1000;
        long endTime = end.getTime() / 1000;
        List<Map<String, Object>> secondRechargeMap = new ArrayList<>();
        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
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
        List<Map<String, Object>> secondRechargeMapList = new ArrayList<>();
        List<String> rechargeGoalTables;
        Map<String, List<String>> rechargeHefuTableMap;
        for (String item : serverId) {
            String RECHARGESUCCESSLOG = TableName.RechargeSuccess;
            rechargeGoalTables = QueryUtil.getInstance().getQueryTables(RECHARGESUCCESSLOG, TableType.No, startDate+" 00:00:00", endDate+" 23:59:59");
            rechargeHefuTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(item, RECHARGESUCCESSLOG, sdf.parse(startDate), sdf.parse(endDate)));
            for (String key : rechargeHefuTableMap.keySet()) {
                List<String> tableList = rechargeHefuTableMap.get(key);
                tableList.retainAll(rechargeGoalTables);//过滤重复数据表
                for (String s : tableList) {
                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    if (dblog == null) {
                        continue;
                    }
                    String sqlStr = getFirstAndSecondRechargeSql(s, item, channelNames, endDate, blackUserStr);
                    List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                    secondRechargeMapList.addAll(dataMap);
                }
            }
        }
        List<Map<String, Object>> secondRechargeUserMap = new ArrayList<>();//获取二次付费的map
        Map<String, String> rechargeMap = new TreeMap<>();
        for (Map<String, Object> map : secondRechargeMapList) {
            String userId = map.get("userId").toString();
            long time = Long.parseLong(map.get("time").toString());
            float amount = Float.parseFloat(map.get("totalFee").toString());
//            int isMoon = 0;
//            if (map.get("isMoon") != null) {
//                isMoon = Integer.parseInt(map.get("isMoon").toString());
//            }
            String itemId = map.get("itemId").toString();
            if (rechargeMap.containsKey(userId)) {
                String[] str = rechargeMap.get(userId).split(",");
                Map<String, Object> dataMap = new HashMap<>();
                long timeTemp = Long.parseLong(str[0]);
                float amountTemp = Float.parseFloat(str[1]);
//                int isMoonTemp = Integer.parseInt(str[2]);
                String itemIdTemp = str[2];
                long timeGap;
                if (timeTemp < time) {
                    if (time >= startTime && time <= endTime) {
                        timeGap = time - timeTemp;
                        dataMap.put("userId", userId);
                        dataMap.put("time", timeGap);
                        dataMap.put("totalFee", amount);
//                        dataMap.put("isMoon", isMoon);
                        dataMap.put("itemId", itemId);
                        secondRechargeUserMap.add(dataMap);
                    }
                } else {
                    if (timeTemp >= startTime && timeTemp <= endTime) {
                        timeGap = timeTemp - time;
                        dataMap.put("userId", userId);
                        dataMap.put("time", timeGap);
                        dataMap.put("totalFee", amountTemp);
//                        dataMap.put("isMoon", isMoonTemp);
                        dataMap.put("itemId", itemIdTemp);
                        secondRechargeUserMap.add(dataMap);
                    }
                }
                rechargeMap = new TreeMap<>();
            } else {
                rechargeMap.put(userId, time + "," + amount + "," + itemId);
            }
        }
        //System.out.println("secondRechargeUserMap" + secondRechargeUserMap);
        //按照间隔天数来获取数据(新增15,16,17,18,19分别对应间隔10分钟，30分钟，1小时，2-3小时，3小时)
        int[] timeGap = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 14, 30, 15, 16, 17, 18, 19};
        int[] project = new int[]{-1, 6, 30, 68, 98, 128, 198, 258, 328, 648};
        Map<Integer, Integer> timeGapMap = new TreeMap<>();
        Map<Integer, Integer> projectMap = new TreeMap<>();
        for (int value : timeGap) {//初始化
            timeGapMap.put(value, 0);
        }
        for (int value : project) {
            projectMap.put(value, 0);
        }
        for (Map<String, Object> dataMap : secondRechargeUserMap) {
            long time = Long.parseLong(dataMap.get("time").toString());
            String itemId = dataMap.get("itemId").toString();
            float amount = Float.parseFloat(dataMap.get("totalFee").toString());
            for (int value : timeGap) {
                int count = 0;
                if (value == 8) {
                    if (time >= (value) * 24 * 60 * 60 && time < (value + 6) * 24 * 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else if (value == 14) {
                    if (time >= (value) * 24 * 60 * 60 && time < (value + 16) * 24 * 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else if (value == 30) {
                    if (time >= (value) * 24 * 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else if (value == 15) {
                    if (time >= 10 * 60 && time < 30 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else if (value == 16) {
                    if (time >= 30 * 60 && time < 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else if (value == 17) {
                    if (time >= 60 * 60 && time < 2 * 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else if (value == 18) {
                    if (time >= 2 * 60 * 60 && time < 3 * 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else if (value == 19) {
                    if (time >= 3 * 60 * 60 && time < 24 * 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else {
                    if (time >= (value) * 24 * 60 * 60 && time < (value + 1) * 24 * 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        } else {
                            timeGapMap.put(value, count);
                        }
                    }
                }
            }
            for (int value : project) {
                int count = 0;
                if (value == -1) {
                    if (itemId.contains("day")) {
                        count++;
                        if (projectMap.containsKey(value)) {
                            projectMap.put(value, count + projectMap.get(value));
                        }
                        projectMap.put(value, count);
                    }
                } else {
                    if (value == amount) {
                        count++;
                        if (projectMap.containsKey(value)) {
                            projectMap.put(value, count + projectMap.get(value));
                        } else {
                            projectMap.put(value, count);
                        }
                    }
                }
            }
        }
        Map<String, Object> timeGapResMap = new TreeMap<>();
        Map<String, Object> projectResMap = new TreeMap<>();
        for (int key : timeGapMap.keySet()) {
            String timeGapName = getTimeGapName(key);
            timeGapResMap.put(timeGapName, timeGapMap.get(key));
        }
        for (int key : projectMap.keySet()) {
            String projectName = getProjectName(key);
            projectResMap.put(projectName, projectMap.get(key));
        }
        secondRechargeMap.add(timeGapResMap);
        secondRechargeMap.add(projectResMap);
        //System.out.println("secondRechargeMap====" + secondRechargeMap);
        return secondRechargeMap;
    }

    private String getTimeGapName(int time) {
        String timeGapName = "";
        switch (time) {
            case 0:
                timeGapName = "间隔0天";
                break;
            case 1:
                timeGapName = "间隔1天";
                break;
            case 2:
                timeGapName = "间隔2天";
                break;
            case 3:
                timeGapName = "间隔3天";
                break;
            case 4:
                timeGapName = "间隔4天";
                break;
            case 5:
                timeGapName = "间隔5天";
                break;
            case 6:
                timeGapName = "间隔6天";
                break;
            case 7:
                timeGapName = "间隔7天";
                break;
            case 8:
                timeGapName = "间隔8~14天";
                break;
            case 14:
                timeGapName = "间隔14~29天";
                break;
            case 15:
                timeGapName = "间隔10分钟";
                break;
            case 16:
                timeGapName = "间隔30分钟";
                break;
            case 17:
                timeGapName = "间隔1小时";
                break;
            case 18:
                timeGapName = "间隔2-3小时";
                break;
            case 19:
                timeGapName = "间隔3小时以上";
                break;
            case 30:
                timeGapName = "间隔30天以上";
                break;
        }
        return timeGapName;
    }

    private String getProjectName(int project) {
        String projectName = "";
        switch (project) {
            case -1:
                projectName = "每日礼包";
                break;
            case 6:
                projectName = "6元蓝钻";
                break;
            case 30:
                projectName = "30元蓝钻";
                break;
            case 68:
                projectName = "68元蓝钻";
                break;
            case 98:
                projectName = "98元蓝钻";
                break;
            case 128:
                projectName = "128元蓝钻";
                break;
            case 198:
                projectName = "198元蓝钻";
                break;
            case 258:
                projectName = "258元蓝钻";
                break;
            case 328:
                projectName = "328元蓝钻";
                break;
            case 648:
                projectName = "648元蓝钻";
                break;
        }
        return projectName;
    }

    //获取首充的userId
    private String getFirstRechargeUserSql(String table, String serverId, String channelNames, String endDate, String blackUserStr) {
        String sqlStr = "SELECT s.userId";
        sqlStr += " FROM (SELECT userId,min(t.time) time,sid FROM (SELECT * FROM " + table + " WHERE status=1 AND statusReason=7 ORDER BY TIME ASC) t";
        sqlStr += " GROUP BY t.userId) s";
        sqlStr += " WHERE s.time  <= UNIX_TIMESTAMP('" + endDate + " 23:59:59') AND s.sid IN ('" + serverId + "')";
        if (!Strings.isBlank(blackUserStr)) {
            sqlStr += " AND s.userId NOT IN (" + blackUserStr + ")";
        }
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND s.platformName IN (" + channelNames + ")";
        }
        return sqlStr;
    }

    //获取首充和二次充值的列表
    private String getFirstAndSecondRechargeSql(String table, String serverId, String channelNames, String endDate, String blackUserStr) {
        String sqlStr = "SELECT r1.userId, r1.time,r1.totalFee,r1.itemId";
        sqlStr += " FROM " + table + " r1";
        sqlStr += " WHERE r1.status=1 AND r1.statusReason AND 2 > ( ";
        sqlStr += " SELECT COUNT(*) FROM " + table + " r2 WHERE r2.userId=r1.userId AND r2.time < r1.time)";
        sqlStr += " AND r1.userId IN (" + getFirstRechargeUserSql(table, serverId, channelNames, endDate, blackUserStr) + ")";
        sqlStr += " AND r1.sid IN ('" + serverId + "')";
        sqlStr += " ORDER BY r1.userId,r1.time ASC;";
        return sqlStr;
    }

}
