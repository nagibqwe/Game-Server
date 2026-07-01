/**
 * Auto generated, do not edit it
 *
 * changejob配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Changejob_Bean{
    /**
     * 洗髓阶段ID
     */
    private final int id;
    /**
     * 洗髓阶段ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 洗髓名称
     */
    private final String changejob_name;
    /**
     * 洗髓名称
     * @return
     */
    public final String getChangejob_name(){
        return changejob_name;
    }
    /**
     * 开放条件
     */
    private final int changejob_condition;
    /**
     * 开放条件
     * @return
     */
    public final int getChangejob_condition(){
        return changejob_condition;
    }
    /**
     * 洗髓任务组ID
     */
    private final ReadIntegerArray task_group;
    /**
     * 洗髓任务组ID
     * @return
     */
    public final ReadIntegerArray getTask_group(){
        return task_group;
    }
    /**
     * 完成后增加属性
     */
    private final ReadIntegerArrayEs contribute_describe;
    /**
     * 完成后增加属性
     * @return
     */
    public final ReadIntegerArrayEs getContribute_describe(){
        return contribute_describe;
    }
    /**
     * 解锁时装
     */
    private final int model_change;
    /**
     * 解锁时装
     * @return
     */
    public final int getModel_change(){
        return model_change;
    }
    /**
     * 转职完成奖励(道具_数量_绑定_职业)
     */
    private final ReadIntegerArrayEs changejob_reward;
    /**
     * 转职完成奖励(道具_数量_绑定_职业)
     * @return
     */
    public final ReadIntegerArrayEs getChangejob_reward(){
        return changejob_reward;
    }
    /**
     * 激活时的公告频道(10跑马灯)
     */
    private final int notice;
    /**
     * 激活时的公告频道(10跑马灯)
     * @return
     */
    public final int getNotice(){
        return notice;
    }
    /**
     * 掉落物品后聊天发送(0世界4系统14传闻）
     */
    private final ReadIntegerArray chatchannel;
    /**
     * 掉落物品后聊天发送(0世界4系统14传闻）
     * @return
     */
    public final ReadIntegerArray getChatchannel(){
        return chatchannel;
    }

    public Cfg_Changejob_Bean(int id,String changejob_name,int changejob_condition,String task_groupStr,String contribute_describeStr,int model_change,String changejob_rewardStr,int notice,String chatchannelStr){
        this.id = id;
        this.changejob_name = changejob_name;
        this.changejob_condition = changejob_condition;
        this.task_group = new ReadIntegerArray(task_groupStr,",");
        this.contribute_describe = new ReadIntegerArrayEs(contribute_describeStr,"}",",");
        this.model_change = model_change;
        this.changejob_reward = new ReadIntegerArrayEs(changejob_rewardStr,"}",",");
        this.notice = notice;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("changejob_name:").append(changejob_name).append(";");
        str.append("changejob_condition:").append(changejob_condition).append(";");
        str.append("task_group:").append(task_group).append(";");
        str.append("contribute_describe:").append(contribute_describe).append(";");
        str.append("model_change:").append(model_change).append(";");
        str.append("changejob_reward:").append(changejob_reward).append(";");
        str.append("notice:").append(notice).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        return str.toString();
    }
}
