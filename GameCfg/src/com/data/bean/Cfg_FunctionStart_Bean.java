/**
 * Auto generated, do not edit it
 *
 * FunctionStart配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_FunctionStart_Bean{
    /**
     * 功能id 主功能ID为10000到990000
     */
    private final int function_id;
    /**
     * 功能id 主功能ID为10000到990000
     * @return
     */
    public final int getFunction_id(){
        return function_id;
    }
    /**
     * id配置，用于生成代码， （枚举值;注释）
     */
    private final String id_code;
    /**
     * id配置，用于生成代码， （枚举值;注释）
     * @return
     */
    public final String getId_code(){
        return id_code;
    }
    /**
     * 父功能Id
     */
    private final int parent_id;
    /**
     * 父功能Id
     * @return
     */
    public final int getParent_id(){
        return parent_id;
    }
    /**
     * 打开菜单，只在主菜单和顶部菜单的功能有效
     */
    private final int open_menu;
    /**
     * 打开菜单，只在主菜单和顶部菜单的功能有效
     * @return
     */
    public final int getOpen_menu(){
        return open_menu;
    }
    /**
     * 功能排序:在底部按钮就根据值从小到达从左到右排序，顶部就先根据值来决定是第几排（1-99第一排，100-199第二排以此类推）还要根据值来决定在收缩的时候是否隐藏（第一排1到49不隐藏，其他隐藏，以此类推）
     */
    private final int function_sort_num;
    /**
     * 功能排序:在底部按钮就根据值从小到达从左到右排序，顶部就先根据值来决定是第几排（1-99第一排，100-199第二排以此类推）还要根据值来决定在收缩的时候是否隐藏（第一排1到49不隐藏，其他隐藏，以此类推）
     * @return
     */
    public final int getFunction_sort_num(){
        return function_sort_num;
    }
    /**
     * 功能是否在跨服中显示（0不显示，1显示）
     */
    private final int function_in_cross;
    /**
     * 功能是否在跨服中显示（0不显示，1显示）
     * @return
     */
    public final int getFunction_in_cross(){
        return function_in_cross;
    }
    /**
     * 开启需要的变量列表(变量取值参考FunctionVariable配置表)(@_@)
     */
    private final ReadIntegerArrayEs start_variables;
    /**
     * 开启需要的变量列表(变量取值参考FunctionVariable配置表)(@_@)
     * @return
     */
    public final ReadIntegerArrayEs getStart_variables(){
        return start_variables;
    }
    /**
     * 功能名称
     */
    private final String function_name;
    /**
     * 功能名称
     * @return
     */
    public final String getFunction_name(){
        return function_name;
    }
    /**
     * 是否纳入监测快捷提升（0，不监测；1，监测）
     */
    private final int guide;
    /**
     * 是否纳入监测快捷提升（0，不监测；1，监测）
     * @return
     */
    public final int getGuide(){
        return guide;
    }
    /**
     * 是否纳入后台开关
（0否，1是）
     */
    private final int back_switch;
    /**
     * 是否纳入后台开关
（0否，1是）
     * @return
     */
    public final int getBack_switch(){
        return back_switch;
    }

    public Cfg_FunctionStart_Bean(int function_id,String id_code,int parent_id,int open_menu,int function_sort_num,int function_in_cross,String start_variablesStr,String function_name,int guide,int back_switch){
        this.function_id = function_id;
        this.id_code = id_code;
        this.parent_id = parent_id;
        this.open_menu = open_menu;
        this.function_sort_num = function_sort_num;
        this.function_in_cross = function_in_cross;
        this.start_variables = new ReadIntegerArrayEs(start_variablesStr,"}",",");
        this.function_name = function_name;
        this.guide = guide;
        this.back_switch = back_switch;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("function_id:").append(function_id).append(";");
        str.append("id_code:").append(id_code).append(";");
        str.append("parent_id:").append(parent_id).append(";");
        str.append("open_menu:").append(open_menu).append(";");
        str.append("function_sort_num:").append(function_sort_num).append(";");
        str.append("function_in_cross:").append(function_in_cross).append(";");
        str.append("start_variables:").append(start_variables).append(";");
        str.append("function_name:").append(function_name).append(";");
        str.append("guide:").append(guide).append(";");
        str.append("back_switch:").append(back_switch).append(";");
        return str.toString();
    }
}
