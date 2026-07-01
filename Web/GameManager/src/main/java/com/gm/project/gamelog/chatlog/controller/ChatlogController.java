package com.gm.project.gamelog.chatlog.controller;

import java.util.*;

import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.dbclient.TableType;
import com.gm.common.utils.DateUtils;
import com.gm.common.utils.StringUtils;
import com.gm.common.utils.sql.SqlUtil;
import com.gm.framework.web.page.PageDomain;
import com.gm.framework.web.page.TableSupport;
import com.gm.project.common.utils.GameLogUtil;
import com.gm.project.gamelog.rolestate.domain.RoleState;
import com.gm.project.gmtool.server.domain.TServer;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Controller;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.project.gamelog.chatlog.domain.Chatlog;
import com.gm.project.gamelog.chatlog.service.IChatlogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 聊天日志Controller
 * 
 * @author gm
 * @date 2021-06-08
 */
@Controller
@RequestMapping("/gamelog/chatlog")
public class ChatlogController extends BaseController
{
    private String prefix = "gamelog/chatlog";

    @Autowired
    private IChatlogService chatlogService;

    @RequiresPermissions("gamelog:chatlog:view")
    @GetMapping()
    public String chatlog()
    {
        return prefix + "/chatlog";
    }
    /**
     * 查询聊天日志列表
     */
    @RequiresPermissions("gamelog:chatlog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Chatlog chatlog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        startPage();
        List<Chatlog> list = chatlogService.selectChatlogList(chatlog,param);
        return getDataTable(list);
    }

    @GetMapping("to_chatlog_monitor")
    public String to_chatlog_monitor()
    {
        return prefix + "/chatlog_monitor";
    }
    /**
     * 查询聊天日志列表
     */
    @PostMapping("/tableNameList")
    @ResponseBody
    public TableDataInfo tableNameList(Integer serverId)
    {
        List<String> tableNameList = new ArrayList<>();
        String tableName = "chatlog";
        //
        String startDate = DateUtils.getNewDateForMinute2(DateUtils.getDate(),-60*1440);
        String endDate = DateUtils.getDate();
        List<TServer> tServerList = DBServerMgr.getInstance().checkHeFu(serverId, DateUtils.parseDate(startDate), DateUtils.parseDate(endDate));
        List<String> realTables = DBServerMgr.getInstance().getQueryTables(tableName, TableType.Month, startDate, endDate);
        for (TServer db : tServerList) {
            List<String> tableList = DBServerMgr.getInstance().queryTables(db, tableName);
            if (tableList != null) {
                realTables.retainAll(tableList);
            }
            if (realTables.isEmpty()) {
                continue;
            }
            Collections.sort(realTables);
            for (String realTableName : realTables) {
                tableNameList.add(db.getServerId()+"_"+realTableName);
            }
        }

        return getDataTable(tableNameList);
    }

    /**
     * 查询聊天日志列表
     */
    @PostMapping("/chatlog_monitor")
    @ResponseBody
    public TableDataInfo chatlog_monitor(Chatlog chatlog,String startDate,String endDate,Integer serverId,String tableNameList)
    {

        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        Map<String,Object> param = new HashMap<>();
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        param.put("serverId",serverId);
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            param.put("pageNum",pageNum);
            param.put("pageSize",pageSize);
        }
        param.put("tableNameList",tableNameList);
        param.put("startDate",startDate);
        param.put("endDate",endDate);
        TableDataInfo tableDataInfo = getDataTable(new ArrayList<>());
        List<Chatlog> list = chatlogService.selectChatlogList(chatlog,param);
        if(param.containsKey("count")){
            tableDataInfo.setTotal((int)param.get("count"));
        }
        tableDataInfo.setRows(list);
        return tableDataInfo;
    }
    /**
     * 导出聊天日志列表
     */
    @RequiresPermissions("gamelog:chatlog:export")
    @Log(title = "聊天日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Chatlog chatlog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        List<Chatlog> list = chatlogService.selectChatlogList(chatlog,param);
        ExcelUtil<Chatlog> util = new ExcelUtil<Chatlog>(Chatlog.class);
        return util.exportExcel(list, "聊天日志数据");
    }

    /**
     * 禁言
     * @return
     */
    @GetMapping("/chatLogBanChat")
    public String chatLogBanChat(){
        return prefix + "/chatLogBanChat";
    }

    /**
     * 封号
     * @return
     */
    @GetMapping("/chatLogBanAccount")
    public String chatLogBanAccount(){
        return prefix + "/chatLogBanAccount";
    }

    @GetMapping("/chatLogHistory")
    public String chatLogHistory(Integer serverId, String startDate, String endDate, Long roleId,ModelMap map){
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,1000);
        Chatlog log = new Chatlog();
        log.setRoleId(roleId);
        List<Chatlog> list = chatlogService.selectChatlogList(log,param);
        map.put("chatlogList",list);
        return prefix + "/chatLogHistory";
    }


}
