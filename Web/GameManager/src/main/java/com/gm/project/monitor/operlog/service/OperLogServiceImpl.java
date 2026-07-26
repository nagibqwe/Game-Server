package com.gm.project.monitor.operlog.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.common.utils.text.Convert;
import com.gm.project.monitor.operlog.domain.OperLog;
import com.gm.project.monitor.operlog.mapper.OperLogMapper;

/**
 * Журнал операций 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class OperLogServiceImpl implements IOperLogService
{
    @Autowired
    private OperLogMapper operLogMapper;

    /**
     * ДобавитьЖурнал операций
     * 
     * @param operLog Журнал операций对象
     */
    @Override
    public void insertOperlog(OperLog operLog)
    {
        operLogMapper.insertOperlog(operLog);
    }

    /**
     * 查询系统Журнал операций集合
     * 
     * @param operLog Журнал операций对象
     * @return Журнал операций集合
     */
    @Override
    public List<OperLog> selectOperLogList(OperLog operLog)
    {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 批量Удалить系统Журнал операций
     * 
     * @param ids 需要Удалить的Данные
     * @return
     */
    @Override
    public int deleteOperLogByIds(String ids)
    {
        return operLogMapper.deleteOperLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 查询Журнал операций详细
     * 
     * @param operId ДействияID
     * @return Журнал операций对象
     */
    @Override
    public OperLog selectOperLogById(Long operId)
    {
        return operLogMapper.selectOperLogById(operId);
    }
    
    /**
     * 清空Журнал операций
     */
    @Override
    public void cleanOperLog()
    {
        operLogMapper.cleanOperLog();
    }
}
