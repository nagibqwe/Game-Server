package com.kits.project.serverListConfig.serverUpdate.service.impl;

import java.util.List;
import com.kits.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.serverUpdate.mapper.SdkServerUpdateMapper;
import com.kits.project.serverListConfig.serverUpdate.domain.SdkServerUpdate;
import com.kits.project.serverListConfig.serverUpdate.service.ISdkServerUpdateService;
import com.kits.common.utils.text.Convert;

/**
 * 服务器信息修改时间记录Service业务层处理
 * 
 * @author gm
 * @date 2021-12-08
 */
@Service
public class SdkServerUpdateServiceImpl implements ISdkServerUpdateService 
{
    @Autowired
    private SdkServerUpdateMapper sdkServerUpdateMapper;

    /**
     * 查询服务器信息修改时间记录
     * 
     * @param updateTime 服务器信息修改时间记录ID
     * @return 服务器信息修改时间记录
     */
    @Override
    public SdkServerUpdate selectSdkServerUpdateById(Long updateTime)
    {
        return sdkServerUpdateMapper.selectSdkServerUpdateById(updateTime);
    }

    /**
     * 查询服务器信息修改时间记录列表
     * 
     * @param sdkServerUpdate 服务器信息修改时间记录
     * @return 服务器信息修改时间记录
     */
    @Override
    public List<SdkServerUpdate> selectSdkServerUpdateList(SdkServerUpdate sdkServerUpdate)
    {
        return sdkServerUpdateMapper.selectSdkServerUpdateList(sdkServerUpdate);
    }

    /**
     * 新增服务器信息修改时间记录
     * 
     * @param sdkServerUpdate 服务器信息修改时间记录
     * @return 结果
     */
    @Override
    public int insertSdkServerUpdate(SdkServerUpdate sdkServerUpdate)
    {
        return sdkServerUpdateMapper.insertSdkServerUpdate(sdkServerUpdate);
    }

    /**
     * 修改服务器信息修改时间记录
     * 
     * @param sdkServerUpdate 服务器信息修改时间记录
     * @return 结果
     */
    @Override
    public int updateSdkServerUpdate(SdkServerUpdate sdkServerUpdate)
    {
//        sdkServerUpdate.setUpdateTime(DateUtils.getNowDate());
        return sdkServerUpdateMapper.updateSdkServerUpdate(sdkServerUpdate);
    }

    /**
     * 删除服务器信息修改时间记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSdkServerUpdateByIds(String ids)
    {
        return sdkServerUpdateMapper.deleteSdkServerUpdateByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除服务器信息修改时间记录信息
     * 
     * @param updateTime 服务器信息修改时间记录ID
     * @return 结果
     */
    @Override
    public int deleteSdkServerUpdateById(Long updateTime)
    {
        return sdkServerUpdateMapper.deleteSdkServerUpdateById(updateTime);
    }
}
