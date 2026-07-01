package com.game.chat.structs;

/**
 * @author hewei@haowan123.com
 *
 * 多媒体数据，包含语音、图片等
 */
public class MultiMedia {

    private String condition;// 消息
    private int playTime;//播放时间或者文件长度
    private int time;// 发送时间

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

}
