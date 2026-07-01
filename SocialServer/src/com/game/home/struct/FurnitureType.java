package com.game.home.struct;

/**
 * @Desc TODO 1，主题；2，窗户；3， 家具；4，装饰
 * @Date 2021/8/6 15:57
 * @Auth ZUncle
 */
public enum  FurnitureType {
    Style(1),
    Window(2),
    Furniture(3),
    Decorate(4),
    ;
    final int type;

    FurnitureType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
