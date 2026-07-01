package common.map;

import com.data.CfgManager;
import com.data.Global;
import com.data.bean.Cfg_GroundBuff_Bean;
import com.game.backpack.structs.Equip;
import com.game.equip.struct.EquipPart;
import com.game.manager.Manager;
import com.game.map.structs.*;
import com.game.monster.structs.Monster;
import com.game.npc.structs.Tombstone;
import com.game.pet.structs.Pet;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.Player;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.SkillMagic;
import com.game.structs.Fighter;
import com.game.structs.IActionScript;
import com.game.utils.Utils;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * @author admin
 */
public class MapCleanerScript implements IScript, IActionScript {

    private static final Logger log = LogManager.getLogger("MapCleanerScript");

    @Override
    public int getId() {
        return ScriptEnum.MapCleanerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public boolean action(MapObject map, long heart) {
        long curTime = TimeUtils.Time();

        map.clearTriggerOverEvent();

        //检测是否有垃圾魔法
        OnCheckMagic(map, curTime, heart);

        //清理僵尸玩家，下线为什么不调退地图接口
        OnCheckPlayer(map, curTime, heart);

        //定时清理墓碑信息
        OnCheckTombstone(map);

        //定时查看地图BUFF处理
        onCheckGroundBuff(map, curTime, heart);

        //地图经验
        onCheckSpecialMapExp(map, curTime, heart);
        return true;
    }

    /**
     * @param curTime 当前时间
     * @param heart   心跳时间
     * @param delay   检测jiange
     * @return 返回是否到检测时间了
     */
    private boolean isCanCheck(long curTime, long heart, int delay) {
        if (delay <= heart) {
            return true;
        }
        long tail = curTime % delay;
        return heart + tail > delay;
    }

    //检测地图上的召唤物是否有垃圾
    private void OnCheckMagic(MapObject map, long curTime, long heart) {
        //每隔2分钟检测一次
        if (!isCanCheck(curTime, heart, 120000)) {
            return;
        }

        List<SkillMagic> magics = new ArrayList<>(map.getMagics());

        for (SkillMagic magic : magics) {
            Player player = map.getPlayer(magic.getOwnerId());
            if (player == null) {
                Manager.mapManager.manager().onQuitMap(map, magic, true);
            } else {
                if ((magic.getStart() + 120000) < TimeUtils.Time()) {
                    Manager.mapManager.manager().onQuitMap(map, magic, true);
                }
            }
        }
    }

    //清理僵尸玩家
    private void OnCheckPlayer(MapObject map, long curTime, long heart) {
        try {
            if (map.getId() > 0) {
                return;
            }

            //每隔5分钟检测一次
            if (!isCanCheck(curTime, heart, 300000)) {
                return;
            }

            //第一类
            List<Player> players = new ArrayList<>(map.getPlayers().values());
            players.forEach((Player player) -> {
                if (!player.isOnline()) {
                    Manager.mapManager.manager().onQuitMap(map, player, true);
                } else {
                    MapObject map1 = Manager.mapManager.getMap(player.gainMapId());
                    if (!map.equals(map1)) {
                        Manager.mapManager.manager().clearPlayer(map, player);
                    }
                }
            });

            //第二类
            for (Area area : map.getAreas().values()) {
//                List<Player> players = new ArrayList<>(area.getPlayers());
                for (Player player : area.getPlayers()) {
                    if (map.containPlayer(player.getId())) {
                        continue;
                    }
                    map.addPlayer(player.getId(), player);
                }
            }

            //检测地图玩家，
            //30秒检测,是否有过期装备，如果完全没有过期的装备,有过期的装备就从新计算一下就注释掉
            if (isCanCheck(curTime, heart, 30000)) {
                for (Player player : map.getPlayers().values()) {
                    for (EquipPart equipPart : player.getEquipParts()) {
                        if (equipPart == null || null == equipPart.getEquip()) {
                            continue;
                        }

                        Equip e = equipPart.getEquip();
                        if (e.haveLost()) {
                            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.EQUIP);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }



    private void OnCheckTombstone(MapObject map) {

        //地图上是否有墓碑
        if (map.getTombstone().size() <= 0) {
            return;
        }
        //需要删除的墓碑
        List<Tombstone> list = new ArrayList<>();
        //找出需要删除的墓碑
        for (Tombstone t : map.getTombstone().values()) {
            if (t.getDieTime() <= TimeUtils.Time()) {
                list.add(t);
            }
        }
        if (list.size() <= 0) {
            return;
        }
        for (Tombstone t : list) {
            Manager.mapManager.manager().onQuitMap(map, t, true);
        }

    }

    /**
     * 主动检查地图上的BUFF生效器的工作
     *
     * @param map
     * @param curTime
     * @param heart
     */
    private void onCheckGroundBuff(MapObject map, long curTime, long heart) {
        if (map.getGroundBuffs().isEmpty()) {
            return;
        }

        List<GroundBuff> temp = new ArrayList<>(map.getGroundBuffs().values());
        for (GroundBuff gb : temp) {
            if (gb.getType() == 1) {
                continue;
            }
            Cfg_GroundBuff_Bean bean = CfgManager.getCfg_GroundBuff_Container().getValueByKey(gb.getModelId());
            if (bean == null) {
                Manager.mapManager.manager().onQuitMap(map, gb, true);
                continue;
            }
            GroundBuffBirth(map, gb, curTime, bean);
            if (gb.getMaxNum() >= bean.getActiveTimes()) {
                Manager.mapManager.manager().onQuitMap(map, gb, true);
            }
        }

    }

    private void GroundBuffBirth(MapObject map, GroundBuff gb, long curTime, Cfg_GroundBuff_Bean bean) {
        long offset = curTime - gb.getLastUpdateTime();
        if (offset < bean.getActiveStep()) {
            return;
        }

        gb.setMaxNum(gb.getMaxNum() + 1);
        gb.setLastUpdateTime(curTime);

        List<Fighter> players = MapUtils.getFighter(map, gb.gainCurPos());
        if (players == null) {
            return;
        }

        if (players.isEmpty()) {
            return;
        }

        for (Fighter fighter : players) {
            if (gb.getGroupNo() == -1) {
                if (fighter instanceof Monster) {
                    float dis = Utils.getDistance(fighter.gainCurPos(), gb.gainCurPos());
                    if (dis > bean.getDisValue()) {
                        continue;
                    }

                    Manager.buffManager.deal().onAddBuff(fighter, fighter, bean.getBuff_id());
                } else {
                    continue;
                }
            }

            if (fighter instanceof Monster) {
                continue;
            }

            if (fighter instanceof Robot) {
                continue;
            }

            Player player = null;
            if (fighter instanceof Pet) {
                player = map.getPlayer(((Pet) fighter).getOwnerId());
            }

            if (fighter instanceof Player) {
                player = (Player) fighter;
            }

            if (player == null) {
                continue;
            }

            float dis = Utils.getDistance(fighter.gainCurPos(), gb.gainCurPos());
            if (dis > bean.getDisValue()) {
                continue;
            }

            Manager.buffManager.deal().onAddBuff(fighter, fighter, bean.getBuff_id());
        }
    }

    /**
     * 检查特殊地图经验
     */
    private void onCheckSpecialMapExp(MapObject map, long curTime, long heart) {
        if (!isCanCheck(curTime, heart, Global.OnHookClientMapExpTimes * 1000)) {
            return;
        }
        Manager.playerHookManager.deal().sendSpecialMapPlayerExpUp(map);
    }

}
