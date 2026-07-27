package com.gm.project.gmtool.activityTemplate.service.impl;

import java.util.List;
import com.gm.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.activityTemplate.mapper.ActivityTemplateMapper;
import com.gm.project.gmtool.activityTemplate.domain.ActivityTemplate;
import com.gm.project.gmtool.activityTemplate.service.IActivityTemplateService;
import com.gm.common.utils.text.Convert;

/**
 * 运营活动模板Service业务层处理
 * 
 * @author gm
 * @date 2021-09-07
 */
@Service
public class ActivityTemplateServiceImpl implements IActivityTemplateService 
{
    @Autowired
    private ActivityTemplateMapper activityTemplateMapper;

    /**
     * 查询运营活动模板
     * 
     * @param id 运营活动模板ID
     * @return 运营活动模板
     */
    @Override
    public ActivityTemplate selectActivityTemplateById(Integer id)
    {
        return activityTemplateMapper.selectActivityTemplateById(id);
    }

    /**
     * 查询运营活动模板列表
     * 
     * @param activityTemplate 运营活动模板
     * @return 运营活动模板
     */
    @Override
    public List<ActivityTemplate> selectActivityTemplateList(ActivityTemplate activityTemplate)
    {
        return activityTemplateMapper.selectActivityTemplateList(activityTemplate);
    }

    /**
     * 新增运营活动模板
     * 
     * @param activityTemplate 运营活动模板
     * @return 结果
     */
    @Override
    public int insertActivityTemplate(ActivityTemplate activityTemplate)
    {
//        activityTemplate.setCreateTime(DateUtils.getNowDate());
        return activityTemplateMapper.insertActivityTemplate(activityTemplate);
    }

    /**
     * 修改运营活动模板
     * 
     * @param activityTemplate 运营活动模板
     * @return 结果
     */
    @Override
    public int updateActivityTemplate(ActivityTemplate activityTemplate)
    {
        return activityTemplateMapper.updateActivityTemplate(activityTemplate);
    }

    /**
     * 删除运营活动模板对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteActivityTemplateByIds(String ids)
    {
        return activityTemplateMapper.deleteActivityTemplateByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除运营活动模板信息
     * 
     * @param id 运营活动模板ID
     * @return 结果
     */
    @Override
    public int deleteActivityTemplateById(Integer id)
    {
        return activityTemplateMapper.deleteActivityTemplateById(id);
    }

    @Override
    public List<ActivityTemplate> selectActivityTemplateByIds(String ids) {
        return activityTemplateMapper.selectActivityTemplateByIds(Convert.toStrArray(ids));
    }
}
