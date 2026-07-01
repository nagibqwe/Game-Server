package com.gm.project.gmtool.service;

import com.gm.project.gmtool.config.StatConfigService;
import com.gm.project.gmtool.job.StatLoginLogTask;
import com.gm.project.gmtool.job.StatRechargeTask;
import com.gm.project.gmtool.job.StatRoleStateTask;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
public class StatService {
    private static Logger logger = Logger.getLogger(StatService.class);
    /**
     * 任务调度器
     */
    private Scheduler scheduler = null;

    public void initialize() {
    }
    private StatService(){}
    private static StatService instance = new StatService();
    public static StatService getInstance(){
        if(null == instance){
            instance = new StatService();
        }
        return instance;
    }
    //private Map<String,Boolean> isRunMap = new ConcurrentHashMap<>();
    public void startService(){
        try {
            // 任务调度器
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            logger.error("添加统计");
            /**================  添加【xxx】统计任务  start ================= **/
            // 各个在线时长区间的在线玩家个数统计（00:30）
           statJob(StatLoginLogTask.class, StatConfigService.getInstance().getValue("stat.login.log.corn"), true );
           statJob(StatRoleStateTask.class, StatConfigService.getInstance().getValue("stat.recharge.log.corn"), true );
           statJob(StatRechargeTask.class, StatConfigService.getInstance().getValue("stat.roleState.log.corn"), true );

        } catch (Exception e){
           logger.error(e.getMessage(), e);
        }
    }
    /**
     * job调度方法
     * @param cls	job任务class
     * @param cornStr	corn时间字符串
     */
    private void statJob(Class<?> cls, String cornStr, boolean isDelay ){
        try {

            JobDetail jobDetail = new JobDetail(cls.getName(), Scheduler.DEFAULT_GROUP, cls);

            CronTrigger trigger = new CronTrigger(cls.getSimpleName(), Scheduler.DEFAULT_GROUP, cornStr);

            Calendar cal = Calendar.getInstance();

            // 延时10秒执行?
            if(isDelay){
                cal.add(Calendar.SECOND, 10);
                trigger.setStartTime(cal.getTime());
            }

            scheduler.scheduleJob(jobDetail, trigger);

        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }
    /**
     * 资源释放
     */
    public void release() {
        try {
            if(scheduler != null){
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
           logger.error(e.getMessage(),e);
        }
    }

//    public Map<String, Boolean> getIsRunMap() {
//        return isRunMap;
//    }
}
