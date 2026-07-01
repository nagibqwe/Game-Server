/**
 * Auto generated, do not edit it
 *
 * NatureWeapon配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_NatureWeapon_Bean{
    /**
     * 翅膀等级
     */
    private final int id;
    /**
     * 翅膀等级
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 名称
     */
    private final String name;
    /**
     * 名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 强化进度
     */
    private final int progress;
    /**
     * 强化进度
     * @return
     */
    public final int getProgress(){
        return progress;
    }
    /**
     * 剩余总经验client ignore
     */
    private final int last_exp;
    /**
     * 剩余总经验client ignore
     * @return
     */
    public final int getLast_exp(){
        return last_exp;
    }
    /**
     * 本级属性(@;@_@)
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 本级属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 激活技能(@_@)
     */
    private final ReadIntegerArray skill;
    /**
     * 激活技能(@_@)
     * @return
     */
    public final ReadIntegerArray getSkill(){
        return skill;
    }
    /**
     * 升阶物品id(@_@)
     */
    private final ReadIntegerArray up_item;
    /**
     * 升阶物品id(@_@)
     * @return
     */
    public final ReadIntegerArray getUp_item(){
        return up_item;
    }
    /**
     * 是否激活新外观的外观ID
     */
    private final ReadIntegerArray ModelID;
    /**
     * 是否激活新外观的外观ID
     * @return
     */
    public final ReadIntegerArray getModelID(){
        return ModelID;
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

    public Cfg_NatureWeapon_Bean(int id,String name,int progress,int last_exp,String attributeStr,String skillStr,String up_itemStr,String ModelIDStr,int camera_size){
        this.id = id;
        this.name = name;
        this.progress = progress;
        this.last_exp = last_exp;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.skill = new ReadIntegerArray(skillStr,",");
        this.up_item = new ReadIntegerArray(up_itemStr,",");
        this.ModelID = new ReadIntegerArray(ModelIDStr,",");
        this.camera_size = camera_size;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("progress:").append(progress).append(";");
        str.append("last_exp:").append(last_exp).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("skill:").append(skill).append(";");
        str.append("up_item:").append(up_item).append(";");
        str.append("ModelID:").append(ModelID).append(";");
        str.append("camera_size:").append(camera_size).append(";");
        return str.toString();
    }
}
