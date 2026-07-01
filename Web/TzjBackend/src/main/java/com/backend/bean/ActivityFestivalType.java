package com.backend.bean;

import org.nutz.dao.entity.annotation.*;

@Table("t_activity_festival_type")
public class ActivityFestivalType {

    @Column
    @Comment("活动BOSS分类配置ID")
    private int id;

    @Column
    @Comment("后台显示的BOSS类型")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
