package com.gm.project.gmtool.activityFestivalRelation.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.activityFestivalRelation.mapper.ActivityFestivalRelationMapper;
import com.gm.project.gmtool.activityFestivalRelation.domain.ActivityFestivalRelation;
import com.gm.project.gmtool.activityFestivalRelation.service.IActivityFestivalRelationService;
import com.gm.common.utils.text.Convert;

/**
 * Связи праздничных событийService业务层处理
 * 
 * @author gm
 * @date 2021-11-08
 */
@Service
public class ActivityFestivalRelationServiceImpl implements IActivityFestivalRelationService 
{
    @Autowired
    private ActivityFestivalRelationMapper activityFestivalRelationMapper;

    /**
     * 查询Связи праздничных событий
     * 
     * @param logicId Связи праздничных событийID
     * @return Связи праздничных событий
     */
    @Override
    public ActivityFestivalRelation selectActivityFestivalRelationById(Integer logicId)
    {
        return activityFestivalRelationMapper.selectActivityFestivalRelationById(logicId);
    }

    /**
     * 查询Связи праздничных событий列表
     * 
     * @param activityFestivalRelation Связи праздничных событий
     * @return Связи праздничных событий
     */
    @Override
    public List<ActivityFestivalRelation> selectActivityFestivalRelationList(ActivityFestivalRelation activityFestivalRelation)
    {
        return activityFestivalRelationMapper.selectActivityFestivalRelationList(activityFestivalRelation);
    }

    /**
     * ДобавитьСвязи праздничных событий
     * 
     * @param activityFestivalRelation Связи праздничных событий
     * @return Результат
     */
    @Override
    public int insertActivityFestivalRelation(ActivityFestivalRelation activityFestivalRelation)
    {
        return activityFestivalRelationMapper.insertActivityFestivalRelation(activityFestivalRelation);
    }

    /**
     * ИзменитьСвязи праздничных событий
     * 
     * @param activityFestivalRelation Связи праздничных событий
     * @return Результат
     */
    @Override
    public int updateActivityFestivalRelation(ActivityFestivalRelation activityFestivalRelation)
    {
        return activityFestivalRelationMapper.updateActivityFestivalRelation(activityFestivalRelation);
    }

    /**
     * УдалитьСвязи праздничных событий对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteActivityFestivalRelationByIds(String ids)
    {
        return activityFestivalRelationMapper.deleteActivityFestivalRelationByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьСвязи праздничных событийИнформация
     * 
     * @param logicId Связи праздничных событийID
     * @return Результат
     */
    @Override
    public int deleteActivityFestivalRelationById(Integer logicId)
    {
        return activityFestivalRelationMapper.deleteActivityFestivalRelationById(logicId);
    }

    /**
     * Удалить全部Связи праздничных событий
     * @return
     */
    @Override
    public int deleteAllActFestivalRelation() {
        return activityFestivalRelationMapper.deleteAllActFestivalRelation();
    }

    /**
     * 根据活动type的ID获取Тип праздникаID列表
     * @param logicId
     * @return
     */
    @Override
    public List<Integer> selectActFestivalRelationByLogicId(Integer logicId) {
        return activityFestivalRelationMapper.selectActFestivalRelationByLogicId(logicId);
    }
}
