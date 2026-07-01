package common.crosshorseboss;

import com.data.CfgManager;
import com.data.bean.Cfg_Bossnew_HorseBoss_Bean;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.crosshorseboss.script.ICrosshorseBoss;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import game.message.CrossHorseBossMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by cxl on 2021/4/16.
 */
public class CrossHorseBossScript implements ICrosshorseBoss {


    private static final Logger log = LogManager.getLogger(CrossHorseBossScript.class);

    @Override
    public int getId() {
        return  ScriptEnum.CrossHorseBossScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 取消归属
     * @param player
     */
    @Override
    public void onReqCancelAffiliation(Player player, int cfgId) {
        Cfg_Bossnew_HorseBoss_Bean bean = CfgManager.getCfg_Bossnew_HorseBoss_Container().getValueByKey(cfgId);
        if (bean == null){
            log.error("Cfg_Bossnew_HorseBoss_Bean 配置表不存在  {}",cfgId);
            return;
        }
        if (player.playerCrossData.toFightSid <= 0){
            log.error("玩家不再战斗服  {}",player.getId());
            return;
        }
        CrossHorseBossMessage.G2FReqCancelAffiliation.Builder msg = CrossHorseBossMessage.G2FReqCancelAffiliation.newBuilder();
        msg.setCfgId(cfgId);
        msg.setPlayerId(player.getId());
        ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(),
                CrossHorseBossMessage.G2FReqCancelAffiliation.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        log.info("取消归属：  cfgid {}  fightSid： {}",cfgId,player.playerCrossData.toFightSid);

    }
    /**
     * 取消归属
     * @param playerId
     * @param cfgId
     */
    @Override
    public void onG2FReqCancelAffiliation(long playerId, int cfgId) {
       Player player =   PlayerManager.getInstance().getPlayerCache(playerId);
       if (player == null){
           log.error("玩家不再此跨服  {} ", player.getId());
           return;
       }
        MapObject mapObject =  Manager.mapManager.getMap(player.getCurGps().getMapId());
       if (mapObject == null){
           log.error("mapObject  == null  {} ", player.getCurGps().getMapId());
           return;
       }
        Cfg_Bossnew_HorseBoss_Bean bean = CfgManager.getCfg_Bossnew_HorseBoss_Container().getValueByKey(cfgId);
        if (bean == null){
            log.error("Cfg_Bossnew_HorseBoss_Bean 配置表不存在  {}",cfgId);
            return;
        }
        if (bean.getCloneid() != mapObject.getMapModelId()){
            log.error("取消的地图和玩家当前地图不匹配   {} curMapModelId {}",bean.getCloneid(),mapObject.getMapModelId() );
            return;
        }
        for (Monster monster : mapObject.getMonsters().values()) {
            if (monster.getModelId() == bean.getMonsterid()){
                long cleanPlayerId = 0;
                if (monster.getHatreds().size() > 0){
                    Fighter fighter = monster.getHatreds().get(0).getTarget();
                    cleanPlayerId =  fighter.getId();
                    monster.removeHatred(fighter);
                }
                Manager.bossManager.manager().syncBossDamageRank(monster);
                if (cleanPlayerId <=0){
                    log.info("取消归属 失败 cleanPlayerId  {}",cleanPlayerId);
                    break;
                }
                CrossHorseBossMessage.ResCancelAffiliationResult.Builder msg = CrossHorseBossMessage.ResCancelAffiliationResult.newBuilder();
                msg.setPlayerId(cleanPlayerId);
                MessageUtils.send_to_map(mapObject, CrossHorseBossMessage.ResCancelAffiliationResult.MsgID.eMsgID_VALUE,msg.build().toByteArray());
                break;
            }
        }
    }

    /**
     * 请求面板信息
     * @param player
     * @param level
     */
    @Override
    public void onReqCrossHorseBossPanel(Player player, int level) {

        int dailyRemainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.CrossHosreBoss.getValue());
        int dailyMaxCount = Manager.dailyActiveManager.deal().getDailyMaxCount(player, DailyActiveDefine.CrossHosreBoss.getValue());

        CrossHorseBossMessage.G2PReqCrossHorseBossPanel.Builder msg = CrossHorseBossMessage.G2PReqCrossHorseBossPanel.newBuilder();
        msg.setRoleId(player.getId());
        msg.setLevel(level);
        msg.setRemainCount(dailyRemainCount);
        msg.setMaxCount(dailyMaxCount);

        MessageUtils.send_to_public(CrossHorseBossMessage.G2PReqCrossHorseBossPanel.MsgID.eMsgID_VALUE,msg.build().toByteArray());
    }

    /**
     * 请求关注
     * @param player
     * @param bossId
     */
    @Override
    public void onReqFollowCrossHorseBoss(Player player, boolean isFollow, int bossId) {

        CrossHorseBossMessage.G2PReqFollowCrossHorseBoss.Builder msg = CrossHorseBossMessage.G2PReqFollowCrossHorseBoss.newBuilder();
        msg.setBossId(bossId);
        msg.setFollowValue(isFollow);
        msg.setRoleId(player.getId());
        MessageUtils.send_to_public(CrossHorseBossMessage.G2PReqFollowCrossHorseBoss.MsgID.eMsgID_VALUE,msg.build().toByteArray());
    }

    /**
     * 刷新提示
     * @param messInfo
     */
    public  void onP2GResCrossHorseBossRefreshTip(CrossHorseBossMessage.P2GResCrossHorseBossRefreshTip messInfo){
        List<Long> roleIds = messInfo.getRoleIdsList();
        CrossHorseBossMessage.ResCrossHorseBossRefreshTip.Builder msg = CrossHorseBossMessage.ResCrossHorseBossRefreshTip.newBuilder();
        msg.setBossId(messInfo.getBossId());
        Iterator<Long> iterator = roleIds.iterator();
        Set<Long> hasSend = new HashSet<>();
        while (iterator.hasNext()) {
            long roleId = iterator.next();
            Player player = Manager.playerManager.getPlayerOnline(roleId);
            if (player == null || hasSend.contains(roleId)) {
                continue;
            }
            hasSend.add(roleId);
            MessageUtils.send_to_player(player, CrossHorseBossMessage.ResCrossHorseBossRefreshTip.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }
}
