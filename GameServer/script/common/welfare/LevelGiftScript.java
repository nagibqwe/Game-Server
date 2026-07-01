package common.welfare;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Level_reward_Bean;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.welfare.script.ILevelGiftScript;
import game.message.WelfareMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @explain: desc
 * @time Created on 2019/12/24 17:38.
 * @author: tc
 */
public class LevelGiftScript implements ILevelGiftScript {
	/**
	 * 获取scriptId
	 *
	 * @return
	 */
	@Override
	public int getId() {
		return ScriptEnum.LevelGiftBaseScript;
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
	 * 领取等级礼包
	 *
	 * @param player
	 * @param level
	 */
	@Override
	public void onReqReceiveLevelGift(Player player, int level) {
		if (player.getLevel() < level)
			return;

		Cfg_Level_reward_Bean bean = CfgManager.getCfg_Level_reward_Container().getValueByKey(level);
		if (bean == null)
			return;

		if (bean.getLimitValue() == 0)
			return;
		//vip领取 和 普通领取奖励 都领取 返回
		if (player.getLevelGifts().contains((Object) level) && player.getLevelVipGifts().contains((Object)level)){
			return;
		}
		List<Item> allItems = new ArrayList<>();
		List<Item> levelitems = null;
		//判断普通领取 是否领取过了
		if(!player.getLevelGifts().contains((Object) level)){
			levelitems = Item.createItems(player.getCareer(), bean.getQ_reward(), 1);
			allItems.addAll(levelitems);
		}
		List<Item> vipitems = null;
		//判断vip领取 是否领取过了
		if(!player.getLevelVipGifts().contains((Object) level)){
			if(player.getVipLv() >= bean.getVipLimit()){
				vipitems = Item.createItems(player.getCareer(), bean.getQ_reward_vip(), 1);
				allItems.addAll(vipitems);
			}
		}
		//没有奖励可以领取
		if(allItems.size() == 0){
			return;
		}
		if (Manager.backpackManager.manager().onHasAddSpaces(player, allItems) != 0) {
			MessageUtils.notify_player(player, Notify.ERROR, MessageString.MainTaskNoBagCell);
			return;
		}

		if (bean.getLimitValue() > 0) {
			if (remainOrInit(level, bean.getLimitValue()) <= 0) {
				MessageUtils.notify_player(player, Notify.ERROR, MessageString.LevelGiftReceiveFailed);
				return;
			}
			dec(level);
		}
		if(levelitems!=null && levelitems.size()>0){
			player.getLevelGifts().add(level);
			Manager.backpackManager.manager().addItems(player, levelitems, ItemChangeReason.LevelGiftAdd, level);
		}
		if(vipitems!=null && vipitems.size()>0){
			player.getLevelVipGifts().add(level);
			Manager.backpackManager.manager().addItems(player, vipitems, ItemChangeReason.LevelGiftVipAdd, level);
		}

		freshDataNtf(player);
		//BI
		Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WELFARE_LevelGift, ItemChangeReason.LevelGiftAdd, level);
	}

	/**
	 * 玩家上线
	 *
	 * @param player
	 */
	@Override
	public void playerOnline(Player player) {
		freshDataNtf(player);
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

		WelfareMessage.ResWelfareLevelGiftData.Builder builder = WelfareMessage.ResWelfareLevelGiftData.newBuilder();
		for (Cfg_Level_reward_Bean bean : CfgManager.getCfg_Level_reward_Container().getValuees()) {
			WelfareMessage.LevelGiftInfo.Builder lg = WelfareMessage.LevelGiftInfo.newBuilder();
			lg.setLevel(bean.getQ_level());
			if (bean.getLimitValue() > 0)
				lg.setRemain(ServerParamUtil.levelGiftMap.getOrDefault(bean.getQ_level(), bean.getLimitValue()));
			else
				lg.setRemain(bean.getLimitValue());
			lg.setReceive(player.getLevelGifts().contains((Object) bean.getQ_level()));
			//vip礼包是否领取
			lg.setVipReceive(player.getLevelVipGifts().contains((Object) bean.getQ_level()));

			builder.addData(lg);
		}
		MessageUtils.send_to_player(player, WelfareMessage.ResWelfareLevelGiftData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
	}

	private int remainOrInit(int level, int limit) {
		if (ServerParamUtil.levelGiftMap.containsKey(level)) {
			return ServerParamUtil.levelGiftMap.get(level);
		}

		ServerParamUtil.levelGiftMap.put(level, limit);
		ServerParamUtil.saveLevelGift();
		return limit;
	}

	private void dec(int level) {
		if (!ServerParamUtil.levelGiftMap.containsKey(level))
			return;

		int count = ServerParamUtil.levelGiftMap.get(level);
		ServerParamUtil.levelGiftMap.put(level, count - 1);
		ServerParamUtil.saveLevelGift();
	}
}
