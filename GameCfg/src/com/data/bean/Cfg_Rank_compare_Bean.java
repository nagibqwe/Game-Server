/**
 * Auto generated, do not edit it
 *
 * Rank_compare配置表
 */
package com.data.bean;

	
public class Cfg_Rank_compare_Bean{
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
     * 对比名字，其他表示抛开上面
     */
    private final String name;
    /**
     * 对比名字，其他表示抛开上面
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 点击提升打开的功能面板
     */
    private final int promote;
    /**
     * 点击提升打开的功能面板
     * @return
     */
    public final int getPromote(){
        return promote;
    }

    public Cfg_Rank_compare_Bean(int id,String name,int promote){
        this.id = id;
        this.name = name;
        this.promote = promote;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("promote:").append(promote).append(";");
        return str.toString();
    }
}
