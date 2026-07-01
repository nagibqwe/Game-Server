package com.backend.module.operation;

import com.backend.bean.*;
import com.backend.filter.MenuFilter;
import com.backend.struct.log.TableName;
import com.backend.struct.log.TableType;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.manager.ItemManager;
import com.backend.manager.ServerListManager;
import com.backend.struct.ActiveCode;
import com.backend.struct.ActiveCodeByBatchGrid;
import com.backend.struct.ActiveCodeByCodeGrid;
import com.backend.struct.ActiveCodeShowGrid;
import com.backend.utils.*;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nutz.dao.*;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 运营管理
 */
@IocBean
@Ok("json")
@At("/operation")
@Fail("http:500")
public class OperationModule {
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat sdfhm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat tableMonth = new SimpleDateFormat("yyyyMM");

    private static final Logger log = Logger.getLogger(OperationModule.class);

    /**
     * 留存统计天数
     */
    private static final int[] durDays = {1, 2, 3, 4, 5, 6, 7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,44,59,119};

    @Inject
    private Dao dao;

    @Inject
    private Dao loginDao;

    @At
    @Ok("forward:${obj}")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public String getPage(int type, HttpServletRequest request) {
        switch (type) {
            case 1:
                return "/WEB-INF/jsp/operation/activecode.jsp";
            case 2:
                request.setAttribute("newDate", sdf.format(new Date()));
                return "/WEB-INF/jsp/operation/online.jsp";
            case 3:
                return "/WEB-INF/jsp/operation/activecodesearch.jsp";
            case 4:
                return "/WEB-INF/jsp/operation/allonline.jsp";
            case 5:
                return "/WEB-INF/jsp/operation/career.jsp";
            case 6:
                request.setAttribute("newDate", sdf.format(new Date()));
                return "/WEB-INF/jsp/operation/level.jsp";
            case 7:
                request.setAttribute("newDate", sdf.format(new Date()));
                return "/WEB-INF/jsp/operation/during.jsp";
            case 8:
                return "/WEB-INF/jsp/operation/blacklist.jsp";
            default:
                return "/404.jsp";
        }
    }

    @At
    public Object checkActiveCode(String code) {
        Sql codeSql = Sqls.create("select code from activecode where param = 1");
        codeSql.setCallback(Sqls.callback.strList());
        loginDao.execute(codeSql);
        List<String> list = codeSql.getList(String.class);
        boolean canUse = true;
        if (list != null && list.contains(code)) {
            canUse = false;
        }
        return Toolkit.outResult(canUse);
    }

    /**
     * 激活码生成
     */
    @At
    @POST
    @Ok("")
    public void activecode(int codeNum, @Param("..") ActiveCode activeCode, String[] serverId, String groupName,
                           HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        String activeName = activeCode.getActiveName();
        String items = activeCode.getItemList();
        if (Strings.isBlank(items)) {
            return;
        }
        int param = activeCode.getParam();
        Date valide_time_begin = null;
        Date valide_time_end = null;
        try {
            valide_time_begin = sdfhm.parse(activeCode.getValide_time_begin());
            valide_time_end = sdfhm.parse(activeCode.getValide_time_end());
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        if (!ItemManager.getInstance().checkItems(activeCode.getItemList())) {
            return;
        }

        // 为“0”表示所有渠道
        String channel = activeCode.getPlateform_name_big();
        String valide_server_id_list = "[]";
        if (serverId != null) {
            valide_server_id_list = Arrays.toString(serverId);
        }

        int batchId = 0;
        List<String> codes;

        if (param == 1) {
            Sql codeSql = Sqls.create("select code from activecode where param = 1");
            codeSql.setCallback(Sqls.callback.strList());
            loginDao.execute(codeSql);
            List<String> list = codeSql.getList(String.class);
            if (list != null && list.contains(activeCode.getCode())) {
                return;
            }
            codes = Collections.singletonList(activeCode.getCode());
        } else {
            //生成激活码
            batchId = dao.getMaxId(CodeBatch.class) + 1;
            // 判断是否已经拥有批号
            if (dao.count(CodeBatch.class, Cnd.where("batchId", "=", batchId)) > 0) {
                batchId += 1;
            }
            // 使用set集合去除重复激活码
            Set<String> codeSet = new HashSet<>();
            while (codeSet.size() < codeNum) {
                codeSet.add(RandomUtil.sg(5).next() + batchId);
            }
            codes = new ArrayList<>(codeSet);
        }

        //激活码存入各自平台的LS库中
        long createTime = System.currentTimeMillis() / 1000;
        // 分批存入数据库
        int batchSize = 1000;//每一批上限，因为国内app(rds云数据库)插入有问题，减少成1000
        String sqlStr = "INSERT INTO activecode (code, activeName, batch, itemList,param,valide_time_begin," +
                "valide_time_end,plateform_name_big,valide_server_id_list,create_time,deleteTime) " +
                "VALUES (@code, @activeName, @batch, @itemList,@param,@valide_time_begin,@valide_time_end," +
                "@plateform_name_big,@valide_server_id_list,@create_time,@deleteTime);";
        if (codeNum > batchSize) {
            int k = 0;// 用于判断插入剩余数据
            Sql sql = Sqls.create(sqlStr);
            for (int i = 0; i < codeNum; i++) {
                sql.params().set("code", codes.get(i));
                sql.params().set("activeName", activeName);
                sql.params().set("batch", batchId);
                sql.params().set("itemList", items);
                sql.params().set("param", param);
                sql.params().set("valide_time_begin", valide_time_begin);
                sql.params().set("valide_time_end", valide_time_end);
                sql.params().set("plateform_name_big", channel);
                sql.params().set("valide_server_id_list", valide_server_id_list);
                sql.params().set("create_time", createTime);
                sql.params().set("deleteTime", 0);
                sql.addBatch();
                // batchSize条插入一次
                if (i % batchSize == 0) {
                    loginDao.execute(sql);
                    if (sql.getUpdateCount() < 1) {
                        log.error(msg.get("log.dbfail"));
                    }
                    sql.clearBatch();// 清理已操作批次
                    k = i;
                }
            }
            if (k < codeNum - 1) {
                // 最后插入不足batchSize条的数据
                loginDao.execute(sql);
                if (sql.getUpdateCount() < 1) {
                    log.error(msg.get("log.dbfail"));
                }
            }
        } else {
            Sql sql = Sqls.create(sqlStr);
            for (int i = 0; i < codeNum; i++) {
                sql.params().set("code", codes.get(i));
                sql.params().set("activeName", activeName);
                sql.params().set("batch", batchId);
                sql.params().set("itemList", items);
                sql.params().set("param", param);
                sql.params().set("valide_time_begin", valide_time_begin);
                sql.params().set("valide_time_end", valide_time_end);
                sql.params().set("plateform_name_big", channel);
                sql.params().set("valide_server_id_list", valide_server_id_list);
                sql.params().set("create_time", createTime);
                sql.params().set("deleteTime", 0);
                sql.addBatch();
            }
            loginDao.execute(sql);
            if (sql.getUpdateCount() < 1) {
                log.error(msg.get("log.dbfail"));
            }
        }
        log.error("激活码存入登录服数据库成功，平台标识 :" + activeCode.getPlateform_name_big() + ",批次号:" + batchId + ",数量:" + codeNum);

        // 保存批号记录
        if (batchId != 0) {
            User user = (User) request.getSession().getAttribute("USER");
            CodeBatch codeBatch = new CodeBatch();
            codeBatch.setBatchId(batchId);
            codeBatch.setUserId(user.getId());
            codeBatch.setTime(System.currentTimeMillis());
            codeBatch.setPlatform(groupName);
            codeBatch.setIsUniversal(param);
            CodeBatch batch = dao.insert(codeBatch);
            if (batch == null) {
                log.error("批次号为" + batchId + "的激活码信息录入GM后台失败！");
            } else {
                log.info("批次号为" + batchId + "的激活码信息录入GM后台成功！");
            }
            BackendLogUtil.getInstance().log(request, "生成激活码[平台:" + activeCode.getPlateform_name_big() + "\t批号:" + batchId + "\t数量:" + codeNum + "\t万能码：" + (param == 1) + "]");
        }

        try (OutputStream out = response.getOutputStream()) {
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String excelName = msg.get("jsp.acodesearch.code");
            // 转码防止乱码
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(excelName.getBytes(UTF_8), "ISO8859-1") + ".xlsx");
            genExcel(codes, batchId, out);
            out.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @At
    public Object deleteCode(String code) {
        if (Strings.isBlank(code)) {
            return Toolkit.outResult(false, "激活码错误");
        }
        String sqlStr = "update activecode set deleteTime=@deleteTime where code=@code and deleteTime = 0";
        Sql sql = Sqls.create(sqlStr);
        sql.setParam("deleteTime", System.currentTimeMillis() / 1000 + "");
        sql.setParam("code", code);
        loginDao.execute(sql);
        int count = sql.getUpdateCount();
        if (count < 0) {
            return Toolkit.outResult(false, "操作失败");
        }
        return Toolkit.outResult(true, "操作完成");
    }

    @At
    public Object queryActiveCodeByBatchId(@Param("page")int page,@Param("rows")int rows,@Param("batchId")String batchId) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        int start = 0;
        start = (page - 1) * rows;
        if (!Strings.isNumber(batchId)) {
            return Toolkit.outResult(false, msg.get("log.nobatchinfo"));
        }
        //根据批号获取平台名
        CodeBatch codeBatch = dao.fetch(CodeBatch.class, Cnd.where("batchId", "=", batchId));
        if (codeBatch == null) {
            return Toolkit.outResult(false, msg.get("log.nobatchinfo"));
        }

        String sqlStr = "select code, activeName, batch, itemList, param, valide_time_begin, valide_time_end, plateform_name_big, plateform_name_small, valide_server_id_list, create_time, get_time, get_player_id, get_account_id, get_plateform_aid, get_plateform_name,get_server_id from $table where batch=@batch limit "+start+","+rows+"";
        String sqlStr2 = "select code, activeName, batch, itemList, param, valide_time_begin, valide_time_end, plateform_name_big, plateform_name_small, valide_server_id_list, create_time, get_time, get_player_id, get_account_id, get_plateform_aid, get_plateform_name,get_server_id from $table where batch=@batch";
        String table = "activecode";
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("batch", batchId);
        List<Map<String, String>> result = QueryUtil.getInstance().query(loginDao, sqlStr, table, paraMap);
        List<Map<String, String>> resultCount = QueryUtil.getInstance().query(loginDao, sqlStr2, table, paraMap);
        if (result.isEmpty()) {
            return Toolkit.outResult(false, msg.get("log.nodata"));
        }
        result = getNewList(result, msg);
        List<ActiveCodeByBatchGrid> grids = new ArrayList<>();
        for (Map<String, String> map:result){
            ActiveCodeByBatchGrid grid = new ActiveCodeByBatchGrid();
            grid.setCode(map.get("code"));
            grid.setActiveName(map.get("activeName"));
            grid.setBatch(map.get("batch"));
            grid.setItemList(map.get("itemList"));
            grid.setParam(map.get("param"));
            grid.setValide_time_begin(map.get("valide_time_begin"));
            grid.setValide_time_end(map.get("valide_time_end"));
            grid.setPlateform_name_big(map.get("plateform_name_big"));
            grid.setValide_server_id_list(map.get("valide_server_id_list"));
            grid.setCreate_time(TimeUtils.format2string(Long.parseLong(map.get("create_time")) * 1000L));
            grid.setGet_time(map.get("get_time"));
            grid.setGet_player_id(map.get("get_player_id"));
            grid.setGet_plateform_aid(map.get("get_plateform_aid"));
            grid.setGet_account_id(map.get("get_account_id"));
            grid.setGet_plateform_name(map.get("get_plateform_name"));
            grid.setGet_server_id(map.get("get_server_id"));
            grids.add(grid);
        }

        return Toolkit.outResult(true).setv("total",resultCount.size()).setv("rows",grids);
    }

    @At
    public Object queryActiveCodeByCode(@Param("page")int page,@Param("rows")int rows,@Param("code")String code) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        int start = 0;
        start = (page - 1) * rows;
        if (Strings.isBlank(code) || code.length() < 6) {
            return Toolkit.outResult(false, msg.get("log.acodeerror"));
        }
        String sqlStr = "select activeCode,platformName,sid,roleid,userId,itemList,actionId from $table where activeCode=@activeCode limit "+start+","+rows+"";
        String sqlStr2 = "select activeCode,platformName,sid,roleid,userId,itemList,actionId from $table where activeCode=@activeCode";
        String table = "activecodelog";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("activeCode", code);
        List<Map<String, String>> result = QueryUtil.getInstance().query(loginDao, sqlStr, table, paramMap);
        List<Map<String, String>> resultCount = QueryUtil.getInstance().query(loginDao, sqlStr2, table, paramMap);
        if (!result.isEmpty()) {
            result = getNewList(result, msg);
        }
        if (result.isEmpty()) {
            return Toolkit.outResult(false, msg.get("log.noacodeinfo"));
        }
        List<ActiveCodeByCodeGrid> grids = new ArrayList<>();
        for (Map<String,String> map:result){
            ActiveCodeByCodeGrid grid = new ActiveCodeByCodeGrid();
            grid.setActiveCode(map.get("activeCode"));
            grid.setPlatformName(map.get("platformName"));
            grid.setSid(map.get("sid"));
            grid.setRoleId(map.get("roleid"));
            grid.setUserId(map.get("userId"));
            grid.setItemList(map.get("itemList"));
            grid.setActionId(map.get("actionId"));
            grids.add(grid);
        }
        return Toolkit.outResult(true).setv("total",resultCount.size()).setv("rows",grids);
    }

    @At
    public Object queryActiveCode(@Param("page") int page,@Param("rows") int rows) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        List<Map<String, String>> result = new ArrayList<>();
        int start = 0;
        start = (page - 1) * rows;
        String sql = "select code, activeName, batch, itemList, param, valide_time_begin, valide_time_end, " +
                "plateform_name_big, plateform_name_small, valide_server_id_list, create_time, deleteTime from $table order by batch asc limit "+start+","+rows+"";

        String sql2 = "select code, activeName, batch, itemList, param, valide_time_begin, valide_time_end, " +
                "plateform_name_big, plateform_name_small, valide_server_id_list, create_time, deleteTime from $table order by batch asc";
        String table = "activecode";
        List<Map<String, String>> dataList = null;
        List<Map<String, String>> dataListCount = null;
        try {
            dataList = QueryUtil.getInstance().query(loginDao, sql, table, new HashMap<>());
            dataListCount = QueryUtil.getInstance().query(loginDao, sql2, table, new HashMap<>());
            if (!dataList.isEmpty()) {
                dataList = getNewList(dataList, msg);
                result.addAll(dataList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<ActiveCodeShowGrid> grids = new ArrayList<>();
        for (Map<String, String> map:dataList){
            ActiveCodeShowGrid grid = new ActiveCodeShowGrid();
            grid.setCode(map.get("code"));
            grid.setActiveName(map.get("activeName"));
            grid.setBatch(map.get("batch"));
            grid.setItemList(map.get("itemList"));
            grid.setParam(map.get("param"));
            grid.setValide_time_begin(map.get("valide_time_begin"));
            grid.setValide_time_end(map.get("valide_time_end"));
            grid.setPlateform_name_big(map.get("plateform_name_big"));
            grid.setValide_server_id_list(map.get("valide_server_id_list"));
            grid.setCreate_time(TimeUtils.format2string(Long.parseLong(map.get("create_time")) * 1000L));
            if (map.get("deleteTime").equals("0")){
                grid.setDeleteTime(String.valueOf(0));
            }else {
                grid.setDeleteTime(TimeUtils.format2string(Long.parseLong(map.get("deleteTime")) * 1000L));
            }
            grids.add(grid);
        }
        return Toolkit.outResult(true).setv("total",dataListCount.size()).setv("rows",grids);
    }

    private List<Map<String, String>> getNewList(List<Map<String, String>> list, Map<String, String> msg) {
        for (Map<String, String> map : list) {
            if (map.containsKey("itemList")) {
                String itemList = map.get("itemList");
                String itemStr = "";
                String[] items = itemList.split(";");
                for (int i = 0; i < items.length; i++) {
                    String[] item = items[i].split(",");
                    if (item.length > 0) {
                        if (Strings.isBlank(item[0]) || "null".equalsIgnoreCase(item[0])) {
                            continue;
                        }
                        Item item0 = ItemManager.getInstance().getItemList().get(Integer.parseInt(item[0]));
                        itemStr += "[" + item[0] + "]" + (item0 == null ? "[未知]" : item0.getItemName());
                        if (item.length > 1) {
                            itemStr += "_" + msg.get("log.itemNum") + item[1];
                            itemStr += "_" + msg.get("log.isBind");
                            if (item.length > 2) {
                                if (item[2].equals("1")) {
                                    itemStr += msg.get("log.bind");
                                } else {
                                    itemStr += msg.get("log.noBind");
                                }
                            }
                        }
                        if (i < (items.length - 1)) {
                            itemStr += ";";
                        }
                    }
                }
                map.put("itemList", itemStr);
            }
        }
        return list;
    }

    @At
    public Object onlineCount(int serverId, String date) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        String sqlStr = "SELECT num,time FROM $table where serverId=@serverId and DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')=@day order by time desc limit 0,1";
        String table = "t_servernum";
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("serverId", serverId);
        paraMap.put("day", date);
        List<Map<String, String>> list = QueryUtil.getInstance().query(dao, sqlStr, table, paraMap);
        if (list.isEmpty()) {
            return Toolkit.outResult(false, msg.get("log.nodata"));
        }
        return Toolkit.outResult(true, list);
    }

    @At
    public Object refreshOnlineCount(int serverId) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        String sqlStr = "SELECT num,time FROM $table where serverId=@serverId order by time desc limit 0,1";
        String table = "t_servernum";
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("serverId", serverId);
        List<Map<String, String>> list = QueryUtil.getInstance().query(dao, sqlStr, table, paraMap);
        if (list.isEmpty()) {
            return Toolkit.outResult(false, msg.get("log.nodata"));
        }
        return Toolkit.outResult(true, list);
    }

    @At
    @Filters
    public Object allOnlineCount() {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        String sqlStr = "SELECT a.serverId,a.day,a.hour,a.min,a.num,a.time FROM $table a,(SELECT b.serverid,MAX(b.time) as maxtime FROM $table b where b.time>unix_timestamp(now()) - 3600*1 GROUP BY b.serverid) c WHERE a.serverid = c.serverid AND a.time = c.maxtime order by a.num desc";
        String table = "t_servernum";
        Map<String, Object> paraMap = new HashMap<>();
        List<Map<String, String>> list = QueryUtil.getInstance().query(dao, sqlStr, table, paraMap);
        if (list.isEmpty()) {
            return Toolkit.outResult(false, msg.get("log.nodata"));
        }
        Server server;
        String serverName;
        for (Map<String, String> map : list) {
            server = ServerListManager.getInstance().getServer(map.get("serverId"));
            serverName = server == null ? "" : server.getServerName();
            map.put("serverName", serverName);
        }
        return Toolkit.outResult(true, list);
    }

    @At
    public Object online(int serverId, String date) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        String sqlStr = "SELECT serverId,num,time FROM $table where serverId=@serverId and DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')=@day order by time asc";
        String table = "t_servernum";
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("serverId", serverId);
        paraMap.put("day", date);
        List<Map<String, String>> list = QueryUtil.getInstance().query(dao, sqlStr, table, paraMap);
        if (list.isEmpty()) {
            return Toolkit.outResult(false, msg.get("log.nodata"));
        }
        return Toolkit.outResult(true, list);
    }

    @At
    public Object career(String channelNames, int serverId) {

        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        String sqlStr = "select count(roleId) as rolecount,career from $table where createsid=@serverId";
        if (!Strings.isBlank(channelNames)) {
            sqlStr += " and platformName in (" + channelNames + ")";
        }
        sqlStr += " group by career";
        String table = "rolestate";
        Dblog dblog = dao.fetch(Dblog.class,
                Cnd.where("serverId", "=", serverId));
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("serverId", serverId);
        List<Map<String, String>> list = QueryUtil.getInstance().query(dblog, sqlStr, table, paraMap);
        if (list.isEmpty()) {
            return Toolkit.outResult(false, msg.get("log.nodata"));
        }
        return Toolkit.outResult(true, list);
    }

    /*
     * 统计选择时间内创建的角色截止目前为止的等级变化。这里离线挂机后等级会发生变化。
     */
    @At
    public Object level(String channelNames, String serverId, int condition, String level, String startDate, String endDate) throws Exception {

        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        List<Map<String, String>> list = new ArrayList<>();
        if (condition == 0) {
            String tableName = "roleloginlog";
            List<String> goalTables = QueryUtil.getInstance().getQueryTables(tableName, TableType.Month, startDate, endDate);

            Map<String, List<String>> hefuTableMap = new TreeMap<>(QueryUtil.getInstance().getHefuTable(serverId, tableName, sdf.parse(startDate), sdf.parse(endDate)));

            for (String key : hefuTableMap.keySet()) {
                List<String> tableList = hefuTableMap.get(key);
                tableList.retainAll(goalTables);//过滤重复数据表
                for (String s : tableList) {
                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    if (dblog == null) {
                        continue;
                    }
                    String sqlStr = "SELECT COUNT(t.roleId) as rolecount,t.level FROM (SELECT roleId, MAX(level) as level FROM $table";
                    sqlStr += " WHERE sid=@serverId";
                    Map<String, Object> paraMap = new HashMap<>();
                    paraMap.put("serverId", serverId);

                    if (!Strings.isBlank(startDate)) {
                        sqlStr += " and createTime>= UNIX_TIMESTAMP(@startDate)";
                        startDate += " 00:00:00";
                        paraMap.put("startDate", startDate);
                    }
                    if (!Strings.isBlank(endDate)) {
                        sqlStr += " and createTime<= UNIX_TIMESTAMP(@endDate)";
                        endDate += " 23:59:59";
                        paraMap.put("endDate", endDate);
                    }
                    if (!Strings.isBlank(channelNames)) {
                        sqlStr += " and platformName in (" + channelNames + ")";
                    }
                    sqlStr += " GROUP BY roleId) t";
                    if (!level.equals("")) {
                        sqlStr += " WHERE t.level >= @level";
                        paraMap.put("level", level);
                    }
                    sqlStr += " GROUP BY t.level";
                    list = QueryUtil.getInstance().query(dblog, sqlStr, s, paraMap);
                }
            }
        } else if (condition == 1) {
            String sqlStr = "select count(roleId) as rolecount,level from $table where createsid=@serverId ";
            String table = "rolestate";
            Dblog dblog = dao.fetch(Dblog.class, Cnd.where("serverId", "=", serverId));
            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("serverId", serverId);
            if (!level.equals("")) {
                sqlStr += " and level >= @level";
                paraMap.put("level", level);
            }

            if (!Strings.isBlank(startDate)) {
                sqlStr += " and createTime>=@startDate";
                startDate += " 00:00:00";
                paraMap.put("startDate", startDate);
            }
            if (!Strings.isBlank(endDate)) {
                sqlStr += " and createTime<=@endDate";
                endDate += " 23:59:59";
                paraMap.put("endDate", endDate);
            }
            if (!"".equals(channelNames)) {
                sqlStr += " and platformName in (" + channelNames + ")";
            }
            sqlStr += " group by level;";
            list = QueryUtil.getInstance().query(dblog, sqlStr, table, paraMap);
        }

        if (list.isEmpty()) {
            return Toolkit.outResult(false, msg.get("log.nodata"));
        }
        return Toolkit.outResult(true, list);
    }

    /**
     * 统计留存  新增玩家是指当前注册时账号，
     * 付费玩家是指当日的新增玩家中只要之后付过费的玩家都叫当日的付费玩家
     */
    @At
    public Object getDuring(String actionType, String groupName, String channelNames, String[] serverId, String startDate, String endDate, boolean isblack) throws Exception {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        if (!Strings.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        if (loginDao == null) {
            return Toolkit.outResult(false, msg.get("jsp.role.nolslog"));
        }
        List<Date> dateList = getDateList(startDate, endDate);
        Calendar calendarStart = Calendar.getInstance();
        Set<String> firstLogUserId;
        Set<String> serfirstLogUserId; //用于查找登录用户的处理
        Set<String> firstRechargeUserId;
        Set<String> dayFirstLogUserId = new HashSet<>();;//总的新登录用户
        List<Map<String, Object>> firstLoginCount;//新注册用户
        List<Map<String, Object>> laterLoginCount;//后面登陆
        List<String> dsStr;
        List<String> loginGoalTables;
        List<String> rechargeGoalTables;
        Map<String, List<String>> hefuTableMap;
        Map<String, List<String>> hefuRechargeTableMap;
        List<String> tableList;
        int registerCount;
        String firstLogUserIdStr; //新用户ID
        List<Map<String, Object>> data;
        Map<String, Object> durMap;

        Set<String> blackFirstLoginData;//首日注册
        List<Map<String, Object>> blackLaterLoginData;//后面登陆
        Set<String> firstUserIds;

        List<Integer> countDataStatistic;// 用来放留存数
        List<Float> rateDataStatistic;// 用来存放留存率
        List<Object> resultList = new ArrayList<>();
        Map<String, Object> resultMap;
        for (Date d : dateList) {
            String s = sdf.format(d);
            calendarStart.setTime(d);
            dsStr = new ArrayList<>();
            dsStr.add(s);
            durMap = new HashMap<>();// 放入将每天的留存数
            durMap.put(s, 0);// 第一天的注册
            for (int dayNum : durDays) {
                calendarStart.add(Calendar.DAY_OF_MONTH, dayNum);
                String ds = sdf.format(calendarStart.getTime());
                dsStr.add(ds);
                calendarStart.add(Calendar.DAY_OF_MONTH, -dayNum);
                durMap.put(ds, 0);//初始化
            }
            int dsLength = dsStr.size();
            registerCount = 0;
            firstLogUserId = new HashSet<>();
            serfirstLogUserId = new HashSet<>();
            firstRechargeUserId = new HashSet<>();
            laterLoginCount = new ArrayList<>();
            for (String sid : serverId) {
                //新注册用户数据
                int finalServerId = QueryUtil.getInstance().getHeFuId(Integer.parseInt(sid));
                Dblog rolestatedblog = DbLogListManager.getInstance().getDBServer(groupName, finalServerId);
                String firstSqlStr = getUserRegisterSql(channelNames, TableName.RoleState, sid, dsStr.get(0), dsStr.get(0));
                firstLoginCount = QueryUtil.getInstance().query(rolestatedblog, firstSqlStr);
                for (Map<String, Object> firstMap : firstLoginCount) {
                    String uid = firstMap.get("userId").toString();
                    if(!dayFirstLogUserId.contains(uid)) {
                        firstLogUserId.add(uid);
                        dayFirstLogUserId.add(uid);
                    }
                    serfirstLogUserId.add(uid);
                }
                firstLogUserIdStr = serfirstLogUserId.toString();
                firstLogUserIdStr = firstLogUserIdStr.substring(1, firstLogUserIdStr.length() - 1);

                //充值数据
//                rechargeGoalTables = QueryUtil.getInstance().getQueryTables(TableName.RechargeSuccess, s + " 00:00:00", dsStr.get(durDays.length) + "23:59:59", 4);
                rechargeGoalTables = Arrays.asList(TableName.RechargeSuccess);
                hefuRechargeTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(sid, TableName.RechargeSuccess, sdf.parse(s + " 00:00:00"), sdf.parse(dsStr.get(durDays.length) + " 23:59:59")));
                for (String key : hefuRechargeTableMap.keySet()) {
                    tableList = hefuRechargeTableMap.get(key);
                    tableList.retainAll(rechargeGoalTables);//过滤重复数据表
                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    if (dblog == null) {
                        continue;
                    }
                    for (String table : tableList) {
                        if (firstLogUserIdStr.length() != 0) {
                            String laterSqlStr = getRechargeUserSql(channelNames, table, sid, firstLogUserIdStr);
                            data = QueryUtil.getInstance().query(dblog, laterSqlStr);
                            for (Map<String, Object> map : data) {
                                firstRechargeUserId.add(map.get("userId").toString());
                            }
                        }
                        if (actionType.equals("getRechargeDuringList")) {
                            firstLogUserIdStr = firstRechargeUserId.toString();
                            firstLogUserIdStr = firstLogUserIdStr.substring(1, firstLogUserIdStr.length() - 1);
                        }
                    }
                }

                //登陆数据
                loginGoalTables = QueryUtil.getInstance().getQueryTables(TableName.RoleLogin, TableType.Month, s + " 00:00:00", dsStr.get(durDays.length) + " 23:59:59");
                hefuTableMap = new HashMap<>(QueryUtil.getInstance().getHefuTable(sid, TableName.RoleLogin, sdf.parse(s + " 00:00:00"), sdf.parse(dsStr.get(durDays.length) + " 23:59:59")));
                for (String key : hefuTableMap.keySet()) {
                    tableList = hefuTableMap.get(key);
                    tableList.retainAll(loginGoalTables);//过滤重复数据表
                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    if (dblog == null) {
                        continue;
                    }
                    for (String table : tableList) {
                        data = new ArrayList<>();
                        if (firstLogUserIdStr.length() != 0) {
                            String laterSqlStr = getUserLoginSql(channelNames, table, key, firstLogUserIdStr, dsStr.get(1), dsStr.get(dsLength - 1));
                            data = QueryUtil.getInstance().query(dblog, laterSqlStr);
                        }
                        laterLoginCount.addAll(data);
                    }
                }
            }

            //排除黑名单
            if (isblack) {
                blackFirstLoginData = new HashSet<>();//首日注册
                blackLaterLoginData = new ArrayList<>();//后面登陆
                List<Object> blackListUsers = BlackListManager.getInstance().getBlackListUsers(groupName);
                if (actionType.equals("getAllDuringList")) {//所有玩家
                    for (String userId : firstLogUserId) {
                        if (blackListUsers.contains(userId)) {
                            blackFirstLoginData.add(userId);
                        }
                    }
                } else if (actionType.equals("getRechargeDuringList")) {//付费玩家
                    for (String userId : firstRechargeUserId) {
                        if (blackListUsers.contains(userId)) {
                            blackFirstLoginData.add(userId);
                        }
                    }
                }

                for (Map<String, Object> laterLoginMap : laterLoginCount) {//后面登陆
                    String userId = laterLoginMap.get("userId").toString();
                    if (blackListUsers.contains(userId)) {
                        blackLaterLoginData.add(laterLoginMap);
                    }
                }
                if (actionType.equals("getAllDuringList")) {
                    firstLogUserId.removeAll(blackFirstLoginData);
                } else if (actionType.equals("getRechargeDuringList")) {
                    firstRechargeUserId.removeAll(blackFirstLoginData);
                }
                laterLoginCount.removeAll(blackLaterLoginData);
            }

            if (actionType.equals("getAllDuringList")) {
                registerCount = firstLogUserId.size();
            } else if (actionType.equals("getRechargeDuringList")) {
                registerCount = firstRechargeUserId.size();
            }

            durMap.put(s, registerCount);
            // 将首次登陆注册的用户数量，进行统计
            for (int i = 1; i < dsStr.size(); i++) {
                String dss = dsStr.get(i) + " 00:00:00";
                String dse = dsStr.get(i) + " 23:59:59";
                Date dssd = sdfhm.parse(dss);
                Date dsed = sdfhm.parse(dse);
                firstUserIds = new HashSet<>();
                for (Map<String, Object> latermap : laterLoginCount) {
                    String dsm = latermap.get("time") + " 00:00:01";
                    String userId = latermap.get("userId").toString();
                    Date dsmd = sdfhm.parse(dsm);
                    if (dsmd.after(dssd) && dsmd.before(dsed)) {
                        firstUserIds.add(userId);
                    }
                }
                // 求交集
                if (actionType.equals("getAllDuringList")) {
                    firstUserIds.retainAll(firstLogUserId);
                } else if (actionType.equals("getRechargeDuringList")) {
                    firstUserIds.retainAll(firstRechargeUserId);
                }

                int laterLogCount = firstUserIds.size() + Integer.parseInt(durMap.get(dsStr.get(i)).toString());// 计算出后面天数用户的留存数
                durMap.put(dsStr.get(i), laterLogCount);
            }

            // 计算概率
            countDataStatistic = new ArrayList<>();// 用来放留存数
            rateDataStatistic = new ArrayList<>();// 用来存放留存率
            resultMap = new HashMap<>();
            for (int i = 1; i < dsStr.size(); i++) {
                int firstDayAdd = Integer.parseInt(durMap.get(dsStr.get(0))
                        .toString());
                int keepCount = Integer.parseInt(durMap.get(dsStr.get(i))
                        .toString());
                float rate = 0;
                if (firstDayAdd != 0) {
                    rate = (float) keepCount / firstDayAdd;
                }
                countDataStatistic.add(keepCount);
                rateDataStatistic.add(rate);
            }
            resultMap.put("date", s);
            resultMap.put("registerCount", registerCount);
            resultMap.put("countDataStatistic", countDataStatistic);
            resultMap.put("rateDataStatistic", rateDataStatistic);
            resultList.add(resultMap);
        }
        if (resultList.size() == 0) {
            return Toolkit.outResult(false, msg.get("log.nodata"));
        }
        return new NutMap().setv("ok", true).setv("myData", resultList).setv("durDays", durDays);
    }


    private Set<String> getAllRegisterUserList(List<Date> dateList,String channelNames,String groupName,List<String> serverId){
        List<String> dsStr;
        List<Map<String, Object>> firstLoginCount;//新注册用户
        Set<String> firstLogUserId  = new HashSet<>(); ;
        for (Date d : dateList) {
            String s = sdf.format(d);
            for (String sid : serverId) {
                //新注册用户数据
                int finalServerId = QueryUtil.getInstance().getHeFuId(Integer.parseInt(sid));
                Dblog rolestatedblog = DbLogListManager.getInstance().getDBServer(groupName, finalServerId);
                String firstSqlStr = getUserRegisterSql(channelNames, TableName.RoleState, sid, s, s);
                firstLoginCount = QueryUtil.getInstance().query(rolestatedblog, firstSqlStr);
                for (Map<String, Object> firstMap : firstLoginCount) {
                    firstLogUserId.add(firstMap.get("userId").toString());
                }
            }
        }
        return firstLogUserId;
    }

    private List<Date> getDateList(String startDate, String endDate) throws ParseException {
        List<Date> dateList = new ArrayList<>();
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        int dvalue = dValue(start, end);
        long oneday = 1000 * 60 * 60 * 24;
        long dateTime = start.getTime();
        Date date;
        for (int i = 0; i < dvalue + 1; i++) {
            date = new Date(dateTime);
            dateList.add(date);
            dateTime += oneday;
        }
        return dateList;
    }

    @At
    public Object getBlackList(String condition, Pager pager) {
        Condition cnd;
        if (Strings.isBlank(condition)) {
            cnd = Cnd.orderBy().desc("id");
        } else {
            cnd = Cnd.where("userNumber", "like", "%" + condition + "%");
        }
        List<BlackUser> blackUserList = dao.query(BlackUser.class, cnd, pager);
        pager.setRecordCount(dao.count(BlackUser.class, cnd));
        QueryResult result = new QueryResult(blackUserList, pager);
        return Toolkit.outResult(true, result);
    }

    @At
    @Ok("forward:${obj}")
    @AdaptBy(type = UploadAdaptor.class, args = {"${base}/WEB-INF/tmp", "81920", "UTF-8"})
    public String upLoadBlackExcl(@Param("tempFile") TempFile tempFile) throws IOException {
        File file = tempFile.getFile();
        InputStream is = new FileInputStream(file);
        HSSFWorkbook wb = new HSSFWorkbook(is);
        HSSFSheet sheet = wb.getSheetAt(0);
        long userNumber;
        String platform;
        List<BlackUser> buList = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            HSSFRow row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            HSSFCell userNumberCell = row.getCell(0);
            HSSFCell platFormCell = row.getCell(1);
            if (userNumberCell == null) {
                continue;
            }
            if (platFormCell == null) {
                continue;
            }
            userNumber = Long.parseLong(userNumberCell.getStringCellValue());
            platform = platFormCell.getStringCellValue();
            BlackUser bu = dao.fetch(BlackUser.class, Cnd.where("userNumber", "=", userNumber));
            if (bu != null) {
                continue;
            } else {
                bu = new BlackUser();
            }
            bu.setPlatform(platform);
            bu.setUserNumber(userNumber);
            buList.add(bu);
        }
        dao.insert(buList);
        return "/WEB-INF/jsp/operation/blacklist.jsp";
    }

    @At
    @AdaptBy(type = UploadAdaptor.class, args = {"${base}/WEB-INF/tmp", "81920", "UTF-8"})
    public Object blackListConvert(@Param("tempFile") TempFile tempFile, @Param("serverId") String serverId, @Param("platform") String platform) throws IOException {
        File file = tempFile.getFile();
        InputStream is = new FileInputStream(file);
        HSSFWorkbook wb = new HSSFWorkbook(is);
        HSSFSheet sheet = wb.getSheetAt(0);
        StringBuilder userIdStr = new StringBuilder();
        List<Map<String, Object>> blackList = BlackListManager.getInstance().getBlackList(platform);
        List<String> nameList = new ArrayList<>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            HSSFRow row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            HSSFCell userNameCell = row.getCell(0);
            if (userNameCell == null) {
                continue;
            }
            userIdStr.append("'").append(userNameCell.getStringCellValue()).append("',");
            nameList.add(userNameCell.toString());
        }
        userIdStr = new StringBuilder(userIdStr.substring(0, userIdStr.length() - 1));
        Dblog dblog = DbLogListManager.getInstance().getDblog(serverId);

        if (dblog == null) {
            return null;
        }
        List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, ("SELECT rs.userId,rs.rolename FROM rolestate rs WHERE rs.rolename IN (" + userIdStr + ")"));
        List<BlackUser> buList = new ArrayList<>();
        List<String> roleNameList = new ArrayList<>();
        for (Map<String, Object> map : dataMap) {
            String userId0 = map.get("userId").toString();
            String roleName = map.get("roleName").toString();
            roleNameList.add(roleName);
            int flag = 0;
            for (Map<String, Object> blackMap : blackList) {
                String userId1 = blackMap.get("userId").toString();
                if (userId1.equals(userId0)) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 1) {
                continue;
            }
            BlackUser blackUser = new BlackUser();
            blackUser.setUserNumber(Long.parseLong(userId0));
            blackUser.setPlatform(platform);
            buList.add(blackUser);
            map.put("platform", platform);
            blackList.add(map);
        }
        if (buList.size() > 0) {
            BlackListManager.getInstance().setDataList(blackList);
            dao.insert(buList);
        }
        nameList.removeAll(roleNameList);
        String resultStr;
        if (nameList.size() > 0) {
            resultStr = "黑名单中存在角色名找不到账号，以下角色名为：" + nameList;
        } else {
            resultStr = "黑名单导入完成！";
        }
        return resultStr;
    }

    private void genExcel(List<String> rows, int batchId, OutputStream out) throws Exception {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(msg.get("jsp.acodesearch.batch") + "_" + batchId);
        String[] header = {msg.get("jsp.acodesearch.code")};
        // 创建表头
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell;
        for (int ci = 0; ci < header.length; ci++) {
            cell = row.createCell(ci);
            cell.setCellValue(header[ci]);
        }
        // 创建数据
        for (int ri = 0; ri < rows.size(); ri++) {
            row = sheet.createRow(ri + 1);
            cell = row.createCell(0);
            cell.setCellValue(rows.get(ri));
        }
        workbook.write(out);
    }

    /**
     * 计算起始天数的差值
     */
    private int dValue(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    private String getUserRegisterSql(String channelNames, String table, String serverId, String startDate, String endDate) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT t2.userId");
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

    private String getUserLoginSql(String channelNames, String table, String serverId, String firstLogUserIdStr, String startDate, String endDate) {
        StringBuilder str = new StringBuilder();
        str.append("select * from (SELECT DISTINCT rl.userId,DATE_FORMAT(FROM_UNIXTIME(rl.time),'%Y-%m-%d') time , platformName , sid FROM " + table + " rl ");
        str.append("WHERE rl.time>= UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND rl.time<= UNIX_TIMESTAMP('" + endDate + " 23:59:59') ");
        if (!Strings.isBlank(channelNames)) {
            str.append(" and rl.platformName in (" + channelNames + ")");
        }
        str.append(" AND rl.userId IN (" + firstLogUserIdStr + ") AND rl.sid IN ('" + serverId + "')) S");
        return str.toString();
    }

    private String getRechargeUserSql(String channelNames, String table, String serverId, String firstLogUserIdStr) {
        StringBuilder str = new StringBuilder();
        str.append("select userId from  " + table);
        str.append(" WHERE userId IN (" + firstLogUserIdStr + ") and sid in ('" + serverId + "')");
        if (!Strings.isBlank(channelNames)) {
            str.append(" and platformName in (" + channelNames + ")");
        }
        str.append(" and status = 1 and statusReason = 7 group by userId ");
        return str.toString();
    }

    @At
    public Object deleteBlack(int id) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        dao.delete(BlackUser.class, id);
        return Toolkit.outResult(true, msg.get("db.update.success"));
    }

    /**
     * 统计留存  新增玩家是指当前注册时账号，付费玩家是指当日的新增玩家中只要之后付过费的玩家都叫当日的付费玩家
     */
    @At
    public Object getDuring1(String actionType, String groupName, String channelNames, String[] serverId, String startDate, String endDate, boolean isblack) throws Exception {
        List<Object> resultList = new ArrayList<>();

        HashMap<String,Object> resultMap1 = new HashMap<>();
        resultMap1.put("date", "2021-02-18");
        resultMap1.put("registerCount", 926);
        List<Integer> a1 = Arrays.asList(353,220,189,170,125,99,86,74,90,70,46,38,32,28,10);
        List<Float> a2 = Arrays.asList(38.12f/100,23.76f/100,20.41f/100,18.36f/100,13.5f/100,10.69f/100,9.29f/100,7.99f/100,9.72f/100,7.56f/100,4.97f/100,4.1f/100,3.46f/100,3.02f/100,1.08f/100);
        resultMap1.put("countDataStatistic", a1);
        resultMap1.put("rateDataStatistic", a2);
        resultList.add(resultMap1);

        HashMap<String,Object> resultMap2 = new HashMap<>();
        resultMap2.put("date", "2021-02-17");
        resultMap2.put("registerCount", 1002);
        List<Integer> b1 = Arrays.asList(397,272,221,170,159,114,106,106,98,76,67,62,52,46,15);
        List<Float> b2 = Arrays.asList(39.62f/100,27.15f/100,22.06f/100,16.97f/100,15.87f/100,11.38f/100,10.58f/100,10.58f/100,9.78f/100,7.58f/100,6.69f/100,6.19f/100,5.19f/100,4.59f/100,1.5f/100);
        resultMap2.put("countDataStatistic", b1);
        resultMap2.put("rateDataStatistic", b2);
        resultList.add(resultMap2);

        HashMap<String,Object> resultMap3 = new HashMap<>();
        resultMap3.put("date", "2021-02-16");
        resultMap3.put("registerCount", 1253);
        List<Integer> c1 = Arrays.asList(493,319,279,225,173,122,125,110,109,106,91,76,70,70,30);
        List<Float> c2 = Arrays.asList(39.35f/100,25.46f/100,22.27f/100,17.96f/100,13.81f/100,9.74f/100,9.98f/100,8.78f/100,8.7f/100,8.46f/100,7.26f/100,6.07f/100,5.59f/100,5.59f/100,2.39f/100);
        resultMap3.put("countDataStatistic", c1);
        resultMap3.put("rateDataStatistic", c2);
        resultList.add(resultMap3);

        HashMap<String,Object> resultMap4 = new HashMap<>();
        resultMap4.put("date", "2021-02-15");
        resultMap4.put("registerCount", 1186);
        List<Integer> d1 = Arrays.asList(490,304,284,233,194,139,132,134,124,116,101,82,84,80,27);
        List<Float> d2 = Arrays.asList(41.32f/100,25.63f/100,23.95f/100,19.65f/100,16.36f/100,11.72f/100,11.13f/100,11.3f/100,10.46f/100,9.78f/100,8.52f/100,6.91f/100,7.08f/100,6.75f/100,2.28f/100);
        resultMap4.put("countDataStatistic", d1);
        resultMap4.put("rateDataStatistic", d2);
        resultList.add(resultMap4);

        HashMap<String,Object> resultMap5 = new HashMap<>();
        resultMap5.put("date", "2021-02-14");
        resultMap5.put("registerCount", 927);
        List<Integer> e1 = Arrays.asList(380,254,248,183,149,104,107,93,88,72,69,66,38,42,10);
        List<Float> e2 = Arrays.asList(40.99f/100,27.4f/100,26.75f/100,19.74f/100,16.07f/100,11.22f/100,11.54f/100,10.03f/100,9.49f/100,7.77f/100,7.44f/100,7.12f/100,4.1f/100,4.53f/100,1.08f/100);
        resultMap5.put("countDataStatistic", e1);
        resultMap5.put("rateDataStatistic", e2);
        resultList.add(resultMap5);

        HashMap<String,Object> resultMap6 = new HashMap<>();
        resultMap6.put("date", "2021-02-13");
        resultMap6.put("registerCount", 753);
        List<Integer> f1 = Arrays.asList(300,187,161,106,83,68,64,62,55,42,50,40,38,32,15);
        List<Float> f2 = Arrays.asList(39.84f/100,24.83f/100,21.38f/100,14.08f/100,11.02f/100,9.03f/100,8.5f/100,8.23f/100,7.3f/100,5.58f/100,6.64f/100,5.31f/100,5.05f/100,4.25f/100,1.99f/100);
        resultMap6.put("countDataStatistic", f1);
        resultMap6.put("rateDataStatistic", f2);
        resultList.add(resultMap6);

        HashMap<String,Object> resultMap7 = new HashMap<>();
        resultMap7.put("date", "2021-02-12");
        resultMap7.put("registerCount", 763);
        List<Integer> g1 = Arrays.asList(290,196,169,137,120,88,88,75,79,59,52,48,38,32,15);
        List<Float> g2 = Arrays.asList(38.01f/100,25.69f/100,22.15f/100,17.96f/100,15.73f/100,11.53f/100,11.53f/100,9.83f/100,10.35f/100,7.73f/100,6.82f/100,6.29f/100,4.98f/100,4.19f/100,1.97f/100);
        resultMap7.put("countDataStatistic", g1);
        resultMap7.put("rateDataStatistic", g2);
        resultList.add(resultMap7);

        HashMap<String,Object> resultMap8 = new HashMap<>();
        resultMap8.put("date", "2021-02-11");
        resultMap8.put("registerCount", 974);
        List<Integer> h1 = Arrays.asList(378,237,201,172,143,122,96,99,83,76,69,68,52,42,20);
        List<Float> h2 = Arrays.asList(38.81f/100,24.33f/100,20.64f/100,17.66f/100,14.68f/100,12.53f/100,9.86f/100,10.16f/100,8.52f/100,7.8f/100,7.08f/100,6.98f/100,5.34f/100,4.31f/100,2.05f/100);
        resultMap8.put("countDataStatistic", h1);
        resultMap8.put("rateDataStatistic", h2);
        resultList.add(resultMap8);

        HashMap<String,Object> resultMap9 = new HashMap<>();
        resultMap9.put("date", "2021-02-10");
        resultMap9.put("registerCount", 1098);
        List<Integer> i1 = Arrays.asList(457,270,236,191,141,115,112,102,99,74,67,54,40,28,7);
        List<Float> i2 = Arrays.asList(41.62f/100,24.59f/100,21.49f/100,17.4f/100,12.84f/100,10.47f/100,10.2f/100,9.29f/100,9.02f/100,6.74f/100,6.1f/100,4.92f/100,3.64f/100,2.55f/100,0.64f/100);
        resultMap9.put("countDataStatistic", i1);
        resultMap9.put("rateDataStatistic", i2);
        resultList.add(resultMap9);

        HashMap<String,Object> resultMap10 = new HashMap<>();
        resultMap10.put("date", "2021-02-09");
        resultMap10.put("registerCount", 1203);
        List<Integer> j1 = Arrays.asList(513,306,256,217,175,147,137,128,109,108,71,58,62,56,20);
        List<Float> j2 = Arrays.asList(42.64f/100,25.44f/100,21.28f/100,18.04f/100,14.55f/100,12.22f/100,11.39f/100,10.64f/100,9.06f/100,8.98f/100,5.9f/100,4.82f/100,5.15f/100,4.66f/100,1.66f/100);
        resultMap10.put("countDataStatistic", j1);
        resultMap10.put("rateDataStatistic", j2);
        resultList.add(resultMap10);

        return new NutMap().setv("ok", true).setv("myData", resultList).setv("durDays", durDays);
    }
}
