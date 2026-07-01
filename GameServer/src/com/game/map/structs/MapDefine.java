package com.game.map.structs;

/**
 * 地图枚举类
 *
 * @author format by luosv on 2018.06.14
 * @info MapState
 */
public class MapDefine {

    //地图类型
    /**
     * 登录场景配置
     */
    public static final int LOADING = -1;

    /**
     * 世界地图
     */
    public static final int WORLD_MAP = 0;

    /**
     * 副本地图
     */
    public static final int COPY_MAP = 1;

    /**
     * 竞技场地图
     */
    public static final int JJC_MAP = 2;

    /**
     * 帮会地图
     */
    public static final int GUILD_MAP = 3;

    public static final int Copy_Plane = 5;

    /**
     * 跨服世界野图
     */
    public static final int CROSS_WORLD_MAP = 3;

    //地图pk状态
    /**
     * 不允许pk
     */
    public static final int PK_STATE0 = 0;

    /**
     * 正常pk
     */
    public static final int PK_STATE1 = 1;

    /**
     * 无惩罚pk
     */
    public static final int PK_STATE2 = 2;


    /**
     * 最大距离偏差
     */
    public final static float MAX_ERROR_DISTANCE = 3.0f;


    //地图格子类型  大于2的类型可以移动， 大于1的可以跳跃
    /**
     * 普通地面
     */
    public final static int CELL_TYPE_NONE = 0;

    /**
     * 阻挡
     */
    public final static int CELL_TYPE_BLOCK = 1;

    /**
     * 可跳跃阻挡
     */
    public final static int CELL_TYPE_JUMP = 2;

    /**
     * 水面
     */
    public final static int CELL_TYPE_WATER = 3;

    /**
     * 草地
     */
    public final static int CELL_TYPE_GRASS = 4;

    /**
     * 砖石地面
     */
    public final static int CELL_TYPE_STONE = 5;

    /**
     * 沙地
     */
    public final static int CELL_TYPE_SAND = 6;

    /**
     * 沼泽
     */
    public final static int CELL_TYPE_MARSH = 7;

    /**
     * 木地板
     */
    public final static int CELL_TYPE_WOOD = 8;

    /**
     * 雪地
     */
    public final static int CELL_TYPE_SNOW = 9;

    /**
     * 用户设置的阻挡
     */
    public final static int CELL_TYPE_USER_BLOCK = 10;

    /**
     * 安全区
     */
    public final static int CELL_TYPE_SAFE = 11;


    /**
     * 动态阻挡类型
     */
    final static int DYNAMIC_BLOCK_NONE = 0;
    final static int DYNAMIC_BLOCK_CIRCLE = 1;
    final static int DYNAMIC_BLOCK_ABB = 2;
    final static int DYNAMIC_BLOCK_OBB = 3;


    //切换地图
    /**
     * 传送成功
     */
    public final static int CHANGE_MAP_RESULT_SUCCESS = 0;

    /**
     * 未知原因
     */
    public final static int CHANGE_MAP_RESULT_FAILED_NONE = -1;

    /**
     * 地图人数已满
     */
    public final static int CHANGE_MAP_RESULT_FAILED_PLAYER_OVERFLOW = -2;

    /**
     * 死亡中不能传送
     */
    public final static int CHANGE_MAP_RESULT_FAILED_PLAYER_DIE = -3;

    /**
     * 距离太远
     */
    public final static int CHANGE_MAP_RESULT_FAILED_DISTANCE_LONG = -4;

    /**
     * 不能传送到副本地图
     */
    public final static int CHANGE_MAP_RESULT_FAILED_TO_COPY_MAP = -5;

    /**
     * 没有找到目标地图
     */
    public final static int CHANGE_MAP_RESULT_FAILED_UN_FIND = -6;

    /**
     * 战斗状态不能切换
     */
    public final static int CHANGE_MAP_RESULT_FAILED_BATTLE = -7;

    /**
     * 组队状态不能进入
     */
    public final static int CHANGE_MAP_RESULT_FAILED_TEAM = -8;

    /**
     * 副本中不能传送
     */
    public final static int CHANGE_MAP_RESULT_FAILED_LOCATION_COPY_MAP = -9;

    /**
     * 跨服野图不在了，请等待
     */
    public final static int CHANGE_MAP_RESULT_FAILED_CROSS_MAP_NOT_SERVER = -10;

    /**
     * 跨服野图改变出错了
     */
    public final static int CHANGE_MAP_RESULT_FAILED_CROSS_MAP_CHANGE = -11;

    /**
     * 传送的时候，距离小于规则的距离了
     */
    public final static int TRANSPORT_ERROR_LESS30 = -12;


    /**
     * 切换地图类型
     */
    public final static int CHANGE_MAP_TYPE_FAILED = -1;
    public final static int CHANGE_MAP_TYPE_NONE = 0;
    public final static int CHANGE_MAP_TYPE_RELIVE = 1;
    public final static int CHANGE_MAP_TYPE_GM = 2;

}
