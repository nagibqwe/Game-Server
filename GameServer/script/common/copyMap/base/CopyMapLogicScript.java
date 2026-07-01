package common.copyMap.base;

import com.data.CfgManager;
import com.data.Global;
import com.data.MessageString;
import com.data.bean.Cfg_Clone_level_Bean;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Daily_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.copymap.scripts.ICopyLogicScript;
import com.game.copymap.structs.CopyMapType;
import com.game.copymap.structs.ZoneCache;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.data.ItemChangeReason;
import com.game.structs.ServerStr;
import com.game.utils.BIUtils;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.core.util.IDConfigUtil;
import game.message.CopyMapMessage;
import game.message.CrossServerMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class CopyMapLogicScript implements ICopyLogicScript {

    private static final Logger log = LogManager.getLogger(CopyMapLogicScript.class);

    @Override
    public int getId() {
        return ScriptEnum.CopyLogicBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onReqUpMorale(Player player, int type) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        int zoneModelId = map.getZoneModelId();
        if (player.playerCrossData.isToFightServer()) {
            zoneModelId = player.playerCrossData.toZoneModelId;
        }
        //是否可以加鼓舞
        Cfg_Clone_map_Bean config = CfgManager.getCfg_Clone_map_Container().getValueByKey(zoneModelId);
        if (config.getCanUpMorale() != 1) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CanNotUpMorale);
            return;
        }
        ReadArray<Integer> morale = Global.Exp_Clone_Power_Up.get(type);
        if (morale == null) {
            return;
        }
        ZoneCache data = map.getZone();
        int rate = data.getMerge().getOrDefault(player.getId(), 1);

        //是否能加buff
        int buffId = morale.get(1);
        //是否货币足够
        int num = morale.get(0) * rate;

        if (!Manager.buffManager.deal().isCanAdd(player, buffId)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.UpMoraleAgain);
            return;
        }
        if (type == 0) {
            if (!Manager.currencyManager.manager().onDecItemCoin(player, num, ItemChangeReason.UpMoraleDec, IDConfigUtil.getLogId(), ItemCoinType.BindMoney)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.MoneyNotEnough);
                return;
            }
        } else {
            if (!Manager.currencyManager.manager().decBindGoldOrGold(player, num, ItemChangeReason.UpMoraleDec, IDConfigUtil.getLogId())) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.GoldNotEnough);
                return;
            }
        }
        Manager.buffManager.deal().onAddBuff(player, player, buffId);

        log.info("鼓舞buffId={} player={}", buffId, player);

        //跨服鼓舞
        if (player.playerCrossData.isToFightServer()) {
            CrossServerMessage.G2F_UpMorale.Builder msg = CrossServerMessage.G2F_UpMorale.newBuilder();
            msg.setRoleId(player.getId());
            msg.setType(0);
            ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), CrossServerMessage.G2F_UpMorale.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }


    @Override
    public void onReqCrossUpMorale(Player player, boolean dec, int type) {

    }

    @Override
    public void vipBuyCount(Player player, int cloneId) {
        Cfg_Daily_Bean dailyBean = null;
        Cfg_Daily_Bean[] dailyBeans = CfgManager.getCfg_Daily_Container().getValuees();
        for (Cfg_Daily_Bean tempDailyBean : dailyBeans) {
            for (int i = 0; i < tempDailyBean.getCloneID().size(); i++) {
                if (tempDailyBean.getCloneID().get(i) == cloneId) {
                    dailyBean = tempDailyBean;
                    break;
                }
            }
        }
        if (dailyBean == null) {
            log.error("未找到对应副本的dailyId, cloneId: " + cloneId);
            return;
        }

        int canBuyCount = Manager.dailyActiveManager.deal().getDailyCanBuyCount(player, dailyBean.getId());
        if (canBuyCount <= 0) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.NotHaveBuyCount);
            return;
        }
        int hasBuyCount = player.getDailyActiveData().getDailyBuyCount().getOrDefault(dailyBean.getId(), 0);
        int needGold = Manager.dailyActiveManager.deal().getDailyBuyNeedGold(dailyBean.getId(), hasBuyCount + 1);
        if (!Manager.currencyManager.manager().decBindGoldOrGold(player, needGold, ItemChangeReason.VipCopyMapBuyDec, cloneId)) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Not_Enough_Gold);
            return;
        }

        //TODO 增加日常购买次数
        ConcurrentHashMap<Integer, Integer> dailyBuyCount = player.getDailyActiveData().getDailyBuyCount();
        dailyBuyCount.put(dailyBean.getId(), hasBuyCount + 1);
        //资源找回记录购买册数
        Manager.retrieveResManager.getScript().addVipBuyCount(player, dailyBean.getId(), 1);

        int dailyRemainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, dailyBean.getId());
        int dailyMaxCount = Manager.dailyActiveManager.deal().getDailyMaxCount(player, dailyBean.getId());
        int dailyCanBuyCount = Manager.dailyActiveManager.deal().getDailyCanBuyCount(player, dailyBean.getId());
        int buyCount = player.getDailyActiveData().getDailyBuyCount().getOrDefault(dailyBean.getId(), 0);
        String dailyName = ServerStr.getChatTableName(dailyBean.getName());
        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.BuySuceessCountAdd, dailyName, dailyName);
        CopyMapMessage.ResVipBuyCount.Builder builder = CopyMapMessage.ResVipBuyCount.newBuilder();
        builder.setCopyId(cloneId);
        builder.setBuyCount(buyCount);
        builder.setCanBuyCount(dailyCanBuyCount);
        builder.setMaxCount(dailyMaxCount);
        builder.setRemainCount(dailyRemainCount);
        MessageUtils.send_to_player(player, CopyMapMessage.ResVipBuyCount.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }


    @Override
    public void changeCloneSetting(Player player, CopyMapMessage.ReqCopySetting mess) {

        Cfg_Clone_map_Bean clone_map_bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mess.getCopyId());
        if (mess.getMergeCount() < 0 || clone_map_bean == null) {
            return;
        }
        Cfg_Daily_Bean daily_bean = CfgManager.getCfg_Daily_Container().getValueByKey(clone_map_bean.getDailyid());
        if (daily_bean == null || daily_bean.getSweep() != 1) {
            log.warn("副本不能合并次数 clone={} ", mess.getCopyId());
            return;
        }
        int dailyRemainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, daily_bean.getId());
        if (dailyRemainCount < mess.getMergeCount()) {
            return;
        }
        Manager.countManager.setCount(player, BaseCountType.COPY_Merge_Count, mess.getCopyId(), Count.RefreshType.CountType_Day, mess.getMergeCount());

        CopyMapMessage.ResCopySetting.Builder builder = CopyMapMessage.ResCopySetting.newBuilder();
        builder.setCopyId(mess.getCopyId());
        builder.setMergeCount(mess.getMergeCount());
        MessageUtils.send_to_player(player, CopyMapMessage.ResCopySetting.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        log.info("设置副本合并次数 clone={}, merge={}", clone_map_bean.getId(), mess.getMergeCount());
    }

    @Override
    public void onReqManyCopyPanel(Player player, CopyMapMessage.ReqOpenManyCopyPanel mess) {
        Cfg_Daily_Bean dailyBean = null;
        for (Cfg_Daily_Bean tempDailyBean : CfgManager.getCfg_Daily_Container().getValuees()) {
            for (int i = 0; i < tempDailyBean.getCloneID().size(); i++) {
                if (tempDailyBean.getCloneID().get(i) == mess.getCopyId()) {
                    dailyBean = tempDailyBean;
                }
            }
        }
        if (dailyBean == null || dailyBean.getTimes() < 0) {
            log.error("多人副本Daily配置表错误: " + mess.getCopyId());
            return;
        }

        int dailyRemainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, dailyBean.getId());
        int dailyMaxCount = Manager.dailyActiveManager.deal().getDailyMaxCount(player, dailyBean.getId());
        int dailyCanBuyCount = Manager.dailyActiveManager.deal().getDailyCanBuyCount(player, dailyBean.getId());
        int buyCount = player.getDailyActiveData().getDailyBuyCount().getOrDefault(dailyBean.getId(), 0);
        int mergeCount = (int) Manager.countManager.getCount(player, BaseCountType.COPY_Merge_Count, mess.getCopyId());

        CopyMapMessage.ResOpenManyCopyPanel.Builder builder = CopyMapMessage.ResOpenManyCopyPanel.newBuilder();
        builder.setCopyId(mess.getCopyId());
        builder.setRemainCount(dailyRemainCount);
        builder.setMaxCount(dailyMaxCount);
        builder.setBuyCount(buyCount);
        builder.setCanBuyCount(dailyCanBuyCount);
        builder.setMergeCount(mergeCount);
        MessageUtils.send_to_player(player, CopyMapMessage.ResOpenManyCopyPanel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 检测副本挑战次数
     */
    @Override
    public boolean checkEnterTimes(Player player, int copyId) {
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(copyId);
        if (bean == null) {
            log.error("Cfg_Clone_map_Bean配置表不存在：" + copyId);
            return false;
        }
        Cfg_Daily_Bean daily_bean = CfgManager.getCfg_Daily_Container().getValueByKey(bean.getDailyid());
        if (daily_bean == null) {
            int dailyId = Manager.copyMapManager.getZoneDaily().getOrDefault(copyId, 0);
            daily_bean = CfgManager.getCfg_Daily_Container().getValueByKey(dailyId);
        }
        if (daily_bean != null) {
            int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, daily_bean.getId());
            return remainCount != 0;
        }
        return false;
    }

    /**
     * 获取副本的合并次数
     *
     * @param copyId 副本id
     */
    @Override
    public int getMergeCount(Player player, int copyId) {
        Cfg_Clone_map_Bean clone_map_bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(copyId);
        int dailyRemainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, clone_map_bean.getDailyid());
        if (dailyRemainCount < 0) {
            return 1;
        }
        int merge = player.getCloneSetting().getOrDefault(copyId, 0);
        if (merge < 2) {
            return 1;
        }
        return merge;
    }

    @Override
    public void OnReqCloneFightInfo(Player player, CopyMapMessage.ReqCloneFightInfo mess) {
        //如果玩家是在跨服， 就往跨服发
        if (!player.playerCrossData.isToFightServer()) {
            MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
            if (null == mapObject) {
                return;
            }

            if (mapObject.getZoneModelId() != mess.getModelId()) {
                return;
            }
            Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mess.getModelId());
            if (bean == null) {
                log.error("Cfg_Clone_map_Bean配置表找不到：" + mess.getModelId());
                return;
            }
            if (bean.getType() == CopyMapType.GodDevilWar_CopyMap) {
                IScript script = ScriptManager.getInstance().GetScriptClass(ScriptEnum.GodDevilWarCrossActivityScript);
                script.call(player, "onReqCloneFightInfo");
            }
            return;
        }

        CrossServerMessage.G2FReqCloneFightInfo.Builder msg = CrossServerMessage.G2FReqCloneFightInfo.newBuilder();
        msg.setFightId(player.playerCrossData.toFightId);
        msg.setModelId(player.playerCrossData.toZoneModelId);
        msg.setRoleId(player.getId());
        ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), CrossServerMessage.G2FReqCloneFightInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void biInstance(Player player, int instance_id, int instance_status, int instance_result, int instance_diff, boolean isExtra) {
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(instance_id);
        if (bean == null) {
            return;
        }
        int floor = 0;
        if (bean.getId() == 37) {//万妖卷
            floor = player.getSingleTowerData().getCurLayer();
        } else if (bean.getId() == 500001) {//剑灵阁
            floor = instance_diff;
        } else if (bean.getType() == 9) {//天芒鬼城
            floor = instance_diff;
        } else if (bean.getType() == 16) {//个人boss
            instance_diff = bean.getClone_story();
        }
        Cfg_Clone_level_Bean bean_level = CfgManager.getCfg_Clone_level_Container().getValueByKey(instance_id * 100 + instance_diff);
        //副本奖励BI记录
//        String getItems = "";
//        if (instance_status == 2 || instance_result == 1) {//扫荡
//            if (!bean.getSuccess_reward().isEmpty()) {
//                getItems = BIUtils.makeBIRewardStr(bean.getSuccess_reward());
//            }
//            //额外奖励
//            if (!bean.getSuccess_reward().isEmpty() && isExtra) {
//                if (!( getItems.equals(""))) {
//                    getItems += ",";
//                }
//                getItems += BIUtils.makeBIRewardStr(bean.getExtra_reward());
//            }
//        } else if (instance_result == 2 || instance_result == 3) {
//            if (!bean.getSuccess_reward().isEmpty()) {
//                getItems = BIUtils.makeBIRewardStr(bean.getFail_reward());
//            }
//        }

        //(测试要求)进入时副本结果传空
        Integer result = instance_result;
        if (instance_status == 0) {
            result = null;
        }

        if (bean_level != null) {
            Manager.biManager.getScript().biInstance(player, instance_id, bean.getType(), bean.getType_name(), bean.getDuplicate_name(), bean_level.getMin_lv(),
                    instance_status, result, bean_level.getDescribe(), floor == 0 ? "" : String.valueOf(floor));
        } else {
            Manager.biManager.getScript().biInstance(player, instance_id, bean.getType(), bean.getType_name(), bean.getDuplicate_name(), bean.getMin_lv(),
                    instance_status, result, String.valueOf(instance_diff), floor == 0 ? "" : String.valueOf(floor));
        }
    }

}
