package com.gm.project.gmtool.gamerole.service;

import com.gm.project.gamelog.rolestate.domain.RoleState;

import java.util.List;

/**
 * 游戏角色信息Service接口
 * 
 * @author gm
 * @date 2021-09-07
 */
public interface IGameRoleService
{

    List<RoleState> queryByRoleName(Integer selectServerId, String roleName);

    List<RoleState> queryByPlatFormAccount(Integer selectServerId, String pAccount);

    List<RoleState> queryByPlatFormUid(Integer selectServerId, String pUid);

    List<RoleState> queryByUserId(Integer selectServerId, String userId);

    List<RoleState> queryByRoleId(Integer selectServerId, String roleId, Integer roleIdType);



}
