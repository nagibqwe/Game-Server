package com.game.utils;

import game.core.util.StringUtils;

/**
 * @explain: ServerParamUtil的补充，封装一个能适当通用的处理
 * @time Created on 2020/2/11 16:30.
 * @author: tc
 */
public class ServerParamCommon {
	//////////////////////////////////////////////////////////////////
	/// Key
	//////////////////////////////////////////////////////////////////
	private static String KEY_FuDiForeverTitle = "fu_di_forever_title";
	private static String KEY_FuDiTopGuild = "fu_di_top_guild_";

	//////////////////////////////////////////////////////////////////
	/// Interface
	//////////////////////////////////////////////////////////////////

	/**
	 * 福地是否给永久称号
	 * @return
	 */
	public static boolean isFuDiForeverTitle() {
		return StringUtils.parseInt(query(KEY_FuDiForeverTitle, "0"), 0) == 1;
	}

	/**
	 * 设置福地给永久称号
	 */
	public static void setFuDiForeverTitle() {
		save(KEY_FuDiForeverTitle, "1");
	}

	/**
	 * 获取福地排名公会
	 * @param rank
	 * @return
	 */
	public static long getFuDiTopGuild(int rank) {
		return StringUtils.parseLong(query(KEY_FuDiTopGuild + rank, "0"), 0L);
	}

	/**
	 * 保存福地排名公会
	 * @param rank
	 * @param guildId
	 */
	public static void setFuDiTopGuild(int rank, long guildId) {
		save(KEY_FuDiTopGuild + rank, String.valueOf(guildId));
	}





	//////////////////////////////////////////////////////////////////
	/// Common
	//////////////////////////////////////////////////////////////////
	private static String query(String key) {
		return ServerParamUtil.commonMap.get(key);
	}

	private static String query(String key, String defaultStr) {
		return ServerParamUtil.commonMap.getOrDefault(key, defaultStr);
	}

	private static void save(String key, String value) {
		ServerParamUtil.commonMap.put(key, value);
		ServerParamUtil.saveCommon();
	}
}
