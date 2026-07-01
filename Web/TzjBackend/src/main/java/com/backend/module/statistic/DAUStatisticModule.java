package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.filter.MenuFilter;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.utils.*;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@IocBean
@Ok("json")
@At("/dauStatistic")
@Fail("http:500")
public class DAUStatisticModule {
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String ROLELOGINLOG = "roleloginlog";
    @Inject
    protected Dao dao;

    @At
    @Ok("jsp:jsp.statistic.playerDau")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index(HttpServletRequest request) {
        request.setAttribute("nowDate", format.format(new Date()));
    }

    @At
    public Object getDAU(String groupName, String[] serverId, String[] channelName, String startDate, String endDate, boolean isBlack, int level) throws Exception {
        Map<String, Object> dauMap = new LinkedHashMap<>();//最终的日期对应的DAU的MAP
        String channelNames = "";
        if (channelName != null && channelName.length > 0) {
            List<String> collect = Arrays.stream(channelName).map(n -> "\'" + n + "\'").collect(Collectors.toList());
            channelNames = StringUtils.concat(collect, ",");
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
        List<String> roleLoginGoalTables = QueryUtil.getInstance().getQueryTables(ROLELOGINLOG, TableType.Month, startDate, endDate);
        for (Date d : dateList) {
            //某一天
            String s = sdf.format(d);
            List<Map<String, Object>> dauMapList = new ArrayList<>();
            for (String value : serverId) {
                //某一个服
                //key:serverId -> value:tableName
                Map<String, List<String>> roleLoginHefuTableMap;
                try {
                    roleLoginHefuTableMap = QueryUtil.getInstance().getHefuTable(value, ROLELOGINLOG, sdf.parse(s + " 00:00:00"), sdf.parse(s + " 23:59:59"));
                } catch (Exception e) {
                    e.printStackTrace();
                    return Toolkit.outResult(false, "查询数据异常，serverId：" + value);
                }
                for (String key : roleLoginHefuTableMap.keySet()) {
                    List<String> tableList = roleLoginHefuTableMap.get(key);
                    tableList.retainAll(roleLoginGoalTables);//过滤重复数据表
                    for (String item : tableList) {
                        Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                        if (dblog == null) {
                            continue;
                        }
                        String sqlStr = getDAUSql(item, channelNames, value, blackUserStr, s, s, level);
                        List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                        for (Map<String, Object> map : dataMap) {
                            boolean isHaveDay = false;//判断是否这一天已经存在在itemdataMapList里面了
                            if (!dauMapList.isEmpty()) {
                                for (Map<String, Object> itemMap : dauMapList) {
                                    if (map.get("userId").toString().equals(itemMap.get("userId").toString())) {
                                        isHaveDay = true;
                                        if (Integer.parseInt(map.get("level").toString()) > Integer.parseInt(itemMap.get("level").toString())) {
                                            itemMap.put("level", map.get("level").toString());
                                        }
                                    }
                                }
                            }
                            if (!isHaveDay) {
                                dauMapList.add(map);
                            }
                        }
                    }
                }
            }
            dauMap.put(s, dauMapList.size());
        }
        return Toolkit.outResult(true, dauMap);
    }

    //获取DAU的SQL
    private String getDAUSql(String table, String channelNames, String serverId, String blackUserStr, String startDate, String endDate, int level) {
        String sqlStr = "SELECT userId,level FROM (SELECT userId,max(level) level,platformName,sid ";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE time  BETWEEN UNIX_TIMESTAMP('" + startDate + " 00:00:00')  AND UNIX_TIMESTAMP('" + endDate + " 23:59:59')";
        sqlStr += " group by userId) t";
        sqlStr += " WHERE t.sid IN ('" + serverId + "')";
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " AND t.platformName IN (" + channelNames + ")";
        }
        if (!Strings.isBlank(blackUserStr)) {
            sqlStr += "AND t.userId NOT IN (" + blackUserStr + ")";
        }
        if (level != 0) {
            sqlStr += " AND t.level >= " + level;
        }

        return sqlStr;
    }
}
