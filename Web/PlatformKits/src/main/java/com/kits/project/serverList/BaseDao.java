package com.kits.project.serverList;

import com.kits.common.utils.LogUtil;

public abstract class BaseDao {
	protected static void info(String info){
		LogUtil.info(info);
	}

	/**
	 * 获取指定时间到现在的时间数（毫秒）
	 * 
	 * @param time
	 * @return
	 */
	protected static long getDurationToNow(long time) {
		return System.currentTimeMillis() - time;
	}
}
