package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.bean.User;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.ExportExcelUtil;
import com.backend.utils.QueryUtil;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 玩家在线时长统计
 */
@IocBean
@Ok("json")
@At("/onlinestatistic")
@Fail("http:500")
public class OnTimeStatisticModule {

    private static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static String ROLELOGINLOG = "roleloginlog";
    private static String RECHARGESUCCESSLOG = "rechargelog";
    private Map<User, List<Map<String, Object>>> dataMap = new HashMap<>();
    private String exName = "";

    @At("/")
    @Ok("jsp:jsp.statistic.onlineTime")
    public void index(HttpServletRequest request) {
        BackendLogUtil.getInstance().log(request, "进入在线时长统计页面");
        request.setAttribute("nowDate", sdf.format(new Date()));
    }

    @At
    //统计选择日期的活跃玩家的在线时长
    public Object onlineTimeStatistic(String[] serverId, String groupName, String channelNames, String startDate,
                                      String endDate, boolean isblack, int pageIndex, int pageSize,
                                      int selectCondition, String minPay, String maxPay) throws Exception {

        String judge = "onlineTimeStatistic";
        List<Map<String, Object>> onlineTimeDataMap = getData(judge, serverId, groupName, channelNames, startDate, endDate, isblack,
                pageIndex, pageSize, selectCondition, minPay, maxPay);
        int[] sectionTime = {0, 5 * 60, 30 * 60, 60 * 60, 120 * 60, 180 * 60};
        Map<String, Integer> resMap = new LinkedHashMap<>(); //结果map
        Map<Integer, Integer> countMap = new TreeMap<>(); //计数map
        //初始化计数map
        for (int i = 1; i < sectionTime.length; i++) {
            countMap.put(sectionTime[i], 0);
        }
        if (onlineTimeDataMap.size() != 0) {
            for (Map<String, Object> onlineMap : onlineTimeDataMap) {
                if (!onlineMap.containsKey("onlineTime")) {
                    continue;
                }
                String onlineTime = onlineMap.get("onlineTime").toString();
                long value = Long.parseLong(onlineTime);
                for (int i = 0; i < sectionTime.length; i++) {
                    int count = 0;
                    if (i < (sectionTime.length - 1)) {
                        if (i == 0) {
                            if (value == sectionTime[0]) {
                                count++;
                                if (countMap.containsKey(sectionTime[0])) {
                                    count += countMap.get(sectionTime[0]);
                                }
                                countMap.put(sectionTime[0], count);
                            }
                        }
                        if (value > sectionTime[i] && value <= sectionTime[i + 1]) {
                            count++;
                            if (countMap.containsKey(sectionTime[i])) {
                                count += countMap.get(sectionTime[i]);
                            }
                            countMap.put(sectionTime[i], count);
                        }
                    } else {
                        if (value > sectionTime[i]) {
                            count++;
                            if (countMap.containsKey(sectionTime[i])) {
                                count += countMap.get(sectionTime[i]);
                            }
                            countMap.put(sectionTime[i], count);
                        }
                    }
                }
            }
            Set<Integer> keys = countMap.keySet();
            for (Integer key : keys) {
                String timeInfo = TimeCastInfo(key);
                Integer roleCount = countMap.get(key);
                resMap.put(timeInfo, roleCount);
            }
        }
        return resMap;
    }

    private String TimeCastInfo(int time) {
        switch (time) {
            case 0:
                return "小于5分钟（≤5分钟）";
            case 5 * 60:
                return "5分钟~30分钟（>5分钟,≤30分钟）";
            case 30 * 60:
                return "30分钟~1小时（>30分钟,≤1小时）";
            case 60 * 60:
                return "1小时~2小时（>1小时,≤2小时）";
            case 120 * 60:
                return "2小时~3小时 （>2小时,≤3小时）";
            case 180 * 60:
                return "3小时以上 （>3小时）";
            default:
                return "";
        }
    }

    @At
    //获取每日在线时长记录
    public Object dailyOnlineTimeStatistic(String[] serverId, String groupName, String channelNames, String startDate,
                                           String endDate, boolean isblack, int pageIndex, int pageSize,
                                           int selectCondition, String minPay, String maxPay) throws Exception {
        String judge = "dailyOnlineTimeStatistic";
        List<Map<String, Object>> onlineTimeDataMap = getData(judge, serverId, groupName, channelNames, startDate, endDate, isblack,
                pageIndex, pageSize, selectCondition, minPay, maxPay);
        exName = "OnlineTimeDetail";
        dataMap.put((User) Mvcs.getHttpSession().getAttribute("USER"), onlineTimeDataMap);
        return onlineTimeDataMap;
    }

    @At
    //获取每日在线时长记录的条数
    public Object dailyOnlineTimeCount(String[] serverId, String platfName, String channelNames, String startDate,
                                       String endDate, boolean isblack, int pageIndex, int pageSize,
                                       int selectCondition, String minPay, String maxPay) throws Exception {
        String judge = "dailyOnlineTimeCount";
        return getData(judge, serverId, platfName, channelNames, startDate, endDate, isblack, pageIndex, pageSize, selectCondition, minPay, maxPay);
    }

    //获取数据
    private List<Map<String, Object>> getData(String judge, String[] serverId, String platfName, String channelNames,
                                              String startDate, String endDate, boolean isblack, int pageIndex, int pageSize,
                                              int selectCondition, String minPay, String maxPay) throws Exception {
        String oldEndDate = endDate;
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(sdf.parse(startDate));
        end.setTime(sdf.parse(endDate));
        end.add(Calendar.DATE, 1);//endDate往后加一天
        endDate = sdf.format(end.getTime());

        //处理渠道字符串
        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        //统计在线时长
        List<Map<String, Object>> onlineTimeDataMap = new ArrayList<>();
        //统计付费玩家
        List<Map<String, Object>> rechargeMapList = new ArrayList<>();

        String blackUserStr = "";//黑名单账号
        if (isblack) {
            List<Map<String, Object>> blackList = BlackListManager.getInstance().getBlackList(platfName);
            Set<String> blackSet = new HashSet<>();
            for (Map<String, Object> map : blackList) {
                blackSet.add(map.get("userId").toString());
            }
            blackUserStr = blackSet.toString();
            blackUserStr = blackUserStr.substring(1, blackUserStr.length() - 1);
        }

        for (String s : serverId) {

            List<String> rechargeGoalTables = QueryUtil.getInstance().getQueryTables(RECHARGESUCCESSLOG, TableType.Month, startDate+" 00:00:00", endDate+" 23:59:59");
            Map<String, List<String>> rechargeHefuReTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(s, RECHARGESUCCESSLOG, sdf.parse(startDate), sdf.parse(endDate)));
            //在线时长计算数据
            List<String> goalTables = QueryUtil.getInstance().getQueryTables(ROLELOGINLOG, TableType.Month, startDate+" 00:00:00", endDate+" 23:59:59");

            Map<String, List<String>> loginHefuTableMap = new TreeMap<>(QueryUtil.getInstance().getHefuTable(s, ROLELOGINLOG, sdf.parse(startDate), sdf.parse(endDate)));

            if (selectCondition == 0 || selectCondition == 1) {

                for (String key : rechargeHefuReTableMap.keySet()) {
                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    List<String> tableList = rechargeHefuReTableMap.get(key);
                    tableList.retainAll(rechargeGoalTables);//过滤重复数据表
                    //计算出每日充值玩家
                    for (String value : tableList) {
                        //统计玩家充值时所出的等级，等级对应的充值总金额分布，次数分布
                        String strSql = getRechargeRolesSql(value, s, channelNames, startDate, endDate, minPay, maxPay);
                        List<Map<String, Object>> data = QueryUtil.getInstance().query(dblog, strSql);
                        rechargeMapList.addAll(data);
                    }
                }
            } else if (selectCondition == 2) {
                for (String key : loginHefuTableMap.keySet()) {
                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    List<String> tableList = loginHefuTableMap.get(key);
                    tableList.retainAll(goalTables);//过滤重复数据表
                    for (String value : tableList) {

                        String strSql = getLoseRoleSql(value, s, startDate, oldEndDate);
                        List<Map<String, Object>> data = QueryUtil.getInstance().query(dblog, strSql);
                        rechargeMapList.addAll(data);
                    }
                }
            } else if (selectCondition == 3) {
                for (String key : loginHefuTableMap.keySet()) {
                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    List<String> tableList = loginHefuTableMap.get(key);
                    tableList.retainAll(goalTables);//过滤重复数据表
                    for (String value : tableList) {
                        for (String rechargeKey : rechargeHefuReTableMap.keySet()) {
                            List<String> rechargeTableList = rechargeHefuReTableMap.get(rechargeKey);
                            rechargeTableList.retainAll(rechargeGoalTables);//过滤重复数据表
                            for (String item : rechargeTableList) {
                                String strSql = getLoseRechargeRoleSql(item, value, s, channelNames, startDate, oldEndDate, minPay, maxPay);
                                List<Map<String, Object>> data = QueryUtil.getInstance().query(dblog, strSql);
                                rechargeMapList.addAll(data);
                            }
                        }
                    }
                }
            }

            String rechargeRoleIdStr;
            Set<String> rechargeRoleIdList = new HashSet<>();
            for (Map<String, Object> rechargeMap : rechargeMapList) {
                if (rechargeMap.containsKey("roleId")) {
                    rechargeRoleIdList.add(rechargeMap.get("roleId").toString());
                }
            }
            if (!rechargeRoleIdList.isEmpty()) {
                rechargeRoleIdStr = rechargeRoleIdList.toString().replace("[", "").replace("]", "").replace("{", "\"").replace("}", "\"");
            } else {
                rechargeRoleIdStr = "\"\"";
            }
            for (String key : loginHefuTableMap.keySet()) {
                List<String> tableList = loginHefuTableMap.get(key);
                tableList.retainAll(goalTables);//过滤重复数据表
                for (String value : tableList) {

                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    if (dblog == null) {
                        continue;
                    }
                    String sqlStr = "";
                    if (judge.equals("dailyOnlineTimeCount")) {
                        sqlStr = getDailyOnlineCountSqlStr(value, s, channelNames, startDate, endDate, blackUserStr, selectCondition, rechargeRoleIdStr);
                        int counts = QueryUtil.getInstance().queryCount(dblog, sqlStr);
                        //统计每日在线时长的条数
                        Map<String, Object> countMap = new TreeMap<>();
                        countMap.put("counts", counts);
                        onlineTimeDataMap.add(countMap);
                    } else {
                        if (judge.equals("onlineTimeStatistic")) {
                            sqlStr = getOnLineSqlStr(value, s, channelNames, startDate, endDate, blackUserStr, selectCondition, rechargeRoleIdStr);
                        } else if (judge.equals("dailyOnlineTimeStatistic")) {
                            sqlStr = getDailyOnlineSqlStr(value, s, channelNames, startDate, endDate, blackUserStr, selectCondition, rechargeRoleIdStr, pageIndex, pageSize);
                        }
                        List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                        for (Map<String, Object> map : dataMap) {
                            for (String columnName : map.keySet()) {
                                if (columnName.equals("userId") || columnName.equals("roleId")) {
                                    String idStr = map.get(columnName).toString();
                                    map.put(columnName, idStr);
                                }
                            }
                        }
                        if (judge.equals("onlineTimeStatistic")) {
                            getResult(onlineTimeDataMap, dataMap);
                        } else if (judge.equals("dailyOnlineTimeStatistic")) {
                            onlineTimeDataMap.addAll(dataMap);
                        }
                    }
                }
            }
        }
        return onlineTimeDataMap;
    }

    private void getResult(List<Map<String, Object>> onlineTimeDataMap, List<Map<String, Object>> dataMapList) {
        //由于存在合服以及多服的选择，所以这里处理时，把多个服务器的按天产出累加以及消耗累加
        for (Map<String, Object> map : dataMapList) {
            boolean isHaveDay = false;//判断是否这一天已经存在在itemdataMapList里面了
            if (!onlineTimeDataMap.isEmpty()) {
                for (Map<String, Object> itemMap : onlineTimeDataMap) {
                    if (map.get("userId").toString().equals(itemMap.get("userId").toString()) && map.get("roleId").toString().equals(itemMap.get("roleId").toString())) {
                        isHaveDay = true;
                        if (map.containsKey("onlineTime")) {
                            itemMap.put("onlineTime", getSum(map.get("onlineTime").toString(), itemMap.get("onlineTime").toString()));
                        }
                    }
                }
            }
            if (!isHaveDay) {
                onlineTimeDataMap.add(map);
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

    //统计在线时长的SQL
    private String getOnLineSqlStr(String table, String serverId, String channelNames, String startDate, String endDate, String blackUserStr, int selectCondition, String rechargeRoleIdStr) {
        StringBuilder str = new StringBuilder();
        str.append("select a.userId , a.roleId,sum(a.onlineTime) onlineTime from (SELECT userId,roleId,onlineTime");
        str.append(" FROM " + table);
        str.append(" WHERE TYPE = 0 AND time BETWEEN UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 00:00:00') AND sid IN ('" + serverId + "')");
        if (!Strings.isBlank(channelNames)) {
            str.append(" and platformName in (" + channelNames + ")");
        }
        if (!Strings.isBlank(blackUserStr)) {
            str.append(" and userId NOT IN (" + blackUserStr + ")");
        }
        str.append(" UNION ALL");
        str.append(" SELECT userId,roleId,onlineTime");
        str.append(" FROM " + table);
        str.append(" WHERE TYPE = 2 AND time BETWEEN UNIX_TIMESTAMP('" + startDate + " 01:00:00') AND UNIX_TIMESTAMP('" + endDate + " 01:00:00') AND sid IN ('" + serverId + "')");
        if (!Strings.isBlank(channelNames)) {
            str.append(" and platformName in (" + channelNames + ")");
        }
        if (!Strings.isBlank(blackUserStr)) {
            str.append(" and userId NOT IN (" + blackUserStr + ")");
        }
        str.append(") a");
        if (selectCondition == 0) {
            str.append(" where a.roleId NOT IN (" + rechargeRoleIdStr + ")");
        } else if (selectCondition == 1 || selectCondition == 2 || selectCondition == 3) {
            str.append(" where a.roleId IN (" + rechargeRoleIdStr + ")");
        }
        str.append(" group by a.userId,a.roleId");
        return str.toString();
    }

    //获取每日在线时长数据的SQL
    private String getDailyOnlineSqlStr(String table, String serverId, String channelNames, String startDate, String endDate, String blackUserStr, int selectCondition, String rechargeRoleIdStr, int pageIndex, int pageSize) {
        StringBuilder str = new StringBuilder();
        str.append(" SELECT * from (SELECT userId,roleId,roleName,platformName,sid as serverId,FROM_UNIXTIME(createTime) createTime,level,SEC_TO_TIME(onlinetime) onlineTime,FROM_UNIXTIME(time) time");
        str.append(" FROM " + table);
        str.append(" WHERE TYPE = 0 AND time BETWEEN UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 00:00:00') AND sid IN ('" + serverId + "')");
        if (!Strings.isBlank(channelNames)) {
            str.append(" and platformName in (" + channelNames + ")");
        }
        if (!Strings.isBlank(blackUserStr)) {
            str.append(" and userId NOT IN (" + blackUserStr + ")");
        }
        str.append(" UNION ALL");
        str.append(" SELECT userId,roleId,roleName,platformName,sid as serverId,FROM_UNIXTIME(createTime) createTime,level,SEC_TO_TIME(onlinetime) onlineTime,FROM_UNIXTIME(time) time");
        str.append(" FROM " + table);
        str.append(" WHERE TYPE = 2 AND time BETWEEN UNIX_TIMESTAMP('" + startDate + " 01:00:00') AND UNIX_TIMESTAMP('" + endDate + " 01:00:00') AND sid IN ('" + serverId + "')");
        if (!Strings.isBlank(channelNames)) {
            str.append(" and platformName in (" + channelNames + ")");
        }
        if (!Strings.isBlank(blackUserStr)) {
            str.append(" and userId NOT IN (" + blackUserStr + ")");
        }
        str.append(") a");
        if (selectCondition == 0) {
            str.append(" where a.roleId NOT IN (" + rechargeRoleIdStr + ")");
        } else if (selectCondition == 1 || selectCondition == 2 || selectCondition == 3) {
            str.append(" where a.roleId IN (" + rechargeRoleIdStr + ")");
        }
        str.append(" limit " + pageIndex + "," + pageSize);
        return str.toString();
    }

    //获取每日在线时长数据条数的SQL
    private String getDailyOnlineCountSqlStr(String table, String serverId, String channelNames, String startDate, String endDate, String blackUserStr, int selectCondition, String rechargeRoleIdStr) {
        StringBuilder str = new StringBuilder();
        str.append(" SELECT count(*) counts from (SELECT userId,roleId,roleName,platformName,serverId,FROM_UNIXTIME(createTime) createTime,level,SEC_TO_TIME(onlinetime) onlineTime,FROM_UNIXTIME(time) time");
        str.append(" FROM " + table);
        str.append(" WHERE TYPE = 0 AND time BETWEEN UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 00:00:00') AND serverId IN ('" + serverId + "')");
        if (!Strings.isBlank(channelNames)) {
            str.append(" and platformName in (" + channelNames + ")");
        }
        if (!Strings.isBlank(blackUserStr)) {
            str.append(" and userId NOT IN (" + blackUserStr + ")");
        }
        str.append(" UNION ALL");
        str.append(" SELECT userId,roleId,roleName,platformName,serverId,FROM_UNIXTIME(createTime) createTime,level,SEC_TO_TIME(onlinetime) onlineTime,FROM_UNIXTIME(time) time");
        str.append(" FROM " + table);
        str.append(" WHERE TYPE = 2 AND time BETWEEN UNIX_TIMESTAMP('" + startDate + " 01:00:00') AND UNIX_TIMESTAMP('" + endDate + " 01:00:00') AND serverId IN ('" + serverId + "')");
        if (!Strings.isBlank(channelNames)) {
            str.append(" and platformName in (" + channelNames + ")");
        }
        if (!Strings.isBlank(blackUserStr)) {
            str.append(" and userId NOT IN (" + blackUserStr + ")");
        }
        str.append(") a");
        if (selectCondition == 0) {
            str.append(" where a.roleId NOT IN (" + rechargeRoleIdStr + ")");
        } else if (selectCondition == 1 || selectCondition == 2 || selectCondition == 3) {
            str.append(" where a.roleId IN (" + rechargeRoleIdStr + ")");
        }
        return str.toString();
    }

    //获取充值玩家的SQL(获取截止到截止日期内的付过费的角色的在付费总额为指定付费上下限的角色)
    private String getRechargeRolesSql(String table, String serverId, String channelNames, String startDate, String endDate, String minPay, String maxPay) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT t.roleId");
        str.append(" FROM (SELECT roleId, SUM(amount) amount");
        str.append(" FROM " + table);
        str.append(" WHERE time < UNIX_TIMESTAMP('" + endDate + " 00:00:00') AND sid in ('" + serverId + "')");
        str.append(" group by roleId ) t");
        if (!minPay.equals(null) && !minPay.equals("") && !maxPay.equals(null) && !maxPay.equals("")) {
            str.append(" where t.amount >=" + Float.parseFloat(minPay) + " and t.amount <=" + Float.parseFloat(maxPay));
        }
        return str.toString();
    }

    //获取流失玩家（获取选择日期登录过的角色在选择日期的结束日期当天未登录过的角色）
    private String getLoseRoleSql(String table, String serverId, String startDate, String endDate) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT roleId");
        str.append(" FROM " + table);
        str.append(" WHERE TIME BETWEEN UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 23:59:59') AND serverId in ('" + serverId + "')");
        str.append(" AND roleId NOT IN(SELECT roleId");
        str.append(" FROM " + table);
        str.append(" WHERE TIME BETWEEN UNIX_TIMESTAMP('" + endDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 23:59:59') AND serverId in ('" + serverId + "')");
        str.append(" GROUP BY roleId)GROUP BY roleId");
        return str.toString();
    }

    //获取流失付费玩家（获取流失玩家付过费的角色在付费总额为指定付费上下限的角色）
    private String getLoseRechargeRoleSql(String rechargeTable, String logintable, String serverId, String channelNames, String startDate, String endDate, String minPay, String maxPay) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT t.roleId");
        str.append(" FROM (SELECT roleId, SUM(amount) amount");
        str.append(" FROM " + rechargeTable);
        str.append(" WHERE sid in ('" + serverId + "')");
        str.append(" group by roleId ) t");
        str.append(" WHERE t.roleId IN (SELECT roleId");
        str.append(" FROM " + logintable);
        str.append(" WHERE TIME BETWEEN UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 23:59:59') AND serverId in ('" + serverId + "')");
        str.append(" AND roleId NOT IN(SELECT roleId");
        str.append(" FROM " + logintable);
        str.append(" WHERE TIME BETWEEN UNIX_TIMESTAMP('" + endDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 23:59:59') AND serverId in ('" + serverId + "')");
        str.append(" GROUP BY roleId)GROUP BY roleId )");
        if (!minPay.equals(null) && !minPay.equals("") && !maxPay.equals(null) && !maxPay.equals("")) {
            str.append(" and t.amount >=" + Float.parseFloat(minPay) + " and t.amount <=" + Float.parseFloat(maxPay));
        }
        return str.toString();
    }

    @At
    @Ok("")
    public void exportExcel() {
        // 取得文件名
        String fileName = exName;
        // 取得表名
        String tableName = exName;
        ExportExcelUtil ex = new ExportExcelUtil();
        User user = (User) Mvcs.getHttpSession().getAttribute("USER");
        List<Map<String, Object>> dataMapList = new ArrayList<>();
        for (User key : dataMap.keySet()) {
            if (key.getId() == user.getId()) {
                dataMapList = dataMap.get(key);
                break;
            }
        }
        HttpServletResponse response = Mvcs.getResp();
        ex.exportExcel(tableName, dataMapList, response, fileName);
    }

}