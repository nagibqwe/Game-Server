/**
 * Auto generated, do not edit it
 *
 * Cross_fudi_main配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Cross_fudi_main_Bean{
    /**
     * ID
     */
    private final int id;
    /**
     * ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 跨服阶段
     */
    private final int cross_stage;
    /**
     * 跨服阶段
     * @return
     */
    public final int getCross_stage(){
        return cross_stage;
    }
    /**
     * 福地等级（0，出生点；1，一级福地；2，二级福地；3，三级福地）
     */
    private final int position;
    /**
     * 福地等级（0，出生点；1，一级福地；2，二级福地；3，三级福地）
     * @return
     */
    public final int getPosition(){
        return position;
    }
    /**
     * 占领该福地后能够进入的福地ID
     */
    private final ReadIntegerArray enter_position;
    /**
     * 占领该福地后能够进入的福地ID
     * @return
     */
    public final ReadIntegerArray getEnter_position(){
        return enter_position;
    }
    /**
     * 连线
     */
    private final ReadIntegerArray line;
    /**
     * 连线
     * @return
     */
    public final ReadIntegerArray getLine(){
        return line;
    }
    /**
     * 对应的副本ID
     */
    private final int clone_id;
    /**
     * 对应的副本ID
     * @return
     */
    public final int getClone_id(){
        return clone_id;
    }
    /**
     * 每天的最大天禁值
     */
    private final int max_tianjin;
    /**
     * 每天的最大天禁值
     * @return
     */
    public final int getMax_tianjin(){
        return max_tianjin;
    }
    /**
     * 立即占领福地需要的占领积分client ignore
     */
    private final int occupy_personal_score;
    /**
     * 立即占领福地需要的占领积分client ignore
     * @return
     */
    public final int getOccupy_personal_score(){
        return occupy_personal_score;
    }
    /**
     * 刷新时刻
     */
    private final ReadIntegerArray Refresh_time;
    /**
     * 刷新时刻
     * @return
     */
    public final ReadIntegerArray getRefresh_time(){
        return Refresh_time;
    }

    public Cfg_Cross_fudi_main_Bean(int id,int cross_stage,int position,String enter_positionStr,String lineStr,int clone_id,int max_tianjin,int occupy_personal_score,String Refresh_timeStr){
        this.id = id;
        this.cross_stage = cross_stage;
        this.position = position;
        this.enter_position = new ReadIntegerArray(enter_positionStr,",");
        this.line = new ReadIntegerArray(lineStr,",");
        this.clone_id = clone_id;
        this.max_tianjin = max_tianjin;
        this.occupy_personal_score = occupy_personal_score;
        this.Refresh_time = new ReadIntegerArray(Refresh_timeStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("cross_stage:").append(cross_stage).append(";");
        str.append("position:").append(position).append(";");
        str.append("enter_position:").append(enter_position).append(";");
        str.append("line:").append(line).append(";");
        str.append("clone_id:").append(clone_id).append(";");
        str.append("max_tianjin:").append(max_tianjin).append(";");
        str.append("occupy_personal_score:").append(occupy_personal_score).append(";");
        str.append("Refresh_time:").append(Refresh_time).append(";");
        return str.toString();
    }
}
