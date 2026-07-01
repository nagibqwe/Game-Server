/**
 * Auto generated, do not edit it
 *
 * marry_show配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Marry_show_Bean{
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
     * 0：总任务
1：拥有一个异性好友
2：和任意异性亲密度达到520以上
3：发送或接收到金玉良缘或以上的求婚
4：成功缔结一次仙缘
5：成功预约一场婚礼
6：成功举办一场婚礼
     */
    private final int type;
    /**
     * 0：总任务
1：拥有一个异性好友
2：和任意异性亲密度达到520以上
3：发送或接收到金玉良缘或以上的求婚
4：成功缔结一次仙缘
5：成功预约一场婚礼
6：成功举办一场婚礼
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 奖励
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 奖励
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }

    public Cfg_Marry_show_Bean(int id,int type,String rewardStr){
        this.id = id;
        this.type = type;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
