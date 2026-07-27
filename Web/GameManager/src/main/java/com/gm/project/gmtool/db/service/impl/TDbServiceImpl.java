package com.gm.project.gmtool.db.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.db.mapper.TDbMapper;
import com.gm.project.gmtool.db.domain.TDb;
import com.gm.project.gmtool.db.service.ITDbService;
import com.gm.common.utils.text.Convert;

/**
 * 日志库列Service业务层处理
 * 
 * @author gm
 * @date 2021-09-08
 */
@Service
public class TDbServiceImpl implements ITDbService 
{
    @Autowired
    private TDbMapper tDbMapper;

    /**
     * 查询日志库列
     * 
     * @param id 日志库列ID
     * @return 日志库列
     */
    @Override
    public TDb selectTDbById(Integer id)
    {
        return tDbMapper.selectTDbById(id);
    }

    /**
     * 查询日志库列
     *
     * @param serverId 服务器id
     * @return 日志库列
     */
    @Override
    public TDb selectTDbByServerId(Integer serverId)
    {
        return tDbMapper.selectTDbByServerId(serverId);
    }

    /**
     * 查询日志库列列表
     * 
     * @param tDb 日志库列
     * @return 日志库列
     */
    @Override
    public List<TDb> selectTDbList(TDb tDb)
    {
        return tDbMapper.selectTDbList(tDb);
    }

    /**
     * 新增日志库列
     * 
     * @param tDb 日志库列
     * @return 结果
     */
    @Override
    public int insertTDb(TDb tDb)
    {
        tDb.setUpdateDate(new Date());
        return tDbMapper.insertTDb(tDb);
    }

    /**
     * 修改日志库列
     * 
     * @param tDb 日志库列
     * @return 结果
     */
    @Override
    public int updateTDb(TDb tDb)
    {
        tDb.setUpdateDate(new Date());
        return tDbMapper.updateTDb(tDb);
    }

    /**
     * 删除日志库列对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTDbByIds(String ids)
    {
        return tDbMapper.deleteTDbByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除日志库列信息
     * 
     * @param id 日志库列ID
     * @return 结果
     */
    @Override
    public int deleteTDbById(Integer id)
    {
        return tDbMapper.deleteTDbById(id);
    }
}
