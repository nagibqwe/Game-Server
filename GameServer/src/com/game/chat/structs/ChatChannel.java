package com.game.chat.structs;

/**
 * @Desc TODO
 * @Date 2022/1/5 15:15
 * @Auth ZUncle
 */
public enum ChatChannel {
    NONE(-1),                         //无
    CHATCHANNEL_WORLD(0),             //世界
    CHATCHANNEL_GUILD(1),             //帮会
    CHATCHANNEL_TEAM(2),             //队伍
    CHATCHANNEL_ROLE(3),              //私聊
    CHATCHANNEL_SYSTEM(4),            //系统
    CHATCHANNEL_CURMAP(8),            //当前地图聊天
    CHATCHANNEL_TEAMHELP(9),         //组队帮助频道，主要用于通知的类型
    CHATCHANNEL_CROSS(13),           //跨服聊天
    CHATCHANNEL_CHUANWEN(14),          //系统 传闻 频道

    ;
    final int channel;

    ChatChannel(int channel) {
        this.channel = channel;
    }

    public int getChannel() {
        return channel;
    }

    public static ChatChannel find(int c) {
        for (ChatChannel channel : ChatChannel.values()) {
            if (channel.getChannel() == c) {
                return channel;
            }
        }
        return NONE;
    }

    @Override
    public String toString() {
        return "ChatChannel{" +
                "channel=" + channel +
                '}';
    }
}
