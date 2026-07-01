/**
 * Auto generated, do not edit it
 *
 * bossnew_HorseBoss配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Bossnew_HorseBoss_Bean{
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
     * 怪物名称
     */
    private final String monster_name;
    /**
     * 怪物名称
     * @return
     */
    public final String getMonster_name(){
        return monster_name;
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
     * 是否为跨服（0否1是）
     */
    private final int crossSever;
    /**
     * 是否为跨服（0否1是）
     * @return
     */
    public final int getCrossSever(){
        return crossSever;
    }
    /**
     * 怪物ID或采集物ID
     */
    private final int monsterid;
    /**
     * 怪物ID或采集物ID
     * @return
     */
    public final int getMonsterid(){
        return monsterid;
    }
    /**
     * 进入所需等级
     */
    private final int enterLevel;
    /**
     * 进入所需等级
     * @return
     */
    public final int getEnterLevel(){
        return enterLevel;
    }
    /**
     * 击杀获得的荒古令
     */
    private final int score;
    /**
     * 击杀获得的荒古令
     * @return
     */
    public final int getScore(){
        return score;
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
     * 需要脉轮评级
     */
    private final int power;
    /**
     * 需要脉轮评级
     * @return
     */
    public final int getPower(){
        return power;
    }
    /**
     * 副本ID
     */
    private final int cloneid;
    /**
     * 副本ID
     * @return
     */
    public final int getCloneid(){
        return cloneid;
    }
    /**
     * 刷新数量
     */
    private final int num;
    /**
     * 刷新数量
     * @return
     */
    public final int getNum(){
        return num;
    }
    /**
     * 刷新坐标(@;@_@)1.随机坐标点；2固定坐标点；3固定坐标点；4固定坐标点
     */
    private final ReadIntegerArrayEs pos;
    /**
     * 刷新坐标(@;@_@)1.随机坐标点；2固定坐标点；3固定坐标点；4固定坐标点
     * @return
     */
    public final ReadIntegerArrayEs getPos(){
        return pos;
    }
    /**
     * 初始时间(秒) client ignore
     */
    private final int initial_time;
    /**
     * 初始时间(秒) client ignore
     * @return
     */
    public final int getInitial_time(){
        return initial_time;
    }
    /**
     * 标准时间(秒)client ignore
     */
    private final int standard_time;
    /**
     * 标准时间(秒)client ignore
     * @return
     */
    public final int getStandard_time(){
        return standard_time;
    }
    /**
     * 浮动值（秒） client ignore
     */
    private final int float_time;
    /**
     * 浮动值（秒） client ignore
     * @return
     */
    public final int getFloat_time(){
        return float_time;
    }
    /**
     * 上下限(开服时间_上限_下限;开服时间_上限_下限)(@;@_@)client ignore
     */
    private final ReadIntegerArrayEs limit_time;
    /**
     * 上下限(开服时间_上限_下限;开服时间_上限_下限)(@;@_@)client ignore
     * @return
     */
    public final ReadIntegerArrayEs getLimit_time(){
        return limit_time;
    }
    /**
     * 增加时间(开服时间_增加时间(秒))(@;@_@) client ignore
     */
    private final ReadIntegerArrayEs increase_time;
    /**
     * 增加时间(开服时间_增加时间(秒))(@;@_@) client ignore
     * @return
     */
    public final ReadIntegerArrayEs getIncrease_time(){
        return increase_time;
    }
    /**
     * 是否扣除收益次数（0，不扣除；1，扣除）
     */
    private final int if_raward;
    /**
     * 是否扣除收益次数（0，不扣除；1，扣除）
     * @return
     */
    public final int getIf_raward(){
        return if_raward;
    }

    public Cfg_Bossnew_HorseBoss_Bean(int ID,String monster_name,int canShow,int crossSever,int monsterid,int enterLevel,int score,int layer,int power,int cloneid,int num,String posStr,int initial_time,int standard_time,int float_time,String limit_timeStr,String increase_timeStr,int if_raward){
        this.ID = ID;
        this.monster_name = monster_name;
        this.canShow = canShow;
        this.crossSever = crossSever;
        this.monsterid = monsterid;
        this.enterLevel = enterLevel;
        this.score = score;
        this.layer = layer;
        this.power = power;
        this.cloneid = cloneid;
        this.num = num;
        this.pos = new ReadIntegerArrayEs(posStr,"}",",");
        this.initial_time = initial_time;
        this.standard_time = standard_time;
        this.float_time = float_time;
        this.limit_time = new ReadIntegerArrayEs(limit_timeStr,"}",",");
        this.increase_time = new ReadIntegerArrayEs(increase_timeStr,"}",",");
        this.if_raward = if_raward;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("monster_name:").append(monster_name).append(";");
        str.append("canShow:").append(canShow).append(";");
        str.append("crossSever:").append(crossSever).append(";");
        str.append("monsterid:").append(monsterid).append(";");
        str.append("enterLevel:").append(enterLevel).append(";");
        str.append("score:").append(score).append(";");
        str.append("layer:").append(layer).append(";");
        str.append("power:").append(power).append(";");
        str.append("cloneid:").append(cloneid).append(";");
        str.append("num:").append(num).append(";");
        str.append("pos:").append(pos).append(";");
        str.append("initial_time:").append(initial_time).append(";");
        str.append("standard_time:").append(standard_time).append(";");
        str.append("float_time:").append(float_time).append(";");
        str.append("limit_time:").append(limit_time).append(";");
        str.append("increase_time:").append(increase_time).append(";");
        str.append("if_raward:").append(if_raward).append(";");
        return str.toString();
    }
}
