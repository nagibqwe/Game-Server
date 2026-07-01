/**
 * Auto generated, do not edit it
 *
 * SoulArmor_signet_lottery配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_SoulArmor_signet_lottery_Bean{
    /**
     * 流水id=类型*1000+祈灵等级
     */
    private final int id;
    /**
     * 流水id=类型*1000+祈灵等级
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 抽奖类型：1地灵回响，2天灵回响，3洪荒回响
     */
    private final int type;
    /**
     * 抽奖类型：1地灵回响，2天灵回响，3洪荒回响
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 祈灵等级
     */
    private final int level;
    /**
     * 祈灵等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 升级所需祈灵经验
     */
    private final int levelUPExp;
    /**
     * 升级所需祈灵经验
     * @return
     */
    public final int getLevelUPExp(){
        return levelUPExp;
    }
    /**
     * 该等级最高可抽取品质
（前端显示用）(：1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩)
     */
    private final int items;
    /**
     * 该等级最高可抽取品质
（前端显示用）(：1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩)
     * @return
     */
    public final int getItems(){
        return items;
    }
    /**
     * 该等级最高可抽取装备星级
（前端显示用）
     */
    private final int star;
    /**
     * 该等级最高可抽取装备星级
（前端显示用）
     * @return
     */
    public final int getStar(){
        return star;
    }
    /**
     * 抽出各个品质的权重.（实际能抽到的部位、星级在掉落中配置）
格式：品质_权重（十万）_掉落组id
     */
    private final ReadIntegerArrayEs probability;
    /**
     * 抽出各个品质的权重.（实际能抽到的部位、星级在掉落中配置）
格式：品质_权重（十万）_掉落组id
     * @return
     */
    public final ReadIntegerArrayEs getProbability(){
        return probability;
    }

    public Cfg_SoulArmor_signet_lottery_Bean(int id,int type,int level,int levelUPExp,int items,int star,String probabilityStr){
        this.id = id;
        this.type = type;
        this.level = level;
        this.levelUPExp = levelUPExp;
        this.items = items;
        this.star = star;
        this.probability = new ReadIntegerArrayEs(probabilityStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("level:").append(level).append(";");
        str.append("levelUPExp:").append(levelUPExp).append(";");
        str.append("items:").append(items).append(";");
        str.append("star:").append(star).append(";");
        str.append("probability:").append(probability).append(";");
        return str.toString();
    }
}
