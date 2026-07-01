package com.gm.project.gmtool.recharge.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.recharge.mapper.RechargeMapper;
import com.gm.project.gmtool.recharge.domain.Recharge;
import com.gm.project.gmtool.recharge.service.IRechargeService;
import com.gm.common.utils.text.Convert;

/**
 * 后台模拟充值Service业务层处理
 * 
 * @author gm
 * @date 2021-11-28
 */
@Service
public class RechargeServiceImpl implements IRechargeService 
{
    @Autowired
    private RechargeMapper rechargeMapper;

    /**
     * 查询后台模拟充值
     * 
     * @param id 后台模拟充值ID
     * @return 后台模拟充值
     */
    @Override
    public Recharge selectRechargeById(Long id)
    {
        return rechargeMapper.selectRechargeById(id);
    }

    /**
     * 查询后台模拟充值列表
     * 
     * @param recharge 后台模拟充值
     * @return 后台模拟充值
     */
    @Override
    public List<Recharge> selectRechargeList(Recharge recharge)
    {
        return rechargeMapper.selectRechargeList(recharge);
    }

    /**
     * 新增后台模拟充值
     * 
     * @param recharge 后台模拟充值
     * @return 结果
     */
    @Override
    public int insertRecharge(Recharge recharge)
    {
        return rechargeMapper.insertRecharge(recharge);
    }

    /**
     * 修改后台模拟充值
     * 
     * @param recharge 后台模拟充值
     * @return 结果
     */
    @Override
    public int updateRecharge(Recharge recharge)
    {
        return rechargeMapper.updateRecharge(recharge);
    }

    /**
     * 删除后台模拟充值对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteRechargeByIds(String ids)
    {
        return rechargeMapper.deleteRechargeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除后台模拟充值信息
     * 
     * @param id 后台模拟充值ID
     * @return 结果
     */
    @Override
    public int deleteRechargeById(Long id)
    {
        return rechargeMapper.deleteRechargeById(id);
    }
}
