package com.gm.project.gmtool.server.service;

import java.util.List;
import com.gm.project.gmtool.server.domain.TServer;

/**
 * 服务器列Service接口
 * 
 * @author gm
 * @date 2021-07-14
 */
public interface ITServerService 
{
    /**
     * 查询服务器列
     * 
     * @param id 服务器列ID
     * @return 服务器列
     */
    public TServer selectTServerById(Long id);

    /**
     * 查询服务器列列表(页面展示用)
     * @param tServer
     * @return
     */
    public List<TServer> selectTServerListShow(TServer tServer);

    /**
     * 查询服务器列列表
     * 
     * @param tServer 服务器列
     * @return 服务器列集合
     */
    public List<TServer> selectTServerList(TServer tServer);

    /**
     * 新增服务器列
     * 
     * @param tServer 服务器列
     * @return 结果
     */
    public int insertTServer(TServer tServer);

    /**
     * 修改服务器列
     * 
     * @param tServer 服务器列
     * @return 结果
     */
    public int updateTServer(TServer tServer);

    /**
     * 批量删除服务器列
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTServerByIds(String ids);

    /**
     * 删除服务器列信息
     * 
     * @param id 服务器列ID
     * @return 结果
     */
    public int deleteTServerById(Long id);

    /**
     * 根据页面输入的值查询服务器信息
     * @param tServer
     * @return
     */
    public List<TServer> selectTServerByInput(TServer tServer);

    public TServer selectTServerByServerId(int serverId);

    public TServer selectTServerByServerId(String serverId);

    public List<TServer> selectServerByServerIds(String serverIds);
}
