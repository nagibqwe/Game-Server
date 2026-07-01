package com.gm.project.gmtool.rechargeItemLog.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.rechargeItemLog.mapper.RechargeItemLogMapper;
import com.gm.project.gmtool.rechargeItemLog.domain.RechargeItemLog;
import com.gm.project.gmtool.rechargeItemLog.service.IRechargeItemLogService;
import com.gm.common.utils.text.Convert;

/**
 * 充值配置日志Service业务层处理
 * 
 * @author gm
 * @date 2021-08-25
 */
@Service
public class RechargeItemLogServiceImpl implements IRechargeItemLogService 
{
    @Autowired
    private RechargeItemLogMapper rechargeItemLogMapper;

    /**
     * 查询充值配置日志
     * 
     * @param id 充值配置日志ID
     * @return 充值配置日志
     */
    @Override
    public RechargeItemLog selectRechargeItemLogById(Integer id)
    {
        return rechargeItemLogMapper.selectRechargeItemLogById(id);
    }

    /**
     * 查询充值配置日志列表
     * 
     * @param rechargeItemLog 充值配置日志
     * @return 充值配置日志
     */
    @Override
    public List<RechargeItemLog> selectRechargeItemLogList(RechargeItemLog rechargeItemLog)
    {
        return rechargeItemLogMapper.selectRechargeItemLogList(rechargeItemLog);
    }

    /**
     * 新增充值配置日志
     * 
     * @param rechargeItemLog 充值配置日志
     * @return 结果
     */
    @Override
    public int insertRechargeItemLog(RechargeItemLog rechargeItemLog)
    {
        return rechargeItemLogMapper.insertRechargeItemLog(rechargeItemLog);
    }

    /**
     * 修改充值配置日志
     * 
     * @param rechargeItemLog 充值配置日志
     * @return 结果
     */
    @Override
    public int updateRechargeItemLog(RechargeItemLog rechargeItemLog)
    {
        return rechargeItemLogMapper.updateRechargeItemLog(rechargeItemLog);
    }

    /**
     * 删除充值配置日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteRechargeItemLogByIds(String ids)
    {
        return rechargeItemLogMapper.deleteRechargeItemLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除充值配置日志信息
     * 
     * @param id 充值配置日志ID
     * @return 结果
     */
    @Override
    public int deleteRechargeItemLogById(Integer id)
    {
        return rechargeItemLogMapper.deleteRechargeItemLogById(id);
    }

}
