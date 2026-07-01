package com.game.map.structs;

import com.game.backpack.structs.Item;
import com.game.boss.struct.SuitGemBossInfo;
import com.game.copymap.structs.*;
import com.game.eightdiagrams.structs.EightDaramsMapData;
import com.game.guildactivity.struct.GuildActivitySnatchMapInfo;
import com.game.guildactivity.struct.GuildBaseMapData;
import com.game.guildbattle.structs.GuildBattleData;
import com.game.guildbattle.structs.GuildBattleMapData;
import com.game.marriage.struct.WeddingMapInfo;
import com.game.universe.struct.UniverseWarData;
import com.game.worldbonfire.struct.WorldBonfire;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 所有副本参数定义
 *
 * @author lw
 */
public class MapParam {

    /**
     * 公共活动参数1 - 100
     */

    /**
     * 位面数据
     */
    public static final int CopyMapCommon_Plane = 1;

    /**
     * 副本归属
     */
    public static final int CopyMapCommon_Affiliation = 2;

    /**
     * 活动参数从100 - N
     */

    /**
     * 爬塔副本（万妖塔）
     */
    public static final int CopyMapSpc_SingleTower = 100;

    /**
     * 星级副本
     */
    public static final int CopyMapSpc_StarCopy = 101;

    /**
     * 帮派活动--福地争夺
     */
    public static final int CopyMapSpc_GuildActivityBoss = 102;

    /**
     * 帮派活动--福地夺宝
     */
    public static final int CopyMapSpc_GuildActivitySnatch = 103;

    /**
     * 竞技场
     * */
    public static final int CopyMapSpc_JJC = 105;

    /**
     * 副本掉落情况
     */
    public static final int CopyMap_PlayerDrop = 109;

    /**
     * 九天争锋
     */
    public static final int NinthFoucused = 111;

    /**
     * 神魔战场（三界之门）
     */
    public static final int GodDevilWar = 112;

    /**
     * 魂兽岛
     */
    public static final int SoulAnimalIsland = 113;

    /**
     * 领地战
     */
    public static final int MapOrWar = 116;

    /**
     * 八星阵图
     */
    public static final int EightDiagramsWar = 117;

    /**
     * 套装宝石Boss
     */
    public static final int SuitGemBoss = 118;

    /**
     * 世界篝火
     */
    public static final int CopyMapSpc_WorldBonfire = 119;

    /**
     * 太虚战场
     */
    public static final int UniverseWar = 120;

    /**
     * 仙盟任务副本
     */
    public static final int CopyMap_GuildTask = 123;

    /**
     * 仙盟驻地
     */
    public static final int CopyMap_GuildBase = 124;

    /**
     * 假无限层
     */
    public static final int CopyMap_Once = 125;

    /**
     * 剑灵阁
     */
    public static final int CopyMap_SwordSoulTower = 126;

    /**
     * 剑冢副本
     */
    public static final int CopyMap_SwordSoulCopy = 127;

    /**
     * NPC定时刷新
     */
    public static final int CopyMap_NpcTimerMap = 128;

    //////////////////////////////////////getter和setter/////////////////////////////////////////////////////////
    /**
     * 获取副本归属
     *
     * @param mapObject
     * @return
     */
    public static long getMapAffiliation(MapObject mapObject) {
        Long values = (Long) mapObject.getParam(CopyMapCommon_Affiliation);
        if (values == null) {
            return 0;
        }
        return values;
    }

    /**
     * 设置副本归属
     *
     * @param mapObject
     * @return
     */
    public static void setMapAffiliation(MapObject mapObject, long affiliation) {
        mapObject.getParams().put(CopyMapCommon_Affiliation, affiliation);
    }

    /**
     * 获取副本掉落情况
     *
     * @param copyMapContext
     * @return
     */
    public static ConcurrentHashMap<Long, List<Item>> getMapPlayerDrop(MapObject copyMapContext) {
        ConcurrentHashMap<Long, List<Item>> values = (ConcurrentHashMap<Long, List<Item>>) copyMapContext.getParam(CopyMap_PlayerDrop);
        if (values == null) {
            values = new ConcurrentHashMap<>();
            copyMapContext.getParams().put(CopyMap_PlayerDrop, values);
        }
        return values;
    }

    /**
     * 获取帮派活动--福地夺宝地图数据
     *
     * @param mapObject
     * @return
     */
    public static GuildActivitySnatchMapInfo getGuildActivitySnatchMapData(MapObject mapObject) {
        GuildActivitySnatchMapInfo values = (GuildActivitySnatchMapInfo) mapObject.getParam(CopyMapSpc_GuildActivitySnatch);
        if (values == null) {
            values = new GuildActivitySnatchMapInfo();
            mapObject.getParams().put(CopyMapSpc_GuildActivitySnatch, values);
        }
        return values;
    }

    /**
     * 获取位面地图内数据
     *
     * @param mapObject
     * @return
     */
    public static PlaneMapData getPlaneMapData(MapObject mapObject) {
        PlaneMapData values = (PlaneMapData) mapObject.getParam(CopyMapCommon_Plane);
        if (values == null) {
            values = new PlaneMapData();
            mapObject.getParams().put(CopyMapCommon_Plane, values);
        }
        return values;
    }

    public static StarCopyMapData getStarCopyMapData(MapObject mapObject) {
        StarCopyMapData values = (StarCopyMapData) mapObject.getParam(CopyMapSpc_StarCopy);
        if (values == null) {
            values = new StarCopyMapData();
            mapObject.getParams().put(CopyMapSpc_StarCopy, values);
        }
        return values;
    }

    public static SingleTowerMapData getSingleTowerMapData(MapObject mapObject) {
        SingleTowerMapData values = (SingleTowerMapData) mapObject.getParam(CopyMapSpc_SingleTower);
        if (values == null) {
            values = new SingleTowerMapData();
            mapObject.getParams().put(CopyMapSpc_SingleTower, values);
        }
        return values;
    }

    public static HashMap<Integer, Object> getJJCParam(MapObject mapObject) {
        HashMap<Integer, Object> values = (HashMap<Integer, Object>) mapObject.getParam(CopyMapSpc_JJC);
        if(null == values) {
            values = new HashMap<>();
            mapObject.getParams().put(CopyMapSpc_JJC, values);
        }
        return values;
    }

    public static HashMap<Integer, Object> getNinthFoucusedParam(MapObject mapObject) {
        HashMap<Integer, Object> values = (HashMap<Integer, Object>) mapObject.getParam(NinthFoucused);
        if(null == values) {
            values = new HashMap<>();
            mapObject.getParams().put(NinthFoucused, values);
        }
        return values;
    }

    public static SoulAnimallslandData getSoulAnimalIsland(MapObject mapObject) {
        SoulAnimallslandData values = (SoulAnimallslandData) mapObject.getParam(SoulAnimalIsland);
        if (values == null) {
            values = new SoulAnimallslandData();
            mapObject.getParams().put(SoulAnimalIsland, values);
        }
        return values;
    }

    public static GodDevilWarCopyData getGodDevilWarCopyData(MapObject mapObject) {
        GodDevilWarCopyData values = (GodDevilWarCopyData) mapObject.getParam(GodDevilWar);
        if(null == values) {
            values = new GodDevilWarCopyData();
            mapObject.getParams().put(GodDevilWar, values);
        }
        return values;
    }

    public static ManorWarMapData getManOrWarData(MapObject mapObject) {
        ManorWarMapData warData = (ManorWarMapData) mapObject.getParam(MapOrWar);
        if(null == warData) {
            warData = new ManorWarMapData();
            mapObject.getParams().put(MapOrWar, warData);
        }
        return warData;
    }

    public static EightDaramsMapData getEightDaramsMapData(MapObject mapObject) {
        EightDaramsMapData values = (EightDaramsMapData) mapObject.getParam(EightDiagramsWar);
        if (values == null) {
            values = new EightDaramsMapData();
            mapObject.getParams().put(EightDiagramsWar, values);
        }
        return values;
    }

    public static ConcurrentHashMap<Long, SuitGemBossInfo> getSuitGemBossMapData(MapObject mapObject) {
        ConcurrentHashMap<Long, SuitGemBossInfo> values = (ConcurrentHashMap<Long, SuitGemBossInfo>) mapObject.getParam(SuitGemBoss);
        if (values == null) {
            values = new ConcurrentHashMap<>();
            mapObject.getParams().put(SuitGemBoss, values);
        }
        return values;
    }

    public static WorldBonfire getMapWorldBonfire(MapObject mapObject) {
        WorldBonfire values = (WorldBonfire) mapObject.getParam(CopyMapSpc_WorldBonfire);
        if (values == null) {
            values = new WorldBonfire();
            mapObject.getParams().put(CopyMapSpc_WorldBonfire, values);
        }
        return values;
    }

    public static UniverseWarData getUniverseWarData(MapObject mapObject) {
        UniverseWarData values = (UniverseWarData) mapObject.getParam(UniverseWar);
        if (values == null) {
            values = new UniverseWarData();
            mapObject.getParams().put(UniverseWar, values);
        }
        return values;
    }

    public static GuildTaskData getGuildTaskData(MapObject mapObject) {
        GuildTaskData values = (GuildTaskData) mapObject.getParam(CopyMap_GuildTask);
        if (values == null) {
            values = new GuildTaskData();
            mapObject.getParams().put(CopyMap_GuildTask, values);
        }
        return values;
    }

    public static GuildBaseMapData getGuildBaseMapData(MapObject mapObject) {
        GuildBaseMapData values = (GuildBaseMapData) mapObject.getParam(CopyMap_GuildBase);
        if (values == null) {
            values = new GuildBaseMapData();
            mapObject.getParams().put(CopyMap_GuildBase, values);
        }
        return values;
    }

    public static boolean isOnceCopyEnd(MapObject mapObject) {
        if (mapObject.getParam(CopyMap_Once) == null) {
            mapObject.getParams().put(CopyMap_Once, false);
        }
        return (boolean) mapObject.getParam(CopyMap_Once);
}

    public static void setOnceCopyEnd(MapObject mapObject) {
        mapObject.getParams().put(CopyMap_Once, true);
    }


    public static SwordSoulTowerData getSwordSoulTowerData(MapObject mapObject) {
        SwordSoulTowerData values = (SwordSoulTowerData) mapObject.getParam(CopyMap_SwordSoulTower);
        if (values == null) {
            values = new SwordSoulTowerData();
            mapObject.getParams().put(CopyMap_SwordSoulTower, values);
        }
        return values;
    }

    public static CopyBaseData getSwordSoulCopyData(MapObject mapObject) {
        CopyBaseData values = (CopyBaseData) mapObject.getParam(CopyMap_SwordSoulCopy);
        if (values == null) {
            values = new CopyBaseData();
            mapObject.getParams().put(CopyMap_SwordSoulCopy, values);
        }
        return values;
    }

    public static HashMap<Integer, Boolean> getNpcTimerState(MapObject mapObject){
        HashMap<Integer, Boolean> values = (HashMap<Integer, Boolean>) mapObject.getParam(CopyMap_NpcTimerMap);
        if(null == values) {
            values = new HashMap<>();
            mapObject.getParams().put(CopyMap_NpcTimerMap, values);
        }
        return values;
    }
}
