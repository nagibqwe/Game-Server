package com.game.boss.script;

import com.data.bean.Cfg_Bossnew_world_Bean;
import com.game.player.structs.Player;

import java.util.List;

/**
 * @Desc TODO boss 卷轴
 * @Date 2020/8/26 21:52
 * @Auth ZUncle
 */
public interface IBossCoin {

    /**
     * 是否能使用boss刷新卷
     *
     * @param beans     刷新boss时用于传递boss的配置表列表，判断传null即可
     * @param all       是否刷新本层
     * @param notify    不能刷新是否给出提示
     */
    boolean canResetBossData(Player player, List<Cfg_Bossnew_world_Bean> beans, boolean all, boolean notify);

    /**
     * 刷新boss
     *
     * @param type  boss类型
     * @param all   是否刷新本层
     */
    void resetBossData(Player player, int type, boolean all);

    /**
     * 是否能召唤Boss，返回boss配置表id或0
     *
     * @param type 类型
     */
    int canCallBoss(Player player, int type);

    /**
     * 召唤Boss
     *
     * @param type 类型
     */
    void callBoss(Player player, int type);

}
