/**
 * Auto generated, do not edit it
 *
 * universe_command配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Universe_command_Bean{
    /**
     * 主键
     */
    private final int id;
    /**
     * 主键
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 对应人数(包含指挥在内的人数）
     */
    private final int count;
    /**
     * 对应人数(包含指挥在内的人数）
     * @return
     */
    public final int getCount(){
        return count;
    }
    /**
     * 人数对应增加的BUFF
     */
    private final int buff;
    /**
     * 人数对应增加的BUFF
     * @return
     */
    public final int getBuff(){
        return buff;
    }
    /**
     * 对应指挥官的奖励（邮件发放）
itemid_num_bind_occ
bind:0=不绑定，1=绑定
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 对应指挥官的奖励（邮件发放）
itemid_num_bind_occ
bind:0=不绑定，1=绑定
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }

    public Cfg_Universe_command_Bean(int id,int count,int buff,String rewardStr){
        this.id = id;
        this.count = count;
        this.buff = buff;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("count:").append(count).append(";");
        str.append("buff:").append(buff).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
