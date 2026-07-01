/**
 * Auto generated, do not edit it
 *
 * Gather配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Gather_Bean{
    /**
     * 采集物ID
     */
    private final int id;
    /**
     * 采集物ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 名称
     */
    private final String name;
    /**
     * 名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 类型 0普通采集物（都可以采集） 1任务采集物（只有接受了某些任务才能采集）2婚宴食物（普通婚宴采集）3婚宴喜糖（喜从天降采集）
     */
    private final int type;
    /**
     * 类型 0普通采集物（都可以采集） 1任务采集物（只有接受了某些任务才能采集）2婚宴食物（普通婚宴采集）3婚宴喜糖（喜从天降采集）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 资源
     */
    private final int res;
    /**
     * 资源
     * @return
     */
    public final int getRes(){
        return res;
    }
    /**
     * 友方占领
     */
    private final int Friends_flag;
    /**
     * 友方占领
     * @return
     */
    public final int getFriends_flag(){
        return Friends_flag;
    }
    /**
     * 敌方占领
     */
    private final int Enemy_flag;
    /**
     * 敌方占领
     * @return
     */
    public final int getEnemy_flag(){
        return Enemy_flag;
    }
    /**
     * 模型缩放（百分比）
     */
    private final int size_scale;
    /**
     * 模型缩放（百分比）
     * @return
     */
    public final int getSize_scale(){
        return size_scale;
    }
    /**
     * 头像icon
     */
    private final int icon;
    /**
     * 头像icon
     * @return
     */
    public final int getIcon(){
        return icon;
    }
    /**
     * 采集时间(毫秒)
     */
    private final int collect_time;
    /**
     * 采集时间(毫秒)
     * @return
     */
    public final int getCollect_time(){
        return collect_time;
    }
    /**
     * 刷新时间(毫秒) client ignore
     */
    private final int refresh_time;
    /**
     * 刷新时间(毫秒) client ignore
     * @return
     */
    public final int getRefresh_time(){
        return refresh_time;
    }
    /**
     * 是否消失 client ignore
     */
    private final int isHide;
    /**
     * 是否消失 client ignore
     * @return
     */
    public final int getIsHide(){
        return isHide;
    }
    /**
     * 掉落ID client ignore
     */
    private final int dropId;
    /**
     * 掉落ID client ignore
     * @return
     */
    public final int getDropId(){
        return dropId;
    }
    /**
     * 采集完触发的脚本 client ignore
     */
    private final int scriptId;
    /**
     * 采集完触发的脚本 client ignore
     * @return
     */
    public final int getScriptId(){
        return scriptId;
    }
    /**
     * 掉落数量（目前只用于公会战）
     */
    private final int drop_num;
    /**
     * 掉落数量（目前只用于公会战）
     * @return
     */
    public final int getDrop_num(){
        return drop_num;
    }
    /**
     * 采集共享(1共享，0不共享）
     */
    private final int share;
    /**
     * 采集共享(1共享，0不共享）
     * @return
     */
    public final int getShare(){
        return share;
    }
    /**
     * 采集后Action Type(1:特效，2:动画,3:对话泡泡,10:混合)
     */
    private final int afterType;
    /**
     * 采集后Action Type(1:特效，2:动画,3:对话泡泡,10:混合)
     * @return
     */
    public final int getAfterType(){
        return afterType;
    }
    /**
     * 特效id
     */
    private final int effectId;
    /**
     * 特效id
     * @return
     */
    public final int getEffectId(){
        return effectId;
    }
    /**
     * 动作片段名字
     */
    private final String animName;
    /**
     * 动作片段名字
     * @return
     */
    public final String getAnimName(){
        return animName;
    }
    /**
     * 动作片段名字
     */
    private final String dialog;
    /**
     * 动作片段名字
     * @return
     */
    public final String getDialog(){
        return dialog;
    }
    /**
     * 混合Type_执行顺序（0：顺序，1同时执行）
     */
    private final ReadIntegerArray multType;
    /**
     * 混合Type_执行顺序（0：顺序，1同时执行）
     * @return
     */
    public final ReadIntegerArray getMultType(){
        return multType;
    }
    /**
     * 靠近是否显示按钮（(1是，0否））
     */
    private final int showButton;
    /**
     * 靠近是否显示按钮（(1是，0否））
     * @return
     */
    public final int getShowButton(){
        return showButton;
    }
    /**
     * 任务隐藏(@;@_@)
     */
    private final ReadIntegerArrayEs takHinde;
    /**
     * 任务隐藏(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getTakHinde(){
        return takHinde;
    }
    /**
     * 采集时增加的BUFF（采集取消后消失）
     */
    private final int getbuff;
    /**
     * 采集时增加的BUFF（采集取消后消失）
     * @return
     */
    public final int getGetbuff(){
        return getbuff;
    }
    /**
     * 采集时触发的动作（传功疗伤是0，蹲下采集是1，站立采集是2，祭拜是3，）
     */
    private final int animid;
    /**
     * 采集时触发的动作（传功疗伤是0，蹲下采集是1，站立采集是2，祭拜是3，）
     * @return
     */
    public final int getAnimid(){
        return animid;
    }

    public Cfg_Gather_Bean(int id,String name,int type,int res,int Friends_flag,int Enemy_flag,int size_scale,int icon,int collect_time,int refresh_time,int isHide,int dropId,int scriptId,int drop_num,int share,int afterType,int effectId,String animName,String dialog,String multTypeStr,int showButton,String takHindeStr,int getbuff,int animid){
        this.id = id;
        this.name = name;
        this.type = type;
        this.res = res;
        this.Friends_flag = Friends_flag;
        this.Enemy_flag = Enemy_flag;
        this.size_scale = size_scale;
        this.icon = icon;
        this.collect_time = collect_time;
        this.refresh_time = refresh_time;
        this.isHide = isHide;
        this.dropId = dropId;
        this.scriptId = scriptId;
        this.drop_num = drop_num;
        this.share = share;
        this.afterType = afterType;
        this.effectId = effectId;
        this.animName = animName;
        this.dialog = dialog;
        this.multType = new ReadIntegerArray(multTypeStr,",");
        this.showButton = showButton;
        this.takHinde = new ReadIntegerArrayEs(takHindeStr,"}",",");
        this.getbuff = getbuff;
        this.animid = animid;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("type:").append(type).append(";");
        str.append("res:").append(res).append(";");
        str.append("Friends_flag:").append(Friends_flag).append(";");
        str.append("Enemy_flag:").append(Enemy_flag).append(";");
        str.append("size_scale:").append(size_scale).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("collect_time:").append(collect_time).append(";");
        str.append("refresh_time:").append(refresh_time).append(";");
        str.append("isHide:").append(isHide).append(";");
        str.append("dropId:").append(dropId).append(";");
        str.append("scriptId:").append(scriptId).append(";");
        str.append("drop_num:").append(drop_num).append(";");
        str.append("share:").append(share).append(";");
        str.append("afterType:").append(afterType).append(";");
        str.append("effectId:").append(effectId).append(";");
        str.append("animName:").append(animName).append(";");
        str.append("dialog:").append(dialog).append(";");
        str.append("multType:").append(multType).append(";");
        str.append("showButton:").append(showButton).append(";");
        str.append("takHinde:").append(takHinde).append(";");
        str.append("getbuff:").append(getbuff).append(";");
        str.append("animid:").append(animid).append(";");
        return str.toString();
    }
}
