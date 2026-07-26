package com.gm.project.gmtool.activityLuckyValue.service;

import java.util.List;
import com.gm.project.gmtool.activityLuckyValue.domain.ActivityLuckyValue;

/**
 * 抽奖幸运值Service接口
 * 
 * @author gm
 * @date 2021-09-16
 */
public interface IActivityLuckyValueService 
{
    /**
     * 查询抽奖幸运值
     * 
     * @param id 抽奖幸运值ID
     * @return 抽奖幸运值
     */
    public ActivityLuckyValue selectActivityLuckyValueById(Integer id);

    /**
     * 查询抽奖幸运值列表
     * 
     * @param activityLuckyValue 抽奖幸运值
     * @return 抽奖幸运值集合
     */
    public List<ActivityLuckyValue> selectActivityLuckyValueList(ActivityLuckyValue activityLuckyValue);

    /**
     * Добавить抽奖幸运值
     * 
     * @param activityLuckyValue 抽奖幸运值
     * @return Результат
     */
    public int insertActivityLuckyValue(ActivityLuckyValue activityLuckyValue);

    /**
     * Изменить抽奖幸运值
     * 
     * @param activityLuckyValue 抽奖幸运值
     * @return Результат
     */
    public int updateActivityLuckyValue(ActivityLuckyValue activityLuckyValue);

    /**
     * 批量Удалить抽奖幸运值
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteActivityLuckyValueByIds(String ids);

    /**
     * Удалить抽奖幸运值Информация
     * 
     * @param id 抽奖幸运值ID
     * @return Результат
     */
    public int deleteActivityLuckyValueById(Integer id);
}
