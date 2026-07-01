package com.gm.project.gmtool.activityBossType.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.activityBossType.mapper.ActivityBossTypeMapper;
import com.gm.project.gmtool.activityBossType.domain.ActivityBossType;
import com.gm.project.gmtool.activityBossType.service.IActivityBossTypeService;
import com.gm.common.utils.text.Convert;

/**
 * 运营活动boss类型Service业务层处理
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
     * 查询运营活动boss类型
     * 
     * @param id 运营活动boss类型ID
     * @return 运营活动boss类型
     */
    @Override
    public ActivityBossType selectActivityBossTypeById(Integer id)
    {
        return activityBossTypeMapper.selectActivityBossTypeById(id);
    }

    /**
     * 查询运营活动boss类型列表
     * 
     * @param activityBossType 运营活动boss类型
     * @return 运营活动boss类型
     */
    @Override
    public List<ActivityBossType> selectActivityBossTypeList(ActivityBossType activityBossType)
    {
        return activityBossTypeMapper.selectActivityBossTypeList(activityBossType);
    }

    /**
     * 新增运营活动boss类型
     * 
     * @param activityBossType 运营活动boss类型
     * @return 结果
     */
    @Override
    public int insertActivityBossType(ActivityBossType activityBossType)
    {
        return activityBossTypeMapper.insertActivityBossType(activityBossType);
    }

    /**
     * 修改运营活动boss类型
     * 
     * @param activityBossType 运营活动boss类型
     * @return 结果
     */
    @Override
    public int updateActivityBossType(ActivityBossType activityBossType)
    {
        return activityBossTypeMapper.updateActivityBossType(activityBossType);
    }

    /**
     * 删除运营活动boss类型对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteActivityBossTypeByIds(String ids)
    {
        return activityBossTypeMapper.deleteActivityBossTypeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除运营活动boss类型信息
     * 
     * @param id 运营活动boss类型ID
     * @return 结果
     */
    @Override
    public int deleteActivityBossTypeById(Integer id)
    {
        return activityBossTypeMapper.deleteActivityBossTypeById(id);
    }

    /**
     * 删除全部的运营活动boss类型
     * @return
     */
    @Override
    public int deleteAllActBossType() {
        return activityBossTypeMapper.deleteAllActBossType();
    }
}
