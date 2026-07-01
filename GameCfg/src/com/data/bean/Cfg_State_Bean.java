/**
 * Auto generated, do not edit it
 *
 * state配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_State_Bean{
    /**
     * 境界的ID
=group*100+sort
     */
    private final int id;
    /**
     * 境界的ID
=group*100+sort
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 境界组，其他地方调用
     */
    private final int group;
    /**
     * 境界组，其他地方调用
     * @return
     */
    public final int getGroup(){
        return group;
    }
    /**
     * 任务编号和排序
     */
    private final int sort;
    /**
     * 任务编号和排序
     * @return
     */
    public final int getSort(){
        return sort;
    }
    /**
     * 达成的成就条件(@_@)
条件都需要定义在functionVariable中
     */
    private final ReadIntegerArray condition;
    /**
     * 达成的成就条件(@_@)
条件都需要定义在functionVariable中
     * @return
     */
    public final ReadIntegerArray getCondition(){
        return condition;
    }

    public Cfg_State_Bean(int id,int group,int sort,String conditionStr){
        this.id = id;
        this.group = group;
        this.sort = sort;
        this.condition = new ReadIntegerArray(conditionStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("group:").append(group).append(";");
        str.append("sort:").append(sort).append(";");
        str.append("condition:").append(condition).append(";");
        return str.toString();
    }
}
