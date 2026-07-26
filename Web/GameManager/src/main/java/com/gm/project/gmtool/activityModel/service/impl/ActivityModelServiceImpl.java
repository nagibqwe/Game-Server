package com.gm.project.gmtool.activityModel.service.impl;

import java.util.List;

import com.gm.project.gmtool.activityModel.domain.ActivityModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.activityModel.mapper.ActivityModelMapper;
import com.gm.project.gmtool.activityModel.service.IActivityModelService;
import com.gm.common.utils.text.Convert;

/**
 * Библиотека моделей событийService业务层处理
 * 
 * @author gm
 * @date 2021-09-14
 */
@Service
public class ActivityModelServiceImpl implements IActivityModelService
{
    @Autowired
    private ActivityModelMapper activityModelMapper;

    /**
     * 查询Библиотека моделей событий
     * 
     * @param id Библиотека моделей событийID
     * @return Библиотека моделей событий
     */
    @Override
    public ActivityModel selectModelById(Integer id)
    {
        return activityModelMapper.selectModelById(id);
    }

    /**
     * 查询Библиотека моделей событий列表
     * 
     * @param activityModel Библиотека моделей событий
     * @return Библиотека моделей событий
     */
    @Override
    public List<ActivityModel> selectModelList(ActivityModel activityModel)
    {
        return activityModelMapper.selectModelList(activityModel);
    }

    /**
     * ДобавитьБиблиотека моделей событий
     * 
     * @param activityModel Библиотека моделей событий
     * @return Результат
     */
    @Override
    public int insertModel(ActivityModel activityModel)
    {
        return activityModelMapper.insertModel(activityModel);
    }

    /**
     * ИзменитьБиблиотека моделей событий
     * 
     * @param activityModel Библиотека моделей событий
     * @return Результат
     */
    @Override
    public int updateModel(ActivityModel activityModel)
    {
        return activityModelMapper.updateModel(activityModel);
    }

    /**
     * УдалитьБиблиотека моделей событий对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteModelByIds(String ids)
    {
        return activityModelMapper.deleteModelByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьБиблиотека моделей событийИнформация
     * 
     * @param id Библиотека моделей событийID
     * @return Результат
     */
    @Override
    public int deleteModelById(Integer id)
    {
        return activityModelMapper.deleteModelById(id);
    }
}
