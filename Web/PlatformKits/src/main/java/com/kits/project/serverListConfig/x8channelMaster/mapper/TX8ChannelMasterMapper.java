package com.kits.project.serverListConfig.x8channelMaster.mapper;

import java.util.List;

import com.kits.project.serverList.domain.ChannelGrid;
import com.kits.project.serverListConfig.x8channelMaster.domain.TX8ChannelMaster;

/**
 * 渠道商-实际的发行渠道商,360,小米等Mapper接口
 * 
 * @author gm
 * @date 2021-07-08
 */
public interface TX8ChannelMasterMapper 
{
    /**
     * 查询渠道商-实际的发行渠道商,360,小米等
     * 
     * @param chnmId 渠道商-实际的发行渠道商,360,小米等ID
     * @return 渠道商-实际的发行渠道商,360,小米等
     */
    public TX8ChannelMaster selectTX8ChannelMasterById(Long chnmId);

    /**
     * 查询渠道商-实际的发行渠道商,360,小米等列表
     * 
     * @param tX8ChannelMaster 渠道商-实际的发行渠道商,360,小米等
     * @return 渠道商-实际的发行渠道商,360,小米等集合
     */
    public List<TX8ChannelMaster> selectTX8ChannelMasterList(TX8ChannelMaster tX8ChannelMaster);

    /**
     * 新增渠道商-实际的发行渠道商,360,小米等
     * 
     * @param tX8ChannelMaster 渠道商-实际的发行渠道商,360,小米等
     * @return 结果
     */
    public int insertTX8ChannelMaster(TX8ChannelMaster tX8ChannelMaster);

    /**
     * 修改渠道商-实际的发行渠道商,360,小米等
     * 
     * @param tX8ChannelMaster 渠道商-实际的发行渠道商,360,小米等
     * @return 结果
     */
    public int updateTX8ChannelMaster(TX8ChannelMaster tX8ChannelMaster);

    /**
     * 删除渠道商-实际的发行渠道商,360,小米等
     * 
     * @param chnmId 渠道商-实际的发行渠道商,360,小米等ID
     * @return 结果
     */
    public int deleteTX8ChannelMasterById(Long chnmId);

    /**
     * 批量删除渠道商-实际的发行渠道商,360,小米等
     * 
     * @param chnmIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteTX8ChannelMasterByIds(String[] chnmIds);

    /**
     * 查询信息渠道信息
     * @return
     */
    public List<ChannelGrid> selectChannelInfo();
}
