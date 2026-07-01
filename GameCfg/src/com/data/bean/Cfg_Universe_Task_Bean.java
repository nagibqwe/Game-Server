/**
 * Auto generated, do not edit it
 *
 * universe_Task配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Universe_Task_Bean{
    /**
     * 任务编号
     */
    private final int id;
    /**
     * 任务编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 阵营（程序用于区分各个服务器）
     */
    private final int camp;
    /**
     * 阵营（程序用于区分各个服务器）
     * @return
     */
    public final int getCamp(){
        return camp;
    }
    /**
     * 任务组
     */
    private final int group;
    /**
     * 任务组
     * @return
     */
    public final int getGroup(){
        return group;
    }
    /**
     * 对应universe_boss的主键（代表击杀任意一个即可完成任务)
主要是针对不同世界等级完成任务
     */
    private final ReadIntegerArrayEs target;
    /**
     * 对应universe_boss的主键（代表击杀任意一个即可完成任务)
主要是针对不同世界等级完成任务
     * @return
     */
    public final ReadIntegerArrayEs getTarget(){
        return target;
    }
    /**
     * 任务目标数量
     */
    private final int num;
    /**
     * 任务目标数量
     * @return
     */
    public final int getNum(){
        return num;
    }

    public Cfg_Universe_Task_Bean(int id,int camp,int group,String targetStr,int num){
        this.id = id;
        this.camp = camp;
        this.group = group;
        this.target = new ReadIntegerArrayEs(targetStr,"}",",");
        this.num = num;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("camp:").append(camp).append(";");
        str.append("group:").append(group).append(";");
        str.append("target:").append(target).append(";");
        str.append("num:").append(num).append(";");
        return str.toString();
    }
}
