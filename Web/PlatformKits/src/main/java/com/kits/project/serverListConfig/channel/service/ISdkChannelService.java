package com.kits.project.serverListConfig.channel.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kits.project.serverListConfig.channel.domain.SdkChannel;

/**
 * 渠道信息Service接口
 * 
 * @author gm
 * @date 2021-04-28
 */
public interface ISdkChannelService 
{
    /**
     * 获取渠道map
     *
     * @return
     */
    public ConcurrentHashMap<Long, SdkChannel> getChannelHashMap();

    /**
     * 查询渠道信息
     * 
     * @param id 渠道信息ID
     * @return 渠道信息
     */
    public SdkChannel selectSdkChannelById(Long id);

    /**
     * 查询渠道信息列表
     * 
     * @param sdkChannel 渠道信息
     * @return 渠道信息集合
     */
    public List<SdkChannel> selectSdkChannelList(SdkChannel sdkChannel);

    /**
     * 新增渠道信息
     * 
     * @param sdkChannel 渠道信息
     * @return 结果
     */
    public int insertSdkChannel(SdkChannel sdkChannel);

    /**
     * 修改渠道信息
     * 
     * @param sdkChannel 渠道信息
     * @return 结果
     */
    public int updateSdkChannel(SdkChannel sdkChannel);

    /**
     * 批量删除渠道信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdkChannelByIds(String ids);

    /**
     * 删除渠道信息信息
     * 
     * @param id 渠道信息ID
     * @return 结果
     */
    public int deleteSdkChannelById(Long id);

    /**
     * 获取可用渠道(包括已有的渠道)
     * @param channelIds 渠道IDs
     * @return
     */
    public List<SdkChannel> selectChannels(String channelIds);

    /**
     * 获取页面展示的渠道(服务器列表已有的渠道数据)
     * @param channelIds 渠道IDs
     * @return
     */
    public List<SdkChannel> selectShowChannels(String channelIds);

    /**
     * 修改渠道使用状态
     * @param paramMap
     * @return
     */
    public int updateSdkChannelIsUse(Map paramMap);

    /**
     * 根据主键id查询渠道ID
     * @param ids
     * @return
     */
    public List<Long> selectChannelIdsByIds(String ids);

    public void reloadSdkChannel();
}
