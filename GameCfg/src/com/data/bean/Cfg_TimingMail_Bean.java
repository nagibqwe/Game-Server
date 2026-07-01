/**
 * Auto generated, do not edit it
 *
 * TimingMail配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_TimingMail_Bean{
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
     * 邮件触发条件，对应FunctionVariable
可配置多个条件，同时满足则触发
     */
    private final ReadIntegerArrayEs condition;
    /**
     * 邮件触发条件，对应FunctionVariable
可配置多个条件，同时满足则触发
     * @return
     */
    public final ReadIntegerArrayEs getCondition(){
        return condition;
    }
    /**
     * 邮件标题
对应MessageString的ID字段
     */
    private final int MailTitle;
    /**
     * 邮件标题
对应MessageString的ID字段
     * @return
     */
    public final int getMailTitle(){
        return MailTitle;
    }
    /**
     * 邮件内容
对应MessageString的ID字段
     */
    private final int Mail;
    /**
     * 邮件内容
对应MessageString的ID字段
     * @return
     */
    public final int getMail(){
        return Mail;
    }
    /**
     * 奖励
item_num_bind_occ
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 奖励
item_num_bind_occ
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }

    public Cfg_TimingMail_Bean(int id,String conditionStr,int MailTitle,int Mail,String rewardStr){
        this.id = id;
        this.condition = new ReadIntegerArrayEs(conditionStr,"}",",");
        this.MailTitle = MailTitle;
        this.Mail = Mail;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("MailTitle:").append(MailTitle).append(";");
        str.append("Mail:").append(Mail).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
