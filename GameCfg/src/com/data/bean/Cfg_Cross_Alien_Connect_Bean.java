/**
 * Auto generated, do not edit it
 *
 * Cross_Alien_Connect配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Cross_Alien_Connect_Bean{
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
     * 副本类型
（1：一级副本
2：二级副本
3：三级副本）
     */
    private final int type;
    /**
     * 副本类型
（1：一级副本
2：二级副本
3：三级副本）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 链接城市
（对应Cross_fudi_main主键）
     */
    private final ReadIntegerArray ConnectCity;
    /**
     * 链接城市
（对应Cross_fudi_main主键）
     * @return
     */
    public final ReadIntegerArray getConnectCity(){
        return ConnectCity;
    }
    /**
     * 副本名字

     */
    private final String copyName;
    /**
     * 副本名字

     * @return
     */
    public final String getCopyName(){
        return copyName;
    }
    /**
     * 副本ID
对应clone_map表主键
     */
    private final int copyId;
    /**
     * 副本ID
对应clone_map表主键
     * @return
     */
    public final int getCopyId(){
        return copyId;
    }
    /**
     * 对应须弥宝库的副本
对应Cross_Alien_Gem_Copy主键
     */
    private final int GemCopy;
    /**
     * 对应须弥宝库的副本
对应Cross_Alien_Gem_Copy主键
     * @return
     */
    public final int getGemCopy(){
        return GemCopy;
    }

    public Cfg_Cross_Alien_Connect_Bean(int id,int type,String ConnectCityStr,String copyName,int copyId,int GemCopy){
        this.id = id;
        this.type = type;
        this.ConnectCity = new ReadIntegerArray(ConnectCityStr,",");
        this.copyName = copyName;
        this.copyId = copyId;
        this.GemCopy = GemCopy;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("ConnectCity:").append(ConnectCity).append(";");
        str.append("copyName:").append(copyName).append(";");
        str.append("copyId:").append(copyId).append(";");
        str.append("GemCopy:").append(GemCopy).append(";");
        return str.toString();
    }
}
