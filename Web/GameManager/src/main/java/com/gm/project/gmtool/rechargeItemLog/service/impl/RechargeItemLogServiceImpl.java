package com.gm.project.gmtool.rechargeItemLog.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.rechargeItemLog.mapper.RechargeItemLogMapper;
import com.gm.project.gmtool.rechargeItemLog.domain.RechargeItemLog;
import com.gm.project.gmtool.rechargeItemLog.service.IRechargeItemLogService;
import com.gm.common.utils.text.Convert;

/**
 * Журнал настроек пополненияService业务层处理
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
     * 查询Журнал настроек пополнения
     * 
     * @param id Журнал настроек пополненияID
     * @return Журнал настроек пополнения
     */
    @Override
    public RechargeItemLog selectRechargeItemLogById(Integer id)
    {
        return rechargeItemLogMapper.selectRechargeItemLogById(id);
    }

    /**
     * 查询Журнал настроек пополнения列表
     * 
     * @param rechargeItemLog Журнал настроек пополнения
     * @return Журнал настроек пополнения
     */
    @Override
    public List<RechargeItemLog> selectRechargeItemLogList(RechargeItemLog rechargeItemLog)
    {
        return rechargeItemLogMapper.selectRechargeItemLogList(rechargeItemLog);
    }

    /**
     * ДобавитьЖурнал настроек пополнения
     * 
     * @param rechargeItemLog Журнал настроек пополнения
     * @return Результат
     */
    @Override
    public int insertRechargeItemLog(RechargeItemLog rechargeItemLog)
    {
        return rechargeItemLogMapper.insertRechargeItemLog(rechargeItemLog);
    }

    /**
     * ИзменитьЖурнал настроек пополнения
     * 
     * @param rechargeItemLog Журнал настроек пополнения
     * @return Результат
     */
    @Override
    public int updateRechargeItemLog(RechargeItemLog rechargeItemLog)
    {
        return rechargeItemLogMapper.updateRechargeItemLog(rechargeItemLog);
    }

    /**
     * УдалитьЖурнал настроек пополнения对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteRechargeItemLogByIds(String ids)
    {
        return rechargeItemLogMapper.deleteRechargeItemLogByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьЖурнал настроек пополненияИнформация
     * 
     * @param id Журнал настроек пополненияID
     * @return Результат
     */
    @Override
    public int deleteRechargeItemLogById(Integer id)
    {
        return rechargeItemLogMapper.deleteRechargeItemLogById(id);
    }

}
