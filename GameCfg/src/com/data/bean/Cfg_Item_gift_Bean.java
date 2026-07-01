/**
 * Auto generated, do not edit it
 *
 * item_gift配置表
 */
package com.data.bean;

	
public class Cfg_Item_gift_Bean{
    /**
     * 物品ID
     */
    private final int id;
    /**
     * 物品ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 物品名字
     */
    private final String name;
    /**
     * 物品名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 物品类型(1：赠送材料；2：赠送礼物；3：仙侣赠送；)
     */
    private final int type;
    /**
     * 物品类型(1：赠送材料；2：赠送礼物；3：仙侣赠送；)
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 增加的亲密度
     */
    private final int qinmi;
    /**
     * 增加的亲密度
     * @return
     */
    public final int getQinmi(){
        return qinmi;
    }
    /**
     * 赠送后消耗or进背包(0,直接消耗；1，消耗后通过邮件进入对方背包）
     */
    private final int if_enter_bag;
    /**
     * 赠送后消耗or进背包(0,直接消耗；1，消耗后通过邮件进入对方背包）
     * @return
     */
    public final int getIf_enter_bag(){
        return if_enter_bag;
    }

    public Cfg_Item_gift_Bean(int id,String name,int type,int qinmi,int if_enter_bag){
        this.id = id;
        this.name = name;
        this.type = type;
        this.qinmi = qinmi;
        this.if_enter_bag = if_enter_bag;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("type:").append(type).append(";");
        str.append("qinmi:").append(qinmi).append(";");
        str.append("if_enter_bag:").append(if_enter_bag).append(";");
        return str.toString();
    }
}
