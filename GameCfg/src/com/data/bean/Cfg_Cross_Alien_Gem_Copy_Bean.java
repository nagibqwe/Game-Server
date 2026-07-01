/**
 * Auto generated, do not edit it
 *
 * Cross_Alien_Gem_Copy配置表
 */
package com.data.bean;

	
public class Cfg_Cross_Alien_Gem_Copy_Bean{
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
     * 对应clone_map的主键
     */
    private final int cloneMap;
    /**
     * 对应clone_map的主键
     * @return
     */
    public final int getCloneMap(){
        return cloneMap;
    }

    public Cfg_Cross_Alien_Gem_Copy_Bean(int id,int cloneMap){
        this.id = id;
        this.cloneMap = cloneMap;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("cloneMap:").append(cloneMap).append(";");
        return str.toString();
    }
}
