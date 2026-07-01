/**
 * Auto generated, do not edit it
 *
 * immortal_soul_iattice配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Immortal_soul_iattice_Bean{
    /**
     * 格子id
     */
    private final int iattice_id;
    /**
     * 格子id
     * @return
     */
    public final int getIattice_id(){
        return iattice_id;
    }
    /**
     * 可镶嵌类型
     */
    private final ReadIntegerArrayEs type;
    /**
     * 可镶嵌类型
     * @return
     */
    public final ReadIntegerArrayEs getType(){
        return type;
    }
    /**
     * 开启条件(FunctionVariable中条件)
     */
    private final ReadIntegerArrayEs condition;
    /**
     * 开启条件(FunctionVariable中条件)
     * @return
     */
    public final ReadIntegerArrayEs getCondition(){
        return condition;
    }

    public Cfg_Immortal_soul_iattice_Bean(int iattice_id,String typeStr,String conditionStr){
        this.iattice_id = iattice_id;
        this.type = new ReadIntegerArrayEs(typeStr,"}",",");
        this.condition = new ReadIntegerArrayEs(conditionStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("iattice_id:").append(iattice_id).append(";");
        str.append("type:").append(type).append(";");
        str.append("condition:").append(condition).append(";");
        return str.toString();
    }
}
