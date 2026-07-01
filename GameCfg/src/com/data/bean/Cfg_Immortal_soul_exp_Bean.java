/**
 * Auto generated, do not edit it
 *
 * immortal_soul_exp配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Immortal_soul_exp_Bean{
    /**
     * 等级
     */
    private final int level;
    /**
     * 等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 蓝色升下级经验_分解总经验
     */
    private final ReadIntegerArray blue_exp;
    /**
     * 蓝色升下级经验_分解总经验
     * @return
     */
    public final ReadIntegerArray getBlue_exp(){
        return blue_exp;
    }
    /**
     * 紫色升下级经验_分解总经验
     */
    private final ReadIntegerArray violet_exp;
    /**
     * 紫色升下级经验_分解总经验
     * @return
     */
    public final ReadIntegerArray getViolet_exp(){
        return violet_exp;
    }
    /**
     * 金色升下级经验_分解总经验
     */
    private final ReadIntegerArray golden_exp;
    /**
     * 金色升下级经验_分解总经验
     * @return
     */
    public final ReadIntegerArray getGolden_exp(){
        return golden_exp;
    }
    /**
     * 红色升下级经验_分解总经验
     */
    private final ReadIntegerArray gules_exp;
    /**
     * 红色升下级经验_分解总经验
     * @return
     */
    public final ReadIntegerArray getGules_exp(){
        return gules_exp;
    }

    public Cfg_Immortal_soul_exp_Bean(int level,String blue_expStr,String violet_expStr,String golden_expStr,String gules_expStr){
        this.level = level;
        this.blue_exp = new ReadIntegerArray(blue_expStr,",");
        this.violet_exp = new ReadIntegerArray(violet_expStr,",");
        this.golden_exp = new ReadIntegerArray(golden_expStr,",");
        this.gules_exp = new ReadIntegerArray(gules_expStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("level:").append(level).append(";");
        str.append("blue_exp:").append(blue_exp).append(";");
        str.append("violet_exp:").append(violet_exp).append(";");
        str.append("golden_exp:").append(golden_exp).append(";");
        str.append("gules_exp:").append(gules_exp).append(";");
        return str.toString();
    }
}
