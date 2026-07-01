package com.gm.project.gmtool.roleTransfer.service;

import java.util.List;
import com.gm.project.gmtool.roleTransfer.domain.RoleTransfer;

/**
 * 角色转移Service接口
 * 
 * @author gm
 * @date 2021-11-03
 */
public interface IRoleTransferService 
{
    /**
     * 查询角色转移
     * 
     * @param roleId 角色转移ID
     * @return 角色转移
     */
    public RoleTransfer selectRoleTransferById(String roleId);

    /**
     * 查询角色转移列表
     * 
     * @param roleTransfer 角色转移
     * @return 角色转移集合
     */
    public List<RoleTransfer> selectRoleTransferList(RoleTransfer roleTransfer);

    /**
     * 新增角色转移
     * 
     * @param roleTransfer 角色转移
     * @return 结果
     */
    public int insertRoleTransfer(RoleTransfer roleTransfer);

    /**
     * 修改角色转移
     * 
     * @param roleTransfer 角色转移
     * @return 结果
     */
    public int updateRoleTransfer(RoleTransfer roleTransfer);

    /**
     * 批量删除角色转移
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleTransferByIds(String ids);

    /**
     * 删除角色转移信息
     * 
     * @param roleId 角色转移ID
     * @return 结果
     */
    public int deleteRoleTransferById(String roleId);
}
