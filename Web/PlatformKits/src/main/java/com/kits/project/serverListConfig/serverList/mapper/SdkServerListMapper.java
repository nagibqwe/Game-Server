package com.kits.project.serverListConfig.serverList.mapper;

import java.util.List;
import com.kits.project.serverListConfig.serverList.domain.SdkServerList;

/**
 * 服务器列表Mapper接口
 * 
 * @author gm
 * @date 2021-04-26
 */
public interface SdkServerListMapper 
{
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
    public int updateSdkServerList(SdkServerList sdkServerList);

    /**
     * 删除服务器列表
     * 
     * @param id 服务器列表ID
     * @return 结果
     */
    public int deleteSdkServerListById(Long id);

    /**
     * 批量删除服务器列表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdkServerListByIds(String[] ids);

    /**
     * 根据服务器列表ID获取对应的ServerIds
     * @param id 服务器列表ID
     * @return
     */
    String selectServerIdsById(Long id);

    /**
     * 根据服务器列表ID获取对应的ChannelIds
     * @param id 服务器列表ID
     * @return
     */
    String selectChannelIdsById(Long id);
}
