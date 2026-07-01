/**
 * Auto generated, do not edit it
 *
 * bonfire配置表
 */
package com.data.bean;

	
public class Cfg_Bonfire_Bean{
    /**
     * ID编号
     */
    private final int id;
    /**
     * ID编号
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
     * 篝火类型（在任务配置表中配置对应类型）
     */
    private final int type;
    /**
     * 篝火类型（在任务配置表中配置对应类型）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 模型ID
     */
    private final int modelid;
    /**
     * 模型ID
     * @return
     */
    public final int getModelid(){
        return modelid;
    }
    /**
     * 存在时间（秒）
     */
    private final int time;
    /**
     * 存在时间（秒）
     * @return
     */
    public final int getTime(){
        return time;
    }
    /**
     * 是否可超过地图上限出现（如BOSS的篝火，在击杀BOSS后，一定会出现在地图上那种，0为不可超过上限，1为可以超过地图篝火上限）
     */
    private final int more_limit;
    /**
     * 是否可超过地图上限出现（如BOSS的篝火，在击杀BOSS后，一定会出现在地图上那种，0为不可超过上限，1为可以超过地图篝火上限）
     * @return
     */
    public final int getMore_limit(){
        return more_limit;
    }
    /**
     * 是否公告
     */
    private final int announcement;
    /**
     * 是否公告
     * @return
     */
    public final int getAnnouncement(){
        return announcement;
    }

    public Cfg_Bonfire_Bean(int id,String name,int type,int modelid,int time,int more_limit,int announcement){
        this.id = id;
        this.name = name;
        this.type = type;
        this.modelid = modelid;
        this.time = time;
        this.more_limit = more_limit;
        this.announcement = announcement;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("type:").append(type).append(";");
        str.append("modelid:").append(modelid).append(";");
        str.append("time:").append(time).append(";");
        str.append("more_limit:").append(more_limit).append(";");
        str.append("announcement:").append(announcement).append(";");
        return str.toString();
    }
}
