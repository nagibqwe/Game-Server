package com.game.welfare.struct;

import game.core.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @explain: desc
 * @time Created on 2020/1/6 21:03.
 * @author: tc
 */
public class RetrieveResData {
	/**
	 * 完成日期
	 */
	private long time;
	/**
	 * 活动对应数据
	 */
	private ConcurrentHashMap<Integer, Long> map;
    /**
     * 开放资源找回的功能列表
     */
	private List<Integer> openType = new ArrayList<>();

	/**
	 *昨天已经获得的活跃度
	 */
	private int hasActivePoint;


	/**
	 * vip购买次数
	 */
	private ConcurrentHashMap<Integer, Integer> vipMap;


	/**
	 * VIP等级
	 */
	private int vipLevel;


	public RetrieveResData() {
		this.time = TimeUtils.Time();
		map = new ConcurrentHashMap<>();
		vipMap = new ConcurrentHashMap<>();
		hasActivePoint = 0;
	}

	public RetrieveResData(int vipLevel) {
		this.time = TimeUtils.Time();
		map = new ConcurrentHashMap<>();
		vipMap = new ConcurrentHashMap<>();
		this.vipLevel = vipLevel;
		hasActivePoint = 0;
	}


	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public ConcurrentHashMap<Integer, Long> getMap() {
		return map;
	}

	public void setMap(ConcurrentHashMap<Integer, Long> map) {
		this.map = map;
	}

    public List<Integer> getOpenType() {
        return openType;
    }

    public void setOpenType(List<Integer> openType) {
        this.openType = openType;
    }

	public int getHasActivePoint() {
		return hasActivePoint;
	}

	public void setHasActivePoint(int hasActivePoint) {
		this.hasActivePoint = hasActivePoint;
	}

	public ConcurrentHashMap<Integer, Integer> getVipMap() {
		return vipMap;
	}

	public void setVipMap(ConcurrentHashMap<Integer, Integer> vipMap) {
		this.vipMap = vipMap;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
}
