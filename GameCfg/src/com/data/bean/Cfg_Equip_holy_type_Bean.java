/**
 * Auto generated, do not edit it
 *
 * Equip_holy_type配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Equip_holy_type_Bean{
    /**
     * 类型id
     */
    private final int id;
    /**
     * 类型id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 类型名字
     */
    private final String name;
    /**
     * 类型名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 开启天数，由开服时间决定
     */
    private final int openFunction;
    /**
     * 开启天数，由开服时间决定
     * @return
     */
    public final int getOpenFunction(){
        return openFunction;
    }
    /**
     * 界面显示的部位
     */
    private final ReadIntegerArray parts_list;
    /**
     * 界面显示的部位
     * @return
     */
    public final ReadIntegerArray getParts_list(){
        return parts_list;
    }
    /**
     * 最大强化等级
     */
    private final int max_level;
    /**
     * 最大强化等级
     * @return
     */
    public final int getMax_level(){
        return max_level;
    }
    /**
     * 魂装外观要求阶数
     */
    private final int hun_need_degree;
    /**
     * 魂装外观要求阶数
     * @return
     */
    public final int getHun_need_degree(){
        return hun_need_degree;
    }
    /**
     * 魂装激活给与的时装ID
填写fashion_total的主键
时装自带男女区分
     */
    private final int hun_fashion_id;
    /**
     * 魂装激活给与的时装ID
填写fashion_total的主键
时装自带男女区分
     * @return
     */
    public final int getHun_fashion_id(){
        return hun_fashion_id;
    }
    /**
     * 魄装外观要求阶数
     */
    private final int po_need_degree;
    /**
     * 魄装外观要求阶数
     * @return
     */
    public final int getPo_need_degree(){
        return po_need_degree;
    }
    /**
     * 魄装激活给与的时装ID
填写fashion_total的主键
时装自带男女区分
     */
    private final int po_fashion_id;
    /**
     * 魄装激活给与的时装ID
填写fashion_total的主键
时装自带男女区分
     * @return
     */
    public final int getPo_fashion_id(){
        return po_fashion_id;
    }
    /**
     * 觉醒要求的最低阶数
     */
    private final int awaken_degree;
    /**
     * 觉醒要求的最低阶数
     * @return
     */
    public final int getAwaken_degree(){
        return awaken_degree;
    }
    /**
     * 觉醒要求最低品质
     */
    private final int awaken_quality;
    /**
     * 觉醒要求最低品质
     * @return
     */
    public final int getAwaken_quality(){
        return awaken_quality;
    }
    /**
     * 觉醒魂装给与的时装ID
填写fashion_total的主键
时装自带男女区分
     */
    private final int awaken_hun_fashion_id;
    /**
     * 觉醒魂装给与的时装ID
填写fashion_total的主键
时装自带男女区分
     * @return
     */
    public final int getAwaken_hun_fashion_id(){
        return awaken_hun_fashion_id;
    }
    /**
     * 觉醒魄装给与的时装ID
填写fashion_total的主键
时装自带男女区分
     */
    private final int awaken_po_fashion_id;
    /**
     * 觉醒魄装给与的时装ID
填写fashion_total的主键
时装自带男女区分
     * @return
     */
    public final int getAwaken_po_fashion_id(){
        return awaken_po_fashion_id;
    }

    public Cfg_Equip_holy_type_Bean(int id,String name,int openFunction,String parts_listStr,int max_level,int hun_need_degree,int hun_fashion_id,int po_need_degree,int po_fashion_id,int awaken_degree,int awaken_quality,int awaken_hun_fashion_id,int awaken_po_fashion_id){
        this.id = id;
        this.name = name;
        this.openFunction = openFunction;
        this.parts_list = new ReadIntegerArray(parts_listStr,",");
        this.max_level = max_level;
        this.hun_need_degree = hun_need_degree;
        this.hun_fashion_id = hun_fashion_id;
        this.po_need_degree = po_need_degree;
        this.po_fashion_id = po_fashion_id;
        this.awaken_degree = awaken_degree;
        this.awaken_quality = awaken_quality;
        this.awaken_hun_fashion_id = awaken_hun_fashion_id;
        this.awaken_po_fashion_id = awaken_po_fashion_id;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("openFunction:").append(openFunction).append(";");
        str.append("parts_list:").append(parts_list).append(";");
        str.append("max_level:").append(max_level).append(";");
        str.append("hun_need_degree:").append(hun_need_degree).append(";");
        str.append("hun_fashion_id:").append(hun_fashion_id).append(";");
        str.append("po_need_degree:").append(po_need_degree).append(";");
        str.append("po_fashion_id:").append(po_fashion_id).append(";");
        str.append("awaken_degree:").append(awaken_degree).append(";");
        str.append("awaken_quality:").append(awaken_quality).append(";");
        str.append("awaken_hun_fashion_id:").append(awaken_hun_fashion_id).append(";");
        str.append("awaken_po_fashion_id:").append(awaken_po_fashion_id).append(";");
        return str.toString();
    }
}
