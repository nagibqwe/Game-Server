/**
 * Auto generated, do not edit it
 *
 * state_manage配置表
 */
package com.data.bean;

	
public class Cfg_State_manage_Bean{
    /**
     * 表示境界等级
     */
    private final int id;
    /**
     * 表示境界等级
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 权限组
1.表示传送
     */
    private final int group;
    /**
     * 权限组
1.表示传送
     * @return
     */
    public final int getGroup(){
        return group;
    }
    /**
     * 等级
     */
    private final int level;
    /**
     * 等级
     * @return
     */
    public final int getLevel(){
        return level;
    }

    public Cfg_State_manage_Bean(int id,int group,int level){
        this.id = id;
        this.group = group;
        this.level = level;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("group:").append(group).append(";");
        str.append("level:").append(level).append(";");
        return str.toString();
    }
}
