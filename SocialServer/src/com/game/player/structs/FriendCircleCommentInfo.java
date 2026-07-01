package com.game.player.structs;

/**
 * 朋友圈评论信息
 */
public class FriendCircleCommentInfo {
    private String chatername;      // 留言人名称
    private String condition;      // 留言内容

    public String getChatername() {
        return chatername;
    }

    public void setChatername(String chatername) {
        this.chatername = chatername;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
