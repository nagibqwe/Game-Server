package com.backend.bean;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("t_activity_lucky_value")
public class ActivityLuckyValue {
    @Id
    @Column
    @Comment("活动ID")
    private int id;

    @Column
    @Comment("总幸运值")
    private int totalLuckyValue;

    @Column
    @Comment("备注说明")
    private String tips;

    @Column
    @Comment("活动状态，0：未验证(测试、删除)，1：已验证(发布、删除)，2：已发布(删除)，     //已过期(删除)这个通过活动结束时间去判断")
    private int state;

    @Column
    @Comment("活动发布平台(groupName)(List JSON化后的字串[plat1,plat2,..])")
    @ColDefine(type=ColType.VARCHAR, width=300)
    private String platform = "";

    @Column
    @Comment("活动要发布到的区服列表(List JSON化后的字串[sid1,sid2,..])")
    @ColDefine(type=ColType.VARCHAR, width=500)
    private String toSidList = "";

    @Column
    @Comment("活动发布成功的区服列表(List JSON化后的字串[sid1,sid2,..])")
    @ColDefine(type=ColType.VARCHAR, width=500)
    private String okSidList = "";

    @Column
    @Comment("活动是否被删除，0：否，1：是")
    private byte isDeleted;

    @Column
    @Comment("活动是否被覆盖正在进行的活动，0：否，1：是")
    private int cover;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalLuckyValue() {
        return totalLuckyValue;
    }

    public void setTotalLuckyValue(int totalLuckyValue) {
        this.totalLuckyValue = totalLuckyValue;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getToSidList() {
        return toSidList;
    }

    public void setToSidList(String toSidList) {
        this.toSidList = toSidList;
    }

    public String getOkSidList() {
        return okSidList;
    }

    public void setOkSidList(String okSidList) {
        this.okSidList = okSidList;
    }

    public byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

}
