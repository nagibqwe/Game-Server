/**
 * Auto generated, do not edit it
 *
 * social_house配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Social_house_Bean{
    /**
     * ID(按照ID进行规模排序）
     */
    private final int id;
    /**
     * ID(按照ID进行规模排序）
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
    /**
     * 对应的资源prefab ID
     */
    private final int map_id;
    /**
     * 对应的资源prefab ID
     * @return
     */
    public final int getMap_id(){
        return map_id;
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
     * 当前规模提升需要的货币_数量
     */
    private final ReadIntegerArray levelup_pay;
    /**
     * 当前规模提升需要的货币_数量
     * @return
     */
    public final ReadIntegerArray getLevelup_pay(){
        return levelup_pay;
    }
    /**
     * 长边对应的格子数量
     */
    private final int square_length;
    /**
     * 长边对应的格子数量
     * @return
     */
    public final int getSquare_length(){
        return square_length;
    }
    /**
     * 短边对应的格子数量
     */
    private final int square_width;
    /**
     * 短边对应的格子数量
     * @return
     */
    public final int getSquare_width(){
        return square_width;
    }

    public Cfg_Social_house_Bean(int id,String name,int map_id,int decorate_num,String levelup_payStr,int square_length,int square_width){
        this.id = id;
        this.name = name;
        this.map_id = map_id;
        this.decorate_num = decorate_num;
        this.levelup_pay = new ReadIntegerArray(levelup_payStr,",");
        this.square_length = square_length;
        this.square_width = square_width;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("map_id:").append(map_id).append(";");
        str.append("decorate_num:").append(decorate_num).append(";");
        str.append("levelup_pay:").append(levelup_pay).append(";");
        str.append("square_length:").append(square_length).append(";");
        str.append("square_width:").append(square_width).append(";");
        return str.toString();
    }
}
