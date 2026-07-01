package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.manager.CurrencyRateManager;
import com.backend.manager.DbLogListManager;
import com.backend.utils.QueryUtil;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 充值金额分布统计
 */
@IocBean
@Ok("json")
@At("/paydiststatistic")
@Fail("http:500")
public class PayDistributeStatisticModule {

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static String RECHARGELOG = "rechargelog";

    @At("/")
    @Ok("jsp:jsp.statistic.payDistribute")
    public void index(HttpServletRequest request) {
        request.setAttribute("nowDate", sdf.format(new Date()));
    }

    /**
     * 充值金额档位分布统计
     */
    @At
    public Object payLevelStatistic(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isblack) throws Exception {
        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);

        List<Map<String, Object>> payLevelDataMap = new ArrayList<>();
        List<Map<String, Object>> payUserDataMap = new ArrayList<>();
        for (String id : serverId) {
            Map<String, List<String>> hefuTableMap = QueryUtil.getInstance().getHefuTable(id, RECHARGELOG, start, end);
            List<String> rechargeGoalTables = QueryUtil.getInstance().getQueryTables(RECHARGELOG, TableType.Month, startDate, endDate);

            for (String key : hefuTableMap.keySet()) {
                List<String> tableList = hefuTableMap.get(key);
                tableList.retainAll(rechargeGoalTables);//过滤重复数据表
                for (String s : tableList) {
                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    if (dblog == null) {
                        continue;
                    }
                    String sqlLvStr = payLevelSqlStr(s, id, channelNames, startDate, endDate);
                    String sqlUserStr = getPayUserSqlStr(s, id, channelNames, startDate, endDate);
                    //先得到充值分布
                    List<Map<String, Object>> paylvdataMap = QueryUtil.getInstance().query(dblog, sqlLvStr);
                    getResult(payLevelDataMap, paylvdataMap, "paylvdata");
                    //得到充值用户
                    List<Map<String, Object>> payudataMap = QueryUtil.getInstance().query(dblog, sqlUserStr);
                    getResult(payUserDataMap, payudataMap, "payudata");
                }
            }
        }

        if (isblack) {
            List<Map<String, Object>> payUserBlackData = new ArrayList<>();
            List<Map<String, Object>> blackList = BlackListManager.getInstance().getBlackList(groupName);
            for (Map<String, Object> stringObjectMap : blackList) {
                //所有玩家的数据
                String userId = stringObjectMap.get("userId").toString();
                for (Map<String, Object> payUserMap : payUserDataMap) {
                    String userid = payUserMap.get("userId").toString();
                    if (userId.equals(userid)) {
                        payUserBlackData.add(payUserMap);
                    }
                }
            }
            payUserDataMap.removeAll(payUserBlackData);
        }

        //进行统计
        for (Map<String, Object> paylvMap : payLevelDataMap) {
            String amount1 = paylvMap.get("amount").toString();
            int count = 0;
            String currency = "";
            for (Map<String, Object> payUserMap : payUserDataMap) {
                String amount2 = payUserMap.get("amount").toString();
                currency = payUserMap.get("currency").toString();
                if (amount1.equals(amount2)) {
                    count++;
                }
            }
            double amount = Double.parseDouble(amount1) * CurrencyRateManager.getInstance().getValue(currency);
            paylvMap.put("amount", amount);
            paylvMap.put("count", count);
        }
        return payLevelDataMap;
    }

    /**
     * 充值金额时间段分布统计(每日)
     */
    @At
    public Object payDayStatistic(String groupName, String[] serverId, String channelNames, String startDate, boolean isblack) throws Exception {
        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        List<Map<String, Object>> payDayDataMap = new ArrayList<>();

        for (String id : serverId) {
            Map<String, List<String>> hefuTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(id, RECHARGELOG, sdf.parse(startDate), sdf.parse(startDate)));
            List<String> rechargeGoalTables = QueryUtil.getInstance().getQueryTables(RECHARGELOG, TableType.Year, startDate, startDate);

            for (String key : hefuTableMap.keySet()) {
                List<String> tableList = hefuTableMap.get(key);
                tableList.retainAll(rechargeGoalTables);//过滤重复数据表
                for (String s : tableList) {
                    String sqlStr = payDaySqlStr(s, id, channelNames, startDate);
                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    if (dblog == null) {
                        continue;
                    }
                    List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                    getResult(payDayDataMap, dataMap, "payDayData");
                }
            }
        }

        if (isblack) {
            List<Map<String, Object>> payDayBlackData = new ArrayList<>();
            List<Map<String, Object>> blackList = BlackListManager.getInstance().getBlackList(groupName);
            for (Map<String, Object> stringObjectMap : blackList) {
                //所有玩家的数据
                String userId = stringObjectMap.get("userId").toString();
                for (Map<String, Object> payDayMap : payDayDataMap) {
                    String userid = payDayMap.get("userId").toString();
                    if (userId.equals(userid)) {
                        payDayBlackData.add(payDayMap);
                    }
                }
            }
            payDayDataMap.removeAll(payDayBlackData);
        }
        List<Map<String, Object>> dataMapList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            int countSum = 0;
            float amountSum = 0;
            for (Map<String, Object> dataMap : payDayDataMap) {
                String currency = dataMap.get("currency").toString();
                int timeHour = Integer.parseInt(dataMap.get("timeHour").toString());
                if (timeHour == i) {
                    countSum++;
                    amountSum += Float.parseFloat(dataMap.get("amount").toString()) * CurrencyRateManager.getInstance().getValue(currency);
                }
            }
            Map<String, Object> map = new HashMap<>();
            String time = i + ":00" + "~" + (i + 1) + ":00";
            map.put("time", time);
            map.put("countSum", countSum);
            map.put("amountSum", amountSum);
            dataMapList.add(map);
        }
        return dataMapList;
    }

    private void getResult(List<Map<String, Object>> itemdataMapList, List<Map<String, Object>> dataMapList, String param) {
        //由于存在合服以及多服的选择，所以这里处理时，把多个服务器的按天产出累加以及消耗累加
        for (Map<String, Object> map : dataMapList) {
            boolean isHaveDay = false;//判断是否这一天已经存在在itemdataMapList里面了
            if (!itemdataMapList.isEmpty()) {
                for (Map<String, Object> itemMap : itemdataMapList) {
                    switch (param) {
                        case "paylvdata":
                            if (map.get("amount").toString().equals(itemMap.get("amount"))) {
                                isHaveDay = true;
                                itemMap.put("amount", itemMap.get("amount"));
                            }
                            break;
                        case "payudata":
                            if (map.get("amount").toString().equals(itemMap.get("amount")) && map.get("userId").toString().equals(itemMap.get("userId")) && map.get("currency").toString().equals(itemMap.get("currency"))) {
                                isHaveDay = true;
                                itemMap.put("amount", itemMap.get("amount"));
                                itemMap.put("userId", itemMap.get("userId"));
                                itemMap.put("currency", itemMap.get("currency"));
                            }
                            break;
                        case "payDayData":
                            if (map.get("amount").toString().equals(itemMap.get("amount")) && map.get("timeHour").toString().equals(itemMap.get("timeHour")) && map.get("userId").toString().equals(itemMap.get("userId")) && map.get("currency").toString().equals(itemMap.get("currency"))) {
                                isHaveDay = true;
                                itemMap.put("amount", itemMap.get("amount"));
                                itemMap.put("timeHour", itemMap.get("timeHour"));
                                itemMap.put("userId", itemMap.get("userId"));
                                itemMap.put("currency", itemMap.get("currency"));
                            }
                            break;
                    }
                }
            }
            if (!isHaveDay) {
                itemdataMapList.add(map);
            }
        }
    }

    /**
     * 充值档次分布
     */
    private String payLevelSqlStr(String table, String serverId, String channelNames, String startDate, String endDate) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT rcl.totalFee FROM " + table + " rcl ");
        str.append("WHERE FROM_UNIXTIME(rcl.time) BETWEEN '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59' AND sid in ('" + serverId + "')");
        if (!Strings.isBlank(channelNames)) {
            str.append(" and rcl.platformName in (" + channelNames + ") ");
        }
        str.append("GROUP BY rcl.totalFee ");
        return str.toString();
    }

    private String getPayUserSqlStr(String table, String serverId, String channelNames, String startDate, String endDate) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT rcl.totalFee,rcl.userId FROM " + table + " rcl ");
        str.append("WHERE FROM_UNIXTIME(rcl.time) BETWEEN '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59' AND sid in ('" + serverId + "')");
        if (!Strings.isBlank(channelNames)) {
            str.append(" and rcl.platformName in (" + channelNames + ") ");
        }
        return str.toString();
    }

    /**
     * 每日充值金额分布
     */
    private String payDaySqlStr(String table, String serverId, String channelNames, String startDate) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT rcl.totalFee,HOUR(FROM_UNIXTIME(rcl.time)) timeHour,rcl.userId,rcl.currency FROM " + table + " rcl ");
        str.append("WHERE FROM_UNIXTIME(rcl.time) BETWEEN '" + startDate + " 00:00:00' AND '" + startDate + " 23:59:59' AND sid in ('" + serverId + "')");
        if (!Strings.isBlank(channelNames)) {
            str.append(" and rcl.platformName in (" + channelNames + ") ");
        }
        return str.toString();
    }

}






