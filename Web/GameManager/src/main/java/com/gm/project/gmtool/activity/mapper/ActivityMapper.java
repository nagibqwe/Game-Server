package com.gm.project.gmtool.activity.mapper;

import java.util.List;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.server.domain.TServer;
import org.apache.ibatis.annotations.Param;

/**
 * 运营活动Mapper接口
 * 
 * @author gm
 * @date 2021-09-07
 */
public interface ActivityMapper 
{
    /**
     * 查询运营活动
     * 
     * @param id 运营活动ID
     * @return 运营活动
     */
    public Activity selectActivityById(Integer id);

    /**
     * 查询运营活动列表
     * 
     * @param activity 运营活动
     * @return 运营活动集合
     */
    public List<Activity> selectActivityList(Activity activity);

    public List<Activity> selectActivityByActIds(@Param("actIds") String actIds);

    /**
     * 新增运营活动
     * 
     * @param activity 运营活动
     * @return 结果
     */
    public int insertActivity(Activity activity);

    /**
     * 修改运营活动
     * 
     * @param activity 运营活动
     * @return 结果
     */
    public int updateActivity(Activity activity);

    /**
     * 删除运营活动
     * 
     * @param id 运营活动ID
     * @return 结果
     */
    public int deleteActivityById(Integer id);

    /**
     * 批量删除运营活动
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteActivityByIds(String[] ids);
}
