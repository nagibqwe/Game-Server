package common.copyMap.boss;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.bean.Cfg_Bossnew_world_Bean;
import com.data.struct.ReadArray;
import com.game.boss.log.BossDieReliveLog;
import com.game.boss.manager.BossManager;
import com.game.boss.struct.Boss;
import com.game.boss.struct.BossData;
import com.game.boss.struct.BossTypeConst;
import com.game.count.structs.VariantType;
import com.game.dailyactive.script.IDailyScript;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.drop.structs.SpecialDropDefine;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.monster.manager.MonsterManager;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.structs.Hatred;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import game.core.dblog.LogService;
import game.core.util.TimeUtils;
import game.message.BossMessage;
import game.message.BossMessage.BossInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 无极墟域
 */
public class WorldBossScript implements IDailyScript {

    private final static Logger log = LogManager.getLogger(WorldBossScript.class);

    @Override
    public int getId() {
        return ScriptEnum.WorldBossActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        //怪物进场
        onMonsterEnter(mapObject);

    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {

        return true;
    }

    @Override
    public void onEnterMap(Player player, MapObject mapObject, boolean login) {

        sendBossPanel(player, mapObject);

        sendBossInfo(player, mapObject);

        changeCamp(player, mapObject);
    }

    /**
     * 发送本层boss数据
     */
    @Override
    public void sendBossPanel(Player player, MapObject map) {

        BossMessage.ResOpenDreamBoss.Builder resMsg = BossMessage.ResOpenDreamBoss.newBuilder();
        int refreshTime;
        for (Boss dreamBoss : BossManager.getWorldBossMap().values()) {
            if (dreamBoss.getMapID() != map.getMapModelId()) {
                continue;
            }
            BossInfo.Builder bInfo = BossInfo.newBuilder();
            bInfo.setBossId(dreamBoss.getConfigId());

            refreshTime = dreamBoss.getNextTime() > 0 ? (int) ((dreamBoss.getNextTime() - TimeUtils.Time()) / 1000) : 0;
            if (refreshTime < 0) {
                refreshTime = 0;
            }
            bInfo.setRefreshTime(refreshTime);

            if (player.getFollowedBossList().contains(dreamBoss.getConfigId())
                    || player.getAutoFollowedBossList().contains(dreamBoss.getConfigId())) {
                bInfo.setIsFollowed(true);

                //重启服了的话的处理
                List<Long> followedPlayerList = BossManager.getBossFollowedMap().get(dreamBoss.getConfigId());
                if (followedPlayerList == null) {
                    followedPlayerList = new ArrayList<>();
                    BossManager.getBossFollowedMap().put(dreamBoss.getConfigId(), followedPlayerList);
                }
                if (!followedPlayerList.contains(player.getId())) {
                    followedPlayerList.add(player.getId());
                }
            } else {
                bInfo.setIsFollowed(false);
            }

            resMsg.addBossList(bInfo);
        }
        int dailyRemainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.WORLD_BOSS.getValue());
        int dailyMaxCount = Manager.dailyActiveManager.deal().getDailyMaxCount(player, DailyActiveDefine.WORLD_BOSS.getValue());
        int dailyCanBuyCount = Manager.dailyActiveManager.deal().getDailyCanBuyCount(player, DailyActiveDefine.WORLD_BOSS.getValue());
        int buyCount = player.getDailyActiveData().getDailyBuyCount().getOrDefault(DailyActiveDefine.WORLD_BOSS.getValue(), 0);

        resMsg.setBossType(BossTypeConst.WORLD_BOSS);
        resMsg.setRemainCount(dailyRemainCount);
        resMsg.setMaxCount(dailyMaxCount);
        resMsg.setCanBuyCount(dailyCanBuyCount);
        resMsg.setBuyCount(buyCount);
        MessageUtils.send_to_player(player, BossMessage.ResOpenDreamBoss.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
    }

    @Override
    public void onQuitMap(Player player, MapObject mapObject, boolean isQuit) {
    }

    /**
     * 怪物出生
     */
    public void onMonsterEnter(MapObject mapObject) {
        for (Boss dreamBoss : BossManager.getWorldBossMap().values()) {
            if (dreamBoss.getMapID() != mapObject.getMapModelId() || mapObject.getLineId() != 1) {
                continue;
            }
            //创建对应的墓碑(重启服后未到刷新时间的怪要建墓碑的)
            if (dreamBoss.getNextTime() > 0) {
                Manager.mapManager.createTombstone(mapObject, dreamBoss.getModelId());
                continue;
            }

            Monster monster = MonsterManager.getInstance().createMonster(dreamBoss.getModelId());
            if (monster != null) {
                Cfg_Bossnew_world_Bean bean = CfgManager.getCfg_Bossnew_world_Container().getValueByKey(dreamBoss.getModelId());
                if (bean.getIf_raward() != 0) {
                    monster.setCamp(mapObject.getMapModelId());
                }
                monster.changeLine(mapObject.getLineId());
                monster.changeMapId(mapObject.getId());
                monster.changeMapModelId(mapObject.getMapModelId());
                monster.setInitPos(dreamBoss.getPos());

                Manager.mapManager.manager().onEnterMap(monster);
                BossData data = ServerParamUtil.BossDataMap.get(dreamBoss.getConfigId());
                if (data != null) {
                    data.setBornTime(TimeUtils.Time());
                    data.setRebornTime(dreamBoss.getNextTime());
                } else {
                    log.error("Error! 刷新世界boss时获取其存库数据data失败，不该发生的，bossId=" + dreamBoss.getConfigId());
                }
                BossDieReliveLog bossDieLog = new BossDieReliveLog();
                bossDieLog.setBossId(dreamBoss.getModelId());
                bossDieLog.setMapId(mapObject.getMapModelId());
                bossDieLog.setType(1);
                LogService.getInstance().execute(bossDieLog);
            } else {
                log.error("世界Boss刷新怪物生成失败：monsterId=" + dreamBoss.getModelId());
            }

        }
        ServerParamUtil.saveWorldBoss();
    }

    @Override
    public void sendBossInfo(Player player, MapObject map) {
        Manager.bossManager.manager().sendBossInfo(player, BossManager.getWorldBossMap().values(), map.getZoneModelId(), BossTypeConst.WORLD_BOSS);
    }

    @Override
    public void onMonsterAfterDie(MapObject mapObject, Monster monster, Fighter fighter) {

    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onMonsterDie(MapObject mapObject, Monster monster, Fighter fighter) {
        if (!(fighter instanceof Player)) {
            return;
        }
        Player player = (Player) fighter;
        int bossId = monster.getModelId();
        Cfg_Bossnew_world_Bean bean = CfgManager.getCfg_Bossnew_world_Container().getValueByKey(bossId);
        if (bean == null) {
            log.error("世界boss死亡后获取策划数据失败，bossId=" + bossId);
            return;
        }

        Manager.dropManager.deal().specialDropReward(monster, player, SpecialDropDefine.WORLD_BOSS, bean.getIf_raward() == 1, -1);
        //更新次数
        for (Hatred hatred : monster.getHatreds()) {
            if (hatred.getHatred() <= 0) {
                continue;
            }
            if (!(hatred.getTarget() instanceof Player)) {
                continue;
            }

            Manager.controlManager.operate(player, FunctionVariable.Daily_Kill_WuJIArea_Boss_Times, 1);
            Manager.countManager.addVariant(player, VariantType.Daily_Kill_WuJIArea_Boss_Times, 1);
        }

        if (monster.getMakerId() != 0) {
            log.info(String.format("地图[%s]中召唤怪[%s]被玩家击杀[%s]", mapObject.getName(), monster.getName(), player.getName()));
            return;
        }

        //记录击杀
        Manager.bossManager.manager().addBossKilledRecord(mapObject, monster, player);
        //计算下次刷新时间
        Boss boss = BossManager.getWorldBossMap().get(bossId);
        if (boss == null) {
            log.error("世界boss死亡后缓存里找不到了，monsterId=" + monster.getModelId() + ", bossId=" + bossId);
            return;
        }

        BossData data = ServerParamUtil.BossDataMap.get(bossId);
        data.setDieNum(data.getDieNum() + 1);

        int rebornTime;
        if (data.getDieNum() > 1) { //非首次死亡的计算
            rebornTime = data.getReBornBaseTime();

            int lifeTime = (int) ((TimeUtils.Time() - data.getBornTime()) / 1000); //生命值相比于配置的标准值，重生时间做上下浮动
            if (lifeTime > bean.getStandard_time()) {
                rebornTime = rebornTime + bean.getFloat_time();
            } else if (lifeTime < bean.getStandard_time()) {
                rebornTime = rebornTime - bean.getFloat_time();
            } //"=="的情况不加减浮动值

            int curOpenServerDay = TimeUtils.getOpenServerDay();
            ReadArray<Integer> arr = Manager.bossManager.manager().getLimitTime(curOpenServerDay, bean.getLimit_time());
            if (arr != null) {
                if (rebornTime < arr.get(1)) { //低于下限了取下限值
                    rebornTime = arr.get(1);
                } else if (rebornTime > arr.get(2)) { //高于上限了取上线值
                    rebornTime = arr.get(2);
                }
            }
        } else { //首次死亡，重生时间按表里配置的初始值来
            rebornTime = bean.getInitial_time();
        }

        boss.setNextTime(TimeUtils.Time() + 1000 * rebornTime);

        data.setBornTime(0L);
        data.setReBornBaseTime(rebornTime);
        data.setRebornTime(boss.getNextTime());
        ServerParamUtil.saveWorldBoss();

        //TODO 击杀后推送给玩家当前怪物刷新情况
        Manager.bossManager.manager().syncWorldBossInfo(mapObject, bossId, BossTypeConst.WORLD_BOSS);
    }

    @Override
    public void onPlayerDie(MapObject mapObject, Fighter fighter, Player player) {

    }

    @Override
    public void action(MapObject mapObject, String method, Object[] params) {

    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter acktter) {
        if (!(acktter instanceof Player)) {
            return;
        }
        Player player = (Player) acktter;
        Manager.worldHelpManager.getScript().sendResSynHarmRank(BossTypeConst.WORLD_BOSS, player, monster);

        Manager.bossManager.manager().syncBossDamageRank(monster);
    }

    @Override
    public void removeMap(MapObject mapObject) {

    }

    /**
     * 刷新阵营
     *
     * @param player
     * @param map
     */
    @Override
    public void changeCamp(Player player, MapObject map) {
        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.WORLD_BOSS.getValue());
        if (remainCount == 0) {
            player.setCamp(map.getMapModelId(), true);

            if (!Manager.worldHelpManager.getScript().isHelp(player.getId())) {
                BossMessage.ResRankCountTips.Builder msg = BossMessage.ResRankCountTips.newBuilder();
                MessageUtils.send_to_player(player, BossMessage.ResRankCountTips.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        } else {
            player.setCamp(0, true);
        }
    }
}
