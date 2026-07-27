package com.gm.project.gmtool.activity.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.activity.mapper.ActivityMapper;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.service.IActivityService;
import com.gm.common.utils.text.Convert;

/**
 * 运营活动Service业务层处理
 * 
 * @author gm
 * @date 2021-09-07
 */
@Service
public class ActivityServiceImpl implements IActivityService 
{
    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 查询运营活动
     * 
     * @param id 运营活动ID
     * @return 运营活动
     */
    @Override
    public Activity selectActivityById(Integer id)
    {
        return activityMapper.selectActivityById(id);
    }

    /**
     * 查询运营活动列表
     * 
     * @param activity 运营活动
     * @return 运营活动
     */
    @Override
    public List<Activity> selectActivityList(Activity activity)
    {
        return activityMapper.selectActivityList(activity);
    }

    @Override
    public List<Activity> selectActivityByActIds(String actIds) {
        return activityMapper.selectActivityByActIds(actIds);
    }

    /**
     * 新增运营活动
     * 
     * @param activity 运营活动
     * @return 结果
     */
    @Override
    public int insertActivity(Activity activity)
    {
        return activityMapper.insertActivity(activity);
    }

    /**
     * 修改运营活动
     * 
     * @param activity 运营活动
     * @return 结果
     */
    @Override
    public int updateActivity(Activity activity)
    {
        return activityMapper.updateActivity(activity);
    }

    /**
     * 删除运营活动对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteActivityByIds(String ids)
    {
        return activityMapper.deleteActivityByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除运营活动信息
     * 
     * @param id 运营活动ID
     * @return 结果
     */
    @Override
    public int deleteActivityById(Integer id)
    {
        return activityMapper.deleteActivityById(id);
    }
}
