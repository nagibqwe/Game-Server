/**
 * Auto generated, do not edit it
 *
 * clone_monster配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Clone_monster_Bean{
    /**
     * 编号ID
     */
    private final int ID;
    /**
     * 编号ID
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 副本ID
     */
    private final int cloneID;
    /**
     * 副本ID
     * @return
     */
    public final int getCloneID(){
        return cloneID;
    }
    /**
     * 波次
     */
    private final int monsterWave;
    /**
     * 波次
     * @return
     */
    public final int getMonsterWave(){
        return monsterWave;
    }
    /**
     * 刷新信息(@;@_@)
     */
    private final ReadIntegerArrayEs monster_information;
    /**
     * 刷新信息(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getMonster_information(){
        return monster_information;
    }
    /**
     * 是否为最后一波(0,不是。1，是）
     */
    private final int if_end;
    /**
     * 是否为最后一波(0,不是。1，是）
     * @return
     */
    public final int getIf_end(){
        return if_end;
    }
    /**
     * 每一波刷怪的延迟时间，单位时间毫秒
     */
    private final int waiting;
    /**
     * 每一波刷怪的延迟时间，单位时间毫秒
     * @return
     */
    public final int getWaiting(){
        return waiting;
    }
    /**
     * 每一波进度，触发进度的玩家坐标点(@_@)
     */
    private final ReadIntegerArray pathfinding;
    /**
     * 每一波进度，触发进度的玩家坐标点(@_@)
     * @return
     */
    public final ReadIntegerArray getPathfinding(){
        return pathfinding;
    }

    public Cfg_Clone_monster_Bean(int ID,int cloneID,int monsterWave,String monster_informationStr,int if_end,int waiting,String pathfindingStr){
        this.ID = ID;
        this.cloneID = cloneID;
        this.monsterWave = monsterWave;
        this.monster_information = new ReadIntegerArrayEs(monster_informationStr,"}",",");
        this.if_end = if_end;
        this.waiting = waiting;
        this.pathfinding = new ReadIntegerArray(pathfindingStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("cloneID:").append(cloneID).append(";");
        str.append("monsterWave:").append(monsterWave).append(";");
        str.append("monster_information:").append(monster_information).append(";");
        str.append("if_end:").append(if_end).append(";");
        str.append("waiting:").append(waiting).append(";");
        str.append("pathfinding:").append(pathfinding).append(";");
        return str.toString();
    }
}
