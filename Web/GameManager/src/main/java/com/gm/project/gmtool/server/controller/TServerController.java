package com.gm.project.gmtool.server.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.MessageUtils;
import com.gm.common.utils.StringUtils;
import com.gm.common.utils.text.Convert;
import com.gm.framework.config.GameManagerConfig;
import com.gm.framework.web.page.PageDomain;
import com.gm.project.gmtool.selectgroup.service.ISelectGroupService;
import com.gm.project.gmtool.utils.GMLogUtil;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import com.gm.project.gmtool.utils.HttpConnectionUtils;
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
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;


/**
 * Сервер列Controller
 *
 * @author gm
 * @date 2021-07-14
 */
@Controller
@RequestMapping("/gmtool/server")
public class TServerController extends BaseController {
    private String prefix = "gmtool/server";

    @Autowired
    private ITServerService tServerService;

    @Autowired
    private ISelectGroupService selectGroupService;

    @Autowired
    private GameManagerConfig gameManagerConfig;

    @RequiresPermissions("gmtool:server:view")
    @GetMapping()
    public String server() {
        return prefix + "/server";
    }

    /**
     * 单个连接的Данные库查询页面
     * @return
     */
    @RequiresPermissions("gmtool:server:customSqlPage")
    @GetMapping("/customSqlPage")
    public String customSqlPage() {
        return prefix + "/customSql";
    }

    @GetMapping("/selectserver")
    public String selectserver(HttpServletRequest request,String modelName, ModelMap mmap) {
        List<String> groupList = selectGroupService.selectServerGroup();
        String selectGroupName = "";
        String requestUrl = request.getParameter("requestUrl");
        String ignoreMerge = request.getParameter("ignoreMerge");
        switch (modelName){
            case "serverList":
                selectGroupName = getSelectGroupName(request,groupList,selectGroupName);
                putMmap(mmap,modelName,selectGroupName,groupList,requestUrl,ignoreMerge);
                mmap.put("selectServerIdList" , request.getParameter("selectServerIdList"));
                break;
            case "activityServerList":
                selectGroupName = getSelectGroupName(request,groupList,selectGroupName);
                putMmap(mmap,modelName,selectGroupName,groupList,requestUrl,ignoreMerge);
                mmap.put("actIds",request.getParameter("actIds"));
//                mmap.put("operationType",request.getParameter("operationType"));
                break;
            default:
                selectGroupName = getSelectGroupName(request,groupList,selectGroupName);
                putMmap(mmap,modelName,selectGroupName,groupList,requestUrl,ignoreMerge);
                break;

        }
        return prefix + "/selectserver" ;
    }

    /**
     * 获取选中的group(没有选中则默认取№一个)
     * @param request
     * @param groupList
     * @param selectGroupName
     * @return
     */
    private String getSelectGroupName(HttpServletRequest request,List<String> groupList,String selectGroupName){
        selectGroupName = request.getParameter("selectGroupName");
        if (StringUtils.isEmpty(selectGroupName)){
            if (null != groupList && groupList.size() > 0){
                selectGroupName = groupList.get(0);
            }
        }
        return selectGroupName;
    }

    /**
     * 通用需要传递的值
     * @param mmap
     * @param modelName
     * @param selectGroupName
     * @param groupList
     * @param requestUrl
     * @param ignoreMerge
     */
    private void putMmap(ModelMap mmap,String modelName,String selectGroupName,List<String> groupList,String requestUrl,String ignoreMerge){
        mmap.put("modelName",modelName);
        mmap.put("selectGroupName" , selectGroupName);
        mmap.put("groupList" , groupList);
        mmap.put("requestUrl" , requestUrl);
        mmap.put("ignoreMerge" , ignoreMerge);
    }

    /**
     * 查询Список серверов
     */
    @RequiresPermissions("gmtool:server:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TServer tServer) {
        startPage();
        List<TServer> list = tServerService.selectTServerListShow(tServer);
        return getDataTable(list);
    }

    /**
     * ЭкспортСписок серверов
     */
    @RequiresPermissions("gmtool:server:export")
    @Log(title = "Список серверов", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TServer tServer) {
        List<TServer> list = tServerService.selectTServerListShow(tServer);
        ExcelUtil<TServer> util = new ExcelUtil<TServer>(TServer.class);
        return util.exportExcel(list, "Сервер列Данные");
    }

    /**
     * ДобавитьСписок серверов
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * ДобавитьСохранитьСписок серверов
     */
    @RequiresPermissions("gmtool:server:add")
    @Log(title = "Список серверов", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TServer tServer) {
        int row = tServerService.insertTServer(tServer);
        if (row > 0){
            GMLogUtil.log("增加СерверИнформация，Id:" + tServer.getId());
        }
        return toAjax(row);
    }

    /**
     * ИзменитьСписок серверов
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        TServer tServer = tServerService.selectTServerById(id);
        mmap.put("tServer", tServer);
        return prefix + "/edit";
    }

    /**
     * ИзменитьСохранитьСписок серверов
     */
    @RequiresPermissions("gmtool:server:edit")
    @Log(title = "Список серверов", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TServer tServer) {
        int row = tServerService.updateTServer(tServer);
        if (row > 0){
            GMLogUtil.log("ИзменитьСерверИнформация，Id:" + tServer.getId());
            DBServerMgr.getInstance().clearDBClient(tServer,null);
        }
        return toAjax(row);
    }

    /**
     * 直接УдалитьСписок серверов
     */
    @RequiresPermissions("gmtool:server:remove")
    @Log(title = "Список серверов", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        int row = tServerService.deleteTServerByIds(ids);
        String[] idsArr = Convert.toStrArray(ids);
        for (String id:idsArr){
            TServer tServer = tServerService.selectTServerById(Long.valueOf(id));
            DBServerMgr.getInstance().clearDBClient(tServer,null);
        }
        GMLogUtil.log("УдалитьСерверИнформация，Id:" + ids);
        return toAjax(row);
    }

    /**
     * 清理Объединение серверовДанные
     */
    @PostMapping("/cleanCombine")
    @ResponseBody
    public AjaxResult cleanCombine(Long id) {
        if (id < 1) {
            return AjaxResult.error(MessageUtils.message("server.id.error"));
        }
        TServer srcSer = tServerService.selectTServerById(id);
        if (srcSer == null) {
            return AjaxResult.error(MessageUtils.message("server.combine.noExist"));
        }
        if (srcSer.getIsHeFu() < 1) {
            return AjaxResult.error(MessageUtils.message("server.combine.not"));
        }
        TServer srcDBlog = tServerService.selectTServerByServerId(srcSer.getServerId());
        if (srcDBlog == null) {
            return AjaxResult.error(MessageUtils.message("server.combine.noDblog"));
        }

        String srcSerStr = srcDBlog.getServerIdList();
        int destSId = srcSer.getHefuServerID();

        // 检查Журнал库的Изменить记录， 如果有， 则可以清理， 如果没有Да不可以清理的
        TServer destDBlog = tServerService.selectTServerByServerId(destSId);
        if (destDBlog == null) {
            return AjaxResult.error(MessageUtils.message("server.combine.noDblog"));
        }

        String destSerStr = destDBlog.getServerIdList();
        if (destSerStr.contains(srcSerStr)) {
            destDBlog.setServerIdList(destSerStr.replace(srcSerStr, ""));
        }

        int no = tServerService.updateTServer(destDBlog);
        if (no < 1) {
            return AjaxResult.error(MessageUtils.message("server.combine.clearUpLogerror"));
        }

        srcDBlog.setIsHeFu(0);
        srcDBlog.setHefuServerID(0);
        srcDBlog.setHefuTime(new Date(0));
        tServerService.updateTServer(srcDBlog);

        srcSer.setHefuServerID(0);
        srcSer.setHefuTime(new Date(0));
        srcSer.setIsHeFu(0);

        no = tServerService.updateTServer(srcSer);
        if (no < 1) {
            return AjaxResult.error(MessageUtils.message("db.update.failure"));
        }

        return AjaxResult.success(MessageUtils.message("db.update.success"));
    }

    /**
     * ТестовыйСервер连接
     *
     * @return
     */
    @PostMapping("/test")
    @ResponseBody
    public Object test(String ids) {
        if (null == ids || "".equals(ids)){
            return AjaxResult.error("传入的id值有误");
        }
        String[] idsArr = Convert.toStrArray(ids);
        TServer server = null;
        StringBuilder sb = new StringBuilder();
        List<Integer> serverSuccessList = new ArrayList<>();
        List<Integer> serverFailedList = new ArrayList<>();
        for (String sid:idsArr){
            long id = Long.parseLong(sid);
            server = tServerService.selectTServerById(id);
            if (null == server) {
                sb.append("id为"+id+"的Сервер不存在");
            }else {
                switch (server.getServerType()) {
                    case 0:
                    case 1:
                    case 4:
                        AjaxResult resultMap = GameServerRequestUtil.gmOrderSendMess(server, "gmTest", "",8000);
                        boolean isOk = Boolean.valueOf(resultMap.get("ok").toString());
                        if (isOk){
                            serverSuccessList.add(server.getServerId());
                        }else {
                            serverFailedList.add(server.getServerId());
                        }
                        break;
                    case 2:
                    case 3:
                        try {
                            String url = "http://" + server.getServerIP() + ":" + server.getServerPort() + "/test";

                            HashMap<String, String> paramMap = new HashMap<String, String>();
                            paramMap.put("secret_key", gameManagerConfig.getRequestServerKey());
                            String res = HttpConnectionUtils.post(url, paramMap);
                            serverSuccessList.add(server.getServerId());
//                            return AjaxResult.success(res);
                        } catch (Exception e) {
                            serverFailedList.add(server.getServerId());
                            e.printStackTrace();
                        }
                }
            }
        }

        sb.append("游戏服连接Список успешных：").append(serverSuccessList).append("\n");
        sb.append("游戏服连接Ошибка列表：").append(serverFailedList).append("\n");
        return AjaxResult.info(sb.toString()).put("ok",true);
    }

    /**
     * ТестовыйДанные库连接
     * @param id
     * @return
     */
    @PostMapping("/dbTest")
    @ResponseBody
    public Object dbTest(Long id){
        TServer server = tServerService.selectTServerById(id);
        if (server == null) {
            return AjaxResult.error(MessageUtils.message("server.combine.noExist"));
        }
        DBClient dbClient = DBServerMgr.getInstance().getLogDBClient(server);
        try {
            Connection connection = dbClient.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.error("ТестовыйОшибка!请检查配置或者权限");
        }
        return AjaxResult.success("ТестовыйУспешно!");
    }

    @PostMapping("/checkInput")
    @ResponseBody
    public AjaxResult checkInput(int serverId) {

        TServer tServer = new TServer();
        tServer.setServerId(serverId);
        List<TServer> servers = tServerService.selectTServerByInput(tServer);
        if (servers.size() > 0) {
            return AjaxResult.error("ID сервера重复");
        } else {
            return AjaxResult.success("ID сервера可用");
        }
    }

    /**
     * 改变СерверУдалёнСтатус(Включить/禁用)
     * @param id
     * @param isDeleted
     * @return
     */
    @PostMapping("/changeIsDeleted")
    @ResponseBody
    public AjaxResult changeIsDeleted(int id,int isDeleted) {

        TServer tServer = new TServer();
        tServer.setId(Long.parseLong(String.valueOf(id)));
        tServer.setIsDeleted(isDeleted);
        int row = tServerService.updateTServer(tServer);
        if (row > 0){
            GMLogUtil.log("ИзменитьСерверДаНет可用Статус，Id:" + tServer.getId());
            TServer server = tServerService.selectTServerById(Long.parseLong(String.valueOf(id)));
            DBServerMgr.getInstance().clearDBClient(server,null);
        }
        return toAjax(row);
    }



    /**
     * 查询游戏Список серверов
     * @param groupName Платформа分组标识
     * @return
     */

    @PostMapping("/gameServerByServerType")
    @ResponseBody
    public TableDataInfo gameServerByServerType(String groupName, int serverType) {
        TServer server = new TServer();
        server.setGroupName(groupName);
        server.setServerType(serverType);
        List<TServer> list = this.tServerService.selectTServerList(server);
        //List<TServer> list = selectGroupService.selectGameServerByServerType(groupName, serverType);
        return getDataTable(list);
    }

    /**
     * 自定义sql语句字段
     * @param serverId
     * @param sqlStr
     * @return
     */
    @PostMapping("/customSqlField")
    @ResponseBody
    public AjaxResult customSqlField(int serverId,String sqlStr) {

        DBClient logDBClient = DBServerMgr.getInstance().getLogDBClient(serverId);
        List<String> fields = new ArrayList<>();
        List<Map<String, Object>> dataMap = new ArrayList<>();
        if (logDBClient == null){
            AjaxResult.info("Данные库无法获取连接！").put("ok",false);
        }else {
            dataMap = logDBClient.selectList(sqlStr);
            if (null == dataMap){
                return AjaxResult.info("可能Да网络、sql语句、权限问题造成了错误！").put("ok",false);
            }
            if (dataMap.size() > 0){
                getFields(fields,dataMap);
            }else {
                return AjaxResult.info("查询Данные为空！").put("ok",false);
            }
        }

        return AjaxResult.info("查询Успешно").put("fields",fields).put("ok",true);
    }

    /**
     * 自定义sql语句查询
     * @param serverId
     * @param sqlStr
     * @return
     */
    @PostMapping("/customSql")
    @ResponseBody
    public AjaxResult customSql(PageDomain pageDomain,int serverId, String sqlStr) {

        int page = pageDomain.getPageNum();
        int rows = pageDomain.getPageSize();

        DBClient logDBClient = DBServerMgr.getInstance().getLogDBClient(serverId);
        List<String> fields = new ArrayList<>();
        List<Map<String, Object>> dataMap = new ArrayList<>();
        if (logDBClient == null){
            AjaxResult.info("Данные库无法获取连接！").put("ok",false);
        }else {
            dataMap = logDBClient.selectList(sqlStr);
            if (dataMap.size() > 0){
                getFields(fields,dataMap);
            }else {
                AjaxResult.info("查询Данные为空！").put("ok",false);
            }
        }

        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = rows * (page - 1);
        toIndex = rows*page >= dataMap.size() ? dataMap.size() : rows*page;

        return AjaxResult.success().put("total",dataMap.size()).put("rows",dataMap.subList(fromIndex,toIndex));
    }

    private void getFields(List<String> fields,List<Map<String, Object>> dataMap){
        Map<String, Object> data = dataMap.get(0);
        for (String field:data.keySet()){
            fields.add(field);
        }
        Collections.sort(fields);
    }
}
