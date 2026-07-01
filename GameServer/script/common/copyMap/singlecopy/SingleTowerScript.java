package common.copyMap.singlecopy;

import com.data.*;
import com.data.bean.Cfg_Challenge_reward_Bean;
import com.data.bean.Cfg_Daily_Bean;
import com.data.container.Cfg_Challenge_reward_Container;
import com.game.backpack.structs.ExpValueItem;
import com.game.backpack.structs.Item;
import com.game.copymap.scripts.ISingleTowerScript;
import com.game.copymap.structs.SingleTowerData;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.message.CopyMapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SingleTowerScript implements ISingleTowerScript {

    private static final Logger logger = LogManager.getLogger(SingleTowerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.SingleTowerActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void sendSingleTowerInfo(Player player) {

        CopyMapMessage.ResChallengeEnterPanel.Builder builder = CopyMapMessage.ResChallengeEnterPanel.newBuilder();
        builder.setOverLevel(player.getSingleTowerData().getCurLayer());
        MessageUtils.send_to_player(player, CopyMapMessage.ResChallengeEnterPanel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void goOnChallenge(Player player) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map.getSetting().getIsscript() != getId()) {
            return;
        }
        if (map.getMonsters().size() > 0) {
            return;
        }

        Manager.copyMapManager.manager().copyMapRefreshMonster(map, map.getZoneModelId(), player.getSingleTowerData().getCurLayer());

        map.addMapOnceScriptEventTimer(getId(), "timeoutEnd", Global.Wanyaojuan_Finish_Time.get(0) * 1000, player.getSingleTowerData().getCurLayer());

        logger.info("万妖卷请求刷怪 layer={} player={}", player.getSingleTowerData().getCurLayer(), player);

    }

    /**
     * 下一关
     *
     * @param player
     */
    @Override
    public void ReqGotoNextChallenge(Player player) {


        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map.getSetting().getIsscript() != getId()) {
            return;
        }
        if (map.getMonsters().size() > 0) {
            return;
        }
        int layer = player.getSingleTowerData().getCurLayer();
        Cfg_Challenge_reward_Bean bean = Cfg_Challenge_reward_Container.GetInstance().getValueByKey(layer);
        if (bean == null || player.getLevel() < bean.getNeed_level()) {
            return;
        }
        //发送限定时间
        sendEndTimeToClient(player, Global.Wanyaojuan_Finish_Time.get(0), map.getMonsters().size() > 0);

        logger.info("万妖卷请求下一关 layer={} player={}", player.getSingleTowerData().getCurLayer(), player);

    }


    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        mapObject.setDelTime(0);
        mapObject.setAutoRemove(false);
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        int layer = player.getSingleTowerData().getCurLayer();
        Cfg_Challenge_reward_Bean bean = Cfg_Challenge_reward_Container.GetInstance().getValueByKey(layer);
        if (bean == null || player.getLevel() < bean.getNeed_level()) {
            return false;
        }
        Cfg_Daily_Bean dailyBean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.SingleTower.getValue());
        if (dailyBean == null) {
            return false;
        }
        return true;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        //发送限定时间
        sendEndTimeToClient(player, Global.Wanyaojuan_Finish_Time.get(0), map.getMonsters().size() > 0);
    }

    private void sendEndTimeToClient(Player player, int endTime, boolean hasRefresh) {
        CopyMapMessage.ResChallengeInfo.Builder builder = CopyMapMessage.ResChallengeInfo.newBuilder();
        builder.setChallengeLevel(player.getSingleTowerData().getCurLayer());
        builder.setEndTime(endTime);
        builder.setRefresh(hasRefresh);
        MessageUtils.send_to_player(player, CopyMapMessage.ResChallengeInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        map.setDelTime(1);
        map.setAutoRemove(true);
    }

    @Override
    public void onDamage(MapObject map, Monster monster, long damage, Fighter attacker) {

    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

    }

    @Override
    public void onMonsterAfterDie(MapObject mapObject, Monster monster, Fighter attacker) {
        if (mapObject.getMonsters().size() <= 0) {
            if (attacker instanceof Player) {
                finish(mapObject, (Player) attacker, true, 1);
            }
        }
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject mapObject, Fighter attacker, Player player) {
        finish(mapObject, player, false, 2);
    }

    @Override
    public void action(MapObject mapObject, String method, Object[] params) {

        Player player = Utils.findOne(mapObject.getPlayers().values(), p -> true);
        if (player == null) {
            mapObject.setStop(true);
            return;
        }
        if ("timeoutEnd".equals(method)) {
            int layer = (int) params[0];
            if (layer == player.getSingleTowerData().getCurLayer()) {
                finish(mapObject, player, false, 3);
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
        if (player == null) {
            return;
        }

        if (!isWin) {
            mapObject.setStop(true);
        }

        SingleTowerData towerData = player.getSingleTowerData();

        CopyMapMessage.ResChallengeEndInfo.Builder message = CopyMapMessage.ResChallengeEndInfo.newBuilder();

        if (isWin) {
            Cfg_Challenge_reward_Bean bean = Cfg_Challenge_reward_Container.GetInstance().getValueByKey(towerData.getCurLayer());
            if (bean == null) {
                logger.error("Cfg_Challenge_rewardBean配置表不存在：" + towerData.getCurLayer());
                return;
            }

            //普通奖励
            List<Item> items = Item.createItems(bean.getNormal_reward());
            //通关奖励
            List<Item> layer = Item.createItems(player.getCareer(), bean.getChapter_reward(), 1);
            items.addAll(layer);

            for (Item item : items) {
                CopyMapMessage.cardItemInfo.Builder mItem = CopyMapMessage.cardItemInfo.newBuilder();
                mItem.setItemId(item.getItemModelId());
                mItem.setNum(item instanceof ExpValueItem ? ((ExpValueItem) item).getExpNum() : item.getNum());
                mItem.setBind(item.isBind());
                message.addReward(mItem);
            }

            if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CopyMapGet, mapObject.getZoneModelId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                        , MessageString.System, MessageString.BAGISSPACETOMAIL
                        , MessageString.GetAwardNotEnoughSpaceContent, items, ItemChangeReason.CopyMapGet);
            }
        }

        message.setChallengeLevel(towerData.getCurLayer());
        message.setState(isWin ? 1 : 0);
        MessageUtils.send_to_player(player, CopyMapMessage.ResChallengeEndInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());

        Manager.copyMapManager.logic().biInstance(player, mapObject.getZoneModelId(), 1, type, towerData.getCurLayer(), false);

//        logger.error("万妖  {}={}：" ,isWin,towerData.getCurLayer());

        if (isWin) {
            towerData.setCurLayer(towerData.getCurLayer() + 1);

            Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.SingleTower, 1);
            Manager.controlManager.operate(player, FunctionVariable.WanYaoJuanNum, 1);
        }
    }

}
