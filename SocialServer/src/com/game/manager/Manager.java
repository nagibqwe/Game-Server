package com.game.manager;

import com.game.community.manager.CommunityManager;
import com.game.count.manager.CountManager;
import com.game.friend.manager.FriendManager;
import com.game.gm.manager.GmManager;
import com.game.home.manager.HomeManager;
import com.game.mail.manager.MailManager;
import com.game.player.manager.GlobalPlayerManager;
import com.game.server.manager.ServerManager;
import game.core.script.ScriptManager;

/**
 * @Desc TODO
 * @Date 2021/6/8 18:22
 * @Auth ZUncle
 */
public class Manager {

    //脚本管理
    public static ScriptManager scriptManager = ScriptManager.getInstance();
    //服务器管理
    public static ServerManager serverManager = ServerManager.getInstance();
    //命令行
    public static GmManager gmManager = GmManager.getInstance();
    /**
     * 好友管理类
     */
    public static FriendManager friendManager = FriendManager.getInstance();
    /**
     * 全局玩家管理器
     */
    public static GlobalPlayerManager globalPlayerManager = GlobalPlayerManager.getInstance();
    /**
     * 计数器
     */
    public static CountManager countManager = CountManager.getInstance();
    /**
     * 邮件
     */
    public static MailManager mailManager = MailManager.getInstance();
    /**
     * 仙府脚本
     */
    public static HomeManager homeManager = HomeManager.getInstance();
    /**
     * 社区信息
     */
    public static CommunityManager communityManager = CommunityManager.getInstance();
}
