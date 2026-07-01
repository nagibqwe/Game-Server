/**
 * Auto generated, do not edit it
 *
 * wash配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Wash_Bean{
    /**
     * 流水号
     */
    private final int Id;
    /**
     * 流水号
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 装备部位(0头盔.1武器.2胸甲.3项链.4腰带.5腿甲.6鞋子.7戒指)
     */
    private final int type_eq;
    /**
     * 装备部位(0头盔.1武器.2胸甲.3项链.4腰带.5腿甲.6鞋子.7戒指)
     * @return
     */
    public final int getType_eq(){
        return type_eq;
    }
    /**
     * 条目序号1-5条目的序号
     */
    private final int type_v;
    /**
     * 条目序号1-5条目的序号
     * @return
     */
    public final int getType_v(){
        return type_v;
    }
    /**
     * 概率：下限,上限,权重(@;@_@)
     */
    private final ReadIntegerArrayEs probability;
    /**
     * 概率：下限,上限,权重(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getProbability(){
        return probability;
    }
    /**
     * 祝福值
     */
    private final int blessing;
    /**
     * 祝福值
     * @return
     */
    public final int getBlessing(){
        return blessing;
    }
    /**
     * 洗练最高档时的公告频道(10跑马灯)
     */
    private final int notice;
    /**
     * 洗练最高档时的公告频道(10跑马灯)
     * @return
     */
    public final int getNotice(){
        return notice;
    }
    /**
     * 洗练最高档时的聊天发送(0世界4系统14传闻）
     */
    private final ReadIntegerArray chatchannel;
    /**
     * 洗练最高档时的聊天发送(0世界4系统14传闻）
     * @return
     */
    public final ReadIntegerArray getChatchannel(){
        return chatchannel;
    }

    public Cfg_Wash_Bean(int Id,int type_eq,int type_v,String probabilityStr,int blessing,int notice,String chatchannelStr){
        this.Id = Id;
        this.type_eq = type_eq;
        this.type_v = type_v;
        this.probability = new ReadIntegerArrayEs(probabilityStr,"}",",");
        this.blessing = blessing;
        this.notice = notice;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("type_eq:").append(type_eq).append(";");
        str.append("type_v:").append(type_v).append(";");
        str.append("probability:").append(probability).append(";");
        str.append("blessing:").append(blessing).append(";");
        str.append("notice:").append(notice).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        return str.toString();
    }
}
