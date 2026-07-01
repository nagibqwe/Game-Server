package com.kits.project.serverListConfig.x8server.service.impl;

import java.util.List;
import com.kits.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.x8server.mapper.TX8GameServerMapper;
import com.kits.project.serverListConfig.x8server.domain.TX8GameServer;
import com.kits.project.serverListConfig.x8server.service.ITX8GameServerService;
import com.kits.common.utils.text.Convert;

/**
 * 游戏区服Service业务层处理
 * 
 * @author gm
 * @date 2021-07-08
 */
@Service
public class TX8GameServerServiceImpl implements ITX8GameServerService 
{
    @Autowired
    private TX8GameServerMapper tX8GameServerMapper;

    /**
     * 查询游戏区服
     * 
     * @param svrId 游戏区服ID
     * @return 游戏区服
     */
    @Override
    public TX8GameServer selectTX8GameServerById(Long svrId)
    {
        return tX8GameServerMapper.selectTX8GameServerById(svrId);
    }

    /**
     * 查询游戏区服列表
     * 
     * @param tX8GameServer 游戏区服
     * @return 游戏区服
     */
    @Override
    public List<TX8GameServer> selectTX8GameServerList(TX8GameServer tX8GameServer)
    {
        return tX8GameServerMapper.selectTX8GameServerList(tX8GameServer);
    }

    /**
     * 新增游戏区服
     * 
     * @param tX8GameServer 游戏区服
     * @return 结果
     */
    @Override
    public int insertTX8GameServer(TX8GameServer tX8GameServer)
    {
        tX8GameServer.setCreateTime(DateUtils.getNowDate());
        return tX8GameServerMapper.insertTX8GameServer(tX8GameServer);
    }

    /**
     * 修改游戏区服
     * 
     * @param tX8GameServer 游戏区服
     * @return 结果
     */
    @Override
    public int updateTX8GameServer(TX8GameServer tX8GameServer)
    {
        tX8GameServer.setUpdateTime(DateUtils.getNowDate());
        return tX8GameServerMapper.updateTX8GameServer(tX8GameServer);
    }

    /**
     * 删除游戏区服对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTX8GameServerByIds(String ids)
    {
        return tX8GameServerMapper.deleteTX8GameServerByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除游戏区服信息
     * 
     * @param svrId 游戏区服ID
     * @return 结果
     */
    @Override
    public int deleteTX8GameServerById(Long svrId)
    {
        return tX8GameServerMapper.deleteTX8GameServerById(svrId);
    }

    /**
     * 查询x8服务器信息
     * @return
     */
    @Override
    public List<TX8GameServer> selectServerInfo() {
        return tX8GameServerMapper.selectServerInfo();
    }
}
