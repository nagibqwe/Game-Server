package com.game.npc.structs;

import com.game.map.structs.MapGps;
import com.game.server.GameServer;
import com.game.structs.GameObject;
import game.core.map.IMapObject;
import game.core.map.Position;

import java.util.HashMap;
import java.util.List;

/**
 * 墓碑
 * 客户端一个纯显示的东西
 * @author zhaibiao
 */
public class Tombstone extends GameObject implements IMapObject{
    
    private int monsterId;//怪物Id
    private long bornTime;//墓碑出现时间
    private long dieTime;//墓碑消失时间
    private MapGps curGps = new MapGps();

    public int getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(int monsterId) {
        this.monsterId = monsterId;
    }

    public long getBornTime() {
        return bornTime;
    }

    public void setBornTime(long bornTime) {
        this.bornTime = bornTime;
    }

    public long getDieTime() {
        return dieTime;
    }

    public void setDieTime(long dieTime) {
        this.dieTime = dieTime;
    }

    public MapGps getCurGps() {
        return curGps;
    }

    public void setCurGps(MapGps curGps) {
        this.curGps = curGps;
    }

    @Override
    public int gainLine() {
        return curGps.getLine();
    }

    @Override
    public int gainMapModelId() {
        return curGps.getModelId();
    }

    @Override
    public long gainMapId() {
        return curGps.getMapId();
    }


    @Override
    public Position gainCurPos() {
        return getCurGps().getPos();
    }

    @Override
    public boolean canSee(IMapObject player) {
        return true;
    }

    /**
     * 获取任务隐藏id集合
     *
     * @return
     */
    @Override
    public HashMap<Integer, List<Integer>> gainHideTaskIds() {
        return null;
    }


    @Override
    public void release() {

    }
}
