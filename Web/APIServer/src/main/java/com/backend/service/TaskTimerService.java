package com.backend.service;

import org.apache.log4j.Logger;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.Timer;

@IocBean(name = "taskTimer")
public class TaskTimerService {

	@Inject("refer:$ioc")
	protected Ioc ioc;

	public void startAutoOpenServerTask(int key) {
		if( key < 1) {
			return;
		}
		Timer tt = new Timer("AutoOpenServer-timer" + key);
		tt.schedule(new AutoOpenServerTimerTask(key), 1, key * 60 * 1000);
	}

}
