package com.game.guild.script;

import com.game.guild.structs.Guild;
import com.game.guild.structs.GuildMember;
import com.game.player.structs.Player;

import java.util.List;

public interface IGuildManagerScript {

    /**
     * tick
     */
    void tick();

    /**
     * 请求公会列表
     * @param player
     */
    void reqGuildList(Player player);

    /**
     * 请求公会信息
     * @param player
     */
    void reqGuildInfo(Player player);

    /**
     * 创建公会
     * @param player
     * @param name
     * @param icon
     * @param notice
     * @return
     */
    void createGuild(Player player, String name, int icon, String notice);

    /**
     * 请求申请加入公会
     * @param player
     * @param ids
     */
    void reqApplyGuild(Player player, List<Long> ids);

    /**
     * 公会申请处理
     * @param player
     * @param aId
     * @param flag
     * @return
     */
    void reqApplyDelGuild(Player player, List<Long> aId, boolean flag);

    /**
     * 公会改名
     * @param player
     * @param name
     * @return
     */
    void reqChangeGuildName(Player player, String name);

    /**
     * 更改官职
     * @param player
     * @param roleId
     * @param rank
     */
    void reqChangeGuildRank(Player player, long roleId, int rank);

    /**
     * 公会信息修改
     * @param player
     * @param isAutoApply
     * @param limitLv
     * @param limitFightPoint
     * @param icon
     * @param notice
     */
    void reqChangeGuildSetting(Player player, boolean isAutoApply, int limitLv, long limitFightPoint, int icon, String notice);

    /**
     * 请求建筑升级
     * @param player
     * @param type
     */
    void reqGuildConstructUp(Player player, int type);

    /**
     * 请求公会日志
     * @param player
     */
    void reqGuildLogList(Player player);

    /**
     * 离开或者踢出公会
     * @param player
     * @param roleId
     */
    void leaveGuild(Player player, long roleId);

    /**
     * 弹劾会长
     * @param player
     * @return
     */
    void reqImpeachMaster(Player player);

    /**
     * 领取公会工资
     * @param player
     */
    void reqGetItem(Player player);

    /**
     * 玩家上线处理
     */
    void playerOnLine(Player p);

    /**
     * 保存所有公会
     */
    void saveAllGuild();

    /**
     * 改变公会资金
     * @param guild
     * @param exp
     * @param res
     * @param actionId
     */
    void changeGuildExp(Guild guild, long exp, int res, long actionId);

    /**
     * 是否是代理盟主
     * @param guildMember
     * @param guild
     * @return
     */
    boolean isProxyChairMan(GuildMember guildMember, Guild guild);

    /**
     * 一键招人
     * @param player
     */
    void oneKeyJoinPlayer(Player player);

    /**
     * 请求进入仙盟驻地
     * @param player
     */
    void reqEnterGuildBase(Player player);

    /**
     * 打开仙盟宝箱
     * @param player
     * @param id
     */
    void reqGuildGiftOpen(Player player, long id);

    /**
     * 更新仙盟宝匣进度
     * @param player
     * @param type
     * @param changeNum
     */
    void incrGiftProgress(Player player, int type, int changeNum);
}
