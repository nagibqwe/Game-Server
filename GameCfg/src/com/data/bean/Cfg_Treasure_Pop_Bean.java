/**
 * Auto generated, do not edit it
 *
 * Treasure_Pop配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Treasure_Pop_Bean{
    /**
     * 寻宝类别
     */
    private final int id;
    /**
     * 寻宝类别
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 奖池类型
1、寻宝奖池
2、仙魄奖池
3、造化奖池
4、鸿蒙奖池
5，上古寻宝
6，仙甲寻宝
     */
    private final int rewardType;
    /**
     * 奖池类型
1、寻宝奖池
2、仙魄奖池
3、造化奖池
4、鸿蒙奖池
5，上古寻宝
6，仙甲寻宝
     * @return
     */
    public final int getRewardType(){
        return rewardType;
    }
    /**
     * 单次花费
货币类型_num
     */
    private final ReadIntegerArray moneyCost;
    /**
     * 单次花费
货币类型_num
     * @return
     */
    public final ReadIntegerArray getMoneyCost(){
        return moneyCost;
    }
    /**
     * 购买后获得的抽取道具id_num
     */
    private final ReadIntegerArray item;
    /**
     * 购买后获得的抽取道具id_num
     * @return
     */
    public final ReadIntegerArray getItem(){
        return item;
    }
    /**
     * 每天免费抽取次数
     */
    private final int freeTimes;
    /**
     * 每天免费抽取次数
     * @return
     */
    public final int getFreeTimes(){
        return freeTimes;
    }
    /**
     * 寻宝次数_道具id_道具数量
     */
    private final ReadIntegerArrayEs times;
    /**
     * 寻宝次数_道具id_道具数量
     * @return
     */
    public final ReadIntegerArrayEs getTimes(){
        return times;
    }
    /**
     * 购买后获得的赠送道具类型_num
     */
    private final ReadIntegerArray gold;
    /**
     * 购买后获得的赠送道具类型_num
     * @return
     */
    public final ReadIntegerArray getGold(){
        return gold;
    }
    /**
     * 获得的积分
货币类型_num
     */
    private final ReadIntegerArray integral;
    /**
     * 获得的积分
货币类型_num
     * @return
     */
    public final ReadIntegerArray getIntegral(){
        return integral;
    }
    /**
     * 对应的奖池区间
     */
    private final ReadIntegerArray section;
    /**
     * 对应的奖池区间
     * @return
     */
    public final ReadIntegerArray getSection(){
        return section;
    }
    /**
     * 具体次数必中type中的一个道具
     */
    private final ReadIntegerArrayEs frequency;
    /**
     * 具体次数必中type中的一个道具
     * @return
     */
    public final ReadIntegerArrayEs getFrequency(){
        return frequency;
    }
    /**
     * 仙甲寻宝的保底奖励
     */
    private final ReadIntegerArrayEs guarantees_reward;
    /**
     * 仙甲寻宝的保底奖励
     * @return
     */
    public final ReadIntegerArrayEs getGuarantees_reward(){
        return guarantees_reward;
    }
    /**
     * 幸运值上限（rewardType=1，3，4，5才可使用）
     */
    private final int luck_limit;
    /**
     * 幸运值上限（rewardType=1，3，4，5才可使用）
     * @return
     */
    public final int getLuck_limit(){
        return luck_limit;
    }
    /**
     * 幸运值达到上限时，增加的大奖抽中概率
（Treasure_Hunt表中type=2的为大奖）
概率需要扩大10000倍填入，方便程序计算
     */
    private final int luck_limit_mult;
    /**
     * 幸运值达到上限时，增加的大奖抽中概率
（Treasure_Hunt表中type=2的为大奖）
概率需要扩大10000倍填入，方便程序计算
     * @return
     */
    public final int getLuck_limit_mult(){
        return luck_limit_mult;
    }
    /**
     * 增加幸运值保底次数，在幸运值满的情况下，达到次数则必中大奖
     */
    private final int luck_limit_times;
    /**
     * 增加幸运值保底次数，在幸运值满的情况下，达到次数则必中大奖
     * @return
     */
    public final int getLuck_limit_times(){
        return luck_limit_times;
    }

    public Cfg_Treasure_Pop_Bean(int id,int rewardType,String moneyCostStr,String itemStr,int freeTimes,String timesStr,String goldStr,String integralStr,String sectionStr,String frequencyStr,String guarantees_rewardStr,int luck_limit,int luck_limit_mult,int luck_limit_times){
        this.id = id;
        this.rewardType = rewardType;
        this.moneyCost = new ReadIntegerArray(moneyCostStr,",");
        this.item = new ReadIntegerArray(itemStr,",");
        this.freeTimes = freeTimes;
        this.times = new ReadIntegerArrayEs(timesStr,"}",",");
        this.gold = new ReadIntegerArray(goldStr,",");
        this.integral = new ReadIntegerArray(integralStr,",");
        this.section = new ReadIntegerArray(sectionStr,",");
        this.frequency = new ReadIntegerArrayEs(frequencyStr,"}",",");
        this.guarantees_reward = new ReadIntegerArrayEs(guarantees_rewardStr,"}",",");
        this.luck_limit = luck_limit;
        this.luck_limit_mult = luck_limit_mult;
        this.luck_limit_times = luck_limit_times;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("rewardType:").append(rewardType).append(";");
        str.append("moneyCost:").append(moneyCost).append(";");
        str.append("item:").append(item).append(";");
        str.append("freeTimes:").append(freeTimes).append(";");
        str.append("times:").append(times).append(";");
        str.append("gold:").append(gold).append(";");
        str.append("integral:").append(integral).append(";");
        str.append("section:").append(section).append(";");
        str.append("frequency:").append(frequency).append(";");
        str.append("guarantees_reward:").append(guarantees_reward).append(";");
        str.append("luck_limit:").append(luck_limit).append(";");
        str.append("luck_limit_mult:").append(luck_limit_mult).append(";");
        str.append("luck_limit_times:").append(luck_limit_times).append(";");
        return str.toString();
    }
}
