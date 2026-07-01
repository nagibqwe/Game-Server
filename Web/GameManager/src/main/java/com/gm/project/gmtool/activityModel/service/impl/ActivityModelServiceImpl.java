package com.gm.project.gmtool.activityModel.service.impl;

import java.util.List;

import com.gm.project.gmtool.activityModel.domain.ActivityModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.activityModel.mapper.ActivityModelMapper;
import com.gm.project.gmtool.activityModel.service.IActivityModelService;
import com.gm.common.utils.text.Convert;

/**
 * 运营活动模型库Service业务层处理
 * 
 * @author gm
 * @date 2021-09-14
 */
@Service
public class ActivityModelServiceImpl implements IActivityModelService
{
    @Autowired
    private ActivityModelMapper activityModelMapper;

    /**
     * 查询运营活动模型库
     * 
     * @param id 运营活动模型库ID
     * @return 运营活动模型库
     */
    @Override
    public ActivityModel selectModelById(Integer id)
    {
        return activityModelMapper.selectModelById(id);
    }

    /**
     * 查询运营活动模型库列表
     * 
     * @param activityModel 运营活动模型库
     * @return 运营活动模型库
     */
    @Override
    public List<ActivityModel> selectModelList(ActivityModel activityModel)
    {
        return activityModelMapper.selectModelList(activityModel);
    }

    /**
     * 新增运营活动模型库
     * 
     * @param activityModel 运营活动模型库
     * @return 结果
     */
    @Override
    public int insertModel(ActivityModel activityModel)
    {
        return activityModelMapper.insertModel(activityModel);
    }

    /**
     * 修改运营活动模型库
     * 
     * @param activityModel 运营活动模型库
     * @return 结果
     */
    @Override
    public int updateModel(ActivityModel activityModel)
    {
        return activityModelMapper.updateModel(activityModel);
    }

    /**
     * 删除运营活动模型库对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteModelByIds(String ids)
    {
        return activityModelMapper.deleteModelByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除运营活动模型库信息
     * 
     * @param id 运营活动模型库ID
     * @return 结果
     */
    @Override
    public int deleteModelById(Integer id)
    {
        return activityModelMapper.deleteModelById(id);
    }
}
