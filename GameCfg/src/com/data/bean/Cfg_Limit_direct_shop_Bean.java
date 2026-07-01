/**
 * Auto generated, do not edit it
 *
 * limit_direct_shop配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Limit_direct_shop_Bean{
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

    public Cfg_Limit_direct_shop_Bean(int id,int group,String conditionStr,String condition2Str,int time,int buyNum){
        this.id = id;
        this.group = group;
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
        str.append("condition:").append(condition).append(";");
        str.append("condition2:").append(condition2).append(";");
        str.append("time:").append(time).append(";");
        str.append("buyNum:").append(buyNum).append(";");
        return str.toString();
    }
}
