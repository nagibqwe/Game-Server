package com.game.player.structs;

import java.util.ArrayList;
import java.util.List;

public class FriendCircleInfo {
    /**
     * 朋友圈唯一id
     */
    private long friendCircleId;
    /**
     * 内容
     */
    private String condition;
    /**
     * 发布时间
     */
    private long time;
    /**
     * 朋友圈评论信息
     */
    private List<FriendCircleCommentInfo> friendCircleCommentInfoList = new ArrayList<>();

    public FriendCircleInfo() {
    }

    public long getFriendCircleId() {
        return friendCircleId;
    }

    public void setFriendCircleId(long friendCircleId) {
        this.friendCircleId = friendCircleId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<FriendCircleCommentInfo> getFriendCircleCommentInfoList() {
        return friendCircleCommentInfoList;
    }

    public void setFriendCircleCommentInfoList(List<FriendCircleCommentInfo> friendCircleCommentInfoList) {
        this.friendCircleCommentInfoList = friendCircleCommentInfoList;
    }
}
