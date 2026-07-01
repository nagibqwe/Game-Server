/**
 * Auto generated, do not edit it
 *
 * Cross_devil_Group_Boss配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Cross_devil_Group_Boss_Bean{
    /**
     * 怪物ID
     */
    private final int id;
    /**
     * 怪物ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 开服时间区间
     */
    private final ReadIntegerArray day;
    /**
     * 开服时间区间
     * @return
     */
    public final ReadIntegerArray getDay(){
        return day;
    }
    /**
     * 世界等级区间
     */
    private final ReadIntegerArray level;
    /**
     * 世界等级区间
     * @return
     */
    public final ReadIntegerArray getLevel(){
        return level;
    }
    /**
     * 刷新间隔事件
（秒）

     */
    private final int refresh_time;
    /**
     * 刷新间隔事件
（秒）

     * @return
     */
    public final int getRefresh_time(){
        return refresh_time;
    }
    /**
     * 怪物名
     */
    private final String name;
    /**
     * 怪物名
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 头像
     */
    private final int icon;
    /**
     * 头像
     * @return
     */
    public final int getIcon(){
        return icon;
    }
    /**
     * 击杀获得积分
所有参与攻击的玩家按伤害比例瓜分
     */
    private final int point;
    /**
     * 击杀获得积分
所有参与攻击的玩家按伤害比例瓜分
     * @return
     */
    public final int getPoint(){
        return point;
    }

    public Cfg_Cross_devil_Group_Boss_Bean(int id,String dayStr,String levelStr,int refresh_time,String name,int icon,int point){
        this.id = id;
        this.day = new ReadIntegerArray(dayStr,",");
        this.level = new ReadIntegerArray(levelStr,",");
        this.refresh_time = refresh_time;
        this.name = name;
        this.icon = icon;
        this.point = point;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("day:").append(day).append(";");
        str.append("level:").append(level).append(";");
        str.append("refresh_time:").append(refresh_time).append(";");
        str.append("name:").append(name).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("point:").append(point).append(";");
        return str.toString();
    }
}
