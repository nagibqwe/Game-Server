package common.commercialize;

import com.data.*;
import com.data.bean.Cfg_RechargeAward_Bean;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.commercialize.inter.IFCCharge;
import com.game.commercialize.struct.Charge;
import com.game.commercialize.struct.FCCharge;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.recharge.log.RechargeRewardLog;
import com.game.script.structs.ScriptEnum;
import com.game.structs.GlobalType;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.util.TimeUtils;
import game.message.CommercializeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class FCChargeScript implements IFCCharge {
	private static final Logger logger = LogManager.getLogger(FCChargeScript.class);

	/**
	 * 首充
	 */
	private final int _FCTypFirst = 0;
	/**
	 * 续充
	 */
	private final int _FCTypNext = 1;

	/**
	 * 百元首冲
	 */
	private final int _FCTypHundredFirst = 2;

	/**
	 * 请求领取首充、续充奖励
	 *
	 * @param player
	 * @param cfgID
	 */
	@Override
	public void onReqFCChargeReward(Player player, int cfgID) {
		if (player == null)
			return;
		FCCharge charge = player.getFcCharge();
		if (charge == null || charge.isReward(cfgID))
			return;
		if (!Manager.commercializeManager.isOpen(player, CommercializeMessage.Commercialize.FCCharge))
			return;
		Cfg_RechargeAward_Bean cfg = CfgManager.getCfg_RechargeAward_Container().getValueByKey(cfgID);
		if (cfg == null)
			return;
		Charge cc = charge.get(cfg.getAwardType());
		if (cc == null)
			return;
		if (cc.getCfgID() == -1 || cc.isReward() || cc.getGold() < cfg.getNeedRecharge() || cc.getCfgID() != cfgID)
			return;

		// 发奖
		List<Item> items = Item.createItems(cfg.getItemAward());
		List<Item> equips = Item.createItems(player.getCareer(), cfg.getEquipAward(), 1);
		items.addAll(equips);
		if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0)
			return;

		charge.addReward(cfgID);
		if (cfg.getAwardType() == _FCTypFirst) {
			// 检查首充是否已经领完奖
			int cId = getNextID(cfgID, _FCTypFirst);
			if (cId == -1) {
				charge.setFcComplete(true);
				Manager.controlManager.deal().changePlayerFunc(player);
			}
		}
		cc.setStartTime(TimeUtils.Time());
		cc.setReward(true);

		// 如果是续充，可以一直领
		if (cfg.getAwardType() == _FCTypNext) {
			// 退回前一天
			cc.setStartTime(cc.getStartTime() - GlobalType.MILLIS_PER_DAY);
			reset(cc, _FCTypNext);
		}

		if (cfg.getAwardType() == _FCTypHundredFirst){
			int cId = getNextID(cfgID, _FCTypHundredFirst);
			if (cId == -1) {
				charge.setFcHundredComplete(true);
			}
		}

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.MEDICINESATTRIBUTE);
		Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.FirstRechargeGet, cfgID);

		Manager.controlManager.operate(player, FunctionVariable.ReceiveFirstRechargeReward, 1);

		//记录日志
		RechargeRewardLog rewardLog = new RechargeRewardLog();
		rewardLog.setPlayerInfo(player.getPlatformName(), player.getCreateServerId(), player.getUserId(), player.getId(), player.getName());
		rewardLog.setRewardId(cfgID);
		rewardLog.setActionId(cfgID);
		LogService.getInstance().execute(rewardLog);

		//公告
		if (cfg.getRadio() != 0 || cfg.getChatchannel() != null) {
			String rewardStr = Utils.makeItemsStr(items);
			StringBuilder awardType = new StringBuilder();
			switch (cfg.getAwardType()) {
				case _FCTypFirst:
					awardType.append("2&_首充");
					break;
				case _FCTypNext:
				case _FCTypHundredFirst:
					awardType.append("2&_续充");	//欧阳帆喊这样改
					break;
				default:
			}
			//@todo 修改公告

			MessageUtils.notify_allOnlinePlayer(cfg.getRadio(),cfg.getChatchannel(), MessageString.RECHARGE_AWARDGET_NOTICE,player.getId()+"", player.getName(), awardType.toString(), rewardStr, Utils.makeUrlStr(MessageString.RECHARGE_AWARDGET_NOTICE));
		}

		onReqCommercialize(player);
		//记录bi数据
		if (cfg.getAwardType() == _FCTypFirst) {
//			Manager.biManager.getScript().biActivity(player, ItemChangeReason.FirstRechargeGet, BIActiityTypeEnum.CHARGE.getId(), cfgID);
			Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.FIRST_CHARGE, ItemChangeReason.FirstRechargeGet, cfgID);
		}
		else if (cfg.getAwardType() == _FCTypHundredFirst) {
//			Manager.biManager.getScript().biActivity(player, ItemChangeReason.HundredFirstRecharge, BIActiityTypeEnum.CHARGE.getId(), cfgID);
			Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.FIRST_CHARGE, ItemChangeReason.HundredFirstRecharge, cfgID);
		}
	}

	/**
	 * 玩家上线
	 *
	 * @param player
	 */
	@Override
	public void playerOnline(Player player) {
		if (player.getFcCharge() == null)
			player.setFcCharge(FCCharge.New());

		reset(player);
	}

	/**
	 * 请求商业化内容
	 *
	 * @param player
	 */
	@Override
	public void onReqCommercialize(Player player) {
		if (player == null)
			return;

		FCCharge charge = player.getFcCharge();
		if (charge == null)
			return;

		reset(player);
		CommercializeMessage.ResFCChargeData.Builder builder = CommercializeMessage.ResFCChargeData.newBuilder();
		CommercializeMessage.FCChargeData.Builder first = CommercializeMessage.FCChargeData.newBuilder();
		first.setCfgID(charge.getFirst().getCfgID());
		first.setGoldCount(charge.getFirst().getGold());
		first.setIsReward(charge.getFirst().isReward());
		CommercializeMessage.FCChargeData.Builder next = CommercializeMessage.FCChargeData.newBuilder();
		next.setCfgID(charge.getNext().getCfgID());
		next.setGoldCount(charge.getNext().getGold());
		next.setIsReward(charge.getNext().isReward());
		CommercializeMessage.FCChargeData.Builder hundred = CommercializeMessage.FCChargeData.newBuilder();
		hundred.setCfgID(charge.getHundred().getCfgID());
		hundred.setGoldCount(charge.getHundred().getGold());
		hundred.setIsReward(charge.getHundred().isReward());
		builder.setFirstData(first);
		builder.setNextData(next);
		builder.setHundredData(hundred);
		MessageUtils.send_to_player(player, CommercializeMessage.ResFCChargeData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
	}

	/**
	 * 获取scriptId
	 *
	 * @return
	 */
	@Override
	public int getId() {
		return ScriptEnum.FCChargeBaseScript;
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

	private void reset(Player player) {
		if (player == null)
			return;
		if (!Manager.commercializeManager.isOpen(player, CommercializeMessage.Commercialize.FCCharge))
			return;
		FCCharge charge = player.getFcCharge();
		if (charge == null)
			return;

		reset(charge.getFirst(), _FCTypFirst);
		reset(charge.getNext(), _FCTypNext);
		reset(charge.getHundred(), _FCTypHundredFirst);
	}

	private void reset(Charge charge, int typ) {
		if (charge == null || !charge.isReward())
			return;

		long now = TimeUtils.Time();

		// 当天已经重置过了
		if (TimeUtils.isSameDay(now, charge.getStartTime()))
			return;

		int cfgID = getNextID(charge.getCfgID(), typ);

		// 重置
		charge.setStartTime(now);
		if (cfgID != -1) {
			charge.setCfgID(cfgID);
			charge.setReward(false);

			// 续充不重置金额
			if (typ != _FCTypNext) charge.setGold(0);
		}
	}

	private int getNextID(int curID, int typ) {
		for (Cfg_RechargeAward_Bean bean : CfgManager.getCfg_RechargeAward_Container().getValuees()) {
			if (bean.getAwardType() == typ && bean.getId() > curID) {
				return bean.getId();
			}
		}
		return -1;
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
		if (player == null)
			return;
//		if (!Manager.commercializeManager.isOpen(player, CommercializeMessage.Commercialize.FCCharge))
//			return;
		FCCharge charge = player.getFcCharge();
		if (charge == null)
			return;

		boolean success = false;

		// TODO 判断是否会累积充值的元宝数量

		long realAdd = totalFee;
		if (!charge.isFcComplete() && !charge.getFirst().isReward()) {
			long old = charge.getFirst().getGold();
			charge.getFirst().setGold(old + totalFee);
			success = true;

			Cfg_RechargeAward_Bean cfg = CfgManager.getCfg_RechargeAward_Container().getValueByKey(charge.getFirst().getCfgID());
			if (cfg != null && old < cfg.getNeedRecharge()) {
				realAdd = Math.max(0, charge.getFirst().getGold() - cfg.getNeedRecharge());
			}
			Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.MEDICINESATTRIBUTE);
		}

		if (checkFCChargeComplete(charge)) {
			Manager.controlManager.deal().changePlayerFunc(player);

			// 首充已经完成，累计续充
			if (!charge.getNext().isReward()) {
				charge.getNext().setGold(charge.getNext().getGold() + realAdd);
				success = true;
			}
		}

		if (!charge.isFcHundredComplete() && !charge.getHundred().isReward()){
			long old = charge.getHundred().getGold();
			charge.getHundred().setGold(old + totalFee);
			success = true;
		}
		if (success)
			onReqCommercialize(player);
	}

	/**
	 * 首充续充功能是否已经打开
	 * @param player
	 * @param function
	 * @return
	 */
	public boolean checkFCChargeFunc(Player player, int function) {
		if (player == null)
			return false;

		FCCharge charge = player.getFcCharge();
		if (charge == null)
			return false;

		if (function == FunctionStart.FirstCharge) {
			// 首充，已经完成则关闭，否则打开
			if (charge.isFcComplete()  && charge.isFcHundredComplete()){
				return false;
			}else{
				return true;
			}
		} else if (function == FunctionStart.ReCharge) {
			// 续充，必须完成一次首充
			return checkFCChargeComplete(charge);
		}
		return false;
	}

	/**
	 * 检查首充是否完成
	 * @param charge
	 * @return
	 */
	private boolean checkFCChargeComplete(FCCharge charge) {
		if (charge == null)
			return false;

		if (charge.getRewards().size() > 0)
			return true;

		Charge first = charge.getFirst();
		if (first == null || first.getCfgID() == -1)
			return false;

		if (first.isReward())
			return true;

		Cfg_RechargeAward_Bean cfg = CfgManager.getCfg_RechargeAward_Container().getValueByKey(first.getCfgID());
		if (cfg == null)
			return false;

		return first.getGold() >= cfg.getNeedRecharge();
	}
}
