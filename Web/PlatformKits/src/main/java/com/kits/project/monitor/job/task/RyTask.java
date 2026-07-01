package com.kits.project.monitor.job.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kits.common.utils.LogUtil;
import com.kits.common.utils.http.HttpUtils;
import com.kits.project.serverListConfig.apiserver.domain.SdkApiserver;
import com.kits.project.serverListConfig.apiserver.service.ISdkApiserverService;
import com.kits.project.serverListConfig.server.service.ISdkServerService;
import com.kits.project.serverListConfig.serverUpdate.domain.SdkServerUpdate;
import com.kits.project.serverListConfig.serverUpdate.service.ISdkServerUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kits.common.utils.StringUtils;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定时任务调度测试
 * 
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask
{
    @Autowired
    private ISdkServerService sdkServerService;

    @Autowired
    private ISdkApiserverService sdkApiserverService;

    @Autowired
    private ISdkServerUpdateService sdkServerUpdateService;

    public static ConcurrentHashMap<Integer, Integer> hashMap = new ConcurrentHashMap<Integer, Integer>();

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }

    /**
     *向APIserver获取自动开服的服务器状态
     */
    public void getServerState(){
        //向APIserver获取自动开服的服务器状态
        long currentTimeMillis = System.currentTimeMillis();
        SdkApiserver sdkApiserver = new SdkApiserver();
        List<SdkApiserver> list = sdkApiserverService.selectSdkApiserverList(sdkApiserver);
        List<String> serverStateResultList = new ArrayList<>();
        String serverStateResult = "";
        if (null != list && list.size() > 0){
            for (int i = 0; i < list.size(); i++){
                serverStateResult = HttpUtils.sendGet(list.get(i).getApiUrl() + "/query/getServerState", "serverId=0");
                serverStateResultList.add(serverStateResult);
            }
        }
        //获取服务器配置 服务器id列表
        List<Long> serverIds = sdkServerService.selectServerIds();

        for (String serverState:serverStateResultList){
            if (!serverState.equals("")){
                JSONObject  root = JSON.parseObject(serverState);
                JSONArray dataJSONArray = root.getJSONArray("data");
                HashMap map = new HashMap();
                for(int i = 0; i < dataJSONArray.size(); i++){
                    JSONObject jsonObject = dataJSONArray.getJSONObject(i);
                    int serverId = jsonObject.getIntValue("serverId");
                    int openState = jsonObject.getIntValue("openState");
                    int registerNum = jsonObject.getIntValue("registerNum");
                    for (int j = 0; j < serverIds.size(); j++){
                        if (serverId == Integer.parseInt(serverIds.get(j).toString())){
                            hashMap.put(serverId,registerNum);
                            if (openState == 1){
                                map.put("openState",openState);
                                map.put("serverId",serverId);
                                sdkServerService.updateSdkServerState(map);
                            }
                        }
                    }
                }
            }else {
                continue;
            }
        }
        LogUtil.info("向APIServre请求服务器状态信息耗时:" +(System.currentTimeMillis() - currentTimeMillis)+ "ms");
    }

    /**
     * 定时刷新缓存(所有表记载的缓存时间节点和服务器配置的时间戳一致)
     *
     * 只有sdkServer表记载了缓存的时间戳
     */
    public void refreshServerCache(){
        long currentTimeMillis = System.currentTimeMillis();
        ConcurrentHashMap<String, Long> serverUpdateHashMap = sdkServerService.getServerUpdateHashMap();
        Long updateServerTime = serverUpdateHashMap.get("updateServerTime");//内存中的时间戳
        List<SdkServerUpdate> sdkServerUpdates = sdkServerUpdateService.selectSdkServerUpdateList(new SdkServerUpdate());
        if (null != sdkServerUpdates && sdkServerUpdates.size() > 0){
            SdkServerUpdate sdkServerUpdate = sdkServerUpdates.get(0);
            Long updateTime = sdkServerUpdate.getUpdateTime();//数据库中存储的时间戳
            if (updateTime > updateServerTime){
                sdkServerService.reloadAllCache(updateTime);
//                serverUpdateHashMap.put("updateServerTime",updateTime);
                LogUtil.info("定时刷新缓存成功耗时:" +(System.currentTimeMillis() - currentTimeMillis)+ "ms");
            }else {
                LogUtil.info("数据没有变化不刷新");
            }
        }else {
            LogUtil.info("数据没有变化不刷新");
        }
    }
}
