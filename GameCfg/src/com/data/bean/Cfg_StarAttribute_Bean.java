/**
 * Auto generated, do not edit it
 *
 * StarAttribute配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_StarAttribute_Bean{
    /**
     * 激活需要达到的星级
     */
    private final int ID;
    /**
     * 激活需要达到的星级
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 属性(万分比)(@;@_@)
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 属性(万分比)(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 是否公告（0不公告；1公告）
     */
    private final int radio;
    /**
     * 是否公告（0不公告；1公告）
     * @return
     */
    public final int getRadio(){
        return radio;
    }

    public Cfg_StarAttribute_Bean(int ID,String attributeStr,int radio){
        this.ID = ID;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.radio = radio;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("radio:").append(radio).append(";");
        return str.toString();
    }
}
