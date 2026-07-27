package com.gm.project.gmtool.rechargeItemLog.mapper;

import java.util.List;
import com.gm.project.gmtool.rechargeItemLog.domain.RechargeItemLog;

/**
 * 充值配置日志Mapper接口
 * 
 * @author gm
 * @date 2021-08-25
 */
public interface RechargeItemLogMapper 
{
    /**
     * 查询充值配置日志
     * 
     * @param id 充值配置日志ID
     * @return 充值配置日志
     */
    public RechargeItemLog selectRechargeItemLogById(Integer id);

    /**
     * 查询充值配置日志列表
     * 
     * @param rechargeItemLog 充值配置日志
     * @return 充值配置日志集合
     */
    public List<RechargeItemLog> selectRechargeItemLogList(RechargeItemLog rechargeItemLog);

    /**
     * 新增充值配置日志
     * 
     * @param rechargeItemLog 充值配置日志
     * @return 结果
     */
    public int insertRechargeItemLog(RechargeItemLog rechargeItemLog);

    /**
     * 修改充值配置日志
     * 
     * @param rechargeItemLog 充值配置日志
     * @return 结果
     */
    public int updateRechargeItemLog(RechargeItemLog rechargeItemLog);

    /**
     * 删除充值配置日志
     * 
     * @param id 充值配置日志ID
     * @return 结果
     */
    public int deleteRechargeItemLogById(Integer id);

    /**
     * 批量删除充值配置日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRechargeItemLogByIds(String[] ids);

}
