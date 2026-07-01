package com.kits.project.serverListConfig.x8server.service;

import java.util.List;
import com.kits.project.serverListConfig.x8server.domain.TX8GameServer;

/**
 * 游戏区服Service接口
 * 
 * @author gm
 * @date 2021-07-08
 */
public interface ITX8GameServerService 
{
    /**
     * 查询游戏区服
     * 
     * @param svrId 游戏区服ID
     * @return 游戏区服
     */
    public TX8GameServer selectTX8GameServerById(Long svrId);

    /**
     * 查询游戏区服列表
     * 
     * @param tX8GameServer 游戏区服
     * @return 游戏区服集合
     */
    public List<TX8GameServer> selectTX8GameServerList(TX8GameServer tX8GameServer);

    /**
     * 新增游戏区服
     * 
     * @param tX8GameServer 游戏区服
     * @return 结果
     */
    public int insertTX8GameServer(TX8GameServer tX8GameServer);

    /**
     * 修改游戏区服
     * 
     * @param tX8GameServer 游戏区服
     * @return 结果
     */
    public int updateTX8GameServer(TX8GameServer tX8GameServer);

    /**
     * 批量删除游戏区服
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTX8GameServerByIds(String ids);

    /**
     * 删除游戏区服信息
     * 
     * @param svrId 游戏区服ID
     * @return 结果
     */
    public int deleteTX8GameServerById(Long svrId);

    /**
     * 查询x8服务器信息
     * @return
     */
    public List<TX8GameServer> selectServerInfo();

}
