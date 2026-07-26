package com.gm.project.gmtool.activity.service;

import java.util.List;
import com.gm.project.gmtool.activity.domain.Activity;

/**
 * Игровые событияService接口
 * 
 * @author gm
 * @date 2021-09-07
 */
public interface IActivityService 
{
    /**
     * 查询Игровые события
     * 
     * @param id Игровые событияID
     * @return Игровые события
     */
    public Activity selectActivityById(Integer id);

    /**
     * 查询Игровые события列表
     * 
     * @param activity Игровые события
     * @return Игровые события集合
     */
    public List<Activity> selectActivityList(Activity activity);

    public List<Activity> selectActivityByActIds(String actIds);

    /**
     * ДобавитьИгровые события
     * 
     * @param activity Игровые события
     * @return Результат
     */
    public int insertActivity(Activity activity);

    /**
     * ИзменитьИгровые события
     * 
     * @param activity Игровые события
     * @return Результат
     */
    public int updateActivity(Activity activity);

    /**
     * 批量УдалитьИгровые события
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteActivityByIds(String ids);

    /**
     * УдалитьИгровые событияИнформация
     * 
     * @param id Игровые событияID
     * @return Результат
     */
    public int deleteActivityById(Integer id);
}
