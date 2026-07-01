/**
 * Auto generated, do not edit it
 *
 * marry_order配置表
 */
package com.data.bean;

	
public class Cfg_Marry_order_Bean{
    /**
     * 婚宴场次
     */
    private final int id;
    /**
     * 婚宴场次
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 时间，距离0点的分钟数
     */
    private final int time;
    /**
     * 时间，距离0点的分钟数
     * @return
     */
    public final int getTime(){
        return time;
    }

    public Cfg_Marry_order_Bean(int id,int time){
        this.id = id;
        this.time = time;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("time:").append(time).append(";");
        return str.toString();
    }
}
