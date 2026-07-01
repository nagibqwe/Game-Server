/**
 * Auto generated, do not edit it
 *
 * state_stifle_add配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_State_stifle_add_Bean{
    /**
     * ID（类型*100+进化等级）
     */
    private final int id;
    /**
     * ID（类型*100+进化等级）
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 类型（1：经验器灵；2：战斗器灵；3：追击器灵）
     */
    private final int type;
    /**
     * 类型（1：经验器灵；2：战斗器灵；3：追击器灵）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     *  进化等级
     */
    private final int jinhua_level;
    /**
     *  进化等级
     * @return
     */
    public final int getJinhua_level(){
        return jinhua_level;
    }
    /**
     * 进化需要的物品_数量；
     */
    private final ReadIntegerArrayEs jinhua_need_item;
    /**
     * 进化需要的物品_数量；
     * @return
     */
    public final ReadIntegerArrayEs getJinhua_need_item(){
        return jinhua_need_item;
    }
    /**
     * 进化需要的货币_数量；
     */
    private final ReadIntegerArray jinhua_need_money;
    /**
     * 进化需要的货币_数量；
     * @return
     */
    public final ReadIntegerArray getJinhua_need_money(){
        return jinhua_need_money;
    }
    /**
     * 进化的成功率（万分比）
     */
    private final int jinghua_succes;
    /**
     * 进化的成功率（万分比）
     * @return
     */
    public final int getJinghua_succes(){
        return jinghua_succes;
    }
    /**
     * 核心核心属性
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 核心核心属性
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 核心百分比属性
     */
    private final ReadIntegerArrayEs per_attribute;
    /**
     * 核心百分比属性
     * @return
     */
    public final ReadIntegerArrayEs getPer_attribute(){
        return per_attribute;
    }
    /**
     * 核心技能
     */
    private final ReadIntegerArray skill;
    /**
     * 核心技能
     * @return
     */
    public final ReadIntegerArray getSkill(){
        return skill;
    }
    /**
     * 界面上核心属性描述
     */
    private final String max_times;
    /**
     * 界面上核心属性描述
     * @return
     */
    public final String getMax_times(){
        return max_times;
    }
    /**
     * 是否为当前版本满级
     */
    private final int if_max;
    /**
     * 是否为当前版本满级
     * @return
     */
    public final int getIf_max(){
        return if_max;
    }
    /**
     * 开启需要的法宝等级ID
     */
    private final int need_level;
    /**
     * 开启需要的法宝等级ID
     * @return
     */
    public final int getNeed_level(){
        return need_level;
    }
    /**
     * 需要的物品
     */
    private final ReadIntegerArray need_item;
    /**
     * 需要的物品
     * @return
     */
    public final ReadIntegerArray getNeed_item(){
        return need_item;
    }
    /**
     * 进化界面上显示的文字
     */
    private final String add_tips;
    /**
     * 进化界面上显示的文字
     * @return
     */
    public final String getAdd_tips(){
        return add_tips;
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

    public Cfg_State_stifle_add_Bean(int id,int type,int jinhua_level,String jinhua_need_itemStr,String jinhua_need_moneyStr,int jinghua_succes,String attributeStr,String per_attributeStr,String skillStr,String max_times,int if_max,int need_level,String need_itemStr,String add_tips,int notice,String chatchannelStr){
        this.id = id;
        this.type = type;
        this.jinhua_level = jinhua_level;
        this.jinhua_need_item = new ReadIntegerArrayEs(jinhua_need_itemStr,"}",",");
        this.jinhua_need_money = new ReadIntegerArray(jinhua_need_moneyStr,",");
        this.jinghua_succes = jinghua_succes;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.per_attribute = new ReadIntegerArrayEs(per_attributeStr,"}",",");
        this.skill = new ReadIntegerArray(skillStr,",");
        this.max_times = max_times;
        this.if_max = if_max;
        this.need_level = need_level;
        this.need_item = new ReadIntegerArray(need_itemStr,",");
        this.add_tips = add_tips;
        this.notice = notice;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("jinhua_level:").append(jinhua_level).append(";");
        str.append("jinhua_need_item:").append(jinhua_need_item).append(";");
        str.append("jinhua_need_money:").append(jinhua_need_money).append(";");
        str.append("jinghua_succes:").append(jinghua_succes).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("per_attribute:").append(per_attribute).append(";");
        str.append("skill:").append(skill).append(";");
        str.append("max_times:").append(max_times).append(";");
        str.append("if_max:").append(if_max).append(";");
        str.append("need_level:").append(need_level).append(";");
        str.append("need_item:").append(need_item).append(";");
        str.append("add_tips:").append(add_tips).append(";");
        str.append("notice:").append(notice).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        return str.toString();
    }
}
