package com.gm.project.system.role.mapper;

import java.util.List;
import com.gm.project.system.role.domain.Role;

/**
 * 角色表 Данные层
 * 
 * @author ruoyi
 */
public interface RoleMapper
{
    /**
     * 根据条件分页查询角色Данные
     * 
     * @param role 角色Информация
     * @return 角色Данные集合Информация
     */
    public List<Role> selectRoleList(Role role);

    /**
     * 根据ID пользователя查询角色
     * 
     * @param userId ID пользователя
     * @return 角色列表
     */
    public List<Role> selectRolesByUserId(Long userId);

    /**
     * 通过ID персонажа查询角色
     * 
     * @param roleId ID персонажа
     * @return 角色对象Информация
     */
    public Role selectRoleById(Long roleId);

    /**
     * 通过ID персонажаУдалить角色
     * 
     * @param roleId ID персонажа
     * @return Результат
     */
    public int deleteRoleById(Long roleId);

    /**
     * 批量角色用户Информация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteRoleByIds(Long[] ids);

    /**
     * Изменить角色Информация
     * 
     * @param role 角色Информация
     * @return Результат
     */
    public int updateRole(Role role);

    /**
     * Добавить角色Информация
     * 
     * @param role 角色Информация
     * @return Результат
     */
    public int insertRole(Role role);

    /**
     * 校验Имя персонажаДаНет唯一
     * 
     * @param roleName Имя персонажа
     * @return 角色Информация
     */
    public Role checkRoleNameUnique(String roleName);
    
    /**
     * 校验角色权限ДаНет唯一
     * 
     * @param roleKey 角色权限
     * @return 角色Информация
     */
    public Role checkRoleKeyUnique(String roleKey);
}
