package com.kits.project.clientlog.clientlogdata.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.clientlog.clientlogdata.mapper.ClientlogMapper;
import com.kits.project.clientlog.clientlogdata.domain.Clientlog;
import com.kits.project.clientlog.clientlogdata.service.IClientlogService;
import com.kits.common.utils.text.Convert;

/**
 * 客户端日志Service业务层处理
 * 
 * @author gzg
 * @date 2021-06-18
 */
@Service
public class ClientlogServiceImpl implements IClientlogService 
{
    @Autowired
    private ClientlogMapper clientlogMapper;

    /**
     * 查询客户端日志
     * 
     * @param id 客户端日志ID
     * @return 客户端日志
     */
    @Override
    public Clientlog selectClientlogById(Long id)
    {
        return clientlogMapper.selectClientlogById(id);
    }
    /**
     * 查询客户端日志
     *
     * @param clientlog 客户端日志
     * @return 客户端日志
     */
    @Override
    public Clientlog selectClientlogByKey(Clientlog clientlog){
        return clientlogMapper.selectClientlogByKey(clientlog);
    }
    /**
     * 查询客户端日志列表
     * 
     * @param clientlog 客户端日志
     * @return 客户端日志
     */
    @Override
    public List<Clientlog> selectClientlogList(Clientlog clientlog)
    {
        return clientlogMapper.selectClientlogList(clientlog);
    }

    /**
     * 新增客户端日志
     * 
     * @param clientlog 客户端日志
     * @return 结果
     */
    @Override
    public int insertClientlog(Clientlog clientlog)
    {
        return clientlogMapper.insertClientlog(clientlog);
    }

    /**
     * 修改客户端日志
     * 
     * @param clientlog 客户端日志
     * @return 结果
     */
    @Override
    public int updateClientlog(Clientlog clientlog)
    {
        return clientlogMapper.updateClientlog(clientlog);
    }

    /**
     * 删除客户端日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteClientlogByIds(String ids)
    {
        return clientlogMapper.deleteClientlogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除客户端日志信息
     * 
     * @param id 客户端日志ID
     * @return 结果
     */
    @Override
    public int deleteClientlogById(Long id)
    {
        return clientlogMapper.deleteClientlogById(id);
    }
}
