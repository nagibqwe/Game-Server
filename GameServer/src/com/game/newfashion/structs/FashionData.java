package com.game.newfashion.structs;

/**
 * Created by cxl on 2020/8/25.
 */
public class FashionData {



    private int fashionID;//时装ID

    private int star;//星级

    private int type ;//类型1，衣服；2武器；3，背饰；4坐骑；5，宠物；6法宝,7魂甲,

    public int getFashionID() {
        return fashionID;
    }

    public void setFashionID(int fashionID) {
        this.fashionID = fashionID;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
