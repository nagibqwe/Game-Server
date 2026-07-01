package common.copyMap.boss;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Bossstate_Bean;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.container.Cfg_Bossstate_Container;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.boss.script.IStateBossScript;
import com.game.chat.structs.Notify;
import com.game.cooldown.structs.CooldownTypes;
import com.game.copymap.structs.StateBossCopyData;
import com.game.count.structs.VariantType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.drop.structs.SpecialDropDefine;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.nature.structs.HuaxinEntity;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.Skill;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import com.game.vip.structs.VipPower;
import com.game.welfare.struct.RetrieveType;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage;
import game.message.ZoneMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 又名  个人boss
 * 境界boss 脚本
 */
public class BossStateScript implements IStateBossScript {

    private static final Logger logger = LogManager.getLogger(BossStateScript.class);

    @Override
    public int getId() {
        return ScriptEnum.BossStateActivityScript;
    }


    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 领取首通奖励
     *
     * @param layer 领取层数
     */
    private void sendFirstReward(Player player, int layer) {
        if (layer > player.getStateBossCurrLayer()) {
            return;
        }
        if (player.getFirstLayers().contains(layer)) {
            return;
        }
        Cfg_Bossstate_Bean bean = Cfg_Bossstate_Container.GetInstance().getValueByKey(layer);

        //发送挑战奖励
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < bean.getFrist_reward().size(); i++) {
            ReadArray<Integer> array = bean.getFrist_reward().get(i);
            if (array.get(3) == player.getCareer() || array.get(3) == 9) {
                items.add(Item.createItem(array.get(0), array.get(1), array.get(2) == 1));
            }
        }
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.BossStateChanllageGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                    , MessageString.System, MessageString.BAGISSPACETOMAIL
                    , MessageString.GetAwardNotEnoughSpaceContent, items, ItemChangeReason.BossStateChanllageGet);
        }
        player.getFirstLayers().add(layer);

        doBossInfo(player);

    }


    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        mapObject.setAutoRemove(false);

        StateBossCopyData zone = new StateBossCopyData();
        zone.setZoneId(mapObject.zone.getZoneId());
        zone.setLevel(mapObject.zone.getLevel());
        mapObject.setZone(zone);

        mapObject.addMapLoopScriptEventTimer(getId(), "tickEnd", -1, 0, 3000);
        if (zone.getLevel() <= 3) {
            mapObject.addMapLoopScriptEventTimer(getId(), "tickHP", -1, 0, 1000, objects);
        }

    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {

        if (level == player.getStateBossCurrLayer() + 1) {
            return true;
        }
        if (level > player.getStateBossCurrLayer() + 1) {
            return false;
        }

        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.StateBoss.getValue());

        if (remainCount <= 0) {
            return false;
        }
        //层数限制
        Cfg_Bossstate_Bean bean = Cfg_Bossstate_Container.GetInstance().getValueByKey(level);

        return bean != null;
    }

    @Override
    public void onEnterMap(Player player, MapObject mapObject, boolean login) {

        doRefreshBoss(player, mapObject);

        HuaxinEntity entity = player.getCurHuaxinEntity();
        if (entity == null) {
            return;
        }
        for (Skill skill : entity.getBaseSkills().values()) {
            Manager.cooldownManager.addCooldown(player, CooldownTypes.Player_FlySowrd_CD_Skill, String.valueOf(skill.getSkillId()), 0);
        }
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        map.setStop(true);
        map.setAutoRemove(true);
    }

    @Override
    public void onDamage(MapObject map, Monster monster, long damage, Fighter attacker) {
    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

        StateBossCopyData zone = map.getZone();
        zone.setRefreshMonster(false);

        Player player = (Player) attacker;

        Manager.dropManager.deal().specialDropReward(monster, player, SpecialDropDefine.StateBoss, false, -1);

        Manager.countManager.addVariant(player, VariantType.Daily_Kill_Self_Boss_Times, 1);
        if (zone.getLevel() == player.getStateBossCurrLayer() + 1) {
            player.setStateBossCurrLayer(zone.getLevel());
            sendFirstReward(player, zone.getLevel());
        } else {
            Manager.retrieveResManager.getScript().count(player, RetrieveType.StateBoss);
            Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.StateBoss, 1);
        }
        sendBossInfoUpdate(player);

        doBossInfo(player);

        finish(map, player, true, 1);
    }

    @Override
    public void onMonsterAfterDie(MapObject mapObject, Monster monster, Fighter attacker) {
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject mapObject, Fighter attacker, Player player) {
        finish(mapObject, player, false, 2);
    }


    @Override
    public void action(MapObject map, String method, Object[] params) {

        StateBossCopyData zone = map.getZone();

        if ("tickHP".equals(method)) {
            Player player = (Player)params[0];
            if (zone.getLevel() <= 3 && zone.getLevel() == player.getStateBossCurrLayer() + 1) {
                int hpPercent = (int) (player.getCurHp() * 10000.0 / player.getAttribute().MaxHP());
                if (hpPercent < 2000) {
                    player.setCurHp(player.getAttribute().MaxHP());
                }
            }
        }
        if ("tickEnd".equals(method)) {
            //TODO 副本超时结束
            if (zone.getEndTime() > 0 && zone.getEndTime() <= TimeUtils.Time()) {
                Player player = Utils.findOne(map.getPlayers().values(), o -> true);
                finish(map, player, false, 3);
            }
        }
    }

    @Override
    public void removeMap(MapObject mapObject) {
    }

    /**
     * 挑战完成
     *
     * @param mapObject 副本
     * @param player    玩家
     * @param isWin     胜利or失败
     */
    private void finish(MapObject mapObject, Player player, boolean isWin, int type) {
        if (mapObject.isStop()) {
            return;
        }

        StateBossCopyData zone = mapObject.getZone();

        if (!isWin) {
            mapObject.setStop(true);
            mapObject.setAutoRemove(true);
        }

        HashMap<Integer, Item> dropItemsHistory = zone.getDropItemsHistory(player.getId());

        ZoneMessage.ResBossStateResultPanl.Builder message = ZoneMessage.ResBossStateResultPanl.newBuilder();
        message.setMaxFloor(player.getStateBossCurrLayer());
        message.setState(isWin);

        for (Item item : dropItemsHistory.values()) {
            ZoneMessage.itemInfo.Builder itemInfo = ZoneMessage.itemInfo.newBuilder();
            itemInfo.setModelId(item.getItemModelId());
            itemInfo.setNum(item.getNum());
            message.addInfo(itemInfo);
        }
        MessageUtils.send_to_player(player, ZoneMessage.ResBossStateResultPanl.MsgID.eMsgID_VALUE, message.build().toByteArray());

        Manager.copyMapManager.logic().biInstance(player, mapObject.getZoneModelId(), 1, type, zone.getLevel(), false);
    }

    private void sendBossInfoUpdate(Player player) {
        CopyMapMessage.ResupdateBossState.Builder builder = CopyMapMessage.ResupdateBossState.newBuilder();
        Cfg_Bossstate_Bean bean = Cfg_Bossstate_Container.GetInstance().getValueByKey(player.getStateBossCurrLayer());

        CopyMapMessage.BossStateInfo.Builder bossInfo = CopyMapMessage.BossStateInfo.newBuilder();
        bossInfo.setLayer(bean.getID());
        bossInfo.setBossId(bean.getMonster());
        bossInfo.setFirst(true);
        bossInfo.setIsGetReward(player.getFirstLayers().contains(bean.getID()));
        builder.setMaxLayer(player.getStateBossCurrLayer() + 1);
        builder.setBossList(bossInfo);
        MessageUtils.send_to_player(player, CopyMapMessage.ResupdateBossState.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private void sendEndTimeToClient(Player player, MapObject map, long endTime) {
        CopyMapMessage.ResCopymapNeedTime.Builder msg = CopyMapMessage.ResCopymapNeedTime.newBuilder();
        msg.setEndTime((int) ((endTime - TimeUtils.Time()) / 1000));
        msg.setModelId(map.getZoneModelId());
        msg.setWaitEndToStart(0);
        MessageUtils.send_to_player(player, CopyMapMessage.ResCopymapNeedTime.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 请求境界boss购买
     *
     * @param player
     */
    @Override
    public void doBuyBossCount(Player player) {
        //增加VIP宝珠状态检查
        if (!player.getVipPearl().canFree()) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.C_Vip_Power_No_Ues_Notice);
            return;
        }
        ConcurrentHashMap<Integer, Integer> dailyBuyCount = player.getDailyActiveData().getDailyBuyCount();
        Integer hasBuyCount = dailyBuyCount.getOrDefault(DailyActiveDefine.StateBoss.getValue(), 0);
        if (hasBuyCount >= Manager.vipManager.power().getVipPurNum(player, VipPower.POWER_20)) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.BOSSFIGHT_TISHENGVIPADDNUM);
            return;
        }
        int needGold = Manager.vipManager.power().getVipAddNumPrice(hasBuyCount + 1, VipPower.POWER_20);
        if (!Manager.currencyManager.manager().decBindGoldOrGold(player, needGold, ItemChangeReason.BuyBossStateVipCountDec, IDConfigUtil.getLogId())) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Not_Enough_Gold);
            return;
        }

        dailyBuyCount.put(DailyActiveDefine.StateBoss.getValue(), hasBuyCount + 1);

        //资源找回记录购买册数
        Manager.retrieveResManager.getScript().addVipBuyCount(player, DailyActiveDefine.StateBoss.getValue(), 1);

        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.C_SHOP_TIPS_BUYSUXESSSSSS);

        CopyMapMessage.ResBuyBossStateCount.Builder msg = CopyMapMessage.ResBuyBossStateCount.newBuilder();
        msg.setCount(Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.StateBoss.getValue()));
        MessageUtils.send_to_player(player, CopyMapMessage.ResBuyBossStateCount.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 请求境界boss刷新
     *
     * @param player
     */
    @Override
    public void doRefreshBoss(Player player, MapObject mapObject) {

        StateBossCopyData zone = mapObject.getZone();

        if (zone.isRefreshMonster()) {
            return;
        }
        if (!canEnterMap(player, zone.getZoneId(), zone.getLevel())) {
            return;
        }

        zone.getDropItemsHistory().clear();

        Cfg_Bossstate_Bean bean = Cfg_Bossstate_Container.GetInstance().getValueByKey(zone.getLevel());

        //TODO 刷新境界boss
        Manager.monsterManager.createMonster(mapObject, new Position(bean.getPos().get(0), bean.getPos().get(1)), bean.getMonster());

        Cfg_Clone_map_Bean clone_bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());

        zone.setEndTime(TimeUtils.Time() + clone_bean.getExist_time());

        sendEndTimeToClient(player, mapObject, zone.getEndTime());

        zone.setRefreshMonster(true);
//        Manager.copyMapManager.manager().copyMapRefreshMonster(mapObject, mapObject.getZoneModelId(), zone.getLevel());

        logger.info("境界boss刷新layer={} player={}", zone.getLevel(), player);
    }

    /**
     * 请求打开境界boss界面
     *
     * @param player
     */
    @Override
    public void doBossInfo(Player player) {
        CopyMapMessage.ResOpenBossStatePanle.Builder builder = CopyMapMessage.ResOpenBossStatePanle.newBuilder();
        int stateBossCurrLayer = player.getStateBossCurrLayer();
        for (Cfg_Bossstate_Bean bean : Cfg_Bossstate_Container.GetInstance().getValuees()) {
            boolean getedFirstReward = player.getFirstLayers().contains(bean.getID());
            if (getedFirstReward && bean.getShow_boss() == 0) {
                continue;
            }
            CopyMapMessage.BossStateInfo.Builder bossInfo = CopyMapMessage.BossStateInfo.newBuilder();
            bossInfo.setFirst(bean.getID() <= stateBossCurrLayer);
            bossInfo.setIsGetReward(getedFirstReward);
            bossInfo.setBossId(bean.getMonster());
            bossInfo.setLayer(bean.getID());
            builder.addBossList(bossInfo);
        }
        builder.setMaxLayer(stateBossCurrLayer + 1);
        ConcurrentHashMap<Integer, Integer> dailyBuyCount = player.getDailyActiveData().getDailyBuyCount();
        builder.setBoughtCount(dailyBuyCount.getOrDefault(DailyActiveDefine.StateBoss.getValue(), 0));
        builder.setCount(Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.StateBoss.getValue()));
        MessageUtils.send_to_player(player, CopyMapMessage.ResOpenBossStatePanle.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }
}
