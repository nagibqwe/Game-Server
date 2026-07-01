/**
 * Auto generated, do not edit it
 *
 * marry_battle_time配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Marry_battle_time_Bean{
    /**
     * 排序id(类型*100+场次）
     */
    private final int id;
    /**
     * 排序id(类型*100+场次）
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 类型
0，报名；1海选；2小组；3冠军（地榜）；4冠军（天榜）
     */
    private final int type;
    /**
     * 类型
0，报名；1海选；2小组；3冠军（地榜）；4冠军（天榜）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 场次，0为不限制场次；自然数为对应的场次，-1是准备时间，可以进入准备场地
     */
    private final int game;
    /**
     * 场次，0为不限制场次；自然数为对应的场次，-1是准备时间，可以进入准备场地
     * @return
     */
    public final int getGame(){
        return game;
    }
    /**
     * 开始时间（周几_从0点开始的分钟数）
     */
    private final ReadIntegerArray start_time;
    /**
     * 开始时间（周几_从0点开始的分钟数）
     * @return
     */
    public final ReadIntegerArray getStart_time(){
        return start_time;
    }
    /**
     * 结束时间（周几_从0点开始的分钟数）
     */
    private final ReadIntegerArray over_time;
    /**
     * 结束时间（周几_从0点开始的分钟数）
     * @return
     */
    public final ReadIntegerArray getOver_time(){
        return over_time;
    }

    public Cfg_Marry_battle_time_Bean(int id,int type,int game,String start_timeStr,String over_timeStr){
        this.id = id;
        this.type = type;
        this.game = game;
        this.start_time = new ReadIntegerArray(start_timeStr,",");
        this.over_time = new ReadIntegerArray(over_timeStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("game:").append(game).append(";");
        str.append("start_time:").append(start_time).append(";");
        str.append("over_time:").append(over_time).append(";");
        return str.toString();
    }
}
