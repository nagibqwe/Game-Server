/**
 * Auto generated, do not edit it
 *
 * rank配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Rank_Bean{
    /**
     * 排行榜ID
     */
    private final int id;
    /**
     * 排行榜ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 排行榜名字
     */
    private final String name;
    /**
     * 排行榜名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 进入排行榜条件(@_@)
     */
    private final ReadIntegerArray conditions;
    /**
     * 进入排行榜条件(@_@)
     * @return
     */
    public final ReadIntegerArray getConditions(){
        return conditions;
    }

    public Cfg_Rank_Bean(int id,String name,String conditionsStr){
        this.id = id;
        this.name = name;
        this.conditions = new ReadIntegerArray(conditionsStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("conditions:").append(conditions).append(";");
        return str.toString();
    }
}
