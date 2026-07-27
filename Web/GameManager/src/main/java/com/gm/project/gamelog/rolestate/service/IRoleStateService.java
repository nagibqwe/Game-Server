package com.gm.project.gamelog.rolestate.service;

import java.util.List;
import java.util.Map;

import com.gm.project.gamelog.rolestate.domain.RoleState;

/**
 * 角色快照日志Service接口
 * 
 * @author gm
 * @date 2021-09-07
 */
public interface IRoleStateService 
{
//    /**
//     * 查询角色快照日志
//     *
//     * @param roleId 角色快照日志ID
//     * @return 角色快照日志
//     */
//    public RoleState selectRoleStateById(Long roleId);

    /**
     * 查询角色快照日志列表
     * 
     * @param roleState 角色快照日志
     * @return 角色快照日志集合
     */
    public List<RoleState> selectRoleStateList(RoleState roleState, Map<String, Object> param);

    public List<RoleState> selectRoleStateList(Integer serverId, String roleIds);

//    /**
//     * 新增角色快照日志
//     *
//     * @param roleState 角色快照日志
//     * @return 结果
//     */
//    public int insertRoleState(RoleState roleState);
//
//    /**
//     * 修改角色快照日志
//     *
//     * @param roleState 角色快照日志
//     * @return 结果
//     */
//    public int updateRoleState(RoleState roleState);
//
//    /**
//     * 批量删除角色快照日志
//     *
//     * @param ids 需要删除的数据ID
//     * @return 结果
//     */
//    public int deleteRoleStateByIds(String ids);
//
//    /**
//     * 删除角色快照日志信息
//     *
//     * @param roleId 角色快照日志ID
//     * @return 结果
//     */
//    public int deleteRoleStateById(Long roleId);
}
