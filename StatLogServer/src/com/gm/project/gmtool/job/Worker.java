package com.gm.project.gmtool.job;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionException;


/**
 * 工作线程
 */
public abstract class Worker implements Runnable {

	private int sleepTime;
	private Logger logger;

	/**
	 * 设置工作线程 sleep时间，单位
	 * @param sleepTime
	 */
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}


	public boolean init()
	{
		logger = Logger.getLogger(Worker.class);
		

		return true;
	}
    //根据间隔 定时执行
//	private StatLoginLogTask statLoginLogTask = new StatLoginLogTask();
//	private StatRechargeTask statRechargeTask = new StatRechargeTask();
//	private StatRoleStateTask statRoleStateTask = new StatRoleStateTask();

	public abstract void execute() throws JobExecutionException;
	private void exeonce () throws Exception 
	{
		this.execute();
//		statLoginLogTask.execute();
//		statRechargeTask.execute();
//		statRoleStateTask.execute();
	}
	@Override
	public void run() {
		
		if(!init())
		{
			return;
		}
		
		while(!isStop)
		{		
			try {
				exeonce();
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				logger.error(e);
			}			
		}
		isStoped = true;
	}
	
	private boolean isStop = false;
	private boolean isStoped = false;
	
	public void stop_wait()
	{
		this.isStop = true;
		while(!isStoped)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
