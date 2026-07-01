package com.game.recharge.handler;

import com.game.manager.Manager;
import com.game.recharge.manager.RechargeServer;
import com.game.recharge.structs.Recharge;
import game.core.command.ICommand;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerRechargeHandler implements ICommand {
	private final Logger log = LogManager.getLogger("RechargeManager");

	private Channel session;
	private Recharge recharge;
	private String data;
	private Byte src;

	public PlayerRechargeHandler(Channel session, Recharge recharge, String data, Byte src) {
		this.session = session;
		this.recharge = recharge;
		this.data = data;
		this.src = src;
	}

	@Override
	public void action() {
		try {
			int result = Manager.rechargeManager.deal().Recharge(recharge, data, src);
			if (session != null)
				RechargeServer.writeResponse(session, result);
		} catch (Exception e) {
			RechargeServer.writeResponse(session, 0);
			log.error(e, e);
		}
	}
}
