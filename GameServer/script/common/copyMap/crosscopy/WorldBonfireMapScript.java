package common.copyMap.crosscopy;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Characters_Bean;
import com.data.bean.Cfg_Gather_Bean;
import com.data.bean.Cfg_World_bonfire_Bean;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.copymap.scripts.ICopyGatherScript;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.structs.Gather;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.worldbonfire.manager.WorldBonfireManager;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.CrossServerMessage;
import game.message.WorldBonfireMessage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @Description
 * @auther lw
 * @create 2019-10-15 10:01
 */
public class WorldBonfireMapScript implements IMapBaseScript , ICopyGatherScript {
    private static final Logger logger = LogManager.getLogger(WorldBonfireMapScript.class);

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        List<CommonMessage.CrossAttribute> list = (List<CommonMessage.CrossAttribute>) objects[1];
        long createTime = list.get(0).getValue();
        MapParam.getMapWorldBonfire(mapObject).setBonFireCreateTime(createTime);
        MapParam.getMapWorldBonfire(mapObject).setLv(list.get(0).getType());
        MapParam.getMapWorldBonfire(mapObject).setExp(list.get(0).getParam1());
        long activeTime = WorldBonfireManager.getActiveTime();
        long nowTime = TimeUtils.Time();
        int lastTime =   (int)(activeTime - (nowTime - createTime));
        mapObject.addMapLoopScriptEventTimer(this.getId(), "addExp", -1, 0, Global.World_Bonfire_Exp_Interval * 1000);
        mapObject.addMapOnceScriptEventTimer(getId(), "timeOutClose",lastTime + 2 * 1000);

    }

    @Override
    public boolean onBeginGather(Player player, Gather gather) {
        Cfg_Gather_Bean bean = CfgManager.getCfg_Gather_Container().getValueByKey(gather.getModelId());
        if (bean == null){
            return false;
        }
        long count = Manager.countManager.getCount(player, BaseCountType.WorldBonfireGather, gather.getModelId());

        logger.error("onBeginGather  count: {} gather.getModelId():  {} ",count,gather.getModelId());
        if (count  >=    Global.World_Bonfire_Fire_meat.get(1)){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldBonfireMeat2);
            return false;
        }
        return true;
    }

    @Override
    public void onGather(Player player, Gather gather) {
        MapObject map = Manager.mapManager.getMap(gather.gainMapId());
        if (map == null) {
            return;
        }

        Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(gather.getModelId());
        if (null == gatherCfg) {
            logger.error("配置的没有，怎么初始化的！");
            return;
        }

        //采集掉落
        List<List<Integer>> itemDrops = Manager.dropManager.deal().getItemDrops(player, gatherCfg.getDropId());
        List<Item> list = Item.createItems(itemDrops, 1);
        if (!Manager.backpackManager.manager().addItems(player, list, ItemChangeReason.DropByGatherGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                    MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, list, ItemChangeReason.DropByGatherGet);
            MessageUtils.notify_player(player, Notify.CHAT,MessageString.Bossnew_Soulgather);
        }
        logger.error("onGather   gather.getModelId():  {} ",gather.getModelId());
        if (gather.getModelId() == Global.World_Bonfire_Fire_meat.get(0)){
            //记录玩家一次采集次数
            Manager.countManager.addCount(player, BaseCountType.WorldBonfireGather, gather.getModelId(), Count.RefreshType.CountType_Day, 1);

            sendGatherCount(player);
            //通知游戏服，需要加上统计次数
            CrossServerMessage.F2GCloneCDRecordAdd.Builder msg = CrossServerMessage.F2GCloneCDRecordAdd.newBuilder();
            msg.setCdTimes(1);
            msg.setCdType(Count.RefreshType.CountType_Day.getValue());
            msg.setCdHour(Count.RefreshType.CountType_Day.getHour());
            msg.setCdkey(BaseCountType.WorldBonfireGather.getValue());
            msg.setDefinekey(gather.getModelId());
            msg.setFightId(map.getId());
            msg.addRoleIds(player.getId());
            FightClientManager.GetInstance().send_to_game(player.getIosession(), CrossServerMessage.F2GCloneCDRecordAdd.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public void onOutGather(Player player, Gather gather) {

    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return false;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login){
        if (map == null) {
            return;
        }

        Manager.copyMapManager.manager().sendF2GCloneEnterAddOne(map, player);

        sendGatherCount(player);

    }

    private void sendGatherCount(Player player){
        long count = Manager.countManager.getCount(player, BaseCountType.WorldBonfireGather, Global.World_Bonfire_Fire_meat.get(0));
        //记录玩家一次采集次数
        F2PWorldBonfirePanel.Builder builder = F2PWorldBonfirePanel.newBuilder();
        builder.setRoleId(player.getId());
        builder.setGatherCount((int) count);
        MessageUtils.send_to_public(F2PWorldBonfirePanel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        G2PWorldBonfireLeave.Builder builder = G2PWorldBonfireLeave.newBuilder();
        builder.setRoleId(player.getId());
        builder.setTeamId(0);
        MessageUtils.send_to_public(G2PWorldBonfireLeave.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        if (isQuit)
            Manager.crossServerManager.sendToPublicPlayerOutCrossFight(player, map.getId(), map.getZoneModelId());
    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

    }

    @Override
    public void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker) {

    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject map, Fighter attacker, Player player) {

    }

    @Override
    public void action(MapObject map, String method, Object[] params) {
        switch (method) {
            case "addExp":
                addExp(map);
                break;
            case "timeOutClose":
                timeOutClose(map);
                break;
            default:
                break;
        }
    }

    @Override
    public void removeMap(MapObject map) {

    }

    @Override
    public int getId() {
        return ScriptEnum.WorldBonfireActivityScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    private void timeOutClose(MapObject map)
    {
        for (Player player: map.getPlayers().values()){
            Manager.copyMapManager.manager().onReqCopyMapOut(player);
        }
        map.setStop(true);
    }

    private void addExp(MapObject mapObject) {
        int lv = MapParam.getMapWorldBonfire(mapObject).getLv();
        Cfg_World_bonfire_Bean bean = CfgManager.getCfg_World_bonfire_Container().getValueByKey(lv);
        for (Player player : mapObject.getPlayers().values()) {
            Cfg_Characters_Bean charactersBean = CfgManager.getCfg_Characters_Container().getValueByKey(player.getLevel());

            float percentage = (float)bean.getExp_addition() / (float) 100;
            long addExp = (long) (percentage * charactersBean.getBonfire() + charactersBean.getBonfire());
            Manager.currencyManager.manager().addEXP(player,addExp , ItemChangeReason.WorldBonfireExpGet, IDConfigUtil.getLogId());
        }
    }
}
