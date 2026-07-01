package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.filter.MenuFilter;
import com.backend.struct.log.TableName;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.utils.QueryUtil;
import com.backend.utils.DateUtil;
import com.backend.utils.Toolkit;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
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
@At("/ltvstatistic")
@Fail("http:500")
public class PlyLtvStatisticModule {

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static String RECHARGELOG = "rechargelog";
    @Inject
    protected Dao dao;

    @At("/")
    @Ok("jsp:jsp.statistic.playerLtv")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index(HttpServletRequest request) {
        request.setAttribute("newDate", sdf.format(new Date()));
    }


    /**
     * 统计LTV的值(押后)  LTV的计算为：
     * 假设1号的新增为X，LTV1=X在1号的付费总额/X ，
     * 同理LTV2=(X在1号的付费总额+X在2号的付费总额）/X
     */
    @At
    public Object ltvDataStatistic(String groupName, String[] serverId, String channelNames, String startDate, String endDate, boolean isblack) throws Exception {
        //-1 表示 当前天 占位
        Integer[] ltvDays = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28, 29,44,59,119};
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);

        List<String> dateStrList = new ArrayList<>();
        int dvalue = DateUtil.dValue(start, end);
        long oneday = 1000 * 60 * 60 * 24;
        long dateTime = start.getTime();
        Date date;
        for (int i = 0; i < dvalue + 1; i++) {
            date = new Date(dateTime);
            dateStrList.add(sdf.format(date));
            dateTime += oneday;
        }
        String endDateStr = getCalDate(endDate, 60);
        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        List<Map<String, Object>> rechargeMapList = new ArrayList<>();
        Map<String,String> createUsers = new HashMap<>();
        for (String sid : serverId) {
            //得到选择时间内所有角色注册的信息
            int finalServerId = QueryUtil.getInstance().getHeFuId(Integer.parseInt(sid));
            Dblog rolestatedblog = DbLogListManager.getInstance().getDBServer(groupName, finalServerId);
            String firstSqlStr = getUserRegisterSql(channelNames, TableName.RoleState, sid, startDate, endDateStr);
            List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(rolestatedblog, firstSqlStr);

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

            //得到付费数据
            Map<String, List<String>> hefuTableMap = QueryUtil.getInstance().getHefuTable(sid, RECHARGELOG, start, sdf.parse(endDateStr));
            for (String key : hefuTableMap.keySet()) {
                List<String> tableList = hefuTableMap.get(key);
                //计算出每日充值玩家
                for (String value : tableList) {
                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    if (dblog == null) {
                        continue;
                    }
                    //统计玩家充值时所出的等级，等级对应的充值总金额分布，次数分布
                    String strSql = getRechargeAmountSql(value, sid, channelNames, startDate, endDateStr);
                    List<Map<String, Object>> data = QueryUtil.getInstance().query(dblog, strSql);
                    getResult(rechargeMapList, data, "chargeTime");
                }
            }
        }
        if (isblack) {
            List<Map<String, Object>> blackReUserData = new ArrayList<>();
            List<Map<String, Object>> blackRechargeData = new ArrayList<>();
            List<Map<String, Object>> blackList = BlackListManager.getInstance().getBlackList(groupName);
            Map<String,String> blackUsers = new HashMap<>();
            for (Map<String, Object> stringObjectMap : blackList) {
                //所有玩家的数据
                String userId = stringObjectMap.get("userId").toString();
                for(String user : createUsers.keySet()){
                    if (userId.equals(user)) {
                        blackUsers.put(user,createUsers.get(user));
                    }
                }
                //充值玩家
                for (Map<String, Object> rechargeUserMap : rechargeMapList) {
                    String user = rechargeUserMap.get("userId").toString();
                    if (userId.equals(user)) {
                        blackRechargeData.add(rechargeUserMap);
                    }
                }
            }

            for(String user : blackUsers.keySet()){
                createUsers.remove(user);
            }
            rechargeMapList.removeAll(blackRechargeData);
        }

        List<Map<String, Object>> dataMapList = new ArrayList<>();



        for (String dateStr : dateStrList) {
            List<String> reDs = getDateStrList(ltvDays, sdf.parse(dateStr));
            //取当前天的日期
//            String currDay = DateUtil.datePath();
//            reDs.add(currDay);
            //筛选出各个时间段的注册user（用set）
            Set<String> registerUserIds = new HashSet<>();
            for(String userid : createUsers.keySet()){
                if (dateStr.equals(createUsers.get(userid))) {
                    registerUserIds.add(userid);
                }
            }
            float acount = 0;
            int reglength = registerUserIds.size();
            Map<Integer, Object> ltvMap = new HashMap<>();
            for (int i = 0; i < reDs.size(); i++) {
                //筛选出各个时间段的付费user
                Set<String> rechargeUserIds = new HashSet<>();
                for (Map<String, Object> rechargeMap : rechargeMapList) {
                    String createTime = rechargeMap.get("chargeTime").toString();
                    String userid = rechargeMap.get("userId").toString();
                    if (reDs.get(i).equals(createTime)) {
                        rechargeUserIds.add(userid);
                    }
                }
                rechargeUserIds.retainAll(registerUserIds);
                //得到每天的付费金额
                for (Map<String, Object> rechargeMap : rechargeMapList) {
                    String createTime = rechargeMap.get("chargeTime").toString();
                    String userid = rechargeMap.get("userId").toString();
                    String amountSum = rechargeMap.get("amountSum").toString();
                    if (reDs.get(i).equals(createTime) && rechargeUserIds.contains(userid)) {
                        acount += Float.parseFloat(amountSum);
                    }
                }

                if (reglength == 0) {
                    ltvMap.put(ltvDays[i] + 1, 0);
                } else {
                    ltvMap.put(ltvDays[i] + 1, acount / (float) reglength);
                }
            }
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("reglength", reglength);
            dataMap.put("ltvMap", ltvMap);
            dataMap.put("date", dateStr);
            dataMapList.add(dataMap);
        }
//        Integer[] newLtvDay = new Integer[ltvDays.length + 1];
//        for(int i = 0;i<ltvDays.length;i++){
//
//        }

        return Toolkit.outResult(true).setv("ltvDays", ltvDays).setv("dataMapList", dataMapList);
    }

    private void getResult(List<Map<String, Object>> itemdataMapList, List<Map<String, Object>> dataMapList, String param) {
        //由于存在合服以及多服的选择，所以这里处理时，把多个服务器的按天产出累加以及消耗累加
        for (Map<String, Object> map : dataMapList) {
            boolean isHaveDay = false;//判断是否这一天已经存在在itemdataMapList里面了
            if (!itemdataMapList.isEmpty()) {
                for (Map<String, Object> itemMap : itemdataMapList) {
                    if (map.get(param).toString().equals(itemMap.get(param).toString()) && map.get("userId").toString().equals(itemMap.get("userId").toString())) {
                        isHaveDay = true;
                        if (map.containsKey("amountSum")) {
                            itemMap.put("amountSum", getSum(map.get("amountSum").toString(), itemMap.get("amountSum").toString()));
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

    private String getCalDate(String date, int days) throws ParseException {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(sdf.parse(date));
        calendarStart.add(Calendar.DAY_OF_MONTH, days);
        return sdf.format(calendarStart.getTime());
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

    private String getRegisterUserSql(String serverId, String channelNames, String table, String startDate, String endDate) {
        String str = "select userId,DATE_FORMAT(from_unixtime(createTime), '%Y-%m-%d') as createTime from " + table
                + " where substring_index(substring_index(data,',',1),']',1)='[" + serverId + "'";
        if (!Strings.isBlank(channelNames)) {
            str += " and platformName in (" + channelNames + ")";
        }
        str += " and createTime between unix_timestamp('" + startDate + " 00:00:00') and unix_timestamp('" + endDate + " 23:59:59') "
                + "group by userId,DATE_FORMAT(from_unixtime(createTime), '%Y-%m-%d')";
        return str;
    }

    private String getRechargeAmountSql(String table, String serverId, String channelNames, String startDate, String endDate) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT S.userId,SUM(S.amount) AS amountSum,S.chargeTime FROM ( ");
        str.append("SELECT rsl.userId,rsl.totalFee as amount,DATE_FORMAT(FROM_UNIXTIME(rsl.time),'%Y-%m-%d') AS chargeTime FROM " + table + " rsl ");
        str.append("WHERE rsl.time BETWEEN UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 23:59:59')"
                + " AND rsl.statusReason = 5 AND rsl.sid in ('" + serverId + "')");
        if (!Strings.isBlank(channelNames)) {
            str.append(" and rsl.platformName in (" + channelNames + ")");
        }
        str.append(") S GROUP BY S.userId,S.chargeTime ");
        return str.toString();
    }

    private List<String> getDateStrList(Integer[] lengthDays, Date date) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(date);
        List<String> dateStrList = new ArrayList<>();
        for (int dayNum : lengthDays) {
            calendarStart.add(Calendar.DAY_OF_MONTH, dayNum);
            String ds = sdf.format(calendarStart.getTime());
            dateStrList.add(ds);
            calendarStart.add(Calendar.DAY_OF_MONTH, -dayNum);
        }
        return dateStrList;
    }
}






