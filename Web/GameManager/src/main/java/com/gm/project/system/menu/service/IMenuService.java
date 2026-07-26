package com.gm.project.system.menu.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gm.framework.web.domain.Ztree;
import com.gm.project.system.menu.domain.Menu;
import com.gm.project.system.role.domain.Role;
import com.gm.project.system.user.domain.User;

/**
 * 菜单 业务层
 * 
 * @author ruoyi
 */
public interface IMenuService
{
    /**
     * 根据ID пользователя查询菜单
     * 
     * @param user 用户Информация
     * @return 菜单列表
     */
    public List<Menu> selectMenusByUser(User user);

    /**
     * 查询系统菜单列表
     * 
     * @param menu 菜单Информация
     * @return 菜单列表
     */
    public List<Menu> selectMenuList(Menu menu);

    /**
     * 查询菜单集合
     * 
     * @return 所有菜单Информация
     */
    public List<Menu> selectMenuAll();

    /**
     * 根据ID пользователя查询权限
     * 
     * @param userId ID пользователя
     * @return 权限列表
     */
    public Set<String> selectPermsByUserId(Long userId);

    /**
     * 根据ID персонажа查询菜单
     * 
     * @param role 角色对象
     * @return 菜单列表
     */
    public List<Ztree> roleMenuTreeData(Role role);

    /**
     * 查询所有菜单Информация
     * 
     * @return 菜单列表
     */
    public List<Ztree> menuTreeData();

    /**
     * 查询系统所有权限
     * 
     * @return 权限列表
     */
    public Map<String, String> selectPermsAll();

    /**
     * УдалитьМенюИнформация
     * 
     * @param menuId 菜单ID
     * @return Результат
     */
    public int deleteMenuById(Long menuId);

    /**
     * 根据菜单ID查询Информация
     * 
     * @param menuId 菜单ID
     * @return 菜单Информация
     */
    public Menu selectMenuById(Long menuId);

    /**
     * 查询菜单数量
     * 
     * @param parentId 菜单父ID
     * @return Результат
     */
    public int selectCountMenuByParentId(Long parentId);

    /**
     * 查询菜单使用数量
     * 
     * @param menuId 菜单ID
     * @return Результат
     */
    public int selectCountRoleMenuByMenuId(Long menuId);

    /**
     * ДобавитьСохранить菜单Информация
     * 
     * @param menu 菜单Информация
     * @return Результат
     */
    public int insertMenu(Menu menu);

    /**
     * ИзменитьСохранить菜单Информация
     * 
     * @param menu 菜单Информация
     * @return Результат
     */
    public int updateMenu(Menu menu);

    /**
     * 校验菜单НазваниеДаНет唯一
     * 
     * @param menu 菜单Информация
     * @return Результат
     */
    public String checkMenuNameUnique(Menu menu);
}
