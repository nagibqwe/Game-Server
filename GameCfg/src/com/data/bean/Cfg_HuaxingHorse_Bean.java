/**
 * Auto generated, do not edit it
 *
 * HuaxingHorse配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_HuaxingHorse_Bean{
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
     * 排序
     */
    private final int order;
    /**
     * 排序
     * @return
     */
    public final int getOrder(){
        return order;
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
     * 激活需要的条件（0物品激活，1服务器激活）
     */
    private final int active_condition;
    /**
     * 激活需要的条件（0物品激活，1服务器激活）
     * @return
     */
    public final int getActive_condition(){
        return active_condition;
    }
    /**
     * 摄像机Y轴高度（单位厘米）
     */
    private final int scene_camera_y_add;
    /**
     * 摄像机Y轴高度（单位厘米）
     * @return
     */
    public final int getScene_camera_y_add(){
        return scene_camera_y_add;
    }
    /**
     * 场景中对于摄像机跟随距离的加成(单位厘米)
     */
    private final int scene_camera_disadd;
    /**
     * 场景中对于摄像机跟随距离的加成(单位厘米)
     * @return
     */
    public final int getScene_camera_disadd(){
        return scene_camera_disadd;
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
    /**
     * 激活学习的技能
     */
    private final int Passive_skill;
    /**
     * 激活学习的技能
     * @return
     */
    public final int getPassive_skill(){
        return Passive_skill;
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
    /**
     * 场景中对于摄像机pitch加成(单位角度)
     */
    private final int scene_camera_pitchadd;
    /**
     * 场景中对于摄像机pitch加成(单位角度)
     * @return
     */
    public final int getScene_camera_pitchadd(){
        return scene_camera_pitchadd;
    }
    /**
     * 是否可以在坐骑上打坐（0不可以，1可以）
     */
    private final int can_sit_down;
    /**
     * 是否可以在坐骑上打坐（0不可以，1可以）
     * @return
     */
    public final int getCan_sit_down(){
        return can_sit_down;
    }

    public Cfg_HuaxingHorse_Bean(int id,String name,int order,int if_fashion,int active_condition,int scene_camera_y_add,int scene_camera_disadd,int active_item,String rent_attStr,String star_itemnumStr,int Passive_skill,int isShow,int scene_camera_pitchadd,int can_sit_down){
        this.id = id;
        this.name = name;
        this.order = order;
        this.if_fashion = if_fashion;
        this.active_condition = active_condition;
        this.scene_camera_y_add = scene_camera_y_add;
        this.scene_camera_disadd = scene_camera_disadd;
        this.active_item = active_item;
        this.rent_att = new ReadIntegerArrayEs(rent_attStr,"}",",");
        this.star_itemnum = new ReadIntegerArrayEs(star_itemnumStr,"}",",");
        this.Passive_skill = Passive_skill;
        this.isShow = isShow;
        this.scene_camera_pitchadd = scene_camera_pitchadd;
        this.can_sit_down = can_sit_down;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("order:").append(order).append(";");
        str.append("if_fashion:").append(if_fashion).append(";");
        str.append("active_condition:").append(active_condition).append(";");
        str.append("scene_camera_y_add:").append(scene_camera_y_add).append(";");
        str.append("scene_camera_disadd:").append(scene_camera_disadd).append(";");
        str.append("active_item:").append(active_item).append(";");
        str.append("rent_att:").append(rent_att).append(";");
        str.append("star_itemnum:").append(star_itemnum).append(";");
        str.append("Passive_skill:").append(Passive_skill).append(";");
        str.append("isShow:").append(isShow).append(";");
        str.append("scene_camera_pitchadd:").append(scene_camera_pitchadd).append(";");
        str.append("can_sit_down:").append(can_sit_down).append(";");
        return str.toString();
    }
}
