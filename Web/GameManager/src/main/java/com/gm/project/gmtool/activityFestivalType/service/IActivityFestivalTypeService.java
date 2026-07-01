package com.gm.project.gmtool.activityFestivalType.service;

import java.util.List;
import com.gm.project.gmtool.activityFestivalType.domain.ActivityFestivalType;

/**
 * 节日类型Service接口
 * 
 * @author gm
 * @date 2021-09-09
 */
public interface IActivityFestivalTypeService 
{
    /**
     * 查询节日类型
     * 
     * @param id 节日类型ID
     * @return 节日类型
     */
    public ActivityFestivalType selectActivityFestivalTypeById(Integer id);

    /**
     * 查询节日类型列表
     * 
     * @param activityFestivalType 节日类型
     * @return 节日类型集合
     */
    public List<ActivityFestivalType> selectActivityFestivalTypeList(ActivityFestivalType activityFestivalType);

    /**
     * 新增节日类型
     * 
     * @param activityFestivalType 节日类型
     * @return 结果
     */
    public int insertActivityFestivalType(ActivityFestivalType activityFestivalType);

    /**
     * 修改节日类型
     * 
     * @param activityFestivalType 节日类型
     * @return 结果
     */
    public int updateActivityFestivalType(ActivityFestivalType activityFestivalType);

    /**
     * 批量删除节日类型
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteActivityFestivalTypeByIds(String ids);

    /**
     * 删除节日类型信息
     * 
     * @param id 节日类型ID
     * @return 结果
     */
    public int deleteActivityFestivalTypeById(Integer id);

    /**
     * 删除全部节日类型
     * @return
     */
    public int deleteAllActFestivalType();

    /**
     * 根据节日ID列表查询节日类型信息
     * @param ids
     * @return
     */
    public List<ActivityFestivalType> selectActivityFestivalTypeByIds(List<Integer> ids);
}
