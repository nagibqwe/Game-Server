package com.game.qq.structs;

/**
 * @Desc TODO
 * 1:名称
 * 2:消息
 * 3:标题
 * 4:评论
 * 5:签名
 * 6:搜索
 * 7:其他 详情参考附录内容
 * @Date 2022/1/4 18:14
 * @Auth ZUncle
 */
public enum QQUic {
    Name(1),
    Message(2),
    Title(3),
    Ap(4),
    Sign(5),
    Search(6),
    Other(7),
    ;
    final int type;

    QQUic(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
