package com.game.biqq;

import java.util.List;

/**
 * 请求数据
 * @Auther: gouzhongliang
 * @Date: 2021/12/17 14:58
 */
public class ReqMessage {

    private Title title;

    private List<ReqData> data;

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public List<ReqData> getData() {
        return data;
    }

    public void setData(List<ReqData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ReqMessage{" +
                "title=" + title +
                ", data=" + data +
                '}';
    }
}
