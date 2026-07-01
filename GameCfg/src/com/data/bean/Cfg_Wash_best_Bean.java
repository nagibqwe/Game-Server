/**
 * Auto generated, do not edit it
 *
 * wash_best配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Wash_best_Bean{
    /**
     * 流水号
     */
    private final int Id;
    /**
     * 流水号
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 装备部位(0头盔.1武器.2胸甲.3项链.4腰带.5腿甲.6鞋子.7戒指)
     */
    private final int type;
    /**
     * 装备部位(0头盔.1武器.2胸甲.3项链.4腰带.5腿甲.6鞋子.7戒指)
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 条件:祝福值达到保底
     */
    private final int condition;
    /**
     * 条件:祝福值达到保底
     * @return
     */
    public final int getCondition(){
        return condition;
    }
    /**
     * 属性：属性类型，值；（@;@_@）
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 属性：属性类型，值；（@;@_@）
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 部位开启洗练等级
     */
    private final int level_limit;
    /**
     * 部位开启洗练等级
     * @return
     */
    public final int getLevel_limit(){
        return level_limit;
    }

    public Cfg_Wash_best_Bean(int Id,int type,int condition,String attributeStr,int level_limit){
        this.Id = Id;
        this.type = type;
        this.condition = condition;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.level_limit = level_limit;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("type:").append(type).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("level_limit:").append(level_limit).append(";");
        return str.toString();
    }
}
