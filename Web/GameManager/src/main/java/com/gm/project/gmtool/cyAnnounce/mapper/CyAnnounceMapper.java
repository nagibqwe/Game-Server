package com.gm.project.gmtool.cyAnnounce.mapper;

import java.util.List;
import com.gm.project.gmtool.cyAnnounce.domain.CyAnnounce;

/**
 * 循环公告Mapper接口
 * 
 * @author gm
 * @date 2021-10-27
 */
public interface CyAnnounceMapper 
{
    /**
     * 查询循环公告
     * 
     * @param id 循环公告ID
     * @return 循环公告
     */
    public CyAnnounce selectCyAnnounceById(Integer id);

    /**
     * 查询循环公告列表(启用列表)
     * 
     * @param cyAnnounce 循环公告
     * @return 循环公告集合
     */
    public List<CyAnnounce> selectCyAnnounceList(CyAnnounce cyAnnounce);

    /**
     * 新增循环公告
     * 
     * @param cyAnnounce 循环公告
     * @return 结果
     */
    public int insertCyAnnounce(CyAnnounce cyAnnounce);

    /**
     * 修改循环公告
     * 
     * @param cyAnnounce 循环公告
     * @return 结果
     */
    public int updateCyAnnounce(CyAnnounce cyAnnounce);

    /**
     * 删除循环公告
     * 
     * @param id 循环公告ID
     * @return 结果
     */
    public int deleteCyAnnounceById(Integer id);

    /**
     * 批量删除循环公告
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCyAnnounceByIds(String[] ids);

    /**
     * 查询循环公告列表(禁用列表)
     * @param cyAnnounce
     * @return
     */
    public List<CyAnnounce> selectCyAnnounceDisableList(CyAnnounce cyAnnounce);
}
