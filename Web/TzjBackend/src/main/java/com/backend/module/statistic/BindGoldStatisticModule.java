package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.filter.MenuFilter;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.utils.ListToStringUtil;
import com.backend.utils.QueryUtil;
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
@At("/bindgold")
@Fail("http:500")
public class BindGoldStatisticModule {
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static String SHOPBUYLOG = "shopbuylog";

    @At("/")
    @Ok("jsp:jsp.statistic.bindgold")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index(HttpServletRequest request) {
        request.setAttribute("nowDate", sdf.format(new Date()));
    }

    /**
     * 对元宝的走向进行统计
     */
    @At
    public Object bindGoldInfo(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isBlack) throws Exception {

        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }

        List<String> shopGoalTables = QueryUtil.getInstance().getQueryTables(SHOPBUYLOG, TableType.Month, startDate, endDate);
        Map<String, List<String>> hefuTableMap = new HashMap<>();
        //黑名单
        String blackUsers = "";
        if (isBlack) {
            List<Object> blackList = BlackListManager.getInstance().getBlackListUsers(groupName.trim());
            if (!blackList.isEmpty()) {
                blackUsers = ListToStringUtil.listToString(blackList);
            }
        }
        for (String id : serverId) {
            hefuTableMap.putAll(QueryUtil.getInstance().getHefuTable(id, SHOPBUYLOG, sdf.parse(startDate), sdf.parse(endDate)));
        }
        Map<String, Object> shopBuyLogMap = new HashMap<>();
        for (String key : hefuTableMap.keySet()) {
            List<String> tableList = hefuTableMap.get(key);
            tableList.retainAll(shopGoalTables);//过滤重复数据表
            for (String s : tableList) {
                String sqlStr = getShopBuyItemSql(key, s, channelNames, startDate, endDate, blackUsers);
                Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                if (dblog == null) {
                    continue;
                }
                List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                for (Map<String, Object> map : dataMap) {
                    String itemModelId = map.get("itemModelId").toString();
                    //TODO 修改item报错
//					ItemInfo item = ItemManager.getInstance().getItem(Integer.parseInt(itemModelId));
//					map.put("itemName", Mvcs.getMessage(Mvcs.getReq(),item.getItemName()));
//					String mapkey = itemModelId + "_" + map.get("sid").toString();
//					if(shopBuyLogMap.containsKey(mapkey)){
//						String itemSumStr = map.get("itemSum").toString();
//						String itemSumDataStr = shopBuyLogMap.get("itemSum").toString();
//						String itemCountStr = map.get("itemCount").toString();
//						String itemCountDataStr = shopBuyLogMap.get("itemCount").toString();
//						String userCountStr = map.get("userCount").toString();
//						String userCountDataStr = shopBuyLogMap.get("userCount").toString();
//						int itemSum = Integer.parseInt(itemSumStr) + Integer.parseInt(itemSumDataStr);
//						int itemCount = Integer.parseInt(itemCountStr) + Integer.parseInt(itemCountDataStr);
//						int userCount = Integer.parseInt(userCountStr) + Integer.parseInt(userCountDataStr);
//						shopBuyLogMap.put("itemSum", itemSum);
//						shopBuyLogMap.put("itemCount", itemCount);
//						shopBuyLogMap.put("userCount", userCount);
//						continue;
//					}
//					shopBuyLogMap.put(mapkey, map);
                }
            }
        }
        return shopBuyLogMap;
    }

    /**
     * 对元宝统计进行汇总
     */
    @At
    public Object bindGoldStatis(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isBlack) throws ParseException {

        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }

        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        List<Date> dateList = new ArrayList<>();
        int dvalue = dValue(start, end);
        long oneday = 1000 * 60 * 60 * 24;
        long dateTime = start.getTime();
        Date date;
        for (int i = 0; i < dvalue + 1; i++) {
            date = new Date(dateTime);
            dateList.add(date);
            dateTime += oneday;
        }
        List<String> shopGoalTables = QueryUtil.getInstance().getQueryTables(SHOPBUYLOG, TableType.Month, startDate, endDate);
        Map<String, List<String>> hefuTableMap = new HashMap<>();
        //黑名单
        String blackUsers = "";
        if (isBlack) {
            List<Object> blackList = BlackListManager.getInstance().getBlackListUsers(groupName.trim());
            if (!blackList.isEmpty()) {
                blackUsers = ListToStringUtil.listToString(blackList);
            }
        }
        for (String id : serverId) {
            hefuTableMap.putAll(QueryUtil.getInstance().getHefuTable(id, SHOPBUYLOG, sdf.parse(startDate), sdf.parse(endDate)));
        }
        List<Map<String, Object>> shopBuyLogMap = new ArrayList<>();
        for (String key : hefuTableMap.keySet()) {
            List<String> tableList = hefuTableMap.get(key);
            tableList.retainAll(shopGoalTables);//过滤重复数据表
            for (String s : tableList) {
                String sqlStr = getShopBuyDateSql(key, s, channelNames, startDate, endDate, blackUsers);
                Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                if (dblog == null) {
                    continue;
                }
                List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                shopBuyLogMap.addAll(dataMap);
            }
        }
        return shopBuyLogMap;
    }

    private String getShopBuyItemSql(String serverId, String table, String channelNames, String startDate, String endDate, String blackUsers) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT COUNT(s.userId) userCount,s.itemModelId,s.sid,SUM(s.buyTimes) itemCount,SUM(s.moneyNum) itemSum ");
        str.append("FROM (SELECT sl.userId,sl.itemModelId,sl.sid,sl.buyTimes,sl.moneyNum ");
        str.append("FROM " + table + " sl WHERE sl.moneyType = 4 AND sl.sid IN (" + serverId + ") ");
        str.append("AND sl.time >= UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND sl.time <= UNIX_TIMESTAMP('" + endDate + " 23:59:59') ");
        if (blackUsers != null && !blackUsers.equals("")) {
            str.append("AND sl.userId NOT IN (");
            str.append(blackUsers + ")");
        }
        if (!Strings.isBlank(channelNames)) {
            str.append(" AND sl.platformName in (" + channelNames + ")");
        }
        str.append(") S GROUP BY S.itemModelId");
        return str.toString();
    }

    private String getShopBuyDateSql(String serverId, String table, String channelNames, String startDate, String endDate, String blackUsers) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT COUNT(s.userId) userCount,s.sid,SUM(s.buyTimes) timeSum,SUM(s.moneyNum) moneySum,s.rtime FROM(");
        str.append("SELECT sl.userId,sl.sid,sl.buyTimes,sl.moneyNum,DATE_FORMAT(FROM_UNIXTIME(sl.time),'%Y-%m-%d') rtime ");
        str.append("FROM " + table + " sl WHERE sl.moneyType = 4 AND sl.sid IN (" + serverId + ") ");
        str.append("AND sl.time >= UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND sl.time <= UNIX_TIMESTAMP('" + endDate + " 23:59:59') ");
        if (blackUsers != null && blackUsers != "") {
            str.append("AND sl.userId NOT IN (");
            str.append(blackUsers + ")");
        }
        if (!Strings.isBlank(channelNames)) {
            str.append(" AND sl.platformName in (" + channelNames + ")");
        }
        str.append(") S GROUP BY S.rtime");
        return str.toString();
    }

    /**
     * 计算起始天数的差值
     */
    private int dValue(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

}







