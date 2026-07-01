package com.game.friend.struct;

/**
 * @Desc TODO
 * @Date 2020/8/25 15:18
 * @Auth ZUncle
 */
public enum Relation {

    RelationType_SELF(-1), //自己
    RelationType_Normal(0), //常规
    RelationType_Friend(1), //好友
    RelationType_Enemy(2), //仇人
    RelationType_Shield(3), //屏蔽
    RelationType_RecommendFriend(4), //推荐好友
    RelationType_LaterPlayerList(5), //最近聊天列表
    ;
    int value;

    Relation(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
