package common.commercialize;

import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Recharge_daily_total_Bean;
import com.data.container.Cfg_Recharge_daily_total_Container;
import com.data.struct.ReadArray;
import com.game.backpack.structs.BindStatus;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.commercialize.inter.IDailyRecharge;
import com.game.commercialize.log.DailyRechargeAwardLog;
import com.game.commercialize.struct.DailyRechargeActivity;
import com.game.commercialize.struct.DailyRechargeDefine;
import com.game.commercialize.struct.DailyRechargeReward;
import com.game.commercialize.struct.DailyRechargeStage;
import com.game.db.bean.DailyAccRechargeBean;
import com.game.db.dao.DailyAccRechargeDao;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.thread.SaveServer;
import com.game.structs.GlobalType;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.CommercializeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * FIXME 注意事项：如果配置新一轮活动，需要先把每日累充的开关关掉，配好之后再打开
 */
public class DailyRechargeScript implements IDailyRecharge {
	private static final Logger logger = LogManager.getLogger(DailyRechargeScript.class);
	private final DailyAccRechargeDao accRechargeDao = new DailyAccRechargeDao();

	/**
	 * 获取scriptId
	 *
	 * @return
	 */
	@Override
	public int getId() {
		return ScriptEnum.DailyRechargeBaseScript;
	}

	/**
	 * 调用脚本
	 *
	 * @param args 参数
	 * @return
	 */
	@Override
	public Object call(Object... args) {
		return null;
	}

	/**
	 * 请求每日累充配置文件
	 *
	 * @param player
	 */
	@Override
	public void onReqDailyRechargeCfg(Player player) {
		if (player == null)
			return;
		reset(player);

		CommercializeMessage.ResDailyRechargeCfg.Builder infoList = CommercializeMessage.ResDailyRechargeCfg.newBuilder();
		long startTime = 0;
		long endTime = 0;

		DailyRechargeActivity activity = Manager.commercializeManager.getPlayerDailyAccRecharge(player.getId());
		for (DailyRechargeStage stage : activity.getStageList()) {
			for (DailyRechargeReward reward : stage.getRewardList()) {
				Cfg_Recharge_daily_total_Bean recharge = Cfg_Recharge_daily_total_Container.GetInstance().getValueByKey(reward.getRewardId());
				if (recharge == null)
					continue;

				startTime = getTime(recharge.getStartTime());
				endTime = getTime(recharge.getEndTime());
				CommercializeMessage.DailyRechargeCfg.Builder cfg = CommercializeMessage.DailyRechargeCfg.newBuilder();
				cfg.setAwardId(reward.getRewardId());
				cfg.setPosition(recharge.getPosition());
				cfg.setDay(recharge.getDay());
				cfg.setMoney(recharge.getMoney());
				for (ReadArray<Integer> items : recharge.getAward().getValuees()) {
					if (items.size() != 3)
						continue;
					CommercializeMessage.ItemInfo.Builder item = CommercializeMessage.ItemInfo.newBuilder();
					item.setItemID(items.get(0));
					item.setNum(items.get(1));
					item.setBind(BindStatus.getBind(items.get(2)));
					cfg.addItems(item);
				}
				infoList.addCfgList(cfg);
			}
		}

		infoList.setStartTime(startTime / 1000);
		infoList.setEndTime(endTime / 1000);
		MessageUtils.send_to_player(player, CommercializeMessage.ResDailyRechargeCfg.MsgID.eMsgID_VALUE, infoList.build().toByteArray());
	}

	/**
	 * 请求每日累充信息
	 *
	 * @param player 玩家
	 */
	@Override
	public void onReqCommercialize(Player player) {
		if (player == null) {
			logger.error("同步每日累充给客户端错误");
			return;
		}

		// 先同步配置
		//TODO 关闭协议
		//onReqDailyRechargeCfg(player);

		// 再同步数据
		//TODO 关闭协议
		//DailyRechargeActivity activity = Manager.commercializeManager.getPlayerDailyAccRecharge(player.getId());
		//CommercializeMessage.SyncDailyRechargeInfo.Builder infoList = CommercializeMessage.SyncDailyRechargeInfo.newBuilder();
		//for (DailyRechargeStage stage : activity.getStageList()) {
		//	for (DailyRechargeReward reward : stage.getRewardList()) {
		//		CommercializeMessage.DailyRechargeInfo.Builder info = CommercializeMessage.DailyRechargeInfo.newBuilder();
		//		info.setAwardId(reward.getRewardId());
		//		info.setStatus(reward.getState());
		//		info.setDay(reward.getDay());
		//		infoList.addDailyRechargeList(info);
		//	}
		//}
		//infoList.setIsOpen(isOpen(player) && isValid(getValidCfgOne()));
		//infoList.setCount(activity.getGold());
		//MessageUtils.send_to_player(player, CommercializeMessage.SyncDailyRechargeInfo.MsgID.eMsgID_VALUE, infoList.build().toByteArray());
	}

	/**
	 * 完成了一次充值
	 * TODO 这里最好用商业化的配置做为参数
	 *
	 * @param player
	 * @param commercializeID 商业化订单ID
	 * @param gold            充值元宝数量
	 */
	@Override
	public void recharge(Player player, int commercializeID, int gold,int totalFee) {
		// TODO 判断是否计入每日累充

		// TODO 老的每日累充暂时 不用
	//if (player == null) {
	//	return;
	//}

	//// 检查活动是否开启
	//if ( !isValid(getValidCfgOne())) {
	//	return;
	//}

	//// 尝试重置
	//reset(player);

	//DailyRechargeActivity activity = Manager.commercializeManager.getPlayerDailyAccRecharge(player.getId());
	//activity.setGold(activity.getGold() + gold);
	//for (DailyRechargeStage stage : activity.getStageList()) {
	//	if (stage.getGold() > activity.getGold())
	//		continue;

	//	// 当日已经计算
	//	long now = TimeUtils.Time();
	//	if (TimeUtils.isSameDay(stage.getLastModifyTime(), now))
	//		continue;

	//	stage.setLastModifyTime(now);

	//	List<DailyRechargeReward> list = stage.getRewardList();
	//	for (int i = 0; i < list.size(); i++) {
	//		if (list.get(i).getState() == DailyRechargeDefine.DISSATISFY)
	//			list.get(i).addDay();
	//	}
	//}

	//// 保存并同步数据
	//saveDailyAccRecharge(activity);
	//onReqCommercialize(player);
	}

	/**
	 * 请求领取每日累充奖励
	 *
	 * @param player  玩家
	 * @param awardId 奖励id
	 */
	@Override
	public void onReqDailyAccRechargeAward(Player player, int awardId) {

		// TODO 老的每日累充暂时 不用
		if (awardId >=0) {
			return;
		}
		//----老的每日累充暂时 不用
		if (player == null || awardId <= 0) {
			return;
		}
		if (!isOpen(player)) {
			return;
		}
		Cfg_Recharge_daily_total_Bean recharge = Cfg_Recharge_daily_total_Container.GetInstance().getValueByKey(awardId);
		if (recharge == null || recharge.getAward() == null || !isValid(recharge)) {
			logger.error(player.getInfo() + "领取每日累充奖励失败-请求领取的奖励配置异常或者已过期！");
			return;
		}

		// 尝试重置
		reset(player);

		DailyRechargeActivity activity = Manager.commercializeManager.getPlayerDailyAccRecharge(player.getId());
		// 得到具体的奖励项
		DailyRechargeReward rechargeAward = null;
		for (DailyRechargeStage stage : activity.getStageList()) {
			for (DailyRechargeReward award : stage.getRewardList()) {
				if (award.getRewardId() == awardId) {
					rechargeAward = award;
					break;
				}
			}
		}

		if (rechargeAward == null) {
			logger.error(player.getInfo() + "领取每日累充奖励失败-请求领取的奖励ID异常！");
			return;
		}
		if (rechargeAward.getState() != DailyRechargeDefine.CAN_RECEIVE) {
			logger.error(player.getInfo() + "领取每日累充奖励失败-请求领取的奖励状态异常！" + rechargeAward.getState());
			return;
		}

		// 更新奖励状态
		rechargeAward.setGetReward(true);
		saveDailyAccRecharge(activity);

		// 给物品
		List<Item> items = Item.createItems(recharge.getAward());
		// 是否有足够的空间加入物品
		long actionId = IDConfigUtil.getLogId();
		if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
			Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.AllMenUpAwardGet, actionId);
		} else {
			// 无空间发邮件
			MessageUtils.notify_player(player, Notify.ERROR, MessageString.BAGISSPACETOMAIL);
			Manager.mailManager.sendMailToPlayer(player.getId()
					, MailType.SpecialActivityMail
					, MessageString.System
					, MessageString.DailyRechargeAward
					, MessageString.GetAwardNotEnoughSpaceContent
					, items, ItemChangeReason.AllMenUpAwardGet, actionId);
		}

		// 记录数据库领取日志
		DailyRechargeAwardLog log = new DailyRechargeAwardLog();
		log.setPlayerInfo(player.getPlatformName()
				, player.getCreateServerId()
				, player.getUserId()
				, player.getId()
				, player.getName());
		log.setAwardId(awardId);
		log.setAward(items.toString());
		LogService.getInstance().execute(log);

		// 通知
		MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, MessageString.DailyRechargeNotice,
				player.getName(),
				Utils.makeUrlStr(MessageString.DailyRechargeNotice));

		// 同步最新数据到客户端
		onReqCommercialize(player);
		//记录bi数据
		Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.EVERYDAY_CHARGE, ItemChangeReason.AllMenUpAwardGet, recharge.getID());
	}

	/**
	 * 活动过期，该领还没有领的邮件发放
	 *
	 * @param player
	 */
	private void checkTimeOut(Player player) {
		DailyRechargeActivity activity = Manager.commercializeManager.getPlayerDailyAccRecharge(player.getId());
		if (activity == null)
			return;

		// 重置版本号
		boolean isChange = false;
		for (DailyRechargeStage stage : activity.getStageList()) {
			for (DailyRechargeReward reward : stage.getRewardList()) {
				// 未领处理
				if (reward == null){
					logger.error("DailyRechargeStage  "  + stage.getGold()     + "  DailyRechargeReward  "  + reward);
					continue;
				}
				if (reward.getState() != DailyRechargeDefine.CAN_RECEIVE)
					continue;

				// 检查是否已经过期
				Cfg_Recharge_daily_total_Bean activityCfg = Cfg_Recharge_daily_total_Container.GetInstance().getValueByKey(reward.getRewardId());
				if (activityCfg == null) {
					isChange = true;
					reward.setGetReward(true);
				} else if (!isValid(activityCfg)) {
					isChange = true;
					reward.setGetReward(true);
					if(!isOpen(player))
						dailyRechargeAwardMail(activity.getRoleId(), reward.getRewardId());
				}
			}
		}

		if (isChange)
			saveDailyAccRecharge(activity);
	}

	/**
	 * FIXME 注意事项：如果配置新一轮活动，需要先把每日累充的开关关掉，配好之后再打开
	 * 重置每日累充领奖状态
	 *
	 * @param player
	 */
	private void reset(Player player) {
		if (player == null)
			return;

		// 纯粹是处理过期的奖励
		checkTimeOut(player);

		if (!isOpen(player))
			return;

		// 如果已经重置成功了
		if (resetVersion(player))
			return;

		// 活动没开
		if (!isValid(getValidCfgOne()))
			return;

		DailyRechargeActivity activity = Manager.commercializeManager.getPlayerDailyAccRecharge(player.getId());
		if (activity == null)
			return;

		// 不需要重置
		if (!activity.isNeedReset())
			return;

		// 重置每日领奖状态
		activity.setLastResetTime(TimeUtils.Time());
		activity.setGold(0);

		for (DailyRechargeStage stage : activity.getStageList()) {
			DailyRechargeReward reward = stage.getRewardList().get(0);
			if (reward.getState() == DailyRechargeDefine.CAN_RECEIVE) {
				// 已经完成但没有领奖，就自动发奖
				reward.setGetReward(true);
				if(!isOpen(player))
					dailyRechargeAwardMail(activity.getRoleId(), reward.getRewardId());
			}

			if (reward.getState() != DailyRechargeDefine.DISSATISFY) {
				// 切换为下一档单天充值
				Cfg_Recharge_daily_total_Bean recharge = Cfg_Recharge_daily_total_Container.GetInstance().getValueByKey(reward.getRewardId());
				if (recharge == null) {
					logger.error(player.getInfo() + " 已有的每日累充活动数据在ActivityDataDailyRecharge中找不到:" + reward.getRewardId());
					continue;
				}

				reward = getNextReward(reward.getRewardId(), recharge.getDay() + 1);
				if (reward != null){
					reward.setNeedDay(1); // 每天的充值，只需要满足一天
					stage.getRewardList().set(0, reward);
				}else{
					stage.getRewardList().remove(0);//当这一档最后一天结束时候，删除这档
				}
			}
		}
		// 保存数据
		saveDailyAccRecharge(activity);
	}

	/**
	 * 所有未发的奖全部发送，如果没有下一个版本，则不清数据。
	 *
	 * @param player
	 */
	private boolean resetVersion(Player player) {
		if (player == null)
			return false;

		DailyRechargeActivity activity = Manager.commercializeManager.getPlayerDailyAccRecharge(player.getId());
		if (activity == null)
			return false;

		boolean isRest = false;
		Cfg_Recharge_daily_total_Bean obj = getValidCfgOne();
		if (obj != null && version(obj) != activity.getVersion())
			isRest = true;

		if (!isRest)
			return false;

		// 重置版本号
		boolean isChange = false;
		for (DailyRechargeStage stage : activity.getStageList()) {
			for (DailyRechargeReward reward : stage.getRewardList()) {
				// 未领处理
				if (reward.getState() == DailyRechargeDefine.CAN_RECEIVE) {
					reward.setGetReward(true);
					if(!isOpen(player))
						dailyRechargeAwardMail(activity.getRoleId(), reward.getRewardId());
					isChange = true;
				}
			}
		}
		if (isChange)
			saveDailyAccRecharge(activity);

		// 如果活动没开，保留数据，不清空原数据
		if (!isOpen(player) || !isValid(obj))
			return true;

		logger.info("重置玩家每日累充版本数据：" + player.getInfo() +
				" version: " + activity.getVersion() + " -> " + version(obj));

		// 写新数据
		zeroDailyRechargeActivity(activity, player.getId());
		saveDailyAccRecharge(activity);
		return true;
	}

	private DailyRechargeActivity zeroDailyRechargeActivity(DailyRechargeActivity activity, long roleID) {
		List<DailyRechargeStage> list = initDailyRechargeStage();
		if (activity == null)
			activity = new DailyRechargeActivity();

		activity.setRoleId(roleID);
		activity.setGold(0);

		if (list == null) {
			activity.setStageList(new ArrayList<>());
			activity.setVersion(0);
		} else {
			// 以下都是硬条件，不会为null
			DailyRechargeReward reward = list.get(0).getRewardList().get(0);
			activity.setVersion(version(Cfg_Recharge_daily_total_Container.GetInstance().getValueByKey(reward.getRewardId())));
			activity.setStageList(list);
		}

		activity.setLastResetTime(TimeUtils.Time());
		return activity;
	}

	/**
	 * 上线加载每日累充计数据
	 *
	 * @param player 玩家
	 */
	public void playerOnline(Player player) {
		//TODO 功能关闭
		//try {
		//	DailyRechargeActivity activity = Manager.commercializeManager.getPlayerDailyAccRecharge(player.getId());
		//	if (activity != null) {
		//		// 检查重置
		//		reset(player);
		//		return;
		//	}
//
		//	DailyAccRechargeBean bean = accRechargeDao.selectByRoleId(player.getId());
		//	if (bean != null) {
		//		try {
		//			activity = JsonUtils.parseObject(bean.getDailyAccRechargeData(), DailyRechargeActivity.class);
		//		} catch (Exception ex) {
		//			logger.error("JSON转对象时发生异常了！ > " + ex + " roleID:" + player.getId() + " data:" + bean.getDailyAccRechargeData());
		//			return;
		//		}
//
		//		if (player.getId() != activity.getRoleId()) {
		//			logger.error("加载每日累充数据时数据库查询到的数据不正确 > player:" + player.getId() + "(" + player.getName() + ")");
		//			return;
		//		}
//
		//		// 先保存到内存
		//		Manager.commercializeManager.getDailyAccRechargeRecord().put(activity.getRoleId(), activity);
//
		//		// 检查重置
		//		reset(player);
		//	} else {
		//		// New新的
		//		activity = zeroDailyRechargeActivity(null, player.getId());
		//		saveDailyAccRecharge(activity);
		//	}
		//} catch (Exception e) {
		//	logger.error("上线加载每日累充数据时发生异常了！ > " + e);
		//}
	}

	/**
	 * 每日累充数据存库
	 *
	 * @param activity 统计数据
	 */
	@Override
	public void saveDailyAccRecharge(DailyRechargeActivity activity) {
		// 保存内存
		Manager.commercializeManager.getDailyAccRechargeRecord().put(activity.getRoleId(), activity);

		// 保存数据库
		DailyAccRechargeBean bean = new DailyAccRechargeBean();
		bean.setRoleId(activity.getRoleId());
		bean.setDailyAccRechargeData(JsonUtils.toJSONString(activity));
		// FIXME 已经支持插入或者保存
		Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.DAILY_ACC_UPDATE, SaveServer.MERGE);
	}

	private DailyRechargeReward getNextReward(int cfgID, int nextDay) {
		Cfg_Recharge_daily_total_Bean before = Cfg_Recharge_daily_total_Container.GetInstance().getValueByKey(cfgID);
		for (Cfg_Recharge_daily_total_Bean bean : Cfg_Recharge_daily_total_Container.GetInstance().getValuees()) {
			if (!isValid(bean))
				continue;

			if (bean.getPosition() == DailyRechargeDefine.Type_EveryDay
					&& bean.getMoney() == before.getMoney()
					&& bean.getDay() == nextDay
					&& bean.getStartTime() == before.getStartTime()
					&& bean.getEndTime() == before.getEndTime())
				return new DailyRechargeReward(bean.getID(), bean.getPosition(), bean.getDay());
		}
		return null;
	}

	/**
	 * 初始化每日充值奖励阶段
	 */
	private List<DailyRechargeStage> initDailyRechargeStage() {
		List<Cfg_Recharge_daily_total_Bean> validCfgList = getValidCfg();
		if (validCfgList.size() == 0)
			return null;

		HashMap<Integer, List<Cfg_Recharge_daily_total_Bean>> hashMap = new HashMap<>();
		for (Cfg_Recharge_daily_total_Bean bean : validCfgList) {
			// 从map查找，如果没找到，则初始化并保存一个list
			List<Cfg_Recharge_daily_total_Bean> list = hashMap.computeIfAbsent(bean.getMoney(), k -> new ArrayList<>());
			list.add(bean);
		}

		List<DailyRechargeStage> stageList = new ArrayList<>();
		for (Map.Entry<Integer, List<Cfg_Recharge_daily_total_Bean>> entry : hashMap.entrySet()) {
			List<Cfg_Recharge_daily_total_Bean> list = entry.getValue();
			DailyRechargeReward reward = null;
			List<DailyRechargeReward> rewards = new ArrayList<>();
			rewards.add(null);

			for (Cfg_Recharge_daily_total_Bean bean : list) {
				if (bean.getPosition() == DailyRechargeDefine.Type_EveryDay) {
					// 取按天的第一档
					if (bean.getDay() == 1 && reward == null)
						reward = new DailyRechargeReward(bean.getID(), bean.getPosition(), bean.getDay());
				} else
					rewards.add(new DailyRechargeReward(bean.getID(), bean.getPosition(), bean.getDay()));
			}

			if (reward == null) {
				logger.error("每日累计充配置错误(没有找到第一档)，Money Stage：" + entry.getKey());
				continue;
			}

			reward.setNeedDay(1); // 每天的充值，只需要满足一天
			rewards.set(0, reward);
			DailyRechargeStage stage = new DailyRechargeStage(entry.getKey(), rewards);
			stageList.add(stage);
		}
		return stageList;
	}

	/**
	 * 玩家完成首充后第二天开启每日累充
	 *
	 * @param player 玩家
	 * @return bool
	 */
	private boolean isOpen(Player player) {
		// 判断开关
		return Manager.commercializeManager.isOpen(player, CommercializeMessage.Commercialize.DailyRecharge);
	}

	private boolean isValid(Cfg_Recharge_daily_total_Bean bean) {
		if (bean == null)
			return false;
		int day = TimeUtils.getOpenServerDay();
		return bean.getStartTime() <= day && day <= bean.getEndTime();
	}

	private int version(Cfg_Recharge_daily_total_Bean bean) {
		int v = bean.getStartTime() << 16;
		v |= bean.getEndTime();
		return v;
	}

	private void dailyRechargeAwardMail(long roleId, int awardId) {
		if (awardId <= 0) {
			return;
		}

		Cfg_Recharge_daily_total_Bean activityCfg = Cfg_Recharge_daily_total_Container.GetInstance().getValueByKey(awardId);
		if (activityCfg == null || activityCfg.getAward() == null) {
			logger.error("每日累充邮件奖励失败-奖励配置异常或者已过期！ID:" + awardId);
			return;
		}

		//给物品
		List<Item> items = Item.createItems(activityCfg.getAward());
		Manager.mailManager.sendMailToPlayer(roleId
				, MailType.SpecialActivityMail
				, MessageString.System
				, MessageString.DailyRechargeAward
				, MessageString.DailyRechargeMailContent
				, items,ItemChangeReason.DailyRechargeRewardGet);
	}

	private Cfg_Recharge_daily_total_Bean getValidCfgOne() {
		for (Cfg_Recharge_daily_total_Bean bean : Cfg_Recharge_daily_total_Container.GetInstance().getValuees()) {
			if (isValid(bean))
				return bean;
		}
		return null;
	}

	private List<Cfg_Recharge_daily_total_Bean> getValidCfg() {
		List<Cfg_Recharge_daily_total_Bean> list = new ArrayList<>();
		for (Cfg_Recharge_daily_total_Bean bean : Cfg_Recharge_daily_total_Container.GetInstance().getValuees()) {
			if (isValid(bean))
				list.add(bean);
		}
		return list;
	}

	/**
	 * 86400000 = 24 * 3600 * 1000
	 *
	 * @param day
	 * @return
	 */
	private long getTime(long day) {
//		Calendar instance = Calendar.getInstance();
//		instance.setTimeInMillis(TimeUtils.getOpenServerTime());
//		instance.add(Calendar.DAY_OF_MONTH, day);
//		return instance.getTimeInMillis();
		return TimeUtils.getOpenServerTime() + day * GlobalType.MILLIS_PER_DAY;
	}
}