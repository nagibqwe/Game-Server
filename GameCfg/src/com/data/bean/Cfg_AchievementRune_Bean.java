/**
 * Auto generated, do not edit it
 *
 * AchievementRune配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_AchievementRune_Bean{
    /**
     * key值
     */
    private final int id;
    /**
     * key值
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 激活一次所需成就积分
     */
    private final int Need;
    /**
     * 激活一次所需成就积分
     * @return
     */
    public final int getNeed(){
        return Need;
    }
    /**
     * 升级所需经验
     */
    private final int upNeedExp;
    /**
     * 升级所需经验
     * @return
     */
    public final int getUpNeedExp(){
        return upNeedExp;
    }
    /**
     * 基础成长属性(属性类型_增加的值)(@;@_@)
     */
    private final ReadIntegerArrayEs att;
    /**
     * 基础成长属性(属性类型_增加的值)(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAtt(){
        return att;
    }
    /**
     * 普通激活获得
     */
    private final int normalGet;
    /**
     * 普通激活获得
     * @return
     */
    public final int getNormalGet(){
        return normalGet;
    }
    /**
     * 暴击获得
     */
    private final int critGet;
    /**
     * 暴击获得
     * @return
     */
    public final int getCritGet(){
        return critGet;
    }
    /**
     * 超级暴击获得
     */
    private final int superGet;
    /**
     * 超级暴击获得
     * @return
     */
    public final int getSuperGet(){
        return superGet;
    }
    /**
     * 暴击概率万分比
     */
    private final int critPer;
    /**
     * 暴击概率万分比
     * @return
     */
    public final int getCritPer(){
        return critPer;
    }
    /**
     * 超级暴击概率万分比
     */
    private final int superPer;
    /**
     * 超级暴击概率万分比
     * @return
     */
    public final int getSuperPer(){
        return superPer;
    }
    /**
     * 没有免费次数扣除所需元宝数
     */
    private final int decGold;
    /**
     * 没有免费次数扣除所需元宝数
     * @return
     */
    public final int getDecGold(){
        return decGold;
    }
    /**
     * 每隔多少次增加元宝数量
     */
    private final int eachCount;
    /**
     * 每隔多少次增加元宝数量
     * @return
     */
    public final int getEachCount(){
        return eachCount;
    }
    /**
     * 每次增加数量
     */
    private final int eachAdd;
    /**
     * 每次增加数量
     * @return
     */
    public final int getEachAdd(){
        return eachAdd;
    }
    /**
     * 扣除上线
     */
    private final int limitDecGold;
    /**
     * 扣除上线
     * @return
     */
    public final int getLimitDecGold(){
        return limitDecGold;
    }

    public Cfg_AchievementRune_Bean(int id,int Need,int upNeedExp,String attStr,int normalGet,int critGet,int superGet,int critPer,int superPer,int decGold,int eachCount,int eachAdd,int limitDecGold){
        this.id = id;
        this.Need = Need;
        this.upNeedExp = upNeedExp;
        this.att = new ReadIntegerArrayEs(attStr,"}",",");
        this.normalGet = normalGet;
        this.critGet = critGet;
        this.superGet = superGet;
        this.critPer = critPer;
        this.superPer = superPer;
        this.decGold = decGold;
        this.eachCount = eachCount;
        this.eachAdd = eachAdd;
        this.limitDecGold = limitDecGold;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("Need:").append(Need).append(";");
        str.append("upNeedExp:").append(upNeedExp).append(";");
        str.append("att:").append(att).append(";");
        str.append("normalGet:").append(normalGet).append(";");
        str.append("critGet:").append(critGet).append(";");
        str.append("superGet:").append(superGet).append(";");
        str.append("critPer:").append(critPer).append(";");
        str.append("superPer:").append(superPer).append(";");
        str.append("decGold:").append(decGold).append(";");
        str.append("eachCount:").append(eachCount).append(";");
        str.append("eachAdd:").append(eachAdd).append(";");
        str.append("limitDecGold:").append(limitDecGold).append(";");
        return str.toString();
    }
}
