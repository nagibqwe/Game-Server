/**
 * Auto generated, do not edit it
 *
 * GodWeaponSuit配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_GodWeaponSuit_Bean{
    /**
     * ID
     */
    private final int SuitId;
    /**
     * ID
     * @return
     */
    public final int getSuitId(){
        return SuitId;
    }
    /**
     * 套装名字
     */
    private final String suitname;
    /**
     * 套装名字
     * @return
     */
    public final String getSuitname(){
        return suitname;
    }
    /**
     * 套装属性属性(@;@_@)
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 套装属性属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 用于预览套装时候显示的的图片
     */
    private final String Icon;
    /**
     * 用于预览套装时候显示的的图片
     * @return
     */
    public final String getIcon(){
        return Icon;
    }
    /**
     * 用于电机预览套装时的展示图片
     */
    private final String Texture;
    /**
     * 用于电机预览套装时的展示图片
     * @return
     */
    public final String getTexture(){
        return Texture;
    }
    /**
     * 套装的数量
     */
    private final int num;
    /**
     * 套装的数量
     * @return
     */
    public final int getNum(){
        return num;
    }
    /**
     * 用于套装显示ID：头部，身体，特效
     */
    private final ReadIntegerArrayEs ModelId;
    /**
     * 用于套装显示ID：头部，身体，特效
     * @return
     */
    public final ReadIntegerArrayEs getModelId(){
        return ModelId;
    }
    /**
     * 展示面板显示的文字
     */
    private final String ShowTittle;
    /**
     * 展示面板显示的文字
     * @return
     */
    public final String getShowTittle(){
        return ShowTittle;
    }

    public Cfg_GodWeaponSuit_Bean(int SuitId,String suitname,String attributeStr,String Icon,String Texture,int num,String ModelIdStr,String ShowTittle){
        this.SuitId = SuitId;
        this.suitname = suitname;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.Icon = Icon;
        this.Texture = Texture;
        this.num = num;
        this.ModelId = new ReadIntegerArrayEs(ModelIdStr,"}",",");
        this.ShowTittle = ShowTittle;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("SuitId:").append(SuitId).append(";");
        str.append("suitname:").append(suitname).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("Icon:").append(Icon).append(";");
        str.append("Texture:").append(Texture).append(";");
        str.append("num:").append(num).append(";");
        str.append("ModelId:").append(ModelId).append(";");
        str.append("ShowTittle:").append(ShowTittle).append(";");
        return str.toString();
    }
}
