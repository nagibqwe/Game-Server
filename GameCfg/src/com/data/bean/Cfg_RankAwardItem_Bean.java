/**
 * Auto generated, do not edit it
 *
 * RankAwardItem配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_RankAwardItem_Bean{
    /**
     * key值
应服务器要求必须从1开始
     */
    private final int id;
    /**
     * key值
应服务器要求必须从1开始
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 所属排名id
对应RankAwardType表主键
     */
    private final int owner_id;
    /**
     * 所属排名id
对应RankAwardType表主键
     * @return
     */
    public final int getOwner_id(){
        return owner_id;
    }
    /**
     * 奖励类型
（0排名奖励,活动结束发奖励
1达成奖励，玩家自主领取）
     */
    private final int award_type;
    /**
     * 奖励类型
（0排名奖励,活动结束发奖励
1达成奖励，玩家自主领取）
     * @return
     */
    public final int getAward_type(){
        return award_type;
    }
    /**
     * 最高排名
（适用于排名奖励类型）
     */
    private final int top_rank;
    /**
     * 最高排名
（适用于排名奖励类型）
     * @return
     */
    public final int getTop_rank(){
        return top_rank;
    }
    /**
     * 最低排名
（适用于排名奖励类型）
     */
    private final int bottom_rank;
    /**
     * 最低排名
（适用于排名奖励类型）
     * @return
     */
    public final int getBottom_rank(){
        return bottom_rank;
    }
    /**
     * 领取奖励需要达到的值（不同排行榜对应各自的值）
比如等级榜对应等级 （坐骑数值为阶*100+星）
     */
    private final int need_value;
    /**
     * 领取奖励需要达到的值（不同排行榜对应各自的值）
比如等级榜对应等级 （坐骑数值为阶*100+星）
     * @return
     */
    public final int getNeed_value(){
        return need_value;
    }
    /**
     * 最大领取数量，为0表示不限制（适用于达成奖励类型）
     */
    private final int max_get_count;
    /**
     * 最大领取数量，为0表示不限制（适用于达成奖励类型）
     * @return
     */
    public final int getMax_get_count(){
        return max_get_count;
    }
    /**
     * 物品奖励，需要配置职业区分
itemid_num_bind_occ
bind:0不绑定，1绑定
occ：0男1女9通用
     */
    private final ReadIntegerArrayEs award_items;
    /**
     * 物品奖励，需要配置职业区分
itemid_num_bind_occ
bind:0不绑定，1绑定
occ：0男1女9通用
     * @return
     */
    public final ReadIntegerArrayEs getAward_items(){
        return award_items;
    }

    public Cfg_RankAwardItem_Bean(int id,int owner_id,int award_type,int top_rank,int bottom_rank,int need_value,int max_get_count,String award_itemsStr){
        this.id = id;
        this.owner_id = owner_id;
        this.award_type = award_type;
        this.top_rank = top_rank;
        this.bottom_rank = bottom_rank;
        this.need_value = need_value;
        this.max_get_count = max_get_count;
        this.award_items = new ReadIntegerArrayEs(award_itemsStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("owner_id:").append(owner_id).append(";");
        str.append("award_type:").append(award_type).append(";");
        str.append("top_rank:").append(top_rank).append(";");
        str.append("bottom_rank:").append(bottom_rank).append(";");
        str.append("need_value:").append(need_value).append(";");
        str.append("max_get_count:").append(max_get_count).append(";");
        str.append("award_items:").append(award_items).append(";");
        return str.toString();
    }
}
