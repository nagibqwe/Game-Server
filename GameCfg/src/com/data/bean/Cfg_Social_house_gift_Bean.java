/**
 * Auto generated, do not edit it
 *
 * social_house_gift配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Social_house_gift_Bean{
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
     * 名称
     */
    private final String name;
    /**
     * 名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 资源ID
     */
    private final int res;
    /**
     * 资源ID
     * @return
     */
    public final int getRes(){
        return res;
    }
    /**
     * 消耗的货币_数量
     */
    private final ReadIntegerArray type;
    /**
     * 消耗的货币_数量
     * @return
     */
    public final ReadIntegerArray getType(){
        return type;
    }
    /**
     * 增加的人气
     */
    private final int popularity_num;
    /**
     * 增加的人气
     * @return
     */
    public final int getPopularity_num(){
        return popularity_num;
    }
    /**
     * 每日赠送的最大次数
     */
    private final int daily_max;
    /**
     * 每日赠送的最大次数
     * @return
     */
    public final int getDaily_max(){
        return daily_max;
    }

    public Cfg_Social_house_gift_Bean(int id,String name,int res,String typeStr,int popularity_num,int daily_max){
        this.id = id;
        this.name = name;
        this.res = res;
        this.type = new ReadIntegerArray(typeStr,",");
        this.popularity_num = popularity_num;
        this.daily_max = daily_max;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("res:").append(res).append(";");
        str.append("type:").append(type).append(";");
        str.append("popularity_num:").append(popularity_num).append(";");
        str.append("daily_max:").append(daily_max).append(";");
        return str.toString();
    }
}
