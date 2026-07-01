package com.gm.project.gmtool.activityBossType.mapper;

import java.util.List;
import com.gm.project.gmtool.activityBossType.domain.ActivityBossType;

/**
 * 运营活动boss类型Mapper接口
 * 
 * @author gm
 * @date 2021-09-14
 */
public interface ActivityBossTypeMapper 
{
    /**
     * 查询运营活动boss类型
     * 
     * @param id 运营活动boss类型ID
     * @return 运营活动boss类型
     */
    public ActivityBossType selectActivityBossTypeById(Integer id);

    /**
     * 查询运营活动boss类型列表
     * 
     * @param activityBossType 运营活动boss类型
     * @return 运营活动boss类型集合
     */
    public List<ActivityBossType> selectActivityBossTypeList(ActivityBossType activityBossType);

    /**
     * 新增运营活动boss类型
     * 
     * @param activityBossType 运营活动boss类型
     * @return 结果
     */
    public int insertActivityBossType(ActivityBossType activityBossType);

    /**
     * 修改运营活动boss类型
     * 
     * @param activityBossType 运营活动boss类型
     * @return 结果
     */
    public int updateActivityBossType(ActivityBossType activityBossType);

    /**
     * 删除运营活动boss类型
     * 
     * @param id 运营活动boss类型ID
     * @return 结果
     */
    public int deleteActivityBossTypeById(Integer id);

    /**
     * 批量删除运营活动boss类型
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteActivityBossTypeByIds(String[] ids);

    /**
     * 删除全部的运营活动boss类型
     * @return
     */
    public int deleteAllActBossType();
}
