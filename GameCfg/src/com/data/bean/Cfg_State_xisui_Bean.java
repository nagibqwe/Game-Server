/**
 * Auto generated, do not edit it
 *
 * state_xisui配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_State_xisui_Bean{
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
     * 洗髓玩法
     */
    private final String name;
    /**
     * 洗髓玩法
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 洗髓目标
     */
    private final String target_name;
    /**
     * 洗髓目标
     * @return
     */
    public final String getTarget_name(){
        return target_name;
    }
    /**
     * 玩法描述
     */
    private final String describe;
    /**
     * 玩法描述
     * @return
     */
    public final String getDescribe(){
        return describe;
    }
    /**
     * 条件：条件类型_条件参数(1、等级条件 2、境界条件)
     */
    private final ReadIntegerArrayEs condition;
    /**
     * 条件：条件类型_条件参数(1、等级条件 2、境界条件)
     * @return
     */
    public final ReadIntegerArrayEs getCondition(){
        return condition;
    }

    public Cfg_State_xisui_Bean(int id,String name,String target_name,String describe,String conditionStr){
        this.id = id;
        this.name = name;
        this.target_name = target_name;
        this.describe = describe;
        this.condition = new ReadIntegerArrayEs(conditionStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("target_name:").append(target_name).append(";");
        str.append("describe:").append(describe).append(";");
        str.append("condition:").append(condition).append(";");
        return str.toString();
    }
}
