package game.core.timer;

import game.core.command.ICommand;


/** 
 * 
 * 计时事件接口
 */
public interface ITimerEvent extends ICommand {
	//获得剩余时间
	public long remain();
}
