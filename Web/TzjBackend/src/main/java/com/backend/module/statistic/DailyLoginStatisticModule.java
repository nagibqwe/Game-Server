package com.backend.module.statistic;

import com.backend.bean.Dblog;
import com.backend.bean.User;
import com.backend.filter.MenuFilter;
import com.backend.struct.log.TableType;
import com.backend.manager.DbLogListManager;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.ExportExcelUtil;
import com.backend.utils.QueryUtil;
import net.sf.json.JSONArray;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@IocBean
@Ok("json")
@At("/dailyLogin")
@Fail("http:500")
/**
 * 指定玩家登录记录
 */
public class DailyLoginStatisticModule {
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static String ROLELOGINLOG = "roleloginlog";
    private Map<User, List<Map<String, Object>>> dataMap = new HashMap<>();
    private String exName = "";

    @At
    @Ok("jsp:jsp.statistic.dailyLogin")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    //初始化界面
    public void index(HttpServletRequest request) {
        BackendLogUtil.getInstance().log(request, "进入指定玩家每日登录情况统计界面");
        Set<String> groupNames = DbLogListManager.getInstance().getPlatformDBNames();
        //获取平台列表
        request.setAttribute("groupNameList", groupNames);

        List<Map<String, Object>> dbServerList = new ArrayList<>();
        //获取平台对应游戏数据库列表
        for (String groupName : groupNames) {
            List<Dblog> dbServers = DbLogListManager.getInstance().getServerDBs(groupName);
            for (Dblog dbServer : dbServers) {
                Map<String, Object> dbServerMap = new HashMap<>();
                dbServerMap.put("groupName", dbServer.getGroupName());
                dbServerMap.put("serverType", dbServer.getServerType());
                dbServerMap.put("serverId", dbServer.getServerId());
                dbServerMap.put("serverName", dbServer.getServerName());
                dbServerList.add(dbServerMap);
            }
        }
        JSONArray ja = new JSONArray();
        ja.addAll(dbServerList);
        request.setAttribute("dbServerList", ja);

        Map<String, String> conditionMap = new TreeMap<>();
        conditionMap.put("userId", "账号ID(userId)");
        conditionMap.put("roleId", "角色ID(roleId)");
        conditionMap.put("roleName", "角色名称(roleName)");
        request.setAttribute("conditionMap", conditionMap);
        request.setAttribute("nowDate", sdf.format(new Date()));
    }

    @At
    /*
     * 指定玩家每日新建角色登录情况
     *
     */
    public Object getUserLogin(String serverIds, String condition, String conditionContent, String createStartDate, String createEndDate, String startDate, String endDate, int pageIndex, int pageSize) throws Exception {
        String judge = "getUserLogin,data";
        List<Map<String, Object>> dailyLoginMap = getData(judge, serverIds, condition, conditionContent, createStartDate, createEndDate, startDate, endDate, pageIndex, pageSize);
        exName = "SpecialPlayerCreateRoleLogin";
        dataMap.put((User) Mvcs.getHttpSession().getAttribute("USER"), dailyLoginMap);
        return dailyLoginMap;
    }

    @At
    /*
     * 指定玩家登录每日登录情况
     */
    public Object getRoleLogin(String serverIds, String condition, String conditionContent, String createStartDate, String createEndDate, String startDate, String endDate, int pageIndex, int pageSize) throws Exception {
        String judge = "getRoleLogin,data";
        List<Map<String, Object>> dailyLoginMap = getData(judge, serverIds, condition, conditionContent, createStartDate, createEndDate, startDate, endDate, pageIndex, pageSize);
        exName = "SpecialPlayerLogin";
        dataMap.put((User) Mvcs.getHttpSession().getAttribute("USER"), dailyLoginMap);
        return dailyLoginMap;
    }

    @At
    /*
     * 指定玩家每日新建角色登录情况的条数
     */
    public Object getUserLoginCounts(String serverIds, String condition, String conditionContent, String createStartDate, String createEndDate, String startDate, String endDate, int pageIndex, int pageSize) throws Exception {
        String judge = "getUserLoginCounts,count";
        return getData(judge, serverIds, condition, conditionContent, createStartDate, createEndDate, startDate, endDate, pageIndex, pageSize);
    }

    @At
    /*
     * 指定玩家登录每日登录情况的条数
     */
    public Object getRoleLoginCounts(String serverIds, String condition, String conditionContent, String createStartDate, String createEndDate, String startDate, String endDate, int pageIndex, int pageSize) throws Exception {
        String judge = "getRoleLoginCounts,count";
        return getData(judge, serverIds, condition, conditionContent, createStartDate, createEndDate, startDate, endDate, pageIndex, pageSize);
    }

    /**
     * @param judge            判断参数，获取不同所需的数据
     * @param serverIds        服务器ID
     * @param condition        传入的条件选项的值
     * @param conditionContent 传入条件的内容
     * @param createStartDate  传入角色创建开始时间
     * @param createEndDate    传入角色创建结束时间
     * @param pageIndex        分页的当前页
     * @param pageSize         分页每页显示的条数
     */
    private List<Map<String, Object>> getData(String judge, String serverIds, String condition, String conditionContent, String createStartDate, String createEndDate, String startDate, String endDate, int pageIndex, int pageSize) throws Exception {
        startDate = startDate + " 00:00:00";
        createStartDate = createStartDate + " 00:00:00";
        endDate = endDate + " 23:59:59";
        createEndDate = createEndDate + " 23:59:59";
        String[] serverId = serverIds.split(",");
        List<Map<String, Object>> dailyLoginMap = new ArrayList<>();
        for (String s : serverId) {
            List<String> goalTables = QueryUtil.getInstance().getQueryTables(ROLELOGINLOG,  TableType.Month, startDate, endDate);
            Map<String, List<String>> hefuTableMap = new TreeMap<>(QueryUtil.getInstance().getHefuTable(s, ROLELOGINLOG, sdf.parse(startDate), sdf.parse(endDate)));

            for (String key : hefuTableMap.keySet()) {
                List<String> tableList = hefuTableMap.get(key);
                tableList.retainAll(goalTables);//过滤重复数据表
                for (String value : tableList) {

                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    if (dblog == null) {
                        continue;
                    }
                    //获取sql
                    String sqlStr = "";
                    String sqlCountStr = "";
                    String[] judges = judge.split(",");
                    if (judges[1].equals("count")) {
                        if (judges[0].equals("getUserLoginCounts")) {
                            sqlCountStr = getUserLoginCountsSql(value, condition, conditionContent, createStartDate, createEndDate, startDate, endDate);
                        } else if (judges[0].equals("getRoleLoginCounts")) {
                            sqlCountStr = getRoleLoginCountsSql(value, condition, conditionContent, startDate, endDate);
                        }
                        Map<String, Object> countMap = new TreeMap<>();
                        int counts = QueryUtil.getInstance().queryCount(dblog, sqlCountStr);
                        countMap.put("counts", counts);
                        dailyLoginMap.add(countMap);
                    } else if (judges[1].equals("data")) {
                        if (judges[0].equals("getUserLogin")) {
                            sqlStr = getUserLoginSql(value, condition, conditionContent, createStartDate, createEndDate, startDate, endDate, pageIndex, pageSize);
                        } else if (judges[0].equals("getRoleLogin")) {
                            sqlStr = getRoleLoginSql(value, condition, conditionContent, startDate, endDate, pageIndex, pageSize);
                        }
                        List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, sqlStr);

                        for (Map<String, Object> map : dataMap) {
                            //columnName：列名
                            for (String columnName : map.keySet()) {
                                if (columnName.equals("roleId") || columnName.equals("userId")) {
                                    String idStr = map.get(columnName).toString();
                                    map.put(columnName, idStr);
                                }
                            }
                        }
                        dailyLoginMap.addAll(dataMap);
                    }
                }
            }
        }
        return dailyLoginMap;
    }

    //获取指定账号在某天新建角色，并且这些角色在某天登录的情况的sql
    private String getUserLoginSql(String table, String condition, String conditionContent, String createStartDate, String createEndDate, String startDate, String endDate, int pageIndex, int pageSize) {
        String strSql = "select userId,roleId,roleName,FROM_UNIXTIME(createTime) createTime,max(level) level,FROM_UNIXTIME(max(time)) time";
        strSql += " from " + table;
        strSql += " where " + condition + " in (" + conditionContent + ")";
        strSql += " and createTime BETWEEN UNIX_TIMESTAMP('" + createStartDate + "') AND UNIX_TIMESTAMP('" + createEndDate + "')";
        strSql += " and time BETWEEN UNIX_TIMESTAMP('" + startDate + "') AND UNIX_TIMESTAMP('" + endDate + "')";
        strSql += " group by roleId,DATE_FORMAT(FROM_UNIXTIME(time), '%Y-%m-%d') limit " + pageIndex + "," + pageSize;
        return strSql;
    }

    //获取指定账号在某天新建角色，并且这些角色在某天登录的情况的条数
    private String getUserLoginCountsSql(String table, String condition, String conditionContent, String createStartDate, String createEndDate, String startDate, String endDate) {
        String strSql = "select count(*) count from (select userId,roleId,roleName,FROM_UNIXTIME(createTime) createTime,max(level) level,FROM_UNIXTIME(max(time)) time";
        strSql += " from " + table;
        strSql += " where " + condition + " in (" + conditionContent + ")";
        strSql += " and createTime BETWEEN UNIX_TIMESTAMP('" + createStartDate + "') AND UNIX_TIMESTAMP('" + createEndDate + "')";
        strSql += " and time BETWEEN UNIX_TIMESTAMP('" + startDate + "') AND UNIX_TIMESTAMP('" + endDate + "')";
        strSql += " group by roleId,DATE_FORMAT(FROM_UNIXTIME(time), '%Y-%m-%d')) a ";
        return strSql;
    }

    //获取指定角色在某天登录的情况的SQL
    private String getRoleLoginSql(String table, String condition, String conditionContent, String startDate, String endDate, int pageIndex, int pageSize) {
        String strSql = "select userId,roleId,roleName,FROM_UNIXTIME(createTime) createTime,max(level) level,FROM_UNIXTIME(max(time)) time";
        strSql += " from " + table;
        strSql += " where " + condition + " in (" + conditionContent + ")";
        strSql += " and time BETWEEN UNIX_TIMESTAMP('" + startDate + "') AND UNIX_TIMESTAMP('" + endDate + "')";
        strSql += " group by roleId,DATE_FORMAT(FROM_UNIXTIME(time), '%Y-%m-%d') limit " + pageIndex + "," + pageSize;
        return strSql;
    }

    //获取指定角色在某天登录的情况的条数
    private String getRoleLoginCountsSql(String table, String condition, String conditionContent, String startDate, String endDate) {
        String strSql = "select count(*) count from (select userId,roleId,roleName,FROM_UNIXTIME(createTime) createTime,max(level) level,FROM_UNIXTIME(max(time)) time";
        strSql += " from " + table;
        strSql += " where " + condition + " in (" + conditionContent + ")";
        strSql += " and time BETWEEN UNIX_TIMESTAMP('" + startDate + "') AND UNIX_TIMESTAMP('" + endDate + "')";
        strSql += " group by roleId,DATE_FORMAT(FROM_UNIXTIME(time), '%Y-%m-%d')) a";
        return strSql;
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
        if (!dataMapList.isEmpty()) {
            ex.exportExcel(tableName, dataMapList, response, fileName);
        }
    }

}
