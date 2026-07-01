package com.game.couplefight.structs;

import com.game.fightroom.structs.FightRoom;

/**
 * 小组赛房间
 * @Auther: gouzhongliang
 * @Date: 2021/8/6 11:00
 */
public class GroupsRoom extends CoupleFightRoom{

    public GroupsRoom(FightRoom room, CoupleTeam t1, CoupleTeam t2) {
        super(room, t1, t2);
    }
}
