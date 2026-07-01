/**
 * Auto generated, do not edit it
 *
 * World_Support配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_World_Support_Bean{
    /**
     * ID
     */
    private final int id;
    /**
     * ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 等级区间
     */
    private final ReadIntegerArray level_rank;
    /**
     * 等级区间
     * @return
     */
    public final ReadIntegerArray getLevel_rank(){
        return level_rank;
    }
    /**
     * 支援奖励：物品ID_数量_最大数量
     */
    private final ReadIntegerArray pic_res;
    /**
     * 支援奖励：物品ID_数量_最大数量
     * @return
     */
    public final ReadIntegerArray getPic_res(){
        return pic_res;
    }
    /**
     * 感谢物品ID_每日可获得的最大数量
     */
    private final ReadIntegerArray S_title_rank;
    /**
     * 感谢物品ID_每日可获得的最大数量
     * @return
     */
    public final ReadIntegerArray getS_title_rank(){
        return S_title_rank;
    }
    /**
     * 感谢的奖励
     */
    private final ReadIntegerArray S_title_item;
    /**
     * 感谢的奖励
     * @return
     */
    public final ReadIntegerArray getS_title_item(){
        return S_title_item;
    }
    /**
     * 被感谢的奖励_数量_每日最大获取数量
     */
    private final ReadIntegerArray S_title_fight;
    /**
     * 被感谢的奖励_数量_每日最大获取数量
     * @return
     */
    public final ReadIntegerArray getS_title_fight(){
        return S_title_fight;
    }
    /**
     * 支援的最大人数
     */
    private final int max_times;
    /**
     * 支援的最大人数
     * @return
     */
    public final int getMax_times(){
        return max_times;
    }
    /**
     * 冷却时间（秒）
     */
    private final int cold_times;
    /**
     * 冷却时间（秒）
     * @return
     */
    public final int getCold_times(){
        return cold_times;
    }

    public Cfg_World_Support_Bean(int id,String level_rankStr,String pic_resStr,String S_title_rankStr,String S_title_itemStr,String S_title_fightStr,int max_times,int cold_times){
        this.id = id;
        this.level_rank = new ReadIntegerArray(level_rankStr,",");
        this.pic_res = new ReadIntegerArray(pic_resStr,",");
        this.S_title_rank = new ReadIntegerArray(S_title_rankStr,",");
        this.S_title_item = new ReadIntegerArray(S_title_itemStr,",");
        this.S_title_fight = new ReadIntegerArray(S_title_fightStr,",");
        this.max_times = max_times;
        this.cold_times = cold_times;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("level_rank:").append(level_rank).append(";");
        str.append("pic_res:").append(pic_res).append(";");
        str.append("S_title_rank:").append(S_title_rank).append(";");
        str.append("S_title_item:").append(S_title_item).append(";");
        str.append("S_title_fight:").append(S_title_fight).append(";");
        str.append("max_times:").append(max_times).append(";");
        str.append("cold_times:").append(cold_times).append(";");
        return str.toString();
    }
}
