package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.struct.log.TableName;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.QueryUtil;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@IocBean
@Ok("json")
@At("/plstatistic")
@Fail("http:500")
public class PlyLveStatisticModule {
    private static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat sdfhm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @At("/")
    @Ok("jsp:jsp.statistic.playerLeave")

    public void index(HttpServletRequest request) {
        BackendLogUtil.getInstance().log(request, "进入每日登录情况统计界面");
        request.setAttribute("nowDate", sdf.format(new Date()));
    }

    /**
     * 统计选择日期内每日登陆用户，流失用户（超过一天未登录且截止日期内都未登录的），流失付费用户
     */
    @At
    public Object playerLeaveCount(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isblack,
                                   String select_condition, String minPay, String maxPay) throws Exception {
        String judge = "playerLeaveCount";
        return getDataList(judge, groupName, serverId, channelNames, startDate, endDate, isblack, select_condition, minPay, maxPay);

    }

    /**
     * 获取流失用户的角色等级分布
     */
    @At
    public Object playerLeaveRank(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isblack,
                                  String select_condition, String minPay, String maxPay) throws Exception {
        String judge = "playerLeaveRank";
        return getDataList(judge, groupName, serverId, channelNames, startDate, endDate, isblack, select_condition, minPay, maxPay);
    }

    /**
     * 流失用户累积充值
     */
    @At
    public Object playerLeaveAmount(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isblack,
                                    String select_condition, String minPay, String maxPay) throws Exception {
        String judge = "playerLeaveAmount";
        return getDataList(judge, groupName, serverId, channelNames, startDate, endDate, isblack, select_condition, minPay, maxPay);
    }

    //获取数据
    private List<Map<String, Object>> getDataList(String judge, String groupName, String serverId[], String channelNames,
                                                  String startDate, String endDate, boolean isblack, String selectCondition, String minPay, String maxPay) throws Exception {
        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        //查询选择日期内的每日登陆用户总数
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        List<Date> dateList = new ArrayList<>();
        int dvalue = dValue(start, end);
        long oneday = 1000 * 60 * 60 * 24;
        long dateTime = start.getTime();
        String endTime = endDate + " 23:59:59";
        Date date;
        for (int i = 0; i < dvalue + 1; i++) {
            date = new Date(dateTime - oneday);
            dateList.add(date);
            dateTime += oneday;
        }
//        Dao loginDao = LoginServerManager.getInstance().getLoginDao();
//        if (loginDao == null) {
//            return null;
//        }

        List<Map<String, Object>> dataList = new ArrayList<>();
        //（检查合服）
        Map<String, List<String>> allUserhefuTableMap = new HashMap<>();
        for (String sid : serverId) {
            allUserhefuTableMap.putAll(QueryUtil.getInstance().getHefuTable(sid, TableName.RoleLogin, start, end));
        }

        List<String> loginGoalTables = QueryUtil.getInstance().getQueryTables(TableName.RoleLogin, TableType.Month, startDate+" 00:00:00", endDate+" 23:59:59");
        //得到统计时间内的所有用户数据
        List<Map<String, Object>> allUserDataList = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry:allUserhefuTableMap.entrySet()) {
            //统计普通用户流失
            Dblog dblog = DbLogListManager.getInstance().getDblog(entry.getKey());
            if (dblog == null) {
                continue;
            }
            loginGoalTables.retainAll(entry.getValue());
            for (String loginGoalTable : loginGoalTables) {
                String getLoginAllSql = getUserSql(loginGoalTable, "", channelNames, startDate, endDate, "", "", "", null);
                allUserDataList.addAll(QueryUtil.getInstance().query(dblog, getLoginAllSql));
            }
        }

        String dateeStr = getCalDate(endDate);
        //（检查合服）
        List<String> rechargeGoalTables = QueryUtil.getInstance().getQueryTables(TableName.RechargeSuccess, TableType.No, startDate+" 00:00:00", endDate+" 23:59:59");
        //得到统计时间内的所有付费用户数据
        List<Map<String, Object>> rechargeDataMap = new ArrayList<>();
        if ("1".equals(selectCondition) || "playerLeaveRank".equals(judge) || "playerLeaveAmount".equals(judge)) {
            for (String sid : serverId) {
                Map<String, List<String>> hefuTableMap = QueryUtil.getInstance().getHefuTable(sid, TableName.RechargeSuccess, sdf.parse(startDate), sdf.parse(dateeStr));

                for (String key : hefuTableMap.keySet()) {
                    List<String> tableList = hefuTableMap.get(key);
                    tableList.retainAll(rechargeGoalTables);//过滤重复数据表
                    for (String t : tableList) {
                        String sqlStr = getUserSql(t, sid, channelNames, "", "", minPay, maxPay, TableName.RechargeSuccess, allUserDataList);
                        Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                        if (dblog == null) {
                            continue;
                        }
                        List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                        //由于存在合服以及多服的选择，所以这里处理时，把多个服务器的按天产出累加以及消耗累加
                        for (Map<String, Object> map : dataMap) {
                            boolean isHaveDay = false;//判断是否这一天已经存在在itemdataMapList里面了
                            if (!rechargeDataMap.isEmpty()) {
                                for (Map<String, Object> itemMap : rechargeDataMap) {
                                    if (map.get("userId").toString().equals(itemMap.get("userId").toString()) && map.get("date").toString().equals(itemMap.get("date").toString())) {
                                        isHaveDay = true;
                                    }
                                }
                            }
                            if (!isHaveDay) {
                                rechargeDataMap.add(map);
                            }
                        }
                    }
                }
            }
        }

        //踢出黑名单中的数据
        if (isblack) {
            List<Map<String, Object>> blackAllData = new ArrayList<>();
            List<Map<String, Object>> blackRechargeData = new ArrayList<>();
            List<Map<String, Object>> blackList = BlackListManager.getInstance().getBlackList(groupName);
            for (Map<String, Object> stringObjectMap : blackList) {
                //所有玩家的数据
                String userId = stringObjectMap.get("userId").toString();
                for (Map<String, Object> loginUserMap : allUserDataList) {
                    String userid = loginUserMap.get("userId").toString();
                    if (userId.equals(userid)) {
                        blackAllData.add(loginUserMap);
                    }
                }
                //充值玩家
                for (Map<String, Object> rechargeUserMap : rechargeDataMap) {
                    String userid = rechargeUserMap.get("userId").toString();
                    if (userId.equals(userid)) {
                        blackRechargeData.add(rechargeUserMap);
                    }
                }
            }
            allUserDataList.removeAll(blackAllData);
            rechargeDataMap.removeAll(blackRechargeData);
        }
        Set<String> ordUserIdList = new HashSet<>();//存放普通流失用户的userid
        Set<String> vipUserIdList = new HashSet<>();//存放付费流失用户的userid
        String vipUserId = "";//转换为string的付费流失用户
        for (Date d : dateList) {
            String dates = sdf.format(d) + " 00:00:00";
            String daten = sdf.format(d) + " 23:59:59";
            String datess = sdf.format(d.getTime() + oneday) + " 00:00:00";
            /*
             * 分别获取统计日的登录玩家和第二天未登录且到截止日都未登录的玩家
             */
            Set<String> firstLoginUserIdList = new HashSet<>();//统计日的登录用户
            Set<String> lastLoginUserIdList = new HashSet<>();//统计日后到截止日的登录用户
            for (Map<String, Object> loginUserMap : allUserDataList) {
                String ltime = loginUserMap.get("date").toString() + " 00:00:01";
                String userId = loginUserMap.get("userId").toString();
                if (sdfhm.parse(ltime).before(sdfhm.parse(daten)) && sdfhm.parse(ltime).after(sdfhm.parse(dates))) {
                    firstLoginUserIdList.add(userId);
                }
                if (sdfhm.parse(ltime).before(sdfhm.parse(endTime)) && sdfhm.parse(ltime).after(sdfhm.parse(datess))) {
                    lastLoginUserIdList.add(userId);
                }
            }
            /*
             * 分别获取统计日的付费玩家和统计日后一周的付费玩家
             */
            Set<String> firstVipUserIdList = new HashSet<>();//统计日的付费用户
            Set<String> lastVipUserIdList = new HashSet<>();//统计日后一周的付费用户
            Set<String> needVipUserIdList = new HashSet<>();//存放付费用户id
            for (Map<String, Object> rechargeUserMap : rechargeDataMap) {
                String userId = rechargeUserMap.get("userId").toString();
                firstVipUserIdList.add(userId);//统计日中所有登录过的付费玩家id
                lastVipUserIdList.add(userId);//
                needVipUserIdList.add(userId);
            }
            //（检查合服）
//            Map<String, List<String>> levelhefuTableMap = new HashMap<>();
//            for (String id : serverId) {
//                levelhefuTableMap.putAll(QueryUtil.getInstance().getHefuTable(id, TableName.RoleCreate, sdf.parse(dates), sdf.parse(daten)));
//            }
            if (judge.equals("playerLeaveCount")) {
                //统计流失率
                firstVipUserIdList.retainAll(firstLoginUserIdList);//当日总登录付费玩家
                int firstLoginCount = firstLoginUserIdList.size();//总登录玩家
                firstLoginUserIdList.removeAll(lastLoginUserIdList);//第二天到截止日登录过的玩家
                int laterLoginCount = firstLoginUserIdList.size();//没登录过的玩家--流失玩家数
                float ordrate = 0;
                if (firstLoginCount != 0) {
                    ordrate = (float) laterLoginCount / firstLoginCount;
                }
                lastVipUserIdList.retainAll(firstLoginUserIdList);//第二天到截止日登陆过的付费玩家
                int laterVipLoginCount = firstVipUserIdList.size() - lastVipUserIdList.size();//没登录过的付费玩家--流失玩家
                float viprate = 0;
                if (firstVipUserIdList.size() != 0) {
                    viprate = (float) laterVipLoginCount / firstVipUserIdList.size();
                }
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("date", sdf.format(d));
                dataMap.put("ordfcount", firstLoginCount);
                dataMap.put("ordlcount", laterLoginCount);
                dataMap.put("ordrate", ordrate);
                dataMap.put("vipfcount", firstVipUserIdList.size());
                dataMap.put("viplcount", laterVipLoginCount);
                dataMap.put("viprate", viprate);
                dataList.add(dataMap);
            }
            if (judge.equals("playerLeaveRank") || judge.equals("playerLeaveAmount")) {
                firstVipUserIdList.retainAll(firstLoginUserIdList);//当日登录过的付费玩家
                firstLoginUserIdList.removeAll(lastLoginUserIdList);//流失普通玩家的id
                needVipUserIdList.retainAll(lastLoginUserIdList);//第二天到截止日期登录过的玩家
                lastVipUserIdList.removeAll(needVipUserIdList);//第二天到截止日期登录过的付费玩家
                firstVipUserIdList.removeAll(lastVipUserIdList);//流失付费玩家的id
                ordUserIdList.addAll(firstLoginUserIdList);
                vipUserIdList.addAll(firstVipUserIdList);
            }
        }

        if (judge.equals("playerLeaveRank") || judge.equals("playerLeaveAmount")) {
            Map<String, Integer> ordcMap = new LinkedHashMap<>();//统计普通用户等级数据
            Map<String, Integer> vipcMap = new LinkedHashMap<>();//统计付费用户等级数据
            String frontOrdUsers = ordUserIdList.toString();
            String ordUsers = frontOrdUsers.substring(1, frontOrdUsers.length() - 1);
            String frontVipUsers = vipUserIdList.toString();
            String vipUsers = frontVipUsers.substring(1, frontVipUsers.length() - 1);
            vipUserId = vipUsers;//保存付费流失用户
            List<Map<String, Object>> ordDataMap = new ArrayList<>();
            List<Map<String, Object>> vipDataMap = new ArrayList<>();
            if (ordUsers != null && !"".equals(ordUsers)) {
                for (String id : serverId) {
                    Dblog dblog = DbLogListManager.getInstance().getDblog(id);
                    String sqlStr = getUserLevelSql(id, channelNames, ordUsers);
                    ordDataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                }
            }
            if (vipUsers != null && !"".equals(vipUsers)) {
                for (String id : serverId) {
                    Dblog dblog = DbLogListManager.getInstance().getDblog(id);
                    String sqlStr = getUserLevelSql(id, channelNames, vipUsers);
                    vipDataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                }
            }
            if (ordDataMap != null && !"".equals(ordDataMap)) {
                for (Map<String, Object> map : ordDataMap) {
                    String level = map.get("level").toString();
                    if (ordcMap.containsKey(level)) {
                        ordcMap.put(level, ordcMap.get(level) + 1);
                    } else {
                        ordcMap.put(level, 1);
                    }
                }
            }
            if (vipDataMap != null && !"".equals(vipDataMap)) {
                for (Map<String, Object> map : vipDataMap) {
                    String level = map.get("level").toString();
                    if (vipcMap.containsKey(level)) {
                        vipcMap.put(level, vipcMap.get(level) + 1);
                    } else {
                        vipcMap.put(level, 1);
                    }
                }
            }
            Map<Integer, Integer> ordMap = new LinkedHashMap<>();//统计等级对应用户数
            Map<Integer, Integer> vipMap = new LinkedHashMap<>();//统计等级对应付费用户数
//            for (int i = 1; i <= 800; i++) {
//                ordMap.put(i, 0);
//                vipMap.put(i, 0);
//            }
            //将新的等级对应的用户数覆盖
            for (String key : ordcMap.keySet()) {
                ordMap.put(Integer.parseInt(key), ordcMap.get(key));
            }
            for (String key : vipcMap.keySet()) {
                vipMap.put(Integer.parseInt(key), vipcMap.get(key));
            }
            if(ordMap.size()>0&&vipMap.isEmpty()){
                for (Integer i:ordMap.keySet()) {
                    vipMap.put(i, 0);
                }
            }
            //将多余的数据清除,并用一个map数组保存下来
//            for (int i = 1; i <= 600; i++) {
//                if (ordMap.get(i) == 0 && vipMap.get(i) == 0) {
//                    ordMap.remove(i);
//                    vipMap.remove(i);
//                }
//            }
            if (judge.equals("playerLeaveRank")) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("ordMap", ordMap);
                dataMap.put("vipMap", vipMap);
                dataList.add(dataMap);
            }
        }
        if (judge.equals("playerLeaveAmount")) {
            int[] amountSum = new int[]{0, 10, 20, 30, 70, 150, 300, 500, 1000, 1500, 2000, 5000, 10000};
            String[] amountSums = new String[]{"0~10", "10~20", "20~30", "30~70", "70~150", "150~300", "300~500", "500~1000", "1000~1500", "1500~2000", "2000~5000", "5000~10000", ">=10000"};
            Map<String, Integer> paylcount = new LinkedHashMap<>();//满足条件的流失用户数
            Map<String, Object> rechargeSum = new LinkedHashMap<>();//流失用户充值总额
            for (String id : serverId) {
                Map<String, List<String>> hefuTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(id, TableName.RechargeSuccess, sdf.parse(startDate), sdf.parse(dateeStr)));
                for (String key : hefuTableMap.keySet()) {
                    List<String> tableList = hefuTableMap.get(key);
                    tableList.retainAll(rechargeGoalTables);//过滤重复数据表
                    for (String s : tableList) {
                        for (int i = 0; i < amountSum.length; i++) {
                            String sqlStr = getAmountSql(s, channelNames, vipUserId, amountSum[i]);
                            Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                            if (dblog == null) {
                                continue;
                            }
                            List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                            if (!dataMap.isEmpty()) {
                                for (Map<String, Object> map : dataMap) {
                                    if (map.containsKey("counts")) {
                                        paylcount.put(amountSums[i], Integer.parseInt(map.get("counts").toString()));
                                    }
                                    if (map.containsKey("amount") && map.get("amount") != null) {
                                        rechargeSum.put(amountSums[i], map.get("amount").toString());
                                    } else {
                                        rechargeSum.put(amountSums[i], 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("paylcount", paylcount);
            dataMap.put("rechargeSum", rechargeSum);
            dataList.add(dataMap);
        }
        return dataList;
    }

    /**
     * 获取选择时间内所有登录用户
     */
    private String getUserSql(String table, String serverId, String channelNames, String startDate, String endDate, String minPay, String maxPay,
                              String tableJudge, List<Map<String, Object>> allUserDataList) {
        StringBuilder str = new StringBuilder();
        if (!tableJudge.equals(TableName.RechargeSuccess)) {
            str.append("SELECT userId, DATE_FORMAT(FROM_UNIXTIME(time),'%Y-%m-%d') date");
            str.append(" FROM " + table);
            str.append(" where time between UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 23:59:59') ");
            if (!Strings.isBlank(channelNames)) {
                str.append(" and platformName in (" + channelNames + ")");
            }
            str.append(" GROUP BY userId,date");
        }
        if (tableJudge.equals(TableName.RechargeSuccess)) {
            str.append("SELECT userId ");
            str.append(" FROM " + table);
            str.append(" where status = 1 and statusReason = 7");
            str.append(" and sid in ('" + serverId + "')");
            if (minPay != null && !maxPay.equals("") && !maxPay.equals("") && maxPay != null) {
                str.append(" and (totalFee between  " + minPay + " and " + maxPay + ")");
            }
            String userId = "";
            if (allUserDataList != null) {
                for (Map<String, Object> map : allUserDataList) {
                    userId += map.get("userId").toString() + ",";
                }
                if (userId != null && !"".equals(userId)) {
                    userId = userId.substring(0, userId.length() - 1);
                    str.append(" and userId in (" + userId + ")");
                }
            }
            if (!Strings.isBlank(channelNames)) {
                str.append(" and platformName in (" + channelNames + ")");
            }
            str.append(" GROUP BY userId");
        }
        return str.toString();
    }

    /**
     * 获取用户等级的SQL
     */
    private String getUserLevelSql(String serverId, String channelNames, String users) {
        StringBuilder str = new StringBuilder();
        str.append("select userId,max(level) as level");
        str.append(" FROM rolestate");
        str.append(" where userId in (" + users + ")");
        str.append(" and createsid in ('" + serverId + "')");
        if (!Strings.isBlank(channelNames)) {
            str.append(" and platformName in (" + channelNames + ")");
        }
        str.append(" group by userId");
        return str.toString();
    }

    /**
     * 获取用户累充金额
     */
    private String getAmountSql(String table, String channelNames, String vipUserId, int amountSum) {
        StringBuilder str = new StringBuilder();
        str.append("select sum(t.totalFee) as amount,count(t.userId) as counts");
        str.append(" FROM (" + "select userId,sum(totalFee) as totalFee from " + table);
        if (vipUserId != null && !"".equals(vipUserId)) {
            str.append(" where status = 1 and statusReason = 7 and userId in (" + vipUserId + ") group by userId) t");
        } else {
            str.append(" where status = 1 and statusReason = 7 and userId=0 group by userId) t");
        }
        if (amountSum == 0) {
            str.append(" where t.totalFee>0 and t.totalFee<=10");
        } else if (amountSum == 10) {
            str.append(" where t.totalFee>10 and t.totalFee<=20");
        } else if (amountSum == 20) {
            str.append(" where t.totalFee>20 and t.totalFee<=30");
        } else if (amountSum == 30) {
            str.append(" where t.totalFee>30 and t.totalFee<=70");
        } else if (amountSum == 70) {
            str.append(" where t.totalFee>70 and t.totalFee<=150");
        } else if (amountSum == 150) {
            str.append(" where t.totalFee>150 and t.totalFee<=300");
        } else if (amountSum == 300) {
            str.append(" where t.totalFee>300 and t.totalFee<=500");
        } else if (amountSum == 500) {
            str.append(" where t.totalFee>500 and t.totalFee<=1000");
        } else if (amountSum == 1000) {
            str.append(" where t.totalFee>1000 and t.totalFee<=1500");
        } else if (amountSum == 1500) {
            str.append(" where t.totalFee>1500 and t.totalFee<=2000");
        } else if (amountSum == 2000) {
            str.append(" where t.totalFee>2000 and t.totalFee<=5000");
        } else if (amountSum == 5000) {
            str.append(" where t.totalFee>5000 and t.totalFee<=10000");
        } else if (amountSum == 10000) {
            str.append(" where t.totalFee>=10000 ");
        }
        if (!Strings.isBlank(channelNames)) {
            str.append(" and t.platformName in (" + channelNames + ")");
        }
        return str.toString();
    }

    /**
     * 计算起始天数的差值
     */
    private int dValue(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    /**
     * 得到计算后的日期
     */
    private String getCalDate(String endDate) throws ParseException {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(sdf.parse(endDate));
        calendarStart.add(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(calendarStart.getTime());
    }
}
