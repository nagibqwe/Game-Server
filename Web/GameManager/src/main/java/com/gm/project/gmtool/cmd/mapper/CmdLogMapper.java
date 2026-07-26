package com.gm.project.gmtool.cmd.mapper;

import java.util.List;
import com.gm.project.gmtool.cmd.domain.CmdLog;

/**
 * Журнал операций горячего обновленияMapper接口
 * 
 * @author gm
 * @date 2021-07-30
 */
public interface CmdLogMapper 
{
    /**
     * 查询Журнал операций горячего обновления
     * 
     * @param id Журнал операций горячего обновленияID
     * @return Журнал операций горячего обновления
     */
    public CmdLog selectCmdLogById(Long id);

    /**
     * 查询Журнал операций горячего обновления列表
     * 
     * @param cmdLog Журнал операций горячего обновления
     * @return Журнал операций горячего обновления集合
     */
    public List<CmdLog> selectCmdLogList(CmdLog cmdLog);

    /**
     * ДобавитьЖурнал операций горячего обновления
     * 
     * @param cmdLog Журнал операций горячего обновления
     * @return Результат
     */
    public int insertCmdLog(CmdLog cmdLog);

    /**
     * ИзменитьЖурнал операций горячего обновления
     * 
     * @param cmdLog Журнал операций горячего обновления
     * @return Результат
     */
    public int updateCmdLog(CmdLog cmdLog);

    /**
     * УдалитьЖурнал операций горячего обновления
     * 
     * @param id Журнал операций горячего обновленияID
     * @return Результат
     */
    public int deleteCmdLogById(Long id);

    /**
     * 批量УдалитьЖурнал операций горячего обновления
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteCmdLogByIds(String[] ids);
}
