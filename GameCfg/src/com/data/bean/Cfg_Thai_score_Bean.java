/**
 * Auto generated, do not edit it
 *
 * Thai_score配置表
 */
package com.data.bean;

	
public class Cfg_Thai_score_Bean{
    /**
     * 唯一id
     */
    private final int Id;
    /**
     * 唯一id
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 是否是VIP
     */
    private final int vip;
    /**
     * 是否是VIP
     * @return
     */
    public final int getVip(){
        return vip;
    }
    /**
     * 开服天数>=
     */
    private final int openDays;
    /**
     * 开服天数>=
     * @return
     */
    public final int getOpenDays(){
        return openDays;
    }
    /**
     * 登录天数>=
     */
    private final int loadDays;
    /**
     * 登录天数>=
     * @return
     */
    public final int getLoadDays(){
        return loadDays;
    }
    /**
     * 玩家等级>=
     */
    private final int level;
    /**
     * 玩家等级>=
     * @return
     */
    public final int getLevel(){
        return level;
    }

    public Cfg_Thai_score_Bean(int Id,int vip,int openDays,int loadDays,int level){
        this.Id = Id;
        this.vip = vip;
        this.openDays = openDays;
        this.loadDays = loadDays;
        this.level = level;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("vip:").append(vip).append(";");
        str.append("openDays:").append(openDays).append(";");
        str.append("loadDays:").append(loadDays).append(";");
        str.append("level:").append(level).append(";");
        return str.toString();
    }
}
