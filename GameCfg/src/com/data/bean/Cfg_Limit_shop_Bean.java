/**
 * Auto generated, do not edit it
 *
 * limit_shop配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Limit_shop_Bean{
    /**
     * 商城编号
     */
    private final int id;
    /**
     * 商城编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * ItemId_num_bind_occ
bind：0未绑定，1绑定
occ：0男1女9通用
     */
    private final ReadIntegerArrayEs reward;
    /**
     * ItemId_num_bind_occ
bind：0未绑定，1绑定
occ：0男1女9通用
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 1等级
2任务
3境界
4VIP等级
5登陆天数
6购买了指定礼包出现(-1代表最后一个，购买完就消失） 
 //强化x件到N   private final int COND_INTENSIFY = 7;
 //开服天数   private final int COND_OPENSERVERDAY = 8;
 //功能开启   private final int COND_FUNCOPEN = 9;
 //寻宝X次  private final int COND_XUNBAO = 10;（对应普通寻宝表的类型）
 //套装，激活x阶N套  private final int COND_SUIT = 11;
 //神兽岛  private final int COND_SOULBEAST = 12;
//仙甲寻宝每轮结束当天  private final int COND_XIANJIALASTDAY = 13;(参数为轮数）
14 仙甲寻宝每轮寻宝次数
     */
    private final ReadIntegerArrayEs condition;
    /**
     * 1等级
2任务
3境界
4VIP等级
5登陆天数
6购买了指定礼包出现(-1代表最后一个，购买完就消失） 
 //强化x件到N   private final int COND_INTENSIFY = 7;
 //开服天数   private final int COND_OPENSERVERDAY = 8;
 //功能开启   private final int COND_FUNCOPEN = 9;
 //寻宝X次  private final int COND_XUNBAO = 10;（对应普通寻宝表的类型）
 //套装，激活x阶N套  private final int COND_SUIT = 11;
 //神兽岛  private final int COND_SOULBEAST = 12;
//仙甲寻宝每轮结束当天  private final int COND_XIANJIALASTDAY = 13;(参数为轮数）
14 仙甲寻宝每轮寻宝次数
     * @return
     */
    public final ReadIntegerArrayEs getCondition(){
        return condition;
    }
    /**
     * 现价
     */
    private final int presentPrice;
    /**
     * 现价
     * @return
     */
    public final int getPresentPrice(){
        return presentPrice;
    }
    /**
     * 有效购买时间（秒）
-1代表无限制时间
     */
    private final int time;
    /**
     * 有效购买时间（秒）
-1代表无限制时间
     * @return
     */
    public final int getTime(){
        return time;
    }

    public Cfg_Limit_shop_Bean(int id,String rewardStr,String conditionStr,int presentPrice,int time){
        this.id = id;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.condition = new ReadIntegerArrayEs(conditionStr,"}",",");
        this.presentPrice = presentPrice;
        this.time = time;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("presentPrice:").append(presentPrice).append(";");
        str.append("time:").append(time).append(";");
        return str.toString();
    }
}
