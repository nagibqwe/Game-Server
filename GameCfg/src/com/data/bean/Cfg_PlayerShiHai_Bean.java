/**
 * Auto generated, do not edit it
 *
 * PlayerShiHai配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_PlayerShiHai_Bean{
    /**
     * 境界等级
     */
    private final int id;
    /**
     * 境界等级
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 境界名称
     */
    private final String name;
    /**
     * 境界名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 战斗力
     */
    private final int fight_power;
    /**
     * 战斗力
     * @return
     */
    public final int getFight_power(){
        return fight_power;
    }
    /**
     * 当前属性(@;@_@)
     */
    private final ReadIntegerArrayEs cur_attribute;
    /**
     * 当前属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getCur_attribute(){
        return cur_attribute;
    }
    /**
     * 升级增加的属性(@;@_@)
     */
    private final ReadIntegerArrayEs add_attribute;
    /**
     * 升级增加的属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAdd_attribute(){
        return add_attribute;
    }
    /**
     * 升级需要通关万妖塔的层数
     */
    private final int need_copy_level;
    /**
     * 升级需要通关万妖塔的层数
     * @return
     */
    public final int getNeed_copy_level(){
        return need_copy_level;
    }
    /**
     * 升级需要的道具(@;@_@)
     */
    private final ReadIntegerArrayEs need_item;
    /**
     * 升级需要的道具(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getNeed_item(){
        return need_item;
    }

    public Cfg_PlayerShiHai_Bean(int id,String name,int fight_power,String cur_attributeStr,String add_attributeStr,int need_copy_level,String need_itemStr){
        this.id = id;
        this.name = name;
        this.fight_power = fight_power;
        this.cur_attribute = new ReadIntegerArrayEs(cur_attributeStr,"}",",");
        this.add_attribute = new ReadIntegerArrayEs(add_attributeStr,"}",",");
        this.need_copy_level = need_copy_level;
        this.need_item = new ReadIntegerArrayEs(need_itemStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("fight_power:").append(fight_power).append(";");
        str.append("cur_attribute:").append(cur_attribute).append(";");
        str.append("add_attribute:").append(add_attribute).append(";");
        str.append("need_copy_level:").append(need_copy_level).append(";");
        str.append("need_item:").append(need_item).append(";");
        return str.toString();
    }
}
