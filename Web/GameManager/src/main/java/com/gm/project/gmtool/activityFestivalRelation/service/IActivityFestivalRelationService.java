package com.gm.project.gmtool.activityFestivalRelation.service;

import java.util.List;
import com.gm.project.gmtool.activityFestivalRelation.domain.ActivityFestivalRelation;

/**
 * Связи праздничных событийService接口
 * 
 * @author gm
 * @date 2021-11-08
 */
public interface IActivityFestivalRelationService 
{
    /**
     * 查询Связи праздничных событий
     * 
     * @param logicId Связи праздничных событийID
     * @return Связи праздничных событий
     */
    public ActivityFestivalRelation selectActivityFestivalRelationById(Integer logicId);

    /**
     * 查询Связи праздничных событий列表
     * 
     * @param activityFestivalRelation Связи праздничных событий
     * @return Связи праздничных событий集合
     */
    public List<ActivityFestivalRelation> selectActivityFestivalRelationList(ActivityFestivalRelation activityFestivalRelation);

    /**
     * ДобавитьСвязи праздничных событий
     * 
     * @param activityFestivalRelation Связи праздничных событий
     * @return Результат
     */
    public int insertActivityFestivalRelation(ActivityFestivalRelation activityFestivalRelation);

    /**
     * ИзменитьСвязи праздничных событий
     * 
     * @param activityFestivalRelation Связи праздничных событий
     * @return Результат
     */
    public int updateActivityFestivalRelation(ActivityFestivalRelation activityFestivalRelation);

    /**
     * 批量УдалитьСвязи праздничных событий
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteActivityFestivalRelationByIds(String ids);

    /**
     * УдалитьСвязи праздничных событийИнформация
     * 
     * @param logicId Связи праздничных событийID
     * @return Результат
     */
    public int deleteActivityFestivalRelationById(Integer logicId);

    /**
     * Удалить全部Связи праздничных событий
     * @return
     */
    public int deleteAllActFestivalRelation();

    /**
     * 根据活动type的ID获取Тип праздникаID列表
     * @param logicId
     * @return
     */
    public List<Integer> selectActFestivalRelationByLogicId(Integer logicId);
}
