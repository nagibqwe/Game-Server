package com.game.biqq;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/12/17 14:31
 */
public class Title {
    /**业务id,这里是腾讯这边的业务id，如果通用想打印自己的id，可以放在key_x*/
    private String app_id;
    /**业务名称,这里是英文名称*/
    private String app_name;
    /**unix时间戳，从1970年1月1日（UTC/GMT的午夜）开始所经过的秒数*/
    private Integer timestamp;
    /**序列号，唯一标识一条消息请求，客户端自己保证id唯一，重传需要参照这个id是否回包，保证消息不冗余发送。服务器原样返回*/
    private String seq_id;
    /**重试次数，由客户端计数并决定何时中止重试，服务器原样返回*/
    private Integer retry_times;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getTimestamp() {
        return timestamp.toString();
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int timestamp(){
        return timestamp;
    }

    public int retryTimes(){
        return retry_times;
    }

    public String getSeq_id() {
        return seq_id;
    }

    public void setSeq_id(String seq_id) {
        this.seq_id = seq_id;
    }

    public String getRetry_times() {
        return retry_times.toString();
    }

    public void setRetry_times(int retry_times) {
        this.retry_times = retry_times;
    }

    @Override
    public String toString() {
        return "Title{" +
                "app_id='" + app_id + '\'' +
                ", app_name='" + app_name + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", seq_id='" + seq_id + '\'' +
                ", retry_times='" + retry_times + '\'' +
                '}';
    }
}
