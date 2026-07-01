package com.kits.project.serverListConfig.x8channel.service;

import java.util.List;
import com.kits.project.serverListConfig.x8channel.domain.TX8Channel;

/**
 * 渠道-包含游戏各个渠道商的配置Service接口
 * 
 * @author gm
 * @date 2021-07-08
 */
public interface ITX8ChannelService 
{
    /**
     * 查询渠道-包含游戏各个渠道商的配置
     * 
     * @param chnId 渠道-包含游戏各个渠道商的配置ID
     * @return 渠道-包含游戏各个渠道商的配置
     */
    public TX8Channel selectTX8ChannelById(Long chnId);

    /**
     * 查询渠道-包含游戏各个渠道商的配置列表
     * 
     * @param tX8Channel 渠道-包含游戏各个渠道商的配置
     * @return 渠道-包含游戏各个渠道商的配置集合
     */
    public List<TX8Channel> selectTX8ChannelList(TX8Channel tX8Channel);

    /**
     * 新增渠道-包含游戏各个渠道商的配置
     * 
     * @param tX8Channel 渠道-包含游戏各个渠道商的配置
     * @return 结果
     */
    public int insertTX8Channel(TX8Channel tX8Channel);

    /**
     * 修改渠道-包含游戏各个渠道商的配置
     * 
     * @param tX8Channel 渠道-包含游戏各个渠道商的配置
     * @return 结果
     */
    public int updateTX8Channel(TX8Channel tX8Channel);

    /**
     * 批量删除渠道-包含游戏各个渠道商的配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTX8ChannelByIds(String ids);

    /**
     * 删除渠道-包含游戏各个渠道商的配置信息
     * 
     * @param chnId 渠道-包含游戏各个渠道商的配置ID
     * @return 结果
     */
    public int deleteTX8ChannelById(Long chnId);
}
