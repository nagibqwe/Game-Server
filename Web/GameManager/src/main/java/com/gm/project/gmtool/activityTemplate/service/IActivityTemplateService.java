package com.gm.project.gmtool.activityTemplate.service;

import java.util.List;
import com.gm.project.gmtool.activityTemplate.domain.ActivityTemplate;

/**
 * Шаблоны событийService接口
 * 
 * @author gm
 * @date 2021-09-07
 */
public interface IActivityTemplateService 
{
    /**
     * 查询Шаблоны событий
     * 
     * @param id Шаблоны событийID
     * @return Шаблоны событий
     */
    public ActivityTemplate selectActivityTemplateById(Integer id);

    /**
     * 查询Шаблоны событий列表
     * 
     * @param activityTemplate Шаблоны событий
     * @return Шаблоны событий集合
     */
    public List<ActivityTemplate> selectActivityTemplateList(ActivityTemplate activityTemplate);

    /**
     * ДобавитьШаблоны событий
     * 
     * @param activityTemplate Шаблоны событий
     * @return Результат
     */
    public int insertActivityTemplate(ActivityTemplate activityTemplate);

    /**
     * ИзменитьШаблоны событий
     * 
     * @param activityTemplate Шаблоны событий
     * @return Результат
     */
    public int updateActivityTemplate(ActivityTemplate activityTemplate);

    /**
     * 批量УдалитьШаблоны событий
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteActivityTemplateByIds(String ids);

    /**
     * УдалитьШаблоны событийИнформация
     * 
     * @param id Шаблоны событийID
     * @return Результат
     */
    public int deleteActivityTemplateById(Integer id);

    public List<ActivityTemplate> selectActivityTemplateByIds(String ids);
}
