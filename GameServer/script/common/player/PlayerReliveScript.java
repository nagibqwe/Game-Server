package common.player;

import com.data.CfgManager;
import com.data.Global;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.data.bean.Cfg_Relive_Bean;
import com.game.chat.structs.Notify;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.copymap.scripts.ICopyReliveScript;
import com.game.copymap.structs.CopyMapType;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.data.MessageString;
import com.game.map.structs.MapParam;
import com.game.map.structs.MapUtils;
import com.game.nature.structs.Nature;
import com.game.pet.structs.Pet;
import com.game.player.script.ImPlayerScript;
import com.game.player.structs.Player;
import com.game.player.structs.ReliveType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.EntityState;
import com.data.ItemChangeReason;
import com.game.task.structs.TaskHelp;
import com.game.utils.MessageUtils;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.F2GReliveRes;
import game.message.CrossServerMessage.G2FRelive;
import game.message.MapMessage;
import game.message.MapMessage.ResRelive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author lw
 */
public class PlayerReliveScript extends ImPlayerScript implements IScript {

    private static final Logger logger = LogManager.getLogger(PlayerReliveScript.class);

    @Override
    public int getId() {
        return ScriptEnum.PlayerReliveBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void OnPlayerRelive(Player player, ReliveType type, boolean glod, Position pos) {
        if (player.playerCrossData.isToFightServer()) {
            onPlayerReliveCorss(player, type, glod);
            return;
        }
        //玩家未死
        if (!player.isDie()) {
            player.removeSate(EntityState.Dead);
            ResRelive.Builder msg = ResRelive.newBuilder();
            msg.setPlayerId(player.getId());
            msg.setMapId(player.getCurGps().getModelId());
            msg.setCurPos(MapUtils.getPos(player.gainCurPos()));
            MessageUtils.send_to_roundPlayer(player, ResRelive.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
            return;
        }

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map == null) {
            //原地复活
            player.reset();
            player.onHpChange(player);

            ResRelive.Builder msg = ResRelive.newBuilder();
            msg.setPlayerId(player.getId());
            msg.setMapId(player.gainMapModelId());
            msg.setCurPos(MapUtils.getPos(player.gainCurPos()));
            MessageUtils.send_to_roundPlayer(player, ResRelive.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
            //tail
            dealCurReliveTail(player);
            onCallPet(player);
//            onCallFabao(player);
//            Manager.biManager.getScript().biDeath(player,map.getMapModelId(),0,0,"",0,0, 2);
            logger.error("当前玩家地图不见了" + player);
            return;
        }
        long nowTime = TimeUtils.Time();
        long deadTime = player.getReviveData().getLastDeadTime();
        //原地复活
        if (ReliveType.CurPos == type) {
            int waitTime = player.getReviveData().getWaitTimeCD();
            if (nowTime - deadTime < waitTime) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.DEAL_REPLAY_CDING);
                return;
            }
            if (!checkCurRelive(player, glod)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.ReliveErrorItem);
                return;
            }
            //原地复活
            player.reset();
            player.onHpChange(player);

            ResRelive.Builder msg = ResRelive.newBuilder();
            msg.setPlayerId(player.getId());
            msg.setMapId(map.getMapModelId());
            msg.setCurPos(MapUtils.getPos(player.gainCurPos()));
            MessageUtils.send_to_roundPlayer(player, ResRelive.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
            //tail
            dealCurReliveTail(player);
            onCallPet(player);
            onCallFabao(player);
//            Manager.biManager.getScript().biDeath(player,map.getMapModelId(),0,0,"",0,0, 2);
            logger.info("玩家当前位置复活" + player);
            return;
        }

        //Gm目标复活
        if (ReliveType.Gm == type) {
            logger.info(TaskHelp.getPlayerInfo(player) + "请求复活，服务器强制复活" + type + "isGold:" + glod);
            //原地复活
            player.reset();
            player.onHpChange(player);
            player.changeCurPos(pos, true);
            player.getReviveData().reset();

            ResRelive.Builder msg = ResRelive.newBuilder();
            msg.setPlayerId(player.getId());
            msg.setMapId(map.getMapModelId());
            msg.setCurPos(MapUtils.getPos(player.gainCurPos()));
            MessageUtils.send_to_roundPlayer(player, ResRelive.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
            onCallPet(player);
            onCallFabao(player);
//            Manager.biManager.getScript().biDeath(player,map.getMapModelId(),0,0,"",0,0, 0);
            return;
        }

        Cfg_Relive_Bean relive_bean = CfgManager.getCfg_Relive_Container().getValueByKey(map.getSetting().getRelive_type());
        if (relive_bean == null) {
            logger.info("relive_bean  null " + map.getSetting().getRelive_type());
            return;
        }

        if ((nowTime - deadTime) < relive_bean.getSafe_relive_time()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.DEAL_REPLAY_CDING);
            return;
        }

        doHomeRelive(player);
        logger.info("复活点复活" + player);

        onCallPet(player);
        onCallFabao(player);
    }

    @Override
    public void doHomeRelive(Player player) {

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        Position pos = map.getRelivePos(player.gainCurPos());

        IScript iScript = Manager.scriptManager.GetScriptClass(map.getSetting().getIsscript());
        if (iScript instanceof ICopyReliveScript) {
            ICopyReliveScript cr = (ICopyReliveScript) iScript;
            pos = cr.doCreateRelivePosition(map, player);
            if (pos == null) {
                pos = map.getRelivePos(player.gainCurPos());
            }
            if (pos == null) {
                pos = player.gainCurPos();
            }
        }

        player.reset();
        player.onHpChange(player);
        
        player.changeCurPos(pos, true);

        MapMessage.ResRelive.Builder msg = MapMessage.ResRelive.newBuilder();
        msg.setPlayerId(player.getId());
        msg.setMapId(player.gainMapModelId());
        msg.setCurPos(MapUtils.getPos(player.gainCurPos()));
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResRelive.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);

    }

    public void G2FReliveHandler(G2FRelive mess) {
        //游戏服通知跨服战场复活
        Player player = Manager.playerManager.getPlayerOnline(mess.getRoleId());
        if (player == null) {
            return;
        }

        if (!player.isDie()) {
            player.removeSate(EntityState.Dead);
            ResRelive.Builder msg = ResRelive.newBuilder();
            msg.setPlayerId(player.getId());
            msg.setMapId(player.getCurGps().getModelId());
            msg.setCurPos(MapUtils.getPos(player.gainCurPos()));
            MessageUtils.send_to_roundPlayer(player, ResRelive.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
            return;
        }

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map == null) {
            return;
        }
        Cfg_Relive_Bean relive_bean = CfgManager.getCfg_Relive_Container().getValueByKey(map.getSetting().getRelive_type());
        if (relive_bean == null) {
            logger.info("relive_bean  null " + relive_bean.getRelive_id());
            return;
        }
        if(relive_bean.getAuto_relive_time() <= 0){
            logger.info("can not relive");
            return;
        }

        long nowTime = TimeUtils.Time();
        long deadTime = player.getReviveData().getLastDeadTime();

        //原地复活
        if (ReliveType.CurPos.compareTo(mess.getType())) {

            int waitTime = player.getReviveData().getWaitTimeCD();
            if (nowTime - deadTime < waitTime) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.DEAL_REPLAY_CDING);
                return;
            }
            player.reset();
            player.onHpChange(player);

            ResRelive.Builder res = ResRelive.newBuilder();
            res.setPlayerId(player.getId());
            res.setMapId(map.getMapModelId());
            res.setCurPos(MapUtils.getPos(player.gainCurPos()));
            MessageUtils.send_to_roundPlayer(player, ResRelive.MsgID.eMsgID_VALUE, res.build().toByteArray(), true);
            dealCurReliveTail(player);
            F2GReliveRes.Builder msg = F2GReliveRes.newBuilder();
            msg.setRoleId(mess.getRoleId());
            msg.setDec(true);
            FightClientManager.GetInstance().send_to_game(player.getIosession(), F2GReliveRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
//            Manager.biManager.getScript().biDeath(player,map.getMapModelId(),0,0,"",0,0, 2);
        }

        if (ReliveType.GoBack.compareTo(mess.getType())) {

            Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());

            if ((nowTime - deadTime) < relive_bean.getSafe_relive_time()) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.DEAL_REPLAY_CDING);
                return;
            }

            doHomeRelive(player);
            logger.info("复活点复活" + player);
        }
    }

    private void onPlayerReliveCorss(Player player, ReliveType type, boolean gold) {
        logger.info(TaskHelp.getPlayerInfo(player) + "是跨服复活！");
        if (ReliveType.CurPos == type && !checkCurRelive(player, gold)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ReliveErrorItem);
            return;
        }
        G2FRelive.Builder msg = G2FRelive.newBuilder();
        msg.setRoleId(player.getId());
        msg.setType(type.getValue());
        ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), G2FRelive.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void F2GReliveResHandler(CrossServerMessage.F2GReliveRes messInfo) {

        Player player = Manager.playerManager.getPlayerCache(messInfo.getRoleId());
        if (player == null) {
            return;
        }
        if (messInfo.getDec()) {
            dealCurReliveTail(player);
        }
    }

    //召唤宠物
    private void onCallPet(Player player) {
        Pet pet = Manager.petManager.getBattlePet(player);
        if (pet == null) {
            return;
        }
        pet.reset();
        pet.getCurGps().setPos(player.getCurGps().getPos());
        Manager.mapManager.manager().onEnterMap(pet);
    }

    private void onCallFabao(Player player) {
        Nature nature = player.getStifleData().getNature();
        if (nature.getCurrentModelId() <= 0) {
            return;
        }
        nature.reset();
        Manager.mapManager.manager().onEnterMap(nature);
    }

    //原地复活之后处理
    private void dealCurReliveTail(Player player) {
        int item = Global.ReviveItem;
        boolean can = Manager.backpackManager.manager().canDeleteItemNum(player, item, 1);
        if (can) {
            Manager.backpackManager.manager().onRemoveItem(player, item, 1, ItemChangeReason.ReliveDec, IDConfigUtil.getLogId());
            return;
        }

        int money = Global.ReviveYB;
        Manager.currencyManager.manager().decGold(player, money, ItemChangeReason.ReliveDec, IDConfigUtil.getLogId());
    }

    //检测原地复活条件
    private boolean checkCurRelive(Player player, boolean gold) {
        int item = Global.ReviveItem;
        boolean can = Manager.backpackManager.manager().canDeleteItemNum(player, item, 1);
        if (can) {
            return true;
        }
        if (!gold) {
            return false;
        }
        int money = Global.ReviveYB;
        return Manager.currencyManager.manager().canRemoveGold(player, money);
    }

}
