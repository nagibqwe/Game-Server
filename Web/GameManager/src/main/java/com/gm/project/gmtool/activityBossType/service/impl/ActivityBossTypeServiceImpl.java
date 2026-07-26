package com.gm.project.gmtool.activityBossType.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.activityBossType.mapper.ActivityBossTypeMapper;
import com.gm.project.gmtool.activityBossType.domain.ActivityBossType;
import com.gm.project.gmtool.activityBossType.service.IActivityBossTypeService;
import com.gm.common.utils.text.Convert;

/**
 * Тип босса событияService业务层处理
 * 
 * @author gm
 * @date 2021-09-14
 */
@Service
public class ActivityBossTypeServiceImpl implements IActivityBossTypeService 
{
    @Autowired
    private ActivityBossTypeMapper activityBossTypeMapper;

    /**
     * 查询Тип босса события
     * 
     * @param id Тип босса событияID
     * @return Тип босса события
     */
    @Override
    public ActivityBossType selectActivityBossTypeById(Integer id)
    {
        return activityBossTypeMapper.selectActivityBossTypeById(id);
    }

    /**
     * 查询Тип босса события列表
     * 
     * @param activityBossType Тип босса события
     * @return Тип босса события
     */
    @Override
    public List<ActivityBossType> selectActivityBossTypeList(ActivityBossType activityBossType)
    {
        return activityBossTypeMapper.selectActivityBossTypeList(activityBossType);
    }

    /**
     * ДобавитьТип босса события
     * 
     * @param activityBossType Тип босса события
     * @return Результат
     */
    @Override
    public int insertActivityBossType(ActivityBossType activityBossType)
    {
        return activityBossTypeMapper.insertActivityBossType(activityBossType);
    }

    /**
     * ИзменитьТип босса события
     * 
     * @param activityBossType Тип босса события
     * @return Результат
     */
    @Override
    public int updateActivityBossType(ActivityBossType activityBossType)
    {
        return activityBossTypeMapper.updateActivityBossType(activityBossType);
    }

    /**
     * УдалитьТип босса события对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteActivityBossTypeByIds(String ids)
    {
        return activityBossTypeMapper.deleteActivityBossTypeByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьТип босса событияИнформация
     * 
     * @param id Тип босса событияID
     * @return Результат
     */
    @Override
    public int deleteActivityBossTypeById(Integer id)
    {
        return activityBossTypeMapper.deleteActivityBossTypeById(id);
    }

    /**
     * Удалить全部的Тип босса события
     * @return
     */
    @Override
    public int deleteAllActBossType() {
        return activityBossTypeMapper.deleteAllActBossType();
    }
}
