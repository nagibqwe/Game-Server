package com.game.bi.biqq;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/12/17 20:37
 */
public class ReqData {

    private String log_name = "log_common";

    private String log_fields;

    public ReqData(){}

    public ReqData(ReqDataDetail dataDetail){
        this.log_fields = dataDetail.toString();
    }

    public String getLog_name() {
        return log_name;
    }

    public void setLog_name(String log_name) {
        this.log_name = log_name;
    }

    public String getLog_fields() {
        return log_fields;
    }

    public void setLog_fields(String log_fields) {
        this.log_fields = log_fields;
    }
}
