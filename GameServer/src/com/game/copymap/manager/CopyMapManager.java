package com.game.copymap.manager;

import com.data.CfgManager;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.game.copymap.scripts.ICopyManagerScript;
import com.game.copymap.scripts.ICopyLogicScript;
import com.game.copymap.scripts.ISingleTowerScript;
import com.game.copymap.structs.*;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapDefine;
import com.game.map.structs.MapParam;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.player.structs.ReliveType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.ServerStr;
import game.core.map.Position;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 副本管理类
 */
public class CopyMapManager {

    private static final Logger log = LogManager.getLogger(CopyMapManager.class);

    /**
     * 副本配置ID-> 日常配置ID
     */
    final ConcurrentHashMap<Integer, Integer> zoneDaily = new ConcurrentHashMap<>();

    final ConcurrentHashMap<Long, CopyMapTeam> copyMapTeams = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer, Integer> getZoneDaily() {
        return zoneDaily;
    }

    public ConcurrentHashMap<Long, CopyMapTeam> getCopyMapTeams() {
        return copyMapTeams;
    }

    public String getCopyMapName(int cpModelId) {
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(cpModelId);
        if (bean != null) {
            return ServerStr.getChatTableName(bean.getDuplicate_name());
        }
        return "clone" + cpModelId;
    }

    public void outZone(Player player) {
        if (player.isDie()) {
            Manager.playerManager.deal(ScriptEnum.PlayerReliveBaseScript).OnPlayerRelive(player, ReliveType.Gm, false, player.gainCurPos());
        }

        if (player.getCurHp() != player.getAttribute().MaxHP()) {
            player.setCurHp(player.getAttribute().MaxHP());
            player.onHpChange(null);
        }

        Cfg_Mapsetting_Bean mapCfg = CfgManager.getCfg_Mapsetting_Container().getValueByKey(player.gainMapModelId());
        MapObject oldMap = Manager.mapManager.getMap(player.getOld().getMapId());

        if (oldMap == null || oldMap.getMapModelId() == MapManager.CrossWaitMapId) {
            Manager.mapManager.changeMap(player, Manager.playerManager.getBornMapID(), null, -1, true);
            return;
        }

        int changeMapId = oldMap.getMapModelId();

        if (mapCfg.getLeave_mapid() != 0) {
            changeMapId = mapCfg.getLeave_mapid();
        }
        if (changeMapId == player.gainMapModelId()) {
            return;
        }

        MapObject curMap = Manager.mapManager.getMap(player.gainMapId());
        if (curMap.getZoneModelId() == Manager.peakManager.deal().getZoneModelId()) {
            MapObject waitMap = Manager.peakManager.deal().getWaitMap();
            if (waitMap != null) {
                Manager.mapManager.changeMap(player, waitMap.getId(), null, false);
                return;
            }
        }
        Cfg_Clone_map_Bean curCloneBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(curMap.getZoneModelId());

        if ((mapCfg.getType() == MapDefine.COPY_MAP || mapCfg.getType() == MapDefine.Copy_Plane)
                && curCloneBean != null
                && (curCloneBean.getType() == CopyMapType.Plane_CopyMap || (curCloneBean.getType() == CopyMapType.Once_CopyMap && MapParam.isOnceCopyEnd(curMap)))) {
            if (mapCfg.getLeavePosition().size() >= 2) {
                Position pos = new Position(mapCfg.getLeavePosition().get(0), mapCfg.getLeavePosition().get(1));
                Manager.mapManager.changeMap(player, changeMapId, pos, -1, false);
            } else {
                Manager.mapManager.changeMap(player, changeMapId, player.gainCurPos(), -1, false);
            }
        } else {
            if (mapCfg.getLeave_mapid() != 0) {
                Cfg_Mapsetting_Bean targetMap = CfgManager.getCfg_Mapsetting_Container().getValueByKey(mapCfg.getLeave_mapid());
                Position pos = new Position(targetMap.getBornPosition().get(0).get(0), targetMap.getBornPosition().get(0).get(1));
                Manager.mapManager.changeMap(player, changeMapId, pos, -1, false);
            } else {
                Manager.mapManager.changeMap(player, changeMapId, player.getOld().getPos(), -1, false);
            }
        }
    }

    public ICopyManagerScript manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CopyMapManagerBaseScript);
        if (is instanceof ICopyManagerScript) {
            return (ICopyManagerScript) is;
        }
        log.error("没有找到副本管理脚本!={}", ScriptEnum.CopyMapManagerBaseScript);
        return null;
    }

    public ISingleTowerScript singleTower() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.SingleTowerActivityScript);
        if (is instanceof ISingleTowerScript) {
            return (ISingleTowerScript) is;
        }
        log.error("没有找到副本脚本!={}", ScriptEnum.SingleTowerActivityScript);
        return null;
    }

    public ICopyLogicScript logic() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CopyLogicBaseScript);
        if (is instanceof ICopyLogicScript) {
            return (ICopyLogicScript) is;
        }
        log.error("没有找到副本逻辑脚本！={}", ScriptEnum.CopyLogicBaseScript);
        return null;
    }

    private enum Singleton {
        INSTANCE;
        CopyMapManager manager;

        Singleton() {
            this.manager = new CopyMapManager();
        }

        CopyMapManager getProcessor() {
            return manager;
        }
    }

    public static CopyMapManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

}
