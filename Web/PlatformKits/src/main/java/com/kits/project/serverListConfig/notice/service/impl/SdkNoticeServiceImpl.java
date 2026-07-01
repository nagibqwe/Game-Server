package com.kits.project.serverListConfig.notice.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kits.common.utils.DateUtils;
import com.kits.common.utils.security.ShiroUtils;
import com.kits.project.serverListConfig.server.service.ISdkServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kits.project.serverListConfig.notice.mapper.SdkNoticeMapper;
import com.kits.project.serverListConfig.notice.domain.SdkNotice;
import com.kits.project.serverListConfig.notice.service.ISdkNoticeService;
import com.kits.common.utils.text.Convert;

import javax.annotation.PostConstruct;

/**
 * 公告管理Service业务层处理
 *
 * @author gm
 * @date 2021-06-22
 */
@Service
public class SdkNoticeServiceImpl implements ISdkNoticeService
{
    private ConcurrentHashMap<Long, SdkNotice> hashMap = new ConcurrentHashMap<Long, SdkNotice>();

    @Autowired
    private SdkNoticeMapper sdkNoticeMapper;

    @Autowired
    private ISdkServerService sdkServerService;

    /**
     * 项目启动时，初始化公告到缓存
     */
    @PostConstruct
    public void init()
    {
        hashMap.clear();
        List<SdkNotice> noticeList = sdkNoticeMapper.selectSdkNoticeList(new SdkNotice());
        for (SdkNotice notice : noticeList)
        {
            hashMap.put(notice.getNoticeId(), notice);
        }
    }

    /**
     * 获取公告总数量
     *
     * @return
     */
    @Override
    public int getNoticeCount() {
        return hashMap.size();
    }

    /**
     * 获取公告map
     *
     * @return
     */
    @Override
    public ConcurrentHashMap<Long, SdkNotice> getNoticeHashMap() {
        return hashMap;
    }

    /**
     * 查询公告管理
     *
     * @param noticeId 公告管理ID
     * @return 公告管理
     */
    @Override
    public SdkNotice selectSdkNoticeById(Long noticeId)
    {
        return sdkNoticeMapper.selectSdkNoticeById(noticeId);
    }

    /**
     * 查询公告管理列表
     *
     * @param sdkNotice 公告管理
     * @return 公告管理
     */
    @Override
    public List<SdkNotice> selectSdkNoticeList(SdkNotice sdkNotice)
    {
        return sdkNoticeMapper.selectSdkNoticeList(sdkNotice);
    }

    /**
     * 新增公告管理
     *
     * @param sdkNotice 公告管理
     * @return 结果
     */
    @Override
    public int insertSdkNotice(SdkNotice sdkNotice)
    {
        sdkNotice.setCreateTime(DateUtils.getNowDate());
        sdkNotice.setCreateBy(ShiroUtils.getLoginName());
        int row = sdkNoticeMapper.insertSdkNotice(sdkNotice);
        if (row > 0){
            hashMap.put(sdkNotice.getNoticeId(),sdkNotice);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 修改公告管理
     *
     * @param sdkNotice 公告管理
     * @return 结果
     */
    @Override
    public int updateSdkNotice(SdkNotice sdkNotice)
    {
        sdkNotice.setUpdateTime(DateUtils.getNowDate());
        sdkNotice.setUpdateBy(ShiroUtils.getLoginName());
        int row = sdkNoticeMapper.updateSdkNotice(sdkNotice);
        if (row > 0){
            hashMap.put(sdkNotice.getNoticeId(),sdkNotice);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除公告管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSdkNoticeByIds(String ids)
    {
        String[] noticeIds = Convert.toStrArray(ids);
        int row = sdkNoticeMapper.deleteSdkNoticeByIds(noticeIds);
        if (row > 0){
            for (String noticeId:noticeIds){
                hashMap.remove(Long.valueOf(noticeId));
            }
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 删除公告管理信息
     *
     * @param noticeId 公告管理ID
     * @return 结果
     */
    @Override
    public int deleteSdkNoticeById(Long noticeId)
    {
        int row = sdkNoticeMapper.deleteSdkNoticeById(noticeId);
        if (row > 0){
            hashMap.remove(noticeId);
            sdkServerService.setServerUpdateTime(System.currentTimeMillis());
        }
        return row;
    }

    /**
     * 查询公告管理列表(通过条件)
     * @param paramMap
     * @return
     */
    @Override
    public List<SdkNotice> selectSdkNoticeListBy(Map paramMap) {
        return sdkNoticeMapper.selectSdkNoticeListBy(paramMap);
    }

    @Override
    public void reloadSdkNotice() {
        init();
    }
}
