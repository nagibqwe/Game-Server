package com.game.chat.structs;

import java.util.HashSet;
import java.util.Set;

/**
 * 语言聊天的数据组合
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class MediaChatData {

    private int type;
    private long chatId;
    private String roomId;//房间ID值
    private final Set<Long> roleIds = new HashSet<>();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

}
