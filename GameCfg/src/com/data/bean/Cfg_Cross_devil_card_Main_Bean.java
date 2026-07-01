/**
 * Auto generated, do not edit it
 *
 * Cross_devil_card_Main配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Cross_devil_card_Main_Bean{
    /**
     * 卡片id
     */
    private final int id;
    /**
     * 卡片id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 卡片名字
     */
    private final String name;
    /**
     * 卡片名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 所属阵营
对应Cross_devil_card_Camp主键
     */
    private final int camp;
    /**
     * 所属阵营
对应Cross_devil_card_Camp主键
     * @return
     */
    public final int getCamp(){
        return camp;
    }
    /**
     * 开启条件
对应FunctionVariable
填空代表默认开启
     */
    private final ReadIntegerArrayEs condition;
    /**
     * 开启条件
对应FunctionVariable
填空代表默认开启
     * @return
     */
    public final ReadIntegerArrayEs getCondition(){
        return condition;
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

    public Cfg_Cross_devil_card_Main_Bean(int id,String name,int camp,String conditionStr,int notice,String chatchannelStr){
        this.id = id;
        this.name = name;
        this.camp = camp;
        this.condition = new ReadIntegerArrayEs(conditionStr,"}",",");
        this.notice = notice;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("camp:").append(camp).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("notice:").append(notice).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        return str.toString();
    }
}
