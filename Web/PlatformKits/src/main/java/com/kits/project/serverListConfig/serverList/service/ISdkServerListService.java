package com.kits.project.serverListConfig.serverList.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.kits.project.serverListConfig.serverList.domain.SdkServerList;

/**
 * 服务器列表Service接口
 * 
 * @author gm
 * @date 2021-04-26
 */
public interface ISdkServerListService 
{
    /**
     * 获取服务器列表信息map
     *
     * @return
     */
    public ConcurrentHashMap<Long, SdkServerList> getServerListHashMap();

    /**
     * 查询服务器列表
     * 
     * @param id 服务器列表ID
     * @return 服务器列表
     */
    public SdkServerList selectSdkServerListById(Long id);

    /**
     * 查询服务器列表列表
     * 
     * @param sdkServerList 服务器列表
     * @return 服务器列表集合
     */
    public List<SdkServerList> selectSdkServerListList(SdkServerList sdkServerList);

    /**
     * 新增服务器列表
     * 
     * @param sdkServerList 服务器列表
     * @return 结果
     */
    public int insertSdkServerList(SdkServerList sdkServerList);

    /**
     * 修改服务器列表
     * 
     * @param sdkServerList 服务器列表
     * @return 结果
     */
    public int updateSdkServerList(SdkServerList sdkServerList,int updateType);

    /**
     * 批量删除服务器列表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdkServerListByIds(String ids);

    /**
     * 删除服务器列表信息
     * 
     * @param id 服务器列表ID
     * @return 结果
     */
    public int deleteSdkServerListById(Long id);

    /**
     * 通过服务器列表id获取对应的ServerIds
     * @param id
     * @return
     */
    public String selectServerIdsById(Long id);

    /**
     * 通过服务器列表id获取对应的ChannelIds
     * @param id
     * @return
     */
    public String selectChannelIdsById(Long id);

    public void reloadSdkServerList();
}
