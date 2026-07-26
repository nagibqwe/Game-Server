package com.gm.project.gmtool.activity.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.activity.mapper.ActivityMapper;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.service.IActivityService;
import com.gm.common.utils.text.Convert;

/**
 * Игровые событияService业务层处理
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
     * 查询Игровые события
     * 
     * @param id Игровые событияID
     * @return Игровые события
     */
    @Override
    public Activity selectActivityById(Integer id)
    {
        return activityMapper.selectActivityById(id);
    }

    /**
     * 查询Игровые события列表
     * 
     * @param activity Игровые события
     * @return Игровые события
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
     * ДобавитьИгровые события
     * 
     * @param activity Игровые события
     * @return Результат
     */
    @Override
    public int insertActivity(Activity activity)
    {
        return activityMapper.insertActivity(activity);
    }

    /**
     * ИзменитьИгровые события
     * 
     * @param activity Игровые события
     * @return Результат
     */
    @Override
    public int updateActivity(Activity activity)
    {
        return activityMapper.updateActivity(activity);
    }

    /**
     * УдалитьИгровые события对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteActivityByIds(String ids)
    {
        return activityMapper.deleteActivityByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьИгровые событияИнформация
     * 
     * @param id Игровые событияID
     * @return Результат
     */
    @Override
    public int deleteActivityById(Integer id)
    {
        return activityMapper.deleteActivityById(id);
    }
}
