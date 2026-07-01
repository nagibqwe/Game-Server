package common.home;

import com.game.copymap.scripts.ICopyReliveScript;
import com.game.copymap.structs.FightRoomState;
import com.game.copymap.structs.HouseCopyData;
import com.game.copymap.structs.ZoneCache;
import com.game.home.script.IHomeSceneScript;
import com.game.manager.Manager;
import com.game.map.script.ICrossCloneScript;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import game.core.map.Position;
import game.message.CommonMessage;
import game.message.HomeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/7/29 18:27
 * @Auth ZUncle
 */
public class HouseSceneScript implements ICopyReliveScript, IHomeSceneScript, ICrossCloneScript {

    static final Logger logger = LogManager.getLogger(HouseSceneScript.class);

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.HouseSceneScript;
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
     * 玩家进入跨服副本参数信息
     *
     * @param player
     * @param mapObject
     * @param cross
     */
    @Override
    public void enterCross(Player player, MapObject mapObject, List<CommonMessage.CrossAttribute> cross) {

    }

    /**
     * 地图创建初始化
     * 九 零一起玩 www.90 175.com
     * @param map
     * @param args
     */
    @Override
    public void onCreate(MapObject map, Object... args) {

        map.setAutoRemove(false);

        HouseCopyData zone = new HouseCopyData();
        zone.setZoneId(map.zone.getZoneId());
        zone.setLevel(map.zone.getLevel());
        map.setZone(zone);

        List<CommonMessage.CrossAttribute> crossList = (List<CommonMessage.CrossAttribute>) args[1];
        CommonMessage.CrossAttribute houseInfo = crossList.get(0);
        zone.setRoomId((long) args[0]);
        zone.setRoleId(houseInfo.getValue());

        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(map.getId(), FightRoomState.FIGHTING);

        logger.info("创建家园场景 houseId={}", zone.getRoleId());
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

        map.setAutoRemove(false);

        sendHouseMember(map, player, false);
    }

    /**
     * 同步房屋玩家信息
     *
     * @param map
     * @param enter
     * @param close
     */
    void sendHouseMember(MapObject map, Player enter, boolean close) {
        HouseCopyData zone = map.getZone();

        HomeMessage.F2SHomePlayerInfo.Builder message = HomeMessage.F2SHomePlayerInfo.newBuilder();
        message.setHouseId(zone.getRoleId());
        if (!close) {
            map.getPlayers().forEach((id, role) -> message.addRoleId(id));
        }
        if (enter != null) {
            message.setEnterRole(enter.getId());
        }

        MessageUtils.send_to_social(HomeMessage.F2SHomePlayerInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());
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

        sendHouseMember(map, null, false);

        if (map.getPlayers().isEmpty()) {
            map.setAutoRemove(true);
        }
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

    }

    /**
     * 删除地图调用接口
     *
     * @param map
     */
    @Override
    public void removeMap(MapObject map) {

        sendHouseMember(map, null, true);

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
        ZoneCache zone = map.getZone();
        logger.info("获取房屋等级 level={}", zone.getLevel());
        return map.getRelives().get(zone.getLevel() - 1);
    }

    /**
     * 处理场景刷新
     *
     * @param map
     * @param level
     */
    @Override
    public void doSceneChange(MapObject map, int level) {

        ZoneCache zone = map.getZone();
        zone.setLevel(level);
        logger.info("更新房屋等级 level={}", zone.getLevel());

        Position brith = doCreateRelivePosition(map, null);

        for (Player player : map.getPlayers().values()) {
            Manager.mapManager.transport().ResCurMapTransport(player, map, brith, 0, 0);
        }

    }
}
