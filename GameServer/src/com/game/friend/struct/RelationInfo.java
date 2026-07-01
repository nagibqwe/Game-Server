package com.game.friend.struct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.game.player.structs.PlayerWorldInfo;

/**
 * 关系信息:好友、仇人、屏蔽（屏蔽关系与好友、仇人可共存）
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public abstract class RelationInfo {

    /**
     * 角色id
     */
    private long roleId;

    @JsonIgnore
    private transient PlayerWorldInfo info;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public PlayerWorldInfo getInfo() {
        return info;
    }

    public void setInfo(PlayerWorldInfo info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "RelationInfo{" +
                "roleId=" + roleId +
                ", info=" + info +
                '}';
    }
}
