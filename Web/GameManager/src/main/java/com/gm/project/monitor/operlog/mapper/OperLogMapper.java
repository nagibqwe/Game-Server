package com.gm.project.monitor.operlog.mapper;

import java.util.List;
import com.gm.project.monitor.operlog.domain.OperLog;

/**
 * Журнал операций Данные层
 * 
 * @author ruoyi
 */
public interface OperLogMapper
{
    /**
     * ДобавитьЖурнал операций
     * 
     * @param operLog Журнал операций对象
     */
    public void insertOperlog(OperLog operLog);

    /**
     * 查询系统Журнал операций集合
     * 
     * @param operLog Журнал операций对象
     * @return Журнал операций集合
     */
    public List<OperLog> selectOperLogList(OperLog operLog);
    
    /**
     * 批量Удалить系统Журнал операций
     * 
     * @param ids 需要Удалить的Данные
     * @return Результат
     */
    public int deleteOperLogByIds(String[] ids);
    
    /**
     * 查询Журнал операций详细
     * 
     * @param operId ДействияID
     * @return Журнал операций对象
     */
    public OperLog selectOperLogById(Long operId);
    
    /**
     * 清空Журнал операций
     */
    public void cleanOperLog();
}
