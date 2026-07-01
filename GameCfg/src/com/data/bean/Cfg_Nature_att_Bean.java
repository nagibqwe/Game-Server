/**
 * Auto generated, do not edit it
 *
 * Nature_att配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Nature_att_Bean{
    /**
     * ID（类型*1000+位置*100+阶级）
     */
    private final int id;
    /**
     * ID（类型*1000+位置*100+阶级）
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 神物类型(1.坐骑，2.翅膀，3.法器，4.阵道，5.神兵 6.法宝 8.宠物）
     */
    private final int type;
    /**
     * 神物类型(1.坐骑，2.翅膀，3.法器，4.阵道，5.神兵 6.法宝 8.宠物）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 位置（第几个物品）
     */
    private final int Position;
    /**
     * 位置（第几个物品）
     * @return
     */
    public final int getPosition(){
        return Position;
    }
    /**
     * 等阶
     */
    private final int level;
    /**
     * 等阶
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 物品ID
     */
    private final int item_id;
    /**
     * 物品ID
     * @return
     */
    public final int getItem_id(){
        return item_id;
    }
    /**
     * 增加属性(@;@_@)
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 增加属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 增加对应系统培养属性万分比(@_@)
     */
    private final ReadIntegerArray peiyang_att;
    /**
     * 增加对应系统培养属性万分比(@_@)
     * @return
     */
    public final ReadIntegerArray getPeiyang_att(){
        return peiyang_att;
    }
    /**
     * 等级数量上限
     */
    private final int leve_limit;
    /**
     * 等级数量上限
     * @return
     */
    public final int getLeve_limit(){
        return leve_limit;
    }

    public Cfg_Nature_att_Bean(int id,int type,int Position,int level,int item_id,String attributeStr,String peiyang_attStr,int leve_limit){
        this.id = id;
        this.type = type;
        this.Position = Position;
        this.level = level;
        this.item_id = item_id;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.peiyang_att = new ReadIntegerArray(peiyang_attStr,",");
        this.leve_limit = leve_limit;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("Position:").append(Position).append(";");
        str.append("level:").append(level).append(";");
        str.append("item_id:").append(item_id).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("peiyang_att:").append(peiyang_att).append(";");
        str.append("leve_limit:").append(leve_limit).append(";");
        return str.toString();
    }
}
