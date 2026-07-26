package com.gm.project.gmtool.activityFestivalRelation.mapper;

import java.util.List;
import com.gm.project.gmtool.activityFestivalRelation.domain.ActivityFestivalRelation;

/**
 * Связи праздничных событийMapper接口
 * 
 * @author gm
 * @date 2021-11-08
 */
public interface ActivityFestivalRelationMapper 
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
     * УдалитьСвязи праздничных событий
     * 
     * @param logicId Связи праздничных событийID
     * @return Результат
     */
    public int deleteActivityFestivalRelationById(Integer logicId);

    /**
     * 批量УдалитьСвязи праздничных событий
     * 
     * @param logicIds 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteActivityFestivalRelationByIds(String[] logicIds);

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
