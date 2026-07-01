/**
 * Auto generated, do not edit it
 *
 * PlayerOccupation配置表
 */
package com.data.bean;

	
public class Cfg_PlayerOccupation_Bean{
    /**
     * ID职业类别    QiWangFu = 0,
    LiuShanMen = 1,
    WanHuaLou = 2,
    JiLiaoDaYing = 3,
     */
    private final int id;
    /**
     * ID职业类别    QiWangFu = 0,
    LiuShanMen = 1,
    WanHuaLou = 2,
    JiLiaoDaYing = 3,
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 性别(0:女,1:男)
     */
    private final int sex;
    /**
     * 性别(0:女,1:男)
     * @return
     */
    public final int getSex(){
        return sex;
    }
    /**
     * 职业名字
     */
    private final String jobName;
    /**
     * 职业名字
     * @return
     */
    public final String getJobName(){
        return jobName;
    }
    /**
     * 攻击类型（0物攻，1法攻）
     */
    private final int atk_type;
    /**
     * 攻击类型（0物攻，1法攻）
     * @return
     */
    public final int getAtk_type(){
        return atk_type;
    }

    public Cfg_PlayerOccupation_Bean(int id,int sex,String jobName,int atk_type){
        this.id = id;
        this.sex = sex;
        this.jobName = jobName;
        this.atk_type = atk_type;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("sex:").append(sex).append(";");
        str.append("jobName:").append(jobName).append(";");
        str.append("atk_type:").append(atk_type).append(";");
        return str.toString();
    }
}
