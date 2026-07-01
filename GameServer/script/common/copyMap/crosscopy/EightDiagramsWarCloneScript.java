package common.copyMap.crosscopy;

import com.data.CfgManager;
import com.data.Global;
import com.data.MessageString;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_EightCity_Bean;
import com.game.chat.structs.Notify;
import com.game.copymap.scripts.ICopyReliveScript;
import com.game.copymap.structs.FightRoomState;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.monster.manager.MonsterManager;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.welfare.struct.RetrieveType;
import game.core.map.Position;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.EightDiagramsMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by cxl on 2019/10/10.
 */
public class EightDiagramsWarCloneScript  implements ICopyReliveScript,IMapBaseScript {

    private final static Logger log = LogManager.getLogger(EightDiagramsWarCloneScript.class);
    @Override
    public int getId() {
        return ScriptEnum.EightDiagramsWar;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        mapObject.setAutoRemove(false);
        List<CommonMessage.CrossAttribute> createParams = (List<CommonMessage.CrossAttribute>) objects[1];
        for (CommonMessage.CrossAttribute cb :createParams)
        {
           String value =  cb.getParam();
           String[] cityList = value.split(",");
           int cityID   = Integer.parseInt(cityList[0]);
           int groupID  = Integer.parseInt(cityList[1]);
           int birthSid = Integer.parseInt(cityList[2]);
           int curSid   = Integer.parseInt(cityList[3]);
           int curCamp  = Integer.parseInt(cityList[4]);

           MapParam.getEightDaramsMapData(mapObject).setCiytID(cityID);
           MapParam.getEightDaramsMapData(mapObject).setGroupID(groupID);
           MapParam.getEightDaramsMapData(mapObject).setBirthSid(birthSid);
           MapParam.getEightDaramsMapData(mapObject).setCurSid(curSid);
           MapParam.getEightDaramsMapData(mapObject).setCurCamp(curCamp);
           Cfg_EightCity_Bean bean = CfgManager.getCfg_EightCity_Container().getValueByKey(cityID);
           if (bean!=null)
           {
               MapParam.getEightDaramsMapData(mapObject).setBossID(bean.getBossID());
               createMonster(mapObject);
           }
        }

        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());
        if (bean == null) {
            log.error("Cfg_Clone_mapBean配置地图异常：" + mapObject.getZoneModelId());
            return;
        }
        mapObject.addMapOnceScriptEventTimer(getId(), "OnGameOver", bean.getExist_time());

        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTING);
    }

    private void createMonster(MapObject map)
    {
        int bossID =  MapParam.getEightDaramsMapData(map).getBossID();
        int camp = MapParam.getEightDaramsMapData(map).getCurCamp();
        int cityId  = MapParam.getEightDaramsMapData(map).getCiytID();
        Monster monster = MonsterManager.getInstance().createMonster(bossID);
        if (monster != null) {
            Cfg_EightCity_Bean bean = CfgManager.getCfg_EightCity_Container().getValueByKey(cityId);
            Position  pos = bean == null ?  map.getBrithPos():  new Position(bean.getBossPos().get(0), bean.getBossPos().get(1));
            monster.changeLine(map.getLineId());
            monster.changeMapId(map.getId());
            monster.changeMapModelId(map.getMapModelId());
            monster.setInitPos(pos);
            camp =  camp == 0?100:camp;
            log.error("createMonster  Camp {}  ",camp);
            monster.setCamp(camp,true);
            Manager.mapManager.manager().onEnterMap(monster);
        } else {
            log.error("八星阵图Boss刷新怪物生成失败：monsterId=" + bossID);
        }

        addBuffToBirthCiytPlayer(map);
    }
    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return true;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {

        player.setCamp(player.playerCrossData.fightCampNo,true);
        Manager.playerManager.manager().onUpdatePkState(player, PlayerDefine.PkStateCamp, true);
        int cityId  = MapParam.getEightDaramsMapData(map).getCiytID();
        int birthSid = MapParam.getEightDaramsMapData(map).getBirthSid();
        int curSid = MapParam.getEightDaramsMapData(map).getCurSid();
        int sourceServerID = FightClientManager.getServerIdInFightServer(player);
        if (sourceServerID == birthSid  && sourceServerID != curSid)
            Manager.buffManager.deal().onAddBuff(player,player, Global.Manor_boss_buff);

        EightDiagramsMessage.F2PResEnterMapSucc.Builder msg = EightDiagramsMessage.F2PResEnterMapSucc.newBuilder();
        msg.setRoleID(player.getId());
        msg.setCityID(cityId);
        msg.setPlatSid(player.playerCrossData.platSid);
        MessageUtils.send_to_public( EightDiagramsMessage.F2PResEnterMapSucc.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        //副本剩余时间
        int lastTime =  (int)((map.getDelTime()- (1000 * 30)) - TimeUtils.Time())/1000;
        EightDiagramsMessage.ResLastTime.Builder msg1 = EightDiagramsMessage.ResLastTime.newBuilder();
        msg1.setSeconds(lastTime);
        MessageUtils.send_to_player(player, EightDiagramsMessage.ResLastTime.MsgID.eMsgID_VALUE,msg1.build().toByteArray());
        int groupID  = MapParam.getEightDaramsMapData(map).getGroupID();
        Manager.eightDiagramsManager.deal().enterMapSucc(player,cityId,groupID);

        Manager.retrieveResManager.getScript().onSendResourceFindChangeToGame(player, RetrieveType.BajiZhenTu.type());

    }


    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        Manager.buffManager.deal().onRemoveBuff(player,Global.Manor_boss_buff);
        int cityId  = MapParam.getEightDaramsMapData(map).getCiytID();
        int groupID  = MapParam.getEightDaramsMapData(map).getGroupID();
        Manager.eightDiagramsManager.deal().playerOutCity(player,cityId,groupID);
        EightDiagramsMessage.F2PPlayerOutCity.Builder msg = EightDiagramsMessage.F2PPlayerOutCity.newBuilder();
        msg.setRoleID(player.getId());
        msg.setCityID(cityId);
        msg.setPlatSid(player.playerCrossData.platSid);
        MessageUtils.send_to_public( EightDiagramsMessage.F2PPlayerOutCity.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        Manager.playerManager.manager().onUpdatePkState(player, PlayerDefine.PkStatePeace, true);//和平模式
    }

    @Override
    public void onDamage(MapObject map, Monster defense, long damage, Fighter attacker) {
        if (!(attacker instanceof Player)) {
            return;
        }
        int cityId  = MapParam.getEightDaramsMapData(map).getCiytID();
        int groupID  = MapParam.getEightDaramsMapData(map).getGroupID();
        Player player = (Player) attacker;

        Manager.eightDiagramsManager.deal().playerToBossHurt(damage,groupID,cityId,defense.getCurHp(),player);
       //EightDiagramsMessage.F2PPlayerToBossHurt.Builder msg = EightDiagramsMessage.F2PPlayerToBossHurt.newBuilder();
       //msg.setRoleID(player.getId());
       //msg.setCityID(cityId);
       //msg.setHurt(damage);
       //msg.setBossCurHP(defense.getCurHp());
       //msg.setPlatSid(player.playerCrossData.platSid);
       //msg.setRoleName(player.getName());
       //MessageUtils.send_to_public( EightDiagramsMessage.F2PPlayerToBossHurt.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onMonsterDie(MapObject mapObject, Monster monster, Fighter attacker) {
        if (!(attacker instanceof Player)) {
            log.error("attacker    {} ",attacker.getName());
            return;
        }
        Player player = (Player) attacker;
        int birthID =  MapParam.getEightDaramsMapData(mapObject).getBirthSid();
        int sourceServerID = FightClientManager.getServerIdInFightServer(player);
        MapParam.getEightDaramsMapData(mapObject).setCurSid(sourceServerID);
        MapParam.getEightDaramsMapData(mapObject).setCurCamp(player.getCamp());
        log.error("attacker   player.getCamp()   {}   playerName {} ",player.getCamp(),player.getName());
        int cityId  = MapParam.getEightDaramsMapData(mapObject).getCiytID();
        int groupID  = MapParam.getEightDaramsMapData(mapObject).getGroupID();
        Manager.eightDiagramsManager.deal().killBoss(player,cityId,groupID,player.getCamp());

        EightDiagramsMessage.F2PKillBoss.Builder msg = EightDiagramsMessage.F2PKillBoss.newBuilder();
        msg.setRoleID(player.getId());
        msg.setCityID(cityId);
        msg.setColorCamp(player.getCamp());
        msg.setPlatSid(player.playerCrossData.platSid);
        MessageUtils.send_to_public( EightDiagramsMessage.F2PKillBoss.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        Cfg_EightCity_Bean bean = CfgManager.getCfg_EightCity_Container().getValueByKey(cityId);
        if (bean == null){
            log.error("Cfg_EightCity_Bean is null {} " , cityId);
            return;
        }
        if (birthID > 0){
            MessageUtils.notify_Map(mapObject, Notify.CHAT_SYS_MARQUEE, MessageString.BAJI_CHANGE_NOTICE1 ,
                    "3&_" + birthID, ServerStr.getChatTableName(bean.getName()),"3&_" + sourceServerID);
        }else {
            MessageUtils.notify_Map(mapObject, Notify.CHAT_SYS_MARQUEE, MessageString.BAJI_CHANGE_NOTICE2 ,
                    ServerStr.getChatTableName(bean.getName()),"3&_" + sourceServerID);
        }
    }

    private void  addBuffToBirthCiytPlayer(MapObject mapObject)
    {
        int birthSid = MapParam.getEightDaramsMapData(mapObject).getBirthSid();
        int curSid = MapParam.getEightDaramsMapData(mapObject).getCurSid();
        for (Player player:mapObject.getPlayers().values())
        {
            int sourceServerID = FightClientManager.getServerIdInFightServer(player);
            if (sourceServerID == birthSid  && sourceServerID != curSid)
                 Manager.buffManager.deal().onAddBuff(player,player, Global.Manor_boss_buff);
            else
                Manager.buffManager.deal().onRemoveBuff(player,Global.Manor_boss_buff);
        }
    }
    @Override
    public void onMonsterAfterDie(MapObject mapObject, Monster monster, Fighter attacker) {
        createMonster(mapObject);
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject mapObject, Fighter attacker, Player player) {
        if (!(attacker instanceof Player)) {
            return;
        }
        Player attplayer = (Player) attacker;
        int groupID = MapParam.getEightDaramsMapData(mapObject).getGroupID();

        Manager.eightDiagramsManager.deal().killPlayer(groupID,attplayer);
        int curSid = MapParam.getEightDaramsMapData(mapObject).getCurSid();
        int sourceServerID = FightClientManager.getServerIdInFightServer(player);
        if (sourceServerID == curSid){
            player.changeCurPos(mapObject.getBriths().get(0),true);
        }else {
            player.changeCurPos(mapObject.getBriths().get(1),true);
        }

       //EightDiagramsMessage.F2PKillPlayer.Builder msg =  EightDiagramsMessage.F2PKillPlayer.newBuilder();
       //msg.setGroupID(groupID);
       //msg.setRoleID(attplayer.getId());
       //msg.setRoleName(playerName);
       //msg.setPlatSid(platSid);
       //MessageUtils.send_to_public( EightDiagramsMessage.F2PKillPlayer.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void action(MapObject mapObject, String method, Object[] params) {
        switch (method) {

            case "OnGameOver":
                onGameOver(mapObject);
                break;
        }
    }

    private void onGameOver(MapObject mapObject)
    {
        for (Player player :mapObject.getPlayers().values())
        {
           // player.setCamp(1);
            Manager.playerManager.manager().onUpdatePkState(player, PlayerDefine.PkStatePeace, true);//和平模式
        }
        mapObject.setStop(true);
    }

    @Override
    public void removeMap(MapObject mapObject) {
        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTREWARDEND);
    }

    /**
     * 计算副本复活点
     *
     * @param map
     * @param player
     * @return
     */
    @Override
    public Position doCreateRelivePosition(MapObject map, Player player) {
        int curCamp = MapParam.getEightDaramsMapData(map).getCurCamp();
        int myCamp = player.getCamp();
        int index = myCamp == curCamp ? 0 : 1;
        return map.getRelives().get(index);
    }
}
