/**
 * Auto generated, do not edit it
 *
 * limit_gold_shop配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Limit_gold_shop_Bean{
    /**
     * 商品编号，需要对应rechargeItem的ID
10000+group*100+sort
     */
    private final int id;
    /**
     * 商品编号，需要对应rechargeItem的ID
10000+group*100+sort
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 分组（用于区分是否为同一个节点的礼包）

     */
    private final int group;
    /**
     * 分组（用于区分是否为同一个节点的礼包）

     * @return
     */
    public final int getGroup(){
        return group;
    }
    /**
     * 奖励
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 奖励
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 礼包价格
货币类型_货币数量
     */
    private final ReadIntegerArray price;
    /**
     * 礼包价格
货币类型_货币数量
     * @return
     */
    public final ReadIntegerArray getPrice(){
        return price;
    }
    /**
     * 读取FunctionVariable表的主键配置
(下列条件需要全部满足才可以）
2个condition都需要同时满足条件
     */
    private final ReadIntegerArrayEs condition;
    /**
     * 读取FunctionVariable表的主键配置
(下列条件需要全部满足才可以）
2个condition都需要同时满足条件
     * @return
     */
    public final ReadIntegerArrayEs getCondition(){
        return condition;
    }
    /**
     * 读取FunctionVariable表的主键配置
(下列条件满足任意一个条件就算满足）
（client ignore）
     */
    private final ReadIntegerArrayEs condition2;
    /**
     * 读取FunctionVariable表的主键配置
(下列条件满足任意一个条件就算满足）
（client ignore）
     * @return
     */
    public final ReadIntegerArrayEs getCondition2(){
        return condition2;
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
    /**
     * 限制购买数量
     */
    private final int buyNum;
    /**
     * 限制购买数量
     * @return
     */
    public final int getBuyNum(){
        return buyNum;
    }

    public Cfg_Limit_gold_shop_Bean(int id,int group,String rewardStr,String priceStr,String conditionStr,String condition2Str,int time,int buyNum){
        this.id = id;
        this.group = group;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.price = new ReadIntegerArray(priceStr,",");
        this.condition = new ReadIntegerArrayEs(conditionStr,"}",",");
        this.condition2 = new ReadIntegerArrayEs(condition2Str,"}",",");
        this.time = time;
        this.buyNum = buyNum;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("group:").append(group).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("price:").append(price).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("condition2:").append(condition2).append(";");
        str.append("time:").append(time).append(";");
        str.append("buyNum:").append(buyNum).append(";");
        return str.toString();
    }
}
