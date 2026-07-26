package com.gm.project.gmtool.roleAttr.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.roleAttr.mapper.RoleAttrMapper;
import com.gm.project.gmtool.roleAttr.domain.RoleAttr;
import com.gm.project.gmtool.roleAttr.service.IRoleAttrService;
import com.gm.common.utils.text.Convert;

/**
 * Изменить характеристикиService业务层处理
 * 
 * @author gm
 * @date 2021-11-02
 */
@Service
public class RoleAttrServiceImpl implements IRoleAttrService 
{
    @Autowired
    private RoleAttrMapper roleAttrMapper;

    /**
     * 查询Изменить характеристики
     * 
     * @param id Изменить характеристикиID
     * @return Изменить характеристики
     */
    @Override
    public RoleAttr selectRoleAttrById(Integer id)
    {
        return roleAttrMapper.selectRoleAttrById(id);
    }

    /**
     * 查询Изменить характеристики列表
     * 
     * @param roleAttr Изменить характеристики
     * @return Изменить характеристики
     */
    @Override
    public List<RoleAttr> selectRoleAttrList(RoleAttr roleAttr)
    {
        return roleAttrMapper.selectRoleAttrList(roleAttr);
    }

    /**
     * ДобавитьИзменить характеристики
     * 
     * @param roleAttr Изменить характеристики
     * @return Результат
     */
    @Override
    public int insertRoleAttr(RoleAttr roleAttr)
    {
        return roleAttrMapper.insertRoleAttr(roleAttr);
    }

    /**
     * ИзменитьИзменить характеристики
     * 
     * @param roleAttr Изменить характеристики
     * @return Результат
     */
    @Override
    public int updateRoleAttr(RoleAttr roleAttr)
    {
        return roleAttrMapper.updateRoleAttr(roleAttr);
    }

    /**
     * УдалитьИзменить характеристики对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteRoleAttrByIds(String ids)
    {
        return roleAttrMapper.deleteRoleAttrByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьИзменить характеристикиИнформация
     * 
     * @param id Изменить характеристикиID
     * @return Результат
     */
    @Override
    public int deleteRoleAttrById(Integer id)
    {
        return roleAttrMapper.deleteRoleAttrById(id);
    }
}
