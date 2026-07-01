package com.kits.project.serverListConfig.x8serverList.service.impl;

import java.util.List;
import com.kits.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.x8serverList.mapper.TX8ServerListMapper;
import com.kits.project.serverListConfig.x8serverList.domain.TX8ServerList;
import com.kits.project.serverListConfig.x8serverList.service.ITX8ServerListService;
import com.kits.common.utils.text.Convert;

/**
 * 渠道区服策略列Service业务层处理
 * 
 * @author gm
 * @date 2021-07-08
 */
@Service
public class TX8ServerListServiceImpl implements ITX8ServerListService 
{
    @Autowired
    private TX8ServerListMapper tX8ServerListMapper;

    /**
     * 查询渠道区服策略列
     * 
     * @param policyId 渠道区服策略列ID
     * @return 渠道区服策略列
     */
    @Override
    public TX8ServerList selectTX8ServerListById(Long policyId)
    {
        return tX8ServerListMapper.selectTX8ServerListById(policyId);
    }

    /**
     * 查询渠道区服策略列列表
     * 
     * @param tX8ServerList 渠道区服策略列
     * @return 渠道区服策略列
     */
    @Override
    public List<TX8ServerList> selectTX8ServerListList(TX8ServerList tX8ServerList)
    {
        return tX8ServerListMapper.selectTX8ServerListList(tX8ServerList);
    }

    /**
     * 新增渠道区服策略列
     * 
     * @param tX8ServerList 渠道区服策略列
     * @return 结果
     */
    @Override
    public int insertTX8ServerList(TX8ServerList tX8ServerList)
    {
        tX8ServerList.setCreateTime(DateUtils.getNowDate());
        return tX8ServerListMapper.insertTX8ServerList(tX8ServerList);
    }

    /**
     * 修改渠道区服策略列
     * 
     * @param tX8ServerList 渠道区服策略列
     * @return 结果
     */
    @Override
    public int updateTX8ServerList(TX8ServerList tX8ServerList)
    {
        tX8ServerList.setUpdateTime(DateUtils.getNowDate());
        return tX8ServerListMapper.updateTX8ServerList(tX8ServerList);
    }

    /**
     * 删除渠道区服策略列对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTX8ServerListByIds(String ids)
    {
        return tX8ServerListMapper.deleteTX8ServerListByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除渠道区服策略列信息
     * 
     * @param policyId 渠道区服策略列ID
     * @return 结果
     */
    @Override
    public int deleteTX8ServerListById(Long policyId)
    {
        return tX8ServerListMapper.deleteTX8ServerListById(policyId);
    }
}
