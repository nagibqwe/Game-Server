package com.kits.project.serverListConfig.apiserver.service;

import java.util.List;
import com.kits.project.serverListConfig.apiserver.domain.SdkApiserver;

/**
 * APIServer地址管理Service接口
 * 
 * @author gm
 * @date 2021-06-24
 */
public interface ISdkApiserverService 
{
    /**
     * 查询APIServer地址管理
     * 
     * @param id APIServer地址管理ID
     * @return APIServer地址管理
     */
    public SdkApiserver selectSdkApiserverById(Long id);

    /**
     * 查询APIServer地址管理列表
     * 
     * @param sdkApiserver APIServer地址管理
     * @return APIServer地址管理集合
     */
    public List<SdkApiserver> selectSdkApiserverList(SdkApiserver sdkApiserver);

    /**
     * 新增APIServer地址管理
     * 
     * @param sdkApiserver APIServer地址管理
     * @return 结果
     */
    public int insertSdkApiserver(SdkApiserver sdkApiserver);

    /**
     * 修改APIServer地址管理
     * 
     * @param sdkApiserver APIServer地址管理
     * @return 结果
     */
    public int updateSdkApiserver(SdkApiserver sdkApiserver);

    /**
     * 批量删除APIServer地址管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdkApiserverByIds(String ids);

    /**
     * 删除APIServer地址管理信息
     * 
     * @param id APIServer地址管理ID
     * @return 结果
     */
    public int deleteSdkApiserverById(Long id);
}
