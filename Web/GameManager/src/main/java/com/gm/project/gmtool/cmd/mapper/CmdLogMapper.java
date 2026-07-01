package com.gm.project.gmtool.cmd.mapper;

import java.util.List;
import com.gm.project.gmtool.cmd.domain.CmdLog;

/**
 * 热更服务器操作日志Mapper接口
 * 
 * @author gm
 * @date 2021-07-30
 */
public interface CmdLogMapper 
{
    /**
     * 查询热更服务器操作日志
     * 
     * @param id 热更服务器操作日志ID
     * @return 热更服务器操作日志
     */
    public CmdLog selectCmdLogById(Long id);

    /**
     * 查询热更服务器操作日志列表
     * 
     * @param cmdLog 热更服务器操作日志
     * @return 热更服务器操作日志集合
     */
    public List<CmdLog> selectCmdLogList(CmdLog cmdLog);

    /**
     * 新增热更服务器操作日志
     * 
     * @param cmdLog 热更服务器操作日志
     * @return 结果
     */
    public int insertCmdLog(CmdLog cmdLog);

    /**
     * 修改热更服务器操作日志
     * 
     * @param cmdLog 热更服务器操作日志
     * @return 结果
     */
    public int updateCmdLog(CmdLog cmdLog);

    /**
     * 删除热更服务器操作日志
     * 
     * @param id 热更服务器操作日志ID
     * @return 结果
     */
    public int deleteCmdLogById(Long id);

    /**
     * 批量删除热更服务器操作日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmdLogByIds(String[] ids);
}
