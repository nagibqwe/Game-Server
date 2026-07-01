package com.kits.project.serverListConfig.x8serverExtra.service.impl;

import java.util.List;
import com.kits.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.x8serverExtra.mapper.TX8ServerPolicyMapper;
import com.kits.project.serverListConfig.x8serverExtra.domain.TX8ServerPolicy;
import com.kits.project.serverListConfig.x8serverExtra.service.ITX8ServerPolicyService;
import com.kits.common.utils.text.Convert;

/**
 * 渠道区服明细Service业务层处理
 * 
 * @author gm
 * @date 2021-07-08
 */
@Service
public class TX8ServerPolicyServiceImpl implements ITX8ServerPolicyService 
{
    @Autowired
    private TX8ServerPolicyMapper tX8ServerPolicyMapper;

    /**
     * 查询渠道区服明细
     * 
     * @param policyId 渠道区服明细ID
     * @return 渠道区服明细
     */
    @Override
    public TX8ServerPolicy selectTX8ServerPolicyById(Long policyId)
    {
        return tX8ServerPolicyMapper.selectTX8ServerPolicyById(policyId);
    }

    /**
     * 查询渠道区服明细列表
     * 
     * @param tX8ServerPolicy 渠道区服明细
     * @return 渠道区服明细
     */
    @Override
    public List<TX8ServerPolicy> selectTX8ServerPolicyList(TX8ServerPolicy tX8ServerPolicy)
    {
        return tX8ServerPolicyMapper.selectTX8ServerPolicyList(tX8ServerPolicy);
    }

    /**
     * 新增渠道区服明细
     * 
     * @param tX8ServerPolicy 渠道区服明细
     * @return 结果
     */
    @Override
    public int insertTX8ServerPolicy(TX8ServerPolicy tX8ServerPolicy)
    {
        tX8ServerPolicy.setCreateTime(DateUtils.getNowDate());
        return tX8ServerPolicyMapper.insertTX8ServerPolicy(tX8ServerPolicy);
    }

    /**
     * 修改渠道区服明细
     * 
     * @param tX8ServerPolicy 渠道区服明细
     * @return 结果
     */
    @Override
    public int updateTX8ServerPolicy(TX8ServerPolicy tX8ServerPolicy)
    {
        tX8ServerPolicy.setUpdateTime(DateUtils.getNowDate());
        return tX8ServerPolicyMapper.updateTX8ServerPolicy(tX8ServerPolicy);
    }

    /**
     * 删除渠道区服明细对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTX8ServerPolicyByIds(String ids)
    {
        return tX8ServerPolicyMapper.deleteTX8ServerPolicyByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除渠道区服明细信息
     * 
     * @param policyId 渠道区服明细ID
     * @return 结果
     */
    @Override
    public int deleteTX8ServerPolicyById(Long policyId)
    {
        return tX8ServerPolicyMapper.deleteTX8ServerPolicyById(policyId);
    }
}
