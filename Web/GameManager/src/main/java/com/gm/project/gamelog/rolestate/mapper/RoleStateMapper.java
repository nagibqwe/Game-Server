package com.gm.project.gamelog.rolestate.mapper;

import java.util.List;
import com.gm.project.gamelog.rolestate.domain.RoleState;

/**
 * 角色快照日志Mapper接口
 * 
 * @author gm
 * @date 2021-09-07
 */
public interface RoleStateMapper 
{
    /**
     * 查询角色快照日志
     * 
     * @param roleId 角色快照日志ID
     * @return 角色快照日志
     */
    public RoleState selectRoleStateById(Long roleId);

    /**
     * 查询角色快照日志列表
     * 
     * @param roleState 角色快照日志
     * @return 角色快照日志集合
     */
    public List<RoleState> selectRoleStateList(RoleState roleState);

    /**
     * 新增角色快照日志
     * 
     * @param roleState 角色快照日志
     * @return 结果
     */
    public int insertRoleState(RoleState roleState);

    /**
     * 修改角色快照日志
     * 
     * @param roleState 角色快照日志
     * @return 结果
     */
    public int updateRoleState(RoleState roleState);

    /**
     * 删除角色快照日志
     * 
     * @param roleId 角色快照日志ID
     * @return 结果
     */
    public int deleteRoleStateById(Long roleId);

    /**
     * 批量删除角色快照日志
     * 
     * @param roleIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleStateByIds(String[] roleIds);
}
