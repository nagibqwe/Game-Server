package com.game.copymap.structs;

/**
 * @Desc TODO
 * @Date 2021/8/5 14:54
 * @Auth ZUncle
 */
public class HouseCopyData extends ZoneCache {

    long roomId;        //跨服房间ID

    long roleId;        //家园家主ID

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }
}
