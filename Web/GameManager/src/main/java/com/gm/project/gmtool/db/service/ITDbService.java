package com.gm.project.gmtool.db.service;

import java.util.List;
import com.gm.project.gmtool.db.domain.TDb;

/**
 * Журнал库列Service接口
 * 
 * @author gm
 * @date 2021-09-08
 */
public interface ITDbService 
{
    /**
     * 查询Журнал库列
     * 
     * @param id Журнал库列ID
     * @return Журнал库列
     */
    public TDb selectTDbById(Integer id);

    /**
     * 查询Журнал库列
     *
     * @param serverId ID сервера
     * @return Журнал库列
     */
    public TDb selectTDbByServerId(Integer serverId);

    /**
     * 查询Журнал库列列表
     * 
     * @param tDb Журнал库列
     * @return Журнал库列集合
     */
    public List<TDb> selectTDbList(TDb tDb);

    /**
     * ДобавитьЖурнал库列
     * 
     * @param tDb Журнал库列
     * @return Результат
     */
    public int insertTDb(TDb tDb);

    /**
     * ИзменитьЖурнал库列
     * 
     * @param tDb Журнал库列
     * @return Результат
     */
    public int updateTDb(TDb tDb);

    /**
     * 批量УдалитьЖурнал库列
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteTDbByIds(String ids);

    /**
     * УдалитьЖурнал库列Информация
     * 
     * @param id Журнал库列ID
     * @return Результат
     */
    public int deleteTDbById(Integer id);
}
