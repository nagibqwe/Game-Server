package com.kits.project.serverListConfig.channel.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kits.project.serverListConfig.server.service.ISdkServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.channel.mapper.SdkChannelMapper;
import com.kits.project.serverListConfig.channel.domain.SdkChannel;
import com.kits.project.serverListConfig.channel.service.ISdkChannelService;
import com.kits.common.utils.text.Convert;

import javax.annotation.PostConstruct;

/**
 * 渠道信息Service业务层处理
 * 
 * @author gm
 * @date 2021-04-28
 */
@Service
public class SdkChannelServiceImpl implements ISdkChannelService 
{
    private ConcurrentHashMap<Long, SdkChannel> hashMap = new ConcurrentHashMap<Long, SdkChannel>();

    @Autowired
    private SdkChannelMapper sdkChannelMapper;

    @Autowired
    private ISdkServerService sdkServerService;
    /**
     * 项目启动时，初始化渠道信息到缓存
     */
    @PostConstruct
    public void init()
    {
        hashMap.clear();
        List<SdkChannel> sdkChannelList = sdkChannelMapper.selectSdkChannelList(new SdkChannel());
        for (SdkChannel sdkChannel : sdkChannelList)
        {
            hashMap.put(sdkChannel.getChannelId(), sdkChannel);
        }
    }

    /**
     * 获取渠道map
     *
     * @return
     */
    @Override
    public ConcurrentHashMap<Long, SdkChannel> getChannelHashMap() {
        return hashMap;
    }


    /**
     * 查询渠道信息
     * 
     * @param id 渠道信息ID
     * @return 渠道信息
     */
    @Override
    public SdkChannel selectSdkChannelById(Long id)
    {
        return sdkChannelMapper.selectSdkChannelById(id);
    }

    /**
     * 查询渠道信息列表
     * 
     * @param sdkChannel 渠道信息
     * @return 渠道信息
     */
    @Override
    public List<SdkChannel> selectSdkChannelList(SdkChannel sdkChannel)
    {
        return sdkChannelMapper.selectSdkChannelList(sdkChannel);
    }

    /**
     * 新增渠道信息
     * 
     * @param sdkChannel 渠道信息
     * @return 结果
     */
    @Override
    public int insertSdkChannel(SdkChannel sdkChannel)
    {
        int row = sdkChannelMapper.insertSdkChannel(sdkChannel);
        if (row > 0){
            hashMap.put(sdkChannel.getChannelId(),sdkChannel);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 修改渠道信息
     * 
     * @param sdkChannel 渠道信息
     * @return 结果
     */
    @Override
    public int updateSdkChannel(SdkChannel sdkChannel)
    {
        int row = sdkChannelMapper.updateSdkChannel(sdkChannel);
        if (row > 0){
            hashMap.put(sdkChannel.getChannelId(),sdkChannel);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除渠道信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSdkChannelByIds(String ids)
    {
        String[] idsArr = Convert.toStrArray(ids);
        for (String id:idsArr){
            SdkChannel sdkChannel = selectSdkChannelById(Long.valueOf(id));
            hashMap.remove(sdkChannel.getChannelId());
        }
        int row = sdkChannelMapper.deleteSdkChannelByIds(Convert.toStrArray(ids));
        if (row > 0){
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除渠道信息信息
     * 
     * @param id 渠道信息ID
     * @return 结果
     */
    @Override
    public int deleteSdkChannelById(Long id)
    {
        SdkChannel sdkChannel = selectSdkChannelById(id);
        hashMap.remove(sdkChannel.getChannelId());
        int row = sdkChannelMapper.deleteSdkChannelById(id);
        if (row > 0){
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 获取可用渠道(包括已有的渠道)
     * @param channelIds 渠道IDs
     * @return
     */
    @Override
    public List<SdkChannel> selectChannels(String channelIds) {
        return sdkChannelMapper.selectChannels(Convert.toStrArray(channelIds));
    }

    /**
     * 获取页面展示的渠道(服务器列表已有的渠道数据)
     * @param channelIds 渠道IDs
     * @return
     */
    @Override
    public List<SdkChannel> selectShowChannels(String channelIds) {
        return sdkChannelMapper.selectShowChannels(Convert.toStrArray(channelIds));
    }

    /**
     * 修改渠道使用状态
     * @param paramMap
     * @return
     */
    @Override
    public int updateSdkChannelIsUse(Map paramMap) {
        int row = sdkChannelMapper.updateSdkChannelIsUse(paramMap);
        if (row > 0){
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 根据主键id查询渠道ID
     * @param ids
     * @return
     */
    @Override
    public List<Long> selectChannelIdsByIds(String ids) {
        return sdkChannelMapper.selectChannelIdsByIds(Convert.toStrArray(ids));
    }

    @Override
    public void reloadSdkChannel() {
        init();
    }
}
