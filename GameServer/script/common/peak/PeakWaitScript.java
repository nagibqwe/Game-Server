package common.peak;

import com.game.copymap.structs.ExpNoteData;
import com.game.copymap.structs.ZoneCache;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import game.message.PeakMessage;

/**
 * @Desc TODO
 * @Date 2021/8/27 17:35
 * @Auth ZUncle
 */
public class PeakWaitScript implements IMapBaseScript {
    /**
     * 地图创建初始化
     *
     * @param mapObject
     * @param objects
     */
    @Override
    public void onCreate(MapObject mapObject, Object... objects) {

        ExpNoteData peak = new ExpNoteData();

        peak.setZoneId(mapObject.zone.getZoneId());
        peak.setLevel(mapObject.zone.getLevel());
        mapObject.setZone(peak);

        mapObject.setDelTime(0);
        mapObject.setAutoRemove(false);

        mapObject.addMapLoopScriptEventTimer(getId(), "doTick", -1, 0, 2000);

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
        return false;
    }

    /**
     * 进入副本地图接口
     *
     * @param player
     * @param map
     * @param login
     */
    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {

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

        if (method.equals("doTick")) {

            ExpNoteData peak = map.getZone();

            for (Player player : map.getPlayers().values()) {

                long exp = peak.getExpNote().getOrDefault(player.getId(), 0L);

                PeakMessage.ResUpdatePeakExp.Builder message = PeakMessage.ResUpdatePeakExp.newBuilder();
                message.setExpReward(exp);
                MessageUtils.send_to_player(player, PeakMessage.ResUpdatePeakExp.MsgID.eMsgID_VALUE, message.build().toByteArray());
            }
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

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.PeakWaitScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        String method = (String) args[0];
        if ("activityEnd".equalsIgnoreCase(method)) {
            MapObject map = (MapObject) args[1];
            activityEnd(map);
        }
        return null;
    }

    /**
     * 活动结束结束踹出去
     *
     * @param mapObject
     */
    void activityEnd(MapObject mapObject) {
        for (Player player : mapObject.getPlayers().values()) {
            Manager.copyMapManager.manager().onReqCopyMapOut(player);
        }
        ZoneCache zone = mapObject.getZone();
        if (zone instanceof ExpNoteData) {
            ExpNoteData exp = (ExpNoteData)zone;
            exp.getExpNote().clear();
        }
        mapObject.setStop(true);
        mapObject.setAutoRemove(true);
        mapObject.setLastHasPlayerTime(1);
    }
}
