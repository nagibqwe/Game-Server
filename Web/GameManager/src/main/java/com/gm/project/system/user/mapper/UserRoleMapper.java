package com.gm.project.system.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.gm.project.system.user.domain.UserRole;

/**
 * 用户与角色关联表 Данные层
 * 
 * @author ruoyi
 */
public interface UserRoleMapper
{
    /**
     * 通过ID пользователя查询用户和角色关联
     * 
     * @param userId ID пользователя
     * @return 用户和角色关联列表
     */
    public List<UserRole> selectUserRoleByUserId(Long userId);

    /**
     * 通过ID пользователяУдалить用户和角色关联
     * 
     * @param userId ID пользователя
     * @return Результат
     */
    public int deleteUserRoleByUserId(Long userId);

    /**
     * 批量Удалить用户和角色关联
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteUserRole(Long[] ids);

    /**
     * 通过ID персонажа查询角色使用数量
     * 
     * @param roleId ID персонажа
     * @return Результат
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * 批量Добавить用户角色Информация
     * 
     * @param userRoleList 用户角色列表
     * @return Результат
     */
    public int batchUserRole(List<UserRole> userRoleList);

    /**
     * Удалить用户和角色关联Информация
     * 
     * @param userRole 用户和角色关联Информация
     * @return Результат
     */
    public int deleteUserRoleInfo(UserRole userRole);

    /**
     * 批量Отмена授权用户角色
     * 
     * @param roleId ID персонажа
     * @param userIds 需要Удалить的用户ДанныеID
     * @return Результат
     */
    public int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
}
