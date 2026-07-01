package com.gm.project.gmtool.roleAttr.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.roleAttr.mapper.RoleAttrMapper;
import com.gm.project.gmtool.roleAttr.domain.RoleAttr;
import com.gm.project.gmtool.roleAttr.service.IRoleAttrService;
import com.gm.common.utils.text.Convert;

/**
 * 修改属性Service业务层处理
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
     * 查询修改属性
     * 
     * @param id 修改属性ID
     * @return 修改属性
     */
    @Override
    public RoleAttr selectRoleAttrById(Integer id)
    {
        return roleAttrMapper.selectRoleAttrById(id);
    }

    /**
     * 查询修改属性列表
     * 
     * @param roleAttr 修改属性
     * @return 修改属性
     */
    @Override
    public List<RoleAttr> selectRoleAttrList(RoleAttr roleAttr)
    {
        return roleAttrMapper.selectRoleAttrList(roleAttr);
    }

    /**
     * 新增修改属性
     * 
     * @param roleAttr 修改属性
     * @return 结果
     */
    @Override
    public int insertRoleAttr(RoleAttr roleAttr)
    {
        return roleAttrMapper.insertRoleAttr(roleAttr);
    }

    /**
     * 修改修改属性
     * 
     * @param roleAttr 修改属性
     * @return 结果
     */
    @Override
    public int updateRoleAttr(RoleAttr roleAttr)
    {
        return roleAttrMapper.updateRoleAttr(roleAttr);
    }

    /**
     * 删除修改属性对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteRoleAttrByIds(String ids)
    {
        return roleAttrMapper.deleteRoleAttrByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除修改属性信息
     * 
     * @param id 修改属性ID
     * @return 结果
     */
    @Override
    public int deleteRoleAttrById(Integer id)
    {
        return roleAttrMapper.deleteRoleAttrById(id);
    }
}
