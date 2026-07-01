package com.gm.project.gmtool.roleAttr.service;

import java.util.List;
import com.gm.project.gmtool.roleAttr.domain.RoleAttr;

/**
 * 修改属性Service接口
 * 
 * @author gm
 * @date 2021-11-02
 */
public interface IRoleAttrService 
{
    /**
     * 查询修改属性
     * 
     * @param id 修改属性ID
     * @return 修改属性
     */
    public RoleAttr selectRoleAttrById(Integer id);

    /**
     * 查询修改属性列表
     * 
     * @param roleAttr 修改属性
     * @return 修改属性集合
     */
    public List<RoleAttr> selectRoleAttrList(RoleAttr roleAttr);

    /**
     * 新增修改属性
     * 
     * @param roleAttr 修改属性
     * @return 结果
     */
    public int insertRoleAttr(RoleAttr roleAttr);

    /**
     * 修改修改属性
     * 
     * @param roleAttr 修改属性
     * @return 结果
     */
    public int updateRoleAttr(RoleAttr roleAttr);

    /**
     * 批量删除修改属性
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleAttrByIds(String ids);

    /**
     * 删除修改属性信息
     * 
     * @param id 修改属性ID
     * @return 结果
     */
    public int deleteRoleAttrById(Integer id);
}
