/**
 * Auto generated, do not edit it
 *
 * NewRedPacket配置表
 */
package com.data.bean;

	
public class Cfg_NewRedPacket_Bean{
    /**
     * 红包ID
     */
    private final int Id;
    /**
     * 红包ID
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 获得红包的原因1.首充奖励；2.每日充值；3.月卡；4.至尊月卡；5.成长基金6.巅峰基金；7.升级至VIP1；8.升级至VIP2；9.升级至VIP3；10.升级VIP4；11.升级VIP5；12.升级VIP6；13.升级VIP7；14.升级VIP8；15.升级VIP9；16.升级VIP10；17.升级VIP11；18.升级VIP12；19.升级VIP13；20.升级VIP14；21.升级VIP15；
     */
    private final int GetReasons;
    /**
     * 获得红包的原因1.首充奖励；2.每日充值；3.月卡；4.至尊月卡；5.成长基金6.巅峰基金；7.升级至VIP1；8.升级至VIP2；9.升级至VIP3；10.升级VIP4；11.升级VIP5；12.升级VIP6；13.升级VIP7；14.升级VIP8；15.升级VIP9；16.升级VIP10；17.升级VIP11；18.升级VIP12；19.升级VIP13；20.升级VIP14；21.升级VIP15；
     * @return
     */
    public final int getGetReasons(){
        return GetReasons;
    }
    /**
     * 红包内所含货币的数量
     */
    private final int Value;
    /**
     * 红包内所含货币的数量
     * @return
     */
    public final int getValue(){
        return Value;
    }
    /**
     * 红包持续时间（单位：分）
     */
    private final int Time;
    /**
     * 红包持续时间（单位：分）
     * @return
     */
    public final int getTime(){
        return Time;
    }
    /**
     * 最大红包个数
     */
    private final int MaxNumber;
    /**
     * 最大红包个数
     * @return
     */
    public final int getMaxNumber(){
        return MaxNumber;
    }
    /**
     * 最小红包个数
     */
    private final int MinNumber;
    /**
     * 最小红包个数
     * @return
     */
    public final int getMinNumber(){
        return MinNumber;
    }
    /**
     * 红包货币类型：
1.灵玉
2.绑定灵玉
12.元宝
     */
    private final int ItemType;
    /**
     * 红包货币类型：
1.灵玉
2.绑定灵玉
12.元宝
     * @return
     */
    public final int getItemType(){
        return ItemType;
    }
    /**
     * 最小可获得红包内货币数量
     */
    private final int MinGetValue;
    /**
     * 最小可获得红包内货币数量
     * @return
     */
    public final int getMinGetValue(){
        return MinGetValue;
    }

    public Cfg_NewRedPacket_Bean(int Id,int GetReasons,int Value,int Time,int MaxNumber,int MinNumber,int ItemType,int MinGetValue){
        this.Id = Id;
        this.GetReasons = GetReasons;
        this.Value = Value;
        this.Time = Time;
        this.MaxNumber = MaxNumber;
        this.MinNumber = MinNumber;
        this.ItemType = ItemType;
        this.MinGetValue = MinGetValue;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("GetReasons:").append(GetReasons).append(";");
        str.append("Value:").append(Value).append(";");
        str.append("Time:").append(Time).append(";");
        str.append("MaxNumber:").append(MaxNumber).append(";");
        str.append("MinNumber:").append(MinNumber).append(";");
        str.append("ItemType:").append(ItemType).append(";");
        str.append("MinGetValue:").append(MinGetValue).append(";");
        return str.toString();
    }
}
