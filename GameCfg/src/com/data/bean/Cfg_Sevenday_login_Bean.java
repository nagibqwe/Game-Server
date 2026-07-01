/**
 * Auto generated, do not edit it
 *
 * sevenday_login配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Sevenday_login_Bean{
    /**
     * 累积登录天数
     */
    private final int day;
    /**
     * 累积登录天数
     * @return
     */
    public final int getDay(){
        return day;
    }
    /**
     * 签到奖励(@;@_@)ID_数量_是否绑定（0否1是）
ID_num_bind_occ
ID：对应Item表主键
num:数量
bind:0未绑定 1绑定
occ：0男 1女 9通用
     */
    private final ReadIntegerArrayEs award;
    /**
     * 签到奖励(@;@_@)ID_数量_是否绑定（0否1是）
ID_num_bind_occ
ID：对应Item表主键
num:数量
bind:0未绑定 1绑定
occ：0男 1女 9通用
     * @return
     */
    public final ReadIntegerArrayEs getAward(){
        return award;
    }
    /**
     * 模型对应显示的名字
由于名字会在各个配置表，不方便取，所有增加一个字段来进行配置
     */
    private final String modelName;
    /**
     * 模型对应显示的名字
由于名字会在各个配置表，不方便取，所有增加一个字段来进行配置
     * @return
     */
    public final String getModelName(){
        return modelName;
    }
    /**
     * 距离大奖可领取的天数
     */
    private final int rewardDay;
    /**
     * 距离大奖可领取的天数
     * @return
     */
    public final int getRewardDay(){
        return rewardDay;
    }

    public Cfg_Sevenday_login_Bean(int day,String awardStr,String modelName,int rewardDay){
        this.day = day;
        this.award = new ReadIntegerArrayEs(awardStr,"}",",");
        this.modelName = modelName;
        this.rewardDay = rewardDay;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("day:").append(day).append(";");
        str.append("award:").append(award).append(";");
        str.append("modelName:").append(modelName).append(";");
        str.append("rewardDay:").append(rewardDay).append(";");
        return str.toString();
    }
}
