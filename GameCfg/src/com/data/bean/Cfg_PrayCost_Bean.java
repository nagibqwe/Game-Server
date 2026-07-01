/**
 * Auto generated, do not edit it
 *
 * PrayCost配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_PrayCost_Bean{
    /**
     * 次数
Pray_Time_Limit(global表控制的次数上限）
     */
    private final int num;
    /**
     * 次数
Pray_Time_Limit(global表控制的次数上限）
     * @return
     */
    public final int getNum(){
        return num;
    }
    /**
     * 元宝兑换经验消耗(@_@)
num_货币类型
     */
    private final ReadIntegerArray prayExpCost;
    /**
     * 元宝兑换经验消耗(@_@)
num_货币类型
     * @return
     */
    public final ReadIntegerArray getPrayExpCost(){
        return prayExpCost;
    }
    /**
     * 元宝祈福灵石消耗(@_@)
num_货币类型
     */
    private final ReadIntegerArray prayMoneyCost;
    /**
     * 元宝祈福灵石消耗(@_@)
num_货币类型
     * @return
     */
    public final ReadIntegerArray getPrayMoneyCost(){
        return prayMoneyCost;
    }

    public Cfg_PrayCost_Bean(int num,String prayExpCostStr,String prayMoneyCostStr){
        this.num = num;
        this.prayExpCost = new ReadIntegerArray(prayExpCostStr,",");
        this.prayMoneyCost = new ReadIntegerArray(prayMoneyCostStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("num:").append(num).append(";");
        str.append("prayExpCost:").append(prayExpCost).append(";");
        str.append("prayMoneyCost:").append(prayMoneyCost).append(";");
        return str.toString();
    }
}
