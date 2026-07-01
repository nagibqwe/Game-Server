package com.backend.service;

import java.util.Timer;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(name = "taskTimer")
public class TaskTimerService {

	@Inject("refer:$ioc")
	protected Ioc ioc;

	public void StartAnnounceTask(int key) {
		if( key < 1) {
			return;
		}
		Timer tt = new Timer("announce-timer" + key);
		tt.schedule(new AnnounceTimerTask(key), 1, key * 60 * 1000);
	}

}
