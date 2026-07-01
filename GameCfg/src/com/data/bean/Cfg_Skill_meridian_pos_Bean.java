/**
 * Auto generated, do not edit it
 *
 * skill_meridian_pos配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Skill_meridian_pos_Bean{
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
     * 需求转职等级
     */
    private final int change_job;
    /**
     * 需求转职等级
     * @return
     */
    public final int getChange_job(){
        return change_job;
    }
    /**
     * 心法ID
     */
    private final int xinfa_id;
    /**
     * 心法ID
     * @return
     */
    public final int getXinfa_id(){
        return xinfa_id;
    }
    /**
     * 职业
     */
    private final int occ;
    /**
     * 职业
     * @return
     */
    public final int getOcc(){
        return occ;
    }
    /**
     * 经脉名字
     */
    private final String name;
    /**
     * 经脉名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 格子数据，每个格子上的经脉ID
     */
    private final ReadIntegerArray use_gezi;
    /**
     * 格子数据，每个格子上的经脉ID
     * @return
     */
    public final ReadIntegerArray getUse_gezi(){
        return use_gezi;
    }
    /**
     * 
     */
    private final String xinfa_icon;
    /**
     * 
     * @return
     */
    public final String getXinfa_icon(){
        return xinfa_icon;
    }

    public Cfg_Skill_meridian_pos_Bean(int id,int change_job,int xinfa_id,int occ,String name,String use_geziStr,String xinfa_icon){
        this.id = id;
        this.change_job = change_job;
        this.xinfa_id = xinfa_id;
        this.occ = occ;
        this.name = name;
        this.use_gezi = new ReadIntegerArray(use_geziStr,",");
        this.xinfa_icon = xinfa_icon;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("change_job:").append(change_job).append(";");
        str.append("xinfa_id:").append(xinfa_id).append(";");
        str.append("occ:").append(occ).append(";");
        str.append("name:").append(name).append(";");
        str.append("use_gezi:").append(use_gezi).append(";");
        str.append("xinfa_icon:").append(xinfa_icon).append(";");
        return str.toString();
    }
}
