package com.gm.project.gmtool.gmlog.service;

import java.util.List;
import com.gm.project.gmtool.gmlog.domain.GMLog;

/**
 * GM后台日志记录Service接口
 * 
 * @author gm
 * @date 2021-09-01
 */
public interface IGMLogService 
{
    /**
     * 查询GM后台日志记录
     * 
     * @param id GM后台日志记录ID
     * @return GM后台日志记录
     */
    public GMLog selectGMLogById(Long id);

    /**
     * 查询GM后台日志记录列表
     * 
     * @param gMLog GM后台日志记录
     * @return GM后台日志记录集合
     */
    public List<GMLog> selectGMLogList(GMLog gMLog);

    /**
     * 新增GM后台日志记录
     * 
     * @param gMLog GM后台日志记录
     * @return 结果
     */
    public int insertGMLog(GMLog gMLog);

    /**
     * 修改GM后台日志记录
     * 
     * @param gMLog GM后台日志记录
     * @return 结果
     */
    public int updateGMLog(GMLog gMLog);

    /**
     * 批量删除GM后台日志记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGMLogByIds(String ids);

    /**
     * 删除GM后台日志记录信息
     * 
     * @param id GM后台日志记录ID
     * @return 结果
     */
    public int deleteGMLogById(Long id);
}
