package com.gm.project.gmtool.activityFestivalType.mapper;

import java.util.List;
import com.gm.project.gmtool.activityFestivalType.domain.ActivityFestivalType;

/**
 * Тип праздникаMapper接口
 * 
 * @author gm
 * @date 2021-09-09
 */
public interface ActivityFestivalTypeMapper 
{
    /**
     * 查询Тип праздника
     * 
     * @param id Тип праздникаID
     * @return Тип праздника
     */
    public ActivityFestivalType selectActivityFestivalTypeById(Integer id);

    /**
     * 查询Тип праздника列表
     * 
     * @param activityFestivalType Тип праздника
     * @return Тип праздника集合
     */
    public List<ActivityFestivalType> selectActivityFestivalTypeList(ActivityFestivalType activityFestivalType);

    /**
     * ДобавитьТип праздника
     * 
     * @param activityFestivalType Тип праздника
     * @return Результат
     */
    public int insertActivityFestivalType(ActivityFestivalType activityFestivalType);

    /**
     * ИзменитьТип праздника
     * 
     * @param activityFestivalType Тип праздника
     * @return Результат
     */
    public int updateActivityFestivalType(ActivityFestivalType activityFestivalType);

    /**
     * УдалитьТип праздника
     * 
     * @param id Тип праздникаID
     * @return Результат
     */
    public int deleteActivityFestivalTypeById(Integer id);

    /**
     * 批量УдалитьТип праздника
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteActivityFestivalTypeByIds(String[] ids);

    /**
     * Удалить全部Тип праздника
     * @return
     */
    public int deleteAllActFestivalType();

    /**
     * 根据节日ID列表查询Тип праздникаИнформация
     * @param ids
     * @return
     */
    public List<ActivityFestivalType> selectActivityFestivalTypeByIds(List<Integer> ids);
}
