package com.game.bi.timer;

import com.game.bi.manager.BIManager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @explain: desc
 * @time Created on 2019/12/31 18:16.
 * @author: tc
 */
public class BITimer extends TimerEvent {
	private final Logger log = LogManager.getLogger(BITimer.class);

	public BITimer() {
		super(-1, 0, 1000);
	}

	/**
	 * 执行命令.
	 */
	@Override
	public void action() {
		try {
			BIManager.getInstance().getScript().save();
		} catch (Exception e) {
			log.error(e, e);
		}
	}
}
