package com.backend.gm.send;

public enum GMSendErrorType {

    CONNECT(-9999, "Ошибка сокет-соединения"),
    SEND(-999, "Ошибка отправки через сокет"),
    RECEIVE(-99, "Ошибка приёма через сокет"),
    PARSE(-9, "Ошибка парсинга ответа");


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
