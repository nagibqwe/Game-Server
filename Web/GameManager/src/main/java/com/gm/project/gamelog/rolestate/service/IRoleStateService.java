package com.gm.project.gamelog.rolestate.service;

import java.util.List;
import java.util.Map;

import com.gm.project.gamelog.rolestate.domain.RoleState;

/**
 * Журнал снимков персонажейService接口
 * 
 * @author gm
 * @date 2021-09-07
 */
public interface IRoleStateService 
{
//    /**
//     * 查询Журнал снимков персонажей
//     *
//     * @param roleId Журнал снимков персонажейID
//     * @return Журнал снимков персонажей
//     */
//    public RoleState selectRoleStateById(Long roleId);

    /**
     * 查询Журнал снимков персонажей列表
     * 
     * @param roleState Журнал снимков персонажей
     * @return Журнал снимков персонажей集合
     */
    public List<RoleState> selectRoleStateList(RoleState roleState, Map<String, Object> param);

    public List<RoleState> selectRoleStateList(Integer serverId, String roleIds);

//    /**
//     * ДобавитьЖурнал снимков персонажей
//     *
//     * @param roleState Журнал снимков персонажей
//     * @return Результат
//     */
//    public int insertRoleState(RoleState roleState);
//
//    /**
//     * ИзменитьЖурнал снимков персонажей
//     *
//     * @param roleState Журнал снимков персонажей
//     * @return Результат
//     */
//    public int updateRoleState(RoleState roleState);
//
//    /**
//     * 批量УдалитьЖурнал снимков персонажей
//     *
//     * @param ids 需要Удалить的ДанныеID
//     * @return Результат
//     */
//    public int deleteRoleStateByIds(String ids);
//
//    /**
//     * УдалитьЖурнал снимков персонажейИнформация
//     *
//     * @param roleId Журнал снимков персонажейID
//     * @return Результат
//     */
//    public int deleteRoleStateById(Long roleId);
}
