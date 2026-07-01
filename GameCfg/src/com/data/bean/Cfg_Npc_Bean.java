/**
 * Auto generated, do not edit it
 *
 * npc配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadStringArray; 
	
public class Cfg_Npc_Bean{
    /**
     * NPC编号
     */
    private final int id;
    /**
     * NPC编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * NPC名字
     */
    private final String name;
    /**
     * NPC名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 造型资源编号（资源使用：单方向6帧）
     */
    private final int res;
    /**
     * 造型资源编号（资源使用：单方向6帧）
     * @return
     */
    public final int getRes(){
        return res;
    }
    /**
     * 任务模型缩放比(只针对任务对话)
     */
    private final int zoom;
    /**
     * 任务模型缩放比(只针对任务对话)
     * @return
     */
    public final int getZoom(){
        return zoom;
    }
    /**
     * 对话中模型X坐标
untiy参数+180
     */
    private final int pos_X;
    /**
     * 对话中模型X坐标
untiy参数+180
     * @return
     */
    public final int getPos_X(){
        return pos_X;
    }
    /**
     * 对话中模型Y坐标
     */
    private final int pos_Y;
    /**
     * 对话中模型Y坐标
     * @return
     */
    public final int getPos_Y(){
        return pos_Y;
    }
    /**
     * 对话中模型旋转值
     */
    private final int notation;
    /**
     * 对话中模型旋转值
     * @return
     */
    public final int getNotation(){
        return notation;
    }
    /**
     * 头像资源编号
     */
    private final int icon;
    /**
     * 头像资源编号
     * @return
     */
    public final int getIcon(){
        return icon;
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
     * NPC隐藏（用于任务,任务类型_任务ID;任务类型_任务ID）(@;@_@)主线任务配0_任务ID
     */
    private final ReadIntegerArrayEs taskShow;
    /**
     * NPC隐藏（用于任务,任务类型_任务ID;任务类型_任务ID）(@;@_@)主线任务配0_任务ID
     * @return
     */
    public final ReadIntegerArrayEs getTaskShow(){
        return taskShow;
    }
    /**
     * 发言ID列表分号隔开
     */
    private final ReadIntegerArray speakIds;
    /**
     * 发言ID列表分号隔开
     * @return
     */
    public final ReadIntegerArray getSpeakIds(){
        return speakIds;
    }
    /**
     * 是否使用玩家模型（0不使用/1使用）
     */
    private final int PlayerModel;
    /**
     * 是否使用玩家模型（0不使用/1使用）
     * @return
     */
    public final int getPlayerModel(){
        return PlayerModel;
    }
    /**
     * 使用玩家的模型ID（身体ID_武器ID_阵道ID_光环ID_坐骑id_魂甲id）
     */
    private final ReadIntegerArray PlayerModelRes;
    /**
     * 使用玩家的模型ID（身体ID_武器ID_阵道ID_光环ID_坐骑id_魂甲id）
     * @return
     */
    public final ReadIntegerArray getPlayerModelRes(){
        return PlayerModelRes;
    }
    /**
     * NPC对话时需要职业（-1为所有职业）
     */
    private final int professional;
    /**
     * NPC对话时需要职业（-1为所有职业）
     * @return
     */
    public final int getProfessional(){
        return professional;
    }
    /**
     * 是否显示配置表名字0否1是2面片模型
     */
    private final int ShowCfgName;
    /**
     * 是否显示配置表名字0否1是2面片模型
     * @return
     */
    public final int getShowCfgName(){
        return ShowCfgName;
    }
    /**
     * 是否像服务器请求消息（0不请求）
     */
    private final int IsReq_NPC;
    /**
     * 是否像服务器请求消息（0不请求）
     * @return
     */
    public final int getIsReq_NPC(){
        return IsReq_NPC;
    }
    /**
     * NPC的功能类型,0:默认,1:进入一个功能,2:进入副本,3:切换场景
     */
    private final int funcType;
    /**
     * NPC的功能类型,0:默认,1:进入一个功能,2:进入副本,3:切换场景
     * @return
     */
    public final int getFuncType(){
        return funcType;
    }
    /**
     * 和前一列关联,funcType=0,不填
funcType=1,填写functionstartID
funcType=2,填写clonemapID
funcType=3,填写mapsettingID
     */
    private final int funcParam;
    /**
     * 和前一列关联,funcType=0,不填
funcType=1,填写functionstartID
funcType=2,填写clonemapID
funcType=3,填写mapsettingID
     * @return
     */
    public final int getFuncParam(){
        return funcParam;
    }
    /**
     * NPC对话功能面板（1.显示按钮 2.仙盟副本按钮 3.显示倒计时按钮）
     */
    private final int NpcTalkBtn;
    /**
     * NPC对话功能面板（1.显示按钮 2.仙盟副本按钮 3.显示倒计时按钮）
     * @return
     */
    public final int getNpcTalkBtn(){
        return NpcTalkBtn;
    }
    /**
     * 按钮显示文字
     */
    private final String TalkBtnName;
    /**
     * 按钮显示文字
     * @return
     */
    public final String getTalkBtnName(){
        return TalkBtnName;
    }
    /**
     * 
     */
    private final int BtnFunction;
    /**
     * 
     * @return
     */
    public final int getBtnFunction(){
        return BtnFunction;
    }
    /**
     * 填写预设好的npc窗体,1:通用主线副本描述
     */
    private final int npcFormType;
    /**
     * 填写预设好的npc窗体,1:通用主线副本描述
     * @return
     */
    public final int getNpcFormType(){
        return npcFormType;
    }
    /**
     * 存在时间（出生年-月-日_消失年-月-日)（client ignore)
     */
    private final ReadStringArray Limit_time;
    /**
     * 存在时间（出生年-月-日_消失年-月-日)（client ignore)
     * @return
     */
    public final ReadStringArray getLimit_time(){
        return Limit_time;
    }
    /**
     * 脚本刷新坐标(x_z_旋转)（client ignore)
     */
    private final ReadIntegerArray loaction;
    /**
     * 脚本刷新坐标(x_z_旋转)（client ignore)
     * @return
     */
    public final ReadIntegerArray getLoaction(){
        return loaction;
    }

    public Cfg_Npc_Bean(int id,String name,int res,int zoom,int pos_X,int pos_Y,int notation,int icon,int size_scale,String taskShowStr,String speakIdsStr,int PlayerModel,String PlayerModelResStr,int professional,int ShowCfgName,int IsReq_NPC,int funcType,int funcParam,int NpcTalkBtn,String TalkBtnName,int BtnFunction,int npcFormType,String Limit_timeStr,String loactionStr){
        this.id = id;
        this.name = name;
        this.res = res;
        this.zoom = zoom;
        this.pos_X = pos_X;
        this.pos_Y = pos_Y;
        this.notation = notation;
        this.icon = icon;
        this.size_scale = size_scale;
        this.taskShow = new ReadIntegerArrayEs(taskShowStr,"}",",");
        this.speakIds = new ReadIntegerArray(speakIdsStr,",");
        this.PlayerModel = PlayerModel;
        this.PlayerModelRes = new ReadIntegerArray(PlayerModelResStr,",");
        this.professional = professional;
        this.ShowCfgName = ShowCfgName;
        this.IsReq_NPC = IsReq_NPC;
        this.funcType = funcType;
        this.funcParam = funcParam;
        this.NpcTalkBtn = NpcTalkBtn;
        this.TalkBtnName = TalkBtnName;
        this.BtnFunction = BtnFunction;
        this.npcFormType = npcFormType;
        this.Limit_time = new ReadStringArray(Limit_timeStr,",");
        this.loaction = new ReadIntegerArray(loactionStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("res:").append(res).append(";");
        str.append("zoom:").append(zoom).append(";");
        str.append("pos_X:").append(pos_X).append(";");
        str.append("pos_Y:").append(pos_Y).append(";");
        str.append("notation:").append(notation).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("size_scale:").append(size_scale).append(";");
        str.append("taskShow:").append(taskShow).append(";");
        str.append("speakIds:").append(speakIds).append(";");
        str.append("PlayerModel:").append(PlayerModel).append(";");
        str.append("PlayerModelRes:").append(PlayerModelRes).append(";");
        str.append("professional:").append(professional).append(";");
        str.append("ShowCfgName:").append(ShowCfgName).append(";");
        str.append("IsReq_NPC:").append(IsReq_NPC).append(";");
        str.append("funcType:").append(funcType).append(";");
        str.append("funcParam:").append(funcParam).append(";");
        str.append("NpcTalkBtn:").append(NpcTalkBtn).append(";");
        str.append("TalkBtnName:").append(TalkBtnName).append(";");
        str.append("BtnFunction:").append(BtnFunction).append(";");
        str.append("npcFormType:").append(npcFormType).append(";");
        str.append("Limit_time:").append(Limit_time).append(";");
        str.append("loaction:").append(loaction).append(";");
        return str.toString();
    }
}
