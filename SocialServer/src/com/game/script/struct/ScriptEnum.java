package com.game.script.struct;

/**
 * @Desc TODO
 * @Date 2021/6/8 17:53
 * @Auth ZUncle
 */
public class ScriptEnum {

    public final static int ServerScript = 1;           //服务器脚本

    public final static int BackGrandScript = 2;        //后台消息处理脚本

    public final static int GmScript = 4;               //命令行处理脚本
    /**
     * 好友脚本
     */
    public final static int FriendBaseScript = 5;
    /**
     * 玩家脚本脚本
     */
    public final static int GlobalPlayerScript = 6;
    /**
     * 仙府（家园）
     */
    public final static int HomeScript = 7;
    /**
     * 社区信息
     */
    public final static int CommunityScript = 8;
    /**
     * 邮件脚本
     */
    public final static int MailScript = 9;
}
