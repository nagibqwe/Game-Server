package com.gm.project.gmtool.rechargeItemLog.service;

import java.util.List;
import com.gm.project.gmtool.rechargeItemLog.domain.RechargeItemLog;

/**
 * Журнал настроек пополненияService接口
 * 
 * @author gm
 * @date 2021-08-25
 */
public interface IRechargeItemLogService 
{
    /**
     * 查询Журнал настроек пополнения
     * 
     * @param id Журнал настроек пополненияID
     * @return Журнал настроек пополнения
     */
    public RechargeItemLog selectRechargeItemLogById(Integer id);

    /**
     * 查询Журнал настроек пополнения列表
     * 
     * @param rechargeItemLog Журнал настроек пополнения
     * @return Журнал настроек пополнения集合
     */
    public List<RechargeItemLog> selectRechargeItemLogList(RechargeItemLog rechargeItemLog);

    /**
     * ДобавитьЖурнал настроек пополнения
     * 
     * @param rechargeItemLog Журнал настроек пополнения
     * @return Результат
     */
    public int insertRechargeItemLog(RechargeItemLog rechargeItemLog);

    /**
     * ИзменитьЖурнал настроек пополнения
     * 
     * @param rechargeItemLog Журнал настроек пополнения
     * @return Результат
     */
    public int updateRechargeItemLog(RechargeItemLog rechargeItemLog);

    /**
     * 批量УдалитьЖурнал настроек пополнения
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteRechargeItemLogByIds(String ids);

    /**
     * УдалитьЖурнал настроек пополненияИнформация
     * 
     * @param id Журнал настроек пополненияID
     * @return Результат
     */
    public int deleteRechargeItemLogById(Integer id);

}
