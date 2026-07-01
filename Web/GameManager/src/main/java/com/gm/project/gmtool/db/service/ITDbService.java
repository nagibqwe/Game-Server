package com.gm.project.gmtool.db.service;

import java.util.List;
import com.gm.project.gmtool.db.domain.TDb;

/**
 * 日志库列Service接口
 * 
 * @author gm
 * @date 2021-09-08
 */
public interface ITDbService 
{
    /**
     * 查询日志库列
     * 
     * @param id 日志库列ID
     * @return 日志库列
     */
    public TDb selectTDbById(Integer id);

    /**
     * 查询日志库列
     *
     * @param serverId 服务器id
     * @return 日志库列
     */
    public TDb selectTDbByServerId(Integer serverId);

    /**
     * 查询日志库列列表
     * 
     * @param tDb 日志库列
     * @return 日志库列集合
     */
    public List<TDb> selectTDbList(TDb tDb);

    /**
     * 新增日志库列
     * 
     * @param tDb 日志库列
     * @return 结果
     */
    public int insertTDb(TDb tDb);

    /**
     * 修改日志库列
     * 
     * @param tDb 日志库列
     * @return 结果
     */
    public int updateTDb(TDb tDb);

    /**
     * 批量删除日志库列
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTDbByIds(String ids);

    /**
     * 删除日志库列信息
     * 
     * @param id 日志库列ID
     * @return 结果
     */
    public int deleteTDbById(Integer id);
}
