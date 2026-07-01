package com.backend.struct.log.entity.player;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;

import java.util.Map;

@Table(name = "changerolenamelog")
public class ChangeRoleNameLog implements IConvertor {

    @FieldDesc
    private long playerId;                  //玩家Id

    @FieldDesc
    private long userId;                    //账号Id

    @FieldDesc
    private int sid;                        //区服

    @FieldDesc
    private String oldName;                 //改名前的角色名

    @FieldDesc
    private String newName;                 //改名后的角色名

    @FieldDesc
    private int modelId;                    //道具ID

    @FieldDesc
    private String platformName;            //平台名字

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}
