/**
 * Auto generated, do not edit it
 *
 * new_sever_growup配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_New_sever_growup_Bean{
    /**
     * id，id=day+subId-1+sort
     */
    private final int id;
    /**
     * id，id=day+subId-1+sort
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 服务器开启的天数
     */
    private final int day;
    /**
     * 服务器开启的天数
     * @return
     */
    public final int getDay(){
        return day;
    }
    /**
     * 所属类型
1：磨砺之路
2：神装之路
3：荣光之路
4：为盟之道
     */
    private final int subId;
    /**
     * 所属类型
1：磨砺之路
2：神装之路
3：荣光之路
4：为盟之道
     * @return
     */
    public final int getSubId(){
        return subId;
    }
    /**
     * 用于默认排序，完成未领取在前面
     */
    private final int sort;
    /**
     * 用于默认排序，完成未领取在前面
     * @return
     */
    public final int getSort(){
        return sort;
    }
    /**
     * 菜单背景名字
     */
    private final String menuTex;
    /**
     * 菜单背景名字
     * @return
     */
    public final String getMenuTex(){
        return menuTex;
    }
    /**
     * 完成该任务后获得的进度点
     */
    private final int rate;
    /**
     * 完成该任务后获得的进度点
     * @return
     */
    public final int getRate(){
        return rate;
    }
    /**
     * 达成的成就条件(@_@)
条件都需要定义在functionVariable中
     */
    private final ReadIntegerArray condition;
    /**
     * 达成的成就条件(@_@)
条件都需要定义在functionVariable中
     * @return
     */
    public final ReadIntegerArray getCondition(){
        return condition;
    }
    /**
     * 奖励
     */
    private final ReadIntegerArray reward;
    /**
     * 奖励
     * @return
     */
    public final ReadIntegerArray getReward(){
        return reward;
    }

    public Cfg_New_sever_growup_Bean(int id,int day,int subId,int sort,String menuTex,int rate,String conditionStr,String rewardStr){
        this.id = id;
        this.day = day;
        this.subId = subId;
        this.sort = sort;
        this.menuTex = menuTex;
        this.rate = rate;
        this.condition = new ReadIntegerArray(conditionStr,",");
        this.reward = new ReadIntegerArray(rewardStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("day:").append(day).append(";");
        str.append("subId:").append(subId).append(";");
        str.append("sort:").append(sort).append(";");
        str.append("menuTex:").append(menuTex).append(";");
        str.append("rate:").append(rate).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
