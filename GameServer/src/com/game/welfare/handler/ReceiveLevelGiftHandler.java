package com.game.welfare.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.welfare.script.ILevelGiftScript;
import game.core.command.ICommand;
import game.message.WelfareMessage;

/**
 * @explain: desc
 * @time Created on 2019/12/24 18:13.
 * @author: tc
 */
public class ReceiveLevelGiftHandler implements ICommand {
	private Player player;
	private int level;

	public ReceiveLevelGiftHandler(Player player, int level) {
		this.player = player;
		this.level = level;
	}

	/**
	 * 执行命令.
	 */
	@Override
	public void action() {
		ILevelGiftScript script = (ILevelGiftScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.LevelGift);
		if (script != null)
			script.onReqReceiveLevelGift(player, level);
	}
}
