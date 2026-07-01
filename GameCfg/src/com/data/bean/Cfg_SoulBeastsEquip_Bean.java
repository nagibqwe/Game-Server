/**
 * Auto generated, do not edit it
 *
 * SoulBeastsEquip配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_SoulBeastsEquip_Bean{
    /**
     * 装备id,(id构成：部位，职业，品质，等级）（3000000+品质*1000+钻石*100+部位）
     */
    private final int Id;
    /**
     * 装备id,(id构成：部位，职业，品质，等级）（3000000+品质*1000+钻石*100+部位）
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 
1表示1个钻石，2表示2个钻石，0表示没有钻石，钻石显示为左上角
     */
    private final int diamond_Number;
    /**
     * 
1表示1个钻石，2表示2个钻石，0表示没有钻石，钻石显示为左上角
     * @return
     */
    public final int getDiamond_Number(){
        return diamond_Number;
    }
    /**
     * 物品天生颜色（1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩）
     */
    private final int quality;
    /**
     * 物品天生颜色（1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩）
     * @return
     */
    public final int getQuality(){
        return quality;
    }
    /**
     * 装备部位(1头盔、2项圈、3铠甲、4利爪、5羽翼)
     */
    private final int part;
    /**
     * 装备部位(1头盔、2项圈、3铠甲、4利爪、5羽翼)
     * @return
     */
    public final int getPart(){
        return part;
    }
    /**
     * 绑定类型(0：不绑定;1：获得时绑定;2：使用后绑定)
     */
    private final int bind;
    /**
     * 绑定类型(0：不绑定;1：获得时绑定;2：使用后绑定)
     * @return
     */
    public final int getBind(){
        return bind;
    }
    /**
     * 初始属性：属性类型_数值，属性类型1_数值，(@;@_@)
     */
    private final ReadIntegerArrayEs attribute1;
    /**
     * 初始属性：属性类型_数值，属性类型1_数值，(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute1(){
        return attribute1;
    }
    /**
     * 出售给予的货币(@_@)
     */
    private final ReadIntegerArray seal_num;
    /**
     * 出售给予的货币(@_@)
     * @return
     */
    public final ReadIntegerArray getSeal_num(){
        return seal_num;
    }
    /**
     * 回收时是弹出确认提示（0：不弹出;1：弹出）
     */
    private final int confirm;
    /**
     * 回收时是弹出确认提示（0：不弹出;1：弹出）
     * @return
     */
    public final int getConfirm(){
        return confirm;
    }
    /**
     * 是否记录产出日志（0：不记录;1：记录）client ignore
     */
    private final int log;
    /**
     * 是否记录产出日志（0：不记录;1：记录）client ignore
     * @return
     */
    public final int getLog(){
        return log;
    }
    /**
     * 推荐极品属性显示
     */
    private final String recommended_tips;
    /**
     * 推荐极品属性显示
     * @return
     */
    public final String getRecommended_tips(){
        return recommended_tips;
    }
    /**
     * 能否进行合成（0，可以，1不行）
     */
    private final int if_ban;
    /**
     * 能否进行合成（0，可以，1不行）
     * @return
     */
    public final int getIf_ban(){
        return if_ban;
    }
    /**
     * 目标装备ID
     */
    private final ReadIntegerArray target_equip;
    /**
     * 目标装备ID
     * @return
     */
    public final ReadIntegerArray getTarget_equip(){
        return target_equip;
    }
    /**
     * 需求道具（道具ID_数量）(@_@)
     */
    private final ReadIntegerArray demand_item;
    /**
     * 需求道具（道具ID_数量）(@_@)
     * @return
     */
    public final ReadIntegerArray getDemand_item(){
        return demand_item;
    }
    /**
     * 合成大成功的概率（万分比）
     */
    private final int bigsuccess;
    /**
     * 合成大成功的概率（万分比）
     * @return
     */
    public final int getBigsuccess(){
        return bigsuccess;
    }
    /**
     * 目标装备ID
     */
    private final ReadIntegerArray bigsuccess_target_equip;
    /**
     * 目标装备ID
     * @return
     */
    public final ReadIntegerArray getBigsuccess_target_equip(){
        return bigsuccess_target_equip;
    }
    /**
     * 强化提供经验
     */
    private final int seal_exp;
    /**
     * 强化提供经验
     * @return
     */
    public final int getSeal_exp(){
        return seal_exp;
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

    public Cfg_SoulBeastsEquip_Bean(int Id,int diamond_Number,int quality,int part,int bind,String attribute1Str,String seal_numStr,int confirm,int log,String recommended_tips,int if_ban,String target_equipStr,String demand_itemStr,int bigsuccess,String bigsuccess_target_equipStr,int seal_exp,int notice,String chatchannelStr){
        this.Id = Id;
        this.diamond_Number = diamond_Number;
        this.quality = quality;
        this.part = part;
        this.bind = bind;
        this.attribute1 = new ReadIntegerArrayEs(attribute1Str,"}",",");
        this.seal_num = new ReadIntegerArray(seal_numStr,",");
        this.confirm = confirm;
        this.log = log;
        this.recommended_tips = recommended_tips;
        this.if_ban = if_ban;
        this.target_equip = new ReadIntegerArray(target_equipStr,",");
        this.demand_item = new ReadIntegerArray(demand_itemStr,",");
        this.bigsuccess = bigsuccess;
        this.bigsuccess_target_equip = new ReadIntegerArray(bigsuccess_target_equipStr,",");
        this.seal_exp = seal_exp;
        this.notice = notice;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("diamond_Number:").append(diamond_Number).append(";");
        str.append("quality:").append(quality).append(";");
        str.append("part:").append(part).append(";");
        str.append("bind:").append(bind).append(";");
        str.append("attribute1:").append(attribute1).append(";");
        str.append("seal_num:").append(seal_num).append(";");
        str.append("confirm:").append(confirm).append(";");
        str.append("log:").append(log).append(";");
        str.append("recommended_tips:").append(recommended_tips).append(";");
        str.append("if_ban:").append(if_ban).append(";");
        str.append("target_equip:").append(target_equip).append(";");
        str.append("demand_item:").append(demand_item).append(";");
        str.append("bigsuccess:").append(bigsuccess).append(";");
        str.append("bigsuccess_target_equip:").append(bigsuccess_target_equip).append(";");
        str.append("seal_exp:").append(seal_exp).append(";");
        str.append("notice:").append(notice).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        return str.toString();
    }
}
