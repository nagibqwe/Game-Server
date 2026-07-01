package com.game.control.script;

import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.control.structs.FuncOpenData;
import com.game.player.structs.Player;
import game.core.script.IScript;

import java.util.List;

public interface IControlScript extends IScript {

    /**
     * 启服加载
     */
    void load();

    /**
     * 热更新reload
     */
    void reload();

    /**
     * 登陆发送
     * @param player
     */
    void login(Player player);

    /**
     * 后台开关是否开放
     * @param funcId
     */
    boolean isBackOpen(int funcId);

    /**
     * 后台开关状态列表
     * @return
     */
    List<FuncOpenData> getBackFuncList();

    /**
     * 后台改变开关状态
     * @param funcId
     * @param funcState
     * @return
     */
    void changeBackFunc(int funcId, int funcState);

    /**
     * 后台改变开关列表状态
     * @param list
     * @return
     */
    boolean changeBackFuncs(List<FuncOpenData> list);

    /**
     * 玩家功能是否已开启
     * @param player
     * @param funcId
     * @return
     */
    boolean isOpenFunction(Player player, int funcId);

    /**
     * 检查某一个条件满足就true
     * @param player
     * @param params
     * @return
     */
    boolean checkFuncProgressSomeone(Player player, ReadIntegerArrayEs params);

    /**
     * 检查是否满足条件
     * @param player
     * @param params
     * @return
     */
    boolean checkFuncProgress(Player player, ReadIntegerArrayEs params);

    /**
     * 检查是否满足条件
     * @param player
     * @param param
     * @return
     */
    boolean checkFuncProgress(Player player, ReadArray<Integer> param);


    /**
     * 获取FunctionVariable特定id的当前进度
     * @param player
     * @param param
     * @return
     */
    int getFuncProgress(Player player, ReadArray<Integer> param);

    /**
     * 检查类型点完成后功能是否开放
     * @param player
     * @param OverType
     */
    void operateFuncOpen(Player player, int OverType);

    /**
     * gm开放所有功能
     * @param player
     */
    void gmFuncOpen(Player player);

    /**
     * 功能领奖
     * @param player
     * @param id
     */
    void onFuncReward(Player player, int id);

    /**
     * 功能关闭
     * @param player
     */
    void onFuncClose(Player player);

    /**
     * 开关变化同步
     * @param player
     */
    void changePlayerFunc(Player player);

    /**
     * 改变某些开关的同步处理
     * @param player
     * @param funcIDs
     */
    void changePlayerFunc(Player player,int[] funcIDs);

    /**
     * 状态改变
     * @param player
     * @param type
     * @param changeNum
     */
    void operate(Player player, int type, int changeNum);

    /**
     * 状态改变
     * @param player
     * @param types
     * @param changeNum
     */
    void operate(Player player, int changeNum, int... types);

}
