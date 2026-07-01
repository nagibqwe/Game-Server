/**
 * Auto generated, do not edit it
 *
 * synthetic配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Synthetic_Bean{
    /**
     * ID
     */
    private final int ID;
    /**
     * ID
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 目标物品(物品id_个数_10000;物品2id_个数_机率……(@;@_@)
     */
    private final ReadIntegerArrayEs target_id;
    /**
     * 目标物品(物品id_个数_10000;物品2id_个数_机率……(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getTarget_id(){
        return target_id;
    }
    /**
     * 成功机率(万分比)
     */
    private final int probability;
    /**
     * 成功机率(万分比)
     * @return
     */
    public final int getProbability(){
        return probability;
    }
    /**
     * 金币
     */
    private final int money;
    /**
     * 金币
     * @return
     */
    public final int getMoney(){
        return money;
    }
    /**
     * 元宝
     */
    private final int gold;
    /**
     * 元宝
     * @return
     */
    public final int getGold(){
        return gold;
    }
    /**
     * 其他货币材料消耗(货币id_数量;货币2id_数量……或的关系,消耗其中一种货币
     */
    private final ReadIntegerArrayEs currency;
    /**
     * 其他货币材料消耗(货币id_数量;货币2id_数量……或的关系,消耗其中一种货币
     * @return
     */
    public final ReadIntegerArrayEs getCurrency(){
        return currency;
    }
    /**
     * 道具材料消耗(材料id_数量_绑或不绑;材料2id_数量__绑或不绑……*绑定id:1,非绑id:0,无特定需求id:2) *客户端限定为4个(@;@_@)
     */
    private final ReadIntegerArrayEs props;
    /**
     * 道具材料消耗(材料id_数量_绑或不绑;材料2id_数量__绑或不绑……*绑定id:1,非绑id:0,无特定需求id:2) *客户端限定为4个(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getProps(){
        return props;
    }
    /**
     * 是否公告（0不公告；1公告）
     */
    private final int radio;
    /**
     * 是否公告（0不公告；1公告）
     * @return
     */
    public final int getRadio(){
        return radio;
    }

    public Cfg_Synthetic_Bean(int ID,String target_idStr,int probability,int money,int gold,String currencyStr,String propsStr,int radio){
        this.ID = ID;
        this.target_id = new ReadIntegerArrayEs(target_idStr,"}",",");
        this.probability = probability;
        this.money = money;
        this.gold = gold;
        this.currency = new ReadIntegerArrayEs(currencyStr,"}",",");
        this.props = new ReadIntegerArrayEs(propsStr,"}",",");
        this.radio = radio;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("target_id:").append(target_id).append(";");
        str.append("probability:").append(probability).append(";");
        str.append("money:").append(money).append(";");
        str.append("gold:").append(gold).append(";");
        str.append("currency:").append(currency).append(";");
        str.append("props:").append(props).append(";");
        str.append("radio:").append(radio).append(";");
        return str.toString();
    }
}
