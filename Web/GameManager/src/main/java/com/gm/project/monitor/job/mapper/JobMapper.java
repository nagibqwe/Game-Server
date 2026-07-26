package com.gm.project.monitor.job.mapper;

import java.util.List;
import com.gm.project.monitor.job.domain.Job;

/**
 * 调度任务Информация Данные层
 * 
 * @author ruoyi
 */
public interface JobMapper
{
    /**
     * 查询调度任务Журнал集合
     * 
     * @param job 调度Информация
     * @return Журнал операций集合
     */
    public List<Job> selectJobList(Job job);

    /**
     * 查询所有调度任务
     * 
     * @return 调度任务列表
     */
    public List<Job> selectJobAll();

    /**
     * 通过调度ID查询调度任务Информация
     * 
     * @param jobId 调度ID
     * @return 角色对象Информация
     */
    public Job selectJobById(Long jobId);

    /**
     * 通过调度IDУдалить调度任务Информация
     * 
     * @param jobId 调度ID
     * @return Результат
     */
    public int deleteJobById(Long jobId);

    /**
     * 批量Удалить调度任务Информация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public int deleteJobByIds(Long[] ids);

    /**
     * Изменить调度任务Информация
     * 
     * @param job 调度任务Информация
     * @return Результат
     */
    public int updateJob(Job job);

    /**
     * Добавить调度任务Информация
     * 
     * @param job 调度任务Информация
     * @return Результат
     */
    public int insertJob(Job job);
}
