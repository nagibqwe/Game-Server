package game.core.timer;

import game.core.command.ICommand;


/** 
 * 
 * 计时事件接口
 */
public interface ITimerEvent extends ICommand {
	/**
	 * 是否timer时间触发
	 *
	 * @param time
	 * @return
	 */
	 boolean isTimerEventTrigger(long time);
}
