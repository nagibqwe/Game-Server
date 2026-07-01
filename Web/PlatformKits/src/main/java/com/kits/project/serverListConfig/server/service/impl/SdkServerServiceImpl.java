package com.kits.project.serverListConfig.server.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kits.common.utils.DateUtils;
import com.kits.project.serverListConfig.channel.service.ISdkChannelService;
import com.kits.project.serverListConfig.channelids.service.ISdkChannelidChannelidsService;
import com.kits.project.serverListConfig.loginServer.service.ISdkLoginServerService;
import com.kits.project.serverListConfig.notice.service.ISdkNoticeService;
import com.kits.project.serverListConfig.serverExtra.service.ISdkServerExtraService;
import com.kits.project.serverListConfig.serverList.service.ISdkServerListService;
import com.kits.project.serverListConfig.serverUpdate.domain.SdkServerUpdate;
import com.kits.project.serverListConfig.serverUpdate.service.ISdkServerUpdateService;
import com.kits.project.serverListConfig.whiteList.service.ISdkWhiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.server.mapper.SdkServerMapper;
import com.kits.project.serverListConfig.server.domain.SdkServer;
import com.kits.project.serverListConfig.server.service.ISdkServerService;
import com.kits.common.utils.text.Convert;

import javax.annotation.PostConstruct;

/**
 * 服务器配置信息Service业务层处理
 * 
 * @author gm
 * @date 2021-04-25
 */
@Service
public class SdkServerServiceImpl implements ISdkServerService 
{
    private ConcurrentHashMap<Long,SdkServer> hashMap = new ConcurrentHashMap<Long,SdkServer>();
    private ConcurrentHashMap<String,Long> serverUpdateTimeMap = new ConcurrentHashMap<>();//服务器信息缓存更新时间戳

    @Autowired
    private SdkServerMapper sdkServerMapper;

    @Autowired
    private ISdkServerUpdateService sdkServerUpdateService;

    @Autowired
    private ISdkServerListService sdkServerListService;

    @Autowired
    private ISdkServerExtraService sdkServerExtraService;

    @Autowired
    private ISdkChannelService sdkChannelService;

    @Autowired
    private ISdkChannelidChannelidsService sdkChannelidChannelidsService;

    @Autowired
    private ISdkLoginServerService sdkLoginServerService;

    @Autowired
    private ISdkNoticeService sdkNoticeService;

    @Autowired
    private ISdkWhiteService sdkWhiteService;

    /**
     * 项目启动时，初始化服务器配置信息到缓存
     */
    @PostConstruct
    public void init()
    {
        hashMap.clear();
        List<SdkServer> sdkServerList = sdkServerMapper.selectSdkServerList(new SdkServer());
        for (SdkServer sdkServer : sdkServerList)
        {
            hashMap.put(sdkServer.getServerId(), sdkServer);
        }
        setServerUpdateHashMap(System.currentTimeMillis());
    }

    /**
     * 获取服务器map
     *
     * @return
     */
    @Override
    public ConcurrentHashMap<Long, SdkServer> getServerHashMap() {
        return hashMap;
    }

    /**
     * 获取缓存的服务器信息更新时间戳
     * @return
     */
    @Override
    public ConcurrentHashMap<String, Long> getServerUpdateHashMap() {
        return serverUpdateTimeMap;
    }

    /**
     * 设置缓存的服务器信息更新时间戳
     * @param serverUpdateTime
     */
    @Override
    public void setServerUpdateHashMap(Long serverUpdateTime) {
        this.serverUpdateTimeMap.put("updateServerTime",serverUpdateTime);
    }

    /**
     * 查询服务器配置信息
     * 
     * @param id 服务器配置信息ID
     * @return 服务器配置信息
     */
    @Override
    public SdkServer selectSdkServerById(Long id)
    {
        return sdkServerMapper.selectSdkServerById(id);
    }

    /**
     * 查询服务器配置信息列表
     * 
     * @param sdkServer 服务器配置信息
     * @return 服务器配置信息
     */
    @Override
    public List<SdkServer> selectSdkServerList(SdkServer sdkServer)
    {
        return sdkServerMapper.selectSdkServerList(sdkServer);
    }

    /**
     * 新增服务器配置信息
     * 
     * @param sdkServer 服务器配置信息
     * @return 结果
     */
    @Override
    public int insertSdkServer(SdkServer sdkServer)
    {
        if (sdkServer.getCreateTime() == null){
            sdkServer.setCreateTime(DateUtils.getNowDate());
        }
        int row = sdkServerMapper.insertSdkServer(sdkServer);
        if (row > 0){
            hashMap.put(sdkServer.getServerId(),sdkServer);
            setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 修改服务器配置信息
     * 
     * @param sdkServer 服务器配置信息
     * @return 结果
     */
    @Override
    public int updateSdkServer(SdkServer sdkServer)
    {
        sdkServer.setUpdateTime(DateUtils.getNowDate());
        int row = sdkServerMapper.updateSdkServer(sdkServer);
        if (row > 0){
            hashMap.put(sdkServer.getServerId(),sdkServer);
            setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除服务器配置信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSdkServerByIds(String ids)
    {
        String[] idsArr = Convert.toStrArray(ids);
        for (String id:idsArr){
            SdkServer sdkServer = selectSdkServerById(Long.valueOf(id));
            hashMap.remove(sdkServer.getServerId());
        }
        int row = sdkServerMapper.deleteSdkServerByIds(Convert.toStrArray(ids));
        if (row > 0){
            setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除服务器配置信息信息
     * 
     * @param id 服务器配置信息ID
     * @return 结果
     */
    @Override
    public int deleteSdkServerById(Long id)
    {
        SdkServer sdkServer = selectSdkServerById(id);
        hashMap.remove(sdkServer.getServerId());
        int row = sdkServerMapper.deleteSdkServerById(id);
        if (row > 0){
            setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 获取服务器列表可使用的服务器
     * @param serverIds
     * @return
     */
    @Override
    public List<SdkServer> selectServerByServerIds(String serverIds) {
        return sdkServerMapper.selectServerByServerIds(Convert.toStrArray(serverIds));
    }

    /**
     * 更新服务器开放状态
     * @param paramMap
     * @return
     */
    @Override
    public int updateSdkServerState(Map paramMap) {
        int openState = (int) paramMap.get("openState");
        int serverId = (int) paramMap.get("serverId");
        int row = sdkServerMapper.updateSdkServerState(paramMap);
        if (row > 0){
            SdkServer sdkServer = hashMap.get((long)serverId);
            sdkServer.setOpenState((long) openState);
//            setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }
    /**
     * 根据主键id查询渠道ID
     * @param ids
     * @return
     */
    @Override
    public List<Long> selectServerIdsByIds(String ids) {
        return sdkServerMapper.selectServerIdsByIds(Convert.toStrArray(ids));
    }

    /**
     * 查询服务器ID
     * @return
     */
    @Override
    public List<Long> selectServerIds() {
        return sdkServerMapper.selectServerIds();
    }

    @Override
    public void reloadSdkServer(Long curTime) {
        hashMap.clear();
        List<SdkServer> sdkServerList = sdkServerMapper.selectSdkServerList(new SdkServer());
        for (SdkServer sdkServer : sdkServerList)
        {
            hashMap.put(sdkServer.getServerId(), sdkServer);
        }
        setServerUpdateHashMap(curTime);
    }

    @Override
    public int updateSdkServerByServerId(Map paramMap) {
        String serverIp = String.valueOf(paramMap.get("serverIp"));
        Long serverPort = Long.valueOf(String.valueOf(paramMap.get("serverPort")));
        String[] serverIds = (String[]) paramMap.get("serverIds");
        int row = sdkServerMapper.updateSdkServerByServerId(paramMap);
        if (row > 0){
            for (String serverId:serverIds){
                SdkServer sdkServer = hashMap.get(Long.valueOf(serverId));
                if (null != sdkServer){
                    sdkServer.setServerIp(serverIp);
                    sdkServer.setServerPort(serverPort);
                }
            }
        }
        return row;
    }

    @Override
    public void setServerUpdateTime(Long curTime) {
        SdkServerUpdate sdkServerUpdate = new SdkServerUpdate();
        sdkServerUpdate.setUpdateTime(curTime);
        int row = sdkServerUpdateService.updateSdkServerUpdate(sdkServerUpdate);
        if (row <= 0){//说明数据库中没有任何数据,无法进行更新操作,则应该插入一条数据
            SdkServerUpdate sdkServerUpdate1 = new SdkServerUpdate();
            sdkServerUpdate1.setUpdateTime(curTime);
            sdkServerUpdateService.insertSdkServerUpdate(sdkServerUpdate1);
        }
    }

    @Override
    public void reloadAllCache(Long curTime) {
        sdkServerListService.reloadSdkServerList();
        sdkServerExtraService.reloadSdkServerExtra();
        sdkChannelService.reloadSdkChannel();
        sdkChannelidChannelidsService.reloadSdkChannelidChannelids();
        sdkLoginServerService.reloadSdkLoginServer();
        sdkNoticeService.reloadSdkNotice();
        sdkWhiteService.reloadSdkWhite();
        reloadSdkServer(curTime);
    }
}
