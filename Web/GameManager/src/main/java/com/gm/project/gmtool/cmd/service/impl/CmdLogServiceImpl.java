package com.gm.project.gmtool.cmd.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.cmd.mapper.CmdLogMapper;
import com.gm.project.gmtool.cmd.domain.CmdLog;
import com.gm.project.gmtool.cmd.service.ICmdLogService;
import com.gm.common.utils.text.Convert;

/**
 * 热更服务器操作日志Service业务层处理
 * 
 * @author gm
 * @date 2021-07-30
 */
@Service
public class CmdLogServiceImpl implements ICmdLogService 
{
    @Autowired
    private CmdLogMapper cmdLogMapper;

    /**
     * 查询热更服务器操作日志
     * 
     * @param id 热更服务器操作日志ID
     * @return 热更服务器操作日志
     */
    @Override
    public CmdLog selectCmdLogById(Long id)
    {
        return cmdLogMapper.selectCmdLogById(id);
    }

    /**
     * 查询热更服务器操作日志列表
     * 
     * @param cmdLog 热更服务器操作日志
     * @return 热更服务器操作日志
     */
    @Override
    public List<CmdLog> selectCmdLogList(CmdLog cmdLog)
    {
        return cmdLogMapper.selectCmdLogList(cmdLog);
    }

    /**
     * 新增热更服务器操作日志
     * 
     * @param cmdLog 热更服务器操作日志
     * @return 结果
     */
    @Override
    public int insertCmdLog(CmdLog cmdLog)
    {
        return cmdLogMapper.insertCmdLog(cmdLog);
    }

    /**
     * 修改热更服务器操作日志
     * 
     * @param cmdLog 热更服务器操作日志
     * @return 结果
     */
    @Override
    public int updateCmdLog(CmdLog cmdLog)
    {
        return cmdLogMapper.updateCmdLog(cmdLog);
    }

    /**
     * 删除热更服务器操作日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCmdLogByIds(String ids)
    {
        return cmdLogMapper.deleteCmdLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除热更服务器操作日志信息
     * 
     * @param id 热更服务器操作日志ID
     * @return 结果
     */
    @Override
    public int deleteCmdLogById(Long id)
    {
        return cmdLogMapper.deleteCmdLogById(id);
    }
}
