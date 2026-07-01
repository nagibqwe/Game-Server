/**
 * Auto generated, do not edit it
 *
 * robotrandomname配置表
 */
package com.data.bean;

	
public class Cfg_Robotrandomname_Bean{
    /**
     * 表编号
     */
    private final int q_id;
    /**
     * 表编号
     * @return
     */
    public final int getQ_id(){
        return q_id;
    }
    /**
     * 类型（1姓，2男名，3女名）
     */
    private final int q_type;
    /**
     * 类型（1姓，2男名，3女名）
     * @return
     */
    public final int getQ_type(){
        return q_type;
    }
    /**
     * 内容
     */
    private final String q_value;
    /**
     * 内容
     * @return
     */
    public final String getQ_value(){
        return q_value;
    }

    public Cfg_Robotrandomname_Bean(int q_id,int q_type,String q_value){
        this.q_id = q_id;
        this.q_type = q_type;
        this.q_value = q_value;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("q_id:").append(q_id).append(";");
        str.append("q_type:").append(q_type).append(";");
        str.append("q_value:").append(q_value).append(";");
        return str.toString();
    }
}
