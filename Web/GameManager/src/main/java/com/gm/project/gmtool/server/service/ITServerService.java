package com.gm.project.gmtool.server.service;

import java.util.List;
import com.gm.project.gmtool.server.domain.TServer;

/**
 * Сервер列Service接口
 * 
 * @author gm
 * @date 2021-07-14
 */
public interface ITServerService 
{
    /**
     * 查询Сервер列
     * 
     * @param id Сервер列ID
     * @return Сервер列
     */
    public TServer selectTServerById(Long id);

    /**
     * 查询Список серверов(页面展示用)
     * @param tServer
     * @return
     */
    public List<TServer> selectTServerListShow(TServer tServer);

    /**
     * 查询Список серверов
     * 
     * @param tServer Сервер列
     * @return Сервер列集合
     */
    public List<TServer> selectTServerList(TServer tServer);

    /**
     * ДобавитьСервер列
     * 
     * @param tServer Сервер列
     * @return Результат
     */
    public int insertTServer(TServer tServer);

    /**
     * ИзменитьСервер列
     * 
     * @param tServer Сервер列
     * @return Результат
     */
    public int updateTServer(TServer tServer);

    /**
     * 批量УдалитьСервер列
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteTServerByIds(String ids);

    /**
     * УдалитьСервер列Информация
     * 
     * @param id Сервер列ID
     * @return Результат
     */
    public int deleteTServerById(Long id);

    /**
     * 根据页面输入的值查询СерверИнформация
     * @param tServer
     * @return
     */
    public List<TServer> selectTServerByInput(TServer tServer);

    public TServer selectTServerByServerId(int serverId);

    public TServer selectTServerByServerId(String serverId);

    public List<TServer> selectServerByServerIds(String serverIds);
}
