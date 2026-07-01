package com.game.welfare.struct;

/**
 * @explain: desc
 * @time Created on 2020/1/6 20:37.
 * @author: tc
 */
public enum RetrieveType {
	/**
	 * null
	 */
	None(0),
	/**
	 * 师门传道
	 */
//	LeaderPreach(1),
	/**
	 * 法宝
	 */
	Treasure(2),
	/**
	 * 神兵
	 */
	Weapon(3),
	/**
	 * 世界BOSS（无极墟域）
	 */
	WorldBoss(4),
	/**
	 * 天界之门（天禁之门）
	 */
	FairyLand(5),
	/**
	 * 大能遗府
	 */
	StarCopy(6),
	/**
	 * 凌云妖塔（经验副本）
	 */
	ExpCopy(7),
	/**
	 * 心魔幻境
	 */
	XinMoCopy(8),
	/**
	 * 五行副本（锁灵台）
	 */
	FiveCopy(9),
	/**
	 * 境界BOSS（境界圣域）
	 */
	StateBoss(10),
	/**
	 * 套装BOSS（晶甲和域）
	 */
	SuitBoss(11),
	/**
	 * 宝石BOSS（宝石灵域）
	 */
	GemBoss(12),
	/**
	 * 神兽岛（年兽封域）
	 */
	GodIsland(13),
	/**
	 * 天之禁地（天芒鬼城）
	 */
	ArenaYZZD(14),
	/**
	 * 神魔战场（天道秘境）
	 */
	GodDevilWar(15),
	/**
	 * 世界答题（心境博弈）
	 */
	WorldAnswer(16),
	/**
	 * 世界篝火（日暮篝火）
	 */
	WorldBonfire(17),
	/**
	 * 竞技场
	 */
	JJC(18),
	/**
	 * 活跃度,FIXME 记录剩余多少点
	 */
	Activity(19),
	/**
	 * 公会任务完成多少次
	 */
	GuildTask(20),
    /**
     * 精英Boss
     */
    EliteBoss(21),

	/**
	 * 荒古神探
	 */
	HuangGushengTan(22),

	/**
	 * 巅峰竞技
	 */
	PeakJJC(23),
	/**
	 * 八级阵图
	 */
	BajiZhenTu(24),
	/**
	 * 仙盟战
	 */
	GuildBattle(25),
	/**
	 * 仙盟首领
	 */
	GuildBoss(26),
	/**
	 * 魔王入侵
	 */
	MoWangRuQING(27),

	/**
	 * VipBoss
	 */
	VipBoss(28),

	/**
	 * 仙侣护送
	 */
	CoupeEscort(29),
	;

	private final int type;

	RetrieveType(int type) {this.type = type;}

	public int type() {return this.type;}

	public boolean compare(int type) {
		return this.type == type;
	}

	public boolean compare(RetrieveType type) {
		return type.type == this.type;
	}

	public static RetrieveType type(int type) {
		for (RetrieveType rt : RetrieveType.values()) {
			if (rt.compare(type))
				return rt;
		}
		return RetrieveType.None;
	}
}
