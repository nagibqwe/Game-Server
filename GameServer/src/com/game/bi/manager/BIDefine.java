package com.game.bi.manager;

/**
 * @explain: desc
 * @time Created on 2020/3/4 9:18.
 * @author: tc
 */
public class BIDefine {
	// biGuildMember change_type 码
	// 1,创建
	public static final int CTGuildMemberCreate = 1;
	// 2,加入
	public static final int CTGuildMemberJoin = 2;
	// 3,主动解散工会
	public static final int CTGuildMemberDissolve = 3;
	// 4,工会解散被踢出
	public static final int CTGuildMemberBeDissolveQuit = 4;
	// 5,被管理员踢出
	public static final int CTGuildMemberBeQuit = 5;
	// 6,主动退出
	public static final int CTGuildMemberQuit = 6;
	// 7,人数不足自动解散
	public static final int CTGuildMemberNoMember = 7;
	// 8,会长丢失解散公会
	public static final int CTGuildMemberNoChain = 8;

//	public static final int CTGuildMember = 8;

	public static final int CTGuildMemberRankChange = 10;

	//---------------------grow表--------------------
	//激活
	public static final int GrowActive = 1;
	//升级
	public static final int GrowLevelUp = 2;
	//培养
	public static final int GrowTrain = 3;
	//升阶
	public static final int GrowStageUp = 4;
	//技能蜕变
	public static final int GrowTuiBian = 5;
	//器灵进化
	public static final int GrowEvolution = 6;

	//---------------------resource表--------------------
	public static final int OpenServerGrowUpPoint = 1001;
}
