package com.kits.project.serverListConfig.channelids.mapper;

import java.util.List;
import java.util.Map;

import com.kits.project.serverListConfig.channelids.domain.SdkChannelidChannelids;

/**
 * 服务器列中的渠道ID和渠道ID列关系对应Mapper接口
 * 
 * @author gm
 * @date 2021-05-10
 */
public interface SdkChannelidChannelidsMapper 
{
    /**
     * 查询服务器列中的渠道ID和渠道ID列关系对应
     * 
     * @param channelId 服务器列中的渠道ID和渠道ID列关系对应ID
     * @return 服务器列中的渠道ID和渠道ID列关系对应
     */
    public SdkChannelidChannelids selectSdkChannelidChannelidsById(Long channelId);

    /**
     * 查询服务器列中的渠道ID和渠道ID列关系对应列表
     * 
     * @param sdkChannelidChannelids 服务器列中的渠道ID和渠道ID列关系对应
     * @return 服务器列中的渠道ID和渠道ID列关系对应集合
     */
    public List<SdkChannelidChannelids> selectSdkChannelidChannelidsList(SdkChannelidChannelids sdkChannelidChannelids);

    /**
     * 新增服务器列中的渠道ID和渠道ID列关系对应
     * 
     * @param sdkChannelidChannelids 服务器列中的渠道ID和渠道ID列关系对应
     * @return 结果
     */
    public int insertSdkChannelidChannelids(SdkChannelidChannelids sdkChannelidChannelids);

    /**
     * 修改服务器列中的渠道ID和渠道ID列关系对应
     * 
     * @param sdkChannelidChannelids 服务器列中的渠道ID和渠道ID列关系对应
     * @return 结果
     */
    public int updateSdkChannelidChannelids(SdkChannelidChannelids sdkChannelidChannelids);

    /**
     * 删除服务器列中的渠道ID和渠道ID列关系对应
     * 
     * @param channelId 服务器列中的渠道ID和渠道ID列关系对应ID
     * @return 结果
     */
    public int deleteSdkChannelidChannelidsById(Long channelId);

    /**
     * 批量删除服务器列中的渠道ID和渠道ID列关系对应
     * 
     * @param channelIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdkChannelidChannelidsByIds(String[] channelIds);


    /**
     * 替换修改服务器列中的渠道ID和渠道ID列关系对应
     * @param sdkChannelidChannelids
     * @return
     */
    public int replaceInsertSdkChannelidChannelids(SdkChannelidChannelids sdkChannelidChannelids);

    /**
     * 修改后删除没有选择的渠道ID对应的信息
     * @param paramMap
     * @return
     */
    public int deleteSdkChannelidChannelidsByChannelIds(Map paramMap);
}
