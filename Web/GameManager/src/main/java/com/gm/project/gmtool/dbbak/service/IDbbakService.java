package com.gm.project.gmtool.dbbak.service;

import java.util.List;
import com.gm.project.gmtool.dbbak.domain.Dbbak;

/**
 * 数据库备份Service接口
 * 
 * @author gm
 * @date 2021-09-13
 */
public interface IDbbakService 
{
    /**
     * 查询数据库备份
     * 
     * @param id 数据库备份ID
     * @return 数据库备份
     */
    public Dbbak selectDbbakById(Long id);

    public Dbbak selectLatestDbbak(Dbbak dbbak);

    /**
     * 查询数据库备份列表
     * 
     * @param dbbak 数据库备份
     * @return 数据库备份集合
     */
    public List<Dbbak> selectDbbakList(Dbbak dbbak);

    /**
     * 新增数据库备份
     * 
     * @param dbbak 数据库备份
     * @return 结果
     */
    public int insertDbbak(Dbbak dbbak);

    /**
     * 修改数据库备份
     * 
     * @param dbbak 数据库备份
     * @return 结果
     */
    public int updateDbbak(Dbbak dbbak);

    /**
     * 批量删除数据库备份
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDbbakByIds(String ids);

    /**
     * 删除数据库备份信息
     * 
     * @param id 数据库备份ID
     * @return 结果
     */
    public int deleteDbbakById(Long id);
}
