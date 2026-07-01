/**
 * Auto generated, do not edit it
 *
 * FunctionOpenTips配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_FunctionOpenTips_Bean{
    /**
     * 编号
     */
    private final int id;
    /**
     * 编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 对应功能的id
     */
    private final int function_id;
    /**
     * 对应功能的id
     * @return
     */
    public final int getFunction_id(){
        return function_id;
    }
    /**
     * 物品奖励
     */
    private final ReadIntegerArrayEs award_item;
    /**
     * 物品奖励
     * @return
     */
    public final ReadIntegerArrayEs getAward_item(){
        return award_item;
    }
    /**
     * 存在的天数，从开服时间开始算，时间到之后隐藏
     */
    private final int active_day;
    /**
     * 存在的天数，从开服时间开始算，时间到之后隐藏
     * @return
     */
    public final int getActive_day(){
        return active_day;
    }

    public Cfg_FunctionOpenTips_Bean(int id,int function_id,String award_itemStr,int active_day){
        this.id = id;
        this.function_id = function_id;
        this.award_item = new ReadIntegerArrayEs(award_itemStr,"}",",");
        this.active_day = active_day;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("function_id:").append(function_id).append(";");
        str.append("award_item:").append(award_item).append(";");
        str.append("active_day:").append(active_day).append(";");
        return str.toString();
    }
}
