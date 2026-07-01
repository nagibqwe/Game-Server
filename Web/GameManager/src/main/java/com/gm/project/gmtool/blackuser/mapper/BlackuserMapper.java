package com.gm.project.gmtool.blackuser.mapper;

import java.util.List;
import com.gm.project.gmtool.blackuser.domain.Blackuser;

/**
 * 黑名单Mapper接口
 * 
 * @author gm
 * @date 2021-11-04
 */
public interface BlackuserMapper 
{
    /**
     * 查询黑名单
     * 
     * @param id 黑名单ID
     * @return 黑名单
     */
    public Blackuser selectBlackuserById(Integer id);

    /**
     * 查询黑名单列表
     * 
     * @param blackuser 黑名单
     * @return 黑名单集合
     */
    public List<Blackuser> selectBlackuserList(Blackuser blackuser);

    /**
     * 新增黑名单
     * 
     * @param blackuser 黑名单
     * @return 结果
     */
    public int insertBlackuser(Blackuser blackuser);

    /**
     * 修改黑名单
     * 
     * @param blackuser 黑名单
     * @return 结果
     */
    public int updateBlackuser(Blackuser blackuser);

    /**
     * 删除黑名单
     * 
     * @param id 黑名单ID
     * @return 结果
     */
    public int deleteBlackuserById(Integer id);

    /**
     * 批量删除黑名单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBlackuserByIds(String[] ids);
}
