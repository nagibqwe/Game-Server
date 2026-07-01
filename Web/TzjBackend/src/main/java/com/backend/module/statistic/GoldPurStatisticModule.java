package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.filter.MenuFilter;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.QueryUtil;
import org.apache.log4j.Logger;
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

/**
 * 元宝用途统计
 */
@IocBean
@Ok("json")
@At("/goldpurstatistic")
@Fail("http:500")
public class GoldPurStatisticModule {
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger log = Logger.getLogger(GoldPurStatisticModule.class);

    @At("/")
    @Ok("jsp:jsp.statistic.goldConsume")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index(HttpServletRequest request) {
        BackendLogUtil.getInstance().log(request, "进入元宝用途统计页面");
        request.setAttribute("nowDate", sdf.format(new Date()));
    }

    @At
    public Object goldConsume(String groupName, String[] serverId, String channelNames, String startDate, String endDate, int goldType, boolean isBlack) throws Exception {
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

        String table = "goldchangelog";
        if (goldType == 2) {
            table = "bindgoldchangelog";
        }

        String blackUsers = null;
        if (isBlack) {
            List<Object> blackList = BlackListManager.getInstance().getBlackListUsers(groupName);
            if (!blackList.isEmpty()) {
                blackUsers = listToString(blackList);
            }
        }
        List<String> queryTables = QueryUtil.getInstance().getQueryTables(table, TableType.Month, start, end);

        Map<String, List<String>> hefuTableMap = new HashMap<>();
        for (String id : serverId) {
            hefuTableMap.putAll(QueryUtil.getInstance().getHefuTable(id, table, sdf.parse(startDate), sdf.parse(endDate)));
        }
        List<Map<String, Object>> resultMap = new ArrayList<>();
        List<String> tableList;
        String sqlStr;
        Dblog dblog;
        List<Map<String, Object>> dataMap;
        for (String sid : hefuTableMap.keySet()) {
            tableList = hefuTableMap.get(sid);
            tableList.retainAll(queryTables);//过滤重复数据表
            for (String s : tableList) {
                sqlStr = getGoldConsumeSql(sid, s, channelNames, startDate, endDate, blackUsers);
                dblog = DbLogListManager.getInstance().getDblog(sid);
                if (dblog == null) {
                    continue;
                }
                dataMap = QueryUtil.getInstance().query(dblog, sqlStr);
                resultMap.addAll(dataMap);
            }
        }
        if (resultMap.isEmpty()) {
            return new NutMap().setv("ok", false).setv("msg", Mvcs.getMessages(Mvcs.getReq()).get("log.nodata"));
        }
        return new NutMap().setv("ok", true).setv("data", resultMap);
    }

    private String getGoldConsumeSql(String serverId, String table, String channelNames, String startDate, String endDate, String blackList) {
        StringBuilder str = new StringBuilder();
        str.append("select reason,count(distinct(userid)) users,sum(changenum) totalConsume,sid from ");
        str.append(table);
        str.append(" where changenum<0 and sid in(");
        str.append(serverId);
        str.append(") and time between UNIX_TIMESTAMP('");
        str.append(startDate);
        str.append(" 00:00:00') and UNIX_TIMESTAMP('");
        str.append(endDate);
        str.append(" 23:59:59') ");
        if (!Strings.isBlank(channelNames)) {
            str.append(" and platformName in (").append(channelNames).append(") ");
        }
        if (blackList != null) {
            str.append("and userid not in(");
            str.append(blackList);
            str.append(") ");
        }
        str.append("group by reason");
        return str.toString();
    }

    private String listToString(List<Object> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (Object string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string.toString());
        }
        return result.toString();
    }
}
