package com.game.commercialize.struct;

import com.data.CfgManager;
import com.data.bean.Cfg_RechargeAward_Bean;
import game.core.util.TimeUtils;

/**
 * @explain: desc
 * @time Created on 2019/10/30 15:56.
 * @author: tc
 */
public class Charge {
	// 当前激活的配置ID
	private int cfgID;

	// 当前累计充值的元宝
	private long gold;

	// 本轮开启时间
	private long startTime;

	// 是否领奖
	private boolean isReward;

	public static Charge New(int typ) {
		Charge fcCharge = new Charge();

		// 排好序的
		int cfgID = -1;
		for (Cfg_RechargeAward_Bean bean : CfgManager.getCfg_RechargeAward_Container().getValuees()) {
			if (bean.getAwardType() == typ) {
				cfgID = bean.getId();
				break;
			}
		}

		fcCharge.setCfgID(cfgID);
		fcCharge.setStartTime(TimeUtils.Time());
		fcCharge.setReward(false);
		fcCharge.setGold(0);
		return fcCharge;
	}

	public int getCfgID() {
		return cfgID;
	}

	public void setCfgID(int cfgID) {
		this.cfgID = cfgID;
	}

	public long getGold() {
		return gold;
	}

	public void setGold(long gold) {
		this.gold = gold;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public boolean isReward() {
		return isReward;
	}

	public void setReward(boolean reward) {
		isReward = reward;
	}
}
