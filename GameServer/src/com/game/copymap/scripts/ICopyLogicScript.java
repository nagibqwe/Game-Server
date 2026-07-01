package com.game.copymap.scripts;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.CopyMapMessage;

public interface ICopyLogicScript extends IScript {

    /**
     * 副本鼓舞
     * @param player
     * @param type
     */
    void onReqUpMorale(Player player, int type);

    /**
     * 跨服副本鼓舞
     * @param player
     * @param dec
     * @param type
     */
    void onReqCrossUpMorale(Player player, boolean dec, int type);

    /**
     * vip购买副本次数
     *
     * @param cloneId 副本id
     */
    void vipBuyCount(Player player, int cloneId);

    /**
     * 改变副本设置
     */
    void changeCloneSetting(Player player, CopyMapMessage.ReqCopySetting mess);

    /**
     * 请求多人副本面板信息
     */
    void onReqManyCopyPanel(Player player, CopyMapMessage.ReqOpenManyCopyPanel mess);

    /**
     * 检测副本挑战次数
     */
    boolean checkEnterTimes(Player player, int copyId);


    /**
     * 获取副本的合并次数
     * @param copyId 副本id
     */
    int getMergeCount(Player player, int copyId);

    /**
     * 请求副本战报
     */
    void OnReqCloneFightInfo(Player player, CopyMapMessage.ReqCloneFightInfo mess);

    /**
     * 副本BI数据
     *
     * @param instance_id 副本id
     * @param instance_status 副本状态 0进入/1退出/2扫荡
     * @param instance_result 副本结果 1成功/2失败/3超时/4放弃
     * @param instance_diff 副本难度
     */
    void biInstance(Player player, int instance_id, int instance_status, int instance_result, int instance_diff, boolean isExtra);
}
