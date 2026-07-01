package com.game.marriage.script;

import com.game.player.structs.Player;

/**
 * @Desc TODO
 * @Date 2020/8/25 11:57
 * @Auth ZUncle
 */
public interface IMarryWallScript {

    /**
     * 发展关系
     *
     * @param player
     * @param roleId
     */
    void reqMarryAddFriend(Player player, long roleId);


    /**
     * 发展关系 回复
     *
     * @param player
     * @param roleId
     * @param opt
     */
    void reqMarryAddFriendOpt(Player player, long roleId, int opt);

    /**
     * 请求相亲墙数据
     *
     * @param player
     */
    void reqMarryWallDeclaration(Player player);

    /**
     * 领取 缘定三生礼包
     *
     * @param player
     */
    void reqMarryWallReward(Player player);

    /**
     * 发布爱情宣言
     *
     * @param player
     * @param declarationId
     */
    void reqPushMarryDeclaration(Player player, int declarationId);

}
