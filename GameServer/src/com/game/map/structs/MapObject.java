/*
 * 地图类
 */
package com.game.map.structs;

import com.data.bean.Cfg_Mapsetting_Bean;
import com.game.copymap.structs.ZoneCache;
import com.game.map.timer.MapLoopScriptEventTimer;
import com.game.manager.Manager;
import com.game.monster.structs.Monster;
import com.game.npc.structs.Npc;
import com.game.npc.structs.Tombstone;
import com.game.player.structs.Player;
import com.game.robot.struct.Robot;
import com.game.server.GameServer;
import com.game.skill.structs.SkillMagic;
import com.game.structs.GameObject;
import com.game.structs.Gather;
import com.game.utils.Utils;
import game.core.map.Position;
import game.core.timer.TimerEvent;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapObject extends GameObject {

    protected static Logger log = LogManager.getLogger(MapObject.class);

    //区域列表
    private ConcurrentHashMap<Integer, Area> areas = new ConcurrentHashMap<>();
    //npc列表
    private ConcurrentHashMap<Long, Npc> npcs = new ConcurrentHashMap<>();
    //玩家列表
    private ConcurrentHashMap<Long, Player> players = new ConcurrentHashMap<>();
    //机器人列表
    private ConcurrentHashMap<Long, Robot> robots = new ConcurrentHashMap<>();
    //怪物列表
    private ConcurrentHashMap<Long, Monster> monsters = new ConcurrentHashMap<>();
    //采集物表
    private ConcurrentHashMap<Long, Gather> collects = new ConcurrentHashMap<>();
    //采集物表
    private final ConcurrentHashMap<Long, GroundBuff> groundBuffs = new ConcurrentHashMap<>();
    //传送点
    private ConcurrentHashMap<Integer, Transport> transports = new ConcurrentHashMap<>();
    //复活点
    private List<Position> relives = new ArrayList<>();
    //出生点
    private List<Position> briths = new ArrayList<>();
    //阻挡门
    private ConcurrentHashMap<String, DynamicBlock> doors = new ConcurrentHashMap<>();
    //技能魔法
    private ConcurrentHashMap<Long, SkillMagic> magics = new ConcurrentHashMap<>();
    //墓碑
    private ConcurrentHashMap<Long, Tombstone> tombstone = new ConcurrentHashMap<>();
    //参数信息
    private ConcurrentHashMap<Integer, Object> params = new ConcurrentHashMap<>();
    //计时器、心跳
    private final List<TimerEvent> timerEvents = new ArrayList<>();

    //地图设置配置
    volatile Cfg_Mapsetting_Bean setting;
    //阻挡数据
    private byte[][] blocks = null;
    //地图模块Id
    private int mapModelId;
    //地图名字
    private String name;
    //所在线
    private int lineId;
    //地图是否为副本
    private int type;
    //地图创建时间
    private long create;
    //地图中的Cell列数量    一个cell在client 的大小 1.0
    private int colCellCount;
    //地图中的Cell行数量
    private int rowCellCount;
    //地图中的area列数量    
    private int areaCol;
    //地图中的area行数量
    private int areaRow;
    //地图视野范围
    private int round = 5;

    private int area_high = 5; //区域高（格子数）默认5

    private int area_width = 5; //区域宽（格子数）默认5
    //pk开关
    private int pkState = 0;

    private int filterNum = 0;
    //宽
    private int width = 1000;
    //高
    private int height = 1000;
    //上次地图有人的时间
    private long lastHasPlayerTime;
    //地图绑定副本模板
    private int zoneModelId;
    public ZoneCache zone = new ZoneCache();
    //地图拥有着
    private long ownId;
    //地图副本是否停止状态
    private volatile boolean stop = false;
    //地图副本删除时间
    private long delTime;
    //是否受自动删除时间影响,默认true（如果是系统创建的，没人也需要一直保留的副本,设置为false）
    private boolean autoRemove = true;

    public Cfg_Mapsetting_Bean getSetting() {
        return setting;
    }

    public void setSetting(Cfg_Mapsetting_Bean setting) {
        this.setting = setting;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFilterNum() {
        return filterNum;
    }

    public void setFilterNum(int filterNum) {
        this.filterNum = filterNum;
    }

    public int getArea_high() {
        return area_high;
    }

    public ConcurrentHashMap<Integer, Object> getParams() {
        return params;
    }

    public void setParams(ConcurrentHashMap<Integer, Object> params) {
        this.params = params;
    }

    public <T> T getParam(int key) {
        return (T)params.get(key);
    }

    public void setArea_high(int area_high) {
        //不能低于基础值
        if (area_high < 5) {
            return;
        }
        this.area_high = area_high;
    }

    public int getArea_width() {
        return area_width;
    }

    public void setArea_width(int area_width) {
        //不能低于基础值
        if (area_width < 5) {
            return;
        }
        this.area_width = area_width;
    }

    /**
     * 用来发送到聊天框或者系统提示参数列表中的接口，请不要乱用
     *
     * @return 返回组装字符
     */
    public String getNoticeName() {
        return "2&_" + name;
    }

    /**
     * 配置表的地图名字
     *
     * @return 返回配置表的地图名字
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<SkillMagic> getMagics() {
        return magics.values();
    }

    public SkillMagic getMagic(long id) {
        return magics.get(id);
    }

    public void addMagic(SkillMagic m) {
        magics.put(m.getId(), m);
    }

    public void removeMagic(long id) {
        magics.remove(id);
    }

    public void setMagics(ConcurrentHashMap<Long, SkillMagic> magics) {
        this.magics = magics;
    }

    public int getPkState() {
        return pkState;
    }

    public void setPkState(int pkState) {
        this.pkState = pkState;
    }

    public ConcurrentHashMap<String, DynamicBlock> getDoors() {
        return doors;
    }

    public void setDoors(ConcurrentHashMap<String, DynamicBlock> doors) {
        this.doors = doors;
    }

    public ConcurrentHashMap<Long, Tombstone> getTombstone() {
        return tombstone;
    }

    public void setTombstone(ConcurrentHashMap<Long, Tombstone> tombstone) {
        this.tombstone = tombstone;
    }

    //获取地图属性
    public MapGps getGps(Position pos) {
        MapGps gps = new MapGps();
        gps.setMapId(id);
        gps.setModelId(mapModelId);
        gps.setLine(lineId);
        gps.setPos(pos);
        gps.setType(type);
        return gps;
    }

    /**
     * 获取第一个出生点
     *
     * @return
     */
    public Position getBrithPos() {
        return briths.get(0);
    }

    /**
     * 获取最近的复活点
     *
     * @param pos
     * @return
     */
    public Position getRelivePos(Position pos) {
        Position res = null;
        Iterator<Position> iter = relives.iterator();
        while (iter.hasNext()) {
            Position next = iter.next();
            if (next == null) {
                iter.remove();
                continue;
            }
            if (res == null) {
                res = next;
                continue;
            }

            if (Utils.getDistance(pos, next) < Utils.getDistance(pos, res)) {
                res = next;
            }
        }
        return res;
    }

    /**
     * 获取随机复活点
     *
     * @return
     */
    public Position getRandomRelivePos() {
        List<Position> list = new ArrayList<>();
        for (Position bp : relives) {
            list.add(bp);
        }
        return list.get(RandomUtils.nextInt(list.size()));
    }

    public List<Position> getRelives() {
        return relives;
    }

    public void setRelives(List<Position> relives) {
        this.relives = relives;
    }

    public List<Position> getBriths() {
        return briths;
    }

    public void setBriths(List<Position> briths) {
        this.briths = briths;
    }

    public ConcurrentHashMap<Integer, Transport> getTransports() {
        return transports;
    }

    public void setTransports(ConcurrentHashMap<Integer, Transport> transports) {
        this.transports = transports;
    }

    public ConcurrentHashMap<Long, Npc> getNpcs() {
        return npcs;
    }

    public void setNpcs(ConcurrentHashMap<Long, Npc> npcs) {
        this.npcs = npcs;
    }

    public int getAreaCol() {
        return areaCol;
    }

    public void setAreaCol(int areaCol) {
        this.areaCol = areaCol;
    }

    public int getAreaRow() {
        return areaRow;
    }

    public void setAreaRow(int areaRow) {
        this.areaRow = areaRow;
    }

    public int getZoneModelId() {
        return zoneModelId;
    }

    public void setZoneModelId(int zoneModelId) {
        this.zoneModelId = zoneModelId;
    }

    public <T> T getZone() {
        return (T)zone;
    }

    public void setZone(ZoneCache zone) {
        this.zone = zone;
    }

    public long getOwnId() {
        return ownId;
    }

    public void setOwnId(long ownId) {
        this.ownId = ownId;
    }

    public long getDelTime() {
        return delTime;
    }

    public void setDelTime(long delTime) {
        this.delTime = delTime;
    }

    public boolean isAutoRemove() {
        return autoRemove;
    }

    public void setAutoRemove(boolean autoRemove) {
        this.autoRemove = autoRemove;
    }

    public boolean isStop() {
        if (isWorldMap()) {
            return false;
        }
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public ConcurrentHashMap<Long, Player> getPlayers() {
        return players;
    }

    public void removePlayer(long id) {
        players.remove(id);
    }

    public void addPlayer(long id, Player val) {
//        if (players.containsKey(id)) {
//            LOGGER.info("地图里面添加角色的时候已经有这个key了 " + val.getName() + " 原来的名字:" + players.get(id) == null ? "null" : players.get(id).getName());
//        }
        players.put(id, val);
    }

    public boolean containPlayer(long id) {
        return players.containsKey(id);
    }

    public void setPlayers(ConcurrentHashMap<Long, Player> players) {
        this.players = players;
    }

    public ConcurrentHashMap<Long, Robot> getRobots() {
        return robots;
    }

    public void setRobots(ConcurrentHashMap<Long, Robot> robots) {
        this.robots = robots;
    }

    public ConcurrentHashMap<Long, Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(ConcurrentHashMap<Long, Monster> monsters) {
        this.monsters = monsters;
    }

    public ConcurrentHashMap<Integer, Area> getAreas() {
        return areas;
    }

    public void setAreas(ConcurrentHashMap<Integer, Area> areas) {
        this.areas = areas;
    }

    public ConcurrentHashMap<Long, Gather> getCollects() {
        return collects;
    }

    public void setCollects(ConcurrentHashMap<Long, Gather> collects) {
        this.collects = collects;
    }

    public ConcurrentHashMap<Long, GroundBuff> getGroundBuffs() {
        return groundBuffs;
    }

    public int getMapModelId() {
        return mapModelId;
    }

    public void setMapModelId(int mapModelId) {
        this.mapModelId = mapModelId;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreate() {
        return create;
    }

    public void setCreate(long create) {
        this.create = create;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getColCellCount() {
        return colCellCount;
    }

    public void setColCellCount(int colCellCount) {
        this.colCellCount = colCellCount;
    }

    public int getRowCellCount() {
        return rowCellCount;
    }

    public void setRowCellCount(int rowCellCount) {
        this.rowCellCount = rowCellCount;
    }

    public long getLastHasPlayerTime() {
        return lastHasPlayerTime;
    }

    public void setLastHasPlayerTime(long lastHasPlayerTime) {
        this.lastHasPlayerTime = lastHasPlayerTime;
    }

    public boolean isSafe() {
        return pkState == MapDefine.PK_STATE0;
    }

    public boolean isWorldMap() {
        return type == MapDefine.WORLD_MAP;
    }

    @Override
    public String toString() {
        return this.name + " ModelID:" + this.mapModelId + " line:" + this.lineId + " id:" + this.id;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// 通用接口
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //获取阻挡数据
    public byte[][] getBlocks() {
        if (null == blocks) {
            blocks = Manager.mapCfgManager.getMapCfg(this.getMapModelId()).getBlocks();
        }
        return blocks;
    }

    //获取monster
    public Monster getMonster(long id) {
        return this.monsters.get(id);
    }

    //获取player
    public Player getPlayer(long id) {
        return this.players.get(id);
    }

    //获取采集物
    public Gather getGather(long id) {
        return this.collects.get(id);
    }

    public void addTimerEvent(TimerEvent timerEvent) {
        if (isWorldMap()) {
            GameServer.getInstance().getWorldMapServerGroup().getMapServer(id).addTimerEvent(timerEvent);
        } else {
            GameServer.getInstance().getCopyMapServerGroup().getMapServer(id).addTimerEvent(timerEvent);
        }
        timerEvents.add(timerEvent);
    }

    public void removeTimer(TimerEvent timerEvent) {
        timerEvent.delete();
        if (timerEvents.contains(timerEvent)) {
            timerEvents.remove(timerEvent);
        }
    }

    /**
     * 添加定时器过期移除
     */
    public void clearTriggerOverEvent() {
        Iterator<TimerEvent> iter = timerEvents.iterator();
        while (iter.hasNext()) {
            TimerEvent next = iter.next();
            if (next.getLoop() == 0) {
                iter.remove();
                log.info("地图对象 定时器event执行完成清理 map={}, event={}", this, next);
            }
        }
    }

    public void addMapOnceScriptEventTimer(int scriptId, String method, long delay, Object... parms) {
        addTimerEvent(new MapLoopScriptEventTimer(scriptId, method, delay, this, parms));
    }

    public void addMapLoopScriptEventTimer(int scriptId, String method, int loop, long delay, long period, Object... parms) {
        addTimerEvent(new MapLoopScriptEventTimer(scriptId, method, loop, delay, period, this, parms));
    }

    @Override
    public void release() {
        for (TimerEvent timerEvent : timerEvents) {
            timerEvent.delete();
        }
        timerEvents.clear();
    }
}
