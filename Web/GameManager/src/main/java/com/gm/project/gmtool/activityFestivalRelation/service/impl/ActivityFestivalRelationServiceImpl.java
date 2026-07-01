package com.gm.project.gmtool.activityFestivalRelation.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.activityFestivalRelation.mapper.ActivityFestivalRelationMapper;
import com.gm.project.gmtool.activityFestivalRelation.domain.ActivityFestivalRelation;
import com.gm.project.gmtool.activityFestivalRelation.service.IActivityFestivalRelationService;
import com.gm.common.utils.text.Convert;

/**
 * 运营活动节日关系Service业务层处理
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
     * 查询运营活动节日关系
     * 
     * @param logicId 运营活动节日关系ID
     * @return 运营活动节日关系
     */
    @Override
    public ActivityFestivalRelation selectActivityFestivalRelationById(Integer logicId)
    {
        return activityFestivalRelationMapper.selectActivityFestivalRelationById(logicId);
    }

    /**
     * 查询运营活动节日关系列表
     * 
     * @param activityFestivalRelation 运营活动节日关系
     * @return 运营活动节日关系
     */
    @Override
    public List<ActivityFestivalRelation> selectActivityFestivalRelationList(ActivityFestivalRelation activityFestivalRelation)
    {
        return activityFestivalRelationMapper.selectActivityFestivalRelationList(activityFestivalRelation);
    }

    /**
     * 新增运营活动节日关系
     * 
     * @param activityFestivalRelation 运营活动节日关系
     * @return 结果
     */
    @Override
    public int insertActivityFestivalRelation(ActivityFestivalRelation activityFestivalRelation)
    {
        return activityFestivalRelationMapper.insertActivityFestivalRelation(activityFestivalRelation);
    }

    /**
     * 修改运营活动节日关系
     * 
     * @param activityFestivalRelation 运营活动节日关系
     * @return 结果
     */
    @Override
    public int updateActivityFestivalRelation(ActivityFestivalRelation activityFestivalRelation)
    {
        return activityFestivalRelationMapper.updateActivityFestivalRelation(activityFestivalRelation);
    }

    /**
     * 删除运营活动节日关系对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteActivityFestivalRelationByIds(String ids)
    {
        return activityFestivalRelationMapper.deleteActivityFestivalRelationByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除运营活动节日关系信息
     * 
     * @param logicId 运营活动节日关系ID
     * @return 结果
     */
    @Override
    public int deleteActivityFestivalRelationById(Integer logicId)
    {
        return activityFestivalRelationMapper.deleteActivityFestivalRelationById(logicId);
    }

    /**
     * 删除全部运营活动节日关系
     * @return
     */
    @Override
    public int deleteAllActFestivalRelation() {
        return activityFestivalRelationMapper.deleteAllActFestivalRelation();
    }

    /**
     * 根据活动type的ID获取节日类型ID列表
     * @param logicId
     * @return
     */
    @Override
    public List<Integer> selectActFestivalRelationByLogicId(Integer logicId) {
        return activityFestivalRelationMapper.selectActFestivalRelationByLogicId(logicId);
    }
}
