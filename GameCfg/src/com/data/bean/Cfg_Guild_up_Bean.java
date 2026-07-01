/**
 * Auto generated, do not edit it
 *
 * guild_up配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Guild_up_Bean{
    /**
     * key=type*10000+level
     */
    private final int id;
    /**
     * key=type*10000+level
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 建筑类型（1大厅，2商店，3驻地，4日常， 5福利所）
     */
    private final int type;
    /**
     * 建筑类型（1大厅，2商店，3驻地，4日常， 5福利所）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 建筑等级
     */
    private final int level;
    /**
     * 建筑等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 公会基地对应加入玩家数量上限
     */
    private final int base_num;
    /**
     * 公会基地对应加入玩家数量上限
     * @return
     */
    public final int getBase_num(){
        return base_num;
    }
    /**
     * 答题基数（数量）
     */
    private final int question_reward;
    /**
     * 答题基数（数量）
     * @return
     */
    public final int getQuestion_reward(){
        return question_reward;
    }
    /**
     * 维修基金
     */
    private final int maintenance_fund;
    /**
     * 维修基金
     * @return
     */
    public final int getMaintenance_fund(){
        return maintenance_fund;
    }
    /**
     * 升级所需建设度
     */
    private final int needNum;
    /**
     * 升级所需建设度
     * @return
     */
    public final int getNeedNum(){
        return needNum;
    }
    /**
     * 升级带来buff的id
     */
    private final int level_buff;
    /**
     * 升级带来buff的id
     * @return
     */
    public final int getLevel_buff(){
        return level_buff;
    }
    /**
     * 升级所需其他建筑相关(@;@_@)
     */
    private final ReadIntegerArrayEs other;
    /**
     * 升级所需其他建筑相关(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getOther(){
        return other;
    }

    public Cfg_Guild_up_Bean(int id,int type,int level,int base_num,int question_reward,int maintenance_fund,int needNum,int level_buff,String otherStr){
        this.id = id;
        this.type = type;
        this.level = level;
        this.base_num = base_num;
        this.question_reward = question_reward;
        this.maintenance_fund = maintenance_fund;
        this.needNum = needNum;
        this.level_buff = level_buff;
        this.other = new ReadIntegerArrayEs(otherStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("level:").append(level).append(";");
        str.append("base_num:").append(base_num).append(";");
        str.append("question_reward:").append(question_reward).append(";");
        str.append("maintenance_fund:").append(maintenance_fund).append(";");
        str.append("needNum:").append(needNum).append(";");
        str.append("level_buff:").append(level_buff).append(";");
        str.append("other:").append(other).append(";");
        return str.toString();
    }
}
