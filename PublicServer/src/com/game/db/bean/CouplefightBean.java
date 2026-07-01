package com.game.db.bean;

import com.game.couplefight.manager.CouplefightManager;
import com.game.couplefight.structs.CoupleData;
import game.core.util.JsonUtils;

import java.util.Date;

/**
 * 仙侣对决信息
 * @Auther: gouzhongliang
 * @Date: 2021/7/27 18:12
 */
public class CouplefightBean {
    /**活动id*/
    private int activityId;
    /**所在的服务器组*/
    private int group;
    /**数据*/
    private String data;
    /**创建时间*/
    private Date createDate;
    /**活动状态*/
    private int status;

    public CouplefightBean(){}

    public CouplefightBean(CoupleData data){
        this.activityId = data.getActivityId();
        this.group = data.getServerGroupId();
        this.data = JsonUtils.toJSONString(data);
        this.createDate = new Date();
        this.status = CouplefightManager.getInstance().getStatus();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
