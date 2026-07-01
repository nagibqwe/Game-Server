/**
 * Auto generated, do not edit it
 *
 * HuaxingMagic配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_HuaxingMagic_Bean{
    /**
     * 模型ID
     */
    private final int id;
    /**
     * 模型ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 翅膀名称
     */
    private final String name;
    /**
     * 翅膀名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 相机镜头万分比
     */
    private final int camera_size;
    /**
     * 相机镜头万分比
     * @return
     */
    public final int getCamera_size(){
        return camera_size;
    }
    /**
     * 激活需要的物品
     */
    private final int active_item;
    /**
     * 激活需要的物品
     * @return
     */
    public final int getActive_item(){
        return active_item;
    }
    /**
     * 属性类型_激活属性_升星属性(@;@_@)
     */
    private final ReadIntegerArrayEs rent_att;
    /**
     * 属性类型_激活属性_升星属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getRent_att(){
        return rent_att;
    }
    /**
     * 升星需要的数量  阶数_数量(@;@_@)
     */
    private final ReadIntegerArrayEs star_itemnum;
    /**
     * 升星需要的数量  阶数_数量(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getStar_itemnum(){
        return star_itemnum;
    }

    public Cfg_HuaxingMagic_Bean(int id,String name,int camera_size,int active_item,String rent_attStr,String star_itemnumStr){
        this.id = id;
        this.name = name;
        this.camera_size = camera_size;
        this.active_item = active_item;
        this.rent_att = new ReadIntegerArrayEs(rent_attStr,"}",",");
        this.star_itemnum = new ReadIntegerArrayEs(star_itemnumStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("camera_size:").append(camera_size).append(";");
        str.append("active_item:").append(active_item).append(";");
        str.append("rent_att:").append(rent_att).append(";");
        str.append("star_itemnum:").append(star_itemnum).append(";");
        return str.toString();
    }
}
