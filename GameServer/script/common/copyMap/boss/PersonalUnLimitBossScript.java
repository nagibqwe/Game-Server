package common.copyMap.boss;

import com.data.CfgManager;
import com.data.bean.Cfg_Bossnew_world_Bean;
import com.game.boss.struct.Boss;
import com.game.boss.struct.BossTypeConst;
import com.game.count.structs.VariantType;
import com.game.drop.structs.SpecialDropDefine;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.monster.manager.MonsterManager;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2021/8/23 17:14
 * @Auth ZUncle
 */
public class PersonalUnLimitBossScript implements IMapBaseScript {

    final static Logger logger = LogManager.getLogger(PersonalUnLimitBossScript.class);


    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.PersonalUnLimitBossScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * 地图创建初始化
     *
     * @param mapObject
     * @param objects
     */
    @Override
    public void onCreate(MapObject mapObject, Object... objects) {

        Player player = (Player) objects[0];

        doRefreshMonster(mapObject, player, true);

        mapObject.addMapLoopScriptEventTimer(getId(), "tick", -1, 0, 1000, player);
    }

    /**
     * 怪物出生
     */
    public void doRefreshMonster(MapObject mapObject, Player player, boolean create) {

        long curTime = TimeUtils.Time();

        for (Cfg_Bossnew_world_Bean bean : CfgManager.getCfg_Bossnew_world_Container().getValuees()) {

            if (bean.getClone_map() != mapObject.getZoneModelId()) {
                continue;
            }

            Boss boss = player.getUnLimitBoss().get(bean.getID());
            if (boss == null) {
                boss = new Boss();
                boss.setConfigId(bean.getID());
                boss.setModelId(bean.getID());
                boss.setMapID(bean.getClone_map());
                boss.setNextTime(curTime);
                player.getUnLimitBoss().put(boss.getModelId(), boss);
            }

            if (create && boss.getNextTime() < 0) {
                refresh(mapObject, boss, bean);
                continue;
            }
            if (boss.getNextTime() < 0) {
                continue;
            }
            if (boss.getNextTime() > curTime) {
                continue;
            }
            boss.setNextTime(-1L);
            boss.setBornTime(curTime);
            refresh(mapObject, boss, bean);
        }
    }


    void refresh(MapObject mapObject, Boss boss, Cfg_Bossnew_world_Bean bean) {
        Monster monster = MonsterManager.getInstance().createMonster(boss.getModelId());
        if (monster == null) {
            logger.error("个人无限首领 Boss刷新怪物生成失败：monsterId=" + boss.getModelId());
            return;
        }
        monster.changeLine(mapObject.getLineId());
        monster.changeMapId(mapObject.getId());
        monster.changeMapModelId(mapObject.getMapModelId());
        monster.setInitPos(MapManager.getPos(bean.getPos().get(0), bean.getPos().get(1)));

        Manager.mapManager.manager().onEnterMap(monster);

//        logger.info("个人无限首领 刷新 boss={} ", boss);

    }

    /**
     * 是否满足进入条件
     * <p>
     * 若不满足，实现脚本给出提示或错误日志
     *
     * @param player
     * @param model  副本zoneId
     * @param level
     * @return 是否满足条件
     */
    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return true;
    }

    /**
     * 进入副本地图接口
     *  @param player
     * @param map
     * @param login
     */
    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {

        Manager.bossManager.manager().sendBossInfo(player, player.getUnLimitBoss().values(), map.getZoneModelId(), BossTypeConst.WORLD_BOSS);

    }

    /**
     * 离开副本地图接口
     *
     * @param player
     * @param map
     * @param isQuit
     */
    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {

        map.setAutoRemove(true);
        map.setLastHasPlayerTime(1);

    }

    /**
     * 伤害接口
     *
     * @param mapObject
     * @param monster
     * @param damage
     * @param attacker
     */
    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

    }

    /**
     * 怪物死亡接口
     *
     * @param map
     * @param monster
     * @param attacker
     */
    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

        Player player = (Player) attacker;
        Boss boss = player.getUnLimitBoss().get(monster.getModelId());
        if (boss == null) {
            return;
        }
        Cfg_Bossnew_world_Bean bean = CfgManager.getCfg_Bossnew_world_Container().getValueByKey(boss.getModelId());

        Manager.bossManager.manager().calcRefreshTime(boss);

        Manager.dropManager.deal().specialDropReward(monster, player, SpecialDropDefine.WORLD_BOSS, bean.getIf_raward() == 1, -1);

        Manager.countManager.addVariant(player, VariantType.Daily_Kill_UnLimit_Boss_Times, 1);

        //记录击杀
        Manager.bossManager.manager().addBossKilledRecord(map, monster, player);

        Manager.bossManager.manager().sendBossInfo(player, player.getUnLimitBoss().values(), map.getZoneModelId(), BossTypeConst.WORLD_BOSS);
    }

    /**
     * 怪物死亡后
     *
     * @param map
     * @param monster
     * @param attacker
     */
    @Override
    public void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker) {


    }

    /**
     * 怪物脱离战斗
     *
     * @param map
     * @param monster
     * @param attacker
     */
    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    /**
     * 玩家死亡接口
     *
     * @param map
     * @param attacker
     * @param player
     */
    @Override
    public void onPlayerDie(MapObject map, Fighter attacker, Player player) {

    }

    /**
     * 定时执行的函数
     *
     * @param map
     * @param method
     * @param params
     */
    @Override
    public void action(MapObject map, String method, Object[] params) {

        Player player = (Player) params[0];

        if (!map.getPlayers().containsKey(player.getId())){
            return;
        }
        if ("tick".equals(method)) {
            doRefreshMonster(map, player, false);
        }

    }

    /**
     * 删除地图调用接口
     *
     * @param map
     */
    @Override
    public void removeMap(MapObject map) {

    }

}
