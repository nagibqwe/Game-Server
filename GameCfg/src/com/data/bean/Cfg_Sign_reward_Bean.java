/**
 * Auto generated, do not edit it
 *
 * sign_reward配置表
 */
package com.data.bean;

	
public class Cfg_Sign_reward_Bean{
    /**
     * 签到天数
     */
    private final int day;
    /**
     * 签到天数
     * @return
     */
    public final int getDay(){
        return day;
    }
    /**
     * 道具id
     */
    private final int itemId;
    /**
     * 道具id
     * @return
     */
    public final int getItemId(){
        return itemId;
    }
    /**
     * 道具数量
     */
    private final int itemNum;
    /**
     * 道具数量
     * @return
     */
    public final int getItemNum(){
        return itemNum;
    }
    /**
     * 是否绑定（0不绑，1绑定）
     */
    private final int isBind;
    /**
     * 是否绑定（0不绑，1绑定）
     * @return
     */
    public final int getIsBind(){
        return isBind;
    }
    /**
     * 奖励加倍类型
1：境界
2：周卡
3：月卡
4：尊享卡
5：VIP
0：没有加倍
     */
    private final int realmType;
    /**
     * 奖励加倍类型
1：境界
2：周卡
3：月卡
4：尊享卡
5：VIP
0：没有加倍
     * @return
     */
    public final int getRealmType(){
        return realmType;
    }
    /**
     * 类型参数
境界-境界等级
周卡-0
月卡-0
尊享卡-0
VIP-VIP等级
     */
    private final int realmpara;
    /**
     * 类型参数
境界-境界等级
周卡-0
月卡-0
尊享卡-0
VIP-VIP等级
     * @return
     */
    public final int getRealmpara(){
        return realmpara;
    }
    /**
     * 奖励倍率
     */
    private final int realRatio;
    /**
     * 奖励倍率
     * @return
     */
    public final int getRealRatio(){
        return realRatio;
    }

    public Cfg_Sign_reward_Bean(int day,int itemId,int itemNum,int isBind,int realmType,int realmpara,int realRatio){
        this.day = day;
        this.itemId = itemId;
        this.itemNum = itemNum;
        this.isBind = isBind;
        this.realmType = realmType;
        this.realmpara = realmpara;
        this.realRatio = realRatio;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("day:").append(day).append(";");
        str.append("itemId:").append(itemId).append(";");
        str.append("itemNum:").append(itemNum).append(";");
        str.append("isBind:").append(isBind).append(";");
        str.append("realmType:").append(realmType).append(";");
        str.append("realmpara:").append(realmpara).append(";");
        str.append("realRatio:").append(realRatio).append(";");
        return str.toString();
    }
}
