package com.kits.project.serverListConfig.channelids.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kits.project.serverListConfig.server.service.ISdkServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.channelids.mapper.SdkChannelidChannelidsMapper;
import com.kits.project.serverListConfig.channelids.domain.SdkChannelidChannelids;
import com.kits.project.serverListConfig.channelids.service.ISdkChannelidChannelidsService;
import com.kits.common.utils.text.Convert;

import javax.annotation.PostConstruct;

/**
 * 服务器列中的渠道ID和渠道ID列关系对应Service业务层处理
 * 
 * @author gm
 * @date 2021-05-10
 */
@Service
public class SdkChannelidChannelidsServiceImpl implements ISdkChannelidChannelidsService 
{
    private ConcurrentHashMap<Long, SdkChannelidChannelids> hashMap = new ConcurrentHashMap<Long, SdkChannelidChannelids>();

    @Autowired
    private SdkChannelidChannelidsMapper sdkChannelidChannelidsMapper;

    @Autowired
    private ISdkServerService sdkServerService;
    /**
     * 项目启动时，初始化渠道ID和渠道ID列表关系对应到缓存
     */
    @PostConstruct
    public void init()
    {
        hashMap.clear();
        List<SdkChannelidChannelids> sdkChannelidChannelids = sdkChannelidChannelidsMapper.selectSdkChannelidChannelidsList(new SdkChannelidChannelids());
        for (SdkChannelidChannelids sdkChannelidChannelids1 : sdkChannelidChannelids)
        {
            hashMap.put(sdkChannelidChannelids1.getChannelId(), sdkChannelidChannelids1);
        }
    }

    /**
     * 获取渠道ID和渠道ID列表关系对应map
     *
     * @return
     */
    @Override
    public ConcurrentHashMap<Long, SdkChannelidChannelids> getChannelidChannelidsHashMap() {
        return hashMap;
    }

    /**
     * 查询服务器列中的渠道ID和渠道ID列关系对应
     * 
     * @param channelId 服务器列中的渠道ID和渠道ID列关系对应ID
     * @return 服务器列中的渠道ID和渠道ID列关系对应
     */
    @Override
    public SdkChannelidChannelids selectSdkChannelidChannelidsById(Long channelId)
    {
        return sdkChannelidChannelidsMapper.selectSdkChannelidChannelidsById(channelId);
    }

    /**
     * 查询服务器列中的渠道ID和渠道ID列关系对应列表
     * 
     * @param sdkChannelidChannelids 服务器列中的渠道ID和渠道ID列关系对应
     * @return 服务器列中的渠道ID和渠道ID列关系对应
     */
    @Override
    public List<SdkChannelidChannelids> selectSdkChannelidChannelidsList(SdkChannelidChannelids sdkChannelidChannelids)
    {
        return sdkChannelidChannelidsMapper.selectSdkChannelidChannelidsList(sdkChannelidChannelids);
    }

    /**
     * 新增服务器列中的渠道ID和渠道ID列关系对应
     * 
     * @param sdkChannelidChannelids 服务器列中的渠道ID和渠道ID列关系对应
     * @return 结果
     */
    @Override
    public int insertSdkChannelidChannelids(SdkChannelidChannelids sdkChannelidChannelids)
    {
        int row = sdkChannelidChannelidsMapper.insertSdkChannelidChannelids(sdkChannelidChannelids);
        if (row > 0){
            hashMap.put(sdkChannelidChannelids.getChannelId(),sdkChannelidChannelids);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 修改服务器列中的渠道ID和渠道ID列关系对应
     * 
     * @param sdkChannelidChannelids 服务器列中的渠道ID和渠道ID列关系对应
     * @return 结果
     */
    @Override
    public int updateSdkChannelidChannelids(SdkChannelidChannelids sdkChannelidChannelids)
    {
        int row = sdkChannelidChannelidsMapper.updateSdkChannelidChannelids(sdkChannelidChannelids);
        if (row > 0){
            hashMap.put(sdkChannelidChannelids.getChannelId(),sdkChannelidChannelids);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除服务器列中的渠道ID和渠道ID列关系对应对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSdkChannelidChannelidsByIds(String ids)
    {
        String[] channelIdArr = Convert.toStrArray(ids);
        int row = sdkChannelidChannelidsMapper.deleteSdkChannelidChannelidsByIds(Convert.toStrArray(ids));
        if (row > 0){
            for (String channelId:channelIdArr){
                hashMap.remove(Long.valueOf(channelId));
            }
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除服务器列中的渠道ID和渠道ID列关系对应信息`
     * 
     * @param channelId 服务器列中的渠道ID和渠道ID列关系对应ID
     * @return 结果
     */
    @Override
    public int deleteSdkChannelidChannelidsById(Long channelId)
    {
        int row = sdkChannelidChannelidsMapper.deleteSdkChannelidChannelidsById(channelId);
        if (row > 0){
            hashMap.remove(channelId);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 替换修改服务器列中的渠道ID和渠道ID列关系对应
     * @param sdkChannelidChannelids
     * @return
     */
    @Override
    public int replaceInsertSdkChannelidChannelids(SdkChannelidChannelids sdkChannelidChannelids) {
        int row = sdkChannelidChannelidsMapper.replaceInsertSdkChannelidChannelids(sdkChannelidChannelids);
        if (row > 0){
            hashMap.put(sdkChannelidChannelids.getChannelId(),sdkChannelidChannelids);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 修改后删除没有选择的渠道ID对应的信息
     * @param paramMap
     * @return
     */
    @Override
    public int deleteSdkChannelidChannelidsByChannelIds(Map paramMap) {
        List<String> deleteList = (List<String>) paramMap.get("channelIds");
        int row = sdkChannelidChannelidsMapper.deleteSdkChannelidChannelidsByChannelIds(paramMap);
        if (row > 0){
            for (String deleteChannelId:deleteList){
                hashMap.remove(Long.valueOf(deleteChannelId));
            }
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    @Override
    public void reloadSdkChannelidChannelids() {
        init();
    }
}
