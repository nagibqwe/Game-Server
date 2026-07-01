package com.kits.project.serverList.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kits.common.utils.JsonUtils;
import com.kits.common.utils.LogUtil;
import com.kits.common.utils.StringUtils;
import com.kits.common.utils.TypeReference;
import com.kits.common.utils.text.Convert;
import com.kits.project.monitor.job.task.RyTask;
import com.kits.project.serverList.domain.LoginServerList;
import com.kits.project.serverList.domain.ServerList;
import com.kits.project.serverListConfig.channel.domain.SdkChannel;
import com.kits.project.serverListConfig.channel.service.ISdkChannelService;
import com.kits.project.serverListConfig.channelids.domain.SdkChannelidChannelids;
import com.kits.project.serverListConfig.channelids.service.ISdkChannelidChannelidsService;
import com.kits.project.serverListConfig.loginServer.domain.SdkLoginServer;
import com.kits.project.serverListConfig.loginServer.service.ISdkLoginServerService;
import com.kits.project.serverListConfig.server.domain.SdkServer;
import com.kits.project.serverListConfig.server.domain.SdkServerInfo;
import com.kits.project.serverListConfig.server.service.ISdkServerService;
import com.kits.project.serverListConfig.serverExtra.domain.SdkServerExtra;
import com.kits.project.serverListConfig.serverExtra.service.ISdkServerExtraService;
import com.kits.project.serverListConfig.serverList.domain.SdkServerList;
import com.kits.project.serverListConfig.serverList.service.ISdkServerListService;
import com.kits.project.serverListConfig.whiteList.domain.SdkWhite;
import com.kits.project.serverListConfig.whiteList.service.ISdkWhiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class QueryServerList2 {

    @Autowired
    private ISdkChannelService sdkChannelService;
    @Autowired
    private ISdkChannelidChannelidsService sdkChannelidChannelidsService;
    @Autowired
    private ISdkServerService sdkServerService;
    @Autowired
    private ISdkServerExtraService sdkServerExtraService;
    @Autowired
    private ISdkServerListService sdkServerListService;
    @Autowired
    private ISdkWhiteService sdkWhiteService;
    @Autowired
    private ISdkLoginServerService sdkLoginServerService;

    protected final Logger logger = LoggerFactory.getLogger(QueryServerList2.class);
    private static final String FAILED = "failed";
    private static final String SUCCESS = "success";
    private static final int OPEN_STATE_OPEN = 1;//已开服
    private static final int SERVER_STATUS_OPEN = 1;//状态为开放中
    private static final int SERVER_STATUS_KEEP = 2;//状态为维护中

    @GetMapping("/queryServerList")
    protected void queryServerList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long currentTimeMillis = System.currentTimeMillis();
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String chn_id = request.getParameter("chn_id");
        String user_id = request.getParameter("user_id");
        String sec_id = request.getParameter("sec_id");
        String client_version = request.getParameter("client_version");

        //判断参数
        if (null == chn_id || !isNum(chn_id)) {//必须传的参数
            JSON result = getResult(100, FAILED, null);//渠道参数有误
            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(result)));
            return;
        }
        if (null == sec_id){//没有数据则转为空字符串
            sec_id = "";
        }
        if (null == client_version){//没有数据则转为空字符串
            client_version = "";
        }
        boolean isWhite = false;//是否为白名单
        try {
            ConcurrentHashMap<Long, SdkChannel> channelHashMap = sdkChannelService.getChannelHashMap();
            ConcurrentHashMap<Long, SdkChannelidChannelids> channelidChannelidsHashMap = sdkChannelidChannelidsService.getChannelidChannelidsHashMap();
            ConcurrentHashMap<Long, SdkServer> serverHashMap = sdkServerService.getServerHashMap();
            ConcurrentHashMap<Long, SdkServerExtra> serverExtraHashMap = sdkServerExtraService.getServerExtraHashMap();
            ConcurrentHashMap<Long, SdkServerList> serverListHashMap = sdkServerListService.getServerListHashMap();
            ConcurrentHashMap<Long, SdkWhite> whiteHashMap = sdkWhiteService.getWhiteHashMap();
            ConcurrentHashMap<Long, SdkLoginServer> loginServerHashMap = sdkLoginServerService.getLoginServerHashMap();
            //是否包含传过来的channelId
            int count = 0;
            int countAll = channelHashMap.size();
            for (Long channelId : channelHashMap.keySet()) {
                if (Long.valueOf(chn_id).equals(channelId)) {
                    for (SdkWhite white : whiteHashMap.values()) {
                        if (white.getWhiteName().equals(user_id)) {
                            isWhite = true;
                        }
                    }
                    SdkServerList sdkServerList = getSdkServerList(chn_id, channelidChannelidsHashMap, serverListHashMap);
                    if (null == sdkServerList || sdkServerList.getStatus() == 0){
                        JSON result = getResult(300, FAILED, null);//服务器列表数据没有该渠道的相关数据或该服务器列表已被关闭(Status为0)
                        response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(result)));
                        return;
                    }
                    Long serverListId = sdkServerList.getId();
                    String serverIds = sdkServerList.getServerIds();
                    HashMap<String, Object> sdkServerInfoListByIsBackUp = getSdkServerInfo(serverHashMap, serverExtraHashMap, serverListId, serverIds);
                    List<SdkServerInfo> sdkServerInfoListNotBackUp = (List<SdkServerInfo>) sdkServerInfoListByIsBackUp.get("sdkServerInfoListNotBackUp");
                    List<SdkServerInfo> sdkServerInfoListIsBackUp = (List<SdkServerInfo>) sdkServerInfoListByIsBackUp.get("sdkServerInfoListIsBackUp");

                    //根据条件筛选之后的结果集
                    List<SdkServerInfo> resultList = new ArrayList<>();
                    if (null != sdkServerInfoListNotBackUp){
                        List<SdkServerInfo> sdkServerInfoListNotBackUp2 = getSdkServerInfoBySceIdAddAppVersion(sec_id,client_version,sdkServerInfoListNotBackUp,isWhite,false);
                        resultList.addAll(sdkServerInfoListNotBackUp2);
                    }
                    if (null != sdkServerInfoListIsBackUp){
                        List<SdkServerInfo> sdkServerInfoListIsBackUp2 = getSdkServerInfoBySceIdAddAppVersion(sec_id,client_version,sdkServerInfoListIsBackUp,isWhite,true);
                        resultList.addAll(sdkServerInfoListIsBackUp2);
                    }
                    //组装成客户端需要的数据
                    List<ServerList> serverListList = getServerLists(resultList);

                    HashMap<String, Object> dataMap = new HashMap<String, Object>();
                    HashMap<String, Object> result = new HashMap<String, Object>();

                    //组装登录服信息
                    List<LoginServerList> loginServerList = getLoginServerGroup(sdkServerList.getLoginServerGroup(),loginServerHashMap);
                    dataMap.put("lg_server", loginServerList);
                    dataMap.put("servers", serverListList);

                    result.put("data", dataMap);
                    response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(result)));
                    response.getWriter().flush();
                } else {
                    count++;
                    if (count >= countAll) {
                        JSON result = getResult(200, FAILED, null);//没有这个渠道数据
                        response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(result)));
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtil.info("获取服务器列表耗时:" + (System.currentTimeMillis() - currentTimeMillis) + "ms");
    }

    private JSON getResult(Object error, Object status, Object results) {

        HashMap<String, Object> errorMap = new HashMap<String, Object>();

        errorMap.put("error", error);
        errorMap.put("status", status);
        errorMap.put("results", results);

        return (JSON) JSONObject.toJSON(errorMap);
    }

    /**
     * 判断字符串是否可以转换为数字
     *
     * @return
     */
    private boolean isNum(String str) {
        return str.matches("[0-9]+");
    }

    /**
     * 根据channelidChannelids关系表获取服务器列表数据
     *
     * @param chn_id
     * @param channelidChannelidsHashMap
     * @param serverListHashMap
     * @return
     */
    private SdkServerList getSdkServerList(String chn_id, ConcurrentHashMap<Long, SdkChannelidChannelids> channelidChannelidsHashMap, ConcurrentHashMap<Long, SdkServerList> serverListHashMap) {
        SdkServerList sdkServerList1 = null;
        for (Map.Entry<Long, SdkChannelidChannelids> channelidChannelidsEntry : channelidChannelidsHashMap.entrySet()) {
            if (channelidChannelidsEntry.getKey().equals(Long.valueOf(chn_id))) {
                String channelIds = channelidChannelidsEntry.getValue().getChannelIds();
                for (SdkServerList sdkServerList : serverListHashMap.values()) {
                    if (null != sdkServerList.getChannelIds()){
                        if (sdkServerList.getChannelIds().equals(channelIds)) {
                            sdkServerList1 = sdkServerList;
                        }
                    }
                }
            }
        }
        return sdkServerList1;
    }

    /**
     * 根据服务器是否为备服作为区分获取SdkServerInfo的map集合
     *
     * @param serverHashMap
     * @param serverExtraHashMap
     * @param serverListId
     * @param serverIds
     * @return
     */
    private HashMap<String, Object> getSdkServerInfo(ConcurrentHashMap<Long, SdkServer> serverHashMap, ConcurrentHashMap<Long, SdkServerExtra> serverExtraHashMap, Long serverListId, String serverIds) {
        HashMap<String, Object> sdkServerInfoMap = new HashMap<>();
        if (null != serverIds && !serverIds.equals("")){
            String[] serverIdsArr = serverIds.split(",");
            Long[] serverIdsArr2 = StringUtils.stringArrToLongArr(serverIdsArr);
            List<SdkServerExtra> sdkServerExtraList = getSdkServerExtraList(serverListId, serverExtraHashMap);

            List<SdkServerInfo> sdkServerInfoListNotBackUp = new ArrayList<>();
            List<SdkServerInfo> sdkServerInfoListIsBackUp = new ArrayList<>();
            for (Long serverId : serverIdsArr2) {
                for (SdkServerExtra sdkServerExtra : sdkServerExtraList) {
                    SdkServer sdkServer = serverHashMap.get(serverId);
                    if ((sdkServer.getServerId().equals(sdkServerExtra.getServerId()) && (sdkServer.getIsBackup() == 0))) {//服务器非备服服务器
                        SdkServerInfo sdkServerInfo = getSdkServerInfoByIsBack(sdkServer, sdkServerExtra);
                        sdkServerInfoListNotBackUp.add(sdkServerInfo);
                    } else if ((sdkServer.getServerId().equals(sdkServerExtra.getServerId()) && (sdkServer.getIsBackup() == 1))) {//服务器为备服服务器
                        SdkServerInfo sdkServerInfo = getSdkServerInfoByIsBack(sdkServer, sdkServerExtra);
                        sdkServerInfoListIsBackUp.add(sdkServerInfo);
                    }
                }
            }
            sdkServerInfoMap.put("sdkServerInfoListNotBackUp", sdkServerInfoListNotBackUp);
            sdkServerInfoMap.put("sdkServerInfoListIsBackUp", sdkServerInfoListIsBackUp);
        }
        return sdkServerInfoMap;
    }

    /**
     * 获取服务器列表ID下所有的服务器额外信息
     *
     * @param serverListId
     * @param serverExtraHashMap
     * @return
     */
    private List<SdkServerExtra> getSdkServerExtraList(Long serverListId, ConcurrentHashMap<Long, SdkServerExtra> serverExtraHashMap) {
        List<SdkServerExtra> sdkServerExtraList = new ArrayList<>();
        for (SdkServerExtra sdkServerExtra : serverExtraHashMap.values()) {
            if (serverListId.equals(sdkServerExtra.getServerListId())) {
                sdkServerExtraList.add(sdkServerExtra);
            }
        }
        return sdkServerExtraList;
    }

    /**
     * 根据服务器是否为备服作为区分
     *
     * @param sdkServer
     * @param sdkServerExtra
     * @return
     */
    private SdkServerInfo getSdkServerInfoByIsBack(SdkServer sdkServer, SdkServerExtra sdkServerExtra) {
        SdkServerInfo sdkServerInfo = new SdkServerInfo();
        sdkServerInfo.setServerId(sdkServer.getServerId());
        sdkServerInfo.setServerName(sdkServer.getServerName());
        sdkServerInfo.setServerIp(sdkServer.getServerIp());
        sdkServerInfo.setServerPort(sdkServer.getServerPort());
        sdkServerInfo.setOpenState(sdkServer.getOpenState());
        sdkServerInfo.setIsBackup(sdkServer.getIsBackup());
        sdkServerInfo.setSortId(sdkServerExtra.getSortId());
        sdkServerInfo.setServerStatus(sdkServerExtra.getServerStatus());
        sdkServerInfo.setGroupType(sdkServerExtra.getGroupType());
        sdkServerInfo.setServerLabel(sdkServerExtra.getServerLabel());
        sdkServerInfo.setSceId(sdkServerExtra.getSceId());
        sdkServerInfo.setAppVersion(sdkServerExtra.getAppVersion());
        return sdkServerInfo;
    }

    private List<SdkServerInfo> getSdkServerInfoBySceIdAddAppVersion(String sec_id, String client_version, List<SdkServerInfo> sdkServerInfoList, Boolean isWhite,boolean isBackUp) {
        //先根据sceId筛选出List<SdkServerInfo> sdkServerInfos
        List<SdkServerInfo> sdkServerInfos = new ArrayList<>();
        if (null != sec_id && !sec_id.equals("")) {
            for (SdkServerInfo sdkServerInfo : sdkServerInfoList) {
                boolean isContainSecId = isContainsSecId(sdkServerInfo, sec_id);
                if (isContainSecId || sdkServerInfo.getSceId().equals("")) {
                    sdkServerInfos.add(sdkServerInfo);
                }
            }
        } else if (null != sec_id && sec_id.equals("")) {
            for (SdkServerInfo sdkServerInfo : sdkServerInfoList) {
                if (sdkServerInfo.getSceId().equals("")){
                    sdkServerInfos.add(sdkServerInfo);
                }
            }
        }

        //再根据appVersion筛选出List<SdkServerInfo> sdkServerInfos2
        List<SdkServerInfo> sdkServerInfos2 = new ArrayList<>();
        if (null != client_version && !client_version.equals("")){
            for (SdkServerInfo sdkServerInfo : sdkServerInfos){
                boolean isContainAppVersion = isContainAppVersion(sdkServerInfo,client_version);
                if (isContainAppVersion || sdkServerInfo.getAppVersion().equals("")){
                    sdkServerInfos2.add(sdkServerInfo);
                }
            }
        }else if (null != client_version && client_version.equals("")){
            for (SdkServerInfo sdkServerInfo : sdkServerInfos){
                if (sdkServerInfo.getAppVersion().equals("")){
                    sdkServerInfos2.add(sdkServerInfo);
                }
            }
        }

        //最后根据是否为白名单筛选出List<SdkServerInfo> sdkServerInfos3
        List<SdkServerInfo> sdkServerInfos3 = new ArrayList<>();
        if (!isWhite){//非白名单时只能查询open_state为1和server_status为1的服务器列表(状态为开启)和server_status为2的服务器
            if (!isBackUp){//不是备服不用管openState
                for (SdkServerInfo sdkServerInfo : sdkServerInfos2){
                    Long serverStatus = sdkServerInfo.getServerStatus();
                    if (serverStatus == SERVER_STATUS_OPEN || serverStatus == SERVER_STATUS_KEEP){
                        sdkServerInfos3.add(sdkServerInfo);
                    }
                }
            }else {//是备服就需要加上openState的判断
                for (SdkServerInfo sdkServerInfo : sdkServerInfos2){
                    Long serverStatus = sdkServerInfo.getServerStatus();
                    Long openState = sdkServerInfo.getOpenState();
                    if (null != openState){
                        if ((openState == OPEN_STATE_OPEN && serverStatus == SERVER_STATUS_OPEN) || (openState == OPEN_STATE_OPEN && serverStatus == SERVER_STATUS_KEEP)){
                            sdkServerInfos3.add(sdkServerInfo);
                        }
                    }
                }
            }
        }else {//是白名单时全部可以查询
            sdkServerInfos3 = sdkServerInfos2;
        }

        return sdkServerInfos3;
    }

    /**
     * 是否包含客户端传过来的secId参数
     * @param sdkServerInfo
     * @param sec_id
     * @return
     */
    private boolean isContainsSecId(SdkServerInfo sdkServerInfo, String sec_id) {
        boolean flag = false;
        String[] sceIdArr = sdkServerInfo.getSceId().split(",");
        for (String sceId : sceIdArr) {
            if (sceId.equals(sec_id)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 是否包含客户端传过来的appVersion参数
     * @param sdkServerInfo
     * @param client_version
     * @return
     */
    private boolean isContainAppVersion(SdkServerInfo sdkServerInfo, String client_version){
        boolean flag = false;
        if (sdkServerInfo.getAppVersion().equals(client_version)){
            flag = true;
        }
        return flag;
    }

    private List<ServerList> getServerLists(List<SdkServerInfo> resultList){
        //获取(服务器ID,注册人数)的map
        ConcurrentHashMap<Integer, Integer> hashMap = RyTask.hashMap;
        List<ServerList> serverLists = new ArrayList<>();
        if (null != resultList && resultList.size() > 0){
            for (SdkServerInfo sdkServerInfo:resultList){
                ServerList serverList = new ServerList();
                serverList.setSvr_id(Integer.parseInt(String.valueOf(sdkServerInfo.getServerId())));
                serverList.setSvr_name(sdkServerInfo.getServerName());
                serverList.setSvr_host(sdkServerInfo.getServerIp());
                serverList.setSvr_port(Integer.parseInt(String.valueOf(sdkServerInfo.getServerPort())));
                serverList.setSvr_sort(Integer.parseInt(String.valueOf(sdkServerInfo.getSortId())));
                serverList.setSvr_status(Integer.parseInt(String.valueOf(sdkServerInfo.getServerStatus())));
                serverList.setGroup_type(Integer.parseInt(String.valueOf(sdkServerInfo.getGroupType())));
                serverList.setSvr_label(sdkServerInfo.getServerLabel());
                int registerNum = 0;
                if (hashMap.containsKey(Integer.parseInt(String.valueOf(sdkServerInfo.getServerId())))){
                    registerNum = hashMap.get(Integer.parseInt(String.valueOf(sdkServerInfo.getServerId())));
                }
                serverList.setRegister_num(registerNum);
                serverLists.add(serverList);
            }
            return serverLists;
        }
        return serverLists;
    }

    private List<LoginServerList> getLoginServerGroup(String loginServerGroup, ConcurrentHashMap<Long,SdkLoginServer> loginServerHashMap) {
        List<LoginServerList> loginServers = new ArrayList<>();
        List<Long> loginServerIds = Arrays.asList(Convert.toLongArray(loginServerGroup));
        if (loginServerIds.size() > 0){
            for (Long loginServerId:loginServerIds){
                SdkLoginServer sdkLoginServer = loginServerHashMap.get(loginServerId);
                LoginServerList loginServer = new LoginServerList();
                loginServer.setSvr_host(sdkLoginServer.getLoginServerIp());
                loginServer.setSvr_name(sdkLoginServer.getLoginServerName());
                loginServer.setSvr_port(sdkLoginServer.getLoginServerPort());
                loginServers.add(loginServer);
            }
        }
        return loginServers;
    }
}
