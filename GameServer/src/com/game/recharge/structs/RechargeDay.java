package com.game.recharge.structs;

/**
 * @explain: 今日充值数量
 * @time Created on 2019/11/21 15:32.
 * @author: tc
 */
public class RechargeDay {
	private long time;
	private int money;

	public RechargeDay(long time, int money) {
		this.time = time;
		this.money = money;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
}
