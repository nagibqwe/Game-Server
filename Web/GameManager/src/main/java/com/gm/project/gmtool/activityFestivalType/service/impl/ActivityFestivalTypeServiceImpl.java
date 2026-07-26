package com.gm.project.gmtool.activityFestivalType.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.activityFestivalType.mapper.ActivityFestivalTypeMapper;
import com.gm.project.gmtool.activityFestivalType.domain.ActivityFestivalType;
import com.gm.project.gmtool.activityFestivalType.service.IActivityFestivalTypeService;
import com.gm.common.utils.text.Convert;

/**
 * Тип праздникаService业务层处理
 * 
 * @author gm
 * @date 2021-09-09
 */
@Service
public class ActivityFestivalTypeServiceImpl implements IActivityFestivalTypeService 
{
    @Autowired
    private ActivityFestivalTypeMapper activityFestivalTypeMapper;

    /**
     * 查询Тип праздника
     * 
     * @param id Тип праздникаID
     * @return Тип праздника
     */
    @Override
    public ActivityFestivalType selectActivityFestivalTypeById(Integer id)
    {
        return activityFestivalTypeMapper.selectActivityFestivalTypeById(id);
    }

    /**
     * 查询Тип праздника列表
     * 
     * @param activityFestivalType Тип праздника
     * @return Тип праздника
     */
    @Override
    public List<ActivityFestivalType> selectActivityFestivalTypeList(ActivityFestivalType activityFestivalType)
    {
        return activityFestivalTypeMapper.selectActivityFestivalTypeList(activityFestivalType);
    }

    /**
     * ДобавитьТип праздника
     * 
     * @param activityFestivalType Тип праздника
     * @return Результат
     */
    @Override
    public int insertActivityFestivalType(ActivityFestivalType activityFestivalType)
    {
        return activityFestivalTypeMapper.insertActivityFestivalType(activityFestivalType);
    }

    /**
     * ИзменитьТип праздника
     * 
     * @param activityFestivalType Тип праздника
     * @return Результат
     */
    @Override
    public int updateActivityFestivalType(ActivityFestivalType activityFestivalType)
    {
        return activityFestivalTypeMapper.updateActivityFestivalType(activityFestivalType);
    }

    /**
     * УдалитьТип праздника对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteActivityFestivalTypeByIds(String ids)
    {
        return activityFestivalTypeMapper.deleteActivityFestivalTypeByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьТип праздникаИнформация
     * 
     * @param id Тип праздникаID
     * @return Результат
     */
    @Override
    public int deleteActivityFestivalTypeById(Integer id)
    {
        return activityFestivalTypeMapper.deleteActivityFestivalTypeById(id);
    }

    /**
     * Удалить全部Тип праздника
     * @return
     */
    @Override
    public int deleteAllActFestivalType() {
        return activityFestivalTypeMapper.deleteAllActFestivalType();
    }

    /**
     * 根据节日ID列表查询Тип праздникаИнформация
     * @param ids
     * @return
     */
    @Override
    public List<ActivityFestivalType> selectActivityFestivalTypeByIds(List<Integer> ids) {
        return activityFestivalTypeMapper.selectActivityFestivalTypeByIds(ids);
    }
}
