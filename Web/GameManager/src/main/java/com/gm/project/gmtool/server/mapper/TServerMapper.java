package com.gm.project.gmtool.server.mapper;

import java.util.List;
import java.util.Map;

import com.gm.project.gmtool.server.domain.TServer;
import org.apache.ibatis.annotations.Param;

/**
 * Сервер列Mapper接口
 * 
 * @author gm
 * @date 2021-07-14
 */
public interface TServerMapper 
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
     * УдалитьСервер列
     * 
     * @param id Сервер列ID
     * @return Результат
     */
    public int deleteTServerById(Long id);

    /**
     * 批量УдалитьСервер列
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteTServerByIds(String[] ids);

    /**
     * 根据页面输入的值查询СерверИнформация
     * @param tServer
     * @return
     */
    public List<TServer> selectTServerByInput(TServer tServer);

    public TServer selectTServerByServerId(int serverId);

    //----------------- Сервер分组 -----------------
    public List<String> selectServerGroup();

    public List<TServer> selectServerList(@Param("groupName") String groupName, @Param("serverTypeList") String serverTypeList);

    public List<TServer> selectServerByServerIds(String[] serverIds);

}
