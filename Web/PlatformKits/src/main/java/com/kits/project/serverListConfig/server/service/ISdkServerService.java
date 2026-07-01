package com.kits.project.serverListConfig.server.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kits.project.serverListConfig.server.domain.SdkServer;

/**
 * 服务器配置信息Service接口
 * 
 * @author gm
 * @date 2021-04-25
 */
public interface ISdkServerService 
{
    /**
     * 获取服务器map
     *
     * @return
     */
    public ConcurrentHashMap<Long, SdkServer> getServerHashMap();

    /**
     * 获取缓存的服务器信息更新时间戳
     * @return
     */
    public ConcurrentHashMap<String, Long> getServerUpdateHashMap();

    /**
     * 设置缓存的服务器信息更新时间戳
     * @param serverUpdateTime
     */
    public void setServerUpdateHashMap(Long serverUpdateTime);

    /**
     * 查询服务器配置信息
     * 
     * @param id 服务器配置信息ID
     * @return 服务器配置信息
     */
    public SdkServer selectSdkServerById(Long id);

    /**
     * 查询服务器配置信息列表
     * 
     * @param sdkServer 服务器配置信息
     * @return 服务器配置信息集合
     */
    public List<SdkServer> selectSdkServerList(SdkServer sdkServer);

    /**
     * 新增服务器配置信息
     * 
     * @param sdkServer 服务器配置信息
     * @return 结果
     */
    public int insertSdkServer(SdkServer sdkServer);

    /**
     * 修改服务器配置信息
     * 
     * @param sdkServer 服务器配置信息
     * @return 结果
     */
    public int updateSdkServer(SdkServer sdkServer);

    /**
     * 批量删除服务器配置信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdkServerByIds(String ids);

    /**
     * 删除服务器配置信息信息
     * 
     * @param id 服务器配置信息ID
     * @return 结果
     */
    public int deleteSdkServerById(Long id);

    /**
     * 获取服务器列表可使用的服务器
     * @param serverIds
     * @return
     */
    public List<SdkServer> selectServerByServerIds(String serverIds);

    /**
     * 更新服务器开放状态
     * @param paramMap
     * @return
     */
    public int updateSdkServerState(Map paramMap);

    /**
     * 根据主键id查询渠道ID
     * @param ids
     * @return
     */
    public List<Long> selectServerIdsByIds(String ids);

    /**
     * 查询服务器ID
     * @return
     */
    public List<Long> selectServerIds();

    /**
     * 重新加载
     */
    public void reloadSdkServer(Long curTime);

    public int updateSdkServerByServerId(Map paramMap);

    /**
     *只要其中一个表改变则更新数据库中所记的时间戳
     */
    public void setServerUpdateTime(Long curTime);

    /**
     * 重新加载全部有缓存的数据
     */
    public void reloadAllCache(Long curTime);
}
