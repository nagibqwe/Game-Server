package com.game.marriage.struct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.db.bean.MarryBean;
import game.core.util.JsonUtils;
import game.core.util.VersionUpdateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @Description
 * @auther admin
 * @create 2020-06-02 16:08
 */
public class Marriage {

    @JsonIgnore
    private transient long id;

    //情侣一
    @JsonIgnore
    private transient long marriageId;

    //情侣二
    @JsonIgnore
    private transient long beMarriageId;

    //结婚时间
    @JsonIgnore
    private transient long marriageTime;

    //婚宴使用次数
    private int num;

    //婚礼对数
    private int order;

    //申请离婚者
    private long divorceID;

    //已购买的婚宴
    private List<Integer> tList = new ArrayList<>();

    //购买次数
    private int purNum;

    //邀请列表
    private HashMap<Long, String> inviteList = new HashMap<>();

    //申请列表
    private HashMap<Long, String> applyList = new HashMap<>();

    //送祝福队列
    private HashSet<Long> blessList = new HashSet<>();


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMarriageId() {
        return marriageId;
    }

    public void setMarriageId(long marriageId) {
        this.marriageId = marriageId;
    }

    public long getBeMarriageId() {
        return beMarriageId;
    }

    public void setBeMarriageId(long beMarriageId) {
        this.beMarriageId = beMarriageId;
    }

    public long getMarriageTime() {
        return marriageTime;
    }

    public void setMarriageTime(long marriageTime) {
        this.marriageTime = marriageTime;
    }

    public List<Integer> gettList() {
        return tList;
    }

    public void settList(List<Integer> tList) {
        this.tList = tList;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }


    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public long getDivorceID() {
        return divorceID;
    }

    public void setDivorceID(long divorceID) {
        this.divorceID = divorceID;
    }

    public int getPurNum() {
        return purNum;
    }

    public void setPurNum(int purNum) {
        this.purNum = purNum;
    }

    public HashSet<Long> getBlessList() {
        return blessList;
    }

    public void setBlessList(HashSet<Long> blessList) {
        this.blessList = blessList;
    }

    public HashMap<Long, String> getInviteList() {
        return inviteList;
    }

    public void setInviteList(HashMap<Long, String> inviteList) {
        this.inviteList = inviteList;
    }

    public HashMap<Long, String> getApplyList() {
        return applyList;
    }

    public void setApplyList(HashMap<Long, String> applyList) {
        this.applyList = applyList;
    }

    public MarryBean toMarrayBean() {
        MarryBean bean = new MarryBean();
        bean.setMarriageId(id);
        bean.setaId(marriageId);
        bean.setbId(beMarriageId);
        bean.setTime(marriageTime);
        String str = VersionUpdateUtil.dataSave(JsonUtils.toJSONString(this), 512);
        bean.setData(str);
        bean.setWhere(id);
        return bean;
    }

}
