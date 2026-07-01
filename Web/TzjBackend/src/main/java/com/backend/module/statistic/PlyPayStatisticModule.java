package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.struct.log.TableName;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.QueryUtil;
import com.backend.utils.DateUtil;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@IocBean
@Ok("json")
@At("/paystatistic")
@Fail("http:500")
public class PlyPayStatisticModule {

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat sdfhm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @At("/")
    @Ok("jsp:jsp.statistic.playerPay")
    public void index(HttpServletRequest request) {
        BackendLogUtil.getInstance().log(request, "玩家充值统计");
        request.setAttribute("nowDate", sdf.format(new Date()));
    }


    /**
     * 新增付费人数：统计选定日期内新增的角色为新增人数，选定日期内新增角色付过费的为新增付费人数，选定日期内新增角色的首次付费的付费金额，选定日期内新增角色的首日付费的付费金额
     */
    @At
    public Object addedStatistic(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isblack) throws Exception {

        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }

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

        NutMap re = new NutMap();
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        List<Date> dateList = new ArrayList<>();
        int dvalue = DateUtil.dValue(start, end);
        long oneday = 1000 * 60 * 60 * 24;
        long dateTime = start.getTime();
        Date date;
        for (int i = 0; i < dvalue + 1; i++) {
            date = new Date(dateTime);
            dateList.add(date);
            dateTime += oneday;
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (Date d : dateList) {// 将数据按天查询
            String s = sdf.format(d);
            Set<Long> allReRoleDataSet = new HashSet<>();//按天分布新增角色
            Set<Long> allRechargeRoleDataSet = new HashSet<>();//按天分布新增付费角色
            List<Map<String, Object>> firstRechargeAmountDataList = new ArrayList<>();//按天分布首次付费
            List<Map<String, Object>> firstRechargeDayAmountDataList = new ArrayList<>();//按天分布首日付费

            for (String id : serverId) {

                //得到的注册角色
                int finalServerId = QueryUtil.getInstance().getHeFuId(Integer.parseInt(id));
                Dblog dblog = DbLogListManager.getInstance().getDBServer(groupName, finalServerId);
                String roleCreateSql = getRegisterRoleSql(id, TableName.RoleState, channelNames, blackUserStr, s, s);
                List<Map<String, Object>> roleDataMap = QueryUtil.getInstance().query(dblog, roleCreateSql);
                Set<Long> roleIdSet = new HashSet<>();//注册角色id
                if (!roleDataMap.isEmpty()) {
                    for (Map<String, Object> map : roleDataMap) {
                        roleIdSet.add(Long.parseLong(map.get("roleId").toString()));
                    }
                }
                allReRoleDataSet.addAll(roleIdSet);
                String roleIds = roleIdSet.toString().replace("[", "").replace("]", "");

                if (!roleIds.equals("")) {
                    //获取新增付费人数
                    Map<String, List<String>> hefuTableMap = QueryUtil.getInstance().getHefuTable(id, TableName.RechargeSuccess, sdf.parse(s + " 00:00:00"), sdf.parse(s + " 23:59:59"));
                    List<String> rechargeGoalTables = QueryUtil.getInstance().getQueryTables(TableName.RechargeSuccess, TableType.No, s + " 00:00:00", s + " 23:59:59");

                    for (String key : hefuTableMap.keySet()) {
                        List<String> tableList = hefuTableMap.get(key);
                        tableList.retainAll(rechargeGoalTables);//过滤重复数据表
                        Dblog rechargeDblog = DbLogListManager.getInstance().getDblog(key);
                        if (rechargeDblog == null) {
                            continue;
                        }
                        for (String value : tableList) {
                            String rechargeRolesqlStr = getNewRechargeRoleSql(id, value, roleIds);
                            List<Map<String, Object>> rechargeRoleData = QueryUtil.getInstance().query(rechargeDblog, rechargeRolesqlStr);
                            if (!rechargeRoleData.isEmpty()) {
                                for (Map<String, Object> map : rechargeRoleData) {
                                    allRechargeRoleDataSet.add(Long.parseLong(map.get("roleId").toString()));
                                }
                            }

                            String firstRechargeAmountSqlStr = getNewFirstRechargeAmount(id, value, roleIds);
                            List<Map<String, Object>> firstRechargeAmountData = QueryUtil.getInstance().query(rechargeDblog, firstRechargeAmountSqlStr);
                            if (!firstRechargeAmountData.isEmpty()) {
                                getResult(firstRechargeAmountDataList, firstRechargeAmountData);
                            }

                            String firstRechargeDayAmountSqlStr = getNewFirstRechargeDayAmount(id, value, roleIds);
                            List<Map<String, Object>> firstRechargeDayAmountData = QueryUtil.getInstance().query(rechargeDblog, firstRechargeDayAmountSqlStr);
                            if (!firstRechargeAmountData.isEmpty()) {
                                getResult(firstRechargeDayAmountDataList, firstRechargeDayAmountData);
                            }
                        }

                    }
                }
            }
            int allCreateRoleCounts = allReRoleDataSet.size();//总共注册角色人数
            int allRechargeRoleCounts = allRechargeRoleDataSet.size();//总共付费角色
            float allfirstRechargeCounts = 0f;
            for (Map<String, Object> map : firstRechargeAmountDataList) {
                allfirstRechargeCounts += Float.parseFloat(map.get("totalFee").toString());
            }
            float allfirstDayRechargeCounts = 0f;
            for (Map<String, Object> map : firstRechargeDayAmountDataList) {
                allfirstDayRechargeCounts += Float.parseFloat(map.get("totalFee").toString());
            }
            //System.out.println("date==="+s);
            //System.out.println("allCreateRoleCounts==="+allCreateRoleCounts);
            //System.out.println("allRechargeRoleCounts==="+allRechargeRoleCounts);
            //System.out.println("allfirstRechargeCounts==="+allfirstRechargeCounts);
            //System.out.println("allfirstDayRechargeCounts==="+allfirstDayRechargeCounts);
            Map<String, Object> map = new HashMap<>();
            map.put("date", s);
            map.put("allCreateRoleCounts", allCreateRoleCounts);
            map.put("allRechargeRoleCounts", allRechargeRoleCounts);
            map.put("allfirstRechargeCounts", allfirstRechargeCounts);
            map.put("allfirstDayRechargeCounts", allfirstDayRechargeCounts);
            //System.out.println("map===="+map.toString());
            dataList.add(map);
        }
        return re.setv("ok", true).setv("dataList", dataList);
    }

    /**
     * 付费等级（选定时间内首次付过费的角色，付费时在0~10级有多少人，这些人总共充值了多少钱）
     */
    @At
    public Object rankStatistic(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isblack) throws Exception {

        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
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

        NutMap re = new NutMap();
        //得到等级分布
        List<String> lvlist = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            lvlist.add(10 * (i - 1) + "—" + 10 * i);
        }
        List<Map<String, Object>> firstPayMapList = new ArrayList<>();
        for (String id : serverId) {
            List<String> rechargeGoalTables = QueryUtil.getInstance().getQueryTables(TableName.RechargeSuccess, TableType.No, startDate+ " 00:00:00", endDate+ " 23:59:59");
            Map<String, List<String>> hefuTableMap = QueryUtil.getInstance().getHefuTable(id, TableName.RechargeSuccess, sdf.parse(startDate), sdf.parse(endDate));
            for (String key : hefuTableMap.keySet()) {
                Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                List<String> tableList = hefuTableMap.get(key);
                tableList.retainAll(rechargeGoalTables);//过滤重复数据表
                //计算出每日充值玩家
                for (String s : tableList) {
                    //统计玩家充值时所出的等级，等级对应的充值总金额分布，次数分布
                    String getPlayerRankSql = getFirstRechargeRoleInfoSql(id, s, channelNames, blackUserStr, startDate, endDate);
                    List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, getPlayerRankSql);
                    getResult(firstPayMapList, dataMap);
                }
            }
        }


        //对数据进行封装，以便在前台进行展示
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (String levels : lvlist) {
            String[] levelStr = levels.split("—");
            int roleCount = 0;
            float amountSum = 0;
            int fisrtPayCount = 0;
            int downLv = Integer.parseInt(levelStr[0]);
            int upLv = Integer.parseInt(levelStr[1]);
            for (Map<String, Object> map : firstPayMapList) {
                int level = Integer.parseInt(map.get("level").toString());
                double amount = Float.parseFloat(map.get("totalFee").toString());
                int counts = Integer.parseInt(map.get("count").toString());
                if (level >= downLv && level < upLv) {
                    roleCount++;
                    amountSum += amount;
                    fisrtPayCount += counts;
                }
            }
            Map<String, Object> datamap = new HashMap<>();

            datamap.put("levels", levels);
            datamap.put("roleCount", roleCount);
            datamap.put("amountSum", amountSum);
            datamap.put("fisrtPayCount", fisrtPayCount);

            dataList.add(datamap);
        }

        return re.setv("ok", true).setv("mapData", dataList);
    }

    /**
     * 统计付费间隔(选定时间内，首充为X人，间隔一定时间后，二次充值为Y人，三次充值为Z人)
     */
    @At
    public Object intervalStatistic(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isblack) throws Exception {

        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
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
        NutMap re = new NutMap();

        List<Map<String, Object>> dataResultMapList = new ArrayList<>();
        for (String id : serverId) {
            List<String> rechargeGoalTables = QueryUtil.getInstance().getQueryTables(TableName.RechargeSuccess, TableType.No, startDate+" 00:00:00", endDate+" 23:59:59");
            Map<String, List<String>> hefuTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(id, TableName.RechargeSuccess, sdf.parse(startDate), sdf.parse(endDate)));
            for (String key : hefuTableMap.keySet()) {
                Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                List<String> tableList = hefuTableMap.get(key);
                tableList.retainAll(rechargeGoalTables);//过滤重复数据表
                for (String s : tableList) {
                    String strSql = getRechargeSpace(id, s, channelNames, blackUserStr, startDate, endDate);
                    List<Map<String, Object>> data = QueryUtil.getInstance().query(dblog, strSql);
                    dataResultMapList.addAll(data);
                }
            }
        }

        //首先获取首充总共有多少roleId(不重复的)
        Set<String> firstRechargeRole = new HashSet<>();
        List<Long> firstTimes = new ArrayList<>();//首次充值距离注册时间有多长
        List<Long> secondTimes = new ArrayList<>();//二次充值距离注册时间有多长
        List<Long> thirdTimes = new ArrayList<>();//三次充值距离注册时间有多长
        long startTime = sdfhm.parse(startDate + " 00:00:00").getTime() / 1000;//开始时间
        long endTime = sdfhm.parse(endDate + " 23:59:59").getTime() / 1000;//结束时间
        //System.out.println(startTime+"==="+endTime);
        if (!dataResultMapList.isEmpty()) {
            for (Map<String, Object> map : dataResultMapList) {
                firstRechargeRole.add(map.get("roleId").toString());//获取首次充值时间为选择时间的角色
            }
            for (String role : firstRechargeRole) {
                List<Map<String, Object>> dataMapList = new ArrayList<>();
                for (Map<String, Object> map : dataResultMapList) {
                    if (map.get("roleId").toString().equals(role)) {
                        dataMapList.add(map);
                    }
                }
                dataResultMapList.removeAll(dataMapList);
                sortMapList(dataMapList);//对dataMapList进行排序
                Map<String, Object> firstMap = new HashMap<>();
                Map<String, Object> secondMap = new HashMap<>();
                Map<String, Object> thirdMap = new HashMap<>();
                if (dataMapList.size() < 4) {
                    firstMap = dataMapList.get(0);

                    if (dataMapList.size() > 1) {
                        secondMap = dataMapList.get(1);
                    }
                    if (dataMapList.size() > 2) {
                        thirdMap = dataMapList.get(2);
                    }
                }
                if (!firstMap.isEmpty()) {
                    long time = Long.parseLong(firstMap.get("time").toString());
                    if (time >= startTime && time <= endTime) {//只统计首次充值时间为选择时间的数据，这是重点！！！
                        firstTimes.add(getTimeValue(time, Long.parseLong(firstMap.get("createTime").toString().substring(0, firstMap.get("createTime").toString().indexOf(".")))));
                        if (!secondMap.isEmpty()) {
                            secondTimes.add(getTimeValue(time, Long.parseLong(secondMap.get("time").toString())));
                            if (!thirdMap.isEmpty()) {
                                thirdTimes.add(getTimeValue(Long.parseLong(secondMap.get("time").toString()), Long.parseLong(thirdMap.get("time").toString())));
                            }
                        }
                    }
                }
            }
        }

        List<String> timeDisStrList = new ArrayList<>();
        List<Long> timeDisLongList = new ArrayList<>();
        timeDisLongList.add((long) 0);

        long[] timesMi = {0, 10, 20, 30, 40, 50, 60, 2 * 60, 4 * 60, 6 * 60, 12 * 60, 24 * 60, 48 * 60}; //分钟
        for (long l : timesMi) {
            timeDisStrList.add(TimeCastInfo(l));
            timeDisLongList.add(l * 60);
        }
        List<Integer> firstCount = new ArrayList<>();
        List<Integer> secondCount = new ArrayList<>();
        List<Integer> thirdCount = new ArrayList<>();
        for (int i = 0; i < timeDisLongList.size() - 1; i++) {
            int firstcount = 0;
            int secondcount = 0;
            int thirdcount = 0;
            //第一次充值
            for (long ftime : firstTimes) {
                if (timeDisLongList.get(i) < ftime && ftime <= timeDisLongList.get(i + 1)) {
                    firstcount++;
                }
            }
            //第二次充值
            for (long stime : secondTimes) {
                if (timeDisLongList.get(i) < stime && stime <= timeDisLongList.get(i + 1)) {
                    secondcount++;
                }
            }
            //第三次充值
            for (long ttime : thirdTimes) {
                if (timeDisLongList.get(i) < ttime && ttime <= timeDisLongList.get(i + 1)) {
                    thirdcount++;
                }
            }
            firstCount.add(firstcount);
            secondCount.add(secondcount);
            thirdCount.add(thirdcount);
        }
        //48小时以上的单独处理
        int firstcount = 0;
        int secondcount = 0;
        int thirdcount = 0;
        //第一次充值
        for (long ftime : firstTimes) {
            if (timeDisLongList.get(timeDisLongList.size() - 1) <= ftime) {
                firstcount++;
            }
        }
        //第二次充值
        for (long stime : secondTimes) {
            if (timeDisLongList.get(timeDisLongList.size() - 1) <= stime) {
                secondcount++;
            }
        }
        //第三次充值
        for (long ttime : thirdTimes) {
            if (timeDisLongList.get(timeDisLongList.size() - 1) <= ttime) {
                thirdcount++;
            }
        }
        firstCount.add(firstcount);
        secondCount.add(secondcount);
        thirdCount.add(thirdcount);
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("timeDisStrList", timeDisStrList);
        mapData.put("firstCount", firstCount);
        mapData.put("secondCount", secondCount);
        mapData.put("thirdCount", thirdCount);
        return re.setv("ok", true).setv("mapData", mapData);
    }

    //计算时间差值
    private long getTimeValue(long time1, long time2) {
        return time1 > time2 ? (time1 - time2) : (time2 - time1);
    }

    //按照时间进行对map进行排序
    private void sortMapList(List<Map<String, Object>> dataMapList) {
        dataMapList.sort((map1, map2) -> {
            long time1 = Long.parseLong(map1.get("time").toString());//time1是从list里面拿出来的一个
            long time2 = Long.parseLong(map2.get("time").toString()); //time2是从list里面拿出来的第二个time
            return compareTo(time1, time2);
        });
    }

    private int compareTo(long a, long b) {
        return Long.compare(a, b);
    }

    private void getResult(List<Map<String, Object>> reDataMapList, List<Map<String, Object>> dataMapList) {
        //由于存在合服以及多服的选择，所以这里处理时，把多个服务器的按天产出累加以及消耗累加
        for (Map<String, Object> map : dataMapList) {
            boolean isHaveDay = false;//判断是否这一天已经存在在itemdataMapList里面了
            if (!reDataMapList.isEmpty()) {
                for (Map<String, Object> reDataMap : reDataMapList) {
                    if (map.get("roleId").toString().equals(reDataMap.get("roleId").toString())) {
                        isHaveDay = true;
                        if (map.containsKey("totalFee") && map.containsKey("time")) {
                            if (Long.parseLong(map.get("time").toString()) < Long.parseLong(reDataMap.get("time").toString())) {
                                reDataMap.put("totalFee", map.get("totalFee").toString());
                                reDataMap.put("time", map.get("time").toString());
                                if (map.containsKey("level")) {
                                    reDataMap.put("level", map.get("level").toString());
                                }
                                if (map.containsKey("count")) {
                                    reDataMap.put("count", Integer.parseInt(map.get("count").toString()) + Integer.parseInt(reDataMap.get("count").toString()));
                                }
                            }
                        }
                    }
                }
            }
            if (!isHaveDay) {
                reDataMapList.add(map);
            }
        }
    }

    /**
     * 得到新增（注册）角色
     */
    private String getRegisterRoleSql(String serverId, String table, String channelNames, String blackUsers, String startDate, String endDate) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT roleId ");
        str.append(" FROM " + table);
        str.append(" WHERE createTime BETWEEN '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59' AND createsid in (" + serverId + ") ");
        if (!Strings.isBlank(channelNames)) {
            str.append(" AND platformName IN (" + channelNames + ") ");
        }
        if (!Strings.isBlank(blackUsers)) {
            str.append(" AND userId NOT IN (" + blackUsers + ")");
        }
        return str.toString();
    }

    /**
     * 获取新增付费角色（新增角色里面付过费的角色）
     */
    private String getNewRechargeRoleSql(String serverId, String table, String roleIds) {
        StringBuilder str = new StringBuilder();
        str.append(" SELECT roleId ");
        str.append(" FROM " + table);
        str.append(" WHERE sid IN ('" + serverId + "') AND status=1 AND statusReason=7");
        str.append(" AND roleId IN (" + roleIds + ")");
        str.append(" GROUP BY roleId");
        return str.toString();
    }

    /**
     * 获取新增角色中首充角色的充值金额
     */
    private String getNewFirstRechargeAmount(String serverId, String table, String roleIds) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT s.roleId,s.totalFee,s.time FROM (SELECT * FROM (SELECT *");
        str.append(" FROM " + table + " WHERE status=1 AND statusReason=7 AND sid IN ('"+serverId+"')");
        str.append(" ORDER BY TIME ASC) t GROUP BY t.roleId) s");
        str.append(" WHERE s.roleId IN ('" + roleIds + "')");
        return str.toString();
    }

    /**
     * 获取新增角色中首日充值总额
     */
    private String getNewFirstRechargeDayAmount(String serverId, String table, String roleIds) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT s.roleId,s.totalFee,s.time,s.dateTime FROM (SELECT t.roleId,t.dateTime, SUM(t.totalFee) totalFee,t.time FROM (SELECT *, DATE_FORMAT(FROM_UNIXTIME(TIME), '%Y-%m-%d') dateTime");
        str.append(" FROM " + table + " WHERE status=1 AND statusReason=7 AND sid IN ('"+serverId+"')");
        str.append(" ORDER BY TIME ASC) t");
        str.append(" WHERE t.roleId IN ('" + roleIds + "')");
        str.append(" GROUP BY t.roleId,t.dateTime ORDER BY t.dateTime ASC) s GROUP BY s.roleId");
        return str.toString();
    }

    /**
     * 付费等级分布
     */
    private String getFirstRechargeRoleInfoSql(String serverId, String table, String channelNames, String blackUsers, String startDate, String endDate) {
        StringBuilder str = new StringBuilder();
        str.append(" SELECT s.roleId,s.totalFee,s.count,s.level,s.time FROM ( SELECT *,count(*) count FROM ( SELECT *");
        str.append(" FROM " + table +" WHERE status=1 AND statusReason=7 AND sid IN ('"+ serverId + "')");
        str.append(" ORDER BY TIME ASC) t GROUP BY t.roleId) s");
        str.append(" WHERE s.time BETWEEN UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 23:59:59')");
        if (!Strings.isBlank(channelNames)) {
            str.append(" AND s.platformName IN (" + channelNames + ") ");
        }
        if (!Strings.isBlank(blackUsers)) {
            str.append(" AND s.userId NOT IN (" + blackUsers + ")");
        }
        return str.toString();
    }

    /**
     * 付费间隔
     */
    private String getRechargeSpace(String serverId, String table, String channelNames, String blackUsers, String startDate, String endDate) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT t3.roleId,t3.time,unix_timestamp(r.createTime) createTime FROM ( SELECT *");
        str.append(" FROM " + table + " t1");
        str.append(" WHERE t1.status=1 AND t1.statusReason=7 AND (SELECT COUNT(*)");
        str.append(" FROM " + table + " t2");
        str.append(" WHERE t2.roleId=t1.roleId AND t2.time < t1.time) < 3) t3 left join rolestate r on r.roleId=t3.roleId");
        str.append(" WHERE t3.sid IN ('" + serverId + "')");
        if (!Strings.isBlank(channelNames)) {
            str.append(" AND t3.platformName IN (" + channelNames + ") ");
        }
        if (!Strings.isBlank(blackUsers)) {
            str.append(" AND t3.userId NOT IN (" + blackUsers + ")");
        }
        return str.toString();
    }

    private String TimeCastInfo(long d) {
        String str = "";
        if (d == 0) {
            str = "0~10分钟";
        } else if (d == 10) {
            str = "10~20分钟";
        } else if (d == 20) {
            str = "20~30分钟";
        } else if (d == 30) {
            str = "30~40分钟";
        } else if (d == 40) {
            str = "40~50分钟";
        } else if (d == 50) {
            str = "50~60分钟";
        } else if (d == 60) {
            str = "1~2小时";
        } else if (d == 2 * 60) {
            str = "2~4小时";
        } else if (d == 4 * 60) {
            str = "4~6小时";
        } else if (d == 6 * 60) {
            str = "6~12小时";
        } else if (d == 12 * 60) {
            str = "12~24小时";
        } else if (d == 24 * 60) {
            str = "24~48小时";
        } else if (d == 48 * 60) {
            str = "48小时以上";
        }
        return str;
    }

}






