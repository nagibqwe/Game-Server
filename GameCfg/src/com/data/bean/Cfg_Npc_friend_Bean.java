/**
 * Auto generated, do not edit it
 *
 * npc_friend配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Npc_friend_Bean{
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
     * NPC名字
     */
    private final String name;
    /**
     * NPC名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 组ID，相同的ID为同一个NPC好友
     */
    private final int gruopid;
    /**
     * 组ID，相同的ID为同一个NPC好友
     * @return
     */
    public final int getGruopid(){
        return gruopid;
    }
    /**
     * 顺序
     */
    private final int order;
    /**
     * 顺序
     * @return
     */
    public final int getOrder(){
        return order;
    }
    /**
     * 头像ICON
     */
    private final int icon;
    /**
     * 头像ICON
     * @return
     */
    public final int getIcon(){
        return icon;
    }
    /**
     * 等级(-1为玩家当前等级)
     */
    private final int level;
    /**
     * 等级(-1为玩家当前等级)
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 赠送给玩家情义点的时间
     */
    private final int qingyi_time;
    /**
     * 赠送给玩家情义点的时间
     * @return
     */
    public final int getQingyi_time(){
        return qingyi_time;
    }
    /**
     * 获得好友的条件
     */
    private final ReadIntegerArray start_variables;
    /**
     * 获得好友的条件
     * @return
     */
    public final ReadIntegerArray getStart_variables(){
        return start_variables;
    }
    /**
     * 特殊的背景框资源（不填则为默认）
     */
    private final int tes;
    /**
     * 特殊的背景框资源（不填则为默认）
     * @return
     */
    public final int getTes(){
        return tes;
    }

    public Cfg_Npc_friend_Bean(int id,String name,int gruopid,int order,int icon,int level,int qingyi_time,String start_variablesStr,int tes){
        this.id = id;
        this.name = name;
        this.gruopid = gruopid;
        this.order = order;
        this.icon = icon;
        this.level = level;
        this.qingyi_time = qingyi_time;
        this.start_variables = new ReadIntegerArray(start_variablesStr,",");
        this.tes = tes;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("gruopid:").append(gruopid).append(";");
        str.append("order:").append(order).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("level:").append(level).append(";");
        str.append("qingyi_time:").append(qingyi_time).append(";");
        str.append("start_variables:").append(start_variables).append(";");
        str.append("tes:").append(tes).append(";");
        return str.toString();
    }
}
