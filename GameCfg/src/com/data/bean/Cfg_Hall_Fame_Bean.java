/**
 * Auto generated, do not edit it
 *
 * Hall_Fame配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Hall_Fame_Bean{
    /**
     * 阶段ID
     */
    private final int id;
    /**
     * 阶段ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 结算时间（天数，按照开服的时间的当天的24:00）
     */
    private final int time;
    /**
     * 结算时间（天数，按照开服的时间的当天的24:00）
     * @return
     */
    public final int getTime(){
        return time;
    }
    /**
     * 宣传图片资源
     */
    private final String pic_res;
    /**
     * 宣传图片资源
     * @return
     */
    public final String getPic_res(){
        return pic_res;
    }
    /**
     * 区间_称号ID_称号战力
     */
    private final ReadIntegerArrayEs rank;
    /**
     * 区间_称号ID_称号战力
     * @return
     */
    public final ReadIntegerArrayEs getRank(){
        return rank;
    }

    public Cfg_Hall_Fame_Bean(int id,int time,String pic_res,String rankStr){
        this.id = id;
        this.time = time;
        this.pic_res = pic_res;
        this.rank = new ReadIntegerArrayEs(rankStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("time:").append(time).append(";");
        str.append("pic_res:").append(pic_res).append(";");
        str.append("rank:").append(rank).append(";");
        return str.toString();
    }
}
