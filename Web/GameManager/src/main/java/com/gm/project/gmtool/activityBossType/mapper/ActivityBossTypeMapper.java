package com.gm.project.gmtool.activityBossType.mapper;

import java.util.List;
import com.gm.project.gmtool.activityBossType.domain.ActivityBossType;

/**
 * Тип босса событияMapper接口
 * 
 * @author gm
 * @date 2021-09-14
 */
public interface ActivityBossTypeMapper 
{
    /**
     * 查询Тип босса события
     * 
     * @param id Тип босса событияID
     * @return Тип босса события
     */
    public ActivityBossType selectActivityBossTypeById(Integer id);

    /**
     * 查询Тип босса события列表
     * 
     * @param activityBossType Тип босса события
     * @return Тип босса события集合
     */
    public List<ActivityBossType> selectActivityBossTypeList(ActivityBossType activityBossType);

    /**
     * ДобавитьТип босса события
     * 
     * @param activityBossType Тип босса события
     * @return Результат
     */
    public int insertActivityBossType(ActivityBossType activityBossType);

    /**
     * ИзменитьТип босса события
     * 
     * @param activityBossType Тип босса события
     * @return Результат
     */
    public int updateActivityBossType(ActivityBossType activityBossType);

    /**
     * УдалитьТип босса события
     * 
     * @param id Тип босса событияID
     * @return Результат
     */
    public int deleteActivityBossTypeById(Integer id);

    /**
     * 批量УдалитьТип босса события
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteActivityBossTypeByIds(String[] ids);

    /**
     * Удалить全部的Тип босса события
     * @return
     */
    public int deleteAllActBossType();
}
