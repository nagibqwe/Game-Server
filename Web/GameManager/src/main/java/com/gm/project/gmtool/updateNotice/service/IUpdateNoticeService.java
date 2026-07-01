package com.gm.project.gmtool.updateNotice.service;

import java.util.List;
import com.gm.project.gmtool.updateNotice.domain.UpdateNotice;

/**
 * 更新公告Service接口
 * 
 * @author gm
 * @date 2021-10-30
 */
public interface IUpdateNoticeService 
{
    /**
     * 查询更新公告
     * 
     * @param id 更新公告ID
     * @return 更新公告
     */
    public UpdateNotice selectUpdateNoticeById(Integer id);

    /**
     * 查询更新公告列表
     * 
     * @param updateNotice 更新公告
     * @return 更新公告集合
     */
    public List<UpdateNotice> selectUpdateNoticeList(UpdateNotice updateNotice);

    /**
     * 新增更新公告
     * 
     * @param updateNotice 更新公告
     * @return 结果
     */
    public int insertUpdateNotice(UpdateNotice updateNotice);

    /**
     * 修改更新公告
     * 
     * @param updateNotice 更新公告
     * @return 结果
     */
    public int updateUpdateNotice(UpdateNotice updateNotice);

    /**
     * 批量删除更新公告
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUpdateNoticeByIds(String ids);

    /**
     * 删除更新公告信息
     * 
     * @param id 更新公告ID
     * @return 结果
     */
    public int deleteUpdateNoticeById(Integer id);
}
