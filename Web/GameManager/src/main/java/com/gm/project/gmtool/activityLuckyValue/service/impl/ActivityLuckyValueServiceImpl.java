package com.gm.project.gmtool.activityLuckyValue.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.activityLuckyValue.mapper.ActivityLuckyValueMapper;
import com.gm.project.gmtool.activityLuckyValue.domain.ActivityLuckyValue;
import com.gm.project.gmtool.activityLuckyValue.service.IActivityLuckyValueService;
import com.gm.common.utils.text.Convert;

/**
 * 抽奖幸运值Service业务层处理
 * 
 * @author gm
 * @date 2021-09-16
 */
@Service
public class ActivityLuckyValueServiceImpl implements IActivityLuckyValueService 
{
    @Autowired
    private ActivityLuckyValueMapper activityLuckyValueMapper;

    /**
     * 查询抽奖幸运值
     * 
     * @param id 抽奖幸运值ID
     * @return 抽奖幸运值
     */
    @Override
    public ActivityLuckyValue selectActivityLuckyValueById(Integer id)
    {
        return activityLuckyValueMapper.selectActivityLuckyValueById(id);
    }

    /**
     * 查询抽奖幸运值列表
     * 
     * @param activityLuckyValue 抽奖幸运值
     * @return 抽奖幸运值
     */
    @Override
    public List<ActivityLuckyValue> selectActivityLuckyValueList(ActivityLuckyValue activityLuckyValue)
    {
        return activityLuckyValueMapper.selectActivityLuckyValueList(activityLuckyValue);
    }

    /**
     * Добавить抽奖幸运值
     * 
     * @param activityLuckyValue 抽奖幸运值
     * @return Результат
     */
    @Override
    public int insertActivityLuckyValue(ActivityLuckyValue activityLuckyValue)
    {
        return activityLuckyValueMapper.insertActivityLuckyValue(activityLuckyValue);
    }

    /**
     * Изменить抽奖幸运值
     * 
     * @param activityLuckyValue 抽奖幸运值
     * @return Результат
     */
    @Override
    public int updateActivityLuckyValue(ActivityLuckyValue activityLuckyValue)
    {
        return activityLuckyValueMapper.updateActivityLuckyValue(activityLuckyValue);
    }

    /**
     * Удалить抽奖幸运值对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteActivityLuckyValueByIds(String ids)
    {
        return activityLuckyValueMapper.deleteActivityLuckyValueByIds(Convert.toStrArray(ids));
    }

    /**
     * Удалить抽奖幸运值Информация
     * 
     * @param id 抽奖幸运值ID
     * @return Результат
     */
    @Override
    public int deleteActivityLuckyValueById(Integer id)
    {
        return activityLuckyValueMapper.deleteActivityLuckyValueById(id);
    }
}
