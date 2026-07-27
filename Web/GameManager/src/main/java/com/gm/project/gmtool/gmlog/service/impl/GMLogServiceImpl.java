package com.gm.project.gmtool.gmlog.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.gmlog.mapper.GMLogMapper;
import com.gm.project.gmtool.gmlog.domain.GMLog;
import com.gm.project.gmtool.gmlog.service.IGMLogService;
import com.gm.common.utils.text.Convert;

/**
 * GM后台日志记录Service业务层处理
 * 
 * @author gm
 * @date 2021-09-01
 */
@Service
public class GMLogServiceImpl implements IGMLogService 
{
    @Autowired
    private GMLogMapper gMLogMapper;

    /**
     * 查询GM后台日志记录
     * 
     * @param id GM后台日志记录ID
     * @return GM后台日志记录
     */
    @Override
    public GMLog selectGMLogById(Long id)
    {
        return gMLogMapper.selectGMLogById(id);
    }

    /**
     * 查询GM后台日志记录列表
     * 
     * @param gMLog GM后台日志记录
     * @return GM后台日志记录
     */
    @Override
    public List<GMLog> selectGMLogList(GMLog gMLog)
    {
        return gMLogMapper.selectGMLogList(gMLog);
    }

    /**
     * 新增GM后台日志记录
     * 
     * @param gMLog GM后台日志记录
     * @return 结果
     */
    @Override
    public int insertGMLog(GMLog gMLog)
    {
        return gMLogMapper.insertGMLog(gMLog);
    }

    /**
     * 修改GM后台日志记录
     * 
     * @param gMLog GM后台日志记录
     * @return 结果
     */
    @Override
    public int updateGMLog(GMLog gMLog)
    {
        return gMLogMapper.updateGMLog(gMLog);
    }

    /**
     * 删除GM后台日志记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteGMLogByIds(String ids)
    {
        return gMLogMapper.deleteGMLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除GM后台日志记录信息
     * 
     * @param id GM后台日志记录ID
     * @return 结果
     */
    @Override
    public int deleteGMLogById(Long id)
    {
        return gMLogMapper.deleteGMLogById(id);
    }
}
