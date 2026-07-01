/**
 * Auto generated, do not edit it
 *
 * Cross_Alien_Connect_Show配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Cross_Alien_Connect_Show_Bean{
    /**
     * ID
     */
    private final int id;
    /**
     * ID
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

    public Cfg_Cross_Alien_Connect_Show_Bean(int id,String dayStr,String levelStr){
        this.id = id;
        this.day = new ReadIntegerArray(dayStr,",");
        this.level = new ReadIntegerArray(levelStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("day:").append(day).append(";");
        str.append("level:").append(level).append(";");
        return str.toString();
    }
}
