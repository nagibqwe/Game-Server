/**
 * Auto generated, do not edit it
 *
 * fashion_link配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Fashion_link_Bean{
    /**
     * 羁绊ID
     */
    private final int id;
    /**
     * 羁绊ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 羁绊名称
     */
    private final String name;
    /**
     * 羁绊名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 需要的时装ID
     */
    private final ReadIntegerArrayEs need_fashion_id;
    /**
     * 需要的时装ID
     * @return
     */
    public final ReadIntegerArrayEs getNeed_fashion_id(){
        return need_fashion_id;
    }
    /**
     * 未激活完全时的属性(激活件数_属性枚举_属性值)
     */
    private final ReadIntegerArrayEs rent_att;
    /**
     * 未激活完全时的属性(激活件数_属性枚举_属性值)
     * @return
     */
    public final ReadIntegerArrayEs getRent_att(){
        return rent_att;
    }
    /**
     * 激活完全时的属性
     */
    private final ReadIntegerArrayEs activation_att;
    /**
     * 激活完全时的属性
     * @return
     */
    public final ReadIntegerArrayEs getActivation_att(){
        return activation_att;
    }
    /**
     * 每次提升之后增加的属性
     */
    private final ReadIntegerArrayEs star_att;
    /**
     * 每次提升之后增加的属性
     * @return
     */
    public final ReadIntegerArrayEs getStar_att(){
        return star_att;
    }

    public Cfg_Fashion_link_Bean(int id,String name,String need_fashion_idStr,String rent_attStr,String activation_attStr,String star_attStr){
        this.id = id;
        this.name = name;
        this.need_fashion_id = new ReadIntegerArrayEs(need_fashion_idStr,"}",",");
        this.rent_att = new ReadIntegerArrayEs(rent_attStr,"}",",");
        this.activation_att = new ReadIntegerArrayEs(activation_attStr,"}",",");
        this.star_att = new ReadIntegerArrayEs(star_attStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("need_fashion_id:").append(need_fashion_id).append(";");
        str.append("rent_att:").append(rent_att).append(";");
        str.append("activation_att:").append(activation_att).append(";");
        str.append("star_att:").append(star_att).append(";");
        return str.toString();
    }
}
