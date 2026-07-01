package com.game.chum.struct;

/**
 * @explain: desc
 * @time Created on 2019/10/28 20:19.
 * @author: tc
 */
public enum ChumPrivilege {
	NONE(0),
	/**
	 * 法宝日常
	 */
	FB_TASK(1),
	/**
	 * 神兵日常
	 */
	SB_TASK(2),
	/**
	 * 大能遗府
	 */
	DNY(3),
	/**
	 * 天界之门
	 */
	TIA(4),
	;

	private final int value;

	private ChumPrivilege(int typ) {
		this.value = typ;
	}

	public int getValue() {
		return value;
	}

	public static ChumPrivilege get(int value) {
		for (ChumPrivilege privilege : ChumPrivilege.values()) {
			if (privilege.getValue() == value)
				return privilege;
		}
		return NONE;
	}
}
