package common.npc;

import com.data.CfgManager;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.data.bean.Cfg_Npc_Bean;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.monster.structs.Monster;
import com.game.npc.structs.Npc;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import game.core.map.Position;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by cxl on 2021/2/1.
 */
public class NpcTimerMap implements IMapBaseScript {
    private static final Logger log = LogManager.getLogger(NpcTimerMap.class);

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        mapObject.addMapLoopScriptEventTimer(getId(), "tick", -1, 0, 3600000);
    }
    private void tick(MapObject mapObject) {
        Cfg_Mapsetting_Bean mapSet = CfgManager.getCfg_Mapsetting_Container().getValueByKey(mapObject.getMapModelId());
        if (null == mapSet) {
            log.error("mapObject.getMapModelId == null, mapID" + mapObject.getMapModelId());
            return;
        }
        HashMap<Integer, Boolean> npcRefreshMap =  MapParam.getNpcTimerState(mapObject);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        long now = TimeUtils.Time();
        for (Integer npcId  :  mapSet.getLimit_Npc().getValue()) {
            Cfg_Npc_Bean bean = CfgManager.getCfg_Npc_Container().getValueByKey(npcId);
            if (bean == null) {
                log.error("Cfg_Npc_Bean  == null " + npcId);
                continue;
            }
            try {
                long starTime = sdf.parse(bean.getLimit_time().get(0)).getTime();
                long endTime = sdf.parse(bean.getLimit_time().get(1)).getTime();
                if (starTime > now) {
                    continue;
                }
                if (now > endTime) {
                    if (npcRefreshMap.containsKey(npcId)){
                        npcRefreshMap.remove(npcId);
                    }
                    if (!mapObject.getNpcs().containsKey((long)npcId)) {
                        continue;
                    }
                    Manager.mapManager.manager().onQuitMap(mapObject, mapObject.getNpcs().get((long)npcId), true);
                    continue;
                }
                if (now >= starTime && now <= endTime) {
                    if (npcRefreshMap.containsKey(npcId)) {
                        continue;
                    }
                    initNpc(mapObject, bean);
                    npcRefreshMap.put(npcId, true);
                }
            } catch (Exception e) {
                log.error("NPC刷新时间 格式有误  :{}", e);
                return;
            }
        }
    }

    private void initNpc(MapObject mapObject, Cfg_Npc_Bean bean){
        Npc npc = new Npc();
        npc.setId(bean.getId());
        npc.changeMapModelId(mapObject.getMapModelId());
        npc.setModelId(bean.getId());
        npc.changeMapId(mapObject.getId());
        npc.changeLine(mapObject.getLineId());
        Position position = new Position(bean.getLoaction().get(0),bean.getLoaction().get(1));
        npc.changeCurPos(position);
        npc.setNpcDir( bean.getLoaction().get(2));
        Manager.mapManager.manager().onEnterMap(npc);
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return false;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {

    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {

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

    }

    @Override
    public void action(MapObject map, String method, Object[] params) {

        switch (method) {
            case "tick":
                tick(map);
                break;
        }
    }

    @Override
    public void removeMap(MapObject map) {

    }

    @Override
    public int getId() {
        return ScriptEnum.NpcTimerMapScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }



}
