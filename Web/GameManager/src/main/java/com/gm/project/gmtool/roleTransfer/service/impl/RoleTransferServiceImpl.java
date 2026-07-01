package com.gm.project.gmtool.roleTransfer.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.roleTransfer.mapper.RoleTransferMapper;
import com.gm.project.gmtool.roleTransfer.domain.RoleTransfer;
import com.gm.project.gmtool.roleTransfer.service.IRoleTransferService;
import com.gm.common.utils.text.Convert;

/**
 * 角色转移Service业务层处理
 * 
 * @author gm
 * @date 2021-11-03
 */
@Service
public class RoleTransferServiceImpl implements IRoleTransferService 
{
    @Autowired
    private RoleTransferMapper roleTransferMapper;

    /**
     * 查询角色转移
     * 
     * @param roleId 角色转移ID
     * @return 角色转移
     */
    @Override
    public RoleTransfer selectRoleTransferById(String roleId)
    {
        return roleTransferMapper.selectRoleTransferById(roleId);
    }

    /**
     * 查询角色转移列表
     * 
     * @param roleTransfer 角色转移
     * @return 角色转移
     */
    @Override
    public List<RoleTransfer> selectRoleTransferList(RoleTransfer roleTransfer)
    {
        return roleTransferMapper.selectRoleTransferList(roleTransfer);
    }

    /**
     * 新增角色转移
     * 
     * @param roleTransfer 角色转移
     * @return 结果
     */
    @Override
    public int insertRoleTransfer(RoleTransfer roleTransfer)
    {
        return roleTransferMapper.insertRoleTransfer(roleTransfer);
    }

    /**
     * 修改角色转移
     * 
     * @param roleTransfer 角色转移
     * @return 结果
     */
    @Override
    public int updateRoleTransfer(RoleTransfer roleTransfer)
    {
        return roleTransferMapper.updateRoleTransfer(roleTransfer);
    }

    /**
     * 删除角色转移对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteRoleTransferByIds(String ids)
    {
        return roleTransferMapper.deleteRoleTransferByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除角色转移信息
     * 
     * @param roleId 角色转移ID
     * @return 结果
     */
    @Override
    public int deleteRoleTransferById(String roleId)
    {
        return roleTransferMapper.deleteRoleTransferById(roleId);
    }
}
