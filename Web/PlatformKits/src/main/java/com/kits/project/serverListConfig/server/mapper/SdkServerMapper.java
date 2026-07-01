package com.kits.project.serverListConfig.server.mapper;

import java.util.List;
import java.util.Map;

import com.kits.project.serverListConfig.server.domain.SdkServer;

/**
 * 服务器配置信息Mapper接口
 * 
 * @author gm
 * @date 2021-04-25
 */
public interface SdkServerMapper 
{
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
     * 删除服务器配置信息
     * 
     * @param id 服务器配置信息ID
     * @return 结果
     */
    public int deleteSdkServerById(Long id);

    /**
     * 批量删除服务器配置信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdkServerByIds(String[] ids);

    /**
     * 获取服务器列表可使用的服务器
     * @param serverIds
     * @return
     */
    public List<SdkServer> selectServerByServerIds(String[] serverIds);

    /**
     * 更新服务器开放状态
     * @param paramMap
     * @return
     */
    public int updateSdkServerState(Map paramMap);

    /**
     * 根据主键id查询服务器ID
     * @param ids
     * @return
     */
    public List<Long> selectServerIdsByIds(String[] ids);

    /**
     * 查询服务器ID
     * @return
     */
    public List<Long> selectServerIds();

    public int updateSdkServerByServerId(Map paramMap);
}
