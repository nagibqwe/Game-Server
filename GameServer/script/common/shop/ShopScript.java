package common.shop;

import com.data.*;
import com.data.bean.Cfg_Item_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.db.bean.ShopBean;
import com.game.guild.structs.Guild;
import com.game.guild.structs.GuildSysConfig;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.shop.log.shopbuylog;
import com.game.shop.script.IShopScript;
import com.game.shop.structs.ShopDefine;
import com.game.structs.GlobalType;
import com.game.utils.MessageUtils;
import com.game.vip.manager.VipManager;
import com.game.vip.structs.VipPower;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.shopMessage;
import game.message.shopMessage.ReqBuyItem;
import game.message.shopMessage.ReqRefreshShop;
import game.message.shopMessage.ResBuyFailure;
import game.message.shopMessage.ResShopSubList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 商城，商店的脚本管理器
 *
 * @author admin
 */
public class ShopScript implements IShopScript {
	private final Logger log = LogManager.getLogger(ShopScript.class);

	@Override
	public int getId() {
		return ScriptEnum.ShopScript;
	}

	@Override
	public Object call(Object... objects) {
		return null;
	}

	/**
	 * 请求标签处理函数
	 *
	 * @param player   玩家
	 * @param messInfo 消息
	 */
	@Override
	public void onReqShopSubList(Player player, shopMessage.ReqShopSubList messInfo) {
		ResShopSubList.Builder msg = ResShopSubList.newBuilder();
		for (Map.Entry<Integer, ConcurrentHashMap<Integer, ArrayList<Integer>>> shop : Manager.shopManager.getShopLabelSellIDList().entrySet()) {
			Iterator<Map.Entry<Integer, ArrayList<Integer>>> labelIter = shop.getValue().entrySet().iterator();
			shopMessage.shopSubMess.Builder mess = shopMessage.shopSubMess.newBuilder();
			mess.setShopId(shop.getKey());
			while (labelIter.hasNext()) {
				Map.Entry<Integer, ArrayList<Integer>> label = labelIter.next();
				mess.addLabelList(label.getKey());
			}
			msg.addSublist(mess);
		}

		MessageUtils.send_to_player(player, ResShopSubList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
	}

	/**
	 * 请求商城列表
	 *
	 * @param player   玩家
	 * @param messInfo 消息
	 */
	@Override
	public void OnReqShopList(Player player, shopMessage.ReqShopList messInfo) {
		int shopId = messInfo.getShopId();
		int page = messInfo.getLabelId();
		sendShopList(player, shopId, page, messInfo.getGradeLimit() == 0);
	}

	/**
	 * 购买物品
	 *
	 * @param player   玩家
	 * @param messInfo 消息
	 */
	@Override
	public void OnReqBuyItem(Player player, ReqBuyItem messInfo) {
		int result = buyItem(player, messInfo.getSellId(), messInfo.getNum());
		if (result != ShopDefine.Succ) {
			buyFailure(player, messInfo.getSellId(), result);
			return;
		}

		ShopBean bean = Manager.shopManager.getShopBean(messInfo.getSellId());
		freshSellData(player, bean);

		// 发送购买成功
		shopMessage.ResBuySuccess.Builder resBuy = shopMessage.ResBuySuccess.newBuilder();
		resBuy.setSellId(messInfo.getSellId());
		resBuy.setNum(messInfo.getNum());
		MessageUtils.send_to_player(player, shopMessage.ResBuySuccess.MsgID.eMsgID_VALUE, resBuy.build().toByteArray());

		Manager.controlManager.operate(player, FunctionVariable.GetEquipsId, bean.getItemid());
	}

	/**
	 * 刷新商城
	 *
	 * @param player
	 * @param messInfo
	 */
	@Override
	public void OnReqRefreshShop(Player player, ReqRefreshShop messInfo) {
		log.error("该功能未开放！ OnReqRefreshShop");
	}

	/**
	 * 返回所有商城数据
	 *
	 * @return
	 */
	@Override
	public List<ShopBean> allMalls() {
		List<ShopBean> beans = new ArrayList<>();
		for (ShopBean bean : Manager.shopManager.getShopItems().values()) {
			if (bean.isMall())
				beans.add(bean);
		}
		return beans;
	}

	/**
	 * 查询某个商品
	 *
	 * @param sellId
	 * @return
	 */
	@Override
	public ShopBean mall(int sellId) {
		return Manager.shopManager.getShopBean(sellId);
	}

	/**
	 * GM后台更新某商城数据
	 *
	 * @param bean
	 * @return
	 */
	@Override
	public boolean updateShop(ShopBean bean) {
		if (!bean.isMall()) {
			log.error("updateShop 不是商城数据 " + bean.toString());
			return false;
		}

		if (Manager.shopManager.getShopBean(bean.getId()) == null) {
			if (Manager.shopManager.getDao().insert(bean) == 1) {
				Manager.shopManager.insertCache(bean);
				return true;
			}

			log.error("插入商城（插入新数据）失败:" + bean.toString());
			return false;
		} else {
			if (Manager.shopManager.getDao().delete(bean.getId()) == 1) {
				// 删除原来的关系
				Manager.shopManager.deleteCache(bean.getId());

				// 插入新的
				if (Manager.shopManager.getDao().insert(bean) == 1) {
					Manager.shopManager.insertCache(bean);
					return true;
				}

				log.error("更新商城（已经删除老数据，插入新数据）失败:" + bean.toString());
				return false;
			}
		}
		log.error("更新商城（删除原数据）失败:" + bean.toString());
		return false;
	}

	/**
	 * GM后台删除某商城数据
	 *
	 * @param sellId
	 * @return
	 */
	@Override
	public boolean deleteShop(int sellId) {
		if (Manager.shopManager.getDao().delete(sellId) == 1) {
			// 删除原来的关系
			Manager.shopManager.deleteCache(sellId);
			return true;
		}

		log.error("删除商城（删除原数据）失败:" + sellId);
		return false;
	}

	private int buyItem(Player player, int sellId, int num) {
		if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Shop))
			return ShopDefine.ShopReasonFunction;

		ShopBean shop = Manager.shopManager.getShopBean(sellId);
		if (shop == null)
			return ShopDefine.ShopReasonNoSell;

		Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(shop.getItemid());
		if (itemBean == null)
			return ShopDefine.ShopReasonNoItem;


		int can = canBuy(player, shop, num);
		if (can != 0)
			return can;

		//计算实际需要付出的价格
		long money = num * shop.getDiscountprice();

		//根据购买次数算价格
		int count = (int) Manager.countManager.getCount(player, BaseCountType.SHOP_COUNT, shop.getId()) + 1;
		if (count > 0 && shop.getCountDiscountArrayEs() !=null  && shop.getCountDiscountArrayEs().size()>0){
			ReadIntegerArrayEs countDiscount =  shop.getCountDiscountArrayEs();
			ReadArray<Integer> countDiscountArr  = null ;
			for (ReadArray<Integer> readArray :countDiscount.getValuees()){
				if (count >= readArray.get(0) && count <= readArray.get(1)){
					countDiscountArr = readArray;
					break;
				}
			}
			if (countDiscountArr == null){
				countDiscountArr = countDiscount.get(countDiscount.size()-1);
			}
			money = money + countDiscountArr.get(2);
		}

		if (shop.getIsdiscount() == 1) {
			int vipPer =  Manager.vipManager.power().getVipDiscount(player, VipPower.POWER_28);
			money = (long) Math.ceil(money * (double) vipPer / Manager.vipManager.power().getDefaultDiscount());
		}

		if (!Manager.currencyManager.manager().canDecItemCoin(player, money, shop.getCurrencyid()))
			return ShopDefine.ShopReasonNoCoin;

		long now = TimeUtils.Time();

		// 计算过期时间
		long lostTime = 0;
		if (shop.getDuration() > 0) {
			lostTime = now + shop.getDuration();
		} else if (shop.getOverdue().length() > 5) {
			try {
				Date lost = TimeUtils.getDateByString(shop.getOverdue());
				lostTime = lost.getTime();//设置过期时间
			} catch (ParseException ex) {
				log.error("读取过期时间的格式出问题了", ex);
				return ShopDefine.ShopReasonFailed;
			}
		}

		// 购买的物品数量
		List<Item> createItems = Item.createItems(shop.getItemid(), num, (shop.getBind() > 0), lostTime);
		int msId = Manager.backpackManager.manager().onHasAddSpaces(player, createItems);
		if (msId != 0)
			return ShopDefine.ShopReasonNoBag;

		int addReason = ItemChangeReason.ShopBuyCostGet;
		int decReason = ItemChangeReason.ShopBuyCostDec;
		if (shop.getCurrencyid() == ItemCoinType.GemCoin){
			addReason = ItemChangeReason.ShopBuyGoldGet;
			decReason = ItemChangeReason.ShopBuyGoldDec;
		}


		long action = IDConfigUtil.getLogId();

		// 扣除货币
		Manager.currencyManager.manager().onDecItemCoin(player, money, decReason, action, shop.getCurrencyid());
		MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, MessageString.SHOP_KOUCHU, num + "",
				Manager.backpackManager.manager().getName(shop.getItemid()), money + "", Manager.backpackManager.manager().getName(shop.getCurrencyid()));

		// 加物品
		Manager.backpackManager.manager().addItems(player, createItems, addReason, action, shop.getCurrencyid(), money);

		if (shop.getBuynum() > 0 && shop.getLimittype() > 0) {
			Count.RefreshType rType = Count.RefreshType.convert(shop.getLimittype() - 1);
			Manager.countManager.addCount(player, BaseCountType.SHOP_COUNT, sellId, rType, num);
		}

		// 写日志
		shopbuylog buyLog = new shopbuylog();
		buyLog.setAction(action);
		buyLog.setBuyTimes(num);
		buyLog.setItemModelId(shop.getItemid());
		buyLog.setMoneyNum(money);
		buyLog.setMoneyType(shop.getCurrencyid());
		buyLog.setPlatfrom(player.getPlatformName());
		buyLog.setRealNum(num);
		buyLog.setRoleId(player.getId());
		buyLog.setRoleName(player.getName());
		buyLog.setUserId(player.getUserId());
		buyLog.setRolelevel(player.getLevel());
		buyLog.setSellId(sellId);
		buyLog.setShopId(shop.getShopid());
		buyLog.setSid(player.getCreateServerId());
		buyLog.setSrcId(shop.getLabelid());
		buyLog.setReason(addReason);
		LogService.getInstance().execute(buyLog);

		Manager.countManager.addCount(player, BaseCountType.PurShopItem_Times, shop.getItemid(), Count.RefreshType.CountType_Forever, num);
		Manager.controlManager.operate(player, FunctionVariable.ManaStoneShopBuy, num);
		Manager.controlManager.operate(player, FunctionVariable.GuildShopBuy, num);
		if(shop.getLabelid() == 1242100){//声望商城
			Manager.countManager.addCount(player, BaseCountType.IntegralShopBuyItem, shop.getItemid(), Count.RefreshType.CountType_Forever, num);
			Manager.controlManager.operate(player,FunctionVariable.IntegralShopBuy, num);
		}else if (shop.getLabelid() == 1241300) {//银元商城
			Manager.controlManager.operate(player, FunctionVariable.Buy_Any_Good, num);
		}else if (shop.getLabelid() == 1241400) {//荣誉商城
			Manager.countManager.addCount(player, BaseCountType.GloryShopBuyItem, shop.getItemid(), Count.RefreshType.CountType_Forever, num);
			Manager.controlManager.operate(player, FunctionVariable.GloryShop_Buy, num);
		}

		Manager.biManager.getScript().biMall(player, shop.getLimittype(), shop.getShopid(), shop.getItemid(), num,
				shop.getCurrencyid(), (int) money, shop.getDiscountprice(), sellId);
		Manager.biManager.get4399Script().shopBuyTo4399(player, shop, num, (int) money);

		log.info(player.getInfo() + " shop buy succ:" + sellId + " num:" + num + " itemID:" + shop.getItemid());
		return ShopDefine.Succ;
	}

	/**
	 * @param player 玩家
	 * @param sellId 售卖id
	 * @param reason 原因
	 */
	private void buyFailure(Player player, int sellId, int reason) {
		ResBuyFailure.Builder msg = ResBuyFailure.newBuilder();
		msg.setReason(reason);
		msg.setSellId(sellId);
		MessageUtils.send_to_player(player, ResBuyFailure.MsgID.eMsgID_VALUE, msg.build().toByteArray());
		log.error(player.getInfo() + " 购买商店的ID:" + sellId + " 失败了,错误码:" + reason);
	}

	private boolean isValid(ShopBean bean) {
		if (bean.getUptime().length() <= 5 || bean.getDowntime().length() <= 5)
			return true;

		try {
			Date start = TimeUtils.getDateByString(bean.getUptime());
			Date end = TimeUtils.getDateByString(bean.getDowntime());
			long now = TimeUtils.Time();
			return start.getTime() <= now && now < end.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}
	private boolean isExpired(ShopBean bean){
		if (bean.getOpenday()<=0){
			return true;
		}
		int curOpenServerDay = TimeUtils.getOpenServerDay();
		if (curOpenServerDay >= bean.getOpenday()){
			if (bean.getCloseday() <= 0 ){
				return true;
			}
			if (curOpenServerDay  <= bean.getCloseday()){
				return true;
			}
		}
		return false;
	}



	private shopMessage.shopItemInfo.Builder builder(ShopBean bean) {
		shopMessage.shopItemInfo.Builder mess = shopMessage.shopItemInfo.newBuilder();
		mess.setSellId(bean.getId());
		mess.setItemId(bean.getItemid());
		mess.setShopId(bean.getShopid());
		mess.setLabelId(bean.getLabelid());
		mess.setLevel(bean.getLevel());
		mess.setGuildLevel(bean.getGuildlevel());
		mess.setGuildShopLvlStart(bean.getGuildshoplvlstart());
		mess.setGuildShopLvlEnd(bean.getGuildshoplvlend());
		mess.setWorldlvlstart(bean.getWorldlvlstart());
		mess.setWorldlvlend(bean.getWorldlvlend());
		mess.setIsdiscount(bean.getIsdiscount());
		mess.setMilitaryRankLevel(bean.getMilitarylevel());
		mess.setVipLevel(bean.getViplevel());
		mess.setLimitType(bean.getLimittype());
		mess.setBuyLimit(bean.getBuynum());
		mess.setCoinType(bean.getCurrencyid());
		mess.setCoinNum(bean.getDiscountprice()); // 打折后价格
		mess.setOriginalCoinNum(bean.getPrice()); // 打折前价格
		mess.setDiscount(bean.getDiscount());
		mess.setHot(bean.getPromotion());
		mess.setSort(bean.getSort());
		mess.setLostTime(bean.getDowntime());
		mess.setDuration(bean.getDuration());
		mess.setBind(bean.getBind());
		mess.setRefreshCurrency(bean.getRefreshcurrency());
		mess.setRefreshNum(bean.getRefreshnum());
		mess.setShopType(bean.getShoptype());
		mess.setCountdiscount(bean.getCountdiscount());
		return mess;
	}

	private shopMessage.ShopData.Builder builder(Player player, int sellId) {
		shopMessage.ShopData.Builder builder = shopMessage.ShopData.newBuilder();
		int count = (int) Manager.countManager.getCount(player, BaseCountType.SHOP_COUNT, sellId);
		builder.setSellId(sellId);
		builder.setBuyNum(count);
		return builder;
	}

	/**
	 * 刷新某个商品
	 *
	 * @param player
	 * @param bean
	 */
	private void freshSell(Player player, ShopBean bean) {
		if (!bean.isMall())
			return;

		shopMessage.ResFreshItemInfo.Builder builder = shopMessage.ResFreshItemInfo.newBuilder();
		builder.setItemInfo(builder(bean));
		MessageUtils.send_to_player(player, shopMessage.ResFreshItemInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
	}

	/**
	 * 单独刷新某个商品的数据
	 * @param player
	 * @param bean
	 */
	private void freshSellData(Player player, ShopBean bean) {
		shopMessage.SyncShopData.Builder builder = shopMessage.SyncShopData.newBuilder();
		builder.setData(builder(player, bean.getId()));
		builder.setShopId(bean.getShopid());
		builder.setLabelId(bean.getLabelid());
		MessageUtils.send_to_player(player, shopMessage.SyncShopData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
	}

	/**
	 * 能否购买
	 *
	 * @param player
	 * @param bean
	 * @param num
	 * @return
	 */
	private int canBuy(Player player, ShopBean bean, int num) {
		if (num <= 0)
			return ShopDefine.ShopReasonNoNum;

		if (player.getLevel() < bean.getLevel())
			return ShopDefine.ShopReasonLimitLvl;

		int ret = canBuy(player, bean);
		if (ret != ShopDefine.Succ)
			return ret;

		int count = (int) Manager.countManager.getCount(player, BaseCountType.SHOP_COUNT, bean.getId());
		if (bean.getBuynum() >= 0 && count + num > bean.getBuynum())
			return ShopDefine.ShopReasonNoNum;

		return ShopDefine.Succ;
	}

	/**
	 * 硬条件
	 *
	 * @param player
	 * @param bean
	 * @return
	 */
	private int canBuy(Player player, ShopBean bean) {
		int guildLvl = 0;
		int gsLvl = 0;
		Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
		if (guild != null) {
			guildLvl = guild.getLevel();
			gsLvl = guild.getConstructions().get(GuildSysConfig.TYPE_SHOP);
		}
		if (guildLvl < bean.getGuildlevel())
			return ShopDefine.ShopReasonLimitGuildLvl;

		if (gsLvl < bean.getGuildshoplvlstart() || gsLvl > bean.getGuildshoplvlend())
			return ShopDefine.ShopReasonGuildShopLvl;

		int worldLvl = GlobalType.getWorldLevel();
		if (worldLvl < bean.getWorldlvlstart() || worldLvl > bean.getWorldlvlend())
			return ShopDefine.ShopReasonWorldLvl;

		if (player.getVipLv() < bean.getViplevel())
			return ShopDefine.ShopReasonLimitVip;


		if (bean.getViplevel() > 0){
			if(!player.getVipPearl().canFree()){
				return ShopDefine.ShopReasonLimitVip;
			}
		}
		if (bean.getOccupation() >= 0 && player.getCareer() != bean.getOccupation())
			return ShopDefine.ShopReasonLimitCareer;

		if (!isValid(bean))
			return ShopDefine.ShopReasonNotInTime;

		if (!isExpired(bean)){
			return ShopDefine.ShopReasonNotInTime;
		}
		return ShopDefine.Succ;
	}

	/**
	 * 向客户发送商店的具体信息
	 *
	 * @param player       玩家
	 * @param shopId       商店id
	 * @param labelId      page
	 * @param isLevelLimit isLevel
	 */
	private void sendShopList(Player player, int shopId, int labelId, boolean isLevelLimit) {
		shopMessage.ResShopItemList.Builder msg = shopMessage.ResShopItemList.newBuilder();
		ArrayList<Integer> list = Manager.shopManager.getSellIDList(shopId, labelId);
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				ShopBean bean = Manager.shopManager.getShopBean(list.get(i));
				if (bean.getShopid() != shopId)
					continue;

				if (bean.getLabelid() != labelId)
					continue;

				if (isLevelLimit && bean.getLevel() > player.getLevel())
					continue;

				if (canBuy(player, bean) != ShopDefine.Succ)
					continue;

				if (bean.isMall()) {
					msg.addItemList(builder(bean));
				}
				msg.addDataList(builder(player, bean.getId()));
			}
		msg.setLabelId(labelId);
		msg.setShopId(shopId);
		MessageUtils.send_to_player(player, shopMessage.ResShopItemList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
	}
}
