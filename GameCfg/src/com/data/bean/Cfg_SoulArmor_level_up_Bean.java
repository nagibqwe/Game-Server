/**
 * Auto generated, do not edit it
 *
 * SoulArmor_level_up配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_SoulArmor_level_up_Bean{
    /**
     * 流水id
     */
    private final int id;
    /**
     * 流水id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 淬炼等级
     */
    private final int level;
    /**
     * 淬炼等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 升级消耗道具_数量
     */
    private final ReadIntegerArrayEs consume;
    /**
     * 升级消耗道具_数量
     * @return
     */
    public final ReadIntegerArrayEs getConsume(){
        return consume;
    }
    /**
     * 属性加成
     */
    private final ReadIntegerArrayEs levelValue;
    /**
     * 属性加成
     * @return
     */
    public final ReadIntegerArrayEs getLevelValue(){
        return levelValue;
    }
    /**
     * 特定等级额外属性（如+5）
     */
    private final ReadIntegerArrayEs extraValue;
    /**
     * 特定等级额外属性（如+5）
     * @return
     */
    public final ReadIntegerArrayEs getExtraValue(){
        return extraValue;
    }

    public Cfg_SoulArmor_level_up_Bean(int id,int level,String consumeStr,String levelValueStr,String extraValueStr){
        this.id = id;
        this.level = level;
        this.consume = new ReadIntegerArrayEs(consumeStr,"}",",");
        this.levelValue = new ReadIntegerArrayEs(levelValueStr,"}",",");
        this.extraValue = new ReadIntegerArrayEs(extraValueStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("level:").append(level).append(";");
        str.append("consume:").append(consume).append(";");
        str.append("levelValue:").append(levelValue).append(";");
        str.append("extraValue:").append(extraValue).append(";");
        return str.toString();
    }
}
