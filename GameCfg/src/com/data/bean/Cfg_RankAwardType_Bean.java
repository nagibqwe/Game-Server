/**
 * Auto generated, do not edit it
 *
 * RankAwardType配置表
 */
package com.data.bean;

	
public class Cfg_RankAwardType_Bean{
    /**
     * key值
ID=11是写死的消费灵玉榜，不可修改
     */
    private final int id;
    /**
     * key值
ID=11是写死的消费灵玉榜，不可修改
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 排名类型（
0：读取排行榜
1：金元宝消费排名，程序特殊处理）
     */
    private final int rank_type;
    /**
     * 排名类型（
0：读取排行榜
1：金元宝消费排名，程序特殊处理）
     * @return
     */
    public final int getRank_type(){
        return rank_type;
    }
    /**
     * 链接的排行榜id
关联Rank_base主键
     */
    private final int link_rank_id;
    /**
     * 链接的排行榜id
关联Rank_base主键
     * @return
     */
    public final int getLink_rank_id(){
        return link_rank_id;
    }
    /**
     * 开始天数
包含配置当天
     */
    private final int start_day;
    /**
     * 开始天数
包含配置当天
     * @return
     */
    public final int getStart_day(){
        return start_day;
    }
    /**
     * 结束天数
包含配置当天
     */
    private final int end_day;
    /**
     * 结束天数
包含配置当天
     * @return
     */
    public final int getEnd_day(){
        return end_day;
    }
    /**
     * 比拼显示名字
     */
    private final String name;
    /**
     * 比拼显示名字
     * @return
     */
    public final String getName(){
        return name;
    }

    public Cfg_RankAwardType_Bean(int id,int rank_type,int link_rank_id,int start_day,int end_day,String name){
        this.id = id;
        this.rank_type = rank_type;
        this.link_rank_id = link_rank_id;
        this.start_day = start_day;
        this.end_day = end_day;
        this.name = name;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("rank_type:").append(rank_type).append(";");
        str.append("link_rank_id:").append(link_rank_id).append(";");
        str.append("start_day:").append(start_day).append(";");
        str.append("end_day:").append(end_day).append(";");
        str.append("name:").append(name).append(";");
        return str.toString();
    }
}
