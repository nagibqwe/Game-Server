package common.devilseries;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Cross_devil_Group_Boss_Bean;
import com.data.bean.Cfg_Cross_devil_Group_Copy_Bean;
import com.data.bean.Cfg_Cross_devil_Group_Rank_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.boss.struct.Boss;
import com.game.boss.struct.BossTypeConst;
import com.game.devilseries.scripts.IDevilSeriesManagerScript;
import com.game.devilseries.structs.DevilCopyData;
import com.game.devilseries.structs.Devilintegral;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.manager.MonsterManager;
import com.game.monster.structs.Monster;
import com.game.newfashion.manager.NewFashionManager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.structs.Hatred;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.BossMessage;
import game.message.DevilSeriesMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 除魔团
 * Created by cxl on 2021/5/18.
 */
public class DevilSeriesCopyMapScript  implements IMapBaseScript {

    final Logger logger = LogManager.getLogger(DevilSeriesCopyMapScript.class);


    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        mapObject.setAutoRemove(false);
        long curTime = TimeUtils.Time();
        String headName = (String)objects[0];
        Player player =  Manager.playerManager.getPlayerCache( mapObject.getOwnId());
        if (player == null){
            logger.info("创建副本人不存在 :" + mapObject.getOwnId());
            return;
        }
        DevilCopyData copyData = new DevilCopyData();
        copyData.setHeadName(headName);
        copyData.setCloneId(mapObject.getZoneModelId());
        copyData.setMapId(mapObject.getId());
        //记录角色id
        copyData.setRoleId(player.getId());
        copyData.setCareer(player.getCareer());
        copyData.setFashionHead(player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID());
        copyData.setFashionFrame(player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID());
        copyData.setCustomHeadPath(player.getCustomHeadPath());
        copyData.setUseCustomHead(player.isUseCustomHead());


        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());
        if (bean == null) {
            logger.info("副本不存在:" + mapObject.getZoneModelId());
            return ;
        }
        copyData.setEndTime(curTime + bean.getEnter_time() + bean.getExist_time());
        Manager.devilSeriesManager.getDevilOpenCopyDataMap().put(copyData.getMapId(),copyData);
        mapObject.addMapLoopScriptEventTimer(getId(), "tick", -1, 0, 1000);
        mapObject.addMapOnceScriptEventTimer(getId(), "timeoutEnd", bean.getEnter_time()+ bean.getExist_time());
        mapObject.addMapOnceScriptEventTimer(getId(), "kickOutPlayer", bean.getEnter_time()+ bean.getExist_time() + 10000);
        initAllMonster(mapObject);
        sendFollowInfo(mapObject,copyData);
        logger.info("除魔团副本创建成功:{}创建人:{} EndTime  {} ",mapObject.getZoneModelId(),headName,copyData.getEndTime());
    }

    private void sendFollowInfo(MapObject mapObject,DevilCopyData devilCopyData){

        DevilSeriesMessage.ResCreateDeviBossMapResult.Builder msg = DevilSeriesMessage.ResCreateDeviBossMapResult.newBuilder();
        DevilSeriesMessage.DeviBossState.Builder builder = DevilSeriesMessage.DeviBossState.newBuilder();
        builder.setCloneId(mapObject.getZoneModelId());
        builder.setFollowValue(true);
        DevilSeriesMessage.FollowData.Builder builder1 =  DevilSeriesMessage.FollowData.newBuilder();
        builder1.setEndTime(devilCopyData.getEndTime());
        builder1.setMapId(mapObject.getZoneModelId());
        builder1.setHeadName(devilCopyData.getHeadName());
        builder1.setCareer(devilCopyData.getCareer());
//        builder1.setFashionHead(devilCopyData.getFashionHead());
//        builder1.setFashionFrame(devilCopyData.getFashionFrame());

        builder1.setRoleId(devilCopyData.getRoleId());

        builder1.setHead(MapUtils.getHead(devilCopyData.getFashionHead(),devilCopyData.getFashionFrame(),devilCopyData.getCustomHeadPath(),devilCopyData.isUseCustomHead()));

        builder.addFollowDataList(builder1.build());
        msg.setDeviBoss(builder.build());

        for (Player player : Manager.playerManager.getPlayersCache().values()){
            if (player.getDevil().getFollowDevilCopyList().contains(mapObject.getZoneModelId())){
                MessageUtils.send_to_player(player,  DevilSeriesMessage.ResCreateDeviBossMapResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
    }


    private void initAllMonster(MapObject mapObject){
       Cfg_Cross_devil_Group_Copy_Bean bean =  CfgManager.getCfg_Cross_devil_Group_Copy_Container().getValueByKey(mapObject.getZoneModelId());
       if (bean == null){
           logger.info("Cfg_Cross_devil_Group_Copy_Bean 不存在:" + mapObject.getZoneModelId());
           return ;
       }
       ConcurrentHashMap<Long,ConcurrentHashMap<Integer, Boss>> DevilSerierBoss =  Manager.devilSeriesManager.getDevilSerierBoss();
       ConcurrentHashMap<Integer, Boss> bossConcurrentHashMap = new ConcurrentHashMap<>();
        DevilSerierBoss.put(mapObject.getId(),bossConcurrentHashMap);
       for (ReadArray<Integer> bossArr : bean.getBoss().getValuees()){
           Monster monster = MonsterManager.getInstance().createMonster(bossArr.get(0));
           if (monster != null) {
               Position pos =  new Position(bossArr.get(1),bossArr.get(2));
               monster.changeLine(mapObject.getLineId());
               monster.changeMapId(mapObject.getId());
               monster.changeMapModelId(mapObject.getMapModelId());
               monster.setInitPos(pos);

               monster.setCamp(mapObject.getMapModelId(),true);
               Manager.mapManager.manager().onEnterMap(monster);

               Boss boss =  bossConcurrentHashMap.get(bossArr.get(0));
               if (boss == null){
                   boss = new Boss();
                   bossConcurrentHashMap.put(bossArr.get(0),boss);
               }
               boss.setConfigId(bean.getId());
               boss.setModelId(bossArr.get(0));
               boss.setMapID(mapObject.getZoneModelId());
               boss.setId(mapObject.getId());
               boss.setNextTime(0l);
               boss.setPos(pos);

               logger.error(" 除魔团 createMonster sucss  Camp {}  ",mapObject.getZoneModelId());
           } else {
               logger.error("除魔团Boss刷新怪物生成失败：monsterId=" + mapObject.getZoneModelId());
           }
       }
    }

    private void refreshMonster(MapObject mapObject, Boss boss){

        Monster monster = MonsterManager.getInstance().createMonster(boss.getModelId());
        if (monster != null) {
            monster.changeLine(mapObject.getLineId());
            monster.changeMapId(mapObject.getId());
            monster.changeMapModelId(mapObject.getMapModelId());
            Position position = new Position();
            position.setX(boss.getPos().getX());
            position.setY(boss.getPos().getY());
            monster.setInitPos(position);
            monster.setCamp(mapObject.getMapModelId());
            Manager.mapManager.manager().onEnterMap(monster);
            boss.setNextTime(0L);
        }
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return false;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        sendBossInfo(player,map);

        DevilCopyData devilCopyData =  Manager.devilSeriesManager.getDevilOpenCopyDataMap().get(map.getId());
        DevilSeriesMessage.ResEnterDeviBossMap.Builder msg = DevilSeriesMessage.ResEnterDeviBossMap.newBuilder();
        msg.setCloneId(map.getZoneModelId());
        msg.setEndTime(devilCopyData.getEndTime() - TimeUtils.Time());
        MessageUtils.send_to_player(player,  DevilSeriesMessage.ResEnterDeviBossMap.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        Manager.controlManager.operate(player, FunctionVariable.If_today_Slayer, 1);
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {

    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {
        if (!(attacker instanceof Player)) {
            return;
        }
        Player player = (Player) attacker;
        Manager.worldHelpManager.getScript().sendResSynHarmRank(BossTypeConst.SERIES_BOSS, player, monster);
    }

    //积分计算
    private void calculateIntgerl(MapObject map, Monster monster){
        //---算积分 start
        Cfg_Cross_devil_Group_Boss_Bean boss_bean = CfgManager.getCfg_Cross_devil_Group_Boss_Container().getValueByKey(monster.getModelId());
        if (boss_bean == null){
            logger.error("Cfg_Cross_devil_Group_Boss_Bean is null {}" ,monster.getModelId());
            return;
        }
        ConcurrentHashMap<Long, ConcurrentHashMap<Long, Devilintegral>> mapIntegrals =  Manager.devilSeriesManager.getPlayerBossIntegrals();
        ConcurrentHashMap<Long, Devilintegral> playerIntegrals;
        if (!mapIntegrals.containsKey(map.getId())){
            playerIntegrals = new ConcurrentHashMap<>();
            mapIntegrals.put(map.getId(),playerIntegrals);
        }else {
            playerIntegrals = mapIntegrals.get(map.getId());
        }
        int allPoint =  boss_bean.getPoint();
        long allHatred = 0;
        for (Hatred hatred :   monster.getHatreds()){
            allHatred +=hatred.getHatred();
        }
        long eachPointNeedDamage =   allHatred/allPoint;
        Devilintegral devilintegral;
        long now = TimeUtils.Time();
        for (Hatred hatred :   monster.getHatreds()){
            int addPoint =  (int) (hatred.getHatred() / eachPointNeedDamage);
            if (!playerIntegrals.containsKey( hatred.getTarget().getId())){
                devilintegral = new Devilintegral();
                devilintegral.setIntegral(addPoint);
                devilintegral.setRoleId(hatred.getTarget().getId());
                devilintegral.setName(hatred.getTarget().getName());
                devilintegral.setIntegralTime(now);
                playerIntegrals.put(hatred.getTarget().getId(),devilintegral);
            }else {
                devilintegral =  playerIntegrals.get(hatred.getTarget().getId());
                addPoint  +=devilintegral.getIntegral();
                devilintegral.setIntegral(addPoint);
                devilintegral.setIntegralTime(now);
            }
        }

        IDevilSeriesManagerScript script =  Manager.devilSeriesManager.getScript();
        List<Devilintegral> devilintegrals =  playerIntegrals.values().stream().sorted(script::compareIntegral).collect(Collectors.toList());
        ConcurrentHashMap<Long, List<Devilintegral>> integralRankMap =  Manager.devilSeriesManager.getIntegralsRank();
        integralRankMap.put(map.getId(),devilintegrals);
        int rank = 1;
        DevilSeriesMessage.ResSynDeviBossIntegral.Builder msg = DevilSeriesMessage.ResSynDeviBossIntegral.newBuilder();
        DevilSeriesMessage.DeviBossIntegral.Builder builder;
        //生成前50名的消息数据结构
        for (Devilintegral devilintegral1 :devilintegrals){
            if (rank >  Manager.devilSeriesManager.MaxRank){
                devilintegral1.setRank(0);
            }else {
                devilintegral1.setRank(rank);
                builder = DevilSeriesMessage.DeviBossIntegral.newBuilder();
                builder.setRank(rank);
                builder.setIntergral(devilintegral1.getIntegral());
                builder.setName(devilintegral1.getName());
                msg.addIntegralList(builder.build());
            }
            rank++;
        }
        //同步给所有玩家
        for (Devilintegral devilintegral1 :devilintegrals){
            msg.setSelfRank(devilintegral1.getRank());
            msg.setSelfIntergral(devilintegral1.getIntegral());
            Player player =   Manager.playerManager.getPlayerCache(devilintegral1.getRoleId());
            if (player == null ){
                continue;
            }
            MessageUtils.send_to_player(player,  DevilSeriesMessage.ResSynDeviBossIntegral.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
        //---算积分 end;
    }
    private void restRefreshBoss(MapObject map,Monster monster){
        // boss刷新倒计时重置
        ConcurrentHashMap<Long,ConcurrentHashMap<Integer, Boss>> DevilSerierBoss =  Manager.devilSeriesManager.getDevilSerierBoss();
        if (!DevilSerierBoss.containsKey(map.getId())){
            logger.error("onMonsterDie map.getId不存在 {} ",map.getId());
            return;
        }
        ConcurrentHashMap<Integer, Boss> bossConcurrentHashMap =  DevilSerierBoss.get(map.getId());
        Boss boss =  bossConcurrentHashMap.get(monster.getModelId());
        Cfg_Cross_devil_Group_Boss_Bean bean = CfgManager.getCfg_Cross_devil_Group_Boss_Container().getValueByKey(boss.getModelId());
        if (bean == null){
            logger.error("Cfg_Cross_devil_Group_Boss_Bean is null {} ",boss.getModelId());
            return;
        }
        boss.setNextTime(TimeUtils.Time() + bean.getRefresh_time() * 1000 );
    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

        //积分计算
        calculateIntgerl(map,monster);

        //重置刷新boss
        restRefreshBoss(map,monster);

        //同步地图上的人BOSS刷新时间
        ConcurrentHashMap<Integer, Boss> bossMap =  Manager.devilSeriesManager.getDevilSerierBoss().get(map.getId());
        if (bossMap == null){
            logger.error( "bossMap  == null {}",map.getId());
            return;
        }
        Boss boss =  bossMap.get(monster.getModelId());
        BossMessage.ResBossRefreshInfo.Builder resMsg = BossMessage.ResBossRefreshInfo.newBuilder();
        BossMessage.BossInfo.Builder bInfo = BossMessage.BossInfo.newBuilder();
        bInfo.setBossId(boss.getModelId());
        int refreshTime;
        refreshTime = boss.getNextTime() > 0 ? (int) ((boss.getNextTime() - TimeUtils.Time()) / 1000) : 0;
        if (refreshTime < 0) {
            refreshTime = 0;
        }
        bInfo.setRefreshTime(refreshTime);
        resMsg.addBossRefreshList(bInfo);
        resMsg.setBossType(BossTypeConst.SERIES_BOSS);
        MessageUtils.send_to_map(map, BossMessage.ResBossRefreshInfo.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
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
            case "tick":
                tick(map);
                break;
            case "timeoutEnd":
                timeoutEnd(map);
                break;
            case "kickOutPlayer":
                kickOutPlayer(map);
                break;
        }
    }

    private void tick(MapObject mapObject){
        ConcurrentHashMap<Long,ConcurrentHashMap<Integer, Boss>> DevilSerierBoss =  Manager.devilSeriesManager.getDevilSerierBoss();
        if (!DevilSerierBoss.containsKey(mapObject.getId())){
            logger.info("副本ID不存在 {} " ,mapObject.getId());
            return;
        }
        ConcurrentHashMap<Integer, Boss> bossConcurrentHashMap =  DevilSerierBoss.get(mapObject.getId());
        long now = TimeUtils.Time();
        for (Map.Entry<Integer, Boss> entry: bossConcurrentHashMap.entrySet()){
            Boss boss = entry.getValue();
            if (boss.getNextTime() <= 0){
                continue;
            }
            if (now >= boss.getNextTime()){
                refreshMonster(mapObject,boss);
            }
        }
    }
    private void timeoutEnd(MapObject mapObject){
        //发奖
        sendReward(mapObject);
        sendCapReward(mapObject);
        for (Player player : mapObject.getPlayers().values()){
            player.setCamp(mapObject.getMapModelId(),true);
        }
        //删除地图缓存
        Manager.devilSeriesManager.getDevilOpenCopyDataMap().remove(mapObject.getId());
        Manager.devilSeriesManager.getDevilSerierBoss().remove(mapObject.getId());
        Manager.devilSeriesManager.getPlayerBossIntegrals().remove(mapObject.getId());
        Manager.devilSeriesManager.getIntegralsRank().remove(mapObject.getId());

    }
    private void kickOutPlayer(MapObject mapObject){
        for (Player player : mapObject.getPlayers().values()){
            Manager.copyMapManager.manager().onReqCopyMapOut(player);
        }
        mapObject.setStop(true);
    }


    //发团长奖励
    private void sendCapReward(MapObject mapObject){
        DevilCopyData devilCopyData =  Manager.devilSeriesManager.getDevilOpenCopyDataMap().get(mapObject.getId());
        if (devilCopyData == null){
            logger.error("团长未找到 {}",mapObject.getId());
            return;
        }
        Cfg_Cross_devil_Group_Copy_Bean bean = CfgManager.getCfg_Cross_devil_Group_Copy_Container().getValueByKey(devilCopyData.getCloneId());
        if (bean==null){
            logger.error("Cfg_Cross_devil_Group_Copy_Bean null {}",devilCopyData.getCloneId());
            return;
        }

        List<Item> items =   Item.createItems(bean.getCap_Reward());
        if (items!=null && items.size()>0){
            Manager.mailManager.sendMailToPlayer(mapObject.getOwnId(),
                    MessageString.System, MessageString.System, MessageString.Cross_devil_Group_reward_cap_title,
                    MessageString.Cross_devil_Group_reward_cap_tex, items, ItemChangeReason.DevilCopyMapGet);
        }
    }
    //发普通奖励
    private void sendReward(MapObject mapObject){
        List<Devilintegral> devilintegrals =  Manager.devilSeriesManager.getIntegralsRank().get(mapObject.getId());
        if (devilintegrals == null){
            logger.info("devilintegrals  is null {}",mapObject.getId());
            return;
        }

        logger.info("除魔团副本结束 开始发奖  {}" ,mapObject.getId());
        Cfg_Cross_devil_Group_Rank_Bean rank_bean ;
        for (Devilintegral devilintegral: devilintegrals){
            rank_bean = null;
            for ( Cfg_Cross_devil_Group_Rank_Bean bean : CfgManager.getCfg_Cross_devil_Group_Rank_Container().getValuees()){
                if ( devilintegral.getRank() >= bean.getLower_Limit() && devilintegral.getRank() <= bean.getUpper_Limit()){
                    if (ServerParamUtil.worldLv >=bean.getWorld_Level_Limit().get(0) &&
                            ServerParamUtil.worldLv <= bean.getWorld_Level_Limit().get(1)&&
                             bean.getCopyType() == mapObject.getZoneModelId()){
                        rank_bean = bean;
                        break;
                    }
                    if (ServerParamUtil.worldLv >=bean.getWorld_Level_Limit().get(0)
                            &&  bean.getWorld_Level_Limit().get(1) < 0 &&
                            bean.getCopyType() == mapObject.getZoneModelId()){
                        rank_bean = bean;
                        break;
                    }
                }
            }
            if (rank_bean == null){
                logger.error("策划配置有错----没取到排名 rank {}",devilintegral.getRank() );
                continue;
            }

            Player player = Manager.playerManager.getPlayerOnline(devilintegral.getRoleId());
            List<Item> items =   Item.createItems(rank_bean.getReward());
            if (items!=null && items.size()>0){
                if (player!=null){
                    Manager.backpackManager.manager().addItems(player, items,ItemChangeReason.DevilCopyMapGet , IDConfigUtil.getLogId());
                }else {
                    Manager.mailManager.sendMailToPlayer(devilintegral.getRoleId(),
                            MessageString.System, MessageString.System, MessageString.Cross_devil_Group_reward_offline_title,
                            MessageString.Cross_devil_Group_reward_offline_tex, items, ItemChangeReason.DevilCopyMapGet);
                }
            }
        }
    }

    @Override
    public void removeMap(MapObject map) {

    }

    @Override
    public int getId() {
        return ScriptEnum.DevilSeriesCopyMapScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


    public void sendBossInfo(Player player,MapObject mapObject) {
        ConcurrentHashMap<Integer, Boss> bossMap =  Manager.devilSeriesManager.getDevilSerierBoss().get(mapObject.getId());
        if (bossMap == null){
            logger.error("地图异常 刷新Boss失败 {}",mapObject.getId());
            return;
        }

        //推送当前副本地图世界boss刷新信息
        BossMessage.ResBossRefreshInfo.Builder resMsg = BossMessage.ResBossRefreshInfo.newBuilder();
        int refreshTime;
        for (Boss boss :bossMap.values()) {
            if (boss.getMapID() != mapObject.getZoneModelId()) {
                continue;
            }

            BossMessage.BossInfo.Builder bInfo = BossMessage.BossInfo.newBuilder();
            bInfo.setBossId(boss.getModelId());
            refreshTime = boss.getNextTime() > 0 ? (int) ((boss.getNextTime() - TimeUtils.Time()) / 1000) : 0;
            if (refreshTime < 0) {
                refreshTime = 0;
            }
            bInfo.setRefreshTime(refreshTime);
            resMsg.addBossRefreshList(bInfo);
        }
        resMsg.setBossType(BossTypeConst.SERIES_BOSS);
        MessageUtils.send_to_player(player, BossMessage.ResBossRefreshInfo.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
    }
}
