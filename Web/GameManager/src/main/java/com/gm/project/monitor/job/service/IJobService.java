package com.gm.project.monitor.job.service;

import java.util.List;
import org.quartz.SchedulerException;
import com.gm.common.exception.job.TaskException;
import com.gm.project.monitor.job.domain.Job;

/**
 * Планировщик задач调度ИнформацияИнформация 服务层
 * 
 * @author ruoyi
 */
public interface IJobService
{
    /**
     * 获取quartz调度器的计划任务
     * 
     * @param job 调度Информация
     * @return 调度任务集合
     */
    public List<Job> selectJobList(Job job);

    /**
     * 通过调度任务ID查询调度Информация
     * 
     * @param jobId 调度任务ID
     * @return 调度任务对象Информация
     */
    public Job selectJobById(Long jobId);

    /**
     * 暂停任务
     * 
     * @param job 调度Информация
     * @return Результат
     */
    public int pauseJob(Job job) throws SchedulerException;

    /**
     * 恢复任务
     * 
     * @param job 调度Информация
     * @return Результат
     */
    public int resumeJob(Job job) throws SchedulerException;

    /**
     * Удалить任务后，所对应的trigger也将被Удалить
     * 
     * @param job 调度Информация
     * @return Результат
     */
    public int deleteJob(Job job) throws SchedulerException;

    /**
     * 批量Удалить调度Информация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    public void deleteJobByIds(String ids) throws SchedulerException;

    /**
     * 任务调度СтатусИзменить
     * 
     * @param job 调度Информация
     * @return Результат
     */
    public int changeStatus(Job job) throws SchedulerException;

    /**
     * 立即运行任务
     * 
     * @param job 调度Информация
     * @return Результат
     */
    public void run(Job job) throws SchedulerException;

    /**
     * Добавить任务
     * 
     * @param job 调度Информация
     * @return Результат
     */
    public int insertJob(Job job) throws SchedulerException, TaskException;

    /**
     * 更新任务
     * 
     * @param job 调度Информация
     * @return Результат
     */
    public int updateJob(Job job) throws SchedulerException, TaskException;

    /**
     * 校验cron表达式ДаНет有效
     * 
     * @param cronExpression 表达式
     * @return Результат
     */
    public boolean checkCronExpressionIsValid(String cronExpression);
}