package com.kits.project.serverListConfig.whiteList.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.kits.project.serverListConfig.whiteList.domain.SdkWhite;

/**
 * 白名单Service接口
 * 
 * @author gm
 * @date 2021-04-26
 */
public interface ISdkWhiteService 
{
    /**
     * 获取白名单信息map
     *
     * @return
     */
    public ConcurrentHashMap<Long, SdkWhite> getWhiteHashMap();

    /**
     * 查询白名单
     * 
     * @param id 白名单ID
     * @return 白名单
     */
    public SdkWhite selectSdkWhiteById(Long id);

    /**
     * 查询白名单列表
     * 
     * @param sdkWhite 白名单
     * @return 白名单集合
     */
    public List<SdkWhite> selectSdkWhiteList(SdkWhite sdkWhite);

    /**
     * 新增白名单
     * 
     * @param sdkWhite 白名单
     * @return 结果
     */
    public int insertSdkWhite(SdkWhite sdkWhite);

    /**
     * 修改白名单
     * 
     * @param sdkWhite 白名单
     * @return 结果
     */
    public int updateSdkWhite(SdkWhite sdkWhite);

    /**
     * 批量删除白名单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdkWhiteByIds(String ids);

    /**
     * 删除白名单信息
     * 
     * @param id 白名单ID
     * @return 结果
     */
    public int deleteSdkWhiteById(Long id);

    public void reloadSdkWhite();
}
