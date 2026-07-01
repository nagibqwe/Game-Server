package com.game.fightroom.manager;

import com.data.CfgManager;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.game.fightroom.log.FightRoomCreateLog;
import com.game.fightroom.script.IFightManagerScript;
import com.game.fightroom.structs.FightRoom;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.zone.structs.ZoneTeam;
import game.core.dblog.LogService;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class FightManager {

    private final ConcurrentHashMap<Long, FightRoom> frcache = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, FightRoom> getFrcache() {
        return frcache;
    }
    private static final Object obj = new Object();

    private FightManager() {
    }

    private static FightManager manager;

    public static FightManager getInstance() {
        synchronized (obj) {
            if (manager == null) {
                manager = new FightManager();
            }
        }
        return manager;
    }


    public IFightManagerScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerScript);
        if (is instanceof IFightManagerScript) {
            return (IFightManagerScript) is;
        }
        return null;
    }

    private static final Logger LOG = LogManager.getLogger("FightManager");

    /**
     * 获得战斗房间
     *
     * @param fightid
     * @return
     */
    public FightRoom GetRoomByFightId(long fightid) {
        if (frcache.containsKey(fightid)) {
            return frcache.get(fightid);
        }
        return null;
    }


    public FightRoom GetRoomByRoleId(long roleId) {
        Iterator<Entry<Long, FightRoom>> iter = frcache.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Long, FightRoom> en = iter.next();
            FightRoom fr = en.getValue();
            for (ZoneTeam zt : fr.getTeam()) {
                if (zt.getPlist().containsKey(roleId)) {
                    return fr;
                }
            }
        }
        return null;
    }


    //获得副本列表
    public List<FightRoom> getModelFight(int modelId) {
        List<FightRoom> list = new ArrayList<>();
        for (FightRoom fr : frcache.values()) {
            if (fr.getModelId() == modelId) {
                list.add(fr);
            }
        }
        return list;
    }

    /**
     * 根据组id和副本id来获取房间
     *
     * @param modelId
     * @param groupId
     * @return
     */
    public List<FightRoom> getBravePeakRoom(int modelId, int groupId) {
        List<FightRoom> sameModelRoom = getModelFight(modelId);
        List<FightRoom> list = new ArrayList<>();
        for (FightRoom fightRoom : sameModelRoom) {
            if (fightRoom.getServerGroupId() == groupId) {
                list.add(fightRoom);
            }
        }
        return list;
    }

    public FightRoom getFightRoom(int groupId, int modelId) {
        for (FightRoom fr : frcache.values()) {
            if (fr.getServerGroupId() == groupId&&fr.getModelId() == modelId) {
                return fr;
            }
        }
        return null;
    }

    public void RemoveFight(FightRoom ri) {
        LOG.info(ri.getFid() + " 房间删除！");
        getFrcache().remove(ri.getFid());
        Manager.worldBonfireManager.getRooms().remove(ri);
    }
    public  int getLineMaxPeople(int mapId) {
        Cfg_Clone_map_Bean clone_map_bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapId);
        Cfg_Mapsetting_Bean mapsetting_bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(clone_map_bean.getMapid());
        return mapsetting_bean.getOnline();
    }

    public void SaveRoomInfo(FightRoom ri, String plat, int sid) {
        LOG.info(ri.getFid() + " 房间创建！");
        getFrcache().put(ri.getFid(), ri);
        FightRoomCreateLog blog = new FightRoomCreateLog();
        blog.setFid(ri.getFid());
        blog.setIsAuto(ri.isAllReadyStart() ? 1 : 0);
        blog.setModelId(ri.getModelId());
        blog.setPlat(plat);
        blog.setPower(ri.getAttackValue());
        blog.setRoleId(ri.getCrId());
        blog.setRoleName(ri.getCname());
        blog.setSid(sid);
        LogService.getInstance().execute(blog);
    }
}
