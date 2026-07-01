/**
 * Auto generated, do not edit it
 *
 * relive配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Relive_Bean{
    /**
     * 复活KEYid
     */
    private final int relive_id;
    /**
     * 复活KEYid
     * @return
     */
    public final int getRelive_id(){
        return relive_id;
    }
    /**
     * 是否显示击杀者名字
     */
    private final int show_killer_name;
    /**
     * 是否显示击杀者名字
     * @return
     */
    public final int getShow_killer_name(){
        return show_killer_name;
    }
    /**
     * 自动复活类型（0不自动复活，1自动原地复活，2自动安全复活）                         
     */
    private final int auto_relive_type;
    /**
     * 自动复活类型（0不自动复活，1自动原地复活，2自动安全复活）                         
     * @return
     */
    public final int getAuto_relive_type(){
        return auto_relive_type;
    }
    /**
     * 自动复活的时间
     */
    private final int auto_relive_time;
    /**
     * 自动复活的时间
     * @return
     */
    public final int getAuto_relive_time(){
        return auto_relive_time;
    }
    /**
     * 复活按钮显示（填空都不显示，0原地复活，1安全复活）
     */
    private final ReadIntegerArray Button_type;
    /**
     * 复活按钮显示（填空都不显示，0原地复活，1安全复活）
     * @return
     */
    public final ReadIntegerArray getButton_type(){
        return Button_type;
    }
    /**
     * 安全复活的等待时间
按钮上的
     */
    private final int safe_relive_time;
    /**
     * 安全复活的等待时间
按钮上的
     * @return
     */
    public final int getSafe_relive_time(){
        return safe_relive_time;
    }
    /**
     * 原地复活的初始等待时间
按钮上的
     */
    private final int situ_relive_time;
    /**
     * 原地复活的初始等待时间
按钮上的
     * @return
     */
    public final int getSitu_relive_time(){
        return situ_relive_time;
    }
    /**
     * 原地复活的额外时间增量和最大时间
     */
    private final ReadIntegerArray situ_relive_add_time;
    /**
     * 原地复活的额外时间增量和最大时间
     * @return
     */
    public final ReadIntegerArray getSitu_relive_add_time(){
        return situ_relive_add_time;
    }
    /**
     * 原地复活额外时间的清除时间间隔
     */
    private final int situ_relive_recovery_time;
    /**
     * 原地复活额外时间的清除时间间隔
     * @return
     */
    public final int getSitu_relive_recovery_time(){
        return situ_relive_recovery_time;
    }
    /**
     * 死亡界面是否显示“求援”按钮，点击后可向在线的同盟成员求援
0不显示，1显示
     */
    private final int isSeekHelp;
    /**
     * 死亡界面是否显示“求援”按钮，点击后可向在线的同盟成员求援
0不显示，1显示
     * @return
     */
    public final int getIsSeekHelp(){
        return isSeekHelp;
    }

    public Cfg_Relive_Bean(int relive_id,int show_killer_name,int auto_relive_type,int auto_relive_time,String Button_typeStr,int safe_relive_time,int situ_relive_time,String situ_relive_add_timeStr,int situ_relive_recovery_time,int isSeekHelp){
        this.relive_id = relive_id;
        this.show_killer_name = show_killer_name;
        this.auto_relive_type = auto_relive_type;
        this.auto_relive_time = auto_relive_time;
        this.Button_type = new ReadIntegerArray(Button_typeStr,",");
        this.safe_relive_time = safe_relive_time;
        this.situ_relive_time = situ_relive_time;
        this.situ_relive_add_time = new ReadIntegerArray(situ_relive_add_timeStr,",");
        this.situ_relive_recovery_time = situ_relive_recovery_time;
        this.isSeekHelp = isSeekHelp;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("relive_id:").append(relive_id).append(";");
        str.append("show_killer_name:").append(show_killer_name).append(";");
        str.append("auto_relive_type:").append(auto_relive_type).append(";");
        str.append("auto_relive_time:").append(auto_relive_time).append(";");
        str.append("Button_type:").append(Button_type).append(";");
        str.append("safe_relive_time:").append(safe_relive_time).append(";");
        str.append("situ_relive_time:").append(situ_relive_time).append(";");
        str.append("situ_relive_add_time:").append(situ_relive_add_time).append(";");
        str.append("situ_relive_recovery_time:").append(situ_relive_recovery_time).append(";");
        str.append("isSeekHelp:").append(isSeekHelp).append(";");
        return str.toString();
    }
}
