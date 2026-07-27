package com.gm.project.gmtool.activityFestivalRelation.service;

import java.util.List;
import com.gm.project.gmtool.activityFestivalRelation.domain.ActivityFestivalRelation;

/**
 * 运营活动节日关系Service接口
 * 
 * @author gm
 * @date 2021-11-08
 */
public interface IActivityFestivalRelationService 
{
    /**
     * 查询运营活动节日关系
     * 
     * @param logicId 运营活动节日关系ID
     * @return 运营活动节日关系
     */
    public ActivityFestivalRelation selectActivityFestivalRelationById(Integer logicId);

    /**
     * 查询运营活动节日关系列表
     * 
     * @param activityFestivalRelation 运营活动节日关系
     * @return 运营活动节日关系集合
     */
    public List<ActivityFestivalRelation> selectActivityFestivalRelationList(ActivityFestivalRelation activityFestivalRelation);

    /**
     * 新增运营活动节日关系
     * 
     * @param activityFestivalRelation 运营活动节日关系
     * @return 结果
     */
    public int insertActivityFestivalRelation(ActivityFestivalRelation activityFestivalRelation);

    /**
     * 修改运营活动节日关系
     * 
     * @param activityFestivalRelation 运营活动节日关系
     * @return 结果
     */
    public int updateActivityFestivalRelation(ActivityFestivalRelation activityFestivalRelation);

    /**
     * 批量删除运营活动节日关系
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteActivityFestivalRelationByIds(String ids);

    /**
     * 删除运营活动节日关系信息
     * 
     * @param logicId 运营活动节日关系ID
     * @return 结果
     */
    public int deleteActivityFestivalRelationById(Integer logicId);

    /**
     * 删除全部运营活动节日关系
     * @return
     */
    public int deleteAllActFestivalRelation();

    /**
     * 根据活动type的ID获取节日类型ID列表
     * @param logicId
     * @return
     */
    public List<Integer> selectActFestivalRelationByLogicId(Integer logicId);
}
