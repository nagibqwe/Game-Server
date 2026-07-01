package com.game.map.structs;

import com.game.cooldown.structs.Cooldown;
import com.game.player.structs.Player;
import com.game.structs.Position;
import com.game.team.manager.TeamManager;
import com.game.utils.MapUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * 地图基础类
 * @author luck
 */
public abstract class BaseNpc {

    //唯一Id
    protected long id; 
    //模型ID
    protected int modelId; 
    //队伍Id;
    protected long teamId; 
    //名字
    protected String name = "";
    //当前坐标
    protected Position curPos = new Position();
    //方向向量
    protected Position dir = new Position(0, 1);
    //移动路点
    protected ArrayList<Position> roads = new ArrayList<>();
    //冷却列表
    protected HashMap<String, Cooldown> cooldowns = new HashMap<>();
    //阵营
    protected int campNo;
    //移动速度
    protected float speed;
    //地图配置表id,当前地图模型ID
    protected int mapModelId;

    // 下一个路径点的下标
    protected int nextPathPointIdx = 0;

    public long getId() {
        return id;
    }
    
    public void setId(long id){
        this.id = id;
    }
    
    public int getCampNo() {
        return campNo;
    }

    public void setCampNo(int campNo) {
        this.campNo = campNo;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getCurPos() {
        return curPos;
    }

    public void setCurPos(Position curPos) {
        this.curPos = curPos;
    }

    public void forceStopMove(){
        nextPathPointIdx = roads.size();
    }

    protected abstract void onForceStopMove();

    public Position getDir() {
        return dir;
    }

    public void setDir(Position dir) {
        this.dir = dir;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public ArrayList<Position> getRoads() {
        return roads;
    }

    public void setRoads(ArrayList<Position> r){
        roads = r;
        nextPathPointIdx = 0;
    }

    public void cleanRoads(){
        roads.clear();
        nextPathPointIdx = 0;
    }

    public int getMapModelId(){
        return mapModelId;
    }
    
    @Override
    public String toString() {
        return "【" + name + "】 curPos = " + curPos + " modelId=" + modelId + " id =" + id;
    }

    public void moveAi_MoveToPos(Position endpos) {
        setRoads(MapUtils.findRoads(mapModelId, curPos, endpos, -1));
    }

    /**
     * 返回true表示移动完成了
     * @param dt_ms
     * @return
     */
    public boolean moveTick(long dt_ms){
        Position cur = curPos;

        float moveSpeed = getSpeed();


        if (moveSpeed <= 0) {
            roads.clear();
            return true;
        }

        float moveDis = moveSpeed * dt_ms / 1000f;

        while (roads.size() > nextPathPointIdx) {
            Position nextPoint = roads.get(nextPathPointIdx);
            ++nextPathPointIdx;

            float dis = (float)MapUtils.getDistance(nextPoint, cur);

            if (moveDis > dis) {
                setCurPos(nextPoint);
                moveDis -= dis;
                continue;
            }
            Position dirPos = MapUtils.getDirPos(curPos, nextPoint, moveDis);
            setCurPos(dirPos);
            break;
        }
        return roads.size() <= nextPathPointIdx;
    }

    public void onMoveAiRemove() {

    }

    public Collection<Cooldown> getCooldowns() {
        return cooldowns.values();
    }

    public boolean containCooldown(String key){
        return cooldowns.containsKey(key);
    }

    public Cooldown getCooldown(String key){
        return cooldowns.get(key);
    }

    public void addCooldown(String key, Cooldown v){
        cooldowns.put(key, v);
    }

    public Cooldown removeCooldown(String key){
        return cooldowns.remove(key);
    }

}
