package common.welfare;

import com.data.*;
import com.data.bean.Cfg_PrayCost_Bean;
import com.data.bean.Cfg_Pray_Bean;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.count.structs.VariantType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.vip.manager.VipManager;
import com.game.vip.structs.VipPower;
import com.game.welfare.script.IFeelingExpScript;
import game.core.util.IDConfigUtil;
import game.message.WelfareMessage;

public class FeelingExpScript implements IFeelingExpScript {
	// 经验感悟
	private final int EXP = 1;
	// 灵石感悟
	private final int MONEY = 2;

	/**
	 * 获取scriptId
	 *
	 * @return
	 */
	@Override
	public int getId() {
		return ScriptEnum.FeelingExpBaseScript;
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

		WelfareMessage.ResFeelingExpData.Builder builder = WelfareMessage.ResFeelingExpData.newBuilder();
		builder.setUseTimes((int) get(player, timesKey(EXP, false)));
		builder.setDayExp(get(player, countKey(timesKey(EXP, false))) + get(player, countKey(timesKey(EXP, true))));
		builder.setFreeExpTimes((int) get(player, timesKey(EXP, true)));
		builder.setUseMTimes((int) get(player, timesKey(MONEY, false)));
		builder.setDayMoney(get(player, countKey(timesKey(MONEY, false))) + get(player, countKey(timesKey(MONEY, true))));
		builder.setFreeCoinTimes((int) get(player, timesKey(MONEY, true)));
		MessageUtils.send_to_player(player, WelfareMessage.ResFeelingExpData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
	}

	/**
	 * 感悟
	 *
	 * @param player
	 * @param typ
	 * @param times
	 */
	@Override
	public void onReqFeelingExp(Player player, int typ, int times) {
		if (player == null || times <= 0)
			return;
		if (typ != EXP && typ != MONEY)
			return;
		if (!Manager.welfareManager.isOpen(player, WelfareMessage.WelfareType.FeelingExp))
			return;

		long action = IDConfigUtil.getLogId();

		// 成功的免费次数
		int countFree = freeFeelingExp(player, typ, times, action);
		times -= countFree;

		// 当前使用的收费次数
		int cur = (int) get(player, timesKey(typ, false));
		// 最大收费次数
		int max = costMax(player, typ);
		int t = Math.min(max - cur, times);
		int count = 0;
		if (t > 0) {
			// 还有收费次数
			count = feelingExp(player, typ, times, action);
		}
		if (countFree > 0 || count != times) {
			freshDataNtf(player);
			int ms = MessageString.FeelingExpExpSucc;
			if (typ == MONEY)
				ms = MessageString.FeelingExpMoneySucc;
			MessageUtils.notify_player(player, Notify.SUCCESS, ms, String.valueOf(countFree + times - count));
			Manager.controlManager.operate(player, FunctionVariable.Welfare_Any, countFree + times - count);

		}
	}

	private int costMax(Player player, int typ) {
		// 最大普通收费次数
//		int max = CfgManager.getCfg_PrayCost_Container().size();
		int max;
		if (typ == EXP) {
			max = Global.Pray_Time_Limit.get(0);
		} else {
			max = Global.Pray_Time_Limit.get(1);
		}
		// VIP可购买次数
		int vip = 0;
		if (typ == MONEY) vip = Manager.vipManager.power().getVipPurNum(player, VipPower.POWER_24);
		else vip = Manager.vipManager.power().getVipPurNum(player, VipPower.POWER_25);
		return max + vip;
	}

	private int freeFeelingExp(Player player, int typ, int times, long action) {
		// 免费次数
		int free = freeTime(player, typ);
		// 当前使用的免费次数
		int cur = (int) get(player, timesKey(typ, true));

		int N = Math.min(free - cur, times);

		int successTimes = 0;
		for (int i = 0; i < N; i++) {
			Cfg_Pray_Bean bean = CfgManager.getCfg_Pray_Container().getValueByKey(player.getLevel());
			if (bean == null) return successTimes;
			if (!reward(player, bean, typ, action, true)) return successTimes;

			// 成功一次
			successTimes++;
		}
		return successTimes;
	}

	private int feelingExp(Player player, int typ, int times, long action) {
		if (times <= 0)
			return times;

		Cfg_Pray_Bean bean = CfgManager.getCfg_Pray_Container().getValueByKey(player.getLevel());
		if (bean == null)
			return times;

		int costID = 0;
		int costNum = 0;

		// 最大普通收费次数
		int max;
		if (typ == EXP) {
			max = Global.Pray_Time_Limit.get(0);
		} else {
			max = Global.Pray_Time_Limit.get(1);
		}

		int now = (int) get(player, timesKey(typ, false));
		Cfg_PrayCost_Bean costBean = CfgManager.getCfg_PrayCost_Container().getValueByKey(now + 1);
		if (now < max && costBean != null) {
			if (typ == EXP) {
				costID = costBean.getPrayExpCost().get(1);
				costNum = costBean.getPrayExpCost().get(0);
			} else {
				costID = costBean.getPrayMoneyCost().get(1);
				costNum = costBean.getPrayMoneyCost().get(0);
			}
		} else {
			costID = ItemCoinType.GemCoin;
			// 当前使用的收费次数
			int cur = (int) get(player, timesKey(typ, false));

			int num = cur - max + 1;
			if (num <= 0) return times;

			if (typ == MONEY) {
				costNum = Manager.vipManager.power().getVipAddNumPrice(num, VipPower.POWER_24);
			} else {
				costNum = Manager.vipManager.power().getVipAddNumPrice(num, VipPower.POWER_25);
			}
		}

		// 检查消耗
		if (!Manager.currencyManager.manager().canDecItemCoin(player, costNum, costID))
			return times;

		Manager.currencyManager.manager().onDecItemCoin(player, costNum, typ == EXP ? ItemChangeReason.WelfareFeelingExpDec : ItemChangeReason.WelfareFeelingCoinDec, action, costID);
		reward(player, bean, typ, action, false);
		return feelingExp(player, typ, times - 1, action);
	}

	private boolean reward(Player player, Cfg_Pray_Bean bean, int typ, long action, boolean isFree) {
		if (bean == null)
			return false;

		long key = timesKey(typ, isFree);
		add(player, key, 1L);

		switch (typ) {
            case EXP:
                add(player, countKey(key), bean.getExp());

                // 添加经验
                Manager.currencyManager.manager().addEXP(player, bean.getExp(), ItemChangeReason.WelfareFeelingExp, action);

                Manager.countManager.addVariant(player, VariantType.EXPPrayNum, 1);
                Manager.controlManager.operate(player, FunctionVariable.EXPPrayNum, 1);
				//记录BI数据
				Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WELFARE_WuDao, ItemChangeReason.WelfareFeelingExp, typ);
				break;
            case MONEY:
                // 给灵石
                if (bean.getMoney().size() == 2) {
                	if (isFree) {
						add(player, countKey(key), Global.Pray_Free_Time_Reward.get(1));
						Manager.currencyManager.manager().onAddItemCoin(player,
                                Global.Pray_Free_Time_Reward.get(0), Global.Pray_Free_Time_Reward.get(1), ItemChangeReason.WelfareFeelingCoinGet, action);
					} else {
						add(player, countKey(key), bean.getMoney().get(0));
						Manager.currencyManager.manager().onAddItemCoin(player,
                                bean.getMoney().get(1), bean.getMoney().get(0), ItemChangeReason.WelfareFeelingCoinGet, action);
					}

					Manager.countManager.addVariant(player, VariantType.MoneyPrayNum, 1);
					Manager.controlManager.operate(player, FunctionVariable.MoneyPrayNum, 1);
					//记录BI数据
					Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WELFARE_WuDao, ItemChangeReason.WelfareFeelingCoinGet, typ);
				}
                break;
        }

		return true;
	}

	private long timesKey(int typ, boolean isFree) {
		return isFree ? typ * 10 : typ * 100;
    }

    private long countKey(long timeKey) {
		return timeKey + 1;
    }

	private int freeTime(Player player, int typ) {
		int count = Manager.vipManager.power().getVipFreeNum(player, VipPower.POWER_10);
	    switch (typ) {
            case EXP:
                return Global.Welfare_Blessing_Times + count;
            case MONEY:
                return Global.Spirit_Stones_Pray_Free + count;
        }
        return 0;
    }

	private long get(Player player, long key) {
		return Manager.countManager.getCount(player, BaseCountType.Feeling_Exp, key);
	}

	private void add(Player player, long key, long value) {
		Manager.countManager.addCount(player, BaseCountType.Feeling_Exp, key, Count.RefreshType.CountType_Day, value);
	}
}
