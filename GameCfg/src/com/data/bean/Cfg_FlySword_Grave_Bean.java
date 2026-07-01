/**
 * Auto generated, do not edit it
 *
 * FlySword_Grave配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_FlySword_Grave_Bean{
    /**
     * 剑冢的ID
     */
    private final int id;
    /**
     * 剑冢的ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 对应剑灵名字
     */
    private final String name;
    /**
     * 对应剑灵名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 剑的编号
     */
    private final int flysorwd_num;
    /**
     * 剑的编号
     * @return
     */
    public final int getFlysorwd_num(){
        return flysorwd_num;
    }
    /**
     * 剑灵的类型
     */
    private final int type;
    /**
     * 剑灵的类型
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 名剑ICON
     */
    private final int icon;
    /**
     * 名剑ICON
     * @return
     */
    public final int getIcon(){
        return icon;
    }
    /**
     * 背景描述
     */
    private final String describe;
    /**
     * 背景描述
     * @return
     */
    public final String getDescribe(){
        return describe;
    }
    /**
     * 技能展示的ID
     */
    private final int skill;
    /**
     * 技能展示的ID
     * @return
     */
    public final int getSkill(){
        return skill;
    }
    /**
     * 剑主试炼的条件
     */
    private final ReadIntegerArray condition;
    /**
     * 剑主试炼的条件
     * @return
     */
    public final ReadIntegerArray getCondition(){
        return condition;
    }
    /**
     * 副本ID
     */
    private final int clone_id;
    /**
     * 副本ID
     * @return
     */
    public final int getClone_id(){
        return clone_id;
    }
    /**
     * 激活的剑灵的ID
     */
    private final int FlySword_id;
    /**
     * 激活的剑灵的ID
     * @return
     */
    public final int getFlySword_id(){
        return FlySword_id;
    }
    /**
     * 黑屏的开始显示时间
     */
    private final int black_time;
    /**
     * 黑屏的开始显示时间
     * @return
     */
    public final int getBlack_time(){
        return black_time;
    }
    /**
     * 结算开始显示的时间
     */
    private final int jiesuan_time;
    /**
     * 结算开始显示的时间
     * @return
     */
    public final int getJiesuan_time(){
        return jiesuan_time;
    }

    public Cfg_FlySword_Grave_Bean(int id,String name,int flysorwd_num,int type,int icon,String describe,int skill,String conditionStr,int clone_id,int FlySword_id,int black_time,int jiesuan_time){
        this.id = id;
        this.name = name;
        this.flysorwd_num = flysorwd_num;
        this.type = type;
        this.icon = icon;
        this.describe = describe;
        this.skill = skill;
        this.condition = new ReadIntegerArray(conditionStr,",");
        this.clone_id = clone_id;
        this.FlySword_id = FlySword_id;
        this.black_time = black_time;
        this.jiesuan_time = jiesuan_time;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("flysorwd_num:").append(flysorwd_num).append(";");
        str.append("type:").append(type).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("describe:").append(describe).append(";");
        str.append("skill:").append(skill).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("clone_id:").append(clone_id).append(";");
        str.append("FlySword_id:").append(FlySword_id).append(";");
        str.append("black_time:").append(black_time).append(";");
        str.append("jiesuan_time:").append(jiesuan_time).append(";");
        return str.toString();
    }
}
