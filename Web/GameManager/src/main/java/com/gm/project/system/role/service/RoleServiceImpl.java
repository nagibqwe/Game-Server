package com.gm.project.system.role.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gm.common.constant.UserConstants;
import com.gm.common.exception.BusinessException;
import com.gm.common.utils.StringUtils;
import com.gm.common.utils.security.ShiroUtils;
import com.gm.common.utils.spring.SpringUtils;
import com.gm.common.utils.text.Convert;
import com.gm.framework.aspectj.lang.annotation.DataScope;
import com.gm.project.system.role.domain.Role;
import com.gm.project.system.role.domain.RoleDept;
import com.gm.project.system.role.domain.RoleMenu;
import com.gm.project.system.role.mapper.RoleDeptMapper;
import com.gm.project.system.role.mapper.RoleMapper;
import com.gm.project.system.role.mapper.RoleMenuMapper;
import com.gm.project.system.user.domain.UserRole;
import com.gm.project.system.user.mapper.UserRoleMapper;

/**
 * 角色 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class RoleServiceImpl implements IRoleService
{
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleDeptMapper roleDeptMapper;

    /**
     * 根据条件分页查询角色Данные
     * 
     * @param role 角色Информация
     * @return 角色Данные集合Информация
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<Role> selectRoleList(Role role)
    {
        return roleMapper.selectRoleList(role);
    }

    /**
     * 根据ID пользователя查询权限
     * 
     * @param userId ID пользователя
     * @return 权限列表
     */
    @Override
    public Set<String> selectRoleKeys(Long userId)
    {
        List<Role> perms = roleMapper.selectRolesByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (Role perm : perms)
        {
            if (StringUtils.isNotNull(perm))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据ID пользователя查询角色
     * 
     * @param userId ID пользователя
     * @return 角色列表
     */
    @Override
    public List<Role> selectRolesByUserId(Long userId)
    {
        List<Role> userRoles = roleMapper.selectRolesByUserId(userId);
        List<Role> roles = selectRoleAll();
        for (Role role : roles)
        {
            for (Role userRole : userRoles)
            {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue())
                {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    /**
     * 查询所有角色
     * 
     * @return 角色列表
     */
    @Override
    public List<Role> selectRoleAll()
    {
        return SpringUtils.getAopProxy(this).selectRoleList(new Role());
    }

    /**
     * 通过ID персонажа查询角色
     * 
     * @param roleId ID персонажа
     * @return 角色对象Информация
     */
    @Override
    public Role selectRoleById(Long roleId)
    {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     * 通过ID персонажаУдалить角色
     * 
     * @param roleId ID персонажа
     * @return Результат
     */
    @Override
    @Transactional
    public boolean deleteRoleById(Long roleId)
    {
        // Удалить角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        // Удалить角色与部门关联
        roleDeptMapper.deleteRoleDeptByRoleId(roleId);
        return roleMapper.deleteRoleById(roleId) > 0 ? true : false;
    }

    /**
     * 批量Удалить角色Информация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @throws Exception
     */
    @Override
    @Transactional
    public int deleteRoleByIds(String ids)
    {
        Long[] roleIds = Convert.toLongArray(ids);
        for (Long roleId : roleIds)
        {
            checkRoleAllowed(new Role(roleId));
            Role role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0)
            {
                throw new BusinessException(String.format("%1$s已分配,不能Удалить", role.getRoleName()));
            }
        }
        // Удалить角色与菜单关联
        roleMenuMapper.deleteRoleMenu(roleIds);
        // Удалить角色与部门关联
        roleDeptMapper.deleteRoleDept(roleIds);
        return roleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * ДобавитьСохранить角色Информация
     * 
     * @param role 角色Информация
     * @return Результат
     */
    @Override
    @Transactional
    public int insertRole(Role role)
    {
        role.setCreateBy(ShiroUtils.getLoginName());
        // Добавить角色Информация
        roleMapper.insertRole(role);
        return insertRoleMenu(role);
    }

    /**
     * ИзменитьСохранить角色Информация
     * 
     * @param role 角色Информация
     * @return Результат
     */
    @Override
    @Transactional
    public int updateRole(Role role)
    {
        role.setUpdateBy(ShiroUtils.getLoginName());
        // Изменить角色Информация
        roleMapper.updateRole(role);
        // Удалить角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * ИзменитьДанные权限Информация
     * 
     * @param role 角色Информация
     * @return Результат
     */
    @Override
    @Transactional
    public int authDataScope(Role role)
    {
        role.setUpdateBy(ShiroUtils.getLoginName());
        // Изменить角色Информация
        roleMapper.updateRole(role);
        // Удалить角色与部门关联
        roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
        // Добавить角色和部门Информация（Данные权限）
        return insertRoleDept(role);
    }

    /**
     * Добавить角色菜单Информация
     * 
     * @param role 角色对象
     */
    public int insertRoleMenu(Role role)
    {
        int rows = 1;
        // Добавить用户与Управление персонажами
        List<RoleMenu> list = new ArrayList<RoleMenu>();
        for (Long menuId : role.getMenuIds())
        {
            RoleMenu rm = new RoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0)
        {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }

    /**
     * Добавить角色部门Информация(Данные权限)
     *
     * @param role 角色对象
     */
    public int insertRoleDept(Role role)
    {
        int rows = 1;
        // Добавить角色与部门（Данные权限）管理
        List<RoleDept> list = new ArrayList<RoleDept>();
        for (Long deptId : role.getDeptIds())
        {
            RoleDept rd = new RoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size() > 0)
        {
            rows = roleDeptMapper.batchRoleDept(list);
        }
        return rows;
    }

    /**
     * 校验Имя персонажаДаНет唯一
     * 
     * @param role 角色Информация
     * @return Результат
     */
    @Override
    public String checkRoleNameUnique(Role role)
    {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        Role info = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
        {
            return UserConstants.ROLE_NAME_NOT_UNIQUE;
        }
        return UserConstants.ROLE_NAME_UNIQUE;
    }

    /**
     * 校验角色权限ДаНет唯一
     * 
     * @param role 角色Информация
     * @return Результат
     */
    @Override
    public String checkRoleKeyUnique(Role role)
    {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        Role info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
        {
            return UserConstants.ROLE_KEY_NOT_UNIQUE;
        }
        return UserConstants.ROLE_KEY_UNIQUE;
    }

    /**
     * 校验角色ДаНет允许Действия
     * 
     * @param role 角色Информация
     */
    @Override
    public void checkRoleAllowed(Role role)
    {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin())
        {
            throw new BusinessException("不允许Действия超级管理员角色");
        }
    }

    /**
     * 通过ID персонажа查询角色使用数量
     * 
     * @param roleId ID персонажа
     * @return Результат
     */
    @Override
    public int countUserRoleByRoleId(Long roleId)
    {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 角色СтатусИзменить
     * 
     * @param role 角色Информация
     * @return Результат
     */
    @Override
    public int changeStatus(Role role)
    {
        return roleMapper.updateRole(role);
    }

    /**
     * Отмена授权用户角色
     * 
     * @param userRole 用户和角色关联Информация
     * @return Результат
     */
    @Override
    public int deleteAuthUser(UserRole userRole)
    {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * 批量Отмена授权用户角色
     * 
     * @param roleId ID персонажа
     * @param userIds 需要Удалить的用户ДанныеID
     * @return Результат
     */
    @Override
    public int deleteAuthUsers(Long roleId, String userIds)
    {
        return userRoleMapper.deleteUserRoleInfos(roleId, Convert.toLongArray(userIds));
    }

    /**
     * 批量选择授权用户角色
     * 
     * @param roleId ID персонажа
     * @param userIds 需要Удалить的用户ДанныеID
     * @return Результат
     */
    @Override
    public int insertAuthUsers(Long roleId, String userIds)
    {
        Long[] users = Convert.toLongArray(userIds);
        // Добавить用户与Управление персонажами
        List<UserRole> list = new ArrayList<UserRole>();
        for (Long userId : users)
        {
            UserRole ur = new UserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return userRoleMapper.batchUserRole(list);
    }
}
