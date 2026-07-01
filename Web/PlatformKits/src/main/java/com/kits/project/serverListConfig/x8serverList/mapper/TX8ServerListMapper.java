package com.kits.project.serverListConfig.x8serverList.mapper;

import java.util.List;
import com.kits.project.serverListConfig.x8serverList.domain.TX8ServerList;

/**
 * 渠道区服策略列Mapper接口
 * 
 * @author gm
 * @date 2021-07-08
 */
public interface TX8ServerListMapper 
{
    /**
     * 查询渠道区服策略列
     * 
     * @param policyId 渠道区服策略列ID
     * @return 渠道区服策略列
     */
    public TX8ServerList selectTX8ServerListById(Long policyId);

    /**
     * 查询渠道区服策略列列表
     * 
     * @param tX8ServerList 渠道区服策略列
     * @return 渠道区服策略列集合
     */
    public List<TX8ServerList> selectTX8ServerListList(TX8ServerList tX8ServerList);

    /**
     * 新增渠道区服策略列
     * 
     * @param tX8ServerList 渠道区服策略列
     * @return 结果
     */
    public int insertTX8ServerList(TX8ServerList tX8ServerList);

    /**
     * 修改渠道区服策略列
     * 
     * @param tX8ServerList 渠道区服策略列
     * @return 结果
     */
    public int updateTX8ServerList(TX8ServerList tX8ServerList);

    /**
     * 删除渠道区服策略列
     * 
     * @param policyId 渠道区服策略列ID
     * @return 结果
     */
    public int deleteTX8ServerListById(Long policyId);

    /**
     * 批量删除渠道区服策略列
     * 
     * @param policyIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteTX8ServerListByIds(String[] policyIds);
}
