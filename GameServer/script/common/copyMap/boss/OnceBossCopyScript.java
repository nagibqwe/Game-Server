package common.copyMap.boss;

import com.data.CfgManager;
import com.data.Global;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Clone_monster_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.boss.struct.BossTypeConst;
import com.game.drop.structs.SpecialDropDefine;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import game.core.map.Position;
import game.message.BossMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 假无限层
 */
public class OnceBossCopyScript implements IMapBaseScript {

    private static final Logger logger = LogManager.getLogger(OnceBossCopyScript.class);

    @Override
    public int getId() {
        return ScriptEnum.OnceCopyActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {

        mapObject.getParams().putIfAbsent(getId(), new ArrayList<Integer>());
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());
        mapObject.addMapOnceScriptEventTimer(getId(), "kickOut", bean.getExist_time());

        refreshBoss(mapObject);
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
//        List<Integer> onceCopyData = player.getOnceBossData();
//        Cfg_Clone_monster_Bean bean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(model * 1000 + 1);
//        if (bean == null) {
//            return false;
//        }
//        for (int i = 0; i < bean.getMonster_information().getValuees().length; i++) {
//            if (!onceCopyData.contains(bean.getMonster_information().get(i).get(0))) {
//                return true;
//            }
//        }
//        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.UnlimitBossNotice1);
        return true;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {

        List<Integer> onceCopyData = map.getParam(getId());
        Cfg_Clone_monster_Bean bean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(map.getZoneModelId() * 1000 + 1);
        if (bean == null) {
            return;
        }

        BossMessage.ResBossRefreshInfo.Builder resMsg = BossMessage.ResBossRefreshInfo.newBuilder();
        for (int i = 0; i < bean.getMonster_information().getValuees().length; i++) {
            ReadArray<Integer> ll = bean.getMonster_information().getValuees()[i];
            addBossDieInfo(resMsg, ll.get(0), !onceCopyData.contains(ll.get(0)));
        }
        resMsg.setBossType(BossTypeConst.ONCE_BOSS);
        MessageUtils.send_to_player(player, BossMessage.ResBossRefreshInfo.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());

        if (map.getZoneModelId() % 10 != 1) {
            Manager.buffManager.deal().onAddBuff(player, player, Global.Boss_Unlimit_Buff);
        }
    }

    private void addBossDieInfo(BossMessage.ResBossRefreshInfo.Builder resMsg, int monsterId, boolean isAlive) {
        BossMessage.BossInfo.Builder bInfo = BossMessage.BossInfo.newBuilder();
        bInfo.setBossId(monsterId);
        bInfo.setRefreshTime(isAlive ? 0 : -1);
        resMsg.addBossRefreshList(bInfo);
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        if (map.getZoneModelId() % 10 != 1) {
            Manager.buffManager.deal().onRemoveBuff(player, Global.Boss_Unlimit_Buff);
        }
    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {
        if (!(attacker instanceof Player)) {
            logger.error("假无限层击杀者不是玩家！！");
            return;
        }
        Player player = (Player) attacker;
        Manager.dropManager.deal().specialDropReward(monster, player, SpecialDropDefine.Once_BOSS, false, -1);

        List<Integer> onceCopyData = map.getParam(getId());
        onceCopyData.add(monster.getModelId());

        BossMessage.ResBossRefreshInfo.Builder resMsg = BossMessage.ResBossRefreshInfo.newBuilder();
        addBossDieInfo(resMsg, monster.getModelId(), false);
        resMsg.setBossType(BossTypeConst.ONCE_BOSS);
        MessageUtils.send_to_player(player, BossMessage.ResBossRefreshInfo.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());

        boolean isOver = true;
        Cfg_Clone_monster_Bean bean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(map.getZoneModelId() * 1000 + 1);
        for (int i = 0; i < bean.getMonster_information().getValuees().length; i++) {
            if (!onceCopyData.contains(bean.getMonster_information().get(i).get(0))) {
                isOver = false;
            }
        }
        if (isOver) {
            if (map.getZoneModelId() % 10 == 1) {
                MapParam.setOnceCopyEnd(map);
            }
            map.addMapOnceScriptEventTimer(getId(), "kickOut", Global.Special_fb_time * 1000, player);
        }
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
                refreshBoss(map);
                break;
            case "kickOut":
                for (Player player : map.getPlayers().values()) {
                    Manager.copyMapManager.outZone(player);
                }
                break;
        }
    }

    /**
     * 玩家到达指定地点才刷怪
     */
    private void refreshBoss(MapObject map){

        Cfg_Clone_monster_Bean bean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(map.getZoneModelId() * 1000 + 1);
        if (bean == null) {
            return;
        }

        ReadIntegerArrayEs bossFightPoint;
        if (map.getZoneModelId() % 10 == 1) {
            bossFightPoint = Global.BossFightPoint1;
        } else {
            bossFightPoint = Global.BossFightPoint2;
        }
        List<Integer> onceCopyData = map.getParam(getId());
        for (int i = 0; i < bossFightPoint.size(); i++) {
            if (bean.getMonster_information().size() <= i) {
                continue;
            }
            ReadArray<Integer> ll = bean.getMonster_information().getValuees()[i];
            if (onceCopyData.contains(ll.get(0))) {
                continue;
            }
            boolean bossAlive = false;
            for (Monster monster : map.getMonsters().values()) {
                if (monster.getModelId() == ll.get(0)) {
                    bossAlive = true;
                }
            }
            if (!bossAlive) {
                Manager.monsterManager.createMonster(map, new Position(ll.get(2), ll.get(3)), ll.get(0));
            }
        }
    }

    @Override
    public void removeMap(MapObject map) {

    }
}
