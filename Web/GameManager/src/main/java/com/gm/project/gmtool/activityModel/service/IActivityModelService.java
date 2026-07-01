package com.gm.project.gmtool.activityModel.service;

import java.util.List;

import com.gm.project.gmtool.activityModel.domain.ActivityModel;

/**
 * 运营活动模型库Service接口
 * 
 * @author gm
 * @date 2021-09-14
 */
public interface IActivityModelService
{
    /**
     * 查询运营活动模型库
     * 
     * @param id 运营活动模型库ID
     * @return 运营活动模型库
     */
    public ActivityModel selectModelById(Integer id);

    /**
     * 查询运营活动模型库列表
     * 
     * @param activityModel 运营活动模型库
     * @return 运营活动模型库集合
     */
    public List<ActivityModel> selectModelList(ActivityModel activityModel);

    /**
     * 新增运营活动模型库
     * 
     * @param activityModel 运营活动模型库
     * @return 结果
     */
    public int insertModel(ActivityModel activityModel);

    /**
     * 修改运营活动模型库
     * 
     * @param activityModel 运营活动模型库
     * @return 结果
     */
    public int updateModel(ActivityModel activityModel);

    /**
     * 批量删除运营活动模型库
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteModelByIds(String ids);

    /**
     * 删除运营活动模型库信息
     * 
     * @param id 运营活动模型库ID
     * @return 结果
     */
    public int deleteModelById(Integer id);
}
