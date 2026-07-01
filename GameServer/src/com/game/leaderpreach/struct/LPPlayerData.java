package com.game.leaderpreach.struct;

import com.game.player.structs.Player;
import game.core.util.TimeUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @explain: desc
 * @time Created on 2019/11/8 16:41.
 * @author: tc
 */
public class LPPlayerData {
	private long playerID;
	// 上一次扣减活跃点的时间
	private long lastDecTime;
	private boolean exit;
	private int ber = 1; // 加成倍数

	private long startTime = 0;

	private long addExp = 0;

	private int  startLevel = 0;

	private int buffID = 0;

	private long decActivePoint=0;//扣除传道活跃点

	/**掌门传道前的经验值*/
	private transient BigInteger beforeExe;

	private List<Integer> rewardList = new ArrayList<>();

	public boolean isExit() {
		return exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}

	public long getLastDecTime() {
		return lastDecTime;
	}

	public void setLastDecTime(long lastDecTime) {
		this.lastDecTime = lastDecTime;
	}

	public long getPlayerID() {
		return playerID;
	}

	public void setPlayerID(long playerID) {
		this.playerID = playerID;
	}



	public int getBer() {
		return ber;
	}

	public void setBer(int ber) {
		this.ber = ber;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getAddExp() {
		return addExp;
	}

	public void setAddExp(long addExp) {
		this.addExp = addExp;
	}

	public int getStartLevel() {
		return startLevel;
	}

	public void setStartLevel(int startLevel) {
		this.startLevel = startLevel;
	}


	public List<Integer> getRewardList() {
		return rewardList;
	}

	public void setRewardList(List<Integer> rewardList) {
		this.rewardList = rewardList;
	}

	public int getBuffID() {
		return buffID;
	}

	public void setBuffID(int buffID) {
		this.buffID = buffID;
	}

	public BigInteger getBeforeExe() {
		return beforeExe;
	}

	public void setBeforeExe(BigInteger beforeExe) {
		this.beforeExe = beforeExe;
	}

	public long getDecActivePoint() {
		return decActivePoint;
	}

	public void setDecActivePoint(long decActivePoint) {
		this.decActivePoint = decActivePoint;
	}

	public LPPlayerData(Player player , int buffID,long decActivePoint) {
		resetData(player ,buffID,decActivePoint);
	}

	public void resetData(Player player, int buffID,long decActivePoint){
		this.playerID = player.getId();
		this.lastDecTime = 0;
		this.exit = false;
		this.ber = 1;
		this.addExp = 0L;
		this.startLevel = player.getLevel();
		this.startTime = TimeUtils.Time();
		this.buffID = buffID;
		this.beforeExe = player.getTotalExp();
		this.decActivePoint = decActivePoint;
		rewardList.clear();
	}

}
