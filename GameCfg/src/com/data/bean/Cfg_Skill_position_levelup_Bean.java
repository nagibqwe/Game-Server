/**
 * Auto generated, do not edit it
 *
 * skill_position_levelup配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Skill_position_levelup_Bean{
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
     * 心法升级铜币消耗
     */
    private final int money;
    /**
     * 心法升级铜币消耗
     * @return
     */
    public final int getMoney(){
        return money;
    }
    /**
     * 心法增加的属性
     */
    private final ReadIntegerArrayEs att;
    /**
     * 心法增加的属性
     * @return
     */
    public final ReadIntegerArrayEs getAtt(){
        return att;
    }
    /**
     * 心法增加的属性
     */
    private final ReadIntegerArrayEs att_vip;
    /**
     * 心法增加的属性
     * @return
     */
    public final ReadIntegerArrayEs getAtt_vip(){
        return att_vip;
    }

    public Cfg_Skill_position_levelup_Bean(int id,int money,String attStr,String att_vipStr){
        this.id = id;
        this.money = money;
        this.att = new ReadIntegerArrayEs(attStr,"}",",");
        this.att_vip = new ReadIntegerArrayEs(att_vipStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("money:").append(money).append(";");
        str.append("att:").append(att).append(";");
        str.append("att_vip:").append(att_vip).append(";");
        return str.toString();
    }
}
