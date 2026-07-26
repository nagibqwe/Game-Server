package com.gm.project.gmtool.activityModel.service;

import java.util.List;

import com.gm.project.gmtool.activityModel.domain.ActivityModel;

/**
 * Библиотека моделей событийService接口
 * 
 * @author gm
 * @date 2021-09-14
 */
public interface IActivityModelService
{
    /**
     * 查询Библиотека моделей событий
     * 
     * @param id Библиотека моделей событийID
     * @return Библиотека моделей событий
     */
    public ActivityModel selectModelById(Integer id);

    /**
     * 查询Библиотека моделей событий列表
     * 
     * @param activityModel Библиотека моделей событий
     * @return Библиотека моделей событий集合
     */
    public List<ActivityModel> selectModelList(ActivityModel activityModel);

    /**
     * ДобавитьБиблиотека моделей событий
     * 
     * @param activityModel Библиотека моделей событий
     * @return Результат
     */
    public int insertModel(ActivityModel activityModel);

    /**
     * ИзменитьБиблиотека моделей событий
     * 
     * @param activityModel Библиотека моделей событий
     * @return Результат
     */
    public int updateModel(ActivityModel activityModel);

    /**
     * 批量УдалитьБиблиотека моделей событий
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteModelByIds(String ids);

    /**
     * УдалитьБиблиотека моделей событийИнформация
     * 
     * @param id Библиотека моделей событийID
     * @return Результат
     */
    public int deleteModelById(Integer id);
}
