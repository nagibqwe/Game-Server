/**
 * Auto generated, do not edit it
 *
 * attributeAdd配置表
 */
package com.data.bean;

	
public class Cfg_AttributeAdd_Bean{
    /**
     * 变量ID(大于1000的不会在角色属性面板显示)
     */
    private final int id;
    /**
     * 变量ID(大于1000的不会在角色属性面板显示)
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 变量
     */
    private final int variable;
    /**
     * 变量
     * @return
     */
    public final int getVariable(){
        return variable;
    }
    /**
     * 是否显示为百分比
     */
    private final int show_percent;
    /**
     * 是否显示为百分比
     * @return
     */
    public final int getShow_percent(){
        return show_percent;
    }
    /**
     * 客户端是否显示(0不显示,1基础属性，2详细属性)
     */
    private final int hidden;
    /**
     * 客户端是否显示(0不显示,1基础属性，2详细属性)
     * @return
     */
    public final int getHidden(){
        return hidden;
    }
    /**
     * 客户端显示排序(从1开始,越小排序越在前面)
     */
    private final int sorting;
    /**
     * 客户端显示排序(从1开始,越小排序越在前面)
     * @return
     */
    public final int getSorting(){
        return sorting;
    }

    public Cfg_AttributeAdd_Bean(int id,int variable,int show_percent,int hidden,int sorting){
        this.id = id;
        this.variable = variable;
        this.show_percent = show_percent;
        this.hidden = hidden;
        this.sorting = sorting;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("variable:").append(variable).append(";");
        str.append("show_percent:").append(show_percent).append(";");
        str.append("hidden:").append(hidden).append(";");
        str.append("sorting:").append(sorting).append(";");
        return str.toString();
    }
}
