/**
 * Auto generated, do not edit it
 *
 * shielding_symbol配置表
 */
package com.data.bean;

	
public class Cfg_Shielding_symbol_Bean{
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
     * 内容
     */
    private final String type;
    /**
     * 内容
     * @return
     */
    public final String getType(){
        return type;
    }

    public Cfg_Shielding_symbol_Bean(int id,String type){
        this.id = id;
        this.type = type;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        return str.toString();
    }
}
