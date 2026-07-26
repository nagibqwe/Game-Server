package com.gm.project.gmtool.gmlog.mapper;

import java.util.List;
import com.gm.project.gmtool.gmlog.domain.GMLog;

/**
 * Журнал GM-панелиMapper接口
 * 
 * @author gm
 * @date 2021-09-01
 */
public interface GMLogMapper 
{
    /**
     * 查询Журнал GM-панели
     * 
     * @param id Журнал GM-панелиID
     * @return Журнал GM-панели
     */
    public GMLog selectGMLogById(Long id);

    /**
     * 查询Журнал GM-панели列表
     * 
     * @param gMLog Журнал GM-панели
     * @return Журнал GM-панели集合
     */
    public List<GMLog> selectGMLogList(GMLog gMLog);

    /**
     * ДобавитьЖурнал GM-панели
     * 
     * @param gMLog Журнал GM-панели
     * @return Результат
     */
    public int insertGMLog(GMLog gMLog);

    /**
     * ИзменитьЖурнал GM-панели
     * 
     * @param gMLog Журнал GM-панели
     * @return Результат
     */
    public int updateGMLog(GMLog gMLog);

    /**
     * УдалитьЖурнал GM-панели
     * 
     * @param id Журнал GM-панелиID
     * @return Результат
     */
    public int deleteGMLogById(Long id);

    /**
     * 批量УдалитьЖурнал GM-панели
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteGMLogByIds(String[] ids);
}
