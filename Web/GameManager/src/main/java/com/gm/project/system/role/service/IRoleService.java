package com.gm.project.system.role.service;

import java.util.List;
import java.util.Set;
import com.gm.project.system.role.domain.Role;
import com.gm.project.system.user.domain.UserRole;

/**
 * 角色业务层
 * 
 * @author ruoyi
 */
public interface IRoleService
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
     * @return 权限列表
     */
    public Set<String> selectRoleKeys(Long userId);

    /**
     * 根据ID пользователя查询角色
     * 
     * @param userId ID пользователя
     * @return 角色列表
     */
    public List<Role> selectRolesByUserId(Long userId);

    /**
     * 查询所有角色
     * 
     * @return 角色列表
     */
    public List<Role> selectRoleAll();

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
    public boolean deleteRoleById(Long roleId);

    /**
     * 批量Удалить角色用户Информация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     * @throws Exception 异常
     */
    public int deleteRoleByIds(String ids);

    /**
     * ДобавитьСохранить角色Информация
     * 
     * @param role 角色Информация
     * @return Результат
     */
    public int insertRole(Role role);

    /**
     * ИзменитьСохранить角色Информация
     * 
     * @param role 角色Информация
     * @return Результат
     */
    public int updateRole(Role role);

    /**
     * ИзменитьДанные权限Информация
     * 
     * @param role 角色Информация
     * @return Результат
     */
    public int authDataScope(Role role);

    /**
     * 校验Имя персонажаДаНет唯一
     * 
     * @param role 角色Информация
     * @return Результат
     */
    public String checkRoleNameUnique(Role role);

    /**
     * 校验角色权限ДаНет唯一
     * 
     * @param role 角色Информация
     * @return Результат
     */
    public String checkRoleKeyUnique(Role role);

    /**
     * 校验角色ДаНет允许Действия
     * 
     * @param role 角色Информация
     */
    public void checkRoleAllowed(Role role);

    /**
     * 通过ID персонажа查询角色使用数量
     * 
     * @param roleId ID персонажа
     * @return Результат
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * 角色СтатусИзменить
     * 
     * @param role 角色Информация
     * @return Результат
     */
    public int changeStatus(Role role);

    /**
     * Отмена授权用户角色
     * 
     * @param userRole 用户和角色关联Информация
     * @return Результат
     */
    public int deleteAuthUser(UserRole userRole);

    /**
     * 批量Отмена授权用户角色
     * 
     * @param roleId ID персонажа
     * @param userIds 需要Удалить的用户ДанныеID
     * @return Результат
     */
    public int deleteAuthUsers(Long roleId, String userIds);

    /**
     * 批量选择授权用户角色
     * 
     * @param roleId ID персонажа
     * @param userIds 需要Удалить的用户ДанныеID
     * @return Результат
     */
    public int insertAuthUsers(Long roleId, String userIds);
}
