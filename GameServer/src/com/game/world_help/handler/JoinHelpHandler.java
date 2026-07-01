package com.game.world_help.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;

/**
 * @explain: desc
 * @time Created on 2019/12/12 17:15.
 * @author: tc
 */
public class JoinHelpHandler implements ICommand {
	private Player player;
	private int id;

	public JoinHelpHandler(Player player, int id) {
		this.player = player;
		this.id = id;
	}

	/**
	 * 执行命令.
	 */
	@Override
	public void action() {
		Manager.worldHelpManager.getScript().onReqJoinHelp(player, id);
	}
}
