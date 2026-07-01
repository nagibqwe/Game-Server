package com.game.vip.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 * VIP权限脚本
 */
public interface IVipPowerScript extends IScript {

    /**
     * 检查VIP权限是否生效
     * @param player
     * @param powerType
     * @return
     */
    boolean canFree(Player player, int powerType);

    /**
     * 获取VIP权限对应的加成值
     * @param player
     * @param powerType
     * @return
     */
    int getVipPowerValue(Player player, int powerType);

    /**
     * 获取ViP权限对应可购买次数的价格
     * @param purNum
     * @param powerType
     * @return
     */
    int getPriceByPurNum(int purNum, int powerType);

    /**
     * 获取购买次数
     * @param player
     * @param type
     * @return
     */
    int getVipPurNum(Player player, int type);

    /**
     * 获取免费增加数量
     * @param player
     * @param type
     * @return
     */
    int getVipFreeNum(Player player, int type);

    /**
     * 获取折扣
     * @param player
     * @param type
     * @return
     */
    int getVipDiscount(Player player, int type);

    /**
     * 获取vip购买价格
     * @param purNum
     * @param type
     * @return
     */
    int getVipAddNumPrice(int purNum, int type);

    int getDefaultDiscount();

    /**
     * 是否可以免费传送
     * @param player
     * @return
     */
    boolean isCanFreeFly(Player player);

    /**
     * 是否可以免费存储
     * @param player
     * @return
     */
    boolean isCanFreeStore(Player player);

    /**
     * 是否可以免费熔炼
     * @param player
     * @return
     */
    boolean isCanFreeRecycle(Player player);

    /**
     * 是否可以快速战斗
     * @param player
     * @return
     */
    boolean isCanFreeFastBattle(Player player);

    /**
     * 是否可以法宝任务扫荡
     * @param player
     * @return
     */
    boolean isCanFreeFabaoSweep(Player player);

    /**
     * 是否可以神兵任务扫荡
     * @param player
     * @return
     */
    boolean isCanFreeMagicalSweep(Player player);

    /**
     * 是否开启扫荡大能遗府
     * @param player
     * @return
     */
    boolean isOpenStarCopySweep(Player player);

    /**
     * 是否可以开启锁灵台
     * @param player
     * @return
     */
    boolean isCanFreeSuoLinTai(Player player);

    /**
     * 是否可以开启心魔
     * @param player
     * @return
     */
    boolean isCanFreeXinMo(Player player);

    /**
     * 是否可以开启凌云妖塔
     * @param player
     * @return
     */
    boolean isCanFreeLinYun(Player player);

}
