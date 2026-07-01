/**
 * Auto generated, do not edit it
 *
 * Cross_devil_card_Train配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Cross_devil_card_Train_Bean{
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
     * 所属阵营
对应Cross_devil_card_Camp表主键
     */
    private final int camp;
    /**
     * 所属阵营
对应Cross_devil_card_Camp表主键
     * @return
     */
    public final int getCamp(){
        return camp;
    }
    /**
     * 所属卡片
     */
    private final int card;
    /**
     * 所属卡片
     * @return
     */
    public final int getCard(){
        return card;
    }
    /**
     * 阶数
     */
    private final int rank;
    /**
     * 阶数
     * @return
     */
    public final int getRank(){
        return rank;
    }
    /**
     * 级数
     */
    private final int level;
    /**
     * 级数
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 属性类型对应attributeAdd表
     */
    private final ReadIntegerArrayEs att;
    /**
     * 属性类型对应attributeAdd表
     * @return
     */
    public final ReadIntegerArrayEs getAtt(){
        return att;
    }
    /**
     * 升级需要消耗的道具
     */
    private final ReadIntegerArrayEs condition;
    /**
     * 升级需要消耗的道具
     * @return
     */
    public final ReadIntegerArrayEs getCondition(){
        return condition;
    }

    public Cfg_Cross_devil_card_Train_Bean(int id,int camp,int card,int rank,int level,String attStr,String conditionStr){
        this.id = id;
        this.camp = camp;
        this.card = card;
        this.rank = rank;
        this.level = level;
        this.att = new ReadIntegerArrayEs(attStr,"}",",");
        this.condition = new ReadIntegerArrayEs(conditionStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("camp:").append(camp).append(";");
        str.append("card:").append(card).append(";");
        str.append("rank:").append(rank).append(";");
        str.append("level:").append(level).append(";");
        str.append("att:").append(att).append(";");
        str.append("condition:").append(condition).append(";");
        return str.toString();
    }
}
