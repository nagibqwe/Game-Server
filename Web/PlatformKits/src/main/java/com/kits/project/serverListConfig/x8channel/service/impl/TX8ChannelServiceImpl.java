package com.kits.project.serverListConfig.x8channel.service.impl;

import java.util.List;
import com.kits.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.x8channel.mapper.TX8ChannelMapper;
import com.kits.project.serverListConfig.x8channel.domain.TX8Channel;
import com.kits.project.serverListConfig.x8channel.service.ITX8ChannelService;
import com.kits.common.utils.text.Convert;

/**
 * 渠道-包含游戏各个渠道商的配置Service业务层处理
 * 
 * @author gm
 * @date 2021-07-08
 */
@Service
public class TX8ChannelServiceImpl implements ITX8ChannelService 
{
    @Autowired
    private TX8ChannelMapper tX8ChannelMapper;

    /**
     * 查询渠道-包含游戏各个渠道商的配置
     * 
     * @param chnId 渠道-包含游戏各个渠道商的配置ID
     * @return 渠道-包含游戏各个渠道商的配置
     */
    @Override
    public TX8Channel selectTX8ChannelById(Long chnId)
    {
        return tX8ChannelMapper.selectTX8ChannelById(chnId);
    }

    /**
     * 查询渠道-包含游戏各个渠道商的配置列表
     * 
     * @param tX8Channel 渠道-包含游戏各个渠道商的配置
     * @return 渠道-包含游戏各个渠道商的配置
     */
    @Override
    public List<TX8Channel> selectTX8ChannelList(TX8Channel tX8Channel)
    {
        return tX8ChannelMapper.selectTX8ChannelList(tX8Channel);
    }

    /**
     * 新增渠道-包含游戏各个渠道商的配置
     * 
     * @param tX8Channel 渠道-包含游戏各个渠道商的配置
     * @return 结果
     */
    @Override
    public int insertTX8Channel(TX8Channel tX8Channel)
    {
        tX8Channel.setCreateTime(DateUtils.getNowDate());
        return tX8ChannelMapper.insertTX8Channel(tX8Channel);
    }

    /**
     * 修改渠道-包含游戏各个渠道商的配置
     * 
     * @param tX8Channel 渠道-包含游戏各个渠道商的配置
     * @return 结果
     */
    @Override
    public int updateTX8Channel(TX8Channel tX8Channel)
    {
        tX8Channel.setUpdateTime(DateUtils.getNowDate());
        return tX8ChannelMapper.updateTX8Channel(tX8Channel);
    }

    /**
     * 删除渠道-包含游戏各个渠道商的配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTX8ChannelByIds(String ids)
    {
        return tX8ChannelMapper.deleteTX8ChannelByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除渠道-包含游戏各个渠道商的配置信息
     * 
     * @param chnId 渠道-包含游戏各个渠道商的配置ID
     * @return 结果
     */
    @Override
    public int deleteTX8ChannelById(Long chnId)
    {
        return tX8ChannelMapper.deleteTX8ChannelById(chnId);
    }
}
