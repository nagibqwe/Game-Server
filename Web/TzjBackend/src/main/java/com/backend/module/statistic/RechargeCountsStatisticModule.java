package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.filter.MenuFilter;
import com.backend.struct.log.TableName;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.utils.QueryUtil;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@IocBean
@Ok("json")
@At("/rechargeCounts")
@Fail("http:500")
public class RechargeCountsStatisticModule {

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @At
    @Ok("jsp:jsp.statistic.rechargeCounts")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index(HttpServletRequest request) {
        request.setAttribute("newDate", sdf.format(new Date()));
    }

    @At
    public Object getRechargeCounts(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isBlack) throws Exception {
        List<Map<String, Object>> rechargeCountsMap = new ArrayList<>();
        List<Map<String, Object>> rechargeCountsMapList = new ArrayList<>();
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
        for (String item : serverId) {
            String RECHARGESUCCESSLOG = TableName.RechargeSuccess;
            goalTables = QueryUtil.getInstance().getQueryTables(RECHARGESUCCESSLOG, TableType.No, startDate+" 00:00:00", endDate+" 23:59:59");
            hefuTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(item, RECHARGESUCCESSLOG, sdf.parse(startDate), sdf.parse(endDate)));
            for (String key : hefuTableMap.keySet()) {
                List<String> tableList = hefuTableMap.get(key);
                tableList.retainAll(goalTables);//过滤重复数据表
                for (String s : tableList) {
                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    if (dblog == null) {
                        continue;
                    }
                    String sqlStr = getRechargeCountsSql(s, item, channelNames, startDate, endDate, blackUserStr);
                    List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                    getResult(rechargeCountsMapList, dataMap);
                }
            }
        }

        //处理得到的数据
        int[] count = new int[]{1, 2, 5, 10, 20, 30, 31};
        Map<Integer, Float> rechargeTimesMap = new TreeMap<>();//付费次数与人数的map
        Map<Integer, Float> rechargeAmountsMap = new TreeMap<>();//付费次数与付费总额的map
        for (int value : count) {
            rechargeTimesMap.put(value, 0f);
            rechargeAmountsMap.put(value, 0f);
        }
        if (rechargeCountsMapList.size() != 0) {
            for (Map<String, Object> rechargeMap : rechargeCountsMapList) {
                float rechargeCount = Float.parseFloat(rechargeMap.get("counts").toString());
                float amount = Float.parseFloat(rechargeMap.get("amount").toString());
                for (int i = 0; i < count.length; i++) {
                    float roleCounts = 0;
                    float payment;
                    if (i == 0 || i == 1 || i == (count.length - 1)) {
                        if (rechargeCount == count[i]) {
                            roleCounts++;
                            if (rechargeTimesMap.containsKey(count[i])) {
                                roleCounts += rechargeTimesMap.get(count[i]);
                            }
                            payment = amount;
                            if (rechargeAmountsMap.containsKey(count[i])) {
                                payment = rechargeAmountsMap.get(count[i]) + amount;
                            }
                            rechargeTimesMap.put(count[i], roleCounts);
                            rechargeAmountsMap.put(count[i], payment);
                        }
                    } else {
                        if (rechargeCount <= count[i] && rechargeCount > count[i - 1]) {
                            roleCounts++;
                            if (rechargeTimesMap.containsKey(count[i])) {
                                roleCounts += rechargeTimesMap.get(count[i]);
                            }
                            payment = amount;
                            if (rechargeAmountsMap.containsKey(count[i])) {
                                payment = rechargeAmountsMap.get(count[i]) + amount;
                            }
                            rechargeTimesMap.put(count[i], roleCounts);
                            rechargeAmountsMap.put(count[i], payment);
                        }
                    }
                }

            }

            for (int key1 : rechargeTimesMap.keySet()) {
                Map<String, Object> rechargeTimesResMap = new TreeMap<>();//付费次数与人数最终的map
                String rechargeTimes = getRechargeTimesName(key1);
                float rechargeRoles = rechargeTimesMap.get(key1);
                for (int key2 : rechargeAmountsMap.keySet()) {
                    float rechargeAmounts = rechargeAmountsMap.get(key2);
                    if (key1 == key2) {
                        rechargeTimesResMap.put("rechargeTimes", rechargeTimes);
                        rechargeTimesResMap.put("rechargeRoles", rechargeRoles);
                        rechargeTimesResMap.put("rechargeAmounts", rechargeAmounts);
                        rechargeCountsMap.add(rechargeTimesResMap);
                        break;
                    }
                }
            }

        }
        return rechargeCountsMap;
    }

    private String getRechargeTimesName(int counts) {
        String rechargeTimesName = "";
        switch (counts) {
            case 1:
                rechargeTimesName = "1次";
                break;
            case 2:
                rechargeTimesName = "2次";
                break;
            case 5:
                rechargeTimesName = "3~5次";
                break;
            case 10:
                rechargeTimesName = "6~10次";
                break;
            case 20:
                rechargeTimesName = "11~20次";
                break;
            case 30:
                rechargeTimesName = "21~30次";
                break;
            case 31:
                rechargeTimesName = "31次以上";
                break;
        }
        return rechargeTimesName;
    }

    private void getResult(List<Map<String, Object>> itemdataMapList, List<Map<String, Object>> dataMapList) {
        //由于存在合服以及多服的选择，所以这里处理时，把多个服务器的按天产出累加以及消耗累加
        for (Map<String, Object> map : dataMapList) {
            boolean isHaveDay = false;//判断是否这一天已经存在在itemdataMapList里面了
            if (!itemdataMapList.isEmpty()) {
                for (Map<String, Object> itemMap : itemdataMapList) {
                    if (map.get("userId").toString().equals(itemMap.get("userId").toString())) {
                        isHaveDay = true;
                        if (map.containsKey("counts")) {
                            itemMap.put("counts", getSum(map.get("counts").toString(), itemMap.get("counts").toString()));
                        }
                        if (map.containsKey("amount")) {
                            itemMap.put("amount", getSum(map.get("amount").toString(), itemMap.get("amount").toString()));
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
            return Float.parseFloat(s.toString());
        } else if (r != null && s == null) {
            return Float.parseFloat(r.toString());
        } else if (r == null && s == null) {
            return null;
        } else {
            return Float.parseFloat(r.toString()) + Float.parseFloat(s.toString());
        }
    }

    private String getRechargeCountsSql(String table, String serverId, String channelNames, String startDate, String endDate, String blackUsers) {
        String sqlStr = " SELECT count(*) as counts,sum(totalFee) as amount,userId";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE TIME between UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 23:59:59') ";
        sqlStr += " AND status=1 AND statusReason=7 AND sid in ('" + serverId + "')";
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND platformName IN (" + channelNames + ")";
        }
        if (!Strings.isBlank(blackUsers)) {
            sqlStr += " AND userId NOT IN (" + blackUsers + ")";
        }
        sqlStr += " GROUP BY userId";
        return sqlStr;
    }
}
