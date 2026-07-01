package common.crosshorseboss;

import com.data.CfgManager;
import com.data.MessageString;
import com.data.bean.Cfg_Bossnew_HorseBoss_Bean;
import com.game.chat.structs.Notify;
import com.game.copymap.structs.FightRoomState;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.drop.structs.SpecialDropDefine;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.monster.manager.MonsterManager;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import com.game.welfare.struct.RetrieveType;
import game.core.map.Position;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.CrossHorseBossMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by cxl on 2021/4/16.
 */
public class CrossHorseBossCloneMapScript  implements IMapBaseScript {

    final Logger logger = LogManager.getLogger(CrossHorseBossCloneMapScript.class);

    final int groupId   = 1;
    final int lastTime  = 2;//倒计时时间
    final int cfgId     = 3;
    final int overTimestamp = 4;//结束时间戳

    @Override
    public int getId() {
        return ScriptEnum.CrossHorseBossActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


    @Override
    public void onCreate(MapObject mapObject, Object... objects) {

        List<CommonMessage.CrossAttribute> createParams = (List<CommonMessage.CrossAttribute>) objects[1];
        long overTime = 0L;
        int gourpid = 0;
        int congfigId = 0;
        for (CommonMessage.CrossAttribute cb :createParams) {
            gourpid = cb.getType();
            mapObject.getParams().put(groupId,gourpid);
            overTime =  cb.getValue();
            mapObject.getParams().put(lastTime, overTime);
            congfigId = cb.getParam1();
            mapObject.getParams().put(cfgId, congfigId);
            mapObject.getParams().put(overTimestamp, TimeUtils.Time() + overTime);
            break;
        }
        mapObject.addMapOnceScriptEventTimer(getId(), "OnGameOver",overTime);
        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTING);
        //创建BOSS
        createMonster(mapObject);
        logger.error("坐骑跨服副本创建  overTime {} gourpid {} congfigId {}" ,overTime,gourpid,congfigId);
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {


        return true;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {

        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.CrossHosreBoss.getValue());
        if (remainCount == 0) {
            player.setCamp(map.getMapModelId(), true);
            MessageUtils.notify_player(player, Notify.SHOWBOX, MessageString.DREAMBOSSTIMEOVERSERROR);
        } else {
            player.setCamp(0, true);
        }

        CrossHorseBossMessage.ResCrossHorseMapOverTime.Builder msg =  CrossHorseBossMessage.ResCrossHorseMapOverTime.newBuilder();
        long overTime =  (long)(map.getParams().get(overTimestamp)) - TimeUtils.Time() ;
        msg.setOverTime(overTime);
        int cfgId =  (int)(map.getParams().get(this.cfgId));
        msg.setCfgId(cfgId);
        MessageUtils.send_to_player(player,  CrossHorseBossMessage.ResCrossHorseMapOverTime.MsgID.eMsgID_VALUE,msg.build().toByteArray());

        Manager.retrieveResManager.getScript().onSendResourceFindChangeToGame(player, RetrieveType.HuangGushengTan.type());
        logger.error("玩家进入跨服坐骑BOSS {} ",player.getId());
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {


    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {
        Manager.bossManager.manager().syncBossDamageRank(monster);
    }

    private void createMonster(MapObject mapObject){
        int cfgId =  (int)(mapObject.getParams().get(this.cfgId));
        Cfg_Bossnew_HorseBoss_Bean bean = CfgManager.getCfg_Bossnew_HorseBoss_Container().getValueByKey(cfgId);
        if (bean == null){
            logger.error("Cfg_Bossnew_HorseBoss_Bean not find {} " ,cfgId);
            return;
        }
        Monster monster = MonsterManager.getInstance().createMonster(bean.getMonsterid());
        if (monster != null) {
            Position pos = bean == null ?  mapObject.getBrithPos():  new Position(  bean.getPos().get(0).get(0), bean.getPos().get(0).get(1));
            monster.changeLine(mapObject.getLineId());
            monster.changeMapId(mapObject.getId());
            monster.changeMapModelId(mapObject.getMapModelId());
            monster.setInitPos(pos);

            monster.setCamp(mapObject.getMapModelId(),true);
            Manager.mapManager.manager().onEnterMap(monster);
            logger.error("跨服坐骑Bss createMonster sucss  Camp {}  ",mapObject.getMapModelId());
        } else {
            logger.error("跨服坐骑Boss刷新怪物生成失败：monsterId=" + bean.getMonsterid());
        }
    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

        int cfgId =  (int)(map.getParams().get(this.cfgId));
        for (Player player :map.getPlayers().values()) {
            Manager.playerManager.manager().onUpdatePkState(player, PlayerDefine.PkStatePeace, true);//和平模式
            CrossHorseBossMessage.ResCrossBossDie.Builder msg = CrossHorseBossMessage.ResCrossBossDie.newBuilder();
            msg.setCfgID(cfgId);
            MessageUtils.send_to_player(player, CrossHorseBossMessage.ResCrossBossDie.MsgID.eMsgID_VALUE,msg.build().toByteArray());

        }
        Player p1 = (Player) monster.getHatreds().get(0).getTarget();
        Manager.dropManager.deal().specialDropReward(monster, p1, SpecialDropDefine.CrossHorseBoss, true, -1);
        Manager.bossManager.manager().synDropDataFromFightToGame(p1, DailyActiveDefine.CrossHosreBoss.getValue());
        Cfg_Bossnew_HorseBoss_Bean bean = CfgManager.getCfg_Bossnew_HorseBoss_Container().getValueByKey(cfgId);
        if (bean == null){
            logger.error("Cfg_Bossnew_HorseBoss_Bean not find {} " ,cfgId);
            return;
        }
        Manager.copyMapManager.manager().sendF2GCloneEnterAddOne(map, p1, (long)(bean.getScore()));

        CrossHorseBossMessage.F2PReqCrossHorseBossDie.Builder msg = CrossHorseBossMessage.F2PReqCrossHorseBossDie.newBuilder();
        msg.setCloneModelId(map.getMapModelId());
        msg.setFightId(map.getId());
        msg.setGroupId((int)(map.getParams().get(groupId)));
        msg.setModelConfigId(cfgId);
        MessageUtils.send_to_public( CrossHorseBossMessage.F2PReqCrossHorseBossDie.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        map.addMapOnceScriptEventTimer(getId(), "onMonsterDie",10000);
        logger.info("Bossnew_HorseBoss die {} mapCfgID {}  ",monster.getId(),cfgId);

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
            case "OnGameOver":
                onGameOver(map);
                break;
            case "onMonsterDie":
                onGameOver(map);
                break;
        }
    }
    private void onGameOver(MapObject mapObject){
        for (Player player :mapObject.getPlayers().values()) {
            Manager.copyMapManager.manager().onReqCopyMapOut(player);
        }
        mapObject.setStop(true);
        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTEND);
        logger.info("Bossnew_HorseBoss timeover  {}  ",cfgId);
    }

    @Override
    public void removeMap(MapObject map) {
        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(map.getId(), FightRoomState.FIGHTEND);
        logger.info("Bossnew_HorseBoss removeMap  {}  ",map.getId());
    }
}
