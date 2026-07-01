package com.backend.module.log;

import com.backend.bean.Dblog;
import com.backend.filter.MenuFilter;
import com.backend.struct.log.LogRecord;
import com.backend.struct.log.LogType;
import com.backend.utils.LogUtil;
import com.backend.struct.log.LogFieldEntity;
import com.backend.utils.JsonUtils;
import com.backend.utils.QueryUtil;
import com.backend.utils.Toolkit;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

@IocBean
@Ok("json")
@At("/log")
@Fail("http:500")
public class LogModule {

    private static final Logger log = Logger.getLogger(LogModule.class);

    @Inject
    protected Dao dao;

    @At
    @Ok("forward:${obj}")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public String getPage(int logType, HttpServletRequest request) {
        String baseJSP = "/WEB-INF/jsp/log/baseLog.jsp";
        LogFieldEntity logEntity = LogUtil.getLogEntity(LogType.getClass(logType));
        request.setAttribute("nowDate", LogUtil.sdfhms.format(new Date()));
        request.setAttribute("logType", logType);
        request.setAttribute("fields", JsonUtils.toJSONString(logEntity.getFields()));
        request.setAttribute("conditions", JsonUtils.toJSONString(logEntity.getConditions()));
        request.setAttribute("crossLogType", logEntity.getCrossLogType());
        if(logType == 21){
            return "/WEB-INF/jsp/log/chat.jsp";
        }
        return baseJSP;
    }

    @At
    @Ok("forward:${obj}")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public String guildPage(int type, String groupName, String serverId, String guildId, HttpServletRequest request) {
        request.setAttribute("groupName", groupName);
        request.setAttribute("serverId", serverId);
        request.setAttribute("guildId", guildId);
        switch (type) {
            case 0:
                return "/WEB-INF/jsp/log/guild.jsp";
            case 1:
                return "/WEB-INF/jsp/log/guildmember.jsp";
            case 2:
                request.setAttribute("logType", LogType.GuildBaseLog);
                return "/WEB-INF/jsp/log/guildchange.jsp";
        }
        return "404.jsp";
    }

    @At
    public Object getLog(@Param("..") JSONObject json) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        Map<String, Object> params = LogUtil.parseCondition(json);
        int logType = Integer.valueOf(params.get("logType").toString());
        Class clazz = LogType.getClass(logType);
        if (clazz == null) {
            return Toolkit.outResult(false, msg.get("jsp.rolelog.nodata"));
        }
        LogRecord record = LogUtil.getLogEntityData(clazz, params);
        if (!record.getDatas().isEmpty()) {
            return Toolkit.outResult(true, record);
        }
        return Toolkit.outResult(false, msg.get("jsp.rolelog.nodata"));
    }

    @At
    public Object chat(@Param("..") JSONObject json) throws ParseException {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        Map<String, Object> params = LogUtil.parseCondition(json);
        int logType = Integer.valueOf(params.get("logType").toString());
        Class clazz = LogType.getClass(logType);
        if (clazz == null) {
            return Toolkit.outResult(false, msg.get("jsp.rolelog.nodata"));
        }
        LogRecord record = LogUtil.getLogEntityData(clazz, params);
        if (!record.getDatas().isEmpty()) {
            return Toolkit.outResult(true, record);
        }
        return Toolkit.outResult(false, msg.get("jsp.rolelog.nodata"));
    }

    @At
    public Object guild(int serverId) throws ParseException {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        String table = "guildbaselog";
        //1：创建公会，7：公会改名
        String sql = "select guildId, guildName from $table where type in (1, 7)";
        Date startDate = LogUtil.sdf.parse("1970-01-01");
        List<Dblog> dbList = QueryUtil.getInstance().checkHeFu(serverId, startDate , new Date());
        List<Map<String, String>> resultList = new ArrayList<>();
        List<Map<String, String>> tempList;
        for (Dblog db : dbList) {
            List<String> realTables = QueryUtil.getInstance().queryTables(db, table);
            for (String realTable : realTables) {
                tempList = QueryUtil.getInstance().query(db, sql, realTable);
                resultList.addAll(tempList);
            }
        }
        if (resultList.size() > 0) {
            return Toolkit.outResult(true, resultList);
        }
        return Toolkit.outResult(false, msg.get("jsp.rolelog.nodata"));
    }

    @At
    public Object guildMember(int serverId, long guildId) throws ParseException {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        String table = "guildbaselog";
        //join:1加入，0退出
        String joinSql = "select roleId, roleName, 1 as isJoin, time from $table where type in (1, 3) union all " +
                "select roleId, roleName, 0 as isJoin, time from $table where type = 4 union all " +
                "select actId, actName, 0 as isJoin, time from $table where type in (2, 5)";
        Date startDate = LogUtil.sdf.parse("1970-01-01");
        List<Dblog> dbList = QueryUtil.getInstance().checkHeFu(serverId, startDate , new Date());
        List<Map<String, String>> resultList = new ArrayList<>();
        List<Map<String, String>> joinList;
        for (Dblog db : dbList) {
            List<String> realTables = QueryUtil.getInstance().queryTables(db, table);
            for (String realTable : realTables) {
                joinList = QueryUtil.getInstance().query(db, joinSql, realTable);
                resultList.addAll(joinList);
            }
        }

        //筛选出最终存在在公会的成员
        Map<String, String> roleMap = new HashMap<>();
        Map<String, Map<String, String>> memberList = new HashMap<>();
        for (Map<String, String> tempMap : resultList) {
            String roleId = tempMap.get("roleId");
            String join = tempMap.get("isJoin");
            String time = tempMap.get("time");
            if (!roleMap.containsKey(roleId)) {
                if ("1".equals(join)) {
                    memberList.put(roleId, tempMap);
                }
                roleMap.put(roleId, time);
            } else {
                if ("1".equals(join) && Long.parseLong(time) > Long.parseLong(roleMap.get(roleId))) {
                    memberList.put(roleId, tempMap);
                    roleMap.put(roleId, time);
                } else {
                    if (Long.parseLong(time) > Long.parseLong(roleMap.get(roleId))) {
                        memberList.remove(roleId);
                        roleMap.put(roleId, time);
                    }
                }
            }
        }
        if (memberList.size() > 0) {
            return Toolkit.outResult(true, memberList.values());
        }
        return Toolkit.outResult(false, msg.get("jsp.rolelog.nodata"));
    }

    @At
    @POST
    @Ok("void")
    public void exportExcel(@Param("..") JSONObject json, HttpServletResponse response) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        Map<String, Object> param = LogUtil.parseCondition(json);
        param.remove("pageSize");
        Class clazz = LogType.getClass(Integer.parseInt(param.get("logType").toString()));
        List<Map<String, String>> resultList = LogUtil.getLogEntityData(clazz, param).getDatas();
        LogFieldEntity entity = LogUtil.getLogEntity(clazz);

        try (OutputStream out = response.getOutputStream()){
            response.reset();
            response.addHeader("content-type", "application/shlnd.ms-excel;charset=utf-8");
            String excelName = msg.get("log.logData");
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(excelName.getBytes(StandardCharsets.UTF_8), "ISO8859-1")
                    + ".xls");
            // 转码防止乱码
            genExcel(resultList, entity, out);
            out.flush();
        } catch (Exception e) {
            log.error(e);
        }
    }

    /**
     * 导出excel
     * @param dataList  日志记录
     * @param entity    日志实体元数据
     * @param out       输出
     */
    private void genExcel(List<Map<String, String>> dataList, LogFieldEntity entity, OutputStream out) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, entity.getTable());

        int ri = 0, ci = 0;
        HSSFRow row = sheet.createRow(ri++);
        HSSFCell cell;
        for (String name : entity.getFields().values()) {
            cell = row.createCell(ci);
            cell.setCellValue(name);
            ci++;
        }

        for (Map<String, String> dataMap : dataList) {
            row = sheet.createRow(ri++);
            ci = 0;
            for (String name : entity.getFields().keySet()) {
                cell = row.createCell(ci++);
                cell.setCellValue(dataMap.get(name) == null ? "" : dataMap.get(name));
            }
        }
        workbook.write(out);
    }

}
