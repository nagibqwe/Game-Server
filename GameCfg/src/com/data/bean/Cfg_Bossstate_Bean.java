/**
 * Auto generated, do not edit it
 *
 * bossstate配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Bossstate_Bean{
    /**
     * 编号ID，表示层级
     */
    private final int ID;
    /**
     * 编号ID，表示层级
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 是否在列表中显示(0否1是)
     */
    private final int canShow;
    /**
     * 是否在列表中显示(0否1是)
     * @return
     */
    public final int getCanShow(){
        return canShow;
    }
    /**
     * 对应FunctionVariable的ID
     */
    private final ReadIntegerArray stateLevel;
    /**
     * 对应FunctionVariable的ID
     * @return
     */
    public final ReadIntegerArray getStateLevel(){
        return stateLevel;
    }
    /**
     * 该层级刷出的怪物ID
     */
    private final int monster;
    /**
     * 该层级刷出的怪物ID
     * @return
     */
    public final int getMonster(){
        return monster;
    }
    /**
     * 所在层数
     */
    private final int layer;
    /**
     * 所在层数
     * @return
     */
    public final int getLayer(){
        return layer;
    }
    /**
     * 副本地图
     */
    private final int cloneID;
    /**
     * 副本地图
     * @return
     */
    public final int getCloneID(){
        return cloneID;
    }
    /**
     * 推荐战力
     */
    private final int power;
    /**
     * 推荐战力
     * @return
     */
    public final int getPower(){
        return power;
    }
    /**
     * 首次掉落
服务器掉落，但是客户端不显示
item_num_bind_occ
     */
    private final ReadIntegerArrayEs frist_reward;
    /**
     * 首次掉落
服务器掉落，但是客户端不显示
item_num_bind_occ
     * @return
     */
    public final ReadIntegerArrayEs getFrist_reward(){
        return frist_reward;
    }
    /**
     * 扫荡奖励(@;@_@)
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 扫荡奖励(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 刷新坐标(@_@)
     */
    private final ReadIntegerArray pos;
    /**
     * 刷新坐标(@_@)
     * @return
     */
    public final ReadIntegerArray getPos(){
        return pos;
    }
    /**
     * 当前层挑战时间
单位秒
     */
    private final int time;
    /**
     * 当前层挑战时间
单位秒
     * @return
     */
    public final int getTime(){
        return time;
    }
    /**
     * 首次击杀后BOSS是否继续显示在界面上
（0否1是）
     */
    private final int show_boss;
    /**
     * 首次击杀后BOSS是否继续显示在界面上
（0否1是）
     * @return
     */
    public final int getShow_boss(){
        return show_boss;
    }

    public Cfg_Bossstate_Bean(int ID,int canShow,String stateLevelStr,int monster,int layer,int cloneID,int power,String frist_rewardStr,String rewardStr,String posStr,int time,int show_boss){
        this.ID = ID;
        this.canShow = canShow;
        this.stateLevel = new ReadIntegerArray(stateLevelStr,",");
        this.monster = monster;
        this.layer = layer;
        this.cloneID = cloneID;
        this.power = power;
        this.frist_reward = new ReadIntegerArrayEs(frist_rewardStr,"}",",");
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.pos = new ReadIntegerArray(posStr,",");
        this.time = time;
        this.show_boss = show_boss;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("canShow:").append(canShow).append(";");
        str.append("stateLevel:").append(stateLevel).append(";");
        str.append("monster:").append(monster).append(";");
        str.append("layer:").append(layer).append(";");
        str.append("cloneID:").append(cloneID).append(";");
        str.append("power:").append(power).append(";");
        str.append("frist_reward:").append(frist_reward).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("pos:").append(pos).append(";");
        str.append("time:").append(time).append(";");
        str.append("show_boss:").append(show_boss).append(";");
        return str.toString();
    }
}
