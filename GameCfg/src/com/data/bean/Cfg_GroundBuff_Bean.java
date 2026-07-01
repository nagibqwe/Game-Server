/**
 * Auto generated, do not edit it
 *
 * GroundBuff配置表
 */
package com.data.bean;

	
public class Cfg_GroundBuff_Bean{
    /**
     * id
     */
    private final int id;
    /**
     * id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 增加的buff id
     */
    private final int buff_id;
    /**
     * 增加的buff id
     * @return
     */
    public final int getBuff_id(){
        return buff_id;
    }
    /**
     * 使用的特效，other目录下的特效id
     */
    private final int res;
    /**
     * 使用的特效，other目录下的特效id
     * @return
     */
    public final int getRes(){
        return res;
    }
    /**
     * 碰撞半径（单位厘米）
     */
    private final int logic_body_size;
    /**
     * 碰撞半径（单位厘米）
     * @return
     */
    public final int getLogic_body_size(){
        return logic_body_size;
    }
    /**
     * 生效的坐标距离差
     */
    private final int disValue;
    /**
     * 生效的坐标距离差
     * @return
     */
    public final int getDisValue(){
        return disValue;
    }
    /**
     * 激活的次数，如果是临时则写1次就好
     */
    private final int activeTimes;
    /**
     * 激活的次数，如果是临时则写1次就好
     * @return
     */
    public final int getActiveTimes(){
        return activeTimes;
    }
    /**
     * 激活的时间间隔
     */
    private final int activeStep;
    /**
     * 激活的时间间隔
     * @return
     */
    public final int getActiveStep(){
        return activeStep;
    }
    /**
     * 设置阵营，0为所有阵营都加 其它值与玩家的阵营相同就可以加，-1为怪物阵营
     */
    private final int groupNo;
    /**
     * 设置阵营，0为所有阵营都加 其它值与玩家的阵营相同就可以加，-1为怪物阵营
     * @return
     */
    public final int getGroupNo(){
        return groupNo;
    }

    public Cfg_GroundBuff_Bean(int id,int buff_id,int res,int logic_body_size,int disValue,int activeTimes,int activeStep,int groupNo){
        this.id = id;
        this.buff_id = buff_id;
        this.res = res;
        this.logic_body_size = logic_body_size;
        this.disValue = disValue;
        this.activeTimes = activeTimes;
        this.activeStep = activeStep;
        this.groupNo = groupNo;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("buff_id:").append(buff_id).append(";");
        str.append("res:").append(res).append(";");
        str.append("logic_body_size:").append(logic_body_size).append(";");
        str.append("disValue:").append(disValue).append(";");
        str.append("activeTimes:").append(activeTimes).append(";");
        str.append("activeStep:").append(activeStep).append(";");
        str.append("groupNo:").append(groupNo).append(";");
        return str.toString();
    }
}
