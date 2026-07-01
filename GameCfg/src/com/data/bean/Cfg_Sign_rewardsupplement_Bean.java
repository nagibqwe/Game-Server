/**
 * Auto generated, do not edit it
 *
 * sign_rewardsupplement配置表
 */
package com.data.bean;

	
public class Cfg_Sign_rewardsupplement_Bean{
    /**
     * 补签天数
     */
    private final int count;
    /**
     * 补签天数
     * @return
     */
    public final int getCount(){
        return count;
    }
    /**
     * 消耗货币类型
     */
    private final int costType;
    /**
     * 消耗货币类型
     * @return
     */
    public final int getCostType(){
        return costType;
    }
    /**
     * 消耗货币数量
     */
    private final int costCount;
    /**
     * 消耗货币数量
     * @return
     */
    public final int getCostCount(){
        return costCount;
    }

    public Cfg_Sign_rewardsupplement_Bean(int count,int costType,int costCount){
        this.count = count;
        this.costType = costType;
        this.costCount = costCount;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("count:").append(count).append(";");
        str.append("costType:").append(costType).append(";");
        str.append("costCount:").append(costCount).append(";");
        return str.toString();
    }
}
