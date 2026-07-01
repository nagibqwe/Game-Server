package com.game.chum.struct;

import game.core.util.TimeUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @explain: desc
 * @time Created on 2019/10/31 16:32.
 * @author: tc
 */
public class ChumData {
	private ConcurrentHashMap<Integer, Long> chum;

	public static ChumData newClass() {
		ChumData data = new ChumData();
		data.setChum(new ConcurrentHashMap<>());
		return data;
	}

	public ConcurrentHashMap<Integer, Long> getChum() {
		return chum;
	}

	public void setChum(ConcurrentHashMap<Integer, Long> chum) {
		this.chum = chum;
	}

	public boolean checkIsSend(int key) {
		if (!chum.containsKey(key))
			return false;

		return TimeUtils.isSameDay(TimeUtils.Time(), chum.get(key));
	}

	public void setSend(int key) {
		chum.put(key, TimeUtils.Time());
	}
}
