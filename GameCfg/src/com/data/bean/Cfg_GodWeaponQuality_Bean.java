/**
 * Auto generated, do not edit it
 *
 * GodWeaponQuality配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_GodWeaponQuality_Bean{
    /**
     * ID,基础配置表中GROUP参数*100+Quality
     */
    private final int GroupId;
    /**
     * ID,基础配置表中GROUP参数*100+Quality
     * @return
     */
    public final int getGroupId(){
        return GroupId;
    }
    /**
     * 道具ID,道具数量(@;@_@)
     */
    private final ReadIntegerArrayEs Item;
    /**
     * 道具ID,道具数量(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getItem(){
        return Item;
    }
    /**
     * 品质
     */
    private final int Quality;
    /**
     * 品质
     * @return
     */
    public final int getQuality(){
        return Quality;
    }
    /**
     * 每一级增加的当前模具的基础属性十万分比
     */
    private final int AddAttribute;
    /**
     * 每一级增加的当前模具的基础属性十万分比
     * @return
     */
    public final int getAddAttribute(){
        return AddAttribute;
    }
    /**
     * 品质图片
     */
    private final int QualityIcon;
    /**
     * 品质图片
     * @return
     */
    public final int getQualityIcon(){
        return QualityIcon;
    }
    /**
     * 开启对应升级表最大等级
     */
    private final int MaxLevel;
    /**
     * 开启对应升级表最大等级
     * @return
     */
    public final int getMaxLevel(){
        return MaxLevel;
    }

    public Cfg_GodWeaponQuality_Bean(int GroupId,String ItemStr,int Quality,int AddAttribute,int QualityIcon,int MaxLevel){
        this.GroupId = GroupId;
        this.Item = new ReadIntegerArrayEs(ItemStr,"}",",");
        this.Quality = Quality;
        this.AddAttribute = AddAttribute;
        this.QualityIcon = QualityIcon;
        this.MaxLevel = MaxLevel;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("GroupId:").append(GroupId).append(";");
        str.append("Item:").append(Item).append(";");
        str.append("Quality:").append(Quality).append(";");
        str.append("AddAttribute:").append(AddAttribute).append(";");
        str.append("QualityIcon:").append(QualityIcon).append(";");
        str.append("MaxLevel:").append(MaxLevel).append(";");
        return str.toString();
    }
}
