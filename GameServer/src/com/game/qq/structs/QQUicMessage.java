package com.game.qq.structs;

import java.util.ArrayList;

/**
 * @Desc TODO
 * @Date 2022/1/5 14:37
 * @Auth ZUncle
 */
public class QQUicMessage {
    /**
     * 返回码：0正确返回，其他情况为异常
     * 1000020：请求包body错误，1000020：请求包body错误，检查text_list_ 的json数组，text_ 的string类型, text_scene_ 的int类型
     * 1000104：解包失败
     * 1000106：参数错误
     * 1000109：系统错误
     * 2000020：url中缺少appid
     * 2000024：sig 错误或者传了空 body
     */
    int ret;
    /**
     * 如果错误则返回错误信息，正确返回空
     */
    String msg;
    /**
     * 返回的文本条数
     */
    int text_result_cnt_;
    /**
     * 返回的文本数组
     */
    ArrayList<QQUicBack> text_result_list_ = new ArrayList<>();

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public int getText_result_cnt_() {
        return text_result_cnt_;
    }

    public void setText_result_cnt_(int text_result_cnt_) {
        this.text_result_cnt_ = text_result_cnt_;
    }

    public ArrayList<QQUicBack> getText_result_list_() {
        return text_result_list_;
    }

    public void setText_result_list_(ArrayList<QQUicBack> text_result_list_) {
        this.text_result_list_ = text_result_list_;
    }
}
