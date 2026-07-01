package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.struct.log.TableType;
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
 * 该方法主要用来做宝贝阶数的统计（如神兵，坐骑，披风）
 */
@IocBean
@Ok("json")
@At("/lvdistriStatistic")
@Fail("http:500")
public class LvDistriStatisticModule {

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @At("/")
    @Ok("jsp:jsp.statistic.lvDistribute")
    public void index(HttpServletRequest request) {
        request.setAttribute("nowDate", sdf.format(new Date()));
    }

    /**
     * 坐骑统计
     */
    @At
    public Object horseLvStatistic(String serverId, String channelNames, String startDate, String endDate) throws Exception {
        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        String HORSEUPLOG = "horseuplog";
        List<String> horseUplogTables = QueryUtil.getInstance().getQueryTables(HORSEUPLOG, TableType.Month, startDate, endDate);
        Map<String, List<String>> hefuTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(serverId, HORSEUPLOG, start, end));
        List<Map<String, Object>> horseMapList = new ArrayList<>();
        for (String key : hefuTableMap.keySet()) {
            Dblog dblog = DbLogListManager.getInstance().getDblog(key);
            List<String> tableList = hefuTableMap.get(key);
            tableList.retainAll(horseUplogTables);//过滤重复数据表
            //计算出每日充值玩家
            for (String s : tableList) {
                //统计玩家充值时所出的等级，等级对应的充值总金额分布，次数分布
                String strSql = getHorseLvSql(s, channelNames, startDate, endDate);
                List<Map<String, Object>> data = QueryUtil.getInstance().query(dblog, strSql);
                horseMapList.addAll(data);
            }
        }
        return horseMapList;

    }

    private String getHorseLvSql(String table, String channelNames, String startDate, String endDate) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT S.beforeHorseLayer,COUNT(S.userId) as lvcount FROM (");
        str.append("SELECT DISTINCT h.beforeHorseLayer,h.userId FROM ").append(table).append(" h ");
        str.append("WHERE h.time BETWEEN UNIX_TIMESTAMP('").append(startDate).append(" 00:00:00') AND UNIX_TIMESTAMP('").append(endDate).append(" 23:59:59')");
        if (!Strings.isBlank(channelNames)) {
            str.append(" and platformName in (").append(channelNames).append(")");
        }
        str.append(")S GROUP BY S.beforeHorseLayer ");
        return str.toString();
    }


}





