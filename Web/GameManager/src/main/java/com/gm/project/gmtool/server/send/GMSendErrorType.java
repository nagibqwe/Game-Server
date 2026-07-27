package com.gm.project.gmtool.server.send;

public enum GMSendErrorType {

    CONNECT(-9999, "socket连接错误"),
    SEND(-999, "socket发送错误"),
    RECEIVE(-99, "socket接收错误"),
    PARSE(-9, "解析返回值错误");


    private int code;
    private String msg;

    GMSendErrorType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "GMSendErrorType{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
