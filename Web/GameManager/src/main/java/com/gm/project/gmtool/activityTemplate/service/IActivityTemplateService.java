package com.gm.project.gmtool.activityTemplate.service;

import java.util.List;
import com.gm.project.gmtool.activityTemplate.domain.ActivityTemplate;

/**
 * 运营活动模板Service接口
 * 
 * @author gm
 * @date 2021-09-07
 */
public interface IActivityTemplateService 
{
    /**
     * 查询运营活动模板
     * 
     * @param id 运营活动模板ID
     * @return 运营活动模板
     */
    public ActivityTemplate selectActivityTemplateById(Integer id);

    /**
     * 查询运营活动模板列表
     * 
     * @param activityTemplate 运营活动模板
     * @return 运营活动模板集合
     */
    public List<ActivityTemplate> selectActivityTemplateList(ActivityTemplate activityTemplate);

    /**
     * 新增运营活动模板
     * 
     * @param activityTemplate 运营活动模板
     * @return 结果
     */
    public int insertActivityTemplate(ActivityTemplate activityTemplate);

    /**
     * 修改运营活动模板
     * 
     * @param activityTemplate 运营活动模板
     * @return 结果
     */
    public int updateActivityTemplate(ActivityTemplate activityTemplate);

    /**
     * 批量删除运营活动模板
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteActivityTemplateByIds(String ids);

    /**
     * 删除运营活动模板信息
     * 
     * @param id 运营活动模板ID
     * @return 结果
     */
    public int deleteActivityTemplateById(Integer id);

    public List<ActivityTemplate> selectActivityTemplateByIds(String ids);
}
