package com.gm.project.gmtool.cmd.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.cmd.mapper.CmdLogMapper;
import com.gm.project.gmtool.cmd.domain.CmdLog;
import com.gm.project.gmtool.cmd.service.ICmdLogService;
import com.gm.common.utils.text.Convert;

/**
 * Журнал операций горячего обновленияService业务层处理
 * 
 * @author gm
 * @date 2021-07-30
 */
@Service
public class CmdLogServiceImpl implements ICmdLogService 
{
    @Autowired
    private CmdLogMapper cmdLogMapper;

    /**
     * 查询Журнал операций горячего обновления
     * 
     * @param id Журнал операций горячего обновленияID
     * @return Журнал операций горячего обновления
     */
    @Override
    public CmdLog selectCmdLogById(Long id)
    {
        return cmdLogMapper.selectCmdLogById(id);
    }

    /**
     * 查询Журнал операций горячего обновления列表
     * 
     * @param cmdLog Журнал операций горячего обновления
     * @return Журнал операций горячего обновления
     */
    @Override
    public List<CmdLog> selectCmdLogList(CmdLog cmdLog)
    {
        return cmdLogMapper.selectCmdLogList(cmdLog);
    }

    /**
     * ДобавитьЖурнал операций горячего обновления
     * 
     * @param cmdLog Журнал операций горячего обновления
     * @return Результат
     */
    @Override
    public int insertCmdLog(CmdLog cmdLog)
    {
        return cmdLogMapper.insertCmdLog(cmdLog);
    }

    /**
     * ИзменитьЖурнал операций горячего обновления
     * 
     * @param cmdLog Журнал операций горячего обновления
     * @return Результат
     */
    @Override
    public int updateCmdLog(CmdLog cmdLog)
    {
        return cmdLogMapper.updateCmdLog(cmdLog);
    }

    /**
     * УдалитьЖурнал операций горячего обновления对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteCmdLogByIds(String ids)
    {
        return cmdLogMapper.deleteCmdLogByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьЖурнал операций горячего обновленияИнформация
     * 
     * @param id Журнал операций горячего обновленияID
     * @return Результат
     */
    @Override
    public int deleteCmdLogById(Long id)
    {
        return cmdLogMapper.deleteCmdLogById(id);
    }
}
