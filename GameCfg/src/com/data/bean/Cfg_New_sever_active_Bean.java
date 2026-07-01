/**
 * Auto generated, do not edit it
 *
 * new_sever_active配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_New_sever_active_Bean{
    /**
     * key用于标识
     */
    private final int id;
    /**
     * key用于标识
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 表示类型
1.表示宗派之星
2.境界等级
3.结婚
4.联盟争霸
     */
    private final int type;
    /**
     * 表示类型
1.表示宗派之星
2.境界等级
3.结婚
4.联盟争霸
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 排序
     */
    private final int sort;
    /**
     * 排序
     * @return
     */
    public final int getSort(){
        return sort;
    }
    /**
     * 1.创建宗派
2.表示宗派之星中任命X个副宗主
3.宗派成员到达X个
4.宗派等级到达X级
5.完成结婚XX档次，玩家可以结3次
6.联盟争霸中霸主的宗主
7.联盟争霸中霸主帮会的积分排名多少到多少
8.联盟争霸中非霸主的宗派成员领取
9.联盟争霸中霸主宗派成员领取
     */
    private final ReadIntegerArray special_condition;
    /**
     * 1.创建宗派
2.表示宗派之星中任命X个副宗主
3.宗派成员到达X个
4.宗派等级到达X级
5.完成结婚XX档次，玩家可以结3次
6.联盟争霸中霸主的宗主
7.联盟争霸中霸主帮会的积分排名多少到多少
8.联盟争霸中非霸主的宗派成员领取
9.联盟争霸中霸主宗派成员领取
     * @return
     */
    public final ReadIntegerArray getSpecial_condition(){
        return special_condition;
    }
    /**
     * 达成的成就条件
条件都需要定义在functionVariable中
     */
    private final ReadIntegerArray condition;
    /**
     * 达成的成就条件
条件都需要定义在functionVariable中
     * @return
     */
    public final ReadIntegerArray getCondition(){
        return condition;
    }
    /**
     * 达成条件的描述
     */
    private final String des;
    /**
     * 达成条件的描述
     * @return
     */
    public final String getDes(){
        return des;
    }
    /**
     * 该奖项的限制领取次数，在全服只能被领取指定的次数，默认为0表示不限制
     */
    private final int limit_time;
    /**
     * 该奖项的限制领取次数，在全服只能被领取指定的次数，默认为0表示不限制
     * @return
     */
    public final int getLimit_time(){
        return limit_time;
    }
    /**
     * 展示的道具，实际给与的奖励
     */
    private final ReadIntegerArrayEs item;
    /**
     * 展示的道具，实际给与的奖励
     * @return
     */
    public final ReadIntegerArrayEs getItem(){
        return item;
    }

    public Cfg_New_sever_active_Bean(int id,int type,int sort,String special_conditionStr,String conditionStr,String des,int limit_time,String itemStr){
        this.id = id;
        this.type = type;
        this.sort = sort;
        this.special_condition = new ReadIntegerArray(special_conditionStr,",");
        this.condition = new ReadIntegerArray(conditionStr,",");
        this.des = des;
        this.limit_time = limit_time;
        this.item = new ReadIntegerArrayEs(itemStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("sort:").append(sort).append(";");
        str.append("special_condition:").append(special_condition).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("des:").append(des).append(";");
        str.append("limit_time:").append(limit_time).append(";");
        str.append("item:").append(item).append(";");
        return str.toString();
    }
}
