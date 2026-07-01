package com.gm.project.gmtool.white.service;

import java.util.List;
import com.gm.project.gmtool.white.domain.White;

/**
 * 白名单管理Service接口
 * 
 * @author gm
 * @date 2021-11-22
 */
public interface IWhiteService 
{
    /**
     * 查询白名单管理
     * 
     * @param id 白名单管理ID
     * @return 白名单管理
     */
    public White selectWhiteById(Long id);

    /**
     * 查询白名单管理列表
     * 
     * @param white 白名单管理
     * @return 白名单管理集合
     */
    public List<White> selectWhiteList(White white);

    /**
     * 新增白名单管理
     * 
     * @param white 白名单管理
     * @return 结果
     */
    public int insertWhite(White white);

    /**
     * 修改白名单管理
     * 
     * @param white 白名单管理
     * @return 结果
     */
    public int updateWhite(White white);

    /**
     * 批量删除白名单管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWhiteByIds(String ids);

    /**
     * 删除白名单管理信息
     * 
     * @param id 白名单管理ID
     * @return 结果
     */
    public int deleteWhiteById(Long id);
}
