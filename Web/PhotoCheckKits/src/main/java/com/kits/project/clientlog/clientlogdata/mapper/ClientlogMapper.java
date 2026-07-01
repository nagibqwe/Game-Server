package com.kits.project.clientlog.clientlogdata.mapper;

import java.util.List;
import com.kits.project.clientlog.clientlogdata.domain.Clientlog;

/**
 * 客户端日志Mapper接口
 * 
 * @author gzg
 * @date 2021-06-18
 */
public interface ClientlogMapper 
{
    /**
     * 查询客户端日志
     * 
     * @param id 客户端日志ID
     * @return 客户端日志
     */
    public Clientlog selectClientlogById(Long id);
    /**
     * 查询客户端日志
     *
     * @param clientlog 客户端日志
     * @return 客户端日志
     */
    public Clientlog selectClientlogByKey(Clientlog clientlog);

    /**
     * 查询客户端日志列表
     * 
     * @param clientlog 客户端日志
     * @return 客户端日志集合
     */
    public List<Clientlog> selectClientlogList(Clientlog clientlog);

    /**
     * 新增客户端日志
     * 
     * @param clientlog 客户端日志
     * @return 结果
     */
    public int insertClientlog(Clientlog clientlog);

    /**
     * 修改客户端日志
     * 
     * @param clientlog 客户端日志
     * @return 结果
     */
    public int updateClientlog(Clientlog clientlog);

    /**
     * 删除客户端日志
     * 
     * @param id 客户端日志ID
     * @return 结果
     */
    public int deleteClientlogById(Long id);

    /**
     * 批量删除客户端日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteClientlogByIds(String[] ids);
}
