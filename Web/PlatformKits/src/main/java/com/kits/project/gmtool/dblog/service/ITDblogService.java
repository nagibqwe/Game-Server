package com.kits.project.gmtool.dblog.service;

import java.util.List;

import com.kits.framework.aspectj.lang.annotation.DataSource;
import com.kits.framework.aspectj.lang.enums.DataSourceType;
import com.kits.project.gmtool.dblog.domain.TDblog;

/**
 * 日志库列Service接口
 * 
 * @author gm
 * @date 2021-04-23
 */
public interface ITDblogService 
{
    /**
     * 查询日志库列
     * 
     * @param id 日志库列ID
     * @return 日志库列
     */
    public TDblog selectTDblogById(Long id);

    /**
     * 查询日志库列列表
     * 
     * @param tDblog 日志库列
     * @return 日志库列集合
     */

    public List<TDblog> selectTDblogList(TDblog tDblog);

    /**
     * 新增日志库列
     * 
     * @param tDblog 日志库列
     * @return 结果
     */
    public int insertTDblog(TDblog tDblog);

    /**
     * 修改日志库列
     * 
     * @param tDblog 日志库列
     * @return 结果
     */
    public int updateTDblog(TDblog tDblog);

    /**
     * 批量删除日志库列
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTDblogByIds(String ids);

    /**
     * 删除日志库列信息
     * 
     * @param id 日志库列ID
     * @return 结果
     */
    public int deleteTDblogById(Long id);
}
