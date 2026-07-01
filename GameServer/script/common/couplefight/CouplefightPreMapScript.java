package common.couplefight;

import com.game.copymap.structs.FightRoomState;
import com.game.manager.Manager;
import com.game.map.script.ICrossCloneScript;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import game.message.CommonMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * 仙侣对决 小组赛 地图 脚本
 * @Auther: gouzhongliang
 * @Date: 2021/7/7 15:40
 */
public class CouplefightPreMapScript implements ICrossCloneScript {

    final Logger log = LogManager.getLogger(CouplefightPreMapScript.class);

    /**房间id*/
    private final Integer key_roomId = 1;
    /**战斗状态*/
    private final Integer key_status = 2;
    private final int status_prepare = 0;//准备
    private final int status_ready = 1;//准备好
    private final int status_start = 2;//开始
    private final int status_over = 3;//结束
    /**玩家*/
    private final Integer key_players = 3;
    /**准备次数*/
    private final Integer key_ready_times = 4;

    @Override
    public int getId() {
        return ScriptEnum.CouplefightPreMapScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        mapObject.setAutoRemove(false);
        mapObject.getParams().put(key_roomId, objects[0]);

        mapObject.getParams().put(key_status, status_prepare);
        mapObject.getParams().put(key_ready_times, 0);
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return true;
    }

    @Override
    public void onEnterMap(Player player, MapObject mapObject, boolean login) {
        log.info("进入仙侣对决准备地图 map={} player={}", mapObject.getId(), player);
        Manager.playerManager.manager().onUpdatePkState(player, PlayerDefine.PkStatePeace, true);//和平模式
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        //发送消失告知公共服务器

    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

    }

    @Override
    public void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker) {

    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject map, Fighter attacker, Player player) {
        onDie(map, attacker, player.getId());
    }

    private void onDie(MapObject map, Fighter attacker, long pid) {

    }


    @Override
    public void action(MapObject map, String method, Object[] params) {
        switch (method) {
            default:
        }
    }

    private void timeOut(MapObject map) {
    }

    @Override
    public void removeMap(MapObject map) {
        for(Player player : map.getPlayers().values()){
            Manager.copyMapManager.manager().onReqCopyMapOut(player);
        }
        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(map.getId(), FightRoomState.FIGHTREWARDEND);
        map.setStop(true);
        map.setAutoRemove(true);
    }

    @Override
    public void enterCross(Player player, MapObject mapObject, List<CommonMessage.CrossAttribute> cross) {

    }

}
