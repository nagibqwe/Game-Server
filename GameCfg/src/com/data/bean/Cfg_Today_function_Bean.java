/**
 * Auto generated, do not edit it
 *
 * today_function配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Today_function_Bean{
    /**
     * key值
     */
    private final int id;
    /**
     * key值
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 玩法名称
     */
    private final String name;
    /**
     * 玩法名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 玩法时间描述
     */
    private final String time_des;
    /**
     * 玩法时间描述
     * @return
     */
    public final String getTime_des(){
        return time_des;
    }
    /**
     * 功能描述
     */
    private final String des;
    /**
     * 功能描述
     * @return
     */
    public final String getDes(){
        return des;
    }
    /**
     * 奖励物品展示
     */
    private final ReadIntegerArrayEs reward_item;
    /**
     * 奖励物品展示
     * @return
     */
    public final ReadIntegerArrayEs getReward_item(){
        return reward_item;
    }
    /**
     * 开启的天数（闭区间）
     */
    private final ReadIntegerArray open_day;
    /**
     * 开启的天数（闭区间）
     * @return
     */
    public final ReadIntegerArray getOpen_day(){
        return open_day;
    }
    /**
     * 是周几
     */
    private final ReadIntegerArray week_day;
    /**
     * 是周几
     * @return
     */
    public final ReadIntegerArray getWeek_day(){
        return week_day;
    }
    /**
     * 跳转的functionID
     */
    private final int functionID;
    /**
     * 跳转的functionID
     * @return
     */
    public final int getFunctionID(){
        return functionID;
    }
    /**
     * 跳转参数
     */
    private final int parm;
    /**
     * 跳转参数
     * @return
     */
    public final int getParm(){
        return parm;
    }

    public Cfg_Today_function_Bean(int id,String name,String time_des,String des,String reward_itemStr,String open_dayStr,String week_dayStr,int functionID,int parm){
        this.id = id;
        this.name = name;
        this.time_des = time_des;
        this.des = des;
        this.reward_item = new ReadIntegerArrayEs(reward_itemStr,"}",",");
        this.open_day = new ReadIntegerArray(open_dayStr,",");
        this.week_day = new ReadIntegerArray(week_dayStr,",");
        this.functionID = functionID;
        this.parm = parm;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("time_des:").append(time_des).append(";");
        str.append("des:").append(des).append(";");
        str.append("reward_item:").append(reward_item).append(";");
        str.append("open_day:").append(open_day).append(";");
        str.append("week_day:").append(week_day).append(";");
        str.append("functionID:").append(functionID).append(";");
        str.append("parm:").append(parm).append(";");
        return str.toString();
    }
}
