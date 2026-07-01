package common.shop;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Limit_shop_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.equip.struct.EquipPart;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.shop.script.ILimitShopScript;
import com.game.shop.structs.LimitShop;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.util.TimeUtils;
import game.message.shopMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * @explain: desc
 * @time Created on 2020/2/5 14:29.
 * @author: tc
 */
public class LimitShopScript implements ILimitShopScript {
	private final Logger log = LogManager.getLogger(LimitShopScript.class);

	// 永久有效
	private final long FOREVER = -1;

	// 玩家等级
	private final int COND_LVL = 1;
	// 任务
	private final int COND_TASK = 2;
	// 境界等级
	private final int COND_STATE_VIP = 3;
	// VIP等级
	private final int COND_VIP = 4;
	// 登陆天数
	private final int COND_LOGIN = 5;
	// 购买了指定礼包出现(-1代表最后一个，购买完就消失）
	private final int COND_NEXT = 6;
    //强化x件到N
	private final int COND_INTENSIFY = 7;
	//开服天数
	private final int COND_OPENSERVERDAY = 8;
    //功能开启
    private final int COND_FUNCOPEN = 9;
	//寻宝
	private final int COND_XUNBAO = 10;
	//套装，激活x阶N套
	private final int COND_SUIT = 11;
	//神兽岛
	private final int COND_SOULBEAST = 12;
	//仙甲寻宝每轮结束当天
    private final int COND_XIANJIALASTDAY = 13;
    //仙甲寻宝每轮次数
    private final int COND_XIANJIAXUNBAO = 14;

	/**
	 * 获取scriptId
	 *
	 * @return
	 */
	@Override
	public int getId() {
		return ScriptEnum.LimitShopScript;
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
	 * 购买限购商品
	 *
	 * @param player
	 * @param id
	 */
	@Override
	public void onReqLimitBuy(Player player, int id) {
		fresh(player);

		LimitShop shop = player.getLimitShop();
		if (shop == null)
			return;

		if (!shop.getShops().containsKey(id))
			return;

		long now = TimeUtils.Time();
		long endTime = shop.getShops().get(id);
		if (endTime != FOREVER && endTime <= now) {
			// 商品已经过期
			MessageUtils.notify_player(player, Notify.NORMAL, MessageString.LimitShopTimeOut);
			return;
		}

		Cfg_Limit_shop_Bean bean = CfgManager.getCfg_Limit_shop_Container().getValueByKey(id);
		if (bean == null) {
			repairData(player, shop, id);
			return;
		}

		if (!checkCondition(player, bean.getCondition())) {
			repairData(player, shop, id);
			return;
		}

		if (!Manager.currencyManager.manager().canDecItemCoin(player, bean.getPresentPrice(), ItemCoinType.GemCoin))
			return;

		List<Item> items = Item.createItems(bean.getReward());
		if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0)
			return;

		// 扣钱 Reason
		Manager.currencyManager.manager().onDecItemCoin(player, bean.getPresentPrice(), ItemChangeReason.LimitShopDec, id, ItemCoinType.GemCoin);

		shop.getBuys().add(id);
		shop.getShops().remove(id);

		if (bean.getCondition().get(0).get(0) == COND_XIANJIALASTDAY) {
		    shop.setBuySpecialShopTime(now);
        }

		// reward Reason
		Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.LimitShopGet, id,ItemCoinType.GemCoin,bean.getPresentPrice());
		log.info(player.getInfo() + " 购买神秘限购物品成功：" + id);

		fresh(player);
		syncClient(player, false);
		//记录BI数据
//		Manager.biManager.getScript().biActivity(player, ItemChangeReason.LimitShop, BIActiityTypeEnum.SHOP.getId(), id);
		Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.LimitShop, ItemChangeReason.LimitShop);
	}

	/**
	 * 刷新商品
	 *
	 * @param player
	 */
	@Override
	public void refresh(Player player) {
		if (fresh(player))
			syncClient(player, false);
	}

	/**
	 * online
	 *
	 * @param player
	 */
	@Override
	public void online(Player player) {
		fresh(player);
		syncClient(player, true);
	}

	private boolean fresh(Player player) {
		LimitShop shop = player.getLimitShop();
		if (shop == null)
			return false;

		boolean ret = false;
		long now = TimeUtils.Time();
		for (Cfg_Limit_shop_Bean bean : CfgManager.getCfg_Limit_shop_Container().getValuees()) {

		    //仙甲寻宝每轮最后一天  指定时间点结束(特殊处理)，该类型可重复购买
		    if (bean.getCondition().get(0).get(0) == COND_XIANJIALASTDAY) {
                if (!checkCondition(player, bean.getCondition())) continue;
                if (TimeUtils.isSameDay(shop.getBuySpecialShopTime(), now)) continue;//表示当天买过又移除了
                if (shop.getShops().getOrDefault(bean.getId(), 0L) < now) {
                    shop.getShops().put(bean.getId(), Manager.treasureHuntXianjiaManager.getNextRoundTime());
                    log.info("新一轮寻宝限购开启了！");
                    ret = true;
                }
                continue;
            }

			if (shop.getShops().containsKey(bean.getId())) continue;
			if (shop.getBuys().contains(bean.getId())) continue;
			if (!checkCondition(player, bean.getCondition())) continue;

			ret = true;

			if (bean.getTime() == FOREVER)
				shop.getShops().put(bean.getId(), FOREVER);
			else
				shop.getShops().put(bean.getId(), now + bean.getTime() * 1000L);
		}
		return ret;
	}

	private boolean checkCondition(Player player, ReadIntegerArrayEs condition) {
		if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.LimitShop))
			return false;

		for (ReadArray<Integer> cond : condition.getValuees()) {
			if (cond.size() < 2) continue;
			switch (cond.get(0)) {
				case COND_LVL:
					if (player.getLevel() < cond.get(1)) return false;
					else continue;
				case COND_TASK:
					if (!Manager.taskManager.deal().isTaskFinish(player, cond.get(1))) return false;
					else continue;
				case COND_STATE_VIP:
					if (player.getStateVip().getLv() < cond.get(1)) return false;
					else continue;
				case COND_VIP:
					if (player.getVipLv() < cond.get(1)) return false;
					else continue;
				case COND_LOGIN:
					if (player.getLoginGift().getLoginNum() < cond.get(1)) return false;
					else continue;
				case COND_NEXT:
					if (!player.getLimitShop().getBuys().contains(cond.get(1))) return false;
					else continue;
                case COND_INTENSIFY:
                    int num = 0;
                    for (EquipPart part : player.getEquipParts()) {
                        if (part.getLevel() >= cond.get(2)) {
                            num ++;
                        }
                    }
                    return num >= cond.get(1);
                case COND_OPENSERVERDAY:
                    return TimeUtils.getOpenServerDay() == cond.get(1);
                case COND_FUNCOPEN:
                    return Manager.controlManager.deal().isOpenFunction(player, cond.get(1));
                case COND_XUNBAO:
                    long count = Manager.countManager.getCount(player, BaseCountType.PTotalTreasureTypeHuntTimes, cond.get(1));
                    return count >= cond.get(2);
                case COND_SUIT:
                    return Manager.equipManager.deal().gainSuitNum(player, cond.get(1)) >= cond.get(2);
                case COND_SOULBEAST:
                    return Utils.findOne(player.getSoulBeastInfo().getSoulBeasts().values(), s -> s.isWork()) != null;
                case COND_XIANJIALASTDAY:
                    if (Manager.treasureHuntXianjiaManager.getRound() != cond.get(1)) {
                        return false;
                    }
                    return Manager.treasureHuntXianjiaManager.getNextRoundTime() < TimeUtils.Time() + (24 * 60 * 60 * 1000);
                case COND_XIANJIAXUNBAO:
                    int huntXianjiaCount = player.getTreasureHuntXianjiaData().getHuntXianjiaCount();
                    return huntXianjiaCount >= cond.get(1);
                default:
					return false;
			}
		}
		return true;
	}

	private void repairData(Player player, LimitShop shop, int id) {
		shop.getShops().remove(id);
		shop.getBuys().remove((Object) id);
		log.error(player.getInfo() + " 策划配置错误，先拥有，后缺失，修复数据：" + id);
	}

	private void syncClient(Player player, boolean online) {
		LimitShop shop = player.getLimitShop();
		if (shop == null)
			return;

		long now = TimeUtils.Time();
		shopMessage.SyncLimitShop.Builder builder = shopMessage.SyncLimitShop.newBuilder();
		for (Map.Entry<Integer, Long> entry : shop.getShops().entrySet()) {
            long endTime = entry.getValue();
            boolean isOverTime = false;
            //同一天只延迟时间一次
            boolean hasDelayTime = TimeUtils.isSameDay(shop.getExpiredShops().getOrDefault(entry.getKey(), 0L), now);
            if (online && endTime > player.getOffLineTime() && now > endTime && !hasDelayTime) {
                endTime = now + 5 * 60 * 1000;
                shop.getShops().put(entry.getKey(), endTime);
                shop.getExpiredShops().put(entry.getKey(), endTime);
                isOverTime = true;
            }
			if (endTime != FOREVER && endTime <= now) continue;
			shopMessage.LimitShop.Builder ls = shopMessage.LimitShop.newBuilder();
			ls.setId(entry.getKey());
			ls.setEndTime(endTime);
			ls.setIsOverTime(isOverTime);
			builder.addShops(ls);
		}
		builder.addAllBuyIds(shop.getBuys());
		MessageUtils.send_to_player(player, shopMessage.SyncLimitShop.MsgID.eMsgID_VALUE, builder.build().toByteArray());
	}
}
