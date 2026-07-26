package com.gm.project.system.dept.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.gm.project.system.dept.domain.Dept;

/**
 * Подразделения Данные层
 * 
 * @author ruoyi
 */
public interface DeptMapper
{
    /**
     * 查询部门Количество
     * 
     * @param dept 部门Информация
     * @return Результат
     */
    public int selectDeptCount(Dept dept);

    /**
     * 查询部门ДаНет存在用户
     * 
     * @param deptId 部门ID
     * @return Результат
     */
    public int checkDeptExistUser(Long deptId);

    /**
     * 查询ПодразделенияДанные
     * 
     * @param dept 部门Информация
     * @return 部门Информация集合
     */
    public List<Dept> selectDeptList(Dept dept);

    /**
     * УдалитьПодразделенияИнформация
     * 
     * @param deptId 部门ID
     * @return Результат
     */
    public int deleteDeptById(Long deptId);

    /**
     * Добавить部门Информация
     * 
     * @param dept 部门Информация
     * @return Результат
     */
    public int insertDept(Dept dept);

    /**
     * Изменить部门Информация
     * 
     * @param dept 部门Информация
     * @return Результат
     */
    public int updateDept(Dept dept);

    /**
     * Изменить子元素关系
     * 
     * @param depts 子元素
     * @return Результат
     */
    public int updateDeptChildren(@Param("depts") List<Dept> depts);

    /**
     * 根据部门ID查询Информация
     * 
     * @param deptId 部门ID
     * @return 部门Информация
     */
    public Dept selectDeptById(Long deptId);

    /**
     * 校验Название подразделенияДаНет唯一
     * 
     * @param deptName Название подразделения
     * @param parentId 父部门ID
     * @return Результат
     */
    public Dept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);

    /**
     * 根据ID персонажа查询部门
     *
     * @param roleId ID персонажа
     * @return 部门列表
     */
    public List<String> selectRoleDeptTree(Long roleId);

    /**
     * Изменить所在部门的父级部门Статус
     * 
     * @param dept 部门
     */
    public void updateDeptStatus(Dept dept);

    /**
     * 根据ID查询所有子部门
     * 
     * @param deptId 部门ID
     * @return 部门列表
     */
    public List<Dept> selectChildrenDeptById(Long deptId);

    /**
     * 根据ID查询所有子部门（НормаСтатус）
     * 
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(Long deptId);
}
