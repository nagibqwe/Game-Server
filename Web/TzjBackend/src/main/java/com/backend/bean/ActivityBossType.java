package com.backend.bean;

import org.nutz.dao.entity.annotation.*;

@Table("t_activity_boss_type")
public class ActivityBossType {
    @Id
    @Column
    @Comment("活动BOSS分类配置ID")
    private int id;

    @Column
    @Comment("后台显示的BOSS类型")
    private String name;

    @Column
    @Comment("对应的BOSSID")
    @ColDefine(type= ColType.TEXT)
    private String boss_id;

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

    public String getBoss_id() {
        return boss_id;
    }

    public void setBoss_id(String boss_id) {
        this.boss_id = boss_id;
    }
}
