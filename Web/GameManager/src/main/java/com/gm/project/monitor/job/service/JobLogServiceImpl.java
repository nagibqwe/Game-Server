package com.gm.project.monitor.job.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.common.utils.text.Convert;
import com.gm.project.monitor.job.domain.JobLog;
import com.gm.project.monitor.job.mapper.JobLogMapper;

/**
 * Планировщик задачЖурнал заданийИнформация 服务层
 * 
 * @author ruoyi
 */
@Service
public class JobLogServiceImpl implements IJobLogService
{
    @Autowired
    private JobLogMapper jobLogMapper;

    /**
     * 获取quartz调度器Журнал的计划任务
     * 
     * @param jobLog Журнал заданийИнформация
     * @return 调度任务Журнал集合
     */
    @Override
    public List<JobLog> selectJobLogList(JobLog jobLog)
    {
        return jobLogMapper.selectJobLogList(jobLog);
    }

    /**
     * 通过调度任务ЖурналID查询调度Информация
     * 
     * @param jobLogId 调度任务ЖурналID
     * @return 调度任务Журнал对象Информация
     */
    @Override
    public JobLog selectJobLogById(Long jobLogId)
    {
        return jobLogMapper.selectJobLogById(jobLogId);
    }

    /**
     * Добавить任务Журнал
     * 
     * @param jobLog Журнал заданийИнформация
     */
    @Override
    public void addJobLog(JobLog jobLog)
    {
        jobLogMapper.insertJobLog(jobLog);
    }

    /**
     * 批量УдалитьЖурнал заданийИнформация
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteJobLogByIds(String ids)
    {
        return jobLogMapper.deleteJobLogByIds(Convert.toStrArray(ids));
    }

    /**
     * Удалить任务Журнал
     * 
     * @param jobId Журнал заданийID
     */
    @Override
    public int deleteJobLogById(Long jobId)
    {
        return jobLogMapper.deleteJobLogById(jobId);
    }
    
    /**
     * 清空任务Журнал
     */
    @Override
    public void cleanJobLog()
    {
        jobLogMapper.cleanJobLog();
    }
}
