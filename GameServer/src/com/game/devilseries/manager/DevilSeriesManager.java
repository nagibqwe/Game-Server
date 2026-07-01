package com.game.devilseries.manager;

import com.game.boss.struct.Boss;
import com.game.cangbaoge.manager.CangbaogeManager;
import com.game.devilseries.scripts.IDevilSeriesManagerScript;
import com.game.devilseries.structs.DevilCopyData;
import com.game.devilseries.structs.Devilintegral;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public enum DevilSeriesManager {

    instance;

    private static final Logger log = LogManager.getLogger(DevilSeriesManager.class);

    //取前50名
    public static int MaxRank = 50;

    // Key 地图唯一ID  key2 BossID  除魔团BOSS管理
    private ConcurrentHashMap<Long,ConcurrentHashMap<Integer, Boss>> DevilSerierBoss = new ConcurrentHashMap<>();

    //所有玩家积分 key1 ->mapId key2->playerId
    private ConcurrentHashMap<Long,ConcurrentHashMap<Long,Devilintegral>> playerBossIntegrals = new ConcurrentHashMap<>();
    //前50名玩家排名 key 地图唯一ID
    private ConcurrentHashMap<Long,List<Devilintegral>> integralsRank = new ConcurrentHashMap<>();

    //副本关注列表
    private ConcurrentHashMap<Integer,List<Long>>  copyFollowList = new ConcurrentHashMap<>();

    //所有开启的副本
    private ConcurrentHashMap<Long,DevilCopyData> devilOpenCopyDataMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, ConcurrentHashMap<Long, Devilintegral>> getPlayerBossIntegrals() {
        return playerBossIntegrals;
    }

    public void setPlayerBossIntegrals(ConcurrentHashMap<Long, ConcurrentHashMap<Long, Devilintegral>> playerBossIntegrals) {
        this.playerBossIntegrals = playerBossIntegrals;
    }

    public ConcurrentHashMap<Integer, List<Long>> getCopyFollowList() {
        return copyFollowList;
    }

    public void setCopyFollowList(ConcurrentHashMap<Integer, List<Long>> copyFollowList) {
        this.copyFollowList = copyFollowList;
    }

    public ConcurrentHashMap<Long, ConcurrentHashMap<Integer, Boss>> getDevilSerierBoss() {
        return DevilSerierBoss;
    }

    public void setDevilSerierBoss(ConcurrentHashMap<Long, ConcurrentHashMap<Integer, Boss>> devilSerierBoss) {
        DevilSerierBoss = devilSerierBoss;
    }

    public ConcurrentHashMap<Long, DevilCopyData> getDevilOpenCopyDataMap() {
        return devilOpenCopyDataMap;
    }

    public void setDevilOpenCopyDataMap(ConcurrentHashMap<Long, DevilCopyData> devilOpenCopyDataMap) {
        this.devilOpenCopyDataMap = devilOpenCopyDataMap;
    }

    public ConcurrentHashMap<Long, List<Devilintegral>> getIntegralsRank() {
        return integralsRank;
    }

    public void setIntegralsRank(ConcurrentHashMap<Long, List<Devilintegral>> integralsRank) {
        this.integralsRank = integralsRank;
    }

    public IDevilSeriesManagerScript getScript(){
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.DevilSeriesScript);
        if (is instanceof IDevilSeriesManagerScript) {
            return (IDevilSeriesManagerScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

}
