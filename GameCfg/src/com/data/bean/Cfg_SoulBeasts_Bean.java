/**
 * Auto generated, do not edit it
 *
 * SoulBeasts配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_SoulBeasts_Bean{
    /**
     * 魂兽ID
     */
    private final int id;
    /**
     * 魂兽ID
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
     * 头像ID
     */
    private final int icon;
    /**
     * 头像ID
     * @return
     */
    public final int getIcon(){
        return icon;
    }
    /**
     * 大图片ID
     */
    private final int text;
    /**
     * 大图片ID
     * @return
     */
    public final int getText(){
        return text;
    }
    /**
     * 是否显示(0否1是)
     */
    private final int canShow;
    /**
     * 是否显示(0否1是)
     * @return
     */
    public final int getCanShow(){
        return canShow;
    }
    /**
     * 激活要求装备部位ID_品质_星数
1头盔、2项圈、3铠甲、4利爪、5羽翼
     */
    private final ReadIntegerArrayEs needEquip;
    /**
     * 激活要求装备部位ID_品质_星数
1头盔、2项圈、3铠甲、4利爪、5羽翼
     * @return
     */
    public final ReadIntegerArrayEs getNeedEquip(){
        return needEquip;
    }
    /**
     * 附加属性
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 附加属性
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 附加技能
     */
    private final ReadIntegerArray skill;
    /**
     * 附加技能
     * @return
     */
    public final ReadIntegerArray getSkill(){
        return skill;
    }
    /**
     * 提升魂兽系统属性的技能（属性ID_值_生效方式，生效方式1单个魂兽，2全体助战魂兽）client ignore
     */
    private final ReadIntegerArrayEs att_skill;
    /**
     * 提升魂兽系统属性的技能（属性ID_值_生效方式，生效方式1单个魂兽，2全体助战魂兽）client ignore
     * @return
     */
    public final ReadIntegerArrayEs getAtt_skill(){
        return att_skill;
    }
    /**
     * 激活时的公告频道(4跑马灯)
     */
    private final int notice;
    /**
     * 激活时的公告频道(4跑马灯)
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

    public Cfg_SoulBeasts_Bean(int id,String name,int icon,int text,int canShow,String needEquipStr,String attributeStr,String skillStr,String att_skillStr,int notice,String chatchannelStr){
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.text = text;
        this.canShow = canShow;
        this.needEquip = new ReadIntegerArrayEs(needEquipStr,"}",",");
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.skill = new ReadIntegerArray(skillStr,",");
        this.att_skill = new ReadIntegerArrayEs(att_skillStr,"}",",");
        this.notice = notice;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("text:").append(text).append(";");
        str.append("canShow:").append(canShow).append(";");
        str.append("needEquip:").append(needEquip).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("skill:").append(skill).append(";");
        str.append("att_skill:").append(att_skill).append(";");
        str.append("notice:").append(notice).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        return str.toString();
    }
}
