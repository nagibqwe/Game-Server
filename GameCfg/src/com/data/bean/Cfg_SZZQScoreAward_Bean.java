/**
 * Auto generated, do not edit it
 *
 * SZZQScoreAward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_SZZQScoreAward_Bean{
    /**
     * 分数
     */
    private final int score;
    /**
     * 分数
     * @return
     */
    public final int getScore(){
        return score;
    }
    /**
     * 奖励(@;@_@)
     */
    private final ReadIntegerArrayEs award;
    /**
     * 奖励(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAward(){
        return award;
    }
    /**
     * 经验奖励索引
     */
    private final int ues_exp_index;
    /**
     * 经验奖励索引
     * @return
     */
    public final int getUes_exp_index(){
        return ues_exp_index;
    }

    public Cfg_SZZQScoreAward_Bean(int score,String awardStr,int ues_exp_index){
        this.score = score;
        this.award = new ReadIntegerArrayEs(awardStr,"}",",");
        this.ues_exp_index = ues_exp_index;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("score:").append(score).append(";");
        str.append("award:").append(award).append(";");
        str.append("ues_exp_index:").append(ues_exp_index).append(";");
        return str.toString();
    }
}
