package com.gm.project.gmtool.activity.mapper;

import java.util.List;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.server.domain.TServer;
import org.apache.ibatis.annotations.Param;

/**
 * Игровые событияMapper接口
 * 
 * @author gm
 * @date 2021-09-07
 */
public interface ActivityMapper 
{
    /**
     * 查询Игровые события
     * 
     * @param id Игровые событияID
     * @return Игровые события
     */
    public Activity selectActivityById(Integer id);

    /**
     * 查询Игровые события列表
     * 
     * @param activity Игровые события
     * @return Игровые события集合
     */
    public List<Activity> selectActivityList(Activity activity);

    public List<Activity> selectActivityByActIds(@Param("actIds") String actIds);

    /**
     * ДобавитьИгровые события
     * 
     * @param activity Игровые события
     * @return Результат
     */
    public int insertActivity(Activity activity);

    /**
     * ИзменитьИгровые события
     * 
     * @param activity Игровые события
     * @return Результат
     */
    public int updateActivity(Activity activity);

    /**
     * УдалитьИгровые события
     * 
     * @param id Игровые событияID
     * @return Результат
     */
    public int deleteActivityById(Integer id);

    /**
     * 批量УдалитьИгровые события
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteActivityByIds(String[] ids);
}
