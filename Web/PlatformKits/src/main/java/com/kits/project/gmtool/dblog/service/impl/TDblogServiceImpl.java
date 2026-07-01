package com.kits.project.gmtool.dblog.service.impl;

import java.util.List;

import com.kits.framework.aspectj.lang.annotation.DataSource;
import com.kits.framework.aspectj.lang.enums.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.gmtool.dblog.mapper.TDblogMapper;
import com.kits.project.gmtool.dblog.domain.TDblog;
import com.kits.project.gmtool.dblog.service.ITDblogService;
import com.kits.common.utils.text.Convert;

/**
 * 日志库列Service业务层处理
 * 
 * @author gm
 * @date 2021-04-23
 */
@Service
public class TDblogServiceImpl implements ITDblogService 
{
    @Autowired
    private TDblogMapper tDblogMapper;

    /**
     * 查询日志库列
     * 
     * @param id 日志库列ID
     * @return 日志库列
     */
    @Override
    public TDblog selectTDblogById(Long id)
    {
        return tDblogMapper.selectTDblogById(id);
    }

    /**
     * 查询日志库列列表
     * 
     * @param tDblog 日志库列
     * @return 日志库列
     */
    @Override
    public List<TDblog> selectTDblogList(TDblog tDblog)
    {
        return tDblogMapper.selectTDblogList(tDblog);
    }

    /**
     * 新增日志库列
     * 
     * @param tDblog 日志库列
     * @return 结果
     */
    @Override
    public int insertTDblog(TDblog tDblog)
    {
        return tDblogMapper.insertTDblog(tDblog);
    }

    /**
     * 修改日志库列
     * 
     * @param tDblog 日志库列
     * @return 结果
     */
    @Override
    public int updateTDblog(TDblog tDblog)
    {
        return tDblogMapper.updateTDblog(tDblog);
    }

    /**
     * 删除日志库列对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTDblogByIds(String ids)
    {
        return tDblogMapper.deleteTDblogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除日志库列信息
     * 
     * @param id 日志库列ID
     * @return 结果
     */
    @Override
    public int deleteTDblogById(Long id)
    {
        return tDblogMapper.deleteTDblogById(id);
    }
}
