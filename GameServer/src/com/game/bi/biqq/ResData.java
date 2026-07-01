package com.game.bi.biqq;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/12/21 11:04
 */
public class ResData {

    private int code;

    private String msg;

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
        return "ResData{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
