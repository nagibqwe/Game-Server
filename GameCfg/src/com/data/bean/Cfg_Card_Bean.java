/**
 * Auto generated, do not edit it
 *
 * card配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Card_Bean{
    /**
     * 图鉴ID
     */
    private final int id;
    /**
     * 图鉴ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 地图ID
     */
    private final int mapId;
    /**
     * 地图ID
     * @return
     */
    public final int getMapId(){
        return mapId;
    }
    /**
     * 怪物ID
     */
    private final int monsterId;
    /**
     * 怪物ID
     * @return
     */
    public final int getMonsterId(){
        return monsterId;
    }
    /**
     * 模型ID
     */
    private final int modelID;
    /**
     * 模型ID
     * @return
     */
    public final int getModelID(){
        return modelID;
    }
    /**
     * 缩放比例（百分比，100表示原大小）
     */
    private final int modeSize;
    /**
     * 缩放比例（百分比，100表示原大小）
     * @return
     */
    public final int getModeSize(){
        return modeSize;
    }
    /**
     * 图鉴序列小于100
     */
    private final int serial;
    /**
     * 图鉴序列小于100
     * @return
     */
    public final int getSerial(){
        return serial;
    }
    /**
     * 等级小于1000
     */
    private final int level;
    /**
     * 等级小于1000
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 升级经验
     */
    private final int exp;
    /**
     * 升级经验
     * @return
     */
    public final int getExp(){
        return exp;
    }
    /**
     * 升级提升属性(@;@_@)
     */
    private final ReadIntegerArrayEs property;
    /**
     * 升级提升属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getProperty(){
        return property;
    }
    /**
     * 本级总属性(@;@_@)
     */
    private final ReadIntegerArrayEs total_pro;
    /**
     * 本级总属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getTotal_pro(){
        return total_pro;
    }
    /**
     * 升级需要物品
     */
    private final int upItem;
    /**
     * 升级需要物品
     * @return
     */
    public final int getUpItem(){
        return upItem;
    }

    public Cfg_Card_Bean(int id,int mapId,int monsterId,int modelID,int modeSize,int serial,int level,int exp,String propertyStr,String total_proStr,int upItem){
        this.id = id;
        this.mapId = mapId;
        this.monsterId = monsterId;
        this.modelID = modelID;
        this.modeSize = modeSize;
        this.serial = serial;
        this.level = level;
        this.exp = exp;
        this.property = new ReadIntegerArrayEs(propertyStr,"}",",");
        this.total_pro = new ReadIntegerArrayEs(total_proStr,"}",",");
        this.upItem = upItem;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("mapId:").append(mapId).append(";");
        str.append("monsterId:").append(monsterId).append(";");
        str.append("modelID:").append(modelID).append(";");
        str.append("modeSize:").append(modeSize).append(";");
        str.append("serial:").append(serial).append(";");
        str.append("level:").append(level).append(";");
        str.append("exp:").append(exp).append(";");
        str.append("property:").append(property).append(";");
        str.append("total_pro:").append(total_pro).append(";");
        str.append("upItem:").append(upItem).append(";");
        return str.toString();
    }
}
