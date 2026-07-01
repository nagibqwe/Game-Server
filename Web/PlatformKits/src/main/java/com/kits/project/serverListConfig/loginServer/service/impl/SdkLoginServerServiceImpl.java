package com.kits.project.serverListConfig.loginServer.service.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.kits.project.serverListConfig.server.service.ISdkServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.loginServer.mapper.SdkLoginServerMapper;
import com.kits.project.serverListConfig.loginServer.domain.SdkLoginServer;
import com.kits.project.serverListConfig.loginServer.service.ISdkLoginServerService;
import com.kits.common.utils.text.Convert;

import javax.annotation.PostConstruct;

/**
 * 登录服信息Service业务层处理
 * 
 * @author gm
 * @date 2021-10-20
 */
@Service
public class SdkLoginServerServiceImpl implements ISdkLoginServerService 
{
    private ConcurrentHashMap<Long, SdkLoginServer> hashMap = new ConcurrentHashMap<Long, SdkLoginServer>();

    @Autowired
    private SdkLoginServerMapper sdkLoginServerMapper;

    @Autowired
    private ISdkServerService sdkServerService;

    /**
     * 项目启动时，初始化登录服信息到缓存
     */
    @PostConstruct
    public void init()
    {
        hashMap.clear();
        List<SdkLoginServer> sdkLoginServerList = sdkLoginServerMapper.selectSdkLoginServerList(new SdkLoginServer());
        for (SdkLoginServer sdkLoginServer : sdkLoginServerList)
        {
            hashMap.put(sdkLoginServer.getId(), sdkLoginServer);
        }
    }

    /**
     * 获取登录服map
     * @return
     */
    @Override
    public ConcurrentHashMap<Long, SdkLoginServer> getLoginServerHashMap() {
        return hashMap;
    }

    /**
     * 查询登录服信息
     * 
     * @param id 登录服信息ID
     * @return 登录服信息
     */
    @Override
    public SdkLoginServer selectSdkLoginServerById(Long id)
    {
        return sdkLoginServerMapper.selectSdkLoginServerById(id);
    }

    /**
     * 查询登录服信息列表
     * 
     * @param sdkLoginServer 登录服信息
     * @return 登录服信息
     */
    @Override
    public List<SdkLoginServer> selectSdkLoginServerList(SdkLoginServer sdkLoginServer)
    {
        return sdkLoginServerMapper.selectSdkLoginServerList(sdkLoginServer);
    }

    /**
     * 新增登录服信息
     * 
     * @param sdkLoginServer 登录服信息
     * @return 结果
     */
    @Override
    public int insertSdkLoginServer(SdkLoginServer sdkLoginServer)
    {
        int row = sdkLoginServerMapper.insertSdkLoginServer(sdkLoginServer);
        if (row > 0){
            hashMap.put(sdkLoginServer.getId(),sdkLoginServer);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 修改登录服信息
     * 
     * @param sdkLoginServer 登录服信息
     * @return 结果
     */
    @Override
    public int updateSdkLoginServer(SdkLoginServer sdkLoginServer)
    {
        int row = sdkLoginServerMapper.updateSdkLoginServer(sdkLoginServer);
        if (row > 0){
            hashMap.put(sdkLoginServer.getId(),sdkLoginServer);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除登录服信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSdkLoginServerByIds(String ids)
    {
        String[] idsArr = Convert.toStrArray(ids);
        for (String id:idsArr){
            SdkLoginServer sdkLoginServer = selectSdkLoginServerById(Long.valueOf(id));
            hashMap.remove(sdkLoginServer.getId());
        }
        int row = sdkLoginServerMapper.deleteSdkLoginServerByIds(Convert.toStrArray(ids));
        if (row > 0){
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除登录服信息信息
     * 
     * @param id 登录服信息ID
     * @return 结果
     */
    @Override
    public int deleteSdkLoginServerById(Long id)
    {
        SdkLoginServer sdkLoginServer = selectSdkLoginServerById(id);
        hashMap.remove(sdkLoginServer.getId());
        int row = sdkLoginServerMapper.deleteSdkLoginServerById(id);
        if (row > 0){
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 获取页面展示登录服组(已有的登录服数据)
     * @param ids
     * @return
     */
    @Override
    public List<SdkLoginServer> selectShowLoginServers(String ids) {
        return sdkLoginServerMapper.selectShowLoginServers(Convert.toStrArray(ids));
    }

    @Override
    public void reloadSdkLoginServer() {
        init();
    }
}
