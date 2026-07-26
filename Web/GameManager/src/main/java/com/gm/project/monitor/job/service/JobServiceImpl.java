package com.gm.project.monitor.job.service;

import java.util.List;
import javax.annotation.PostConstruct;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gm.common.constant.ScheduleConstants;
import com.gm.common.exception.job.TaskException;
import com.gm.common.utils.text.Convert;
import com.gm.project.monitor.job.domain.Job;
import com.gm.project.monitor.job.mapper.JobMapper;
import com.gm.project.monitor.job.util.CronUtils;
import com.gm.project.monitor.job.util.ScheduleUtils;

/**
 * Планировщик задач调度Информация 服务层
 * 
 * @author ruoyi
 */
@Service
public class JobServiceImpl implements IJobService
{
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobMapper jobMapper;

    /**
     * 项目启动时，初始化定时器 
     * 主要Да防止手动ИзменитьДанные库导致未同步到Планировщик задач处理（注：不能手动ИзменитьДанные库ID和任务组名，Нет则会导致脏Данные）
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException
    {
        scheduler.clear();
        List<Job> jobList = jobMapper.selectJobAll();
        for (Job job : jobList)
        {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    /**
     * 获取quartz调度器的计划任务列表
     * 
     * @param job 调度Информация
     * @return
     */
    @Override
    public List<Job> selectJobList(Job job)
    {
        return jobMapper.selectJobList(job);
    }

    /**
     * 通过调度任务ID查询调度Информация
     * 
     * @param jobId 调度任务ID
     * @return 调度任务对象Информация
     */
    @Override
    public Job selectJobById(Long jobId)
    {
        return jobMapper.selectJobById(jobId);
    }

    /**
     * 暂停任务
     * 
     * @param job 调度Информация
     */
    @Override
    @Transactional
    public int pauseJob(Job job) throws SchedulerException
    {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.updateJob(job);
        if (rows > 0)
        {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 恢复任务
     * 
     * @param job 调度Информация
     */
    @Override
    @Transactional
    public int resumeJob(Job job) throws SchedulerException
    {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        int rows = jobMapper.updateJob(job);
        if (rows > 0)
        {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * Удалить任务后，所对应的trigger也将被Удалить
     * 
     * @param job 调度Информация
     */
    @Override
    @Transactional
    public int deleteJob(Job job) throws SchedulerException
    {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        int rows = jobMapper.deleteJobById(jobId);
        if (rows > 0)
        {
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 批量Удалить调度Информация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    @Transactional
    public void deleteJobByIds(String ids) throws SchedulerException
    {
        Long[] jobIds = Convert.toLongArray(ids);
        for (Long jobId : jobIds)
        {
            Job job = jobMapper.selectJobById(jobId);
            deleteJob(job);
        }
    }

    /**
     * 任务调度СтатусИзменить
     * 
     * @param job 调度Информация
     */
    @Override
    @Transactional
    public int changeStatus(Job job) throws SchedulerException
    {
        int rows = 0;
        String status = job.getStatus();
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status))
        {
            rows = resumeJob(job);
        }
        else if (ScheduleConstants.Status.PAUSE.getValue().equals(status))
        {
            rows = pauseJob(job);
        }
        return rows;
    }

    /**
     * 立即运行任务
     * 
     * @param job 调度Информация
     */
    @Override
    @Transactional
    public void run(Job job) throws SchedulerException
    {
        Long jobId = job.getJobId();
        Job tmpObj = selectJobById(job.getJobId());
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, tmpObj);
        scheduler.triggerJob(ScheduleUtils.getJobKey(jobId, tmpObj.getJobGroup()), dataMap);
    }

    /**
     * Добавить任务
     * 
     * @param job 调度Информация 调度Информация
     */
    @Override
    @Transactional
    public int insertJob(Job job) throws SchedulerException, TaskException
    {
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.insertJob(job);
        if (rows > 0)
        {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
        return rows;
    }

    /**
     * 更新任务的Время表达式
     * 
     * @param job 调度Информация
     */
    @Override
    @Transactional
    public int updateJob(Job job) throws SchedulerException, TaskException
    {
        Job properties = selectJobById(job.getJobId());
        int rows = jobMapper.updateJob(job);
        if (rows > 0)
        {
            updateSchedulerJob(job, properties.getJobGroup());
        }
        return rows;
    }

    /**
     * 更新任务
     * 
     * @param job 调度Информация
     * @param jobGroup 任务组名
     */
    public void updateSchedulerJob(Job job, String jobGroup) throws SchedulerException, TaskException
    {
        Long jobId = job.getJobId();
        // 判断ДаНет存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey))
        {
            // 防止创建时存在Данные问题 先移除，然后在执行创建Действия
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

    /**
     * 校验cron表达式ДаНет有效
     * 
     * @param cronExpression 表达式
     * @return Результат
     */
    @Override
    public boolean checkCronExpressionIsValid(String cronExpression)
    {
        return CronUtils.isValid(cronExpression);
    }
}