package com.gm.project.gmtool.activityFestivalType.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.activityFestivalType.mapper.ActivityFestivalTypeMapper;
import com.gm.project.gmtool.activityFestivalType.domain.ActivityFestivalType;
import com.gm.project.gmtool.activityFestivalType.service.IActivityFestivalTypeService;
import com.gm.common.utils.text.Convert;

/**
 * 节日类型Service业务层处理
 * 
 * @author gm
 * @date 2021-09-09
 */
@Service
public class ActivityFestivalTypeServiceImpl implements IActivityFestivalTypeService 
{
    @Autowired
    private ActivityFestivalTypeMapper activityFestivalTypeMapper;

    /**
     * 查询节日类型
     * 
     * @param id 节日类型ID
     * @return 节日类型
     */
    @Override
    public ActivityFestivalType selectActivityFestivalTypeById(Integer id)
    {
        return activityFestivalTypeMapper.selectActivityFestivalTypeById(id);
    }

    /**
     * 查询节日类型列表
     * 
     * @param activityFestivalType 节日类型
     * @return 节日类型
     */
    @Override
    public List<ActivityFestivalType> selectActivityFestivalTypeList(ActivityFestivalType activityFestivalType)
    {
        return activityFestivalTypeMapper.selectActivityFestivalTypeList(activityFestivalType);
    }

    /**
     * 新增节日类型
     * 
     * @param activityFestivalType 节日类型
     * @return 结果
     */
    @Override
    public int insertActivityFestivalType(ActivityFestivalType activityFestivalType)
    {
        return activityFestivalTypeMapper.insertActivityFestivalType(activityFestivalType);
    }

    /**
     * 修改节日类型
     * 
     * @param activityFestivalType 节日类型
     * @return 结果
     */
    @Override
    public int updateActivityFestivalType(ActivityFestivalType activityFestivalType)
    {
        return activityFestivalTypeMapper.updateActivityFestivalType(activityFestivalType);
    }

    /**
     * 删除节日类型对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteActivityFestivalTypeByIds(String ids)
    {
        return activityFestivalTypeMapper.deleteActivityFestivalTypeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除节日类型信息
     * 
     * @param id 节日类型ID
     * @return 结果
     */
    @Override
    public int deleteActivityFestivalTypeById(Integer id)
    {
        return activityFestivalTypeMapper.deleteActivityFestivalTypeById(id);
    }

    /**
     * 删除全部节日类型
     * @return
     */
    @Override
    public int deleteAllActFestivalType() {
        return activityFestivalTypeMapper.deleteAllActFestivalType();
    }

    /**
     * 根据节日ID列表查询节日类型信息
     * @param ids
     * @return
     */
    @Override
    public List<ActivityFestivalType> selectActivityFestivalTypeByIds(List<Integer> ids) {
        return activityFestivalTypeMapper.selectActivityFestivalTypeByIds(ids);
    }
}
