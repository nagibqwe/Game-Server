/**
 * Auto generated, do not edit it
 *
 * SoulArmor_breach配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_SoulArmor_breach_Bean{
    /**
     * 流水id
     */
    private final int id;
    /**
     * 流水id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 魂甲品质
     */
    private final int quality;
    /**
     * 魂甲品质
     * @return
     */
    public final int getQuality(){
        return quality;
    }
    /**
     * 魂甲名称
     */
    private final String name;
    /**
     * 魂甲名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 外观id
     */
    private final int model;
    /**
     * 外观id
     * @return
     */
    public final int getModel(){
        return model;
    }
    /**
     * 升品消耗  道具id_数量;道具id_数量;道具id_数量;（需要集齐一起使用）
     */
    private final ReadIntegerArrayEs consume;
    /**
     * 升品消耗  道具id_数量;道具id_数量;道具id_数量;（需要集齐一起使用）
     * @return
     */
    public final ReadIntegerArrayEs getConsume(){
        return consume;
    }
    /**
     * 属性
     */
    private final ReadIntegerArrayEs qualityValue;
    /**
     * 属性
     * @return
     */
    public final ReadIntegerArrayEs getQualityValue(){
        return qualityValue;
    }
    /**
     * 专属属性
     */
    private final ReadIntegerArrayEs extraValue;
    /**
     * 专属属性
     * @return
     */
    public final ReadIntegerArrayEs getExtraValue(){
        return extraValue;
    }
    /**
     * 激活时的公告频道(10跑马灯)
     */
    private final int notice;
    /**
     * 激活时的公告频道(10跑马灯)
     * @return
     */
    public final int getNotice(){
        return notice;
    }
    /**
     * 掉落物品后聊天发送(0世界4系统14传闻）
     */
    private final ReadIntegerArray chatchannel;
    /**
     * 掉落物品后聊天发送(0世界4系统14传闻）
     * @return
     */
    public final ReadIntegerArray getChatchannel(){
        return chatchannel;
    }

    public Cfg_SoulArmor_breach_Bean(int id,int quality,String name,int model,String consumeStr,String qualityValueStr,String extraValueStr,int notice,String chatchannelStr){
        this.id = id;
        this.quality = quality;
        this.name = name;
        this.model = model;
        this.consume = new ReadIntegerArrayEs(consumeStr,"}",",");
        this.qualityValue = new ReadIntegerArrayEs(qualityValueStr,"}",",");
        this.extraValue = new ReadIntegerArrayEs(extraValueStr,"}",",");
        this.notice = notice;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("quality:").append(quality).append(";");
        str.append("name:").append(name).append(";");
        str.append("model:").append(model).append(";");
        str.append("consume:").append(consume).append(";");
        str.append("qualityValue:").append(qualityValue).append(";");
        str.append("extraValue:").append(extraValue).append(";");
        str.append("notice:").append(notice).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        return str.toString();
    }
}
