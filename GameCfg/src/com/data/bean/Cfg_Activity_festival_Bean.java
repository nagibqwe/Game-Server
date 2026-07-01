/**
 * Auto generated, do not edit it
 *
 * activity_festival配置表
 */
package com.data.bean;

	
public class Cfg_Activity_festival_Bean{
    /**
     * key值
     */
    private final int id;
    /**
     * key值
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 节日名字
     */
    private final String f_name;
    /**
     * 节日名字
     * @return
     */
    public final String getF_name(){
        return f_name;
    }

    public Cfg_Activity_festival_Bean(int id,String f_name){
        this.id = id;
        this.f_name = f_name;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("f_name:").append(f_name).append(";");
        return str.toString();
    }
}
