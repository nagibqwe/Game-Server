package com.gm.project.gmtool.server.mapper;

import java.util.List;
import java.util.Map;

import com.gm.project.gmtool.server.domain.TServer;
import org.apache.ibatis.annotations.Param;

/**
 * 服务器列Mapper接口
 * 
 * @author gm
 * @date 2021-07-14
 */
public interface TServerMapper 
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
     * 删除服务器列
     * 
     * @param id 服务器列ID
     * @return 结果
     */
    public int deleteTServerById(Long id);

    /**
     * 批量删除服务器列
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTServerByIds(String[] ids);

    /**
     * 根据页面输入的值查询服务器信息
     * @param tServer
     * @return
     */
    public List<TServer> selectTServerByInput(TServer tServer);

    public TServer selectTServerByServerId(int serverId);

    //----------------- 服务器分组 -----------------
    public List<String> selectServerGroup();

    public List<TServer> selectServerList(@Param("groupName") String groupName, @Param("serverTypeList") String serverTypeList);

    public List<TServer> selectServerByServerIds(String[] serverIds);

}
