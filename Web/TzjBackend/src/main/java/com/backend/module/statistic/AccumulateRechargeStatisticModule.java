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
@At("/accumulateRecharge")
@Fail("http:500")
public class AccumulateRechargeStatisticModule {

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static String RECHARGESUCCESSLOG = TableName.RechargeSuccess;

    @At
    @Ok("jsp:jsp.statistic.accumulateRecharge")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index(HttpServletRequest request) {
        request.setAttribute("newDate", sdf.format(new Date()));
    }

    @At
    public Object getAccumulateRecharge(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isBlack) throws Exception {
        Map<String, Object> accumulateRechargeMap = new LinkedHashMap<>();
        List<Map<String, Object>> accumulateRechargeMapList = new ArrayList<>();
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
        for (String s : serverId) {
            goalTables = QueryUtil.getInstance().getQueryTables(RECHARGESUCCESSLOG, TableType.No, startDate+" 00:00:00", endDate+" 23:59:59");
            hefuTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(s, RECHARGESUCCESSLOG, sdf.parse(startDate), sdf.parse(endDate)));
            for (String key : hefuTableMap.keySet()) {
                List<String> tableList = hefuTableMap.get(key);
                tableList.retainAll(goalTables);//过滤重复数据表
                for (String value : tableList) {
                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    if (dblog == null) {
                        continue;
                    }
                    String sqlStr = getAccumulateRechargeSql(value, s, channelNames, startDate, endDate, blackUserStr);
                    List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                    for (Map<String, Object> map : dataMap) {
                        System.out.println(map.get("userId"));
                        System.out.println(map.get("amount"));
                    }
                    getResult(accumulateRechargeMapList, dataMap);
                }
            }
        }
        int[] amountValue = new int[]{0, 10, 20, 30, 70, 150, 300, 500, 1000, 1500, 2000, 5000, 10000};
        Map<Integer, String> rechargeAmountMap = new TreeMap<>();
        for (int value : amountValue) {
            rechargeAmountMap.put(value, "0,0");
        }
        if (accumulateRechargeMapList.size() != 0) {
            for (Map<String, Object> map : accumulateRechargeMapList) {
                double amount = Float.parseFloat(map.get("amount").toString());
                for (int i = 0; i < amountValue.length; i++) {
                    int count = 0;
                    float sum = 0;
                    if (i != (amountValue.length - 1)) {
                        if (amount > amountValue[i] && amount <= amountValue[i + 1]) {
                            count++;
                            sum += amount;
                            if (rechargeAmountMap.containsKey(amountValue[i])) {
                                count += Integer.parseInt(rechargeAmountMap.get(amountValue[i]).substring(0, rechargeAmountMap.get(amountValue[i]).indexOf(",")));
                                sum += Float.parseFloat(rechargeAmountMap.get(amountValue[i]).substring(rechargeAmountMap.get(amountValue[i]).indexOf(",") + 1));
                            }
                            rechargeAmountMap.put(amountValue[i], count + "," + sum);
                        }
                    } else {
                        if (amount > amountValue[i]) {
                            count++;
                            sum += amount;
                            if (rechargeAmountMap.containsKey(amountValue[i])) {
                                count += Integer.parseInt(rechargeAmountMap.get(amountValue[i]).substring(0, rechargeAmountMap.get(amountValue[i]).indexOf(",")));
                                sum += Float.parseFloat(rechargeAmountMap.get(amountValue[i]).substring(rechargeAmountMap.get(amountValue[i]).indexOf(",") + 1));
                            }
                            rechargeAmountMap.put(amountValue[i], count + "," + sum);
                        }
                    }
                }
            }
            for (int key : rechargeAmountMap.keySet()) {
                String amountName = getAmountName(key);
                accumulateRechargeMap.put(amountName, rechargeAmountMap.get(key));
            }
        }
        //System.out.println("accumulateRechargeMap====="+accumulateRechargeMap.toString());
        return accumulateRechargeMap;
    }

    private void getResult(List<Map<String, Object>> itemdataMapList, List<Map<String, Object>> dataMapList) {
        //由于存在合服以及多服的选择，所以这里处理时，把多个服务器的按天产出累加以及消耗累加
        for (Map<String, Object> map : dataMapList) {
            boolean isHaveDay = false;//判断是否这一天已经存在在itemdataMapList里面了
            if (!itemdataMapList.isEmpty()) {
                for (Map<String, Object> itemMap : itemdataMapList) {
                    if (map.get("userId").toString().equals(itemMap.get("userId").toString())) {
                        isHaveDay = true;
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

    private String getAmountName(int amount) {
        String amountName = "";
        switch (amount) {
            case 0:
                amountName = "0~10(大于0，小于等于10)";
                break;
            case 10:
                amountName = "10~20(大于10，小于等于20)";
                break;
            case 20:
                amountName = "20~30(大于20，小于等于30)";
                break;
            case 30:
                amountName = "30~70(大于30，小于等于70)";
                break;
            case 70:
                amountName = "70~150(大于70，小于等于150)";
                break;
            case 150:
                amountName = "150~300(大于150，小于等于300)";
                break;
            case 300:
                amountName = "300~500(大于300，小于等于500)";
                break;
            case 500:
                amountName = "500~1000(大于500，小于等于1000)";
                break;
            case 1000:
                amountName = "1000~1500(大于1000，小于等于1500)";
                break;
            case 1500:
                amountName = "1500~2000(大于1500，小于等于2000)";
                break;
            case 2000:
                amountName = "2000~5000(大于2000，小于等于5000)";
                break;
            case 5000:
                amountName = "5000~10000(大于5000，小于等于10000)";
                break;
            case 10000:
                amountName = ">10000(大于10000)";
                break;
        }
        return amountName;
    }

    private String getAccumulateRechargeSql(String table, String serverId, String channelNames, String startDate, String endDate, String blackUsers) {
        String sqlStr = "SELECT sum(totalFee) as amount,userId";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE TIME between UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 23:59:59') ";
        sqlStr += " AND status=1 AND statusReason=7 AND sid IN ('" + serverId + "')";
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND platformName IN (" + channelNames + ")";
        }
        if (!blackUsers.equals("")) {
            sqlStr += " AND userId NOT IN (" + blackUsers + ")";
        }
        sqlStr += " GROUP BY userId";
        return sqlStr;
    }
}
