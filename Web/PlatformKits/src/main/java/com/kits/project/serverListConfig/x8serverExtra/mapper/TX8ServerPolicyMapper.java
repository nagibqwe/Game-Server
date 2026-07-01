package com.kits.project.serverListConfig.x8serverExtra.mapper;

import java.util.List;
import com.kits.project.serverListConfig.x8serverExtra.domain.TX8ServerPolicy;

/**
 * 渠道区服明细Mapper接口
 * 
 * @author gm
 * @date 2021-07-08
 */
public interface TX8ServerPolicyMapper 
{
    /**
     * 查询渠道区服明细
     * 
     * @param policyId 渠道区服明细ID
     * @return 渠道区服明细
     */
    public TX8ServerPolicy selectTX8ServerPolicyById(Long policyId);

    /**
     * 查询渠道区服明细列表
     * 
     * @param tX8ServerPolicy 渠道区服明细
     * @return 渠道区服明细集合
     */
    public List<TX8ServerPolicy> selectTX8ServerPolicyList(TX8ServerPolicy tX8ServerPolicy);

    /**
     * 新增渠道区服明细
     * 
     * @param tX8ServerPolicy 渠道区服明细
     * @return 结果
     */
    public int insertTX8ServerPolicy(TX8ServerPolicy tX8ServerPolicy);

    /**
     * 修改渠道区服明细
     * 
     * @param tX8ServerPolicy 渠道区服明细
     * @return 结果
     */
    public int updateTX8ServerPolicy(TX8ServerPolicy tX8ServerPolicy);

    /**
     * 删除渠道区服明细
     * 
     * @param policyId 渠道区服明细ID
     * @return 结果
     */
    public int deleteTX8ServerPolicyById(Long policyId);

    /**
     * 批量删除渠道区服明细
     * 
     * @param policyIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteTX8ServerPolicyByIds(String[] policyIds);
}
