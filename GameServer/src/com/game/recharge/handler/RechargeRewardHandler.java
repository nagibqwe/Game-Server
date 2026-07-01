package com.game.recharge.handler;

import com.game.manager.Manager;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @explain: desc
 * @time Created on 2019/11/21 14:10.
 * @author: tc
 */
public class RechargeRewardHandler implements ICommand {
	private final Logger log = LogManager.getLogger("RechargeManager");

	private String order_id;

	public RechargeRewardHandler(String order_id) {
		this.order_id = order_id;
	}

	/**
	 * 执行命令.
	 */
	@Override
	public void action() {
		try {
			Manager.rechargeManager.deal().Reward(order_id);
		} catch (Exception e) {
			log.error(e, e);
		}
	}
}
