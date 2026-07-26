package com.gm.project.gmtool.roleAttr.service;

import java.util.List;
import com.gm.project.gmtool.roleAttr.domain.RoleAttr;

/**
 * Изменить характеристикиService接口
 * 
 * @author gm
 * @date 2021-11-02
 */
public interface IRoleAttrService 
{
    /**
     * 查询Изменить характеристики
     * 
     * @param id Изменить характеристикиID
     * @return Изменить характеристики
     */
    public RoleAttr selectRoleAttrById(Integer id);

    /**
     * 查询Изменить характеристики列表
     * 
     * @param roleAttr Изменить характеристики
     * @return Изменить характеристики集合
     */
    public List<RoleAttr> selectRoleAttrList(RoleAttr roleAttr);

    /**
     * ДобавитьИзменить характеристики
     * 
     * @param roleAttr Изменить характеристики
     * @return Результат
     */
    public int insertRoleAttr(RoleAttr roleAttr);

    /**
     * ИзменитьИзменить характеристики
     * 
     * @param roleAttr Изменить характеристики
     * @return Результат
     */
    public int updateRoleAttr(RoleAttr roleAttr);

    /**
     * 批量УдалитьИзменить характеристики
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteRoleAttrByIds(String ids);

    /**
     * УдалитьИзменить характеристикиИнформация
     * 
     * @param id Изменить характеристикиID
     * @return Результат
     */
    public int deleteRoleAttrById(Integer id);
}
