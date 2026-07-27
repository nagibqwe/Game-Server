package com.gm.project.gmtool.server.service.impl;

import java.util.List;

import com.gm.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.server.mapper.TServerMapper;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.common.utils.text.Convert;

/**
 * 服务器列Service业务层处理
 * 
 * @author gm
 * @date 2021-07-14
 */
@Service
public class TServerServiceImpl implements ITServerService 
{
    @Autowired
    private TServerMapper tServerMapper;

    /**
     * 查询服务器列
     * 
     * @param id 服务器列ID
     * @return 服务器列
     */
    @Override
    public TServer selectTServerById(Long id)
    {
        return tServerMapper.selectTServerById(id);
    }

    /**
     * 查询服务器列列表(页面展示用)
     * @param tServer
     * @return
     */
    @Override
    public List<TServer> selectTServerListShow(TServer tServer) {
        return tServerMapper.selectTServerListShow(tServer);
    }

    /**
     * 查询服务器列列表
     * 
     * @param tServer 服务器列
     * @return 服务器列
     */
    @Override
    public List<TServer> selectTServerList(TServer tServer)
    {
        return tServerMapper.selectTServerList(tServer);
    }

    /**
     * 新增服务器列
     * 
     * @param tServer 服务器列
     * @return 结果
     */
    @Override
    public int insertTServer(TServer tServer)
    {
        return tServerMapper.insertTServer(tServer);
    }

    /**
     * 修改服务器列
     * 
     * @param tServer 服务器列
     * @return 结果
     */
    @Override
    public int updateTServer(TServer tServer)
    {
        return tServerMapper.updateTServer(tServer);
    }

    /**
     * 删除服务器列对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTServerByIds(String ids)
    {
        return tServerMapper.deleteTServerByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除服务器列信息
     * 
     * @param id 服务器列ID
     * @return 结果
     */
    @Override
    public int deleteTServerById(Long id)
    {
        return tServerMapper.deleteTServerById(id);
    }

    @Override
    public List<TServer> selectTServerByInput(TServer tServer) {
        return tServerMapper.selectTServerByInput(tServer);
    }

    @Override
    public TServer selectTServerByServerId(int serverId) {
        return tServerMapper.selectTServerByServerId(serverId);
    }

    @Override
    public TServer selectTServerByServerId(String serverId) {
        if(StringUtils.isEmpty(serverId)){
            return null;
        }
        return tServerMapper.selectTServerByServerId(Integer.parseInt(serverId));
    }

    @Override
    public List<TServer> selectServerByServerIds(String serverIds) {
        return tServerMapper.selectServerByServerIds(Convert.toStrArray(serverIds));
    }
}
