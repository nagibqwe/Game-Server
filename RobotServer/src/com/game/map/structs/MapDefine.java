package com.game.map.structs;

/**
 *
 * @info mapstate
 */
public class MapDefine {

    //地图类型
    public static final int Loading = -1; //登录场景配置
    public static final int WorldMap = 0; //世界地图 
    public static final int CopyMap = 1; //副本地图
    public static final int JJCMap = 2; //竞技场地图
    public static final int GUILDMAP = 3;//帮会地图
    public static final int CrossWorldMap = 4;//跨服世界野图

    //地图pk状态
    public static final int PkState0 = 0; //不允许pk
    public static final int PkState1 = 1; //正常pk
    public static final int PkState2 = 2; //无惩罚pk

    //最大距离偏差
    public final static float MaxErrorDistance = 3.0f;

    //地图格子类型  大于2的类型可以移动， 大于1的可以跳跃
    public final static int Cell_Type_None = 0;       //普通地面
    public final static int Cell_Type_Block = 1;      //阻挡
    public final static int Cell_Type_Jump = 2;      //可跳跃阻挡
    public final static int Cell_Type_Water = 3;      //水面
    public final static int Cell_Type_Grass = 4;      //草地
    public final static int Cell_Type_Stone = 5;      //砖石地面
    public final static int Cell_Type_Sand = 6;       //沙地
    public final static int Cell_Type_Marsh = 7;      //沼泽
    public final static int Cell_Type_Wood = 8;       //木地板
    public final static int Cell_Type_Snow = 9;       //雪地
    public final static int Cell_Type_UserBlock = 10; //用户设置的阻挡
    public final static int Cell_Type_Safe = 11;      //安全区

    //动态阻挡类型
    public final static int DynamicBlock_None = 0;
    public final static int DynamicBlock_Circle = 1;
    public final static int DynamicBlock_AABB = 2;
    public final static int DynamicBlock_OBB = 3;

    //切换地图
    public final static int ChangeMapResult_Success = 0;                //传送成功
    public final static int ChangeMapResult_Failed_None = -1;           //未知原因
    public final static int ChangeMapResult_Failed_PlayerOverflow = -2; //地图人数已满
    public final static int ChangeMapResult_Failed_PlayerDie = -3;      //死亡中不能传送 
    public final static int ChangeMapResult_Failed_DistanceLong = -4;   //距离太远
    public final static int ChangeMapResult_Failed_ToCopyMap = -5;      //不能传送到副本地图
    public final static int ChangeMapResult_Failed_UnFind = -6;         //没有找到目标地图
    public final static int ChangeMapResult_Failed_Battle = -7;         //战斗状态不能切换
    public final static int ChangeMapResult_Failed_Team = -8;           //组队状态不能进入
    public final static int ChangeMapResult_Failed_LocationCopyMap = -9; //副本中不能传送
    public final static int ChangeMapResult_Failed_CrossMapNOTServer = -10; //跨服野图不在了，请等待
    public final static int ChangeMapResult_Failed_CrossMapChange = -11; //跨服野图改变出错了
    //切换地图类型
    public final static int ChangeMapType_Failed = -1;
    public final static int ChangeMapType_None = 0;
    public final static int ChangeMapType_Relive = 1;
    public final static int ChangeMapType_GM = 2;

}
