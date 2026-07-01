/**
 * Auto generated, do not edit it
 *
 * HuaxingFlySword_Advanced配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_HuaxingFlySword_Advanced_Bean{
    /**
     * 阶数ID
     */
    private final int id;
    /**
     * 阶数ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 当前属性
     */
    private final ReadIntegerArrayEs rent_att;
    /**
     * 当前属性
     * @return
     */
    public final ReadIntegerArrayEs getRent_att(){
        return rent_att;
    }
    /**
     * 当前系列剑灵总属性加成（万分比）
     */
    private final int att_all_add;
    /**
     * 当前系列剑灵总属性加成（万分比）
     * @return
     */
    public final int getAtt_all_add(){
        return att_all_add;
    }
    /**
     * 激活需要的物品_数量
     */
    private final ReadIntegerArrayEs active_item;
    /**
     * 激活需要的物品_数量
     * @return
     */
    public final ReadIntegerArrayEs getActive_item(){
        return active_item;
    }
    /**
     * 需要达到的剑灵等级
     */
    private final int levelmax;
    /**
     * 需要达到的剑灵等级
     * @return
     */
    public final int getLevelmax(){
        return levelmax;
    }

    public Cfg_HuaxingFlySword_Advanced_Bean(int id,String rent_attStr,int att_all_add,String active_itemStr,int levelmax){
        this.id = id;
        this.rent_att = new ReadIntegerArrayEs(rent_attStr,"}",",");
        this.att_all_add = att_all_add;
        this.active_item = new ReadIntegerArrayEs(active_itemStr,"}",",");
        this.levelmax = levelmax;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("rent_att:").append(rent_att).append(";");
        str.append("att_all_add:").append(att_all_add).append(";");
        str.append("active_item:").append(active_item).append(";");
        str.append("levelmax:").append(levelmax).append(";");
        return str.toString();
    }
}
