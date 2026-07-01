package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.filter.MenuFilter;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.utils.BackendLogUtil;
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
@At("/firstRecharge")
@Fail("http:500")
public class FirstRechargeStatisticModule {

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static String RECHARGELOG = "rechargelog";
    private static String ROLELOGINLOG = "roleloginlog";
    private DecimalFormat df = new DecimalFormat("0.00");

    @Inject
    protected Dao dao;

    @At
    @Ok("jsp:jsp.statistic.firstRecharge")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index(HttpServletRequest request) {
        BackendLogUtil.getInstance().log(request, "进入首充页面");
        request.setAttribute("newDate", sdf.format(new Date()));
    }


    @At
    public Object getFirstRechargeProject(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isBlack) throws Exception {
        List<Map<String, Object>> firstRechargeProjectMap = new ArrayList<>();
        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
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

        List<String> rechargeGoalTables;
        Map<String, List<String>> hefuTableMap;
        List<Map<String, Object>> rechargeCountsMap;//首充人数
        List<Map<String, Object>> rechargeRolesMap;//当天充值角色
        List<Map<Integer, Object>> rechargeAmountRolesMap;//当天首充6元、30元、68元、98元、128元、188元、258元、328元、648元对应的角色数
        List<Map<Integer, Object>> rechargeMoonRolesMap;//当天首充月卡和尊享卡的角色
        List<Map<String, Object>> rechargeBigRolesMap;//当天首充大号的角色，小号则为总首充人数-首充大号
        for (Date d : dateList) {// 将数据按天查询
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
            String s = sdf.format(d);
            rechargeCountsMap = new ArrayList<>();
            rechargeRolesMap = new ArrayList<>();
            rechargeAmountRolesMap = new ArrayList<>();
//            rechargeMoonRolesMap = new ArrayList<>();
            rechargeBigRolesMap = new ArrayList<>();
            for (String item : serverId) {
                rechargeGoalTables = QueryUtil.getInstance().getQueryTables(RECHARGELOG, TableType.No, s + " 00:00:00", s + " 23:59:59");
                hefuTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(item, RECHARGELOG, sdf.parse(s), sdf.parse(s + " 23:59:59")));
                for (String key : hefuTableMap.keySet()) {
                    List<String> tableList = hefuTableMap.get(key);
                    tableList.retainAll(rechargeGoalTables);//过滤重复数据表
                    for (String element : tableList) {
                        Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                        if (dblog == null) {
                            continue;
                        }
                        /*
                         * 获取当天的首充人数
                         */
                        String columnName = "";
                        int value = 0;
                        String rechargeCountSqlStr = getFirstRechargeCountsSql(element, item, channelNames, s, s, columnName, value, blackUserStr);
                        List<Map<String, Object>> rechargeCountsDataMap = (List<Map<String, Object>>) QueryUtil.getInstance().query(dblog, rechargeCountSqlStr);
                        rechargeCountsMap.addAll(rechargeCountsDataMap);

                        /*
                         * 获取当天充值的角色
                         */
                        String rechargeRoleSqlStr = getRechargeRolesSql(element, item, channelNames, s, s, blackUserStr);
                        List<Map<String, Object>> rechargeRolesDataMap = (List<Map<String, Object>>) QueryUtil.getInstance().query(dblog, rechargeRoleSqlStr);
                        rechargeRolesMap.addAll(rechargeRolesDataMap);

                        /*
                         * 首充6元、30元、68元、98元、128元、188元、258元、328元、648元对应的角色数
                         */
                        columnName = "gameMoney";
                        int[] amountValue = new int[]{60, 300, 680, 980, 1280, 1880, 2580, 3280, 6480};
                        Map<Integer, Object> amountRolesMap = new HashMap<>();
                        List<Map<Integer, Object>> amountRolesList = new ArrayList<>();
                        for (int i = 0; i < amountValue.length; i++) {
                            String rechargeAmountRoleSqlStr = getFirstRechargeCountsSql(element, item, channelNames, s, s, columnName, amountValue[i], blackUserStr);
                            List<Map<String, Object>> rechargeAmountRolesDataMap = (List<Map<String, Object>>) QueryUtil.getInstance().query(dblog, rechargeAmountRoleSqlStr);
                            amountRolesMap.put(amountValue[i], rechargeAmountRolesDataMap);
                        }
                        amountRolesList.add(amountRolesMap);
                        rechargeAmountRolesMap.addAll(amountRolesList);

                        /*
                         * 充值月卡和尊享卡的角色
                         */
//                        columnName = "isMoon";
//                        int[] moonValue = new int[]{1, 2};//1为月卡，2为尊享卡
//                        Map<Integer, Object> moonRolesMap = new HashMap<>();
//                        List<Map<Integer, Object>> moonRolesList = new ArrayList<>();
//                        for (int i = 0; i < moonValue.length; i++) {
//                            String rechargeMoonRoleSqlStr = getFirstRechargeCountsSql(element, item, channelNames, s, s, columnName, moonValue[i], blackUserStr);
//                            List<Map<String, Object>> rechargeMoonRolesDataMap = (List<Map<String, Object>>) QueryUtil.getInstance().query(dblog, rechargeMoonRoleSqlStr);
//                            moonRolesMap.put(moonValue[i], rechargeMoonRolesDataMap);
//                        }
//                        moonRolesList.add(moonRolesMap);
//                        rechargeMoonRolesMap.addAll(moonRolesList);
                        /*
                         * 获取首充人数中大号的账号，小号则为首充账号减去大号账号
                         */
                        String rechargeBigRoelSqlStr = getBigRolesSql(element, item, channelNames, s, s, blackUserStr);
                        List<Map<String, Object>> rechargeBigRolesDataMap = QueryUtil.getInstance().query(dblog, rechargeBigRoelSqlStr);
                        rechargeBigRolesMap.addAll(rechargeBigRolesDataMap);
                    }
                }
            }
            Set<String> rechargeCountSet = new HashSet<>();//当天首充角色
            Set<String> rechargeRoleSet = new HashSet<>();//当天充值角色
            Set<String> rechargeAmount6RoleSet = new HashSet<>();//当天首充6元角色
            Set<String> rechargeAmount30RoleSet = new HashSet<>();//当天首充30元角色
            Set<String> rechargeAmount68RoleSet = new HashSet<>();//当天首充68元角色
            Set<String> rechargeAmount98RoleSet = new HashSet<>();//当天首充98元角色
            Set<String> rechargeAmount128RoleSet = new HashSet<>();//当天首充128元角色
            Set<String> rechargeAmount188RoleSet = new HashSet<>();//当天首充188元角色
            Set<String> rechargeAmount258RoleSet = new HashSet<>();//当天首充258元角色
            Set<String> rechargeAmount328RoleSet = new HashSet<>();//当天首充328元角色
            Set<String> rechargeAmount648RoleSet = new HashSet<>();//当天首充648元角色
            Set<String> rechargeMoon1RoleSet = new HashSet<>();//当天首充月卡角色
            Set<String> rechargeMoon2RoleSet = new HashSet<>();//当天首充尊享卡角色
            Set<String> rechargeBigRoleSet = new HashSet<>();//当天首充大号角色，小号则为当天总首充角色-首充大号角色

            for (Map<String, Object> rechargeCount : rechargeCountsMap) {
                rechargeCountSet.add(rechargeCount.get("roleId").toString());
            }
            for (Map<String, Object> rechargeRole : rechargeRolesMap) {
                rechargeRoleSet.add(rechargeRole.get("roleId").toString());
            }
            for (Map<Integer, Object> rechargeAmount : rechargeAmountRolesMap) {
                for (int amount : rechargeAmount.keySet()) {
                    List<Map<String, Object>> map = (List<Map<String, Object>>) rechargeAmount.get(amount);
                    switch (amount) {
                        case 6:
                            for (Map<String, Object> rechargeAmounts : map) {
                                rechargeAmount6RoleSet.add(rechargeAmounts.get("roleId").toString());
                            }
                            continue;
                        case 30:
                            for (Map<String, Object> rechargeAmounts : map) {
                                rechargeAmount30RoleSet.add(rechargeAmounts.get("roleId").toString());
                            }
                            continue;
                        case 68:
                            for (Map<String, Object> rechargeAmounts : map) {
                                rechargeAmount68RoleSet.add(rechargeAmounts.get("roleId").toString());
                            }
                            continue;
                        case 98:
                            for (Map<String, Object> rechargeAmounts : map) {
                                rechargeAmount98RoleSet.add(rechargeAmounts.get("roleId").toString());
                            }
                            continue;
                        case 128:
                            for (Map<String, Object> rechargeAmounts : map) {
                                rechargeAmount128RoleSet.add(rechargeAmounts.get("roleId").toString());
                            }
                            continue;
                        case 188:
                            for (Map<String, Object> rechargeAmounts : map) {
                                rechargeAmount188RoleSet.add(rechargeAmounts.get("roleId").toString());
                            }
                            continue;
                        case 258:
                            for (Map<String, Object> rechargeAmounts : map) {
                                rechargeAmount258RoleSet.add(rechargeAmounts.get("roleId").toString());
                            }
                            continue;
                        case 328:
                            for (Map<String, Object> rechargeAmounts : map) {
                                rechargeAmount328RoleSet.add(rechargeAmounts.get("roleId").toString());
                            }
                            continue;
                        case 648:
                            for (Map<String, Object> rechargeAmounts : map) {
                                rechargeAmount648RoleSet.add(rechargeAmounts.get("roleId").toString());
                            }
                            continue;
                    }
                }
            }

//            for (Map<Integer, Object> rechargeMoon : rechargeMoonRolesMap) {
//                for (int isMoon : rechargeMoon.keySet()) {
//                    List<Map<String, Object>> map = (List<Map<String, Object>>) rechargeMoon.get(isMoon);
//                    switch (isMoon) {
//                        case 1:
//                            for (Map<String, Object> rechargeisMoons : map) {
//                                rechargeMoon1RoleSet.add(rechargeisMoons.get("roleId").toString());
//                            }
//                            continue;
//                        case 2:
//                            for (Map<String, Object> rechargeisMoons : map) {
//                                rechargeMoon2RoleSet.add(rechargeisMoons.get("roleId").toString());
//                            }
//                            continue;
//                    }
//                }
//            }

            for (Map<String, Object> rechargeBigRole : rechargeBigRolesMap) {
                rechargeBigRoleSet.add(rechargeBigRole.get("roleId").toString());
            }

            long rechargeCounts = rechargeCountSet.size();//当天首充角色数
            long rechargeRoleCouts = rechargeRoleSet.size();//当天充值角色数
            long rechargeAmount6Counts = rechargeAmount6RoleSet.size();//当天首充6元的角色数
            long rechargeAmount30Counts = rechargeAmount30RoleSet.size();//当天首充30元的角色数
            long rechargeAmount68Counts = rechargeAmount68RoleSet.size();//当天首充68元的角色数
            long rechargeAmount98Counts = rechargeAmount98RoleSet.size();//当天首充98元的角色数
            long rechargeAmount128Counts = rechargeAmount128RoleSet.size();//当天首充128元的角色数
            long rechargeAmount188Counts = rechargeAmount188RoleSet.size();//当天首充188元的角色数
            long rechargeAmount258Counts = rechargeAmount258RoleSet.size();//当天首充258元的角色数
            long rechargeAmount328Counts = rechargeAmount328RoleSet.size();//当天首充328元的角色数
            long rechargeAmount648Counts = rechargeAmount648RoleSet.size();//当天首充648元的角色数
            long rechargeMoon1Counts = rechargeMoon1RoleSet.size();//当天首充月卡的角色数
            long rechargeMoon2Counts = rechargeMoon2RoleSet.size();//当天首充尊享卡的角色数
            long rechargeBigRoleCounts = rechargeBigRoleSet.size();//当天首充角色大号
            long rechargeSmallRoleCounts = rechargeCounts - rechargeBigRoleCounts;//当天首充角色小号（=当天首充角色数-当天首充角色大号）
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("date", s);
            dataMap.put("firstRechargeRoles", rechargeCounts);
            double proportion;
            if (rechargeRoleCouts == 0) {
                proportion = 0;
            } else {
                proportion = (double) rechargeCounts / (double) rechargeRoleCouts;
            }
            dataMap.put("proportion", df.format(proportion));
            double smallRolesProportion;
            if (rechargeCounts == 0) {
                smallRolesProportion = 0;
            } else {
                smallRolesProportion = (double) rechargeSmallRoleCounts / (double) rechargeCounts;
            }
            dataMap.put("smallRolesProportion", df.format(smallRolesProportion));
            dataMap.put("firstRechargeAmount6", rechargeAmount6Counts);
            dataMap.put("firstRechargeAmount30", rechargeAmount30Counts);
            dataMap.put("firstRechargeAmount68", rechargeAmount68Counts);
            dataMap.put("firstRechargeAmount98", rechargeAmount98Counts);
            dataMap.put("firstRechargeAmount128", rechargeAmount128Counts);
            dataMap.put("firstRechargeAmount188", rechargeAmount188Counts);
            dataMap.put("firstRechargeAmount258", rechargeAmount258Counts);
            dataMap.put("firstRechargeAmount328", rechargeAmount328Counts);
            dataMap.put("firstRechargeAmount648", rechargeAmount648Counts);
            dataMap.put("firstRechargeisMoon1", rechargeMoon1Counts);
            dataMap.put("firstRechargeisMoon2", rechargeMoon2Counts);
            firstRechargeProjectMap.add(dataMap);
        }
        return firstRechargeProjectMap;
    }

    @At
    public Object getFirstRechargeRoleLevel(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isBlack) throws Exception {
        List<Map<String, Object>> firstRechargeRoleLevelList = new ArrayList<>();
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
        List<String> rechargeGoalTables;
        Map<String, List<String>> hefuTableMap;
        for (String s : serverId) {
            rechargeGoalTables = QueryUtil.getInstance().getQueryTables(RECHARGELOG, TableType.Month, startDate, endDate);
            hefuTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(s, RECHARGELOG, sdf.parse(startDate), sdf.parse(endDate)));
            for (String key : hefuTableMap.keySet()) {
                List<String> tableList = hefuTableMap.get(key);
                tableList.retainAll(rechargeGoalTables);//过滤重复数据表
                for (String value : tableList) {
                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    if (dblog == null) {
                        continue;
                    }
                    String sqlStr = getFirstRechargeRoleLevelSql(value, s, channelNames, startDate, endDate, blackUserStr);
                    List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                    firstRechargeRoleLevelList.addAll(dataMap);
                }
            }
        }
        Map<String, String> firstRechargeRoleLevelMap = new HashMap<>();
        for (Map<String, Object> map : firstRechargeRoleLevelList) {
            String level = map.get("level").toString();
            String roleId = map.get("roleId").toString();
            if (firstRechargeRoleLevelMap.containsKey(level)) {
                firstRechargeRoleLevelMap.put(level, firstRechargeRoleLevelMap.get(level) + "," + roleId);
            } else {
                firstRechargeRoleLevelMap.put(level, roleId);
            }
        }

        //初始化等级
        int count = 0;
        Map<Integer, Integer> firstRechargeLevelCountsMap = new LinkedHashMap<>();
        for (int i = 1; i <= 500; i++) {
            firstRechargeLevelCountsMap.put(i, count);
        }
        for (String key : firstRechargeRoleLevelMap.keySet()) {
            String[] keys = firstRechargeRoleLevelMap.get(key).split(",");
            firstRechargeLevelCountsMap.put(Integer.parseInt(key), keys.length);
        }
        //将等级对应的角色数为0的去掉
        for (int i = 1; i <= 500; i++) {
            if (firstRechargeLevelCountsMap.get(i) == 0) {
                firstRechargeLevelCountsMap.remove(i);
            }
        }

        return firstRechargeLevelCountsMap;
    }

    @At
    public Object getFirstRechargeOnlineTime(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isBlack) throws Exception {
        List<Map<String, Integer>> firstRechargeOnlineTimeMap = new ArrayList<>();
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
        List<String> rechargeGoalTables;
        Map<String, List<String>> rechargeHefuTableMap;
        List<String> roleLoginGoalTables;
        Map<String, List<String>> roleLoginHefuTableMap;
        List<Map<String, Object>> onlineTimeServerMap = new ArrayList<>();
        List<Map<String, Object>> onlineTimeChannelMap = new ArrayList<>();
        for (String s : serverId) {
            List<Map<String, Object>> firstRechargeRoleLevelList = new ArrayList<>();
            rechargeGoalTables = QueryUtil.getInstance().getQueryTables(RECHARGELOG, TableType.Month, startDate, endDate);
            rechargeHefuTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(s, RECHARGELOG, sdf.parse(startDate), sdf.parse(endDate)));
            for (String key : rechargeHefuTableMap.keySet()) {
                List<String> tableList = rechargeHefuTableMap.get(key);
                tableList.retainAll(rechargeGoalTables);//过滤重复数据表
                for (String value : tableList) {
                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    if (dblog == null) {
                        continue;
                    }
                    String sqlStr = getFirstRechargeRoleLevelCreateTimeSql(value, s, startDate, endDate, blackUserStr);
                    List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                    firstRechargeRoleLevelList.addAll(dataMap);
                }
            }
            roleLoginGoalTables = QueryUtil.getInstance().getQueryTables(ROLELOGINLOG, TableType.Month, startDate, endDate);
            roleLoginHefuTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(s, ROLELOGINLOG, sdf.parse(startDate), sdf.parse(endDate)));
            for (Map<String, Object> map : firstRechargeRoleLevelList) {
                String start = map.get("createTime").toString();
                String end = map.get("time").toString();
                String role = map.get("roleId").toString();
                for (String key : roleLoginHefuTableMap.keySet()) {
                    List<String> tableList = roleLoginHefuTableMap.get(key);
                    tableList.retainAll(roleLoginGoalTables);//过滤重复数据表
                    for (String value : tableList) {
                        Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                        if (dblog == null) {
                            continue;
                        }
                        String onlineTimeChannelSqlStr = getRechargeRoleOnTimeSql(value, channelNames, start, end, role);
                        List<Map<String, Object>> dataMap1 = QueryUtil.getInstance().query(dblog, onlineTimeChannelSqlStr);
                        onlineTimeChannelMap.addAll(dataMap1);

                        String onlineTimeServerSqlStr = getRechargeRoleOnTimeSql(value, "", start, end, role);
                        List<Map<String, Object>> dataMap2 = QueryUtil.getInstance().query(dblog, onlineTimeServerSqlStr);
                        onlineTimeServerMap.addAll(dataMap2);
                    }
                }
            }
        }

        int[] sectionTime = {0, 5 * 60, 30 * 60, 120 * 60, 300 * 60, 600 * 60, 900 * 60, 1500 * 60, 1800 * 60};
        Map<String, Integer> channelResMap = new LinkedHashMap<>(); //渠道结果map
        Map<String, Integer> serverResMap = new LinkedHashMap<>();//服务器结果map
        Map<Integer, Integer> channelCountMap = new TreeMap<>(); //渠道计数map
        Map<Integer, Integer> serverCountMap = new TreeMap<>();//服务器计数map
        //初始化计数map
        for (int i = 1; i < sectionTime.length; i++) {
            channelCountMap.put(sectionTime[i], 0);
            serverCountMap.put(sectionTime[i], 0);
            channelResMap.put(TimeCastInfo(sectionTime[i]), 0);
            serverResMap.put(TimeCastInfo(sectionTime[i]), 0);
        }
        if (onlineTimeChannelMap.size() != 0) {
            for (Map<String, Object> onlineMap : onlineTimeChannelMap) {
                if (!onlineMap.containsKey("onlineTime")) {
                    continue;
                }
                String onlineTime = onlineMap.get("onlineTime").toString();
                long value = Long.parseLong(onlineTime);
                for (int i = 0; i < sectionTime.length; i++) {
                    int count = 0;
                    if (i < sectionTime.length - 1) {
                        if (value > sectionTime[i] && value <= sectionTime[i + 1]) {
                            count++;
                            if (channelCountMap.containsKey(sectionTime[i])) {
                                count += channelCountMap.get(sectionTime[i]);
                            }
                            channelCountMap.put(sectionTime[i], count);
                        }
                    } else {
                        if (value > sectionTime[i]) {
                            count++;
                            if (channelCountMap.containsKey(sectionTime[i])) {
                                count += channelCountMap.get(sectionTime[i]);
                            }
                            channelCountMap.put(sectionTime[i], count);
                        }
                    }
                }
            }
            Set<Integer> keys = channelCountMap.keySet();
            for (Integer key : keys) {
                String timeInfo = TimeCastInfo(key);
                Integer roleCount = channelCountMap.get(key);
                channelResMap.put(timeInfo, roleCount);
            }
        }
        if (onlineTimeServerMap.size() != 0) {
            for (Map<String, Object> onlineMap : onlineTimeServerMap) {
                if (!onlineMap.containsKey("onlineTime")) {
                    continue;
                }
                String onlineTime = onlineMap.get("onlineTime").toString();
                long value = Long.parseLong(onlineTime);
                for (int i = 0; i < sectionTime.length; i++) {
                    int count = 0;
                    if (i < sectionTime.length - 1) {
                        if (value > sectionTime[i] && value <= sectionTime[i + 1]) {
                            count++;
                            if (serverCountMap.containsKey(sectionTime[i])) {
                                count += serverCountMap.get(sectionTime[i]);
                            }
                            serverCountMap.put(sectionTime[i], count);
                        }
                    } else {
                        if (value > sectionTime[i]) {
                            count++;
                            if (serverCountMap.containsKey(sectionTime[i])) {
                                count += serverCountMap.get(sectionTime[i]);
                            }
                            serverCountMap.put(sectionTime[i], count);
                        }
                    }
                }
            }
            Set<Integer> keys = serverCountMap.keySet();
            for (Integer key : keys) {
                String timeInfo = TimeCastInfo(key);
                Integer roleCount = serverCountMap.get(key);
                serverResMap.put(timeInfo, roleCount);
            }
        }
        firstRechargeOnlineTimeMap.add(channelResMap);
        firstRechargeOnlineTimeMap.add(serverResMap);
        return firstRechargeOnlineTimeMap;
    }

    private String TimeCastInfo(int time) {
        switch (time) {
            case 0:
                return "小于5分钟（≤5分钟）";
            case 5 * 60:
                return "5分钟~30分钟（>5分钟,≤30分钟）";
            case 30 * 60:
                return "30分钟~2小时（>30分钟,≤2小时）";
            case 120 * 60:
                return "2小时~5小时（>2小时,≤5小时）";
            case 300 * 60:
                return "5小时~10小时 （>5小时,≤10小时）";
            case 600 * 60:
                return "10小时~15小时 （>10小时,≤15小时）";
            case 900 * 60:
                return "15小时~25小时 （>15小时,≤25小时）";
            case 1500 * 60:
                return "25小时~30小时 （>25小时,≤30小时）";
            case 1800 * 60:
                return "30小时以上 （>30小时）";
            default:
                return "";
        }
    }

    /*
     * 获取当天首充人数(通过传入的columnName来筛选首充多少钱以及首充月卡或者首充尊享卡)
     */
    private String getFirstRechargeCountsSql(String table, String serverId, String channelNames, String start, String end, String columnName, int value, String blackUserStr) {
        String sqlStr = " SELECT s.roleId";
        sqlStr += " FROM (SELECT * FROM (SELECT * FROM " + table + " ORDER BY TIME ASC) t GROUP BY t.roleId) s";
        sqlStr += " WHERE s.time BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59') AND s.statusReason=7 AND s.status=1 AND sid in ('" + serverId + "')";
        if (!blackUserStr.equals("")) {
            sqlStr += " AND s.userId NOT IN (" + blackUserStr + ")";
        }
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND s.platformName IN (" + channelNames + ")";
        }
        if (!columnName.equals("")) {
            sqlStr += " AND s." + columnName + " = " + value;
        }
        return sqlStr;
    }

    /*
     * 获取当天付费的角色
     */
    private String getRechargeRolesSql(String table, String serverId, String channelNames, String start, String end, String blackUserStr) {
        String sqlStr = " SELECT roleId";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE TIME BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59')";
        sqlStr += " AND statusReason=7 AND status=1 AND sid in ('" + serverId + "')";
        if (!blackUserStr.equals("")) {
            sqlStr += " AND userId NOT IN (" + blackUserStr + ")";
        }
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND platformName IN (" + channelNames + ")";
        }
        sqlStr += " GROUP BY roleId";
        return sqlStr;
    }

    /*
     * 获取首充人数中大号的账号，小号则为首充账号减去大号账号
     */
    private String getBigRolesSql(String table, String serverId, String channelNames, String start, String end, String blackUserStr) {
        String sqlStr = " SELECT rsl.roleId";
        sqlStr += " FROM (SELECT s.roleId FROM (SELECT *FROM (SELECT * FROM " + table + " ORDER BY TIME ASC) t GROUP BY t.roleId) s";
        sqlStr += " WHERE s.time BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59') AND s.statusReason=7 AND s.status=1 AND s.sid in ('" + serverId + "')";
        if (!blackUserStr.equals("")) {
            sqlStr += " AND s.userId NOT IN (" + blackUserStr + ")";
        }
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND s.platformName IN (" + channelNames + ")";
        }
        sqlStr += " ) rsl, (SELECT r.roleId FROM (SELECT roleId,level,userId  FROM rolestate ORDER BY level DESC) r GROUP BY r.userId) rs ";
        sqlStr += " where rsl.roleId = rs.roleId ";
        return sqlStr;
    }

    /*
     * 获取首充人数中角色等级
     */
    private String getFirstRechargeRoleLevelSql(String table, String serverId, String channelNames, String start, String end, String blackUserStr) {
        String sqlStr = " SELECT s.roleId,s.level,s.time,DATE_FORMAT(FROM_UNIXTIME(s.time), '%Y-%m-%d') datetime";
        sqlStr += " FROM (SELECT * FROM (SELECT * FROM " + table + " ORDER BY TIME ASC) t GROUP BY t.roleId) s";
        sqlStr += " WHERE s.time BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59') AND s.statusReason=7 AND s.status=1 AND sid in ('" + serverId + "')";
        if (!blackUserStr.equals("")) {
            sqlStr += " AND s.userId NOT IN (" + blackUserStr + ")";
        }
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND s.platformName IN (" + channelNames + ")";
        }
        return sqlStr;
    }

    /*
     * 获取首充人数中角色的创建时间以及角色首充的时间
     */
    private String getFirstRechargeRoleLevelCreateTimeSql(String table, String serverId, String start, String end, String blackUserStr) {
        String sqlStr = " SELECT s.roleId,s.time,r.createTime";
        sqlStr += " FROM (SELECT rs.userId,rs.roleId,rs.time";
        sqlStr += " FROM (SELECT * FROM (SELECT * FROM " + table + " ORDER BY TIME ASC) t GROUP BY t.roleId) rs";
        sqlStr += " WHERE rs.time BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59') AND rs.type=5 AND rs.reason=1 AND sid in ('" + serverId + "')) s";
        sqlStr += " LEFT JOIN rolestate r ON s.roleId=r.roleId";
        if (!blackUserStr.equals("")) {
            sqlStr += " AND s.userId NOT IN (" + blackUserStr + ")";
        }
        return sqlStr;
    }

    /*
     * 获取角色注册时间截止首充时间的当前角色的在线时长
     * (这里做了特殊处理，因为在线时长是用户登出时才统计在线时长，因此会有一段角色登录了，截止首充时间未登出，这一段不能统计到在线时长，所以这里做了特殊处理，
     * 将在角色创建时间后截止首充时间前最后一次的登录时间与首充时间相减得到这一段为统计到的在线时长)
     */
    private String getRechargeRoleOnTimeSql(String table, String channelNames, String start, String end, String role) {
        String sqlStr = "";
        sqlStr += "select a.roleId,sum(a.onlineTime) onlineTime from (SELECT userId,roleId,onlineTime";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE TYPE = 0 AND time BETWEEN UNIX_TIMESTAMP('" + start + "') AND " + end;
        sqlStr += " AND roleId IN (" + role + ")";
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND platformName IN (" + channelNames + ")";
        }
        sqlStr += " UNION ALL";
        sqlStr += " SELECT userId,roleId,onlineTime";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE TYPE = 2 AND time BETWEEN UNIX_TIMESTAMP('" + start + "') AND " + end;
        sqlStr += " AND roleId IN (" + role + ")";
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND platformName IN (" + channelNames + ")";
        }
        sqlStr += " UNION ALL";
        sqlStr += " SELECT userId,roleId,(" + end + " - MAX(time)) AS onlineTime";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE TYPE = 1 AND time BETWEEN UNIX_TIMESTAMP('" + start + "') AND " + end;
        sqlStr += " AND roleId IN (" + role + ")";
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND platformName IN (" + channelNames + ")";
        }
        sqlStr += " GROUP BY userId,roleId";
        sqlStr += ") a";
        sqlStr += " group by a.userId,a.roleId";
        return sqlStr;
    }
}
