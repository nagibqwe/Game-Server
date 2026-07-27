package com.gm.project.gmtool.recharge.mapper;

import java.util.List;
import com.gm.project.gmtool.recharge.domain.Recharge;

/**
 * 后台模拟充值Mapper接口
 * 
 * @author gm
 * @date 2021-11-28
 */
public interface RechargeMapper 
{
    /**
     * 查询后台模拟充值
     * 
     * @param id 后台模拟充值ID
     * @return 后台模拟充值
     */
    public Recharge selectRechargeById(Long id);

    /**
     * 查询后台模拟充值列表
     * 
     * @param recharge 后台模拟充值
     * @return 后台模拟充值集合
     */
    public List<Recharge> selectRechargeList(Recharge recharge);

    /**
     * 新增后台模拟充值
     * 
     * @param recharge 后台模拟充值
     * @return 结果
     */
    public int insertRecharge(Recharge recharge);

    /**
     * 修改后台模拟充值
     * 
     * @param recharge 后台模拟充值
     * @return 结果
     */
    public int updateRecharge(Recharge recharge);

    /**
     * 删除后台模拟充值
     * 
     * @param id 后台模拟充值ID
     * @return 结果
     */
    public int deleteRechargeById(Long id);

    /**
     * 批量删除后台模拟充值
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRechargeByIds(String[] ids);
}
