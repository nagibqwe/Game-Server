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
 * Шаблоны событийService业务层处理
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
     * 查询Шаблоны событий
     * 
     * @param id Шаблоны событийID
     * @return Шаблоны событий
     */
    @Override
    public ActivityTemplate selectActivityTemplateById(Integer id)
    {
        return activityTemplateMapper.selectActivityTemplateById(id);
    }

    /**
     * 查询Шаблоны событий列表
     * 
     * @param activityTemplate Шаблоны событий
     * @return Шаблоны событий
     */
    @Override
    public List<ActivityTemplate> selectActivityTemplateList(ActivityTemplate activityTemplate)
    {
        return activityTemplateMapper.selectActivityTemplateList(activityTemplate);
    }

    /**
     * ДобавитьШаблоны событий
     * 
     * @param activityTemplate Шаблоны событий
     * @return Результат
     */
    @Override
    public int insertActivityTemplate(ActivityTemplate activityTemplate)
    {
//        activityTemplate.setCreateTime(DateUtils.getNowDate());
        return activityTemplateMapper.insertActivityTemplate(activityTemplate);
    }

    /**
     * ИзменитьШаблоны событий
     * 
     * @param activityTemplate Шаблоны событий
     * @return Результат
     */
    @Override
    public int updateActivityTemplate(ActivityTemplate activityTemplate)
    {
        return activityTemplateMapper.updateActivityTemplate(activityTemplate);
    }

    /**
     * УдалитьШаблоны событий对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteActivityTemplateByIds(String ids)
    {
        return activityTemplateMapper.deleteActivityTemplateByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьШаблоны событийИнформация
     * 
     * @param id Шаблоны событийID
     * @return Результат
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
