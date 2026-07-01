package com.game.qq.structs;

/**
 * @Desc TODO
 * @Date 2022/1/5 14:40
 * @Auth ZUncle
 */
public class QQUicBack {
    int check_ret_;     //0=合法 1=恶意
    int punish_type_;   //1=完全恶意 2=部分恶意
    String result_text_;  //敏感词检测后的文本，敏感词被替换成**

    public int getCheck_ret_() {
        return check_ret_;
    }

    public void setCheck_ret_(int check_ret_) {
        this.check_ret_ = check_ret_;
    }

    public int getPunish_type_() {
        return punish_type_;
    }

    public void setPunish_type_(int punish_type_) {
        this.punish_type_ = punish_type_;
    }

    public String getResult_text_() {
        return result_text_;
    }

    public void setResult_text_(String result_text_) {
        this.result_text_ = result_text_;
    }
}
