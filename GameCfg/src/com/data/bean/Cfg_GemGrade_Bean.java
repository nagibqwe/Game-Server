/**
 * Auto generated, do not edit it
 *
 * GemGrade配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_GemGrade_Bean{
    /**
     * 总等级id
     */
    private final int Id;
    /**
     * 总等级id
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 等级
     */
    private final int leve;
    /**
     * 等级
     * @return
     */
    public final int getLeve(){
        return leve;
    }
    /**
     * 属性(@;@_@)
     */
    private final ReadIntegerArrayEs addAttr;
    /**
     * 属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAddAttr(){
        return addAttr;
    }

    public Cfg_GemGrade_Bean(int Id,int leve,String addAttrStr){
        this.Id = Id;
        this.leve = leve;
        this.addAttr = new ReadIntegerArrayEs(addAttrStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("leve:").append(leve).append(";");
        str.append("addAttr:").append(addAttr).append(";");
        return str.toString();
    }
}
