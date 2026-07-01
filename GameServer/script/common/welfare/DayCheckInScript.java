package common.welfare;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Sign_rewardCumulative_Bean;
import com.data.bean.Cfg_Sign_reward_Bean;
import com.data.bean.Cfg_Sign_rewardsupplement_Bean;
import com.game.backpack.structs.BindStatus;
import com.game.backpack.structs.Item;
import com.game.bi.enums.SignType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.GlobalType;
import com.game.task.structs.TaskHelp;
import com.game.utils.MessageUtils;
import com.game.welfare.script.IDayCheckInScript;
import com.game.welfare.script.IExclusiveCardScript;
import com.game.welfare.script.ILoginGiftScript;
import com.game.welfare.struct.DayCheckIn;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DayCheckInScript implements IDayCheckInScript {
	private final Logger log = LogManager.getLogger(DayCheckInScript.class);

	private final int realmTypNone = 0; // 没有加倍
	private final int realmTypJJ = 1; // 境界
	private final int realmTypZK = 2; // 周卡
	private final int realmTypYK = 3; // 月卡
	private final int realmTypZXK = 4; // 尊享卡
	private final int realmTypVIP = 5; // VIP

	/**
	 * 获取scriptId
	 *
	 * @return
	 */
	@Override
	public int getId() {
		return ScriptEnum.DayCheckInBaseScript;
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
	 * 玩家上线
	 *
	 * @param player
	 */
	@Override
	public void playerOnline(Player player) {
		DayCheckIn checkIn = player.getDayCheckIn();
		if (checkIn == null) {
			checkIn = DayCheckIn.newDayCheckIn();
			player.setDayCheckIn(checkIn);
		}

		// 检查一下能否进入下一轮
		if (reset(player, checkIn))
			log.info(TaskHelp.getPlayerInfo(player) + " online切换每日签到下一轮:" + checkIn.getRound());
	}

	/**
	 * 请求某福利子项数据
	 *
	 * @param player
	 */
	@Override
	public void freshDataNtf(Player player) {
		if (player == null)
			return;
		DayCheckIn checkIn = player.getDayCheckIn();
		if (checkIn == null)
			return;

		// 检查一下能否进入下一轮
		if (reset(player, checkIn))
			log.info(TaskHelp.getPlayerInfo(player) + " 切换每日签到下一轮:" + checkIn.getRound());

		WelfareMessage.ResDayCheckInData.Builder builder = WelfareMessage.ResDayCheckInData.newBuilder();
		builder.setDay(checkIn.getSignCumulativeDay());
		builder.setRound(checkIn.getRound());
		builder.addAllCheckIns(checkIn.getCheckIns());
		builder.addAllCheckIn2S(checkIn.getCheckIn2s());
		builder.addAllRewardCfgID(checkIn.getRewardCfgID());
		int day = getCurDay(checkIn);
		builder.setCheckInDay(day);
		builder.setIsCheckIn(checkIn.getCheckIns().contains(day));
		MessageUtils.send_to_player(player, WelfareMessage.ResDayCheckInData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
	}

	/**
	 * 请求签到或者领奖
	 *
	 * @param player
	 * @param cfgID  签到配置ID或者领奖配置ID
	 * @param typ    1签到，2领奖
	 */
	@Override
	public void onReqDayCheckIn(Player player, int cfgID, int typ) {
		if (player == null)
			return;
		if (!Manager.welfareManager.isOpen(player, WelfareMessage.WelfareType.DayCheckIn))
			return;

		DayCheckIn checkIn = player.getDayCheckIn();
		if (checkIn == null)
			return;

		boolean res;
		if (typ == 1)
			res = checkIn(player, cfgID);
		else if (typ == 2)
			res = reward(player, cfgID);
		else
			return;

		if (res)
			freshDataNtf(player);
	}

	/**
	 * 条件倍率
	 * @param bean
	 * @param player
	 * @return
	 */
	private int ber(Cfg_Sign_reward_Bean bean, Player player) {
		if (bean.getRealmType() == realmTypJJ && player.getStateVip().getLv() >= bean.getRealmpara())
			return bean.getRealRatio();
		else if (bean.getRealmType() == realmTypZK
				&& ((IExclusiveCardScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.ExclusiveCard)).haveWeekCard(player))
			return bean.getRealRatio();
		else if (bean.getRealmType() == realmTypYK
				&& ((IExclusiveCardScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.ExclusiveCard)).haveMonthCard(player))
			return bean.getRealRatio();
		else if (bean.getRealmType() == realmTypZXK
				&& ((IExclusiveCardScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.ExclusiveCard)).haveExclusiveCard(player))
			return bean.getRealRatio();
		else if (bean.getRealmType() == realmTypVIP && player.getVipLv()>= bean.getRealmpara())
			return bean.getRealRatio();
		return 1;
	}

	private boolean checkIn(Player player, int cfgID) {
		DayCheckIn checkIn = player.getDayCheckIn();
		if (checkIn == null)
			return false;

		if (checkIn.getRound() == 0) {
			log.error(player.getInfo() + " 签到轮数为:0");
			return false;
		}

		int curD = getCurDay(checkIn);
		if (cfgID > curD)
			return false;

		// 只要小于正常值就可以签到
		long nowTime = TimeUtils.Time();
//		if (cfgID == curD && TimeUtils.isSameDay(nowTime, checkIn.getLastTime()))
//			return false;

		Cfg_Sign_reward_Bean bean = CfgManager.getCfg_Sign_reward_Container().getValueByKey(cfgID);
		if (bean == null)
			return false;

		// 是否已经签了
		for (int d : checkIn.getCheckIns()) if (cfgID == d) return false;
		for (int d : checkIn.getCheckIn2s()) if (cfgID == d) return false;

		// 获取道具数量,判断顺序优先级是：境界 > 尊享 > 月卡
		int num = bean.getItemNum();
		num *= ber(bean, player);

		// 检查背包
		Item item = Item.createItem(bean.getItemId(), num, BindStatus.getBind(bean.getIsBind()));
		if (!Manager.backpackManager.manager().onHasAddSpace(player, item)) {
			MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
			return false;
		}

		long actionId = IDConfigUtil.getLogId();

		// 如果是补签，检测消耗
		if (cfgID < curD) {
			Cfg_Sign_rewardsupplement_Bean bq = CfgManager.getCfg_Sign_rewardsupplement_Container().getValueByKey(checkIn.getCheckIn2s().size() + 1);
			if (bq == null)
				return false;

			if (!Manager.currencyManager.manager().canDecItemCoin(player, bq.getCostCount(), bq.getCostType()))
				return false;

			checkIn.getCheckIn2s().add(cfgID);
			Manager.currencyManager.manager().onDecItemCoin(player, bq.getCostCount(),
					ItemChangeReason.WelfareDayCheckInDec, actionId, bq.getCostType());
		} else
			checkIn.getCheckIns().add(cfgID);

		checkIn.setDay(checkIn.getDay() + 1);
		checkIn.setSignCumulativeDay(checkIn.getSignCumulativeDay() + 1);
		checkIn.setLastTime(nowTime);

		List<Item> itemList = new ArrayList<>();
		try {
			itemList.add(item.clone());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		// 发奖
		Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.WelfareDayCheckInGet, actionId);
		((ILoginGiftScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.LoginGift)).freshWelfareRewardNtf(
				player, WelfareMessage.WelfareType.DayCheckIn, itemList);

		//记录BI数据
//		Manager.biManager.getScript().biActivity(player, ItemChangeReason.WelfareDayCheckIn, BIActiityTypeEnum.WELFARE.getId(), cfgID);
		Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WELFARE_DailyCheck, ItemChangeReason.WelfareDayCheckIn, cfgID);

		int rewardType = 1;
		if(cfgID < curD){
			rewardType = 2;//补签
		}
		Manager.biManager.getScript().biSign(player, SignType.WELFARE.getId(), rewardType, checkIn.getDay(), 0);
		return true;
	}

	private boolean reward(Player player, int cfgID) {
		DayCheckIn checkIn = player.getDayCheckIn();
		Cfg_Sign_rewardCumulative_Bean bean = CfgManager.getCfg_Sign_rewardCumulative_Container().getValueByKey(cfgID);
		if (bean == null)
			return false;

		if (bean.getRound() != checkIn.getRound())
			return false;

		if (checkIn.getRewardCfgID().contains(cfgID))
			return false;

		if (checkIn.getSignCumulativeDay() < bean.getDay())
			return false;

		List<Item> items = Item.createItems(bean.getAward());
		if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0) {
			MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
			return false;
		}

		// 添加已领
		checkIn.getRewardCfgID().add(cfgID);

		// 发奖
		List<Item> itemList = Item.clone(items);
		Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.WelfareDayTotalCheckInGet, IDConfigUtil.getLogId());
		((ILoginGiftScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.LoginGift)).freshWelfareRewardNtf(
				player, WelfareMessage.WelfareType.DayCheckIn, itemList);

		// 检查是否转入下一轮
		boolean isComplete = true;
		for (Cfg_Sign_rewardCumulative_Bean rc : CfgManager.getCfg_Sign_rewardCumulative_Container().getValuees()) {
			if (rc.getRound() != checkIn.getRound())
				continue;

			if (!checkIn.getRewardCfgID().contains(rc.getId())) {
				isComplete = false;
				break;
			}
		}

		if (isComplete) {
			checkIn.setSignCumulativeDay(0);
			checkIn.getRewardCfgID().clear();
			checkIn.setRound(getNextRound(checkIn.getRound()));
		}

		//BI
		Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WELFARE_DailyCheck, ItemChangeReason.WelfareDayTotalCheckInGet, cfgID);
		return true;
	}

	private int getNextRound(int curRound) {
		for (int i = CfgManager.getCfg_Sign_rewardCumulative_Container().size() - 1; i >= 0; i--) {
			Cfg_Sign_rewardCumulative_Bean rc = CfgManager.getCfg_Sign_rewardCumulative_Container().getValueByIndex(i);
			if (rc.getRound() == curRound) {
				return rc.getNextRound();
			}
		}
		return 0;
	}

	private boolean reset(Player player, DayCheckIn checkIn) {
		if (!Manager.welfareManager.isOpen(player, WelfareMessage.WelfareType.DayCheckIn))
			return false;

		// 检查时间
		long now = TimeUtils.Time();
		if (checkIn.getDay() == 0 && checkIn.getFirstTime() == 0) {
			// 从来没签过，永远保留在第一轮第一天
			checkIn.setFirstTime(now - GlobalType.MILLIS_PER_DAY);
			return false;
		}

		// 签过
		int all = CfgManager.getCfg_Sign_reward_Container().size();
		int diffDay = TimeUtils.getBetweenDays(now, checkIn.getFirstTime());
		if (diffDay <= all)
			return false;

		checkIn.setFirstTime(now - GlobalType.MILLIS_PER_DAY);
		checkIn.setCheckIns(new ArrayList<>());
		checkIn.setCheckIn2s(new ArrayList<>());
		return true;
	}

	private int getCurDay(DayCheckIn checkIn) {
		return TimeUtils.getBetweenDays(TimeUtils.Time(), checkIn.getFirstTime());
	}
}
