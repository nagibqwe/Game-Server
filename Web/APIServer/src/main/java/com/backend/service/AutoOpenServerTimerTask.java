package com.backend.service;

import com.backend.manager.AutoOpenServerManager;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.text.SimpleDateFormat;
import java.util.TimerTask;

/**
 * 自动开服的计时发送处理
 */
public class AutoOpenServerTimerTask extends TimerTask{

	private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final Log log = Logs.get();

	private int interval;

	AutoOpenServerTimerTask(int interval) {
		this.interval = interval;
	}
	
	@Override
	public void run() {
//		long now = System.currentTimeMillis();
//		int step = interval * 60 * 1000;

		AutoOpenServerManager.getInstance().checkServerOpen();

	}

}
