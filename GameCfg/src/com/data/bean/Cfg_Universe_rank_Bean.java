/**
 * Auto generated, do not edit it
 *
 * universe_rank配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Universe_rank_Bean{
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
     * 结算时间（对应跨服数量）
     */
    private final int time;
    /**
     * 结算时间（对应跨服数量）
     * @return
     */
    public final int getTime(){
        return time;
    }
    /**
     * 区间_临时称号ID_永久称号ID_称号战力
     */
    private final ReadIntegerArrayEs rank;
    /**
     * 区间_临时称号ID_永久称号ID_称号战力
     * @return
     */
    public final ReadIntegerArrayEs getRank(){
        return rank;
    }

    public Cfg_Universe_rank_Bean(int id,int time,String rankStr){
        this.id = id;
        this.time = time;
        this.rank = new ReadIntegerArrayEs(rankStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("time:").append(time).append(";");
        str.append("rank:").append(rank).append(";");
        return str.toString();
    }
}
