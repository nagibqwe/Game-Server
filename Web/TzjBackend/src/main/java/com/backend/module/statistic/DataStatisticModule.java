package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.filter.MenuFilter;
import com.backend.struct.log.TableName;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.manager.ItemManager;
import com.backend.utils.*;
import org.apache.log4j.Logger;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * 服务器数据统计
 */
@IocBean
@Ok("json")
@At("/statistic")
@Fail("http:500")
public class DataStatisticModule {
    private static final Logger log = Logger.getLogger(DataStatisticModule.class);
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Inject
    protected Dao dao;

    @At
    @Ok("forward:${obj}")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public String getPage(int statType, HttpServletRequest request) {
        request.setAttribute("nowDate", sdf.format(new Date()));
        switch (statType) {
            case 1:
                BackendLogUtil.getInstance().log(request, "进入服务器日常数据页面");
                return "/WEB-INF/jsp/statistic/dailydata.jsp";
            case 2:
                BackendLogUtil.getInstance().log(request, "进入商城元宝道具销售统计页面");
                return "/WEB-INF/jsp/statistic/shopitem.jsp";
            case 3:
                BackendLogUtil.getInstance().log(request, "进入货币产出统计页面");
                return "/WEB-INF/jsp/statistic/money.jsp";
            case 4:
                BackendLogUtil.getInstance().log(request, "进入坐骑幻化统计页面");
                return "/WEB-INF/jsp/statistic/horseillusion.jsp";
            case 5:
                BackendLogUtil.getInstance().log(request, "进入骑兵升级统计页面");
                return "/WEB-INF/jsp/statistic/dragoon.jsp";
            case 6:
                BackendLogUtil.getInstance().log(request, "进入披风升级统计页面");
                return "/WEB-INF/jsp/statistic/cloak.jsp";
            case 7:
                BackendLogUtil.getInstance().log(request, "进入宠物升级统计页面");
                return "/WEB-INF/jsp/statistic/pet.jsp";
            case 8:
                BackendLogUtil.getInstance().log(request, "进入用户流失统计页面");
                return "/WEB-INF/jsp/statistic/playerLeave.jsp";
            default:
                return "/404.jsp";
        }
    }

    @At
    public Object dailyData(String groupName, String channelNames, String[] serverId, String startDate,
                            String endDate, boolean isBlack) throws ParseException {

        Map<String, Map<String, String>> resultMap = new HashMap<>();
        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        String mapKey = "day";
        String currencyTypeField = "currency";

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            start.setTime(sdf.parse(startDate));
            end.setTime(sdf.parse(endDate));
            end.add(Calendar.DAY_OF_MONTH, 1);
            start.setTimeInMillis(start.getTimeInMillis());
            end.setTimeInMillis(end.getTimeInMillis() - 1000L);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        //黑名单
        String blackUsers = "";
        if (isBlack) {
            List<Object> blackList = BlackListManager.getInstance().getBlackListUsers(groupName.trim());
            if (blackList.isEmpty()) {
                isBlack = false;
            } else {
                blackUsers = ListToStringUtil.listToString(blackList);
            }
        }
        List<String> exFields = new ArrayList<>();
        exFields.add(mapKey);

        List<String> rmFieldList = new ArrayList<>();
        rmFieldList.add(mapKey);
        rmFieldList.add(currencyTypeField);

        List<String> currencyList = new ArrayList<>();
        currencyList.add("totalmoney");

        List<Map<String, String>> rechargeMap = new ArrayList<>();
        List<Map<String, String>> activeUserList = new ArrayList<>();//活跃玩家
        List<Map<String, String>> activeDeviceList = new ArrayList<>();//活跃设备
        List<Map<String, String>> newUserList = new ArrayList<>();//新增玩家
        List<Map<String, String>> newDeviceList = new ArrayList<>();//新增设备

        //新增付费玩家人数   新增付费率=新增付费玩家人数/新增玩家人数
        List<Map<String, String>> newRechargeUserList = new ArrayList<>();//新增付费玩家人数
        Map<String,String> createUsers = new HashMap<>();
        for (String s : serverId) {

            int sid = Integer.parseInt(s);
            int rechargeServerId = sid % 2048;//充值日志的sid算法
            // 充值总额、充值人数 gamelog
            Map<String, Object> paraMap = new LinkedHashMap<>();
            paraMap.put("serverId", serverId);
//            paraMap.put("rechargeServerId", rechargeServerId);
            paraMap.put("stime", start.getTimeInMillis() / 1000);
            paraMap.put("etime", end.getTimeInMillis() / 1000);
            String table = TableName.RechargeSuccess;
            int tableType = TableType.No;
            String sqlStr;
            Calendar tempstart = Calendar.getInstance();
            tempstart.setTimeInMillis(start.getTimeInMillis());
            Calendar tempend = Calendar.getInstance();
            tempend.setTimeInMillis(end.getTimeInMillis());

            List<Dblog> dbList = QueryUtil.getInstance().checkHeFu(sid, start.getTime(), end.getTime());// 检查合服,获取合服的数据库
            List<String> queryTables = QueryUtil.getInstance().getQueryTables(table, tableType, tempstart, tempend);

            for (Dblog db : dbList) {
                List<String> realTables = QueryUtil.getInstance().queryTables(db, table);// 得到日志库中存在的表名
                realTables.retainAll(queryTables);// 求交集，得到要统计的表
                if (realTables.isEmpty()) {
                    continue;
                }

                for (String realTable : realTables) {
                    sqlStr = "SELECT userId,sum(totalFee) as totalmoney,sum(gameMoney) as totalgold,DATE_FORMAT(from_unixtime(time), '%Y-%m-%d') as day,count(distinct(orderNo)) as totaltimes from $table";
                    sqlStr += " where sid="+sid+" and status=1 and statusReason=7 and time>=@stime and time<=@etime";
                    if (!Strings.isBlank(channelNames)) {
                        sqlStr += " and platformName in (" + channelNames + ")";
                    }
                    if (isBlack) {
                        sqlStr += " and userId not in(" + blackUsers + ")";
                    }
                    sqlStr += " group by userId,day";
                    List<Map<String, String>> singleMap = QueryUtil.getInstance().query(db, sqlStr, realTable, paraMap);
                    rechargeMap.addAll(singleMap);
                }
            }

            // 按天活跃玩家(单服)
            tableType = TableType.Month;
            paraMap.put("stime", start.getTimeInMillis() / 1000);
            paraMap.put("etime", end.getTimeInMillis() / 1000);
            table = TableName.RoleLogin;
            //活跃玩家sql
            sqlStr = "select userId,DATE_FORMAT(from_unixtime(time), '%Y-%m-%d') as day from $table where sid in(@serverId) and time>=@stime and time<=@etime";
            tempstart.setTimeInMillis(start.getTimeInMillis());
            tempend.setTimeInMillis(end.getTimeInMillis());
            if (isBlack) {
                sqlStr += " and userId not in(" + blackUsers + ")";
            }
            if (!Strings.isBlank(channelNames)) {
                sqlStr += " and platformName in (" + channelNames + ")";
            }
            sqlStr += " group by userId,DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')";

            //活跃设备SQL
            String sqlDeviceStr = "select machineCode,DATE_FORMAT(from_unixtime(time), '%Y-%m-%d') as day from $table where sid in(@serverId) and time>=@stime and time<=@etime";
            if (isBlack) {
                sqlDeviceStr += " and userId not in(" + blackUsers + ")";
            }
            if (!Strings.isBlank(channelNames)) {
                sqlDeviceStr += " and platformName in (" + channelNames + ")";
            }
            sqlDeviceStr += " group by machineCode,DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')";

            List<String> roleLoginQueryTables = QueryUtil.getInstance().getQueryTables(table, tableType, tempstart, tempend);

            for (Dblog db : dbList) {
                List<String> realTables = QueryUtil.getInstance().queryTables(db, table);// 得到日志库中存在的表名
                realTables.retainAll(roleLoginQueryTables);// 求交集，得到要统计的表
                if (realTables.isEmpty()) {
                    continue;
                }
                for (String realTable : realTables) {
                    List<Map<String, String>> single = QueryUtil.getInstance().query(db, sqlStr, realTable, paraMap);// 查看活跃玩家数据单服的记录
                    activeUserList.addAll(single);

                    List<Map<String, String>> deviceDataMap = QueryUtil.getInstance().query(db, sqlDeviceStr, realTable, paraMap);// 查看活跃设备数据单服的记录
                    activeDeviceList.addAll(deviceDataMap);
                }
            }




            // 新增玩家
            paraMap.put("startDate", startDate + " 00:00:00");
            paraMap.put("endDate", endDate + " 23:59:59");
            int finalServerId = QueryUtil.getInstance().getHeFuId(sid);
            Dblog dblog = DbLogListManager.getInstance().getDBServer(groupName, finalServerId);


//            table = TableName.RoleCreate;//roleCreatelog2020不用判断合服
//            queryTables = QueryUtil.getInstance().getQueryTables(table, TableType.Year, startDate + " 00:00:00", endDate + " 23:59:59");
//            for (Dblog db : dbList) {
//                List<String> realTables = QueryUtil.getInstance().queryTables(db, table);// 得到日志库中存在的表名
//                realTables.retainAll(queryTables);// 求交集，得到要统计的表
//                if (realTables.isEmpty()) {
//                    continue;
//                }
//
//                for (String realTable : realTables) {
//                    //新增玩家SQL
//                    sqlStr = "select userId,DATE_FORMAT(from_unixtime(time), '%Y-%m-%d') as day from $table where serverId=" + sid;
//                    if (isBlack) {
//                        sqlStr += " and userId not in(" + blackUsers + ")";
//                    }
//                    if (!Strings.isBlank(channelNames)) {
//                        sqlStr += " and platformName in (" + channelNames + ")";
//                    }
//                    sqlStr += " and time between unix_timestamp(@startDate) and unix_timestamp(@endDate) group by userId,DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')";
//
//                    List<Map<String, String>> single = QueryUtil.getInstance().query(db, sqlStr, realTable, paraMap);
//
//                }
//            }
            //ltv new user
            //得到选择时间内所有角色注册的信息
            int finalServerIdx = QueryUtil.getInstance().getHeFuId(sid);
            Dblog rolestatedblog = DbLogListManager.getInstance().getDBServer(groupName, finalServerIdx);
            String firstSqlStr = getUserRegisterSql(channelNames, TableName.RoleState, sid+"", startDate + " 00:00:00", endDate + " 23:59:59");
            List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(rolestatedblog, firstSqlStr);

            if(dataMap != null && dataMap.size()>0){
                for (Map<String, Object> firstMap : dataMap) {
                    String uid = firstMap.get("userId").toString();
                    String createTime = firstMap.get("createTime").toString();
                    Integer idx = createTime.indexOf(' ');
                    createTime = createTime.substring(0,idx);
                    if(createUsers.containsKey(uid)){
                        if(createUsers.get(uid).compareTo(createTime) > 0) {
                            createUsers.put(uid,createTime);
                        }
                    }
                    else{
                        createUsers.put(uid,createTime);
                    }
                }
            }
            if(createUsers != null && createUsers.size()>0){
                for(String key : createUsers.keySet()){
                    Map<String, String> dayUser = new HashMap<>();
                    dayUser.put("userId",key);
                    dayUser.put("day",createUsers.get(key));

                    newUserList.add(dayUser);
                }
            }

            if (!rechargeMap.isEmpty()) {
                for (Map<String, String> rechargeUserMap : rechargeMap) {
                    String rechargeUserId = rechargeUserMap.get("userId");
                    String rechargeDay = rechargeUserMap.get("day");
                    for (Map<String, String> newUserMap :newUserList){
                        String newUserId = newUserMap.get("userId");
                        String newUserDay = newUserMap.get("day");
                        if (rechargeUserId.equals(newUserId) && rechargeDay.equals(newUserDay)){
                            Map<String, String> map = new LinkedHashMap<>();
                            map.put("userId",newUserId);
                            map.put("day",newUserDay);
                            newRechargeUserList.add(map);
                        }else {
                            continue;
                        }
                    }
                }
            }

            //新增设备
            if (dblog != null) {
                table = TableName.RoleState;//rolestate不用判断合服
                //新增设备SQL
                sqlDeviceStr = "select t2.machineCode,DATE_FORMAT(t2.createTime, '%Y-%m-%d') as day from (select machineCode,createTime from $table as t1 where createsid in(@serverId)";
                if (isBlack) {
                    sqlDeviceStr += " and userId not in(" + blackUsers + ")";
                }
                if (!Strings.isBlank(channelNames)) {
                    sqlDeviceStr += " and platformName in (" + channelNames + ")";
                }
                sqlDeviceStr += " group by machineCode,createTime having createTime=(select min(createTime) from $table where machineCode=t1.machineCode )) t2 where t2.createTime between @startDate AND @endDate group by t2.machineCode,DATE_FORMAT(t2.createTime, '%Y-%m-%d')";
                List<Map<String, String>> deviceDataMap = QueryUtil.getInstance().query(dblog, sqlDeviceStr, table, paraMap);// 单服的记录
                newDeviceList.addAll(deviceDataMap);
            } else {
                log.error("没有找到serverId：" + serverId + "(最终合服serverId:" + finalServerId + ")对应的t_dblog配置");
            }

            // 在线人数backend
            paraMap.put("startDate", startDate);
            paraMap.put("endDate", endDate);
            table = "t_servernum";
            sqlStr = "select max(num) as maxnum,avg(num) as avgnum,DATE_FORMAT(from_unixtime(time), '%Y-%m-%d') as day from $table where serverId in(@serverId) and DATE_FORMAT(from_unixtime(time), '%Y-%m-%d') BETWEEN @startDate AND @endDate ";
            sqlStr += " group by DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')";

            List<Map<String, String>> onlineList = QueryUtil.getInstance().query(dao, sqlStr, table, paraMap);
            List<String> initKeys = new ArrayList<>();// 初始化没有的数据
            initKeys.add("avgnum");
            if (!onlineList.isEmpty()) {
                List<String> addOnlineList = addOnlineResult(resultMap, onlineList, mapKey);
                if (onlineList.size() < resultMap.size()) {
                    for (Entry<String, Map<String, String>> entry : resultMap
                            .entrySet()) {
                        if (!addOnlineList.contains(entry.getKey())) {
                            for (String initKey : initKeys) {
                                entry.getValue().put(initKey, "0");
                            }
                        }
                    }
                }
            } else {
                for (Entry<String, Map<String, String>> entry : resultMap
                        .entrySet()) {
                    for (String initKey : initKeys) {
                        if (!entry.getValue().containsKey(initKey)) {
                            entry.getValue().put(initKey, "0");
                        }
                    }
                }
            }

        }
        //统计充值总额和充值人数
        // 组装查询统计结果
        //System.out.println("rechargeMap===="+rechargeMap.toString());
        if (!rechargeMap.isEmpty()) {
            Map<String, List<Map<String, String>>> dataMap = new TreeMap<>();
            Map<String, Map<String, String>> data = new TreeMap<>();
            for (Map<String, String> map : rechargeMap) {
                if (map.containsKey("day")) {
                    String day = map.get("day");
                    map.remove("day");
                    List<Map<String, String>> list = new ArrayList<>();
                    list.add(map);
                    if (dataMap.containsKey(day)) {
                        list.addAll(dataMap.get(day));
                    }
                    dataMap.put(day, list);
                }
            }
            for (String key : dataMap.keySet()) {
                float totalmoney = 0f;
                float totalgold = 0f;
                int totaltimes = 0;
//                float totalbindgold = 0f;
                Set<String> userSet = new HashSet<>();
                for (Map<String, String> map : dataMap.get(key)) {
                    totalmoney += Float.parseFloat(map.get("totalmoney"));
                    totalgold += Float.parseFloat(map.get("totalgold"));
                    totaltimes += Integer.parseInt(map.get("totaltimes"));
//                    totalbindgold += Float.parseFloat(map.get("totalbindgold"));
                    userSet.add(map.get("userId"));
                }
                int totaluser = userSet.size();
                Map<String, String> map = new HashMap<>();
                map.put("totalmoney", totalmoney + "");
                map.put("totalgold", totalgold + "");
                map.put("totaltimes", totaltimes + "");
//                map.put("totalbindgold", totalbindgold + "");
                map.put("totaluser", totaluser + "");
                if (data.containsKey(key)) {
                    map.put("totalmoney", (totalmoney + Float.parseFloat(data.get(key).get("totalmoney"))) + "");
                    map.put("totalgold", (totalgold + Float.parseFloat(data.get(key).get("totalgold"))) + "");
                    map.put("totaltimes", (totaltimes + Integer.parseInt(data.get(key).get("totaltimes"))) + "");
//                    map.put("totalbindgold", (totalbindgold + Float.parseFloat(data.get(key).get("totalbindgold"))) + "");
                    map.put("totaluser", (totaluser + Integer.parseInt(data.get(key).get("totaluser"))) + "");
                }
                data.put(key, map);
            }
            addStatResult(resultMap, data, exFields);
        }

        //统计活跃玩家、活跃设备
        String userKey = "userId";
        String activeNum = "activenum";
        String deviceKey = "machineCode";
        String deviceNum = "deviceNum";
        Map<String, Set<String>> activeNumMap;
        Map<String, Set<String>> deviceNumMap;
        activeNumMap = getAssembleMap(activeUserList, mapKey, userKey);
        getRMap(resultMap, activeNumMap, activeNum);
        deviceNumMap = getAssembleMap(activeDeviceList, mapKey, deviceKey);
        getRMap(resultMap, deviceNumMap, deviceNum);

        Map<String, Set<String>> newUserNumMap;
        Map<String, Set<String>> newDeviceNumMap;

        Map<String, Set<String>> newRechargeUserMap;
        //统计新增玩家、新增设备
        if (!newUserList.isEmpty()) {

            newUserNumMap = getAssembleMap(newUserList, mapKey, userKey);
            getRMap(resultMap, newUserNumMap, "addnum");

            //addResult(resultMap, single, mapKey,"addnum");
        }
        if (!newDeviceList.isEmpty()) {
            newDeviceNumMap = getAssembleMap(newDeviceList, mapKey, deviceKey);
            getRMap(resultMap, newDeviceNumMap, "deviceaddnum");
        }

        //统计新增付费玩家人数
        if (!newRechargeUserList.isEmpty()){
            newRechargeUserMap = getAssembleMap(newRechargeUserList,mapKey,userKey);
            getRMap(resultMap, newRechargeUserMap, "addRechargeNum");
        }

        if (resultMap.isEmpty()) {
            return new NutMap().setv("ok", false).setv("msg", Mvcs.getMessages(Mvcs.getReq()).get("log.nodata"));
        }
        // 根据key排序
        Map<String, Map<String, String>> sortMap = new TreeMap<>(resultMap);
        log.error("日常数据查询结果：" + sortMap);
        return Toolkit.outResult(true, sortMap).setv("type", serverId);

    }


    private String getUserRegisterSql(String channelNames, String table, String serverId, String startDate, String endDate) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT t2.userId,t2.createTime");
        str.append(" FROM (SELECT userId,createTime,createsid FROM " + table + " AS t1");
        if (!Strings.isBlank(channelNames)) {
            str.append(" where platformName in (" + channelNames + ")");
        }
        str.append(" GROUP BY userId,createTime HAVING createTime=( SELECT MIN(createTime)");
        str.append(" FROM " + table + " WHERE userId=t1.userId)) t2");
        str.append(" WHERE UNIX_TIMESTAMP(t2.createTime) BETWEEN UNIX_TIMESTAMP('" + startDate + " 00:00:00')  AND UNIX_TIMESTAMP('" + endDate + " 23:59:59')");
        str.append(" AND t2.createsid = " + serverId);
        return str.toString();
    }

    // 组装查询统计结果
    private Map<String, Set<String>> getAssembleMap(List<Map<String, String>> dataMap, String mapKey, String key) {
        Map<String, Set<String>> activeNumMap = new HashMap<>();
        Set<String> activeSet;
        for (Map<String, String> map : dataMap) {
            if (activeNumMap.containsKey(map.get(mapKey))) {
                if (!activeNumMap.get(map.get(mapKey)).contains(map.get(key))) {
                    activeNumMap.get(map.get(mapKey)).add(String.valueOf(map.get(key)));
                }
            } else {
                activeSet = new HashSet<>();
                activeSet.add(String.valueOf(map.get(key)));
                activeNumMap.put(map.get(mapKey), activeSet);
            }
        }
        return activeNumMap;
    }

    //统计活跃玩家和活跃设备\新增玩家，新增设备
    private void getRMap(Map<String, Map<String, String>> resultMap, Map<String, Set<String>> activeNumMap, String activeKey) {
        Map<String, String> rMap;
        for (Entry<String, Set<String>> entry : activeNumMap.entrySet()) {
            if (resultMap.containsKey(entry.getKey())) {
                if (resultMap.get(entry.getKey()).containsKey(activeKey)) {
                    String temp = Integer.parseInt(resultMap.get(entry.getKey()).get(activeKey)) + entry.getValue().size() + "";
                    resultMap.get(entry.getKey()).put(activeKey, temp);
                } else {
                    resultMap.get(entry.getKey()).put(activeKey, entry.getValue().size() + "");
                }
            } else {
                rMap = new HashMap<>();
                rMap.put(activeKey, entry.getValue().size() + "");
                resultMap.put(entry.getKey(), rMap);
            }
        }
    }

    @At
    public Object shopItemStatistic(String groupName, int serverId, String channelNames, int shopId, int moneyType,
                                    String startDate, String endDate, boolean isBlack) {
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            start.setTime(sdf.parse(startDate));
            end.setTime(sdf.parse(endDate));
            end.add(Calendar.DAY_OF_MONTH, 1);
            end.setTimeInMillis(end.getTimeInMillis() - 1000L);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        String blackUsers = "";
        List<Dblog> dbList = QueryUtil.getInstance().checkHeFu(serverId, start.getTime(), end.getTime());

        String table = "shopbuylog";
        String key = "itemModelId";
        Map<String, Object> paraMap = new LinkedHashMap<>();
        paraMap.put("serverId", serverId);
        paraMap.put("stime", start.getTimeInMillis() / 1000);
        paraMap.put("etime", end.getTimeInMillis() / 1000);
        String shopIdStr = "";
        if(shopId > 0){
            shopIdStr = " and shopId=@shopId";
            paraMap.put("shopId", shopId);
        }
        paraMap.put("moneyType", moneyType);


        List<String> queryTables = QueryUtil.getInstance().getQueryTables(table, TableType.Month, start, end);
        int consumerCount = 0;
        if (isBlack) {
            List<Object> blackList = BlackListManager.getInstance().getBlackListUsers(groupName);
            if (blackList.isEmpty()) {
                isBlack = false;
            } else {
                blackUsers = ListToStringUtil.listToString(blackList);
            }
        }

        for (Dblog db : dbList) {
            List<String> realTables = QueryUtil.getInstance().queryTables(db, table);
            realTables.retainAll(queryTables);
            if (realTables.isEmpty()) {
                continue;
            }
            Map<String, String> sqlMap = new HashMap<>();
            sqlMap.put("select",
                    "select itemModelId,moneyType,count(distinct(userId)) as users,count(distinct(roleId)) as roles,sum(realNum) as totalnum ,sum(moneyNum) as totalgold,sum(buyTimes) as totaltimes from (");
            sqlMap.put("inner_select",
                    "select itemModelId,moneyType,userId,roleId,realnum,moneynum,buyTimes from ");
            if (moneyType == 0) {
                if (!Strings.isBlank(channelNames)) {
                    sqlMap.put("inner_condition", " where sid=@serverId "+shopIdStr+" and time>=@stime and time<=@etime and platformName in (" + channelNames + ")");
                } else {
                    sqlMap.put("inner_condition", " where sid=@serverId "+shopIdStr+" and time>=@stime and time<=@etime");
                }
            } else {
                if (!Strings.isBlank(channelNames)) {
                    sqlMap.put("inner_condition", " where sid=@serverId and moneyType=@moneyType "+shopIdStr+" and time>=@stime and time<=@etime and platformName in (" + channelNames + ")");
                } else {
                    sqlMap.put("inner_condition", " where sid=@serverId and moneyType=@moneyType "+shopIdStr+" and time>=@stime and time<=@etime");
                }
            }
            sqlMap.put("inner_black", " and userId not in(");
            sqlMap.put("inner_union_all", " union all ");
            sqlMap.put("inner_t", ") t");
            sqlMap.put("groupby", " group by itemModelId,moneyType");

            String sqlStr;
            if (isBlack) {
                sqlStr = createSql(realTables, sqlMap, blackUsers);
            } else {
                sqlStr = createSql(realTables, sqlMap);
            }
            Map<String, Map<String, String>> singleMap = QueryUtil.getInstance().queryStat(db, sqlStr, key, paraMap);// 单服的记录
            // 组装查询统计结果
            if (!singleMap.isEmpty()) {
                addStatResult(resultMap, singleMap);
                // 查询总人数
                StringBuilder countSql = new StringBuilder();
                countSql.append("SELECT COUNT(roleId) FROM " + realTables.get(0) + " ");
                countSql.append("WHERE SID = " + db.getServerId()
                        + " AND TIME>=" + paraMap.get("stime") + " AND TIME<=" + paraMap.get("etime"));
                if (!Strings.isBlank(channelNames)) {
                    countSql.append(" and platformName in (" + channelNames + ")");
                }
                countSql.append(" GROUP BY roleId ");
                for (int i = 1; i < realTables.size(); i++) {
                    countSql.append("UNION ALL ");
                    countSql.append("SELECT COUNT(roleId) FROM "
                            + realTables.get(i) + " ");
                    countSql.append("WHERE SID = " + db.getServerId()
                            + " AND TIME>=" + paraMap.get("stime")
                            + " AND TIME<=" + paraMap.get("etime"));
                    if (!Strings.isBlank(channelNames)) {
                        countSql.append(" and platformName in (" + channelNames + ")");
                    }
                    countSql.append(" GROUP BY roleId ");
                }
                int count = QueryUtil.getInstance().queryCount(db, countSql.toString());
                consumerCount += count;
            }
        }
        if (resultMap.isEmpty()) {
            return new NutMap().setv("ok", false).setv("msg", Mvcs.getMessages(Mvcs.getReq()).get("log.nodata"));
        }
        // 物品名称获取
        for (Entry<String, Map<String, String>> entry : resultMap.entrySet()) {
            Map<String, String> map = entry.getValue();
            map.put(key, ItemManager.getInstance().getItemName(Integer.parseInt(entry.getKey())));
            map.put("moneyType", ItemManager.getInstance().getItemName(Integer.parseInt(map.get("moneyType"))));
        }
        return Toolkit.outResult(true, resultMap).setv("consumerCount", consumerCount);
    }

    @At
    public Object moneyOutputAndConsumeStatistic(String groupName, int serverId,
                                                 String[] moneyTypeList, String startDate, String endDate, boolean isBlack, String channelName) {
        Map<String, Map<String, Map<String, String>>> resultMap = new LinkedHashMap<>();
        String goldType = "3";
        String bindGoldType = "4";
        String expType = "8";
        Set<String> moneyTypeSet = new HashSet<>();
        Set<String> otherMoneyTypeSet = new HashSet<>();
        if (!Strings.isBlank(channelName)) {
            channelName = "'" + channelName + "'";
            channelName = channelName.replace(",", "','");
        }
        if (moneyTypeList != null) {
            for (String moneyType : moneyTypeList) {
                if (moneyType.equals("1") || moneyType.equals("2")) {
                    moneyTypeSet.add(moneyType);
                } else if (!moneyType.equals(goldType) && !moneyType.equals(bindGoldType)) {
                    otherMoneyTypeSet.add(moneyType);
                }
                resultMap.put(moneyType, new TreeMap<>());
            }
        } else {
            return new NutMap().setv("ok", false).setv("msg", Mvcs.getMessages(Mvcs.getReq()).get("log.nodata"));
        }

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            start.setTime(sdf.parse(startDate));
            end.setTime(sdf.parse(endDate));
            end.add(Calendar.DAY_OF_MONTH, 1);
            end.setTimeInMillis(end.getTimeInMillis() - 1000L);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        List<Dblog> dbList = QueryUtil.getInstance().checkHeFu(serverId, start.getTime(), end.getTime());// 检查合服,获取合服的数据库

        String goldTable = "goldchangelog";
        String bindGoldTable = "bindgoldchangelog";
        String expTable = "expchangelog";
        String moneyTable = "moneychangelog";
        String coinTable = "coinchangelog";

        String key = "day";
        Map<String, Object> paraMap = new LinkedHashMap<>();
        paraMap.put("serverId", serverId);
        paraMap.put("stime", start.getTimeInMillis() / 1000);
        paraMap.put("etime", end.getTimeInMillis() / 1000);

        List<String> exFields = new ArrayList<>();
        exFields.add(key);

        //元宝
        Calendar tempstart = Calendar.getInstance();
        tempstart.setTimeInMillis(start.getTimeInMillis());
        Calendar tempend = Calendar.getInstance();
        tempend.setTimeInMillis(end.getTimeInMillis());
        String blackUsers = "";
        List<String> queryGoldTables = null;
        if (resultMap.containsKey(goldType)) {
            queryGoldTables = QueryUtil.getInstance().getQueryTables(goldTable, TableType.Month, tempstart, tempend);
            if (isBlack) {
                List<Object> blackList = BlackListManager.getInstance().getBlackListUsers(groupName);
                if (blackList.isEmpty()) {
                    isBlack = false;
                } else {
                    blackUsers = ListToStringUtil.listToString(blackList);
                }
            }
        }

        //绑定元宝
        tempstart.setTimeInMillis(start.getTimeInMillis());
        tempend.setTimeInMillis(end.getTimeInMillis());
        List<String> queryBindGoldTables = null;
        if (resultMap.containsKey(bindGoldType)) {
            queryBindGoldTables = QueryUtil.getInstance().getQueryTables(bindGoldTable, TableType.Month, tempstart, tempend);
        }

        //经验值
        tempstart.setTimeInMillis(start.getTimeInMillis());
        tempend.setTimeInMillis(end.getTimeInMillis());
        List<String> queryExpTables = null;
        if (resultMap.containsKey(expType)) {
            queryExpTables = QueryUtil.getInstance().getQueryTables(expTable, TableType.Month, tempstart, tempend);
        }

        //金币和绑定金币
        tempstart.setTimeInMillis(start.getTimeInMillis());
        tempend.setTimeInMillis(end.getTimeInMillis());
        List<String> queryMoneyTables = null;
        if (!moneyTypeSet.isEmpty()) {
            queryMoneyTables = QueryUtil.getInstance().getQueryTables(moneyTable, TableType.Month, tempstart, tempend);
        }

        //其他货币
        tempstart.setTimeInMillis(start.getTimeInMillis());
        tempend.setTimeInMillis(end.getTimeInMillis());
        List<String> queryOtherMoneyTables = null;
        if (!otherMoneyTypeSet.isEmpty()) {
            queryOtherMoneyTables = QueryUtil.getInstance().getQueryTables(coinTable, TableType.Month, tempstart, tempend);
        }

        for (Dblog db : dbList) {
            //统计元宝
            if (resultMap.containsKey(goldType)) {
                List<String> realGoldTables = QueryUtil.getInstance().queryTables(db, goldTable);// 得到日志库中存在的表名
                realGoldTables.retainAll(queryGoldTables);// 求交集，得到要统计的表

                if (!realGoldTables.isEmpty()) {
                    Map<String, String> sqlMap = new HashMap<>();
                    sqlMap.put("select", "select DATE_FORMAT(from_unixtime(time), '%Y-%m-%d') as day,sum(output) as output,sum(consume) as consume from (");
                    sqlMap.put("inner_select_output", " select time,sum(changenum) as output,0 as consume from ");
                    if (!Strings.isBlank(channelName)) {
                        sqlMap.put("inner_condition_output", " where sid=@serverId and changenum>0 and time>=@stime and time<=@etime and platformName in (" + channelName + ")");
                    } else {
                        sqlMap.put("inner_condition_output", " where sid=@serverId and changenum>0 and time>=@stime and time<=@etime ");
                    }
                    sqlMap.put("inner_groupby", " group by DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')");
                    sqlMap.put("inner_select_consume", " select time,0 as output,sum(changenum) as consume from ");
                    if (!Strings.isBlank(channelName)) {
                        sqlMap.put("inner_condition_consume", " where sid=@serverId and changenum<0 and time>=@stime and time<=@etime and platformName in (" + channelName + ")");
                    } else {
                        sqlMap.put("inner_condition_consume", " where sid=@serverId and changenum<0 and time>=@stime and time<=@etime ");
                    }
                    sqlMap.put("inner_black", " and userId not in(");
                    sqlMap.put("inner_union_all", " union all ");
                    sqlMap.put("inner_t", ") t");
                    sqlMap.put("groupby", " group by DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')");

                    String sqlStr;
                    if (isBlack) {
                        sqlStr = createMoneySql(realGoldTables, sqlMap, blackUsers);
                    } else {
                        sqlStr = createMoneySql(realGoldTables, sqlMap);
                    }
                    Map<String, Map<String, String>> singleMap = QueryUtil.getInstance().queryStat(db, sqlStr, key, paraMap);// 单服的记录
                    if (!singleMap.isEmpty()) {
                        addStatResult(resultMap.get(goldType), singleMap);
                    }
                }
            }

            //统计绑定元宝
            if (resultMap.containsKey(bindGoldType)) {
                List<String> realBindGoldTables = QueryUtil.getInstance().queryTables(db, bindGoldTable);// 得到日志库中存在的表名
                realBindGoldTables.retainAll(queryBindGoldTables);// 求交集，得到要统计的表

                if (!realBindGoldTables.isEmpty()) {
                    Map<String, String> sqlMap = new HashMap<String, String>();
                    sqlMap.put("select", "select DATE_FORMAT(from_unixtime(time), '%Y-%m-%d') as day,sum(output) as output,sum(consume) as consume from (");
                    sqlMap.put("inner_select_output", " select time,sum(changenum) as output,0 as consume from ");
                    if (!Strings.isBlank(channelName)) {
                        sqlMap.put("inner_condition_output", " where sid=@serverId and changenum>0 and time>=@stime and time<=@etime and platformName in (" + channelName + ")");
                    } else {
                        sqlMap.put("inner_condition_output", " where sid=@serverId and changenum>0 and time>=@stime and time<=@etime ");
                    }
                    sqlMap.put("inner_select_consume", " select time,0 as output,sum(changenum) as consume from ");
                    if (!Strings.isBlank(channelName)) {
                        sqlMap.put("inner_condition_consume", " where sid=@serverId and changenum<0 and time>=@stime and time<=@etime and platformName in (" + channelName + ")");
                    } else {
                        sqlMap.put("inner_condition_consume", " where sid=@serverId and changenum<0 and time>=@stime and time<=@etime ");
                    }
                    sqlMap.put("inner_groupby", " group by DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')");
                    sqlMap.put("inner_union_all", " union all ");
                    sqlMap.put("inner_t", ") t");
                    sqlMap.put("groupby", " group by DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')");

                    String sqlStr = createMoneySql(realBindGoldTables, sqlMap);

                    Map<String, Map<String, String>> singleMap = QueryUtil.getInstance().queryStat(db, sqlStr, key, paraMap);// 单服的记录
                    // 组装查询统计结果
                    if (!singleMap.isEmpty()) {
                        addStatResult(resultMap.get(bindGoldType), singleMap);
                    }
                }
            }

            //统计经验值
            if (resultMap.containsKey(expType)) {
                List<String> realExpTables = QueryUtil.getInstance().queryTables(db, expTable);// 得到日志库中存在的表名
                realExpTables.retainAll(queryExpTables);// 求交集，得到要统计的表

                if (!realExpTables.isEmpty()) {
                    Map<String, String> sqlMap = new HashMap<>();
                    sqlMap.put("select", "select DATE_FORMAT(from_unixtime(time), '%Y-%m-%d') as day,sum(output) as output,sum(consume) as consume from (");
                    sqlMap.put("inner_select_output", " select time,sum(changenum) as output,0 as consume from ");
                    if (!Strings.isBlank(channelName)) {
                        sqlMap.put("inner_condition_output", " where sid=@serverId and changenum>0 and time>=@stime and time<=@etime and platformName in (" + channelName + ")");
                    } else {
                        sqlMap.put("inner_condition_output", " where sid=@serverId and changenum>0 and time>=@stime and time<=@etime ");
                    }
                    sqlMap.put("inner_select_consume", " select time,0 as output,sum(changenum) as consume from ");
                    if (!Strings.isBlank(channelName)) {
                        sqlMap.put("inner_condition_consume", " where sid=@serverId and changenum<0 and time>=@stime and time<=@etime and platformName in (" + channelName + ")");
                    } else {
                        sqlMap.put("inner_condition_consume", " where sid=@serverId and changenum<0 and time>=@stime and time<=@etime ");
                    }
                    sqlMap.put("inner_groupby", " group by DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')");
                    sqlMap.put("inner_union_all", " union all ");
                    sqlMap.put("inner_t", ") t");
                    sqlMap.put("groupby", " group by DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')");

                    String sqlStr = createMoneySql(realExpTables, sqlMap);

                    Map<String, Map<String, String>> singleMap = QueryUtil.getInstance().queryStat(db, sqlStr, key, paraMap);// 单服的记录
                    // 组装查询统计结果
                    if (!singleMap.isEmpty()) {
                        addStatResult(resultMap.get(expType), singleMap);
                    }
                }
            }

            //金币或绑定金币
            if (!moneyTypeSet.isEmpty()) {
                List<String> realMoneyTables = QueryUtil.getInstance().queryTables(db, moneyTable);// 得到日志库中存在的表名
                realMoneyTables.retainAll(queryMoneyTables);// 求交集，得到要统计的表

                if (!realMoneyTables.isEmpty()) {

                    for (String moneyType : resultMap.keySet()) {
                        Map<String, String> sqlMap = new HashMap<>();
                        sqlMap.put("select", "select DATE_FORMAT(from_unixtime(time), '%Y-%m-%d') as day,sum(output) as output,sum(consume) as consume from (");
                        sqlMap.put("inner_select_output", " select time,sum(changenum) as output,0 as consume from ");
                        if (!Strings.isBlank(channelName)) {
                            sqlMap.put("inner_condition_output", " where sid=@serverId and changenum>0 and time>=@stime and time<=@etime and platformName in (" + channelName + ")");
                        } else {
                            sqlMap.put("inner_condition_output", " where sid=@serverId and changenum>0 and time>=@stime and time<=@etime ");
                        }
                        sqlMap.put("inner_select_consume", " select time,0 as output,sum(changenum) as consume from ");
                        if (!Strings.isBlank(channelName)) {
                            sqlMap.put("inner_condition_consume", " where sid=@serverId and changenum<0 and time>=@stime and time<=@etime and platformName in (" + channelName + ")");
                        } else {
                            sqlMap.put("inner_condition_consume", " where sid=@serverId and changenum<0 and time>=@stime and time<=@etime ");
                        }
                        sqlMap.put("inner_groupby", " group by DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')");
                        sqlMap.put("inner_union_all", " union all ");
                        sqlMap.put("inner_t", ") t");
                        sqlMap.put("groupby", " group by DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')");

                        String sqlStr = createMoneySql(realMoneyTables, sqlMap);

                        paraMap.put("moneyType", Integer.parseInt(moneyType));
                        Map<String, Map<String, String>> singleMap = QueryUtil.getInstance().queryStat(db, sqlStr, key, paraMap);// 单服的记录

                        // 组装查询统计结果
                        if (!singleMap.isEmpty()) {
                            addStatResult(resultMap.get(moneyType), singleMap, exFields);
                        }
                    }
                }
            }

            //统计其他货币
            if (!otherMoneyTypeSet.isEmpty()) {
                List<String> realOtherMoneyTables = QueryUtil.getInstance().queryTables(db, coinTable);// 得到日志库中存在的表名
                realOtherMoneyTables.retainAll(queryOtherMoneyTables);// 求交集，得到要统计的表

                if (!realOtherMoneyTables.isEmpty()) {

                    for (String moneyType : resultMap.keySet()) {
                        Map<String, String> sqlMap = new HashMap<>();
                        sqlMap.put("select", "select DATE_FORMAT(from_unixtime(time), '%Y-%m-%d') as day,sum(output) as output,sum(consume) as consume from (");
                        sqlMap.put("inner_select_output", " select time,sum(changenum) as output,0 as consume from ");
                        if (!Strings.isBlank(channelName)) {
                            sqlMap.put("inner_condition_output", " where sid=@serverId and changenum>0 and time>=@stime and time<=@etime and platformName in (" + channelName + ")");
                        } else {
                            sqlMap.put("inner_condition_output", " where sid=@serverId and changenum>0 and time>=@stime and time<=@etime ");
                        }
                        sqlMap.put("inner_select_consume", " select time,0 as output,sum(changenum) as consume from ");
                        if (!Strings.isBlank(channelName)) {
                            sqlMap.put("inner_condition_consume", " where sid=@serverId and changenum<0 and time>=@stime and time<=@etime and platformName in (" + channelName + ")");
                        } else {
                            sqlMap.put("inner_condition_consume", " where sid=@serverId and changenum<0 and time>=@stime and time<=@etime ");
                        }
                        sqlMap.put("inner_groupby", " group by DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')");
                        sqlMap.put("inner_union_all", " union all ");
                        sqlMap.put("inner_t", ") t");
                        sqlMap.put("groupby", " group by DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')");

                        String sqlStr = createMoneySql(realOtherMoneyTables, sqlMap);

                        paraMap.put("moneyType", Integer.parseInt(moneyType));
                        Map<String, Map<String, String>> singleMap = QueryUtil.getInstance().queryStat(db, sqlStr, key, paraMap);// 单服的记录

                        // 组装查询统计结果
                        if (!singleMap.isEmpty()) {
                            addStatResult(resultMap.get(moneyType), singleMap, exFields);
                        }
                    }
                }
            }
        }
        if (resultMap.isEmpty()) {
            return Toolkit.outResult(false, Mvcs.getMessages(Mvcs.getReq()).get("log.nodata"));
        }
        return Toolkit.outResult(true, resultMap);
    }

    @At
    public Object horseIllusionStatistic(int serverId, int itemId, String startDate, String endDate) {
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            start.setTime(sdf.parse(startDate));
            end.setTime(sdf.parse(endDate));
            end.add(Calendar.DAY_OF_MONTH, 1);
            end.setTimeInMillis(end.getTimeInMillis() - 1000L);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        List<Dblog> dbList = QueryUtil.getInstance().checkHeFu(serverId,
                start.getTime(), end.getTime());// 检查合服,获取合服的数据库

        String table = "horseillusionlog";
        String key = "horse";
        Map<String, Object> paraMap = new LinkedHashMap<>();
        paraMap.put("serverId", serverId);
        paraMap.put("itemId", itemId);
        paraMap.put("stime", start.getTimeInMillis() / 1000);
        paraMap.put("etime", end.getTimeInMillis() / 1000);
        List<String> queryTables = QueryUtil.getInstance().getQueryTables(
                table, TableType.Month, start, end);
        for (Dblog db : dbList) {
            List<String> realTables = QueryUtil.getInstance().queryTables(
                    db, table);// 得到日志库中存在的表名
            realTables.retainAll(queryTables);// 求交集，得到要统计的表
            if (realTables.isEmpty()) {
                continue;
            }
            Map<String, String> sqlMap = new HashMap<>();
            sqlMap.put(
                    "select",
                    "select concat(curHighestLayer,'_',afterIllusionLevel,'_',illusionItemId) as horse,sum(times) as totaltimes,sum(num) as totalnum from (");
            sqlMap.put(
                    "inner_select",
                    "select curHighestLayer,afterIllusionLevel,illusionItemId,sum(illusionItemNum) as num,count(*) as times from ");
            sqlMap.put(
                    "inner_condition",
                    " where createSid=@serverId and illusionItemId=@itemId and time>=@stime and time<=@etime group by curHighestLayer,afterIllusionLevel");
            sqlMap.put("inner_union_all", " union all ");
            sqlMap.put("inner_t", ") t");
            sqlMap.put("groupby",
                    " group by curHighestLayer,afterIllusionLevel");

            String sqlStr = createSql(realTables, sqlMap);
            Map<String, Map<String, String>> singleMap = QueryUtil.getInstance().queryStat(db, sqlStr, key, paraMap);// 单服的记录
            // 组装查询统计结果
            if (!singleMap.isEmpty()) {
                addStatResult(resultMap, singleMap);
            }
        }
        if (resultMap.isEmpty()) {
            return new NutMap().setv("ok", false).setv("msg", Mvcs.getMessages(Mvcs.getReq()).get("log.nodata"));
        }
        // 根据key排序
        Map<String, Map<String, String>> sortMap = new TreeMap<>(
                new MapKeyComparator());
        sortMap.putAll(resultMap);
        return Toolkit.outResult(true, sortMap);
    }

    @At
    public Object dragoonStatistic(int serverId, String startDate,
                                   String endDate) {
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            start.setTime(sdf.parse(startDate));
            end.setTime(sdf.parse(endDate));
            end.add(Calendar.DAY_OF_MONTH, 1);
            end.setTimeInMillis(end.getTimeInMillis() - 1000L);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        List<Dblog> dbList = QueryUtil.getInstance().checkHeFu(serverId,
                start.getTime(), end.getTime());// 检查合服,获取合服的数据库

        String table = "dragoonuplog";
        String key = "afterkey";
        Map<String, Object> paraMap = new LinkedHashMap<>();
        paraMap.put("serverId", serverId);
        paraMap.put("stime", start.getTimeInMillis() / 1000);
        paraMap.put("etime", end.getTimeInMillis() / 1000);
        List<String> queryTables = QueryUtil.getInstance().getQueryTables(
                table, TableType.Month, start, end);
        for (Dblog db : dbList) {
            List<String> realTables = QueryUtil.getInstance().queryTables(
                    db, table);// 得到日志库中存在的表名
            realTables.retainAll(queryTables);// 求交集，得到要统计的表
            if (realTables.isEmpty()) {
                continue;
            }
            Map<String, String> sqlMap = new HashMap<>();
            sqlMap.put("select",
                    "SELECT t.afterkey,COUNT(t.roleId) totalcount,SUM(totaltimes) totaltimes FROM (");
            sqlMap.put("inner_select",
                    "SELECT afterkey,roleId,COUNT(roleId) totaltimes FROM ");
            sqlMap.put("inner_condition",
                    " where sid=@serverId and time>=@stime and time<=@etime group by roleId,afterkey ");
            sqlMap.put("inner_union_all", " union all ");
            sqlMap.put("inner_t", ") t");
            sqlMap.put("groupby", " group by afterkey");
            String sqlStr = createSql(realTables, sqlMap);

            Map<String, Map<String, String>> singleMap = QueryUtil
                    .getInstance().queryStat(db, sqlStr, key, paraMap);// 单服的记录
            // 组装查询统计结果
            if (!singleMap.isEmpty()) {
                addStatResult(resultMap, singleMap);
            }
        }
        if (resultMap.isEmpty()) {
            return new NutMap().setv("ok", false).setv("msg",
                    Mvcs.getMessages(Mvcs.getReq()).get("log.nodata"));
        }
        // 根据key排序
        Map<String, Map<String, String>> sortMap = new TreeMap<>();
        sortMap.putAll(resultMap);
        return new NutMap().setv("ok", true).setv("data", sortMap);
    }

    @At
    public Object cloakStatistic(int serverId, int itemId, String startDate,
                                 String endDate) {
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            start.setTime(sdf.parse(startDate));
            end.setTime(sdf.parse(endDate));
            end.add(Calendar.DAY_OF_MONTH, 1);
            end.setTimeInMillis(end.getTimeInMillis() - 1000L);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        List<Dblog> dbList = QueryUtil.getInstance().checkHeFu(serverId,
                start.getTime(), end.getTime());// 检查合服,获取合服的数据库

        String table = "cloakuplog";
        String key = "cloak";
        Map<String, Object> paraMap = new LinkedHashMap<>();
        paraMap.put("serverId", serverId);
        paraMap.put("itemId", itemId);
        paraMap.put("stime", start.getTimeInMillis() / 1000);
        paraMap.put("etime", end.getTimeInMillis() / 1000);
        List<String> queryTables = QueryUtil.getInstance().getQueryTables(
                table, TableType.Month, start, end);
        for (Dblog db : dbList) {
            List<String> realTables = QueryUtil.getInstance().queryTables(
                    db, table);// 得到日志库中存在的表名
            realTables.retainAll(queryTables);// 求交集，得到要统计的表
            if (realTables.isEmpty()) {
                continue;
            }
            Map<String, String> sqlMap = new HashMap<>();
            sqlMap.put(
                    "select",
                    "SELECT CONCAT(t.beforeCloakLayer,'_',t.upItemId) cloak,COUNT(t.playerId) totalcount,SUM(totaltimes) totaltimes,SUM(totalnum) totalnum FROM (");
            sqlMap.put(
                    "inner_select",
                    "SELECT playerId,upItemId,beforeCloakLayer,COUNT(playerId) totaltimes,SUM(upItemNum) totalnum FROM ");
            sqlMap.put(
                    "inner_condition",
                    " where createSid=@serverId and upItemId=@itemId and time>=@stime and time<=@etime group by beforeCloakLayer,playerId ");
            sqlMap.put("inner_union_all", " union all ");
            sqlMap.put("inner_t", ") t");
            sqlMap.put("groupby", " GROUP BY beforeCloakLayer");

            String sqlStr = createSql(realTables, sqlMap);
            Map<String, Map<String, String>> singleMap = QueryUtil
                    .getInstance().queryStat(db, sqlStr, key, paraMap);// 单服的记录
            // 组装查询统计结果
            if (!singleMap.isEmpty()) {
                addStatResult(resultMap, singleMap);
            }
        }
        if (resultMap.isEmpty()) {
            return new NutMap().setv("ok", false).setv("msg",
                    Mvcs.getMessages(Mvcs.getReq()).get("log.nodata"));
        }
        // 根据key排序
        Map<String, Map<String, String>> sortMap = new TreeMap<>(
                new MapKeyComparator());
        sortMap.putAll(resultMap);
        return new NutMap().setv("ok", true).setv("data", sortMap);
    }

    @At
    public Object petStatistic(String groupName, int serverId, String startDate, String endDate, boolean isBlack) {
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            start.setTime(sdf.parse(startDate));
            end.setTime(sdf.parse(endDate));
            end.add(Calendar.DAY_OF_MONTH, 1);
            end.setTimeInMillis(end.getTimeInMillis() - 1000L);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        List<Dblog> dbList = QueryUtil.getInstance().checkHeFu(serverId,
                start.getTime(), end.getTime());// 检查合服,获取合服的数据库

        String table = "petlog";
        String key = "petId";
        Map<String, Object> paraMap = new LinkedHashMap<>();
        paraMap.put("serverId", serverId);
        paraMap.put("stime", start.getTimeInMillis() / 1000);
        paraMap.put("etime", end.getTimeInMillis() / 1000);
        List<String> queryTables = QueryUtil.getInstance().getQueryTables(
                table, TableType.Month, start, end);
        String blackUsers = "";
        if (isBlack) {
            List<Object> blackList = BlackListManager.getInstance().getBlackListUsers(groupName);
            if (!blackList.isEmpty()) {
                blackUsers = ListToStringUtil.listToString(blackList);
            }
        }

        for (Dblog db : dbList) {
            List<String> realTables = QueryUtil.getInstance().queryTables(
                    db, table);// 得到日志库中存在的表名
            realTables.retainAll(queryTables);// 求交集，得到要统计的表
            if (realTables.isEmpty()) {
                continue;
            }

            Map<String, String> sqlMap = new HashMap<>();
            sqlMap.put("select", "select petId,sum(times) as totaltimes from (");
            sqlMap.put("inner_select", "select petId,count(1) as times from ");
            sqlMap.put(
                    "inner_condition",
                    " where sid=@serverId and time>=@stime and time<=@etime");
            sqlMap.put("inner_black", " and playerId not in(");
            sqlMap.put("inner_groupby", " group by petId");
            sqlMap.put("inner_union_all", " union all ");
            sqlMap.put("inner_t", ") t");
            sqlMap.put("groupby", " group by petId");

            String sqlStr;
            sqlStr = createPetSql(realTables, sqlMap, blackUsers);
            Map<String, Map<String, String>> singleMap = QueryUtil
                    .getInstance().queryStat(db, sqlStr, key, paraMap);// 单服的记录
            // 组装查询统计结果
            if (!singleMap.isEmpty()) {
                addStatResult(resultMap, singleMap);
            }
        }
        if (resultMap.isEmpty()) {
            return new NutMap().setv("ok", false).setv("msg",
                    Mvcs.getMessages(Mvcs.getReq()).get("log.nodata"));
        }
        // 根据key排序
        Map<String, Map<String, String>> sortMap = new TreeMap<>(resultMap);
        return new NutMap().setv("ok", true).setv("data", sortMap);
    }

    private String createPetSql(List<String> tableList, Map<String, String> sqlMap, String blackList) {
        StringBuffer select = new StringBuffer(sqlMap.get("select"));
        StringBuffer inner_select = new StringBuffer(sqlMap.get("inner_select"));
        StringBuffer inner_condition = new StringBuffer(
                sqlMap.get("inner_condition"));
        StringBuffer inner_black = new StringBuffer(sqlMap.get("inner_black"));
        StringBuffer inner_groupby = new StringBuffer(sqlMap.get("inner_groupby"));
        StringBuffer inner_union_all = new StringBuffer(
                sqlMap.get("inner_union_all"));
        StringBuffer inner_t = new StringBuffer(sqlMap.get("inner_t"));
        StringBuffer groupby = new StringBuffer(sqlMap.get("groupby"));

        for (String table : tableList) {
            select.append(inner_select);
            select.append(table);
            select.append(inner_condition);
            if (blackList != null && !blackList.equals("")) {
                select.append(inner_black);
                select.append(blackList);
                select.append(")");
            }
            select.append(inner_groupby);
            select.append(inner_union_all);
        }
        select.delete(select.length() - 11, select.length());
        select.append(inner_t);
        select.append(groupby);
        return select.toString();
    }

    private String createSql(List<String> tableList, Map<String, String> sqlMap) {
        StringBuffer select = new StringBuffer(sqlMap.get("select"));
        StringBuffer inner_select = new StringBuffer(sqlMap.get("inner_select"));
        StringBuffer inner_condition = new StringBuffer(
                sqlMap.get("inner_condition"));
        StringBuffer inner_union_all = new StringBuffer(
                sqlMap.get("inner_union_all"));
        StringBuffer inner_t = new StringBuffer(sqlMap.get("inner_t"));
        StringBuffer groupby = new StringBuffer(sqlMap.get("groupby"));

        for (String table : tableList) {
            select.append(inner_select);
            select.append(table);
            select.append(inner_condition);
            select.append(inner_union_all);
        }
        select.delete(select.length() - 11, select.length());
        select.append(inner_t);
        select.append(groupby);
        return select.toString();
    }

    private String createSql(List<String> tableList, Map<String, String> sqlMap, String blackList) {
        StringBuffer select = new StringBuffer(sqlMap.get("select"));
        StringBuffer inner_select = new StringBuffer(sqlMap.get("inner_select"));
        StringBuffer inner_condition = new StringBuffer(
                sqlMap.get("inner_condition"));
        StringBuffer inner_black = new StringBuffer(sqlMap.get("inner_black"));
        StringBuffer inner_union_all = new StringBuffer(
                sqlMap.get("inner_union_all"));
        StringBuffer inner_t = new StringBuffer(sqlMap.get("inner_t"));
        StringBuffer groupby = new StringBuffer(sqlMap.get("groupby"));
        if (blackList != null) {
            for (String table : tableList) {
                select.append(inner_select);
                select.append(table);
                select.append(inner_condition);
                select.append(inner_black);
                select.append(blackList);
                select.append(")");
                select.append(inner_union_all);
            }
        } else {
            for (String table : tableList) {
                select.append(inner_select);
                select.append(table);
                select.append(inner_condition);
                select.append(inner_union_all);
            }
        }
        select.delete(select.length() - 11, select.length());
        select.append(inner_t);
        select.append(groupby);
        return select.toString();
    }

    private String createMoneySql(List<String> tableList, Map<String, String> sqlMap, String blackList) {
        StringBuilder select = new StringBuilder(sqlMap.get("select"));
        if (blackList != null) {
            for (String table : tableList) {
                select.append(sqlMap.get("inner_select_output"));
                select.append(table);
                select.append(sqlMap.get("inner_condition_output"));
                select.append(sqlMap.get("inner_black"));
                select.append(blackList);
                select.append(")");
                select.append(sqlMap.get("inner_groupby"));
                select.append(sqlMap.get("inner_union_all"));
                select.append(sqlMap.get("inner_select_consume"));
                select.append(table);
                select.append(sqlMap.get("inner_condition_consume"));
                select.append(sqlMap.get("inner_black"));
                select.append(blackList);
                select.append(")");
                select.append(sqlMap.get("inner_groupby"));
                select.append(sqlMap.get("inner_union_all"));
            }
        } else {
            for (String table : tableList) {
                select.append(sqlMap.get("inner_select_output"));
                select.append(table);
                select.append(sqlMap.get("inner_condition_output"));
                select.append(sqlMap.get("inner_groupby"));
                select.append(sqlMap.get("inner_union_all"));
                select.append(sqlMap.get("inner_select_consume"));
                select.append(table);
                select.append(sqlMap.get("inner_condition_consume"));
                select.append(sqlMap.get("inner_groupby"));
                select.append(sqlMap.get("inner_union_all"));
            }
        }
        select.delete(select.length() - 11, select.length());//去掉末尾的inner_union_all
        select.append(sqlMap.get("inner_t"));
        select.append(sqlMap.get("groupby"));
        return select.toString();
    }

    private String createMoneySql(List<String> tableList, Map<String, String> sqlMap) {
        StringBuffer select = new StringBuffer(sqlMap.get("select"));
        StringBuffer inner_select_output = new StringBuffer(
                sqlMap.get("inner_select_output"));
        StringBuffer inner_condition_output = new StringBuffer(
                sqlMap.get("inner_condition_output"));
        StringBuffer inner_select_consume = new StringBuffer(
                sqlMap.get("inner_select_consume"));
        StringBuffer inner_condition_consume = new StringBuffer(
                sqlMap.get("inner_condition_consume"));
        StringBuffer inner_union_all = new StringBuffer(
                sqlMap.get("inner_union_all"));
        StringBuffer inner_t = new StringBuffer(sqlMap.get("inner_t"));
        StringBuffer groupby = new StringBuffer(sqlMap.get("groupby"));

        for (String table : tableList) {
            select.append(inner_select_output);
            select.append(table);
            select.append(inner_condition_output);
            select.append(sqlMap.get("inner_groupby"));
            select.append(inner_union_all);
            select.append(inner_select_consume);
            select.append(table);
            select.append(inner_condition_consume);
            select.append(sqlMap.get("inner_groupby"));
            select.append(inner_union_all);
        }
        select.delete(select.length() - 11, select.length());
        select.append(inner_t);
        select.append(groupby);
        return select.toString();
    }

    private void addResult(Map<String, Map<String, Object>> resultMap,
                           List<Map<String, Object>> list, String key, String addNum) {
        for (Map<String, Object> dataMap : list) {
            Map<String, Object> map;
            if (resultMap.containsKey(dataMap.get(key))) {
                map = resultMap.get(dataMap.get(key));
            } else {
                map = new HashMap<>();
            }
            for (Entry<String, Object> entry : dataMap.entrySet()) {
                if (entry.getKey().equals(addNum) && map.containsKey(addNum)) {
                    map.put(entry.getKey(), getSum(map.get(entry.getKey()), entry.getValue()));
                } else {
                    map.put(entry.getKey(), entry.getValue());
                }
            }
            resultMap.put(dataMap.get(key).toString(), map);
        }
    }

    private List<String> addOnlineResult(
            Map<String, Map<String, String>> resultMap,
            List<Map<String, String>> list, String key) {
        List<String> addList = new ArrayList<>();
        String avgNum = "avgnum";
        String maxNum = "maxnum";
        for (Map<String, String> dataMap : list) {
            Map<String, String> map;
            if (resultMap.containsKey(dataMap.get(key))) {
                map = resultMap.get(dataMap.get(key));
            } else {
                map = new HashMap<>();
            }
            for (Entry<String, String> entry : dataMap.entrySet()) {
                if (entry.getKey().equals(avgNum) && map.containsKey(avgNum)) {
                    map.put(entry.getKey(), getSum(map.get(entry.getKey()), entry.getValue()).toString());
                } else if (entry.getKey().equals(maxNum) && map.containsKey(maxNum)) {
                    map.put(entry.getKey(), getSum(map.get(entry.getKey()), entry.getValue()).toString());
                } else {
                    map.put(entry.getKey(), entry.getValue());
                }
            }
            resultMap.put(dataMap.get(key), map);
            addList.add(dataMap.get(key));
        }
        return addList;
    }

    private void addStatResult(Map<String, Map<String, String>> resultMap, Map<String, Map<String, String>> singleMap) {
        Map<String, String> map;
        for (Entry<String, Map<String, String>> entry : singleMap.entrySet()) {
            if (resultMap.containsKey(entry.getKey())) {
                map = resultMap.get(entry.getKey());
                for (Entry<String, String> sline : entry.getValue().entrySet()) {
                    map.put(sline.getKey(), getSum(map.get(sline.getKey()), sline.getValue()) + "");
                }
                resultMap.put(entry.getKey(), map);
            } else {
                resultMap.put(entry.getKey(), singleMap.get(entry.getKey()));
            }
        }
    }

    private void addStatResult(Map<String, Map<String, String>> resultMap, Map<String, Map<String, String>> singleMap, List<String> exFields) {
        Map<String, String> map;
        for (Entry<String, Map<String, String>> entry : singleMap.entrySet()) {
            if (resultMap.containsKey(entry.getKey())) {
                map = resultMap.get(entry.getKey());
                for (Entry<String, String> sline : entry.getValue().entrySet()) {
                    if (exFields.contains(sline.getKey())) {
                        continue;
                    }
                    map.put(sline.getKey(), getSum(map.get(sline.getKey()), sline.getValue()).toString());
                }
                resultMap.put(entry.getKey(), map);
            } else {
                resultMap.put(entry.getKey(), singleMap.get(entry.getKey()));
            }
        }
    }

    private Object getSum(Object r, Object s) {
        if (r == null && s != null) {
            return Float.parseFloat(s.toString());
        } else if (r != null && s == null) {
            return Float.parseFloat(r.toString());
        } else if (r == null && s == null) {
            return null;
        } else {
            return Float.parseFloat(r.toString()) + Float.parseFloat(s.toString());
        }
    }

    class MapKeyComparator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {
            String[] a = str1.split("_");
            String[] b = str2.split("_");
            int layerA = Integer.parseInt(a[0]);
            int levelA = Integer.parseInt(a[1]);
            int layerB = Integer.parseInt(b[0]);
            int levelB = Integer.parseInt(b[1]);
            if (layerA > layerB) {
                return 1;
            } else if (layerA < layerB) {
                return -1;
            } else {
                return Integer.compare(levelA, levelB);
            }
        }
    }
}
