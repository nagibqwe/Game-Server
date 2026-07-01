/**
 * Auto generated, do not edit it
 *
 * HuaxingFlySword_skill配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_HuaxingFlySword_skill_Bean{
    /**
     * ID
     */
    private final int id;
    /**
     * ID
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
     * 激活学习的技能
     */
    private final int passive_skill;
    /**
     * 激活学习的技能
     * @return
     */
    public final int getPassive_skill(){
        return passive_skill;
    }
    /**
     * 需要的剑灵培养方式（1，指定剑灵需要的等级；2，指定数量的剑灵达到阶级）
     */
    private final int type;
    /**
     * 需要的剑灵培养方式（1，指定剑灵需要的等级；2，指定数量的剑灵达到阶级）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 激活参数
     */
    private final ReadIntegerArray active_pram;
    /**
     * 激活参数
     * @return
     */
    public final ReadIntegerArray getActive_pram(){
        return active_pram;
    }
    /**
     * 描述
     */
    private final String des;
    /**
     * 描述
     * @return
     */
    public final String getDes(){
        return des;
    }

    public Cfg_HuaxingFlySword_skill_Bean(int id,String name,int passive_skill,int type,String active_pramStr,String des){
        this.id = id;
        this.name = name;
        this.passive_skill = passive_skill;
        this.type = type;
        this.active_pram = new ReadIntegerArray(active_pramStr,",");
        this.des = des;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("passive_skill:").append(passive_skill).append(";");
        str.append("type:").append(type).append(";");
        str.append("active_pram:").append(active_pram).append(";");
        str.append("des:").append(des).append(";");
        return str.toString();
    }
}
