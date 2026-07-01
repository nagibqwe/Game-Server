package com.data.struct;

/**
 * @Desc TODO
 * 1=拜访
 * 2=送礼
 * 3=购买商城家具
 * 4=人气
 * 5=装饰度
 * 6=收集套装
 * 7=收集家具
 * 8=聚宝盆
 * @Date 2021/9/16 18:09
 * @Auth ZUncle
 */
public enum HomeTaskType {
    Visitor(1),
    Gift(2),
    BuyFurniture(3),
    Popularity(4),
    Decorate(5),
    CollectionSuit(6),
    CollectionType(7),
    Tup(8),
    ;
    final int value;

    HomeTaskType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
