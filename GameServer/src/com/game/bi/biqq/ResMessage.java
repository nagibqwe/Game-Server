package com.game.bi.biqq;

/**
 * 返回的数据
 * @Auther: gouzhongliang
 * @Date: 2021/12/21 10:56
 */
public class ResMessage {

    private Title title;

    private ResData data;

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public ResData getData() {
        return data;
    }

    public void setData(ResData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResMessage{" +
                "title=" + title +
                ", data=" + data +
                '}';
    }
}
