/**
 * Auto generated, do not edit it
 *
 * title配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Title_Bean{
    /**
     * 称号ID，对应item中ID
     */
    private final int id;
    /**
     * 称号ID，对应item中ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 称号名称
     */
    private final String name;
    /**
     * 称号名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 分类排序
     */
    private final int type;
    /**
     * 分类排序
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 0是永久称号，>0为称号倒计时单位秒，从激活开始算（限时称号必定自动激活）
     */
    private final int time;
    /**
     * 0是永久称号，>0为称号倒计时单位秒，从激活开始算（限时称号必定自动激活）
     * @return
     */
    public final int getTime(){
        return time;
    }
    /**
     * 得到该道具后是否自动激活，0为不处理，1为自动激活（限时称号必定自动激活）
     */
    private final int activation;
    /**
     * 得到该道具后是否自动激活，0为不处理，1为自动激活（限时称号必定自动激活）
     * @return
     */
    public final int getActivation(){
        return activation;
    }
    /**
     * 称号图片资源
     */
    private final int textrue;
    /**
     * 称号图片资源
     * @return
     */
    public final int getTextrue(){
        return textrue;
    }
    /**
     * 特效称号id，没有填0
     */
    private final int vfx_title;
    /**
     * 特效称号id，没有填0
     * @return
     */
    public final int getVfx_title(){
        return vfx_title;
    }
    /**
     * 称号属性(@;@_@)
     */
    private final ReadIntegerArrayEs property;
    /**
     * 称号属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getProperty(){
        return property;
    }
    /**
     * 获取途径对应的functionstart的面板ID
     */
    private final int openFunc;
    /**
     * 获取途径对应的functionstart的面板ID
     * @return
     */
    public final int getOpenFunc(){
        return openFunc;
    }
    /**
     * 点击前往打开的功能参数
     */
    private final int funcParam;
    /**
     * 点击前往打开的功能参数
     * @return
     */
    public final int getFuncParam(){
        return funcParam;
    }
    /**
     * 是否在列表中显示(0否1是)
     */
    private final int canShow;
    /**
     * 是否在列表中显示(0否1是)
     * @return
     */
    public final int getCanShow(){
        return canShow;
    }
    /**
     * 称号的品质排序，默认穿戴最大品质的称号，数字越大品质越好，相同品质不替换
     */
    private final int quality;
    /**
     * 称号的品质排序，默认穿戴最大品质的称号，数字越大品质越好，相同品质不替换
     * @return
     */
    public final int getQuality(){
        return quality;
    }

    public Cfg_Title_Bean(int id,String name,int type,int time,int activation,int textrue,int vfx_title,String propertyStr,int openFunc,int funcParam,int canShow,int quality){
        this.id = id;
        this.name = name;
        this.type = type;
        this.time = time;
        this.activation = activation;
        this.textrue = textrue;
        this.vfx_title = vfx_title;
        this.property = new ReadIntegerArrayEs(propertyStr,"}",",");
        this.openFunc = openFunc;
        this.funcParam = funcParam;
        this.canShow = canShow;
        this.quality = quality;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("type:").append(type).append(";");
        str.append("time:").append(time).append(";");
        str.append("activation:").append(activation).append(";");
        str.append("textrue:").append(textrue).append(";");
        str.append("vfx_title:").append(vfx_title).append(";");
        str.append("property:").append(property).append(";");
        str.append("openFunc:").append(openFunc).append(";");
        str.append("funcParam:").append(funcParam).append(";");
        str.append("canShow:").append(canShow).append(";");
        str.append("quality:").append(quality).append(";");
        return str.toString();
    }
}
