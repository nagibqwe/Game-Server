package com.kits.project.serverListConfig.serverUpdate.service;

import java.util.List;
import com.kits.project.serverListConfig.serverUpdate.domain.SdkServerUpdate;

/**
 * 服务器信息修改时间记录Service接口
 * 
 * @author gm
 * @date 2021-12-08
 */
public interface ISdkServerUpdateService 
{
    /**
     * 查询服务器信息修改时间记录
     * 
     * @param updateTime 服务器信息修改时间记录ID
     * @return 服务器信息修改时间记录
     */
    public SdkServerUpdate selectSdkServerUpdateById(Long updateTime);

    /**
     * 查询服务器信息修改时间记录列表
     * 
     * @param sdkServerUpdate 服务器信息修改时间记录
     * @return 服务器信息修改时间记录集合
     */
    public List<SdkServerUpdate> selectSdkServerUpdateList(SdkServerUpdate sdkServerUpdate);

    /**
     * 新增服务器信息修改时间记录
     * 
     * @param sdkServerUpdate 服务器信息修改时间记录
     * @return 结果
     */
    public int insertSdkServerUpdate(SdkServerUpdate sdkServerUpdate);

    /**
     * 修改服务器信息修改时间记录
     * 
     * @param sdkServerUpdate 服务器信息修改时间记录
     * @return 结果
     */
    public int updateSdkServerUpdate(SdkServerUpdate sdkServerUpdate);

    /**
     * 批量删除服务器信息修改时间记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdkServerUpdateByIds(String ids);

    /**
     * 删除服务器信息修改时间记录信息
     * 
     * @param updateTime 服务器信息修改时间记录ID
     * @return 结果
     */
    public int deleteSdkServerUpdateById(Long updateTime);
}
