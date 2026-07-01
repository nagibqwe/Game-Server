package com.kits.project.gmtool.server.service;

import java.util.List;
import com.kits.project.gmtool.server.domain.TServer;

/**
 * 服务器列Service接口
 * 
 * @author gm
 * @date 2021-04-28
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
}
