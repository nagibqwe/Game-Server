package com.gm.project.system.dept.service;

import java.util.List;
import com.gm.framework.web.domain.Ztree;
import com.gm.project.system.dept.domain.Dept;
import com.gm.project.system.role.domain.Role;

/**
 * Подразделения 服务层
 * 
 * @author ruoyi
 */
public interface IDeptService
{
    /**
     * 查询ПодразделенияДанные
     * 
     * @param dept 部门Информация
     * @return 部门Информация集合
     */
    public List<Dept> selectDeptList(Dept dept);

    /**
     * 查询Подразделения树
     * 
     * @param dept 部门Информация
     * @return 所有部门Информация
     */
    public List<Ztree> selectDeptTree(Dept dept);

    /**
     * 查询Подразделения树（排除下级）
     * 
     * @param dept 部门Информация
     * @return 所有部门Информация
     */
    public List<Ztree> selectDeptTreeExcludeChild(Dept dept);

    /**
     * 根据ID персонажа查询菜单
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    public List<Ztree> roleDeptTreeData(Role role);

    /**
     * 查询部门Количество
     * 
     * @param parentId 父部门ID
     * @return Результат
     */
    public int selectDeptCount(Long parentId);

    /**
     * 查询部门ДаНет存在用户
     * 
     * @param deptId 部门ID
     * @return Результат true 存在 false 不存在
     */
    public boolean checkDeptExistUser(Long deptId);

    /**
     * УдалитьПодразделенияИнформация
     * 
     * @param deptId 部门ID
     * @return Результат
     */
    public int deleteDeptById(Long deptId);

    /**
     * ДобавитьСохранить部门Информация
     * 
     * @param dept 部门Информация
     * @return Результат
     */
    public int insertDept(Dept dept);

    /**
     * ИзменитьСохранить部门Информация
     * 
     * @param dept 部门Информация
     * @return Результат
     */
    public int updateDept(Dept dept);

    /**
     * 根据部门ID查询Информация
     * 
     * @param deptId 部门ID
     * @return 部门Информация
     */
    public Dept selectDeptById(Long deptId);

    /**
     * 根据ID查询所有子部门（НормаСтатус）
     * 
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(Long deptId);

    /**
     * 校验Название подразделенияДаНет唯一
     * 
     * @param dept 部门Информация
     * @return Результат
     */
    public String checkDeptNameUnique(Dept dept);
}
