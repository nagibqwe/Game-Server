package com.gm.project.monitor.job.mapper;

import java.util.List;
import com.gm.project.monitor.job.domain.JobLog;

/**
 * 调度任务ЖурналИнформация Данные层
 * 
 * @author ruoyi
 */
public interface JobLogMapper
{
    /**
     * 获取quartz调度器Журнал的计划任务
     * 
     * @param jobLog Журнал заданийИнформация
     * @return 调度任务Журнал集合
     */
    public List<JobLog> selectJobLogList(JobLog jobLog);
    
    /**
     * 查询所有调度任务Журнал
     *
     * @return 调度任务Журнал列表
     */
    public List<JobLog> selectJobLogAll();

    /**
     * 通过调度任务ЖурналID查询调度Информация
     * 
     * @param jobLogId 调度任务ЖурналID
     * @return 调度任务Журнал对象Информация
     */
    public JobLog selectJobLogById(Long jobLogId);

    /**
     * Добавить任务Журнал
     * 
     * @param jobLog Журнал заданийИнформация
     * @return Результат
     */
    public int insertJobLog(JobLog jobLog);

    /**
     * 批量УдалитьЖурнал заданийИнформация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteJobLogByIds(String[] ids);

    /**
     * Удалить任务Журнал
     * 
     * @param jobId Журнал заданийID
     * @return Результат
     */
    public int deleteJobLogById(Long jobId);
    
    /**
     * 清空任务Журнал
     */
    public void cleanJobLog();
}
