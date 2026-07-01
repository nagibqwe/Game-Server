/**
 * Auto generated, do not edit it
 *
 * offstring配置表
 */
package com.data.bean;

	
public class Cfg_Offstring_Bean{
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
     * forbiddenStr
     */
    private final String forbiddenStr;
    /**
     * forbiddenStr
     * @return
     */
    public final String getForbiddenStr(){
        return forbiddenStr;
    }

    public Cfg_Offstring_Bean(int id,String forbiddenStr){
        this.id = id;
        this.forbiddenStr = forbiddenStr;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("forbiddenStr:").append(forbiddenStr).append(";");
        return str.toString();
    }
}
