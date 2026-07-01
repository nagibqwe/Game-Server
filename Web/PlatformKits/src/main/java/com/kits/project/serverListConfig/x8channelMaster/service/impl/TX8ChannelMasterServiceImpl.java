package com.kits.project.serverListConfig.x8channelMaster.service.impl;

import java.util.List;
import com.kits.common.utils.DateUtils;
import com.kits.project.serverList.domain.ChannelGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.x8channelMaster.mapper.TX8ChannelMasterMapper;
import com.kits.project.serverListConfig.x8channelMaster.domain.TX8ChannelMaster;
import com.kits.project.serverListConfig.x8channelMaster.service.ITX8ChannelMasterService;
import com.kits.common.utils.text.Convert;

/**
 * 渠道商-实际的发行渠道商,360,小米等Service业务层处理
 * 
 * @author gm
 * @date 2021-07-08
 */
@Service
public class TX8ChannelMasterServiceImpl implements ITX8ChannelMasterService 
{
    @Autowired
    private TX8ChannelMasterMapper tX8ChannelMasterMapper;

    /**
     * 查询渠道商-实际的发行渠道商,360,小米等
     * 
     * @param chnmId 渠道商-实际的发行渠道商,360,小米等ID
     * @return 渠道商-实际的发行渠道商,360,小米等
     */
    @Override
    public TX8ChannelMaster selectTX8ChannelMasterById(Long chnmId)
    {
        return tX8ChannelMasterMapper.selectTX8ChannelMasterById(chnmId);
    }

    /**
     * 查询渠道商-实际的发行渠道商,360,小米等列表
     * 
     * @param tX8ChannelMaster 渠道商-实际的发行渠道商,360,小米等
     * @return 渠道商-实际的发行渠道商,360,小米等
     */
    @Override
    public List<TX8ChannelMaster> selectTX8ChannelMasterList(TX8ChannelMaster tX8ChannelMaster)
    {
        return tX8ChannelMasterMapper.selectTX8ChannelMasterList(tX8ChannelMaster);
    }

    /**
     * 新增渠道商-实际的发行渠道商,360,小米等
     * 
     * @param tX8ChannelMaster 渠道商-实际的发行渠道商,360,小米等
     * @return 结果
     */
    @Override
    public int insertTX8ChannelMaster(TX8ChannelMaster tX8ChannelMaster)
    {
        tX8ChannelMaster.setCreateTime(DateUtils.getNowDate());
        return tX8ChannelMasterMapper.insertTX8ChannelMaster(tX8ChannelMaster);
    }

    /**
     * 修改渠道商-实际的发行渠道商,360,小米等
     * 
     * @param tX8ChannelMaster 渠道商-实际的发行渠道商,360,小米等
     * @return 结果
     */
    @Override
    public int updateTX8ChannelMaster(TX8ChannelMaster tX8ChannelMaster)
    {
        tX8ChannelMaster.setUpdateTime(DateUtils.getNowDate());
        return tX8ChannelMasterMapper.updateTX8ChannelMaster(tX8ChannelMaster);
    }

    /**
     * 删除渠道商-实际的发行渠道商,360,小米等对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTX8ChannelMasterByIds(String ids)
    {
        return tX8ChannelMasterMapper.deleteTX8ChannelMasterByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除渠道商-实际的发行渠道商,360,小米等信息
     * 
     * @param chnmId 渠道商-实际的发行渠道商,360,小米等ID
     * @return 结果
     */
    @Override
    public int deleteTX8ChannelMasterById(Long chnmId)
    {
        return tX8ChannelMasterMapper.deleteTX8ChannelMasterById(chnmId);
    }

    /**
     * 查询信息渠道信息
     * @return
     */
    @Override
    public List<ChannelGrid> selectChannelInfo() {
        return tX8ChannelMasterMapper.selectChannelInfo();
    }
}
