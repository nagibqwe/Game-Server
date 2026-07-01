package com.game.marriage.struct;

/**
 * Created by CXL on 2020/7/23.
 * 婚礼赠送数据
 */
public class WeddingSendData {


    private String sendName;//赠送者名字

    private String beSendName;//被赠送名字

    private int   itemID;//ID

    private int   num;//数量

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getBeSendName() {
        return beSendName;
    }

    public void setBeSendName(String beSendName) {
        this.beSendName = beSendName;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
