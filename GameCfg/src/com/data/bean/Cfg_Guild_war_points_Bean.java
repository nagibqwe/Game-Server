/**
 * Auto generated, do not edit it
 *
 * guild_war_points配置表
 */
package com.data.bean;

	
public class Cfg_Guild_war_points_Bean{
    /**
     * 编号
     */
    private final int id;
    /**
     * 编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 当事人增加积分
     */
    private final int icon;
    /**
     * 当事人增加积分
     * @return
     */
    public final int getIcon(){
        return icon;
    }
    /**
     * 当事人所在仙盟全员增加积分（只针对当前正在战场内的成员）
     */
    private final int count;
    /**
     * 当事人所在仙盟全员增加积分（只针对当前正在战场内的成员）
     * @return
     */
    public final int getCount(){
        return count;
    }

    public Cfg_Guild_war_points_Bean(int id,int icon,int count){
        this.id = id;
        this.icon = icon;
        this.count = count;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("count:").append(count).append(";");
        return str.toString();
    }
}
