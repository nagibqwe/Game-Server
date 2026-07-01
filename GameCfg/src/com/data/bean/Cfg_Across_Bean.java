/**
 * Auto generated, do not edit it
 *
 * across配置表
 */
package com.data.bean;

	
public class Cfg_Across_Bean{
    /**
     * 序号
     */
    private final int Id;
    /**
     * 序号
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 世界等级
     */
    private final int World_Level;
    /**
     * 世界等级
     * @return
     */
    public final int getWorld_Level(){
        return World_Level;
    }
    /**
     * 服务器数量
     */
    private final int Server_num;
    /**
     * 服务器数量
     * @return
     */
    public final int getServer_num(){
        return Server_num;
    }

    public Cfg_Across_Bean(int Id,int World_Level,int Server_num){
        this.Id = Id;
        this.World_Level = World_Level;
        this.Server_num = Server_num;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("World_Level:").append(World_Level).append(";");
        str.append("Server_num:").append(Server_num).append(";");
        return str.toString();
    }
}
