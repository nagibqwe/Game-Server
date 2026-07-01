/**
 * Auto generated, do not edit it
 *
 * pet配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Pet_Bean{
    /**
     * 流水号
     */
    private final int Id;
    /**
     * 流水号
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 宠物名字
     */
    private final String name;
    /**
     * 宠物名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 是否是时装（0，是；1不是：如果是时装，则不显示在化形界面中，只利用化形的出战等基础逻辑，不在化形中增加属性和技能）
     */
    private final int if_fashion;
    /**
     * 是否是时装（0，是；1不是：如果是时装，则不显示在化形界面中，只利用化形的出战等基础逻辑，不在化形中增加属性和技能）
     * @return
     */
    public final int getIf_fashion(){
        return if_fashion;
    }
    /**
     * 头像
     */
    private final int icon;
    /**
     * 头像
     * @return
     */
    public final int getIcon(){
        return icon;
    }
    /**
     * 宠物品质(：1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩)
     */
    private final int quality;
    /**
     * 宠物品质(：1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩)
     * @return
     */
    public final int getQuality(){
        return quality;
    }
    /**
     * 外观ID
     */
    private final int model;
    /**
     * 外观ID
     * @return
     */
    public final int getModel(){
        return model;
    }
    /**
     * 在场景上的缩放值(百分比)
     */
    private final int scene_scale;
    /**
     * 在场景上的缩放值(百分比)
     * @return
     */
    public final int getScene_scale(){
        return scene_scale;
    }
    /**
     * 在ui上的缩放值
     */
    private final int ui_scale;
    /**
     * 在ui上的缩放值
     * @return
     */
    public final int getUi_scale(){
        return ui_scale;
    }
    /**
     * 受击半径（厘米）
     */
    private final int strike_distance;
    /**
     * 受击半径（厘米）
     * @return
     */
    public final int getStrike_distance(){
        return strike_distance;
    }
    /**
     * 解锁条件(类型ID_类型参数；0.任务；1.前置满阶；2.道具；3.宠物功能开启获得；(@;@_@))
     */
    private final ReadIntegerArrayEs unlock;
    /**
     * 解锁条件(类型ID_类型参数；0.任务；1.前置满阶；2.道具；3.宠物功能开启获得；(@;@_@))
     * @return
     */
    public final ReadIntegerArrayEs getUnlock(){
        return unlock;
    }
    /**
     * 是否拥有装备栏（0.无；1.有）
     */
    private final int equip;
    /**
     * 是否拥有装备栏（0.无；1.有）
     * @return
     */
    public final int getEquip(){
        return equip;
    }
    /**
     * 普攻技能ID
     */
    private final int baseskill;
    /**
     * 普攻技能ID
     * @return
     */
    public final int getBaseskill(){
        return baseskill;
    }
    /**
     * 宠物附带战斗技能
     */
    private final int pet_skill;
    /**
     * 宠物附带战斗技能
     * @return
     */
    public final int getPet_skill(){
        return pet_skill;
    }
    /**
     * 初始属性：属性类型_数值，属性类型1_数值，(@;@_@)
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 初始属性：属性类型_数值，属性类型1_数值，(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 满阶阶数，超过之后显示为晋升
     */
    private final int full_degress;
    /**
     * 满阶阶数，超过之后显示为晋升
     * @return
     */
    public final int getFull_degress(){
        return full_degress;
    }
    /**
     * 最大阶数
     */
    private final int max_degree;
    /**
     * 最大阶数
     * @return
     */
    public final int getMax_degree(){
        return max_degree;
    }
    /**
     * 0表示不显示装备，1显示人物装备2显示宠物装备，3显示坐骑装备
     */
    private final int isShow;
    /**
     * 0表示不显示装备，1显示人物装备2显示宠物装备，3显示坐骑装备
     * @return
     */
    public final int getIsShow(){
        return isShow;
    }

    public Cfg_Pet_Bean(int Id,String name,int if_fashion,int icon,int quality,int model,int scene_scale,int ui_scale,int strike_distance,String unlockStr,int equip,int baseskill,int pet_skill,String attributeStr,int full_degress,int max_degree,int isShow){
        this.Id = Id;
        this.name = name;
        this.if_fashion = if_fashion;
        this.icon = icon;
        this.quality = quality;
        this.model = model;
        this.scene_scale = scene_scale;
        this.ui_scale = ui_scale;
        this.strike_distance = strike_distance;
        this.unlock = new ReadIntegerArrayEs(unlockStr,"}",",");
        this.equip = equip;
        this.baseskill = baseskill;
        this.pet_skill = pet_skill;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.full_degress = full_degress;
        this.max_degree = max_degree;
        this.isShow = isShow;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("name:").append(name).append(";");
        str.append("if_fashion:").append(if_fashion).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("quality:").append(quality).append(";");
        str.append("model:").append(model).append(";");
        str.append("scene_scale:").append(scene_scale).append(";");
        str.append("ui_scale:").append(ui_scale).append(";");
        str.append("strike_distance:").append(strike_distance).append(";");
        str.append("unlock:").append(unlock).append(";");
        str.append("equip:").append(equip).append(";");
        str.append("baseskill:").append(baseskill).append(";");
        str.append("pet_skill:").append(pet_skill).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("full_degress:").append(full_degress).append(";");
        str.append("max_degree:").append(max_degree).append(";");
        str.append("isShow:").append(isShow).append(";");
        return str.toString();
    }
}
