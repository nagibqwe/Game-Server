/**
 * Auto generated, do not edit it
 *
 * scuffle_king配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Scuffle_king_Bean{
    /**
     * id
     */
    private final int id;
    /**
     * id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 台子等级
     */
    private final int level;
    /**
     * 台子等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 下一级台子的id
     */
    private final int next_level_id;
    /**
     * 下一级台子的id
     * @return
     */
    public final int getNext_level_id(){
        return next_level_id;
    }
    /**
     * 飞行到下一个台子的飞行ID
     */
    private final long to_next_fly_id;
    /**
     * 飞行到下一个台子的飞行ID
     * @return
     */
    public final long getTo_next_fly_id(){
        return to_next_fly_id;
    }
    /**
     * 进入坐标，3个玩家分别配置。只有第一级台子才会生效(@;@_@)
     */
    private final ReadIntegerArrayEs enter_pos;
    /**
     * 进入坐标，3个玩家分别配置。只有第一级台子才会生效(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getEnter_pos(){
        return enter_pos;
    }

    public Cfg_Scuffle_king_Bean(int id,int level,int next_level_id,long to_next_fly_id,String enter_posStr){
        this.id = id;
        this.level = level;
        this.next_level_id = next_level_id;
        this.to_next_fly_id = to_next_fly_id;
        this.enter_pos = new ReadIntegerArrayEs(enter_posStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("level:").append(level).append(";");
        str.append("next_level_id:").append(next_level_id).append(";");
        str.append("to_next_fly_id:").append(to_next_fly_id).append(";");
        str.append("enter_pos:").append(enter_pos).append(";");
        return str.toString();
    }
}
