/**
 * Auto generated, do not edit it
 *
 * SZZQAward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_SZZQAward_Bean{
    /**
     * id
     */
    private final int id;
    /**
     * id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 名字
     */
    private final String name;
    /**
     * 名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 最低排名
     */
    private final int rank_min;
    /**
     * 最低排名
     * @return
     */
    public final int getRank_min(){
        return rank_min;
    }
    /**
     * 最大排名
     */
    private final int rank_max;
    /**
     * 最大排名
     * @return
     */
    public final int getRank_max(){
        return rank_max;
    }
    /**
     * icon图片
     */
    private final int res_icon;
    /**
     * icon图片
     * @return
     */
    public final int getRes_icon(){
        return res_icon;
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

    public Cfg_SZZQAward_Bean(int id,String name,int rank_min,int rank_max,int res_icon,String awardStr,int ues_exp_index){
        this.id = id;
        this.name = name;
        this.rank_min = rank_min;
        this.rank_max = rank_max;
        this.res_icon = res_icon;
        this.award = new ReadIntegerArrayEs(awardStr,"}",",");
        this.ues_exp_index = ues_exp_index;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("rank_min:").append(rank_min).append(";");
        str.append("rank_max:").append(rank_max).append(";");
        str.append("res_icon:").append(res_icon).append(";");
        str.append("award:").append(award).append(";");
        str.append("ues_exp_index:").append(ues_exp_index).append(";");
        return str.toString();
    }
}
