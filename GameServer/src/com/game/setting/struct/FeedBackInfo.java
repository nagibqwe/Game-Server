package com.game.setting.struct;

public class FeedBackInfo implements Comparable<FeedBackInfo>{

    private int time;

    private int type;

    private String content;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "FeedBackInfo{" +
                "time=" + time +
                ", type=" + type +
                ", content=" + content +
                '}';
    }

    @Override
    public int compareTo(FeedBackInfo o) {
        return this.time - o.getTime();
    }
}
