package com.game.db.bean;

import com.game.couplefight.structs.*;
import game.core.util.JsonUtils;

/**
 * 仙侣对决信息
 * @Auther: gouzhongliang
 * @Date: 2021/7/27 18:12
 */
public class CouplefightTeamBean {
    /**队伍id*/
    private long id;
    /**活动id*/
    private int activityId;
    /**所在的服务器组*/
    private int group;
    /**数据*/
    private String data;

    public CouplefightTeamBean(){}

    public CouplefightTeamBean(int activeId, int group, CoupleTeam team){
        this.id = team.getId();
        this.activityId = activeId;
        this.group = group;
        this.data = JsonUtils.toJSONString(team);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}
