package com.kits.project.serverListConfig.notice.mapper;

import java.util.List;
import java.util.Map;

import com.kits.project.serverListConfig.notice.domain.SdkNotice;

/**
 * 公告管理Mapper接口
 * 
 * @author gm
 * @date 2021-06-22
 */
public interface SdkNoticeMapper 
{
    /**
     * 查询公告管理
     * 
     * @param noticeId 公告管理ID
     * @return 公告管理
     */
    public SdkNotice selectSdkNoticeById(Long noticeId);

    /**
     * 查询公告管理列表
     * 
     * @param sdkNotice 公告管理
     * @return 公告管理集合
     */
    public List<SdkNotice> selectSdkNoticeList(SdkNotice sdkNotice);

    /**
     * 新增公告管理
     * 
     * @param sdkNotice 公告管理
     * @return 结果
     */
    public int insertSdkNotice(SdkNotice sdkNotice);

    /**
     * 修改公告管理
     * 
     * @param sdkNotice 公告管理
     * @return 结果
     */
    public int updateSdkNotice(SdkNotice sdkNotice);

    /**
     * 删除公告管理
     * 
     * @param noticeId 公告管理ID
     * @return 结果
     */
    public int deleteSdkNoticeById(Long noticeId);

    /**
     * 批量删除公告管理
     * 
     * @param noticeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdkNoticeByIds(String[] noticeIds);

    /**
     * 查询公告管理列表(通过条件)
     * @param paramMap
     * @return
     */
    public List<SdkNotice> selectSdkNoticeListBy(Map paramMap);
}
