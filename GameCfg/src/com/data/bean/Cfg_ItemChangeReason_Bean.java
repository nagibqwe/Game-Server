/**
 * Auto generated, do not edit it
 *
 * ItemChangeReason配置表
 */
package com.data.bean;

	
public class Cfg_ItemChangeReason_Bean{
    /**
     * 变量ID
     */
    private final int id;
    /**
     * 变量ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 名字
     */
    private final String name;
    /**
     * 名字
     * @return
     */
    public final String getName(){
        return name;
    }

    public Cfg_ItemChangeReason_Bean(int id,String name){
        this.id = id;
        this.name = name;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        return str.toString();
    }
}
