package com.gm.project.gmtool.updateNotice.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.updateNotice.mapper.UpdateNoticeMapper;
import com.gm.project.gmtool.updateNotice.domain.UpdateNotice;
import com.gm.project.gmtool.updateNotice.service.IUpdateNoticeService;
import com.gm.common.utils.text.Convert;

/**
 * 更新公告Service业务层处理
 * 
 * @author gm
 * @date 2021-10-30
 */
@Service
public class UpdateNoticeServiceImpl implements IUpdateNoticeService 
{
    @Autowired
    private UpdateNoticeMapper updateNoticeMapper;

    /**
     * 查询更新公告
     * 
     * @param id 更新公告ID
     * @return 更新公告
     */
    @Override
    public UpdateNotice selectUpdateNoticeById(Integer id)
    {
        return updateNoticeMapper.selectUpdateNoticeById(id);
    }

    /**
     * 查询更新公告列表
     * 
     * @param updateNotice 更新公告
     * @return 更新公告
     */
    @Override
    public List<UpdateNotice> selectUpdateNoticeList(UpdateNotice updateNotice)
    {
        return updateNoticeMapper.selectUpdateNoticeList(updateNotice);
    }

    /**
     * 新增更新公告
     * 
     * @param updateNotice 更新公告
     * @return 结果
     */
    @Override
    public int insertUpdateNotice(UpdateNotice updateNotice)
    {
        return updateNoticeMapper.insertUpdateNotice(updateNotice);
    }

    /**
     * 修改更新公告
     * 
     * @param updateNotice 更新公告
     * @return 结果
     */
    @Override
    public int updateUpdateNotice(UpdateNotice updateNotice)
    {
        return updateNoticeMapper.updateUpdateNotice(updateNotice);
    }

    /**
     * 删除更新公告对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteUpdateNoticeByIds(String ids)
    {
        return updateNoticeMapper.deleteUpdateNoticeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除更新公告信息
     * 
     * @param id 更新公告ID
     * @return 结果
     */
    @Override
    public int deleteUpdateNoticeById(Integer id)
    {
        return updateNoticeMapper.deleteUpdateNoticeById(id);
    }
}
