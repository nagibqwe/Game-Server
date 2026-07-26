package com.gm.project.system.role.mapper;

import java.util.List;
import com.gm.project.system.role.domain.RoleMenu;

/**
 * 角色与菜单关联表 Данные层
 * 
 * @author ruoyi
 */
public interface RoleMenuMapper
{
    /**
     * 通过ID персонажаУдалить角色和菜单关联
     * 
     * @param roleId ID персонажа
     * @return Результат
     */
    public int deleteRoleMenuByRoleId(Long roleId);
    
    /**
     * 批量Удалить角色菜单关联Информация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteRoleMenu(Long[] ids);
    
    /**
     * 查询菜单使用数量
     * 
     * @param menuId 菜单ID
     * @return Результат
     */
    public int selectCountRoleMenuByMenuId(Long menuId);
    
    /**
     * 批量Добавить角色菜单Информация
     * 
     * @param roleMenuList 角色菜单列表
     * @return Результат
     */
    public int batchRoleMenu(List<RoleMenu> roleMenuList);
}
