package common.copyMap.crossrank;


import com.data.FunctionVariable;
import com.game.crossrank.manager.CrossRankManager;
import com.game.crossrank.scripts.ICrossRank;
import com.game.db.bean.RankPlayer;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.ranklist.manager.RankListManager;
import com.game.ranklist.structs.RankType;
import com.game.script.structs.ScriptEnum;
import com.game.server.log.WorldLevelChangeLog;
import com.game.structs.GlobalType;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.message.CrossRankMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by CXL on 2020/4/13.
 */
public class CrossRankScript implements ICrossRank,IScript {

    private static final Logger log = LogManager.getLogger(CrossRankScript.class);
    @Override
    public int getId() {
        return ScriptEnum.CrossRankScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


    public  void sendRankInfoToPublic(){
        ConcurrentHashMap<Integer, Long> rankLevelMap = RankListManager.getTempRankMap().get(RankType.LEVEL_RANK);
        ConcurrentHashMap<Integer, Long> rankAllMap = RankListManager.getTempRankMap().get(RankType.FIGHT_POWER_RANK);
        int len = rankLevelMap.size() >= CrossRankManager.CrossRankMax ? CrossRankManager.CrossRankMax: rankLevelMap.size();
        List<Long>  rankLevelList = new ArrayList<>();
        for (int i = 0; i<len;i++){
            if (!rankLevelMap.containsKey(i+1)){
                log.error("未找到该名次 对应的角色"  + i+1);
                continue;
            }
            rankLevelList.add((rankLevelMap.get(i+1)));
        }
        len = rankAllMap.size() >= CrossRankManager.CrossRankMax ? CrossRankManager.CrossRankMax: rankAllMap.size();
        List<Long> rankAllList = new ArrayList<>();
        for (int i = 0; i<len;i++){
            if (!rankAllMap.containsKey(i+1)){
                log.error("未找到该名次 对应的角色"  + i+1);
                continue;
            }
            rankAllList.add(rankAllMap.get(i+1));
        }
        for (long roleid : rankLevelList){
            if (!rankAllList.contains(roleid)){
                rankAllList.add(roleid);
            }
        }
        if (rankAllList.size()<=0)
            return;
        CrossRankMessage.ReqG2PSyncCrossRankInfo.Builder msg =  CrossRankMessage.ReqG2PSyncCrossRankInfo.newBuilder();
        List<CrossRankMessage.CrossRankInfo> crossTypeRankInfoList = new ArrayList<>();
        for (long roleID : rankAllList){
            PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(roleID);
            if (pwi == null) {
                continue;
            }
            if (pwi.getLevel() < 110){//小于11级不进入排行
                continue;
            }
            crossTypeRankInfoList.add(createRankInfo(pwi));
         }
        if (crossTypeRankInfoList.size()>0){
            msg.addAllSyncRankInfoList(crossTypeRankInfoList);
            MessageUtils.send_to_public(CrossRankMessage.ReqG2PSyncCrossRankInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    private CrossRankMessage.CrossRankInfo createRankInfo(PlayerWorldInfo pwi){
        CrossRankMessage.CrossRankInfo.Builder crossRankInfo = CrossRankMessage.CrossRankInfo.newBuilder();
        int serverID = ServerConfig.getServerId();
        crossRankInfo.setRoleId(pwi.getRoleid());
        crossRankInfo.setRank(0);
        crossRankInfo.setRoleName(pwi.getRolename());
        crossRankInfo.setServerId(serverID);
        crossRankInfo.setCareer(pwi.getCareer());
        crossRankInfo.setStateVip(pwi.getStateVip());
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(pwi.getRoleid());
        int level  = 0;
        long fightPower = 0L;
        if (rankPlayer == null){
            level  = pwi.getLevel();
            fightPower =  pwi.getFightPower();
        }else {
            level  = rankPlayer.getLevel();
            fightPower =  rankPlayer.getFightPower();
        }
        crossRankInfo.setLevel(level);
        crossRankInfo.setFightPower(fightPower);
        crossRankInfo.setFacade(MapUtils.getFacade(pwi));
        return crossRankInfo.build();
    }

    /**
     * 跨服世界等级
     * @param newCrossLevel
     */
    public void onP2GCrossWorldLv(int newCrossLevel){
        int oldLevel = GlobalType.getWorldLevel();
        if (newCrossLevel > oldLevel){
            GlobalType.setWorldLevel(newCrossLevel);

            log.info("服务器调整新的世界等级为：" + newCrossLevel);
            WorldLevelChangeLog wlcl = new WorldLevelChangeLog();
            wlcl.setNewLevel(newCrossLevel);
            wlcl.setOldLevel(oldLevel);
            wlcl.setPeoples(0);
            wlcl.setTotalLevel(0);
            LogService.getInstance().execute(wlcl);
            checkWorldLevelLimit();
        }

    }
    //世界等级改变需要检测是否有 系统功能的开关条件是由世界等级来开启的
    private void checkWorldLevelLimit() {
        for (Player player : Manager.playerManager.getPlayersCache().values()) {
            if (!player.isOnline()) {
                continue;
            }
            Manager.controlManager.operate(player, FunctionVariable.WorldLevelLimit, 1);
        }
    }
}
