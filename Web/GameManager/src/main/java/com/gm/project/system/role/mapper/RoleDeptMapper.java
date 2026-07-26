package com.gm.project.system.role.mapper;

import java.util.List;
import com.gm.project.system.role.domain.RoleDept;

/**
 * 角色与部门关联表 Данные层
 * 
 * @author ruoyi
 */
public interface RoleDeptMapper
{
    /**
     * 通过ID персонажаУдалить角色和部门关联
     * 
     * @param roleId ID персонажа
     * @return Результат
     */
    public int deleteRoleDeptByRoleId(Long roleId);

    /**
     * 批量Удалить角色部门关联Информация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteRoleDept(Long[] ids);

    /**
     * 查询部门使用数量
     * 
     * @param deptId 部门ID
     * @return Результат
     */
    public int selectCountRoleDeptByDeptId(Long deptId);

    /**
     * 批量Добавить角色部门Информация
     * 
     * @param roleDeptList 角色部门列表
     * @return Результат
     */
    public int batchRoleDept(List<RoleDept> roleDeptList);
}
