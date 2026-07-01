package com.game.map.manager;

import com.data.CfgManager;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.map.command.ChangeMapCommand;
import com.game.map.command.ChangeSpaceMapCommand;
import com.game.map.command.DeleteMapCommand;
import com.game.map.script.IMapBaseScript;
import com.game.map.script.IMapHandler;
import com.game.map.script.IMapManagerScript;
import com.game.map.script.ITransportScript;
import com.game.map.structs.*;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.skill.structs.SkillMagic;
import com.game.structs.Entity;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import game.core.map.IMapObject;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.thread.ServerThread;
import game.message.MapMessage.ResMoveTo;
import game.message.MapMessage.ResStopMove;
import game.message.MapMessage.ResUpdateBlockDoor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapManager extends ServerThread {

    private static final Logger logger = LogManager.getLogger(MapManager.class);

    public static final int CrossWaitMapId = 500; //跨服休息室

    public static final int SpecBossMapId = 1509;

    public static final int AreaArg = 1000;

    public static final int CopyMapOwnerSystemId = 0;

    /**
     * 地图数据缓存
     */
    private final ConcurrentHashMap<Long, MapObject> maps = new ConcurrentHashMap<>();

    /**
     * 野图地图保存
     */
    private final ConcurrentHashMap<Integer, ArrayList<MapObject>> worldMaps = new ConcurrentHashMap<>();

    /**
     * 使用的环境是一个坐标常用位置缓存，避免过多的NEW对象出来
     */
    private static final ConcurrentHashMap<String, Position> posCache = new ConcurrentHashMap<>();

    public MapManager() {
        super(new ThreadGroup("mapGroup"), "mapThread", GameServer.getInstance().getServerTimer());
    }

    public void startMap() {
        for (Cfg_Mapsetting_Bean bean : CfgManager.getCfg_Mapsetting_Container().getValuees()) {
            if (bean.getType() == MapDefine.WORLD_MAP) {
                logger.info("开始启动野图地图： id =" + bean.getMap_id() + ", z =" + bean.getMap_info());
                manager().createWorldMap(bean, 1);
            }
        }
    }

    public MapObject createCopyMap(int cloneId, int level, long ownId, Object... objects) {
        return manager().createCopyMap(cloneId, level, ownId, objects);
    }

    public void deleteMap(MapObject map) {
        map.release();
        maps.remove(map.getId());

        if (map.getLineId() != 0) {
            List<MapObject> list = getWorldMaps().get(map.getMapModelId());
            list.remove(map);
        }

        FightClientManager.GetInstance().removeMapId(map.getId());

        if (map.getSetting().getIsscript()> 0){
            Manager.mapManager.base(map.getSetting().getIsscript()).removeMap(map);
        }

        this.addCommand(new DeleteMapCommand(map));
    }

    //进入野图
    public void changeMap(Player player, int modelId, Position position, int type, boolean isLogin) {
        addCommand(new ChangeMapCommand(player, 0, modelId, 0, position, type, isLogin));
    }

    //进入野图指定线路
    public void changeMap(Player player, int modelId, int line, Position position, int type) {
        addCommand(new ChangeMapCommand(player, 0, modelId, line, position, type, false));
    }

    //进入副本,或者活动地图
    public void changeMap(Player player, long mapId, Position position, boolean isLogin) {
        addCommand(new ChangeMapCommand(player, mapId, 0, 0, position, -1, isLogin));
    }

    //进入跨服休息室
    public void changeSpaceMap(Player player) {
        addCommand(new ChangeSpaceMapCommand(player));
    }

    public ConcurrentHashMap<Long, MapObject> getMaps() {
        return maps;
    }

    public MapObject getMap(long mapId) {
        return maps.get(mapId);
    }

    public MapObject getMap(long ownId, int cloneId) {
        Iterator<Map.Entry<Long, MapObject>> iterator = maps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, MapObject> entry = iterator.next();
            if (entry.getValue().getZoneModelId() == cloneId && entry.getValue().getOwnId() == ownId) {
                return entry.getValue();
            }
        }
        return null;
    }

    public ArrayList<MapObject> getMaps(int modelId) {
        return worldMaps.get(modelId);
    }

    public ConcurrentHashMap<Integer, ArrayList<MapObject>> getWorldMaps() {
        return worldMaps;
    }

    public static Position getPos(int x, int y) {
        String key = x + "_" + y;
        if (posCache.containsKey(key)) {
            return posCache.get(key);
        }
        Position pos = new Position(x, y);
        posCache.put(key, pos);
        return pos;
    }

    public static Position getPos(float x, float y) {
        String key = x + "_" + y;
        if (posCache.containsKey(key)) {
            return posCache.get(key);
        }
        Position pos = new Position(x, y);
        posCache.put(key, pos);
        return pos;
    }

    //获取周围玩家
    public List<Player> getRoundPlayer(Player player, int rad) {
        List<Player> players = new ArrayList<>();
        MapObject map = getMap(player.gainMapId());
        if (map == null) {
            return players;
        }
        List<Area> areas = getRoundAreas(map, player.gainCurPos(), rad);
        for (Area area : areas) {
            players.addAll(area.getPlayers());
        }
        return players;
    }

    private enum Singleton {
        INSTANCE;
        MapManager manager;

        Singleton() {
            this.manager = new MapManager();
        }

        MapManager getProcessor() {
            return manager;
        }
    }

    public static MapManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IMapHandler deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MapDealBaseScript);
        if (is instanceof IMapHandler) {
            IMapHandler imh = (IMapHandler) is;
            return imh;
        } else {
            return null;
        }
    }

    public ITransportScript transport() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.TransportBaseScript);
            if (is instanceof ITransportScript) {
            return (ITransportScript) is;
        } else {
            return null;
        }
    }

    public IMapManagerScript manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MapManagerBaseScript);
        if (is instanceof IMapManagerScript) {
            return (IMapManagerScript) is;
        } else {
            return null;
        }
    }

    public IMapBaseScript base(int scriptId) {
        IScript is = Manager.scriptManager.GetScriptClass(scriptId);
        if (is instanceof IMapBaseScript) {
            return (IMapBaseScript) is;
        }
        return null;
    }

    public void createTombstone(MapObject map, int monsterId) {
       // Cfg_Tombstone_Bean bean = CfgManager.getCfg_Tombstone_Container().getValueByKey(monsterId);
       // if (bean == null) {
       //     return;
       // }
       // Tombstone tombstone = new Tombstone();
       // tombstone.setMonsterId(monsterId);
       // MapGps gps = new MapGps();
       // gps.setLine(map.getLineId());
       // gps.setMapId(map.getId());
       // gps.setModelId(map.getMapModelId());
       // Position pos = new Position();
       // pos.setX(bean.getPos().get(0));
       // pos.setY(bean.getPos().get(1));
       // gps.setPos(pos);
       // tombstone.setCurGps(gps);
       // tombstone.setBornTime(TimeUtils.Time());
       // //进入地图
       // manager().onEnterMap(tombstone);
    }

    //获取区域编号
    public int getAreaId(MapObject map, Position pos) {
        return getAreaId(map, pos.getX(), pos.getY());
    }

    public int getAreaId(IMapObject obj) {
        return getAreaId(getMap(obj.gainMapId()), obj.gainCurPos());
    }

    public int getAreaId(MapObject map, float x, float y) {
        if (map == null) {
            return 0;
        }
        int col = (int) x / map.getArea_width();
        int row = (int) y / map.getArea_high();
        return col * AreaArg + row;
    }

    /**
     * 获取传入地图上,长宽的距离对应的区域数量 返回的数值为未取整的数值,外部自己决定去高还是取低
     *
     * @param map
     * @param dis
     * @return
     */
    public Position getAreaLen(MapObject map, Position dis) {
        Position ret = new Position(dis.getX() / (float) map.getArea_width(), dis.getY() / (float) map.getArea_high());
        return ret;
    }

    //获取周围区域
    public List<Area> getRoundAreas(Player player) {

        return getRoundAreas(getMap(player.gainMapId()), getAreaId(player));
    }

    //获取周围区域
    public List<Area> getRoundAreas(MapObject map, int areaId) {
        List<Area> areas = new ArrayList<>();
        if (map == null) {
            return areas;
        }
        int x = areaId / AreaArg;	//区ID 比如1001
        int y = areaId % AreaArg;

        int radius = map.getRound() / 2;
        int startCol = x - radius;  //得到开始的区域，然后一直累加，取范围
        int startRow = y - radius;

        int areaColCount = map.getAreaCol();
        int areaRowCount = map.getAreaRow();

        for (int i = 0; i < map.getRound(); i++) {
            int areaY = startRow + i;
            if (areaY < 0 || areaY >= areaRowCount) {
                continue;
            }
            for (int j = 0; j < map.getRound(); j++) {
                int areaX = startCol + j;
                if (areaX < 0 || areaX >= areaColCount) {
                    continue;
                }
                int id = areaX * 1000 + areaY;
                Area area = map.getAreas().get(id);
                if (area != null) {
                    areas.add(area);
                }
            }
        }
        return areas;
    }

    /**
     * 按度座标获得区域
     *
     * @param position
     * @param map
     * @return
     */
    public Area getArea(Position position, MapObject map) {
        return map.getAreas().get(getAreaId(map, position));
    }

    /**
     * 按座标点获取周围区域
     *
     * @param position
     * @param map
     * @return
     */
    public List<Area> getRounds(MapObject map, Position position) {
        return getRoundAreas(map, getAreaId(map, position));
    }

    /**
     * 获得半径内的区域Id列表
     *
     * @param position 中心点
     * @param map
     * @param rad 半径
     * @return
     */
    public List<Area> getRoundAreas(MapObject map, Position position, int rad) {
        return getRoundAreasSort(map, position, rad, rad, true);
    }


    public ArrayList<Area> getRoundAreasSort(MapObject map, Position position, int radx, int rady, boolean near) {
        int areaId = getAreaId(map, position);
        int x = areaId / AreaArg;	//区ID 比如1001
        int y = areaId % AreaArg;

        int areaColCount = map.getAreaCol();
        int areaRowCount = map.getAreaRow();

        ArrayList<Area> areaIds = new ArrayList<>();

        if (radx >= areaColCount && rady >= areaRowCount) {
            areaIds.addAll(map.getAreas().values());
            return areaIds;
        }
        int discount = Math.max(radx, rady);
        // 先加上原点
        Area point = map.getAreas().get(areaId);
        if (point != null){
            areaIds.add(point);
        }
        // d为层数
        for (int d = near ? 1 : discount; near ? d <= discount : d > 0; d = near ? d + 1 : d - 1) {
            for (int xi = 0 - d; xi <= d; ++xi) {
                int px = x + xi;
                for (int yi = 0 - d; yi <= d; ++yi) {
//                    // 除了第一次算0,0点以外,后面的都不算了.
//                    if(xi == yi && xi == 0 && d != (near ? 1 : discount)){
//                        continue;
//                    }
                    int py = y + yi;

                    // 不是当层的点都不要
                    if (px != d + x && py != d + y && px != x - d && py != y - d) {
                        continue;
                    }

                    if (px < 0 || py < 0 || (px - x) > radx || (py - y) > rady || px >= areaColCount || py >= areaRowCount) {
                        continue;
                    }
                    int id = px * AreaArg + py;
                    Area area = map.getAreas().get(id);
                    if (area != null) {
//                        for(Area a : areaIds){
//                            if(a.getId() == id){
//                                System.out.println();
//                            }
//                        }
                        areaIds.add(area);
                    }
                }
            }
        }
        return areaIds;
    }

    /**
     * 停止移动
     * @param player
     */
    public void synStopMove(Entity player, boolean includeMe) {
        if (player instanceof Player) {
            ((Player) player).setMoveDir(null);
        }
        ResStopMove.Builder stopmove = ResStopMove.newBuilder();
        stopmove.setObjectId(player.getId());
        stopmove.setPos(MapUtils.getPos(player.gainCurPos()));
        MessageUtils.send_to_roundPlayer(player, ResStopMove.MsgID.eMsgID_VALUE, stopmove.build().toByteArray(), includeMe);

    }

    public boolean changeArea(IMapObject mapObject, Position oldPos, Position newPos) {
        if (null == mapObject) {
            return false;
        }

        MapObject map = getMap(mapObject.gainMapId());
        if (map == null) {
            return false;
        }

        int oldAreaId = getAreaId(map, oldPos);
        int newAreaId = getAreaId(map, newPos);

        if (oldAreaId == newAreaId) {
            return false;
        }

        Area oldArea = map.getAreas().get(oldAreaId);
        Area newArea = map.getAreas().get(newAreaId);

        if (mapObject instanceof Player) {
            Player player = (Player) mapObject;
            if (oldArea == null) {
                logger.error("old area " + oldAreaId + " is null, player " + player.getId() + " position" + player.gainCurPos());
                return false;
            }

            if (newArea == null) {
                logger.error("new area " + newAreaId + " is null, player " + player.getId() + " position" + player.gainCurPos());
                return false;
            }

            if (!map.containPlayer(player.getId())) {
                return false;
            }
            oldArea.removePlayer(player.getId());
            newArea.addPlayer(player.getId(), player);
            return true;
        }

        if (mapObject instanceof Robot) {
            Robot robot = (Robot) mapObject;
            if (oldArea == null) {
                logger.error("old area " + oldAreaId + " is null, robot " + robot.getId() + " position" + robot.gainCurPos());
                return false;
            }

            if (newArea == null) {
                logger.error("new area " + newAreaId + " is null, robot " + robot.getId() + " position" + robot.gainCurPos());
                return false;
            }

            if (!map.getRobots().containsKey(robot.getId())) {
                return false;
            }
            oldArea.getRobots().remove(robot.getId());
            newArea.getRobots().put(robot.getId(), robot);
            return true;
        }

        if (mapObject instanceof Monster) {
            Monster monster = (Monster) mapObject;
            if (oldArea == null) {
                logger.error("old area " + oldAreaId + " is null, monster " + monster.getId() + " position" + monster.gainCurPos());
                return false;
            }

            if (newArea == null) {
                logger.error("new area " + newAreaId + " is null, monster " + monster.getId() + " position" + monster.gainCurPos());
                return false;
            }

            if (!map.getMonsters().containsKey(monster.getId())) {
                return false;
            }
            oldArea.getMonsters().remove(monster.getId());
            newArea.getMonsters().put(monster.getId(), monster);
            return true;
        }

        if (mapObject instanceof SkillMagic) {
            SkillMagic skillMagic = (SkillMagic) mapObject;
            if (oldArea == null) {
                logger.error("old area " + oldAreaId + " is null, skillMagic " + skillMagic.getId() + " position" + skillMagic.gainCurPos());
                return false;
            }

            if (newArea == null) {
                logger.error("new area " + newAreaId + " is null, skillMagic " + skillMagic.getId() + " position" + skillMagic.gainCurPos());
                return false;
            }

            if (map.getMagic(skillMagic.getId()) == null) {
                return false;
            }
            oldArea.removeMagic(skillMagic.getId());
            newArea.addMagic(skillMagic);
            return true;
        }
        return false;
    }

    //移动
    public void sendMoveMessage(Entity entity, List<Position> roads) {
        entity.getDirs().clear();
        ResMoveTo.Builder moveMsg = ResMoveTo.newBuilder();
        moveMsg.setObjectId(entity.getId());
        for (Position pos : roads) {
            moveMsg.addPosList(MapUtils.getPos(pos));
        }
        MessageUtils.send_to_roundPlayer(entity, ResMoveTo.MsgID.eMsgID_VALUE, moveMsg.build().toByteArray(), false);
    }

    /**
     * 设置地图阻挡门信息
     *
     * @param map 地图
     * @param id 阻档的名字
     * @param isOpen 是否关闭阻档块 ， true关闭， false为开启
     */
    public void setBlockDoor(MapObject map, String id, boolean isOpen) {
        DynamicBlock door = map.getDoors().get(id);
        if (null == door) {
            return;
        }
        door.setOpen(isOpen);

        ResUpdateBlockDoor.Builder msg = ResUpdateBlockDoor.newBuilder();
        msg.setId(id);
        msg.setIsopen(isOpen);

        MessageUtils.send_to_map(map, ResUpdateBlockDoor.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 返回发送给客户端专用的地图名字
     * @param mapId
     * @return
     */
    public String getChatMapName(int mapId) {
        Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(mapId);
        if( bean == null){
            return "map" + mapId;
        }
        return ServerStr.getChatTableName(bean.getName());
    }

}
