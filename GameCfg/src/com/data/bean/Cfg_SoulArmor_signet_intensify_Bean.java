/**
 * Auto generated, do not edit it
 *
 * SoulArmor_signet_intensify配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_SoulArmor_signet_intensify_Bean{
    /**
     * 流水id
部位*10000+等级
     */
    private final int id;
    /**
     * 流水id
部位*10000+等级
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 部位编号
     */
    private final int part;
    /**
     * 部位编号
     * @return
     */
    public final int getPart(){
        return part;
    }
    /**
     * 魂印等级
     */
    private final int level;
    /**
     * 魂印等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 强化消耗道具id_数量
     */
    private final ReadIntegerArrayEs consume;
    /**
     * 强化消耗道具id_数量
     * @return
     */
    public final ReadIntegerArrayEs getConsume(){
        return consume;
    }
    /**
     * 等级属性加成
     */
    private final ReadIntegerArrayEs value;
    /**
     * 等级属性加成
     * @return
     */
    public final ReadIntegerArrayEs getValue(){
        return value;
    }

    public Cfg_SoulArmor_signet_intensify_Bean(int id,int part,int level,String consumeStr,String valueStr){
        this.id = id;
        this.part = part;
        this.level = level;
        this.consume = new ReadIntegerArrayEs(consumeStr,"}",",");
        this.value = new ReadIntegerArrayEs(valueStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("part:").append(part).append(";");
        str.append("level:").append(level).append(";");
        str.append("consume:").append(consume).append(";");
        str.append("value:").append(value).append(";");
        return str.toString();
    }
}
