/**
 * Auto generated, do not edit it
 *
 * marry_childAtt配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Marry_childAtt_Bean{
    /**
     * 列表ID=itemCondition*100+stage
     */
    private final int Id;
    /**
     * 列表ID=itemCondition*100+stage
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 仙娃ID激活
对应child表中ID
     */
    private final int childId;
    /**
     * 仙娃ID激活
对应child表中ID
     * @return
     */
    public final int getChildId(){
        return childId;
    }
    /**
     * 仙娃的对应等级
     */
    private final int level;
    /**
     * 仙娃的对应等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 每升阶一次消耗的itemID和数量(@;@_@)
     */
    private final ReadIntegerArrayEs consume;
    /**
     * 每升阶一次消耗的itemID和数量(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getConsume(){
        return consume;
    }
    /**
     * 总祝福值
     */
    private final int blessingValue;
    /**
     * 总祝福值
     * @return
     */
    public final int getBlessingValue(){
        return blessingValue;
    }
    /**
     * 增加的属性(@;@_@)
     */
    private final ReadIntegerArrayEs attributes;
    /**
     * 增加的属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttributes(){
        return attributes;
    }

    public Cfg_Marry_childAtt_Bean(int Id,int childId,int level,String consumeStr,int blessingValue,String attributesStr){
        this.Id = Id;
        this.childId = childId;
        this.level = level;
        this.consume = new ReadIntegerArrayEs(consumeStr,"}",",");
        this.blessingValue = blessingValue;
        this.attributes = new ReadIntegerArrayEs(attributesStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("childId:").append(childId).append(";");
        str.append("level:").append(level).append(";");
        str.append("consume:").append(consume).append(";");
        str.append("blessingValue:").append(blessingValue).append(";");
        str.append("attributes:").append(attributes).append(";");
        return str.toString();
    }
}
