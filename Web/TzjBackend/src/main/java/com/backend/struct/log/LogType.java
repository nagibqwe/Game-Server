package com.backend.struct.log;

import com.backend.struct.log.entity.achievement.AchievementRewardLog;
import com.backend.struct.log.entity.backpack.CoinChangeLog;
import com.backend.struct.log.entity.backpack.ExpChangeLog;
import com.backend.struct.log.entity.backpack.GoldChangeLog;
import com.backend.struct.log.entity.backpack.ItemChangeLog;
import com.backend.struct.log.entity.boss.BossDieReliveLog;
import com.backend.struct.log.entity.chat.ChatLog;
import com.backend.struct.log.entity.cross.CrossCloneEnterLog;
import com.backend.struct.log.entity.gm.BackGMCmdLog;
import com.backend.struct.log.entity.gm.GmCommandLog;
import com.backend.struct.log.entity.guild.GuildBaseLog;
import com.backend.struct.log.entity.mail.MailLog;
import com.backend.struct.log.entity.player.ChangeRoleNameLog;
import com.backend.struct.log.entity.ranklist.RankListLog;
import com.backend.struct.log.entity.recharge.RechargeLog;
import com.backend.struct.log.entity.register.RoleLoginLog;
import com.backend.struct.log.entity.setting.FeedbackLog;
import com.backend.struct.log.entity.world.FightRoomCreateLog;

/**
 * 日志类型
 */
public class LogType {

	public static final int RoleLoginLog = 1;		//登录登出日志

	public static final int GoldChangeLog = 2;		//元宝变化日志

	public static final int ItemChangeLog = 3;		//物品变化日志

	public static final int ExpChangeLog = 4;		//经验变化日志

	public static final int GuildBaseLog = 5;		//公会基本日志

	public static final int CrossCloneEnterLog = 6;	//跨服副本进入日志

	public static final int FightRoomCreateLog = 10;	//战斗房间创建日志

	public static final int AchievementRewardLog = 13;	//成就奖励领取日志

	public static final int ChatLog = 21;				//聊天日志

	public static final int BackGMCmdLog = 22;			//后台指令日志

	public static final int GmCommandLog = 23;			//gm命令日志

	public static final int MailLog = 24;				//邮件日志

	public static final int RankListLog = 25;			//排行榜日志

	public static final int FeedbackLog = 27;			//反馈日志

	public static final int RechargeLog = 28;			//充值日志

	public static final int ChangeNameLog = 29;			//改名日志

	public static final int BossDieReLiveLog = 30;		//精英boss死亡复活日志

    public static final int CoinChangeLog = 31;         //货币变化日志

	public static Class getClass(int logType) {
		switch (logType) {
			case RoleLoginLog:
				return RoleLoginLog.class;
			case GoldChangeLog:
				return GoldChangeLog.class;
			case ItemChangeLog:
				return ItemChangeLog.class;
			case ExpChangeLog:
				return ExpChangeLog.class;
			case GuildBaseLog:
				return GuildBaseLog.class;
			case CrossCloneEnterLog:
				return CrossCloneEnterLog.class;
			case FightRoomCreateLog:
				return FightRoomCreateLog.class;
			case AchievementRewardLog:
				return AchievementRewardLog.class;
			case ChatLog:
				return ChatLog.class;
			case BackGMCmdLog:
				return BackGMCmdLog.class;
			case GmCommandLog:
				return GmCommandLog.class;
			case MailLog:
				return MailLog.class;
			case RankListLog:
				return RankListLog.class;
			case FeedbackLog:
				return FeedbackLog.class;
			case RechargeLog:
				return RechargeLog.class;
			case ChangeNameLog:
				return ChangeRoleNameLog.class;
			case BossDieReLiveLog:
				return BossDieReliveLog.class;
            case CoinChangeLog:
                return CoinChangeLog.class;
		}
		throw new NoClassDefFoundError();
	}

}
