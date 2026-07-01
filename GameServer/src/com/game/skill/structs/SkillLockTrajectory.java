package com.game.skill.structs;

import com.game.skill.config.event.PlayLockTrajectoryEvent;
import game.core.map.Position;

/**
 * 技能弹道处理
 * Created by soko(xysoko@qq.com) on 2018/9/12. copyright 巨匠@雨墨
 */
public class SkillLockTrajectory {

    private PlayLockTrajectoryEvent event;
    private long startTime;

    //当前坐标
    private Position curPos;

    //初始化坐标
    private Position initPos;

    //目标ID值
    private long targetId;

    //地图ID值
    private long mapId;

    //技能ID
    private int skillId;

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public PlayLockTrajectoryEvent getEvent() {
        return event;
    }

    public void setEvent(PlayLockTrajectoryEvent event) {
        this.event = event;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Position getCurPos() {
        return curPos;
    }

    public void setCurPos(Position curPos) {
        this.curPos = curPos;
    }

    public Position getInitPos() {
        return initPos;
    }

    public void setInitPos(Position initPos) {
        this.initPos = initPos;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }
}
