package com.gm.project.system.menu.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.gm.project.system.menu.domain.Menu;

/**
 * 菜单表 Данные层
 * 
 * @author ruoyi
 */
public interface MenuMapper
{
    /**
     * 查询系统所有菜单（含按钮）
     * 
     * @return 菜单列表
     */
    public List<Menu> selectMenuAll();

    /**
     * 根据ID пользователя查询菜单
     * 
     * @param userId ID пользователя
     * @return 菜单列表
     */
    public List<Menu> selectMenuAllByUserId(Long userId);

    /**
     * 查询系统НормаПоказывать菜单（不含按钮）
     * 
     * @return 菜单列表
     */
    public List<Menu> selectMenuNormalAll();

    /**
     * 根据ID пользователя查询菜单
     * 
     * @param userId ID пользователя
     * @return 菜单列表
     */
    public List<Menu> selectMenusByUserId(Long userId);

    /**
     * 根据ID пользователя查询权限
     * 
     * @param userId ID пользователя
     * @return 权限列表
     */
    public List<String> selectPermsByUserId(Long userId);

    /**
     * 根据ID персонажа查询菜单
     * 
     * @param roleId ID персонажа
     * @return 菜单列表
     */
    public List<String> selectMenuTree(Long roleId);

    /**
     * 查询系统菜单列表
     * 
     * @param menu 菜单Информация
     * @return 菜单列表
     */
    public List<Menu> selectMenuList(Menu menu);

    /**
     * 查询系统菜单列表
     * 
     * @param menu 菜单Информация
     * @return 菜单列表
     */
    public List<Menu> selectMenuListByUserId(Menu menu);

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
     * Добавить菜单Информация
     * 
     * @param menu 菜单Информация
     * @return Результат
     */
    public int insertMenu(Menu menu);

    /**
     * Изменить菜单Информация
     * 
     * @param menu 菜单Информация
     * @return Результат
     */
    public int updateMenu(Menu menu);

    /**
     * 校验菜单НазваниеДаНет唯一
     * 
     * @param menuName 菜单Название
     * @param parentId 父菜单ID
     * @return Результат
     */
    public Menu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);
}
