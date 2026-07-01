/**
 * Auto generated, do not edit it
 *
 * immortal_soul_core配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Immortal_soul_core_Bean{
    /**
     * 核心ID（所属剑灵）
     */
    private final int ID;
    /**
     * 核心ID（所属剑灵）
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 品质
     */
    private final ReadIntegerArrayEs quality;
    /**
     * 品质
     * @return
     */
    public final ReadIntegerArrayEs getQuality(){
        return quality;
    }
    /**
     * 星级
     */
    private final ReadIntegerArrayEs star;
    /**
     * 星级
     * @return
     */
    public final ReadIntegerArrayEs getStar(){
        return star;
    }

    public Cfg_Immortal_soul_core_Bean(int ID,String qualityStr,String starStr){
        this.ID = ID;
        this.quality = new ReadIntegerArrayEs(qualityStr,"}",",");
        this.star = new ReadIntegerArrayEs(starStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("quality:").append(quality).append(";");
        str.append("star:").append(star).append(";");
        return str.toString();
    }
}
