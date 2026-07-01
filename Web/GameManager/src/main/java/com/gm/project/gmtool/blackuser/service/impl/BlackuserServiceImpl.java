package com.gm.project.gmtool.blackuser.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.blackuser.mapper.BlackuserMapper;
import com.gm.project.gmtool.blackuser.domain.Blackuser;
import com.gm.project.gmtool.blackuser.service.IBlackuserService;
import com.gm.common.utils.text.Convert;

/**
 * 黑名单Service业务层处理
 * 
 * @author gm
 * @date 2021-11-04
 */
@Service
public class BlackuserServiceImpl implements IBlackuserService 
{
    @Autowired
    private BlackuserMapper blackuserMapper;

    /**
     * 查询黑名单
     * 
     * @param id 黑名单ID
     * @return 黑名单
     */
    @Override
    public Blackuser selectBlackuserById(Integer id)
    {
        return blackuserMapper.selectBlackuserById(id);
    }

    /**
     * 查询黑名单列表
     * 
     * @param blackuser 黑名单
     * @return 黑名单
     */
    @Override
    public List<Blackuser> selectBlackuserList(Blackuser blackuser)
    {
        return blackuserMapper.selectBlackuserList(blackuser);
    }

    /**
     * 新增黑名单
     * 
     * @param blackuser 黑名单
     * @return 结果
     */
    @Override
    public int insertBlackuser(Blackuser blackuser)
    {
        return blackuserMapper.insertBlackuser(blackuser);
    }

    /**
     * 修改黑名单
     * 
     * @param blackuser 黑名单
     * @return 结果
     */
    @Override
    public int updateBlackuser(Blackuser blackuser)
    {
        return blackuserMapper.updateBlackuser(blackuser);
    }

    /**
     * 删除黑名单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBlackuserByIds(String ids)
    {
        return blackuserMapper.deleteBlackuserByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除黑名单信息
     * 
     * @param id 黑名单ID
     * @return 结果
     */
    @Override
    public int deleteBlackuserById(Integer id)
    {
        return blackuserMapper.deleteBlackuserById(id);
    }
}
