/**
 * Auto generated, do not edit it
 *
 * social_house_furniture配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Social_house_furniture_Bean{
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
     * 名称
     */
    private final String name;
    /**
     * 名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 类型（1，主题；2，窗户；3， 家具；4，装饰）
     */
    private final int type;
    /**
     * 类型（1，主题；2，窗户；3， 家具；4，装饰）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 排序
     */
    private final int order;
    /**
     * 排序
     * @return
     */
    public final int getOrder(){
        return order;
    }
    /**
     * 套装
     */
    private final int suit;
    /**
     * 套装
     * @return
     */
    public final int getSuit(){
        return suit;
    }
    /**
     * ui资源
     */
    private final String ui_res;
    /**
     * ui资源
     * @return
     */
    public final String getUi_res(){
        return ui_res;
    }
    /**
     * 资源ID
     */
    private final ReadIntegerArray res;
    /**
     * 资源ID
     * @return
     */
    public final ReadIntegerArray getRes(){
        return res;
    }
    /**
     * 缩放大小_X轴偏移_Y轴偏移_Z轴偏移_X轴旋转_Y轴旋转_Z轴旋转
     */
    private final String show_parm;
    /**
     * 缩放大小_X轴偏移_Y轴偏移_Z轴偏移_X轴旋转_Y轴旋转_Z轴旋转
     * @return
     */
    public final String getShow_parm(){
        return show_parm;
    }
    /**
     * 增加的装饰度
     */
    private final int decorate_num;
    /**
     * 增加的装饰度
     * @return
     */
    public final int getDecorate_num(){
        return decorate_num;
    }
    /**
     * 格子数据0度
     */
    private final String cell_data_0;
    /**
     * 格子数据0度
     * @return
     */
    public final String getCell_data_0(){
        return cell_data_0;
    }
    /**
     * 格子数据90度
     */
    private final String cell_data_90;
    /**
     * 格子数据90度
     * @return
     */
    public final String getCell_data_90(){
        return cell_data_90;
    }
    /**
     * 格子数据180度
     */
    private final String cell_data_180;
    /**
     * 格子数据180度
     * @return
     */
    public final String getCell_data_180(){
        return cell_data_180;
    }
    /**
     * 格子数据270度
     */
    private final String cell_data_270;
    /**
     * 格子数据270度
     * @return
     */
    public final String getCell_data_270(){
        return cell_data_270;
    }

    public Cfg_Social_house_furniture_Bean(int id,String name,int type,int order,int suit,String ui_res,String resStr,String show_parm,int decorate_num,String cell_data_0,String cell_data_90,String cell_data_180,String cell_data_270){
        this.id = id;
        this.name = name;
        this.type = type;
        this.order = order;
        this.suit = suit;
        this.ui_res = ui_res;
        this.res = new ReadIntegerArray(resStr,",");
        this.show_parm = show_parm;
        this.decorate_num = decorate_num;
        this.cell_data_0 = cell_data_0;
        this.cell_data_90 = cell_data_90;
        this.cell_data_180 = cell_data_180;
        this.cell_data_270 = cell_data_270;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("type:").append(type).append(";");
        str.append("order:").append(order).append(";");
        str.append("suit:").append(suit).append(";");
        str.append("ui_res:").append(ui_res).append(";");
        str.append("res:").append(res).append(";");
        str.append("show_parm:").append(show_parm).append(";");
        str.append("decorate_num:").append(decorate_num).append(";");
        str.append("cell_data_0:").append(cell_data_0).append(";");
        str.append("cell_data_90:").append(cell_data_90).append(";");
        str.append("cell_data_180:").append(cell_data_180).append(";");
        str.append("cell_data_270:").append(cell_data_270).append(";");
        return str.toString();
    }
}
