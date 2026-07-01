package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.filter.MenuFilter;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.QueryUtil;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@IocBean
@Ok("json")
@At("/itemChange")
@Fail("http:500")
public class itemChangeStatisticModule {

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @At
    @Ok("jsp:jsp.statistic.itemChange")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index(HttpServletRequest request) {
        BackendLogUtil.getInstance().log(request, "进入物品、货币流向统计页面");
        Map<String, String> langMap = Mvcs.getMessages(Mvcs.getReq());
        Map<Integer, String> reasonMap = new TreeMap<>();
        for (String key : langMap.keySet()) {
            if (key.contains("reason.itemChange")) {
                String[] reasons = key.split("_");
                int reasonNum = Integer.parseInt(reasons[1]);
                String reason = langMap.get(key);
                reasonMap.put(reasonNum, reason);
            }
        }
        request.setAttribute("reasonMap", reasonMap);
        request.setAttribute("nowDate", sdf.format(new Date()));
    }


    /**
     * 获取消耗和产出
     */
    @At
    public Object getConsumeAndOutput(String groupName, String channelNames, String startDate, String endDate, String serverIds, String reasons, String itemIds, boolean isBlack) throws Exception {
        return getData("getConsumeAndOutput", groupName, channelNames, startDate, endDate, serverIds, reasons, itemIds, isBlack);
    }

    /**
     * 获取消耗
     */
    @At
    public Object getConsume(String groupName, String channelNames, String startDate, String endDate, String serverIds, String reasons, String itemIds, boolean isBlack) throws Exception {
        return getData("getConsume", groupName, channelNames, startDate, endDate, serverIds, reasons, itemIds, isBlack);
    }

    /**
     * 获取产出
     */
    @At
    public Object getOutput(String groupName, String channelNames, String startDate, String endDate, String serverIds, String reasons, String itemIds, boolean isBlack) throws Exception {
        return getData("getOutput", groupName, channelNames, startDate, endDate, serverIds, reasons, itemIds, isBlack);
    }

    /**
     * 获取数据
     *
     * @param sign 不同类型数据标识
     */
    private Map<String, Object> getData(String sign, String groupName, String channelNames, String startDate, String endDate, String serverIds, String reasons, String itemIds, boolean isBlack) throws Exception {
        Map<String, Object> dataMap = new TreeMap<>();
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
        List<String> goalTables;
        Map<String, List<String>> hefuTableMap;
        List<Map<String, Object>> itemdataMapList;
        String[] itemId = itemIds.split(";");
        for (String s : itemId) {
            String tableSign = getTableName(s);
            itemdataMapList = new ArrayList<>();
            String[] serverId = serverIds.split(",");
            for (String value : serverId) {
                goalTables = QueryUtil.getInstance().getQueryTables(tableSign, TableType.Month, startDate, endDate);
                hefuTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(value, tableSign, sdf.parse(startDate), sdf.parse(endDate)));
                for (String key : hefuTableMap.keySet()) {
                    List<String> tableList = hefuTableMap.get(key);
                    tableList.retainAll(goalTables);//过滤重复数据表
                    for (String item : tableList) {
                        Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                        if (dblog == null) {
                            continue;
                        }
                        String sqlStr = "";
                        switch (sign) {
                            case "getConsumeAndOutput":
                                sqlStr = getConsumeAndOutputSql(item, value, tableSign, s, channelNames, startDate, endDate, blackUserStr, reasons);
                                break;
                            case "getConsume":
                                sqlStr = getConsumeByReasonSql(item, value, tableSign, s, channelNames, startDate, endDate, blackUserStr, reasons);
                                break;
                            case "getOutput":
                                sqlStr = getOutputByReasonSql(item, value, tableSign, s, channelNames, startDate, endDate, blackUserStr, reasons);
                                break;
                        }

                        List<Map<String, Object>> dataMapList = QueryUtil.getInstance().query(dblog, sqlStr);
                        if (sign.equals("getConsume") || sign.equals("getOutput")) {//解析取出来的原因
                            getResult(itemdataMapList, dataMapList, "reason");
                        } else {
                            getResult(itemdataMapList, dataMapList, "day");
                        }
                    }
                }
            }
            //TODO 修改item报错
//			List<ItemInfo> itemList = ItemManager.getInstance().getItemList();
//			String itemName = "";
//			for(ItemInfo item : itemList){
//				if(item.getItemId() == Integer.parseInt(itemId[m])){
//					itemName = Mvcs.getMessage(Mvcs.getReq(), item.getItemName());
//				}
//			}
//			dataMap.put(itemName, itemdataMapList);
        }
        return dataMap;
    }


    /**
     * 根据item的类型获取不同的日志变化表
     */
    private String getTableName(String item) {
        String str;
        switch (item) {
            case "2":
                str = "moneychangelog";
                break;
            case "3":
                str = "goldchangelog";
                break;
            case "4":
                str = "bindgoldchangelog";
                break;
            case "8":
                str = "expchangelog";
                break;
            case "9":
                str = "physicalstrendthchangelog";
                break;
            case "5":
            case "6":
            case "7":
            case "10":
            case "11":
            case "12":
            case "13":
            case "14":
            case "15":
            case "16":
            case "17":
            case "18":
            case "19":
            case "20":
            case "21":
            case "22":
            case "23":
            case "26":
            case "27":
            case "28":
            case "29":
                str = "coinchangelog";
                break;
            case "24":
            case "25":
                str = "zhenqiandtiechangelog";
                break;
            default:
                str = "itemchangelog";
                break;
        }
        return str;
    }

    private void getResult(List<Map<String, Object>> itemdataMapList, List<Map<String, Object>> dataMapList, String param) {
        //由于存在合服以及多服的选择，所以这里处理时，把多个服务器的按天产出累加以及消耗累加
        for (Map<String, Object> map : dataMapList) {
            boolean isHaveDay = false;//判断是否这一天已经存在在itemdataMapList里面了
            if (!itemdataMapList.isEmpty()) {
                for (Map<String, Object> itemMap : itemdataMapList) {
                    if (map.get(param).toString().equals(itemMap.get(param).toString())) {
                        isHaveDay = true;
                        if (map.containsKey("output")) {
                            itemMap.put("output", getSum(map.get("output").toString(), itemMap.get("output").toString()));
                        }
                        if (map.containsKey("consume")) {
                            itemMap.put("consume", getSum(map.get("consume").toString(), itemMap.get("consume").toString()));
                        }
                    }
                }
            }
            if (!isHaveDay) {
                itemdataMapList.add(map);
            }
        }
    }

    //两个对象求和
    private Object getSum(Object r, Object s) {
        if (r == null && s != null) {
            return Long.parseLong(s.toString());
        } else if (r != null && s == null) {
            return Long.parseLong(r.toString());
        } else if (r == null && s == null) {
            return null;
        } else {
            return Long.parseLong(r.toString()) + Long.parseLong(s.toString());
        }
    }

    /*
     * 获取产出消耗的sql
     */
    private String getConsumeAndOutputSql(String table, String serverId, String tableSign, String itemType, String channelNames, String startDate, String endDate, String blackUsers, String reasons) {
        String sqlStr = "SELECT DATE_FORMAT(FROM_UNIXTIME(TIME), '%Y-%m-%d') AS day, SUM(output) AS output, ABS(SUM(consume)) AS consume";
        sqlStr += " FROM (SELECT TIME, SUM(changenum) AS output,0 AS consume";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE changenum > 0 AND TIME between UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 23:59:59') AND sid in ('" + serverId + "')";
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND platformName IN (" + channelNames + ")";
        }
        if (!Strings.isBlank(blackUsers)) {
            sqlStr += " AND userId NOT IN (" + blackUsers + ")";
        }
        if (tableSign.equals("goldchangelog") || tableSign.equals("expchangelog")) {
            sqlStr += "";
        } else if (tableSign.equals("itemchangelog")) {
            sqlStr += " AND modelid  IN (" + itemType + ")";
        } else {
            sqlStr += " AND monyeyType IN (" + itemType + ")";
        }
        if (!Strings.isBlank(reasons)) {
            sqlStr += " AND reason IN (" + reasons + ")";
        }
        sqlStr += " GROUP BY DATE_FORMAT(FROM_UNIXTIME(TIME), '%Y-%m-%d') UNION ALL";
        sqlStr += " SELECT TIME,0 AS output, SUM(changenum) AS consume";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE changenum < 0 AND TIME between UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 23:59:59') AND sid in ('" + serverId + "')";
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND platformName IN (" + channelNames + ")";
        }
        if (!Strings.isBlank(blackUsers)) {
            sqlStr += " AND userId NOT IN (" + blackUsers + ")";
        }
        if (tableSign.equals("goldchangelog") || tableSign.equals("expchangelog")) {
            sqlStr += "";
        } else if (tableSign.equals("itemchangelog")) {
            sqlStr += " AND modelid  IN (" + itemType + ")";
        } else {
            sqlStr += " AND monyeyType IN (" + itemType + ")";
        }
        if (!Strings.isBlank(reasons)) {
            sqlStr += " AND reason IN (" + reasons + ")";
        }
        sqlStr += " GROUP BY DATE_FORMAT(FROM_UNIXTIME(TIME), '%Y-%m-%d')) t";
        sqlStr += " GROUP BY DATE_FORMAT(FROM_UNIXTIME(TIME), '%Y-%m-%d')";
        return sqlStr;
    }

    /*
     * 获取产出根据原因分组
     */
    private String getOutputByReasonSql(String table, String serverId, String tableSign, String itemType, String channelNames, String startDate, String endDate, String blackUsers, String reasons) {
        String sqlStr = " SELECT SUM(changenum) AS output,reason";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE changenum > 0 AND TIME between UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 23:59:59') AND sid in ('" + serverId + "')";
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND platformName IN (" + channelNames + ")";
        }
        if (!Strings.isBlank(blackUsers)) {
            sqlStr += " AND userId NOT IN (" + blackUsers + ")";
        }
        if (tableSign.equals("goldchangelog") || tableSign.equals("expchangelog")) {
            sqlStr += "";
        } else if (tableSign.equals("itemchangelog")) {
            sqlStr += " AND modelid  IN (" + itemType + ")";
        } else {
            sqlStr += " AND monyeyType IN (" + itemType + ")";
        }
        if (!Strings.isBlank(reasons)) {
            sqlStr += " AND reason IN (" + reasons + ")";
        }
        sqlStr += " GROUP BY reason";
        return sqlStr;
    }

    /*
     * 获取消耗根据原因分组
     */
    private String getConsumeByReasonSql(String table, String serverId, String tableSign, String itemType, String channelNames, String startDate, String endDate, String blackUsers, String reasons) {
        String sqlStr = " SELECT ABS(SUM(changenum)) AS consume,reason";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE changenum < 0 AND TIME between UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 23:59:59') AND sid in ('" + serverId + "')";
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND platformName IN (" + channelNames + ")";
        }
        if (!Strings.isBlank(blackUsers)) {
            sqlStr += " AND userId NOT IN (" + blackUsers + ")";
        }
        if (tableSign.equals("goldchangelog") || tableSign.equals("expchangelog")) {
            sqlStr += "";
        } else if (tableSign.equals("itemchangelog")) {
            sqlStr += " AND modelid  IN (" + itemType + ")";
        } else {
            sqlStr += " AND monyeyType IN (" + itemType + ")";
        }
        if (!Strings.isBlank(reasons)) {
            sqlStr += " AND reason IN (" + reasons + ")";
        }
        sqlStr += " GROUP BY reason";
        return sqlStr;
    }
}
