package com.gm.project.gmtool.serverGroup.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.config.GameManagerConfig;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.serverGroup.domain.ServerGroupGrid;
import com.gm.project.gmtool.utils.*;
import net.sf.json.JSONArray;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/gmtool/serverGroup")
public class ServerGroupController extends BaseController {
    private String prefix = "gmtool/serverGroup";

    @Autowired
    private ITServerService tServerService;

    @Autowired
    private GameManagerConfig gameManagerConfig;

    @RequiresPermissions("gmtool:serverGroup:view")
    @GetMapping()
    public String serverGroup() {
        return prefix + "/serverGroup";
    }

    /**
     * 向公共服获取跨服分组信息
     * @param request
     * @param publicServerId
     * @return
     * @throws UnsupportedEncodingException
     */
    @PostMapping("/getServerGroup")
    @ResponseBody
    public Object getServerGroup(HttpServletRequest request, int publicServerId) throws UnsupportedEncodingException {
        if (publicServerId <= 0){
            return AjaxResult.error("公共服ID有误");
        }
//        Object o = getData();
//        return o;
        HashMap<Integer, HashMap<Integer,List<String>>> serverGroupList = new HashMap<>();
        try {
            serverGroupList = (HashMap<Integer,HashMap<Integer,List<String>>>) sendPS(request,publicServerId,"getServerGroupList","");
        }catch (Exception e){
            return AjaxResult.error().put("ok",false).put("total",0).put("rows",0);
        }
        if (serverGroupList == null||serverGroupList.isEmpty()){
            return AjaxResult.success().put("ok",true).put("total",0).put("rows",0);
        }
        List<ServerGroupGrid> serverGroupGrids = new ArrayList<>();
        for (Map.Entry<Integer,HashMap<Integer,List<String>>> en : serverGroupList.entrySet()) {
            ServerGroupGrid serverGroupGrid = new ServerGroupGrid();

            int bigGroupId = en.getKey();
            serverGroupGrid.setBigGroupId(bigGroupId);

            HashMap<Integer,List<String>> serverGroup = en.getValue();
            for (Map.Entry<Integer,List<String>> entry : serverGroup.entrySet()){
                int groupId = entry.getKey();
                serverGroupGrid.setGroupId(groupId);
                List<String> serverIds = entry.getValue();

                String area = getArea(serverIds.get(0));//发过来的数据全部是一个地区的
                serverGroupGrid.setArea(area);
                int count = 0;
                for (String serverIdStr:serverIds){//服务器约定发过来数据至少包含一个serverId
                    int pserverId = getServerId(serverIdStr);
                    String serverName = getServerName(pserverId);
                    count++;
                    if (count == 1){
                        serverGroupGrid.setServerId1(pserverId);
                        serverGroupGrid.setServerName1(serverName);
                    }
                    if (count == 2){
                        serverGroupGrid.setServerId2(pserverId);
                        serverGroupGrid.setServerName2(serverName);
                    }
                    if (count == 3){
                        serverGroupGrid.setServerId3(pserverId);
                        serverGroupGrid.setServerName3(serverName);
                    }
                    if (count == 4){
                        serverGroupGrid.setServerId4(pserverId);
                        serverGroupGrid.setServerName4(serverName);
                    }
                    if (count == 5){
                        serverGroupGrid.setServerId5(pserverId);
                        serverGroupGrid.setServerName5(serverName);
                    }
                    if (count == 6){
                        serverGroupGrid.setServerId6(pserverId);
                        serverGroupGrid.setServerName6(serverName);
                    }
                    if (count == 7){
                        serverGroupGrid.setServerId7(pserverId);
                        serverGroupGrid.setServerName7(serverName);
                    }
                    if (count == 8){
                        serverGroupGrid.setServerId8(pserverId);
                        serverGroupGrid.setServerName8(serverName);
                    }
                }
            }
            serverGroupGrids.add(serverGroupGrid);
        }
        return AjaxResult.success().put("ok",true).put("total",serverGroupGrids.size()).put("rows",serverGroupGrids);
    }

    //自测数据
    public Object getData(){
        List<ServerGroupGrid> serverGroupGrids = new ArrayList<>();
        ServerGroupGrid groupGrid = new ServerGroupGrid(1,10000,"cn",20003,"杨蓝飞",20004,"陈龙川",20005,"周梦雨",20006,"张三",20007,"李四",20008,"王五",20009,"嘿嘿",0,"");
        ServerGroupGrid groupGrid2 = new ServerGroupGrid(2,20000,"cn",20010,"2222",20011,"3333",20011,"4444",20012,"5555",20013,"6666",20014,"77777",20015,"8888",0,"");
        ServerGroupGrid groupGrid3 = new ServerGroupGrid(3,30000,"cn",20016,"2222",20017,"3333",20018,"4444",20019,"5555",20020,"6666",20021,"77777",20022,"8888",0,"");
        serverGroupGrids.add(groupGrid);
        serverGroupGrids.add(groupGrid2);
        serverGroupGrids.add(groupGrid3);
        return AjaxResult.success().put("ok",true).put("total",3).put("rows",serverGroupGrids);
    }

    /**
     * 向公共服发送命令
     * @param request
     * @param serverId
     * @param action
     * @return
     * @throws UnsupportedEncodingException
     */
    public Object sendPS(HttpServletRequest request, int serverId, String action, String params) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(action)) {
            return AjaxResult.info("命令错误").put("ok",false);
        }
        if (serverId <= 0) {
            return AjaxResult.info("服务器Id有误").put("ok",false);
        }
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("secret_key", gameManagerConfig.getRequestServerKey());
        if (!"".equals(params)){
            paramMap.put("serverGroup",params);
        }
        String url;
        StringBuilder result = new StringBuilder();
        boolean isOk = true;
        TServer ser = tServerService.selectTServerByServerId(serverId);
        if (ser == null) {
            return AjaxResult.info("选择服务器Id不存在").put("ok",false);
        }

        url = "http://" + ser.getServerIP() + ":" + ser.getServerPort() + "/" + action;
        String re = HttpConnectionUtils.get(url, null, paramMap);
        result.append(re);
        if (StringUtils.isBlank(re)) {
            isOk = false;
            result.append("请求数据为空!");
        }
        GMLogUtil.log("执行GM命令,结果:" + result);
        Object object = new Object();
        if (!"".equals(params)){
            object = AjaxResult.info(result.toString()).put("ok",isOk);
        }else {
            object = JsonUtils.parseObject(re, new TypeReference<HashMap<Integer,HashMap<Integer,List<String>>>>(){});
        }
        return object;
    }
    /**
     * 获取服务器ID
     * @param severIdStr
     * @return
     */
    public int getServerId(String severIdStr){
        String[] severIdArr = severIdStr.split("_");
        int serverId = Integer.parseInt(severIdArr[1]);
        return serverId;
    }

    /**
     * 获取地区标志
     * @param severIdStr
     * @return
     */
    public String getArea(String severIdStr){
        String[] severIdArr = severIdStr.split("_");
        String area = severIdArr[0];
        return area;
    }

    /**
     * 获取服务器名
     * @param serverId
     * @return
     */
    public String getServerName(int serverId){
        TServer server = tServerService.selectTServerByServerId(serverId);
        String serverName = "";
        if (server != null){
            serverName = "("+serverId+")"+server.getServerName();
        }else {
            serverName = "("+serverId+")";
        }
        return  serverName;
    }

    /**
     * 设置跨服分组
     * @param request
     * @param serverGroupList
     * @return
     */
    @PostMapping("/setServerGroupList")
    @ResponseBody
    public Object setServerGroupList(HttpServletRequest request,String serverGroupList,int publicServerId) throws UnsupportedEncodingException {

//        HashMap<String,HashMap<Integer,HashMap<Integer,List<String>>>> dataMap = new HashMap<>();
        HashMap<Integer,HashMap<Integer,List<String>>> bIgGroupMap = new HashMap<>();

        List<ServerGroupGrid> serverGroupGridList=(List<ServerGroupGrid>) JSONArray.toList(JSONArray.fromObject(serverGroupList), ServerGroupGrid.class);

        int bigGroupId = 0;
        for (int i = 0; i < serverGroupGridList.size(); i++){
            ServerGroupGrid serverGroupGrid = serverGroupGridList.get(i);
            bigGroupId = serverGroupGrid.getBigGroupId();
            int groupId = serverGroupGrid.getGroupId();
            List<String> serverIdList = getServerIdList(serverGroupGrid);
            HashMap<Integer,List<String>> groupMap = new HashMap<>();

            groupMap.put(groupId,serverIdList);
            bIgGroupMap.put(bigGroupId,groupMap);
        }
//        dataMap.put("serverGroup",bIgGroupMap);
        String serverGroup = JsonUtils.toJSONString(bIgGroupMap);
        Object result = sendPS(request, publicServerId, "setServerGroupList",serverGroup);
        GMLogUtil.log("执行GM命令setServerGroupList,结果:" + result.toString());
        return result;
    }

    /**
     * 组装服务器需要的格式  地区_服务器ID
     * @param serverGroupGrid
     * @return
     */
    private List<String> getServerIdList(ServerGroupGrid serverGroupGrid) {
        List<String> serverIds = new ArrayList<>();
        String area = serverGroupGrid.getArea();
        if (serverGroupGrid.getServerId1() != 0){
            serverIds.add(area+"_"+serverGroupGrid.getServerId1());
        }
        if (serverGroupGrid.getServerId2() != 0){
            serverIds.add(area+"_"+serverGroupGrid.getServerId2());
        }
        if (serverGroupGrid.getServerId3() != 0){
            serverIds.add(area+"_"+serverGroupGrid.getServerId3());
        }
        if (serverGroupGrid.getServerId4() != 0){
            serverIds.add(area+"_"+serverGroupGrid.getServerId4());
        }
        if (serverGroupGrid.getServerId5() != 0){
            serverIds.add(area+"_"+serverGroupGrid.getServerId5());
        }
        if (serverGroupGrid.getServerId6() != 0){
            serverIds.add(area+"_"+serverGroupGrid.getServerId6());
        }
        if (serverGroupGrid.getServerId7() != 0){
            serverIds.add(area+"_"+serverGroupGrid.getServerId7());
        }
        if (serverGroupGrid.getServerId8() != 0){
            serverIds.add(area+"_"+serverGroupGrid.getServerId8());
        }

        return serverIds;
    }
}
