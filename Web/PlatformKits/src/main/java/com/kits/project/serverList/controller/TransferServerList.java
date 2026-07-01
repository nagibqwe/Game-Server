package com.kits.project.serverList.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kits.common.utils.LogUtil;
import com.kits.common.utils.StringUtils;
import com.kits.common.utils.text.Convert;
import com.kits.project.serverList.domain.ChannelGrid;
import com.kits.project.serverList.domain.PublicServerGrid;
import com.kits.project.serverListConfig.channel.domain.SdkChannel;
import com.kits.project.serverListConfig.channel.service.ISdkChannelService;
import com.kits.project.serverListConfig.channelids.domain.SdkChannelidChannelids;
import com.kits.project.serverListConfig.channelids.service.ISdkChannelidChannelidsService;
import com.kits.project.serverListConfig.server.domain.SdkServer;
import com.kits.project.serverListConfig.server.service.ISdkServerService;
import com.kits.project.serverListConfig.serverExtra.domain.SdkServerExtra;
import com.kits.project.serverListConfig.serverExtra.service.ISdkServerExtraService;
import com.kits.project.serverListConfig.serverList.domain.SdkServerList;
import com.kits.project.serverListConfig.serverList.service.ISdkServerListService;
import com.kits.project.serverListConfig.x8app.service.ITX8AppService;
import com.kits.project.serverListConfig.x8channelMaster.service.ITX8ChannelMasterService;
import com.kits.project.serverListConfig.x8server.domain.TX8GameServer;
import com.kits.project.serverListConfig.x8server.service.ITX8GameServerService;
import com.kits.project.serverListConfig.x8serverExtra.domain.TX8ServerPolicy;
import com.kits.project.serverListConfig.x8serverExtra.service.ITX8ServerPolicyService;
import com.kits.project.serverListConfig.x8serverList.domain.TX8ServerList;
import com.kits.project.serverListConfig.x8serverList.service.ITX8ServerListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kits.project.serverListConfig.serverList.controller.SdkServerListController.IsUsed;

@Controller
public class TransferServerList {

    @Autowired
    private ITX8ChannelMasterService tx8ChannelMasterService;

    @Autowired
    private ISdkChannelService sdkChannelService;

    @Autowired
    private ITX8ServerListService tx8ServerListService;

    @Autowired
    private ITX8GameServerService tx8GameServerService;

    @Autowired
    private ISdkServerService sdkServerService;

    @Autowired
    private ISdkChannelidChannelidsService sdkChannelidChannelidsService;

    @Autowired
    private ISdkServerListService sdkServerListService;

    @Autowired
    private ITX8ServerPolicyService tx8ServerPolicyService;

    @Autowired
    private ISdkServerExtraService sdkServerExtraService;

    @Autowired
    private ITX8AppService tx8AppService;

    private static final long serverType_ceshi = 0;//测试
    private static final long serverType_xianfeng = 1;//先锋
    private static final long serverType_zhengshi = 2;//正式
    private static final long serverTag_zhengchang = 0;//正常
    private static final long serverTag_baoman = 1;//爆满
    private static final long serverTag_tuijian = 2;//推荐
    private static final long serverTag_xinfu = 3;//新服

    @GetMapping("/transferServerList")
    protected void transferServerList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        //渠道表和渠道关系表转换
        List<ChannelGrid> channelGrids = tx8ChannelMasterService.selectChannelInfo();
        for (ChannelGrid channelGrid:channelGrids){
            SdkChannel sdkChannel = new SdkChannel();
            sdkChannel.setId(channelGrid.getChnmId());
            sdkChannel.setChannelName(channelGrid.getChnmName());
            sdkChannel.setChannelId(channelGrid.getChnId());
            sdkChannelService.insertSdkChannel(sdkChannel);
        }

        TX8ServerList tX8ServerList = new TX8ServerList();
        List<TX8ServerList> serverLists = tx8ServerListService.selectTX8ServerListList(tX8ServerList);
        HashMap map = new HashMap();
        for (TX8ServerList tx8ServerList:serverLists){
            String channelIds = tx8ServerList.getChnIds();
            updateChannelId_channelIds(channelIds);
            map.put("isUse",IsUsed);
            map.put("channelIds", Convert.toStrArray(channelIds));
            sdkChannelService.updateSdkChannelIsUse(map);
        }

        //服务器配置表转换
        List<TX8GameServer> tx8GameServerList = tx8GameServerService.selectServerInfo();
        for (TX8GameServer tx8GameServer:tx8GameServerList){
            SdkServer sdkServer = new SdkServer();
            sdkServer.setId(tx8GameServer.getSvrId());
            sdkServer.setServerId(Long.valueOf(tx8GameServer.getMapSvrId()));
            sdkServer.setServerName(tx8GameServer.getSvrName());
            sdkServer.setServerIp(tx8GameServer.getSvrIp());
            sdkServer.setServerPort(tx8GameServer.getSvrPort());
            sdkServer.setServerExtconfig(tx8GameServer.getSvrExtconfig());
            sdkServer.setUpdateTime(tx8GameServer.getUpdateTime());
            sdkServer.setCreateTime(tx8GameServer.getCreateTime());
            sdkServerService.insertSdkServer(sdkServer);
        }

        //服务器列表表转换(除开登录服相关信息)
        for (TX8ServerList tx8ServerList:serverLists){
            setServerListInfo(tx8ServerList);
        }

        //服务器额外信息表转换
        HashMap<Long,String> serverMap = serverMap(tx8GameServerList);
        HashMap<Long,Long> serverGroupTypeMap = serverGroupTypeMap(tx8GameServerList);
        TX8ServerPolicy tX8ServerPolicy = new TX8ServerPolicy();
        List<TX8ServerPolicy> tx8ServerPolicyList = tx8ServerPolicyService.selectTX8ServerPolicyList(tX8ServerPolicy);
        HashMap<Long,Long> channelServerListIdMap = new HashMap<Long, Long>();
        for (TX8ServerList tx8ServerList:serverLists){
            String channelIdsStr = tx8ServerList.getChnIds();
            if (null != channelIdsStr && !"".equals(channelIdsStr)){
                Long[] channelIds = StringUtils.stringArrToLongArr(Convert.toStrArray(channelIdsStr));
                for (Long channelId:channelIds){
                    channelServerListIdMap.put(channelId,tx8ServerList.getPolicyId());
                }
            }
        }

        for (TX8ServerPolicy tx8ServerPolicy:tx8ServerPolicyList){
            SdkServerExtra sdkServerExtra = new SdkServerExtra();
            sdkServerExtra.setId(tx8ServerPolicy.getPolicyId());
            sdkServerExtra.setServerListId(channelServerListIdMap.get(tx8ServerPolicy.getChnId()));
            sdkServerExtra.setServerId(Long.valueOf(serverMap.get(tx8ServerPolicy.getSvrId())));
            sdkServerExtra.setSortId(tx8ServerPolicy.getSortId());
            sdkServerExtra.setServerStatus(tx8ServerPolicy.getSvrStatus());
            sdkServerExtra.setGroupType(transferServerGroupType(serverGroupTypeMap.get(tx8ServerPolicy.getSvrId())));
            sdkServerExtra.setServerLabel(transferServerTag(tx8ServerPolicy.getSvrTag()));
            sdkServerExtra.setSceId(tx8ServerPolicy.getSceId());
            sdkServerExtra.setAppVersion("");
            sdkServerExtra.setUpdateTime(tx8ServerPolicy.getUpdateTime());
            sdkServerExtra.setCreateTime(tx8ServerPolicy.getCreateTime());
            sdkServerExtraService.insertSdkServerExtra(sdkServerExtra);
        }

        //服务器列表表更新(更新登录服相关信息)
        List<PublicServerGrid> publicServerGridList = tx8AppService.selectPublicInfo();
        for (PublicServerGrid publicServerGrid:publicServerGridList){
            SdkServerList sdkServerList = sdkServerListService.selectSdkServerListById(publicServerGrid.getPolicyId());
            String publicStr = publicServerGrid.getExtParams();
            JSONObject jsonObject = JSON.parseObject(publicStr);
            if (jsonObject.size() > 0){
                String svr_host = jsonObject.getString("svr_host");
                String svr_private_chids = jsonObject.getString("svr_private_chids");
//                String loginServerIp = getLoginServerIp(svr_host,svr_private_chids,sdkServerList.getChannelIds());
//                sdkServerList.setLoginServerIp(loginServerIp);
//                sdkServerList.setLoginServerName(jsonObject.getString("svr_name"));
//                sdkServerList.setLoginServerPort(jsonObject.getLongValue("svr_port"));
//                sdkServerListService.updateSdkServerList(sdkServerList,2);
            }
        }

        LogUtil.info("数据迁移耗时:" +(System.currentTimeMillis() - currentTimeMillis)+ "ms");
    }

    //更新渠道id和渠道ids关系表数据
    private void updateChannelId_channelIds(String channelIds){
        String[] channelIdsArr = Convert.toStrArray(channelIds);
        for (int i = 0; i < channelIdsArr.length; i++){
            SdkChannelidChannelids channelids = new SdkChannelidChannelids();
            channelids.setChannelId(Long.valueOf(channelIdsArr[i]));
            channelids.setChannelIds(channelIds);
            sdkChannelidChannelidsService.replaceInsertSdkChannelidChannelids(channelids);
        }
    }

    //服务器列表表转换(除开登录服相关信息)
    private void setServerListInfo(TX8ServerList tx8ServerList){
        if (null != tx8ServerList.getSvrPolicyIds() && !"".equals(tx8ServerList.getSvrPolicyIds())){
            SdkServerList sdkServerList = new SdkServerList();
            sdkServerList.setId(tx8ServerList.getPolicyId());
            sdkServerList.setName(tx8ServerList.getPolicyName());
            sdkServerList.setChannelIds(tx8ServerList.getChnIds());
            sdkServerList.setServerIds(setServerIds(tx8ServerList.getSvrPolicyIds()));
            sdkServerList.setStatus(tx8ServerList.getStatus());
            sdkServerList.setUpdateTime(tx8ServerList.getUpdateTime());
            sdkServerList.setCreateTime(tx8ServerList.getCreateTime());
            sdkServerListService.insertSdkServerList(sdkServerList);
        }
    }

    //服务器ID列表映射转换
    private String setServerIds(String svrPolicyIds) {
        String[] serverIdsArr = Convert.toStrArray(svrPolicyIds);
        List<String> serverList = new ArrayList<>();
        Long[] serverIds = StringUtils.stringArrToLongArr(serverIdsArr);
        for (Long serverId:serverIds){
            TX8GameServer tx8GameServer = tx8GameServerService.selectTX8GameServerById(serverId);
            serverList.add(tx8GameServer.getMapSvrId());
        }
        String resultStr = StringUtils.stringListToString(serverList);
        return resultStr;
    }

    //获取服务器ID编号和真实服务器ID的对应map
    private HashMap<Long,String> serverMap(List<TX8GameServer> tx8GameServerList){
        HashMap<Long,String> serverMap = new HashMap<>();
        for (TX8GameServer tx8GameServer:tx8GameServerList){
            serverMap.put(tx8GameServer.getSvrId(),tx8GameServer.getMapSvrId());
        }
        return serverMap;
    }

    //获取服务器ID编号和服务器类型
    private HashMap<Long,Long> serverGroupTypeMap(List<TX8GameServer> tx8GameServerList){
        HashMap<Long,Long> serverGroupTypeMap = new HashMap<>();
        for (TX8GameServer tx8GameServer:tx8GameServerList){
            serverGroupTypeMap.put(tx8GameServer.getSvrId(),tx8GameServer.getGroupType());
        }
        return serverGroupTypeMap;
    }

    //转换服务器类型
    private Long transferServerGroupType(Long oldServerGroupType){
        long newServerGroupType = 0;
        if (oldServerGroupType == 0){
            newServerGroupType = serverType_ceshi;
        }
        if (oldServerGroupType == 1 || oldServerGroupType == 2){
            newServerGroupType = serverType_xianfeng;
        }
        if (oldServerGroupType == 3){
            newServerGroupType = serverType_zhengshi;
        }
        return newServerGroupType;
    }

    //转换服务器标签
    private String transferServerTag(String oldServerTag) {
        List<String> serverTagList = new ArrayList<>();
        String newServerTag = "";
        if (null != oldServerTag && !"".equals(oldServerTag)){
            String[] oldServerTagArr = Convert.toStrArray(oldServerTag);
            for (String oldTag:oldServerTagArr){
                if (Long.valueOf(oldTag) == 1){
                    serverTagList.add(String.valueOf(serverTag_zhengchang));
                }else if (Long.valueOf(oldTag) == 2){
                    serverTagList.add(String.valueOf(serverTag_tuijian));
                }else if (Long.valueOf(oldTag) == 3){
                    serverTagList.add(String.valueOf(serverTag_baoman));
                }else {
                    serverTagList.add(String.valueOf(serverTag_xinfu));
                }
            }
        }
        newServerTag = StringUtils.stringListToString(serverTagList);
        return newServerTag;
    }

    //获取登录服ip
    private String getLoginServerIp(String svr_host, String svr_private_chids, String channelIds) {
        String loginServerIp = "";
        String[] hostArr = svr_host.split("\\|");
        String[] chidsArr = svr_private_chids.split("\\|");
        String[] loginServerIpArr = new String[hostArr.length + chidsArr.length];
        int count = 0;
        if (chidsArr[0].equals("0")){
            loginServerIp = hostArr[hostArr.length - 1];
        }else {
            if (null != channelIds && !"".equals(channelIds)){
                for (int k = 0; k < hostArr.length; k++){
                    String[] channelIdArr = Convert.toStrArray(channelIds);
                    for (int i = 0; i < chidsArr.length; i++){
                        for (int j = 0; j < channelIdArr.length; j++){
                            if (chidsArr[i].equals(channelIdArr[j])){
                                loginServerIpArr[count] = hostArr[k];
                                count+=1;
                            }
                        }
                    }
                }
                loginServerIp = loginServerIpArr[0];//取第一个
            }
        }
        return loginServerIp;
    }
}
