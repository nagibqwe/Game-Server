/**
 * Auto generated, do not edit it
 *
 * bossnew_world配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Bossnew_world_Bean{
    /**
     * 怪物ID
     */
    private final int ID;
    /**
     * 怪物ID
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * BOSS分页
     */
    private final int page;
    /**
     * BOSS分页
     * @return
     */
    public final int getPage(){
        return page;
    }
    /**
     * 掉落装备阶数
     */
    private final int dropEquipShow;
    /**
     * 掉落装备阶数
     * @return
     */
    public final int getDropEquipShow(){
        return dropEquipShow;
    }
    /**
     * 击杀累积天谴值（套装BOSS，宝石BOSS用）
     */
    private final int scourge;
    /**
     * 击杀累积天谴值（套装BOSS，宝石BOSS用）
     * @return
     */
    public final int getScourge(){
        return scourge;
    }
    /**
     * 地图层数
     */
    private final int mapnum;
    /**
     * 地图层数
     * @return
     */
    public final int getMapnum(){
        return mapnum;
    }
    /**
     * 是否是无限层
     */
    private final int infinite;
    /**
     * 是否是无限层
     * @return
     */
    public final int getInfinite(){
        return infinite;
    }
    /**
     * 刷新副本地图
     */
    private final int clone_map;
    /**
     * 刷新副本地图
     * @return
     */
    public final int getClone_map(){
        return clone_map;
    }
    /**
     * 刷新坐标(@;@_@)
     */
    private final ReadIntegerArray pos;
    /**
     * 刷新坐标(@;@_@)
     * @return
     */
    public final ReadIntegerArray getPos(){
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
    /**
     * 是
     */
    private final int min_grade;
    /**
     * 是
     * @return
     */
    public final int getMin_grade(){
        return min_grade;
    }
    /**
     * 复活时是否走跑马灯
（0否1是）
     */
    private final int isNotice;
    /**
     * 复活时是否走跑马灯
（0否1是）
     * @return
     */
    public final int getIsNotice(){
        return isNotice;
    }
    /**
     * 掉落物品后聊天发送(0世界4系统14传闻）
     */
    private final ReadIntegerArray chatchannel;
    /**
     * 掉落物品后聊天发送(0世界4系统14传闻）
     * @return
     */
    public final ReadIntegerArray getChatchannel(){
        return chatchannel;
    }

    public Cfg_Bossnew_world_Bean(int ID,int page,int dropEquipShow,int scourge,int mapnum,int infinite,int clone_map,String posStr,int initial_time,int standard_time,int float_time,String limit_timeStr,String increase_timeStr,int if_raward,int min_grade,int isNotice,String chatchannelStr){
        this.ID = ID;
        this.page = page;
        this.dropEquipShow = dropEquipShow;
        this.scourge = scourge;
        this.mapnum = mapnum;
        this.infinite = infinite;
        this.clone_map = clone_map;
        this.pos = new ReadIntegerArray(posStr,",");
        this.initial_time = initial_time;
        this.standard_time = standard_time;
        this.float_time = float_time;
        this.limit_time = new ReadIntegerArrayEs(limit_timeStr,"}",",");
        this.increase_time = new ReadIntegerArrayEs(increase_timeStr,"}",",");
        this.if_raward = if_raward;
        this.min_grade = min_grade;
        this.isNotice = isNotice;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("page:").append(page).append(";");
        str.append("dropEquipShow:").append(dropEquipShow).append(";");
        str.append("scourge:").append(scourge).append(";");
        str.append("mapnum:").append(mapnum).append(";");
        str.append("infinite:").append(infinite).append(";");
        str.append("clone_map:").append(clone_map).append(";");
        str.append("pos:").append(pos).append(";");
        str.append("initial_time:").append(initial_time).append(";");
        str.append("standard_time:").append(standard_time).append(";");
        str.append("float_time:").append(float_time).append(";");
        str.append("limit_time:").append(limit_time).append(";");
        str.append("increase_time:").append(increase_time).append(";");
        str.append("if_raward:").append(if_raward).append(";");
        str.append("min_grade:").append(min_grade).append(";");
        str.append("isNotice:").append(isNotice).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        return str.toString();
    }
}
