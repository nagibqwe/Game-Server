/**
 * Auto generated, do not edit it
 *
 * RollDodge配置表
 */
package com.data.bean;

	
public class Cfg_RollDodge_Bean{
    /**
     * 翻滚等级
     */
    private final int level;
    /**
     * 翻滚等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 翻滚最大距离，单位厘米
     */
    private final int max_dis;
    /**
     * 翻滚最大距离，单位厘米
     * @return
     */
    public final int getMax_dis(){
        return max_dis;
    }
    /**
     * 翻滚CD，单位毫秒
     */
    private final int cd_time;
    /**
     * 翻滚CD，单位毫秒
     * @return
     */
    public final int getCd_time(){
        return cd_time;
    }
    /**
     * 执行时间，单位毫秒
     */
    private final int execute_time;
    /**
     * 执行时间，单位毫秒
     * @return
     */
    public final int getExecute_time(){
        return execute_time;
    }
    /**
     * 霸体时间，单位毫秒
     */
    private final int super_armor_time;
    /**
     * 霸体时间，单位毫秒
     * @return
     */
    public final int getSuper_armor_time(){
        return super_armor_time;
    }

    public Cfg_RollDodge_Bean(int level,int max_dis,int cd_time,int execute_time,int super_armor_time){
        this.level = level;
        this.max_dis = max_dis;
        this.cd_time = cd_time;
        this.execute_time = execute_time;
        this.super_armor_time = super_armor_time;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("level:").append(level).append(";");
        str.append("max_dis:").append(max_dis).append(";");
        str.append("cd_time:").append(cd_time).append(";");
        str.append("execute_time:").append(execute_time).append(";");
        str.append("super_armor_time:").append(super_armor_time).append(";");
        return str.toString();
    }
}
