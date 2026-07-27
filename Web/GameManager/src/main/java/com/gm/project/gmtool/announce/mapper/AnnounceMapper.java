package com.gm.project.gmtool.announce.mapper;

import java.util.List;
import com.gm.project.gmtool.announce.domain.Announce;

/**
 * 即时公告Mapper接口
 * 
 * @author gm
 * @date 2021-10-21
 */
public interface AnnounceMapper 
{
    /**
     * 查询即时公告
     * 
     * @param id 即时公告ID
     * @return 即时公告
     */
    public Announce selectAnnounceById(Integer id);

    /**
     * 查询即时公告列表
     * 
     * @param announce 即时公告
     * @return 即时公告集合
     */
    public List<Announce> selectAnnounceList(Announce announce);

    /**
     * 新增即时公告
     * 
     * @param announce 即时公告
     * @return 结果
     */
    public int insertAnnounce(Announce announce);

    /**
     * 修改即时公告
     * 
     * @param announce 即时公告
     * @return 结果
     */
    public int updateAnnounce(Announce announce);

    /**
     * 删除即时公告
     * 
     * @param id 即时公告ID
     * @return 结果
     */
    public int deleteAnnounceById(Integer id);

    /**
     * 批量删除即时公告
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAnnounceByIds(String[] ids);
}
