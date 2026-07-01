package com.kits.project.serverListConfig.serverList.service.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.kits.common.utils.DateUtils;
import com.kits.project.serverListConfig.server.service.ISdkServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.serverList.mapper.SdkServerListMapper;
import com.kits.project.serverListConfig.serverList.domain.SdkServerList;
import com.kits.project.serverListConfig.serverList.service.ISdkServerListService;
import com.kits.common.utils.text.Convert;

import javax.annotation.PostConstruct;

/**
 * 服务器列表Service业务层处理
 * 
 * @author gm
 * @date 2021-04-26
 */
@Service
public class SdkServerListServiceImpl implements ISdkServerListService 
{
    private ConcurrentHashMap<Long,SdkServerList> hashMap = new ConcurrentHashMap<Long,SdkServerList>();

    @Autowired
    private SdkServerListMapper sdkServerListMapper;

    @Autowired
    private ISdkServerService sdkServerService;

    /**
     * 项目启动时，初始化服务器列表信息到缓存
     */
    @PostConstruct
    public void init()
    {
        hashMap.clear();
        List<SdkServerList> sdkServerListList = sdkServerListMapper.selectSdkServerListList(new SdkServerList());
        for (SdkServerList sdkServerList : sdkServerListList)
        {
            hashMap.put(sdkServerList.getId(), sdkServerList);
        }
    }

    /**
     * 获取服务器列表信息map
     *
     * @return
     */
    @Override
    public ConcurrentHashMap<Long, SdkServerList> getServerListHashMap() {
        return hashMap;
    }

    /**
     * 查询服务器列表
     * 
     * @param id 服务器列表ID
     * @return 服务器列表
     */
    @Override
    public SdkServerList selectSdkServerListById(Long id)
    {
        return sdkServerListMapper.selectSdkServerListById(id);
    }

    /**
     * 查询服务器列表列表
     * 
     * @param sdkServerList 服务器列表
     * @return 服务器列表
     */
    @Override
    public List<SdkServerList> selectSdkServerListList(SdkServerList sdkServerList)
    {
        return sdkServerListMapper.selectSdkServerListList(sdkServerList);
    }

    /**
     * 新增服务器列表
     * 
     * @param sdkServerList 服务器列表
     * @return 结果
     */
    @Override
    public int insertSdkServerList(SdkServerList sdkServerList)
    {
        if (sdkServerList.getCreateTime() == null){
            sdkServerList.setCreateTime(DateUtils.getNowDate());
        }
        int row = sdkServerListMapper.insertSdkServerList(sdkServerList);
        if (row > 0){
            hashMap.put(sdkServerList.getId(),sdkServerList);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 修改服务器列表
     * 
     * @param sdkServerList 服务器列表
     * @return 结果
     */
    @Override
    public int updateSdkServerList(SdkServerList sdkServerList,int updateType)
    {
        sdkServerList.setUpdateTime(DateUtils.getNowDate());
        int row = sdkServerListMapper.updateSdkServerList(sdkServerList);
        if (row > 0){
            if (updateType == 0){//更新ServerIds的数据
                SdkServerList sdkServerList1 = hashMap.get(sdkServerList.getId());
                sdkServerList1.setServerIds(sdkServerList.getServerIds());
                hashMap.put(sdkServerList.getId(),sdkServerList1);
            }else if (updateType == 1){//更新ChannelIds的数据
                SdkServerList sdkServerList1 = hashMap.get(sdkServerList.getId());
                sdkServerList1.setChannelIds(sdkServerList.getChannelIds());
                hashMap.put(sdkServerList.getId(),sdkServerList1);
            }else if (updateType == 2){//更新数据
                hashMap.put(sdkServerList.getId(),sdkServerList);
            }else if (updateType == 3){//更新loginServerGroup的数据
                SdkServerList sdkServerList1 = hashMap.get(sdkServerList.getId());
                sdkServerList1.setLoginServerGroup(sdkServerList.getLoginServerGroup());
                hashMap.put(sdkServerList.getId(),sdkServerList1);
            }
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除服务器列表对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSdkServerListByIds(String ids)
    {
        String[] serverListIdsArr = Convert.toStrArray(ids);
        for (String serverListId:serverListIdsArr){
            SdkServerList sdkServerList = selectSdkServerListById(Long.valueOf(serverListId));
            hashMap.remove(sdkServerList.getId());
        }
        int row = sdkServerListMapper.deleteSdkServerListByIds(Convert.toStrArray(ids));
        if (row > 0){
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除服务器列表信息
     * 
     * @param id 根据服务器列表ID删除服务器列表数据
     * @return 结果
     */
    @Override
    public int deleteSdkServerListById(Long id)
    {
        int row = sdkServerListMapper.deleteSdkServerListById(id);
        if (row > 0){
            hashMap.remove(id);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 根据服务器列表ID获取对应的ServerIds
     * @param id 服务器列表ID
     * @return
     */
    @Override
    public String selectServerIdsById(Long id) {
        return sdkServerListMapper.selectServerIdsById(id);
    }

    /**
     * 根据服务器列表ID获取对应的ChannelIds
     * @param id 服务器列表ID
     * @return
     */
    @Override
    public String selectChannelIdsById(Long id) {
        return sdkServerListMapper.selectChannelIdsById(id);
    }

    @Override
    public void reloadSdkServerList() {
        init();
    }
}
